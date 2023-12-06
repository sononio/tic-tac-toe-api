package com.sononio.tictactoeapi.web.service;

import com.sononio.tictactoeapi.game.matchmaking.MatchmakingQueue;
import com.sononio.tictactoeapi.game.matchmaking.model.PlayerInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchmakingService {
    private final MatchmakingQueue matchmakingQueue;

    public PlayerInfo searchForGame(String name) {
        return matchmakingQueue.addPlayerToQueue(name);
    }

    public boolean leaveSearching(UUID playerId) {
        return matchmakingQueue.removePlayerFromQueue(playerId);
    }
}
