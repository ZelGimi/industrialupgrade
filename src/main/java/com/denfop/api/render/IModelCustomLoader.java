package com.denfop.api.render;


import net.minecraft.resources.ResourceLocation;

public interface IModelCustomLoader {

    String getType();

    String[] getSuffixes();

    IModelCustom loadInstance(ResourceLocation var1);

}
