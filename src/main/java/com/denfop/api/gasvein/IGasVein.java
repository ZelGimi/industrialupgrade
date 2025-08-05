package com.denfop.api.gasvein;

import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;

public interface IGasVein {


    TypeGas getType();

    void setType(TypeGas type);

    ChunkPos getChunk();

    int getCol();

    void setCol(int col);

    int getMaxCol();

    void setMaxCol(int maxcol);

    void removeCol(int col);


    CompoundTag writeTag();

    CustomPacketBuffer writePacket(RegistryAccess registryAccess);


    boolean isFind();

    void setFind(boolean find);

}
