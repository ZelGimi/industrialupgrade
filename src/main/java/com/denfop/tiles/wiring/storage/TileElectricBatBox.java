package com.denfop.tiles.wiring.storage;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEnergyStorage;
import com.denfop.tiles.base.TileElectricBlock;
import com.denfop.tiles.wiring.EnumElectricBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileElectricBatBox extends TileElectricBlock {

    public TileElectricBatBox(BlockPos pos, BlockState state) {
        super(EnumElectricBlock.BATBOX, BlockEnergyStorage.batbox_iu, pos, state);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockEnergyStorage.batbox_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.electricblock.getBlock(getTeBlock().getId());
    }

}
