package com.denfop.api.tesseract;

import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class Channel {

    private final int channel;
    private TypeMode mode;
    private TypeChannel typeChannel;
    private ITesseract tesseract;
    private boolean isPrivate;
    private boolean active;
    private BlockPos pos;

    public Channel(int channel, ITesseract tesseract, TypeMode mode, TypeChannel typeChannel, boolean isPrivate) {
        this.channel = channel;
        this.mode = mode;
        this.typeChannel = typeChannel;
        this.active = false;
        this.isPrivate = isPrivate;
        this.tesseract = tesseract;
        this.pos = tesseract.getBlockPos();
    }

    public Channel(int channel, TypeMode mode, TypeChannel typeChannel, boolean isPrivate) {
        this.channel = channel;
        this.mode = mode;
        this.typeChannel = typeChannel;
        this.active = false;
        this.isPrivate = isPrivate;
        this.tesseract = null;
        this.pos = BlockPos.ORIGIN;
    }

    public Channel(CustomPacketBuffer customPacketBuffer) {
        this.channel = customPacketBuffer.readInt();
        this.mode = TypeMode.values()[customPacketBuffer.readInt()];
        this.typeChannel = TypeChannel.values()[customPacketBuffer.readInt()];
        this.active = customPacketBuffer.readBoolean();
        this.isPrivate = customPacketBuffer.readBoolean();
    }

    public Channel(NBTTagCompound nbtTagCompound) {
        this.channel = nbtTagCompound.getInteger("channel");
        this.mode = TypeMode.values()[nbtTagCompound.getInteger("mode")];
        this.typeChannel = TypeChannel.values()[nbtTagCompound.getInteger("typeChannel")];
        this.active = nbtTagCompound.getBoolean("active");
        this.isPrivate = nbtTagCompound.getBoolean("isPrivate");
    }

    public ITesseract getTesseract() {
        return tesseract;
    }

    public void setTesseract(final ITesseract tesseract) {
        this.tesseract = tesseract;
        this.pos = tesseract.getBlockPos();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Channel channel1 = (Channel) o;
        return channel == channel1.channel && Objects.equals(tesseract, channel1.tesseract);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channel, tesseract);
    }

    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer packetBuffer = new CustomPacketBuffer();
        packetBuffer.writeInt(channel);
        packetBuffer.writeInt(mode.ordinal());
        packetBuffer.writeInt(typeChannel.ordinal());
        packetBuffer.writeBoolean(active);
        packetBuffer.writeBoolean(isPrivate);
        return packetBuffer;
    }

    public NBTTagCompound writeNBT() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setInteger("channel", channel);
        tagCompound.setInteger("mode", mode.ordinal());
        tagCompound.setInteger("typeChannel", typeChannel.ordinal());
        tagCompound.setBoolean("active", active);
        tagCompound.setBoolean("isPrivate", isPrivate);
        return tagCompound;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(final BlockPos pos) {
        this.pos = pos;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(final boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public TypeChannel getTypeChannel() {
        return typeChannel;
    }

    public void setTypeChannel(final TypeChannel typeChannel) {
        this.typeChannel = typeChannel;
    }

    public int getChannel() {
        return channel;
    }

    public TypeMode getMode() {
        return mode;
    }

    public void setMode(final TypeMode mode) {
        this.mode = mode;
    }

}
