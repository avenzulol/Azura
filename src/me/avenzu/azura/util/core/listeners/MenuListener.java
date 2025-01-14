package me.avenzu.azura.util.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        String title = inventory.getTitle();
        
        ItemStack clickedItem = event.getCurrentItem();
        
        String itemName = clickedItem.getItemMeta().getDisplayName();
        String targetName = title.split(" ")[1];
        Player reporter = (Player) event.getWhoClicked();
        
        if (reporter.getName().equalsIgnoreCase(targetName)) {
            reporter.sendMessage(ChatColor.RED + "[Error] You cannot report yourself.");
            reporter.closeInventory();
            return;
        }

        if (title != null && title.startsWith(ChatColor.RED + "Report ")) {
            event.setCancelled(true);

            if (clickedItem == null || !clickedItem.hasItemMeta()) {
                return;
            }

            String reportMessage = ChatColor.RED + reporter.getName() + " has reported " + targetName + " for ";

            switch (itemName) {
                case "§cReport for Cheating":
                    reportMessage += "Cheating.";
                    break;
                case "§cReport for Chat Abuse":
                    reportMessage += "Chat Abuse.";
                    break;
                case "§cReport for Bad Username":
                    reportMessage += "Bad Username.";
                    break;
                case "§cReport for Bad Skin/Cape":
                    reportMessage += "Bad Skin/Cape.";
                    break;
                default:
                    return;
            }

            reporter.sendMessage(ChatColor.GREEN + "[Success] Your report against " + targetName + " has been submitted.");

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp()) {
                    player.sendMessage(reportMessage);
                }
            }

            reporter.closeInventory();
        }
    }
}