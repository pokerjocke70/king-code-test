package com.sundqvist.king.http.handlers;

import com.sundqvist.king.http.Handler;
import com.sundqvist.king.http.Response;
import com.sundqvist.king.service.UserScoreService;

import java.util.stream.Collectors;

/**
 * Http handler for toplists/highscore
 */
public class TopListHandler implements Handler {

    private final UserScoreService userScoreService;

    public TopListHandler(UserScoreService userScoreService) {
        this.userScoreService = userScoreService;
    }

    @Override
    public boolean supports(String path, String requestMethod) {
        return path.contains("/highscorelist");
    }

    @Override
    public Response invoke(String body, String pathParam, String user) {
        return new Response(200, userScoreService.getUserScores(Integer.parseInt(pathParam)).stream().map(u -> u.userId() + "=" + u.score()).collect(Collectors.joining(",")));
    }
}
