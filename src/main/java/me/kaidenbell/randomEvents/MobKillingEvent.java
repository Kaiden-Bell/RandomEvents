package me.kaidenbell.randomEvents;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;
import java.util.Arrays;
import java.util.Random;

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

        List<String> mobNames = config.getConfig().getStringList("events.mob_challenge.allowed_mobs");


        List<EntityType> allowedMobs = mobNames.stream()
                .map(EntityType::valueOf)
                .toList();

        Random rand = new Random();

        this.targetMobType = allowedMobs.get(rand.nextInt(allowedMobs.size()));
        this.durationInTicks = config.getEventDurationTicks();
    }

    @Override
    public void start() {
        running = true;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        plugin.getServer().broadcastMessage("§cMob Killing Event §astarted! §cKill as many " + "§6" + targetMobType.name().toLowerCase().replace("_", " ") + "s §aas you can!");

        // Schedule event end
        taskID = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this::end, durationInTicks);

        this.scoreboard = new EventScoreboard("Mob Killing Event");
        this.scoreboard.showToAllPlayers();

        updateTaskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            List<PlayerScore> topScores = scoreTracker.getTopScores(3);
            scoreboard.setScores(topScores);
        }, 0L, 100L); // 0 delay, 20 ticks / 1 second
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

