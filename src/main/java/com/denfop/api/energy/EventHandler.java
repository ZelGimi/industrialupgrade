package com.denfop.api.energy;


import com.denfop.api.energy.event.EnergyTileLoadEvent;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.energy.event.EventLoadController;
import com.denfop.api.energy.event.EventUnloadController;
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
    public void onEnergyTileLoad(EventLoadController event) {
        if (((Level) event.getLevel()).isClientSide) {
            return;
        }
        EnergyNetLocal local = EnergyNetGlobal.getForWorld((Level) event.getLevel());
        if (local != null) {
            local.addController((IEnergyController) event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnLoad(EventUnloadController event) {
        if ((event.getLevel()).isClientSide()) {
            return;
        }
        EnergyNetLocal local = EnergyNetGlobal.getForWorld((Level) event.getLevel());
        if (local != null) {
            local.removeController((IEnergyController) event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(EnergyTileLoadEvent event) {
        if (((Level) event.getLevel()).isClientSide) {
            return;
        }

        EnergyNetLocal local = EnergyNetGlobal.getForWorld((Level) event.getLevel());
        if (local != null) {

            local.addTile(event.tile);
        }
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnLoad(EnergyTileUnLoadEvent event) {
        if (((Level) event.getLevel()).isClientSide) {
            return;
        }
        EnergyNetLocal local = EnergyNetGlobal.getForWorld((Level) event.getLevel());
        if (local != null) {
            local.removeTile(event.tile);
        }
    }


    @SubscribeEvent
    public void tick(LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide) {
            return;
        }


        EnergyNetGlobal.onTickEnd(event.getLevel());

    }

    @SubscribeEvent
    public void onWorldUnload(LevelEvent.Unload event) {
        if ((event.getLevel()).isClientSide()) {
            return;
        }
        EnergyNetGlobal.onWorldUnload((Level) event.getLevel());
    }

}
