package com.denfop.render.panel;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelMiniPanelGlass<T extends Entity> extends EntityModel<T> {

    private final ModelPart glass;

    public ModelMiniPanelGlass(int number) {
        super(RenderType::entityCutoutNoCull);

        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        int x = 0, y = 0, z = 0;
        int dx = 0, dy = 0, dz = 0;

        int texWidth = 16;
        int texHeight = 16;
        int texOffX = 0;
        int textOffY = 0;
        switch (number) {
            case 0:
                texWidth = 5;
                texHeight = 5;
                x = 0;
                z = 0;
                dx = 5;
                dz = 5;
                break;
            case 1:
                texWidth = 6;
                texHeight = 5;
                x = 5;
                z = 0;
                dx = 6;
                dz = 5;
                texOffX = 1;
                break;
            case 2:
                texWidth = 5;
                texHeight = 5;
                x = 11;
                z = 0;
                dx = 5;
                dz = 5;
                break;
            case 3:
                texWidth = 5;
                texHeight = 6;
                x = 0;
                z = 5;
                dx = 5;
                dz = 6;
                texOffX = 9;
                break;
            case 4:
                texWidth = 6;
                texHeight = 6;
                x = 5;
                z = 5;
                dx = 6;
                dz = 6;
                break;
            case 5:
                texWidth = 5;
                texHeight = 6;
                x = 11;
                z = 5;
                dx = 5;
                dz = 6;
                texOffX = 9;
                break;
            case 6:
                texWidth = 5;
                texHeight = 5;
                x = 0;
                z = 11;
                dx = 5;
                dz = 5;
                break;
            case 7:
                texWidth = 6;
                texHeight = 5;
                x = 5;
                z = 11;
                dx = 6;
                dz = 5;
                texOffX = 1;
                break;
            case 8:
                texWidth = 5;
                texHeight = 5;
                x = 11;
                z = 11;
                dx = 5;
                dz = 5;
                break;
            default:
                texWidth = 16;
                texHeight = 16;
                x = 0;
                z = 0;
                dx = 16;
                dz = 16;
                break;
        }

        root.addOrReplaceChild("glass",
                CubeListBuilder.create().texOffs(texOffX, textOffY).addBox(x, 16.05F, z, dx, 0, dz),
                PartPose.ZERO);

        LayerDefinition layerDefinition = LayerDefinition.create(mesh, texWidth, texHeight);
        this.glass = layerDefinition.bakeRoot().getChild("glass");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int i, int i1, int i2) {
        glass.render(poseStack, buffer, i, i1, i2);
    }

}
