package com.denfop.tiles.mechanism.multimechanism.simple;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine2;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileExtruding extends TileMultiMachine {

    public TileExtruding() {
        super(EnumMultiMachine.Extruding.usagePerTick, EnumMultiMachine.Rolling.lenghtOperation, 2);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine2.extruder;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base2;
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Extruding;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockExtruding.name");
    }

    public String getStartSoundFile() {
        return "Machines/extruder.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
