package com.powerutils;

import com.denfop.api.IModelRegister;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IModelRender extends IModelRegister {

    @SideOnly(Side.CLIENT)
    void registerModels();

}
