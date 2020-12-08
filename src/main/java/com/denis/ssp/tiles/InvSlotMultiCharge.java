package com.Denfop.ssp.tiles;

import ic2.api.energy.tile.IChargingSlot;
import ic2.api.item.ElectricItem;
import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

public class InvSlotMultiCharge extends InvSlot implements IChargingSlot
{
    public final int tier;

    public InvSlotMultiCharge(final TileEntityInventory base, final int tier, final int slotNumbers, final InvSlot.Access access) {
        super((IInventorySlotHolder<?>) base, "charge", access, slotNumbers, InvSlot.InvSide.TOP);
        this.tier = tier;
    }

    public boolean accepts(final ItemStack stack) {
        return ElectricItem.manager.charge(stack, Double.POSITIVE_INFINITY, this.tier, false, true) > 0.0;
    }

    public double charge(double amount) {
        if (amount <= 0.0) {
            throw new IllegalArgumentException("Amount must be > 0.");
        }
        double charged = 0.0;
        for (final ItemStack stack : this) {
            if (stack == null) {
                continue;
            }
            final double energyIn = ElectricItem.manager.charge(stack, amount, this.tier, false, false);
            amount -= energyIn;
            charged += energyIn;
            if (amount <= 0.0) {
                break;
            }
        }
        return charged;
    }
}
