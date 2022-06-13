package com.sundqvist.king.http.handlers;

import com.sundqvist.king.http.Response;
import com.sundqvist.king.service.UserScoreService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

public class AddScoreHandlerTest {

    @Test
    public void supportsShouldBeTrueForCorrectPathAndMethod() {
        // given
        UserScoreService userScoreService = Mockito.mock(UserScoreService.class);

        // when
        boolean supports = new AddScoreHandler(userScoreService).supports("/score", "POST");

        // then
        Assertions.assertThat(supports).isTrue();
    }

    @Test
    public void invoke() {
        // given
        UserScoreService userScoreService = Mockito.mock(UserScoreService.class);

        // when
        Response response = new AddScoreHandler(userScoreService).invoke("99", "77", "user");

        // then
        Assertions.assertThat(response).isEqualTo(new Response(204, null));
    }


}