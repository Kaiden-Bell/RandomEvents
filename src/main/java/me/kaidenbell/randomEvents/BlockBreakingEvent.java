package me.kaidenbell.randomEvents;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Handler;

public class BlockBreakingEvent implements BaseEvent, Listener {

    private JavaPlugin plugin;
    private ConfigurationManager config;
    private RewardManager rewardManager;
    private ScoreTracker scoreTracker;
    private boolean running;
    private Material targetBlock;
    private int durationInTicks;
    private int taskID;



    public BlockBreakingEvent(JavaPlugin plugin, ConfigurationManager config, RewardManager rewardManager) {
        this.plugin = plugin;
        this.config = config;
        this.rewardManager = rewardManager;
        this.scoreTracker = new ScoreTracker();

        this.targetBlock = config.getBlockTypeForEvent();
        this.durationInTicks = config.getEventDurationTicks();
    }


    @Override
    public void start() {
        running = true;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        // Schedule event end
        taskID = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this::end, durationInTicks);
    }

    @Override
    public void end() {
        if (!running) return;

        running = false;
        HandlerList.unregisterAll(this);
        plugin.getServer().getScheduler().cancelTask(taskID); // Just if the task doesnt end

        List<PlayerScore> topScores = scoreTracker.getTopScores(3);
        rewardManager.distributeRewards(topScores);
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!running) return;
        if (e.getBlock().getType() == targetBlock) {
            scoreTracker.incrementScore(e.getPlayer());
        }
    }
}
