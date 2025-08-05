package com.denfop.tiles.cyclotron;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCyclotron;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityCyclotronParticleAccelerator extends TileEntityMultiBlockElement implements IParticleAccelerator {

    public TileEntityCyclotronParticleAccelerator(BlockPos pos, BlockState state) {
        super(BlockCyclotron.cyclotron_particle_accelerator, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockCyclotron.cyclotron_particle_accelerator;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.cyclotron.getBlock(getTeBlock());
    }

}
