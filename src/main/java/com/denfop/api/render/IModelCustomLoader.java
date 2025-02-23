package com.denfop.api.render;

import net.minecraft.util.ResourceLocation;

public interface IModelCustomLoader {

    String getType();

    String[] getSuffixes();

    IModelCustom loadInstance(ResourceLocation var1);

}
