package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.blocks.state.TileEntityBlockStateContainer;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.render.base.ISpecialParticleModel;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.utils.ParticleBaseBlockDust;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.renderer.block.model.IBakedModel;
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


        double entityPosX = entity.posX + (world.rand.nextFloat() - 0.5) * entity.width;
        double entityPosY = entity.getEntityBoundingBox().minY + 0.1;
        double entityPosZ = entity.posZ + (world.rand.nextFloat() - 0.5) * entity.width;

        double speedX = -entity.motionX * 4.0;
        double speedZ = -entity.motionZ * 4.0;


        int posX = (int) (entityPosX * 10);
        int posY = (int) (entityPosY * 10);
        int posZ = (int) (entityPosZ * 10);

        int speedXInt = (int) (speedX * 1024);
        int speedZInt = (int) (speedZ * 1024);
        long packedCoordinates = 0L;
        packedCoordinates |= (long) (posX & 0xFFFFF) << 44;
        packedCoordinates |= (long) (posY & 0xFFFFF) << 24;
        packedCoordinates |= (long) (posZ & 0xFFFFF) << 4;

        int packedSpeeds = 0;
        packedSpeeds |= (speedXInt & 65565) << 16;
        packedSpeeds |= (speedZInt & 65565);
        buffer.writeLong(packedCoordinates);
        buffer.writeInt(packedSpeeds);
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
        long packedCoordinates = is.readLong();
        int x = (int) ((packedCoordinates >> 44) & 0xFFFFF);
        int y = (int) ((packedCoordinates >> 24) & 0xFFFFF);
        int z = (int) ((packedCoordinates >> 4) & 0xFFFFF);
        int packedSpeeds = is.readInt();
        double xSpeed = ((packedSpeeds >> 16) & 65565) / 1024D;
        double zSpeed = (packedSpeeds & 65565) / 1024D;

        TileEntityBlock block = (TileEntityBlock) entityPlayer.getEntityWorld().getTileEntity(pos);
        if (block != null) {
            IUCore.proxy.requestTick(false, () -> {
                IBlockState state = block.getBlockState();
                ParticleDigging particle = new ParticleBaseBlockDust(
                        entityPlayer.getEntityWorld(),
                        x / 10D,
                        y / 10D,
                        z / 10D,
                        xSpeed,
                        0,
                        zSpeed,
                        state
                );
                if (block.hasSpecialModel()) {
                    IBakedModel model = Minecraft
                            .getMinecraft()
                            .getBlockRendererDispatcher()
                            .getBlockModelShapes()
                            .getModelForState(state);
                    if (model instanceof ISpecialParticleModel) {

                        state = state.getBlock().getExtendedState(state, entityPlayer.getEntityWorld(), pos);


                        ((ISpecialParticleModel) model).enhanceParticle(
                                particle,
                                (TileEntityBlockStateContainer.PropertiesStateInstance) state
                        );
                    }
                }
                particle.init();
                Minecraft.getMinecraft().effectRenderer.addEffect(particle);
            });
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
