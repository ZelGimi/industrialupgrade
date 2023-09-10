package com.denfop.tiles.mechanism.multimechanism.triple;


import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileTripleExtractor extends TileMultiMachine {

    public TileTripleExtractor() {
        super(
                EnumMultiMachine.TRIPLE_EXTRACTOR.usagePerTick,
                EnumMultiMachine.TRIPLE_EXTRACTOR.lenghtOperation,
                0
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine.triple_extractor;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_EXTRACTOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockExtractor2.name");
    }

    public String getStartSoundFile() {
        return "Machines/ExtractorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
