package com.sononio.tictactoeapi.game.matchmaking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class LockRegistry {
//    private final ReentrantLock reentrantLock = new ReentrantLock();
//    private final Map<UUID, Condition> conditions = new ConcurrentHashMap<>();

    private final Map<UUID, Object> locks = new ConcurrentHashMap<>();

    public Object getLock(UUID uuid) {

        if (!locks.containsKey(uuid)) {
            locks.put(uuid, new Object());
        }

        return locks.get(uuid);
    }

    public void update(UUID uuid) {
        val lock = locks.get(uuid);

        if (lock == null) {
            return;
        }

        synchronized (lock) {
            lock.notifyAll();
        }
    }
}
