package com.denfop.tiles.mechanism.multimechanism.simple;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine2;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileCutting extends TileMultiMachine {

    public TileCutting() {
        super(EnumMultiMachine.Cutting.usagePerTick, EnumMultiMachine.Cutting.lenghtOperation, 2);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine2.cutting;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base2;
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Cutting;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCutting.name");
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public String getStartSoundFile() {
        return "Machines/cutter.ogg";
    }


}
