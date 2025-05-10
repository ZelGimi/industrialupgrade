package com.denfop.recipes;

import com.denfop.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ItemStackHelper {
    public static ItemStack fromData(DataBlockEntity<?> entity, int count, int meta) {
        return new ItemStack(entity.getItem(meta), count);
    }
    public static ItemStack fromData(Block entity, int count) {
        return new ItemStack(entity, count);
    }
    public static ItemStack fromData(Block entity) {
        return new ItemStack(entity, 1);
    }
    public static ItemStack fromData(DataBlockEntity<?> entity, int count) {
        return new ItemStack(entity.getItem(0), count);
    }
    public static ItemStack fromData(DataBlockEntity<?> entity) {
        return new ItemStack(entity.getItem(0), 1);
    }
    public static ItemStack fromData(DataItem<?,?> entity, int count, int meta) {
        return new ItemStack(entity.getStack(meta), count);
    }
    public static ItemStack fromData(DataItem<?,?> entity, int count) {
        return new ItemStack(entity.getStack(0), count);
    }
    public static ItemStack fromData(DataItem<?,?> entity) {
        return new ItemStack(entity.getStack(0), 1);
    }
    public static ItemStack fromData(DataMultiBlock<?,?,?> entity, int count, int meta) {
        return new ItemStack(entity.getItemStack(0).getItem(), count);
    }
    public static ItemStack fromData(DataMultiBlock<?,?,?> entity, int count) {
        return new ItemStack(entity.getItemStack(0).getItem(), count);
    }
    public static ItemStack fromData(DataMultiBlock<?,?,?> entity) {
        return new ItemStack(entity.getItemStack(0).getItem(), 1);
    }
    public static ItemStack fromData(DataSimpleItem<?, ?> entity) {
        return new ItemStack(entity.getItem(), 1);
    }
    public static ItemStack fromData(DataSimpleItem<?, ?> entity,int count,int meta) {
        return new ItemStack(entity.getItem(), count);
    }
    public static ItemStack fromData(DataBlock<?, ?, ?> entity,int count, int meta) {
        return new ItemStack(entity.getItem(meta), count);
    }
    public static ItemStack fromData(DataBlock<?, ?, ?> entity,int count) {
        return new ItemStack(entity.getItem(0), count);
    }
    public static ItemStack fromData(DataBlock<?, ?, ?> entity) {
        return new ItemStack(entity.getItem(0), 1);
    }


    public static ItemStack fromData(Item item, int i) {
        return new ItemStack(item, i);
    }
    public static ItemStack fromData(Item item, int i, int i1) {
        return new ItemStack(item, i);
    }
    public static ItemStack fromData(Item item) {
        return new ItemStack(item, 1);
    }

    public static ItemStack fromData(DataSimpleItem<?, ?> item, int i) {
        return new ItemStack(item.getItem(), i);
    }
}
