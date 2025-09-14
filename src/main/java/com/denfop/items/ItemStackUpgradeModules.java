package com.denfop.items;

import com.denfop.IUItem;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuUpgrade;
import com.denfop.inventory.Inventory;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenUpgrade;
import com.denfop.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemStackUpgradeModules extends ItemStackInventory {


    public final ItemStack itemStack1;
    public final ItemStack[] list;
    public final int inventorySize;
    public List<FluidStack> fluidStackList;

    public ItemStackUpgradeModules(Player player, ItemStack stack) {
        super(player, stack, 0);
        this.itemStack1 = stack;
        inventorySize = 0;
        this.list = new ItemStack[9];
        Arrays.fill(this.list, ItemStack.EMPTY);
        if (!player.getLevel().isClientSide) {
            CompoundTag nbt = ModUtils.nbt(containerStack);
            ListTag contentList = nbt.getList("Items1", 10);

            for (int i = 0; i < contentList.size(); ++i) {
                CompoundTag slotNbt = contentList.getCompound(i);
                int slot = slotNbt.getByte("Slot");
                if (slot >= 0 && slot < this.list.length) {
                    this.list[slot] = ItemStack.of(slotNbt);
                }
            }
        }
        this.fluidStackList = new ArrayList<>(Collections.nCopies(9, null));
        if (!(IUItem.ejectorUpgrade.is(itemStack1.getItem()) || IUItem.pullingUpgrade.is(itemStack1.getItem()))) {
            for (int i = 0; i < 9; i++) {
                if (!this.list[i].isEmpty()) {
                    IFluidHandlerItem handler = this.list[i].getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse((CapabilityFluidHandlerItem) this.list[i].getItem().initCapabilities(this.list[i], this.list[i].getTag()));
                    final FluidStack containerFluid = handler.drain(2147483647, IFluidHandler.FluidAction.SIMULATE);
                    if (!containerFluid.isEmpty() && containerFluid.getAmount() > 0) {
                        fluidStackList.set(i, new FluidStack(containerFluid.getFluid(), 1));
                    }
                }
            }
        }
    }

    public ContainerMenuBase<ItemStackUpgradeModules> getGuiContainer(Player player) {
        return new ContainerMenuUpgrade(this, player);
    }

    public ItemStack getItem(int slot) {
        return slot >= inventorySize ? this.list[slot - inventorySize] : this.inventory[slot];
    }

    public ItemStack removeItem(int index, int amount) {
        if (index < this.inventory.length) {
            ItemStack stack;
            if (index >= 0 && index < this.inventory.length && !ModUtils.isEmpty(stack = this.inventory[index])) {
                ItemStack ret;
                if (amount >= ModUtils.getSize(stack)) {
                    ret = stack;
                    this.inventory[index] = ModUtils.emptyStack;
                } else {
                    ret = ModUtils.setSize(stack, amount);
                    this.inventory[index] = ModUtils.decSize(stack, amount);
                }

                this.save();
                return ret;
            } else {
                return ModUtils.emptyStack;
            }
        } else {
            ItemStack stack;
            if (index - this.inventory.length < list.length && !ModUtils.isEmpty(stack =
                    this.list[index - this.inventory.length])) {
                ItemStack ret;
                if (amount >= ModUtils.getSize(stack)) {
                    ret = stack;
                    this.list[index - this.inventory.length] = ModUtils.emptyStack;
                } else {
                    ret = ModUtils.setSize(stack, amount);
                    this.list[index - this.inventory.length] = ModUtils.decSize(stack, amount);
                }

                this.save();
                return ret;
            } else {
                return ModUtils.emptyStack;
            }
        }
    }

    public void setItem(int slot, ItemStack stack) {
        if (slot < this.inventory.length) {
            if (!ModUtils.isEmpty(stack) && ModUtils.getSize(stack) > this.getInventoryStackLimit()) {
                stack = ModUtils.setSize(stack, this.getInventoryStackLimit());
            }

            if (ModUtils.isEmpty(stack)) {
                this.inventory[slot] = ModUtils.emptyStack;
            } else {
                this.inventory[slot] = stack;
            }
        } else {
            if (!ModUtils.isEmpty(stack) && ModUtils.getSize(stack) > this.getInventoryStackLimit()) {
                stack = ModUtils.setSize(stack, this.getInventoryStackLimit());
            }

            if (ModUtils.isEmpty(stack)) {
                this.list[slot - this.inventory.length] = ModUtils.emptyStack;
            } else {
                this.list[slot - this.inventory.length] = stack;
            }
        }
        this.save();
    }

    protected void save() {
        super.save();
        if (!player.getLevel().isClientSide) {
            if (!this.cleared) {
                boolean dropItself = false;

                for (int i = 0; i < this.list.length; ++i) {
                    if (this.isThisContainer(this.list[i])) {
                        this.list[i] = null;
                        dropItself = true;
                    }
                }

                ListTag contentList = new ListTag();

                int idx;
                for (idx = 0; idx < this.list.length; ++idx) {
                    if (!ModUtils.isEmpty(this.list[idx])) {
                        CompoundTag nbt = new CompoundTag();
                        nbt.putByte("Slot", (byte) idx);
                        this.list[idx].save(nbt);
                        contentList.add(nbt);
                    }
                }

                ModUtils.nbt(this.containerStack).put("Items1", contentList);

                this.containerStack = ModUtils.setSize(this.containerStack, 1);

                if (dropItself) {
                    ModUtils.dropAsEntity(this.player.getLevel(), this.player.blockPosition(), this.containerStack);
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

    public ItemStack[] getList() {
        return list;
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(Player player, ContainerMenuBase<?> isAdmin) {
        if (this.itemStack1.is(IUItem.ejectorUpgrade.getItem()) || this.itemStack1.is(IUItem.pullingUpgrade.getItem())) {
            return new ScreenUpgrade((ContainerMenuUpgrade) isAdmin, itemStack1);
        } else {
            return new ScreenUpgrade((ContainerMenuUpgrade) isAdmin, itemStack1);
        }
    }

    @Override
    public void addInventorySlot(final Inventory var1) {

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
