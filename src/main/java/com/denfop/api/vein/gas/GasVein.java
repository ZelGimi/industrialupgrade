package com.denfop.api.vein.gas;

import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;

public interface GasVein {


    TypeGas getType();

    void setType(TypeGas type);

    ChunkPos getChunk();

    int getCol();

    void setCol(int col);

    int getMaxCol();

    void setMaxCol(int maxcol);

    void removeCol(int col);


    CompoundTag writeTag();

    CustomPacketBuffer writePacket();


    boolean isFind();

    void setFind(boolean find);

}
