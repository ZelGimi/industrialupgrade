package com.denfop.api.upgrade;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.upgrade.event.EventItemBlackListLoad;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.datacomponent.UpgradeItem;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.modules.ItemUpgradeModule;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BaseUpgradeSystem implements IUpgradeSystem {


    List<UpgradeModificator> list_modificators;
    int max;

    public BaseUpgradeSystem() {
        this.max = 0;
        this.list_modificators = new ArrayList<>();

        NeoForge.EVENT_BUS.register(this);

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
            this.list_modificators.add(new UpgradeModificator(IUItem.core.getStack(7), "0"));
            this.list_modificators.add(new UpgradeModificator(IUItem.neutroniumingot.getItem(), "1"));
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
        UpgradeItem upgradeItem = container.get(DataComponentsInit.UPGRADE_ITEM);
        for (final UpgradeModificator modification : list_modificators) {
            if (modification.matches(name) && !upgradeItem.upgradeModificators().contains(modification)) {
                List<UpgradeModificator> upgradeModificatorList = upgradeItem.upgradeModificators();
                upgradeModificatorList.add(modification);
                upgradeItem.updateModificator(container, upgradeModificatorList);
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
        if (event.getLevel().isClientSide())
            return;
        RegistryAccess.Frozen registryAccess = ((ServerLevel) event.getLevel()).getServer().registryAccess();
        this.updateListFromNBT(event.item, event.stack, registryAccess);
    }

    @SubscribeEvent
    public void loadItem(EventItemBlackListLoad event) {
        if (event.getLevel().isClientSide())
            return;
        RegistryAccess.Frozen registryAccess = ((ServerLevel) event.getLevel()).getServer().registryAccess();
        this.updateBlackListFromNBT(event.item, event.stack, event.nbt, registryAccess);
    }

    @Override
    public boolean getModifications(final ItemStack item) {
        return !item.getOrDefault(DataComponentsInit.UPGRADE_ITEM, UpgradeItem.EMPTY).upgradeModificators().isEmpty();
    }

    @Override
    public List<UpgradeModificator> getListModifications(final ItemStack item) {
        final List<UpgradeModificator> list = item.getOrDefault(DataComponentsInit.UPGRADE_ITEM, UpgradeItem.EMPTY).upgradeModificators();
        return list != null ? list : new ArrayList<>();
    }

    @Override
    public int getRemaining(final ItemStack item) {
        int k = this.getListModifications(item).size();
        return 4 + k - item.get(DataComponentsInit.UPGRADE_ITEM).amount();
    }

    @Override
    public boolean hasBlackList(final ItemStack item) {
        return !item.getOrDefault(DataComponentsInit.UPGRADE_ITEM, UpgradeItem.EMPTY).blackList().isEmpty();
    }

    @Override
    public boolean hasInMap(final ItemStack stack) {

        return stack.has(DataComponentsInit.UPGRADE_ITEM) && !stack.get(DataComponentsInit.UPGRADE_ITEM).equals(UpgradeItem.EMPTY);
    }

    @Override
    public List<String> getBlackList(final ItemStack item) {
        final List<String> list = item.getOrDefault(DataComponentsInit.UPGRADE_ITEM, UpgradeItem.EMPTY).blackList();
        return list != null ? list : Collections.emptyList();

    }

    @Override
    public List<UpgradeItemInform> getInformation(final ItemStack item) {
        return item.getOrDefault(DataComponentsInit.UPGRADE_ITEM, UpgradeItem.EMPTY).upgradeItemInforms();
    }

    public List<String> getAvailableUpgrade(IUpgradeItem iUpgradeItem, final ItemStack item) {
        UpgradeItem upgradeItem = item.getOrDefault(DataComponentsInit.UPGRADE_ITEM, UpgradeItem.EMPTY);
        final List<UpgradeItemInform> list = upgradeItem.upgradeItemInforms();
        final List<EnumInfoUpgradeModules> list1 = iUpgradeItem.getUpgradeModules();
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
    public void updateListFromNBT(final IUpgradeItem item, ItemStack stack, RegistryAccess registryAccess) {
        UpgradeItem upgradeItem = stack.getOrDefault(DataComponentsInit.UPGRADE_ITEM, UpgradeItem.EMPTY);
        boolean hasID = upgradeItem == UpgradeItem.EMPTY;

        if (!hasID) {
            this.max++;
            upgradeItem = stack.set(DataComponentsInit.UPGRADE_ITEM, UpgradeItem.EMPTY.copy());
        }

        this.addModification();
        int modesTagList = upgradeItem.amount();
        int ost = upgradeItem.upgradeModificators().size() + 4 - modesTagList;
        upgradeItem = upgradeItem.updateCanUpgrade(stack, ost > 0);
        upgradeItem = upgradeItem.updateListUpgrades(stack, getPositiveUpgradeFromLevel(stack));

        ItemEnchantments.Mutable itemenchantments$mutable = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(stack));
        itemenchantments$mutable.removeIf(enchantmentHolder -> true);
        stack.set(DataComponents.ENCHANTMENTS, itemenchantments$mutable.toImmutable());

        updateEnchantment(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH), EnumInfoUpgradeModules.SILK_TOUCH, 1, stack);
        updateEnchantment(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE), EnumInfoUpgradeModules.LUCKY, getModulesValue(EnumInfoUpgradeModules.LUCKY, stack), stack);
        updateEnchantment(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.EFFICIENCY), EnumInfoUpgradeModules.EFFICIENT, calculateEfficiencyLevel(stack), stack);
        updateEnchantment(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FIRE_ASPECT), EnumInfoUpgradeModules.FIRE, getModulesValue(EnumInfoUpgradeModules.FIRE, stack), stack);
        updateEnchantment(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.LOOTING), EnumInfoUpgradeModules.LOOT, getModulesValue(EnumInfoUpgradeModules.LOOT, stack), stack);
        updateEnchantment(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.PROJECTILE_PROTECTION), EnumInfoUpgradeModules.PROTECTION_ARROW, calculateProjectileProtectionLevel(stack), stack);

    }


    public List<Integer> getUpgradeFromList(ItemStack stack) {
        return stack.getOrDefault(DataComponentsInit.UPGRADE_ITEM, UpgradeItem.EMPTY).listUpgrades();
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
    public void setInformation(final IUpgradeItem item, List<EnumInfoUpgradeModules> lst, ItemStack stack, RegistryAccess registryAccess) {
        this.write(item, lst, stack, registryAccess);
    }

    @SubscribeEvent
    public void onWorldUnload(final LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        this.max = 0;
        this.list_modificators.clear();
    }

    @Override
    public void write(final IUpgradeItem item, final List<EnumInfoUpgradeModules> lst, ItemStack stack, RegistryAccess registryAccess) {

        ItemEnchantments.Mutable itemenchantments$mutable = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(stack));
        itemenchantments$mutable.removeIf(enchantmentHolder -> true);
        stack.set(DataComponents.ENCHANTMENTS, itemenchantments$mutable.toImmutable());
        updateEnchantment(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH), EnumInfoUpgradeModules.SILK_TOUCH, 1, stack);
        updateEnchantment(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE), EnumInfoUpgradeModules.LUCKY, getModulesValue(EnumInfoUpgradeModules.LUCKY, stack), stack);
        updateEnchantment(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.EFFICIENCY), EnumInfoUpgradeModules.EFFICIENT, calculateEfficiencyLevel(stack), stack);
        updateEnchantment(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FIRE_ASPECT), EnumInfoUpgradeModules.FIRE, getModulesValue(EnumInfoUpgradeModules.FIRE, stack), stack);
        updateEnchantment(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.LOOTING), EnumInfoUpgradeModules.LOOT, getModulesValue(EnumInfoUpgradeModules.LOOT, stack), stack);
        updateEnchantment(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.PROJECTILE_PROTECTION), EnumInfoUpgradeModules.PROTECTION_ARROW, calculateProjectileProtectionLevel(stack), stack);
        ;
    }


    private void updateEnchantment(Holder<Enchantment> enchantment, EnumInfoUpgradeModules module, int level, ItemStack stack) {
        if (hasModules(module, stack)) {
            stack.enchant(enchantment, level);
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

    public void updateBlackListFromNBT(final IUpgradeWithBlackList item, final ItemStack stack, CompoundTag nbt, RegistryAccess registryAccess) {
        this.updateListFromNBT(item, stack, registryAccess);


    }

    public void updateLevel(ItemStack stack) {
        stack.get(DataComponentsInit.UPGRADE_ITEM).updateListUpgrades(stack, getPositiveUpgradeFromLevel(stack));

    }

    public void updateBlackListFromStack(final ItemStack stack) {


    }

    @Override
    public void removeUpdate(final ItemStack stack, final Level world, final int index) {
        UpgradeItem upgradeItem = stack.get(DataComponentsInit.UPGRADE_ITEM);
        if (upgradeItem != null) {
            List<UpgradeItemInform> listInform = upgradeItem.upgradeItemInforms();
            UpgradeItemInform needRemove = null;
            cycle:
            for (UpgradeItemInform inform : upgradeItem.upgradeItemInforms()) {
                if (index == inform.upgrade.ordinal()) {
                    if (inform.number > 1) {
                        inform.number--;
                        break cycle;
                    } else {
                        needRemove = inform;
                    }
                }
            }
            RegistryAccess registryAccess = ((ServerLevel) world).getServer().registryAccess();
            ItemEnchantments.Mutable itemenchantments$mutable = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(stack));
            itemenchantments$mutable.removeIf(enchantmentHolder -> true);
            stack.set(DataComponents.ENCHANTMENTS, itemenchantments$mutable.toImmutable());
            updateEnchantment(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH), EnumInfoUpgradeModules.SILK_TOUCH, 1, stack);
            updateEnchantment(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE), EnumInfoUpgradeModules.LUCKY, getModulesValue(EnumInfoUpgradeModules.LUCKY, stack), stack);
            updateEnchantment(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.EFFICIENCY), EnumInfoUpgradeModules.EFFICIENT, calculateEfficiencyLevel(stack), stack);
            updateEnchantment(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FIRE_ASPECT), EnumInfoUpgradeModules.FIRE, getModulesValue(EnumInfoUpgradeModules.FIRE, stack), stack);
            updateEnchantment(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.LOOTING), EnumInfoUpgradeModules.LOOT, getModulesValue(EnumInfoUpgradeModules.LOOT, stack), stack);
            updateEnchantment(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.PROJECTILE_PROTECTION), EnumInfoUpgradeModules.PROTECTION_ARROW, calculateProjectileProtectionLevel(stack), stack);
            if (needRemove != null) {
                listInform.remove(needRemove);
                upgradeItem = upgradeItem.updateAmount(stack, upgradeItem.amount() - 1);
                int modesTagList = upgradeItem.amount();
                int ost = upgradeItem.upgradeModificators().size() + 4 - modesTagList;
                upgradeItem = upgradeItem.updateCanUpgrade(stack, ost > 0);
            }
            upgradeItem = upgradeItem.updateUpgrades(stack, listInform);
        }
    }

    @Override
    public List<ItemStack> getListStack(final ItemStack stack) {
        UpgradeItem upgradeItem = stack.get(DataComponentsInit.UPGRADE_ITEM);
        List<ItemStack> list = new LinkedList<>();
        if (upgradeItem != null) {
            for (UpgradeItemInform inform : upgradeItem.upgradeItemInforms()) {
                for (int i = 0; i < inform.number; i++) {
                    list.add(new ItemStack(IUItem.upgrademodule.getItemFromMeta(inform.upgrade.ordinal()), 1));
                }
            }
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
            addupgrade(stack, new ItemStack(modificator.itemstack), modificator.type);
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
