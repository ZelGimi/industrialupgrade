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

public class BlockEntityAerAssembler extends BlockEntityBaseWorldCollector {

    public BlockEntityAerAssembler(BlockPos pos, BlockState state) {
        super(EnumTypeCollector.AER, BlockBaseMachine3Entity.aer_assembler, pos, state);
    }

    public void init() {
        addRecipe(new ItemStack(Items.OAK_SAPLING), getMatterFromEnergy(500000), new ItemStack(Items.SPRUCE_SAPLING));
        addRecipe(new ItemStack(Items.SPRUCE_SAPLING), getMatterFromEnergy(500000), new ItemStack(Items.BIRCH_SAPLING));
        addRecipe(new ItemStack(Items.BIRCH_SAPLING), getMatterFromEnergy(500000), new ItemStack(Items.JUNGLE_SAPLING));
        addRecipe(new ItemStack(Items.JUNGLE_SAPLING), getMatterFromEnergy(500000), new ItemStack(Items.ACACIA_SAPLING));
        addRecipe(new ItemStack(Items.ACACIA_SAPLING), getMatterFromEnergy(500000), new ItemStack(Items.DARK_OAK_SAPLING));
        addRecipe(new ItemStack(Items.DARK_OAK_SAPLING), getMatterFromEnergy(500000), new ItemStack(Items.OAK_SAPLING));

        addRecipe(new ItemStack(Items.WHEAT), getMatterFromEnergy(125000), new ItemStack(Items.HAY_BLOCK));
        addRecipe(new ItemStack(Items.LIGHT_BLUE_WOOL), getMatterFromEnergy(4000000), new ItemStack(Items.SPONGE));
        addRecipe(new ItemStack(Items.WHITE_DYE), getMatterFromEnergy(4000000), new ItemStack(Items.FEATHER));
        addRecipe(new ItemStack(Items.WHEAT_SEEDS), getMatterFromEnergy(20000000), new ItemStack(Items.MELON_SEEDS));
        addRecipe(new ItemStack(Items.MELON_SEEDS), getMatterFromEnergy(2000000), new ItemStack(Items.PUMPKIN_SEEDS));


    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.aer_assembler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


}
