package com.denfop.render.tank;

import com.denfop.render.RenderFluidBlock;
import com.denfop.tiles.base.TileEntityLiquedTank;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileEntityTankRender implements BlockEntityRenderer<TileEntityLiquedTank> {
    private final BlockEntityRendererProvider.Context contex;

    public TileEntityTankRender(BlockEntityRendererProvider.Context p_173636_) {
        this.contex = p_173636_;
    }

    @Override
    public void render(TileEntityLiquedTank tile, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int combinedOverlay) {
        FluidTank tank = tile.fluidTank;
        FluidStack fluidStack = tank.getFluid();

        if (!fluidStack.isEmpty()) {
            final float scale = (tank.getFluidAmount()) * 1F / tank.getCapacity();

            poseStack.pushPose();
            poseStack.translate(0.1 + 0.125, 0.1, 0.1 + 0.125);
            RenderFluidBlock.renderFluid(fluidStack, bufferSource, tile.getLevel(), tile.getPos(), poseStack, 0.88f * scale, 0.8f);
            poseStack.popPose();
        }
    }
}
