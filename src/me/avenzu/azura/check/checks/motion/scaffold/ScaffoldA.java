package me.avenzu.azura.check.checks.motion.scaffold;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;

import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.Material;

public class ScaffoldA extends Check implements Listener {

    private static final double MAX_VERTICAL_MOVEMENT = 0.2;
    private static final double MAX_HORIZONTAL_SPEED = 0.4;
    private static final double MIN_Y_OFFSET = 1.0;

    public ScaffoldA(PlayerStats player) {
        super(player);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction().toString().contains("RIGHT_CLICK_BLOCK") || event.getAction().toString().contains("LEFT_CLICK_BLOCK")) {
            Block block = event.getClickedBlock();
            if (block != null && !isOnGround(player)) {
                double offsetY = player.getLocation().getY() - block.getY();

                if (offsetY >= MIN_Y_OFFSET && block.getType() != Material.AIR) {
                    onViolation("Scaffold (A)", 60000L, 0);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        double verticalVelocity = player.getVelocity().getY();
        double horizontalSpeed = player.getVelocity().length();

        if (Math.abs(verticalVelocity) > MAX_VERTICAL_MOVEMENT) {
            onViolation("Scaffold (A2)", 60000L, 0);
        }

        if (horizontalSpeed > MAX_HORIZONTAL_SPEED) {
            onViolation("Scaffold (A3)", 60000L, 0);
        }
    }

    private boolean isOnGround(Player player) {
        return player.getLocation().getBlock().getType().isSolid();
    }
}