package com.denfop.api.vein.common;

import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;

public interface Vein {

    int getMeta();

    void setMeta(int meta);

    Type getType();

    void setType(Type type);

    ChunkPos getChunk();

    int getCol();

    void setCol(int col);

    int getMaxCol();

    void setMaxCol(int maxcol);

    void removeCol(int col);

    boolean canMining();

    CompoundTag writeTag();

    CustomPacketBuffer writePacket(RegistryAccess registryAccess);

    boolean equals(Object o);

    boolean get();

    void setFind(boolean find);

    boolean isOldMineral();
}
