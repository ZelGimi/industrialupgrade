package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.item.MultiBlockItem;
import com.denfop.blockentity.base.*;
import com.denfop.blockentity.crop.BlockEntityDualMultiCrop;
import com.denfop.blockentity.crop.BlockEntityQuadMultiCrop;
import com.denfop.blockentity.crop.BlockEntitySingleMultiCrop;
import com.denfop.blockentity.crop.BlockEntityTripleMultiCrop;
import com.denfop.blockentity.mechanism.*;
import com.denfop.blockentity.mechanism.bio.*;
import com.denfop.blockentity.mechanism.combpump.BlockEntityAdvCombPump;
import com.denfop.blockentity.mechanism.combpump.BlockEntityImpCombPump;
import com.denfop.blockentity.mechanism.combpump.BlockEntityPerCombPump;
import com.denfop.blockentity.mechanism.combpump.BlockEntitySimpleCombinedPump;
import com.denfop.blockentity.mechanism.cooling.BlockEntityCooling;
import com.denfop.blockentity.mechanism.cooling.BlockEntityFluidCooling;
import com.denfop.blockentity.mechanism.dual.heat.BlockEntityWeldingMachine;
import com.denfop.blockentity.mechanism.energy.BlockEntityEnergyController;
import com.denfop.blockentity.mechanism.energy.BlockEntityEnergyRemover;
import com.denfop.blockentity.mechanism.energy.BlockEntityEnergySubstitute;
import com.denfop.blockentity.mechanism.generator.energy.BlockEntityPeatGenerator;
import com.denfop.blockentity.mechanism.generator.energy.BlockEntitySolarGenerator;
import com.denfop.blockentity.mechanism.generator.energy.coal.BlockEntityGenerator;
import com.denfop.blockentity.mechanism.generator.energy.fluid.BlockEntityBioFuelGenerator;
import com.denfop.blockentity.mechanism.generator.energy.fluid.BlockEntityGasGenerator;
import com.denfop.blockentity.mechanism.generator.energy.fluid.BlockEntityGeoGenerator;
import com.denfop.blockentity.mechanism.generator.energy.redstone.BlockEntityAdvRedstoneGenerator;
import com.denfop.blockentity.mechanism.generator.energy.redstone.BlockEntityImpRedstoneGenerator;
import com.denfop.blockentity.mechanism.generator.energy.redstone.BlockEntityPerRedstoneGenerator;
import com.denfop.blockentity.mechanism.generator.energy.redstone.BlockEntityRedstoneGenerator;
import com.denfop.blockentity.mechanism.generator.things.fluid.BlockEntityAirCollector;
import com.denfop.blockentity.mechanism.generator.things.fluid.BlockEntityWaterGenerator;
import com.denfop.blockentity.mechanism.heat.BlockEntityElectricHeat;
import com.denfop.blockentity.mechanism.heat.BlockEntityFluidHeat;
import com.denfop.blockentity.mechanism.quantum_storage.BlockEntityAdvQuantumStorage;
import com.denfop.blockentity.mechanism.quantum_storage.BlockEntityImpQuantumStorage;
import com.denfop.blockentity.mechanism.quantum_storage.BlockEntityPerQuantumStorage;
import com.denfop.blockentity.mechanism.quantum_storage.BlockEntitySimpleQuantumStorage;
import com.denfop.blockentity.mechanism.radiation_storage.BlockEntitySimpleRadiationStorage;
import com.denfop.blockentity.mechanism.replicator.BlockEntityAdvReplicator;
import com.denfop.blockentity.mechanism.replicator.BlockEntityImpReplicator;
import com.denfop.blockentity.mechanism.replicator.BlockEntityPerReplicator;
import com.denfop.blockentity.mechanism.replicator.BlockEntityReplicator;
import com.denfop.blockentity.mechanism.solardestiller.BlockEntityAdvSolarDestiller;
import com.denfop.blockentity.mechanism.solardestiller.BlockEntityImpSolarDestiller;
import com.denfop.blockentity.mechanism.solardestiller.BlockEntityPerSolarDestiller;
import com.denfop.blockentity.mechanism.solardestiller.BlockEntitySolarDestiller;
import com.denfop.blockentity.mechanism.solarium_storage.BlockEntityAdvSolariumStorage;
import com.denfop.blockentity.mechanism.solarium_storage.BlockEntityImpSolariumStorage;
import com.denfop.blockentity.mechanism.solarium_storage.BlockEntityPerSolariumStorage;
import com.denfop.blockentity.mechanism.solarium_storage.BlockEntitySimpleSolariumStorage;
import com.denfop.blockentity.mechanism.steam.*;
import com.denfop.blockentity.mechanism.vending.BlockEntityAdvVending;
import com.denfop.blockentity.mechanism.vending.BlockEntityImpVending;
import com.denfop.blockentity.mechanism.vending.BlockEntityPerVending;
import com.denfop.blockentity.mechanism.vending.BlockEntityVending;
import com.denfop.blockentity.mechanism.water.BlockEntityAdvWaterGenerator;
import com.denfop.blockentity.mechanism.water.BlockEntityImpWaterGenerator;
import com.denfop.blockentity.mechanism.water.BlockEntityPerWaterGenerator;
import com.denfop.blockentity.mechanism.water.BlockEntitySimpleWaterGenerator;
import com.denfop.blockentity.mechanism.wind.BlockEntityAdvWindGenerator;
import com.denfop.blockentity.mechanism.wind.BlockEntityImpWindGenerator;
import com.denfop.blockentity.mechanism.wind.BlockEntityPerWindGenerator;
import com.denfop.blockentity.mechanism.wind.BlockEntitySimpleWindGenerator;
import com.denfop.blockentity.mechanism.worlcollector.*;
import com.denfop.blockentity.panels.entity.BlockEntityMiniPanels;
import com.denfop.blockentity.tank.BlockEntityOakTank;
import com.denfop.blockentity.tank.BlockEntitySteelTank;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.util.Set;

