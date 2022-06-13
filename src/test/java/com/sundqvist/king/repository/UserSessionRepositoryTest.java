package com.sundqvist.king.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class UserSessionRepositoryTest {

    @Test
    public void storingUserShouldReturnTheSameUser() {
        // given
        UserSessionRepository userSessionRepository = new UserSessionRepository();
        // when
        String key = userSessionRepository.create("user").key();
        // then
        Assertions.assertThat(userSessionRepository.get(key).orElseThrow().id()).isEqualTo("user");
    }

    @Test
    public void getInvalidUserShouldReturnEmpty() {
        // given
        UserSessionRepository userSessionRepository = new UserSessionRepository();
        // when
        userSessionRepository.create("user");
        // then
        Assertions.assertThat(userSessionRepository.get("my-key")).isEmpty();
    }


}