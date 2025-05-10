package com.denfop.api.recipe;


import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class RecipeArrayList<T extends IRecipeInputStack> extends ArrayList<T> {

    @Override
    public boolean contains(final Object o) {
        return super.contains(o);
    }


    public boolean contains(final ItemStack o) {
        for (T t : this)
            if (t.matched(o))
                return true;
        return false;
    }
}
