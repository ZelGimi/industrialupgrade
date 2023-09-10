package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileQuadCentrifuge extends TileMultiMachine {

    public TileQuadCentrifuge() {
        super(
                EnumMultiMachine.QUAD_Centrifuge.usagePerTick,
                EnumMultiMachine.QUAD_Centrifuge.lenghtOperation,
                4
        );
        this.cold.upgrade = true;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine3.quadcentrifuge;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_Centrifuge;
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
