package com.denfop.render.water;

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

public class WaterRotorModel extends EntityModel<Entity> {

    private static final int BLADE_COUNT = 4;

    private static final float[] BASE_ROT_X = new float[]{
            0.0F,
            1.5707964F,
            3.1415927F,
            4.7123890F
    };

    private static final float[] BASE_ROT_Y = new float[]{
            -0.12F,
            0.12F,
            -0.12F,
            0.12F
    };

    private static final float[] BASE_ROT_Z = new float[]{
            0.28F,
            -0.28F,
            0.28F,
            -0.28F
    };

    private final ModelPart[] fullBlades = new ModelPart[BLADE_COUNT];
    private final ModelPart[] erodedBlades = new ModelPart[BLADE_COUNT];
    private final ModelPart[] chippedBlades = new ModelPart[BLADE_COUNT];
    private final ModelPart[] brokenBlades = new ModelPart[BLADE_COUNT];
    private final ModelPart[] stumpBlades = new ModelPart[BLADE_COUNT];

    private final ModelPart[] crackAFull = new ModelPart[BLADE_COUNT];
    private final ModelPart[] crackBFull = new ModelPart[BLADE_COUNT];
    private final ModelPart[] crackABroken = new ModelPart[BLADE_COUNT];
    private final ModelPart[] crackBBroken = new ModelPart[BLADE_COUNT];

    public WaterRotorModel(int radius) {
        super(RenderType::entityCutoutNoCull);

        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        float fullLength = radius * 7.5F;

        float segA = Math.max(4.0F, fullLength * 0.34F);
        float segB = Math.max(3.0F, fullLength * 0.28F);
        float segC = Math.max(2.0F, fullLength * 0.22F);
        float segD = Math.max(2.0F, fullLength * 0.16F);

        float erodedLength = Math.max(4.0F, fullLength * 0.88F);
        float chippedLength = Math.max(3.0F, fullLength * 0.72F);
        float brokenLength = Math.max(3.0F, fullLength * 0.50F);
        float stumpLength = Math.max(2.0F, fullLength * 0.26F);

        float crackLenAFull = Math.max(1.5F, fullLength * 0.18F);
        float crackLenBFull = Math.max(1.0F, fullLength * 0.12F);
        float crackY1Full = Math.max(1.0F, fullLength * 0.20F);
        float crackY2Full = Math.max(1.0F, fullLength * 0.54F);

        float crackLenABroken = Math.max(1.0F, brokenLength * 0.30F);
        float crackLenBBroken = Math.max(1.0F, brokenLength * 0.20F);
        float crackY1Broken = Math.max(0.8F, brokenLength * 0.18F);
        float crackY2Broken = Math.max(1.0F, brokenLength * 0.50F);

        for (int i = 0; i < BLADE_COUNT; i++) {
            PartPose pose = PartPose.rotation(BASE_ROT_X[i], BASE_ROT_Y[i], BASE_ROT_Z[i]);

            fullBlades[i] = bakePart(
                    root,
                    "water_rotor_full_" + i,
                    fullBladeBuilder(segA, segB, segC, segD),
                    pose
            );

            erodedBlades[i] = bakePart(
                    root,
                    "water_rotor_eroded_" + i,
                    erodedBladeBuilder(erodedLength),
                    pose
            );

            chippedBlades[i] = bakePart(
                    root,
                    "water_rotor_chipped_" + i,
                    chippedBladeBuilder(chippedLength),
                    pose
            );

            brokenBlades[i] = bakePart(
                    root,
                    "water_rotor_broken_" + i,
                    brokenBladeBuilder(brokenLength),
                    pose
            );

            stumpBlades[i] = bakePart(
                    root,
                    "water_rotor_stump_" + i,
                    stumpBladeBuilder(stumpLength),
                    pose
            );


            crackAFull[i] = bakePart(
                    root,
                    "water_rotor_crack_a_full_" + i,
                    CubeListBuilder.create()
                            .texOffs(0, 0)
                            .addBox(0.08F, crackY1Full, -2.96F, 0.84F, crackLenAFull, 0.06F),
                    pose
            );

            crackBFull[i] = bakePart(
                    root,
                    "water_rotor_crack_b_full_" + i,
                    CubeListBuilder.create()
                            .texOffs(0, 0)
                            .addBox(0.08F, crackY2Full, 2.90F, 0.84F, crackLenBFull, 0.06F),
                    pose
            );

            crackABroken[i] = bakePart(
                    root,
                    "water_rotor_crack_a_broken_" + i,
                    CubeListBuilder.create()
                            .texOffs(0, 0)
                            .addBox(0.08F, crackY1Broken, -2.96F, 0.84F, crackLenABroken, 0.06F),
                    pose
            );

            crackBBroken[i] = bakePart(
                    root,
                    "water_rotor_crack_b_broken_" + i,
                    CubeListBuilder.create()
                            .texOffs(0, 0)
                            .addBox(0.08F, crackY2Broken, 2.90F, 0.84F, crackLenBBroken, 0.06F),
                    pose
            );
        }
    }

    private static ModelPart bakePart(PartDefinition root, String name, CubeListBuilder builder, PartPose pose) {
        return root.addOrReplaceChild(name, builder, pose).bake(32, 256);
    }

    private static CubeListBuilder fullBladeBuilder(float segA, float segB, float segC, float segD) {
        return CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(0.0F, 0.0F, -3.0F, 1.0F, segA, 6.0F)
                .addBox(0.0F, segA - 1.2F, -2.5F, 1.0F, segB, 5.0F)
                .addBox(0.0F, segA + segB - 1.4F, -1.8F, 1.0F, segC, 3.6F)
                .addBox(0.0F, segA + segB + segC - 1.2F, -0.9F, 1.0F, segD, 1.8F);
    }

