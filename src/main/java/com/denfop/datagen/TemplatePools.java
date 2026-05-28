package com.denfop.datagen;

import com.denfop.Constants;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public final class TemplatePools {

    public static final ResourceKey<StructureTemplatePool> PLAINS_HOUSES = ResourceKey.create(
            Registries.TEMPLATE_POOL,
            new ResourceLocation("minecraft", "village/plains/houses")
    );

    public static final ResourceKey<StructureTemplatePool> PLAINS_TERMINATORS = ResourceKey.create(
            Registries.TEMPLATE_POOL,
            new ResourceLocation("minecraft", "village/plains/terminators")
    );

    private TemplatePools() {
    }

    public static void bootstrap(final BootstapContext<StructureTemplatePool> context) {
        final HolderGetter<StructureProcessorList> processorLists = context.lookup(Registries.PROCESSOR_LIST);
        final HolderGetter<StructureTemplatePool> templatePools = context.lookup(Registries.TEMPLATE_POOL);

        final Holder<StructureProcessorList> mossify10 = processorLists.getOrThrow(ProcessorLists.MOSSIFY_10_PERCENT);
        final Holder<StructureProcessorList> mossify70 = processorLists.getOrThrow(ProcessorLists.MOSSIFY_70_PERCENT);
        final Holder<StructureProcessorList> farmPlains = processorLists.getOrThrow(ProcessorLists.FARM_PLAINS);
        final Holder<StructureProcessorList> emptyProcessor = processorLists.getOrThrow(
                ResourceKey.create(Registries.PROCESSOR_LIST, new ResourceLocation("minecraft", "empty"))
        );

        final Holder<StructureTemplatePool> terminators = templatePools.getOrThrow(PLAINS_TERMINATORS);

        context.register(
                PLAINS_HOUSES,
                new StructureTemplatePool(
                        terminators,
                        ImmutableList.of(

                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_1", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_2", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_3", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_4", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_5", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_6", mossify10), 1),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_7", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_8", mossify10), 3),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_medium_house_1", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_medium_house_2", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_big_house_1", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_butcher_shop_1", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_butcher_shop_2", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_tool_smith_1", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_fletcher_house_1", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_shepherds_house_1"), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_armorer_house_1", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_fisher_cottage_1", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_tannery_1", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_cartographer_1", mossify10), 1),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_library_1", mossify10), 5),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_library_2", mossify10), 1),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_masons_house_1", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_weaponsmith_1", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_temple_3", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_temple_4", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_stable_1", mossify10), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_stable_2"), 2),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_large_farm_1", farmPlains), 4),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_farm_1", farmPlains), 4),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_animal_pen_1"), 1),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_animal_pen_2"), 1),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_animal_pen_3"), 5),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_accessory_1"), 1),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_meeting_point_4", mossify70), 3),
                                Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_meeting_point_5"), 1),


                                Pair.of(StructurePoolElement.legacy(
                                        rl("village/plains/houses/engineer_house").toString(), mossify10
                                ), 4),

                                Pair.of(StructurePoolElement.legacy(
                                        rl("village/plains/houses/mechanic_house").toString(),
                                        mossify10
                                ), 3),

                                Pair.of(StructurePoolElement.legacy(
                                        rl("village/plains/houses/metallurg_house").toString(),
                                        mossify10
                                ), 3),

                                Pair.of(StructurePoolElement.legacy(
                                        rl("village/plains/houses/chemist_house").toString(),
                                        mossify10
                                ), 3),

                                Pair.of(StructurePoolElement.legacy(
                                        rl("village/plains/houses/botanist_house").toString(),
                                        mossify10
                                ), 4),

                                Pair.of(StructurePoolElement.legacy(
                                        rl("village/plains/houses/nuclear_house").toString(),
                                        mossify10
                                ), 1),

                                Pair.of(StructurePoolElement.legacy(
                                        rl("village/plains/houses/storage_house").toString(),
                                        mossify10
                                ), 2),


                                Pair.of(StructurePoolElement.empty(), 10)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
    }

    private static ResourceLocation rl(final String path) {
        return new ResourceLocation(Constants.MOD_ID, path);
    }
}