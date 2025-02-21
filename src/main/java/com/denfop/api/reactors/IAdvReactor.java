package com.denfop.api.reactors;

import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.utils.Timer;
import net.minecraft.item.ItemStack;

public interface IAdvReactor {


    boolean isWork();

    void setWork(final boolean work);

    double getHeat();

    void setHeat(double var1);

    void setUpdate();

    void setItemAt(final int x, final int y);

    void setRad(double rad);

    int getMaxHeat();

    int getStableMaxHeat();

    double getOutput();

    void setOutput(double output);

    ItemStack getItemAt(int var1, int var2);

    void explode();

    ITypeRector getTypeRector();

    Timer getTimer();

    void setTime(EnumTypeSecurity enumTypeSecurity);

    void workTimer();

    EnumTypeSecurity getSecurity();

    void setSecurity(EnumTypeSecurity enumTypeSecurity);

    int getLevel();

    int getWidth();

    int getHeight();

    int getLevelReactor();

    int getMaxLevelReactor();

    void increaseLevelReactor();

    ComponentBaseEnergy getRadiation();

    default double getMulHeat(final int x, final int y, ItemStack stack) {
        return 1;
    }

    ;

    default double getMulOutput(final int x, final int y, ItemStack stack) {
        return 1;
    }

    default double getMulDamage(final int x, final int y, ItemStack stack) {
        return 1;
    }

    default double getMulHeatRod(int x, int y, ItemStack stack) {
        return 1;
    }

    ;

    double getModuleStableHeat();

    double getModuleRadiation();

    double getModuleGeneration();

    double getModuleVent();

    double getModuleComponentVent();

    double getModuleCapacitor();

    double getModuleExchanger();

}
