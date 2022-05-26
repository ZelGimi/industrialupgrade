package com.denfop.qe;



import com.denfop.api.qe.*;
import com.denfop.api.qe.event.*;
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
    public void onEnergyTileLoad(final QETileLoadEvent event) {
        final IQETile tile = event.tile;
        if (tile instanceof IQESource) {
            EnergyTransferList.initIEnergySource((IQESource) event.tile);
        }
        final QENetLocal local = QENetGlobal.getForWorld(event.getWorld());

        if (local != null) {
            local.addTile(event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnload(final QETileUnloadEvent event) {
        final QENetLocal local = QENetGlobal.getForWorld(event.getWorld());
        if (local != null) {
            local.removeTile(event.tile);
        }
    }

    @SubscribeEvent
    public void tick(final TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            QENetGlobal.onTickStart(event.world);
        } else if (event.phase == TickEvent.Phase.END) {
            QENetGlobal.onTickEnd(event.world);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload event) {
        QENetGlobal.onWorldUnload(event.getWorld());
    }

}
