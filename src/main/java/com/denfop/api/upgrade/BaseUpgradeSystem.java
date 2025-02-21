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

public class BaseUpgradeSystem implements IUpgradeSystem {

    Map<Integer, List<UpgradeItemInform>> map;
    Map<Integer, List<UpgradeModificator>> map_modification;
    Map<Integer, ItemStack> map_stack;
    Map<Integer, Integer> map_col;

    Map<Integer, List<Integer>> levelMap;
    Map<Integer, List<String>> map_blackList;
    List<UpgradeModificator> list_modificators;
    int max;

    public BaseUpgradeSystem() {
        this.map = new HashMap<>();
        this.map_blackList = new HashMap<>();
        this.max = 0;
        this.map_col = new HashMap<>();
        this.map_stack = new HashMap<>();
        this.levelMap = new HashMap<>();
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
        if (this.list_modificators.isEmpty()) {
            this.list_modificators.add(new UpgradeModificator(new ItemStack(IUItem.core, 1, 7), "0"));
            this.list_modificators.add(new UpgradeModificator(new ItemStack(IUItem.neutroniumingot, 1), "1"));
        }
    }

    public List<Integer> getPositiveUpgradeFromLevel(ItemStack stack) {
        if (stack.getItem() instanceof ILevelInstruments) {
            ILevelInstruments levelInstruments = (ILevelInstruments) stack.getItem();
            final int level = levelInstruments.getLevel(stack);
            int speed = level / 2;
            int less_draw_energy = level / 5;
            int lucky = level / 10;
            int area = level / 15;
            int depth = level / 20;
            List<Integer> integers = new ArrayList<>();
            integers.add(speed);
            integers.add(less_draw_energy);
            integers.add(lucky);
            integers.add(area);
            integers.add(depth);
            return integers;
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public void addModificate(ItemStack container, String name) {
        NBTTagCompound nbt = ModUtils.nbt(container);
        NBTTagList modificationsTagList = nbt.getTagList("modifications", 10);
        for (int i = 0; i < list_modificators.size(); i++) {
            final UpgradeModificator modificator = list_modificators.get(i);
            if (modificator.matches(name)) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setInteger("index", i);
                modificationsTagList.appendTag(tagCompound);
                nbt.setTag("modifications", modificationsTagList);
                break;
            }
        }


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
        return list != null ? list : Collections.emptyList();

    }

    @Override
    public List<UpgradeItemInform> getInformation(final ItemStack item) {
        final NBTTagCompound nbt = ModUtils.nbt(item);
        final int id = nbt.getInteger("ID_Item");
        final List<UpgradeItemInform> list = this.map.get(id);
        return list != null ? list : Collections.emptyList();
    }

    public List<String> getAvailableUpgrade(IUpgradeItem iUpgradeItem, final ItemStack item) {
        final NBTTagCompound nbt = ModUtils.nbt(item);
        final int id = nbt.getInteger("ID_Item");
        final List<UpgradeItemInform> list = this.map.get(id);
        final List<EnumInfoUpgradeModules> list1 = iUpgradeItem.getUpgradeModules();
        final List<String> stringList = new LinkedList<>();
        cycle:
        for (EnumInfoUpgradeModules enumInfoUpgradeModules : list1) {
            for (UpgradeItemInform upgradeItemInform : list) {
                if (upgradeItemInform.upgrade.equals(enumInfoUpgradeModules)) {
                    if (upgradeItemInform.number < upgradeItemInform.upgrade.max) {
                        stringList.add(TextFormatting.GREEN + "" + (upgradeItemInform.upgrade.max - upgradeItemInform.number) + "x " + (new ItemStack(
                                IUItem.upgrademodule,
                                1,
                                enumInfoUpgradeModules.ordinal()
                        ).getDisplayName()));
                    }
                    continue cycle;
                }
            }
            stringList.add(TextFormatting.GREEN + "" + (enumInfoUpgradeModules.max) + "x " + (new ItemStack(
                    IUItem.upgrademodule,
                    1,
                    enumInfoUpgradeModules.ordinal()
            ).getDisplayName()));

        }
        return list != null ? stringList : Collections.emptyList();
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


        NBTTagList modificationsTagList = nbt.getTagList("modifications", 10);
        List<UpgradeModificator> list = new ArrayList<>();

        this.addModification();


        for (int i = 0; i < modificationsTagList.tagCount(); i++) {
            NBTTagCompound modificationTag = modificationsTagList.getCompoundTagAt(i);
            final int index = modificationTag.getInteger("index");
            if (getModification(index) != null) {
                list.add(getModification(index));
            }
        }


        final List<UpgradeModificator> list1 = this.map_modification.get(id);
        if (list1 != null) {
            list1.clear();
            list1.addAll(list);
        } else {
            this.map_modification.put(id, list);
        }


        NBTTagList modesTagList = nbt.getTagList("modes", 10);
        List<EnumInfoUpgradeModules> lst = new ArrayList<>();
        for (int i = 0; i < modesTagList.tagCount(); i++) {
            NBTTagCompound modeTag = modesTagList.getCompoundTagAt(i);
            int index = modeTag.getInteger("index");
            lst.add(EnumInfoUpgradeModules.getFromID(index));
        }

        int ost = modificationsTagList.tagCount() + 4 - modesTagList.tagCount();
        nbt.setBoolean("canupgrade", ost > 0);

        if (this.map_col.containsKey(id)) {
            this.map_col.replace(id, ost);
        } else {
            this.map_col.put(id, ost);
        }

        if (this.levelMap.containsKey(id)) {
            this.levelMap.replace(id, getPositiveUpgradeFromLevel(stack));
        } else {
            this.levelMap.put(id, getPositiveUpgradeFromLevel(stack));
        }

        this.setInformation(item, lst, stack);

    }

    public List<Integer> getLevel(ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        List<Integer> level;
        int id = nbt.getInteger("ID_Item");
        if (this.levelMap.containsKey(id)) {
            this.levelMap.replace(id, level = getPositiveUpgradeFromLevel(stack));
        } else {
            this.levelMap.put(id, level = getPositiveUpgradeFromLevel(stack));
        }
        return level;
    }

    public List<Integer> getUpgradeFromList(ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final int id = nbt.getInteger("ID_Item");
        return this.levelMap.getOrDefault(id, getLevel(stack));
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
        levelMap.clear();
    }

    @Override
    public void write(final IUpgradeItem item, final List<EnumInfoUpgradeModules> lst, ItemStack stack) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        final int id = nbt.getInteger("ID_Item");

        Map<EnumInfoUpgradeModules, Integer> moduleCounts = lst.stream()
                .collect(Collectors.toMap(
                        module -> module,
                        module -> 1,
                        Integer::sum
                ));

        List<UpgradeItemInform> upgrades = moduleCounts.entrySet().stream()
                .map(entry -> new UpgradeItemInform(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());


        this.map.put(id, upgrades);
        this.map_stack.put(id, stack);


        Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(stack);

        updateEnchantment(enchantmentMap, Enchantments.SILK_TOUCH, EnumInfoUpgradeModules.SILK_TOUCH, 1, stack);
        updateEnchantment(
                enchantmentMap,
                Enchantments.FORTUNE,
                EnumInfoUpgradeModules.LUCKY,
                this.getModulesValue(EnumInfoUpgradeModules.LUCKY, stack),
                stack
        );
        updateEnchantment(
                enchantmentMap,
                Enchantments.EFFICIENCY,
                EnumInfoUpgradeModules.EFFICIENT,
                calculateEfficiencyLevel(stack),
                stack
        );
        updateEnchantment(
                enchantmentMap,
                Enchantments.FIRE_ASPECT,
                EnumInfoUpgradeModules.FIRE,
                this.getModulesValue(EnumInfoUpgradeModules.FIRE, stack),
                stack
        );
        updateEnchantment(
                enchantmentMap,
                Enchantments.LOOTING,
                EnumInfoUpgradeModules.LOOT,
                this.getModulesValue(EnumInfoUpgradeModules.LOOT, stack),
                stack
        );
        updateEnchantment(
                enchantmentMap,
                Enchantments.PROJECTILE_PROTECTION,
                EnumInfoUpgradeModules.PROTECTION_ARROW,
                calculateProjectileProtectionLevel(stack),
                stack
        );


        EnchantmentHelper.setEnchantments(enchantmentMap, stack);
    }

    private void updateEnchantment(
            Map<Enchantment, Integer> enchantmentMap,
            Enchantment enchantment,
            EnumInfoUpgradeModules module,
            int level,
            ItemStack stack
    ) {
        if (this.hasModules(module, stack)) {
            enchantmentMap.put(enchantment, level);
        } else {
            enchantmentMap.remove(enchantment);
        }
    }

    private int getModulesValue(EnumInfoUpgradeModules module, ItemStack stack) {
        UpgradeItemInform modules =  this.getModules(module, stack);
        if (modules == null)
            return 0;
        return modules.number;
    }

    private int calculateEfficiencyLevel(ItemStack stack) {
        int baseLevel = 1;
        int moduleLevel = getModulesValue(EnumInfoUpgradeModules.EFFICIENT, stack);
        if (moduleLevel == 0)
            return 0;
        return baseLevel + (moduleLevel - 1) * 2;
    }

    private int calculateProjectileProtectionLevel(ItemStack stack) {
        int baseLevel = 1;
        int moduleLevel = getModulesValue(EnumInfoUpgradeModules.PROTECTION_ARROW, stack);
        if (moduleLevel == 0)
            return 0;
        return baseLevel + (moduleLevel - 1) * 2;
    }

    @Override
    public void updateBlackListFromNBT(final IUpgradeWithBlackList item, final ItemStack stack, NBTTagCompound nbt) {
        this.updateListFromNBT(item, stack);

        final int id = nbt.getInteger("ID_Item");
        NBTTagList tagList = nbt.getTagList("blacklist", 8);
        List<String> blacklist = new LinkedList<>();

        for (int i = 0; i < tagList.tagCount(); i++) {
            blacklist.add(tagList.getStringTagAt(i));
        }
        blacklist = new ArrayList<>(blacklist);
        nbt.setBoolean("accept_blacklist", true);

        if (this.map_blackList.containsKey(id)) {
            this.map_blackList.replace(id, blacklist);
        } else {
            this.map_blackList.put(id, blacklist);
        }

    }

    public void updateLevel(ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final int id = nbt.getInteger("ID_Item");
        if (this.levelMap.containsKey(id)) {
            this.levelMap.replace(id, getPositiveUpgradeFromLevel(stack));
        } else {
            this.levelMap.put(id, getPositiveUpgradeFromLevel(stack));
        }
    }

    public void updateBlackListFromStack(final ItemStack stack) {
        List<String> lst = new LinkedList<>();
        NBTTagCompound nbt = ModUtils.nbt(stack);
        NBTTagList tagList = nbt.getTagList("blacklist", 8);


        for (int i = 0; i < tagList.tagCount(); i++) {
            lst.add(tagList.getStringTagAt(i));
        }

        lst = new ArrayList<>(lst);
        final int id = nbt.getInteger("ID_Item");


        nbt.setBoolean("accept_blacklist", true);


        this.map_blackList.put(id, lst);

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
        MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, (IUpgradeItem) stack.getItem(), stack));

    }

    @Override
    public List<ItemStack> getListStack(final ItemStack stack) {
        List<ItemStack> list = new LinkedList<>();
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        NBTTagList modesTagList = nbt.getTagList("modes", 10);
        for (int ii = 0; ii < modesTagList.tagCount(); ii++) {
            NBTTagCompound tagCompound = modesTagList.getCompoundTagAt(ii);
            list.add(new ItemStack(IUItem.upgrademodule, 1, tagCompound.getInteger("index")));
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
