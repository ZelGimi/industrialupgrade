package com.denfop.items.bags;

import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.container.ContainerBags;
import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiBags;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.HandHeldInventory;
import com.denfop.utils.ModUtils;
import ic2.api.item.ElectricItem;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandHeldBags extends HandHeldInventory {

    public final int inventorySize;
    public final ItemStack itemStack1;
    private final double coef;

    ItemEnergyBags bags;

    public HandHeldBags(EntityPlayer player, ItemStack stack, int inventorySize) {
        super(player, stack, inventorySize);
        this.inventorySize = inventorySize;
        this.itemStack1 = stack;
        this.coef = 1D - (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, stack).number * 0.25D : 0);
        bags = (ItemEnergyBags) stack.getItem();
        this.updatelist();
    }

    public ContainerBase<HandHeldBags> getGuiContainer(EntityPlayer player) {
        return new ContainerBags(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiBags(new ContainerBags(player, this), itemStack1);
    }

    @Nonnull
    public String getName() {
        return "toolbox";
    }

    public boolean hasCustomName() {

        return false;

    }

    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (!StackUtil.isEmpty(stack) && StackUtil.getSize(stack) > this.getInventoryStackLimit()) {
            stack = StackUtil.copyWithSize(stack, this.getInventoryStackLimit());
        }

        if (StackUtil.isEmpty(stack)) {
            this.inventory[slot] = StackUtil.emptyStack;
        } else {
            this.inventory[slot] = stack;
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
                list.get(list.indexOf(new BagsDescription(stack))).addCount(stack.getCount());
            } else {
                list.add(new BagsDescription(stack));
            }
        }
        final NBTTagCompound nbt = ModUtils.nbt(itemStack1);
        NBTTagCompound nbt1 = new NBTTagCompound();
        nbt1.setInteger("size", list.size());
        for (int i = 0; i < list.size(); i++) {
            nbt1.setTag(String.valueOf(i), list.get(i).write(new NBTTagCompound()));
        }
        nbt.setTag("bag", nbt1);
    }

    public ItemStack get(int index) {
        return this.inventory[index];
    }

    public ItemStack[] getAll() {
        return this.inventory;
    }

    public void put(ItemStack content) {
        this.put(0, content);
    }

    public void put(int index, ItemStack content) {
        if (StackUtil.isEmpty(content)) {
            content = StackUtil.emptyStack;
        }

        this.inventory[index] = content;
        this.save();
    }

    private boolean add(List<ItemStack> stacks, boolean simulate) {
        if (stacks != null && !stacks.isEmpty()) {

            for (ItemStack stack : stacks) {
                for (int i = 0; i < this.inventory.length; i++) {
                    if (this.get(i) == null || this.get(i).isEmpty()) {
                        if (!simulate) {
                            this.put(i, stack.copy());
                            stack.setCount(0);
                        }
                        return true;
                    } else {
                        if (this.get(i).isItemEqual(stack)) {
                            if (this.get(i).getCount() + stack.getCount() <= stack.getMaxStackSize()) {
                                if (stack.getTagCompound() == null && this.get(i).getTagCompound() == null) {
                                    if (!simulate) {
                                        this.get(i).grow(stack.getCount());
                                        stack.setCount(0);
                                    }
                                    return true;
                                } else {
                                    if (stack.getTagCompound() != null &&
                                            stack.getTagCompound().equals(this.get(i).getTagCompound())) {
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

    public boolean add(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.add(Collections.singletonList(stack), false);
        }
    }


    public boolean canAdd(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.add(Collections.singletonList(stack), true);
        }
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if (itemstack.getItem() instanceof ItemEnergyBags) {
            return false;
        }


        if (ElectricItem.manager.canUse(itemStack1, 50 * coef)) {
            return !itemstack.isEmpty();
        } else {
            return false;
        }
    }

}
