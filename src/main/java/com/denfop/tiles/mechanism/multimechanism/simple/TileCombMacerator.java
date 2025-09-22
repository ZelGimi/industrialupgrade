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
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class TileCombMacerator extends TileMultiMachine {

    public static List<String> ores = new ArrayList<>();
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileCombMacerator() {
        super(
                EnumMultiMachine.COMB_MACERATOR.usagePerTick,
                EnumMultiMachine.COMB_MACERATOR.lenghtOperation
        );
        Recipes.recipes.addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.15));
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

            if (name.startsWith("crushed") && !name.startsWith("purifiedcrushed")) {
                String name1 = name.substring("crushed".length());
                if (name1.startsWith("Uranium")) {
                    continue;
                }
                name1 = "ore" + name1;
                final String name2 = "raw" + name.substring("crushed".length());
                if (!OreDictionary
                        .getOres(name1)
                        .isEmpty() && !OreDictionary
                        .getOres(name).isEmpty()) {
                    if (!OreDictionary
                            .getOres(name2).isEmpty()) {
                        if (!ores.contains(name)) {
                            addrecipe(name2, name);
                            ores.add(name);
                        }
                    } else {
                        if (!ores.contains(name)) {
                            addrecipe(name1, name);
                            ores.add(name);
                        }
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
