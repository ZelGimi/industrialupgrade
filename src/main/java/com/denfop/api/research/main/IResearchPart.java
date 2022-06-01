package com.denfop.api.research.main;

import net.minecraft.item.ItemStack;

public interface IResearchPart {

    int getX();

    int getY();

    ItemStack getItemStack();

    EnumPartSampler getPart();

}
