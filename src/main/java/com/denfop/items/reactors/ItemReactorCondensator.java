package com.denfop.items.reactors;

import ic2.api.reactor.IReactor;
import net.minecraft.item.ItemStack;

public class ItemReactorCondensator extends AbstractDamageableReactorComponent {

    public ItemReactorCondensator(String name, int maxdmg) {
        super(name, maxdmg);
    }

    public boolean canStoreHeat(ItemStack stack, IReactor reactor, int x, int y) {
        return this.getCurrentHeat(stack) < this.getMaxCustomDamage(stack);
    }

    public int getMaxHeat(ItemStack stack, IReactor reactor, int x, int y) {
        return this.getMaxCustomDamage(stack);
    }

    private int getCurrentHeat(ItemStack stack) {
        return this.getCustomDamage(stack);
    }

    public int alterHeat(ItemStack stack, IReactor reactor, int x, int y, int heat) {
        if (heat >= 0) {
            int currentHeat = this.getCurrentHeat(stack);
            int amount = Math.min(heat, this.getMaxHeat(stack, reactor, x, y) - currentHeat);
            heat -= amount;
            this.setCustomDamage(stack, currentHeat + amount);
        }
        return heat;
    }

}
