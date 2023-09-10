package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.audio.SoundHandler;
import com.denfop.network.NetworkManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PacketStopSound implements IPacket {

    public PacketStopSound() {

    }

    public PacketStopSound(World world, BlockPos pos) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeBlockPos(pos);
        List<EntityPlayerMP> playersInRange = NetworkManager.getPlayersInRange(
                world,
                pos,
                new ArrayList<>()
        );
        for (EntityPlayerMP player : playersInRange) {
            IUCore.network.getServer().sendPacket(buffer, player);
        }

    }

    @Override
    public byte getId() {
        return 35;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        BlockPos pos = customPacketBuffer.readBlockPos();
        IUCore.proxy.requestTick(false, () -> SoundHandler.stopSound(pos));

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
