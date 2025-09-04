package com.denfop.api.otherenergies.transport;

import com.denfop.api.otherenergies.common.InfoTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.List;
import java.util.Map;

public interface ITransportTile<T, E> {

    E getHandler(Direction facing);

    BlockPos getPos();

    List<InfoTile<ITransportTile>> getValidReceivers();

    long getIdNetwork();

    void setId(long id);

    void AddTile(ITransportTile tile, final Direction dir);

    void RemoveTile(ITransportTile tile, final Direction dir);

    Map<Direction, ITransportTile> getTiles();


    int getHashCodeSource();

    void setHashCodeSource(int hashCode);

}
