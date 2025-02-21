package com.denfop.render.rocketpad;

import com.denfop.tiles.mechanism.TileEntityRocketLaunchPad;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;

public class RocketPadRender {

    public void render(TileEntityRocketLaunchPad te) {
        for (Iterator<DataRocket> iterator = te.rocketList.iterator(); iterator.hasNext(); ) {
            DataRocket rocket = iterator.next();
            GlStateManager.pushMatrix();
            GlStateManager.translate(te.getPos().getX() + 0.5f, te.getPos().getY() + 1 + 0.25,
                    te.getPos().getZ() + 0.5f
            );
            final IBakedModel model = Minecraft
                    .getMinecraft()
                    .getRenderItem()
                    .getItemModelWithOverrides(rocket.getItem(), null, null);

            BlockPos pos = rocket.getPos();
            final BlockPos pos1 = pos.add(-te.getBlockPos().getX(), -te.getBlockPos().getY(), -te.getBlockPos().getZ());
            GlStateManager.translate(pos1.getX(), pos1.getY(),
                    pos1.getZ()
            );
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            GlStateManager.scale(2F, 3F, 2F);

            Minecraft.getMinecraft().getRenderItem().renderItem(
                    rocket.getItem(), model

            );

            GlStateManager.popMatrix();


            for (int i = 0; i < 10; i++) {
                double offsetX = (Math.random() - 0.5) * 0.5;
                double offsetZ = (Math.random() - 0.5) * 0.5;
                te.getWorld().spawnParticle(
                        EnumParticleTypes.SMOKE_LARGE,
                        pos.getX() + 0.5 + offsetX,
                        pos.getY() ,
                        pos.getZ() + 0.5 + offsetZ,
                        0, -0.1, 0
                );
            }


            for (int i = 0; i < 5; i++) {
                double offsetX = (Math.random() - 0.5) * 0.3;
                double offsetZ = (Math.random() - 0.5) * 0.3;
                te.getWorld().spawnParticle(
                        EnumParticleTypes.FLAME,
                        pos.getX() + 0.5 + offsetX,
                        pos.getY() ,
                        pos.getZ() + 0.5 + offsetZ,
                        0, -0.05, 0
                );
            }


            if (te.getWorld().getWorldTime() % 20 == 0) {
                te.getWorld().spawnParticle(
                        EnumParticleTypes.EXPLOSION_LARGE,
                        pos.getX() + 0.5,
                        pos.getY() ,
                        pos.getZ() + 0.5,
                        0, 0, 0
                );
            }


            for (int i = 0; i < 3; i++) {
                double offsetX = (Math.random() - 0.5) * 0.5;
                double offsetZ = (Math.random() - 0.5) * 0.5;
                te.getWorld().spawnParticle(
                        EnumParticleTypes.REDSTONE,
                        pos.getX() + 0.5 + offsetX,
                        pos.getY() ,
                        pos.getZ() + 0.5 + offsetZ,
                        0.2, 0.5, 1.0
                );
            }


            if (te.getWorld().getWorldTime() % 10 == 0) {
                te.getWorld().playSound(
                        null,
                        pos,
                        SoundEvents.ENTITY_FIREWORK_LAUNCH,
                        SoundCategory.BLOCKS,
                        1.0F,
                        1.0F + (float) (Math.random() * 0.2 - 0.1)
                );
            }
            pos = new BlockPos(pos.getX(), pos.getY() + 1,pos.getZ());
            rocket.setPos(pos);


            if (pos.getY() + 1 > 255) {
                iterator.remove();

            }
        }


    }

}
