package com.denfop.api.space.fakebody;

import net.minecraft.nbt.CompoundTag;

public interface IFakePlayer {

    CompoundTag getTag();

    String getName();

    boolean matched(String name);

    CompoundTag writeNBT();

    CompoundTag writeNBT(CompoundTag nbtTagCompound);
}
