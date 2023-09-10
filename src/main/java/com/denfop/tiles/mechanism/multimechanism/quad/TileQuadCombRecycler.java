package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine1;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;


public class TileQuadCombRecycler extends TileMultiMachine {

    public TileQuadCombRecycler() {
        super(EnumMultiMachine.QUAD_RECYCLER.usagePerTick, EnumMultiMachine.QUAD_RECYCLER.lenghtOperation, 1);
    }


    public void initiate(int soundEvent) {
        if (this.getWorld().provider.getWorldTime() % 40 == 0) {
            super.initiate(soundEvent);
        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine1.quad_comb_recycler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base1;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_RECYCLER;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCombRecycler2.name");
    }

    public String getStartSoundFile() {
        return "Machines/RecyclerOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
