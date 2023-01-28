package com.denfop.api.water.upgrade;

import com.denfop.IUItem;
import com.denfop.api.water.upgrade.event.EventRotorItemLoad;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RotorUpgradeSystem implements IRotorUpgradeSystem {

    public static IRotorUpgradeSystem instance;

    Map<Integer, List<RotorUpgradeItemInform>> map;

    Map<Integer, ItemStack> map_stack;

    Map<Integer, Integer> map_col;

    int max;

    public RotorUpgradeSystem() {
        this.map = new HashMap<>();
        this.max = 0;
        this.map_col = new HashMap<>();
        this.map_stack = new HashMap<>();
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void loadItem(EventRotorItemLoad event) {

        updateListFromNBT(event.item, event.stack);
    }

    public int getRemaining(ItemStack item) {
        NBTTagCompound nbt = ModUtils.nbt(item);
        int id = nbt.getInteger("ID_Item");
        return this.map_col.getOrDefault(id, 4);
    }

    public boolean hasInMap(ItemStack stack) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        int id = nbt.getInteger("ID_Item");
        ItemStack item = this.map_stack.get(id);
        if (item == null || item.isEmpty()) {
            return false;
        }
        int id1 = ModUtils.nbt(item).getInteger("ID_Item");
        return (item.isItemEqual(stack) && id1 == id && item.getTagCompound() != null && item
                .getTagCompound()
                .equals(stack.getTagCompound()));
    }

    public List<RotorUpgradeItemInform> getInformation(ItemStack item) {
        NBTTagCompound nbt = ModUtils.nbt(item);
        int id = nbt.getInteger("ID_Item");
        List<RotorUpgradeItemInform> list = this.map.get(id);
        return (list != null) ? list : new ArrayList<>();
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
        NBTTagCompound nbt = ModUtils.nbt(stack);
        boolean hasID = nbt.getBoolean("hasID");
        int id = nbt.getInteger("ID_Item");
        if (!hasID) {
            id = this.max;
            this.max++;
            nbt.setInteger("ID_Item", id);
            nbt.setBoolean("hasID", true);
        }
        List<EnumInfoRotorUpgradeModules> lst = new ArrayList<>();
        int empty = 0;
        boolean canupgrade = false;
        for (int i = 0; i < 4; i++) {
            String name = nbt.getString("mode_module" + i);
            if (name.equals("")) {
                empty++;
                canupgrade = true;
            }
            if (!name.equals("")) {
                int index = IUItem.list1.indexOf(name);
                lst.add(EnumInfoRotorUpgradeModules.getFromID(index));
            }
        }
        int ost = empty;
        nbt.setBoolean("canupgrade", canupgrade);
        if (this.map_col.containsKey(id)) {
            this.map_col.replace(id, ost);
        } else {
            this.map_col.put(id, ost);
        }
        setInformation(item, lst, stack);
    }

    public void setInformation(IRotorUpgradeItem item, List<EnumInfoRotorUpgradeModules> lst, ItemStack stack) {
        write(item, lst, stack);
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.getWorld().isRemote) {
            return;
        }
        this.map.clear();
        this.max = 0;
        this.map_col.clear();
        this.map_stack.clear();
    }

    public void write(IRotorUpgradeItem item, List<EnumInfoRotorUpgradeModules> lst, ItemStack stack) {
        Map<EnumInfoRotorUpgradeModules, Integer> map = new HashMap<>();
        for (EnumInfoRotorUpgradeModules upgrade : lst) {
            if (map.containsKey(upgrade)) {
                map.replace(upgrade, map.get(upgrade) + 1);
                continue;
            }
            map.put(upgrade, 1);
        }
        List<RotorUpgradeItemInform> list = new ArrayList<>();
        for (Map.Entry<EnumInfoRotorUpgradeModules, Integer> map1 : map.entrySet()) {
            list.add(new RotorUpgradeItemInform(map1.getKey(), map1.getValue()));
        }
        NBTTagCompound nbt = ModUtils.nbt(stack);
        int id = nbt.getInteger("ID_Item");
        if (!this.map.containsKey(id)) {
            this.map.put(id, list);
            this.map_stack.put(id, stack);
        } else {
            this.map.replace(id, list);
            this.map_stack.replace(id, stack);
        }
    }

    public void removeUpdate(ItemStack stack, World world, int index) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setString("mode_module" + index, "");
        MinecraftForge.EVENT_BUS.post(new EventRotorItemLoad(world, (IRotorUpgradeItem) stack.getItem(), stack));
    }

    public List<ItemStack> getListStack(ItemStack stack) {
        List<ItemStack> list = new ArrayList<>();
        NBTTagCompound nbt = ModUtils.nbt(stack);
        for (int i = 0; i < 4; i++) {
            String name = nbt.getString("mode_module" + i);
            if (!name.equals("")) {
                int index = IUItem.list1.indexOf(name);
                list.add(new ItemStack(IUItem.water_rotors_upgrade, 1, index));
            } else {
                list.add(ItemStack.EMPTY);
            }
        }
        return list;
    }

    public Map<Integer, ItemStack> getList(ItemStack stack) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        Map<Integer, ItemStack> map = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            String name = nbt.getString("mode_module" + i);
            if (!name.equals("")) {
                int index = IUItem.list1.indexOf(name);
                map.put(i, new ItemStack(IUItem.water_rotors_upgrade, 1, index));
            } else {
                map.put(i, ItemStack.EMPTY);
            }
        }
        return map;
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
