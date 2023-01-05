package com.denfop.api.transport.event;

import com.denfop.api.transport.ITransportTile;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class TransportTileReloadEvent extends WorldEvent {

    public final ITransportTile newtile;
    public final ITransportTile oldtile;

    public TransportTileReloadEvent(World world, ITransportTile energyTile1, ITransportTile energyTile2) {
        super(world);
        this.newtile = energyTile1;
        this.oldtile = energyTile2;
    }

}

