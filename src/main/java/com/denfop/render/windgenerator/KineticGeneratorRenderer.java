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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class KineticGeneratorRenderer implements BlockEntityRenderer<BlockEntityWindGenerator> {

    private static final Map<Integer, RotorModel> rotorModels = new HashMap<>();

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
        Level world = tile.getLevel();
        BlockPos pos = tile.getBlockPos();

        int diameter = windGen.getRotorDiameter();
        if (diameter == 0) return;

        float angle = windGen.getAngle();
        ResourceLocation rotorTexture = windGen.getRotorRenderTexture();
        RotorModel model = rotorModels.computeIfAbsent(diameter, RotorModel::new);

        Direction facing = windGen.getFacing();

        poseStack.pushPose();
        poseStack.translate(0D, 0.5D, 0D);

        switch (facing) {
            case NORTH -> poseStack.translate(0.5, 0, 0);
            case EAST -> poseStack.translate(1, 0, 0.5);
            case SOUTH -> poseStack.translate(0.5, 0, 1);
            case WEST -> poseStack.translate(0, 0, 0.5);
        }

        switch (facing) {
            case NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(-90));
            case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(-180));
            case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(-270));
            case UP -> poseStack.mulPose(Axis.ZP.rotationDegrees(-90));
        }

        if (windGen.getSpace()) {
            angle = windGen.getAngle();
            WindRotor rotor = tile.getRotor();
            if (rotor.getMaxCustomDamage(tile.slot.get(0)) - rotor.getCustomDamage(tile.slot.get(0)) == 0) {
                angle = 0;
            }
            if (!Minecraft.getInstance().isPaused())
                poseStack.mulPose(Axis.XP.rotationDegrees(angle));
        }

        poseStack.translate(-0.2F, 0.0F, 0.0F);

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutout(rotorTexture));
        model.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(BlockEntityWindGenerator tile) {
        return true;
    }
}
