package com.denfop.render.macerator;

import com.denfop.tiles.mechanism.TileEntityMacerator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

import static net.minecraft.world.item.ItemDisplayContext.GROUND;

public class TileEntityRenderMacerator implements BlockEntityRenderer<TileEntityMacerator> {

    public TileEntityRenderMacerator(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(TileEntityMacerator tile, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int combinedLight, int combinedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        ItemStack input = tile.inputSlotA.get(0);
        if (!input.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.55, tile.facing == 4 || tile.facing == 5 ? 0.31 : 0.3);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));

            itemRenderer.renderStatic(
                    input,
                   GROUND,
                    combinedLight,
                    combinedOverlay,
                    poseStack,
                    buffer,tile.getLevel(),
                    0
            );
            poseStack.popPose();
        }

        ItemStack output = tile.outputSlot.get(0);
        if (!output.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.55, 0.4);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));

            itemRenderer.renderStatic(
                    output,
                    GROUND,
                    combinedLight,
                    combinedOverlay,
                    poseStack,
                    buffer,tile.getLevel(),
                    0
            );
            poseStack.popPose();
        }
    }
}
