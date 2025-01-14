package me.avenzu.azura.check.checks.misc.badpackets;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;

public class BadPacketsF extends Check {
    public BadPacketsF(PlayerStats player) {
        super(player);
    }

    @Override
    public void onPacketReceiving(Class<? extends Packet> type, Packet packet) {
        if (type == PacketPlayInBlockDig.class && ((PacketPlayInBlockDig) packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM && player.getLastMovePacket() + 20L > System.currentTimeMillis()) {
            onViolation("BadPackets (F)", 60000L, 0);
        }
    }
}
