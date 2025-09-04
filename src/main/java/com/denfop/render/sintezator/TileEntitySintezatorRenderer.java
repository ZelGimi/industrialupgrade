package com.denfop.render.sintezator;

import com.denfop.blockentity.base.BlockEntitySintezator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileEntitySintezatorRenderer implements BlockEntityRenderer<BlockEntitySintezator> {

    private final BlockRenderDispatcher blockRenderer;
    private final Map<BlockState, BakedModel> modelCache = new HashMap<>();

    public TileEntitySintezatorRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
    }

    @Override
    public void render(BlockEntitySintezator tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        poseStack.pushPose();

        for (int i = 0; i < 9; i++) {
            ItemStack stack = tile.inputslot.get(i);
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                BlockState state = block.defaultBlockState();

                poseStack.pushPose();
                poseStack.translate(0.25 + 0.189 * (i % 3), 0.3125, 0.25 + 0.189 * (i / 3));
                poseStack.scale(0.125f, 0.125f, 0.125f);

                BakedModel model = modelCache.computeIfAbsent(state, blockRenderer::getBlockModel);
                for (Direction direction : Direction.values()) {
                    renderQuads(poseStack, bufferSource, model.getQuads(state, direction, tile.getLevel().getRandom()), combinedLight, combinedOverlay);
                }
                renderQuads(poseStack, bufferSource, model.getQuads(state, null, tile.getLevel().getRandom()), combinedLight, combinedOverlay);

                poseStack.popPose();
            }
        }

        poseStack.popPose();
    }

    private void renderQuads(PoseStack poseStack, MultiBufferSource bufferSource, List<BakedQuad> quads, int combinedLight, int combinedOverlay) {
        var buffer = bufferSource.getBuffer(RenderType.solid());
        var pose = poseStack.last();
        for (BakedQuad quad : quads) {
            buffer.putBulkData(pose, quad, 1.0f, 1.0f, 1.0f, 1.0f, combinedLight, OverlayTexture.NO_OVERLAY);
        }
    }
}
