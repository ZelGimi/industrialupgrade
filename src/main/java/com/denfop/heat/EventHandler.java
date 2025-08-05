package com.denfop.heat;


import com.denfop.api.heat.event.HeatTileLoadEvent;
import com.denfop.api.heat.event.HeatTileUnloadEvent;
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
    public void onEnergyTileLoad(final HeatTileLoadEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        final HeatNetLocal local = HeatNetGlobal.getForWorld((Level) event.getLevel());

        if (local != null) {
            local.addTile(event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnload(final HeatTileUnloadEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        final HeatNetLocal local = HeatNetGlobal.getForWorld((Level) event.getLevel());
        if (local != null) {
            local.removeTile(event.tile);
        }
    }

    @SubscribeEvent
    public void tick(final LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide) {
            return;
        }

        HeatNetGlobal.onTickEnd(event.getLevel());

    }

    @SubscribeEvent
    public void onWorldUnload(final LevelEvent.Unload event) {

        if (event.getLevel().isClientSide()) {
            return;
        }
        HeatNetGlobal.onWorldUnload((Level) event.getLevel());
    }

}
