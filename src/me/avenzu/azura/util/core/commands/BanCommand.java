package me.avenzu.azura.util.core.commands;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.WHITE;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "[Error] That player is not online.");
            return true;
        }

        if (sender instanceof Player) {
            Player senderPlayer = (Player) sender;

            if (!senderPlayer.isOp()) {
                sender.sendMessage(ChatColor.RED + "[Error] You cannot ban players!");
                return true;
            }

            if (senderPlayer.getName().equalsIgnoreCase(target.getName())) {
                sender.sendMessage(ChatColor.RED + "[Error] You cannot ban yourself!");
                return true;
            }

            if (target.isOp()) {
                sender.sendMessage(ChatColor.RED + "[Error] You cannot ban an operator!");
                return true;
            }
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /ban <player> <reason>");
            return true;
        }

        String reason = getBanReason(args[1]);
        if (reason == null) {
            sender.sendMessage(ChatColor.RED + "[Error] Invalid reason. Valid reasons: cheating, chat, name, skin.");
            return true;
        }

        String banDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String banReasonWithDate = reason + "\n" + "Banned On: " + banDate;

        Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), banReasonWithDate, null, sender.getName());
        target.kickPlayer(formatBanMessage(reason, banDate));

        sender.sendMessage(ChatColor.GREEN + "[Success] " + target.getName() + " has been banned for: " + reason);
        return true;
    }

    private String getBanReason(String reasonArg) {
        switch (reasonArg.toLowerCase()) {
            case "cheating":
                return "Cheating through the use of unfair advantages.";
            case "chat":
                return "Abusing the public chat through spam or other means.";
            case "name":
                return "Your username is not allowed on this server.";
            case "skin":
                return "Your skin was deemed inappropriate for this server.";
            default:
                return null;
        }
    }

    private String formatBanMessage(String reason, String banDate) {
        return RED + "You are banned from this server\n"
                + GRAY + "Reason: " + WHITE + reason + "\n\n"
                + RED + "Ban Information\n"
                + RED + "» " + GRAY + "Staff: " + WHITE + "Azura AC\n"
                + RED + "» " + GRAY + "Date: " + WHITE + banDate + "\n\n"
                + GRAY + "Please read the server rules!";
    }
}
