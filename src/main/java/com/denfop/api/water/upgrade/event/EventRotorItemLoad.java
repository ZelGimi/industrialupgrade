package com.denfop.api.water.upgrade.event;

import com.denfop.api.water.upgrade.IRotorUpgradeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.LevelEvent;

public class EventRotorItemLoad extends LevelEvent {

    public final IRotorUpgradeItem item;
    public final ItemStack stack;

    public EventRotorItemLoad(final Level world, IRotorUpgradeItem item, ItemStack stack) {
        super(world);
        this.item = item;
        this.stack = stack;
    }

}
