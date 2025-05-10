package com.denfop.render.mini_smeltery;

import com.denfop.mixin.access.LevelRendererAccessor;
import com.denfop.render.RenderFluidBlock;
import com.denfop.tiles.mechanism.TileEntityMiniSmeltery;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class RenderMiniSmeltery {
    public static void render(TileEntityMiniSmeltery te, RenderLevelStageEvent event) {
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource bufferSource = ((LevelRendererAccessor) event.getLevelRenderer()).getRenderBuffers().bufferSource();

        FluidTank tank = te.fluidTank1;
        FluidStack fluidStack = tank.getFluid();

        if (!fluidStack.isEmpty() && fluidStack.getAmount() - 144 > 0) {
            final float scale = (te.fluidTank1.getFluidAmount() - 144) * 1F / te.fluidTank1.getCapacity();

            poseStack.pushPose();
            poseStack.translate(0.175, 0, 0.175);
            RenderFluidBlock.renderFluid(fluidStack, bufferSource, te.getLevel(), te.getPos(), poseStack, scale, 0.82f);
            poseStack.popPose();
        }

        ItemStack outputItem = te.outputSlot.get(0);
        if (te.outputSlot.isEmpty() && te.fluidTank1.getFluidAmount() > 0) {
            final float scale = (te.fluidTank1.getFluidAmount() - 144) * 1F / te.fluidTank1.getCapacity();
            poseStack.pushPose();
            poseStack.translate(0.04, 0.8, 0.04);
            RenderFluidBlock.renderFluid(fluidStack, bufferSource, te.getLevel(), te.getPos(), poseStack, 0.15f, 0.95f);
            poseStack.popPose();
        }
        if (!outputItem.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.93, 0.25);
            if (te.getFacing() == Direction.EAST || te.getFacing() == Direction.WEST) {
                poseStack.translate(0.25, 0, 0.25);


            }
            poseStack.mulPose(com.mojang.math.Vector3f.XP.rotationDegrees(90));
            if (te.getFacing() == Direction.EAST || te.getFacing() == Direction.WEST) {

                poseStack.mulPose(com.mojang.math.Vector3f.ZP.rotationDegrees(90));
            }
            poseStack.scale(2.0f, 2.0f, 2.0f);

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            itemRenderer.renderStatic(outputItem,
                    net.minecraft.client.renderer.block.model.ItemTransforms.TransformType.GROUND,
                    0xF000F0, OverlayTexture.NO_OVERLAY,
                    poseStack, bufferSource, 0);

            poseStack.popPose();
        }
    }

}
