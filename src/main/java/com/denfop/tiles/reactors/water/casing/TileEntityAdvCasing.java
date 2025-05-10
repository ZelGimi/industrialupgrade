package com.denfop.tiles.reactors.water.casing;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.water.ICasing;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityAdvCasing extends TileEntityMultiBlockElement implements ICasing {

    public TileEntityAdvCasing(BlockPos pos, BlockState state) {
        super(BlockWaterReactors.water_adv_casing, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_adv_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component.getBlock(getTeBlock());
    }

    @Override
    public int getBlockLevel() {
        return 1;
    }

}
