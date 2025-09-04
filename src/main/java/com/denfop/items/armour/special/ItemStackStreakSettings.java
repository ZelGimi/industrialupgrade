package com.denfop.items.armour.special;

import com.denfop.api.container.CustomWorldContainer;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.inventory.Inventory;
import com.denfop.items.ItemStackInventory;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.ModUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class ItemStackStreakSettings extends ItemStackInventory {

    public final int inventorySize;
    public final ItemStack itemStack1;

    public ItemStackStreakSettings(Player player, ItemStack stack) {
        super(player, stack, 0);
        this.inventorySize = 0;
        this.itemStack1 = stack;

    }

    public ContainerMenuBase<ItemStackStreakSettings> getGuiContainer(Player player) {
        return new ContainerMenuStreak(player, this);
    }


    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player player, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new WidgetColorPicker((ContainerMenuStreak) isAdmin, itemStack1);
    }


    @Override
    public void addInventorySlot(final Inventory var1) {

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

        this.save();
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


        return false;
    }

}
