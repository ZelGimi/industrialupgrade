package com.denfop.integration.jei;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.api.multiblock.MultiBlockSystem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockAnvil;
import com.denfop.blocks.BlockStrongAnvil;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.mechanism.*;
import com.denfop.integration.jei.advalloysmelter.AdvAlloySmelterCategory;
import com.denfop.integration.jei.advalloysmelter.AdvAlloySmelterHandler;
import com.denfop.integration.jei.advrefiner.AdvRefinerCategory;
import com.denfop.integration.jei.advrefiner.AdvRefinerHandler;
import com.denfop.integration.jei.aircollector.AirColCategory;
import com.denfop.integration.jei.aircollector.AirColHandler;
import com.denfop.integration.jei.alkalineearthquarry.AlkalineEarthQuarryCategory;
import com.denfop.integration.jei.alkalineearthquarry.AlkalineEarthQuarryHandler;
import com.denfop.integration.jei.alloysmelter.AlloySmelterCategory;
import com.denfop.integration.jei.alloysmelter.AlloySmelterHandler;
import com.denfop.integration.jei.analyzer.AnalyzerCategory;
import com.denfop.integration.jei.analyzer.AnalyzerHandler;
import com.denfop.integration.jei.antiupgradeblock.AntiUpgradeBlockCategory;
import com.denfop.integration.jei.antiupgradeblock.AntiUpgradeBlockHandler;
import com.denfop.integration.jei.anvil.AnvilCategory;
import com.denfop.integration.jei.anvil.AnvilHandler;
import com.denfop.integration.jei.apiary.ApiaryCategory;
import com.denfop.integration.jei.apiary.ApiaryHandler;
import com.denfop.integration.jei.battery_factory.BatteryCategory;
import com.denfop.integration.jei.battery_factory.BatteryHandler;
import com.denfop.integration.jei.bee.BeeCategory;
import com.denfop.integration.jei.bee.BeeHandler;
import com.denfop.integration.jei.bee_centrifuge.CentrifugeCategory;
import com.denfop.integration.jei.bee_centrifuge.CentrifugeHandler;
import com.denfop.integration.jei.bf.BlastFCategory;
import com.denfop.integration.jei.bf.BlastFHandler;
import com.denfop.integration.jei.biomass.BiomassCategory;
import com.denfop.integration.jei.biomass.BiomassHandler;
import com.denfop.integration.jei.brewing.BrewingCategory;
import com.denfop.integration.jei.brewing.BrewingHandler;
import com.denfop.integration.jei.charged_redstone.ChargedRedstoneCategory;
import com.denfop.integration.jei.charged_redstone.ChargedRedstoneHandler;
import com.denfop.integration.jei.chemicalplant.ChemicalPlantCategory;
import com.denfop.integration.jei.chemicalplant.ChemicalPlantHandler;
import com.denfop.integration.jei.cokeoven.CokeOvenCategory;
import com.denfop.integration.jei.cokeoven.CokeOvenHandler;
import com.denfop.integration.jei.colonial_resource.SpaceColonyCategory;
import com.denfop.integration.jei.colonial_resource.SpaceColonyHandler;
import com.denfop.integration.jei.combmac.CombMacCategory;
import com.denfop.integration.jei.combmac.CombMacHandler;
import com.denfop.integration.jei.compressor.CompressorCategory;
import com.denfop.integration.jei.compressor.CompressorHandler;
import com.denfop.integration.jei.crops.CropCategory;
import com.denfop.integration.jei.crops.CropCrossoverCategory;
import com.denfop.integration.jei.crops.CropCrossoverHandler;
import com.denfop.integration.jei.crops.CropHandler;
import com.denfop.integration.jei.crystal_charge.CrystalChargerCategory;
import com.denfop.integration.jei.crystal_charge.CrystalChargerHandler;
import com.denfop.integration.jei.cutting.CuttingCategory;
import com.denfop.integration.jei.cutting.CuttingHandler;
import com.denfop.integration.jei.cyclotron.CyclotronCategory;
import com.denfop.integration.jei.cyclotron.CyclotronHandler;
import com.denfop.integration.jei.deposits.DepositsCategory;
import com.denfop.integration.jei.deposits.DepositsHandler;
import com.denfop.integration.jei.deposits_molot.DepositsMolotCategory;
import com.denfop.integration.jei.deposits_molot.DepositsMolotHandler;
import com.denfop.integration.jei.distiller.DistillerCategory;
import com.denfop.integration.jei.distiller.DistillerHandler;
import com.denfop.integration.jei.doublemolecular.DoubleMolecularTransformerCategory;
import com.denfop.integration.jei.doublemolecular.DoubleMolecularTransformerHandler;
import com.denfop.integration.jei.dryer.DryerCategory;
import com.denfop.integration.jei.dryer.DryerHandler;
import com.denfop.integration.jei.earthquarry.EarthQuarryCategory;
import com.denfop.integration.jei.earthquarry.EarthQuarryHandler;
import com.denfop.integration.jei.electrolyzer.ElectrolyzerCategory;
import com.denfop.integration.jei.electrolyzer.ElectrolyzerHandler;
import com.denfop.integration.jei.electronics.ElectronicsCategory;
import com.denfop.integration.jei.electronics.ElectronicsHandler;
import com.denfop.integration.jei.enchanter.EnchantCategory;
import com.denfop.integration.jei.enchanter.EnchantHandler;
import com.denfop.integration.jei.enrichment.EnrichCategory;
import com.denfop.integration.jei.enrichment.EnrichHandler;
import com.denfop.integration.jei.extractor.ExtractorCategory;
import com.denfop.integration.jei.extractor.ExtractorHandler;
import com.denfop.integration.jei.extruder.ExtruderCategory;
import com.denfop.integration.jei.extruder.ExtruderHandler;
import com.denfop.integration.jei.farmer.FarmerCategory;
import com.denfop.integration.jei.farmer.FarmerHandler;
import com.denfop.integration.jei.fluidadapter.FluidAdapterCategory;
import com.denfop.integration.jei.fluidadapter.FluidAdapterHandler;
import com.denfop.integration.jei.fluidheater.HeaterFluidsCategory;
import com.denfop.integration.jei.fluidheater.HeaterFluidsHandler;
import com.denfop.integration.jei.fluidintegrator.FluidIntegratorCategory;
import com.denfop.integration.jei.fluidintegrator.FluidIntegratorHandler;
import com.denfop.integration.jei.fluidmixer.FluidMixerCategory;
import com.denfop.integration.jei.fluidmixer.FluidMixerHandler;
import com.denfop.integration.jei.fluidseparator.FluidSeparatorCategory;
import com.denfop.integration.jei.fluidseparator.FluidSeparatorHandler;
import com.denfop.integration.jei.fluidsolidmixer.FluidSolidMixerCategory;
import com.denfop.integration.jei.fluidsolidmixer.FluidSolidMixerHandler;
import com.denfop.integration.jei.fquarry.FQuarryCategory;
import com.denfop.integration.jei.fquarry.FQuarryHandler;
import com.denfop.integration.jei.gas_chamber.GasChamberCategory;
import com.denfop.integration.jei.gas_chamber.GasChamberHandler;
import com.denfop.integration.jei.gas_turbine.GasTurbineCategory;
import com.denfop.integration.jei.gas_turbine.GasTurbineHandler;
import com.denfop.integration.jei.gas_well.GasWellCategory;
import com.denfop.integration.jei.gas_well.GasWellHandler;
import com.denfop.integration.jei.gascombiner.GasCombinerCategory;
import com.denfop.integration.jei.gascombiner.GasCombinerHandler;
import com.denfop.integration.jei.gasgenerator.GasGeneratorCategory;
import com.denfop.integration.jei.gasgenerator.GasGeneratorHandler;
import com.denfop.integration.jei.gassensor.GasSensorCategory;
import com.denfop.integration.jei.gassensor.GasSensorHandler;
import com.denfop.integration.jei.gaswell.GasPumpCategory;
import com.denfop.integration.jei.gaswell.GasPumpHandler;
import com.denfop.integration.jei.gearing.GearingCategory;
import com.denfop.integration.jei.gearing.GearingHandler;
import com.denfop.integration.jei.genaddstone.GenAddStoneCategory;
import com.denfop.integration.jei.genaddstone.GenAddStoneHandler;
import com.denfop.integration.jei.genbio.GenBioCategory;
import com.denfop.integration.jei.genbio.GenBioHandler;
import com.denfop.integration.jei.gendiesel.GenDieselCategory;
import com.denfop.integration.jei.gendiesel.GenDieselHandler;
import com.denfop.integration.jei.generator.GeneratorCategory;
import com.denfop.integration.jei.generator.GeneratorHandler;
import com.denfop.integration.jei.genetic_polymizer.GeneticPolymizerCategory;
import com.denfop.integration.jei.genetic_polymizer.GeneticPolymizerHandler;
import com.denfop.integration.jei.genetic_replicator.GeneticReplicatorCategory;
import com.denfop.integration.jei.genetic_replicator.GeneticReplicatorHandler;
import com.denfop.integration.jei.genetic_transposer.GeneticTransposerCategory;
import com.denfop.integration.jei.genetic_transposer.GeneticTransposerHandler;
import com.denfop.integration.jei.geneticstabilizer.GeneticStabilizerCategory;
import com.denfop.integration.jei.geneticstabilizer.GeneticStabilizerHandler;
import com.denfop.integration.jei.genhelium.GenHeliumCategory;
import com.denfop.integration.jei.genhelium.GenHeliumHandler;
import com.denfop.integration.jei.genhydrogen.GenHydCategory;
import com.denfop.integration.jei.genhydrogen.GenHydHandler;
import com.denfop.integration.jei.genlava.GenLavaCategory;
import com.denfop.integration.jei.genlava.GenLavaHandler;
import com.denfop.integration.jei.genmatter.GenMatterCategory;
import com.denfop.integration.jei.genmatter.GenMatterHandler;
import com.denfop.integration.jei.genneutronium.GenNeuCategory;
import com.denfop.integration.jei.genneutronium.GenNeuHandler;
import com.denfop.integration.jei.genobs.GenObsCategory;
import com.denfop.integration.jei.genobs.GenObsHandler;
import com.denfop.integration.jei.genpetrol.GenPetrolCategory;
import com.denfop.integration.jei.genpetrol.GenPetrolHandler;
import com.denfop.integration.jei.genrad1.GenRad1Category;
import com.denfop.integration.jei.genrad1.GenRad1Handler;
import com.denfop.integration.jei.genradiation.GenRadCategory;
import com.denfop.integration.jei.genradiation.GenRadHandler;
import com.denfop.integration.jei.genred.GenRedCategory;
import com.denfop.integration.jei.genred.GenRedHandler;
import com.denfop.integration.jei.gense.GenSECategory;
import com.denfop.integration.jei.gense.GenSEHandler;
import com.denfop.integration.jei.genstar.GenStarCategory;
import com.denfop.integration.jei.genstar.GenStarHandler;
import com.denfop.integration.jei.genstone.GenStoneCategory;
import com.denfop.integration.jei.genstone.GenStoneHandler;
import com.denfop.integration.jei.geothermal.GeoThermalCategory;
import com.denfop.integration.jei.geothermal.GeoThermalHandler;
import com.denfop.integration.jei.graphite_handler.GraphiteCategory;
import com.denfop.integration.jei.graphite_handler.GraphiteHandler;
import com.denfop.integration.jei.handlerho.HandlerHOCategory;
import com.denfop.integration.jei.handlerho.HandlerHOHandler;
import com.denfop.integration.jei.heavyanvil.HeavyAnvilCategory;
import com.denfop.integration.jei.heavyanvil.HeavyAnvilHandler;
import com.denfop.integration.jei.impalloysmelter.ImpAlloySmelterCategory;
import com.denfop.integration.jei.impalloysmelter.ImpAlloySmelterHandler;
import com.denfop.integration.jei.imprefiner.ImpRefinerCategory;
import com.denfop.integration.jei.imprefiner.ImpRefinerHandler;
import com.denfop.integration.jei.incubator.IncubatorCategory;
import com.denfop.integration.jei.incubator.IncubatorHandler;
import com.denfop.integration.jei.industrialorepurifier.IndustrialOrePurifierCategory;
import com.denfop.integration.jei.industrialorepurifier.IndustrialOrePurifierHandler;
import com.denfop.integration.jei.inoculator.InoculatorCategory;
import com.denfop.integration.jei.inoculator.InoculatorHandler;
import com.denfop.integration.jei.insulator.InsulatorCategory;
import com.denfop.integration.jei.insulator.InsulatorHandler;
import com.denfop.integration.jei.itemdivider.ItemDividerCategory;
import com.denfop.integration.jei.itemdivider.ItemDividerHandler;
import com.denfop.integration.jei.itemdividerfluid.ItemDividerFluidCategory;
import com.denfop.integration.jei.itemdividerfluid.ItemDividerFluidHandler;
import com.denfop.integration.jei.laser.LaserCategory;
import com.denfop.integration.jei.laser.LaserHandler;
import com.denfop.integration.jei.macerator.MaceratorCategory;
import com.denfop.integration.jei.macerator.MaceratorHandler;
import com.denfop.integration.jei.mattery_factory.MatteryCategory;
import com.denfop.integration.jei.mattery_factory.MatteryHandler;
import com.denfop.integration.jei.microchip.MicrochipCategory;
import com.denfop.integration.jei.microchip.MicrochipHandler;
import com.denfop.integration.jei.mini_smeltery.MiniSmelteryCategory;
import com.denfop.integration.jei.mini_smeltery.MiniSmelteryHandler;
import com.denfop.integration.jei.modularator.ModulatorCategory;
import com.denfop.integration.jei.modularator.ModulatorCategory1;
import com.denfop.integration.jei.modularator.ModulatorHandler;
import com.denfop.integration.jei.molecular.MolecularTransformerCategory;
import com.denfop.integration.jei.molecular.MolecularTransformerHandler;
import com.denfop.integration.jei.moon_spotter.MoonSpooterCategory;
import com.denfop.integration.jei.moon_spotter.MoonSpooterHandler;
import com.denfop.integration.jei.multiblock.MultiBlockCategory;
import com.denfop.integration.jei.multiblock.MultiBlockHandler;
import com.denfop.integration.jei.mutatron.MutatronCategory;
import com.denfop.integration.jei.mutatron.MutatronHandler;
import com.denfop.integration.jei.neutronseparator.NeutronSeparatorCategory;
import com.denfop.integration.jei.neutronseparator.NeutronSeparatorHandler;
import com.denfop.integration.jei.oilpump.OilPumpCategory;
import com.denfop.integration.jei.oilpump.OilPumpHandler;
import com.denfop.integration.jei.oilpurifier.OilPurifierCategory;
import com.denfop.integration.jei.oilpurifier.OilPurifierHandler;
import com.denfop.integration.jei.orewashing.OreWashingCategory;
import com.denfop.integration.jei.orewashing.OreWashingHandler;
import com.denfop.integration.jei.painting.PaintingCategory;
import com.denfop.integration.jei.painting.PaintingHandler;
import com.denfop.integration.jei.pellets.PelletsCategory;
import com.denfop.integration.jei.pellets.PelletsHandler;
import com.denfop.integration.jei.peralloysmelter.PerAlloySmelterCategory;
import com.denfop.integration.jei.peralloysmelter.PerAlloySmelterHandler;
import com.denfop.integration.jei.plasticcratorplate.PlasticCreatorPlateCategory;
import com.denfop.integration.jei.plasticcratorplate.PlasticCreatorPlateHandler;
import com.denfop.integration.jei.plasticcreator.PlasticCreatorCategory;
import com.denfop.integration.jei.plasticcreator.PlasticCreatorHandler;
import com.denfop.integration.jei.polymerizer.PolymerizerCategory;
import com.denfop.integration.jei.polymerizer.PolymerizerHandler;
import com.denfop.integration.jei.positronconverter.PositronConverterCategory;
import com.denfop.integration.jei.positronconverter.PositronConverterHandler;
import com.denfop.integration.jei.primal_laser.PrimalLaserCategory;
import com.denfop.integration.jei.primal_laser.PrimalLaserHandler;
import com.denfop.integration.jei.primalfluidintergrator.PrimalFluidIntegratorCategory;
import com.denfop.integration.jei.primalfluidintergrator.PrimalFluidIntegratorHandler;
import com.denfop.integration.jei.primalrolling.PrimalRollingCategory;
import com.denfop.integration.jei.primalrolling.PrimalRollingHandler;
import com.denfop.integration.jei.privatizer.PrivatizerCategory;
import com.denfop.integration.jei.privatizer.PrivatizerHandler;
import com.denfop.integration.jei.probeassembler.ProbeAssemblerCategory;
import com.denfop.integration.jei.probeassembler.ProbeAssemblerHandler;
import com.denfop.integration.jei.programming_table.ProgrammingTableCategory;
import com.denfop.integration.jei.programming_table.ProgrammingTableHandler;
import com.denfop.integration.jei.quantumminer.QuantumMinerCategory;
import com.denfop.integration.jei.quantumminer.QuantumMinerHandler;
import com.denfop.integration.jei.quantummolecular.QuantumMolecularCategory;
import com.denfop.integration.jei.quantummolecular.QuantumMolecularHandler;
import com.denfop.integration.jei.quarry.QuarryCategory;
import com.denfop.integration.jei.quarry.QuarryHandler;
import com.denfop.integration.jei.quarry_comb.CMQuarryCategory;
import com.denfop.integration.jei.quarry_comb.СMQuarryHandler;
import com.denfop.integration.jei.quarry_mac.MQuarryCategory;
import com.denfop.integration.jei.quarry_mac.MQuarryHandler;
import com.denfop.integration.jei.radioactiveorehandler.RadioactiveOreHandlerCategory;
import com.denfop.integration.jei.radioactiveorehandler.RadioactiveOreHandlerHandler;
import com.denfop.integration.jei.recycler.RecyclerCategory;
import com.denfop.integration.jei.recycler.RecyclerHandler;
import com.denfop.integration.jei.refiner.RefinerCategory;
import com.denfop.integration.jei.refiner.RefinerHandler;
import com.denfop.integration.jei.refractory_furnace.RefractoryFurnaceCategory;
import com.denfop.integration.jei.refractory_furnace.RefractoryFurnaceHandler;
import com.denfop.integration.jei.refrigeratorfluids.RefrigeratorFluidsCategory;
import com.denfop.integration.jei.refrigeratorfluids.RefrigeratorFluidsHandler;
import com.denfop.integration.jei.replicator.ReplicatorCategory;
import com.denfop.integration.jei.replicator.ReplicatorHandler;
import com.denfop.integration.jei.reversetransriptor.ReverseTransriptorCategory;
import com.denfop.integration.jei.reversetransriptor.ReverseTransriptorHandler;
import com.denfop.integration.jei.rna_collector.RNACollectorCategory;
import com.denfop.integration.jei.rna_collector.RNACollectorHandler;
import com.denfop.integration.jei.rocketassembler.RocketAssemblerCategory;
import com.denfop.integration.jei.rocketassembler.RocketAssemblerHandler;
import com.denfop.integration.jei.rods_factory.RodFactoryCategory;
import com.denfop.integration.jei.rods_factory.RodFactoryHandler;
import com.denfop.integration.jei.rolling.RollingCategory;
import com.denfop.integration.jei.rolling.RollingHandler;
import com.denfop.integration.jei.rotorrods.RotorsRodCategory;
import com.denfop.integration.jei.rotorrods.RotorsRodHandler;
import com.denfop.integration.jei.rotors.RotorsCategory;
import com.denfop.integration.jei.rotors.RotorsHandler;
import com.denfop.integration.jei.rotorsupgrade.RotorUpgradeCategory;
import com.denfop.integration.jei.rotorsupgrade.RotorUpgradeHandler;
import com.denfop.integration.jei.roverassembler.RoverAssemblerCategory;
import com.denfop.integration.jei.roverassembler.RoverAssemblerHandler;
import com.denfop.integration.jei.satelliteassembler.SatelliteAssemblerCategory;
import com.denfop.integration.jei.satelliteassembler.SatelliteAssemblerHandler;
import com.denfop.integration.jei.sawmill.SawmillCategory;
import com.denfop.integration.jei.sawmill.SawmillHandler;
import com.denfop.integration.jei.scrap.ScrapCategory;
import com.denfop.integration.jei.scrap.ScrapHandler;
import com.denfop.integration.jei.sharpener.SharpenerCategory;
import com.denfop.integration.jei.sharpener.SharpenerHandler;
import com.denfop.integration.jei.siliconhandler.SiliconCategory;
import com.denfop.integration.jei.siliconhandler.SiliconHandler;
import com.denfop.integration.jei.singlefluidadapter.SingleFluidAdapterCategory;
import com.denfop.integration.jei.singlefluidadapter.SingleFluidAdapterHandler;
import com.denfop.integration.jei.smeltery_controller.SmelteryControllerCategory;
import com.denfop.integration.jei.smeltery_controller.SmelteryControllerHandler;
import com.denfop.integration.jei.smelterycasting.SmelteryCastingCategory;
import com.denfop.integration.jei.smelterycasting.SmelteryCastingHandler;
import com.denfop.integration.jei.smelteryfurnace.SmelteryFurnaceCategory;
import com.denfop.integration.jei.smelteryfurnace.SmelteryFurnaceHandler;
import com.denfop.integration.jei.socket_factory.SocketCategory;
import com.denfop.integration.jei.socket_factory.SocketHandler;
import com.denfop.integration.jei.solidelectrolyzer.SolidElectrolyzerCategory;
import com.denfop.integration.jei.solidelectrolyzer.SolidElectrolyzerHandler;
import com.denfop.integration.jei.solidfluidintegrator.SolidFluidIntegratorCategory;
import com.denfop.integration.jei.solidfluidintegrator.SolidFluidIntegratorHandler;
import com.denfop.integration.jei.solidmatters.MatterCategory;
import com.denfop.integration.jei.solidmatters.MatterHandler;
import com.denfop.integration.jei.solidmixer.SolidMixerCategory;
import com.denfop.integration.jei.solidmixer.SolidMixerHandler;
import com.denfop.integration.jei.spacebody.SpaceBodyCategory;
import com.denfop.integration.jei.spacebody.SpaceBodyHandler;
import com.denfop.integration.jei.squeezer.SqueezerCategory;
import com.denfop.integration.jei.squeezer.SqueezerHandler;
import com.denfop.integration.jei.stamp.StampCategory;
import com.denfop.integration.jei.stamp.StampHandler;
import com.denfop.integration.jei.sunnarium.SunnariumCategory;
import com.denfop.integration.jei.sunnarium.SunnariumHandler;
import com.denfop.integration.jei.sunnariumpanel.SannariumPanelCategory;
import com.denfop.integration.jei.sunnariumpanel.SannariumPanelHandler;
import com.denfop.integration.jei.synthesis.SynthesisCategory;
import com.denfop.integration.jei.synthesis.SynthesisHandler;
import com.denfop.integration.jei.triplesolidmixer.TripleSolidMixerCategory;
import com.denfop.integration.jei.triplesolidmixer.TripleSolidMixerHandler;
import com.denfop.integration.jei.tuner.TunerCategory;
import com.denfop.integration.jei.tuner.TunerHandler;
import com.denfop.integration.jei.upgradeblock.UpgradeBlockCategory;
import com.denfop.integration.jei.upgradeblock.UpgradeBlockHandler;
import com.denfop.integration.jei.upgrademachine.UpgradeMachineCategory;
import com.denfop.integration.jei.upgrademachine.UpgradeMachineHandler;
import com.denfop.integration.jei.upgraderover.UpgradeRoverCategory;
import com.denfop.integration.jei.upgraderover.UpgradeRoverHandler;
import com.denfop.integration.jei.vein.VeinCategory;
import com.denfop.integration.jei.vein.VeinHandler;
import com.denfop.integration.jei.watergenerator.GenWaterCategory;
import com.denfop.integration.jei.watergenerator.GenWaterHandler;
import com.denfop.integration.jei.waterrotorrods.WaterRotorsCategory;
import com.denfop.integration.jei.waterrotorrods.WaterRotorsHandler;
import com.denfop.integration.jei.waterrotorsupgrade.WaterRotorUpgradeCategory;
import com.denfop.integration.jei.waterrotorsupgrade.WaterRotorUpgradeHandler;
import com.denfop.integration.jei.welding.WeldingCategory;
import com.denfop.integration.jei.welding.WeldingHandler;
import com.denfop.integration.jei.wire_insulator.WireInsulatorCategory;
import com.denfop.integration.jei.wire_insulator.WireInsulatorHandler;
import com.denfop.integration.jei.worldcollector.aer.AerCategory;
import com.denfop.integration.jei.worldcollector.aer.AerHandler;
import com.denfop.integration.jei.worldcollector.aqua.AquaCategory;
import com.denfop.integration.jei.worldcollector.aqua.AquaHandler;
import com.denfop.integration.jei.worldcollector.crystallize.CrystallizeCategory;
import com.denfop.integration.jei.worldcollector.crystallize.CrystallizeHandler;
import com.denfop.integration.jei.worldcollector.earth.EarthCategory;
import com.denfop.integration.jei.worldcollector.earth.EarthHandler;
import com.denfop.integration.jei.worldcollector.end.EndCategory;
import com.denfop.integration.jei.worldcollector.end.EndHandler;
import com.denfop.integration.jei.worldcollector.nether.NetherCategory;
import com.denfop.integration.jei.worldcollector.nether.NetherHandler;
import com.denfop.recipes.ItemStackHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class JEICompat implements IModPlugin {
    private static final ResourceLocation UID = new ResourceLocation(Constants.MOD_ID, "jei");
    public static List<JeiInform> informList = new ArrayList<>();
    public static JeiInform macerator = new JeiInform("macerator", MaceratorCategory.class, MaceratorHandler.class);
    public static JeiInform adv_alloy = new JeiInform("adv_alloy", AdvAlloySmelterCategory.class, AdvAlloySmelterHandler.class);
    public static JeiInform adv_refiner = new JeiInform("adv_refiner", AdvRefinerCategory.class, AdvRefinerHandler.class);
    public static JeiInform air_collector = new JeiInform("air_collector", AirColCategory.class, AirColHandler.class);
    public static JeiInform alkailine = new JeiInform("alkailine", AlkalineEarthQuarryCategory.class, AlkalineEarthQuarryHandler.class);
    public static JeiInform alloy = new JeiInform("alloy", AlloySmelterCategory.class, AlloySmelterHandler.class);
    public static JeiInform analyzer = new JeiInform("analyzer", AnalyzerCategory.class, AnalyzerHandler.class);
    public static JeiInform anti_upgrade = new JeiInform("anti_upgrade", AntiUpgradeBlockCategory.class, AntiUpgradeBlockHandler.class);
    public static JeiInform anvil = new JeiInform("anvil", AnvilCategory.class, AnvilHandler.class);
    public static JeiInform apiary = new JeiInform("apiary", ApiaryCategory.class, ApiaryHandler.class);
    public static JeiInform battery = new JeiInform("battery", BatteryCategory.class, BatteryHandler.class);
    public static JeiInform bee = new JeiInform("bee", BeeCategory.class, BeeHandler.class);
    public static JeiInform bee_centrifuge = new JeiInform("bee_centrifuge", CentrifugeCategory.class, CentrifugeHandler.class);
    public static JeiInform blastFurnace = new JeiInform("blast_furnace", BlastFCategory.class, BlastFHandler.class);
    public static JeiInform biomass = new JeiInform("biomass", BiomassCategory.class, BiomassHandler.class);
    public static JeiInform brewing = new JeiInform("brewing", BrewingCategory.class, BrewingHandler.class);
    public static JeiInform centrifuge = new JeiInform("centrifuge", com.denfop.integration.jei.centrifuge.CentrifugeCategory.class, com.denfop.integration.jei.centrifuge.CentrifugeHandler.class);
    public static JeiInform charged_redstone = new JeiInform("charged_redstone", ChargedRedstoneCategory.class, ChargedRedstoneHandler.class);
    public static JeiInform chemical_plant = new JeiInform("chemical_plant", ChemicalPlantCategory.class, ChemicalPlantHandler.class);
    public static JeiInform coke_oven = new JeiInform("coke_oven", CokeOvenCategory.class, CokeOvenHandler.class);
    public static JeiInform space_colony = new JeiInform("space_colony", SpaceColonyCategory.class, SpaceColonyHandler.class);
    public static JeiInform comb_mac = new JeiInform("comb_mac", CombMacCategory.class, CombMacHandler.class);
    public static JeiInform compressor = new JeiInform("compressor", CompressorCategory.class, CompressorHandler.class);
    public static JeiInform cross_crop = new JeiInform("cross_crop", CropCrossoverCategory.class, CropCrossoverHandler.class);
    public static JeiInform crop = new JeiInform("crop", CropCategory.class, CropHandler.class);
    public static JeiInform crystal_charge = new JeiInform("crystal_charge", CrystalChargerCategory.class, CrystalChargerHandler.class);
    public static JeiInform cutting = new JeiInform("cutting", CuttingCategory.class, CuttingHandler.class);
    public static JeiInform cyclotron = new JeiInform("cyclotron", CyclotronCategory.class, CyclotronHandler.class);
    public static JeiInform deposits = new JeiInform("deposits", DepositsCategory.class, DepositsHandler.class);
    public static JeiInform deposits_molot = new JeiInform("deposits_molot", DepositsMolotCategory.class, DepositsMolotHandler.class);
    public static JeiInform distiller = new JeiInform("distiller", DistillerCategory.class, DistillerHandler.class);
    public static JeiInform double_molecular = new JeiInform("double_molecular", DoubleMolecularTransformerCategory.class, DoubleMolecularTransformerHandler.class);
    public static JeiInform dryer = new JeiInform("dryer", DryerCategory.class, DryerHandler.class);
    public static JeiInform earth_quarry = new JeiInform("earth_quarry", EarthQuarryCategory.class, EarthQuarryHandler.class);
    public static JeiInform electrolyzer = new JeiInform("electrolyzer", ElectrolyzerCategory.class, ElectrolyzerHandler.class);
    public static JeiInform electronics = new JeiInform("electronics", ElectronicsCategory.class, ElectronicsHandler.class);
    public static JeiInform enchant = new JeiInform("enchant", EnchantCategory.class, EnchantHandler.class);
    public static JeiInform enrich = new JeiInform("enrich", EnrichCategory.class, EnrichHandler.class);
    public static JeiInform extruder = new JeiInform("extruder", ExtruderCategory.class, ExtruderHandler.class);
    public static JeiInform extractor = new JeiInform("extractor", ExtractorCategory.class, ExtractorHandler.class);
    public static JeiInform farmer = new JeiInform("farmer", FarmerCategory.class, FarmerHandler.class);
    public static JeiInform fluid_adapter = new JeiInform("fluid_adapter", FluidAdapterCategory.class, FluidAdapterHandler.class);
    public static JeiInform fluid_heater = new JeiInform("fluid_heater", HeaterFluidsCategory.class, HeaterFluidsHandler.class);
    public static JeiInform fluid_integrator = new JeiInform("fluid_integrator", FluidIntegratorCategory.class, FluidIntegratorHandler.class);
    public static JeiInform fluid_mixer = new JeiInform("fluid_mixer", FluidMixerCategory.class, FluidMixerHandler.class);
    public static JeiInform fluid_separator = new JeiInform("fluid_separator", FluidSeparatorCategory.class, FluidSeparatorHandler.class);
    public static JeiInform fluid_solid_mixed = new JeiInform("fluid_solid_mixed", FluidSolidMixerCategory.class, FluidSolidMixerHandler.class);
    public static JeiInform fquarry = new JeiInform("fquarry", FQuarryCategory.class, FQuarryHandler.class);
    public static JeiInform gas_chamber = new JeiInform("gas_chamber", GasChamberCategory.class, GasChamberHandler.class);
    public static JeiInform gas_turbine = new JeiInform("gas_turbine", GasTurbineCategory.class, GasTurbineHandler.class);
    public static JeiInform gas_well = new JeiInform("gas_well", GasWellCategory.class, GasWellHandler.class);
    public static JeiInform gas_combiner = new JeiInform("gas_combiner", GasCombinerCategory.class, GasCombinerHandler.class);
    public static JeiInform gas_gen = new JeiInform("gas_gen", GasGeneratorCategory.class, GasGeneratorHandler.class);
    public static JeiInform gas_sensor = new JeiInform("gas_sensor", GasSensorCategory.class, GasSensorHandler.class);
    public static JeiInform gas_pump = new JeiInform("gas_pump", GasPumpCategory.class, GasPumpHandler.class);
    public static JeiInform gearing = new JeiInform("gearing", GearingCategory.class, GearingHandler.class);
    public static JeiInform gen_addstone = new JeiInform("gen_addstone", GenAddStoneCategory.class, GenAddStoneHandler.class);
    public static JeiInform gen_bio = new JeiInform("gen_bio", GenBioCategory.class, GenBioHandler.class);
    public static JeiInform gen_diesel = new JeiInform("gen_diesel", GenDieselCategory.class, GenDieselHandler.class);
    public static JeiInform generator = new JeiInform("generator", GeneratorCategory.class, GeneratorHandler.class);
    public static JeiInform genetic_polymizer = new JeiInform("genetic_polymizer", GeneticPolymizerCategory.class, GeneticPolymizerHandler.class);
    public static JeiInform genetic_replicator = new JeiInform("genetic_replicator", GeneticReplicatorCategory.class, GeneticReplicatorHandler.class);
    public static JeiInform genetic_transposer = new JeiInform("genetic_transposer", GeneticTransposerCategory.class, GeneticTransposerHandler.class);
    public static JeiInform genetic_stabilizer = new JeiInform("genetic_stabilizer", GeneticStabilizerCategory.class, GeneticStabilizerHandler.class);
    public static JeiInform gen_helium = new JeiInform("gen_helium", GenHeliumCategory.class, GenHeliumHandler.class);
    public static JeiInform gen_hyd = new JeiInform("gen_hyd", GenHydCategory.class, GenHydHandler.class);
    public static JeiInform gen_lava = new JeiInform("gen_lava", GenLavaCategory.class, GenLavaHandler.class);
    public static JeiInform gen_matter = new JeiInform("gen_matter", GenMatterCategory.class, GenMatterHandler.class);
    public static JeiInform gen_neu = new JeiInform("gen_neu", GenNeuCategory.class, GenNeuHandler.class);
    public static JeiInform gen_obs = new JeiInform("gen_obs", GenObsCategory.class, GenObsHandler.class);
    public static JeiInform gen_petrol = new JeiInform("gen_petrol", GenPetrolCategory.class, GenPetrolHandler.class);
    public static JeiInform gen_rad1 = new JeiInform("gen_rad1", GenRad1Category.class, GenRad1Handler.class);
    public static JeiInform gen_rad = new JeiInform("gen_rad", GenRadCategory.class, GenRadHandler.class);
    public static JeiInform gen_red = new JeiInform("gen_red", GenRedCategory.class, GenRedHandler.class);
    public static JeiInform gen_se = new JeiInform("gen_se", GenSECategory.class, GenSEHandler.class);
    public static JeiInform gen_star = new JeiInform("gen_star", GenStarCategory.class, GenStarHandler.class);
    public static JeiInform gen_stone = new JeiInform("gen_stone", GenStoneCategory.class, GenStoneHandler.class);
    public static JeiInform geothermal = new JeiInform("geothermal", GeoThermalCategory.class, GeoThermalHandler.class);
    public static JeiInform graphite = new JeiInform("graphite", GraphiteCategory.class, GraphiteHandler.class);
    public static JeiInform handlerHO = new JeiInform("handlerho", HandlerHOCategory.class, HandlerHOHandler.class);
    public static JeiInform strong_anvil = new JeiInform("strong_anvil", HeavyAnvilCategory.class, HeavyAnvilHandler.class);
    public static JeiInform imp_alloy_smelter = new JeiInform("imp_alloy_smelter", ImpAlloySmelterCategory.class, ImpAlloySmelterHandler.class);
    public static JeiInform imp_refiner = new JeiInform("imp_refiner", ImpRefinerCategory.class, ImpRefinerHandler.class);
    public static JeiInform incubator = new JeiInform("incubator", IncubatorCategory.class, IncubatorHandler.class);
    public static JeiInform industrial_ore_purifier = new JeiInform("industrial_ore_purifier", IndustrialOrePurifierCategory.class, IndustrialOrePurifierHandler.class);
    public static JeiInform inoculator = new JeiInform("inoculator", InoculatorCategory.class, InoculatorHandler.class);
    public static JeiInform insulator = new JeiInform("insulator", InsulatorCategory.class, InsulatorHandler.class);
    public static JeiInform item_divider = new JeiInform("item_divider", ItemDividerCategory.class, ItemDividerHandler.class);
    public static JeiInform item_divider_fluid = new JeiInform("item_divider_fluid", ItemDividerFluidCategory.class, ItemDividerFluidHandler.class);
    public static JeiInform laser = new JeiInform("laser", LaserCategory.class, LaserHandler.class);
    public static JeiInform matter = new JeiInform("matter", MatteryCategory.class, MatteryHandler.class);
    public static JeiInform microchip = new JeiInform("microchip", MicrochipCategory.class, MicrochipHandler.class);
    public static JeiInform mini_smeltery = new JeiInform("mini_smeltery", MiniSmelteryCategory.class, MiniSmelteryHandler.class);
    public static JeiInform modulator = new JeiInform("modulator", ModulatorCategory.class, ModulatorHandler.class);
    public static JeiInform modulator1 = new JeiInform("modulator1", ModulatorCategory1.class, ModulatorHandler.class);
    public static JeiInform molecular = new JeiInform("molecular", MolecularTransformerCategory.class, MolecularTransformerHandler.class);
    public static JeiInform moon_spooter = new JeiInform("moon_spooter", MoonSpooterCategory.class, MoonSpooterHandler.class);
    public static JeiInform multiblock = new JeiInform("multiblock", MultiBlockCategory.class, MultiBlockHandler.class);
    public static JeiInform mutatron = new JeiInform("mutatron", MutatronCategory.class, MutatronHandler.class);
    public static JeiInform neutron_separator = new JeiInform("neutron_separator", NeutronSeparatorCategory.class, NeutronSeparatorHandler.class);
    public static JeiInform oil_pump = new JeiInform("oil_pump", OilPumpCategory.class, OilPumpHandler.class);
    public static JeiInform oil_purifier = new JeiInform("oil_purifier", OilPurifierCategory.class, OilPurifierHandler.class);
    public static JeiInform ore_washing = new JeiInform("ore_washing", OreWashingCategory.class, OreWashingHandler.class);
    public static JeiInform painting = new JeiInform("painting", PaintingCategory.class, PaintingHandler.class);
    public static JeiInform pelletes = new JeiInform("pelletes", PelletsCategory.class, PelletsHandler.class);
    public static JeiInform per_alloy = new JeiInform("per_alloy", PerAlloySmelterCategory.class, PerAlloySmelterHandler.class);
    public static JeiInform plastic_creator_plate = new JeiInform("plastic_creator_plate", PlasticCreatorPlateCategory.class, PlasticCreatorPlateHandler.class);
    public static JeiInform plastic_creator = new JeiInform("plastic_creator", PlasticCreatorCategory.class, PlasticCreatorHandler.class);
    public static JeiInform polymizer = new JeiInform("polymizer", PolymerizerCategory.class, PolymerizerHandler.class);
    public static JeiInform positrons = new JeiInform("positrons", PositronConverterCategory.class, PositronConverterHandler.class);
    public static JeiInform primal_laser = new JeiInform("primal_laser", PrimalLaserCategory.class, PrimalLaserHandler.class);
    public static JeiInform primal_fluid_integrator = new JeiInform("primal_fluid_integrator", PrimalFluidIntegratorCategory.class, PrimalFluidIntegratorHandler.class);
    public static JeiInform primal_rolling = new JeiInform("primal_rolling", PrimalRollingCategory.class, PrimalRollingHandler.class);
    public static JeiInform privatizer = new JeiInform("privatizer", PrivatizerCategory.class, PrivatizerHandler.class);
    public static JeiInform probe_assembler = new JeiInform("probe_assembler", ProbeAssemblerCategory.class, ProbeAssemblerHandler.class);
    public static JeiInform programming = new JeiInform("programming", ProgrammingTableCategory.class, ProgrammingTableHandler.class);
    public static JeiInform quantum_molecular = new JeiInform("quantum_molecular", QuantumMolecularCategory.class, QuantumMolecularHandler.class);
    public static JeiInform quantum_miner = new JeiInform("quantum_miner", QuantumMinerCategory.class, QuantumMinerHandler.class);
    public static JeiInform quarry = new JeiInform("quarry", QuarryCategory.class, QuarryHandler.class);
    public static JeiInform quarry_comb = new JeiInform("quarry_comb", CMQuarryCategory.class, СMQuarryHandler.class);
    public static JeiInform quarry_macerator = new JeiInform("quarry_macerator", MQuarryCategory.class, MQuarryHandler.class);
    public static JeiInform radiation_ore_handler = new JeiInform("radiation_ore_handler", RadioactiveOreHandlerCategory.class, RadioactiveOreHandlerHandler.class);
    public static JeiInform recycler = new JeiInform("recycler", RecyclerCategory.class, RecyclerHandler.class);
    public static JeiInform refiner = new JeiInform("refiner", RefinerCategory.class, RefinerHandler.class);
    public static JeiInform refractory_furnace = new JeiInform("refractory_furnace", RefractoryFurnaceCategory.class, RefractoryFurnaceHandler.class);
    public static JeiInform refractory_furnace_primal = new JeiInform("refractory_furnace_primal", com.denfop.integration.jei.refractoryfurnace.RefractoryFurnaceCategory.class, com.denfop.integration.jei.refractoryfurnace.RefractoryFurnaceHandler.class);
    public static JeiInform refrigerator = new JeiInform("refrigerator", RefrigeratorFluidsCategory.class, RefrigeratorFluidsHandler.class);
    public static JeiInform replicator = new JeiInform("replicator", ReplicatorCategory.class, ReplicatorHandler.class);
    public static JeiInform reverse_transriptor = new JeiInform("reverse_transriptor", ReverseTransriptorCategory.class, ReverseTransriptorHandler.class);
    public static JeiInform rna_collector = new JeiInform("rna_collector", RNACollectorCategory.class, RNACollectorHandler.class);
    public static JeiInform rocket = new JeiInform("rocket", RocketAssemblerCategory.class, RocketAssemblerHandler.class);
    public static JeiInform rod_factory = new JeiInform("rod_factory", RodFactoryCategory.class, RodFactoryHandler.class);
    public static JeiInform rolling = new JeiInform("rolling", RollingCategory.class, RollingHandler.class);
    public static JeiInform rotors = new JeiInform("rotors", RotorsRodCategory.class, RotorsRodHandler.class);
    public static JeiInform rotors1 = new JeiInform("rotors1", RotorsCategory.class, RotorsHandler.class);
    public static JeiInform rotors_upgrade = new JeiInform("rotors_upgrade", RotorUpgradeCategory.class, RotorUpgradeHandler.class);
    public static JeiInform rovers = new JeiInform("rovers", RoverAssemblerCategory.class, RoverAssemblerHandler.class);
    public static JeiInform satellite_assembler = new JeiInform("satellite_assembler", SatelliteAssemblerCategory.class, SatelliteAssemblerHandler.class);
    public static JeiInform sawmill = new JeiInform("sawmill", SawmillCategory.class, SawmillHandler.class);
    public static JeiInform scrap = new JeiInform("scrap", ScrapCategory.class, ScrapHandler.class);
    public static JeiInform sharpener = new JeiInform("sharpener", SharpenerCategory.class, SharpenerHandler.class);
    public static JeiInform silicon = new JeiInform("silicon", SiliconCategory.class, SiliconHandler.class);
    public static JeiInform single_fluid_adapter = new JeiInform("single_fluid_adapter", SingleFluidAdapterCategory.class, SingleFluidAdapterHandler.class);
    public static JeiInform smeltery_controller = new JeiInform("smeltery_controller", SmelteryControllerCategory.class, SmelteryControllerHandler.class);
    public static JeiInform smeltery_casting = new JeiInform("smeltery_casting", SmelteryCastingCategory.class, SmelteryCastingHandler.class);
    public static JeiInform smeltery_furnace = new JeiInform("smeltery_furnace", SmelteryFurnaceCategory.class, SmelteryFurnaceHandler.class);
    public static JeiInform socket = new JeiInform("socket", SocketCategory.class, SocketHandler.class);
    public static JeiInform solid_electrolyzer = new JeiInform("solid_electrolyzer", SolidElectrolyzerCategory.class, SolidElectrolyzerHandler.class);

    public static JeiInform solid_fluid_integrator = new JeiInform("solid_fluid_integrator", SolidFluidIntegratorCategory.class, SolidFluidIntegratorHandler.class);
    public static JeiInform solid_matter = new JeiInform("solid_matter", MatterCategory.class, MatterHandler.class);
    public static JeiInform solid_mixer = new JeiInform("solid_mixer", SolidMixerCategory.class, SolidMixerHandler.class);
    public static JeiInform space_body = new JeiInform("space_body", SpaceBodyCategory.class, SpaceBodyHandler.class);
    public static JeiInform squeezer = new JeiInform("squeezer", SqueezerCategory.class, SqueezerHandler.class);
    public static JeiInform stamp = new JeiInform("stamp", StampCategory.class, StampHandler.class);
    public static JeiInform sunnarium = new JeiInform("sunnarium", SunnariumCategory.class, SunnariumHandler.class);
    public static JeiInform sunnarium_panel = new JeiInform("sunnarium_panel", SannariumPanelCategory.class, SannariumPanelHandler.class);
    public static JeiInform synthesis = new JeiInform("synthesis", SynthesisCategory.class, SynthesisHandler.class);
    public static JeiInform triple_solid_mixer = new JeiInform("triple_solid_mixer", TripleSolidMixerCategory.class, TripleSolidMixerHandler.class);
    public static JeiInform tuner = new JeiInform("tuner", TunerCategory.class, TunerHandler.class);
    public static JeiInform upgrade_block = new JeiInform("upgrade_block", UpgradeBlockCategory.class, UpgradeBlockHandler.class);
    public static JeiInform upgrade_machine = new JeiInform("upgrade_machine", UpgradeMachineCategory.class, UpgradeMachineHandler.class);
    public static JeiInform upgrade_rover = new JeiInform("upgrade_rover", UpgradeRoverCategory.class, UpgradeRoverHandler.class);
    public static JeiInform vein = new JeiInform("vein", VeinCategory.class, VeinHandler.class);
    public static JeiInform water_generator = new JeiInform("water_generator", GenWaterCategory.class, GenWaterHandler.class);
    public static JeiInform water_rotors = new JeiInform("water_rotors", WaterRotorsCategory.class, WaterRotorsHandler.class);
    public static JeiInform water_rotors_upgrade = new JeiInform("water_rotors_upgrade", WaterRotorUpgradeCategory.class, WaterRotorUpgradeHandler.class);
    public static JeiInform welding = new JeiInform("welding", WeldingCategory.class, WeldingHandler.class);
    public static JeiInform wire_insulator = new JeiInform("wire_insulator", WireInsulatorCategory.class, WireInsulatorHandler.class);
    public static JeiInform world_collector_aer = new JeiInform("world_collector_aer", AerCategory.class, AerHandler.class);
    public static JeiInform world_collector_aqua = new JeiInform("world_collector_aqua", AquaCategory.class, AquaHandler.class);
    public static JeiInform world_collector_crystalyzer = new JeiInform("world_collector_crystalyzer", CrystallizeCategory.class, CrystallizeHandler.class);
    public static JeiInform world_collector_earth = new JeiInform("world_collector_earth", EarthCategory.class, EarthHandler.class);
    public static JeiInform world_collector_end = new JeiInform("world_collector_end", EndCategory.class, EndHandler.class);
    public static JeiInform world_collector_nether = new JeiInform("world_collector_nether", NetherCategory.class, NetherHandler.class);
    public static JeiInform scrapbox = new JeiInform("scrapbox", ScrapboxRecipeCategory.class, ScrapboxRecipeHandler.class);


  //  public static JeiInform reactor_schemes = new JeiInform("reactor_schemes", ReactorSchemesCategory.class, ReactorSchemesHandler.class);
    public JEICompat() {

    }

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    public static ItemStack getBlockStack(IMultiTileBlock block) {
        return TileBlockCreator.instance.get(block.getIDBlock()).getItemStack();

    }


    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(getBlockStack(BlockMoreMachine.double_furnace), RecipeTypes.SMELTING);

        registry.addRecipeCatalyst(getBlockStack(BlockMoreMachine.triple_furnace),RecipeTypes.SMELTING);
        registry.addRecipeCatalyst(getBlockStack(BlockMoreMachine.quad_furnace),RecipeTypes.SMELTING);
        registry.addRecipeCatalyst(getBlockStack(BlockSimpleMachine.furnace_iu), RecipeTypes.SMELTING);
        registry.addRecipeCatalyst(getBlockStack(BlocksPhotonicMachine.photonic_furnace),RecipeTypes.SMELTING);
        registry.addRecipeCatalyst(getBlockStack(BlockBaseMachine3.bio_furnace), RecipeTypes.SMELTING);
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 37),
                world_collector_nether.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 23),
                world_collector_end.recipeType
        );

        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 36),
                world_collector_earth.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 39),
                world_collector_crystalyzer.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 35),
                world_collector_aqua.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 34),
                world_collector_aer.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.electric_wire_insulator),
                wire_insulator.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_wire_insulator),
                wire_insulator.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockPrimalWireInsulator.primal_wire_insulator),
                wire_insulator.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.welding),
                welding.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 43),
                water_rotors_upgrade.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.water_rotor_assembler),
                water_rotors.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 9),
                water_generator.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.oilquarry),
                vein.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.upgrade_rover),
                upgrade_rover.recipeType
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.upgrade_machine),
                upgrade_machine.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.upgradeblock, 1, 0),
                upgrade_block.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 3),
                tuner.recipeType
        );

        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.triple_solid_mixer),
                triple_solid_mixer.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine, 1, 11),
                synthesis.recipeType
        );

        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.sunnariumpanelmaker, 1, 0),
                sunnarium.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.sunnariummaker, 1, 0),
                sunnarium_panel.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.stamp_mechanism),
                stamp.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockSqueezer.squeezer),
                squeezer.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.electric_squeezer),
                squeezer.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.steam_squeezer),
                squeezer.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.rover),
                space_body.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.adv_rover),
                space_body.recipeType
        );

        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.imp_rover),
                space_body.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.per_rover),
                space_body.recipeType
        );

        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.satellite),
                space_body.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.adv_satellite),
                space_body.recipeType
        );

        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.imp_satellite),
                space_body.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.per_satellite),
                space_body.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.probe),
                space_body.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.adv_probe),
                space_body.recipeType
        );

        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.imp_probe),
                space_body.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.per_probe),
                space_body.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.rocket),
                space_body.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.adv_rocket),
                space_body.recipeType
        );

        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.imp_rocket),
                space_body.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.per_rocket),
                space_body.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.solid_mixer),
                solid_mixer.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.solidmatter, 1, 0),
                solid_matter.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.solidmatter, 1, 1),
                solid_matter.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.solidmatter, 1, 2),
                solid_matter.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.solidmatter, 1, 3),
                solid_matter.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.solidmatter, 1, 4),
                solid_matter.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.solidmatter, 1, 5),
                solid_matter.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.solidmatter, 1, 6),
                solid_matter.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.solidmatter, 1, 7),
                solid_matter.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.solid_fluid_integrator),
                solid_fluid_integrator.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.solid_state_electrolyzer),
                solid_electrolyzer.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.socket_factory),
                socket.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockSmeltery.smeltery_furnace),
                smeltery_furnace.recipeType
        );

        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockSmeltery.smeltery_casting),
                smeltery_casting.recipeType
        );

        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockSmeltery.smeltery_controller),
                smeltery_controller.recipeType
        );

        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.single_fluid_adapter),
                single_fluid_adapter.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.silicon_crystal_handler),
                silicon.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockPrimalSiliconCrystalHandler.primal_silicon_crystal_handler),
                silicon.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.steam_sharpener),
                sharpener.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 4),
                scrap.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 5),
                scrap.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 6),
                scrap.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 7),
                scrap.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_assembler),
                scrap.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.sawmill),
                sawmill.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.satellite_assembler),
                satellite_assembler.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.rover_assembler),
                rovers.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 18),
                rotors_upgrade.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 17),
                rotors1.recipeType
        );

        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 21),
                rotors.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 0),
                rolling.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 1),
                rolling.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_rolling),
                rolling.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.rolling_machine),
                rolling.recipeType
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_rolling),
                rolling.recipeType
        );

        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 2),
                rolling.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 3),
                rolling.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.reactor_rod_factory),
                rod_factory.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.rocket_assembler),
                rocket.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.rna_collector),
                rna_collector.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.reverse_transcriptor),
                reverse_transriptor.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.replicator_iu),
                replicator.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.adv_replicator),
                replicator.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.imp_replicator),
                replicator.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.per_replicator),
                replicator.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_replicator),
                replicator.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.refrigerator_fluids),
                refrigerator.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockRefractoryFurnace.refractory_furnace),
                refractory_furnace_primal.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.electric_refractory_furnace),
                refractory_furnace.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.oilrefiner, 1, 0),
                refiner.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.simplemachine, 1, 5),
                recycler.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 0),
                recycler.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 1),
                recycler.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 2),
                recycler.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 3),
                recycler.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 4),
                recycler.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 5),
                recycler.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_comb_recycler),
                recycler.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.radioactive_handler_ore),
                radiation_ore_handler.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 8),
                quarry_macerator.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 13),
                quarry_macerator.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 14),
                quarry_macerator.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 15),
                quarry_macerator.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 8),
                quarry_comb.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 13),
                quarry_comb.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 14),
                quarry_comb.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 15),
                quarry_comb.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 8),
                quarry.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 13),
                quarry.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 14),
                quarry.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 15),
                quarry.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.quantum_miner),
                quantum_miner.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.quantum_transformer),
                quantum_molecular.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.programming_table),
                programming.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockPrimalProgrammingTable.primal_programming_table),
                programming.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.probe_assembler),
                probe_assembler.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 2),
                privatizer.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.rolling_machine),
                primal_rolling.recipeType
        );

        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockPrimalFluidIntegrator.primal_fluid_integrator),
                primal_fluid_integrator.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockPrimalLaserPolisher.primal_laser_polisher),
                primal_laser.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.positronconverter),
                positrons.recipeType
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.polymerizer),
                polymizer.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine1, 1, 11),
                plastic_creator.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine1, 1, 13),
                plastic_creator_plate.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.per_alloy_smelter),
                per_alloy.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.pallet_generator),
                pelletes.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine1, 1, 3),
                painting.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 8),
                ore_washing.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 9),
                ore_washing.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 10),
                ore_washing.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 11),
                ore_washing.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_orewashing),
                ore_washing.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.bio_orewashing),
                ore_washing.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.oil_purifier),
                oil_purifier.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.oilgetter, 1),
                oil_pump.recipeType
        );

        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.wireless_oil_pump),
                oil_pump.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.neutronseparator),
                neutron_separator.recipeType
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.mutatron),
                mutatron.recipeType
        );
        for (Map.Entry<String, MultiBlockStructure> entry : MultiBlockSystem.getInstance().mapMultiBlocks.entrySet()) {
            registry.addRecipeCatalyst(
                    entry.getValue().itemStackList.get(0),
                    multiblock.recipeType
            );


        }
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.moon_spotter),
                moon_spooter.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.night_converter),
                moon_spooter.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.blockmolecular),
                molecular.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 9),
                modulator.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 9),
                modulator1.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockMiniSmeltery.mini_smeltery),
                mini_smeltery.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 6),
                microchip.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockSolderingMechanism.primal_soldering_mechanism),
                microchip.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.matter_factory),
                matter.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.laser_polisher),
                laser.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.item_divider_to_fluid),
                item_divider_fluid.recipeType
        );

        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.item_divider),
                item_divider.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.insulator),
                insulator.recipeType
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.inoculator),
                inoculator.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.industrial_ore_purifier),
                industrial_ore_purifier.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.incubator),
                incubator.recipeType
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.imp_refiner),
                imp_refiner.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.imp_alloy_smelter),
                imp_alloy_smelter.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockStrongAnvil.block_strong_anvil),
                strong_anvil.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine, 1, 12),
                handlerHO.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.double_handlerho),
                handlerHO.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.triple_handlerho),
                handlerHO.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.quad_handlerho),
                handlerHO.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_handlerho),
                handlerHO.recipeType
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_handler_ore),
                handlerHO.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.graphite_handler),
                graphite.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockGeothermalPump.geothermal_controller),
                geothermal.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 7),
                gen_stone.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine, 1, 13),
                gen_star.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.imp_se_generator, 1, 0),
                gen_se.recipeType
        );  registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.blockSE, 1, 0),
                gen_se.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.adv_se_generator, 1, 0),
                gen_se.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.redstone_generator),
                gen_red.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.adv_redstone_generator),
                gen_red.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.imp_redstone_generator),
                gen_red.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.per_redstone_generator),
                gen_red.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_redstone_generator),
                gen_red.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.nuclear_waste_recycler),
                gen_rad.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.radiation_purifier),
                gen_rad1.recipeType
        );

        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine1, 1, 5),
                gen_petrol.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine1, 1, 10),
                gen_obs.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 5),
                gen_neu.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockSimpleMachine.generator_matter),
                gen_matter.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine.per_matter),
                gen_matter.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine.imp_matter),
                gen_matter.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine.adv_matter),
                gen_matter.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_gen_matter),
                gen_matter.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine1, 1, 12),
                gen_lava.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine1, 1, 9),
                gen_hyd.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine1, 1, 14),
                gen_helium.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.genetic_stabilizer),
                genetic_stabilizer.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.genetic_transposer),
                genetic_transposer.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.genetic_replicator),
                genetic_replicator.recipeType
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.genetic_polymerizer),
                genetic_polymizer.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.geogenerator_iu),
                generator.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine1.adv_geo),
                generator.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine1.imp_geo),
                generator.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine1.per_geo),
                generator.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_geogenerator),
                generator.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine1, 1, 4),
                gen_diesel.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.gen_bio),
                gen_bio.recipeType
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.gen_addition_stone),
                gen_addstone.recipeType
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockMoreMachine3.gearing),
                gearing.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockMoreMachine3.doublegearing),
                gearing.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockMoreMachine3.triplegearing),
                gearing.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockMoreMachine3.quadgearing),
                gearing.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_gearing),
                gearing.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.bio_gearing),
                gearing.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.gas_pump),
                gas_pump.recipeType
        );

        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.wireless_gas_pump),
                gas_pump.recipeType
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.gasSensor.getItem()),
                gas_sensor.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.gas_generator),
                gas_gen.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.gas_combiner),
                gas_combiner.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockGasWell.gas_well_controller),
                gas_well.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockGasTurbine.gas_turbine_controller),
                gas_turbine.recipeType
        );

        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockGasChamber.primal_gas_chamber),
                gas_chamber.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 8),
                fquarry.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 13),
                fquarry.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 14),
                fquarry.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 15),
                fquarry.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.solid_fluid_mixer),
                fluid_solid_mixed.recipeType
        );

        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.steam_solid_fluid_mixer),
                fluid_solid_mixed.recipeType
        );

        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.fluid_separator),
                fluid_separator.recipeType
        );

        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.fluid_mixer),
                fluid_mixer.recipeType
        );

        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.fluid_integrator),
                fluid_integrator.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.fluid_heater),
                fluid_heater.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockPrimalFluidHeater.primal_fluid_heater),
                fluid_heater.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.fluid_adapter),
                fluid_adapter.recipeType
        );

        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 0),
                farmer.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 1),
                farmer.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 2),
                farmer.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 3),
                farmer.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_fermer),
                farmer.recipeType
        );

        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.simplemachine, 1, 3),
                extractor.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 9),
                extractor.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 10),
                extractor.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 11),
                extractor.recipeType
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_extractor),
                extractor.recipeType
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.bio_extractor),
                extractor.recipeType
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_extractor),
                extractor.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 4),
                extruder.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 5),
                extruder.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 6),
                extruder.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 7),
                extruder.recipeType
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_extruding),
                extruder.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.bio_extruder),
                extruder.recipeType
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_extruder),
                extruder.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine, 1, 10),
                enrich.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.enchanter_books),
                enchant.recipeType
        );

        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.electronic_assembler),
                electronics.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockElectronicsAssembler.electronics_assembler),
                electronics.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine1, 1, 15),
                electrolyzer.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_electrolyzer),
                electrolyzer.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockEarthQuarry.earth_controller),
                earth_quarry.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockDryer.dryer),
                dryer.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.electric_dryer),
                dryer.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.steamdryer),
                dryer.recipeType
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.blockdoublemolecular.getItem()),
                double_molecular.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.solardestiller),
                distiller.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.adv_solar_destiller),
                distiller.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.imp_solar_destiller),
                distiller.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.per_solar_destiller),
                distiller.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_destiller),
                distiller.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.crystal_charge),
                crystal_charge.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockCyclotron.cyclotron_controller),
                cyclotron.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 8),
                cutting.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 9),
                cutting.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 10),
                cutting.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 11),
                cutting.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_cutting),
                cutting.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.bio_cutting),
                cutting.recipeType
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_cutting),
                cutting.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_crystal_charge),
                crystal_charge.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.simplemachine, 1, 1),
                compressor.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 3),
                compressor.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 4),
                compressor.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_compressor),
                compressor.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.bio_compressor),
                compressor.recipeType
        );

        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 5),
                compressor.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_compressor),
                compressor.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockCompressor.compressor),
                compressor.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 6),
                comb_mac.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 7),
                comb_mac.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 8),
                comb_mac.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 9),
                comb_mac.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_comb_mac),
                comb_mac.recipeType
        );
        for (int i = 7; i < 13; i++) {
            registry.addRecipeCatalyst(
                    ItemStackHelper.fromData(IUItem.colonial_building, 1, i),
                    space_colony.recipeType
            );
        }
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.cokeoven, 1, 0),
                coke_oven.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.adv_cokeoven, 1, 0),
                coke_oven.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockChemicalPlant.chemical_plant_controller),
                chemical_plant.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 12),
                centrifuge.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 13),
                centrifuge.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 14),
                centrifuge.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 15),
                centrifuge.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_centrifuge),
                centrifuge.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.bio_centrifuge),
                centrifuge.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.battery_factory),
                battery.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.electric_brewing),
                brewing.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_bio_generator),
                biomass.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.blastfurnace, 1, 0),
                blastFurnace.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.centrifuge),
                bee_centrifuge.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockHive.forest_hive),
                bee.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockHive.winter_hive),
                bee.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockHive.tropical_hive),
                bee.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockHive.plains_hive),
                bee.recipeType
        );

        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockHive.swamp_hive),
                bee.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockApiary.apiary),
                apiary.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockAnvil.block_anvil),
                anvil.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 8),
                anti_upgrade.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine2.analyzer),
                analyzer.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.wireless_mineral_quarry),
                analyzer.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine.quantum_quarry),
                analyzer.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine.imp_quantum_quarry),
                analyzer.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine.adv_quantum_quarry),
                analyzer.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine.per_quantum_quarry),
                analyzer.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines, 1, 4),
                alloy.recipeType
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.alkalineearthquarry),
                alkailine.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.oiladvrefiner, 1, 0),
                adv_refiner.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 11),
                air_collector.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.basemachine, 1, 3),
                adv_alloy.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.simplemachine, 1, 0),
                macerator.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockMacerator.macerator),
                macerator.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 0),
                macerator.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 1),
                macerator.recipeType
        );
        registry.addRecipeCatalyst(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 2),
                macerator.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_macerator),
                macerator.recipeType
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.bio_macerator),
                macerator.recipeType
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_macerator),
                macerator.recipeType
        );

    }

    public static IGuiHelper guiHelper;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        guiHelper = jeiHelpers.getGuiHelper();
        informList.forEach(jeiInform -> jeiInform.initCategory(registration, guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        informList.forEach(jeiInform -> jeiInform.InitHandler(registration));

    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {

    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {

    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {

    }
}
