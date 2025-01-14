package me.avenzu.azura.check.checks.misc;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import me.avenzu.azura.util.entity.NMSUtil;
import me.avenzu.azura.util.math.MathUtil;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInTransaction;
import net.minecraft.server.v1_8_R3.PacketPlayOutTransaction;

/**
 * @author SkidRevenant
 * at 28/05/2018
 */
public class PingSpoof extends Check {

    private boolean sent;

    public PingSpoof(PlayerStats player) {
        super(player);
    }

    @Override
    public void onPacketReceiving(Class<? extends Packet> type, Packet packet) {
        if (packet instanceof PacketPlayInTransaction) {
            sent = false;

            long delay = System.currentTimeMillis() - player.getLastMovePacket();

            int playerPing = NMSUtil.getNmsPlayer(player.getBukkitPlayer()).ping;

            if (delay < 20L) {
                return;
            }

            if (playerPing > (delay + 50L) * 2.75) {
                this.onViolation(String.format("PingSpoof (+%dms)", playerPing - delay), 60000L, 0);
            }

        } else if (packet instanceof PacketPlayInFlying) {
            if (sent) {
                return;
            }

            NMSUtil.getNmsPlayer(player.getBukkitPlayer()).playerConnection.sendPacket(new PacketPlayOutTransaction(0, (short) MathUtil.RANDOM.nextInt(1000), false));
            sent = true;
        }
    }
}
