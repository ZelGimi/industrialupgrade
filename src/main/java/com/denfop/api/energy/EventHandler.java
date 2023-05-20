package com.denfop.api.energy;


import com.denfop.api.energy.event.EnergyMultiTileLoadEvent;
import com.denfop.api.energy.event.EnergyMultiTileUnLoadEvent;
import com.denfop.api.energy.event.EnergyTileLoadEvent;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.energy.event.EventLoadController;
import com.denfop.api.energy.event.EventUnloadController;
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
    public void onEnergyTileLoad(EventLoadController event) {
        if ((event.getWorld()).isRemote) {
            return;
        }
        EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());
        if (local != null) {
            local.addController((IEnergyController) event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnLoad(EventUnloadController event) {
        if ((event.getWorld()).isRemote) {
            return;
        }
        EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());
        if (local != null) {
            local.removeController((IEnergyController) event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(EnergyTileLoadEvent event) {
        if ((event.getWorld()).isRemote) {
            return;
        }

        EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());
        if (local != null) {

            local.addTile(event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(EnergyMultiTileLoadEvent event) {
        if ((event.getWorld()).isRemote) {
            return;
        }

        EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());
        if (local != null) {

            local.addTileEntity(event.tile, event.subject);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnLoad(EnergyMultiTileUnLoadEvent event) {
        if ((event.getWorld()).isRemote) {
            return;
        }
        EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());
        if (local != null) {
            local.removeTileEntity(event.subject, event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnLoad(EnergyTileUnLoadEvent event) {
        if ((event.getWorld()).isRemote) {
            return;
        }
        EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());
        if (local != null) {
            local.removeTile(event.tile);
        }
    }


    @SubscribeEvent
    public void tick(TickEvent.WorldTickEvent event) {
        if (event.world.isRemote) {
            return;
        }

        if (event.phase == TickEvent.Phase.END) {
            EnergyNetGlobal.onTickEnd(event.world);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if ((event.getWorld()).isRemote) {
            return;
        }
        EnergyNetGlobal.onWorldUnload(event.getWorld());
    }

}
