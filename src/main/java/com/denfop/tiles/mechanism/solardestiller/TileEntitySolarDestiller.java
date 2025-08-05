package com.denfop.tiles.mechanism.solardestiller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntitySolarDestiller extends TileEntityBaseSolarDestiller {

    public TileEntitySolarDestiller(BlockPos pos, BlockState state) {
        super(EnumTypeStyle.DEFAULT, BlockBaseMachine3.solardestiller, pos, state);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.solardestiller;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
