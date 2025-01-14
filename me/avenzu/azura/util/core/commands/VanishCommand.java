package me.avenzu.azura.util.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.HashSet;
import java.util.Set;

public class VanishCommand implements CommandExecutor {
	
    private static final Set<Player> vanishedPlayers = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.isOp()) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        Player player = (Player) sender;
        
        if (isVanished(player)) {
            unvanishPlayer(player);
            player.sendMessage("§aYou have been unvanished. You are now visible to all players.");
        } else {
            vanishPlayer(player);
            player.sendMessage("§aYou have vanished. You are now invisible to all players.");
        }

        return true;
    }

    public static void vanishPlayer(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.hidePlayer(player);
        }

        player.setPlayerListName("§7" + player.getName());

        vanishedPlayers.add(player);
    }

    static void unvanishPlayer(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.showPlayer(player);
        }

        player.setPlayerListName(player.getName());

        vanishedPlayers.remove(player);
    }

    static boolean isVanished(Player player) {
        return vanishedPlayers.contains(player);
    }
}
