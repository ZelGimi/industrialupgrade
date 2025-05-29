package com.denfop.tiles.transport.tiles.biopipe;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBioPipe;
import com.denfop.tiles.transport.types.BioType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityBioPipe extends com.denfop.tiles.transport.tiles.TileEntityBioPipe {
    public TileEntityBioPipe(BlockPos pos, BlockState state) {
        super(BioType.bpipe, BlockBioPipe.biofuel, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBioPipe.biofuel;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.biopipes.getBlock(getTeBlock().getId());
    }
}
