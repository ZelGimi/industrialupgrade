package com.denfop.api.research.event;

import com.denfop.api.research.main.IResearchItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class EventLoadResearchItem extends WorldEvent {

    public final IResearchItem item;
    public final ItemStack stack;

    public EventLoadResearchItem(final World world, IResearchItem item, ItemStack stack) {
        super(world);
        this.item = item;
        this.stack = stack;
    }

}
