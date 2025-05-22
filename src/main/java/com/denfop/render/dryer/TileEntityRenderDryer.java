package com.denfop.render.dryer;

import com.denfop.render.RenderFluidBlock;
import com.denfop.tiles.mechanism.TileEntityDryer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileEntityRenderDryer implements BlockEntityRenderer<TileEntityDryer> {
    private final BlockEntityRendererProvider.Context contex;

    public TileEntityRenderDryer(BlockEntityRendererProvider.Context p_173636_) {
        this.contex = p_173636_;
    }

    @Override
    public void render(TileEntityDryer te, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int combinedOverlay) {
        FluidTank tank = te.fluidTank1;
        FluidStack fluidStack = tank.getFluid();
        if (!fluidStack.isEmpty()) {
            final float scale = (te.fluidTank1.getFluidAmount()) * 1F / te.fluidTank1.getCapacity();

            poseStack.pushPose();
            poseStack.translate(0.17, 0, 0.17);
            poseStack.translate(0.0, 1.2, 0.0);
            RenderFluidBlock.renderFluid(fluidStack, bufferSource, te.getLevel(), te.getPos(), poseStack, scale * 0.4f, 0.83f);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0.1 + 0.6, 0, 0.1 + 0.6);
            poseStack.translate(0.0, 0.7, 0.0);
            RenderFluidBlock.renderFluid(fluidStack, bufferSource, te.getLevel(), te.getPos(), poseStack, 0.4f, 0.3f);
            poseStack.popPose();
        }
        ItemStack itemstack = te.outputSlot.get(0);
        if (!itemstack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.65, 0.4);

            poseStack.mulPose(com.mojang.math.Vector3f.XP.rotationDegrees(90));
            contex.getItemRenderer().renderStatic(itemstack, net.minecraft.client.renderer.block.model.ItemTransforms.TransformType.GROUND,
                    packedLight, combinedOverlay, poseStack, bufferSource, 0);
            poseStack.popPose();
        }
    }
}
