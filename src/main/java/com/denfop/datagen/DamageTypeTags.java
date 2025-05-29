package com.denfop.datagen;

import com.denfop.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTags extends DamageTypeTagsProvider {
    public DamageTypeTags(PackOutput p_270719_, CompletableFuture<HolderLookup.Provider> p_270256_,@Nullable ExistingFileHelper existingFileHelper) {
        super(p_270719_, p_270256_, Constants.MOD_ID, existingFileHelper);
    }
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(net.minecraft.tags.DamageTypeTags.BYPASSES_ARMOR).add(DamageTypes.radiationObject,DamageTypes.beeObject,DamageTypes.currentObject,DamageTypes.frostbiteObject,DamageTypes.poison_gasObject);
        this.tag(net.minecraft.tags.DamageTypeTags.IS_FIRE).add(DamageTypes.radiationObject);
    }
}
