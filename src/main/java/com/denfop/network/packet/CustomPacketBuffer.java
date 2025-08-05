package com.denfop.network.packet;

import com.denfop.IUCore;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CustomPacketBuffer extends RegistryFriendlyByteBuf {

    private static final Charset utf8 = StandardCharsets.UTF_8;

    public CustomPacketBuffer(final ByteBuf wrapped, RegistryAccess p_319803_) {
        super(wrapped, p_319803_);
    }

    public CustomPacketBuffer(final RegistryFriendlyByteBuf wrapped) {
        super(wrapped, wrapped.registryAccess());
    }

    public CustomPacketBuffer(final FriendlyByteBuf wrapped) {
        super(wrapped, IUCore.registry == null ? IUCore.registryAccess : IUCore.registry);
    }

    public CustomPacketBuffer(final int size, RegistryAccess p_319803_) {
        super(Unpooled.buffer().capacity(size), p_319803_);
    }

    public CustomPacketBuffer(byte[] data, RegistryAccess p_319803_) {
        this(p_319803_);
        this.writeBytes(data);
    }

    public CustomPacketBuffer(RegistryAccess p_319803_) {
        super(Unpooled.buffer(), p_319803_);
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
