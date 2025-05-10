package com.denfop.tiles.transport.tiles.radiation;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockRadPipe;
import com.denfop.tiles.transport.types.RadTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityRadiationPipe extends com.denfop.tiles.transport.tiles.TileEntityRadPipes {
    public TileEntityRadiationPipe(BlockPos pos, BlockState state) {
        super(RadTypes.rad_cable, BlockRadPipe.radiation, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockRadPipe.radiation;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.radcable_item.getBlock(getTeBlock().getId());
    }
}
