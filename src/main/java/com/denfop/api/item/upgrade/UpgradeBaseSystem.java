package com.denfop.api.item.upgrade;

import com.denfop.items.EnumInfoUpgradeModules;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public interface UpgradeBaseSystem {

    void updateLevel(ItemStack stack);

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

    UpgradeItemInform getModules(final EnumInfoUpgradeModules module, final ItemStack item, List<UpgradeItemInform> list);

    boolean hasModules(final EnumInfoUpgradeModules module, final ItemStack item, List<UpgradeItemInform> list);

    boolean hasModules(EnumInfoUpgradeModules module, ItemStack item);

    void updateListFromNBT(UpgradeItem item, ItemStack stack, RegistryAccess registryAccess);

    void setInformation(UpgradeItem item, List<EnumInfoUpgradeModules> lst, ItemStack stack, RegistryAccess registryAccess);

    void write(UpgradeItem item, List<EnumInfoUpgradeModules> lst, ItemStack stack, RegistryAccess registryAccess);

    void updateBlackListFromNBT(UpgradeWithBlackList item, ItemStack stack, CompoundTag nbt, RegistryAccess registryAccess);

    void removeUpdate(ItemStack stack, Level world, int index);

    List<ItemStack> getListStack(ItemStack stack);

    void addRecipe(Item stack, List<EnumInfoUpgradeModules> lst);

    boolean shouldUpdate(EnumInfoUpgradeModules type, ItemStack stack1);

    void updateBlackListFromStack(final ItemStack stack);

    List<Integer> getUpgradeFromList(ItemStack stack);

    List<String> getAvailableUpgrade(UpgradeItem upgradeItem, ItemStack stack);
}
