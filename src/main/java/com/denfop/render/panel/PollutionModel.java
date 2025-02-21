package com.denfop.render.panel;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class PollutionModel extends ModelBase {

    private final ModelRenderer pollution;
    List<ModelCord> list = new ArrayList<>();

    public PollutionModel(Random random, int index) {
        this.textureWidth = 0;
        this.textureHeight = 0;
        this.pollution = new ModelRenderer(this, 0, 0);

        int maxIterations;
        if (index == 0) {
            maxIterations = 0;
        } else if (index == 1) {
            maxIterations = 64;
        } else if (index == 2) {
            maxIterations = 128;
        } else {
            maxIterations = 192;
        }

        if (maxIterations > 0) {
            int i = 0;
            while (i < maxIterations) {
                int x = random.nextInt(16);
                int z = random.nextInt(16);
                final ModelCord modelCord = new ModelCord(x, z);
                if (!list.contains(modelCord)) {
                    list.add(modelCord);
                    i++;
                    this.pollution.setTextureSize(x, z);
                    pollution.addBox(x, 16.1F, z, 1, 0, 1);
                }
            }
        }
        list.clear();


    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float scale) {
        this.pollution.render(0.0625F);
    }

}

@SideOnly(Side.CLIENT)
class ModelCord {

    private final int x;
    private final int z;

    public ModelCord(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ModelCord modelCord = (ModelCord) o;
        return x == modelCord.x && z == modelCord.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

}
