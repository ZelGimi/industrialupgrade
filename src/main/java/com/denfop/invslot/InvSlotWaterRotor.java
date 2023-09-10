package com.denfop.invslot;

import com.denfop.api.windsystem.IWindRotor;
import com.denfop.items.ItemWaterRotor;
import com.denfop.tiles.mechanism.water.TileBaseWaterGenerator;
import com.denfop.utils.DamageHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;

public class InvSlotWaterRotor extends InvSlot {


    private final TileBaseWaterGenerator windGenerator;

    public InvSlotWaterRotor(TileBaseWaterGenerator windGenerator) {
        super(windGenerator, TypeItemSlot.INPUT, 1);
        this.setStackSizeLimit(1);
        this.windGenerator = windGenerator;

    }


    public int damage(int amount, double chance) {
        int damageApplied = 0;
        if (chance > 0) {
            if (this.windGenerator.getWorld().rand.nextInt(101) <= (int) (chance * 100)) {
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
    public boolean accepts(final ItemStack stack, final int index) {

        return stack.getItem() instanceof ItemWaterRotor && ((IWindRotor) stack.getItem()).getLevel() >= windGenerator
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
