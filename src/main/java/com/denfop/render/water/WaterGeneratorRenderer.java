package com.denfop.render.water;

import com.denfop.api.windsystem.IWindMechanism;
import com.denfop.api.windsystem.WindRotor;
import com.denfop.blockentity.mechanism.water.BlockEntityBaseWaterGenerator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class WaterGeneratorRenderer implements BlockEntityRenderer<BlockEntityBaseWaterGenerator> {

    private static final Map<Integer, WaterRotorModel> ROTOR_MODELS = new HashMap<>();

    public WaterGeneratorRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(
            BlockEntityBaseWaterGenerator tile,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    ) {
        IWindMechanism mechanism = tile;
        WindRotor rotor = tile.getRotor();
        ResourceLocation rotorTexture = mechanism.getRotorRenderTexture();

        int diameter = mechanism.getRotorDiameter();
        if (diameter <= 0) {
            diameter = 3;
        }

        if (rotor == null || rotorTexture == null || tile.slot.get(0).isEmpty()) {
            return;
        }

        WaterRotorModel model = ROTOR_MODELS.computeIfAbsent(diameter, WaterRotorModel::new);
        WaterRotorDamageProfile damageProfile = WaterRotorDamageProfile.resolve(tile);

        float angle = mechanism.getAngle();
        Direction facing = mechanism.getFacing();

        boolean rotorBroken = rotor.getMaxCustomDamage(tile.slot.get(0)) - rotor.getCustomDamage(tile.slot.get(0)) <= 0;
        boolean spinning = false;

        poseStack.pushPose();
        poseStack.translate(0.0D, 0.5D, 0.0D);

        switch (facing) {
            case NORTH -> poseStack.translate(0.5D, 0.0D, 0.0D);
            case EAST -> poseStack.translate(1.0D, 0.0D, 0.5D);
            case SOUTH -> poseStack.translate(0.5D, 0.0D, 1.0D);
            case WEST -> poseStack.translate(0.0D, 0.0D, 0.5D);
            case UP, DOWN -> poseStack.translate(0.5D, 0.0D, 0.5D);
        }

        switch (facing) {
            case NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
            case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(-180.0F));
            case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(-270.0F));
            case UP -> poseStack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
            case DOWN -> poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
            default -> {
            }
        }

        if (mechanism.getSpace()) {
            if (rotorBroken) {
                angle = 0.0F;
            }

            if (!Minecraft.getInstance().isPaused()) {
                poseStack.mulPose(Axis.XP.rotationDegrees(angle));
                spinning = !rotorBroken;
            }
        }

        poseStack.translate(-0.2F, 0.0F, 0.0F);

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(rotorTexture));
        float animationTime = tile.getLevel() != null ? tile.getLevel().getGameTime() + partialTicks : partialTicks;

        model.renderDamagedRotor(
                poseStack,
                buffer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                damageProfile,
                animationTime,
                spinning
        );

        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(BlockEntityBaseWaterGenerator tile) {
        return true;
    }
}