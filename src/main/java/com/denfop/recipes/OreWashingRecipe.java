package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.Recipes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class OreWashingRecipe {

    public static void init() {
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
        addrecipe(new ItemStack(Blocks.GRAVEL));
        addrecipe(
                "crushedIron",
                "crushedPurifiedIron",
                new ItemStack(Ic2Items.smallIronDust.getItem(), 2, Ic2Items.smallIronDust.getItemDamage())
        );
        addrecipe("crushedTin", "crushedPurifiedTin", new ItemStack(Ic2Items.smallTinDust.getItem(), 2,
                Ic2Items.smallTinDust.getItemDamage()
        ));
        addrecipe("crushedGold", "crushedPurifiedGold",
                new ItemStack(Ic2Items.smallGoldDust.getItem(), 2, Ic2Items.smallGoldDust.getItemDamage())
        );
        addrecipe("crushedUranium", "crushedPurifiedUranium",
                new ItemStack(Ic2Items.smallLeadDust.getItem(), 2, Ic2Items.smallLeadDust.getItemDamage())
        );
        addrecipe("crushedCopper", "crushedPurifiedCopper",
                new ItemStack(Ic2Items.smallLeadDust.getItem(), 2, Ic2Items.smallCopperDust.getItemDamage())
        );
        addrecipe("crushedLead", "crushedPurifiedLead",
                new ItemStack(Ic2Items.smallLeadDust.getItem(), 2, Ic2Items.smallSulfurDust.getItemDamage())
        );

    }

    public static void addrecipe(ItemStack input) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        NBTTagCompound nbt = ModUtils.nbt();
        nbt.setInteger("amount", 1000);
        com.denfop.api.Recipes.recipes.addRecipe(
                "orewashing",
                new BaseMachineRecipe(
                        new Input(
                                input1.forStack(input)
                        ),
                        new RecipeOutput(nbt, Ic2Items.stoneDust)
                )
        );
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
        com.denfop.api.Recipes.recipes.addRecipe(
                "orewashing",
                new BaseMachineRecipe(
                        new Input(
                                input1.forStack(new ItemStack(IUItem.crushed, 1, meta))
                        ),
                        new RecipeOutput(nbt, stack)
                )
        );
    }

    public static void addrecipe(String input, String purifiedcrushed, ItemStack output) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        ItemStack[] stack;
        if (output != null) {
            stack = new ItemStack[3];
        } else {
            stack = new ItemStack[2];

        }
        stack[0] = OreDictionary.getOres(purifiedcrushed).get(0);
        stack[1] = Ic2Items.stoneDust;
        if (output != null) {
            stack[2] = output;
        }
        NBTTagCompound nbt = ModUtils.nbt();
        nbt.setInteger("amount", 1000);
        com.denfop.api.Recipes.recipes.addRecipe(
                "orewashing",
                new BaseMachineRecipe(
                        new Input(
                                input1.forOreDict(input)
                        ),
                        new RecipeOutput(nbt, stack)
                )
        );
    }

}
