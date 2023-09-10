package com.denfop.tiles.mechanism.multimechanism.triple;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine2;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileTripleCutting extends TileMultiMachine {

    public TileTripleCutting() {
        super(
                EnumMultiMachine.TRIPLE_Cutting.usagePerTick,
                EnumMultiMachine.TRIPLE_Cutting.lenghtOperation,
                2
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine2.triple_cutting;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base2;
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_Cutting;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCutting2.name");
    }

    public String getStartSoundFile() {
        return "Machines/cutter.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
