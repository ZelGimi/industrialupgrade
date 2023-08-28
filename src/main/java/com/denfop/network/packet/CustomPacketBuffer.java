package com.denfop.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CustomPacketBuffer extends PacketBuffer {

    private static final Charset utf8 = StandardCharsets.UTF_8;

    public CustomPacketBuffer(final ByteBuf wrapped) {
        super(wrapped);
    }

    public CustomPacketBuffer(final int size) {
        super(Unpooled.buffer().capacity(size));
    }

    public CustomPacketBuffer(byte[] data) {
        this();
        this.writeBytes(data);
    }

    public CustomPacketBuffer() {
        super(Unpooled.buffer());
    }


    public CustomPacketBuffer writeString(String s) {
        byte[] bytes = s.getBytes(utf8);
        this.writeVarInt(bytes.length);
        this.writeBytes(bytes);
        return this;
    }

    public String readString() {
        int len = this.readVarInt();
        byte[] bytes = new byte[len];
        this.readBytes(bytes);
        return new String(bytes, utf8);
    }

    public void flip() {
        this.readerIndex(0);
    }

    public ByteBuf toByteBuf() {
        int len = this.writerIndex() - this.readerIndex();
        if (len <= 0) {
            return Unpooled.EMPTY_BUFFER;
        } else {


            return Unpooled.wrappedBuffer(this.array(), this.readerIndex(), len);
        }
    }

}
