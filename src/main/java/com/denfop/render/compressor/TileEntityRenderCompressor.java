package com.denfop.render.compressor;


import com.denfop.tiles.mechanism.TileEntityCompressor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class TileEntityRenderCompressor implements BlockEntityRenderer<TileEntityCompressor> {
    private final BlockEntityRendererProvider.Context contex;

    public TileEntityRenderCompressor(BlockEntityRendererProvider.Context p_173636_) {
        this.contex = p_173636_;
    }

    @Override
    public void render(TileEntityCompressor te, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int combinedOverlay) {
        ItemStack itemstack = te.outputSlot.get(0);
        if (!itemstack.isEmpty()) {
            poseStack.pushPose();
            if ((itemstack.getItem() instanceof BlockItem)) {
                if (te.facing == 4 || te.facing == 5) {
                    poseStack.translate(0.5, 0.41, 0.31);
                } else {
                    poseStack.translate(0.5, 0.41, 0.3);
                }
            } else {
                poseStack.translate(0.5, 0.42, 0.37501);
            }

            poseStack.mulPose(com.mojang.math.Vector3f.XP.rotationDegrees(90));
            contex.getItemRenderer().renderStatic(itemstack, net.minecraft.client.renderer.block.model.ItemTransforms.TransformType.GROUND,
                    packedLight, combinedOverlay, poseStack, bufferSource, 0);
            poseStack.popPose();
        }
        itemstack = te.inputSlotA.get(0);
        if (!itemstack.isEmpty()) {
            poseStack.pushPose();
            if ((itemstack.getItem() instanceof BlockItem)) {
                if (te.facing == 4 || te.facing == 5) {
                    poseStack.translate(0.5, 0.41, 0.31);
                } else {
                    poseStack.translate(0.5, 0.41, 0.3);
                }
            } else {
                poseStack.translate(0.5, 0.42, 0.37501);
            }

            poseStack.mulPose(com.mojang.math.Vector3f.XP.rotationDegrees(90));
            contex.getItemRenderer().renderStatic(itemstack, net.minecraft.client.renderer.block.model.ItemTransforms.TransformType.GROUND,
                    packedLight, combinedOverlay, poseStack, bufferSource, 0);
            poseStack.popPose();
        }
    }
}
