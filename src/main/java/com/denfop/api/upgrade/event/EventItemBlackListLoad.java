package com.denfop.api.upgrade.event;

import com.denfop.api.upgrade.IUpgradeWithBlackList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;

public class EventItemBlackListLoad extends LevelEvent {

    public final IUpgradeWithBlackList item;
    public final ItemStack stack;
    public final CompoundTag nbt;

    public EventItemBlackListLoad(
            final Level world,
            IUpgradeWithBlackList item,
            ItemStack stack,
            final CompoundTag nbt2
    ) {
        super(world);
        this.item = item;
        this.stack = stack;
        this.nbt = nbt2;
    }

}
