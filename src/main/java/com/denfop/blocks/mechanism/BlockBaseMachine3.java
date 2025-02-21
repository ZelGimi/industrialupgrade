package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.item.IMultiBlockItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.mechanism.TileEntityNightConverter;
import com.denfop.tiles.mechanism.TileEntityNightTransformer;
import com.denfop.tiles.base.TileAntiUpgradeBlock;
import com.denfop.tiles.base.TileAutoSpawner;
import com.denfop.tiles.base.TileEntityAntiMagnet;
import com.denfop.tiles.base.TileEntityAutoDigger;
import com.denfop.tiles.base.TileEntityAutomaticMechanism;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.base.TileEntityCombinerSEGenerators;
import com.denfop.tiles.base.TileEntityRefrigeratorFluids;
import com.denfop.tiles.base.TileEntitySimulatorReactor;
import com.denfop.tiles.base.TileEntityTeleporter;
import com.denfop.tiles.base.TileEntityTesseract;
import com.denfop.tiles.base.TileEntityWirelessGraphiteController;
import com.denfop.tiles.base.TileEntityWirelessHeatController;
import com.denfop.tiles.base.TileEntityWirelessMatterCollector;
import com.denfop.tiles.base.TileLimiter;
import com.denfop.tiles.base.TileQuantumMolecular;
import com.denfop.tiles.base.TileRadiationPurifier;
import com.denfop.tiles.crop.TileEntityDualMultiCrop;
import com.denfop.tiles.crop.TileEntityQuadMultiCrop;
import com.denfop.tiles.crop.TileEntitySingleMultiCrop;
import com.denfop.tiles.crop.TileEntityTripleMultiCrop;
import com.denfop.tiles.mechanism.TileAdvHandlerHeavyOre;
import com.denfop.tiles.mechanism.TileAdvScanner;
import com.denfop.tiles.mechanism.TileCanner;
import com.denfop.tiles.mechanism.TileCrystalCharger;
import com.denfop.tiles.mechanism.TileEntityAlkalineEarthQuarry;
import com.denfop.tiles.mechanism.TileEntityAmpereGenerator;
import com.denfop.tiles.mechanism.TileEntityAnalyzerChest;
import com.denfop.tiles.mechanism.TileEntityApothecaryBee;
import com.denfop.tiles.mechanism.TileEntityAutoCrafter;
import com.denfop.tiles.mechanism.TileEntityAutoFuse;
import com.denfop.tiles.mechanism.TileEntityAutoOpenBox;
import com.denfop.tiles.mechanism.TileEntityBatteryFactory;
import com.denfop.tiles.mechanism.TileEntityBrewingPlant;
import com.denfop.tiles.mechanism.TileEntityCactusFarm;
import com.denfop.tiles.mechanism.TileEntityCentrifuge;
import com.denfop.tiles.mechanism.TileEntityChargepadExperience;
import com.denfop.tiles.mechanism.TileEntityChickenFarm;
import com.denfop.tiles.mechanism.TileEntityCollectorProductBee;
import com.denfop.tiles.mechanism.TileEntityCowFarm;
import com.denfop.tiles.mechanism.TileEntityElectricDryer;
import com.denfop.tiles.mechanism.TileEntityElectricRefractoryFurnace;
import com.denfop.tiles.mechanism.TileEntityElectricSqueezer;
import com.denfop.tiles.mechanism.TileEntityElectronicsAssembler;
import com.denfop.tiles.mechanism.TileEntityEnchanterBooks;
import com.denfop.tiles.mechanism.TileEntityEnergyBarrier;
import com.denfop.tiles.mechanism.TileEntityEnergyTrash;
import com.denfop.tiles.mechanism.TileEntityFacadeBlock;
import com.denfop.tiles.mechanism.TileEntityFieldCleaner;
import com.denfop.tiles.mechanism.TileEntityFluidAdapter;
import com.denfop.tiles.mechanism.TileEntityFluidHeater;
import com.denfop.tiles.mechanism.TileEntityFluidIntegrator;
import com.denfop.tiles.mechanism.TileEntityFluidMixer;
import com.denfop.tiles.mechanism.TileEntityFluidSeparator;
import com.denfop.tiles.mechanism.TileEntityFluidTrash;
import com.denfop.tiles.mechanism.TileEntityGasCombiner;
import com.denfop.tiles.mechanism.TileEntityGeneticPolymerizer;
import com.denfop.tiles.mechanism.TileEntityGeneticReplicator;
import com.denfop.tiles.mechanism.TileEntityGeneticStabilize;
import com.denfop.tiles.mechanism.TileEntityGeneticTransposer;
import com.denfop.tiles.mechanism.TileEntityGenomeExtractor;
import com.denfop.tiles.mechanism.TileEntityGraphiteHandler;
import com.denfop.tiles.mechanism.TileEntityHologramSpace;
import com.denfop.tiles.mechanism.TileEntityImpAlloySmelter;
import com.denfop.tiles.mechanism.TileEntityIncubator;
import com.denfop.tiles.mechanism.TileEntityIndustrialOrePurifier;
import com.denfop.tiles.mechanism.TileEntityInoculator;
import com.denfop.tiles.mechanism.TileEntityInsulator;
import com.denfop.tiles.mechanism.TileEntityItemDivider;
import com.denfop.tiles.mechanism.TileEntityItemDividerFluids;
import com.denfop.tiles.mechanism.TileEntityItemManipulator;
import com.denfop.tiles.mechanism.TileEntityItemTrash;
import com.denfop.tiles.mechanism.TileEntityLaserPolisher;
import com.denfop.tiles.mechanism.TileEntityMachineCharge;
import com.denfop.tiles.mechanism.TileEntityMatterFactory;
import com.denfop.tiles.mechanism.TileEntityMobMagnet;
import com.denfop.tiles.mechanism.TileEntityMoonSpotter;
import com.denfop.tiles.mechanism.TileEntityMutatron;
import com.denfop.tiles.mechanism.TileEntityNeutronSeparator;
import com.denfop.tiles.mechanism.TileEntityNuclearWasteRecycler;
import com.denfop.tiles.mechanism.TileEntityOilPurifier;
import com.denfop.tiles.mechanism.TileEntityPalletGenerator;
import com.denfop.tiles.mechanism.TileEntityPerAlloySmelter;
import com.denfop.tiles.mechanism.TileEntityPigFarm;
import com.denfop.tiles.mechanism.TileEntityPlantCollector;
import com.denfop.tiles.mechanism.TileEntityPlantFertilizer;
import com.denfop.tiles.mechanism.TileEntityPlantGardener;
import com.denfop.tiles.mechanism.TileEntityPolymerizer;
import com.denfop.tiles.mechanism.TileEntityPositronConverter;
import com.denfop.tiles.mechanism.TileEntityProbeAssembler;
import com.denfop.tiles.mechanism.TileEntityProgrammingTable;
import com.denfop.tiles.mechanism.TileEntityPurifierSoil;
import com.denfop.tiles.mechanism.TileEntityQuantumMiner;
import com.denfop.tiles.mechanism.TileEntityRNACollector;
import com.denfop.tiles.mechanism.TileEntityRadioactiveOreHandler;
import com.denfop.tiles.mechanism.TileEntityReactorSafetyDoom;
import com.denfop.tiles.mechanism.TileEntityRecipeTuner;
import com.denfop.tiles.mechanism.TileEntityRefrigeratorCoolant;
import com.denfop.tiles.mechanism.TileEntityResearchTableSpace;
import com.denfop.tiles.mechanism.TileEntityReverseTransriptor;
import com.denfop.tiles.mechanism.TileEntityRocketAssembler;
import com.denfop.tiles.mechanism.TileEntityRocketLaunchPad;
import com.denfop.tiles.mechanism.TileEntityRodFactory;
import com.denfop.tiles.mechanism.TileEntityRodManufacturer;
import com.denfop.tiles.mechanism.TileEntityRollingMachine;
import com.denfop.tiles.mechanism.TileEntityRotorAssembler;
import com.denfop.tiles.mechanism.TileEntityRotorModifier;
import com.denfop.tiles.mechanism.TileEntityRoverAssembler;
import com.denfop.tiles.mechanism.TileEntitySafe;
import com.denfop.tiles.mechanism.TileEntitySaplingGardener;
import com.denfop.tiles.mechanism.TileEntitySatelliteAssembler;
import com.denfop.tiles.mechanism.TileEntitySawmill;
import com.denfop.tiles.mechanism.TileEntitySheepFarm;
import com.denfop.tiles.mechanism.TileEntityShield;
import com.denfop.tiles.mechanism.TileEntitySiliconCrystalHandler;
import com.denfop.tiles.mechanism.TileEntitySingleFluidAdapter;
import com.denfop.tiles.mechanism.TileEntitySocketFactory;
import com.denfop.tiles.mechanism.TileEntitySoilAnalyzer;
import com.denfop.tiles.mechanism.TileEntitySolidFluidIntegrator;
import com.denfop.tiles.mechanism.TileEntitySolidFluidMixer;
import com.denfop.tiles.mechanism.TileEntitySolidMixer;
import com.denfop.tiles.mechanism.TileEntitySolidStateElectrolyzer;
import com.denfop.tiles.mechanism.TileEntityStampMechanism;
import com.denfop.tiles.mechanism.TileEntityTreeBreaker;
import com.denfop.tiles.mechanism.TileEntityTripleSolidMixer;
import com.denfop.tiles.mechanism.TileEntityUpgradeMachineFactory;
import com.denfop.tiles.mechanism.TileEntityUpgradeRover;
import com.denfop.tiles.mechanism.TileEntityWaterRotorAssembler;
import com.denfop.tiles.mechanism.TileEntityWaterRotorModifier;
import com.denfop.tiles.mechanism.TileEntityWeeder;
import com.denfop.tiles.mechanism.TileEntityWirelessControllerReactors;
import com.denfop.tiles.mechanism.TileEntityWirelessGasPump;
import com.denfop.tiles.mechanism.TileEntityWirelessMineralQuarry;
import com.denfop.tiles.mechanism.TileEntityWirelessOilPump;
import com.denfop.tiles.mechanism.TileGasPump;
import com.denfop.tiles.mechanism.TileGenerationAdditionStone;
import com.denfop.tiles.mechanism.TileImpHandlerHeavyOre;
import com.denfop.tiles.mechanism.TileImpOilRefiner;
import com.denfop.tiles.mechanism.TileImpScanner;
import com.denfop.tiles.mechanism.TilePatternStorage;
import com.denfop.tiles.mechanism.TilePerHandlerHeavyOre;
import com.denfop.tiles.mechanism.TilePerScanner;
import com.denfop.tiles.mechanism.TilePrivatizer;
import com.denfop.tiles.mechanism.TileSimplePump;
import com.denfop.tiles.mechanism.TileSimpleScanner;
import com.denfop.tiles.mechanism.TileSolidCooling;
import com.denfop.tiles.mechanism.TileTunerWireless;
import com.denfop.tiles.mechanism.TileWireInsulator;
import com.denfop.tiles.mechanism.bio.TileBioCentrifuge;
import com.denfop.tiles.mechanism.bio.TileBioCompressor;
import com.denfop.tiles.mechanism.bio.TileBioCutting;
import com.denfop.tiles.mechanism.bio.TileBioExtractor;
import com.denfop.tiles.mechanism.bio.TileBioFurnace;
import com.denfop.tiles.mechanism.bio.TileBioGearing;
import com.denfop.tiles.mechanism.bio.TileBioMacerator;
import com.denfop.tiles.mechanism.bio.TileBioOreWashing;
import com.denfop.tiles.mechanism.bio.TileBioRolling;
import com.denfop.tiles.mechanism.combpump.TileEntityAdvCombPump;
import com.denfop.tiles.mechanism.combpump.TileEntityImpCombPump;
import com.denfop.tiles.mechanism.combpump.TileEntityPerCombPump;
import com.denfop.tiles.mechanism.combpump.TileEntitySimpleCombinedPump;
import com.denfop.tiles.mechanism.cooling.TileCooling;
import com.denfop.tiles.mechanism.cooling.TileFluidCooling;
import com.denfop.tiles.mechanism.dual.heat.TileWeldingMachine;
import com.denfop.tiles.mechanism.energy.TileEnergyController;
import com.denfop.tiles.mechanism.energy.TileEnergyRemover;
import com.denfop.tiles.mechanism.energy.TileEnergySubstitute;
import com.denfop.tiles.mechanism.generator.energy.TileEntitySolarGenerator;
import com.denfop.tiles.mechanism.generator.energy.TilePeatGenerator;
import com.denfop.tiles.mechanism.generator.energy.coal.TileEntityGenerator;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileEntityGeoGenerator;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileGasGenerator;
import com.denfop.tiles.mechanism.generator.energy.redstone.TileAdvRedstoneGenerator;
import com.denfop.tiles.mechanism.generator.energy.redstone.TileImpRedstoneGenerator;
import com.denfop.tiles.mechanism.generator.energy.redstone.TilePerRedstoneGenerator;
import com.denfop.tiles.mechanism.generator.energy.redstone.TileRedstoneGenerator;
import com.denfop.tiles.mechanism.generator.things.fluid.TileAirCollector;
import com.denfop.tiles.mechanism.generator.things.fluid.TileWaterGenerator;
import com.denfop.tiles.mechanism.heat.TileElectricHeat;
import com.denfop.tiles.mechanism.heat.TileFluidHeat;
import com.denfop.tiles.mechanism.quantum_storage.TileEntityAdvQuantumStorage;
import com.denfop.tiles.mechanism.quantum_storage.TileEntityImpQuantumStorage;
import com.denfop.tiles.mechanism.quantum_storage.TileEntityPerQuantumStorage;
import com.denfop.tiles.mechanism.quantum_storage.TileEntitySimpleQuantumStorage;
import com.denfop.tiles.mechanism.radiation_storage.TileEntitySimpleRadiationStorage;
import com.denfop.tiles.mechanism.replicator.TileAdvReplicator;
import com.denfop.tiles.mechanism.replicator.TileImpReplicator;
import com.denfop.tiles.mechanism.replicator.TilePerReplicator;
import com.denfop.tiles.mechanism.replicator.TileReplicator;
import com.denfop.tiles.mechanism.solardestiller.TileEntityAdvSolarDestiller;
import com.denfop.tiles.mechanism.solardestiller.TileEntityImpSolarDestiller;
import com.denfop.tiles.mechanism.solardestiller.TileEntityPerSolarDestiller;
import com.denfop.tiles.mechanism.solardestiller.TileEntitySolarDestiller;
import com.denfop.tiles.mechanism.solarium_storage.TileEntityAdvSolariumStorage;
import com.denfop.tiles.mechanism.solarium_storage.TileEntityImpSolariumStorage;
import com.denfop.tiles.mechanism.solarium_storage.TileEntityPerSolariumStorage;
import com.denfop.tiles.mechanism.solarium_storage.TileEntitySimpleSolariumStorage;
import com.denfop.tiles.mechanism.steam.TileAdvSteamQuarry;
import com.denfop.tiles.mechanism.steam.TileBioExtruder;
import com.denfop.tiles.mechanism.steam.TileEntitySteamAmpereGenerator;
import com.denfop.tiles.mechanism.steam.TileEntitySteamBioGenerator;
import com.denfop.tiles.mechanism.steam.TileEntitySteamBoiler;
import com.denfop.tiles.mechanism.steam.TileEntitySteamConverter;
import com.denfop.tiles.mechanism.steam.TileEntitySteamDryer;
import com.denfop.tiles.mechanism.steam.TileEntitySteamFluidHeater;
import com.denfop.tiles.mechanism.steam.TileEntitySteamPressureConverter;
import com.denfop.tiles.mechanism.steam.TileEntitySteamSolidFluidMixer;
import com.denfop.tiles.mechanism.steam.TileEntitySteamSqueezer;
import com.denfop.tiles.mechanism.steam.TileQuarryPipe;
import com.denfop.tiles.mechanism.steam.TileSteamCompressor;
import com.denfop.tiles.mechanism.steam.TileSteamCrystalCharge;
import com.denfop.tiles.mechanism.steam.TileSteamCutting;
import com.denfop.tiles.mechanism.steam.TileSteamElectrolyzer;
import com.denfop.tiles.mechanism.steam.TileSteamExtractor;
import com.denfop.tiles.mechanism.steam.TileSteamExtruder;
import com.denfop.tiles.mechanism.steam.TileSteamHandlerHeavyOre;
import com.denfop.tiles.mechanism.steam.TileSteamMacerator;
import com.denfop.tiles.mechanism.steam.TileSteamPeatGenerator;
import com.denfop.tiles.mechanism.steam.TileSteamPump;
import com.denfop.tiles.mechanism.steam.TileSteamQuarry;
import com.denfop.tiles.mechanism.steam.TileSteamRolling;
import com.denfop.tiles.mechanism.steam.TileSteamSharpener;
import com.denfop.tiles.mechanism.steam.TileSteamStorage;
import com.denfop.tiles.mechanism.steam.TileSteamWireInsulator;
import com.denfop.tiles.mechanism.vending.TileEntityAdvVending;
import com.denfop.tiles.mechanism.vending.TileEntityImpVending;
import com.denfop.tiles.mechanism.vending.TileEntityPerVending;
import com.denfop.tiles.mechanism.vending.TileEntityVending;
import com.denfop.tiles.mechanism.water.TileAdvWaterGenerator;
import com.denfop.tiles.mechanism.water.TileImpWaterGenerator;
import com.denfop.tiles.mechanism.water.TilePerWaterGenerator;
import com.denfop.tiles.mechanism.water.TileSimpleWaterGenerator;
import com.denfop.tiles.mechanism.wind.TileAdvWindGenerator;
import com.denfop.tiles.mechanism.wind.TileImpWindGenerator;
import com.denfop.tiles.mechanism.wind.TilePerWindGenerator;
import com.denfop.tiles.mechanism.wind.TileSimpleWindGenerator;
import com.denfop.tiles.mechanism.worlcollector.TileAerAssembler;
import com.denfop.tiles.mechanism.worlcollector.TileAquaAssembler;
import com.denfop.tiles.mechanism.worlcollector.TileCrystallize;
import com.denfop.tiles.mechanism.worlcollector.TileEarthAssembler;
import com.denfop.tiles.mechanism.worlcollector.TileEnderAssembler;
import com.denfop.tiles.mechanism.worlcollector.TileNetherAssembler;
import com.denfop.tiles.panels.entity.TileEntityMiniPanels;
import com.denfop.tiles.tank.TileEntityOakTank;
import com.denfop.tiles.tank.TileEntitySteelTank;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockBaseMachine3 implements IMultiTileBlock, IMultiBlockItem {
    radiation_purifier(TileRadiationPurifier.class, 1),
    privatizer(TilePrivatizer.class, 2),
    tuner(TileTunerWireless.class, 3),
    spawner(TileAutoSpawner.class, 4),
    elec_heat(TileElectricHeat.class, 5),
    fluid_heat(TileFluidHeat.class, 6),
    antimagnet(TileEntityAntiMagnet.class, 7),
    antiupgradeblock(TileAntiUpgradeBlock.class, 8),
    watergenerator(TileWaterGenerator.class, 9),
    cooling(TileCooling.class, 10),
    aircollector(TileAirCollector.class, 11),
    combiner_se_generators(TileEntityCombinerSEGenerators.class, 12),
    probe_assembler(TileEntityProbeAssembler.class, 13),
    research_table_space(TileEntityResearchTableSpace.class, 14),
    rocket_assembler(TileEntityRocketAssembler.class, 15),
    rocket_launch_pad(TileEntityRocketLaunchPad.class, 16),
    rotor_assembler(TileEntityRotorAssembler.class, 17),
    rotor_modifier(TileEntityRotorModifier.class, 18),
    satellite_assembler(TileEntitySatelliteAssembler.class, 19),
    soil_analyzer(TileEntitySoilAnalyzer.class, 20),
    rods_manufacturer(TileEntityRodManufacturer.class, 21),
    ender_assembler(TileEnderAssembler.class, 23),
    gas_combiner(TileEntityGasCombiner.class, 26),
    analyzer_chest(TileEntityAnalyzerChest.class, 27),
    simple_wind_generator(TileSimpleWindGenerator.class, 28),
    adv_wind_generator(TileAdvWindGenerator.class, 29),
    imp_wind_generator(TileImpWindGenerator.class, 30),
    per_wind_generator(TilePerWindGenerator.class, 31),
    energy_controller(TileEnergyController.class, 32),
    substitute(TileEnergySubstitute.class, 33),

    aer_assembler(TileAerAssembler.class, 34),
    aqua_assembler(TileAquaAssembler.class, 35),
    earth_assembler(TileEarthAssembler.class, 36),
    nether_assembler(TileNetherAssembler.class, 37),
    auto_digger(TileEntityAutoDigger.class, 38),
    crystallize(TileCrystallize.class, 39),
    double_handlerho(TileAdvHandlerHeavyOre.class, 40),
    triple_handlerho(TileImpHandlerHeavyOre.class, 41),
    quad_handlerho(TilePerHandlerHeavyOre.class, 42),
    water_modifier(TileEntityWaterRotorModifier.class, 43),
    water_rotor_assembler(TileEntityWaterRotorAssembler.class, 44),
    simple_water_generator(TileSimpleWaterGenerator.class, 45),
    adv_water_generator(TileAdvWaterGenerator.class, 46),
    imp_water_generator(TileImpWaterGenerator.class, 47),
    per_water_generator(TilePerWaterGenerator.class, 48),
    scanner_iu(TileSimpleScanner.class, 49),
    adv_scanner(TileAdvScanner.class, 50),
    imp_scanner(TileImpScanner.class, 51),
    per_scanner(TilePerScanner.class, 52),
    replicator_iu(TileReplicator.class, 53),
    adv_replicator(TileAdvReplicator.class, 54),
    imp_replicator(TileImpReplicator.class, 55),
    per_replicator(TilePerReplicator.class, 56),

    solarium_storage(TileEntitySimpleSolariumStorage.class, 57),
    adv_solarium_storage(TileEntityAdvSolariumStorage.class, 58),
    imp_solarium_storage(TileEntityImpSolariumStorage.class, 59),
    per_solarium_storage(TileEntityPerSolariumStorage.class, 60),
    quantum_storage(TileEntitySimpleQuantumStorage.class, 61),
    adv_quantum_storage(TileEntityAdvQuantumStorage.class, 62),
    imp_quantum_storage(TileEntityImpQuantumStorage.class, 63),
    per_quantum_storage(TileEntityPerQuantumStorage.class, 64),
    solardestiller(TileEntitySolarDestiller.class, 65),
    adv_solar_destiller(TileEntityAdvSolarDestiller.class, 66),
    imp_solar_destiller(TileEntityImpSolarDestiller.class, 67),
    per_solar_destiller(TileEntityPerSolarDestiller.class, 68),
    limiter(TileLimiter.class, 69),
    redstone_generator(TileRedstoneGenerator.class, 70),
    adv_redstone_generator(TileAdvRedstoneGenerator.class, 71),
    imp_redstone_generator(TileImpRedstoneGenerator.class, 72),
    per_redstone_generator(TilePerRedstoneGenerator.class, 73),
    welding(TileWeldingMachine.class, 74),
    gas_generator(TileGasGenerator.class, 76),
    pattern_storage_iu(TilePatternStorage.class, 77),
    generator_iu(TileEntityGenerator.class, 78),
    geogenerator_iu(TileEntityGeoGenerator.class, 79),
    pump_iu(TileSimplePump.class, 80),
    solar_iu(TileEntitySolarGenerator.class, 81),
    teleporter_iu(TileEntityTeleporter.class, 84),
    canner_iu(TileCanner.class, 89),

    minipanel(TileEntityMiniPanels.class, 91),
    facademechanism(TileEntityFacadeBlock.class, 92),
    battery_factory(TileEntityBatteryFactory.class, 93),
    socket_factory(TileEntitySocketFactory.class, 94),
    matter_factory(TileEntityMatterFactory.class, 95),
    laser_polisher(TileEntityLaserPolisher.class, 96),
    graphite_handler(TileEntityGraphiteHandler.class, 97),
    moon_spotter(TileEntityMoonSpotter.class, 98),
    stamp_mechanism(TileEntityStampMechanism.class, 99),
    reactor_rod_factory(TileEntityRodFactory.class, 100),
    nuclear_waste_recycler(TileEntityNuclearWasteRecycler.class, 101),
    reactor_safety_doom(TileEntityReactorSafetyDoom.class, 102),
    pallet_generator(TileEntityPalletGenerator.class, 103),
    enchanter_books(TileEntityEnchanterBooks.class, 104),
    refrigerator_coolant(TileEntityRefrigeratorCoolant.class, 105),
    autocrafter(TileEntityAutoCrafter.class, 106),
    tesseract(TileEntityTesseract.class, 107),
    comb_pump(TileEntitySimpleCombinedPump.class, 108),
    adv_comb_pump(TileEntityAdvCombPump.class, 109),
    imp_comb_pump(TileEntityImpCombPump.class, 110),
    per_comb_pump(TileEntityPerCombPump.class, 111),
    safe(TileEntitySafe.class, 112),
    vending(TileEntityVending.class, 113),
    adv_vending(TileEntityAdvVending.class, 114),
    imp_vending(TileEntityImpVending.class, 115),
    per_vending(TileEntityPerVending.class, 116),
    autofuse(TileEntityAutoFuse.class, 117),
    auto_open_box(TileEntityAutoOpenBox.class, 118),
    exp_chargepad(TileEntityChargepadExperience.class, 119),
    recipe_tuner(TileEntityRecipeTuner.class, 120),


    energy_trash(TileEntityEnergyTrash.class, 121),
    fluid_trash(TileEntityFluidTrash.class, 122),
    item_trash(TileEntityItemTrash.class, 123),
    rolling_machine(TileEntityRollingMachine.class, 124),
    matter_collector(TileEntityWirelessMatterCollector.class, 125),

    machine_charger(TileEntityMachineCharge.class, 126),
    automatic_mechanism(TileEntityAutomaticMechanism.class, 127),
    wireless_oil_pump(TileEntityWirelessOilPump.class, 128),
    wireless_mineral_quarry(TileEntityWirelessMineralQuarry.class, 129),
    wireless_controller_reactors(TileEntityWirelessControllerReactors.class, 130),
    simulation_reactors(TileEntitySimulatorReactor.class, 131),
    radiation_storage(TileEntitySimpleRadiationStorage.class, 132),
    wireless_controller_graphite_reactors(TileEntityWirelessGraphiteController.class, 133),
    wireless_controller_heat_reactors(TileEntityWirelessHeatController.class, 134),
    energy_remover(TileEnergyRemover.class, 135),
    energy_barrier(TileEntityEnergyBarrier.class, 136),
    mob_magnet(TileEntityMobMagnet.class, 137),
    upgrade_machine(TileEntityUpgradeMachineFactory.class, 138),
    imp_refiner(TileImpOilRefiner.class, 139),
    refrigerator_fluids(TileEntityRefrigeratorFluids.class, 140),
    item_divider(TileEntityItemDivider.class, 141),
    item_divider_to_fluid(TileEntityItemDividerFluids.class, 142),
    fluid_separator(TileEntityFluidSeparator.class, 143),
    polymerizer(TileEntityPolymerizer.class, 144),
    fluid_adapter(TileEntityFluidAdapter.class, 145),
    fluid_integrator(TileEntityFluidIntegrator.class, 146),
    solid_state_electrolyzer(TileEntitySolidStateElectrolyzer.class, 147),
    oil_purifier(TileEntityOilPurifier.class, 148),

    purifier_soil(TileEntityPurifierSoil.class, 149),
    electric_dryer(TileEntityElectricDryer.class, 150),
    electric_squeezer(TileEntityElectricSqueezer.class, 151),
    fluid_cooling(TileFluidCooling.class, 152),
    solid_cooling(TileSolidCooling.class, 153),
    imp_alloy_smelter(TileEntityImpAlloySmelter.class, 154),
    per_alloy_smelter(TileEntityPerAlloySmelter.class, 155),
    fluid_mixer(TileEntityFluidMixer.class, 156),
    solid_fluid_mixer(TileEntitySolidFluidMixer.class, 157),
    fluid_heater(TileEntityFluidHeater.class, 158),
    silicon_crystal_handler(TileEntitySiliconCrystalHandler.class, 159),
    neutronseparator(TileEntityNeutronSeparator.class, 160),
    positronconverter(TileEntityPositronConverter.class, 161),
    itemmanipulator(TileEntityItemManipulator.class, 162),
    steamboiler(TileEntitySteamBoiler.class, 163,EnumTypeCasing.STEAM),
    steampressureconverter(TileEntitySteamPressureConverter.class, 164,EnumTypeCasing.STEAM),
    steamdryer(TileEntitySteamDryer.class, 165,EnumTypeCasing.STEAM),
    steam_macerator(TileSteamMacerator.class, 166,EnumTypeCasing.STEAM),
    steam_compressor(TileSteamCompressor.class, 167,EnumTypeCasing.STEAM),
    steam_extractor(TileSteamExtractor.class, 168,EnumTypeCasing.STEAM),
    steam_extruder(TileSteamExtruder.class, 169,EnumTypeCasing.STEAM),
    steam_cutting(TileSteamCutting.class, 170,EnumTypeCasing.STEAM),
    steam_converter(TileEntitySteamConverter.class, 171,EnumTypeCasing.STEAM),
    steam_squeezer(TileEntitySteamSqueezer.class, 172,EnumTypeCasing.STEAM),
    solid_fluid_integrator(TileEntitySolidFluidIntegrator.class, 173),
    solid_mixer(TileEntitySolidMixer.class, 174),
    peat_generator(TilePeatGenerator.class, 175),
    triple_solid_mixer(TileEntityTripleSolidMixer.class, 176),
    single_fluid_adapter(TileEntitySingleFluidAdapter.class, 177),
    steam_peat_generator(TileSteamPeatGenerator.class, 178,EnumTypeCasing.STEAM),
    steam_handler_ore(TileSteamHandlerHeavyOre.class, 179,EnumTypeCasing.STEAM),
    steam_electrolyzer(TileSteamElectrolyzer.class, 180,EnumTypeCasing.STEAM),
    steam_ampere_generator(TileEntitySteamAmpereGenerator.class, 181,EnumTypeCasing.STEAM),
    steam_solid_fluid_mixer(TileEntitySteamSolidFluidMixer.class, 182,EnumTypeCasing.STEAM),
    programming_table(TileEntityProgrammingTable.class, 183),
    electronic_assembler(TileEntityElectronicsAssembler.class, 184),
    oak_tank(TileEntityOakTank.class, 185),
    steel_tank(TileEntitySteelTank.class, 186),
    steam_pump(TileSteamPump.class, 187,EnumTypeCasing.STEAM),
    steam_sharpener(TileSteamSharpener.class, 188,EnumTypeCasing.STEAM),
    alkalineearthquarry(TileEntityAlkalineEarthQuarry.class, 189),
    radioactive_handler_ore(TileEntityRadioactiveOreHandler.class, 190),
    industrial_ore_purifier(TileEntityIndustrialOrePurifier.class, 191),
    gas_pump(TileGasPump.class, 192),
    wireless_gas_pump(TileEntityWirelessGasPump.class, 193),
    quantum_miner(TileEntityQuantumMiner.class, 194),
    quantum_transformer(TileQuantumMolecular.class, 195),
    steam_storage(TileSteamStorage.class, 196,EnumTypeCasing.STEAM),
    quarry_pipe(TileQuarryPipe.class, 197),
    steam_quarry(TileSteamQuarry.class, 198,EnumTypeCasing.STEAM),
    adv_steam_quarry(TileAdvSteamQuarry.class, 199,EnumTypeCasing.STEAM),
    steam_rolling(TileSteamRolling.class, 200,EnumTypeCasing.STEAM),
    electric_wire_insulator(TileWireInsulator.class, 201),
    steam_wire_insulator(TileSteamWireInsulator.class, 202,EnumTypeCasing.STEAM),
    steam_crystal_charge(TileSteamCrystalCharge.class, 203,EnumTypeCasing.STEAM),
    crystal_charge(TileCrystalCharger.class, 204),
    ampere_generator(TileEntityAmpereGenerator.class, 205),
    steam_bio_generator(TileEntitySteamBioGenerator.class, 206,EnumTypeCasing.BIO),
    bio_macerator(TileBioMacerator.class, 207,EnumTypeCasing.BIO),
    bio_compressor(TileBioCompressor.class, 208,EnumTypeCasing.BIO),
    bio_extractor(TileBioExtractor.class, 209,EnumTypeCasing.BIO),
    bio_extruder(TileBioExtruder.class, 210,EnumTypeCasing.BIO),
    bio_cutting(TileBioCutting.class, 211,EnumTypeCasing.BIO),
    bio_furnace(TileBioFurnace.class, 212,EnumTypeCasing.BIO),
    bio_rolling(TileBioRolling.class, 213,EnumTypeCasing.BIO),
    bio_orewashing(TileBioOreWashing.class, 214,EnumTypeCasing.BIO),
    bio_centrifuge(TileBioCentrifuge.class, 215,EnumTypeCasing.BIO),
    bio_gearing(TileBioGearing.class, 216,EnumTypeCasing.BIO),
    electric_refractory_furnace(TileEntityElectricRefractoryFurnace.class, 217),
    chicken_farm(TileEntityChickenFarm.class, 218),
    sheep_farm(TileEntitySheepFarm.class, 219),
    cow_farm(TileEntityCowFarm.class, 220),
    sapling_gardener(TileEntitySaplingGardener.class, 221),
    tree_breaker(TileEntityTreeBreaker.class, 222),
    cactus_farm(TileEntityCactusFarm.class, 223),
    pig_farm(TileEntityPigFarm.class, 224),
    electric_brewing(TileEntityBrewingPlant.class, 225),
    sawmill(TileEntitySawmill.class, 226),
    gen_addition_stone(TileGenerationAdditionStone.class, 227),
    plant_fertilizer(TileEntityPlantFertilizer.class,228),
    plant_collector(TileEntityPlantCollector.class,229),
    plant_gardener(TileEntityPlantGardener.class,230),
    field_cleaner(TileEntityFieldCleaner.class,231),
    weeder(TileEntityWeeder.class,232),
    apothecary_bee(TileEntityApothecaryBee.class,233),
    collector_product_bee(TileEntityCollectorProductBee.class,234),
    incubator(TileEntityIncubator.class,235),
    insulator(TileEntityInsulator.class,236),
    rna_collector(TileEntityRNACollector.class,237),
    mutatron(TileEntityMutatron.class,238),
    reverse_transcriptor(TileEntityReverseTransriptor.class,239),
    genetic_stabilizer(TileEntityGeneticStabilize.class,240),
    centrifuge(TileEntityCentrifuge.class, 241),
    genetic_replicator(TileEntityGeneticReplicator.class,242),
    inoculator(TileEntityInoculator.class,243),
    genome_extractor(TileEntityGenomeExtractor.class,244),
    genetic_transposer(TileEntityGeneticTransposer.class,245),
    genetic_polymerizer(TileEntityGeneticPolymerizer.class,246),
    shield(TileEntityShield.class,247),
    hologram_space(TileEntityHologramSpace.class,248),
    upgrade_rover(TileEntityUpgradeRover.class,249),
    single_multi_crop(TileEntitySingleMultiCrop.class,250),
   dual_multi_crop(TileEntityDualMultiCrop.class,251),
   triple_multi_crop(TileEntityTripleMultiCrop.class,252),
    quad_multi_crop(TileEntityQuadMultiCrop.class,253),
    night_transformer(TileEntityNightTransformer.class,254),
    night_converter(TileEntityNightConverter.class,255),
    rover_assembler(TileEntityRoverAssembler.class, 256),
    steam_fluid_heater(TileEntitySteamFluidHeater.class, 257,EnumTypeCasing.STEAM),
    ;

    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("basemachine3");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumTypeCasing rarity;
    private TileEntityBlock dummyTe;


    BlockBaseMachine3(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumTypeCasing.DEFAULT);

    }

    int idBlock;
    public  int getIDBlock(){
        return idBlock;
    };

    public void setIdBlock(int id){
        idBlock = id;
    };
    BlockBaseMachine3(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumTypeCasing rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;
        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }

    public void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockBaseMachine3 block : values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception ignored) {


                }
            }
        }

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
    @Nonnull
    public ResourceLocation getIdentifier() {
        return IDENTITY;
    }

    @Override
    public boolean hasItem() {
        return true;
    }

    @Override
    public Class<? extends TileEntityBlock> getTeClass() {
        return this.teClass;
    }

    @Override
    public boolean hasActive() {
        return true;
    }

    @Override
    @Nonnull
    public Set<EnumFacing> getSupportedFacings() {
        return ModUtils.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 1.0f;
    }

    @Override
    @Nonnull
    public MultiTileBlock.HarvestTool getHarvestTool() {
        if (this == BlockBaseMachine3.oak_tank) {
            return MultiTileBlock.HarvestTool.Axe;
        }
        if (this == BlockBaseMachine3.steel_tank || this == BlockBaseMachine3.quarry_pipe) {
            return MultiTileBlock.HarvestTool.Pickaxe;
        }
        if (rarity == EnumTypeCasing.BIO || rarity == EnumTypeCasing.STEAM)
            return MultiTileBlock.HarvestTool.Pickaxe;
        return this != BlockBaseMachine3.rolling_machine ? MultiTileBlock.HarvestTool.Wrench : MultiTileBlock.HarvestTool.Pickaxe;
    }

    @Override
    @Nonnull
    public MultiTileBlock.DefaultDrop getDefaultDrop() {
        if (this == oak_tank || this == steel_tank || this == quarry_pipe) {
            return MultiTileBlock.DefaultDrop.Self;
        }
        if (rarity == EnumTypeCasing.BIO || rarity == EnumTypeCasing.STEAM)
            return MultiTileBlock.DefaultDrop.Self;
        return this != BlockBaseMachine3.rolling_machine ? MultiTileBlock.DefaultDrop.Machine :
                MultiTileBlock.DefaultDrop.Self;
    }

    @Override
    public boolean allowWrenchRotating() {
        return this != BlockBaseMachine3.rolling_machine && this != BlockBaseMachine3.oak_tank && this != BlockBaseMachine3.steel_tank && this.rarity != EnumTypeCasing.BIO && this.rarity != EnumTypeCasing.STEAM;
    }

    @Override
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }

    @Override
    public boolean hasUniqueRender(final ItemStack itemStack) {
        return false;
    }

    @Override
    public ModelResourceLocation getModelLocation(final ItemStack itemStack) {
        return null;
    }
}
