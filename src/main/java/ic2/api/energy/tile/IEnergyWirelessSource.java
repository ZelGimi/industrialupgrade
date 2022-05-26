package ic2.api.energy.tile;

import net.minecraft.world.chunk.Chunk;

import java.util.List;

public interface IEnergyWirelessSource extends IEnergySource{

    List<IEnergyTile> getList();

    void setList(List<IEnergyTile> lst);

    Chunk getChunk();
}
