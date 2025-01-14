package me.avenzu.azura.check.checks.misc.badpackets;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;

public class BadPacketsC extends Check {

    private long lastDig;

    public BadPacketsC(PlayerStats player) {
        super(player);
    }

    @Override
    public void onPacketReceiving(Class<? extends Packet> type, Packet packet) {
        if (type == PacketPlayInBlockDig.class) {
            PacketPlayInBlockDig blockDig = (PacketPlayInBlockDig) packet;

            if (blockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                long now = System.currentTimeMillis();

                if (lastDig + 20L > now) {
                    onViolation("BadPackets (C)", 60000L, 0);
                }

                lastDig = now;
            }
        }
    }
}
