package com.denfop.items.reactors;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import net.minecraft.item.ItemStack;

public class ItemReactorReflector extends AbstractDamageableReactorComponent {

    public ItemReactorReflector(String name, int maxDamage) {
        super(name, maxDamage);
    }

    public boolean acceptUraniumPulse(
            ItemStack stack,
            IReactor reactor,
            ItemStack pulsingStack,
            int youX,
            int youY,
            int pulseX,
            int pulseY,
            boolean heatrun
    ) {
        if (!heatrun) {
            IReactorComponent source = (IReactorComponent) pulsingStack.getItem();
            source.acceptUraniumPulse(pulsingStack, reactor, stack, pulseX, pulseY, youX, youY, heatrun);
        } else if (this.getCustomDamage(stack) + 1 >= this.getMaxCustomDamage(stack)) {
            reactor.setItemAt(youX, youY, null);
        } else {
            this.setCustomDamage(stack, this.getCustomDamage(stack) + 1);
        }

        return true;
    }

    public float influenceExplosion(ItemStack stack, IReactor reactor) {
        return -1.0F;
    }

}
