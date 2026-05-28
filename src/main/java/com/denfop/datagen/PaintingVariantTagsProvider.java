package com.denfop.datagen;

import com.denfop.Constants;
import com.denfop.painting.IUPainting;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.world.entity.decoration.PaintingVariant;

public class PaintingVariantTagsProvider extends TagsProvider<PaintingVariant> {

    public PaintingVariantTagsProvider(DataGenerator pOutput, @org.jetbrains.annotations.Nullable net.minecraftforge.common.data.ExistingFileHelper existingFileHelper) {
        super(pOutput, Registry.PAINTING_VARIANT, Constants.MOD_ID, existingFileHelper);
    }

    protected void addTags() {
        this.tag(PaintingVariantTags.PLACEABLE).add(IUPainting.CHEMIST_PICTURE.getKey());
    }
}
