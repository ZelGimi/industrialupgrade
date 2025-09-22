package com.denfop.api.steam;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface ISteamBlade {

    int getLevel();

    double getCoef();

    boolean damageBlade(ItemStack stack);

    ResourceLocation getTexture();

}
