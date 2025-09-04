package com.denfop.api.otherenergies.pressure;


import com.denfop.api.otherenergies.pressure.event.PressureTileLoadEvent;
import com.denfop.api.otherenergies.pressure.event.PressureTileUnloadEvent;
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
    public void tick(final TickEvent.LevelTickEvent event) {
        if (event.level.isClientSide) {
            return;
        }
        if (event.phase == TickEvent.Phase.END) {
            PressureNetGlobal.onTickEnd(event.level);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(final LevelEvent.Unload event) {

        if (event.getLevel().isClientSide()) {
            return;
        }
        PressureNetGlobal.onWorldUnload((Level) event.getLevel());
    }

}
