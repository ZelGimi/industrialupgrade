package com.denfop.items.creative;

import com.denfop.items.energy.ItemBattery;
import com.denfop.utils.ElectricItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemCreativeBattery extends ItemBattery {
    public ItemCreativeBattery(boolean wireless) {
        super(Math.pow(10, 24), Double.MAX_VALUE, 1, wireless);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity entity, int slot, boolean isSelected) {
        super.inventoryTick(itemStack, world, entity, slot, isSelected);
        ElectricItem.manager.charge(itemStack, Double.MAX_VALUE, Integer.MAX_VALUE, true, false);
    }
}
