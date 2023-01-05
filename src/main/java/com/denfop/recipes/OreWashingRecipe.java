package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.MachineRecipe;
import ic2.api.recipe.Recipes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class OreWashingRecipe {

    public static void init() {
        final Iterable<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> recipe1 = Recipes.oreWashing.getRecipes();
        final Iterator<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> iter1 = recipe1.iterator();
        while (iter1.hasNext()) {
            MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe2 = iter1.next();
            List<ItemStack> list = (List<ItemStack>) recipe2.getOutput();
            if (list.get(0).isItemEqual(Ic2Items.purifiedCrushedSilverOre)) {
                iter1.remove();
            }

        }
        addrecipe(0, null);
        addrecipe(1, new ItemStack(Blocks.SAND));
        addrecipe(2, new ItemStack(Ic2Items.smallLeadDust.getItem(), 2, 10));
        addrecipe(3, new ItemStack(IUItem.smalldust, 2, 16));
        addrecipe(6, new ItemStack(Ic2Items.smallLeadDust.getItem(), 2, 1));
        addrecipe(7, new ItemStack(IUItem.smalldust, 2, 16));
        addrecipe(8, new ItemStack(Ic2Items.smallLeadDust.getItem(), 2, 0));
        addrecipe(9, new ItemStack(Ic2Items.smallLeadDust.getItem(), 2, 2));
        addrecipe(10, new ItemStack(Ic2Items.smallLeadDust.getItem(), 2, 0));
        addrecipe(11, new ItemStack(Ic2Items.smallLeadDust.getItem(), 2, 10));
        addrecipe(12, new ItemStack(IUItem.smalldust, 2, 10));
        addrecipe(14, new ItemStack(IUItem.smalldust, 2, 14));
        addrecipe(15, new ItemStack(Ic2Items.smallLeadDust.getItem(), 2, 10));
        addrecipe(16, null);
        addrecipe(17, new ItemStack(IUItem.smalldust, 2, 9));
        addrecipe(18, new ItemStack(IUItem.smalldust, 2, 15));
    }

    public static void addrecipe(int meta, ItemStack output) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        ItemStack[] stack;
        if (output != null) {
            stack = new ItemStack[3];
        } else {
            stack = new ItemStack[2];

        }
        stack[0] = new ItemStack(IUItem.purifiedcrushed, 1, meta);
        stack[1] = Ic2Items.stoneDust;
        if (output != null) {
            stack[2] = output;
        }
        NBTTagCompound nbt = ModUtils.nbt();
        nbt.setInteger("amount", 1000);
        Recipes.oreWashing.addRecipe(input1.forStack(new ItemStack(IUItem.crushed, 1, meta)), nbt, false, stack);
    }

}
