package com.denfop.tiles.mechanism.solarium_storage;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityImpSolariumStorage extends TileEntitySolariumStorage {

    public TileEntityImpSolariumStorage(BlockPos pos, BlockState state) {
        super(1600000, EnumTypeStyle.IMPROVED,BlockBaseMachine3.adv_solarium_storage,pos,state);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.imp_solarium_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
