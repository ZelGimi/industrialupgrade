package com.denfop.items;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.blocks.IULeaves;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.item.ItemLeaves;
import net.minecraft.item.ItemStack;

public class ItemBlockLeaves extends ItemLeaves {

    public ItemBlockLeaves(Block block) {
        super((BlockLeaves) block);
        this.setHasSubtypes(false);
    }

    public String getUnlocalizedName() {
        return Constants.ABBREVIATION + "." + super.getUnlocalizedName().substring(5);
    }

    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + "." + this.block.getStateFromMeta(stack.getMetadata()).getValue(IULeaves.typeProperty)
                .getName();
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return Localization.translate(this.getUnlocalizedName(stack));
    }

}
