package com.denfop.render.rocketpad;

import com.denfop.blockentity.mechanism.BlockEntityRocketLaunchPad;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

import java.util.Iterator;

import static net.minecraft.world.item.ItemDisplayContext.GROUND;

public class RocketPadRender {

    private static final float SCALE_X = 2.0F;
    private static final float SCALE_Y = 3.0F;
    private static final float SCALE_Z = 2.0F;

    private RocketPadRender() {
    }

    public static void render(BlockEntityRocketLaunchPad te,
                              float partialTicks,
                              PoseStack poseStack,
                              MultiBufferSource bufferSource,
                              int combinedLight,
                              int combinedOverlay) {

        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();

        if (te.getLevel() == null) {
            return;
        }

        if (te.rocketAnimations.isEmpty() && !te.roverSlot.get(0).isEmpty()) {
            ItemStack previewRocket = RocketItemResolver.fromRover(te.roverSlot.get(0));
            if (!previewRocket.isEmpty()) {
                renderIdleRocket(te, previewRocket, poseStack, bufferSource, itemRenderer, combinedLight, combinedOverlay);
            }
            return;
        }

        long gameTime = te.getLevel().getGameTime();

        for (Iterator<RocketLaunchAnimation> iterator = te.rocketAnimations.iterator(); iterator.hasNext(); ) {
            RocketLaunchAnimation animation = iterator.next();

            if (animation.shouldRemove(partialTicks, gameTime)) {
                iterator.remove();
                continue;
            }

            renderAnimatedRocket(
                    te,
                    animation,
                    partialTicks,
                    poseStack,
                    bufferSource,
                    itemRenderer,
                    combinedLight,
                    combinedOverlay,
                    gameTime
            );
        }
    }

    private static void renderIdleRocket(BlockEntityRocketLaunchPad te,
                                         ItemStack rocket,
                                         PoseStack poseStack,
                                         MultiBufferSource bufferSource,
                                         ItemRenderer itemRenderer,
                                         int combinedLight,
                                         int combinedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.25D, 0.5D);
        poseStack.scale(SCALE_X, SCALE_Y, SCALE_Z);

        itemRenderer.renderStatic(
                rocket,
                GROUND,
                combinedLight,
                combinedOverlay,
                poseStack,
                bufferSource,
                te.getLevel(),
                0
        );
        poseStack.popPose();
    }

    private static void renderAnimatedRocket(BlockEntityRocketLaunchPad te,
                                             RocketLaunchAnimation animation,
                                             float partialTicks,
                                             PoseStack poseStack,
                                             MultiBufferSource bufferSource,
                                             ItemRenderer itemRenderer,
                                             int combinedLight,
                                             int combinedOverlay,
                                             long gameTime) {

        double renderX = animation.getRenderX(partialTicks, gameTime) - te.getBlockPos().getX();
        double renderY = animation.getRenderY(partialTicks, gameTime) - te.getBlockPos().getY();
        double renderZ = animation.getRenderZ(partialTicks, gameTime) - te.getBlockPos().getZ();

        float pitch = animation.getPitch(partialTicks, gameTime);
        float roll = animation.getRoll(partialTicks, gameTime);

        poseStack.pushPose();
        poseStack.translate(0.5D + renderX, 0.25D + renderY, 0.5D + renderZ);

        // Обертаємо навколо центру предмета
        Quaternionf rotation = new Quaternionf()
                .rotateZ((float) Math.toRadians(roll))
                .rotateX((float) Math.toRadians(pitch));

        poseStack.mulPose(rotation);
        poseStack.scale(SCALE_X, SCALE_Y, SCALE_Z);

        itemRenderer.renderStatic(
                animation.getRocketStack(),
                GROUND,
                combinedLight,
                combinedOverlay,
                poseStack,
                bufferSource,
                te.getLevel(),
                0
        );

        poseStack.popPose();
    }
}