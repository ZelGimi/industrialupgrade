package com.denfop.api.space.upgrades.event;

import com.denfop.api.space.rovers.IRovers;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class EventItemLoad extends WorldEvent {

    public final IRovers item;
    public final ItemStack stack;

    public EventItemLoad(final World world, IRovers item, ItemStack stack) {
        super(world);
        this.item = item;
        this.stack = stack;
    }

}
