package com.denfop.tiles.mechanism.wind;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileAdvWindGenerator extends TileWindGenerator {


    public TileAdvWindGenerator(BlockPos pos, BlockState state) {
        super(EnumLevelGenerators.TWO, BlockBaseMachine3.adv_wind_generator, pos, state);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.adv_wind_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
