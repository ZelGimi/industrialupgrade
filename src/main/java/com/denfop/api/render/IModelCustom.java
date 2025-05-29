package com.denfop.api.render;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IModelCustom {

    String getType();

    @OnlyIn(Dist.CLIENT)
    void renderAll();

}
