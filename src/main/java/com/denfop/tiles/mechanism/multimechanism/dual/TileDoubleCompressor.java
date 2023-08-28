package com.denfop.tiles.mechanism.multimechanism.dual;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;
import net.minecraft.util.SoundEvent;

public class TileDoubleCompressor extends TileMultiMachine {

    public TileDoubleCompressor() {
        super(
                EnumMultiMachine.DOUBLE_COMPRESSER.usagePerTick,
                EnumMultiMachine.DOUBLE_COMPRESSER.lenghtOperation,
                0
        );
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
