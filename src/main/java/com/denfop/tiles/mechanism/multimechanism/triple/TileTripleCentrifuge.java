package com.denfop.tiles.mechanism.multimechanism.triple;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileTripleCentrifuge extends TileMultiMachine {

    public TileTripleCentrifuge() {
        super(
                EnumMultiMachine.TRIPLE_Centrifuge.usagePerTick,
                EnumMultiMachine.TRIPLE_Centrifuge.lenghtOperation,
                4
        );
        this.cold.upgrade = true;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine3.triplecentrifuge;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_Centrifuge;
    }

    public String getStartSoundFile() {
        return "Machines/centrifuge.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


 /*   public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }
*/

}
