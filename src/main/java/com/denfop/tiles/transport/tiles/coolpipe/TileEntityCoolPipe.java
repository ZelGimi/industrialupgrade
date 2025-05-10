package com.denfop.tiles.transport.tiles.coolpipe;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCoolPipe;
import com.denfop.tiles.transport.tiles.TileEntityCoolPipes;
import com.denfop.tiles.transport.types.CoolType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityCoolPipe extends TileEntityCoolPipes {
    public TileEntityCoolPipe(BlockPos pos, BlockState state) {
        super(CoolType.cool, BlockCoolPipe.cool, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockCoolPipe.cool;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.coolpipes.getBlock(getTeBlock().getId());
    }
}
