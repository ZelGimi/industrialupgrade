package com.denfop.blockentity.mechanism.worlcollector;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBaseWorldCollector;
import com.denfop.blockentity.base.EnumTypeCollector;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAquaAssembler extends BlockEntityBaseWorldCollector {

    public BlockEntityAquaAssembler(BlockPos pos, BlockState state) {
        super(EnumTypeCollector.AQUA, BlockBaseMachine3Entity.aqua_assembler, pos, state);
    }

    public void init() {

        addRecipe(new ItemStack(Items.LIME_DYE), 32, new ItemStack(Items.SLIME_BALL));
        addRecipe(new ItemStack(Items.YELLOW_DYE), 8, new ItemStack(Items.WHITE_DYE));
        addRecipe(new ItemStack(Items.WATER_BUCKET), 8, new ItemStack(Items.ICE));
        addRecipe(new ItemStack(Items.COD), 8, new ItemStack(Items.SALMON));
        addRecipe(new ItemStack(Items.SALMON), 8, new ItemStack(Items.TROPICAL_FISH));
        addRecipe(new ItemStack(Items.TROPICAL_FISH), 8, new ItemStack(Items.PUFFERFISH));

        addRecipe(new ItemStack(Items.BLUE_WOOL), 32, new ItemStack(Items.LAPIS_BLOCK));
        addRecipe(new ItemStack(Items.LIME_WOOL), 128, new ItemStack(Items.EMERALD_BLOCK));
        addRecipe(new ItemStack(Items.LIGHT_BLUE_WOOL), 128, new ItemStack(Items.DIAMOND_BLOCK));
        addRecipe(new ItemStack(Items.RED_WOOL), 32, new ItemStack(Items.REDSTONE_BLOCK));
        addRecipe(new ItemStack(Items.YELLOW_WOOL), 64, new ItemStack(Items.GOLD_BLOCK));
        addRecipe(new ItemStack(Items.LIGHT_GRAY_WOOL), 64, new ItemStack(Items.IRON_BLOCK));
        addRecipe(new ItemStack(Items.BLACK_WOOL), 32, new ItemStack(Items.COAL_BLOCK));

        addRecipe(new ItemStack(Items.WHEAT_SEEDS), 2, new ItemStack(Items.SUGAR_CANE));


    }


    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.aqua_assembler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
