package com.denfop.api.transport.event;

import com.denfop.api.transport.ITransportTile;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;

public class TransportTileUnLoadEvent<T, E> extends LevelEvent {

    public final ITransportTile<T, E> tile;

    public TransportTileUnLoadEvent(Level world, ITransportTile<T, E> energyTile1) {
        super(world);
        this.tile = energyTile1;
    }

}
