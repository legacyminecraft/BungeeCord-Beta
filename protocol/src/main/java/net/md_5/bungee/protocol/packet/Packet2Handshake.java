package net.md_5.bungee.protocol.packet;

import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Packet2Handshake extends DefinedPacket {

    private String username;

    private Packet2Handshake() {
        super(0x02);
    }

    public Packet2Handshake(String username) {
        this();
        this.username = username;
    }

    @Override
    public void read(ByteBuf buf) {
        username = readString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        writeString(username, buf);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }
}
