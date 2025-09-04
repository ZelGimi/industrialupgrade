package com.denfop.api.reactors;

import com.denfop.api.container.CustomWorldContainer;
import com.denfop.inventory.Inventory;
import net.minecraft.world.item.ItemStack;

public class InventoryReactorModules<T extends IAdvReactor & CustomWorldContainer> extends Inventory {

    private double stableHeat;
    private double radiation;
    private double generation;
    private double componentVent;
    private double vent;
    private double exchanger;
    private double capacitor;

    public InventoryReactorModules(final T base) {
        super(base, TypeItemSlot.INPUT, 4);
        this.setStackSizeLimit(1);
    }

    @Override
    public boolean canPlaceItem(final int index, final ItemStack stack) {
        return stack.getItem() instanceof IReactorModule;
    }

    @Override
    public ItemStack set(int i, final ItemStack content) {
        super.set(i, content);
        stableHeat = 1;
        radiation = 1;
        generation = 1;
        vent = 1;
        componentVent = 1;
        exchanger = 1;
        capacitor = 1;
        for (ItemStack stack : this.contents) {
            if (stack.isEmpty()) {
                continue;
            }
            if (!(stack.getItem() instanceof IReactorModule)) {
                continue;
            }
            IReactorModule module = (IReactorModule) stack.getItem();
            this.stableHeat *= module.getStableHeat(stack);
            this.radiation *= module.getRadiation(stack);
            this.generation *= module.getGeneration(stack);
            this.componentVent *= module.getComponentVent(stack);
            this.vent *= module.getVent(stack);
            this.exchanger *= module.getExchanger(stack);
            this.capacitor *= module.getCapacitor(stack);
        }
        ((IAdvReactor) this.base).setUpdate();
        return content;
    }

    public void load() {
        stableHeat = 1;
        radiation = 1;
        generation = 1;
        vent = 1;
        componentVent = 1;
        exchanger = 1;
        capacitor = 1;
        for (ItemStack stack : this.contents) {
            if (stack.isEmpty()) {
                continue;
            }
            if (!(stack.getItem() instanceof IReactorModule)) {
                continue;
            }
            IReactorModule module = (IReactorModule) stack.getItem();
            this.stableHeat *= module.getStableHeat(stack);
            this.radiation *= module.getRadiation(stack);
            this.generation *= module.getGeneration(stack);
            this.componentVent *= module.getComponentVent(stack);
            this.vent *= module.getVent(stack);
            this.exchanger *= module.getExchanger(stack);
            this.capacitor *= module.getCapacitor(stack);
        }
        ((IAdvReactor) this.base).setUpdate();
    }

    public double getStableHeat() {
        return stableHeat;
    }

    public double getRadiation() {
        return radiation;
    }

    public double getComponentVent() {
        return componentVent;
    }

    public double getGeneration() {
        return generation;
    }

    public double getExchanger() {
        return exchanger;
    }

    public double getCapacitor() {
        return capacitor;
    }

    public double getVent() {
        return vent;
    }

}
