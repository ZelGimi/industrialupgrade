package com.denfop.render.steam;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class SteamRod extends ModelBase {

    ModelRenderer rotor1;

    public SteamRod(int radius) {
        this.textureWidth = 32;
        this.textureHeight = 256;
        this.rotor1 = new ModelRenderer(this, 0, 0);
        this.rotor1.addBox(0.0F, 0.0F, -4.0F, 1, radius * 8, 8);
        this.rotor1.setRotationPoint(-8.0F, 0.0F, 0.0F);
        this.rotor1.setTextureSize(32, 256);
        this.rotor1.mirror = true;
        setRotation(this.rotor1, 0.0F, -0.5F, 0.0F);
    }

    private static void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float scale) {
        this.rotor1.render(scale);
    }

}
