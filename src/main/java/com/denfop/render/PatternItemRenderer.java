package com.denfop.render;

import com.denfop.items.storage.ItemPattern;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

public class PatternItemRenderer extends BlockEntityWithoutLevelRenderer {

    public static final PatternItemRenderer INSTANCE = new PatternItemRenderer(
            Minecraft.getInstance().getBlockEntityRenderDispatcher(),
            Minecraft.getInstance().getEntityModels()
    );

    public PatternItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
    }

    @Override
    public void renderByItem(ItemStack stack,
                             ItemDisplayContext displayContext,
                             PoseStack poseStack,
                             MultiBufferSource buffer,
                             int packedLight,
                             int packedOverlay) {

        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();

        if (!(stack.getItem() instanceof ItemPattern patternItem)) {
            return;
        }

        ItemStack renderStack = ItemStack.EMPTY;
        FluidStack renderFluid = FluidStack.EMPTY;

        if (Screen.hasShiftDown()) {
            ItemStack previewItem = patternItem.getPreviewStack(stack, mc.level.registryAccess());
            if (!previewItem.isEmpty() && !(previewItem.getItem() instanceof ItemPattern)) {
                renderStack = previewItem;
            } else {
                FluidStack previewFluid = patternItem.getPreviewFluid(stack, mc.level.registryAccess());
                if (!previewFluid.isEmpty()) {
                    renderFluid = previewFluid.copy();
                }
            }
        }

        if (!renderStack.isEmpty() && renderStack.getItem() != stack.getItem()) {
            BakedModel model = itemRenderer.getModel(renderStack, mc.level, null, 0);

            poseStack.pushPose();
            Lighting.setupForFlatItems();
            poseStack.translate(0.5F, 0.5F, 0.0F);

            itemRenderer.render(
                    renderStack,
                    ItemDisplayContext.GUI,
                    false,
                    poseStack,
                    buffer,
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    model
            );
            poseStack.popPose();

            int count = renderStack.getCount();
            if (count > 1 && displayContext != ItemDisplayContext.GROUND) {
                drawTextOverlay(String.valueOf(count), mc, poseStack, buffer, packedLight, false);
            }
            return;
        }

        if (!renderFluid.isEmpty()) {
            poseStack.pushPose();
            Lighting.setupForFlatItems();


            poseStack.translate(0.0F, 1.0F, 0.01F);
            poseStack.scale(1.0F, -1.0F, 1.0F);

            renderFluidInGui(renderFluid, poseStack, buffer, packedLight);
            poseStack.popPose();
            if (displayContext != ItemDisplayContext.GROUND)
                drawTextOverlay(renderFluid.getAmount() + "mb", mc, poseStack, buffer, packedLight, true);
        }
    }

    private void drawTextOverlay(String text,
                                 Minecraft mc,
                                 PoseStack poseStack,
                                 MultiBufferSource buffer,
                                 int packedLight, boolean fluid) {
        Font font = mc.font;

        poseStack.pushPose();
        poseStack.translate(0.9, 0.25, 1);
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        poseStack.scale(0.025F, 0.025F, 1F);

        Matrix4f matrix4f = poseStack.last().pose();
        float bgOpacity = mc.options.getBackgroundOpacity(0);
        int bgColor = (int) (bgOpacity * 255.0F) << 24;
        float x = (float) (-font.width(text) / 2);
        if (fluid) {
            x -= 13.5f;
        }
        font.drawInBatch(text, x, 0.0F, 553648127, false, matrix4f, buffer, Font.DisplayMode.NORMAL, bgColor, packedLight);
        font.drawInBatch(text, x, 0.0F, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight);

        poseStack.popPose();
    }

    private void renderFluidInGui(FluidStack fluidStack,
                                  PoseStack poseStack,
                                  MultiBufferSource buffer,
                                  int packedLight) {
        Minecraft mc = Minecraft.getInstance();

        IClientFluidTypeExtensions fluidExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        ResourceLocation stillTexture = fluidExtensions.getStillTexture(fluidStack);

        if (stillTexture == null) {
            return;
        }

        TextureAtlasSprite sprite = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
        int tint = fluidExtensions.getTintColor(fluidStack);

        int a = (tint >> 24) & 0xFF;
        int r = (tint >> 16) & 0xFF;
        int g = (tint >> 8) & 0xFF;
        int b = tint & 0xFF;

        if (a == 0) {
            a = 255;
        }

        Matrix4f matrix = poseStack.last().pose();

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        BufferBuilder buffer1 = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        addVertex(buffer1, matrix, 0, 0, 0, sprite.getU0(), sprite.getV0(), r, g, b, a, packedLight);
        addVertex(buffer1, matrix, 0, 16, 0, sprite.getU0(), sprite.getV1(), r, g, b, a, packedLight);
        addVertex(buffer1, matrix, 16, 16, 0, sprite.getU1(), sprite.getV1(), r, g, b, a, packedLight);
        addVertex(buffer1, matrix, 16, 0, 0, sprite.getU1(), sprite.getV0(), r, g, b, a, packedLight);
        BufferUploader.drawWithShader(buffer1.buildOrThrow());
    }

    private void addVertex(VertexConsumer consumer,
                           Matrix4f matrix,
                           float x, float y, float z,
                           float u, float v,
                           int r, int g, int b, int a,
                           int packedLight) {
        consumer.addVertex(matrix, x / 16.0F, y / 16.0F, z)
                .setUv(u, v)
                .setColor(r, g, b, a);
    }
}