package com.denfop.inventory;

import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.api.widget.ITypeSlot;
import com.denfop.blockentity.mechanism.wind.BlockEntityWindGenerator;
import com.denfop.items.ItemWindRod;
import com.denfop.items.ItemWindRotor;
import net.minecraft.world.item.ItemStack;

public class InventoryRotorBlades extends Inventory implements ITypeSlot {

    private final BlockEntityWindGenerator windGenerator;

    public InventoryRotorBlades(BlockEntityWindGenerator windGenerator) {
        super(windGenerator, TypeItemSlot.INPUT, 1);
        this.setStackSizeLimit(64);
        this.windGenerator = windGenerator;

    }

    @Override
    public EnumTypeSlot getTypeSlot(int slotid) {

        return EnumTypeSlot.ROD_PART;


    }

    @Override
    public boolean canPlaceItem(final int index, final ItemStack stack) {
        if (this.windGenerator.getRotor() == null) {
            return false;
        }
        if (this.windGenerator.getRotor() != null && stack.getItem() instanceof ItemWindRod) {
            return ((ItemWindRod) stack.getItem()).getLevel(this.windGenerator.getRotor().getLevel(), ((ItemWindRod<?>) stack.getItem()).getElement().getId());
        }
        return false;
    }

    public void consume(int amount) {
        consume(amount, false);
    }

    public void work() {
        if (this.get(0).isEmpty()) {
            return;
        }
        final ItemStack stack = this.windGenerator.slot.get(0);
        if (this.windGenerator.getRotor() != null && this.get(0).getItem() instanceof ItemWindRod) {
            if (((ItemWindRod) this.get(0).getItem()).getLevel(
                    this.windGenerator.getRotor().getLevel(),
                    ((ItemWindRod<?>) this.get(0).getItem()).getElement().getId()
            )) {
                if (((ItemWindRotor) stack.getItem()).getCustomDamage(stack) <= ((ItemWindRotor) stack.getItem()).getMaxCustomDamage(
                        stack) * 0.75) {
                    this.windGenerator.slot.damage(
                            (int) (-1 * ((ItemWindRotor) stack.getItem()).getMaxCustomDamage(stack) * 0.25),
                            0
                    );
                    this.get(0).shrink(1);
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
