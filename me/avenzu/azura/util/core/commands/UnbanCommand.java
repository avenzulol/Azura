package me.avenzu.azura.util.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.BanList;
import org.bukkit.BanList.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class UnbanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (sender instanceof Player) {
            Player senderPlayer = (Player) sender;
            
            if(senderPlayer.isOp() == false) {
                sender.sendMessage(ChatColor.RED + "[Error] You cannot unban players!");
                return true;
            }
        }
        
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /unban <player>");
            return false;
        }

        String playerName = args[0];
        BanList banList = Bukkit.getBanList(Type.NAME);

        if (banList.isBanned(playerName)) {
            banList.pardon(playerName);
            sender.sendMessage(ChatColor.GREEN + "[Success] Player " + ChatColor.WHITE + playerName + ChatColor.GREEN + " has been unbanned.");
        } else {
            sender.sendMessage(ChatColor.RED + "[Error] Player " + ChatColor.WHITE + playerName + ChatColor.RED + " is not banned.");
        }

        return true;
    }
}
