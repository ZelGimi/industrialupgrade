package com.denfop.blockentity.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileQuarryPipe extends BlockEntityBase {

    public TileQuarryPipe(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockBaseMachine3Entity.quarry_pipe, p_155229_, p_155230_);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.quarry_pipe;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


}
