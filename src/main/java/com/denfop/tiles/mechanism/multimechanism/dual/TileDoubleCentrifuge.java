package com.denfop.tiles.mechanism.multimechanism.dual;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;
import net.minecraft.util.SoundEvent;

public class TileDoubleCentrifuge extends TileMultiMachine {

    public TileDoubleCentrifuge() {
        super(
                EnumMultiMachine.DOUBLE_Centrifuge.usagePerTick,
                EnumMultiMachine.DOUBLE_Centrifuge.lenghtOperation,
                4
        );
        this.cold.upgrade = true;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine3.doublecentrifuge;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_Centrifuge;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.centrifuge.getSoundEvent();
    }

}
