package com.denfop.render;

import com.denfop.blockentity.mechanism.BlockEntityPrimalGasChamber;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.joml.Matrix4f;

public class TileEntityRenderGasChamber implements BlockEntityRenderer<BlockEntityPrimalGasChamber> {
    private final BlockEntityRendererProvider.Context contex;

    public TileEntityRenderGasChamber(BlockEntityRendererProvider.Context p_173636_) {
        this.contex = p_173636_;
    }

    @Override
    public void render(BlockEntityPrimalGasChamber tile, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int combinedOverlay) {
        renderTanks(tile, poseStack, bufferSource, packedLight, combinedOverlay);
        if (Minecraft.getInstance().hitResult instanceof BlockHitResult hitResult
                && tile.getBlockPos().equals(hitResult.getBlockPos())) {

            poseStack.pushPose();
            poseStack.translate(0.5, 3.25, 0.5);

            Component text = (tile.fluidTank1.isEmpty()) ?
                    Component.literal("FluidTank: 0/" + tile.fluidTank1.getCapacity()) :
                    Component.literal(tile.fluidTank1.getFluid().getDisplayName().getString() + ": " +
                            tile.fluidTank1.getFluidAmount() + "/" + tile.fluidTank1.getCapacity());

            Component text1 = (tile.fluidTank2.isEmpty()) ?
                    Component.literal("FluidTank: 0/" + tile.fluidTank2.getCapacity()) :
                    Component.literal(tile.fluidTank2.getFluid().getDisplayName().getString() + ": " +
                            tile.fluidTank2.getFluidAmount() + "/" + tile.fluidTank2.getCapacity());
            Component text2 = (tile.fluidTank3.isEmpty()) ?
                    Component.literal("FluidTank: 0/" + tile.fluidTank3.getCapacity()) :
                    Component.literal(tile.fluidTank3.getFluid().getDisplayName().getString() + ": " +
                            tile.fluidTank3.getFluidAmount() + "/" + tile.fluidTank3.getCapacity());

            Component text3 = Component.literal(String.format("%d", (int) (tile.getProgress() * 100)) + "%");

            renderFloatingText(text, poseStack, bufferSource, packedLight);
            poseStack.translate(0, -0.25, 0);
            renderFloatingText(text1, poseStack, bufferSource, packedLight);
            poseStack.translate(0, -0.25, 0);
            renderFloatingText(text2, poseStack, bufferSource, packedLight);
            poseStack.translate(0, -0.25, 0);
            renderFloatingText(text3, poseStack, bufferSource, packedLight);
            poseStack.popPose();
        }
    }

    private void renderTanks(BlockEntityPrimalGasChamber tile, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int combinedOverlay) {
        if (!tile.fluidTank1.getFluid().isEmpty() && tile.fluidTank1.getFluid().getFluid() != null & tile.fluidTank1
                .getFluid()
                .getFluid() != Fluids.EMPTY) {
            poseStack.pushPose();
            poseStack.translate(0.175, 0, 0.175);
            if (tile.facing == 2 || tile.facing == 3) {
                poseStack.translate(0.67 + 0.125, +1, 0.022 - 0.125);
            } else {
                poseStack.translate(0.022 - 0.125, +1, 0.67 + 0.125);
            }
            float scale = tile.fluidTank1.getFluidAmount() * 1F / tile.fluidTank1.getCapacity();
            scale *= 0.94f;
            if (tile.facing == 2 || tile.facing == 3) {
                RenderFluidBlock.renderFluid(tile.fluidTank1.getFluid(), bufferSource, tile.getLevel(), tile.getPos(), poseStack, scale, 0.38f, 0.95f);
            } else {
                RenderFluidBlock.renderFluid(tile.fluidTank1.getFluid(), bufferSource, tile.getLevel(), tile.getPos(), poseStack, scale, 0.95f, 0.38f);

            }
            poseStack.popPose();
        }
        if (!tile.fluidTank2.getFluid().isEmpty() && tile.fluidTank2.getFluid().getFluid() != null & tile.fluidTank2
                .getFluid()
                .getFluid() != Fluids.EMPTY) {
            poseStack.pushPose();
            poseStack.translate(0.175, 0, 0.175);
            if (tile.facing == 2 || tile.facing == 3) {
                poseStack.translate(0 + 0.125, +1, 0.022 - 0.125);
            } else {
                poseStack.translate(0.022 - 0.125, +1, 0 + 0.125);
            }
            float scale = tile.fluidTank2.getFluidAmount() * 1F / tile.fluidTank2.getCapacity();
            scale *= 0.94f;
            if (tile.facing == 2 || tile.facing == 3) {
                RenderFluidBlock.renderFluid(tile.fluidTank2.getFluid(), bufferSource, tile.getLevel(), tile.getPos(), poseStack, scale, 0.38f, 0.95f);
            } else {
                RenderFluidBlock.renderFluid(tile.fluidTank2.getFluid(), bufferSource, tile.getLevel(), tile.getPos(), poseStack, scale, 0.95f, 0.38f);

            }
            poseStack.popPose();
        }
        if (!tile.fluidTank3.getFluid().isEmpty() && tile.fluidTank3.getFluid().getFluid() != null & tile.fluidTank3
                .getFluid()
                .getFluid() != Fluids.EMPTY) {
            poseStack.pushPose();
            poseStack.translate(0.0251, 0, 0.0251);

            float scale = tile.fluidTank3.getFluidAmount() * 1F / tile.fluidTank3.getCapacity();
            scale *= 0.94f;
            if (tile.facing == 2 || tile.facing == 3) {
                RenderFluidBlock.renderFluid(tile.fluidTank3.getFluid(), bufferSource, tile.getLevel(), tile.getPos(), poseStack, scale, 0.95f, 0.95f);
            } else {
                RenderFluidBlock.renderFluid(tile.fluidTank3.getFluid(), bufferSource, tile.getLevel(), tile.getPos(), poseStack, scale, 0.95f, 0.95f);

            }
            poseStack.popPose();
        }
    }

    private void renderFloatingText(Component text, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(contex.getEntityRenderer().cameraOrientation());
        poseStack.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = poseStack.last().pose();
        float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
        int j = (int) (f1 * 255.0F) << 24;
        Font font = contex.getFont();
        float f2 = (float) (-font.width(text) / 2);
        font.drawInBatch(text, f2, (float) 0, 553648127, false, matrix4f, buffer, Font.DisplayMode.NORMAL, j, packedLight);
        if (true) {
            font.drawInBatch(text, f2, (float) 0, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight);
        }
        poseStack.popPose();
    }
}
