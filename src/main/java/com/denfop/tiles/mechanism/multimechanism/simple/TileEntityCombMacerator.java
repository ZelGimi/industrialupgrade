package com.denfop.tiles.mechanism.multimechanism.simple;

import com.denfop.IUCore;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.core.init.Localization;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityCombMacerator extends TileEntityMultiMachine {

    public TileEntityCombMacerator() {
        super(
                EnumMultiMachine.COMB_MACERATOR.usagePerTick,
                EnumMultiMachine.COMB_MACERATOR.lenghtOperation,
                1
        );
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addrecipe(String input, String output) {
        ItemStack stack;

        stack = OreDictionary.getOres(output).get(0).copy();


        stack.setCount(3);
        IUCore.get_comb_crushed.add(stack);
        final IRecipeInputFactory input1 = ic2.api.recipe.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "comb_macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.forOreDict(input)
                        ),
                        new RecipeOutput(null, stack)
                )
        );

    }

    public void init() {

        for (String name : OreDictionary.getOreNames()) {

            if (name.startsWith("crushed") && !name.startsWith("crushedPurified")) {

                String name1 = name.substring("crushed".length());

                name1 = "ore" + name1;

                if (OreDictionary
                        .getOres(name1)
                        .size() > 0 && OreDictionary.getOres(name1) != null && OreDictionary.getOres(name) != null && OreDictionary
                        .getOres(name)
                        .size() > 0) {
                    addrecipe(name1, name);
                }

            }
        }
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.COMB_MACERATOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCombMacerator.name");
    }

    public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
