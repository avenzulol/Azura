package me.avenzu.azura.check.checks.misc.invtweaks;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInWindowClick;

public class InvTweaksE extends Check {
    public InvTweaksE(PlayerStats player) {
        super(player);
    }

    @Override
    public void onPacketReceiving(Class<? extends Packet> type, Packet packet) {
        if (type == PacketPlayInWindowClick.class && ((PacketPlayInWindowClick) packet).a() == 0 && !player.isInventoryOpen()) {
            onViolation("InvTweaks (E)", 60000L, 0);
        }
    }
}
