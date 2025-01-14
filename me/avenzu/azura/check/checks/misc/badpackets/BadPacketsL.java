package me.avenzu.azura.check.checks.misc.badpackets;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

public class BadPacketsL extends Check {
    public BadPacketsL(PlayerStats player) {
        super(player);
    }

    @Override
    public void onPacketReceiving(Class<? extends Packet> type, Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            PacketPlayInFlying flying = (PacketPlayInFlying) packet;

            if (!flying.h()) {
                return;
            }

            if (Math.abs(flying.e()) > 90) {
                onViolation("BadPackets (L)", 60000L, 0);
            }
        }
    }
}
