package com.denfop.items.reactors;

import ic2.api.reactor.IReactor;
import net.minecraft.item.ItemStack;

public class ItemReactorPlating extends AbstractReactorComponent {

    private final int maxHeatAdd;
    private final float effectModifier;

    public ItemReactorPlating(String name, int maxheatadd, float effectmodifier) {
        super(name);
        this.maxHeatAdd = maxheatadd;
        this.effectModifier = effectmodifier;
    }

    public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatrun) {
        if (heatrun) {
            reactor.setMaxHeat(reactor.getMaxHeat() + this.maxHeatAdd);
            reactor.setHeatEffectModifier(reactor.getHeatEffectModifier() * this.effectModifier);
        }

    }

    public float influenceExplosion(ItemStack stack, IReactor reactor) {
        return this.effectModifier >= 1.0F ? 0.0F : this.effectModifier;
    }

}
