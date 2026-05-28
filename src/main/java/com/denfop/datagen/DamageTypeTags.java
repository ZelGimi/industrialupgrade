package com.denfop.datagen;

import com.denfop.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTags extends DamageTypeTagsProvider {
    public DamageTypeTags(PackOutput p_270719_, CompletableFuture<HolderLookup.Provider> p_270256_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_270719_, p_270256_, Constants.MOD_ID, existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider pProvider) {
         this.tag(net.minecraft.tags.DamageTypeTags.IS_FIRE).addOptionalTag(DamageTypes.radiationObject.location());
        this.tag(net.minecraft.tags.DamageTypeTags.BYPASSES_ARMOR).addOptionalTag(DamageTypes.beeObject.location());
        this.tag(net.minecraft.tags.DamageTypeTags.BYPASSES_ARMOR).addOptionalTag(DamageTypes.currentObject.location());
        this.tag(net.minecraft.tags.DamageTypeTags.BYPASSES_ARMOR).addOptionalTag(DamageTypes.frostbiteObject.location());
        this.tag(net.minecraft.tags.DamageTypeTags.BYPASSES_ARMOR).addOptionalTag(DamageTypes.poison_gasObject.location());
    }
}