    private static CubeListBuilder erodedBladeBuilder(float length) {
        float base = Math.max(3.0F, length * 0.42F);
        float mid = Math.max(2.0F, length * 0.30F);
        float tip = Math.max(2.0F, length * 0.18F);
        float tooth = Math.max(1.0F, length * 0.10F);

        return CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(0.0F, 0.0F, -3.0F, 1.0F, base, 6.0F)
                .addBox(0.0F, base - 1.0F, -2.4F, 1.0F, mid, 4.8F)
                .addBox(0.0F, base + mid - 1.0F, -1.7F, 1.0F, tip, 3.4F)
                .addBox(0.0F, base + mid + tip - 0.8F, -0.8F, 1.0F, tooth, 1.6F);
    }


    private static CubeListBuilder chippedBladeBuilder(float length) {
        float base = Math.max(3.0F, length * 0.48F);
        float mid = Math.max(2.0F, length * 0.24F);
        float tip = Math.max(1.0F, length * 0.10F);

        return CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(0.0F, 0.0F, -3.0F, 1.0F, base, 6.0F)
                .addBox(0.0F, base - 0.9F, -2.2F, 1.0F, mid, 4.4F)
                .addBox(0.0F, base + mid - 0.7F, -1.1F, 1.0F, tip, 2.2F);
    }


    private static CubeListBuilder brokenBladeBuilder(float length) {
        float base = Math.max(2.0F, length * 0.58F);
        float jagA = Math.max(1.0F, length * 0.14F);
        float jagB = Math.max(1.0F, length * 0.10F);

        return CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(0.0F, 0.0F, -3.0F, 1.0F, base, 6.0F)
                .addBox(0.0F, base - 0.7F, -2.3F, 1.0F, jagA, 2.5F)
                .addBox(0.0F, base + 0.1F, -0.7F, 1.0F, jagB, 1.6F);
    }

    private static CubeListBuilder stumpBladeBuilder(float length) {
        float base = Math.max(1.5F, length * 0.74F);
        float jag = Math.max(1.0F, length * 0.12F);

        return CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(0.0F, 0.0F, -3.0F, 1.0F, base, 6.0F)
                .addBox(0.0F, base - 0.5F, -2.2F, 1.0F, jag, 2.3F);
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
            WaterRotorDamageProfile profile,
            float animationTime,
            boolean spinning
    ) {
        float wearTint = profile.getWearTint();
        float crackTint = profile.getCrackTint();

        for (int i = 0; i < BLADE_COUNT; i++) {
            WaterRotorDamageProfile.BladeRenderState blade = profile.getBlade(i);

            if (blade.getGeometry() == WaterRotorDamageProfile.BladeGeometry.MISSING) {
                continue;
            }

            ModelPart bladePart = getBladePart(i, blade.getGeometry());
            if (bladePart == null) {
                continue;
            }

            float wobble = profile.getBladeWobble(i, animationTime, spinning);
            float wobbleX = wobble * 0.18F;
            float wobbleY = wobble * 0.32F;

            float bladeShade = Mth.clamp(wearTint * blade.getShade(), 0.16F, 1.0F);

            poseStack.pushPose();
            poseStack.translate(0.0F, blade.getOffsetY(), blade.getOffsetZ());
            poseStack.mulPose(Axis.XP.rotationDegrees(blade.getTiltX() + wobbleX));
            poseStack.mulPose(Axis.YP.rotationDegrees(blade.getTiltY() + wobbleY));
            poseStack.mulPose(Axis.ZP.rotationDegrees(blade.getTiltZ() + wobble));

            bladePart.render(
                    poseStack,
                    buffer,
                    packedLight,
                    packedOverlay,
                    color(bladeShade,
                            bladeShade,
                            bladeShade,
                            1.0F)
            );

            float crackShade = Mth.clamp(crackTint * blade.getShade(), 0.10F, 0.78F);
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
            WaterRotorDamageProfile.BladeGeometry geometry,
            boolean crackAEnabled,
            boolean crackBEnabled,
            PoseStack poseStack,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            float crackShade
    ) {
        switch (geometry) {
            case FULL, ERODED -> {
                if (crackAEnabled) {
                    crackAFull[index].render(
                            poseStack,
                            buffer,
                            packedLight,
                            packedOverlay, color(
                                    crackShade,
                                    crackShade,
                                    crackShade,
                                    1.0F)
                    );
                }
                if (crackBEnabled) {
                    crackBFull[index].render(
                            poseStack,
                            buffer,
                            packedLight,
                            packedOverlay, color(
                                    crackShade,
                                    crackShade,
                                    crackShade,
                                    1.0F)
                    );
                }
            }
            case CHIPPED -> {

                if (crackAEnabled) {
                    crackAFull[index].render(
                            poseStack,
                            buffer,
                            packedLight,
                            packedOverlay, color(
                                    crackShade,
                                    crackShade,
                                    crackShade,
                                    1.0F)
                    );
                }
            }
            case BROKEN -> {
                if (crackAEnabled) {
                    crackABroken[index].render(
                            poseStack,
                            buffer,
                            packedLight,
                            packedOverlay, color(
                                    crackShade,
                                    crackShade,
                                    crackShade,
                                    1.0F)
                    );
                }
            }
            case STUMP, MISSING -> {

            }
        }
    }

    private ModelPart getBladePart(int index, WaterRotorDamageProfile.BladeGeometry geometry) {
        return switch (geometry) {
            case FULL -> fullBlades[index];
            case ERODED -> erodedBlades[index];
            case CHIPPED -> chippedBlades[index];
            case BROKEN -> brokenBlades[index];
            case STUMP -> stumpBlades[index];
            case MISSING -> null;
        };
    }
}