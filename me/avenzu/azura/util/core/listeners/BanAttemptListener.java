package me.avenzu.azura.util.core.listeners;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.WHITE;

import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class BanAttemptListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        BanEntry banEntry = banList.getBanEntry(player.getName());

        if (banEntry != null) {
            String bannedPlayerName = player.getName();
            String reason = banEntry.getReason();
            String banDate = extractBanDate(reason);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.isOp()) {
                    onlinePlayer.sendMessage(ChatColor.RED + "[Warning] " + bannedPlayerName + " has attempted to join the server but is banned!");
                }
            }

            String customBanMessage = formatBanMessage(reason.split("\n")[0], banDate);
            event.disallow(Result.KICK_BANNED, customBanMessage);
        }
    }
    
    private String extractBanDate(String reason) {
        for (String line : reason.split("\n")) {
            if (line.startsWith("Banned On: ")) {
                return line.replace("Banned On: ", "").trim();
            }
        }
        return "Unknown";
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
