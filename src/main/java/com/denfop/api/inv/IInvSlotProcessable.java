package com.denfop.api.inv;

import ic2.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;

public interface IInvSlotProcessable {

    RecipeOutput process();

    void consume();

    boolean isEmpty();

    ItemStack get1(int i);


}
