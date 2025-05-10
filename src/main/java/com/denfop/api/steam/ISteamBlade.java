package com.denfop.api.steam;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface ISteamBlade {

    int getLevel();

    double getCoef();

    boolean damageBlade(ItemStack stack);

    ResourceLocation getTexture();
}
