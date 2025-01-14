package me.avenzu.azura.util.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ReportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /report <player>");
            return true;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        Inventory reportMenu = Bukkit.createInventory(null, 9, ChatColor.RED + "Report " + targetName);

        ItemStack cheatingItem = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta cheatingMeta = cheatingItem.getItemMeta();
        cheatingMeta.setDisplayName(ChatColor.RED + "Report for Cheating");
        cheatingItem.setItemMeta(cheatingMeta);

        ItemStack chatAbuseItem = new ItemStack(Material.BOOK_AND_QUILL);
        ItemMeta chatAbuseMeta = chatAbuseItem.getItemMeta();
        chatAbuseMeta.setDisplayName(ChatColor.RED + "Report for Chat Abuse");
        chatAbuseItem.setItemMeta(chatAbuseMeta);

        ItemStack badUsernameItem = new ItemStack(Material.NAME_TAG);
        ItemMeta badUsernameMeta = badUsernameItem.getItemMeta();
        badUsernameMeta.setDisplayName(ChatColor.RED + "Report for Bad Username");
        badUsernameItem.setItemMeta(badUsernameMeta);

        ItemStack badSkinItem = new ItemStack(Material.LEATHER);
        ItemMeta badSkinMeta = badSkinItem.getItemMeta();
        badSkinMeta.setDisplayName(ChatColor.RED + "Report for Bad Skin/Cape");
        badSkinItem.setItemMeta(badSkinMeta);

        reportMenu.setItem(1, cheatingItem);
        reportMenu.setItem(3, chatAbuseItem);
        reportMenu.setItem(5, badUsernameItem);
        reportMenu.setItem(7, badSkinItem);

        player.openInventory(reportMenu);

        return true;
    }
}