package com.denfop.tiles.mechanism.multimechanism.dual;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;
import net.minecraft.util.SoundEvent;

public class TileDoubleAssamplerScrap extends TileMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileDoubleAssamplerScrap() {
        super(
                EnumMultiMachine.DOUBLE_AssamplerScrap.usagePerTick,
                EnumMultiMachine.DOUBLE_AssamplerScrap.lenghtOperation
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.075));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine3.double_assamplerscrap;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_AssamplerScrap;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.AssamplerScrap.getSoundEvent();
    }


}
