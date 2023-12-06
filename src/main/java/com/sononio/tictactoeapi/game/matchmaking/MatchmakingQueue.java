package com.sononio.tictactoeapi.game.matchmaking;

import com.sononio.tictactoeapi.game.core.GameSettings;
import com.sononio.tictactoeapi.game.matchmaking.model.GameInfo;
import com.sononio.tictactoeapi.game.matchmaking.model.PlayerInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class MatchmakingQueue {
    private final GameRegistry gameRegistry;
    private final LockRegistry lockRegistry;
    private final Map<UUID, PlayerInfo> playersInQueue = new LinkedHashMap<>();

    public synchronized Optional<PlayerInfo> findPlayer(UUID playerId) {
        return Optional.ofNullable(playersInQueue.get(playerId));
    }

    public synchronized PlayerInfo addPlayerToQueue(String name) {
        val playerInfo = new PlayerInfo(UUID.randomUUID(), name);
        playersInQueue.put(playerInfo.id(), playerInfo);
        log.info("Added player to searching queue: {}", playerInfo);
        return playerInfo;
    }

    public synchronized boolean removePlayerFromQueue(UUID playerId) {
        val removed = playersInQueue.remove(playerId) != null;

        if (removed) {
            log.info("Removed player from searching queue: {}", playerId);
        } else {
            log.info("Cannot remove player from searching queue, no such player: {}", playerId);
        }

        return removed;
    }

    public synchronized Optional<GameInfo> createGame() {
        log.debug("Found {} players in searching queue", playersInQueue.size());
        if (playersInQueue.size() < 2) {
            log.debug("Not enough players to create a game");
            return Optional.empty();
        }

        val playerInfos = playersInQueue.entrySet().stream()
                .limit(2)
                .map(Map.Entry::getValue)
                .toList();

        val gameInfo = gameRegistry.createGame(
                playerInfos.get(0),
                playerInfos.get(1),
                new GameSettings(3, 3, 3));

        removePlayerFromQueue(playerInfos.get(0).id());
        removePlayerFromQueue(playerInfos.get(1).id());
        lockRegistry.update(playerInfos.get(0).id());
        lockRegistry.update(playerInfos.get(1).id());

        log.info("Created game: {}", gameInfo);
        return Optional.of(gameInfo);
    }
}
