package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileQuadMacerator extends TileMultiMachine {

    public TileQuadMacerator() {
        super(
                EnumMultiMachine.QUAD_MACERATOR.usagePerTick,
                EnumMultiMachine.QUAD_MACERATOR.lenghtOperation,
                0
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine.quad_macerator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_MACERATOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockMacerator3.name");
    }

    public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
