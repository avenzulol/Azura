package me.avenzu.azura.check.checks.misc.badpackets;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;

public class BadPacketsK extends Check {
    public BadPacketsK(PlayerStats player) {
        super(player);
    }

    @Override
    public void onPacketReceiving(Class<? extends Packet> type, Packet packet) {
        if (type == PacketPlayInBlockPlace.class) {
            PacketPlayInBlockPlace blockPlace = (PacketPlayInBlockPlace) packet;

            if (blockPlace.getFace() == 255 && blockPlace.a().asLong() >= 0) {
                onViolation("BadPackets (K)", 60000L, 0);
            }
        }
    }
}
