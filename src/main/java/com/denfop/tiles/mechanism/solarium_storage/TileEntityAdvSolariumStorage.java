package com.denfop.tiles.mechanism.solarium_storage;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityAdvSolariumStorage extends TileEntitySolariumStorage {

    public TileEntityAdvSolariumStorage(BlockPos pos, BlockState state) {
        super(400000, EnumTypeStyle.ADVANCED, BlockBaseMachine3.adv_solarium_storage, pos, state);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.adv_solarium_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
