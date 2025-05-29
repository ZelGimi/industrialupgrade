package com.denfop.render.primal_laser_polisher;

import com.denfop.tiles.mechanism.TileEntityPrimalLaserPolisher;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

import static net.minecraft.world.item.ItemDisplayContext.GROUND;

public class PrimalLaserPolisherRender implements BlockEntityRenderer<TileEntityPrimalLaserPolisher> {
    private final BlockEntityRendererProvider.Context contex;

    public PrimalLaserPolisherRender(BlockEntityRendererProvider.Context p_173636_) {
        this.contex = p_173636_;
    }

    @Override
    public void render(TileEntityPrimalLaserPolisher tile, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int combinedOverlay) {
        ItemStack itemstack = tile.inputSlotA.get(0);
        final ItemStack itemstack1 = tile.outputSlot.get(0);
        ItemStack itemStack = itemstack1.isEmpty() ? itemstack : itemstack1;
        ItemRenderer itemRenderer = contex.getItemRenderer();
        if (!itemStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.88, 0.4);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            itemRenderer.renderStatic(itemStack, GROUND,
                    packedLight, combinedOverlay, poseStack, bufferSource,tile.getLevel(), 0);
            poseStack.popPose();
        }
    }
}
