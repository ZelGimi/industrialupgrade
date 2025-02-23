package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.network.NetworkManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PacketCableSound implements IPacket {

    public PacketCableSound() {

    }

    public PacketCableSound(World world, BlockPos pos, double volume, double pitch) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(60);
        buffer.writeByte(this.getId());
        buffer.writeBlockPos(pos);
        buffer.writeDouble(volume);
        buffer.writeDouble(pitch);
        List<EntityPlayerMP> playersInRange = NetworkManager.getPlayersInRange(
                world,
                pos,
                new ArrayList<>()
        );
        for (EntityPlayerMP player : playersInRange) {
            IUCore.network.getServer().sendPacket(new CustomPacketBuffer(buffer), player);
        }
    }

    @Override
    public byte getId() {
        return 33;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        BlockPos pos = customPacketBuffer.readBlockPos();
        final double volume = customPacketBuffer.readDouble();
        final double pitch = customPacketBuffer.readDouble();
        entityPlayer.playSound(
                SoundEvents.ENTITY_GENERIC_BURN,
                (float) volume,
                (float) pitch
        );


        for (int l = 0; l < 8; ++l) {
            entityPlayer.getEntityWorld().spawnParticle(
                    EnumParticleTypes.SMOKE_LARGE,
                    (double) pos.getX() + Math.random(),
                    (double) pos.getY() + 1.2D,
                    (double) pos.getZ() + Math.random(),
                    0.0D,
                    0.0D,
                    0.0D
            );
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
