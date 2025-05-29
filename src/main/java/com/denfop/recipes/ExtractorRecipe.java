package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.blocks.FluidName;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class ExtractorRecipe {

    public static void addextractor(ItemStack input, int n, ItemStack output) {

        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "extractor",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, n)
                        ),
                        new RecipeOutput(null, output)
                )
        );


    }

    public static void addextractor(String input, int n, ItemStack output) {
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "extractor",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, n)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addextractor(String input, int n, String output) {
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "extractor",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, n)
                        ),
                        new RecipeOutput(null, input1.getInput(output).getInputs().get(0))
                )
        );
    }

    public static void addextractor(ItemStack input, ItemStack output) {
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "extractor",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, 1)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addextractor(ItemStack input, ItemStack output, int count) {
        final IInputHandler input1 = Recipes.inputFactory;
        output = output.copy();
        output.setCount(count);
        com.denfop.api.Recipes.recipes.addRecipe(
                "extractor",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, 1)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void init() {


        addextractor(
                new ItemStack(Blocks.SNOW),
                1,
                new ItemStack(Items.SNOWBALL, 4)
        );
        addextractor(
                new ItemStack(Items.REDSTONE),
                9,
                new ItemStack(IUItem.compressed_redstone.getItem(), 1)
        );
        addextractor(new ItemStack(IUItem.crafting_elements.getStack(481), 4), new ItemStack(Items.BONE, 2));

        addextractor(new ItemStack(IUItem.ore2.getItem(7)), new ItemStack(IUItem.crafting_elements.getStack(481), 2));
        addextractor(
                "wool",
                1,
                new ItemStack(Blocks.WHITE_WOOL)
        );
        addextractor(
                new ItemStack(Blocks.NETHER_BRICKS),
                1,
                new ItemStack(Items.NETHER_BRICK, 4)
        );
        addextractor(
                IUItem.latex,
                IUItem.rubber, 3
        );
        addextractor(
                new ItemStack(IUItem.crafting_elements.getStack(477), 4),
                new ItemStack(IUItem.ore2.getItem(2), 1), 1
        );
     /*   addextractor(
                new ItemStack(IUItem.crafting_elements,1,477),
                new ItemStack(IUItem.iudust,4,31)
        );*/

        addextractor(
                new ItemStack(Blocks.CLAY),
                1,
                new ItemStack(Items.CLAY_BALL, 4)
        );

        addextractor(
                ModUtils.getCellFromFluid(FluidName.fluidair.getInstance().get()),
                new ItemStack(IUItem.fluidCell.getItem())
        );
        addextractor(
                new ItemStack(Blocks.BRICKS),
                1,
                new ItemStack(Items.BRICK, 4)
        );


    }

}
