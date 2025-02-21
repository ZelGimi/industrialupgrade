package com.denfop.pressure;


import com.denfop.api.pressure.event.PressureTileLoadEvent;
import com.denfop.api.pressure.event.PressureTileUnloadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventHandler {

    public EventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(final PressureTileLoadEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }
        final PressureNetLocal local = PressureNetGlobal.getForWorld(event.getWorld());

        if (local != null) {
            local.addTile(event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnload(final PressureTileUnloadEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }
        final PressureNetLocal local = PressureNetGlobal.getForWorld(event.getWorld());
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
            PressureNetGlobal.onTickEnd(event.world);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload event) {

        if (event.getWorld().isRemote) {
            return;
        }
        PressureNetGlobal.onWorldUnload(event.getWorld());
    }

}
