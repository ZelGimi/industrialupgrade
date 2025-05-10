package com.denfop.render.pump;

import com.denfop.tiles.mechanism.TileEntityPrimalPump;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;

public class TileEntityRenderPump implements BlockEntityRenderer<TileEntityPrimalPump> {


    private final BlockEntityRendererProvider.Context contex;

    public TileEntityRenderPump(BlockEntityRendererProvider.Context p_173636_) {
        this.contex = p_173636_;
    }

    @Override
    public void render(TileEntityPrimalPump tile, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int combinedOverlay) {
        if (Minecraft.getInstance().hitResult instanceof BlockHitResult hitResult
                && tile.getBlockPos().equals(hitResult.getBlockPos())) {

            poseStack.pushPose();
            poseStack.translate(0.5, 1.75, 0.5);

            Component text;
            if (tile.fluidTank.getFluid().isEmpty()) {
                text =  Component.literal("FluidTank: 0/" + tile.fluidTank.getCapacity());
            } else {
                text = Component.literal(tile.fluidTank.getFluid().getDisplayName().getString() + ": " +
                                tile.fluidTank.getFluidAmount() + "/" + tile.fluidTank.getCapacity());
            }


            renderFloatingText(text, poseStack, bufferSource, packedLight);
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
        font.drawInBatch(text, f2, (float) 0, 553648127, false, matrix4f, buffer, false, j, packedLight);
        if (true) {
            font.drawInBatch(text, f2, (float) 0, -1, false, matrix4f, buffer, false, 0, packedLight);
        }
        poseStack.popPose();
    }
}
