package com.denfop.render.panel;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class PollutionModel<T extends Entity> extends EntityModel<T> {

    private final ModelPart pollution;

    public PollutionModel(RandomSource random, int index) {
        super(RenderType::entityCutoutNoCull);
        var mesh = new MeshDefinition();
        var root = mesh.getRoot();

        Set<ModelCord> set = new HashSet<>();

        int maxIterations = switch (index) {
            case 1 -> 64;
            case 2 -> 128;
            case 3, -1 -> 192;
            default -> 0;
        };

        for (int i = 0; i < maxIterations; ) {
            int x = random.nextInt(16);
            int z = random.nextInt(16);
            ModelCord cord = new ModelCord(x, z);

            if (set.add(cord)) {
                // y = 16.1 заменено на y = 0.1F здесь для примера
                root.addOrReplaceChild("box" + i,
                        CubeListBuilder.create().texOffs(0, 0).addBox(x, 16.1F, z, 1, 0, 1),
                        PartPose.ZERO);
                i++;
            }
        }

        this.pollution = LayerDefinition.create(mesh, 0, 0).bakeRoot();
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight,
                               int packedOverlay, int col) {
        pollution.render(poseStack, buffer, packedLight, packedOverlay, col);
    }

}

class ModelCord {
    private final int x;
    private final int z;

    public ModelCord(int x, int z) {
        this.x = x;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModelCord that)) return false;
        return x == that.x && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }
}
