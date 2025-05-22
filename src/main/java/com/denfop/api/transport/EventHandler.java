package com.denfop.api.transport;

import com.denfop.api.energy.event.TileLoadEvent;
import com.denfop.api.energy.event.TileUnLoadEvent;
import com.denfop.api.energy.event.TilesUpdateEvent;
import com.denfop.api.transport.event.TransportTileLoadEvent;
import com.denfop.api.transport.event.TransportTileUnLoadEvent;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventHandler {

    public EventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }


    @SubscribeEvent(priority = EventPriority.LOW)
    public void load(TileLoadEvent event) {

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void unLoad(TileUnLoadEvent event) {
        TileEntity tile = event.tileentity;

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void update(TilesUpdateEvent event) {


    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(final TransportTileLoadEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }

        final TransportNetLocal local = TransportNetGlobal.getForWorld(event.getWorld());
        if (local != null) {
            local.addTile(event.tile);
        }
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnLoad(final TransportTileUnLoadEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }

        final TransportNetLocal local = TransportNetGlobal.getForWorld(event.getWorld());

        if (local != null) {
            local.removeTile(event.tile);
        }
    }


    @SubscribeEvent
    public void tick(final TickEvent.WorldTickEvent event) {
        if (event.world.isRemote) {
            return;
        }
        if (event.phase == TickEvent.Phase.END) {
            TransportNetGlobal.onTickEnd(event.world);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload event) {
        if (event.getWorld().isRemote) {
            return;
        }
        TransportNetGlobal.onWorldUnload(event.getWorld());
    }

}
