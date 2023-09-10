package com.denfop.tiles.mechanism.multimechanism.dual;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileDoubleGearMachine extends TileMultiMachine {

    public TileDoubleGearMachine() {
        super(EnumMultiMachine.DOUBLE_Gearing.usagePerTick, EnumMultiMachine.DOUBLE_Gearing.lenghtOperation, 1);

    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine3.doublegearing;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_Gearing;
    }

}
