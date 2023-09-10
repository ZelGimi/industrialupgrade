package com.denfop.tiles.mechanism.multimechanism.dual;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;
import net.minecraft.util.SoundEvent;

public class TileDoubleAssamplerScrap extends TileMultiMachine {

    public TileDoubleAssamplerScrap() {
        super(
                EnumMultiMachine.DOUBLE_AssamplerScrap.usagePerTick,
                EnumMultiMachine.DOUBLE_AssamplerScrap.lenghtOperation,
                3
        );
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
