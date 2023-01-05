package com.denfop.api.windsystem.upgrade;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public interface IRotorUpgradeSystem {


    RotorUpgradeItemInform getModules(final EnumInfoRotorUpgradeModules module, List<RotorUpgradeItemInform> list);


    int getRemaining(ItemStack item);


    boolean hasInMap(ItemStack stack);


    RotorUpgradeItemInform getModules(final EnumInfoRotorUpgradeModules module, final ItemStack item);

    List<RotorUpgradeItemInform> getInformation(ItemStack item);


    boolean hasModules(final EnumInfoRotorUpgradeModules module, final ItemStack item);

    boolean hasModules(final EnumInfoRotorUpgradeModules module, final ItemStack item, List<RotorUpgradeItemInform> list);

    void updateListFromNBT(IRotorUpgradeItem item, ItemStack stack);


    void setInformation(final IRotorUpgradeItem item, List<EnumInfoRotorUpgradeModules> lst, ItemStack stack);

    void write(IRotorUpgradeItem item, List<EnumInfoRotorUpgradeModules> lst, ItemStack stack);


    void removeUpdate(ItemStack stack, World world, int index);

    List<ItemStack> getListStack(ItemStack stack);

    boolean shouldUpdate(final EnumInfoRotorUpgradeModules type, final ItemStack stack1);

    Map<Integer, ItemStack> getList(final ItemStack stack);

}
