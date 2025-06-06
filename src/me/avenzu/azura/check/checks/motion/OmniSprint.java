package me.avenzu.azura.check.checks.motion;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;

public class OmniSprint extends Check {
    public OmniSprint(PlayerStats player) {
        super(player);
    }

    @Override
    public void onMove(Location from, Location to, PlayerMoveEvent event) {
        double deltaX = to.getX() - from.getX();
        double deltaZ = to.getZ() - from.getZ();

        float yaw = Math.abs(from.getYaw()) % 360;

        if (!player.getBukkitPlayer().isSprinting()) {
            return;
        }

        if (deltaX < 0.0 && deltaZ > 0.0 && yaw > 180.0f && yaw < 270.0f || deltaX < 0.0 && deltaZ < 0.0 && yaw > 270.0f && yaw < 360.0f || deltaX > 0.0 && deltaZ < 0.0 && yaw > 0.0f && yaw < 90.0f || deltaX > 0.0 && deltaZ > 0.0 && yaw > 90.0f && yaw < 180.0f) {
            onViolation("OmniSprint", 60000L, 0);
        }
    }
}
