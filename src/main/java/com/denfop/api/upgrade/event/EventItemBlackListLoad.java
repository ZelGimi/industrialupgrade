package com.denfop.api.upgrade.event;

import com.denfop.api.upgrade.IUpgradeWithBlackList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class EventItemBlackListLoad extends WorldEvent {

    public final IUpgradeWithBlackList item;
    public final ItemStack stack;
    public final NBTTagCompound nbt;

    public EventItemBlackListLoad(
            final World world,
            IUpgradeWithBlackList item,
            ItemStack stack,
            final NBTTagCompound nbt2
    ) {
        super(world);
        this.item = item;
        this.stack = stack;
        this.nbt = nbt2;
    }

}
