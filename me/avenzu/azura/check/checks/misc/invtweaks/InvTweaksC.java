package me.avenzu.azura.check.checks.misc.invtweaks;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;

public class InvTweaksC extends Check {

    public InvTweaksC(PlayerStats player) {
        super(player);
    }

    @Override
    public void onPacketReceiving(Class<? extends Packet> type, Packet packet) {
        if (type == PacketPlayInArmAnimation.class && player.isInventoryOpen()) {
            onViolation("InvTweaks (C)", 60000L, 2);
        }
    }
}
