package me.avenzu.azura.check.checks.misc.badpackets;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;

public class BadPacketsH extends Check {
    public BadPacketsH(PlayerStats player) {
        super(player);
    }

    @Override
    public void onPacketReceiving(Class<? extends Packet> type, Packet packet) {
        if (type == PacketPlayInEntityAction.class && player.getLastMovePacket() + 550L < System.currentTimeMillis()) {
            onViolation("BadPackets (H)", 60000L, 0);
        }
    }
}
