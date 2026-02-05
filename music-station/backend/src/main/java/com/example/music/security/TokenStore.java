package com.example.music.security;

import com.example.music.entity.Role;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class TokenStore {
    private final Map<String, SessionUser> sessions = new ConcurrentHashMap<>();

    public String createToken(String username, Role role) {
        String token = UUID.randomUUID().toString().replace("-", "");
        sessions.put(token, new SessionUser(username, role));
        return token;
    }

    public Optional<SessionUser> findByToken(String token) {
        return Optional.ofNullable(sessions.get(token));
    }

    public record SessionUser(String username, Role role) {
    }
}
