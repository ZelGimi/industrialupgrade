package com.denfop.api.research.event;

import com.denfop.api.research.main.IResearch;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class EventDownloadData extends WorldEvent {

    public final IResearch research;
    public final ItemStack stack;

    public EventDownloadData(final World world, IResearch research, ItemStack stack) {
        super(world);
        this.research = research;
        this.stack = stack;
    }

}
