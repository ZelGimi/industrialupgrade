package com.denfop.api.space.upgrades.event;

import com.denfop.api.space.rovers.api.IRoversItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class EventItemLoad extends WorldEvent {

    public final IRoversItem item;
    public final ItemStack stack;

    public EventItemLoad(final World world, IRoversItem item, ItemStack stack) {
        super(world);
        this.item = item;
        this.stack = stack;
    }

}
