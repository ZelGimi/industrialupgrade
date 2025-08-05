package com.denfop.villager;

import com.denfop.IUItem;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


import static com.denfop.register.Register.POI_TYPE;
import static com.denfop.register.Register.VILLAGER_PROFESSIONS;

public class VillagerInit {


    public static final RegistryObject<PoiType> ENGINEER_POI = registerPoi("engineer", IUItem.programming_table.getObject(0));
    public static final RegistryObject<PoiType> MECHANIC_POI = registerPoi("mechanic", IUItem.basemachine2.getObject(BlockBaseMachine3.generator_iu.getId()));
    public static final RegistryObject<PoiType> NUCLEAR_POI = registerPoi("nuclear",IUItem.basemachine.getObject(10));
    public static final RegistryObject<PoiType> METALLURG_POI = registerPoi("metallurg", IUItem.anvil.getObject(0));
    public static final RegistryObject<PoiType> CHEMIST_POI = registerPoi("chemist", IUItem.fluidIntegrator.getObject(0));
    public static final RegistryObject<PoiType> BOTANIST_POI = registerPoi("botanist", IUItem.apiary.getObject(0));

    public static final RegistryObject<VillagerProfession> ENGINEER = registerProfession("engineer", ENGINEER_POI);
    public static final RegistryObject<VillagerProfession> MECHANIC = registerProfession("mechanic", MECHANIC_POI);
    public static final RegistryObject<VillagerProfession> NUCLEAR = registerProfession("nuclear", NUCLEAR_POI);
    public static final RegistryObject<VillagerProfession> METALLURG = registerProfession("metallurg", METALLURG_POI);
    public static final RegistryObject<VillagerProfession> CHEMIST = registerProfession("chemist", CHEMIST_POI);
    public static final RegistryObject<VillagerProfession> BOTANIST = registerProfession("botanist", BOTANIST_POI);

    private static RegistryObject<PoiType> registerPoi(String name, RegistryObject<? extends Block> block) {
        return POI_TYPE.register(name, () ->
               new PoiType(ImmutableSet.copyOf(block.get().getStateDefinition().getPossibleStates()),
                        1, 1));
    }

    private static RegistryObject<VillagerProfession> registerProfession(String name, RegistryObject<PoiType> poiType) {
        return VILLAGER_PROFESSIONS.register(name, () -> new VillagerProfession(
                name,
                poiTypeHolder -> poiTypeHolder.value() == poiType.get(),
                poiTypeHolder -> poiTypeHolder.value() == poiType.get(),
                ImmutableSet.of(),
                ImmutableSet.of(),
                SoundEvents.VILLAGER_WORK_ARMORER
        ));
    }


    public static void init() {}
}
