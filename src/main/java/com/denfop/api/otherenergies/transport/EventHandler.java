package com.denfop.api.otherenergies.transport;

import com.denfop.api.otherenergies.transport.event.TransportTileLoadEvent;
import com.denfop.api.otherenergies.transport.event.TransportTileUnLoadEvent;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    public EventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
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
    public void tick(final TickEvent.LevelTickEvent event) {
        if (event.level.isClientSide) {
            return;
        }
        if (event.phase == TickEvent.Phase.END) {
            TransportNetGlobal.onTickEnd(event.level);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(final LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        TransportNetGlobal.onWorldUnload((Level) event.getLevel());
    }

}
