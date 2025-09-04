package com.denfop.api.otherenergies.heat;


import com.denfop.api.otherenergies.heat.event.HeatTileLoadEvent;
import com.denfop.api.otherenergies.heat.event.HeatTileUnloadEvent;
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
    public void tick(final TickEvent.LevelTickEvent event) {
        if (event.level.isClientSide) {
            return;
        }
        if (event.phase == TickEvent.Phase.END) {
            HeatNetGlobal.onTickEnd(event.level);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(final LevelEvent.Unload event) {

        if (event.getLevel().isClientSide()) {
            return;
        }
        HeatNetGlobal.onWorldUnload((Level) event.getLevel());
    }

}
