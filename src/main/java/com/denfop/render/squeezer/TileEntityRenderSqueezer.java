package com.denfop.render.squeezer;

import com.denfop.blockentity.mechanism.BlockEntitySqueezer;
import com.denfop.render.RenderFluidBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import static net.minecraft.world.item.ItemDisplayContext.GROUND;

public class TileEntityRenderSqueezer implements BlockEntityRenderer<BlockEntitySqueezer> {
    private final BlockEntityRendererProvider.Context contex;

    public TileEntityRenderSqueezer(BlockEntityRendererProvider.Context p_173636_) {
        this.contex = p_173636_;
    }

    @Override
    public void render(BlockEntitySqueezer tile, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int combinedOverlay) {
        ItemStack itemStack = tile.inputSlotA.get(0);
        ItemRenderer itemRenderer = contex.getItemRenderer();
        if (!itemStack.isEmpty()) {
            poseStack.pushPose();
            if (tile.facing == 4) {
                poseStack.translate(0.48, 0.875, -0.125);
            }
            if (tile.facing == 5) {
                poseStack.translate(0.52, 0.875, 0.89);
            }
            if (tile.facing == 3) {
                poseStack.translate(0, 0.875, 0.48);
            }
            if (tile.facing == 2) {
                poseStack.translate(1, 0.875, 0.3);
            }
            poseStack.mulPose(Axis.XP.rotationDegrees(90));

            itemRenderer.renderStatic(itemStack, GROUND,
                    packedLight, combinedOverlay, poseStack, bufferSource, tile.getLevel(), 0);
            poseStack.popPose();
        }
        FluidTank tank = tile.fluidTank1;
        FluidStack fluidStack = tank.getFluid();

        if (!fluidStack.isEmpty()) {
            final float scale = 1.45f * (tank.getFluidAmount()) * 1F / tank.getCapacity();

            poseStack.pushPose();
            if (tile.facing == 4) {
                poseStack.translate(0.07 + 0.12, 0.18, 0.6 + 0.08);
            }
            if (tile.facing == 5) {
                poseStack.translate(0.07 + 0.12, 0.18, -0.4 + 0.12);
            }
            if (tile.facing == 2) {
                poseStack.translate(-0.4 + 0.12, 0.18, 0.07 + 0.12);
            }
            if (tile.facing == 3) {
                poseStack.translate(0.6 + 0.08, 0.18, 0.07 + 0.12);
            }
            RenderFluidBlock.renderFluid(fluidStack, bufferSource, tile.getLevel(), tile.getPos(), poseStack, scale, 0.82f, 0);
            poseStack.popPose();
        }
    }
}
