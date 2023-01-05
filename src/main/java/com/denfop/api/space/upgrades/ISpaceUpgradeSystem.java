package com.denfop.api.space.upgrades;

import com.denfop.api.space.rovers.EnumTypeUpgrade;
import com.denfop.api.space.rovers.IRovers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public interface ISpaceUpgradeSystem {

    boolean getModifications(ItemStack item);


    int getRemaining(ItemStack item);

    boolean hasInMap(ItemStack stack);

    List<String> getBlackList(ItemStack item);

    List<SpaceUpgradeItemInform> getInformation(ItemStack item);

    SpaceUpgradeItemInform getModules(EnumTypeUpgrade module, ItemStack item);

    boolean hasModules(EnumTypeUpgrade module, ItemStack item);

    void updateListFromNBT(IRovers item, ItemStack stack);

    void setInformation(IRovers item, List<EnumTypeUpgrade> lst, ItemStack stack);

    void write(IRovers item, List<EnumTypeUpgrade> lst, ItemStack stack);

    void removeUpdate(ItemStack stack, World world, int index);

    List<ItemStack> getListStack(ItemStack stack);

    void addRecipe(Item stack, List<EnumTypeUpgrade> lst);

}
