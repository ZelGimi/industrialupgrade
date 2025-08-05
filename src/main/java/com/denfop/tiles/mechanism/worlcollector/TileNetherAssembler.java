package com.denfop.tiles.mechanism.worlcollector;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.tiles.base.EnumTypeCollector;
import com.denfop.tiles.base.TileBaseWorldCollector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class TileNetherAssembler extends TileBaseWorldCollector {

    public TileNetherAssembler(BlockPos pos, BlockState state) {
        super(EnumTypeCollector.NETHER, BlockBaseMachine3.nether_assembler, pos, state);
    }

    public void init() {

        addRecipe(new ItemStack(Blocks.SAND), 0.75, new ItemStack(Blocks.SOUL_SAND));
        addRecipe(new ItemStack(Items.IRON_INGOT), 16, new ItemStack(Items.QUARTZ));
        addRecipe(new ItemStack(Items.BONE), 16, new ItemStack(Items.BLAZE_ROD));
        addRecipe(new ItemStack(Items.WHEAT_SEEDS), 16, new ItemStack(Items.NETHER_WART));
        addRecipe(new ItemStack(Blocks.COBBLESTONE), 0.75, new ItemStack(Blocks.NETHERRACK));
        addRecipe(new ItemStack(Items.GOLD_INGOT), 16, new ItemStack(Blocks.GLOWSTONE));
        addRecipe(new ItemStack(Items.SKELETON_SKULL), 32, new ItemStack(Items.WITHER_SKELETON_SKULL, 1));
        addRecipe(new ItemStack(Items.LAVA_BUCKET), 4, new ItemStack(Blocks.MAGMA_BLOCK));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.nether_assembler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


}
