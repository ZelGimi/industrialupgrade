package com.powerutils;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IModelRender {

    @SideOnly(Side.CLIENT)
    void registerModels();

}
