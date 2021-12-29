//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.denfop.api.render;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IModelCustom {

    String getType();

    @SideOnly(Side.CLIENT)
    void renderAll();

    @SideOnly(Side.CLIENT)
    void renderOnly(String... var1);

    @SideOnly(Side.CLIENT)
    void renderPart(String var1);

    @SideOnly(Side.CLIENT)
    void renderAllExcept(String... var1);

    @SideOnly(Side.CLIENT)
    String[] getPartNames();

}
