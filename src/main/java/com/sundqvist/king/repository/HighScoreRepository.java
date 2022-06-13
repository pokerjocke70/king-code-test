package com.sundqvist.king.repository;

import com.sundqvist.king.domain.UserScore;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HighScoreRepository {

    private static final System.Logger LOGGER = System.getLogger(HighScoreRepository.class.getName());

    private final ConcurrentHashMap<String, Map<Integer, List<Integer>>> concurrentHashMap = new ConcurrentHashMap<>();

    public void addScore(String user, int level, int score) {
        LOGGER.log(System.Logger.Level.INFO, "Adding score {0} to level {1} for user {2}", score, level, user);
        concurrentHashMap.compute(user, (s, map) -> map == null ? initMap(level, score) : addToMap(map, level, score));
    }

    /**
     * Returns a list of user scores, max 15 scores per level
     * @param level the level to fetch scores for
     * @return never null
     */
    public List<UserScore> getUserScores(int level) {
        return concurrentHashMap.entrySet()
                .stream()
                .filter(e -> e.getValue().containsKey(level))
                .map(e -> new UserScore(e.getKey(), e.getValue().get(level).stream().max(Integer::compareTo).orElseThrow()))
                .sorted(Comparator.comparingInt(UserScore::score).reversed())
                .limit(15)
                .toList();
    }

    /**
     * Adds a score to a level, will insert new list if no level was found
     *
     * @param map the map to update
     * @param level level to set
     * @param score score
     * @return the same map
     */
    private Map<Integer, List<Integer>> addToMap(Map<Integer, List<Integer>> map, int level, int score) {
        if (map.containsKey(level)) {
            map.get(level).add(score);
        } else {
            map.put(level, initScores(score));
        }
        return map;
    }

    /**
     * Init the level list
     *
     * @param level level
     * @param score score
     * @return A new Map with the level and a single score in the list
     */
    private Map<Integer, List<Integer>> initMap(int level, int score) {
        LOGGER.log(System.Logger.Level.INFO, "Init level {0} with score {1}", level, score);
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        map.put(level, initScores(score));
        return map;
    }

    /**
     * Init the scores list with new value
     *
     * @param score initial score
     * @return List of 1 element
     */
    private List<Integer> initScores(int score) {
        LinkedList<Integer> scores = new LinkedList<>();
        scores.add(score);
        return scores;
    }
}
