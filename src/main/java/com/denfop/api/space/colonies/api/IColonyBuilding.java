package com.denfop.api.space.colonies.api;

import com.denfop.api.space.colonies.enums.EnumTypeBuilding;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.nbt.NBTTagCompound;

public interface IColonyBuilding {

    CustomPacketBuffer writePacket(CustomPacketBuffer customPacketBuffer);

    NBTTagCompound writeTag(NBTTagCompound tag);

    IColony getColony();

    int getMinLevelColony();

    boolean isIgnore();

    void work();

    EnumTypeBuilding getTypeBuilding();

    byte getId();

    int getPeople();

}
