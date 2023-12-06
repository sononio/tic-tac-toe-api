package com.sononio.tictactoeapi.schedule;

import com.sononio.tictactoeapi.game.matchmaking.MatchmakingQueue;
import com.sononio.tictactoeapi.game.matchmaking.model.GameInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchmakingSchedule {
    private final MatchmakingQueue matchmakingQueue;

    @Scheduled(fixedDelay = 1000L)
    public void createGames() {
        log.debug("Start scheduled task: CREATE GAMES");
        Optional<GameInfo> createdGame;

        do {
            createdGame = matchmakingQueue.createGame();
        } while (createdGame.isPresent());
        log.debug("Completed scheduled task: CREATE GAMES");
    }
}
