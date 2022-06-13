package com.sundqvist.king.http;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import com.sundqvist.king.domain.UserSession;
import com.sundqvist.king.repository.UserSessionRepository;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.sundqvist.king.http.util.HttpHelper.*;

public class AuthenticationFilter extends Filter {

    public static final String USER_SESSION_ID = "USER_SESSION_ID";

    private final UserSessionRepository userStore;

    private final Set<String> nonValidatingPaths = Set.of("highscorelist");

    public AuthenticationFilter(UserSessionRepository userStore) {
        this.userStore = userStore;
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        URI requestURI = exchange.getRequestURI();
        if (requestURI.getPath().endsWith("/login")) {
            storeSession(exchange, requestURI);
            return;
        } else if (!nonValidatingPaths.contains(getPathParameter(requestURI, 1))) {
            if (!validateSessionKey(exchange, requestURI)) {
                return;
            }
        }
        chain.doFilter(exchange);
    }

    private boolean validateSessionKey(HttpExchange exchange, URI requestURI) {
        Optional.ofNullable(getFirstQueryParameter(requestURI))
                .flatMap(userStore::get)
                .ifPresentOrElse(s -> exchange.setAttribute(USER_SESSION_ID, s.id()), () -> sendResponse(exchange, "Unauthorized", 401));

        return exchange.getResponseCode() == -1;
    }

    private void storeSession(HttpExchange exchange, URI requestURI) {
        String uuid = UUID.randomUUID().toString();
        userStore.store(uuid, new UserSession(getFirstPathParameter(requestURI), LocalDateTime.now().plusMinutes(10L)));
        sendResponse(exchange, uuid, 200);
    }


    @Override
    public String description() {
        return "Authentication filter";
    }
}
