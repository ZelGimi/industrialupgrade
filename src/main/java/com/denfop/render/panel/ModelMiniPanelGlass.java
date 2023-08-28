package com.denfop.render.panel;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMiniPanelGlass extends ModelBase {

    private final ModelRenderer glass;

    public ModelMiniPanelGlass(int number) {

        switch (number) {
            case 1:
            case 7:
                this.textureWidth = 6;
                this.textureHeight = 5;
                this.glass = new ModelRenderer(this, 1, 0);
                break;
            case 0:
            case 2:
            case 6:
            case 8:
                this.textureWidth = 5;
                this.textureHeight = 5;
                this.glass = new ModelRenderer(this, 0, 0);
                break;
            case 3:
            case 5:
                this.textureWidth = 5;
                this.textureHeight = 6;
                this.glass = new ModelRenderer(this, 9, 0);
                break;
            case 4:
                this.textureWidth = 6;
                this.textureHeight = 6;
                this.glass = new ModelRenderer(this, 0, 0);
                break;
            default:
                this.textureWidth = 16;
                this.textureHeight = 16;
                this.glass = new ModelRenderer(this, 0, 0);
                break;
        }

        switch (number) {
            case 0:
                this.glass.addBox(0, 16.001F, 0, 5, 0, 5);
                break;
            case 1:
                this.glass.addBox(5, 16.001F, 0, 6, 0, 5);
                break;
            case 2:
                this.glass.addBox(11, 16.001F, 0, 5, 0, 5);
                break;
            case 3:
                this.glass.addBox(0, 16.001F, 5, 5, 0, 6);
                break;
            case 4:
                this.glass.addBox(5, 16.001F, 5, 6, 0, 6);
                break;
            case 5:
                this.glass.addBox(11, 16.001F, 5, 5, 0, 6);
                break;
            case 6:
                this.glass.addBox(0, 16.001F, 11, 5, 0, 5);
                break;
            case 7:
                this.glass.addBox(5, 16.001F, 11, 6, 0, 5);
                break;
            case 8:
                this.glass.addBox(11, 16.001F, 11, 5, 0, 5);
                break;
            default:
                this.glass.addBox(0, 16.001F, 0, 16, 0, 16);
                break;
        }
        this.glass.mirror = true;
        this.glass.setRotationPoint(0F, 0.0F, 0.0F);
        this.glass.mirror = true;

    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float scale) {
        this.glass.render(0.0625F);
    }

}
