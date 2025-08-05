package com.denfop.api.tesseract;

import com.denfop.api.tesseract.event.EventAdderChannel;
import com.denfop.api.tesseract.event.EventLoadTesseract;
import com.denfop.api.tesseract.event.EventRemoverChannel;
import com.denfop.api.tesseract.event.EventUnLoadTesseract;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

public class EventHandler {

    public EventHandler() {
        NeoForge.EVENT_BUS.register(this);
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
    public void tick(LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide) {
            return;
        }

        TesseractSystem.instance.onTick(event.getLevel());
    }

    @SubscribeEvent
    public void onWorldUnload(LevelEvent.Unload event) {
        if ((event.getLevel()).isClientSide()) {
            return;
        }
        TesseractSystem.instance.onWorldUnload((Level) event.getLevel());
    }

}
