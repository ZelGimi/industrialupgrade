package com.denfop.render;

import com.denfop.blocks.FluidName;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

public class RenderFluidBlock {
    public static void renderFluid(FluidStack fluidStack, MultiBufferSource bufferSource, Level level, BlockPos pos, PoseStack poseStack, float scale, float scale1) {
        if (fluidStack.isEmpty())
            return;
        if (Fluids.LAVA == fluidStack.getFluid()) {
            fluidStack = new FluidStack(FluidName.fluidlava.getInstance().get(), fluidStack.getAmount());
        }
        if (Fluids.WATER == fluidStack.getFluid()) {
            fluidStack = new FluidStack(FluidName.fluidwater.getInstance().get(), fluidStack.getAmount());
        }
        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());

        ResourceLocation stillTexture = fluidTypeExtensions.getFlowingTexture(fluidStack);
        if (stillTexture == null)
            return;

        FluidState state = fluidStack.getFluid().defaultFluidState();

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
        int tintColor = fluidTypeExtensions.getTintColor(state, level, pos);

        poseStack.translate(-0.5, 0, -0.5);
        poseStack.scale(2f * scale1, scale, 2f * scale1);
        float height = 1f;
        MultiBufferSource pBuffer = bufferSource;
        VertexConsumer builder = pBuffer.getBuffer(ItemBlockRenderTypes.getRenderLayer(state));
        int i;
        if (level != null) {
            i = LevelRenderer.getLightColor(level, pos);
        } else {
            i = 15728880;
        }
        drawQuad(builder, poseStack, 0.25f, height, 0.25f, 0.75f, height, 0.75f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), i, tintColor);

        drawQuad(builder, poseStack, 0.25f, 0, 0.25f, 0.75f, height, 0.25f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), i, tintColor);

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
        poseStack.translate(-1f, 0, -1.5f);
        drawQuad(builder, poseStack, 0.25f, 0, 0.75f, 0.75f, height, 0.75f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), i, tintColor);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
        poseStack.translate(-1f, 0, 0);
        drawQuad(builder, poseStack, 0.25f, 0, 0.25f, 0.75f, height, 0.25f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), i, tintColor);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YN.rotationDegrees(90));
        poseStack.translate(0, 0, -1f);
        drawQuad(builder, poseStack, 0.25f, 0, 0.25f, 0.75f, height, 0.25f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), i, tintColor);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(-0, 1.1, 1);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(180));
        drawQuad(builder, poseStack, 0.25f, height, 0.25f, 0.75f, height, 0.75f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), i, tintColor);
        drawQuad(builder, poseStack, 0.25f, 0, 0.25f, 0.75f, height, 0.25f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), i, tintColor);
        poseStack.popPose();
    }

    public static void renderFluid(FluidStack fluidStack, MultiBufferSource bufferSource, Level level, BlockPos pos, PoseStack poseStack, float scale, float scale1, float scale2) {
        if (fluidStack.isEmpty())
            return;
        if (Fluids.LAVA == fluidStack.getFluid()) {
            fluidStack = new FluidStack(FluidName.fluidlava.getInstance().get(), fluidStack.getAmount());
        }
        if (Fluids.WATER == fluidStack.getFluid()) {
            fluidStack = new FluidStack(FluidName.fluidwater.getInstance().get(), fluidStack.getAmount());
        }
        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());

        ResourceLocation stillTexture = fluidTypeExtensions.getFlowingTexture(fluidStack);
        if (stillTexture == null)
            return;

        FluidState state = fluidStack.getFluid().defaultFluidState();

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
        int tintColor = fluidTypeExtensions.getTintColor(state, level, pos);

        poseStack.translate(-0.5, 0, -0.5);
        poseStack.scale(2f * scale1, scale, 2f * scale2);
        float height = 1f;
        MultiBufferSource pBuffer = bufferSource;
        VertexConsumer builder = pBuffer.getBuffer(ItemBlockRenderTypes.getRenderLayer(state));
        int i;
        if (level != null) {
            i = LevelRenderer.getLightColor(level, pos);
        } else {
            i = 15728880;
        }
        drawQuad(builder, poseStack, 0.25f, height, 0.25f, 0.75f, height, 0.75f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), i, tintColor);

        drawQuad(builder, poseStack, 0.25f, 0, 0.25f, 0.75f, height, 0.25f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), i, tintColor);

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
        poseStack.translate(-1f, 0, -1.5f);
        drawQuad(builder, poseStack, 0.25f, 0, 0.75f, 0.75f, height, 0.75f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), i, tintColor);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
        poseStack.translate(-1f, 0, 0);
        drawQuad(builder, poseStack, 0.25f, 0, 0.25f, 0.75f, height, 0.25f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), i, tintColor);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YN.rotationDegrees(90));
        poseStack.translate(0, 0, -1f);
        drawQuad(builder, poseStack, 0.25f, 0, 0.25f, 0.75f, height, 0.25f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), i, tintColor);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(-0, 1.1, 1);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(180));
        drawQuad(builder, poseStack, 0.25f, height, 0.25f, 0.75f, height, 0.75f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), i, tintColor);
        drawQuad(builder, poseStack, 0.25f, 0, 0.25f, 0.75f, height, 0.25f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), i, tintColor);
        poseStack.popPose();
    }

    private static void drawVertex(VertexConsumer builder, PoseStack poseStack, float x, float y, float z, float u, float v, int packedLight, int color) {
        builder.vertex(poseStack.last().pose(), x, y, z)
                .color(color)
                .uv(u, v)
                .uv2(packedLight)
                .normal(1, 0, 0)
                .endVertex();
    }

    private static void drawQuad(VertexConsumer builder, PoseStack poseStack, float x0, float y0, float z0, float x1, float y1, float z1, float u0, float v0, float u1, float v1, int packedLight, int color) {
        drawVertex(builder, poseStack, x0, y0, z0, u0, v0, packedLight, color);
        drawVertex(builder, poseStack, x0, y1, z1, u0, v1, packedLight, color);
        drawVertex(builder, poseStack, x1, y1, z1, u1, v1, packedLight, color);
        drawVertex(builder, poseStack, x1, y0, z0, u1, v0, packedLight, color);
    }
}
