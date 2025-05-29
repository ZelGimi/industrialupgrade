package com.denfop.tiles.se;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSolarEnergy;
import com.denfop.tiles.base.TileSolarGeneratorEnergy;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileSolarGenerator extends TileSolarGeneratorEnergy {


    public TileSolarGenerator(BlockPos pos, BlockState state) {

        super(1, BlockSolarEnergy.se_gen, pos, state);

    }

    public IMultiTileBlock getTeBlock() {
        return BlockSolarEnergy.se_gen;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blockSE.getBlock();
    }


}
