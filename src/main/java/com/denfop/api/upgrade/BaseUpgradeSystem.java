package com.denfop.api.upgrade;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.upgrade.event.EventItemBlackListLoad;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.modules.ItemUpgradeModule;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
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
        this.list_modificators = new ArrayList<>();
        this.map_modification = new HashMap<>();

        MinecraftForge.EVENT_BUS.register(this);

    }

    public static void addupgrade(Item container, ItemStack fill) {
        NBTTagCompound nbt = ModUtils.nbt();
        nbt.setString(
                "mode_module",
                fill.getItem() instanceof ItemUpgradeModule ? ItemUpgradeModule.getType(fill.getItemDamage()).name : "blacklist"
        );
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "upgradeblock",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(container, 1, OreDictionary.WILDCARD_VALUE)),
                                input.getInput(fill)
                        ),
                        new RecipeOutput(nbt, new ItemStack(container, 1, OreDictionary.WILDCARD_VALUE))
                )
        );
        Recipes.recipes.addRecipe(
                "antiupgradeblock",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(container, 1, OreDictionary.WILDCARD_VALUE))
                        ),
                        new RecipeOutput(nbt, fill)
                )
        );
    }

    public static void addupgrade(Item container, ItemStack fill, String type) {
        NBTTagCompound nbt = ModUtils.nbt();
        nbt.setString(
                "type",
                type
        );
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "upgradeblock",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(container, 1, OreDictionary.WILDCARD_VALUE)),
                                input.getInput(fill)
                        ),
                        new RecipeOutput(nbt, new ItemStack(container, 1, OreDictionary.WILDCARD_VALUE))
                )
        );
    }

    public void addModification() {
        this.list_modificators.clear();
        this.list_modificators.add(new UpgradeModificator(new ItemStack(IUItem.core, 1, 7), "0"));
        this.list_modificators.add(new UpgradeModificator(new ItemStack(IUItem.neutroniumingot, 1), "1"));
    }

    public void addModificate(ItemStack container, String name) {
        NBTTagCompound nbt = ModUtils.nbt(container);
        nbt.setString("modification_module" + name, name);
    }

    public boolean needModificate(ItemStack container, ItemStack fill) {
        final List<UpgradeModificator> list = this.getListModifications(container);
        for (UpgradeModificator modificator : list) {
            if (modificator.matches(fill)) {
                return false;
            }
        }
        return true;
    }

    @SubscribeEvent
    public void loadItem(EventItemLoad event) {

        this.updateListFromNBT(event.item, event.stack);
    }

    @SubscribeEvent
    public void loadItem(EventItemBlackListLoad event) {

        this.updateBlackListFromNBT(event.item, event.stack, event.nbt);
    }

    @Override
    public boolean getModifications(final ItemStack item) {
        return this.map_modification.containsKey(ModUtils.nbt(item).getInteger("ID_Item"));
    }

    @Override
    public List<UpgradeModificator> getListModifications(final ItemStack item) {
        final int id = ModUtils.nbt(item).getInteger("ID_Item");
        final List<UpgradeModificator> list = this.map_modification.get(id);
        return list != null ? list : new ArrayList<>();
    }

    @Override
    public int getRemaining(final ItemStack item) {
        final NBTTagCompound nbt = ModUtils.nbt(item);
        final int id = nbt.getInteger("ID_Item");
        int k = this.getListModifications(item).size();
        return this.map_col.getOrDefault(id, 4 + k);
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
    public List<String> getBlackList(final ItemStack item) {
        final NBTTagCompound nbt = ModUtils.nbt(item);
        final int id = nbt.getInteger("ID_Item");
        final List<String> list = this.map_blackList.get(id);
        return list != null ? list : new ArrayList<>();

    }

    @Override
    public List<UpgradeItemInform> getInformation(final ItemStack item) {
        final NBTTagCompound nbt = ModUtils.nbt(item);
        final int id = nbt.getInteger("ID_Item");
        final List<UpgradeItemInform> list = this.map.get(id);
        return list != null ? list : new ArrayList<>();
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
    public UpgradeItemInform getModules(final EnumInfoUpgradeModules module, final ItemStack item, List<UpgradeItemInform> list) {
        for (UpgradeItemInform upgrade : list) {
            if (upgrade.matched(module)) {
                return upgrade;
            }
        }
        return null;
    }

    @Override
    public boolean hasModules(final EnumInfoUpgradeModules module, final ItemStack item) {
        if (!(item.getItem() instanceof IUpgradeItem)) {
            return false;
        }
        List<UpgradeItemInform> list = getInformation(item);
        for (UpgradeItemInform upgrade : list) {
            if (upgrade.matched(module)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasModules(final EnumInfoUpgradeModules module, final ItemStack item, List<UpgradeItemInform> list) {
        if (!(item.getItem() instanceof IUpgradeItem)) {
            return false;
        }
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
        int id = nbt.getInteger("ID_Item");
        if (!hasID) {
            id = this.max;
            this.max++;
            nbt.setInteger("ID_Item", id);
            nbt.setBoolean("hasID", true);
        }
        List<EnumInfoUpgradeModules> lst = new ArrayList<>();
        List<UpgradeModificator> list = new ArrayList<>();
        this.addModification();
        for (int i = 0; i < 2; i++) {
            String name = nbt.getString("modification_module" + i);
            if (name.isEmpty()) {
                continue;
            }

            if (getModification(i) != null) {
                list.add(getModification(i));
            }
        }
        final List<UpgradeModificator> list1 = this.map_modification.get(id);
        if (list1 != null) {
            list1.clear();
            list1.addAll(list);
        } else {
            this.map_modification.put(id, list);
        }
        int ost;
        int empty = 0;
        boolean canupgrade = false;
        for (int i = 0; i < 4 + list.size(); i++) {
            String name = nbt.getString("mode_module" + i);
            if (name.equals("")) {
                empty++;
                canupgrade = true;
            }
            if (!name.equals("")) {
                int index = IUItem.list.indexOf(name);
                lst.add(EnumInfoUpgradeModules.getFromID(index));
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

    private UpgradeModificator getModification(int i) {
        if (i == 0) {
            return this.list_modificators.get(0);
        }
        if (i == 1) {
            return this.list_modificators.get(1);
        }
        return null;
    }

    @Override
    public void setInformation(final IUpgradeItem item, List<EnumInfoUpgradeModules> lst, ItemStack stack) {
        this.write(item, lst, stack);
    }

    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload event) {
        if (event.getWorld().isRemote) {
            return;
        }
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
        } else {
            enchantmentMap.remove(Enchantments.SILK_TOUCH);
        }
        if (this.hasModules(EnumInfoUpgradeModules.LUCKY, stack)) {
            enchantmentMap.put(Enchantments.FORTUNE, this.getModules(EnumInfoUpgradeModules.LUCKY, stack).number);
        } else {
            enchantmentMap.remove(Enchantments.FORTUNE);
        }
        if (this.hasModules(EnumInfoUpgradeModules.EFFICIENT, stack)) {
            enchantmentMap.put(Enchantments.EFFICIENCY, (1 + (this.getModules(
                    EnumInfoUpgradeModules.EFFICIENT,
                    stack
            ).number - 1) * 2));
        } else {
            enchantmentMap.remove(Enchantments.EFFICIENCY);
        }
        if (this.hasModules(EnumInfoUpgradeModules.FIRE, stack)) {
            enchantmentMap.put(Enchantments.FIRE_ASPECT, this.getModules(EnumInfoUpgradeModules.FIRE, stack).number);
        } else {
            enchantmentMap.remove(Enchantments.FIRE_ASPECT);
        }
        if (this.hasModules(EnumInfoUpgradeModules.LOOT, stack)) {
            enchantmentMap.put(Enchantments.LOOTING, this.getModules(EnumInfoUpgradeModules.LOOT, stack).number);
        } else {
            enchantmentMap.remove(Enchantments.LOOTING);
        }
        if (this.hasModules(EnumInfoUpgradeModules.PROTECTION_ARROW, stack)) {
            enchantmentMap.put(Enchantments.PROJECTILE_PROTECTION, (1 + (this.getModules(
                    EnumInfoUpgradeModules.PROTECTION_ARROW,
                    stack
            ).number - 1) * 2));
        } else {
            enchantmentMap.remove(Enchantments.PROJECTILE_PROTECTION);
        }

        EnchantmentHelper.setEnchantments(enchantmentMap, stack);
    }

    @Override
    public void updateBlackListFromNBT(final IUpgradeWithBlackList item, final ItemStack stack, NBTTagCompound nbt) {
        this.updateListFromNBT(item, stack);
        List<String> lst = new ArrayList<>();
        int size = nbt.getInteger("size");
        ModUtils.nbt(stack).setInteger("size", size);
        for (int j = 0; j < size; j++) {
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

    public void updateBlackListFromStack(final ItemStack stack) {
        List<String> lst = new ArrayList<>();
        NBTTagCompound nbt = ModUtils.nbt(stack);
        int size = nbt.getInteger("size");
        for (int j = 0; j < size; j++) {
            String l = "number_" + j;
            if (!nbt.getString(l).isEmpty()) {
                lst.add(nbt.getString(l));
            }
        }
        final int id = nbt.getInteger("ID_Item");
        nbt.setBoolean("accept_blacklist", true);
        if (this.map_blackList.containsKey(id)) {
            this.map_blackList.replace(id, lst);
        } else {
            this.map_blackList.put(id, lst);
        }
    }

    @Override
    public void removeUpdate(final ItemStack stack, final World world, final int index) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setString("mode_module" + index, "");
        MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, (IUpgradeItem) stack.getItem(), stack));

    }

    @Override
    public List<ItemStack> getListStack(final ItemStack stack) {
        List<ItemStack> list = new ArrayList<>();
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        int size = this.getListModifications(stack).size();
        for (int i = 0; i < 4 + size; i++) {
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
    public void addRecipe(final Item stack, final List<EnumInfoUpgradeModules> lst) {
        for (EnumInfoUpgradeModules upgrades : lst) {
            addupgrade(stack, new ItemStack(IUItem.upgrademodule, 1, upgrades.ordinal()));

        }
        if (lst.contains(EnumInfoUpgradeModules.DIG_DEPTH)) {
            addupgrade(stack, new ItemStack(IUItem.module9, 1, 12));

        }
        for (UpgradeModificator modificator : this.list_modificators) {
            addupgrade(stack, modificator.itemstack, modificator.type);
        }
    }

    @Override
    public boolean shouldUpdate(final EnumInfoUpgradeModules type, final ItemStack stack1) {
        List<UpgradeItemInform> list = getInformation(stack1);
        for (UpgradeItemInform inform : list) {
            if (inform.upgrade == type) {
                if (inform.number >= type.max) {
                    return false;
                }
            }
        }
        return true;
    }

}
