package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;


public class TileEntityPhoCombRecycler extends TileMultiMachine {


    public TileEntityPhoCombRecycler() {
        super(EnumMultiMachine.Pho_RECYCLER.usagePerTick, EnumMultiMachine.Pho_RECYCLER.lenghtOperation);
    }


    public void initiate(int soundEvent) {
        if (this.getWorld().provider.getWorldTime() % 40 == 0) {
            super.initiate(soundEvent);
        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlocksPhotonicMachine.photonic_comb_recycler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Pho_RECYCLER;
    }


}
