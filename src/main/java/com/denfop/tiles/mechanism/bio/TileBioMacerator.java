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

public class TileBioMacerator extends TileBioMultiMachine {


    public TileBioMacerator() {
        super(
                EnumMultiMachine.MACERATOR.usagePerTick,
                EnumMultiMachine.MACERATOR.lenghtOperation,
                4
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.bio_macerator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.MACERATOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockMacerator.name");
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.steam.getSoundEvent();
    }

}
