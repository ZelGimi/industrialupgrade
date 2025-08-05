package com.denfop.blocks.blockitem;

import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockDeposits2;
import com.denfop.blocks.ItemBlockCore;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemBlockDeposits2 extends ItemBlockCore<BlockDeposits2.Type> {
    public ItemBlockDeposits2(BlockCore p_40565_, BlockDeposits2.Type element) {
        super(p_40565_, element, new Properties());
    }
    @Override
    public Component getName(ItemStack pStack) {
        return getBlock().getCloneItemStack(null,null,null).getDisplayName();
    }
    @Override
    public void fillItemCategory(CreativeModeTab p_40569_, NonNullList<ItemStack> p_40570_) {

    }
}
