package com.denfop.villager;

import com.denfop.IUItem;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.denfop.register.Register.POI_TYPE;
import static com.denfop.register.Register.VILLAGER_PROFESSIONS;

public class VillagerInit {


    public static final DeferredHolder<PoiType, PoiType> ENGINEER_POI = registerPoi("engineer", IUItem.programming_table.getObject(0));
    public static final DeferredHolder<PoiType, PoiType> MECHANIC_POI = registerPoi("mechanic", IUItem.basemachine2.getObject(BlockBaseMachine3Entity.generator_iu.getId()));
    public static final DeferredHolder<PoiType, PoiType> NUCLEAR_POI = registerPoi("nuclear", IUItem.basemachine.getObject(10));
    public static final DeferredHolder<PoiType, PoiType> METALLURG_POI = registerPoi("metallurg", IUItem.anvil.getObject(0));
    public static final DeferredHolder<PoiType, PoiType> CHEMIST_POI = registerPoi("chemist", IUItem.fluidIntegrator.getObject(0));
    public static final DeferredHolder<PoiType, PoiType> BOTANIST_POI = registerPoi("botanist", IUItem.apiary.getObject(0));

    public static final DeferredHolder<VillagerProfession, VillagerProfession> ENGINEER = registerProfession("engineer", ENGINEER_POI);
    public static final DeferredHolder<VillagerProfession, VillagerProfession> MECHANIC = registerProfession("mechanic", MECHANIC_POI);
    public static final DeferredHolder<VillagerProfession, VillagerProfession> NUCLEAR = registerProfession("nuclear", NUCLEAR_POI);
    public static final DeferredHolder<VillagerProfession, VillagerProfession> METALLURG = registerProfession("metallurg", METALLURG_POI);
    public static final DeferredHolder<VillagerProfession, VillagerProfession> CHEMIST = registerProfession("chemist", CHEMIST_POI);
    public static final DeferredHolder<VillagerProfession, VillagerProfession> BOTANIST = registerProfession("botanist", BOTANIST_POI);

    private static DeferredHolder<PoiType, PoiType> registerPoi(String name, DeferredHolder<Block, ? extends Block> block) {
        return POI_TYPE.register(name, () ->
                new PoiType(ImmutableSet.copyOf(block.get().getStateDefinition().getPossibleStates()),
                        1, 1));
    }

    private static DeferredHolder<VillagerProfession, VillagerProfession> registerProfession(String name, DeferredHolder<PoiType, PoiType> poiType) {
        return VILLAGER_PROFESSIONS.register(name, () -> new VillagerProfession(
                name,
                poiTypeHolder -> poiTypeHolder.value() == poiType.get(),
                poiTypeHolder -> poiTypeHolder.value() == poiType.get(),
                ImmutableSet.of(),
                ImmutableSet.of(),
                SoundEvents.VILLAGER_WORK_ARMORER
        ));
    }


    public static void init() {
    }
}
