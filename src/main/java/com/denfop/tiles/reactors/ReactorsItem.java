package com.denfop.tiles.reactors;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import net.minecraft.item.ItemStack;

public class ReactorsItem {

    private final ItemStack stack;
    private final int x;
    private final int y;
    private final IReactor reactor;
    private final IReactorComponent comp;

    public ReactorsItem(ItemStack stack, int x, int y, IReactor reactor) {
        this.stack = stack;
        this.x = x;
        this.y = y;
        this.reactor = reactor;
        this.comp = (IReactorComponent) stack.getItem();
    }

    public void update() {
        for (int pass = 0; pass < 2; ++pass) {
            this.comp.processChamber(this.stack, this.reactor, this.x, this.y, pass == 0);
        }
    }

}
