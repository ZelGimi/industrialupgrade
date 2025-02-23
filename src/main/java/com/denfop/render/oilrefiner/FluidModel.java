package com.denfop.render.oilrefiner;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;

import static net.minecraft.util.EnumFacing.*;

public class FluidModel extends ModelBase {


    private final ModelRenderer fluid;

    public FluidModel(int type, final EnumFacing facing) {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.fluid = new ModelRenderer(this, 0, 0);
        if (facing == NORTH) {
            if (type == 0) {
                this.fluid.addBox(
                        7F,
                        4F,
                        (float) -0.05,
                        2,
                        14,
                        2
                );
            }
            if (type == 1) {
                this.fluid.addBox(
                        13.05F,
                        4F,
                        4F,
                        2,
                        14,
                        2
                );
            }
            if (type == 2) {
                this.fluid.addBox(
                        (float) 0.95,
                        4F,
                        4F,
                        2,
                        14,
                        2
                );
            }
        }
        if (facing == SOUTH) {
            if (type == 0) {
                this.fluid.addBox(
                        7F,
                        4F,
                        (float) 14.05,
                        2,
                        14,
                        2
                );
            }
            if (type == 1) {
                this.fluid.addBox(
                        13.05F,
                        4F,
                        10F,
                        2,
                        14,
                        2
                );
            }
            if (type == 2) {
                this.fluid.addBox(
                        (float) 0.95,
                        4F,
                        10F,
                        2,
                        14,
                        2
                );
            }
        }
        if (facing == EAST) {
            if (type == 0) {
                this.fluid.addBox(
                        14.05F,
                        4F,
                        (float) 7F,
                        2,
                        14,
                        2
                );
            }
            if (type == 1) {
                this.fluid.addBox(
                        10F,
                        4F,
                        13.05F,
                        2,
                        14,
                        2
                );
            }
            if (type == 2) {
                this.fluid.addBox(
                        (float) 10F,
                        4F,
                        0.95F,
                        2,
                        14,
                        2
                );
            }
        }
        if (facing == WEST) {
            if (type == 0) {
                this.fluid.addBox(
                        0.05F,
                        4F,
                        (float) 7F,
                        2,
                        14,
                        2
                );
            }
            if (type == 1) {
                this.fluid.addBox(
                        4F,
                        4F,
                        13.05F,
                        2,
                        14,
                        2
                );
            }
            if (type == 2) {
                this.fluid.addBox(
                        (float) 4,
                        4F,
                        0.95F,
                        2,
                        14,
                        2
                );
            }
        }
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float scale) {

        GlStateManager.translate(0, 0.25 - 0.25 * scale, 0);
        GlStateManager.scale(1, Math.min(1, scale), 1);
        this.fluid.render(0.0625F);
    }

}
