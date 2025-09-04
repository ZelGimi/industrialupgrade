package com.denfop.api.otherenergies.transport;

import com.denfop.api.otherenergies.transport.event.TransportTileLoadEvent;
import com.denfop.api.otherenergies.transport.event.TransportTileUnLoadEvent;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

public class EventHandler {

    public EventHandler() {
        NeoForge.EVENT_BUS.register(this);
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(final TransportTileLoadEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        final TransportNetLocal local = TransportNetGlobal.getForWorld((Level) event.getLevel());
        if (local != null) {
            local.addTile(event.tile);
        }
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnLoad(final TransportTileUnLoadEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        final TransportNetLocal local = TransportNetGlobal.getForWorld((Level) event.getLevel());

        if (local != null) {
            local.removeTile(event.tile);
        }
    }


    @SubscribeEvent
    public void tick(final LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide) {
            return;
        }
        TransportNetGlobal.onTickEnd(event.getLevel());
    }

    @SubscribeEvent
    public void onWorldUnload(final LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        TransportNetGlobal.onWorldUnload((Level) event.getLevel());
    }

}
