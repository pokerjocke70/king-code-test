package com.sundqvist.king.service;

import com.sundqvist.king.domain.UserScore;
import com.sundqvist.king.repository.HighScoreRepository;

import java.util.List;

/**
 * Service class for score related operations
 */
public class UserScoreService {

    private final HighScoreRepository highScoreRepository;

    public UserScoreService(HighScoreRepository highScoreRepository) {
        this.highScoreRepository = highScoreRepository;
    }

    public void addScoreToUser(String user, int level, int score){
        highScoreRepository.addScore(user, level, score);
    }

    public List<UserScore> getUserScores(int level){
        return highScoreRepository.getUserScores(level);
    }
}
