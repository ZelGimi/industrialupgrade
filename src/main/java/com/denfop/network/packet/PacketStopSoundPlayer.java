package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.audio.EnumSound;
import com.denfop.audio.SoundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketStopSoundPlayer implements IPacket {

    public PacketStopSoundPlayer(EnumSound name, EntityPlayer player) {
        final CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeInt(name.ordinal());
        IUCore.network.getServer().sendPacket(buffer, (EntityPlayerMP) player);
    }


    public PacketStopSoundPlayer() {

    }

    @Override
    public byte getId() {
        return 50;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        SoundHandler.stopSound(EnumSound.values()[customPacketBuffer.readInt()]);
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
