package com.denfop.inventory;

import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.api.widget.ITypeSlot;
import com.denfop.api.windsystem.WindRotor;
import com.denfop.blockentity.mechanism.water.BlockEntityBaseWaterGenerator;
import com.denfop.items.ItemWaterRotor;
import com.denfop.utils.DamageHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.world.item.ItemStack;

public class InventoryWaterRotor extends Inventory implements ITypeSlot {


    private final BlockEntityBaseWaterGenerator windGenerator;

    public InventoryWaterRotor(BlockEntityBaseWaterGenerator windGenerator) {
        super(windGenerator, TypeItemSlot.INPUT, 1);
        this.setStackSizeLimit(1);
        this.windGenerator = windGenerator;

    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.WATER_ROTOR;
    }

    public int damage(int amount, double chance) {
        int damageApplied = 0;
        if (chance > 0) {
            if (!(this.windGenerator.getWorld().random.nextInt(101) <= (int) (chance * 100))) {
                return 0;
            }
        }

        ItemStack stack = this.get(0);
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

        return stack.getItem() instanceof ItemWaterRotor && ((WindRotor) stack.getItem()).getLevel() >= windGenerator
                .getLevelGenerator()
                .getMin() && ((WindRotor) stack.getItem()).getLevel() <= windGenerator.getLevelGenerator().getMax();
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        this.windGenerator.change();
        if (!content.isEmpty()) {
            this.windGenerator.energy.setSourceTier(this.windGenerator.getRotor().getSourceTier());
        } else {
            this.windGenerator.energy.setSourceTier(1);
        }
        return content;
    }

}