;

public enum BlockBaseMachine3Entity implements MultiBlockEntity, MultiBlockItem {
    radiation_purifier(BlockEntityRadiationPurifier.class, 1),
    privatizer(BlockEntityPrivatizer.class, 2),
    tuner(BlockEntityTunerWireless.class, 3),
    spawner(BlockEntityAutoSpawner.class, 4),
    elec_heat(BlockEntityElectricHeat.class, 5),
    fluid_heat(BlockEntityFluidHeat.class, 6),
    antimagnet(BlockEntityAntiMagnet.class, 7),
    antiupgradeblock(BlockEntityAntiUpgradeBlock.class, 8),
    watergenerator(BlockEntityWaterGenerator.class, 9),
    cooling(BlockEntityCooling.class, 10),
    aircollector(BlockEntityAirCollector.class, 11),
    combiner_se_generators(BlockEntityCombinerSEGenerators.class, 12),
    probe_assembler(BlockEntityProbeAssembler.class, 13),
    research_table_space(BlockEntityResearchTableSpace.class, 14),
    rocket_assembler(BlockEntityRocketAssembler.class, 15),
    rocket_launch_pad(BlockEntityRocketLaunchPad.class, 16),
    rotor_assembler(BlockEntityRotorAssembler.class, 17),
    rotor_modifier(BlockEntityRotorModifier.class, 18),
    satellite_assembler(BlockEntitySatelliteAssembler.class, 19),
    soil_analyzer(BlockEntitySoilAnalyzer.class, 20),
    rods_manufacturer(BlockEntityRodManufacturer.class, 21),
    ender_assembler(BlockEntityEnderAssembler.class, 23),
    gas_combiner(BlockEntityGasCombiner.class, 26),
    analyzer_chest(BlockEntityAnalyzerChest.class, 27),
    simple_wind_generator(BlockEntitySimpleWindGenerator.class, 28),
    adv_wind_generator(BlockEntityAdvWindGenerator.class, 29),
    imp_wind_generator(BlockEntityImpWindGenerator.class, 30),
    per_wind_generator(BlockEntityPerWindGenerator.class, 31),
    energy_controller(BlockEntityEnergyController.class, 32),
    substitute(BlockEntityEnergySubstitute.class, 33),
    aer_assembler(BlockEntityAerAssembler.class, 34),
    aqua_assembler(BlockEntityAquaAssembler.class, 35),
    earth_assembler(BlockEntityEarthAssembler.class, 36),
    nether_assembler(BlockEntityNetherAssembler.class, 37),
    auto_digger(BlockEntityAutoDigger.class, 38),
    crystallize(BlockEntityCrystallize.class, 39),
    double_handlerho(BlockEntityAdvHandlerHeavyOre.class, 40),
    triple_handlerho(BlockEntityImpHandlerHeavyOre.class, 41),
    quad_handlerho(BlockEntityPerHandlerHeavyOre.class, 42),
    water_modifier(BlockEntityWaterRotorModifier.class, 43),
    water_rotor_assembler(BlockEntityWaterRotorAssembler.class, 44),
    simple_water_generator(BlockEntitySimpleWaterGenerator.class, 45),
    adv_water_generator(BlockEntityAdvWaterGenerator.class, 46),
    imp_water_generator(BlockEntityImpWaterGenerator.class, 47),
    per_water_generator(BlockEntityPerWaterGenerator.class, 48),
    scanner_iu(BlockEntitySimpleScanner.class, 49),
    adv_scanner(BlockEntityAdvScanner.class, 50),
    imp_scanner(BlockEntityImpScanner.class, 51),
    per_scanner(BlockEntityPerScanner.class, 52),
    replicator_iu(BlockEntityReplicator.class, 53),
    adv_replicator(BlockEntityAdvReplicator.class, 54),
    imp_replicator(BlockEntityImpReplicator.class, 55),
    per_replicator(BlockEntityPerReplicator.class, 56),

