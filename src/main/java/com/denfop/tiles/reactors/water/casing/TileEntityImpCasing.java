package com.denfop.tiles.reactors.water.casing;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.water.ICasing;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityImpCasing extends TileEntityMultiBlockElement implements ICasing {

    public TileEntityImpCasing(BlockPos pos, BlockState state) {
        super(BlockWaterReactors.water_imp_casing, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_imp_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component.getBlock(getTeBlock());
    }

    @Override
    public int getBlockLevel() {
        return 2;
    }

}
