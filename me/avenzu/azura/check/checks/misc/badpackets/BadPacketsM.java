package me.avenzu.azura.check.checks.misc.badpackets;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInHeldItemSlot;

public class BadPacketsM extends Check {
    public BadPacketsM(PlayerStats player) {
        super(player);
    }

    @Override
    public void onPacketReceiving(Class<? extends Packet> type, Packet packet) {
        if (type == PacketPlayInHeldItemSlot.class && player.getLastMovePacket() + 20L > System.currentTimeMillis() && !player.getBukkitPlayer().isDead()) {
            onViolation("BadPackets (M)", 60000L, 0);
        }
    }
}
