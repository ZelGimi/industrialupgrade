package com.denfop.render.rocketpad;

import com.denfop.IUItem;
import com.denfop.api.space.rovers.api.IRoversItem;
import com.denfop.tiles.mechanism.TileEntityRocketLaunchPad;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

import java.util.Iterator;

import static net.minecraft.world.item.ItemDisplayContext.GROUND;

public class RocketPadRender  {




    public static void render(TileEntityRocketLaunchPad te, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();

        if (te.rocketList.isEmpty() && !te.roverSlot.get(0).isEmpty()) {
            ItemStack rocket = te.roverSlot.get(0);
            switch (((IRoversItem) rocket.getItem()).getLevel()) {
                case ONE:
                    rocket = new ItemStack(IUItem.rocket.getItem());
                    break;
                case TWO:
                    rocket = new ItemStack(IUItem.adv_rocket.getItem());
                    break;
                case THREE:
                    rocket = new ItemStack(IUItem.imp_rocket.getItem());
                    break;
                case FOUR:
                    rocket = new ItemStack(IUItem.per_rocket.getItem());
                    break;
            }

            poseStack.pushPose();
            poseStack.translate(0.5,  0.25,  0.5);

            poseStack.scale(2F, 3F, 2F);

            itemRenderer.renderStatic(rocket,GROUND, combinedLight, combinedOverlay, poseStack, bufferSource,te.getLevel(),  0);
            poseStack.popPose();
        } else {
            for (Iterator<DataRocket> iterator = te.rocketList.iterator(); iterator.hasNext(); ) {
                DataRocket rocket = iterator.next();

                poseStack.pushPose();
                poseStack.translate(0.5,  0.25,  0.5);

                double y = rocket.getPos();
                double y1 = y - te.getBlockPos().getY();
                poseStack.translate(0, y1, 0);
                poseStack.scale(2F, 3F, 2F);

                itemRenderer.renderStatic(rocket.getItem(), GROUND, combinedLight, combinedOverlay, poseStack, bufferSource,te.getLevel(),  0);
                poseStack.popPose();

                BlockPos pos = new BlockPos(te.getBlockPos().getX(), (int) y, te.getBlockPos().getZ());

                for (int i = 0; i < 10; i++) {
                    double offsetX = (Math.random() - 0.5) * 0.5;
                    double offsetZ = (Math.random() - 0.5) * 0.5;
                    te.getLevel().addParticle(
                            net.minecraft.core.particles.ParticleTypes.LARGE_SMOKE,
                            pos.getX() + 0.5 + offsetX,
                            y,
                            pos.getZ() + 0.5 + offsetZ,
                            0, -0.1, 0
                    );
                }

                for (int i = 0; i < 5; i++) {
                    double offsetX = (Math.random() - 0.5) * 0.3;
                    double offsetZ = (Math.random() - 0.5) * 0.3;
                    te.getLevel().addParticle(
                            net.minecraft.core.particles.ParticleTypes.FLAME,
                            pos.getX() + 0.5 + offsetX,
                            y,
                            pos.getZ() + 0.5 + offsetZ,
                            0, -0.05, 0
                    );
                }

                if (te.getLevel().getGameTime() % 20 == 0) {
                    te.getLevel().addParticle(
                            net.minecraft.core.particles.ParticleTypes.EXPLOSION,
                            pos.getX() + 0.5,
                            y,
                            pos.getZ() + 0.5,
                            0, 0, 0
                    );
                }

                for (int i = 0; i < 3; i++) {
                    double offsetX = (Math.random() - 0.5) * 0.5;
                    double offsetZ = (Math.random() - 0.5) * 0.5;
                    te.getLevel().addParticle(
                            new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.0F),
                            pos.getX() + 0.5 + offsetX,
                            y,
                            pos.getZ() + 0.5 + offsetZ,
                            1.0, 0.0, 0.0
                    );
                }

                if (te.getLevel().getGameTime() % 10 == 0) {
                    te.getLevel().playSound(
                            null,
                            pos,
                            SoundEvents.FIREWORK_ROCKET_LAUNCH,
                            SoundSource.BLOCKS,
                            1.0F,
                            1.0F + (float) (Math.random() * 0.2 - 0.1)
                    );
                }

                rocket.setPos(rocket.getPos() + 0.25);

                if (rocket.getPos() + 0.25 > 255) {
                    iterator.remove();
                }
            }
        }
    }


}
