package com.denfop.events;


import com.denfop.Constants;
import com.denfop.mixin.access.StructureTemplatePoolAccessor;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.TagsUpdatedEvent;

import java.util.ArrayList;
import java.util.Objects;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class VillagePoolInjectorNbt {

    private VillagePoolInjectorNbt() {
    }

    @SubscribeEvent
    public static void onTagsUpdated(final TagsUpdatedEvent event) {
        if (event.getUpdateCause() != TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD) {
            return;
        }

        final RegistryAccess registryAccess = event.getRegistryAccess();

        addToPool(
                ResourceLocation.tryBuild("minecraft", "village/plains/houses"),
                ResourceLocation.tryBuild(Constants.MOD_ID, "village/plains/houses/engineer_house"),
                registryAccess,
                3
        );
        addToPool(
                ResourceLocation.tryBuild("minecraft", "village/plains/houses"),
                ResourceLocation.tryBuild(Constants.MOD_ID, "village/plains/houses/mechanic_house"),
                registryAccess,
                3
        );
        addToPool(
                ResourceLocation.tryBuild("minecraft", "village/plains/houses"),
                ResourceLocation.tryBuild(Constants.MOD_ID, "village/plains/houses/metallurg_house"),
                registryAccess,
                3
        );
        addToPool(
                ResourceLocation.tryBuild("minecraft", "village/plains/houses"),
                ResourceLocation.tryBuild(Constants.MOD_ID, "village/plains/houses/chemist_house"),
                registryAccess,
                3
        );
        addToPool(
                ResourceLocation.tryBuild("minecraft", "village/plains/houses"),
                ResourceLocation.tryBuild(Constants.MOD_ID, "village/plains/houses/botanist_house"),
                registryAccess,
                3
        );
        addToPool(
                ResourceLocation.tryBuild("minecraft", "village/plains/houses"),
                ResourceLocation.tryBuild(Constants.MOD_ID, "village/plains/houses/nuclear_house"),
                registryAccess,
                2
        );
        addToPool(
                ResourceLocation.tryBuild("minecraft", "village/plains/houses"),
                ResourceLocation.tryBuild(Constants.MOD_ID, "village/plains/houses/storage_house"),
                registryAccess,
                2
        );
    }


    private static void addToPool(ResourceLocation poolId, ResourceLocation toAdd, RegistryAccess regAccess, int weight) {
        Registry<StructureTemplatePool> registry = regAccess.registryOrThrow(Registries.TEMPLATE_POOL);
        Registry<StructureProcessorList> holdergetter1 = regAccess.registryOrThrow(Registries.PROCESSOR_LIST);
        Holder<StructureProcessorList> holder3 = holdergetter1.getHolder(ProcessorLists.MOSSIFY_10_PERCENT).get();
        StructureTemplatePool pool = Objects.requireNonNull(registry.get(poolId), poolId.getPath());
        StructureTemplatePoolAccessor poolAccess = (StructureTemplatePoolAccessor) pool;
        if (!(poolAccess.getRawTemplates() instanceof ArrayList)) {
            poolAccess.setRawTemplates(new ArrayList(poolAccess.getRawTemplates()));
        }
        SinglePoolElement addedElement = SinglePoolElement.legacy(toAdd.toString(), holder3).apply(StructureTemplatePool.Projection.RIGID);
        poolAccess.getRawTemplates().add(Pair.of(addedElement, 6));
        poolAccess.getTemplates().add(addedElement);
    }
}