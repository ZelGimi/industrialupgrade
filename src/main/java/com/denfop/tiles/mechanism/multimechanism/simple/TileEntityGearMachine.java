package com.denfop.tiles.mechanism.multimechanism.simple;

import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.register.RegisterOreDictionary;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.IRecipeInputFactory;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityGearMachine extends TileEntityMultiMachine {

    public TileEntityGearMachine() {
        super(EnumMultiMachine.Gearing.usagePerTick, EnumMultiMachine.Gearing.lenghtOperation, 3);
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addrecipe(String input, String output) {
        final IRecipeInputFactory input1 = ic2.api.recipe.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "gearing",
                new BaseMachineRecipe(
                        new Input(
                                input1.forOreDict(input, 4)
                        ),
                        new RecipeOutput(null, OreDictionary.getOres(output).get(0))
                )
        );
    }

    public void init() {
        for (int i = 0; i < RegisterOreDictionary.itemNames().size(); i++) {

            addrecipe(
                    "ingot" + RegisterOreDictionary.itemNames().get(i),
                    "gear" + RegisterOreDictionary.itemNames().get(i)
            );


        }
        for (int i = 0; i < RegisterOreDictionary.itemNames1().size(); i++) {

            addrecipe(
                    "ingot" + RegisterOreDictionary.itemNames1().get(i),
                    "gear" + RegisterOreDictionary.itemNames1().get(i)
            );


        }
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Gearing;
    }

}
