package com.denfop.render.autocollector;

import com.denfop.blockentity.mechanism.BlockEntityAutoLatexCollector;
import com.denfop.render.RenderFluidBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileEntityRenderAutoLatexCollector implements BlockEntityRenderer<BlockEntityAutoLatexCollector> {
    private final BlockEntityRendererProvider.Context contex;

    public TileEntityRenderAutoLatexCollector(BlockEntityRendererProvider.Context p_173636_) {
        this.contex = p_173636_;
    }

    @Override
    public void render(BlockEntityAutoLatexCollector te, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int combinedOverlay) {
        FluidTank tank = te.tank;
        FluidStack fluidStack = tank.getFluid();
        if (!fluidStack.isEmpty()) {
            final float scale = (te.tank.getFluidAmount()) * 1F / te.tank.getCapacity();


            poseStack.pushPose();
            poseStack.translate(0.55, -0.38, 0.55);
            poseStack.translate(0.0, 0.4, 0.0);
            RenderFluidBlock.renderFluid(fluidStack, bufferSource, te.getLevel(), te.getPos(), poseStack, scale * 0.5f, 0.45f, 0);
            poseStack.popPose();
        }

    }
}
