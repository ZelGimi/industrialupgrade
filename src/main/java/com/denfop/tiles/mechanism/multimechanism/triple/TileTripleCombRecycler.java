package com.denfop.tiles.mechanism.multimechanism.triple;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine1;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileTripleCombRecycler extends TileMultiMachine {

    public TileTripleCombRecycler() {
        super(
                EnumMultiMachine.TRIPLE_COMB_RRECYCLER.usagePerTick,
                EnumMultiMachine.TRIPLE_COMB_RRECYCLER.lenghtOperation,
                1
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine1.triple_comb_recycler;
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
        return EnumMultiMachine.TRIPLE_COMB_RRECYCLER;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCombRecycler1.name");
    }

    public String getStartSoundFile() {
        return "Machines/RecyclerOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
