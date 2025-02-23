package com.denfop.api.space.upgrades;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.space.rovers.enums.EnumTypeUpgrade;
import com.denfop.api.space.rovers.api.IRoversItem;
import com.denfop.api.space.upgrades.api.ISpaceUpgradeSystem;
import com.denfop.api.space.upgrades.event.EventItemLoad;
import com.denfop.api.space.upgrades.info.SpaceUpgradeItemInform;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseSpaceUpgradeSystem implements ISpaceUpgradeSystem {

    Map<Integer, List<SpaceUpgradeItemInform>> map;
    Map<Integer, ItemStack> map_stack;
    Map<Integer, Integer> map_col;

    int max;

    public BaseSpaceUpgradeSystem() {
        this.map = new HashMap<>();
        this.max = 0;
        this.map_col = new HashMap<>();
        this.map_stack = new HashMap<>();

        MinecraftForge.EVENT_BUS.register(this);

    }

    public static void addupgrade(Item container, ItemStack fill) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "roverupgradeblock",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(container, 1, OreDictionary.WILDCARD_VALUE)),
                                input.getInput(fill)
                        ),
                        new RecipeOutput(null, new ItemStack(container, 1, OreDictionary.WILDCARD_VALUE))
                )
        );
    }










    @SubscribeEvent
    public void loadItem(EventItemLoad event) {

        this.updateListFromNBT(event.item, event.stack);
    }


    @Override
    public int getRemaining(final ItemStack item) {
        final NBTTagCompound nbt = ModUtils.nbt(item);
        final int id = nbt.getInteger("ID_Item");
        return this.map_col.getOrDefault(id, 23);
    }


    @Override
    public boolean hasInMap(final ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final int id = nbt.getInteger("ID_Item");
        ItemStack item = this.map_stack.get(id);
        if (item == null || item.isEmpty()) {
            return false;
        }
        int id1 = ModUtils.nbt(item).getInteger("ID_Item");

        return item.isItemEqual(stack) && id1 == id && (item.getTagCompound() != null && item
                .getTagCompound()
                .equals(stack.getTagCompound()));
    }

    @Override
    public List<SpaceUpgradeItemInform> getInformation(final ItemStack item) {
        final NBTTagCompound nbt = ModUtils.nbt(item);
        final int id = nbt.getInteger("ID_Item");
        final List<SpaceUpgradeItemInform> list = this.map.get(id);
        return list != null ? list : Collections.emptyList();
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
        if (!(item.getItem() instanceof IRoversItem)) {
            return false;
        }
        List<SpaceUpgradeItemInform> list = getInformation(item);
        for (SpaceUpgradeItemInform upgrade : list) {
            if (upgrade.matched(module)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void updateListFromNBT(final IRoversItem item, ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        boolean hasID = nbt.getBoolean("hasID");
        int id = nbt.getInteger("ID_Item");

        if (!hasID) {
            id = this.max;
            this.max++;
            nbt.setInteger("ID_Item", id);
            nbt.setBoolean("hasID", true);
        }


        NBTTagList modesTagList = nbt.getTagList("modes", 10);
        List<EnumTypeUpgrade> lst = new ArrayList<>();
        for (int i = 0; i < modesTagList.tagCount(); i++) {
            NBTTagCompound modeTag = modesTagList.getCompoundTagAt(i);
            int index = modeTag.getInteger("index");
            lst.add(EnumTypeUpgrade.getFromID(index));
        }

        int ost = 23 - modesTagList.tagCount();
        nbt.setBoolean("canupgrade", ost > 0);

        if (this.map_col.containsKey(id)) {
            this.map_col.replace(id, ost);
        } else {
            this.map_col.put(id, ost);
        }



        this.setInformation(item, lst, stack);

    }




    @Override
    public void setInformation(final IRoversItem item, List<EnumTypeUpgrade> lst, ItemStack stack) {
        this.write(item, lst, stack);
    }

    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload event) {
        if (event.getWorld().isRemote) {
            return;
        }
        this.map.clear();
        this.max = 0;
        this.map_col.clear();
        this.map_stack.clear();
    }

    @Override
    public void write(final IRoversItem item, final List<EnumTypeUpgrade> lst, ItemStack stack) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        final int id = nbt.getInteger("ID_Item");

        Map<EnumTypeUpgrade, Integer> moduleCounts = lst.stream()
                .collect(Collectors.toMap(
                        module -> module,
                        module -> 1,
                        Integer::sum
                ));

        List<SpaceUpgradeItemInform> upgrades = moduleCounts.entrySet().stream()
                .map(entry -> new SpaceUpgradeItemInform(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());


        this.map.put(id, upgrades);
        this.map_stack.put(id, stack);
    }



    private int getModulesValue(EnumTypeUpgrade module, ItemStack stack) {
        SpaceUpgradeItemInform modules =  this.getModules(module, stack);
        if (modules == null)
            return 0;
        return modules.number;
    }




    @Override
    public void removeUpdate(final ItemStack stack, final World world, final int index) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        NBTTagList modesTagList = nbt.getTagList("modes", 10);
        int i = 0;
        for (int ii = 0; ii < modesTagList.tagCount(); ii++) {
            NBTTagCompound tagCompound = modesTagList.getCompoundTagAt(ii);
            if (tagCompound.getInteger("index") == index) {
                i = ii;
                break;
            }
        }
        modesTagList.removeTag(i);
        MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, (IRoversItem) stack.getItem(), stack));

    }

    @Override
    public List<ItemStack> getListStack(final ItemStack stack) {
        List<ItemStack> list = new LinkedList<>();
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        NBTTagList modesTagList = nbt.getTagList("modes", 10);
        for (int ii = 0; ii < modesTagList.tagCount(); ii++) {
            NBTTagCompound tagCompound = modesTagList.getCompoundTagAt(ii);
            list.add(new ItemStack(IUItem.spaceupgrademodule, 1, tagCompound.getInteger("index")));
        }
        return list;
    }

    @Override
    public void addRecipe(final Item stack, final EnumTypeUpgrade... lst) {
        for (EnumTypeUpgrade upgrades : lst) {
            addupgrade(stack, new ItemStack(IUItem.spaceupgrademodule, 1, upgrades.ordinal()));

        }
    }

    @Override
    public boolean shouldUpdate(final EnumTypeUpgrade type, final ItemStack stack1) {
        List<SpaceUpgradeItemInform> list = getInformation(stack1);
        for (SpaceUpgradeItemInform inform : list) {
            if (inform.upgrade == type) {
                if (inform.number >= type.getMax()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public List<String> getAvailableUpgrade(final IRoversItem iUpgradeItem, final ItemStack item) {
        final NBTTagCompound nbt = ModUtils.nbt(item);
        final int id = nbt.getInteger("ID_Item");
        final List<SpaceUpgradeItemInform> list = this.map.get(id);
        final List<EnumTypeUpgrade> list1 = iUpgradeItem.getUpgradeModules();
        final List<String> stringList = new LinkedList<>();
        cycle:
        for (EnumTypeUpgrade enumInfoUpgradeModules : list1) {
            for (SpaceUpgradeItemInform upgradeItemInform : list) {
                if (upgradeItemInform.upgrade.equals(enumInfoUpgradeModules)) {
                    if (upgradeItemInform.number < upgradeItemInform.upgrade.getMax()) {
                        stringList.add(TextFormatting.GREEN + "" + (upgradeItemInform.upgrade.getMax() - upgradeItemInform.number) +
                                "x " + (new ItemStack(
                                IUItem.spaceupgrademodule,
                                1,
                                enumInfoUpgradeModules.ordinal()
                        ).getDisplayName()));
                    }
                    continue cycle;
                }
            }
            stringList.add(TextFormatting.GREEN + "" + (enumInfoUpgradeModules.getMax()) + "x " + (new ItemStack(
                    IUItem.spaceupgrademodule,
                    1,
                    enumInfoUpgradeModules.ordinal()
            ).getDisplayName()));

        }
        return list != null ? stringList : Collections.emptyList();
    }

}
