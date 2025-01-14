package me.avenzu.azura.stats;

import java.util.Deque;
import java.util.LinkedList;

import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.avenzu.azura.check.CheckManager;
import me.avenzu.azura.util.entity.NMSUtil;
import me.avenzu.azura.util.location.VelocityUtils;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInCloseWindow;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

@Getter
public class PlayerStats {

    private final Player bukkitPlayer;
    private final CheckManager checkManager;

    private final Deque<VelocityUtils> velocityDeque = new LinkedList<>();

    @Setter
    private double horizontalVelocity;

    private long swingDelay, lastSwing;
    private long lastMovePacket;

    private boolean digging;
    private boolean inventoryOpen;

    public PlayerStats(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;

        (checkManager = new CheckManager(this)).start();
    }

    public boolean processIncomingPacket(Packet packet) {
        Class<? extends Packet> type = packet.getClass();
        
        if (packet instanceof PacketPlayInFlying) {
            swingDelay += 50L;
            lastMovePacket = System.currentTimeMillis();

            velocityDeque.removeIf(velocity -> velocity.getCreationTime() + 2000L < System.currentTimeMillis());
        } else if (type == PacketPlayInArmAnimation.class) {
            swingDelay = 0L;

            lastSwing = System.currentTimeMillis();
        } else if (type == PacketPlayInBlockDig.class) {
            switch (((PacketPlayInBlockDig) packet).c()) {
                case START_DESTROY_BLOCK:
                    digging = true;
                    break;

                case STOP_DESTROY_BLOCK:
                case ABORT_DESTROY_BLOCK:
                    digging = false;
                    break;
            }
        } else if (type == PacketPlayInClientCommand.class) {
            if (((PacketPlayInClientCommand)packet).a() == PacketPlayInClientCommand.EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
                inventoryOpen = true;
            }
        } else if (type == PacketPlayInCloseWindow.class) {
            inventoryOpen = false;
        }

        checkManager.getChecks().forEach(check -> check.onPacketReceiving(type, packet));
        return true;
    }

    public boolean processOutgoingPacket(Packet packet) {
        checkManager.getChecks().forEach(check -> check.onPacketSend(packet.getClass(), packet));
        return true;
    }

    public double decreaseVelocity(double decrement) {
        return horizontalVelocity = Math.max(0, horizontalVelocity - decrement);
    }

    public boolean isOnGround() {
        return NMSUtil.getNmsPlayer(bukkitPlayer).onGround;
    }

    public int getTick() {
        return MinecraftServer.currentTick;
    }

}
