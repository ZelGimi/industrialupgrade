package com.denfop.invslot;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.api.windsystem.IWindRotor;
import com.denfop.items.ItemWaterRotor;
import com.denfop.tiles.hydroturbine.TileEntityHydroTurbineController;
import com.denfop.utils.DamageHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;

public class InventoryHydroTurbineRotor extends Inventory implements ITypeSlot {


    private final TileEntityHydroTurbineController windGenerator;

    public InventoryHydroTurbineRotor(TileEntityHydroTurbineController windGenerator) {
        super(windGenerator, TypeItemSlot.INPUT, 1);
        this.setInventoryStackLimit(1);
        this.windGenerator = windGenerator;

    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.WATER_ROTOR;
    }

    public int damage(int amount, double chance) {
        int damageApplied = 0;
        if (chance > 0) {
            if (!(this.windGenerator.getWorld().rand.nextInt(101) <= (int) (chance * 100))) {
                return 0;
            }
        }

        ItemStack stack = this.get(0);
        if (!ModUtils.isEmpty(stack)) {
            DamageHandler.damage(stack, amount, null);
            if (DamageHandler.getDamage(stack) <= DamageHandler.getMaxDamage(stack) * 0.75) {
                this.windGenerator.need_repair = true;
            }
        }


        return damageApplied;
    }

    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {

        return stack.getItem() instanceof ItemWaterRotor && ((IWindRotor) stack.getItem()).getLevel() >= 1 && ((IWindRotor) stack.getItem()).getLevel() <= 14;
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        this.windGenerator.change();
        if (!content.isEmpty()) {
            this.windGenerator.energy.getEnergy().setSourceTier(this.windGenerator.getRotor().getSourceTier());
        } else {
            this.windGenerator.energy.getEnergy().setSourceTier(1);
        }

    }

}
