package com.denfop.render;

import net.minecraftforge.common.MinecraftForge;


public class RenderHandler {

    public RenderHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

}
