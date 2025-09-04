package com.denfop.datagen;

import com.denfop.Constants;
import com.denfop.villager.VillagerInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.tags.PoiTypeTags;

import java.util.concurrent.CompletableFuture;

public class IUPoiTypeTagsProvider extends PoiTypeTagsProvider {

    public IUPoiTypeTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @org.jetbrains.annotations.Nullable net.minecraftforge.common.data.ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, Constants.MOD_ID, existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE).add(VillagerInit.BOTANIST_POI.getKey(), VillagerInit.CHEMIST_POI.getKey(), VillagerInit.ENGINEER_POI.getKey(), VillagerInit.MECHANIC_POI.getKey(), VillagerInit.NUCLEAR_POI.getKey(), VillagerInit.METALLURG_POI.getKey());
    }
}
