package com.denfop.tiles.mechanism.bio;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileBioMultiMachine;
import com.denfop.tiles.base.TileSteamMultiMachine;
import net.minecraft.util.SoundEvent;

public class TileBioExtractor extends TileBioMultiMachine {



    public TileBioExtractor() {
        super(
                EnumMultiMachine.EXTRACTOR.usagePerTick,
                EnumMultiMachine.EXTRACTOR.lenghtOperation,
                4
        );
       }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.bio_extractor;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.EXTRACTOR;
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.steam.getSoundEvent();
    }

}
