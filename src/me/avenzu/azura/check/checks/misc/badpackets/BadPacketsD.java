package me.avenzu.azura.check.checks.misc.badpackets;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;

public class BadPacketsD extends Check {
    public BadPacketsD(PlayerStats player) {
        super(player);
    }

    @Override
    public void onPacketReceiving(Class<? extends Packet> type, Packet packet) {
        if (type == PacketPlayInEntityAction.class && player.getLastMovePacket() + 20L > System.currentTimeMillis()) {
            onViolation("BadPackets (D)", 60000L, 1);
        }
    }
}
