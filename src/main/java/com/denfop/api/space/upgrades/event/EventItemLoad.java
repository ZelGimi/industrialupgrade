package com.denfop.api.space.upgrades.event;

import com.denfop.api.space.rovers.api.IRoversItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;

public class EventItemLoad extends LevelEvent {

    public final IRoversItem item;
    public final ItemStack stack;

    public EventItemLoad(final Level world, IRoversItem item, ItemStack stack) {
        super(world);
        this.item = item;
        this.stack = stack;
    }

}
