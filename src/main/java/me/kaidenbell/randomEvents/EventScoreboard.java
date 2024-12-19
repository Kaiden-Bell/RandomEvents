package me.kaidenbell.randomEvents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.List;

public class EventScoreboard {
    private Scoreboard scoreboard;
    private Objective objective;


    public EventScoreboard(String title) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        this.scoreboard = manager.getNewScoreboard();

        this.objective = scoreboard.registerNewObjective("EventTopScores", "dummy", ChatColor.BOLD + title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void setScores(List<PlayerScore> topScores) {
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        // Sort high to low
        for (int i = 0; i < topScores.size(); i++) {
            PlayerScore ps = topScores.get(i);
            Player p = Bukkit.getPlayer(ps.getPlayerUUID());
            if (p == null) continue;

            String name = p.getName();
            int score = ps.getScore();

            objective.getScore(ChatColor.GREEN + "" + (i+1) + ". " + name).setScore(score);
        }
    }

    public void showToALlPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
    }

    public void removeForAllPlayers() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard empty = manager.getNewScoreboard();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(empty);
        }
    }
}
