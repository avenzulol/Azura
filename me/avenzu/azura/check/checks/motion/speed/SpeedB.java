package me.avenzu.azura.check.checks.motion.speed;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import me.avenzu.azura.util.math.MathUtil;

public class SpeedB extends Check {

    private double lastSpeed;
    private int violationLevel;

    private static final double MAX_WALK_SPEED = 0.22;
    private static final double MAX_SPRINT_SPEED = 0.36;
    private static final double BOOST_THRESHOLD = 0.1;
    private static final int MAX_TICKS_IN_LAG = 2;

    public SpeedB(PlayerStats player) {
        super(player);
    }

    @Override
    public void onMove(Location from, Location to, PlayerMoveEvent event) {
        double deltaX = to.getX() - from.getX();
        double deltaZ = to.getZ() - from.getZ();

        double currentSpeed = MathUtil.getMagnitude(deltaX, deltaZ);
        double maxAllowedSpeed = player.getBukkitPlayer().isSprinting() ? MAX_SPRINT_SPEED : MAX_WALK_SPEED;

        if (currentSpeed > maxAllowedSpeed + BOOST_THRESHOLD) {
            if (++violationLevel > MAX_TICKS_IN_LAG) {
                onViolation(String.format("Speed (B) [%.3f]", currentSpeed), 50000L, 1);
            }
        } else {
            violationLevel = Math.max(0, violationLevel - 1);
        }

        lastSpeed = currentSpeed;
    }
}