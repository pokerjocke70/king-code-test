package com.sundqvist.king.http.handlers;

import com.sundqvist.king.http.Response;
import com.sundqvist.king.service.UserScoreService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

public class TopListHandlerTest {

    @Test
    public void supportsShouldBeTrueForCorrectPathAndMethod() {
        // given
        UserScoreService userScoreService = Mockito.mock(UserScoreService.class);

        // when
        boolean supports = new TopListHandler(userScoreService).supports("/highscorelist", "GET");

        // then
        Assertions.assertThat(supports).isTrue();
    }

    @Test
    public void invoke() {
        // given
        UserScoreService userScoreService = Mockito.mock(UserScoreService.class);

        // when
        Response response = new TopListHandler(userScoreService).invoke(null, "77", null);

        // then
        Assertions.assertThat(response).isEqualTo(new Response(200, ""));
    }
}