package com.denfop.api.upgrade.event;

import com.denfop.api.upgrade.IUpgradeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class EventItemLoad extends WorldEvent {

    public final IUpgradeItem item;
    public final ItemStack stack;

    public EventItemLoad(final World world, IUpgradeItem item, ItemStack stack) {
        super(world);
        this.item = item;
        this.stack = stack;
    }

}
