package com.denfop.inventory;


import net.minecraft.world.Container;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Objects;

public class HandlerInventory {

    private final IItemHandler handler;
    private final Container inventory;

    public HandlerInventory(IItemHandler handler, Container inventory) {
        this.handler = handler;
        this.inventory = inventory;
    }

    public Container getInventory() {
        return inventory;
    }

    public IItemHandler getHandler() {
        return handler;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HandlerInventory that = (HandlerInventory) o;
        return handler == that.handler;
    }

    @Override
    public int hashCode() {
        return Objects.hash(handler, inventory);
    }

}
