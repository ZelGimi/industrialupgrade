package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine2;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileQuadExtruding extends TileMultiMachine {

    public TileQuadExtruding() {
        super(
                EnumMultiMachine.QUAD_Extruding.usagePerTick,
                EnumMultiMachine.QUAD_Extruding.lenghtOperation,
                2
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine2.quad_extruder;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base2;
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_Extruding;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockExtruding3.name");
    }

    public String getStartSoundFile() {
        return "Machines/extruder.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
