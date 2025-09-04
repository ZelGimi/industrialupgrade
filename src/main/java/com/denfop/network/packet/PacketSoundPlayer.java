package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.sound.EnumSound;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PacketSoundPlayer implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketSoundPlayer(String name, Player player) {
        final CustomPacketBuffer buffer = new CustomPacketBuffer(player.registryAccess());
        buffer.writeByte(this.getId());
        buffer.writeString(name);
        this.buffer = buffer;
        IUCore.network.getServer().sendPacket(this, buffer, (ServerPlayer) player);
    }

    public PacketSoundPlayer(EnumSound name, Player player) {
        final CustomPacketBuffer buffer = new CustomPacketBuffer(player.registryAccess());
        buffer.writeByte(this.getId());
        buffer.writeString(name.getNameSounds());
        this.buffer = buffer;
        IUCore.network.getServer().sendPacket(this, buffer, (ServerPlayer) player);
    }

    public PacketSoundPlayer() {

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
        return 16;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        entityPlayer.playSound(EnumSound.getSondFromString(customPacketBuffer.readString()), 1, 1);
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
