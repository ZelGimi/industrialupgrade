package com.denfop.tiles.mechanism.multimechanism.simple;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSimpleMachine;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileMacerator extends TileMultiMachine {

    public TileMacerator() {
        super(
                EnumMultiMachine.MACERATOR.usagePerTick,
                EnumMultiMachine.MACERATOR.lenghtOperation,
                4
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockSimpleMachine.macerator_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.simplemachine;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.MACERATOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockMacerator.name");
    }

    public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
