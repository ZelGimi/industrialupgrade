package com.denfop.datagen;

import com.denfop.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class PaintingVariantTagsProvider extends TagsProvider<PaintingVariant> {

    public PaintingVariantTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @org.jetbrains.annotations.Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, Registries.PAINTING_VARIANT, pProvider, Constants.MOD_ID, existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(PaintingVariantTags.PLACEABLE).addOptional(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "chemist_picture"));
    }
}
