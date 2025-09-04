package com.denfop.render.rolling;

import com.denfop.blockentity.mechanism.BlockEntityRollingMachine;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static net.minecraft.world.item.ItemDisplayContext.GROUND;

public class RenderItemRolling implements BlockEntityRenderer<BlockEntityRollingMachine> {
    private final ItemRenderer itemRenderer;

    public RenderItemRolling(BlockEntityRendererProvider.Context p_173636_) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(
            BlockEntityRollingMachine tile,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    ) {
        ItemStack itemstack = tile.inputSlotA.get(0);
        if (!itemstack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0, 0, 0);

            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();

            if (!tile.getActive()) {
                poseStack.translate(0.5, 0.8, 0.4);
                if (tile.getFacing() == Direction.EAST || tile.getFacing() == Direction.WEST) {
                    poseStack.translate(0, 0, 0.02);
                }
                poseStack.scale(0.65f, 0.65f, 0.65f);
            } else {
                poseStack.translate(0.49, 0.8, 0.3);
                if (tile.getFacing() == Direction.EAST || tile.getFacing() == Direction.WEST) {
                    poseStack.translate(0, 0, 0.15);
                    poseStack.scale(1.5f, 0.4f, 0.4f);
                } else {
                    poseStack.scale(0.4f, 0.4f, 1.5f);
                }
            }

            poseStack.mulPose(Axis.XP.rotationDegrees(90));

            itemRenderer.renderStatic(itemstack, GROUND, packedLight, packedOverlay, poseStack, bufferSource, tile.getLevel(), 0);
            poseStack.popPose();
        }

        if (!itemstack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0, 0, 0);

            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();

            if (itemstack.getCount() > 1) {
                if (tile.getFacing() == Direction.EAST || tile.getFacing() == Direction.WEST) {
                    poseStack.translate(0.5, 1.02, -0.1);
                    poseStack.mulPose(Axis.XP.rotationDegrees(90));
                } else {
                    poseStack.translate(0.1, 1.02, 0.5);
                    poseStack.mulPose(Axis.XP.rotationDegrees(90));
                    poseStack.mulPose(Axis.ZP.rotationDegrees(90));
                }

                for (int i = 0; i < itemstack.getCount() - 1; i++) {
                    poseStack.translate(0, 0, -0.0075);
                    itemRenderer.renderStatic(itemstack, GROUND, packedLight, packedOverlay, poseStack, bufferSource, tile.getLevel(), 0);
                }
            }

            poseStack.popPose();
        }

        ItemStack itemstack1 = tile.outputSlot.get(0);
        if (!itemstack1.isEmpty() && itemstack1.getCount() > 0) {
            poseStack.pushPose();
            poseStack.translate(0, 0, 0);

            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();

            if (tile.getFacing() != Direction.EAST && tile.getFacing() != Direction.WEST) {
                poseStack.translate(1.07, 1.01, 0.5);
            } else {
                poseStack.translate(0.49, 1.01, 0.81);
            }

            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            if (tile.getFacing() != Direction.EAST && tile.getFacing() != Direction.WEST) {
                poseStack.mulPose(Axis.ZP.rotationDegrees(90));
            }

            for (int i = 0; i <= itemstack1.getCount(); i++) {
                poseStack.translate(0, 0, -0.0075);
                itemRenderer.renderStatic(itemstack1, GROUND, packedLight, packedOverlay, poseStack, bufferSource, tile.getLevel(), 0);
            }

            poseStack.popPose();
        }
    }
}
