package com.denfop.api.storage;

import com.denfop.api.storage.autocrafting.PatternStack;
import net.minecraft.world.item.ItemStack;

public interface PatternItem {

    PatternStack getPattern(ItemStack stack);

    boolean hasPattern(ItemStack stack);
}
