package com.denfop.api.reactors;

import com.denfop.api.item.IDamageItem;
import com.denfop.items.reactors.ItemComponentVent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LogicComponent {

    private final IReactorItem item;
    private final ItemStack stack;
    private final IAdvReactor reactor;
    private final int maxDamage;
    private final boolean componentVent;
    private final byte x;
    private final byte y;
    public byte nearRod = 0;
    public short damage;
    double heat;
    private int maxDamageItem;
    private byte count;
    private List<LogicComponent> logicComponents;


    public LogicComponent(IReactorItem item, int x, int y, ItemStack stack, IAdvReactor reactor) {
        this.item = item;
        this.x = (byte) x;
        this.y = (byte) y;
        this.logicComponents = new ArrayList<>(Collections.nCopies(4, LogicReactor.NULL));
        this.heat = item.getHeat(reactor);
        if (item.getType() == EnumTypeComponent.ROD) {
            this.damage = 1;
        }
        this.stack = stack;
        this.count = 0;
        this.reactor = reactor;
        this.componentVent = this.getItem() instanceof ItemComponentVent;
        if (stack != null && stack.getItem() instanceof IDamageItem damageItem) {
            this.maxDamage = (damageItem).getMaxCustomDamage(stack);
            this.maxDamageItem =damageItem.getCustomDamage(stack);
        } else {
            this.maxDamage = 0;
            this.maxDamageItem = 0;
        }
    }

    public int getX() {
        return x;
    }

    public int getDamage() {
        return damage;
    }

    public int getY() {
        return y;
    }

    public List<LogicComponent> getLogicComponents() {
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
        LogicComponent that = (LogicComponent) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public void updateAllInterface(List<LogicComponent> logicComponents, int width, int height, final IAdvReactor reactor) {
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
            final LogicComponent cmp = logicComponents.get(x + i + width * this.y);
            if (cmp.getItem() != null && cmp.x != -1) {
                this.logicComponents.set(k, cmp);
                if (this.getItem().getType() == EnumTypeComponent.ROD && cmp.getItem().getType() == EnumTypeComponent.ROD) {
                    double temp_heat = this.heat;
                    this.heat += cmp.heat / 4;
                    cmp.heat += temp_heat / 4;
                }

            }
            if (cmp.equals(LogicReactor.NULL)) {
                this.count++;
            }
        }
        for (int j = -1, k = 2; j <= 1; j += 2, k++) {
            if (y + j < 0 || y + j >= height) {
                this.count++;
                continue;
            }
            final LogicComponent cmp = logicComponents.get(x + width * (y + j));
            if (cmp.getItem() != null) {
                this.logicComponents.set(k, cmp);
                if (this.getItem().getType() == EnumTypeComponent.ROD && cmp.getItem().getType() == EnumTypeComponent.ROD) {
                    double temp_heat = this.heat;
                    this.heat += cmp.heat / 4;
                    cmp.heat += temp_heat / 4;
                }
            }
            if (cmp.equals(LogicReactor.NULL)) {
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

    public boolean onTick() {
        if (this.getItem().getType() == EnumTypeComponent.ROD) {
            return false;
        }
        if (this.getItem().getType() == EnumTypeComponent.ENERGY_COUPLER || this
                .getItem()
                .getType() == EnumTypeComponent.NEUTRON_PROTECTOR) {
            this.item.damageItem(this.stack, 1);
            this.maxDamageItem += 1;
        } else {
            if (damage != 0) {
                if (!componentVent) {
                    if (this.maxDamageItem > 0) {
                        this.item.damageItem(this.stack, damage);
                    }
                    if (this.maxDamageItem > 0 && this.getItem().getType() == EnumTypeComponent.COOLANT_ROD) {
                        this.item.damageItem(this.stack,  damage);
                    }
                    this.maxDamageItem -= damage * -1;

                    if (this.maxDamageItem > this.maxDamage) {
                        this.maxDamageItem = this.maxDamage;
                    }
                    if (this.maxDamageItem < 0) {
                        this.maxDamageItem = 0;
                    }
                } else {
                    if (this.damage >= 150 * getItem().getLevel()) {
                        this.item.damageItem(this.stack,  damage);
                        this.maxDamageItem = 1;
                    }
                }
            }
        }
        if (this.getItem().getType() == EnumTypeComponent.COOLANT_ROD) {
            double heat = reactor.getHeat();
            heat -= this.heat;
            heat = Math.max(0, heat);
            reactor.setHeat(heat);
        }

        return maxDamageItem == maxDamage;
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
