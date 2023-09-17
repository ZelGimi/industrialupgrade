package com.denfop.api.vein;

import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;

public interface IVein {

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

    NBTTagCompound writeTag();

    CustomPacketBuffer writePacket();

    boolean equals(Object o);

    boolean get();

    void setFind(boolean find);

}
