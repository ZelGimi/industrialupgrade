package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class OreWashingRecipe {

    public static void init() {
        addrecipe(0, null);
        addrecipe(1, new ItemStack(Blocks.SAND));
        addrecipe(2, new ItemStack(IUItem.smalldust.getStack(10), 2));
        addrecipe(3, new ItemStack(IUItem.smalldust.getStack(16), 2));
        addrecipe(6, new ItemStack(IUItem.smalldust.getStack(1), 2));
        addrecipe(7, new ItemStack(IUItem.smalldust.getStack(16), 2));
        addrecipe(8, new ItemStack(IUItem.smalldust.getStack(0), 2));
        addrecipe(9, new ItemStack(IUItem.smalldust.getStack(2), 2));
        addrecipe(10, new ItemStack(IUItem.smalldust.getStack(0), 2));
        addrecipe(11, new ItemStack(IUItem.smalldust.getStack(10), 2));
        addrecipe(12, new ItemStack(IUItem.smalldust.getStack(10), 2));
        addrecipe(14, new ItemStack(IUItem.smalldust.getStack(14), 2));
        addrecipe(15, new ItemStack(IUItem.smalldust.getStack(10), 2));
        addrecipe(16, null);
        addrecipe(17, new ItemStack(IUItem.smalldust.getStack(9), 2));
        addrecipe(18, new ItemStack(IUItem.smalldust.getStack(15), 2));
        addrecipe(new ItemStack(Blocks.GRAVEL));
        addrecipe(
                "forge:crushed/Iron",
                "forge:purifiedcrushed/Iron",
                new ItemStack(IUItem.smallGoldDust.getItem(), 2)
        );
        addrecipe("forge:crushed/Tin", "forge:purifiedcrushed/Tin", new ItemStack(IUItem.smallTinDust.getItem(), 2
        ));
        addrecipe("forge:crushed/Gold", "forge:purifiedcrushed/Gold",
                new ItemStack(IUItem.smallGoldDust.getItem(), 2)
        );
        addrecipe("forge:crushed/Uranium", "forge:purifiedcrushed/Uranium",
                new ItemStack(IUItem.smallLeadDust.getItem(), 2)
        );
        addrecipe("forge:crushed/Copper", "forge:purifiedcrushed/Copper",
                new ItemStack(IUItem.smallLeadDust.getItem(), 2)
        );
        addrecipe("forge:crushed/Lead", "forge:purifiedcrushed/Lead",
                new ItemStack(IUItem.smalldust.getStack(14), 2)
        );
        addrecipe("forge:crushed/Osmium", "forge:purifiedcrushed/Osmium",
                new ItemStack(IUItem.smalldust.getStack(17), 1)
        );
        addrecipe("forge:crushed/Tantalum", "forge:purifiedcrushed/Tantalum",
                new ItemStack(IUItem.smalldust.getStack(11), 1)
        );
        addrecipe("forge:crushed/Cadmium", "forge:purifiedcrushed/Cadmium",
                new ItemStack(IUItem.smalldust.getStack(18), 1)
        );


        addrecipe(28, new ItemStack(IUItem.smalldust.getStack(26), 2));
        addrecipe(29, new ItemStack(IUItem.smalldust.getStack(27), 2));
        addrecipe(30, new ItemStack(IUItem.smalldust.getStack(24), 2));
        addrecipe(31, new ItemStack(IUItem.smalldust.getStack(46), 2));
        addrecipe(32, new ItemStack(IUItem.smalldust.getStack(27), 2));
        addrecipe(33, new ItemStack(IUItem.smalldust.getStack(10), 2));
        addrecipe(34, new ItemStack(IUItem.smalldust.getStack(26), 2));
        addrecipe(35, new ItemStack(IUItem.smalldust.getStack(24), 2));
        addrecipe(36, new ItemStack(IUItem.smalldust.getStack(35), 2));
        addrecipe(37, new ItemStack(IUItem.smalldust.getStack(45), 2));
        addrecipe(38, new ItemStack(IUItem.smalldust.getStack(9), 2));
        addrecipe(39, new ItemStack(IUItem.smalldust.getStack(42), 2));
        addrecipe(40, new ItemStack(IUItem.smalldust.getStack(1), 2));
        addrecipe(41, new ItemStack(IUItem.smalldust.getStack(14), 2));
        addrecipe(42, new ItemStack(IUItem.smalldust.getStack(27), 2));
    }

    public static void addrecipe(ItemStack input) {
        final IInputHandler input1 = Recipes.inputFactory;
        CompoundTag nbt = ModUtils.nbt();
        nbt.putInt("amount", 1000);
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
        stack[0] = new ItemStack(IUItem.purifiedcrushed.getStack(meta), 1);
        stack[1] = IUItem.stoneDust;
        if (output != null) {
            stack[2] = output;
        }
        CompoundTag nbt = ModUtils.nbt();
        nbt.putInt("amount", 1000);
        com.denfop.api.Recipes.recipes.addRecipe(
                "orewashing",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(new ItemStack(IUItem.crushed.getStack(meta), 1))
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
        stack[0] = input1.getInput(purifiedcrushed).getInputs().get(0);
        stack[1] = IUItem.stoneDust;
        if (output != null) {
            stack[2] = output;
        }
        CompoundTag nbt = ModUtils.nbt();
        nbt.putInt("amount", 1000);
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
