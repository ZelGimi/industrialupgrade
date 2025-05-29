package com.denfop.tiles.wiring.storage;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEnergyStorage;
import com.denfop.tiles.base.TileElectricBlock;
import com.denfop.tiles.wiring.EnumElectricBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileElectricMFSU extends TileElectricBlock {

    public TileElectricMFSU(BlockPos pos, BlockState state) {
        super(EnumElectricBlock.MFSU, BlockEnergyStorage.mfsu_iu, pos, state);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockEnergyStorage.mfsu_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.electricblock.getBlock(getTeBlock().getId());
    }

}
