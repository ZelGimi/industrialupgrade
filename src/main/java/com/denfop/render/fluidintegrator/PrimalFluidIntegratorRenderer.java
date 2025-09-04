package com.denfop.render.fluidintegrator;

import com.denfop.blockentity.mechanism.BlockEntityPrimalFluidIntegrator;
import com.denfop.render.RenderFluidBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

import static net.minecraft.world.item.ItemDisplayContext.GROUND;

@OnlyIn(Dist.CLIENT)
public class PrimalFluidIntegratorRenderer implements BlockEntityRenderer<BlockEntityPrimalFluidIntegrator> {
    private final ItemRenderer itemRenderer;
    private final BlockEntityRendererProvider.Context contex;
    private float rotation = 0;
    private float prevRotation = 0;

    public PrimalFluidIntegratorRenderer(BlockEntityRendererProvider.Context p_173636_) {
        this.contex = p_173636_;
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(BlockEntityPrimalFluidIntegrator tile, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        renderTanks(tile, poseStack, bufferSource, packedLight, packedOverlay);

        ItemStack itemStack = tile.inputSlotA.get(0);
        if (!itemStack.isEmpty()) {
            poseStack.pushPose();

            float scale = Math.min(1.0f, tile.fluidTank1.getFluidAmount() / (float) tile.fluidTank1.getCapacity());
            Vec3 offset = getOffset(tile.getFacing(), scale);
            poseStack.translate(offset.x, offset.y, offset.z);

            if (scale > 0.1f) {
                poseStack.mulPose(Axis.XP.rotationDegrees(rotation));
                poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
                poseStack.mulPose(Axis.ZP.rotationDegrees(rotation));
            } else {
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
            }

            poseStack.scale(0.9f, 0.9f, 0.9f);

            itemRenderer.renderStatic(itemStack, GROUND,
                    packedLight, packedOverlay, poseStack, bufferSource, tile.getLevel(), 0);

            poseStack.popPose();
        }

        BlockPos cameraPos = tile.getBlockPos();
        HitResult hitResult = Minecraft.getInstance().hitResult;
        if (hitResult instanceof BlockHitResult blockHit &&
                (cameraPos.equals(blockHit.getBlockPos()) || cameraPos.above().equals(blockHit.getBlockPos()))) {

            String text = (int) (tile.getProgress() * 100) + "%";
            poseStack.pushPose();
            poseStack.translate(0.5, 1.5, 0.5);

            Component text1 =
                    Component.literal(text);

            renderFloatingText(text1, poseStack, bufferSource, packedLight);
            poseStack.translate(0, -0.25, 0);
            poseStack.popPose();
        }

        ItemStack outputStack = tile.outputSlot.get(0);
        if (!outputStack.isEmpty()) {
            poseStack.pushPose();

            Vec3 outputOffset = getOutputOffset(tile.getFacing());
            poseStack.translate(outputOffset.x, outputOffset.y, outputOffset.z);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    outputStack,
                    GROUND,
                    packedLight,
                    packedOverlay,
                    poseStack,
                    bufferSource,
                    tile.getLevel(),
                    0
            );

            poseStack.popPose();
        }

        rotation = prevRotation + (rotation - prevRotation) * partialTicks;
        prevRotation = rotation;
        rotation += 0.25f;
    }

    private void renderTanks(BlockEntityPrimalFluidIntegrator tile, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (!tile.fluidTank1.getFluid().isEmpty()) {
            poseStack.pushPose();
            final float scale = tile.fluidTank1.getFluidAmount() * 0.75F / tile.fluidTank1.getCapacity();
            switch (tile.facing) {
                case 2:
                    poseStack.translate(0.44 + 0.19, 0.3, +0.19 + 0.19);
                    break;
                case 3:
                    poseStack.translate(-0.06 + 0.19, 0.3, +0.19 + 0.19);
                    break;
                case 4:
                    poseStack.translate(+0.19 + 0.19, 0.3, -0.059 + 0.19);
                    break;
                case 5:
                    poseStack.translate(0.19 + 0.19, 0.3, +0.44 + 0.19);
                    break;
            }
            RenderFluidBlock.renderFluid(tile.fluidTank1.getFluid(), bufferSource, tile.getLevel(), tile.getPos(), poseStack, scale, 0.62f, 0);
            poseStack.popPose();
        }
        if (!tile.fluidTank2.getFluid().isEmpty()) {
            poseStack.pushPose();
            final float scale = tile.fluidTank2.getFluidAmount() * 0.7F / tile.fluidTank2.getCapacity();
            switch (tile.facing) {
                case 2:
                    poseStack.translate(-0.24 + 0.25, +0.15, +0.26 + 0.25);
                    break;
                case 3:
                    poseStack.translate(+0.76 + 0.25, +0.15, +0.26 + 0.25);
                    break;
                case 4:
                    poseStack.translate(+0.26 + 0.25, +0.15, +0.76 + 0.25);
                    break;
                case 5:
                    poseStack.translate(+0.26 + 0.25, +0.15, -0.24 + 0.25);
                    break;
            }
            RenderFluidBlock.renderFluid(tile.fluidTank2.getFluid(), bufferSource, tile.getLevel(), tile.getPos(), poseStack, scale, 0.49f, 1);
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

    private Vec3 getOffset(Direction facing, float scale) {
        return switch (facing) {
            case SOUTH -> new Vec3(0.2, 0.3 + 0.6 * scale, 0.5);
            case NORTH -> new Vec3(0.75, 0.3 + 0.6 * scale, 0.5);
            case WEST -> new Vec3(0.5, 0.3 + 0.6 * scale, 0.2);
            case EAST -> new Vec3(0.5, 0.3 + 0.6 * scale, 0.75);
            default -> Vec3.ZERO;
        };
    }

    private Vec3 getOutputOffset(Direction facing) {
        return switch (facing) {
            case SOUTH -> new Vec3(1.0, 0.87, 0.5);
            case NORTH -> new Vec3(0.0, 0.87, 0.5);
            case WEST -> new Vec3(0.5, 0.87, 0.9);
            case EAST -> new Vec3(0.5, 0.87, -0.1);
            default -> Vec3.ZERO;
        };
    }
}
