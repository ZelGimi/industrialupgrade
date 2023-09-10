package com.denfop.tiles.mechanism.multimechanism.dual;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine1;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;
import net.minecraft.util.SoundEvent;

public class TileDoubleCombRecycler extends TileMultiMachine {

    public TileDoubleCombRecycler() {
        super(
                EnumMultiMachine.DOUBLE_COMB_RECYCLER.usagePerTick,
                EnumMultiMachine.DOUBLE_COMB_RECYCLER.lenghtOperation,
                1
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine1.double_comb_recycler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base1;
    }

    public void initiate(int soundEvent) {
        if (this.getWorld().provider.getWorldTime() % 40 == 0) {
            super.initiate(soundEvent);
        }
    }


    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_COMB_RECYCLER;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.RecyclerOp.getSoundEvent();
    }


}
