package net.md_5.bungee.protocol.packet;

import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = false)
public class Packet9Respawn extends DefinedPacket {

    private byte dimension;

    private Packet9Respawn() {
        super(0x09);
    }

    public Packet9Respawn(byte dimension) {
        this();
        this.dimension = dimension;
    }

    @Override
    public void read(ByteBuf buf) {
        dimension = buf.readByte();
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(dimension);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }
}
