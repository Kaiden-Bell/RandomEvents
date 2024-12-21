package me.kaidenbell.randomEvents;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BlockBreakingEvent implements BaseEvent, Listener {

    private JavaPlugin plugin;
    private ConfigurationManager config;
    private RewardManager rewardManager;
    private ScoreTracker scoreTracker;
    private boolean running;
    private Material targetBlock;
    private int durationInTicks;
    private int taskID;

    private EventScoreboard scoreboard;
    private int updateTaskID;



    public BlockBreakingEvent(JavaPlugin plugin, ConfigurationManager config, RewardManager rewardManager) {
        this.plugin = plugin;
        this.config = config;
        this.rewardManager = rewardManager;
        this.scoreTracker = new ScoreTracker();


        List<String> blockNames = config.getConfig().getStringList("events.block_challenge.allowed_blocks");
        List<Material> allowedBlocks = blockNames.stream()
                .map(Material::valueOf)
                .toList();

        Random rand = new Random();

        this.targetBlock = allowedBlocks.get(rand.nextInt(allowedBlocks.size()));
        this.durationInTicks = config.getEventDurationTicks();
    }


    @Override
    public void start() {
        running = true;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        plugin.getServer().broadcastMessage("§aBlock Breaking Event started! Break as many §6" + targetBlock.name().toUpperCase().replace("_", " ") + " blocks as you can!");

        // Schedule event end
        taskID = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this::end, durationInTicks);

        this.scoreboard = new EventScoreboard("Block Breaking Event");
        this.scoreboard.showToAllPlayers();

        updateTaskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            List<PlayerScore> topScores = scoreTracker.getTopScores(3);
            scoreboard.setScores(topScores);
        }, 0L, 20L); // 0 delay, 20 ticks / 1 Second
    }

    @Override
    public void end() {
        if (!running) return;

        running = false;
        HandlerList.unregisterAll(this);
        plugin.getServer().getScheduler().cancelTask(taskID); // Just if the task doesnt end

        List<PlayerScore> topScores = scoreTracker.getTopScores(3);
        rewardManager.distributeRewards(topScores);

        scoreboard.removeForAllPlayers();
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
