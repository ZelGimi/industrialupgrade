package com.denfop.api.transport.event;

import com.denfop.api.transport.ITransportTile;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class TransportTileLoadEvent<T, E> extends WorldEvent {

    public final ITransportTile<T, E> tile;

    public TransportTileLoadEvent(World world, ITransportTile<T, E> energyTile1) {
        super(world);
        this.tile = energyTile1;
    }

}
