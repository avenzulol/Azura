package me.avenzu.azura.util.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MuteCommand implements CommandExecutor {

    public static final Map<UUID, Instant> mutedPlayers = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.isOp()) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§cUsage: /mute <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return true;
        }

        UUID targetUUID = target.getUniqueId();

        if (sender.equals(target)) {
            sender.sendMessage("§cYou cannot mute yourself.");
            return true;
        }
        
        if (target.isOp()) {
            sender.sendMessage("§cYou cannot mute a staff member.");
            return true;
        }

        if (mutedPlayers.containsKey(targetUUID)) {
            sender.sendMessage("§cPlayer " + target.getName() + " has already been muted.");
        } else {
            Instant muteExpiry = Instant.now().plusMillis(5 * 60 * 1000);
            mutedPlayers.put(targetUUID, muteExpiry);
            sender.sendMessage("§cPlayer " + target.getName() + " has been muted for 5 minutes.");
            target.sendMessage("§cYou have been muted for 5 minutes!");
        }

        return true;
    }

    public static boolean isMuted(Player player) {
        Instant muteExpiry = mutedPlayers.get(player.getUniqueId());
        if (muteExpiry != null && muteExpiry.isAfter(Instant.now())) {
            return true;
        } else {
            mutedPlayers.remove(player.getUniqueId());
            return false;
        }
    }

    public static long getRemainingMuteTime(Player player) {
        Instant muteExpiry = mutedPlayers.get(player.getUniqueId());
        if (muteExpiry != null && muteExpiry.isAfter(Instant.now())) {
            return muteExpiry.getEpochSecond() - Instant.now().getEpochSecond();
        }
        return 0;
    }
}