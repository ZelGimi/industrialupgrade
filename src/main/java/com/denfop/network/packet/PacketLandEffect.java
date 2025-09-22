package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.blocks.state.TileEntityBlockStateContainer;
import com.denfop.render.base.ISpecialParticleModel;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.utils.ParticleBaseBlockDust;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketLandEffect implements IPacket {

    public PacketLandEffect() {

    }

    public PacketLandEffect(final World world, BlockPos pos, final double x, final double y, final double z, final int count) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(60);
        buffer.writeByte(this.getId());
        buffer.writeBlockPos(pos);
        buffer.writeBlockPos(new BlockPos(x, y, z));
        buffer.writeShort(count);
        for (final EntityPlayer player : world.playerEntities) {
            if (!(player instanceof EntityPlayerMP)) {
                continue;
            }
            final double distance = player.getDistanceSq(x, y, z);
            if (distance > 1024.0) {
                continue;
            }
            IUCore.network.getServer().sendPacket(buffer, (EntityPlayerMP) player);
        }
    }

    @Override
    public byte getId() {
        return 13;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void readPacket(final CustomPacketBuffer is, final EntityPlayer entityPlayer) {
        final BlockPos pos = is.readBlockPos();
        final BlockPos pos1 = is.readBlockPos();
        double x = pos1.getX();
        double y = pos1.getY();
        double z = pos1.getZ();
        int count = is.readShort();
        TileEntityBlock block = (TileEntityBlock) entityPlayer.getEntityWorld().getTileEntity(pos);
        IUCore.proxy.requestTick(false, () -> {
            final Minecraft mc = Minecraft.getMinecraft();
            if (mc.gameSettings.particleSetting <= 1 && (mc.gameSettings.particleSetting != 1 || entityPlayer.getEntityWorld().rand.nextInt(
                    3) != 0)) {
                IBlockState state = block.getBlockState();

                for (int i = 0; i < count; ++i) {
                    double mx = entityPlayer.getEntityWorld().rand.nextGaussian() * 0.15;
                    double my = entityPlayer.getEntityWorld().rand.nextGaussian() * 0.15;
                    double mz = entityPlayer.getEntityWorld().rand.nextGaussian() * 0.15;

                    ParticleDigging particle = new ParticleBaseBlockDust(
                            entityPlayer.getEntityWorld(),
                            x,
                            y,
                            z,
                            mx,
                            my,
                            mz,
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
                    mc.effectRenderer.addEffect(particle);
                }


            }
        });
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