    solarium_storage(BlockEntitySimpleSolariumStorage.class, 57),
    adv_solarium_storage(BlockEntityAdvSolariumStorage.class, 58),
    imp_solarium_storage(BlockEntityImpSolariumStorage.class, 59),
    per_solarium_storage(BlockEntityPerSolariumStorage.class, 60),
    quantum_storage(BlockEntitySimpleQuantumStorage.class, 61),
    adv_quantum_storage(BlockEntityAdvQuantumStorage.class, 62),
    imp_quantum_storage(BlockEntityImpQuantumStorage.class, 63),
    per_quantum_storage(BlockEntityPerQuantumStorage.class, 64),
    solardestiller(BlockEntitySolarDestiller.class, 65),
    adv_solar_destiller(BlockEntityAdvSolarDestiller.class, 66),
    imp_solar_destiller(BlockEntityImpSolarDestiller.class, 67),
    per_solar_destiller(BlockEntityPerSolarDestiller.class, 68),
    limiter(BlockEntityLimiter.class, 69),
    redstone_generator(BlockEntityRedstoneGenerator.class, 70),
    adv_redstone_generator(BlockEntityAdvRedstoneGenerator.class, 71),
    imp_redstone_generator(BlockEntityImpRedstoneGenerator.class, 72),
    per_redstone_generator(BlockEntityPerRedstoneGenerator.class, 73),
    welding(BlockEntityWeldingMachine.class, 74),
    gas_generator(BlockEntityGasGenerator.class, 76),
    pattern_storage_iu(BlockEntityPatternStorage.class, 77),
    generator_iu(BlockEntityGenerator.class, 78),
    geogenerator_iu(BlockEntityGeoGenerator.class, 79),
    pump_iu(BlockEntitySimplePump.class, 80),
    solar_iu(BlockEntitySolarGenerator.class, 81),
    teleporter_iu(BlockEntityTeleporter.class, 84),
    canner_iu(BlockEntityCanner.class, 89),

