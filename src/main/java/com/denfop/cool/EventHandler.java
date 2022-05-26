package com.denfop.cool;



import com.denfop.api.cooling.*;
import com.denfop.api.cooling.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventHandler {

    public EventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(final CoolTileLoadEvent event) {
        final ICoolTile tile = event.tile;
        if (tile instanceof ICoolSource) {
            EnergyTransferList.initIEnergySource((ICoolSource) event.tile);
        }
        final CoolNetLocal local = CoolNetGlobal.getForWorld(event.getWorld());

        if (local != null) {
            local.addTile(event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnload(final CoolTileUnloadEvent event) {
        final CoolNetLocal local = CoolNetGlobal.getForWorld(event.getWorld());
        if (local != null) {
            local.removeTile(event.tile);
        }
    }

    @SubscribeEvent
    public void tick(final TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            CoolNetGlobal.onTickStart(event.world);
        } else if (event.phase == TickEvent.Phase.END) {
            CoolNetGlobal.onTickEnd(event.world);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload event) {
        CoolNetGlobal.onWorldUnload(event.getWorld());
    }

}
