package com.denfop.tiles.mechanism.multimechanism.simple;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSimpleMachine;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileCompressor extends TileMultiMachine {

    public TileCompressor() {
        super(
                EnumMultiMachine.COMPRESSER.usagePerTick,
                EnumMultiMachine.COMPRESSER.lenghtOperation,
                4
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockSimpleMachine.compressor_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.simplemachine;
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.COMPRESSER;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCompressor.name");
    }

    public String getStartSoundFile() {
        return "Machines/CompressorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
