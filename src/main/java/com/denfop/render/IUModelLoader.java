package com.denfop.render;

import com.denfop.Constants;
import ic2.core.model.IReloadableModel;
import ic2.core.model.ModelComparator;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IUModelLoader implements ICustomModelLoader {

    private static final Map<ResourceLocation, IReloadableModel> models = new HashMap<>();

    public IUModelLoader() {
    }

    public void register(String path, IReloadableModel model) {
        this.register(new ResourceLocation(Constants.MOD_ID, path), model);
    }

    public void register(ResourceLocation location, IReloadableModel model) {
        models.put(location, model);
    }

    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {

        for (final IReloadableModel model : models.values()) {
            model.onReload();
        }

        ModelComparator.onReload();
    }

    public boolean accepts(@Nonnull ResourceLocation modelLocation) {
        return models.containsKey(modelLocation);
    }

    @Nonnull
    public IModel loadModel(@Nonnull ResourceLocation modelLocation) throws IOException {
        return models.get(modelLocation);
    }

}
