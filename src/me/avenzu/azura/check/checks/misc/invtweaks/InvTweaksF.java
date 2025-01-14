package me.avenzu.azura.check.checks.misc.invtweaks;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;

public class InvTweaksF extends Check {

    private int threshold;

    public InvTweaksF(PlayerStats player) {
        super(player);
    }

    @Override
    public void onPacketReceiving(Class<? extends Packet> type, Packet packet) {
        if (type == PacketPlayInEntityAction.class) {
            if (player.isInventoryOpen()) {
                if (++threshold > 1) {
                    onViolation("InvTweaks (F)", 60000L, 0);
                }
            } else {
                threshold = 0;
            }
        }
    }
}
