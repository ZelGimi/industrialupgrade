package com.denfop.items.reactors;

import ic2.api.reactor.IReactor;
import net.minecraft.item.ItemStack;

public class ItemReactorVent extends ItemReactorHeatStorage {

    public final int selfVent;
    public final int reactorVent;

    public ItemReactorVent(String name, int heatStorage, int selfvent, int reactorvent) {
        super(name, heatStorage);
        this.selfVent = selfvent;
        this.reactorVent = reactorvent;
    }

    public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatrun) {
        if (heatrun) {
            int rheat;
            if (this.reactorVent > 0) {
                rheat = reactor.getHeat();
                int reactorDrain = Math.min(rheat, this.reactorVent);

                rheat -= reactorDrain;
                if (this.alterHeat(stack, reactor, x, y, reactorDrain) > 0) {
                    return;
                }

                reactor.setHeat(rheat);
            }

            rheat = this.alterHeat(stack, reactor, x, y, -this.selfVent);
            if (rheat <= 0) {
                reactor.addEmitHeat(rheat + this.selfVent);
            }
        }

    }

}
