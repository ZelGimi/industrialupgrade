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
import net.minecraft.world.level.block.state.BlockState;

public class TileEnderAssembler extends TileBaseWorldCollector {

    public TileEnderAssembler(BlockPos pos, BlockState state) {
        super(EnumTypeCollector.END, BlockBaseMachine3.ender_assembler, pos, state);
    }

    public void init() {

        addRecipe(new ItemStack(Items.MAGMA_CREAM), 32, new ItemStack(Items.ENDER_PEARL));
        addRecipe(new ItemStack(Items.APPLE), 64, new ItemStack(Items.CHORUS_FRUIT));
        addRecipe(new ItemStack(Items.SKELETON_SKULL), 64, new ItemStack(Items.DRAGON_HEAD));
        addRecipe(new ItemStack(Items.GLASS_BOTTLE), 64, new ItemStack(Items.EXPERIENCE_BOTTLE));

    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.ender_assembler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


}
