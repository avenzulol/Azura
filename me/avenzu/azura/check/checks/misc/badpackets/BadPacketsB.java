package me.avenzu.azura.check.checks.misc.badpackets;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;

public class BadPacketsB extends Check {
    public BadPacketsB(PlayerStats player) {
        super(player);
    }

    @Override
    public void onPacketReceiving(Class<? extends Packet> type, Packet packet) {
        if (type == PacketPlayInArmAnimation.class && player.getLastMovePacket() + 20L > System.currentTimeMillis()) {
            onViolation("BadPackets (B)", 60000L, 2);
        }
    }
}
