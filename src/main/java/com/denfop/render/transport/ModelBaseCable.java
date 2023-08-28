package com.denfop.render.transport;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public class ModelBaseCable extends ModelBase {


    private final List<ModelRenderer> models = new ArrayList<>();

    public ModelBaseCable(EnumFacing facing) {
        if (facing == null) {
            this.textureWidth = 16;
            this.textureHeight = 16;

            models.add(new ModelRenderer(this, 2, 6).addBox(6, 6, 6, 4, 0, 4));
            models.add(new ModelRenderer(this, -2, 6).addBox(6, 10, 6, 4, 0, 4));
            models.add(new ModelRenderer(this, 6, 6).addBox(6, 6, 6, 4, 4, 0));
            models.add(new ModelRenderer(this, 2, 6).addBox(6, 6, 10, 4, 4, 0));
            models.add(new ModelRenderer(this, 6, 2).addBox(6, 6, 6, 0, 4, 4));
            models.add(new ModelRenderer(this, 2, 2).addBox(10, 6, 6, 0, 4, 4));

        } else {
            switch (facing) {
                case SOUTH:
                    this.textureWidth = 16;
                    this.textureHeight = 16;
                    models.add(new ModelRenderer(this, 0, 0).addBox(6, 6, 10, 0, 4, 6));
                    models.add(new ModelRenderer(this, 4, 0).addBox(10, 6, 10, 0, 4, 6));
                    models.add(new ModelRenderer(this, 0, 0).addBox(6, 6, 10, 4, 0, 6));
                    models.add(new ModelRenderer(this, -4, 0).addBox(6, 10, 10, 4, 0, 6));
                    break;
                case WEST:
                    this.textureWidth = 16;
                    this.textureHeight = 16;
                    models.add(new ModelRenderer(this, -4, 6).addBox(0, 6, 6, 6, 0, 4));
                    models.add(new ModelRenderer(this, 6, 6).addBox(0, 10, 6, 6, 0, 4));
                    models.add(new ModelRenderer(this, 0, 6).addBox(0, 6, 6, 6, 4, 0));
                    models.add(new ModelRenderer(this, 4, 6).addBox(0, 6, 10, 6, 4, 0));
                    break;
                case NORTH:
                    this.textureWidth = 16;
                    this.textureHeight = 16;
                    models.add(new ModelRenderer(this, 0, 0).addBox(6, 6, 0, 0, 4, 6));
                    models.add(new ModelRenderer(this, 4, 0).addBox(10, 6, 0, 0, 4, 6));
                    models.add(new ModelRenderer(this, 0, 0).addBox(6, 6, 0, 4, 0, 6));
                    models.add(new ModelRenderer(this, -4, 0).addBox(6, 10, 0, 4, 0, 6));
                    break;
                case UP:
                    this.textureWidth = 16;
                    this.textureHeight = 16;
                    models.add(new ModelRenderer(this, 6, -4).addBox(6, 10, 6, 0, 6, 4));
                    models.add(new ModelRenderer(this, 2, -4).addBox(10, 10, 6, 0, 6, 4));
                    models.add(new ModelRenderer(this, 2, 0).addBox(6, 10, 10, 4, 6, 0));
                    models.add(new ModelRenderer(this, 6, 0).addBox(6, 10, 6, 4, 6, 0));

                    break;
                case DOWN:
                    this.textureWidth = 16;
                    this.textureHeight = 16;
                    models.add(new ModelRenderer(this, 6, -4).addBox(6, 0, 6, 0, 6, 4));
                    models.add(new ModelRenderer(this, 2, -4).addBox(10, 0, 6, 0, 6, 4));
                    models.add(new ModelRenderer(this, 2, 0).addBox(6, 0, 10, 4, 6, 0));
                    models.add(new ModelRenderer(this, 6, 0).addBox(6, 0, 6, 4, 6, 0));
                    break;
                case EAST:
                    this.textureWidth = 16;
                    this.textureHeight = 16;
                    models.add(new ModelRenderer(this, -4, 6).addBox(10, 6, 6, 6, 0, 4));
                    models.add(new ModelRenderer(this, 6, 6).addBox(10, 10, 6, 6, 0, 4));
                    models.add(new ModelRenderer(this, 0, 6).addBox(10, 6, 6, 6, 4, 0));
                    models.add(new ModelRenderer(this, 4, 6).addBox(10, 6, 10, 6, 4, 0));
                    break;
            }
        }


    }


    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float scale) {
        models.forEach(model -> model.render(0.0625F));
    }

}
