package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.audio.EnumSound;
import com.denfop.audio.SoundHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PacketStopSoundPlayer implements IPacket {

    public PacketStopSoundPlayer(EnumSound name, Player player) {
        final CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeInt(name.ordinal());
        IUCore.network.getServer().sendPacket(buffer, (ServerPlayer) player);
    }


    public PacketStopSoundPlayer() {

    }

    @Override
    public byte getId() {
        return 50;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        SoundHandler.stopSound(EnumSound.values()[customPacketBuffer.readInt()]);
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
