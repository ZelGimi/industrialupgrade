package com.denfop.datagen;

import com.denfop.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;

public class PaintingVariantProvider {
    public static final ResourceKey<PaintingVariant> chemist_picture = registerKey("chemist_picture");

    private static ResourceKey<PaintingVariant> registerKey(String name) {
        return ResourceKey.create(Registries.PAINTING_VARIANT, ResourceLocation.tryBuild(Constants.MOD_ID, name));
    }

    public static void bootstrap(BootstrapContext<PaintingVariant> pContext) {
        pContext.register(chemist_picture, new PaintingVariant(4, 4, chemist_picture.location()));


    }
}
