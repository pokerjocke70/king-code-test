package com.sundqvist.king.repository;

import com.sundqvist.king.domain.UserSession;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.time.LocalDateTime;

public class UserSessionRepositoryTest {

    @Test
    public void storingUserWithInactivationDateInFutureShouldReturnTheSameUser() {
        // given
        UserSessionRepository userSessionRepository = new UserSessionRepository();
        UserSession user = new UserSession("user", LocalDateTime.now().plusMinutes(1L));
        // when
        userSessionRepository.store("my-key", user);
        // then
        Assertions.assertThat(userSessionRepository.get("my-key").orElseThrow()).isSameAs(user);
    }

    @Test
    public void storingUserWithInActivationDateInPastShouldReturnNull() {
        // given
        UserSessionRepository userSessionRepository = new UserSessionRepository();
        UserSession user = new UserSession("user", LocalDateTime.now().minusMinutes(1L));
        // when
        userSessionRepository.store("my-key", user);
        // then
        Assertions.assertThat(userSessionRepository.get("my-key")).isEmpty();
    }


}