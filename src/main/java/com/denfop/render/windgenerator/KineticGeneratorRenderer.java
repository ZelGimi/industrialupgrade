package com.denfop.render.windgenerator;

import com.denfop.api.windsystem.IWindMechanism;
import com.denfop.api.windsystem.IWindRotor;
import com.denfop.tiles.mechanism.wind.TileWindGenerator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
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

public class KineticGeneratorRenderer implements BlockEntityRenderer<TileWindGenerator> {

    private static final Map<Integer, RotorModel> rotorModels = new HashMap<>();

    public KineticGeneratorRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(
            TileWindGenerator tile,
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
            case NORTH -> poseStack.mulPose(Vector3f.YP.rotationDegrees(-90));
            case EAST -> poseStack.mulPose(Vector3f.YP.rotationDegrees(-180));
            case SOUTH -> poseStack.mulPose(Vector3f.YP.rotationDegrees(-270));
            case UP -> poseStack.mulPose(Vector3f.ZP.rotationDegrees(-90));
        }

        if (windGen.getSpace()) {
            angle = windGen.getAngle();
            IWindRotor rotor = tile.getRotor();
            if (rotor.getMaxCustomDamage(tile.slot.get(0)) - rotor.getCustomDamage(tile.slot.get(0)) == 0){
                angle = 0;
            }
            if (!Minecraft.getInstance().isPaused())
            poseStack.mulPose(Vector3f.XP.rotationDegrees(angle));
        }

        poseStack.translate(-0.2F, 0.0F, 0.0F);

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutout(rotorTexture));
        model.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);

        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(TileWindGenerator tile) {
        return true;
    }
}
