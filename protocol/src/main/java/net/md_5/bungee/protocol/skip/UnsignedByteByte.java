package net.md_5.bungee.protocol.skip;

import io.netty.buffer.ByteBuf;

class UnsignedByteByte extends Instruction {

    @Override
    void read(ByteBuf in) {
        int size = in.readUnsignedByte();
        in.skipBytes(size);
    }
}
