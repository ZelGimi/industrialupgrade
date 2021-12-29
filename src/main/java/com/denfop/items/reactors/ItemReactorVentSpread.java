package com.denfop.items.reactors;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import net.minecraft.item.ItemStack;

public class ItemReactorVentSpread extends AbstractReactorComponent {

    public final int sideVent;

    public ItemReactorVentSpread(String name, int sidevent) {
        super(name);
        this.sideVent = sidevent;
    }

    public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatrun) {
        if (heatrun) {
            this.cool(reactor, x - 1, y);
            this.cool(reactor, x + 1, y);
            this.cool(reactor, x, y - 1);
            this.cool(reactor, x, y + 1);
        }

    }

    private void cool(IReactor reactor, int x, int y) {
        ItemStack stack = reactor.getItemAt(x, y);
        if (stack != null && stack.getItem() instanceof IReactorComponent) {
            IReactorComponent comp = (IReactorComponent) stack.getItem();
            if (comp.canStoreHeat(stack, reactor, x, y)) {
                int self = comp.alterHeat(stack, reactor, x, y, -this.sideVent);
                if (self <= 0) {
                    reactor.addEmitHeat(self + this.sideVent);
                }
            }
        }

    }

}
