package com.denfop.api.space.upgrades;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.space.rovers.EnumTypeUpgrade;
import com.denfop.api.space.rovers.IRovers;
import com.denfop.api.space.upgrades.event.EventItemLoad;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseSpaceUpgradeSystem implements ISpaceUpgradeSystem {

    Map<Integer, List<SpaceUpgradeItemInform>> map;
    Map<Integer, List<SpaceUpgradeItemInform>> map_modification;
    Map<Integer, ItemStack> map_stack;
    Map<Integer, Integer> map_col;
    Map<Integer, List<String>> map_blackList;
    int max;

    public BaseSpaceUpgradeSystem() {
        this.map = new HashMap<>();
        this.map_blackList = new HashMap<>();
        this.max = 0;
        this.map_col = new HashMap<>();
        this.map_stack = new HashMap<>();
        this.map_modification = new HashMap<>();

        MinecraftForge.EVENT_BUS.register(this);

    }

    public static void addupgrade(Item container, ItemStack fill) {
        NBTTagCompound nbt = ModUtils.nbt();
        nbt.setString(
                "mode_module",
                EnumTypeUpgrade.getFromID(fill.getItemDamage()).getUpgrade()
        );
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "upgrade_rovers",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(container, 1, OreDictionary.WILDCARD_VALUE)),
                                input.getInput(fill)
                        ),
                        new RecipeOutput(nbt, new ItemStack(container, 1, OreDictionary.WILDCARD_VALUE))
                )
        );
    }

    @SubscribeEvent
    public void loadItem(EventItemLoad event) {
        this.updateListFromNBT(event.item, event.stack);
    }


    @Override
    public boolean getModifications(final ItemStack item) {
        return this.map_modification.containsKey(ModUtils.nbt(item).getInteger("ID_Item"));
    }

    @Override
    public int getRemaining(final ItemStack item) {
        final NBTTagCompound nbt = ModUtils.nbt(item);
        final int id = nbt.getInteger("ID_Item");
        return this.map_col.getOrDefault(id, 4);
    }


    @Override
    public boolean hasInMap(final ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final int id = nbt.getInteger("ID_Item");
        if (!this.map_stack.containsKey(id)) {
            return false;
        }
        ItemStack item = this.map_stack.get(id);
        int id1 = ModUtils.nbt(item).getInteger("ID_Item");

        return item.isItemEqual(stack) && id1 == id;
    }

    @Override
    public List<String> getBlackList(final ItemStack item) {
        final NBTTagCompound nbt = ModUtils.nbt(item);
        final int id = nbt.getInteger("ID_Item");
        return this.map_blackList.containsKey(id) ? this.map_blackList.get(id) : new ArrayList<>();

    }

    @Override
    public List<SpaceUpgradeItemInform> getInformation(final ItemStack item) {
        final NBTTagCompound nbt = ModUtils.nbt(item);
        final int id = nbt.getInteger("ID_Item");
        return this.map.containsKey(id) ? this.map.get(id) : new ArrayList<>();
    }

    @Override
    public SpaceUpgradeItemInform getModules(final EnumTypeUpgrade module, final ItemStack item) {
        List<SpaceUpgradeItemInform> list = getInformation(item);
        for (SpaceUpgradeItemInform upgrade : list) {
            if (upgrade.matched(module)) {
                return upgrade;
            }
        }
        return null;
    }

    @Override
    public boolean hasModules(final EnumTypeUpgrade module, final ItemStack item) {
        List<SpaceUpgradeItemInform> list = getInformation(item);
        for (SpaceUpgradeItemInform upgrade : list) {
            if (upgrade.matched(module)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateListFromNBT(final IRovers item, ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        boolean hasID = nbt.getBoolean("hasID");
        int id = nbt.getInteger("ID_Item");
        if (!hasID) {
            id = this.max;
            this.max++;
            nbt.setInteger("ID_Item", id);
            nbt.setBoolean("hasID", true);
        }
        List<EnumTypeUpgrade> lst = new ArrayList<>();
        List<SpaceUpgradeItemInform> list = new ArrayList<>();
        if (this.map_modification.containsKey(id)) {
            this.map_modification.replace(id, list);
        } else {
            this.map_modification.put(id, list);
        }
        int ost;
        int empty = 0;
        boolean canupgrade = false;
        for (int i = 0; i < 4; i++) {
            String name = nbt.getString("mode_module" + i);
            if (name.equals("")) {
                empty++;
                canupgrade = true;
            }
            if (!name.equals("")) {
                int index = IUItem.list_space_upgrades.indexOf(name);
                lst.add(EnumTypeUpgrade.getFromID(index));
            }
        }
        ost = empty;
        nbt.setBoolean("canupgrade", canupgrade);

        if (this.map_col.containsKey(id)) {
            this.map_col.replace(id, ost);
        } else {
            this.map_col.put(id, ost);
        }

        this.setInformation(item, lst, stack);

    }


    @Override
    public void setInformation(final IRovers item, List<EnumTypeUpgrade> lst, ItemStack stack) {
        this.write(item, lst, stack);
    }

    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload event) {
        this.map.clear();
        this.map_blackList.clear();
        this.max = 0;
        this.map_col.clear();
        this.map_stack.clear();
        this.map_modification.clear();
    }

    @Override
    public void write(final IRovers item, final List<EnumTypeUpgrade> lst, ItemStack stack) {
        Map<EnumTypeUpgrade, Integer> map = new HashMap<>();
        for (EnumTypeUpgrade upgrade : lst) {
            if (map.containsKey(upgrade)) {
                map.replace(upgrade, map.get(upgrade) + 1);
            } else {
                map.put(upgrade, 1);
            }
        }
        List<SpaceUpgradeItemInform> list = new ArrayList<>();
        for (Map.Entry<EnumTypeUpgrade, Integer> map1 : map.entrySet()) {
            list.add(new SpaceUpgradeItemInform(map1.getKey(), map1.getValue()));
        }

        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final int id = nbt.getInteger("ID_Item");
        if (!this.map.containsKey(id)) {
            this.map.put(id, list);
            this.map_stack.put(id, stack);
        } else {
            this.map.replace(id, list);
            this.map_stack.replace(id, stack);
        }

    }

    @Override
    public void removeUpdate(final ItemStack stack, final World world, final int index) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setString("mode_module" + index, "");
        MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, (IRovers) stack.getItem(), stack));

    }

    @Override
    public List<ItemStack> getListStack(final ItemStack stack) {
        List<ItemStack> list = new ArrayList<>();
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        for (int i = 0; i < 4; i++) {
            String name = nbt.getString("mode_module" + i);
            if (!name.equals("")) {
                int index = IUItem.list.indexOf(name);
                list.add(new ItemStack(IUItem.upgrademodule, 1, index));
            } else {
                list.add(ItemStack.EMPTY);
            }
        }
        return list;
    }

    @Override
    public void addRecipe(final Item stack, final List<EnumTypeUpgrade> lst) {
    /*    for (EnumInfoUpgradeModules upgrades : lst) {
            addupgrade(stack, new ItemStack(IUItem.upgrademodule, 1, upgrades.ordinal()));
        }
        if (lst.contains(EnumInfoUpgradeModules.DIG_DEPTH)) {
            addupgrade(stack, new ItemStack(IUItem.module9, 1, 12));

        }*/
    }

}
