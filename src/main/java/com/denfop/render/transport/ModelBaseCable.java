package com.denfop.render.transport;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModelBaseCable extends ModelBase {


    ModelRenderer model;
    public static <T> List<T> reverseOrder(List<T> list) {
        return list.stream()
                .sorted((a, b) -> -list.indexOf(a) + list.indexOf(b))
                .collect(Collectors.toList());
    }
    public  ModelBaseCable(byte connect) {
        if (connect == 0) {
            this.textureWidth = 16;
            this.textureHeight = 16;

            model = new ModelRenderer(this, 2, 6).addBox(6, 6, 6, 4, 0, 4).setTextureOffset(-2, 6).addBox(6, 10, 6, 4, 0, 4)
                    .setTextureOffset(6,6).addBox(6, 6, 6, 4, 4, 0)
                    .setTextureOffset(2, 6).addBox(6, 6, 10, 4, 4, 0)
                    .setTextureOffset(6, 2).addBox(6, 6, 6, 0, 4, 4)
                    .setTextureOffset(2, 2).addBox(10, 6, 6, 0, 4, 4);


        } else {

            this.textureWidth = 16;
            this.textureHeight = 16;
            model = new ModelRenderer(this, 2, 6).addBox(6, 6, 6, 4, 0, 4).setTextureOffset(-2, 6).addBox(6, 10, 6, 4, 0, 4)
                    .setTextureOffset(6,6).addBox(6, 6, 6, 4, 4, 0)
                    .setTextureOffset(2, 6).addBox(6, 6, 10, 4, 4, 0)
                    .setTextureOffset(6, 2).addBox(6, 6, 6, 0, 4, 4)
                    .setTextureOffset(2, 2).addBox(10, 6, 6, 0, 4, 4);
            final List<EnumFacing> var4 = reverseOrder(Arrays.stream(EnumFacing.VALUES).collect(Collectors.toList()));

            for (EnumFacing facing :var4) {
                if ((connect & 1) != 1) {
                    connect = (byte) (connect >> 1);
                    continue;
                }
                connect = (byte) (connect >> 1);
                switch (facing) {
                    case SOUTH:
                        this.textureWidth = 16;
                        this.textureHeight = 16;
                        model .setTextureOffset( 0, 0).addBox(6, 6, 10, 0, 4, 6);
                        model .setTextureOffset( 4, 0).addBox(10, 6, 10, 0, 4, 6);
                        model .setTextureOffset(0, 0).addBox(6, 6, 10, 4, 0, 6);
                        model .setTextureOffset(-4, 0).addBox(6, 10, 10, 4, 0, 6);
                        model .setTextureOffset(2,6).addBox(6, 6, 16, 4, 4, 0);

                         break;
                    case WEST:
                        this.textureWidth = 16;
                        this.textureHeight = 16;
                        model .setTextureOffset( -4, 6).addBox(0, 6, 6, 6, 0, 4);
                        model .setTextureOffset( 6, 6).addBox(0, 10, 6, 6, 0, 4);
                        model .setTextureOffset( 0, 6).addBox(0, 6, 6, 6, 4, 0);
                        model .setTextureOffset(4, 6).addBox(0, 6, 10, 6, 4, 0);
                        model .setTextureOffset(6, 2).addBox(0, 6, 6, 0, 4, 4);
                       break;
                    case NORTH:
                        this.textureWidth = 16;
                        this.textureHeight = 16;
                        model .setTextureOffset( 0, 0).addBox(6, 6, 0, 0, 4, 6);
                        model .setTextureOffset( 4, 0).addBox(10, 6, 0, 0, 4, 6);
                        model .setTextureOffset( 0, 0).addBox(6, 6, 0, 4, 0, 6);
                        model .setTextureOffset(-4, 0).addBox(6, 10, 0, 4, 0, 6);
                        model .setTextureOffset(6,6).addBox(6, 6, 0, 4, 4, 0);

                        break;
                    case UP:
                        this.textureWidth = 16;
                        this.textureHeight = 16;
                        model .setTextureOffset(6, -4).addBox(6, 10, 6, 0, 6, 4);
                        model .setTextureOffset( 2, -4).addBox(10, 10, 6, 0, 6, 4);
                        model .setTextureOffset(2, 0).addBox(6, 10, 10, 4, 6, 0);
                        model .setTextureOffset(6, 0).addBox(6, 10, 6, 4, 6, 0);
                        model .setTextureOffset( 2, 6).addBox(6, 16, 6, 4, 0, 4);
                        break;
                    case DOWN:
                        this.textureWidth = 16;
                        this.textureHeight = 16;
                        model .setTextureOffset( 6, -4).addBox(6, 0, 6, 0, 6, 4);
                        model .setTextureOffset( 2, -4).addBox(10, 0, 6, 0, 6, 4);
                        model .setTextureOffset( 2, 0).addBox(6, 0, 10, 4, 6, 0);
                        model .setTextureOffset( 6, 0).addBox(6, 0, 6, 4, 6, 0);
                        model .setTextureOffset( 2, 6).addBox(6, 0, 6, 4, 0, 4);

                        break;
                    case EAST:
                        this.textureWidth = 16;
                        this.textureHeight = 16;
                        model .setTextureOffset(-4, 6).addBox(10, 6, 6, 6, 0, 4);
                        model .setTextureOffset( 6, 6).addBox(10, 10, 6, 6, 0, 4);
                        model .setTextureOffset(0, 6).addBox(10, 6, 6, 6, 4, 0);
                        model .setTextureOffset( 4, 6).addBox(10, 6, 10, 6, 4, 0);
                        model .setTextureOffset(2, 2).addBox(16, 6, 6, 0, 4, 4);

                        break;
                }
            }
        }

    }


    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float scale) {
        if(this.boxList.size() > 0)
        this.boxList.remove(0);
        model.render(0.0625F);
    }

}
