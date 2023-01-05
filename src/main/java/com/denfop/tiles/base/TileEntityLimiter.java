package com.denfop.tiles.base;

import com.denfop.componets.AdvEnergy;
import com.denfop.invslot.InvSlotLimiter;
import ic2.api.energy.EnergyNet;
import ic2.core.block.TileEntityBlock;

import java.util.Collections;
import java.util.EnumSet;

public class TileEntityLimiter extends TileEntityInventory {

    private final AdvEnergy energy;
    private final InvSlotLimiter slot;

    public TileEntityLimiter(){
    this.energy = this.addComponent(new AdvEnergy(
            this,
            EnergyNet.instance.getPowerFromTier(0) * 8.0D,
            EnumSet.complementOf(EnumSet.of(this.getFacing())),
            EnumSet.of(this.getFacing()),
            14,
            0,
            false
    ));
    this.energy.setLimit(true);
    this.slot = new InvSlotLimiter(this);

    }

}
