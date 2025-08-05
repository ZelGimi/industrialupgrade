package com.denfop.api.space.colonies.api;

import com.denfop.api.space.colonies.enums.EnumTypeBuilding;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public interface IColonyBuilding {

    CustomPacketBuffer writePacket(CustomPacketBuffer customPacketBuffer);

    CompoundTag writeTag(CompoundTag tag, HolderLookup.Provider p_323640_);

    IColony getColony();

    int getMinLevelColony();

    boolean isIgnore();

    void work();

    EnumTypeBuilding getTypeBuilding();

    byte getId();

    int getPeople();

}
