package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.utils.ParticleBaseBlockDust;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class PacketRunParticles implements IPacket {

    public PacketRunParticles() {
    }

    public PacketRunParticles(final World world, final BlockPos pos, final Entity entity) {
        final CustomPacketBuffer buffer = new CustomPacketBuffer(64);
        buffer.writeByte(this.getId());
        try {
            EncoderHandler.encode(buffer, pos, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        buffer.writeDouble(entity.posX + (world.rand.nextFloat() - 0.5) * entity.width);
        buffer.writeDouble(entity.getEntityBoundingBox().minY + 0.1);
        buffer.writeDouble(entity.posZ + (world.rand.nextFloat() - 0.5) * entity.width);
        buffer.writeDouble(-entity.motionX * 4.0);
        buffer.writeDouble(-entity.motionZ * 4.0);
        for (final EntityPlayer player : world.playerEntities) {
            if (!(player instanceof EntityPlayerMP)) {
                continue;
            }
            final double distance = player.getDistanceSq(entity.posX, entity.posY, entity.posZ);
            if (distance > 1024.0) {
                continue;
            }
            IUCore.network.getServer().sendPacket(buffer, (EntityPlayerMP) player);
        }
    }

    @Override
    public byte getId() {
        return 14;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void readPacket(final CustomPacketBuffer is, final EntityPlayer entityPlayer) {
        final BlockPos pos;
        try {
            pos = (BlockPos) DecoderHandler.decode(is, EncodedType.BlockPos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final double x = is.readDouble();
        final double y = is.readDouble();
        final double z = is.readDouble();
        final double xSpeed = is.readDouble();
        final double zSpeed = is.readDouble();
        TileEntityBlock block = (TileEntityBlock) entityPlayer.getEntityWorld().getTileEntity(pos);
        IUCore.proxy.requestTick(false, () -> {
            IBlockState state = block.getBlockState();
            ParticleDigging particle = new ParticleBaseBlockDust(
                    entityPlayer.getEntityWorld(),
                    x,
                    y,
                    z,
                    xSpeed,
                    0,
                    zSpeed,
                    state
            );
            particle.init();
            Minecraft.getMinecraft().effectRenderer.addEffect(particle);
        });
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
