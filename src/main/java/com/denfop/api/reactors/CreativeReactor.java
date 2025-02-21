package com.denfop.api.reactors;

import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.invslot.InvSlot;
import com.denfop.utils.Timer;
import net.minecraft.item.ItemStack;

public class CreativeReactor implements IAdvReactor {

    public static int[] graphite_capacitors = new int[]{1, 6, 8, 8};
    public static int[] heat_graphite = new int[]{1, 2, 4, 8};
    public static double[] graphite_capacitor = new double[]{0.02, 0.04, 0.08, 0.12};
    private final InvSlot slot;
    public int level;
    public Timer timer = new Timer(9999, 0, 0);
    public Timer red_timer = new Timer(0, 2, 30);
    public Timer yellow_timer = new Timer(0, 15, 0);
    public boolean explode = false;
    private EnumReactors enumReactors;
    private double heat;
    private double output;
    private double rad;
    private EnumTypeSecurity security = EnumTypeSecurity.NONE;
    private int col;

    public CreativeReactor(EnumReactors enumReactors, InvSlot slot) {

        if (enumReactors != null) {
            this.enumReactors = enumReactors;
            level = (enumReactors.ordinal() % 3) + 1;
        } else {
            this.enumReactors = null;
            level = 0;
        }
        this.slot = slot;
        col = 0;
    }

    public void reset(EnumReactors enumReactors) {
        this.enumReactors = enumReactors;
        level = (enumReactors.ordinal() % 3) + 1;
        this.col = 0;
    }

    public void tick(double maxHeat) {
        workTimer();
        if (!this.timer.canWork()) {
            this.explode();

        } else if (!this.yellow_timer.canWork()) {
            this.explode();
        } else if (!this.red_timer.canWork()) {
            this.explode();
        } else if (this.getHeat() >= this.getMaxHeat() && maxHeat >= this.getMaxHeat() * 1.5) {
            this.explode();
        }
    }

    @Override
    public boolean isWork() {
        return true;
    }

    @Override
    public void setWork(final boolean work) {

    }

    @Override
    public double getHeat() {
        return this.heat;
    }

    @Override
    public void setHeat(final double var1) {
        this.heat = var1;
        if (this.heat > this.getMaxHeat()) {
            this.heat = this.getMaxHeat();
        }
        if (this.heat < this.getStableMaxHeat()) {
            this.setSecurity(EnumTypeSecurity.STABLE);
            this.setTime(EnumTypeSecurity.STABLE);
        } else if (this.heat >= this.getStableMaxHeat() && this.heat <=
                this.getStableMaxHeat() + (this.getMaxHeat() - this.getStableMaxHeat()) * 0.75) {
            this.setSecurity(EnumTypeSecurity.UNSTABLE);
            this.setTime(EnumTypeSecurity.UNSTABLE);
        } else {
            this.setSecurity(EnumTypeSecurity.ERROR);
            this.setTime(EnumTypeSecurity.ERROR);
        }
    }

    @Override
    public void setUpdate() {

    }

    @Override
    public void setItemAt(final int x, final int y) {
        this.slot.set(y * this.getWidth() + x, ItemStack.EMPTY);
    }

    public double getRad() {
        return rad;
    }

    @Override
    public void setRad(final double rad) {
        this.rad = rad;
    }

    @Override
    public int getMaxHeat() {
        return enumReactors.getMaxHeat();
    }

    @Override
    public int getStableMaxHeat() {
        return enumReactors.getMaxStable();
    }

    @Override
    public double getOutput() {
        return this.output;
    }

    @Override
    public void setOutput(final double output) {
        this.output = output;
    }

    @Override
    public ItemStack getItemAt(final int x, final int y) {
        return this.slot.get(y * this.getWidth() + x);
    }

    @Override
    public void explode() {
        this.explode = true;
    }

    @Override
    public ITypeRector getTypeRector() {
        return enumReactors.getType();
    }

    @Override
    public Timer getTimer() {
        return timer;
    }

    @Override
    public void setTime(final EnumTypeSecurity enumTypeSecurity) {
        if (this.security == enumTypeSecurity) {
            switch (enumTypeSecurity) {
                case STABLE:
                    timer = new Timer(9999, 0, 0);
                    break;
                case ERROR:
                    timer = new Timer(0, 2, 30);
                    break;
                case UNSTABLE:
                    timer = new Timer(0, 15, 0);
                    break;
            }
        }
    }

    @Override
    public void workTimer() {
        switch (this.security) {
            case UNSTABLE:
                this.yellow_timer.work();
                if (!this.red_timer.getMinute(3)) {
                    this.red_timer.rework();
                }
                break;
            case STABLE:
                this.timer.work();
                if (!this.yellow_timer.getMinute(15)) {
                    this.yellow_timer.rework();
                }
                if (!this.red_timer.getMinute(3)) {
                    this.red_timer.rework();
                }
                break;
            case ERROR:
                this.red_timer.work();
                break;
        }
    }

    @Override
    public EnumTypeSecurity getSecurity() {
        return this.security;
    }

    @Override
    public void setSecurity(final EnumTypeSecurity enumTypeSecurity) {
        this.security = enumTypeSecurity;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public double getMulHeatRod(final int x, final int y, final ItemStack stack) {
        if (this.enumReactors != null && this.enumReactors.getType() == ITypeRector.GRAPHITE_FLUID) {
            double coef = 1;
            for (int i = 0; i < graphite_capacitors[level - 1]; i++) {
                coef *= (1 - graphite_capacitor[level - 1]);
            }
            coef *= 1 + ((4) / 55D);
            return coef;
        }
        return IAdvReactor.super.getMulHeatRod(x, y, stack);
    }

    @Override
    public double getMulOutput(final int x, final int y, final ItemStack stack) {
        if (this.enumReactors != null && this.enumReactors.getType() == ITypeRector.GRAPHITE_FLUID) {
            double level = 1 + (5 - 1 / 4D) * 0.05;
            return 1 * level;
        }
        if (this.enumReactors != null && this.enumReactors.getType() == ITypeRector.HIGH_SOLID) {
            final int maxCol = heat_graphite[level - 1];
            if (col < maxCol) {
                col++;
                double level1 = 1;
                double level = 1 + (5 - 1 / 4D) * 0.05;
                level1 *= level;
                return level1;
            }
        }

        return IAdvReactor.super.getMulOutput(x, y, stack);
    }

    @Override
    public int getWidth() {
        return enumReactors.getWidth();
    }

    @Override
    public int getHeight() {
        return enumReactors.getHeight();
    }

    @Override
    public int getLevelReactor() {
        return level;
    }

    @Override
    public int getMaxLevelReactor() {
        return level;
    }

    @Override
    public void increaseLevelReactor() {

    }

    @Override
    public ComponentBaseEnergy getRadiation() {
        return null;
    }

    @Override
    public double getModuleStableHeat() {
        return 1;
    }

    @Override
    public double getModuleRadiation() {
        return 1;
    }

    @Override
    public double getModuleGeneration() {
        return 1;
    }

    @Override
    public double getModuleVent() {
        return 1;
    }

    @Override
    public double getModuleComponentVent() {
        return 1;
    }

    @Override
    public double getModuleCapacitor() {
        return 1;
    }

    @Override
    public double getModuleExchanger() {
        return 1;
    }

}
