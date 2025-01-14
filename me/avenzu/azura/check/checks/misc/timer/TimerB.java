package me.avenzu.azura.check.checks.misc.timer;

import java.util.Deque;
import java.util.LinkedList;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayOutPosition;

public class TimerB extends Check {

    private long lastFlying;
    private final Deque<Double> timerSpeedDeque = new LinkedList<>();

    public TimerB(PlayerStats player) {
        super(player);
    }

    @Override
    public void onPacketReceiving(Class<? extends Packet> type, Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            long now = System.currentTimeMillis();

            if (lastFlying + 50L > System.currentTimeMillis()) {
                return;
            }

            double timerSpeed = 50.0 / (now - lastFlying);

            timerSpeedDeque.addLast(timerSpeed);

            if (timerSpeedDeque.size() == 20) {

                double averageTimerSpeed = timerSpeedDeque.stream()
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(0);

                if (averageTimerSpeed < 0.6) {
                    onViolation(String.format("Timer (B) [%s]", averageTimerSpeed), 60000L, 1);
                }

                timerSpeedDeque.clear();
            }

            lastFlying = now;
        }
    }

    @Override
    public void onPacketSend(Class<? extends Packet> type, Packet packet) {
        if (type == PacketPlayOutPosition.class) {
            if (timerSpeedDeque.size() > 0) {
                timerSpeedDeque.removeLast();
            }
        }
    }
}
