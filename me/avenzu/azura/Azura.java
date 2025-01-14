package me.avenzu.azura;

import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.WHITE;

import java.util.Map;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.avenzu.azura.listener.BukkitListener;
import me.avenzu.azura.stats.PlayerStats;
import me.avenzu.azura.util.core.commands.AzuraCommand;
import me.avenzu.azura.util.core.commands.BanCommand;
import me.avenzu.azura.util.core.commands.MuteCommand;
import me.avenzu.azura.util.core.commands.ReportCommand;
import me.avenzu.azura.util.core.commands.StaffModeCommand;
import me.avenzu.azura.util.core.commands.UnbanCommand;
import me.avenzu.azura.util.core.commands.UnmuteCommand;
import me.avenzu.azura.util.core.commands.VanishCommand;
import me.avenzu.azura.util.core.listeners.BanAttemptListener;
import me.avenzu.azura.util.core.listeners.BanListener;
import me.avenzu.azura.util.core.listeners.ChatListener;
import me.avenzu.azura.util.core.listeners.MenuListener;

public class Azura extends JavaPlugin {
	
	private int reportNotificationTaskId;

    private final Map<Player, PlayerStats> statsMap = new WeakHashMap<>();

    public static Azura INST;

    @Override
    public void onEnable() {
        INST = this;
        
        saveDefaultConfig();
        
        this.getCommand("azura").setExecutor(new AzuraCommand());
	    this.getCommand("report").setExecutor(new ReportCommand());
	    this.getCommand("ban").setExecutor(new BanCommand());
	    this.getCommand("unban").setExecutor(new UnbanCommand());
        this.getCommand("mute").setExecutor(new MuteCommand());
        this.getCommand("unmute").setExecutor(new UnmuteCommand());
        this.getCommand("vanish").setExecutor(new VanishCommand());
        this.getCommand("staffmode").setExecutor(new StaffModeCommand());
        
        getServer().getPluginManager().registerEvents(new BanAttemptListener(), this);
        getServer().getPluginManager().registerEvents(new StaffModeCommand(), this);
	    getServer().getPluginManager().registerEvents(new BanListener(), this);
	    getServer().getPluginManager().registerEvents(new MenuListener(), this);
	    getServer().getPluginManager().registerEvents(new ChatListener(), this);
	    
	    startReportNotification();
        new BukkitListener();
    }

    @Override
    public void onDisable() {
    	saveConfig();
        statsMap.clear();
        stopReportNotification();
    }
    
    public PlayerStats getStats(Player player) {
        return statsMap.computeIfAbsent(player, PlayerStats::new);
    }
    
    private void startReportNotification() {
        reportNotificationTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
            this,
            () -> notifyReporting(null),
            0L,
            60 * 20L
        );
    }
    
    private void stopReportNotification() {
        if (reportNotificationTaskId != 0) {
            Bukkit.getScheduler().cancelTask(reportNotificationTaskId);
        }
    }
    
    public static void notifyReporting(String flaggedPlayer) {
        String message = DARK_GRAY.toString() + "[" 
                        + RED.toString() + "Azura" 
                        + DARK_GRAY.toString() + "] " 
                        + GRAY.toString() + "» "
                        + RED.toString() + "Use /report <username> "
                        + WHITE.toString() + "to report rule breakers!";

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }

        Bukkit.getConsoleSender().sendMessage(message);
    }
}
