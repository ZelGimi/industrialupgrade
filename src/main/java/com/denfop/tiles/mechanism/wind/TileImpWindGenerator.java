package com.denfop.tiles.mechanism.wind;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileImpWindGenerator extends TileWindGenerator {


    public TileImpWindGenerator(BlockPos pos, BlockState state) {
        super(EnumLevelGenerators.THREE,BlockBaseMachine3.imp_wind_generator,pos,state);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.imp_wind_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
