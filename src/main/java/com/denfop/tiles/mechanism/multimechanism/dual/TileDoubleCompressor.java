package com.denfop.tiles.mechanism.multimechanism.dual;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;
import net.minecraft.util.SoundEvent;

public class TileDoubleCompressor extends TileMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileDoubleCompressor() {
        super(
                EnumMultiMachine.DOUBLE_COMPRESSER.usagePerTick,
                EnumMultiMachine.DOUBLE_COMPRESSER.lenghtOperation
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.075));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine.double_commpressor;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base;
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_COMPRESSER;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.compressor.getSoundEvent();
    }


}
