package com.denfop.api.upgrade;

import com.denfop.utils.EnumInfoUpgradeModules;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public interface IUpgradeSystem {

    boolean getModifications(ItemStack item);

    List<UpgradeModificator> getListModifications(ItemStack item);

    int getRemaining(ItemStack item);

    boolean hasBlackList(ItemStack item);

    boolean hasInMap(ItemStack stack);

    List<String> getBlackList(ItemStack item);

    List<UpgradeItemInform> getInformation(ItemStack item);

    UpgradeItemInform getModules(EnumInfoUpgradeModules module, ItemStack item);

    boolean hasModules(EnumInfoUpgradeModules module, ItemStack item);

    void updateListFromNBT(IUpgradeItem item, ItemStack stack);

    void setInformation(IUpgradeItem item, List<EnumInfoUpgradeModules> lst, ItemStack stack);

    void write(IUpgradeItem item, List<EnumInfoUpgradeModules> lst, ItemStack stack);

    void updateBlackListFromNBT(IUpgradeWithBlackList item, ItemStack stack, NBTTagCompound nbt);

}
