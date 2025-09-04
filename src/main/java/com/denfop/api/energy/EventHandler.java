package com.denfop.api.energy;


import com.denfop.api.energy.event.load.EnergyTileLoadEvent;
import com.denfop.api.energy.event.load.EventLoadController;
import com.denfop.api.energy.event.unload.EnergyTileUnLoadEvent;
import com.denfop.api.energy.event.unload.EventUnloadController;
import com.denfop.api.energy.interfaces.EnergyController;
import com.denfop.api.energy.networking.EnergyNetGlobal;
import com.denfop.api.energy.networking.EnergyNetLocal;
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
    public void onEnergyTileLoad(EventLoadController event) {
        if (((Level) event.getLevel()).isClientSide) {
            return;
        }
        EnergyNetLocal local = EnergyNetGlobal.getForWorld((Level) event.getLevel());
        if (local != null) {
            local.addController((EnergyController) event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnLoad(EventUnloadController event) {
        if ((event.getLevel()).isClientSide()) {
            return;
        }
        EnergyNetLocal local = EnergyNetGlobal.getForWorld((Level) event.getLevel());
        if (local != null) {
            local.removeController((EnergyController) event.tile);
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
    public void tick(TickEvent.LevelTickEvent event) {
        if (event.level.isClientSide) {
            return;
        }

        if (event.phase == TickEvent.Phase.END) {
            EnergyNetGlobal.onTickEnd(event.level);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(LevelEvent.Unload event) {
        if ((event.getLevel()).isClientSide()) {
            return;
        }
        EnergyNetGlobal.onWorldUnload((Level) event.getLevel());
    }

}
