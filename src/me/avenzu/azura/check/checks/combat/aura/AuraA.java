package me.avenzu.azura.check.checks.combat.aura;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

public class AuraA extends Check {

    public AuraA(PlayerStats player) {
        super(player);
    }

    @Override
    public void onPacketReceiving(Class<? extends Packet> type, Packet packet) {
        if (type == PacketPlayInUseEntity.class && ((PacketPlayInUseEntity) packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK && player.getLastMovePacket() + 20L > System.currentTimeMillis()) {
            onViolation("KillAura (A)", 10000L, 6);
        }
    }
}
