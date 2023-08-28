package com.denfop.network.packet;

import com.denfop.IUCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;

public class PacketExplosion implements IPacket {

    public PacketExplosion() {
    }

    public PacketExplosion(Explosion explosion) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeBlockPos(new BlockPos(explosion.getPosition()));
        for (final EntityPlayer player : explosion.world.playerEntities) {
            if (!(player instanceof EntityPlayerMP)) {
                continue;
            }
            final double distance = player.getDistanceSq(explosion.getPosition().x, explosion.getPosition().y,
                    explosion.getPosition().z
            );
            if (distance > 1024) {
                continue;
            }
            IUCore.network.getServer().sendPacket(buffer, (EntityPlayerMP) player);
        }
    }

    @Override
    public byte getId() {
        return 15;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        BlockPos pos = customPacketBuffer.readBlockPos();
        new Explosion(entityPlayer.world, entityPlayer, pos.getX(), pos.getY(), pos.getZ(), 4, false, false).doExplosionB(true);

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
