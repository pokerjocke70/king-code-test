package com.sundqvist.king.http.handlers;

import com.sundqvist.king.http.Handler;
import com.sundqvist.king.http.Response;
import com.sundqvist.king.service.UserScoreService;

/**
 * Http handler for adding score.
 */
public class AddScoreHandler implements Handler {

    private final UserScoreService userScoreService;

    public AddScoreHandler(UserScoreService userScoreService) {
        this.userScoreService = userScoreService;
    }

    @Override
    public boolean supports(String path, String requestMethod) {
        return path.contains("/score") && requestMethod.equalsIgnoreCase("POST");
    }

    @Override
    public Response invoke(String body, String pathParam, String user) {
        userScoreService.addScoreToUser(user, Integer.parseInt(pathParam), Integer.parseInt(body));
        return new Response(204, null);
    }
}