    minipanel(BlockEntityMiniPanels.class, 91),
    facademechanism(BlockEntityFacadeBlock.class, 92),
    battery_factory(BlockEntityBatteryFactory.class, 93),
    socket_factory(BlockEntitySocketFactory.class, 94),
    matter_factory(BlockEntityMatterFactory.class, 95),
    laser_polisher(BlockEntityLaserPolisher.class, 96),
    graphite_handler(BlockEntityGraphiteHandler.class, 97),
    moon_spotter(BlockEntityMoonSpotter.class, 98),
    stamp_mechanism(BlockEntityStampMechanism.class, 99),
    reactor_rod_factory(BlockEntityRodFactory.class, 100),
    nuclear_waste_recycler(BlockEntityNuclearWasteRecycler.class, 101),
    reactor_safety_doom(BlockEntityReactorSafetyDoom.class, 102),
    pallet_generator(BlockEntityPalletGenerator.class, 103),
    enchanter_books(BlockEntityEnchanterBooks.class, 104),
    refrigerator_coolant(BlockEntityRefrigeratorCoolant.class, 105),
    autocrafter(BlockEntityAutoCrafter.class, 106),
    tesseract(BlockEntityTesseract.class, 107),
    comb_pump(BlockEntitySimpleCombinedPump.class, 108),
    adv_comb_pump(BlockEntityAdvCombPump.class, 109),
    imp_comb_pump(BlockEntityImpCombPump.class, 110),
    per_comb_pump(BlockEntityPerCombPump.class, 111),
    safe(BlockEntitySafe.class, 112),
    vending(BlockEntityVending.class, 113),
    adv_vending(BlockEntityAdvVending.class, 114),
    imp_vending(BlockEntityImpVending.class, 115),
    per_vending(BlockEntityPerVending.class, 116),
    autofuse(BlockEntityAutoFuse.class, 117),
    auto_open_box(BlockEntityAutoOpenBox.class, 118),
    exp_chargepad(BlockEntityChargepadExperience.class, 119),
    recipe_tuner(BlockEntityRecipeTuner.class, 120),
    energy_trash(BlockEntityEnergyTrash.class, 121),
    fluid_trash(BlockEntityFluidTrash.class, 122),
    item_trash(BlockEntityItemTrash.class, 123),
    rolling_machine(BlockEntityRollingMachine.class, 124),
    matter_collector(BlockEntityWirelessMatterCollector.class, 125),

