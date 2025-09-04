package com.denfop.render.windgenerator;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;

public class RotorModel<T extends Entity> extends EntityModel<T> {


    private final ModelPart rotor1;
    private final ModelPart rotor2;
    private final ModelPart rotor3;
    private final ModelPart rotor4;

    public RotorModel(int radius) {
        super(RenderType::entityCutoutNoCull);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        rotor1 = root.addOrReplaceChild("rotor1", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, 0.0F, -4.0F, 1, radius * 8, 8).mirror(),
                PartPose.rotation(0.0F, -0.5F, 0.0F)).bake(32, 256);

        rotor2 = root.addOrReplaceChild("rotor2", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, 0.0F, -4.0F, 1, radius * 8, 8).mirror(),
                PartPose.rotation(3.1F, 0.5F, 0.0F)).bake(32, 256);

        rotor3 = root.addOrReplaceChild("rotor3", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, 0.0F, -4.0F, 1, radius * 8, 8).mirror(),
                PartPose.rotation(4.7F, 0.0F, 0.5F)).bake(32, 256);

        rotor4 = root.addOrReplaceChild("rotor4", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, 0.0F, -4.0F, 1, radius * 8, 8).mirror(),
                PartPose.rotation(1.5F, 0.0F, -0.5F)).bake(32, 256);

    }


    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        rotor1.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rotor2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rotor3.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rotor4.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
