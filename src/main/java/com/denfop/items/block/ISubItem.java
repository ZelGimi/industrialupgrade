package com.denfop.items.block;


import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public interface ISubItem<T extends IStringSerializable> {

    ItemStack getItemStack(T var1);


}
