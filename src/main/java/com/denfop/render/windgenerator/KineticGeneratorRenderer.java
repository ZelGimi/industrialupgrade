package com.denfop.render.windgenerator;

import com.denfop.api.windsystem.IWindMechanism;
import com.denfop.api.windsystem.WindRotor;
import com.denfop.blockentity.mechanism.wind.BlockEntityWindGenerator;
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

public class KineticGeneratorRenderer implements BlockEntityRenderer<BlockEntityWindGenerator> {

    private static final Map<Integer, RotorModel> ROTOR_MODELS = new HashMap<>();

    public KineticGeneratorRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(
            BlockEntityWindGenerator tile,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    ) {
        IWindMechanism windGen = tile;

        int diameter = windGen.getRotorDiameter();
        WindRotor rotor = tile.getRotor();
        ResourceLocation rotorTexture = windGen.getRotorRenderTexture();

        if (diameter <= 0 || rotor == null || rotorTexture == null || tile.slot.get(0).isEmpty()) {
            return;
        }

        RotorModel model = ROTOR_MODELS.computeIfAbsent(diameter, RotorModel::new);
        RotorDamageProfile damageProfile = RotorDamageProfile.resolve(tile);

        float angle = windGen.getAngle();
        Direction facing = windGen.getFacing();

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

        boolean rotorBroken = rotor.getMaxCustomDamage(tile.slot.get(0)) - rotor.getCustomDamage(tile.slot.get(0)) <= 0;
        boolean spinning = false;

        if (windGen.getSpace()) {
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
    public boolean shouldRenderOffScreen(BlockEntityWindGenerator tile) {
        return true;
    }
}