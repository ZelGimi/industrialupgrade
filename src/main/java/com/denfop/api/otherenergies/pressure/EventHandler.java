package com.denfop.api.otherenergies.pressure;


import com.denfop.api.otherenergies.pressure.event.PressureTileLoadEvent;
import com.denfop.api.otherenergies.pressure.event.PressureTileUnloadEvent;
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
    public void onEnergyTileLoad(final PressureTileLoadEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        final PressureNetLocal local = PressureNetGlobal.getForWorld((Level) event.getLevel());

        if (local != null) {
            local.addTileEntity(event.tile.getPos(), event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnload(final PressureTileUnloadEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        final PressureNetLocal local = PressureNetGlobal.getForWorld((Level) event.getLevel());
        if (local != null) {
            local.removeTile(event.tile);
        }
    }

    @SubscribeEvent
    public void tick(final LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide) {
            return;
        }

        PressureNetGlobal.onTickEnd(event.getLevel());

    }

    @SubscribeEvent
    public void onWorldUnload(final LevelEvent.Unload event) {

        if (event.getLevel().isClientSide()) {
            return;
        }
        PressureNetGlobal.onWorldUnload((Level) event.getLevel());
    }

}
