package com.denfop.api.otherenergies.transport.event;

import com.denfop.api.otherenergies.transport.ITransportTile;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;

public class TransportTileLoadEvent<T, E> extends LevelEvent {

    public final ITransportTile<T, E> tile;

    public TransportTileLoadEvent(Level world, ITransportTile<T, E> energyTile1) {
        super(world);
        this.tile = energyTile1;
    }

}
