package com.denfop.tiles.wiring.chargepad;


import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockChargepadStorage;
import com.denfop.tiles.base.TileElectricBlock;
import com.denfop.tiles.wiring.EnumElectricBlock;

public class TileChargepadBatBox extends TileElectricBlock {

    public TileChargepadBatBox() {
        super(EnumElectricBlock.BATBOX_CHARGEPAD);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockChargepadStorage.batbox_iu_chargepad;
    }

    public BlockTileEntity getBlock() {
        return IUItem.chargepadelectricblock;
    }

}
