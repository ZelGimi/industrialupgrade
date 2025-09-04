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
        addRecipe("c:dusts/Coal", new ItemStack(Items.DIAMOND), 1000000D);

        addRecipe("c:ingots/Copper", Recipes.inputFactory.getInput("c:ingots/Nickel").getInputs().get(0), 450000D);

        addRecipe("c:ingots/Lead", Recipes.inputFactory.getInput("c:ingots/Gold").getInputs().get(0), 450000D);


        addRecipe("c:ingots/Tin", Recipes.inputFactory.getInput("c:ingots/Silver").getInputs().get(0), 800000D);


        addRecipe("c:ingots/Silver",
                Recipes.inputFactory.getInput("c:ingots/Tungsten").getInputs().get(0), 700000D
        );

        addRecipe("c:ingots/Mikhail",
                Recipes.inputFactory.getInput("c:ingots/Magnesium").getInputs().get(0), 700000D
        );

        addRecipe("c:ingots/Magnesium", Recipes.inputFactory.getInput("c:ingots/Caravky").getInputs().get(0), 900000D);

        addRecipe("c:ingots/Manganese", Recipes.inputFactory.getInput("c:ingots/Cobalt").getInputs().get(0), 350000);


        addRecipe("c:ingots/Caravky", new ItemStack(IUItem.iuingot.getStack(18), 1), 600000);
        addRecipe("c:ingots/Cobalt", new ItemStack(IUItem.iuingot.getStack(16), 1), 350000);
        addRecipe("c:ingots/Germanium", new ItemStack(IUItem.iuingot.getStack(15), 1), 300000);

        addRecipe("c:ingots/Spinel", Recipes.inputFactory.getInput("c:ingots/Iridium").getInputs().get(0), 2500000D);

        addRecipe("c:ingots/Tungsten",
                Recipes.inputFactory.getInput("c:ingots/Spinel").getInputs().get(0), 800000D
        );

        addRecipe("c:ingots/Chromium",
                Recipes.inputFactory.getInput("c:ingots/Mikhail").getInputs().get(0), 900000D
        );

        addRecipe("c:ingots/Platinum",
                Recipes.inputFactory.getInput("c:ingots/Chromium").getInputs().get(0), 600000D
        );

        addRecipe("c:ingots/Gold", Recipes.inputFactory.getInput("c:ingots/Platinum").getInputs().get(0), 800000D);

    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.earth_assembler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


}
