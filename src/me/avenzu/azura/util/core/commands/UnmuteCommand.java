package me.avenzu.azura.util.core.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnmuteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.isOp()) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§cUsage: /unmute <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return true;
        }

        UUID targetUUID = target.getUniqueId();

        if (!MuteCommand.mutedPlayers.containsKey(targetUUID)) {
            sender.sendMessage("§cPlayer " + target.getName() + " is not muted.");
            return true;
        }

        MuteCommand.mutedPlayers.remove(targetUUID);
        sender.sendMessage("§aPlayer " + target.getName() + " has been unmuted.");
        target.sendMessage("§aYou have been unmuted.");

        return true;
    }
}