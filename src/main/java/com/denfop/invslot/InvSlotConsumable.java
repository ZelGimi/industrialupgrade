package com.denfop.invslot;

import com.denfop.tiles.base.IInventorySlotHolder;
import ic2.core.item.DamageHandler;
import ic2.core.util.StackUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class InvSlotConsumable extends InvSlot {

    public InvSlotConsumable(IInventorySlotHolder<?> base, String name, int count) {
        super(base, name, Access.I, count, InvSide.TOP);
    }

    public InvSlotConsumable(IInventorySlotHolder<?> base, String name, Access access, int count, InvSide preferredSide) {
        super(base, name, access, count, preferredSide);
    }

    public abstract boolean accepts(ItemStack var1, final int index);

    public boolean canOutput() {
        return super.canOutput() || this.access != Access.NONE && !this.isEmpty() && !this.accepts(this.get(), 0);
    }

    public ItemStack consume(int amount) {
        return this.consume(amount, false, false);
    }

    public ItemStack consume(int amount, boolean simulate, boolean consumeContainers) {
        ItemStack ret = null;

        for (int i = 0; i < this.size(); ++i) {
            ItemStack stack = this.get(i);
            if (StackUtil.getSize(stack) >= 1 && this.accepts(
                    stack,
                    i
            ) && (ret == null || StackUtil.checkItemEqualityStrict(
                    stack,
                    ret
            )) && (StackUtil.getSize(stack) == 1 || consumeContainers || !stack.getItem().hasContainerItem(stack))) {
                int currentAmount = Math.min(amount, StackUtil.getSize(stack));
                amount -= currentAmount;
                if (!simulate) {
                    if (StackUtil.getSize(stack) == currentAmount) {
                        if (!consumeContainers && stack.getItem().hasContainerItem(stack)) {
                            ItemStack container = stack.getItem().getContainerItem(stack);
                            if (container.isEmpty() && container.isItemStackDamageable() && DamageHandler.getDamage(container) > DamageHandler.getMaxDamage(
                                    container)) {
                                container = ItemStack.EMPTY;
                            }

                            this.put(i, container);
                        } else {
                            this.clear(i);
                        }
                    } else {
                        this.put(i, StackUtil.decSize(stack, currentAmount));
                    }
                }

                if (ret == null) {
                    ret = StackUtil.copyWithSize(stack, currentAmount);
                } else {
                    ret = StackUtil.incSize(ret, currentAmount);
                }

                if (amount == 0) {
                    break;
                }
            }
        }

        return ret;
    }

    public int damage(int amount, boolean simulate) {
        return this.damage(amount, simulate, null);
    }

    public int damage(int amount, boolean simulate, EntityLivingBase src) {
        int damageApplied = 0;
        ItemStack target = null;

        for (int i = 0; i < this.size() && amount > 0; ++i) {
            ItemStack stack = this.get(i);
            if (!StackUtil.isEmpty(stack)) {
                Item item = stack.getItem();
                if (this.accepts(
                        stack,
                        i
                ) && item.isDamageable() && (target == null || item == target.getItem() && ItemStack.areItemStackTagsEqual(
                        stack,
                        target
                ))) {
                    if (target == null) {
                        target = stack.copy();
                    }

                    if (simulate) {
                        stack = stack.copy();
                    }

                    int maxDamage = DamageHandler.getMaxDamage(stack);

                    do {
                        int currentAmount = Math.min(amount, maxDamage - DamageHandler.getDamage(stack));
                        DamageHandler.damage(stack, currentAmount, src);
                        damageApplied += currentAmount;
                        amount -= currentAmount;
                        if (DamageHandler.getDamage(stack) >= maxDamage) {
                            stack = StackUtil.decSize(stack);
                            if (StackUtil.isEmpty(stack)) {
                                break;
                            }

                            DamageHandler.setDamage(stack, 0, false);
                        }
                    } while (amount > 0 && !StackUtil.isEmpty(stack));

                    if (!simulate) {
                        this.put(i, stack);
                    }
                }
            }
        }

        return damageApplied;
    }

}
