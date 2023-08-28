package com.denfop.tiles.mechanism.multimechanism.simple;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine1;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class TileCombMacerator extends TileMultiMachine {

    public static List<String> ores = new ArrayList<>();

    public TileCombMacerator() {
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
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "comb_macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, stack)
                )
        );

    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine1.comb_macerator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base1;
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
                    if (!ores.contains(name)) {
                        addrecipe(name1, name);
                        ores.add(name);
                    }
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
