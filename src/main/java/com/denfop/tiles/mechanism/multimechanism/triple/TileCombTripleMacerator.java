package com.denfop.tiles.mechanism.multimechanism.triple;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine1;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;


public class TileCombTripleMacerator extends TileMultiMachine {

    public TileCombTripleMacerator() {
        super(
                EnumMultiMachine.COMB_TRIPLE_MACERATOR.usagePerTick,
                EnumMultiMachine.COMB_TRIPLE_MACERATOR.lenghtOperation,
                1
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine1.triple_comb_macerator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base1;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.COMB_TRIPLE_MACERATOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCombMacerator2.name");
    }

    public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
