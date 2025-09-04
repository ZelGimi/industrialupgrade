package com.denfop.blockentity.transport.types;


import net.minecraft.resources.ResourceLocation;

public interface ICableItem {

    String getNameCable();

    String getMainPath();

    float getThickness();

    ResourceLocation getRecourse();

    default double getLoss() {
        return 0;
    }

    default double getCapacity() {
        return Integer.MAX_VALUE;
    }

}
