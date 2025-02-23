package com.denfop.api.space.upgrades.api;

import com.denfop.api.space.rovers.enums.EnumTypeUpgrade;
import com.denfop.api.space.rovers.api.IRoversItem;
import com.denfop.api.space.upgrades.info.SpaceUpgradeItemInform;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public interface ISpaceUpgradeSystem {




    int getRemaining(ItemStack item);

    boolean hasInMap(ItemStack stack);


    List<SpaceUpgradeItemInform> getInformation(ItemStack item);

    SpaceUpgradeItemInform getModules(EnumTypeUpgrade module, ItemStack item);

    boolean hasModules(EnumTypeUpgrade module, ItemStack item);
    void updateListFromNBT(final IRoversItem item, ItemStack stack);




    void setInformation(final IRoversItem item, List<EnumTypeUpgrade> lst, ItemStack stack);

    void write(IRoversItem item, List<EnumTypeUpgrade> lst, ItemStack stack);

    void removeUpdate(ItemStack stack, World world, int index);

    List<ItemStack> getListStack(ItemStack stack);

    void addRecipe(Item stack,EnumTypeUpgrade... lst);

    boolean shouldUpdate(final EnumTypeUpgrade type, final ItemStack stack1);

    List<String> getAvailableUpgrade(IRoversItem iUpgradeItem, ItemStack stack);

}
