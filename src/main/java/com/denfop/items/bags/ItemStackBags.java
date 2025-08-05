package com.denfop.items.bags;

import com.denfop.ElectricItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.container.ContainerBags;
import com.denfop.container.ContainerBase;
import com.denfop.datacomponent.ContainerAdditionalItem;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.gui.GuiBags;
import com.denfop.gui.GuiCore;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.ItemStackInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemStackBags extends ItemStackInventory {


    public final ItemStack itemStack1;
    public final List<ItemStack> list;
    private final double coef;
    public ContainerAdditionalItem containerAdditionItem;


    public ItemStackBags(Player player, ItemStack stack, int inventorySize) {
        super(player, stack, inventorySize);
        this.containerAdditionItem = containerStack.get(DataComponentsInit.CONTAINER_ADDITIONAL);
        if (this.containerAdditionItem == null) {
            this.containerAdditionItem = ContainerAdditionalItem.EMPTY.updateItems(containerStack, new ArrayList<>());
            containerStack.set(DataComponentsInit.CONTAINER_ADDITIONAL, containerAdditionItem);
        }
        if (containerAdditionItem.listItem().isEmpty()) {
            ItemStack[] object = new ItemStack[9];
            Arrays.fill(object, ItemStack.EMPTY);
            List<ItemStack> list = Arrays.asList(object);
            containerAdditionItem = containerAdditionItem.updateItems(containerStack, list);
        }
        this.list = containerAdditionItem.listItem();
        this.itemStack1 = stack;
        this.coef = 1D - (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, stack).number * 0.25D : 0);

    }

    @Override
    public ItemStack getItem(int slot) {
        return slot >= inventorySize ? this.list.get(slot - inventorySize) : this.inventory.get(slot);

    }

    public void save() {
        super.save();
        if (!this.player.level().isClientSide) {
            if (!this.cleared) {
                boolean dropItself = false;

                for (int i = 0; i < this.list.size(); ++i) {
                    if (this.isThisContainer(this.list.get(i))) {
                        this.list.set(i, ItemStack.EMPTY);
                        dropItself = true;
                    }
                }

                ListTag contentList = new ListTag();

                int idx;
                this.containerAdditionItem = this.containerAdditionItem.updateItems(containerStack, list);

                if (dropItself) {
                    ModUtils.dropAsEntity(this.player.level(), this.player.blockPosition(), this.containerStack);
                    this.clear();
                } else {
                    idx = this.getPlayerInventoryIndex();
                    if (idx < -1) {
                        this.clear();
                    } else if (idx == -1) {
                        this.player.getInventory().setPickedItem(this.containerStack);
                    } else {
                        this.player.getInventory().setItem(idx, this.containerStack);
                    }
                }

            }
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot < this.inventory.size()) {
            if (!ModUtils.isEmpty(stack) && ModUtils.getSize(stack) > this.getInventoryStackLimit()) {
                stack = ModUtils.setSize(stack, this.getInventoryStackLimit());
            }

            if (ModUtils.isEmpty(stack)) {
                this.inventory.set(slot, ModUtils.emptyStack);
            } else {
                this.inventory.set(slot, stack);
            }
        } else {
            if (!ModUtils.isEmpty(stack) && ModUtils.getSize(stack) > this.getInventoryStackLimit()) {
                stack = ModUtils.setSize(stack, this.getInventoryStackLimit());
            }
            if (ModUtils.isEmpty(stack)) {
                this.list.set(slot - this.inventory.size(), ModUtils.emptyStack);
            } else {
                this.list.set(slot - this.inventory.size(), stack);
            }
        }
        this.updatelist();
        this.save();
    }

    private void updatelist() {
        List<BagsDescription> list = new ArrayList<>();
        for (ItemStack stack : this.inventory) {
            if (stack == null) {
                continue;
            }
            if (stack.isEmpty()) {
                continue;
            }
            if (list.isEmpty()) {
                list.add(new BagsDescription(stack));
            } else if (list.contains(new BagsDescription(stack))) {
                for (BagsDescription bagsDescription : list) {
                    if (bagsDescription.equals(new BagsDescription(stack))) {
                        bagsDescription.addCount(stack.getCount());
                    }
                }
            } else {
                list.add(new BagsDescription(stack));
            }
        }
        this.itemStack1.set(DataComponentsInit.DESCRIPTIONS_CONTAINER, list);
    }


    private boolean addWithoutSave(List<ItemStack> stacks, boolean simulate) {

        if (stacks != null && !stacks.isEmpty()) {
            boolean white = itemStack1.getOrDefault(DataComponentsInit.BLACK_LIST, false);
            for (ItemStack stack : stacks) {

                boolean can = false;
                if (white) {
                    for (ItemStack stack1 : this.list) {
                        if (!stack1.isEmpty() && stack1.is(stack.getItem())) {
                            can = true;
                            break;
                        }
                    }
                } else {
                    for (ItemStack stack1 : this.list) {
                        if (!stack1.isEmpty() && stack1.is(stack.getItem())) {
                            can = false;
                            break;
                        } else {
                            can = true;
                        }
                    }
                }
                if (!can) {
                    return false;
                }
                for (int i = 0; i < this.inventory.size(); i++) {
                    if (this.get(i) == null || this.get(i).isEmpty()) {
                        if (!simulate) {
                            this.putWithoutSave(i, stack.copy());
                            stack.setCount(0);
                        }
                        return true;
                    } else {
                        if (this.get(i).is(stack.getItem())) {
                            if (this.get(i).getCount() + stack.getCount() <= stack.getMaxStackSize()) {
                                if (stack.getComponents().isEmpty() && this.get(i).getComponents().isEmpty()) {
                                    if (!simulate) {
                                        this.get(i).grow(stack.getCount());
                                        stack.setCount(0);
                                    }
                                    return true;
                                } else {
                                    if (!stack.getComponents().isEmpty() &&
                                            stack.getComponents().equals(this.get(i).getComponents())) {
                                        if (!simulate) {
                                            this.get(i).grow(stack.getCount());
                                            stack.setCount(0);

                                        }
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }


    public boolean addWithoutSave(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.addWithoutSave(Collections.singletonList(stack), false);
        }
    }


    public void putWithoutSave(int index, ItemStack content) {
        if (ModUtils.isEmpty(content)) {
            content = ModUtils.emptyStack;
        }

        this.inventory.set(index, content);
        this.save();
    }

    @Override
    public ItemStack removeItem(int index, int amount) {
        if (index < this.inventory.size()) {
            ItemStack stack;
            if (index >= 0 && index < this.inventory.size() && !ModUtils.isEmpty(stack = this.inventory.get(index))) {
                ItemStack ret;
                if (amount >= ModUtils.getSize(stack)) {
                    ret = stack;
                    this.inventory.set(index, ModUtils.emptyStack);
                } else {
                    ret = ModUtils.setSize(stack, amount);
                    this.inventory.set(index, ModUtils.decSize(stack, amount));
                }

                this.save();
                return ret;
            } else {
                return ModUtils.emptyStack;
            }
        } else {
            ItemStack stack;
            if (index - this.inventory.size() < list.size() && !ModUtils.isEmpty(stack =
                    this.list.get(index - this.inventory.size()))) {
                ItemStack ret;
                if (amount >= ModUtils.getSize(stack)) {
                    ret = stack;
                    this.list.set(index - this.inventory.size(), ModUtils.emptyStack);
                } else {
                    ret = ModUtils.setSize(stack, amount);
                    this.list.set(index - this.inventory.size(), ModUtils.decSize(stack, amount));
                }

                this.save();
                return ret;
            } else {
                return ModUtils.emptyStack;
            }
        }
    }

    @Override
    public ItemStackInventory getParent() {
        return this;
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public GuiCore<ContainerBase<?>> getGui(Player var1, ContainerBase<?> menu) {
        ContainerBags containerLeadBox = (ContainerBags) menu;
        return new GuiBags(containerLeadBox, this.itemStack1);
    }

    public ContainerBags getGuiContainer(Player player) {
        return new ContainerBags(player, this);
    }


    @Override
    public boolean canPlaceItem(int p_18952_, ItemStack itemstack) {
        if (itemstack.getItem() instanceof ItemEnergyBags) {
            return false;
        }
        boolean white = itemStack1.getOrDefault(DataComponentsInit.BLACK_LIST, false);
        boolean can = false;
        if (white) {
            for (ItemStack stack1 : this.list) {
                if (!stack1.isEmpty() && stack1.is(itemstack.getItem())) {
                    can = true;
                    break;
                }
            }
        } else {
            for (ItemStack stack1 : this.list) {
                if (!stack1.isEmpty() && stack1.is(itemstack.getItem())) {
                    can = false;
                    break;
                } else {
                    can = true;
                }
            }
        }
        if (!can) {
            return false;
        }

        if (ElectricItem.manager.canUse(itemStack1, 50 * coef)) {
            return !itemstack.isEmpty();
        } else {
            return false;
        }
    }


}
