package com.denfop.tiles.mechanism.multimechanism.dual;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine1;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;
import net.minecraft.util.SoundEvent;

public class TileCombDoubleMacerator extends TileMultiMachine {

    public TileCombDoubleMacerator() {
        super(
                EnumMultiMachine.COMB_DOUBLE_MACERATOR.usagePerTick,
                EnumMultiMachine.COMB_DOUBLE_MACERATOR.lenghtOperation,
                1
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine1.double_comb_macerator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base1;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.COMB_DOUBLE_MACERATOR;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.MaceratorOp.getSoundEvent();
    }

}
