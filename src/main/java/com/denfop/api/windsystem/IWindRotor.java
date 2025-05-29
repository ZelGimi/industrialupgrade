package com.denfop.api.windsystem;

import com.denfop.api.item.IDamageItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface IWindRotor extends IDamageItem {

    int getDiameter(ItemStack var1);

    ResourceLocation getRotorRenderTexture(ItemStack var1);

    float getEfficiency(ItemStack var1);

    int getLevel();

    int getIndex();

    int getSourceTier();

}
