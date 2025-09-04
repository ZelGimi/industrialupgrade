package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.sound.EnumSound;
import com.denfop.sound.SoundHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PacketStopSoundPlayer implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketStopSoundPlayer(EnumSound name, Player player) {
        final CustomPacketBuffer buffer = new CustomPacketBuffer(player.registryAccess());
        buffer.writeByte(this.getId());
        buffer.writeInt(name.ordinal());
        this.buffer = buffer;
        IUCore.network.getServer().sendPacket(this, buffer, (ServerPlayer) player);
    }

    public PacketStopSoundPlayer() {

    }

    @Override
    public CustomPacketBuffer getPacketBuffer() {
        return buffer;
    }

    @Override
    public void setPacketBuffer(CustomPacketBuffer customPacketBuffer) {
        buffer = customPacketBuffer;
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
