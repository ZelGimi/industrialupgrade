package com.denfop.blocks;

import ic2.core.init.Localization;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockCore extends ItemBlock {

    protected BlockCore blockCore;


    public ItemBlockCore(BlockCore block) {
        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setNoRepair();
        this.blockCore = block;
    }

    public String getUnlocalizedName(ItemStack stack) {
        return this.blockCore.getUnlocalizedName(stack);
    }

    public EnumRarity getRarity(ItemStack stack) {
        return this.blockCore.getRarity(stack);
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return Localization.translate(this.getUnlocalizedName(stack));
    }

    public int getMetadata(int i) {
        return i;
    }


    public boolean hasEffect(ItemStack stack) {
        return stack.isItemEnchanted();
    }

    public boolean isEnchantable(ItemStack stack) {
        return false;
    }


}
