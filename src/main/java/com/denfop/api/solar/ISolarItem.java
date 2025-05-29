package com.denfop.api.solar;


import net.minecraft.resources.ResourceLocation;

public interface ISolarItem {

    EnumSolarType getType();

    double getGenerationValue(int damage);

    ResourceLocation getResourceLocation(int meta);

}
