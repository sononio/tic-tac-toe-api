package com.sononio.tictactoeapi.web.service;

import com.sononio.tictactoeapi.game.core.Game;
import com.sononio.tictactoeapi.game.core.GameState;
import com.sononio.tictactoeapi.game.core.Side;
import com.sononio.tictactoeapi.game.matchmaking.GameRegistry;
import com.sononio.tictactoeapi.game.matchmaking.LockRegistry;
import com.sononio.tictactoeapi.game.matchmaking.model.PlayerInGame;
import com.sononio.tictactoeapi.web.exceprion.GameNotStartedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameService {
    private final GameRegistry gameRegistry;
    private final LockRegistry lockRegistry;
    private final LongPollingService longPollingService;

    public Game get(UUID gameId) {
        return gameRegistry.findGame(gameId).orElseThrow(GameNotStartedException::new);
    }

    public Game getWithPolling(UUID gameId, int lastTurn, Optional<Long> timeout) {
        longPollingService.waitForGameTurn(gameId, (game) -> game.getCurrentTurn() > lastTurn, timeout);
        return gameRegistry.findGame(gameId).orElseThrow(GameNotStartedException::new);
    }

    public Game getByPlayer(UUID playerId) {
        val player = gameRegistry.findPlayerInGame(playerId).orElseThrow(GameNotStartedException::new);
        return player.game();
    }

    public Game getByPlayerWithPolling(UUID playerId, Optional<Long> timeout) {
        val player = gameRegistry.findPlayerInGame(playerId).orElseThrow(GameNotStartedException::new);
        val wantedStates = Set.of(
                GameState.FINISHED,
                player.side() == Side.X
                        ? GameState.X_MOVE
                        : GameState.O_MOVE);

        longPollingService.waitForGameTurn(
                player.game().getUuid(),
                game -> wantedStates.contains(game.getState()),
                timeout);

        return player.game();
    }

    public GameState makeMove(UUID playerId, int x, int y) {
        val player = gameRegistry.findPlayerInGame(playerId).orElseThrow(GameNotStartedException::new);
        val state = player.game().move(player.side(), x, y);
        lockRegistry.update(player.game().getUuid());
        return state;
    }

    public PlayerInGame getPlayerInGame(UUID playerId) {
        return gameRegistry.findPlayerInGame(playerId).orElseThrow(GameNotStartedException::new);
    }

    public List<UUID> list() {
        return gameRegistry.list();
    }
}
