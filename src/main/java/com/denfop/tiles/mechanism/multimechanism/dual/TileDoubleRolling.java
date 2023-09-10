package com.denfop.tiles.mechanism.multimechanism.dual;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine2;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileDoubleRolling extends TileMultiMachine {

    public TileDoubleRolling() {
        super(EnumMultiMachine.Rolling.usagePerTick, EnumMultiMachine.Rolling.lenghtOperation, 2);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine2.double_rolling;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base2;
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_Rolling;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockRolling1.name");
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public String getStartSoundFile() {
        return "Machines/plate.ogg";
    }


}
