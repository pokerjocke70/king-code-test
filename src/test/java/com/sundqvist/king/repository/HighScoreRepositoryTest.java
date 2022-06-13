package com.sundqvist.king.repository;

import com.sundqvist.king.domain.UserScore;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

public class HighScoreRepositoryTest {

    @Test
    public void addingScoresToRepositoryShouldReturnCorrectList() {
        // given
        HighScoreRepository highScoreRepository = new HighScoreRepository();
        populateRepository(highScoreRepository);
        // when
        List<UserScore> userScores = highScoreRepository.getUserScores(7);
        // then
        Assertions.assertThat(userScores).isEqualTo(List.of(new UserScore("1", 27), new UserScore("4", 25), new UserScore("2", 21), new UserScore("3", 19)));
    }

    @Test
    public void addingScoresToRepositoryShouldReturnMax15Elements() {
        // given
        HighScoreRepository highScoreRepository = new HighScoreRepository();
        populateRepository(highScoreRepository, 100, 100);
        // when
        List<UserScore> userScores = highScoreRepository.getUserScores(100);
        // then
        Assertions.assertThat(userScores).hasSize(15);
        Assertions.assertThat(userScores.stream().findFirst().orElseThrow().score()).isEqualTo(99);
    }

    private void populateRepository(HighScoreRepository highScoreRepository) {
        highScoreRepository.addScore("1", 7, 23);
        highScoreRepository.addScore("1", 7, 27);
        highScoreRepository.addScore("1", 8, 13);
        highScoreRepository.addScore("2", 7, 2);
        highScoreRepository.addScore("2", 7, 21);
        highScoreRepository.addScore("3", 7, 19);
        highScoreRepository.addScore("3", 7, 7);
        highScoreRepository.addScore("4", 7, 25);
        highScoreRepository.addScore("4", 4, 23);
        highScoreRepository.addScore("5", 2, 23);
        highScoreRepository.addScore("5", 1, 23);
        highScoreRepository.addScore("6", 8, 23);
        highScoreRepository.addScore("7", 3, 23);
        highScoreRepository.addScore("1", 3, 2);
    }

    private void populateRepository(HighScoreRepository highScoreRepository, int level, int count){
        IntStream.range(0, count).forEach(i -> highScoreRepository.addScore(String.valueOf(i), level, i));
    }
}