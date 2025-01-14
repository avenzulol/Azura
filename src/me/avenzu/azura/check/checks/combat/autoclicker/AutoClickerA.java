package me.avenzu.azura.check.checks.combat.autoclicker;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

public class AutoClickerA extends Check {

    private int threshold;

    public AutoClickerA(PlayerStats player) {
        super(player);
    }

    @Override
    public void onPacketReceiving(Class<? extends Packet> type, Packet packet) {
        if (player.isDigging()) {
            threshold /= 4;
            return;
        }

        if (packet instanceof PacketPlayInFlying) {
            if (player.getSwingDelay() <= 100L) { //no cps drops, proably cheating
                if (++threshold > 100) { //TODO: find appropriate threshold
                    onViolation("AutoClicker (A)", 60000L, 0);

                    threshold = 0;
                }

            } else {
                threshold /= 4;
            }
        }
    }
}
