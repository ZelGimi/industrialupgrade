package com.denfop.cool;


import com.denfop.api.cool.event.CoolTileLoadEvent;
import com.denfop.api.cool.event.CoolTileUnloadEvent;
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
    public void onEnergyTileLoad(final CoolTileLoadEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        final CoolNetLocal local = CoolNetGlobal.getForWorld((Level) event.getLevel());

        if (local != null) {
            local.addTile(event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnload(final CoolTileUnloadEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        final CoolNetLocal local = CoolNetGlobal.getForWorld((Level) event.getLevel());
        if (local != null) {
            local.removeTile(event.tile);
        }
    }

    @SubscribeEvent
    public void tick(final LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide) {
            return;
        }

        CoolNetGlobal.onTickEnd(event.getLevel());

    }

    @SubscribeEvent
    public void onWorldUnload(final LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        CoolNetGlobal.onWorldUnload((Level) event.getLevel());
    }

}
