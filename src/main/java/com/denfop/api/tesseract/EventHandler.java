package com.denfop.api.tesseract;

import com.denfop.api.tesseract.event.EventAdderChannel;
import com.denfop.api.tesseract.event.EventLoadTesseract;
import com.denfop.api.tesseract.event.EventRemoverChannel;
import com.denfop.api.tesseract.event.EventUnLoadTesseract;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
    public void tick(TickEvent.LevelTickEvent event) {
        if (event.level.isClientSide) {
            return;
        }

        if (event.phase == TickEvent.Phase.END) {
            TesseractSystem.instance.onTick(event.level);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(LevelEvent.Unload event) {
        if ((event.getLevel()).isClientSide()) {
            return;
        }
        TesseractSystem.instance.onWorldUnload((Level) event.getLevel());
    }

}
