package com.denfop.api.item.upgrade.event;

import com.denfop.api.item.upgrade.UpgradeWithBlackList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.LevelEvent;

public class EventItemBlackListLoad extends LevelEvent {

    public final UpgradeWithBlackList item;
    public final ItemStack stack;
    public final CompoundTag nbt;

    public EventItemBlackListLoad(
            final Level world,
            UpgradeWithBlackList item,
            ItemStack stack,
            final CompoundTag nbt2
    ) {
        super(world);
        this.item = item;
        this.stack = stack;
        this.nbt = nbt2;
    }

}
