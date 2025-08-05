package com.denfop.api.windsystem.upgrade;

import com.denfop.IUItem;
import com.denfop.api.windsystem.upgrade.event.EventRotorItemLoad;
import com.denfop.datacomponent.DataComponentsInit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.*;

public class RotorUpgradeSystem implements IRotorUpgradeSystem {

    public static IRotorUpgradeSystem instance;


    int max;

    public RotorUpgradeSystem() {
        this.max = 0;
        instance = this;
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void loadItem(EventRotorItemLoad event) {

        updateListFromNBT(event.item, event.stack);
    }

    public int getRemaining(ItemStack item) {
        List<RotorUpgradeItemInform> list = item.getOrDefault(DataComponentsInit.WIND_UPGRADE, Collections.emptyList());
        int col = 0;
        for (RotorUpgradeItemInform rotorUpgradeItemInform : list) {
            col += rotorUpgradeItemInform.number;
        }
        return 4 - col;
    }

    public boolean hasInMap(ItemStack stack) {
        return stack.has(DataComponentsInit.WIND_UPGRADE);
    }

    public List<RotorUpgradeItemInform> getInformation(ItemStack item) {

        return item.getOrDefault(DataComponentsInit.WIND_UPGRADE, Collections.emptyList());
    }

    public RotorUpgradeItemInform getModules(EnumInfoRotorUpgradeModules module, ItemStack item) {
        List<RotorUpgradeItemInform> list = getInformation(item);
        for (RotorUpgradeItemInform upgrade : list) {
            if (upgrade.matched(module)) {
                return upgrade;
            }
        }
        return null;
    }

    public RotorUpgradeItemInform getModules(EnumInfoRotorUpgradeModules module, List<RotorUpgradeItemInform> list) {
        for (RotorUpgradeItemInform upgrade : list) {
            if (upgrade.matched(module)) {
                return upgrade;
            }
        }
        return null;
    }

    public boolean hasModules(EnumInfoRotorUpgradeModules module, ItemStack item) {
        if (!(item.getItem() instanceof IRotorUpgradeItem)) {
            return false;
        }
        List<RotorUpgradeItemInform> list = getInformation(item);
        for (RotorUpgradeItemInform upgrade : list) {
            if (upgrade.matched(module)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasModules(EnumInfoRotorUpgradeModules module, ItemStack item, List<RotorUpgradeItemInform> list) {
        if (!(item.getItem() instanceof IRotorUpgradeItem)) {
            return false;
        }
        for (RotorUpgradeItemInform upgrade : list) {
            if (upgrade.matched(module)) {
                return true;
            }
        }
        return false;
    }

    public void updateListFromNBT(IRotorUpgradeItem item, ItemStack stack) {
        if (!stack.has(DataComponentsInit.WIND_UPGRADE)) {
            stack.set(DataComponentsInit.WIND_UPGRADE, new ArrayList<>());
        }
        setInformation(item, Collections.emptyList(), stack);
    }

    public void setInformation(IRotorUpgradeItem item, List<EnumInfoRotorUpgradeModules> lst, ItemStack stack) {
        write(item, lst, stack);
    }

    @SubscribeEvent
    public void onWorldUnload(LevelEvent.Unload event) {
        this.max = 0;

    }

    public void write(IRotorUpgradeItem item, List<EnumInfoRotorUpgradeModules> lst, ItemStack stack) {
    }

    public void removeUpdate(ItemStack stack, Level world, int index) {
        stack.set(DataComponentsInit.WIND_UPGRADE, new ArrayList<>());
    }

    public List<ItemStack> getListStack(ItemStack stack) {
        List<RotorUpgradeItemInform> listUpgrade = stack.getOrDefault(DataComponentsInit.WIND_UPGRADE, Collections.emptyList());
        List<ItemStack> list = new ArrayList<>();
        for (RotorUpgradeItemInform rotorUpgradeItemInform : listUpgrade) {
            for (int i = 0; i < rotorUpgradeItemInform.number; i++)
                list.add(new ItemStack(IUItem.rotors_upgrade.getStack(rotorUpgradeItemInform.upgrade.ordinal()), 1));

        }
        if (list.size() < 4) {
            for (int i = list.size() - 1; i < list.size(); i++) {
                list.add(ItemStack.EMPTY);
                if (list.size() >= 4)
                    break;
            }
        }
        return list;
    }

    public Map<Integer, ItemStack> getList(ItemStack stack) {
        List<RotorUpgradeItemInform> listUpgrade = stack.getOrDefault(DataComponentsInit.WIND_UPGRADE, Collections.emptyList());

        Map<Integer, ItemStack> map = new HashMap<>();
        int j = 0;
        for (RotorUpgradeItemInform rotorUpgradeItemInform : listUpgrade) {
            for (int i = 0; i < rotorUpgradeItemInform.number; i++) {
                map.put(j, new ItemStack(IUItem.rotors_upgrade.getStack(rotorUpgradeItemInform.upgrade.ordinal()), 1));
                j++;
            }
        }
        j += 1;
        if (j < 4) {
            for (int i = j - 1; i < j; i++) {
                map.put(j, ItemStack.EMPTY);
            }
        }
        return map;
    }

    @Override
    public void addUpdate(ItemStack itemStack, Level world, EnumInfoRotorUpgradeModules fromID) {
        List<RotorUpgradeItemInform> rotorUpgradeItemInforms = itemStack.getOrDefault(DataComponentsInit.WIND_UPGRADE, new ArrayList<>());
        boolean find = false;
        for (RotorUpgradeItemInform rotorUpgradeItemInform : rotorUpgradeItemInforms) {
            if (rotorUpgradeItemInform.upgrade == fromID) {
                rotorUpgradeItemInform.number++;
                find = true;
                break;
            }
        }
        if (!find) {
            rotorUpgradeItemInforms.add(new RotorUpgradeItemInform(fromID, 1));

        }
        itemStack.set(DataComponentsInit.WIND_UPGRADE, rotorUpgradeItemInforms);
    }

    public boolean shouldUpdate(EnumInfoRotorUpgradeModules type, ItemStack stack1) {
        List<RotorUpgradeItemInform> list = getInformation(stack1);
        for (RotorUpgradeItemInform inform : list) {
            if (inform.upgrade == type &&
                    inform.number >= type.max) {
                return false;
            }
        }
        return true;
    }

}
