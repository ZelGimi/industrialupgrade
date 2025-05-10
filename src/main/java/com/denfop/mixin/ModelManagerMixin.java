package com.denfop.mixin;

import net.minecraft.client.renderer.texture.AtlasSet;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(ModelManager.class)
public abstract class ModelManagerMixin {


    @Shadow
    @Final
    @Mutable
    private AtlasSet atlases;


    @Shadow
    @Final
    @Mutable
    private Map<ResourceLocation, BakedModel> bakedRegistry;


}
