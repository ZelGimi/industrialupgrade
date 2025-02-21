package com.denfop.api.reactors;

import com.denfop.api.item.IDamageItem;
import com.denfop.items.reactors.ItemComponentVent;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LogicCreativeComponent {

    private final IReactorItem item;
    private final ItemStack stack;
    private final IAdvReactor reactor;
    private final boolean componentVent;
    private final int x;
    private final int y;
    public int nearRod = 0;
    public int damage;
    double heat;
    private int maxDamage = 0;
    private int damageItem;
    private int count;
    private List<LogicCreativeComponent> logicComponents;


    public LogicCreativeComponent(IReactorItem item, int x, int y, ItemStack stack, IAdvReactor reactor) {
        this.item = item;
        this.x = x;
        this.y = y;
        this.logicComponents = new ArrayList<>(Collections.nCopies(4, LogicCreativeReactor.NULL));
        this.heat = item.getHeat(reactor);
        if (item.getType() == EnumTypeComponent.ROD) {
            this.damage = 1;
        }
        this.stack = stack;
        this.count = 0;
        this.reactor = reactor;
        this.componentVent = this.stack.getItem() instanceof ItemComponentVent;
        if (stack.getItem() instanceof IDamageItem) {
            this.maxDamage = ((IDamageItem) stack.getItem()).getMaxCustomDamage(stack);
        }
        this.damageItem = this.maxDamage;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<LogicCreativeComponent> getLogicComponents() {
        return logicComponents;
    }

    public IReactorItem getItem() {
        return item;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LogicCreativeComponent that = (LogicCreativeComponent) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public void updateAllInterface(
            List<LogicCreativeComponent> logicComponents,
            int width,
            int height,
            final IAdvReactor reactor
    ) {
        if (x == -1 || y == -1) {
            return;
        }

        for (int i = -1, k = 0; i <= 1; i += 2, k++) {
            if (x + i < 0 || x + i >= width) {
                this.count++;
                continue;
            }
            if (this.getItem().getType() == EnumTypeComponent.ROD) {
                this.heat *= reactor.getMulHeatRod(x, y, this.stack);
            }
            final LogicCreativeComponent cmp = logicComponents.get(x + i + width * this.y);
            if (cmp.getItem() != null && cmp.x != -1) {
                this.logicComponents.set(k, cmp);
                if (this.getItem().getType() == EnumTypeComponent.ROD && cmp.getItem().getType() == EnumTypeComponent.ROD) {
                    double temp_heat = this.heat;
                    this.heat += cmp.heat / 4;
                    cmp.heat += temp_heat / 4;
                }

            }
            if (cmp.equals(LogicCreativeReactor.NULL)) {
                this.count++;
            }
        }
        for (int j = -1, k = 2; j <= 1; j += 2, k++) {
            if (y + j < 0 || y + j >= height) {
                this.count++;
                continue;
            }
            final LogicCreativeComponent cmp = logicComponents.get(x + width * (y + j));
            if (cmp.getItem() != null) {
                this.logicComponents.set(k, cmp);
                if (this.getItem().getType() == EnumTypeComponent.ROD && cmp.getItem().getType() == EnumTypeComponent.ROD) {
                    double temp_heat = this.heat;
                    this.heat += cmp.heat / 4;
                    cmp.heat += temp_heat / 4;
                }
            }
            if (cmp.equals(LogicCreativeReactor.NULL)) {
                this.count++;
            }
            if (this.getItem().getType() == EnumTypeComponent.ROD && count != 0) {
                this.heat *= ((count + 1) * 1.5);
            }
        }
        this.logicComponents.removeIf(logicComponent -> logicComponent.x == -1);
    }

    public boolean canExtractHeat() {
        return this.getItem().caneExtractHeat();
    }

    public ItemStack getStack() {
        return stack;
    }

    public void calculateDamageOther() {
        if (this.getItem().getRepairOther(this.reactor) > 0) {
            for (LogicCreativeComponent component : this.logicComponents) {
                component.damage -= this.getItem().getRepairOther(reactor);
            }
        }
    }

    public boolean onTick() {
        if (this.getItem().getType() == EnumTypeComponent.ROD) {
            return false;
        }
        if (componentVent) {
            if (this.damage > 150 * this.getItem().getLevel()) {
                this.damageItem = 0;
            }
        } else {
            this.damageItem -= damage;
            if (damageItem > maxDamage) {
                damageItem = maxDamage;
            }
            ((IDamageItem) stack.getItem()).setCustomDamage(stack, maxDamage - damageItem);
        }
        if (this.getItem().getType() == EnumTypeComponent.COOLANT_ROD) {
            double heat = reactor.getHeat();
            heat -= this.heat;
            heat = Math.max(0, heat);
            reactor.setHeat(heat);
        }

        return damageItem <= 0;
    }

    public double getHeat() {
        if (this.getItem().getType() == EnumTypeComponent.CAPACITOR || this
                .getItem()
                .getType() == EnumTypeComponent.COOLANT_ROD) {
            return 0;
        } else {
            return this.heat;
        }
    }

}
