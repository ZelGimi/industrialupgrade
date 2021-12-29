//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.denfop.api.render;

import net.minecraft.util.ResourceLocation;
import thaumcraft.client.lib.obj.WavefrontObject.ModelFormatException;

public interface IModelCustomLoader {

    String getType();

    String[] getSuffixes();

    IModelCustom loadInstance(ResourceLocation var1) throws ModelFormatException;

}
