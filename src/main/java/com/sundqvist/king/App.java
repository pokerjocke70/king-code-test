package com.sundqvist.king;

import com.sundqvist.king.http.handlers.AddScoreHandler;
import com.sundqvist.king.http.AuthenticationFilter;
import com.sundqvist.king.http.HttpServer;
import com.sundqvist.king.http.handlers.TopListHandler;
import com.sundqvist.king.repository.HighScoreRepository;
import com.sundqvist.king.repository.UserSessionRepository;
import com.sundqvist.king.service.UserScoreService;

import java.io.IOException;
import java.util.Set;

/**
 * King code test main application
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {

        HighScoreRepository highScoreRepository = new HighScoreRepository();
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(new UserSessionRepository());
        UserScoreService userScoreService = new UserScoreService(highScoreRepository);
        var handlers = Set.of(new TopListHandler(userScoreService), new AddScoreHandler(userScoreService));
        new HttpServer(authenticationFilter, handlers, 8081).run();
    }
}
