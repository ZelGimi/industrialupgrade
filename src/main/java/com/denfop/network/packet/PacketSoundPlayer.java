package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.sound.EnumSound;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PacketSoundPlayer implements IPacket {

    public PacketSoundPlayer(String name, Player player) {
        final CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeString(name);
        IUCore.network.getServer().sendPacket(buffer, (ServerPlayer) player);
    }

    public PacketSoundPlayer(EnumSound name, Player player) {
        final CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeString(name.getNameSounds());
        IUCore.network.getServer().sendPacket(buffer, (ServerPlayer) player);
    }

    public PacketSoundPlayer() {

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
