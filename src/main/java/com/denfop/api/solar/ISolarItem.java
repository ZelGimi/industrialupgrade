package com.denfop.api.solar;


import net.minecraft.util.ResourceLocation;

public interface ISolarItem {

    EnumSolarType getType();

    double getGenerationValue(int damage);

    ResourceLocation getResourceLocation(int meta);

}
