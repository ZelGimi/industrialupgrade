package com.denfop.tiles.mechanism.multimechanism.dual;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine2;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;
import net.minecraft.util.SoundEvent;

public class TileDoubleCutting extends TileMultiMachine {

    public TileDoubleCutting() {
        super(
                EnumMultiMachine.DOUBLE_Cutting.usagePerTick,
                EnumMultiMachine.DOUBLE_Cutting.lenghtOperation,
                2
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine2.double_cutting;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base2;
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_Cutting;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.cutter.getSoundEvent();
    }

}
