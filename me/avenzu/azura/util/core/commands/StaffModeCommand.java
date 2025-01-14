package me.avenzu.azura.util.core.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.avenzu.azura.Azura;

import java.util.*;

public class StaffModeCommand implements CommandExecutor, Listener {

    private final FileConfiguration config = Azura.INST.getConfig();
    private static final Map<Player, ItemStack[]> playerInventories = new HashMap<>();
    private static final Map<UUID, Long> vanishCooldowns = new HashMap<>();
    private static final long VANISH_COOLDOWN = 200;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.isOp()) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (isInStaffMode(player)) {
            resetInventory(player);
            player.sendMessage("§aYou have exited staff mode.");
        } else {
            enterStaffMode(player);
            player.sendMessage("§aYou are now in staff mode.");
        }

        return true;
    }

    private boolean isInStaffMode(Player player) {
        return playerInventories.containsKey(player);
    }

    private void enterStaffMode(Player player) {
        playerInventories.put(player, player.getInventory().getContents());
        player.getInventory().clear();
        addStaffModeItems(player);
    }

    private void addStaffModeItems(Player player) {
        ItemStack torch = new ItemStack(Material.TORCH);
        ItemMeta torchMeta = torch.getItemMeta();
        torchMeta.setDisplayName("§eVisible");
        torchMeta.setLore(Arrays.asList("§7Click to toggle vanish."));
        torch.setItemMeta(torchMeta);
        player.getInventory().setItem(6, torch);

        ItemStack autobanItem = getAutobanDyeItem();
        player.getInventory().setItem(7, autobanItem);

        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta compassMeta = compass.getItemMeta();
        compassMeta.setDisplayName("§eTeleport to Player");
        compass.setItemMeta(compassMeta);
        player.getInventory().setItem(8, compass);
    }

    private ItemStack getAutobanDyeItem() {
        boolean isAutobanEnabled = config.getBoolean("settings.autoban");
        Material material = Material.INK_SACK;
        byte dyeColor = (byte) (isAutobanEnabled ? 10 : 8);
        ItemStack dye = new ItemStack(material, 1, dyeColor);

        ItemMeta meta = dye.getItemMeta();
        meta.setDisplayName(isAutobanEnabled ? "§aAutoban (Enabled)" : "§7Autoban (Disabled)");
        meta.setLore(Arrays.asList("§7Click to toggle the autoban setting."));
        dye.setItemMeta(meta);

        return dye;
    }

    private void resetInventory(Player player) {
        ItemStack[] originalInventory = playerInventories.remove(player);
        if (originalInventory != null) {
            player.getInventory().setContents(originalInventory);
        }
        VanishCommand.unvanishPlayer(player);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isInStaffMode(player)) return;

        ItemStack item = player.getInventory().getItemInHand();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;

        String itemName = item.getItemMeta().getDisplayName();

        if (itemName.equals("§eVisible") || itemName.equals("§eVanished")) {
            if (canToggleVanish(player)) {
                toggleVanish(player, item);
            }
        } else if (itemName.equals("§aAutoban (Enabled)") || itemName.equals("§7Autoban (Disabled)")) {
            toggleAutoban(player);
        } else if (itemName.equals("§eTeleport to Player")) {
            player.sendMessage("§cTeleport to Player is coming soon!");
        }

        event.setCancelled(true);
    }

    private boolean canToggleVanish(Player player) {
        long currentTime = System.currentTimeMillis();
        UUID playerId = player.getUniqueId();

        if (vanishCooldowns.containsKey(playerId)) {
            long lastToggleTime = vanishCooldowns.get(playerId);
            if (currentTime - lastToggleTime < VANISH_COOLDOWN) {
                player.sendMessage("§cYou must wait before toggling vanish again!");
                return false;
            }
        }

        vanishCooldowns.put(playerId, currentTime);
        return true;
    }

    private void toggleVanish(Player player, ItemStack item) {
        if (VanishCommand.isVanished(player)) {
            VanishCommand.unvanishPlayer(player);
            player.sendMessage("§aYou are now visible.");
            updateItem(item, Material.TORCH, "§eVisible");
        } else {
            VanishCommand.vanishPlayer(player);
            player.sendMessage("§aYou have vanished and are now invisible.");
            updateItem(item, Material.LEVER, "§eVanished");
        }
    }

    private void toggleAutoban(Player player) {
        boolean currentState = config.getBoolean("settings.autoban");
        boolean newState = !currentState;

        config.set("settings.autoban", newState);
        Azura.INST.saveConfig();

        ItemStack autobanItem = getAutobanDyeItem();
        player.getInventory().setItem(7, autobanItem);

        player.sendMessage("§aAutoban has been " + (newState ? "enabled" : "disabled") + "!");
    }

    private void updateItem(ItemStack item, Material material, String displayName) {
        item.setType(material);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (isInStaffMode(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (isInStaffMode(player)) {
            resetInventory(player);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (isInStaffMode(player)) {
            event.setCancelled(true);
        }
    }
}