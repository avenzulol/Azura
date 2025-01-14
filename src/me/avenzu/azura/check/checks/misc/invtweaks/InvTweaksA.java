package me.avenzu.azura.check.checks.misc.invtweaks;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInCloseWindow;

public class InvTweaksA extends Check {
    public InvTweaksA(PlayerStats player) {
        super(player);
    }

    @Override
    public void onPacketReceiving(Class<? extends Packet> type, Packet packet) {
        if (type == PacketPlayInCloseWindow.class && player.getBukkitPlayer().isSprinting()) {
            onViolation("InvTweaks (A)", 60000L, 0);
        }
    }
}
