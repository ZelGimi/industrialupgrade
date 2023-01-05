package com.denfop.api.windsystem;

import com.denfop.items.ItemAdvancedWindRotor;
import com.denfop.tiles.mechanism.wind.TileEntityWindGenerator;
import ic2.core.block.invslot.InvSlot;
import ic2.core.item.DamageHandler;
import ic2.core.util.StackUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InvSlotWindRotor extends InvSlot {


    private final TileEntityWindGenerator windGenerator;

    public InvSlotWindRotor(TileEntityWindGenerator windGenerator) {
        super(windGenerator, "rotor_slot", InvSlot.Access.I, 1, InvSide.ANY);
        this.setStackSizeLimit(1);
        this.windGenerator = windGenerator;

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
                if (item.isDamageable() && (target == null || item == target.getItem() && ItemStack.areItemStackTagsEqual(
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

    public int damage(int amount, double chance) {
        int damageApplied = 0;
        if (chance > 0) {
            if (this.windGenerator.getWorld().rand.nextInt(101) <= (int) (chance * 100)) {
                return 0;
            }
        }

        ItemStack stack = this.get(0);
        if (!StackUtil.isEmpty(stack)) {
            Item item = stack.getItem();
            if (item.isDamageable()) {
                int maxDamage = DamageHandler.getMaxDamage(stack);

                do {
                    int currentAmount = Math.min(amount, maxDamage - DamageHandler.getDamage(stack));
                    DamageHandler.damage(stack, currentAmount, null);
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
                if (stack.getItemDamage() >= stack.getMaxDamage() * 0.25) {
                    this.windGenerator.need_repair = true;
                }
                if (stack.getItemDamage() >= 1) {
                    this.windGenerator.can_repair = true;
                }

            }
        }


        return damageApplied;
    }

    @Override
    public boolean accepts(final ItemStack stack) {

        return stack.getItem() instanceof ItemAdvancedWindRotor && ((IWindRotor) stack.getItem()).getLevel() >= windGenerator
                .getLevel()
                .getMin() && ((IWindRotor) stack.getItem()).getLevel() <= windGenerator.getLevel().getMax();
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        this.windGenerator.change();
        if (!content.isEmpty()) {
            this.windGenerator.energy.setSourceTier(this.windGenerator.getRotor().getSourceTier());
        } else {
            this.windGenerator.energy.setSourceTier(1);
        }

    }

}
