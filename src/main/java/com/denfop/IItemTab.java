package com.denfop;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public interface IItemTab extends ItemLike {
    default void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_){
        if (allowedIn(p_41391_))
            p_41392_.add(new ItemStack(this));
    }

    default boolean allowedIn(CreativeModeTab p_220153_) {
        CreativeModeTab creativemodetab = this.getItemCategory();
        return creativemodetab != null && (p_220153_.getType() == CreativeModeTab.Type.SEARCH || p_220153_ == creativemodetab);
    }
    CreativeModeTab getItemCategory();
}
