package com.denfop.api.upgrade.event;

import com.denfop.api.upgrade.IUpgradeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.LevelEvent;

public class EventItemLoad extends LevelEvent {

    public final IUpgradeItem item;
    public final ItemStack stack;

    public EventItemLoad(final Level world, IUpgradeItem item, ItemStack stack) {
        super(world);
        this.item = item;
        this.stack = stack;
    }

}
