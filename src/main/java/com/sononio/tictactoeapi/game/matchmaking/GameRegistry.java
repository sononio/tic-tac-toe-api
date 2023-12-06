package com.sononio.tictactoeapi.game.matchmaking;

import com.sononio.tictactoeapi.game.core.Game;
import com.sononio.tictactoeapi.game.core.GameSettings;
import com.sononio.tictactoeapi.game.core.Side;
import com.sononio.tictactoeapi.game.matchmaking.model.GameInfo;
import com.sononio.tictactoeapi.game.matchmaking.model.PlayerInGame;
import com.sononio.tictactoeapi.game.matchmaking.model.PlayerInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameRegistry {
    private final LinkedHashMap<UUID, Game> games = new LinkedHashMap<>();
    private final Map<UUID, PlayerInGame> playersInGame = new HashMap<>();

    public synchronized GameInfo createGame(PlayerInfo playerInfo1, PlayerInfo playerInfo2, GameSettings settings) {
        val game = new Game(settings);
        games.put(game.getUuid(), game);

        val player1 = new PlayerInGame(playerInfo1, game, Side.X);
        val player2 = new PlayerInGame(playerInfo2, game, Side.O);
        playersInGame.put(playerInfo1.id(), player1);
        playersInGame.put(playerInfo2.id(), player2);

        return new GameInfo(game.getUuid(), playerInfo1.id(), playerInfo2.id());
    }

    public synchronized Optional<Game> findGame(UUID gameId) {
        return Optional.ofNullable(games.get(gameId));
    }

    public synchronized boolean deleteGame(UUID gameId) {
        return games.remove(gameId) != null;
    }

    public synchronized Optional<PlayerInGame> findPlayerInGame(UUID playerId) {
        return Optional.ofNullable(playersInGame.get(playerId));
    }

    public synchronized List<UUID> list() {
        val gameIds = new ArrayList<>(games.keySet());
        Collections.reverse(gameIds);
        return gameIds;
    }
}
