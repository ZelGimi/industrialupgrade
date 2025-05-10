package com.denfop.items.armour.special;

import com.denfop.ElectricItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlot;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.ItemStackInventory;
import com.denfop.items.bags.BagsDescription;
import com.denfop.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemStackLegsBags extends ItemStackInventory {

    public final int inventorySize;
    public final ItemStack itemStack1;
    private final double coef;

    public ItemStackLegsBags(Player player, ItemStack stack) {
        super(player, stack, 45);
        this.inventorySize = 45;
        this.itemStack1 = stack;
        this.coef = 1D - (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, stack).number * 0.25D : 0);
        this.updatelist();
    }

    public ContainerBase<ItemStackLegsBags> getGuiContainer(Player player) {
        return new ContainerLegsBags(player, this);
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player player, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiLegsBags((ContainerLegsBags) isAdmin, itemStack1);
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
        return "toolbox";
    }

    public boolean hasCustomName() {

        return false;

    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (!ModUtils.isEmpty(stack) && ModUtils.getSize(stack) > this.getInventoryStackLimit()) {
            stack = ModUtils.setSize(stack, this.getInventoryStackLimit());
        }

        if (ModUtils.isEmpty(stack)) {
            this.inventory[slot] = ModUtils.emptyStack;
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
        final CompoundTag nbt = ModUtils.nbt(itemStack1);
        CompoundTag nbt1 = new CompoundTag();
        nbt1.putInt("size", list.size());
        for (int i = 0; i < list.size(); i++) {
            nbt1.put(String.valueOf(i), list.get(i).write(new CompoundTag()));
        }
        nbt.put("bag", nbt1);
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
        if (ModUtils.isEmpty(content)) {
            content = ModUtils.emptyStack;
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
                        if (this.get(i).is(stack.getItem())) {
                            if (this.get(i).getCount() + stack.getCount() <= stack.getMaxStackSize()) {
                                if (stack.getTag() == null && this.get(i).getTag() == null) {
                                    if (!simulate) {
                                        this.get(i).grow(stack.getCount());
                                        stack.setCount(0);
                                    }
                                    return true;
                                } else {
                                    if (stack.getTag() != null &&
                                            stack.getTag().equals(this.get(i).getTag())) {
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


        if (ElectricItem.manager.canUse(itemStack1, 50 * coef)) {
            return !itemstack.isEmpty();
        } else {
            return false;
        }
    }

}
