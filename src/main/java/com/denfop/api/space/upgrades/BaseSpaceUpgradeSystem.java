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
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.datacomponent.UpgradeRover;
import com.denfop.recipe.IInputHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BaseSpaceUpgradeSystem implements ISpaceUpgradeSystem {

    public static List<Runnable> list = new ArrayList<>();

    int max;

    public BaseSpaceUpgradeSystem() {
        this.max = 0;

        NeoForge.EVENT_BUS.register(this);

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
        return 24 - item.get(DataComponentsInit.UPGRADE_ROVER).amount();
    }

    @Override
    public boolean hasInMap(final ItemStack stack) {
        return stack.has(DataComponentsInit.UPGRADE_ROVER) && !stack.get(DataComponentsInit.UPGRADE_ROVER).equals(UpgradeRover.EMPTY);

    }

    @Override
    public List<SpaceUpgradeItemInform> getInformation(final ItemStack item) {
        return item.getOrDefault(DataComponentsInit.UPGRADE_ROVER, UpgradeRover.EMPTY).upgradeItemInforms();
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
        UpgradeRover upgradeItem = stack.getOrDefault(DataComponentsInit.UPGRADE_ROVER, UpgradeRover.EMPTY);
        boolean hasID = upgradeItem == UpgradeRover.EMPTY;

        if (!hasID) {
            this.max++;
            upgradeItem = stack.set(DataComponentsInit.UPGRADE_ROVER, UpgradeRover.EMPTY.copy());
        }

        int modesTagList = upgradeItem.amount();
        int ost = 24 - modesTagList;
        upgradeItem = upgradeItem.updateCanUpgrade(stack, ost > 0);


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
        this.max = 0;
    }

    @Override
    public void write(final IRoversItem item, final List<EnumTypeUpgrade> lst, ItemStack stack) {

    }

    private int getModulesValue(EnumTypeUpgrade module, ItemStack stack) {
        SpaceUpgradeItemInform modules = this.getModules(module, stack);
        if (modules == null)
            return 0;
        return modules.number;
    }

    @Override
    public void removeUpdate(final ItemStack stack, final Level world, final int index) {


    }

    @Override
    public List<ItemStack> getListStack(final ItemStack stack) {
        List<ItemStack> list = new LinkedList<>();

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
        UpgradeRover upgradeItem = item.getOrDefault(DataComponentsInit.UPGRADE_ROVER, UpgradeRover.EMPTY);
        final List<SpaceUpgradeItemInform> list = upgradeItem.upgradeItemInforms();
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
