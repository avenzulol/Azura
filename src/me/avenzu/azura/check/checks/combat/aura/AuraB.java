package me.avenzu.azura.check.checks.combat.aura;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import me.avenzu.azura.util.math.MathUtil;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

public class AuraB extends Check {

    private double lastDistance;
    private boolean attacked;

    public AuraB(PlayerStats player) {
        super(player);
    }

    @Override
    public void onMove(Location from, Location to, PlayerMoveEvent event) {
        double deltaX = to.getX() - from.getX();
        double deltaZ = to.getZ() - from.getZ();

        double horizontalDistance = MathUtil.getMagnitude(deltaX, deltaZ);

        if (attacked) {
            double distanceRange = Math.abs(horizontalDistance - lastDistance);

            if (distanceRange > 0 && distanceRange < 0.003) {
                onViolation("KillAura (B)", 10000L, 12);
            }

            lastDistance = horizontalDistance;

            attacked = false;
        }
    }

    @Override
    public void onPacketReceiving(Class<? extends Packet> type, Packet packet) {
        if (type == PacketPlayInUseEntity.class && ((PacketPlayInUseEntity) packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            attacked = true;
        }
    }
}
