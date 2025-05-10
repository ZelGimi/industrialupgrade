package com.denfop.render.transport;

import com.denfop.tiles.transport.tiles.TileEntityMultiCable;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;

public class TileEntityCableRenderer implements BlockEntityRenderer<TileEntityMultiCable> {

    private final BlockEntityRendererProvider.Context context;

    public TileEntityCableRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(TileEntityMultiCable te, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        DataCable data = te.dataCable;
        if (data == null) {
            data = new DataCable(te.connectivity, ItemStack.EMPTY, null);
            te.dataCable = data;
        }

        poseStack.pushPose();

        poseStack.translate(0.0D, 0.0D, 0.0D);

        if (te.stackFacade != null && !te.stackFacade.isEmpty()) {
            if (data.getItemStack().isEmpty() || !ItemStack.isSameItemSameTags(data.getItemStack(), te.stackFacade)) {
                data.setItemStack(te.stackFacade);
                data.setBakedModel(Minecraft.getInstance().getItemRenderer().getModel(te.stackFacade, te.getLevel(), null, 0));
            }

            renderBlock(data, poseStack, bufferSource, packedLight);
        }

        poseStack.popPose();
    }

    private void renderBlock(DataCable item, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.scale(2f, 2f, 2f);

        Minecraft.getInstance().getItemRenderer().render(
                item.getItemStack(),
                ItemTransforms.TransformType.FIXED,
                false,
                poseStack,
                bufferSource,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                item.getBakedModel()
        );
        poseStack.popPose();
    }
}
