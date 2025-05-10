package com.denfop.tiles.gaswell;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasWell;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityGasWellDrill extends TileEntityMultiBlockElement implements IDrill {


    public TileEntityGasWellDrill(BlockPos pos, BlockState state) {
        super(BlockGasWell.gas_well_drill, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasWell.gas_well_drill;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_well.getBlock(getTeBlock());
    }

}
