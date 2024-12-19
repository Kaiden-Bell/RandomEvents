package me.kaidenbell.randomEvents;


import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class ScoreTracker {
    private Map<UUID, Integer> scores = new HashMap<>();

    public void incrementScore(Player player) {
        scores.put(player.getUniqueId(), scores.getOrDefault(player.getUniqueId(), 0) + 1);
    }

    public List<PlayerScore> getTopScores(int n) {
        return scores.entrySet().stream()
                .map(e -> new PlayerScore(e.getKey(), e.getValue()))
                .sorted((a, b) -> Integer.compare(b.getScore(), a.getScore()))
                .limit(n)
                .collect(Collectors.toList());
    }
}
