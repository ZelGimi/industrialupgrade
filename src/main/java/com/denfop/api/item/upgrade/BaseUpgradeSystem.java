package com.denfop.api.item.upgrade;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.item.upgrade.event.EventItemBlackListLoad;
import com.denfop.api.item.upgrade.event.EventItemLoad;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.modules.ItemUpgradeModule;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;
import java.util.stream.Collectors;

public class BaseUpgradeSystem implements UpgradeBaseSystem {

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
        CompoundTag nbt = ModUtils.nbt();
        nbt.putString(
                "mode_module",
                fill.getItem() instanceof ItemUpgradeModule ? ItemUpgradeModule.getType(IUItem.upgrademodule.getMeta((ItemUpgradeModule) fill.getItem())).name : "blacklist"
        );
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "upgradeblock",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(container, 1)),
                                input.getInput(fill)
                        ),
                        new RecipeOutput(nbt, new ItemStack(container, 1))
                )
        );
        Recipes.recipes.addRecipe(
                "antiupgradeblock",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(container, 1))
                        ),
                        new RecipeOutput(nbt, fill)
                )
        );
    }

    public static void addupgrade(Item container, ItemStack fill, String type) {
        CompoundTag nbt = ModUtils.nbt();
        nbt.putString(
                "type",
                type
        );
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "upgradeblock",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(container, 1)),
                                input.getInput(fill)
                        ),
                        new RecipeOutput(nbt, new ItemStack(container, 1))
                )
        );
    }

    public void addModification() {
        if (this.list_modificators.isEmpty()) {
            this.list_modificators.add(new UpgradeModificator(IUItem.core.getRegistryObject(7), "0"));
            this.list_modificators.add(new UpgradeModificator(IUItem.neutroniumingot.getRegistryObject(), "1"));
        }
    }

    public List<Integer> getPositiveUpgradeFromLevel(ItemStack stack) {
        if (stack.getItem() instanceof LevelInstruments) {
            LevelInstruments levelInstruments = (LevelInstruments) stack.getItem();
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
        CompoundTag nbt = ModUtils.nbt(container);
        ListTag modificationsTagList = nbt.getList("modifications", 10);
        for (int i = 0; i < list_modificators.size(); i++) {
            final UpgradeModificator modificator = list_modificators.get(i);
            if (modificator.matches(name)) {
                CompoundTag tagCompound = new CompoundTag();
                tagCompound.putInt("index", i);
                modificationsTagList.add(tagCompound);
                nbt.put("modifications", modificationsTagList);
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
        return this.map_modification.containsKey(ModUtils.nbt(item).getInt("ID_Item"));
    }

    @Override
    public List<UpgradeModificator> getListModifications(final ItemStack item) {
        final int id = ModUtils.nbt(item).getInt("ID_Item");
        final List<UpgradeModificator> list = this.map_modification.get(id);
        return list != null ? list : new ArrayList<>();
    }

    @Override
    public int getRemaining(final ItemStack item) {
        final CompoundTag nbt = ModUtils.nbt(item);
        final int id = nbt.getInt("ID_Item");
        int k = this.getListModifications(item).size();
        return this.map_col.getOrDefault(id, 4 + k);
    }

    @Override
    public boolean hasBlackList(final ItemStack item) {
        final CompoundTag nbt = ModUtils.nbt(item);
        final int id = nbt.getInt("ID_Item");
        return map_blackList.containsKey(id);
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
    public List<String> getBlackList(final ItemStack item) {
        final CompoundTag nbt = ModUtils.nbt(item);
        final int id = nbt.getInt("ID_Item");
        final List<String> list = this.map_blackList.get(id);
        return list != null ? list : Collections.emptyList();

    }

    @Override
    public List<UpgradeItemInform> getInformation(final ItemStack item) {
        final CompoundTag nbt = ModUtils.nbt(item);
        final int id = nbt.getInt("ID_Item");
        final List<UpgradeItemInform> list = this.map.get(id);
        return list != null ? list : Collections.emptyList();
    }

    public List<String> getAvailableUpgrade(UpgradeItem upgradeItem, final ItemStack item) {
        final CompoundTag nbt = ModUtils.nbt(item);
        final int id = nbt.getInt("ID_Item");
        final List<UpgradeItemInform> list = this.map.get(id);
        final List<EnumInfoUpgradeModules> list1 = upgradeItem.getUpgradeModules();
        final List<String> stringList = new LinkedList<>();
        cycle:
        for (EnumInfoUpgradeModules enumInfoUpgradeModules : list1) {
            for (UpgradeItemInform upgradeItemInform : list) {
                if (upgradeItemInform.upgrade.equals(enumInfoUpgradeModules)) {
                    if (upgradeItemInform.number < upgradeItemInform.upgrade.max) {
                        stringList.add(ChatFormatting.GREEN + "" + (upgradeItemInform.upgrade.max - upgradeItemInform.number) + "x " + (new ItemStack(
                                IUItem.upgrademodule.getItemFromMeta(enumInfoUpgradeModules.ordinal()),
                                1
                        ).getDisplayName().getString()));
                    }
                    continue cycle;
                }
            }
            stringList.add(ChatFormatting.GREEN + "" + (enumInfoUpgradeModules.max) + "x " + (new ItemStack(
                    IUItem.upgrademodule.getItemFromMeta(enumInfoUpgradeModules.ordinal()),
                    1

            ).getDisplayName().getString()));

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
        if (!(item.getItem() instanceof UpgradeItem)) {
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
        if (!(item.getItem() instanceof UpgradeItem)) {
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
    public void updateListFromNBT(final UpgradeItem item, ItemStack stack) {
        final CompoundTag nbt = ModUtils.nbt(stack);
        boolean hasID = nbt.getBoolean("hasID");
        int id = nbt.getInt("ID_Item");

        if (!hasID) {
            id = this.max;
            this.max++;
            nbt.putInt("ID_Item", id);
            nbt.putBoolean("hasID", true);
        }


        ListTag modificationsTagList = nbt.getList("modifications", 10);
        List<UpgradeModificator> list = new ArrayList<>();

        this.addModification();


        for (int i = 0; i < modificationsTagList.size(); i++) {
            CompoundTag modificationTag = modificationsTagList.getCompound(i);
            final int index = modificationTag.getInt("index");
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


        ListTag modesTagList = nbt.getList("modes", 10);
        List<EnumInfoUpgradeModules> lst = new ArrayList<>();
        for (int i = 0; i < modesTagList.size(); i++) {
            CompoundTag modeTag = modesTagList.getCompound(i);
            int index = modeTag.getInt("index");
            lst.add(EnumInfoUpgradeModules.getFromID(index));
        }

        int ost = modificationsTagList.size() + 4 - modesTagList.size();
        nbt.putBoolean("canupgrade", ost > 0);

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
        final CompoundTag nbt = ModUtils.nbt(stack);
        List<Integer> level;
        int id = nbt.getInt("ID_Item");
        if (this.levelMap.containsKey(id)) {
            this.levelMap.replace(id, level = getPositiveUpgradeFromLevel(stack));
        } else {
            this.levelMap.put(id, level = getPositiveUpgradeFromLevel(stack));
        }
        return level;
    }

    public List<Integer> getUpgradeFromList(ItemStack stack) {
        final CompoundTag nbt = ModUtils.nbt(stack);
        final int id = nbt.getInt("ID_Item");
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
    public void setInformation(final UpgradeItem item, List<EnumInfoUpgradeModules> lst, ItemStack stack) {
        this.write(item, lst, stack);
    }

    @SubscribeEvent
    public void onWorldUnload(final LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
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
    public void write(final UpgradeItem item, final List<EnumInfoUpgradeModules> lst, ItemStack stack) {
        CompoundTag nbt = ModUtils.nbt(stack);
        final int id = nbt.getInt("ID_Item");

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
                Enchantments.BLOCK_FORTUNE,
                EnumInfoUpgradeModules.LUCKY,
                this.getModulesValue(EnumInfoUpgradeModules.LUCKY, stack),
                stack
        );
        updateEnchantment(
                enchantmentMap,
                Enchantments.BLOCK_EFFICIENCY,
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
                Enchantments.MOB_LOOTING,
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
        UpgradeItemInform modules = this.getModules(module, stack);
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
    public void updateBlackListFromNBT(final UpgradeWithBlackList item, final ItemStack stack, CompoundTag nbt) {
        this.updateListFromNBT(item, stack);

        final int id = nbt.getInt("ID_Item");
        ListTag tagList = nbt.getList("blacklist", 8);
        List<String> blacklist = new LinkedList<>();

        for (int i = 0; i < tagList.size(); i++) {
            blacklist.add(tagList.getString(i));
        }
        blacklist = new ArrayList<>(blacklist);
        nbt.putBoolean("accept_blacklist", true);

        if (this.map_blackList.containsKey(id)) {
            this.map_blackList.replace(id, blacklist);
        } else {
            this.map_blackList.put(id, blacklist);
        }

    }

    public void updateLevel(ItemStack stack) {
        final CompoundTag nbt = ModUtils.nbt(stack);
        final int id = nbt.getInt("ID_Item");
        if (this.levelMap.containsKey(id)) {
            this.levelMap.replace(id, getPositiveUpgradeFromLevel(stack));
        } else {
            this.levelMap.put(id, getPositiveUpgradeFromLevel(stack));
        }
    }

    public void updateBlackListFromStack(final ItemStack stack) {
        List<String> lst = new LinkedList<>();
        CompoundTag nbt = ModUtils.nbt(stack);
        ListTag tagList = nbt.getList("blacklist", 8);


        for (int i = 0; i < tagList.size(); i++) {
            lst.add(tagList.getString(i));
        }

        lst = new ArrayList<>(lst);
        final int id = nbt.getInt("ID_Item");


        nbt.putBoolean("accept_blacklist", true);


        this.map_blackList.put(id, lst);

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
        MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, (UpgradeItem) stack.getItem(), stack));

    }

    @Override
    public List<ItemStack> getListStack(final ItemStack stack) {
        List<ItemStack> list = new LinkedList<>();
        final CompoundTag nbt = ModUtils.nbt(stack);
        ListTag modesTagList = nbt.getList("modes", 10);
        for (int ii = 0; ii < modesTagList.size(); ii++) {
            CompoundTag tagCompound = modesTagList.getCompound(ii);
            list.add(new ItemStack(IUItem.upgrademodule.getItemFromMeta(tagCompound.getInt("index")), 1));
        }
        return list;
    }

    @Override
    public void addRecipe(Item stack, List<EnumInfoUpgradeModules> lst) {
        for (EnumInfoUpgradeModules upgrades : lst) {
            addupgrade(stack, new ItemStack(IUItem.upgrademodule.getStack(upgrades.ordinal()), 1));

        }
        if (lst.contains(EnumInfoUpgradeModules.DIG_DEPTH)) {
            addupgrade(stack, new ItemStack(IUItem.module9.getStack(12), 1));

        }
        for (UpgradeModificator modificator : this.list_modificators) {
            addupgrade(stack, new ItemStack(modificator.itemstack.get()), modificator.type);
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
