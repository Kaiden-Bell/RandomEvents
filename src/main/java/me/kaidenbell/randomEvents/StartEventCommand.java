package me.kaidenbell.randomEvents;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;



public class StartEventCommand implements CommandExecutor {
    private final EventHandler eventHandler;

    public StartEventCommand(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (eventHandler.getCurrentEvent() != null && eventHandler.getCurrentEvent().isRunning()) {
            sender.sendMessage("§cAn event is already running! Use /stopevent to stop it before starting a new one.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cPlease specify an event type: block or mob.");
            return true;
        }

        String eventType = args[0].toLowerCase();
        switch(eventType) {
            case "block":
                eventHandler.startEvent(new BlockBreakingEvent(eventHandler.getPlugin(), eventHandler.getConfig(), eventHandler.getRewardManager()));
                sender.sendMessage("§aBlock breaking event started!");
                break;

            case "mob":
                eventHandler.startEvent(new MobKillingEvent(eventHandler.getPlugin(), eventHandler.getConfig(), eventHandler.getRewardManager()));
                sender.sendMessage("§aMob killing event started!");
                break;
            default:
                sender.sendMessage("§cInvalid event type! Use /startevent [block|mob].");
        }
        return true;
    }
}
