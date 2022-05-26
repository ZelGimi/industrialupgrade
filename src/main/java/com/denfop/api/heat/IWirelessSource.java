package com.denfop.api.heat;

import com.denfop.api.ITemperature;
import net.minecraft.world.chunk.Chunk;

import java.util.List;

public interface IWirelessSource extends IHeatSource {

    List<IHeatTile> getList();

    void setList(List<IHeatTile> lst);

    Chunk getChunk();

    ITemperature getITemperature();

}
