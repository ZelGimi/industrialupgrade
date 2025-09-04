package com.denfop.api.otherenergies.transport.event;

import com.denfop.api.otherenergies.transport.ITransportTile;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.LevelEvent;

public class TransportTileReloadEvent extends LevelEvent {

    public final ITransportTile newtile;
    public final ITransportTile oldtile;

    public TransportTileReloadEvent(Level world, ITransportTile energyTile1, ITransportTile energyTile2) {
        super(world);
        this.newtile = energyTile1;
        this.oldtile = energyTile2;
    }

}

