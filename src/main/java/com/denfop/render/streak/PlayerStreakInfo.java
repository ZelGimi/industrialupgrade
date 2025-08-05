package com.denfop.render.streak;

import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;

public class PlayerStreakInfo {

    private RGB rgb;
    private boolean rainbow;

    public PlayerStreakInfo(RGB rgb, boolean rainbow) {
        this.rgb = rgb;
        this.rainbow = rainbow;
    }

    public PlayerStreakInfo(CompoundTag nbtTagCompound) {
        this.rgb = new RGB(nbtTagCompound.getShort("red"), nbtTagCompound.getShort("green"), nbtTagCompound.getShort("blue"));
        this.rainbow = nbtTagCompound.getBoolean("rainbow");
    }

    public PlayerStreakInfo(CustomPacketBuffer customPacketBuffer) {
        this.rgb = new RGB(customPacketBuffer.readShort(), customPacketBuffer.readShort(), customPacketBuffer.readShort());
        this.rainbow = customPacketBuffer.readBoolean();
    }

    public CompoundTag writeNBT() {
        final CompoundTag nbt = new CompoundTag();
        nbt.putShort("red", rgb.getRed());
        nbt.putShort("blue", rgb.getBlue());
        nbt.putShort("green", rgb.getGreen());
        nbt.putBoolean("rainbow", rainbow);
        return nbt;
    }

    public CustomPacketBuffer writePacket(RegistryAccess registryAccess) {
        CustomPacketBuffer packetBuffer = new CustomPacketBuffer(registryAccess);
        packetBuffer.writeShort(rgb.getRed());
        packetBuffer.writeShort(rgb.getBlue());
        packetBuffer.writeShort(rgb.getGreen());
        packetBuffer.writeBoolean(this.rainbow);
        return packetBuffer;
    }

    public boolean isRainbow() {
        return rainbow;
    }

    public void setRainbow(final boolean rainbow) {
        this.rainbow = rainbow;
    }

    public RGB getRgb() {
        return rgb;
    }

    public void setRgb(final RGB rgb) {
        this.rgb = rgb;
    }

}
