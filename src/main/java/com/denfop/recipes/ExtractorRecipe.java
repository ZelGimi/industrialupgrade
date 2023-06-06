package com.denfop.recipes;

import com.denfop.IUCore;
import com.denfop.Ic2Items;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.Recipes;
import ic2.core.item.type.DustResourceType;
import ic2.core.ref.ItemName;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ExtractorRecipe {

    public static void addextractor(ItemStack input, int n, ItemStack output) {

        final IRecipeInputFactory input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "extractor",
                new BaseMachineRecipe(
                        new Input(
                                input1.forStack(input, n)
                        ),
                        new RecipeOutput(null, output)
                )
        );


    }

    public static void addextractor(String input, int n, ItemStack output) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "extractor",
                new BaseMachineRecipe(
                        new Input(
                                input1.forOreDict(input, n)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addextractor(String input, int n, String output) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "extractor",
                new BaseMachineRecipe(
                        new Input(
                                input1.forOreDict(input, n)
                        ),
                        new RecipeOutput(null, OreDictionary.getOres(output).get(0))
                )
        );
    }

    public static void addextractor(ItemStack input, ItemStack output) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "extractor",
                new BaseMachineRecipe(
                        new Input(
                                input1.forStack(input, 1)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addextractor(ItemStack input, ItemStack output, int count) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        output = output.copy();
        output.setCount(count);
        com.denfop.api.Recipes.recipes.addRecipe(
                "extractor",
                new BaseMachineRecipe(
                        new Input(
                                input1.forStack(input, 1)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void init() {

        if (IUCore.isHasVersion("ic2", "220")) {
            addextractor(ItemName.dust.getItemStack(DustResourceType.netherrack), Ic2Items.smallSulfurDust);
        }
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
                Ic2Items.latex,
                Ic2Items.rubber, 3
        );
        addextractor(
                Ic2Items.rubberWood,
                Ic2Items.rubber
        );
        addextractor(
                new ItemStack(Blocks.CLAY),
                1,
                new ItemStack(Items.CLAY_BALL, 4)
        );
        addextractor(
                Ic2Items.rubberSapling,
                Ic2Items.rubber
        );
        addextractor(
                ModUtils.getCellFromFluid("ic2aer"),
                Ic2Items.FluidCell
        );
        addextractor(
                new ItemStack(Blocks.BRICK_BLOCK),
                1,
                new ItemStack(Items.BRICK, 4)
        );
        addextractor(
                new ItemStack(Items.GUNPOWDER),
                1,
                Ic2Items.sulfurDust
        );
        addextractor(
                Ic2Items.filledTinCan,
                Ic2Items.tinCan
        );
    }

}
