package com.denfop.api.inv;

import ic2.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;

public interface IInvSlotProcessableMulti {

    RecipeOutput process(int slotId);

    void consume(int slotId);

    boolean isEmpty(int slotId);

    ItemStack get1(int i);


}
