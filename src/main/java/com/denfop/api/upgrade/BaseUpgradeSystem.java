package com.denfop.api.upgrade;

import com.denfop.IUItem;
import com.denfop.api.upgrade.event.EventItemBlackListLoad;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.utils.EnumInfoUpgradeModules;
import com.denfop.utils.ModUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseUpgradeSystem implements IUpgradeSystem {

    Map<Integer, List<UpgradeItemInform>> map;
    Map<Integer, List<UpgradeModificator>> map_modification;
    Map<Integer, ItemStack> map_stack;
    Map<Integer, Integer> map_col;
    Map<Integer, List<String>> map_blackList;
    List<UpgradeModificator> list_modificators;
    int max;

    public BaseUpgradeSystem() {
        this.map = new HashMap<>();
        this.map_blackList = new HashMap<>();
        this.max = 0;
        this.map_col = new HashMap<>();
        this.map_stack = new HashMap<>();
        this.list_modificators= new ArrayList<>();
        this.list_modificators.add(new UpgradeModificator(new ItemStack(IUItem.core,1,7)));
        this.list_modificators.add(new UpgradeModificator(new ItemStack(IUItem.neutroniumingot,1)));
        this.map_modification = new HashMap<>();

        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

    }


    @SubscribeEvent
    public void loadItem(EventItemLoad event) {
        this.updateListFromNBT(event.item, event.stack);
        event.item.setUpdate(true);
    }

    @SubscribeEvent
    public void loadItem(EventItemBlackListLoad event) {
        this.updateBlackListFromNBT(event.item, event.stack, event.nbt);
        event.item.setUpdate(true);
    }


    @Override
    public boolean getModifications(final ItemStack item) {
        return this.map_modification.containsKey(ModUtils.nbt(item).getInteger("ID_Item"));
    }

    @Override
    public List<UpgradeModificator> getListModifications(final ItemStack item) {
        return getModifications(item) ? this.map_modification.get(ModUtils.nbt(item).getInteger("ID_Item")) : new ArrayList<>();
    }

    @Override
    public int getRemaining(final ItemStack item) {
        final NBTTagCompound nbt = ModUtils.nbt(item);
        final int id = nbt.getInteger("ID_Item");
        return this.map_col.getOrDefault(id, 4);
    }

    @Override
    public boolean hasBlackList(final ItemStack item) {
        final NBTTagCompound nbt = ModUtils.nbt(item);
        final int id = nbt.getInteger("ID_Item");
        return map_blackList.containsKey(id);
    }

    @Override
    public boolean hasInMap(final ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final int id = nbt.getInteger("ID_Item");
        if(!this.map_stack.containsKey(id))
            return false;
        ItemStack item =  this.map_stack.get(id);
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
    public List<UpgradeItemInform> getInformation(final ItemStack item) {
        final NBTTagCompound nbt = ModUtils.nbt(item);
        final int id = nbt.getInteger("ID_Item");
        return this.map.containsKey(id) ? this.map.get(id) : new ArrayList<>();
    }

    @Override
    public UpgradeItemInform getModules(final EnumInfoUpgradeModules module, final ItemStack item) {
        List<UpgradeItemInform> list = getInformation(item);
        for (UpgradeItemInform upgrade : list) {
            if (upgrade.matched(module)) {
                return upgrade;
            }
        }
        return null;
    }

    @Override
    public boolean hasModules(final EnumInfoUpgradeModules module, final ItemStack item) {
        List<UpgradeItemInform> list = getInformation(item);
        for (UpgradeItemInform upgrade : list) {
            if (upgrade.matched(module)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateListFromNBT(final IUpgradeItem item, ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        boolean hasID = nbt.getBoolean("hasID");
        int id =  nbt.getInteger("ID_Item");
        if (!hasID) {
            id = this.max;
            this.max++;
            nbt.setInteger("ID_Item", id);
            nbt.setBoolean("hasID", true);
        }
        List<EnumInfoUpgradeModules> lst = new ArrayList<>();
        List<UpgradeModificator> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            String name = nbt.getString("modification_module"+i);
            if(getModification(name) != null)
            list.add(getModification(name));
        }
        if(this.map_modification.containsKey(id)){
            this.map_modification.replace(id,list);
        }else
            this.map_modification.put(id,list);


        boolean canupgrade = nbt.getString("mode_module" + 3).equals("");
        nbt.setBoolean("canupgrade",canupgrade);
        int ost = 0;
            for (int i = 0; i < 4 ; i++) {
            String name = nbt.getString("mode_module" + i);
            if (name.equals("")) {
                ost = 4 - i;
                break;
            }
            int index = IUItem.list.indexOf(name);
            lst.add(EnumInfoUpgradeModules.getFromID(index));
        }
        if(this.map_col.containsKey(id)){
            this.map_col.replace(id,ost);
        }else
            this.map_col.put(id,ost);

        this.setInformation(item, lst, stack);

    }

    private UpgradeModificator getModification(String name) {
        if(name.equals("0"))
            return this.list_modificators.get(0);
        if(name.equals("1"))
            return this.list_modificators.get(1);
        return null;
    }

    @Override
    public void setInformation(final IUpgradeItem item, List<EnumInfoUpgradeModules> lst, ItemStack stack) {
        this.write(item, lst, stack);
    }

    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload event) {
        this.map.clear();
        this.map_blackList.clear();
        this.max = 0;
        this.map_col.clear();
        this.map_stack.clear();
        this.list_modificators.clear();
        this.map_modification.clear();
    }

    @Override
    public void write(final IUpgradeItem item, final List<EnumInfoUpgradeModules> lst, ItemStack stack) {
        Map<EnumInfoUpgradeModules, Integer> map = new HashMap<>();
        for (EnumInfoUpgradeModules upgrade : lst) {
            if (map.containsKey(upgrade)) {
                map.replace(upgrade, map.get(upgrade) + 1);
            } else {
                map.put(upgrade, 1);
            }
        }
        List<UpgradeItemInform> list = new ArrayList<>();
        for (Map.Entry<EnumInfoUpgradeModules, Integer> map1 : map.entrySet()) {
            list.add(new UpgradeItemInform(map1.getKey(), map1.getValue()));
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
        Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(stack);

        if (this.hasModules(EnumInfoUpgradeModules.SILK_TOUCH, stack)) {
            enchantmentMap.put(Enchantments.SILK_TOUCH, 1);
        }
        if (this.hasModules(EnumInfoUpgradeModules.LUCKY, stack)) {
            enchantmentMap.put(Enchantments.FORTUNE, this.getModules(EnumInfoUpgradeModules.LUCKY, stack).number);
        }
        if (this.hasModules(EnumInfoUpgradeModules.EFFICIENT, stack)) {
            enchantmentMap.put(Enchantments.EFFICIENCY, (1 + (this.getModules(
                    EnumInfoUpgradeModules.EFFICIENT,
                    stack
            ).number - 1) * 2));
        }
        if (this.hasModules(EnumInfoUpgradeModules.FIRE, stack)) {
            enchantmentMap.put(Enchantments.FIRE_ASPECT, this.getModules(EnumInfoUpgradeModules.FIRE, stack).number);
        }
        if (this.hasModules(EnumInfoUpgradeModules.LOOT, stack)) {
            enchantmentMap.put(Enchantments.LOOTING, this.getModules(EnumInfoUpgradeModules.LOOT, stack).number);
        }
        if (this.hasModules(EnumInfoUpgradeModules.PROTECTION_ARROW, stack)) {
            enchantmentMap.put(Enchantments.PROJECTILE_PROTECTION, (1 + (this.getModules(
                    EnumInfoUpgradeModules.PROTECTION_ARROW,
                    stack
            ).number - 1) * 2));
        }

        EnchantmentHelper.setEnchantments(enchantmentMap, stack);
    }

    @Override
    public void updateBlackListFromNBT(final IUpgradeWithBlackList item, final ItemStack stack, NBTTagCompound nbt) {
        this.updateListFromNBT(item, stack);
        List<String> lst = new ArrayList();
        for (int j = 0; j < 18; j++) {
            String l = "number_" + j;
            if (!nbt.getString(l).isEmpty()) {
                lst.add(nbt.getString(l));
            }
        }
        nbt = ModUtils.nbt(stack);
        final int id = nbt.getInteger("ID_Item");
        nbt.setBoolean("accept_blacklist", true);
        if (this.map_blackList.containsKey(id)) {
            this.map_blackList.replace(id, lst);
        } else {
            this.map_blackList.put(id, lst);
        }

    }

}
