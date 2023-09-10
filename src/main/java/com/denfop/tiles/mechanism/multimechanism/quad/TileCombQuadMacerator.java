package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine1;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileCombQuadMacerator extends TileMultiMachine {

    public TileCombQuadMacerator() {
        super(
                EnumMultiMachine.COMB_QUAD_MACERATOR.usagePerTick,
                EnumMultiMachine.COMB_QUAD_MACERATOR.lenghtOperation,
                1
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine1.quad_comb_macerator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base1;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.COMB_QUAD_MACERATOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCombMacerator3.name");
    }

    public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
