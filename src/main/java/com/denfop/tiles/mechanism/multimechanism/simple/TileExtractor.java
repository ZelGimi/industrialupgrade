package com.denfop.tiles.mechanism.multimechanism.simple;


import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSimpleMachine;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileExtractor extends TileMultiMachine {

    public TileExtractor() {
        super(
                EnumMultiMachine.EXTRACTOR.usagePerTick,
                EnumMultiMachine.EXTRACTOR.lenghtOperation,
                4
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockSimpleMachine.extractor_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.simplemachine;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.EXTRACTOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockExtractor.name");
    }

    public String getStartSoundFile() {
        return "Machines/ExtractorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
