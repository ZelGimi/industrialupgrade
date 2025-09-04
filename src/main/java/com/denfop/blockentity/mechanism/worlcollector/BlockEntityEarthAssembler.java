package com.denfop.blockentity.mechanism.worlcollector;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBaseWorldCollector;
import com.denfop.blockentity.base.EnumTypeCollector;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityEarthAssembler extends BlockEntityBaseWorldCollector {

    public BlockEntityEarthAssembler(BlockPos pos, BlockState state) {
        super(EnumTypeCollector.EARTH, BlockBaseMachine3Entity.earth_assembler, pos, state);
    }

    public void init() {
        addRecipe("forge:dusts/Coal", new ItemStack(Items.DIAMOND), 1000000D);

        addRecipe("forge:ingots/Copper", Recipes.inputFactory.getInput("forge:ingots/Nickel").getInputs().get(0), 450000D);

        addRecipe("forge:ingots/Lead", Recipes.inputFactory.getInput("forge:ingots/Gold").getInputs().get(0), 450000D);


        addRecipe("forge:ingots/Tin", Recipes.inputFactory.getInput("forge:ingots/Silver").getInputs().get(0), 800000D);


        addRecipe("forge:ingots/Silver",
                Recipes.inputFactory.getInput("forge:ingots/Tungsten").getInputs().get(0), 700000D
        );

        addRecipe("forge:ingots/Mikhail",
                Recipes.inputFactory.getInput("forge:ingots/Magnesium").getInputs().get(0), 700000D
        );

        addRecipe("forge:ingots/Magnesium", Recipes.inputFactory.getInput("forge:ingots/Caravky").getInputs().get(0), 900000D);

        addRecipe("forge:ingots/Manganese", Recipes.inputFactory.getInput("forge:ingots/Cobalt").getInputs().get(0), 350000);


        addRecipe("forge:ingots/Caravky", new ItemStack(IUItem.iuingot.getStack(18), 1), 600000);
        addRecipe("forge:ingots/Cobalt", new ItemStack(IUItem.iuingot.getStack(16), 1), 350000);
        addRecipe("forge:ingots/Germanium", new ItemStack(IUItem.iuingot.getStack(15), 1), 300000);

        addRecipe("forge:ingots/Spinel", Recipes.inputFactory.getInput("forge:ingots/Iridium").getInputs().get(0), 2500000D);

        addRecipe("forge:ingots/Tungsten",
                Recipes.inputFactory.getInput("forge:ingots/Spinel").getInputs().get(0), 800000D
        );

        addRecipe("forge:ingots/Chromium",
                Recipes.inputFactory.getInput("forge:ingots/Mikhail").getInputs().get(0), 900000D
        );

        addRecipe("forge:ingots/Platinum",
                Recipes.inputFactory.getInput("forge:ingots/Chromium").getInputs().get(0), 600000D
        );

        addRecipe("forge:ingots/Gold", Recipes.inputFactory.getInput("forge:ingots/Platinum").getInputs().get(0), 800000D);

    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.earth_assembler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


}
