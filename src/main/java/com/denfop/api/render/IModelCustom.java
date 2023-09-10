package com.denfop.api.render;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IModelCustom {

    String getType();

    @SideOnly(Side.CLIENT)
    void renderAll();

}
