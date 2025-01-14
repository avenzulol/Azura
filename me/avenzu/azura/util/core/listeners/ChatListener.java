package me.avenzu.azura.util.core.listeners;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.avenzu.azura.util.core.commands.MuteCommand;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (MuteCommand.mutedPlayers.containsKey(playerUUID)) {
            event.setCancelled(true);

            long remainingTime = MuteCommand.getRemainingMuteTime(player);

            long minutes = remainingTime / 60;
            long seconds = remainingTime % 60;

            String mutedMessage = ChatColor.RED + ChatColor.STRIKETHROUGH.toString() + "------------------------------------------------\n" +
                                  ChatColor.RED + "You are currently muted for abusing the chat.\n" +
                                  ChatColor.WHITE + "Your mute will expire in: " + ChatColor.RED + minutes + "m " + seconds + "s\n" +
                                  ChatColor.WHITE + " \n" +
                                  ChatColor.WHITE + "If you believe this was false contact staff!\n" +
                                  ChatColor.RED + ChatColor.STRIKETHROUGH.toString() + "------------------------------------------------";

            player.sendMessage(mutedMessage);
        }
    }
}
