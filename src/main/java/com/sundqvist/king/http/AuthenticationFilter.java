package com.sundqvist.king.http;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import com.sundqvist.king.domain.UserSession;
import com.sundqvist.king.http.util.HttpHelper;
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
        } else if (!nonValidatingPaths.contains(getPathParameter(requestURI, 1))) {
            validateSessionKey(exchange, chain, requestURI);
        } else {
            chain.doFilter(exchange);
        }

    }

    private void validateSessionKey(HttpExchange exchange, Chain chain, URI requestURI) throws IOException {
        String sessionKey = HttpHelper.getFirstQueryParameter(requestURI);
        if(sessionKey != null){
            Optional<UserSession> userSession = userStore.get(sessionKey);
            if (userSession.isPresent()) {
                exchange.setAttribute(USER_SESSION_ID, userSession.get().id());
                chain.doFilter(exchange);
            } else {
                // No valid session, abort
                sendResponse(exchange, "Unauthorized", 401);
            }
        } else {
            // No valid session, abort
            sendResponse(exchange, "Missing sessionkey parameter", 401);
        }
    }

    private void storeSession(HttpExchange exchange, URI requestURI) {
        String userId = getFirstPathParameter(requestURI);
        String uuid = UUID.randomUUID().toString();
        userStore.store(uuid, new UserSession(userId, LocalDateTime.now().plusMinutes(10L)));
        sendResponse(exchange, uuid, 200);
    }


    @Override
    public String description() {
        return "Authentication filter";
    }
}
