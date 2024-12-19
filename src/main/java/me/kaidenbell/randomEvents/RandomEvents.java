package me.kaidenbell.randomEvents;

import org.bukkit.plugin.java.JavaPlugin;

public final class RandomEvents extends JavaPlugin {

    private EventHandler eventHandler;
    private ConfigurationManager configManager;
    private RewardManager rewardManager;


    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        configManager = new ConfigurationManager(getConfig());
        rewardManager = new RewardManager(this);
        eventHandler = new EventHandler(this, configManager, rewardManager);

        eventHandler.scheduleRandomEvent();
        getCommand("startevent").setExecutor(new StartEventCommand(eventHandler));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        eventHandler.stopActiveEvent();
    }
}
