package com.denfop.render.panel;

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

public class BottomModel<T extends Entity> extends EntityModel<T> {

    private final ModelPart glass;

    public BottomModel(int number) {
        super(RenderType::entityCutoutNoCull);
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        CubeListBuilder builder = CubeListBuilder.create();

        switch (number) {
            case 0:
                builder.addBox(0, 15F, 0, 5, 1, 5);
                break;
            case 1:
                builder.addBox(5, 15F, 0, 6, 1, 5);
                break;
            case 2:
                builder.addBox(11, 15F, 0, 5, 1, 5);
                break;
            case 3:
                builder.addBox(0, 15F, 5, 5, 1, 6);
                break;
            case 4:
                builder.addBox(5, 15F, 5, 6, 1, 6);
                break;
            case 5:
                builder.addBox(11, 15F, 5, 5, 1, 6);
                break;
            case 6:
                builder.addBox(0, 15F, 11, 5, 1, 5);
                break;
            case 7:
                builder.addBox(5, 15F, 11, 6, 1, 5);
                break;
            case 8:
                builder.addBox(11, 15F, 11, 5, 1, 5);
                break;
            default:
                builder.addBox(0, 15F, 0, 16, 1, 16);
                break;
        }

        this.glass = partdefinition.addOrReplaceChild("glass", builder, PartPose.ZERO).bake(64, 64);
    }


    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        glass.render(stack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
