package com.sononio.tictactoeapi.web.service;

import com.sononio.tictactoeapi.game.core.Game;
import com.sononio.tictactoeapi.game.matchmaking.LockRegistry;
import com.sononio.tictactoeapi.game.matchmaking.model.PlayerStatus;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Service
@Slf4j
public class LongPollingService {
    private final PlayerService playerService;
    private final GameService gameService;
    private final LockRegistry lockRegistry;

    public LongPollingService(
            @Lazy PlayerService playerService,
            @Lazy GameService gameService,
            LockRegistry lockRegistry) {
        this.playerService = playerService;
        this.gameService = gameService;
        this.lockRegistry = lockRegistry;
    }

    public void waitForPlayerInGame(UUID playerId, Optional<Long> timeoutOpt) {
        long timeout = timeoutOpt.orElse(Long.MAX_VALUE);
        LocalDateTime start = LocalDateTime.now();

        while (ChronoUnit.MILLIS.between(start, LocalDateTime.now()) <= timeout
                && playerService.getPlayerStatus(playerId) != PlayerStatus.IN_GAME) {
            waitForUpdate(playerId, timeout);
        }
    }

    public void waitForGameTurn(UUID gameId, Predicate<Game> endPolling, Optional<Long> timeoutOpt) {
        long timeout = timeoutOpt.orElse(Long.MAX_VALUE);
        LocalDateTime start = LocalDateTime.now();

        while (ChronoUnit.MILLIS.between(start, LocalDateTime.now()) <= timeout
                && !endPolling.test(gameService.get(gameId))) {
            waitForUpdate(gameId, timeout);
        }
    }

    private void waitForUpdate(UUID uuid, long timeout) {
        val lock = lockRegistry.getLock(uuid);
        synchronized (lock) {
            try {
                lock.wait(timeout);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
