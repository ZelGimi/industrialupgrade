package com.denfop.api.upgrade;

import com.denfop.items.EnumInfoUpgradeModules;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public interface IUpgradeSystem {

    void addModification();

    void addModificate(ItemStack container, String name);

    boolean needModificate(ItemStack container, ItemStack fill);

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

    void removeUpdate(ItemStack stack, World world, int index);

    List<ItemStack> getListStack(ItemStack stack);

    void addRecipe(Item stack, List<EnumInfoUpgradeModules> lst);

    boolean shouldUpdate(EnumInfoUpgradeModules type, ItemStack stack1);

}
