package com.denfop.api.tesseract.event;

import com.denfop.api.tesseract.ITesseract;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class EventLoadTesseract extends WorldEvent {

    private final ITesseract tesseract;

    public EventLoadTesseract(ITesseract tesseract, final World world) {
        super(world);
        this.tesseract = tesseract;
    }

    public ITesseract getTesseract() {
        return tesseract;
    }

}
