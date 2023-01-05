package com.denfop.tiles.mechanism.multimechanism.triple;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityTripleRecycler extends TileEntityMultiMachine {

    public TileEntityTripleRecycler() {
        super(
                EnumMultiMachine.TRIPLE_RECYCLER.usagePerTick,
                EnumMultiMachine.TRIPLE_RECYCLER.lenghtOperation,
                1
        );
    }

    public void initiate(int soundEvent) {
        if (this.getWorld().provider.getWorldTime() % 40 == 0) {
            super.initiate(soundEvent);
        }
    }


    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_RECYCLER;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockRecycler1.name");
    }

    public String getStartSoundFile() {
        return "Machines/RecyclerOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
