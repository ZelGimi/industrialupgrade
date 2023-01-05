package com.denfop.api.windsystem;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IWindRotor {

    int getDiameter(ItemStack var1);

    ResourceLocation getRotorRenderTexture(ItemStack var1);

    float getEfficiency(ItemStack var1);

    int getLevel();

    int getIndex();

    int getSourceTier();

}
