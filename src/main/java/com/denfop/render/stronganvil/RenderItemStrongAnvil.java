package com.denfop.render.stronganvil;

import com.denfop.blockentity.base.BlockEntityStrongAnvil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

public class RenderItemStrongAnvil implements BlockEntityRenderer<BlockEntityStrongAnvil> {


    private final ItemRenderer itemRenderer;

    public RenderItemStrongAnvil(BlockEntityRendererProvider.Context p_173636_) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(BlockEntityStrongAnvil tile, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ItemStack itemStack = tile.inputSlotA.get(0);
        if (!itemStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 1, 0.3);

            poseStack.mulPose(com.mojang.math.Vector3f.XP.rotationDegrees(90));
            itemRenderer.renderStatic(itemStack, net.minecraft.client.renderer.block.model.ItemTransforms.TransformType.GROUND,
                    combinedLight, combinedOverlay, poseStack, buffer, 0);
            if (tile.facing == 5 || tile.facing == 4) {
                poseStack.translate(0, -1, -0.05);

            } else {
                poseStack.translate(-1, 0, -0.05);
            }
            for (int i = 0; i < itemStack.getCount() - 1; i++) {
                poseStack.translate(0, 0, -0.0075);
                itemRenderer.renderStatic(itemStack, net.minecraft.client.renderer.block.model.ItemTransforms.TransformType.GROUND,
                        combinedLight, combinedOverlay, poseStack, buffer, 0);
            }
            poseStack.popPose();
        }


        ItemStack outputStack = tile.outputSlot.get(0);
        if (!outputStack.isEmpty()) {
            poseStack.pushPose();

            poseStack.translate(1.3, 1.0, 0.4);
            poseStack.mulPose(com.mojang.math.Vector3f.XP.rotationDegrees(90));
            if (tile.facing == 5 || tile.facing == 4) {
                poseStack.translate(-0.75, 1, 0.0);

            } else {
                poseStack.translate(0, 0, 0.0);
            }

            for (int i = 0; i < outputStack.getCount(); i++) {
                poseStack.translate(0, 0, -0.0075);
                itemRenderer.renderStatic(outputStack, net.minecraft.client.renderer.block.model.ItemTransforms.TransformType.GROUND,
                        combinedLight, combinedOverlay, poseStack, buffer, 0);

            }


            poseStack.popPose();
        }
    }
}
