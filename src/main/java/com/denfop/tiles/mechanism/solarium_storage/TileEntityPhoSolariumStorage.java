package com.denfop.tiles.mechanism.solarium_storage;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityPhoSolariumStorage extends TileEntitySolariumStorage {

    public TileEntityPhoSolariumStorage(BlockPos pos, BlockState state) {
        super(128000000, EnumTypeStyle.PHOTONIC, BlocksPhotonicMachine.photonic_solarium_storage, pos, state);
    }

    public IMultiTileBlock getTeBlock() {
        return BlocksPhotonicMachine.photonic_solarium_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine.getBlock(getTeBlock().getId());
    }

}
