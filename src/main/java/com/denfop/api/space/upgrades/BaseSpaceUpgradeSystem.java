package com.denfop.api.space.upgrades;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.space.rovers.api.IRoversItem;
import com.denfop.api.space.rovers.enums.EnumTypeUpgrade;
import com.denfop.api.space.upgrades.api.ISpaceUpgradeSystem;
import com.denfop.api.space.upgrades.event.EventItemLoad;
import com.denfop.api.space.upgrades.info.SpaceUpgradeItemInform;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;
import java.util.stream.Collectors;

public class BaseSpaceUpgradeSystem implements ISpaceUpgradeSystem {

    public static List<Runnable> list = new ArrayList<>();
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
                                input.getInput(new ItemStack(container, 1)),
                                input.getInput(fill)
                        ),
                        new RecipeOutput(null, new ItemStack(container, 1))
                )
        );
    }

    @SubscribeEvent
    public void loadItem(EventItemLoad event) {

        this.updateListFromNBT(event.item, event.stack);
    }

    @Override
    public int getRemaining(final ItemStack item) {
        final CompoundTag nbt = ModUtils.nbt(item);
        final int id = nbt.getInt("ID_Item");
        return this.map_col.getOrDefault(id, 24);
    }

    @Override
    public boolean hasInMap(final ItemStack stack) {
        final CompoundTag nbt = ModUtils.nbt(stack);
        final int id = nbt.getInt("ID_Item");
        ItemStack item = this.map_stack.get(id);
        if (item == null || item.isEmpty()) {
            return false;
        }
        int id1 = ModUtils.nbt(item).getInt("ID_Item");

        return item.is(stack.getItem()) && id1 == id && (item.getTag() != null && item
                .getTag()
                .equals(stack.getTag()));
    }

    @Override
    public List<SpaceUpgradeItemInform> getInformation(final ItemStack item) {
        final CompoundTag nbt = ModUtils.nbt(item);
        final int id = nbt.getInt("ID_Item");
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
        final CompoundTag nbt = ModUtils.nbt(stack);
        boolean hasID = nbt.getBoolean("hasID");
        int id = nbt.getInt("ID_Item");

        if (!hasID) {
            id = this.max;
            this.max++;
            nbt.putInt("ID_Item", id);
            nbt.putBoolean("hasID", true);
        }


        ListTag modesTagList = nbt.getList("modes", 10);
        List<EnumTypeUpgrade> lst = new ArrayList<>();
        for (int i = 0; i < modesTagList.size(); i++) {
            CompoundTag modeTag = modesTagList.getCompound(i);
            int index = modeTag.getInt("index");
            lst.add(EnumTypeUpgrade.getFromID(index));
        }

        int ost = 24 - modesTagList.size();
        nbt.putBoolean("canupgrade", ost > 0);

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
    public void onWorldUnload(final LevelEvent.Unload event) {
        if ((event.getLevel()).isClientSide()) {
            return;
        }
        this.map.clear();
        this.max = 0;
        this.map_col.clear();
        this.map_stack.clear();
    }

    @Override
    public void write(final IRoversItem item, final List<EnumTypeUpgrade> lst, ItemStack stack) {
        CompoundTag nbt = ModUtils.nbt(stack);
        final int id = nbt.getInt("ID_Item");

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
        SpaceUpgradeItemInform modules = this.getModules(module, stack);
        if (modules == null)
            return 0;
        return modules.number;
    }

    @Override
    public void removeUpdate(final ItemStack stack, final Level world, final int index) {
        final CompoundTag nbt = ModUtils.nbt(stack);
        ListTag modesTagList = nbt.getList("modes", 10);
        int i = 0;
        for (int ii = 0; ii < modesTagList.size(); ii++) {
            CompoundTag tagCompound = modesTagList.getCompound(ii);
            if (tagCompound.getInt("index") == index) {
                i = ii;
                break;
            }
        }
        modesTagList.remove(i);
        MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, (IRoversItem) stack.getItem(), stack));

    }

    @Override
    public List<ItemStack> getListStack(final ItemStack stack) {
        List<ItemStack> list = new LinkedList<>();
        final CompoundTag nbt = ModUtils.nbt(stack);
        ListTag modesTagList = nbt.getList("modes", 10);
        for (int ii = 0; ii < modesTagList.size(); ii++) {
            CompoundTag tagCompound = modesTagList.getCompound(ii);
            list.add(new ItemStack(IUItem.spaceupgrademodule.getStack(tagCompound.getInt("index")), 1));
        }
        return list;
    }

    @Override
    public void addRecipe(final Item stack, final EnumTypeUpgrade... lst) {
        for (EnumTypeUpgrade upgrades : lst) {
            addupgrade(stack, new ItemStack(IUItem.spaceupgrademodule.getStack(upgrades.ordinal()), 1));

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
        final CompoundTag nbt = ModUtils.nbt(item);
        final int id = nbt.getInt("ID_Item");
        final List<SpaceUpgradeItemInform> list = this.map.get(id);
        final List<EnumTypeUpgrade> list1 = iUpgradeItem.getUpgradeModules();
        final List<String> stringList = new LinkedList<>();
        cycle:
        for (EnumTypeUpgrade enumInfoUpgradeModules : list1) {
            for (SpaceUpgradeItemInform upgradeItemInform : list) {
                if (upgradeItemInform.upgrade.equals(enumInfoUpgradeModules)) {
                    if (upgradeItemInform.number < upgradeItemInform.upgrade.getMax()) {
                        stringList.add(ChatFormatting.GREEN + "" + (upgradeItemInform.upgrade.getMax() - upgradeItemInform.number) +
                                "x " + (new ItemStack(
                                IUItem.spaceupgrademodule.getStack(enumInfoUpgradeModules.ordinal()),
                                1
                        ).getDisplayName().getString()));
                    }
                    continue cycle;
                }
            }
            stringList.add(ChatFormatting.GREEN + "" + (enumInfoUpgradeModules.getMax()) + "x " + (new ItemStack(
                    IUItem.spaceupgrademodule.getStack(enumInfoUpgradeModules.ordinal()),
                    1
            ).getDisplayName().getString()));

        }
        return list != null ? stringList : Collections.emptyList();
    }

}
