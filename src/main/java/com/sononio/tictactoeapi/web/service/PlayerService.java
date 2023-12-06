package com.sononio.tictactoeapi.web.service;

import com.sononio.tictactoeapi.game.matchmaking.GameRegistry;
import com.sononio.tictactoeapi.game.matchmaking.MatchmakingQueue;
import com.sononio.tictactoeapi.game.matchmaking.model.PlayerStatus;
import com.sononio.tictactoeapi.web.exceprion.PlayerNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlayerService {
    private final MatchmakingQueue matchmakingQueue;
    private final GameRegistry gameRegistry;
    private final LongPollingService longPollingService;

    public PlayerStatus getPlayerStatus(UUID playerId) {
        if (matchmakingQueue.findPlayer(playerId).isPresent())
            return PlayerStatus.IN_QUEUE;
        if (gameRegistry.findPlayerInGame(playerId).isPresent())
            return PlayerStatus.IN_GAME;

        throw new PlayerNotFoundException();
    }

    public PlayerStatus getPlayerStatusWithPolling(UUID playerId, Optional<Long> timeout) {
        longPollingService.waitForPlayerInGame(playerId, timeout);

        if (matchmakingQueue.findPlayer(playerId).isPresent())
            return PlayerStatus.IN_QUEUE;
        if (gameRegistry.findPlayerInGame(playerId).isPresent())
            return PlayerStatus.IN_GAME;

        throw new PlayerNotFoundException();
    }
}
