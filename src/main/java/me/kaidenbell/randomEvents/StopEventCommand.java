package me.kaidenbell.randomEvents;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StopEventCommand implements CommandExecutor {
    private final EventHandler eventHandler;

    public StopEventCommand(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (eventHandler.getCurrentEvent() == null || !eventHandler.getCurrentEvent().isRunning()) {
            sender.sendMessage("§cNo event is currently running.");
            return true;
        }
        eventHandler.stopActiveEvent();
        sender.sendMessage("§aThe current event has been stopped.");
        return true;
    }
}
