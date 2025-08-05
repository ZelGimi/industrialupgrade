package com.denfop.api.render;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface IModelCustom {

    String getType();

    @OnlyIn(Dist.CLIENT)
    void renderAll();

}
