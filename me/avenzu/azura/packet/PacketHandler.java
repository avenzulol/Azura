package me.avenzu.azura.packet;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.RequiredArgsConstructor;
import me.avenzu.azura.stats.PlayerStats;
import net.minecraft.server.v1_8_R3.Packet;

@RequiredArgsConstructor
public class PacketHandler extends ChannelDuplexHandler {

    private final PlayerStats player;

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        try {
            if (!player.processOutgoingPacket((Packet) msg)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.write(ctx, msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (!player.processIncomingPacket((Packet) msg)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.channelRead(ctx, msg);
    }

}
