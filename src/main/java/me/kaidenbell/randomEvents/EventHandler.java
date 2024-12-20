package me.kaidenbell.randomEvents;

import org.bukkit.plugin.java.JavaPlugin;

public class EventHandler {
    private BaseEvent currentEvent;
    private ConfigurationManager config;
    private RewardManager rewardManager;
    private JavaPlugin plugin;

    public EventHandler(JavaPlugin plugin, ConfigurationManager config, RewardManager rewardManager) {
        this.plugin = plugin;
        this.config = config;
        this.rewardManager = rewardManager;
    }

    public void startEvent(BaseEvent event) {
        if (currentEvent != null && currentEvent.isRunning()) {
            currentEvent.end();
        }
        plugin.getServer().broadcastMessage("§aThe Event has started!");
        currentEvent = event;
        currentEvent.start();
    }

    public void stopActiveEvent() {
        if (currentEvent != null && currentEvent.isRunning()) {
            currentEvent.end();
            currentEvent = null;
        }
    }

    public void scheduleRandomEvent() {
        long interval = config.getRandomEventIntervalTicks();

        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (currentEvent != null || !currentEvent.isRunning()) {
                BaseEvent newEvent = Math.random() > 0.5 ?
                        new BlockBreakingEvent(plugin, config, rewardManager) :
                        new MobKillingEvent(plugin, config, rewardManager);
                long delayTicks = 60 * 20; // New Event starts in 60 seconds
                long warningTicks = delayTicks - (10 * 20);

                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    startEvent(newEvent);
                }, delayTicks);

                if (warningTicks > 0) {
                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                        plugin.getServer().broadcastMessage("§eAn event will start in 10 seconds!");
                    }, warningTicks);
                }
            }
        }, interval, interval);
    }


    public JavaPlugin getPlugin() {
        return plugin;
    }

    public ConfigurationManager getConfig() {
        return config;
    }

    public RewardManager getRewardManager() {
        return rewardManager;
    }

    public BaseEvent getCurrentEvent() {
        return currentEvent;
    }
}
