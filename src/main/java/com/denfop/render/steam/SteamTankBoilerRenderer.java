package com.denfop.render.steam;

import com.denfop.render.RenderFluidBlock;
import com.denfop.tiles.mechanism.steamboiler.TileEntitySteamTankBoiler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.fluids.FluidStack;

public class SteamTankBoilerRenderer implements BlockEntityRenderer<TileEntitySteamTankBoiler> {

    private final BlockEntityRendererProvider.Context context;

    public SteamTankBoilerRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(
            TileEntitySteamTankBoiler tile,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int combinedLight,
            int combinedOverlay
    ) {
        FluidStack fluidStack = tile.getTank().getFluid();
        if (fluidStack.isEmpty()) {
            return;
        }
        poseStack.pushPose();
        float scale = fluidStack.getAmount() / (float) tile.getTank().getCapacity();
        poseStack.translate(0.04+ 0.05, 0, 0.04+ 0.05);
        poseStack.scale(0.95f, 0.95f * scale, 0.95f);
        RenderFluidBlock.renderFluid(fluidStack, buffer, tile.getLevel(), tile.getPos(), poseStack, 0.95f * scale, 0.95f,0);
        poseStack.popPose();
    }


}
