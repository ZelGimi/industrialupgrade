package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockPlanksRubberWood;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.datagen.itemtag.IItemTag;
import net.minecraft.world.item.Item;

public class ItemBlockRubberPlanks extends ItemBlockCore<BlockPlanksRubberWood.Type> implements IItemTag {
    public ItemBlockRubberPlanks(BlockCore p_40565_, BlockPlanksRubberWood.Type element) {
        super(p_40565_, element, new Properties().tab(IUCore.RecourseTab));
    }

    @Override
    public Item getItem() {
        return this;
    }


    @Override
    public String[] getTags() {

        String name = getElement().getName();
        return new String[]{"logs"};
    }
}
