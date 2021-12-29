package com.denfop.render;

import com.denfop.api.render.IModelCustom;
import com.denfop.api.render.IModelCustomLoader;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class AdvancedModelLoader {

    private static final Map<String, IModelCustomLoader> instances = Maps.newHashMap();

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
            FMLLog.severe("The resource name %s is not valid", resource);
            throw new IllegalArgumentException("The resource name is not valid");
        } else {
            String suffix = name.substring(i + 1);
            IModelCustomLoader loader = instances.get(suffix);
            if (loader == null) {
                FMLLog.severe("The resource name %s is not supported", resource);
                throw new IllegalArgumentException("The resource name is not supported");
            } else {
                return loader.loadInstance(resource);
            }
        }
    }

    public static Collection<String> getSupportedSuffixes() {
        return instances.keySet();
    }

    static {
        registerModelHandler(new ObjModelLoader());
    }
}
