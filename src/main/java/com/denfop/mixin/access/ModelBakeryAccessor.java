package com.denfop.mixin.access;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.texture.AtlasSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ModelBakery.class)
public interface ModelBakeryAccessor {
    @Accessor
    Map<ResourceLocation, UnbakedModel> getUnbakedCache();

    @Accessor
    ResourceManager getResourceManager();

    @Accessor
    AtlasSet getAtlasSet();

    @Accessor
    Map<ResourceLocation, UnbakedModel> getTopLevelModels();

    @Accessor
    Map<ResourceLocation, Pair<TextureAtlas, TextureAtlas.Preparations>> getAtlasPreparations();
}
