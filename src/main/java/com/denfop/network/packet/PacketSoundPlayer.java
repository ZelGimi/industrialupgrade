package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.audio.EnumSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketSoundPlayer implements IPacket {

    public PacketSoundPlayer(String name, EntityPlayer player) {
        final CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeString(name);
        IUCore.network.getServer().sendPacket(buffer, (EntityPlayerMP) player);
    }

    public PacketSoundPlayer(EnumSound name, EntityPlayer player) {
        final CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeString(name.getNameSounds());
        IUCore.network.getServer().sendPacket(buffer, (EntityPlayerMP) player);
    }

    public PacketSoundPlayer() {

    }

    @Override
    public byte getId() {
        return 16;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        entityPlayer.playSound(EnumSound.getSondFromString(customPacketBuffer.readString()), 1, 1);
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
