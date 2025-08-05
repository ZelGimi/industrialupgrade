package com.denfop.api.tesseract.event;

import com.denfop.api.tesseract.ITesseract;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.LevelEvent;

public class EventLoadTesseract extends LevelEvent {

    private final ITesseract tesseract;

    public EventLoadTesseract(ITesseract tesseract, final Level world) {
        super(world);
        this.tesseract = tesseract;
    }

    public ITesseract getTesseract() {
        return tesseract;
    }

}
