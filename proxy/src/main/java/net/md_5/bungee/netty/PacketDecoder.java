package net.md_5.bungee.netty;

import com.legacyminecraft.bungeeposeidon.ping.ServerListPingHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.packet.DefinedPacket;
import net.md_5.bungee.protocol.skip.PacketReader;

import java.io.IOException;
import java.util.List;

/**
 * This class will attempt to read a packet from {@link PacketReader}, with the
 * specified {@link #protocol} before returning a new {@link ByteBuf} with the
 * copied contents of all bytes read in this frame.
 * <p/>
 * It is based on {@link ReplayingDecoder} so that packets will only be returned
 * when all needed data is present.
 */
@AllArgsConstructor
public class PacketDecoder extends ReplayingDecoder<Void> {

    @Getter
    @Setter
    private Protocol protocol;
    private HandlerBoss handlerBoss;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (this.handlerBoss.getHandler() instanceof InitialHandler initialHandler
                && initialHandler.getThisState() == InitialHandler.State.HANDSHAKE
                && tryHandlePing(initialHandler, ctx, in)) {
            return;
        }

        // Store our start index
        int startIndex = in.readerIndex();
        // Run packet through framer
        DefinedPacket packet = protocol.read(in.readUnsignedByte(), in);
        // If we got this far, it means we have formed a packet, so lets grab the end index
        int endIndex = in.readerIndex();
        // Allocate a buffer big enough for all bytes we have read
        ByteBuf buf = in.copy(startIndex, endIndex - startIndex);
        // Checkpoint our state incase we don't have enough data for another packet
        checkpoint();
        // Store our decoded message
        out.add(new PacketWrapper(packet, buf));
    }

    private boolean tryHandlePing(InitialHandler initialHandler, ChannelHandlerContext ctx, ByteBuf buf) throws IOException {
        ServerListPingHandler pingHandler = this.handlerBoss.getPingHandler();
        if (pingHandler == null) {
            buf.markReaderIndex();
            int packetId = buf.readUnsignedByte();
            buf.resetReaderIndex();
            if (packetId > 2 && packetId != 250) {
                pingHandler = this.handlerBoss.enablePingProtocol(initialHandler);
            }
        }

        if (pingHandler != null) {
            try (ByteBufInputStream input = new ByteBufInputStream(buf);
                 ByteBufOutputStream output = new ByteBufOutputStream(ctx.alloc().ioBuffer())) {
                pingHandler.handlePing(input, output);
                ctx.writeAndFlush(output.buffer());
                if (pingHandler.isClosed()) {
                    ctx.channel().close();
                }
            }
            return true;
        }

        return false;
    }
}
