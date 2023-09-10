package com.denfop.tiles.mechanism.multimechanism.simple;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine2;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileRolling extends TileMultiMachine {

    public TileRolling() {
        super(EnumMultiMachine.Rolling.usagePerTick, EnumMultiMachine.Rolling.lenghtOperation, 2);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine2.rolling;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base2;
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Rolling;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockRolling.name");
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public String getStartSoundFile() {
        return "Machines/plate.ogg";
    }

}
