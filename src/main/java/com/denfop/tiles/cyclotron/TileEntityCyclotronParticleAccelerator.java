package com.denfop.tiles.cyclotron;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCyclotron;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;

public class TileEntityCyclotronParticleAccelerator extends TileEntityMultiBlockElement implements IParticleAccelerator {

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockCyclotron.cyclotron_particle_accelerator;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.cyclotron;
    }

}
