package com.denfop.tiles.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileSteamMultiMachine;
import net.minecraft.util.SoundEvent;

public class TileSteamCutting extends TileSteamMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileSteamCutting() {
        super(
                EnumMultiMachine.Cutting.usagePerTick,
                EnumMultiMachine.Cutting.lenghtOperation,
                4
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.125));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.2));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.steam_cutting;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Cutting;
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.steam.getSoundEvent();
    }

}
