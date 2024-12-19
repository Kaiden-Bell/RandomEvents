package me.kaidenbell.randomEvents;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;
import java.util.Objects;

public class MobKillingEvent implements BaseEvent, Listener {
    private JavaPlugin plugin;
    private ConfigurationManager config;
    private RewardManager rewardManager;
    private ScoreTracker scoreTracker;
    private boolean running;
    private EntityType targetMobType;
    private int durationInTicks;
    private int taskID;

    private EventScoreboard scoreboard;
    private int updateTaskID;

    public MobKillingEvent(JavaPlugin plugin, ConfigurationManager config, RewardManager rewardManager) {
        this.plugin = plugin;
        this.config = config;
        this.rewardManager = rewardManager;
        this.scoreTracker = new ScoreTracker();

        this.targetMobType = config.getMobTypeForEvent();
        this.durationInTicks = config.getEventDurationTicks();
    }

    @Override
    public void start() {
        running = true;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        // Schedule event end
        taskID = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this::end, durationInTicks);

        this.scoreboard = new EventScoreboard("Mob Killing Event");
        this.scoreboard.showToALlPlayers();

        updateTaskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            List<PlayerScore> topScores = scoreTracker.getTopScores(3);
            scoreboard.setScores(topScores);
        }, 0L, 100L); // 0 delay, 100 ticks
    }

    @Override
    public void end() {
        if (!running) return;

        running = false;
        HandlerList.unregisterAll(this);
        plugin.getServer().getScheduler().cancelTask(taskID);
        plugin.getServer().getScheduler().cancelTask(updateTaskID);


        List<PlayerScore> topScores = scoreTracker.getTopScores(3);
        rewardManager.distributeRewards(topScores);

        scoreboard.removeForAllPlayers();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent d) {
        if (!running) return;

        if (d.getEntity().getType() == targetMobType && d.getEntity().getKiller() != null) {
            scoreTracker.incrementScore((Player) d.getEntity().getKiller());
        }
    }
}

