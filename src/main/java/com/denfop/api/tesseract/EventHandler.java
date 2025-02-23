package com.denfop.api.tesseract;

import com.denfop.api.tesseract.event.EventAdderChannel;
import com.denfop.api.tesseract.event.EventLoadTesseract;
import com.denfop.api.tesseract.event.EventRemoverChannel;
import com.denfop.api.tesseract.event.EventUnLoadTesseract;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventHandler {

    public EventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void addTesseract(EventLoadTesseract eventLoadTesseract) {
        TesseractSystem.instance.addTesseract(eventLoadTesseract.getTesseract());
    }

    @SubscribeEvent
    public void removeTesseract(EventUnLoadTesseract eventUnLoadTesseract) {
        TesseractSystem.instance.removeTesseract(eventUnLoadTesseract.getTesseract());
    }

    @SubscribeEvent
    public void addChannel(EventAdderChannel eventAdderChannel) {
        TesseractSystem.instance.addChannel(eventAdderChannel.getChannel());
    }

    @SubscribeEvent
    public void removeChannel(EventRemoverChannel eventRemoverChannel) {
        TesseractSystem.instance.removeChannel(eventRemoverChannel.getChannel());
    }

    @SubscribeEvent
    public void tick(TickEvent.WorldTickEvent event) {
        if (event.world.isRemote) {
            return;
        }

        if (event.phase == TickEvent.Phase.END) {
            TesseractSystem.instance.onTick(event.world);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if ((event.getWorld()).isRemote) {
            return;
        }
        TesseractSystem.instance.onWorldUnload(event.getWorld());
    }

}
