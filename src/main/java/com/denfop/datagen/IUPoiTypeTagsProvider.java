package com.denfop.datagen;

import com.denfop.Constants;
import com.denfop.villager.VillagerInit;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class IUPoiTypeTagsProvider extends PoiTypeTagsProvider {

    public IUPoiTypeTagsProvider(DataGenerator pOutput, ExistingFileHelper fileHelper) {
        super(pOutput, Constants.MOD_ID, fileHelper);
    }

    protected void addTags() {
        this.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE).add(VillagerInit.BOTANIST_POI.getKey(), VillagerInit.CHEMIST_POI.getKey(), VillagerInit.ENGINEER_POI.getKey(), VillagerInit.MECHANIC_POI.getKey(), VillagerInit.NUCLEAR_POI.getKey(), VillagerInit.METALLURG_POI.getKey());
    }
}
