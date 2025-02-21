package com.denfop.api.gasvein;

import com.denfop.api.gasvein.TypeGas;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;

public interface IGasVein {



    TypeGas getType();

    void setType(TypeGas type);

    ChunkPos getChunk();

    int getCol();

    void setCol(int col);

    int getMaxCol();

    void setMaxCol(int maxcol);

    void removeCol(int col);


    NBTTagCompound writeTag();

    CustomPacketBuffer writePacket();


    boolean isFind();

    void setFind(boolean find);

}
