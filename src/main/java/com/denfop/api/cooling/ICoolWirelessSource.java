package com.denfop.api.cooling;

import com.denfop.api.ITemperature;
import net.minecraft.world.chunk.Chunk;

import java.util.List;

public interface ICoolWirelessSource extends ICoolSource {

    List<ICoolSink> getList();

    void setList(List<ICoolSink> lst);

    Chunk getChunk();


}
