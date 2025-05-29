package com.denfop.api.windsystem;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.invslot.InvSlot;
import com.denfop.items.ItemWindRotor;
import com.denfop.tiles.mechanism.wind.TileWindGenerator;
import com.denfop.utils.DamageHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.world.item.ItemStack;

public class InvSlotWindRotor extends InvSlot implements ITypeSlot {


    private final TileWindGenerator windGenerator;

    public InvSlotWindRotor(TileWindGenerator windGenerator) {
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
    public boolean accepts(final ItemStack stack, final int index) {

        return stack.getItem() instanceof ItemWindRotor && ((IWindRotor) stack.getItem()).getLevel() >= windGenerator
                .getLevelGenerator()
                .getMin() && ((IWindRotor) stack.getItem()).getLevel() <= windGenerator.getLevelGenerator().getMax();
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
