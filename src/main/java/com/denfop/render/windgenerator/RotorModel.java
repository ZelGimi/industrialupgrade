package com.denfop.render.windgenerator;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class RotorModel extends EntityModel<Entity> {

    private static final int BLADE_COUNT = 4;

    private static final float[] BASE_ROT_X = new float[]{0.0F, 3.1F, 4.7F, 1.5F};
    private static final float[] BASE_ROT_Y = new float[]{-0.5F, 0.5F, 0.0F, 0.0F};
    private static final float[] BASE_ROT_Z = new float[]{0.0F, 0.0F, 0.5F, -0.5F};

    private final ModelPart[] fullBlades = new ModelPart[BLADE_COUNT];
    private final ModelPart[] chippedBlades = new ModelPart[BLADE_COUNT];
    private final ModelPart[] brokenBlades = new ModelPart[BLADE_COUNT];
    private final ModelPart[] stumpBlades = new ModelPart[BLADE_COUNT];

    private final ModelPart[] crackAFull = new ModelPart[BLADE_COUNT];
    private final ModelPart[] crackBFull = new ModelPart[BLADE_COUNT];

    private final ModelPart[] crackABroken = new ModelPart[BLADE_COUNT];
    private final ModelPart[] crackBBroken = new ModelPart[BLADE_COUNT];

    public RotorModel(int radius) {
        super(RenderType::entityCutoutNoCull);

        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        float fullLength = radius * 8.0F;

        float chippedBase = Math.max(3.0F, fullLength * 0.68F);
        float chippedToothA = Math.max(2.0F, fullLength * 0.14F);
        float chippedToothB = Math.max(2.0F, fullLength * 0.10F);

        float brokenBase = Math.max(3.0F, fullLength * 0.47F);
        float brokenToothA = Math.max(2.0F, fullLength * 0.16F);
        float brokenToothB = Math.max(2.0F, fullLength * 0.10F);
        float brokenToothC = Math.max(2.0F, fullLength * 0.08F);

        float stumpBase = Math.max(2.0F, fullLength * 0.24F);
        float stumpToothA = Math.max(1.0F, fullLength * 0.08F);
        float stumpToothB = Math.max(1.0F, fullLength * 0.06F);

        float crackLenAFull = Math.max(2.0F, fullLength * 0.20F);
        float crackLenBFull = Math.max(2.0F, fullLength * 0.15F);
        float crackY1Full = Math.max(1.0F, fullLength * 0.18F);
        float crackY2Full = Math.max(2.0F, fullLength * 0.48F);

        float crackLenABroken = Math.max(1.0F, brokenBase * 0.32F);
        float crackLenBBroken = Math.max(1.0F, brokenBase * 0.22F);
        float crackY1Broken = Math.max(0.8F, brokenBase * 0.16F);
        float crackY2Broken = Math.max(1.2F, brokenBase * 0.52F);

        for (int i = 0; i < BLADE_COUNT; i++) {
            PartPose pose = PartPose.rotation(BASE_ROT_X[i], BASE_ROT_Y[i], BASE_ROT_Z[i]);

            fullBlades[i] = root.addOrReplaceChild(
                    "rotor_full_" + i,
                    CubeListBuilder.create()
                            .texOffs(0, 0)
                            .addBox(0.0F, 0.0F, -4.0F, 1.0F, fullLength, 8.0F),
                    pose
            ).bake(32, 256);

            chippedBlades[i] = root.addOrReplaceChild(
                    "rotor_chipped_" + i,
                    CubeListBuilder.create()
                            .texOffs(0, 0)
                            .addBox(0.0F, 0.0F, -4.0F, 1.0F, chippedBase, 8.0F)
                            .addBox(0.0F, chippedBase - 1.0F, -4.0F, 1.0F, chippedToothA, 2.8F)
                            .addBox(0.0F, chippedBase + 1.2F, -0.8F, 1.0F, chippedToothB, 2.4F)
                            .addBox(0.0F, chippedBase + 0.4F, 2.0F, 1.0F, chippedToothA * 0.85F, 2.0F),
                    pose
            ).bake(32, 256);

            brokenBlades[i] = root.addOrReplaceChild(
                    "rotor_broken_" + i,
                    CubeListBuilder.create()
                            .texOffs(0, 0)
                            .addBox(0.0F, 0.0F, -4.0F, 1.0F, brokenBase, 8.0F)
                            .addBox(0.0F, brokenBase - 1.2F, -4.0F, 1.0F, brokenToothA, 2.2F)
                            .addBox(0.0F, brokenBase + 0.3F, -1.1F, 1.0F, brokenToothB, 1.9F)
                            .addBox(0.0F, brokenBase + 1.7F, 1.2F, 1.0F, brokenToothC, 1.7F)
                            .addBox(0.0F, brokenBase - 0.6F, 3.0F, 1.0F, brokenToothB, 1.0F),
                    pose
            ).bake(32, 256);

            stumpBlades[i] = root.addOrReplaceChild(
                    "rotor_stump_" + i,
                    CubeListBuilder.create()
                            .texOffs(0, 0)
                            .addBox(0.0F, 0.0F, -4.0F, 1.0F, stumpBase, 8.0F)
                            .addBox(0.0F, stumpBase - 0.8F, -4.0F, 1.0F, stumpToothA, 2.4F)
                            .addBox(0.0F, stumpBase + 0.2F, -0.8F, 1.0F, stumpToothB, 1.8F)
                            .addBox(0.0F, stumpBase - 0.2F, 2.2F, 1.0F, stumpToothA * 0.8F, 1.8F),
                    pose
            ).bake(32, 256);

            crackAFull[i] = root.addOrReplaceChild(
                    "rotor_crack_a_full_" + i,
                    CubeListBuilder.create()
                            .texOffs(0, 0)
                            .addBox(0.07F, crackY1Full, -4.08F, 0.86F, crackLenAFull, 0.12F),
                    pose
            ).bake(32, 256);

            crackBFull[i] = root.addOrReplaceChild(
                    "rotor_crack_b_full_" + i,
                    CubeListBuilder.create()
                            .texOffs(0, 0)
                            .addBox(0.07F, crackY2Full, 3.96F, 0.86F, crackLenBFull, 0.12F),
                    pose
            ).bake(32, 256);

            crackABroken[i] = root.addOrReplaceChild(
                    "rotor_crack_a_broken_" + i,
                    CubeListBuilder.create()
                            .texOffs(0, 0)
                            .addBox(0.07F, crackY1Broken, -4.08F, 0.86F, crackLenABroken, 0.12F),
                    pose
            ).bake(32, 256);

            crackBBroken[i] = root.addOrReplaceChild(
                    "rotor_crack_b_broken_" + i,
                    CubeListBuilder.create()
                            .texOffs(0, 0)
                            .addBox(0.07F, crackY2Broken, 3.96F, 0.86F, crackLenBBroken, 0.12F),
                    pose
            ).bake(32, 256);
        }
    }

    private static int color(float red, float green, float blue, float alpha) {
        int a = Mth.clamp((int) (alpha * 255.0F), 0, 255);
        int r = Mth.clamp((int) (red * 255.0F), 0, 255);
        int g = Mth.clamp((int) (green * 255.0F), 0, 255);
        int b = Mth.clamp((int) (blue * 255.0F), 0, 255);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    @Override
    public void setupAnim(
            Entity entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int rgba) {
        for (int i = 0; i < BLADE_COUNT; i++) {
            fullBlades[i].render(poseStack, buffer, packedLight, packedOverlay, rgba);
        }
    }

    public void renderDamagedRotor(
            PoseStack poseStack,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            RotorDamageProfile profile,
            float animationTime,
            boolean spinning
    ) {
        float wearTint = profile.getWearTint();
        float crackTint = profile.getCrackTint();

        for (int i = 0; i < BLADE_COUNT; i++) {
            RotorDamageProfile.BladeRenderState blade = profile.getBlade(i);

            if (blade.getGeometry() == RotorDamageProfile.BladeGeometry.MISSING) {
                continue;
            }

            ModelPart bladePart = getBladePart(i, blade.getGeometry());
            if (bladePart == null) {
                continue;
            }

            float wobble = profile.getBladeWobble(i, animationTime, spinning);
            float wobbleY = wobble * 0.35F;
            float wobbleX = wobble * 0.20F;

            float bladeShade = Mth.clamp(wearTint * blade.getShade(), 0.18F, 1.0F);

            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(blade.getTiltX() + wobbleX));
            poseStack.mulPose(Axis.YP.rotationDegrees(blade.getTiltY() + wobbleY));
            poseStack.mulPose(Axis.ZP.rotationDegrees(blade.getTiltZ() + wobble));

            bladePart.render(
                    poseStack,
                    buffer,
                    packedLight,
                    packedOverlay, color(bladeShade, bladeShade, bladeShade, 1)
            );

            float crackShade = Mth.clamp(crackTint * blade.getShade(), 0.10F, 0.72F);
            renderCracksForGeometry(
                    i,
                    blade.getGeometry(),
                    blade.hasCrackA(),
                    blade.hasCrackB(),
                    poseStack,
                    buffer,
                    packedLight,
                    packedOverlay,
                    crackShade
            );

            poseStack.popPose();
        }
    }

    private void renderCracksForGeometry(
            int index,
            RotorDamageProfile.BladeGeometry geometry,
            boolean crackAEnabled,
            boolean crackBEnabled,
            PoseStack poseStack,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            float crackShade
    ) {
        switch (geometry) {
            case FULL, CHIPPED -> {
                if (crackAEnabled) {
                    crackAFull[index].render(
                            poseStack, buffer, packedLight, packedOverlay, color(crackShade, crackShade, crackShade, 1)
                    );
                }
                if (crackBEnabled) {
                    crackBFull[index].render(
                            poseStack, buffer, packedLight, packedOverlay, color(crackShade, crackShade, crackShade, 1)
                    );
                }
            }
            case BROKEN -> {
                if (crackAEnabled) {
                    crackABroken[index].render(
                            poseStack, buffer, packedLight, packedOverlay, color(crackShade, crackShade, crackShade, 1)
                    );
                }
                if (crackBEnabled) {
                    crackBBroken[index].render(
                            poseStack, buffer, packedLight, packedOverlay, color(crackShade, crackShade, crackShade, 1)
                    );
                }
            }
            case STUMP, MISSING -> {

            }
        }
    }

    private ModelPart getBladePart(int index, RotorDamageProfile.BladeGeometry geometry) {
        return switch (geometry) {
            case FULL -> fullBlades[index];
            case CHIPPED -> chippedBlades[index];
            case BROKEN -> brokenBlades[index];
            case STUMP -> stumpBlades[index];
            case MISSING -> null;
        };
    }
}