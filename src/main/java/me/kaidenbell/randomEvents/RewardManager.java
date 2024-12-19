package me.kaidenbell.randomEvents;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;

import java.util.List;

public class RewardManager {
    private JavaPlugin plugin;

    public RewardManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void distributeRewards(List<PlayerScore> topPlayers) {
        for (int i = 0; i < topPlayers.size(); i++) {
            Player player = Bukkit.getPlayer(topPlayers.get(i).getPlayerUUID());
            if (player == null) continue;

            switch (i) {
                case 0:
                    player.sendMessage("You got first place! Congratulations! Here's a few diamonds");
                    player.getInventory().addItem(new ItemStack(Material.DIAMOND, 5));
                    break;
                case 2:
                    player.sendMessage("You got second place! Congratulations! Here's a few emeralds!");
                    player.getInventory().addItem(new ItemStack(Material.EMERALD, 5));
                    break;
                case 3:
                    player.sendMessage("You got third! Congratulations! Here's a few iron ingots!");
                    player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 5));
                    break;
                default:
                    player.sendMessage("Thanks for playing! Here's a few ...");
                    player.getInventory().addItem(new ItemStack(Material.DIRT, 64));
            }
        }
    }
}
