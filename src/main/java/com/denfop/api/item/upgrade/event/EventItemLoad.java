package com.denfop.api.item.upgrade.event;

import com.denfop.api.item.upgrade.UpgradeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.LevelEvent;

public class EventItemLoad extends LevelEvent {

    public final UpgradeItem item;
    public final ItemStack stack;

    public EventItemLoad(final Level world, UpgradeItem item, ItemStack stack) {
        super(world);
        this.item = item;
        this.stack = stack;
    }

}
