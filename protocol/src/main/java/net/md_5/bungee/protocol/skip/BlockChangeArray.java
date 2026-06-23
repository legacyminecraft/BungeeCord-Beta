package net.md_5.bungee.protocol.skip;

import io.netty.buffer.ByteBuf;

class BlockChangeArray extends Instruction {

    @Override
    void read(ByteBuf in) {
        short size = in.readShort();
        for (short s = 0; s < size; s++) {
            in.readShort();
            in.readByte();
            in.readByte();
        }
    }
}