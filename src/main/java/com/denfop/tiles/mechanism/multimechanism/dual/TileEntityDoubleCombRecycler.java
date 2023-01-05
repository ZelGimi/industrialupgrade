package com.denfop.tiles.mechanism.multimechanism.dual;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityDoubleCombRecycler extends TileEntityMultiMachine {

    public TileEntityDoubleCombRecycler() {
        super(
                EnumMultiMachine.DOUBLE_COMB_RECYCLER.usagePerTick,
                EnumMultiMachine.DOUBLE_COMB_RECYCLER.lenghtOperation,
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
        return EnumMultiMachine.DOUBLE_COMB_RECYCLER;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCombRecycler.name");
    }

    public String getStartSoundFile() {
        return "Machines/RecyclerOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
