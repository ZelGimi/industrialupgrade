package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockDeposits1;
import com.denfop.blocks.ItemBlockCore;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ItemBlockDeposits1 extends ItemBlockCore<BlockDeposits1.Type> {
    public ItemBlockDeposits1(BlockCore p_40565_, BlockDeposits1.Type element) {
        super(p_40565_, element, new Properties().tab(IUCore.RecourseTab));
    }

    @Override
    public Component getName(ItemStack pStack) {
        return getBlock().getCloneItemStack(null, null, null).getDisplayName();
    }
}
