package com.denfop.render.panel;

import com.denfop.Constants;
import com.denfop.api.solar.EnumTypeParts;
import com.denfop.api.solar.ISolarItem;
import com.denfop.blockentity.panels.entity.BlockEntityMiniPanels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class TileEntityMiniPanelRender implements BlockEntityRenderer<BlockEntityMiniPanels> {

    private static final Map<Integer, ModelMiniPanelGlass> panelModels = new HashMap<>();
    private static final Map<Integer, BottomModel> bottomModels = new HashMap<>();
    private static final ResourceLocation bottomTextures = ResourceLocation.tryBuild(
            Constants.TEXTURES,
            "textures/block/admsp_bottom.png"
    );
    private static final ModelMiniPanelGlass bonusPanel = new ModelMiniPanelGlass(10);
    private static final BottomModel bonusBottom = new BottomModel(10);

    public TileEntityMiniPanelRender(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(
            BlockEntityMiniPanels te,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    ) {
        poseStack.pushPose();
        panelModels.clear();
        if (te.getBonus(EnumTypeParts.GENERATION) == 0) {
            for (int i = 0; i < 9; i++) {
                ItemStack stack = te.invSlotGlass.get(i);
                if (stack.isEmpty()) {
                    continue;
                }

                ModelMiniPanelGlass model = panelModels.computeIfAbsent(i, ModelMiniPanelGlass::new);
                BottomModel model1 = bottomModels.computeIfAbsent(i, BottomModel::new);

                ResourceLocation glassTexture = ((ISolarItem) stack.getItem()).getResourceLocation(0);

                VertexConsumer glassConsumer = bufferSource.getBuffer(RenderType.entitySolid(glassTexture));
                model.renderToBuffer(poseStack, glassConsumer, packedLight, packedOverlay, 0xFFFFFFFF);

                VertexConsumer bottomConsumer = bufferSource.getBuffer(RenderType.entitySolid(bottomTextures));
                model1.renderToBuffer(poseStack, bottomConsumer, packedLight, packedOverlay, 0xFFFFFFFF);
            }
        } else {
            ItemStack stack = te.invSlotGlass.get(0);
            ResourceLocation glassTexture = ((ISolarItem) stack.getItem()).getResourceLocation(stack.getDamageValue());

            VertexConsumer glassConsumer = bufferSource.getBuffer(RenderType.entitySolid(glassTexture));
            bonusPanel.renderToBuffer(poseStack, glassConsumer, packedLight, packedOverlay, 0xFFFFFFFF);

            VertexConsumer bottomConsumer = bufferSource.getBuffer(RenderType.entitySolid(bottomTextures));
            bonusBottom.renderToBuffer(poseStack, bottomConsumer, packedLight, packedOverlay, 0xFFFFFFFF);
        }

        poseStack.popPose();
    }
}
