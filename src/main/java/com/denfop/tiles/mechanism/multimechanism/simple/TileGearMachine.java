package com.denfop.tiles.mechanism.multimechanism.simple;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.recipe.IInputHandler;
import com.denfop.register.RegisterOreDictionary;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;
import net.minecraftforge.oredict.OreDictionary;

public class TileGearMachine extends TileMultiMachine {

    public TileGearMachine() {
        super(EnumMultiMachine.Gearing.usagePerTick, EnumMultiMachine.Gearing.lenghtOperation, 3);
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addrecipe(String input, String output) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "gearing",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, 4)
                        ),
                        new RecipeOutput(null, OreDictionary.getOres(output).get(0))
                )
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine3.gearing;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3;
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
        addrecipe(
                "ingotOsmium",
                "gearOsmium"
        );
        addrecipe(
                "ingotTantalum",
                "gearTantalum"
        );
        addrecipe(
                "ingotCadmium",
                "gearCadmium"
        );
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Gearing;
    }

}
