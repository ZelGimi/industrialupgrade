package com.denfop.api.space.colonies;

import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.colonies.api.IColonyBuilding;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.nbt.NBTTagCompound;

public abstract class Building implements IColonyBuilding {


    private final IColony colonie;

    public Building(IColony colonie) {
        this.colonie = colonie;
    }
    @Override
    public CustomPacketBuffer writePacket(final CustomPacketBuffer customPacketBuffer) {
        customPacketBuffer.writeByte(this.getId());
        return customPacketBuffer;
    }

    @Override
    public boolean isIgnore() {
        return false;
    }

    @Override
    public NBTTagCompound writeTag(final NBTTagCompound tag) {
        tag.setByte("type", this.getId());
        return tag;
    }

    @Override
    public IColony getColony() {
        return this.colonie;
    }


}
