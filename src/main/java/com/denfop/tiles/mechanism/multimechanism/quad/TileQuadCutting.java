package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine2;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileQuadCutting extends TileMultiMachine {

    public TileQuadCutting() {
        super(EnumMultiMachine.QUAD_Cutting.usagePerTick, EnumMultiMachine.QUAD_Cutting.lenghtOperation,
                2
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine2.quad_cutting;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base2;
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_Cutting;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCutting3.name");
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public String getStartSoundFile() {
        return "Machines/cutter.ogg";
    }

}
