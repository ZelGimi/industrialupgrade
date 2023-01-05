package com.denfop.api.space.colonies;

import net.minecraft.nbt.NBTTagCompound;

public interface IColonyBuilding {

    String getName();

    NBTTagCompound writeTag(NBTTagCompound tag);

    IColony getColony();

    void work();

    void remove();

}
