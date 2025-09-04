package com.denfop.render.pump;

import com.denfop.blockentity.mechanism.BlockEntityPrimalPump;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import org.joml.Matrix4f;

public class TileEntityRenderPump implements BlockEntityRenderer<BlockEntityPrimalPump> {


    private final BlockEntityRendererProvider.Context contex;

    public TileEntityRenderPump(BlockEntityRendererProvider.Context p_173636_) {
        this.contex = p_173636_;
    }

    @Override
    public void render(BlockEntityPrimalPump tile, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int combinedOverlay) {
        if (Minecraft.getInstance().hitResult instanceof BlockHitResult hitResult
                && tile.getBlockPos().equals(hitResult.getBlockPos())) {

            poseStack.pushPose();
            poseStack.translate(0.5, 1.75, 0.5);

            Component text;
            if (tile.fluidTank.getFluid().isEmpty()) {
                text = Component.literal("FluidTank: 0/" + tile.fluidTank.getCapacity());
            } else {
                text = Component.literal(tile.fluidTank.getFluid().getHoverName().getString() + ": " +
                        tile.fluidTank.getFluidAmount() + "/" + tile.fluidTank.getCapacity());
            }


            renderFloatingText(text, poseStack, bufferSource, packedLight);
            poseStack.popPose();
        }
    }

    private void renderFloatingText(Component text, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        boolean flag = true;
        int i = 0;
        poseStack.pushPose();
        poseStack.translate(0, 0, 0);
        poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        poseStack.scale(0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = poseStack.last().pose();
        float f = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
        int j = (int) (f * 255.0F) << 24;
        Font font = Minecraft.getInstance().font;
        float f1 = (float) (-font.width(text) / 2);
        font.drawInBatch(
                text, f1, (float) i, 553648127, false, matrix4f, buffer, Font.DisplayMode.SEE_THROUGH, j, packedLight
        );
        if (flag) {
            font.drawInBatch(text, f1, (float) i, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight);
        }

        poseStack.popPose();
    }
}
