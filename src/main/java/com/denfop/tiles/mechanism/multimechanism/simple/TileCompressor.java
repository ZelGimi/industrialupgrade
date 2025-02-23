package com.denfop.tiles.mechanism.multimechanism.simple;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSimpleMachine;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileCompressor extends TileMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileCompressor() {
        super(
                EnumMultiMachine.COMPRESSER.usagePerTick,
                EnumMultiMachine.COMPRESSER.lenghtOperation
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.15));
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
