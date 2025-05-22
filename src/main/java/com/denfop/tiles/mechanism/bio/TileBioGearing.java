package com.denfop.tiles.mechanism.bio;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileBioMultiMachine;
import net.minecraft.util.SoundEvent;

public class TileBioGearing extends TileBioMultiMachine {


    public TileBioGearing() {
        super(
                EnumMultiMachine.Gearing.usagePerTick,
                EnumMultiMachine.Gearing.lenghtOperation,
                4
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.bio_gearing;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Gearing;
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.steam.getSoundEvent();
    }


}
