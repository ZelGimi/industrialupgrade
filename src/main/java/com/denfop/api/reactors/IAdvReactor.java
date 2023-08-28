package com.denfop.api.reactors;

import com.denfop.tiles.reactors.ReactorsItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public interface IAdvReactor {

    List<ReactorsItem> getReactorsItems();

    boolean isWork();

    void setWork(final boolean work);

    boolean isFull();

    TileEntity getCoreTe();

    int getHeat();

    void setHeat(int var1);

    int addHeat(int var1);

    int getMaxHeat();

    void setMaxHeat(int var1);

    void addEmitHeat(int var1);

    float getHeatEffectModifier();

    void setHeatEffectModifier(float var1);

    float getReactorEnergyOutput();

    double getReactorEUEnergyOutput();

    float addOutput(float var1);

    ItemStack getItemAt(int var1, int var2);

    void setItemAt(int var1, int var2, ItemStack var3);

    void explode();

    int getTickRate();

    boolean produceEnergy();

}
