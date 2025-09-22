package com.denfop.tiles.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileBioMultiMachine;
import net.minecraft.util.SoundEvent;

public class TileBioExtruder extends TileBioMultiMachine {


    public TileBioExtruder() {
        super(
                EnumMultiMachine.Extruding.usagePerTick,
                EnumMultiMachine.Extruding.lenghtOperation,
                4
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.bio_extruder;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Extruding;
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.steam.getSoundEvent();
    }


}
