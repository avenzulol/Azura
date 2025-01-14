package me.avenzu.azura.check;

import static org.bukkit.ChatColor.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import lombok.RequiredArgsConstructor;
import me.avenzu.azura.stats.PlayerStats;
import me.avenzu.azura.util.core.commands.AzuraCommand;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.Packet;

@RequiredArgsConstructor
public abstract class Check {

	public final PlayerStats player;

	private final Deque<Long> violations = new LinkedList<>();
	private final int FLAG_THRESHOLD = 100;

	protected void onViolation(String details, long time, int vl) {
		long now = System.currentTimeMillis();

		if (violations.contains(now)) {
			return;
		}

		violations.addLast(now);

		int violationsCount = Math
				.toIntExact(this.violations.stream().filter(timestamp -> timestamp + time > now).count());

		if (player.getBukkitPlayer().isOp()) {
			return;
		}

		MinecraftServer.getServer().processQueue.add(() -> {
			for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				if (onlinePlayer.isOp()) {
					onlinePlayer.sendMessage(String.format(
							DARK_GRAY + "[" + RED + "Azura" + DARK_GRAY + "] " + GRAY + "» " + WHITE
									+ player.getBukkitPlayer().getName() + " was flagged for " + RED + "%s §c(×%s)",
							details, violationsCount));
				}
			}
		});

		if (violationsCount >= FLAG_THRESHOLD) {
			if (AzuraCommand.isAutobanEnabled()) {
				executeAutoban();
				resetFlags();
			} else {
				for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
					if (onlinePlayer.isOp()) {
						onlinePlayer.sendMessage(String.format(
								DARK_GRAY + "[" + RED + "Azura" + DARK_GRAY + "] " + GRAY
										+ "» Player %s would be autobanned for %s, but autoban is currently disabled.",
								player.getBukkitPlayer().getName(), details));
						resetFlags();
					}
				}
			}
		}
	}

	private void executeAutoban() {
		String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		Bukkit.broadcastMessage(AQUA + "A player has been removed from your game.\n" + RED
				+ "Use /report to continue helping out the server!");

		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + player.getBukkitPlayer().getName() + " cheating");

		player.getBukkitPlayer().kickPlayer(formatBanMessage(currentDate));
	}

	private String formatBanMessage(String banDate) {
		return RED + "You are banned from this server\n" + GRAY + "Reason: " + WHITE
				+ "Cheating through the use of unfair advantages.\n\n" + RED + "Ban Information\n" + RED + "» " + GRAY
				+ "Staff: " + WHITE + "Azura AC\n" + RED + "» " + GRAY + "Date: " + WHITE + banDate + "\n\n" + GRAY
				+ "Please read the server rules!";
	}

	public void resetFlags() {
		violations.clear();
	}

	public void onPacketReceiving(Class<? extends Packet> type, Packet packet) {
	}

	public void onPacketSend(Class<? extends Packet> type, Packet packet) {
	}

	public void onMove(Location from, Location to, PlayerMoveEvent event) {
	}
}