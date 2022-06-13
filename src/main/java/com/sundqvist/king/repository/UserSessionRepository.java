package com.sundqvist.king.repository;

import com.sundqvist.king.domain.UserSession;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserSessionRepository {

    private final System.Logger LOGGER = System.getLogger(UserSessionRepository.class.getName());

    private final ConcurrentHashMap<String, UserSession> concurrentHashMap = new ConcurrentHashMap<>();

    public UserSessionRepository() {
        // Schedule a cleanup task to run every minute
        new Timer(true).schedule(new TimerTask() {

            @Override
            public void run() {
                var now = LocalDateTime.now();
                concurrentHashMap.forEachKey(1, s -> {
                    if (concurrentHashMap.get(s).expiration().isBefore(now)) {
                        LOGGER.log(System.Logger.Level.INFO, "Removing expired key {0}", s);
                        concurrentHashMap.remove(s);
                    }
                });
            }
        }, 30_000L, 60_000L);
    }

    public UserSession create(String user){
        String key = UUID.randomUUID().toString();
        UserSession userSession = new UserSession(user, LocalDateTime.now().plusMinutes(10L), key);
        concurrentHashMap.put(key, userSession);
        return userSession;
    }

    public Optional<UserSession> get(String key) {
        return Optional.ofNullable(concurrentHashMap.get(key))
                .filter(UserSession::isValid);
    }
}
