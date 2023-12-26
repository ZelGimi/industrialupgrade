package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class OreWashingRecipe {

    public static void init() {
        addrecipe(0, null);
        addrecipe(1, new ItemStack(Blocks.SAND));
        addrecipe(2, new ItemStack(IUItem.smallLeadDust.getItem(), 2, 10));
        addrecipe(3, new ItemStack(IUItem.smalldust, 2, 16));
        addrecipe(6, new ItemStack(IUItem.smallLeadDust.getItem(), 2, 1));
        addrecipe(7, new ItemStack(IUItem.smalldust, 2, 16));
        addrecipe(8, new ItemStack(IUItem.smallLeadDust.getItem(), 2, 0));
        addrecipe(9, new ItemStack(IUItem.smallLeadDust.getItem(), 2, 2));
        addrecipe(10, new ItemStack(IUItem.smallLeadDust.getItem(), 2, 0));
        addrecipe(11, new ItemStack(IUItem.smallLeadDust.getItem(), 2, 10));
        addrecipe(12, new ItemStack(IUItem.smalldust, 2, 10));
        addrecipe(14, new ItemStack(IUItem.smalldust, 2, 14));
        addrecipe(15, new ItemStack(IUItem.smallLeadDust.getItem(), 2, 10));
        addrecipe(16, null);
        addrecipe(17, new ItemStack(IUItem.smalldust, 2, 9));
        addrecipe(18, new ItemStack(IUItem.smalldust, 2, 15));
        addrecipe(new ItemStack(Blocks.GRAVEL));
        addrecipe(
                "crushedIron",
                "crushedPurifiedIron",
                new ItemStack(IUItem.smallGoldDust.getItem(), 2, IUItem.smallGoldDust.getItemDamage())
        );
        addrecipe("crushedTin", "crushedPurifiedTin", new ItemStack(IUItem.smallTinDust.getItem(), 2,
                IUItem.smallTinDust.getItemDamage()
        ));
        addrecipe("crushedGold", "crushedPurifiedGold",
                new ItemStack(IUItem.smallGoldDust.getItem(), 2, IUItem.smallGoldDust.getItemDamage())
        );
        addrecipe("crushedUranium", "crushedPurifiedUranium",
                new ItemStack(IUItem.smallLeadDust.getItem(), 2, IUItem.smallLeadDust.getItemDamage())
        );
        addrecipe("crushedCopper", "crushedPurifiedCopper",
                new ItemStack(IUItem.smallLeadDust.getItem(), 2, IUItem.smallCopperDust.getItemDamage())
        );
        addrecipe("crushedLead", "crushedPurifiedLead",
                new ItemStack(IUItem.smallLeadDust.getItem(), 2, IUItem.smallSulfurDust.getItemDamage())
        );
        addrecipe("crushedOsmium", "crushedPurifiedOsmium",
                new ItemStack(IUItem.smalldust,1,17)
        );
        addrecipe("crushedTantalum", "crushedPurifiedTantalum",
                new ItemStack(IUItem.smalldust,1,11)
        );
        addrecipe("crushedCadmium", "crushedPurifiedCadmium",
                new ItemStack(IUItem.smalldust,1,18)
        );
    }

    public static void addrecipe(ItemStack input) {
        final IInputHandler input1 = Recipes.inputFactory;
        NBTTagCompound nbt = ModUtils.nbt();
        nbt.setInteger("amount", 1000);
        com.denfop.api.Recipes.recipes.addRecipe(
                "orewashing",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(nbt, IUItem.stoneDust)
                )
        );
    }

    public static void addrecipe(int meta, ItemStack output) {
        final IInputHandler input1 = Recipes.inputFactory;
        ItemStack[] stack;
        if (output != null) {
            stack = new ItemStack[3];
        } else {
            stack = new ItemStack[2];

        }
        stack[0] = new ItemStack(IUItem.purifiedcrushed, 1, meta);
        stack[1] = IUItem.stoneDust;
        if (output != null) {
            stack[2] = output;
        }
        NBTTagCompound nbt = ModUtils.nbt();
        nbt.setInteger("amount", 1000);
        com.denfop.api.Recipes.recipes.addRecipe(
                "orewashing",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(new ItemStack(IUItem.crushed, 1, meta))
                        ),
                        new RecipeOutput(nbt, stack)
                )
        );
    }

    public static void addrecipe(String input, String purifiedcrushed, ItemStack output) {
        final IInputHandler input1 = Recipes.inputFactory;
        ItemStack[] stack;
        if (output != null) {
            stack = new ItemStack[3];
        } else {
            stack = new ItemStack[2];

        }
        stack[0] = OreDictionary.getOres(purifiedcrushed).get(0);
        stack[1] = IUItem.stoneDust;
        if (output != null) {
            stack[2] = output;
        }
        NBTTagCompound nbt = ModUtils.nbt();
        nbt.setInteger("amount", 1000);
        com.denfop.api.Recipes.recipes.addRecipe(
                "orewashing",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(nbt, stack)
                )
        );
    }

}
