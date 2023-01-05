package com.denfop.tiles.mechanism.multimechanism.triple;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityTripleCombRecycler extends TileEntityMultiMachine {

    public TileEntityTripleCombRecycler() {
        super(
                EnumMultiMachine.TRIPLE_COMB_RRECYCLER.usagePerTick,
                EnumMultiMachine.TRIPLE_COMB_RRECYCLER.lenghtOperation,
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
