package com.denfop.tiles.reactors.heat.circulationpump;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityPerCirculationPump extends TileEntityBaseCirculationPump {

    public TileEntityPerCirculationPump(BlockPos pos, BlockState state) {
        super(3,BlockHeatReactor.heat_per_circulationpump,pos,state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_per_circulationpump;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor.getBlock(getTeBlock());
    }

}
