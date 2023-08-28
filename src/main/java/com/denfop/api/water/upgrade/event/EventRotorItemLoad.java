package com.denfop.api.water.upgrade.event;

import com.denfop.api.water.upgrade.IRotorUpgradeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class EventRotorItemLoad extends WorldEvent {

    public final IRotorUpgradeItem item;
    public final ItemStack stack;

    public EventRotorItemLoad(final World world, IRotorUpgradeItem item, ItemStack stack) {
        super(world);
        this.item = item;
        this.stack = stack;
    }

}
