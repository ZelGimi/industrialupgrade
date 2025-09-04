package com.denfop.render.fluidheater;

import com.denfop.blockentity.mechanism.BlockEntityPrimalFluidHeater;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import org.joml.Matrix4f;

public class TileEntityRenderFluidHeater implements BlockEntityRenderer<BlockEntityPrimalFluidHeater> {


    private final BlockEntityRendererProvider.Context contex;

    public TileEntityRenderFluidHeater(BlockEntityRendererProvider.Context p_173636_) {
        this.contex = p_173636_;
    }

    @Override
    public void render(BlockEntityPrimalFluidHeater tile, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int combinedOverlay) {
        if (Minecraft.getInstance().hitResult instanceof BlockHitResult hitResult
                && tile.getBlockPos().equals(hitResult.getBlockPos())) {

            poseStack.pushPose();
            poseStack.translate(0.5, 1.75, 0.5);

            Component text = (tile.fluidTank1.isEmpty()) ?
                    Component.literal("FluidTank: 0/" + tile.fluidTank1.getCapacity()) :
                    Component.literal(tile.fluidTank1.getFluid().getDisplayName().getString() + ": " +
                            tile.fluidTank1.getFluidAmount() + "/" + tile.fluidTank1.getCapacity());

            Component text1 = (tile.fluidTank2.isEmpty()) ?
                    Component.literal("FluidTank: 0/" + tile.fluidTank2.getCapacity()) :
                    Component.literal(tile.fluidTank2.getFluid().getDisplayName().getString() + ": " +
                            tile.fluidTank2.getFluidAmount() + "/" + tile.fluidTank2.getCapacity());

            Component text2 = Component.literal(String.format("%d", (int) (tile.getProgress() * 100)) + "%");

            renderFloatingText(text, poseStack, bufferSource, packedLight);
            poseStack.translate(0, -0.25, 0);
            renderFloatingText(text1, poseStack, bufferSource, packedLight);
            poseStack.translate(0, -0.25, 0);
            renderFloatingText(text2, poseStack, bufferSource, packedLight);
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
