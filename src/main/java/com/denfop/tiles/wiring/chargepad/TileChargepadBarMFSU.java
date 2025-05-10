package com.denfop.tiles.wiring.chargepad;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockChargepadStorage;
import com.denfop.tiles.base.TileElectricBlock;
import com.denfop.tiles.wiring.EnumElectricBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileChargepadBarMFSU extends TileElectricBlock {

    public TileChargepadBarMFSU(BlockPos pos, BlockState state) {
        super(EnumElectricBlock.BAR_MFSU_CHARGEPAD, BlockChargepadStorage.bar_mfsu_chargepad, pos, state);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockChargepadStorage.bar_mfsu_chargepad;
    }

    public BlockTileEntity getBlock() {
        return IUItem.chargepadelectricblock.getBlock(getTeBlock().getId());
    }
}
