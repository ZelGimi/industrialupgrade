package com.denfop.tiles.mechanism.multimechanism.quad;


import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileQuadExtractor extends TileMultiMachine {

    public TileQuadExtractor() {
        super(
                EnumMultiMachine.QUAD_EXTRACTOR.usagePerTick,
                EnumMultiMachine.QUAD_EXTRACTOR.lenghtOperation,
                0
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine.quad_extractor;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_EXTRACTOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockExtractor3.name");
    }

    public String getStartSoundFile() {
        return "Machines/ExtractorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
