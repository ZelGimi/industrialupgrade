package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.audio.SoundHandler;
import com.denfop.network.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class PacketStopSound implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketStopSound() {

    }

    public PacketStopSound(Level world, BlockPos pos) {
        List<ServerPlayer> playersInRange = NetworkManager.getPlayersInRange(
                world,
                pos,
                new ArrayList<>()
        );
        for (ServerPlayer player : playersInRange) {
            CustomPacketBuffer buffer = new CustomPacketBuffer(world.registryAccess());
            buffer.writeByte(this.getId());
            buffer.writeBlockPos(pos);

            this.buffer = buffer;
            IUCore.network.getServer().sendPacket(this, buffer, player);
        }

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
        return 35;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        BlockPos pos = customPacketBuffer.readBlockPos();
        SoundHandler.stopSound(pos);

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
