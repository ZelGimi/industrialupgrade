package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.blocks.FluidName;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

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
                        new RecipeOutput(null, OreDictionary.getOres(output).get(0))
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
                "blockWool",
                1,
                new ItemStack(Blocks.WOOL)
        );
        addextractor(
                new ItemStack(Blocks.NETHER_BRICK),
                1,
                new ItemStack(Items.NETHERBRICK, 4)
        );
        addextractor(
                IUItem.latex,
                IUItem.rubber, 3
        );
        addextractor(
                new ItemStack(IUItem.rubWood),
                IUItem.rubber
        );
        addextractor(
                new ItemStack(Blocks.CLAY),
                1,
                new ItemStack(Items.CLAY_BALL, 4)
        );
        addextractor(
                new ItemStack(IUItem.rubberSapling),
                IUItem.rubber
        );
        addextractor(
                ModUtils.getCellFromFluid(FluidName.fluidair.getInstance()),
                IUItem.FluidCell
        );
        addextractor(
                new ItemStack(Blocks.BRICK_BLOCK),
                1,
                new ItemStack(Items.BRICK, 4)
        );
        addextractor(
                new ItemStack(Items.GUNPOWDER),
                1,
                IUItem.sulfurDust
        );

    }

}
