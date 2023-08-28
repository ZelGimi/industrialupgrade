package com.denfop.items.block;


import com.denfop.Localization;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.function.Function;

public class ItemBlockIU extends ItemBlock {

    public static Function<Block, Item> supplier = ItemBlockIU::new;

    public ItemBlockIU(Block block) {
        super(block);
    }

    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName();
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return Localization.translate(this.getUnlocalizedName(stack));
    }

    public boolean canHarvestBlock(IBlockState block, ItemStack stack) {
        return false;
    }

    public int getItemBurnTime(ItemStack stack) {

        return -1;

    }


}
