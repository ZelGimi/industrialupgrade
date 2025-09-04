package com.denfop.api.windsystem;

import com.denfop.api.item.DamageItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public interface WindRotor extends DamageItem {

    int getDiameter(ItemStack var1);

    ResourceLocation getRotorRenderTexture(ItemStack var1);

    float getEfficiency(ItemStack var1);

    int getLevel();

    int getIndex();

    int getSourceTier();

    Color getColor();
}
