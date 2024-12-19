package me.kaidenbell.randomEvents;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

public class ConfigurationManager {
    private FileConfiguration config;


    public ConfigurationManager(FileConfiguration config) {
        this.config = config;
    }


    public Material getBlockTypeForEvent() {
        String blockName = config.getString("events.block_breaking.block_type","STONE");
        return Material.getMaterial(blockName.toUpperCase());
    }

    public int getEventDurationTicks() {
        return config.getInt("events.default_duration_seconds", 60) * 20;
    }

    public long getRandomEventIntervalTicks() {
        return config.getLong("events.random_interval_minutes", 30) * 1200;
    }

    public EntityType getMobTypeForEvent() {
        String mobName = config.getString("events.mob_challenge.mob_type", "ZOMBIE");
        return EntityType.valueOf(mobName.toUpperCase());
    }
}
