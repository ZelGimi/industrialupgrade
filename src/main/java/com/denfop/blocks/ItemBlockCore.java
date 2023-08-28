package com.denfop.blocks;

import com.denfop.Localization;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemBlockCore extends ItemBlock {

    protected BlockCore blockCore;


    public ItemBlockCore(BlockCore block) {
        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setNoRepair();
        this.blockCore = block;
    }

    @Nonnull
    public String getUnlocalizedName(@Nonnull ItemStack stack) {
        return this.blockCore.getUnlocalizedName(stack);
    }

    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return this.blockCore.getRarity(stack);
    }

    @Nonnull
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return Localization.translate(this.getUnlocalizedName(stack));
    }

    public int getMetadata(int i) {
        return i;
    }


    public boolean hasEffect(ItemStack stack) {
        return stack.isItemEnchanted();
    }

    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return false;
    }


}
