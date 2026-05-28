package com.denfop.api.storage;

import com.denfop.api.storage.autocrafting.PatternStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;

public interface PatternItem {

    PatternStack getPattern(ItemStack stack, HolderLookup.Provider provider);

    boolean hasPattern(ItemStack stack);
}