    machine_charger(BlockEntityMachineCharge.class, 126),
    automatic_mechanism(BlockEntityAutomaticMechanism.class, 127),
    wireless_oil_pump(BlockEntityWirelessOilPump.class, 128),
    wireless_mineral_quarry(BlockEntityWirelessMineralQuarry.class, 129),
    wireless_controller_reactors(BlockEntityWirelessControllerReactors.class, 130),
    simulation_reactors(BlockEntitySimulatorReactor.class, 131),
    radiation_storage(BlockEntitySimpleRadiationStorage.class, 132),
    wireless_controller_graphite_reactors(BlockEntityWirelessGraphiteController.class, 133),
    wireless_controller_heat_reactors(BlockEntityWirelessHeatController.class, 134),
    energy_remover(BlockEntityEnergyRemover.class, 135),
    energy_barrier(BlockEntityEnergyBarrier.class, 136),
    mob_magnet(BlockEntityMobMagnet.class, 137),
    upgrade_machine(BlockEntityUpgradeMachineFactory.class, 138),
    imp_refiner(BlockEntityImpOilRefiner.class, 139),
    refrigerator_fluids(BlockEntityRefrigeratorFluids.class, 140),
    item_divider(BlockEntityItemDivider.class, 141),
    item_divider_to_fluid(BlockEntityItemDividerFluids.class, 142),
    fluid_separator(BlockEntityFluidSeparator.class, 143),
    polymerizer(BlockEntityPolymerizer.class, 144),
    fluid_adapter(BlockEntityFluidAdapter.class, 145),
    fluid_integrator(BlockEntityFluidIntegrator.class, 146),
    solid_state_electrolyzer(BlockEntitySolidStateElectrolyzer.class, 147),
    oil_purifier(BlockEntityOilPurifier.class, 148),
    purifier_soil(BlockEntityPurifierSoil.class, 149),
    electric_dryer(BlockEntityElectricDryer.class, 150),
    electric_squeezer(BlockEntityElectricSqueezer.class, 151),
    fluid_cooling(BlockEntityFluidCooling.class, 152),
    solid_cooling(BlockEntitySolidCooling.class, 153),
    imp_alloy_smelter(BlockEntityImpAlloySmelter.class, 154),
    per_alloy_smelter(BlockEntityPerAlloySmelter.class, 155),
    fluid_mixer(BlockEntityFluidMixer.class, 156),
    solid_fluid_mixer(BlockEntitySolidFluidMixer.class, 157),
    fluid_heater(BlockEntityFluidHeater.class, 158),
    silicon_crystal_handler(BlockEntitySiliconCrystalHandler.class, 159),
    neutronseparator(BlockEntityNeutronSeparator.class, 160),
    positronconverter(BlockEntityPositronConverter.class, 161),
    itemmanipulator(BlockEntityItemManipulator.class, 162),
    steamboiler(BlockEntitySteamBoiler.class, 163, EnumTypeCasing.STEAM),
    steampressureconverter(BlockEntitySteamPressureConverter.class, 164, EnumTypeCasing.STEAM),
    steamdryer(BlockEntitySteamDryer.class, 165, EnumTypeCasing.STEAM),
    steam_macerator(BlockEntitySteamMacerator.class, 166, EnumTypeCasing.STEAM),
    steam_compressor(BlockEntitySteamCompressor.class, 167, EnumTypeCasing.STEAM),
    steam_extractor(BlockEntitySteamExtractor.class, 168, EnumTypeCasing.STEAM),
    steam_extruder(BlockEntitySteamExtruder.class, 169, EnumTypeCasing.STEAM),
    steam_cutting(BlockEntitySteamCutting.class, 170, EnumTypeCasing.STEAM),
    steam_converter(BlockEntitySteamConverter.class, 171, EnumTypeCasing.STEAM),
    steam_squeezer(BlockEntitySteamSqueezer.class, 172, EnumTypeCasing.STEAM),
    solid_fluid_integrator(BlockEntitySolidFluidIntegrator.class, 173),
    solid_mixer(BlockEntitySolidMixer.class, 174),
    peat_generator(BlockEntityPeatGenerator.class, 175),
    triple_solid_mixer(BlockEntityTripleSolidMixer.class, 176),
    single_fluid_adapter(BlockEntitySingleFluidAdapter.class, 177),
    steam_peat_generator(BlockEntitySteamPeatGenerator.class, 178, EnumTypeCasing.STEAM),
    steam_handler_ore(BlockEntitySteamHandlerHeavyOre.class, 179, EnumTypeCasing.STEAM),
    steam_electrolyzer(BlockEntitySteamElectrolyzer.class, 180, EnumTypeCasing.STEAM),
    steam_ampere_generator(BlockEntitySteamAmpereGenerator.class, 181, EnumTypeCasing.STEAM),
    steam_solid_fluid_mixer(BlockEntitySteamSolidFluidMixer.class, 182, EnumTypeCasing.STEAM),
    programming_table(BlockEntityProgrammingTable.class, 183),
    electronic_assembler(BlockEntityElectronicsAssembler.class, 184),
    oak_tank(BlockEntityOakTank.class, 185),
    steel_tank(BlockEntitySteelTank.class, 186),
    steam_pump(BlockEntitySteamPump.class, 187, EnumTypeCasing.STEAM),
    steam_sharpener(BlockEntitySteamSharpener.class, 188, EnumTypeCasing.STEAM),
    alkalineearthquarry(BlockEntityAlkalineEarthQuarry.class, 189),
    radioactive_handler_ore(BlockEntityRadioactiveOreHandler.class, 190),
    industrial_ore_purifier(BlockEntityIndustrialOrePurifier.class, 191),
    gas_pump(BlockEntityGasPump.class, 192),
    wireless_gas_pump(BlockEntityWirelessGasPump.class, 193),
    quantum_miner(BlockEntityQuantumMiner.class, 194),
    quantum_transformer(BlockEntityQuantumMolecular.class, 195),
    steam_storage(BlockEntitySteamStorage.class, 196, EnumTypeCasing.STEAM),
    quarry_pipe(TileQuarryPipe.class, 197),
    steam_quarry(BlockEntitySteamQuarry.class, 198, EnumTypeCasing.STEAM),
    adv_steam_quarry(BlockEntityAdvSteamQuarry.class, 199, EnumTypeCasing.STEAM),
    steam_rolling(BlockEntitySteamRolling.class, 200, EnumTypeCasing.STEAM),
    electric_wire_insulator(BlockEntityWireInsulator.class, 201),
    steam_wire_insulator(BlockEntitySteamWireInsulator.class, 202, EnumTypeCasing.STEAM),
    steam_crystal_charge(BlockEntitySteamCrystalCharge.class, 203, EnumTypeCasing.STEAM),
    crystal_charge(BlockEntityCrystalCharger.class, 204),
    ampere_generator(BlockEntityAmpereGenerator.class, 205),
    steam_bio_generator(BlockEntitySteamBioGenerator.class, 206, EnumTypeCasing.BIO),
    bio_macerator(BlockEntityBioMacerator.class, 207, EnumTypeCasing.BIO),
    bio_compressor(BlockEntityBioCompressor.class, 208, EnumTypeCasing.BIO),
    bio_extractor(BlockEntityBioExtractor.class, 209, EnumTypeCasing.BIO),
    bio_extruder(BlockEntityBioExtruder.class, 210, EnumTypeCasing.BIO),
    bio_cutting(BlockEntityBioCutting.class, 211, EnumTypeCasing.BIO),
    bio_furnace(BlockEntityBioFurnace.class, 212, EnumTypeCasing.BIO),
    bio_rolling(BlockEntityBioRolling.class, 213, EnumTypeCasing.BIO),
    bio_orewashing(BlockEntityBioOreWashing.class, 214, EnumTypeCasing.BIO),
    bio_centrifuge(BlockEntityBioCentrifuge.class, 215, EnumTypeCasing.BIO),
    bio_gearing(BlockEntityBioGearing.class, 216, EnumTypeCasing.BIO),
    electric_refractory_furnace(BlockEntityElectricRefractoryFurnace.class, 217),
    chicken_farm(BlockEntityChickenFarm.class, 218),
    sheep_farm(BlockEntitySheepFarm.class, 219),
    cow_farm(BlockEntityCowFarm.class, 220),
    sapling_gardener(BlockEntitySaplingGardener.class, 221),
    tree_breaker(BlockEntityTreeBreaker.class, 222),
    cactus_farm(BlockEntityCactusFarm.class, 223),
    pig_farm(BlockEntityPigFarm.class, 224),
    electric_brewing(BlockEntityBrewingPlant.class, 225),
    sawmill(BlockEntitySawmill.class, 226),
    gen_addition_stone(BlockEntityGenerationAdditionStone.class, 227),
    plant_fertilizer(BlockEntityPlantFertilizer.class, 228),
    plant_collector(BlockEntityPlantCollector.class, 229),
    plant_gardener(BlockEntityPlantGardener.class, 230),
    field_cleaner(BlockEntityFieldCleaner.class, 231),
    weeder(BlockEntityWeeder.class, 232),
    apothecary_bee(BlockEntityApothecaryBee.class, 233),
    collector_product_bee(BlockEntityCollectorProductBee.class, 234),
    incubator(BlockEntityIncubator.class, 235),
    insulator(BlockEntityInsulator.class, 236),
    rna_collector(BlockEntityRNACollector.class, 237),
    mutatron(BlockEntityMutatron.class, 238),
    reverse_transcriptor(BlockEntityReverseTransriptor.class, 239),
    genetic_stabilizer(BlockEntityGeneticStabilize.class, 240),
    centrifuge(BlockEntityCentrifuge.class, 241),
    genetic_replicator(BlockEntityGeneticReplicator.class, 242),
    inoculator(BlockEntityInoculator.class, 243),
    genome_extractor(BlockEntityGenomeExtractor.class, 244),
    genetic_transposer(BlockEntityGeneticTransposer.class, 245),
    genetic_polymerizer(BlockEntityGeneticPolymerizer.class, 246),
    shield(BlockEntityShield.class, 247),
    hologram_space(TileEntityHologramSpace.class, 248),
    upgrade_rover(BlockEntityUpgradeRover.class, 249),
    single_multi_crop(BlockEntitySingleMultiCrop.class, 250),
    dual_multi_crop(BlockEntityDualMultiCrop.class, 251),
    triple_multi_crop(BlockEntityTripleMultiCrop.class, 252),
    quad_multi_crop(BlockEntityQuadMultiCrop.class, 253),
    night_transformer(BlockEntityNightTransformer.class, 254),
    night_converter(BlockEntityNightConverter.class, 255),
    rover_assembler(BlockEntityRoverAssembler.class, 256),
    steam_fluid_heater(BlockEntitySteamFluidHeater.class, 257, EnumTypeCasing.STEAM),
    gen_bio(BlockEntityBioFuelGenerator.class, 258),
    ampere_storage(BlockEntityAmpereStorage.class, 259),
    bio_generator(BlockEntityBioGenerator.class, 260),
    steam_generator(BlockEntitySteamGenerator.class, 261),
    auto_latex_collector(BlockEntityAutoLatexCollector.class, 262),
    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    private final EnumTypeCasing rarity;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BlockEntityBase>> blockType;

