package com.denfop.tiles.lightning_rod;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockLightningRod;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityLightningRodGrounding extends TileEntityMultiBlockElement implements IGrounding {


    public TileEntityLightningRodGrounding(BlockPos pos, BlockState state) {
        super(BlockLightningRod.lightning_rod_grounding, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockLightningRod.lightning_rod_grounding;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.lightning_rod.getBlock(getTeBlock());
    }

}
