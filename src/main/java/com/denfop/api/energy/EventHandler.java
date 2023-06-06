package com.denfop.api.energy;


import com.denfop.api.energy.event.*;
import ic2.api.energy.prefab.BasicSink;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.util.math.BlockPos;
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
    public void onEnergyIC2TileLoad(ic2.api.energy.event.EnergyTileLoadEvent event) {
        if ((event.getWorld()).isRemote) {
            return;
        }
        final IEnergyTile tile = event.tile;
        IAdvEnergyTile energyTile = null;
        if (tile instanceof BasicSink) {
            energyTile = new com.denfop.api.energy.BasicSinkIC2(((BasicSink) tile));
        }
        EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());
        if (local != EnergyNetLocal.EMPTY && energyTile != null) {
            local.addTile(energyTile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyIC2TileUnLoad(ic2.api.energy.event.EnergyTileUnloadEvent event) {
        if ((event.getWorld()).isRemote) {
            return;
        }
        final IEnergyTile tile = event.tile;
        IAdvEnergyTile energyTile = null;
        if (tile instanceof BasicSink) {
            final BlockPos pos = ((BasicSink) tile).getPosition();
            energyTile = EnergyNetGlobal.instance.getSubTile(event.getWorld(), pos);
        }
        if (energyTile != EnergyNetGlobal.EMPTY && energyTile != null) {
            EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());
            if (local != null) {
                local.removeTile(energyTile);
            }
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