    ;

    BlockBaseMachine3Entity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumTypeCasing.DEFAULT);

    }

    BlockBaseMachine3Entity(final Class<? extends BlockEntityBase> teClass, final int itemMeta, final EnumTypeCasing rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;


    }

    ;

    public int getIDBlock() {
        return idBlock;
    }

    public void setIdBlock(int id) {
        idBlock = id;
    }

    public void buildDummies() {
        final ModContainer mc = IUCore.instance.modContainer;
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        if (this.getTeClass() != null) {
            for (Constructor<?> constructor : this.teClass.getConstructors()) {
                try {

                    this.dummyTe = (BlockEntityBase) constructor.newInstance(BlockPos.ZERO, defaultState);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (dummyTe != null)
                    break;
            }
        }
    }

    @Override
    public void setDefaultState(BlockState blockState) {
        this.defaultState = blockState;
    }

    @Override
    public void setType(DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BlockEntityBase>> blockEntityType) {
        this.blockType = blockEntityType;
    }

    @Override
    public BlockEntityType<? extends BlockEntityBase> getBlockType() {
        return this.blockType.get();
    }

    @Override
    public String getMainPath() {
        return "basemachine3";
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public int getId() {
        return this.itemMeta;
    }


    @Override
    public boolean hasItem() {
        return true;
    }

    @Override
    public Class<? extends BlockEntityBase> getTeClass() {
        return this.teClass;
    }

    @Override
    public boolean hasActive() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    @Nonnull
    public Set<Direction> getSupportedFacings() {
        return ModUtils.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 1.0F;
    }

    @Override
    @Nonnull
    public HarvestTool getHarvestTool() {
        if (this == BlockBaseMachine3Entity.oak_tank) {
            return HarvestTool.Axe;
        }
        if (this == BlockBaseMachine3Entity.steel_tank
                || this == BlockBaseMachine3Entity.quarry_pipe || this == BlockBaseMachine3Entity.auto_latex_collector
        ) {
            return HarvestTool.Pickaxe;
        }
        if (rarity == EnumTypeCasing.BIO || rarity == EnumTypeCasing.STEAM) {
            return HarvestTool.Pickaxe;
        }
        return this != BlockBaseMachine3Entity.rolling_machine ? HarvestTool.Wrench : HarvestTool.Pickaxe;
    }

    @Override
    @Nonnull
    public DefaultDrop getDefaultDrop() {
        if (this == oak_tank || this == steel_tank
                || this == quarry_pipe || this == BlockBaseMachine3Entity.auto_latex_collector
        ) {
            return DefaultDrop.Self;
        }
        if (this == item_trash || this == fluid_trash
                || this == energy_trash
        ) {
            return DefaultDrop.Self;
        }
        if (rarity == EnumTypeCasing.BIO || rarity == EnumTypeCasing.STEAM) {
            return DefaultDrop.Self;
        }
        return this != BlockBaseMachine3Entity.rolling_machine ? DefaultDrop.Machine :
                DefaultDrop.Self;
    }

    @Override
    public boolean allowWrenchRotating() {
        return this != BlockBaseMachine3Entity.rolling_machine && this.rarity != EnumTypeCasing.BIO && this.rarity != EnumTypeCasing.STEAM;
    }


    @Override
    public BlockEntityBase getDummyTe() {
        return this.dummyTe;
    }

    @Override
    public boolean hasUniqueRender(final ItemStack itemStack) {
        return false;
    }

}
