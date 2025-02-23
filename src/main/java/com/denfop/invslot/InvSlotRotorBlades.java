package com.denfop.invslot;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.items.ItemWindRod;
import com.denfop.items.ItemWindRotor;
import com.denfop.tiles.mechanism.wind.TileWindGenerator;
import net.minecraft.item.ItemStack;

public class InvSlotRotorBlades extends InvSlot implements ITypeSlot {

    private final TileWindGenerator windGenerator;

    public InvSlotRotorBlades(TileWindGenerator windGenerator) {
        super(windGenerator, TypeItemSlot.INPUT, 1);
        this.setStackSizeLimit(64);
        this.windGenerator = windGenerator;

    }

    @Override
    public EnumTypeSlot getTypeSlot(int slotid) {

        return EnumTypeSlot.ROD_PART;


    }

    @Override
    public boolean accepts(final ItemStack stack, final int index) {
        if (this.windGenerator.getRotor() == null) {
            return false;
        }
        if (this.windGenerator.getRotor() != null && stack.getItem() instanceof ItemWindRod) {
            return ((ItemWindRod) stack.getItem()).getLevel(this.windGenerator.getRotor().getLevel(), stack.getItemDamage());
        }
        return false;
    }

    public void consume(int amount) {
        consume(amount, false);
    }

    public void work() {
        if (this.get().isEmpty()) {
            return;
        }
        final ItemStack stack = this.windGenerator.slot.get();
        if (this.windGenerator.getRotor() != null && this.get().getItem() instanceof ItemWindRod) {
            if (((ItemWindRod) this.get().getItem()).getLevel(
                    this.windGenerator.getRotor().getLevel(),
                    this.get().getItemDamage()
            )) {
                if (((ItemWindRotor) stack.getItem()).getCustomDamage(stack) <= ((ItemWindRotor) stack.getItem()).getMaxCustomDamage(
                        stack) * 0.75) {
                    this.windGenerator.slot.damage(
                            (int) (-1 * ((ItemWindRotor) stack.getItem()).getMaxCustomDamage(stack) * 0.25),
                            0
                    );
                    this.get().shrink(1);
                }
            }
        }
    }

    public void consume(int amount, boolean simulate) {
        for (int i = 0; i < size(); i++) {
            ItemStack stack = get(i);
            if (!stack.isEmpty() && amount > 0) {
                int currentAmount = Math.min(amount, stack.getCount());
                if (!simulate) {
                    stack.shrink(currentAmount);
                }
                amount -= currentAmount;
                if (amount == 0) {
                    break;
                }
            }
        }
    }

}
