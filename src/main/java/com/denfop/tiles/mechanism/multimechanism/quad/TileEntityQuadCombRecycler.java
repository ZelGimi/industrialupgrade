package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;


public class TileEntityQuadCombRecycler extends TileEntityMultiMachine {

    public TileEntityQuadCombRecycler() {
        super(EnumMultiMachine.QUAD_RECYCLER.usagePerTick, EnumMultiMachine.QUAD_RECYCLER.lenghtOperation, 1);
    }


    public void initiate(int soundEvent) {
        if (this.getWorld().provider.getWorldTime() % 40 == 0) {
            super.initiate(soundEvent);
        }
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
