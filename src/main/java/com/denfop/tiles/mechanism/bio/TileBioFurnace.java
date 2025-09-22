package com.denfop.tiles.mechanism.bio;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileBioMultiMachine;
import net.minecraft.util.SoundEvent;

public class TileBioFurnace extends TileBioMultiMachine {


    public TileBioFurnace() {
        super(
                EnumMultiMachine.ELECTRIC_FURNACE.usagePerTick,
                EnumMultiMachine.ELECTRIC_FURNACE.lenghtOperation,
                4
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.bio_furnace;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.ELECTRIC_FURNACE;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockFurnace.name");
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.steam.getSoundEvent();
    }

}
