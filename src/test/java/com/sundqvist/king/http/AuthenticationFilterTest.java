package com.sundqvist.king.http;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import com.sundqvist.king.domain.UserSession;
import com.sundqvist.king.repository.UserSessionRepository;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

public class AuthenticationFilterTest {

    @Test
    public void doFilterOnLoginShouldNotContinueChain() throws IOException {
        // given
        HttpExchange httpExchange = Mockito.mock(HttpExchange.class);
        UserSessionRepository userSessionRepository = Mockito.mock(UserSessionRepository.class);
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userSessionRepository);
        Filter.Chain chain = Mockito.mock(Filter.Chain.class);
        UserSession userSession = new UserSession("user", LocalDateTime.now(), "k");

        // when
        BDDMockito.when(userSessionRepository.get("user")).thenReturn(Optional.of(userSession));
        BDDMockito.when(userSessionRepository.create("user")).thenReturn(userSession);
        BDDMockito.when(httpExchange.getRequestURI()).thenReturn(URI.create("/user/login"));
        BDDMockito.when(httpExchange.getResponseBody()).thenReturn(new ByteArrayOutputStream());
        authenticationFilter.doFilter(httpExchange, chain);

        // then
        Mockito.verifyNoInteractions(chain);
        Mockito.verify(httpExchange, Mockito.times(1)).sendResponseHeaders(Mockito.eq(200), Mockito.eq(4L));

    }

    @Test
    public void doFilterOnLoggedInUserShouldContinueChain() throws IOException {
        // given
        HttpExchange httpExchange = Mockito.mock(HttpExchange.class);
        UserSessionRepository userSessionRepository = Mockito.mock(UserSessionRepository.class);
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userSessionRepository);
        Filter.Chain chain = Mockito.mock(Filter.Chain.class);

        // when
        BDDMockito.when(userSessionRepository.get("123")).thenReturn(Optional.of(new UserSession("user", LocalDateTime.now(), "k")));
        BDDMockito.when(httpExchange.getRequestURI()).thenReturn(URI.create("/77/score?sessionkey=123"));
        BDDMockito.when(httpExchange.getResponseCode()).thenReturn(-1);
        authenticationFilter.doFilter(httpExchange, chain);

        // then
        Mockito.verify(chain, Mockito.times(1)).doFilter(httpExchange);

    }

    @Test
    public void doFilterOnInvalidUserShouldNotContinueChain() throws IOException {
        // given
        HttpExchange httpExchange = Mockito.mock(HttpExchange.class);
        UserSessionRepository userSessionRepository = Mockito.mock(UserSessionRepository.class);
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userSessionRepository);
        Filter.Chain chain = Mockito.mock(Filter.Chain.class);

        // when
        BDDMockito.when(userSessionRepository.get("123")).thenReturn(Optional.empty());
        BDDMockito.when(httpExchange.getRequestURI()).thenReturn(URI.create("/77/score?sessionkey=123"));
        BDDMockito.when(httpExchange.getResponseBody()).thenReturn(new ByteArrayOutputStream());
        authenticationFilter.doFilter(httpExchange, chain);

        // then
        Mockito.verifyNoInteractions(chain);
        Mockito.verify(httpExchange, Mockito.times(1)).sendResponseHeaders(Mockito.eq(401), Mockito.eq(12L));
    }

    @Test
    public void doFilterOnOpenEndpointShouldContinueChain() throws IOException {
        // given
        HttpExchange httpExchange = Mockito.mock(HttpExchange.class);
        UserSessionRepository userSessionRepository = Mockito.mock(UserSessionRepository.class);
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userSessionRepository);
        Filter.Chain chain = Mockito.mock(Filter.Chain.class);

        // when

        BDDMockito.when(httpExchange.getRequestURI()).thenReturn(URI.create("/77/highscorelist"));
        authenticationFilter.doFilter(httpExchange, chain);

        // then
        Mockito.verify(chain, Mockito.times(1)).doFilter(httpExchange);

    }
}