package com.denfop.api.windsystem;

import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.api.widget.ITypeSlot;
import com.denfop.blockentity.windturbine.BlockEntityWindTurbineController;
import com.denfop.inventory.Inventory;
import com.denfop.items.ItemWindRotor;
import com.denfop.utils.DamageHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.world.item.ItemStack;

public class InventoryWindTurbineRotor extends Inventory implements ITypeSlot {


    private final BlockEntityWindTurbineController windGenerator;

    public InventoryWindTurbineRotor(BlockEntityWindTurbineController windGenerator) {
        super(windGenerator, TypeItemSlot.INPUT, 1);
        this.setStackSizeLimit(1);
        this.windGenerator = windGenerator;

    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.ROTOR;
    }

    public int damage(int amount, double chance) {
        int damageApplied = 0;
        if (chance > 0) {
            if (!(this.windGenerator.getWorld().random.nextInt(101) <= (int) (chance * 100))) {
                return 0;
            }
        }

        ItemStack stack = this.get(0);
        ItemWindRotor rotor = (ItemWindRotor) stack.getItem();
        if (!ModUtils.isEmpty(stack)) {
            DamageHandler.damage(stack, amount, null);
            if (DamageHandler.getDamage(stack) >= DamageHandler.getMaxDamage(stack) * 0.25) {
                this.windGenerator.need_repair = true;
            }
        }


        return damageApplied;
    }

    @Override
    public boolean canPlaceItem(final int index, final ItemStack stack) {

        return stack.getItem() instanceof ItemWindRotor && ((WindRotor) stack.getItem()).getLevel() >= 1 && ((WindRotor) stack.getItem()).getLevel() <= 14;
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        this.windGenerator.change();
        if (!content.isEmpty()) {
            this.windGenerator.energy.getEnergy().setSourceTier(this.windGenerator.getRotor().getSourceTier());
        } else {
            this.windGenerator.energy.getEnergy().setSourceTier(1);
        }
        return content;
    }

}
