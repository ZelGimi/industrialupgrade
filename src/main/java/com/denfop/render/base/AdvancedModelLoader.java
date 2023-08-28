package com.denfop.render.base;

import com.denfop.api.render.IModelCustom;
import com.denfop.api.render.IModelCustomLoader;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class AdvancedModelLoader {

    private static final Map<String, IModelCustomLoader> instances = Maps.newHashMap();

    static {
        registerModelHandler(new ObjModelLoader());
    }

    public AdvancedModelLoader() {
    }

    public static void registerModelHandler(IModelCustomLoader modelHandler) {
        String[] var1 = modelHandler.getSuffixes();


        for (String suffix : var1) {
            instances.put(suffix, modelHandler);
        }

    }

    public static IModelCustom loadModel(ResourceLocation resource) throws IllegalArgumentException,
            WavefrontObject.ModelFormatException {
        String name = resource.getResourcePath();
        int i = name.lastIndexOf(46);
        if (i == -1) {
            throw new IllegalArgumentException("The resource name+" + resource + " is not valid");
        } else {
            String suffix = name.substring(i + 1);
            IModelCustomLoader loader = instances.get(suffix);
            if (loader == null) {
                throw new IllegalArgumentException("The resource name+" + resource + " is not supported");
            } else {
                return loader.loadInstance(resource);
            }
        }
    }

}
