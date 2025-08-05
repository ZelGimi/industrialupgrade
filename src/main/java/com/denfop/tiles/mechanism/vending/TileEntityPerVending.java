package com.denfop.tiles.mechanism.vending;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityPerVending extends TileEntityBaseVending {

    public TileEntityPerVending(BlockPos pos, BlockState state) {
        super(EnumTypeStyle.PERFECT, BlockBaseMachine3.per_vending, pos, state);
    }


    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.per_vending;
    }

}
