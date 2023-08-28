package com.denfop.tiles.reactors;


import com.denfop.api.reactors.IAdvReactor;
import com.denfop.items.reactors.IReactorComponent;
import net.minecraft.item.ItemStack;

public class ReactorsItem {

    private final ItemStack stack;
    private final int x;
    private final int y;
    private final IAdvReactor reactor;
    private final IReactorComponent comp;

    public ReactorsItem(ItemStack stack, int x, int y, IAdvReactor reactor) {
        this.stack = stack;
        this.x = x;
        this.y = y;
        this.reactor = reactor;
        this.comp = (IReactorComponent) stack.getItem();

    }

    public void update() {
        for (int pass = 0; pass < 2; ++pass) {


        }
    }

}
