package com.denfop.items;

import com.denfop.IUItem;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerUpgrade;
import com.denfop.datacomponent.ContainerAdditionalItem;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.gui.GUIUpgrade;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlot;
import com.denfop.utils.ModUtils;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemStackUpgradeModules extends ItemStackInventory {


    public final ItemStack itemStack1;
    public final List<ItemStack> list;
    public final int inventorySize;
    public ContainerAdditionalItem containerAdditionItem;
    public List<FluidStack> fluidStackList;

    public ItemStackUpgradeModules(Player player, ItemStack stack) {
        super(player, stack, 0);
        this.itemStack1 = stack;
        inventorySize = 0;
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
        this.fluidStackList = new ArrayList<>(Collections.nCopies(9, null));
        if (!(IUItem.ejectorUpgrade.is(itemStack1.getItem()) || IUItem.pullingUpgrade.is(itemStack1.getItem()))) {
            for (int i = 0; i < 9; i++) {
                if (!this.list.get(i).isEmpty()) {
                    IFluidHandlerItem handler = this.list.get(i).getCapability(Capabilities.FluidHandler.ITEM, null);
                    final FluidStack containerFluid = handler.drain(2147483647, IFluidHandler.FluidAction.SIMULATE);
                    if (!containerFluid.isEmpty() && containerFluid.getAmount() > 0) {
                        fluidStackList.set(i, new FluidStack(containerFluid.getFluid(), 1));
                    }
                }
            }
        }
    }

    public ContainerBase<ItemStackUpgradeModules> getGuiContainer(Player player) {
        return new ContainerUpgrade(this, player);
    }

    public ItemStack getItem(int slot) {
        return slot >= inventorySize ? this.list.get(slot - inventorySize) : this.inventory.get(slot);
    }

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

    public ContainerAdditionalItem getContainerAdditionItem() {
        return containerAdditionItem;
    }

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
        this.save();
    }

    protected void save() {
        super.save();
        if (!player.level().isClientSide) {
            if (!this.cleared) {
                boolean dropItself = false;

                for (int i = 0; i < this.list.size(); ++i) {
                    if (this.isThisContainer(this.list.get(i))) {
                        this.list.set(i, ItemStack.EMPTY);
                        dropItself = true;
                    }
                }

                ListTag contentList = new ListTag();

                this.containerAdditionItem = this.containerAdditionItem.updateItems(containerStack, list);

                if (dropItself) {
                    ModUtils.dropAsEntity(this.player.level(), this.player.blockPosition(), this.containerStack);
                    this.clear();
                } else {
                    int idx = this.getPlayerInventoryIndex();
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


    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<?>> getGui(Player player, ContainerBase<?> isAdmin) {
        if (this.itemStack1.is(IUItem.ejectorUpgrade.getItem()) || this.itemStack1.is(IUItem.pullingUpgrade.getItem())) {
            return new GUIUpgrade((ContainerUpgrade) isAdmin, itemStack1);
        } else {
            return new GUIUpgrade((ContainerUpgrade) isAdmin, itemStack1);
        }
    }

    @Override
    public ItemStackInventory getParent() {
        return this;
    }

    @Override
    public void addInventorySlot(final InvSlot var1) {

    }

    @Override
    public int getBaseIndex(final InvSlot var1) {
        return 0;
    }


    @Nonnull
    public String getName() {
        return "";
    }

    public boolean hasCustomName() {
        return false;
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

}
