package com.denfop.api.space.fakebody;

import net.minecraft.nbt.NBTTagCompound;

public interface IFakePlayer {

    NBTTagCompound getTag();

    String getName();

    boolean matched(String name);

    NBTTagCompound writeNBT();

}
