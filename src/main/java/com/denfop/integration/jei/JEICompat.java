package com.denfop.integration.jei;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.api.multiblock.MultiBlockSystem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockAnvil;
import com.denfop.blocks.BlockStrongAnvil;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.mechanism.BlockAdvRefiner;
import com.denfop.blocks.mechanism.BlockApiary;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.blocks.mechanism.BlockBlastFurnace;
import com.denfop.blocks.mechanism.BlockChemicalPlant;
import com.denfop.blocks.mechanism.BlockCokeOven;
import com.denfop.blocks.mechanism.BlockCompressor;
import com.denfop.blocks.mechanism.BlockConverterMatter;
import com.denfop.blocks.mechanism.BlockCyclotron;
import com.denfop.blocks.mechanism.BlockDoubleMolecularTransfomer;
import com.denfop.blocks.mechanism.BlockDryer;
import com.denfop.blocks.mechanism.BlockEarthQuarry;
import com.denfop.blocks.mechanism.BlockElectronicsAssembler;
import com.denfop.blocks.mechanism.BlockGasChamber;
import com.denfop.blocks.mechanism.BlockGasTurbine;
import com.denfop.blocks.mechanism.BlockGasWell;
import com.denfop.blocks.mechanism.BlockGeothermalPump;
import com.denfop.blocks.mechanism.BlockHive;
import com.denfop.blocks.mechanism.BlockMacerator;
import com.denfop.blocks.mechanism.BlockMiniSmeltery;
import com.denfop.blocks.mechanism.BlockMolecular;
import com.denfop.blocks.mechanism.BlockMoreMachine;
import com.denfop.blocks.mechanism.BlockMoreMachine1;
import com.denfop.blocks.mechanism.BlockMoreMachine2;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.blocks.mechanism.BlockPetrolQuarry;
import com.denfop.blocks.mechanism.BlockPrimalFluidHeater;
import com.denfop.blocks.mechanism.BlockPrimalFluidIntegrator;
import com.denfop.blocks.mechanism.BlockPrimalLaserPolisher;
import com.denfop.blocks.mechanism.BlockPrimalProgrammingTable;
import com.denfop.blocks.mechanism.BlockPrimalSiliconCrystalHandler;
import com.denfop.blocks.mechanism.BlockPrimalWireInsulator;
import com.denfop.blocks.mechanism.BlockRefiner;
import com.denfop.blocks.mechanism.BlockRefractoryFurnace;
import com.denfop.blocks.mechanism.BlockSimpleMachine;
import com.denfop.blocks.mechanism.BlockSmeltery;
import com.denfop.blocks.mechanism.BlockSolarEnergy;
import com.denfop.blocks.mechanism.BlockSolderingMechanism;
import com.denfop.blocks.mechanism.BlockSolidMatter;
import com.denfop.blocks.mechanism.BlockSqueezer;
import com.denfop.blocks.mechanism.BlockSunnariumMaker;
import com.denfop.blocks.mechanism.BlockSunnariumPanelMaker;
import com.denfop.blocks.mechanism.BlockUpgradeBlock;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.gui.GuiAdvAlloySmelter;
import com.denfop.gui.GuiAdvOilRefiner;
import com.denfop.gui.GuiAlloySmelter;
import com.denfop.gui.GuiConverterSolidMatter;
import com.denfop.gui.GuiDoubleMolecularTransformer;
import com.denfop.gui.GuiElectrolyzer;
import com.denfop.gui.GuiEnriched;
import com.denfop.gui.GuiFisher;
import com.denfop.gui.GuiGenStone;
import com.denfop.gui.GuiGenerationMicrochip;
import com.denfop.gui.GuiHandlerHeavyOre;
import com.denfop.gui.GuiImpOilRefiner;
import com.denfop.gui.GuiMolecularTransformer;
import com.denfop.gui.GuiObsidianGenerator;
import com.denfop.gui.GuiOilRefiner;
import com.denfop.gui.GuiPainting;
import com.denfop.gui.GuiPlasticCreator;
import com.denfop.gui.GuiPlasticPlateCreator;
import com.denfop.gui.GuiRodManufacturer;
import com.denfop.gui.GuiSunnariumMaker;
import com.denfop.gui.GuiSunnariumPanelMaker;
import com.denfop.gui.GuiSynthesis;
import com.denfop.gui.GuiUpgradeBlock;
import com.denfop.gui.GuiWelding;
import com.denfop.gui.GuiWitherMaker;
import com.denfop.integration.jei.advalloysmelter.AdvAlloySmelterCategory;
import com.denfop.integration.jei.advalloysmelter.AdvAlloySmelterHandler;
import com.denfop.integration.jei.advalloysmelter.AdvAlloySmelterRecipeWrapper;
import com.denfop.integration.jei.advrefiner.AdvRefinerCategory;
import com.denfop.integration.jei.advrefiner.AdvRefinerHandler;
import com.denfop.integration.jei.advrefiner.AdvRefinerRecipeWrapper;
import com.denfop.integration.jei.aircollector.AirColCategory;
import com.denfop.integration.jei.aircollector.AirColHandler;
import com.denfop.integration.jei.aircollector.AirColRecipeWrapper;
import com.denfop.integration.jei.alkalineearthquarry.AlkalineEarthQuarryCategory;
import com.denfop.integration.jei.alkalineearthquarry.AlkalineEarthQuarryHandler;
import com.denfop.integration.jei.alkalineearthquarry.AlkalineEarthQuarryWrapper;
import com.denfop.integration.jei.alloysmelter.AlloySmelterCategory;
import com.denfop.integration.jei.alloysmelter.AlloySmelterHandler;
import com.denfop.integration.jei.alloysmelter.AlloySmelterRecipeWrapper;
import com.denfop.integration.jei.analyzer.AnalyzerCategory;
import com.denfop.integration.jei.analyzer.AnalyzerHandler;
import com.denfop.integration.jei.analyzer.AnalyzerWrapper;
import com.denfop.integration.jei.antiupgradeblock.AntiUpgradeBlockCategory;
import com.denfop.integration.jei.antiupgradeblock.AntiUpgradeBlockHandler;
import com.denfop.integration.jei.antiupgradeblock.AntiUpgradeBlockWrapper;
import com.denfop.integration.jei.anvil.AnvilCategory;
import com.denfop.integration.jei.anvil.AnvilHandler;
import com.denfop.integration.jei.anvil.AnvilWrapper;
import com.denfop.integration.jei.apiary.ApiaryCategory;
import com.denfop.integration.jei.apiary.ApiaryHandler;
import com.denfop.integration.jei.apiary.ApiaryWrapper;
import com.denfop.integration.jei.battery_factory.BatteryCategory;
import com.denfop.integration.jei.battery_factory.BatteryHandler;
import com.denfop.integration.jei.battery_factory.BatteryRecipeWrapper;
import com.denfop.integration.jei.bee.BeeCategory;
import com.denfop.integration.jei.bee.BeeHandler;
import com.denfop.integration.jei.bee.BeeWrapper;
import com.denfop.integration.jei.bf.BlastFCategory;
import com.denfop.integration.jei.bf.BlastFHandler;
import com.denfop.integration.jei.bf.BlastFWrapper;
import com.denfop.integration.jei.biomass.BiomassCategory;
import com.denfop.integration.jei.biomass.BiomassHandler;
import com.denfop.integration.jei.biomass.BiomassWrapper;
import com.denfop.integration.jei.blastfurnace.BFCategory;
import com.denfop.integration.jei.blastfurnace.BFHandler;
import com.denfop.integration.jei.blastfurnace.BFWrapper;
import com.denfop.integration.jei.brewing.BrewingCategory;
import com.denfop.integration.jei.brewing.BrewingHandler;
import com.denfop.integration.jei.brewing.BrewingRecipeWrapper;
import com.denfop.integration.jei.canning.CanningCategory;
import com.denfop.integration.jei.canning.CanningHandler;
import com.denfop.integration.jei.canning.CanningRecipeWrapper;
import com.denfop.integration.jei.centrifuge.CentrifugeCategory;
import com.denfop.integration.jei.centrifuge.CentrifugeHandler;
import com.denfop.integration.jei.centrifuge.CentrifugeWrapper;
import com.denfop.integration.jei.charged_redstone.ChargedRedstoneCategory;
import com.denfop.integration.jei.charged_redstone.ChargedRedstoneHandler;
import com.denfop.integration.jei.charged_redstone.ChargedRedstoneWrapper;
import com.denfop.integration.jei.chemicalplant.ChemicalPlantCategory;
import com.denfop.integration.jei.chemicalplant.ChemicalPlantHandler;
import com.denfop.integration.jei.chemicalplant.ChemicalPlantWrapper;
import com.denfop.integration.jei.cokeoven.CokeOvenCategory;
import com.denfop.integration.jei.cokeoven.CokeOvenHandler;
import com.denfop.integration.jei.cokeoven.CokeOvenWrapper;
import com.denfop.integration.jei.colonial_resource.SpaceColonyCategory;
import com.denfop.integration.jei.colonial_resource.SpaceColonyHandler;
import com.denfop.integration.jei.colonial_resource.SpaceColonyRecipeWrapper;
import com.denfop.integration.jei.combmac.CombMacCategory;
import com.denfop.integration.jei.combmac.CombMacHandler;
import com.denfop.integration.jei.combmac.CombMacRecipeWrapper;
import com.denfop.integration.jei.compressor.CompressorCategory;
import com.denfop.integration.jei.compressor.CompressorHandler;
import com.denfop.integration.jei.compressor.CompressorWrapper;
import com.denfop.integration.jei.convertermatter.ConverterCategory;
import com.denfop.integration.jei.convertermatter.ConverterHandler;
import com.denfop.integration.jei.convertermatter.ConverterWrapper;
import com.denfop.integration.jei.crops.CropCategory;
import com.denfop.integration.jei.crops.CropCrossoverCategory;
import com.denfop.integration.jei.crops.CropCrossoverHandler;
import com.denfop.integration.jei.crops.CropCrossoverWrapper;
import com.denfop.integration.jei.crops.CropHandler;
import com.denfop.integration.jei.crops.CropWrapper;
import com.denfop.integration.jei.crystal_charge.CrystalChargerCategory;
import com.denfop.integration.jei.crystal_charge.CrystalChargerHandler;
import com.denfop.integration.jei.crystal_charge.CrystalChargerRecipeWrapper;
import com.denfop.integration.jei.cutting.CuttingCategory;
import com.denfop.integration.jei.cutting.CuttingHandler;
import com.denfop.integration.jei.cutting.CuttingWrapper;
import com.denfop.integration.jei.cyclotron.CyclotronCategory;
import com.denfop.integration.jei.cyclotron.CyclotronHandler;
import com.denfop.integration.jei.cyclotron.CyclotronWrapper;
import com.denfop.integration.jei.deposits.DepositsCategory;
import com.denfop.integration.jei.deposits.DepositsHandler;
import com.denfop.integration.jei.deposits.DepositsWrapper;
import com.denfop.integration.jei.distiller.DistillerCategory;
import com.denfop.integration.jei.distiller.DistillerHandler;
import com.denfop.integration.jei.distiller.DistillerWrapper;
import com.denfop.integration.jei.doublemolecular.DoubleMolecularTransformerCategory;
import com.denfop.integration.jei.doublemolecular.DoubleMolecularTransformerHandler;
import com.denfop.integration.jei.doublemolecular.DoubleMolecularTransformerRecipeWrapper;
import com.denfop.integration.jei.dryer.DryerCategory;
import com.denfop.integration.jei.dryer.DryerHandler;
import com.denfop.integration.jei.dryer.DryerWrapper;
import com.denfop.integration.jei.earthquarry.EarthQuarryCategory;
import com.denfop.integration.jei.earthquarry.EarthQuarryHandler;
import com.denfop.integration.jei.earthquarry.EarthQuarryWrapper;
import com.denfop.integration.jei.electrolyzer.ElectrolyzerCategory;
import com.denfop.integration.jei.electrolyzer.ElectrolyzerHandler;
import com.denfop.integration.jei.electrolyzer.ElectrolyzerRecipeWrapper;
import com.denfop.integration.jei.electronics.ElectronicsCategory;
import com.denfop.integration.jei.electronics.ElectronicsHandler;
import com.denfop.integration.jei.electronics.ElectronicsRecipeWrapper;
import com.denfop.integration.jei.enchanter.EnchantCategory;
import com.denfop.integration.jei.enchanter.EnchantHandler;
import com.denfop.integration.jei.enchanter.EnchantRecipeWrapper;
import com.denfop.integration.jei.enrichment.EnrichCategory;
import com.denfop.integration.jei.enrichment.EnrichHandler;
import com.denfop.integration.jei.enrichment.EnrichRecipeWrapper;
import com.denfop.integration.jei.extractor.ExtractorCategory;
import com.denfop.integration.jei.extractor.ExtractorHandler;
import com.denfop.integration.jei.extractor.ExtractorWrapper;
import com.denfop.integration.jei.extruder.ExtruderCategory;
import com.denfop.integration.jei.extruder.ExtruderHandler;
import com.denfop.integration.jei.extruder.ExtruderWrapper;
import com.denfop.integration.jei.farmer.FarmerCategory;
import com.denfop.integration.jei.farmer.FarmerHandler;
import com.denfop.integration.jei.farmer.FarmerRecipeWrapper;
import com.denfop.integration.jei.fishmachine.FishMCategory;
import com.denfop.integration.jei.fishmachine.FishMHandler;
import com.denfop.integration.jei.fishmachine.FishMWrapper;
import com.denfop.integration.jei.fluidadapter.FluidAdapterCategory;
import com.denfop.integration.jei.fluidadapter.FluidAdapterHandler;
import com.denfop.integration.jei.fluidadapter.FluidAdapterRecipeWrapper;
import com.denfop.integration.jei.fluidheater.HeaterFluidsCategory;
import com.denfop.integration.jei.fluidheater.HeaterFluidsHandler;
import com.denfop.integration.jei.fluidheater.HeaterFluidsWrapper;
import com.denfop.integration.jei.fluidintegrator.FluidIntegratorCategory;
import com.denfop.integration.jei.fluidintegrator.FluidIntegratorHandler;
import com.denfop.integration.jei.fluidintegrator.FluidIntegratorRecipeWrapper;
import com.denfop.integration.jei.fluidmixer.FluidMixerCategory;
import com.denfop.integration.jei.fluidmixer.FluidMixerHandler;
import com.denfop.integration.jei.fluidmixer.FluidMixerRecipeWrapper;
import com.denfop.integration.jei.fluidseparator.FluidSeparatorCategory;
import com.denfop.integration.jei.fluidseparator.FluidSeparatorHandler;
import com.denfop.integration.jei.fluidseparator.FluidSeparatorRecipeWrapper;
import com.denfop.integration.jei.fluidsolidmixer.FluidSolidMixerCategory;
import com.denfop.integration.jei.fluidsolidmixer.FluidSolidMixerHandler;
import com.denfop.integration.jei.fluidsolidmixer.FluidSolidMixerRecipeWrapper;
import com.denfop.integration.jei.fquarry.FQuarryCategory;
import com.denfop.integration.jei.fquarry.FQuarryHandler;
import com.denfop.integration.jei.fquarry.FQuarryWrapper;
import com.denfop.integration.jei.gas_chamber.GasChamberCategory;
import com.denfop.integration.jei.gas_chamber.GasChamberHandler;
import com.denfop.integration.jei.gas_chamber.GasChamberRecipeWrapper;
import com.denfop.integration.jei.gas_turbine.GasTurbineCategory;
import com.denfop.integration.jei.gas_turbine.GasTurbineHandler;
import com.denfop.integration.jei.gas_turbine.GasTurbineWrapper;
import com.denfop.integration.jei.gas_well.GasWellCategory;
import com.denfop.integration.jei.gas_well.GasWellHandler;
import com.denfop.integration.jei.gas_well.GasWellWrapper;
import com.denfop.integration.jei.gascombiner.GasCombinerCategory;
import com.denfop.integration.jei.gascombiner.GasCombinerHandler;
import com.denfop.integration.jei.gascombiner.GasCombinerRecipeWrapper;
import com.denfop.integration.jei.gasgenerator.GasGeneratorCategory;
import com.denfop.integration.jei.gasgenerator.GasGeneratorHandler;
import com.denfop.integration.jei.gasgenerator.GasGeneratorWrapper;
import com.denfop.integration.jei.gassensor.GasSensorCategory;
import com.denfop.integration.jei.gassensor.GasSensorHandler;
import com.denfop.integration.jei.gassensor.GasSensorWrapper;
import com.denfop.integration.jei.gaswell.GasPumpCategory;
import com.denfop.integration.jei.gaswell.GasPumpHandler;
import com.denfop.integration.jei.gaswell.GasPumpWrapper;
import com.denfop.integration.jei.gearing.GearingCategory;
import com.denfop.integration.jei.gearing.GearingHandler;
import com.denfop.integration.jei.gearing.GearingWrapper;
import com.denfop.integration.jei.genaddstone.GenAddStoneCategory;
import com.denfop.integration.jei.genaddstone.GenAddStoneHandler;
import com.denfop.integration.jei.genaddstone.GenAddStoneRecipeWrapper;
import com.denfop.integration.jei.gendiesel.GenDieselCategory;
import com.denfop.integration.jei.gendiesel.GenDieselHandler;
import com.denfop.integration.jei.gendiesel.GenDieselWrapper;
import com.denfop.integration.jei.generator.GeneratorCategory;
import com.denfop.integration.jei.generator.GeneratorHandler;
import com.denfop.integration.jei.generator.GeneratorWrapper;
import com.denfop.integration.jei.genetic_polymizer.GeneticPolymizerCategory;
import com.denfop.integration.jei.genetic_polymizer.GeneticPolymizerHandler;
import com.denfop.integration.jei.genetic_polymizer.GeneticPolymizerWrapper;
import com.denfop.integration.jei.genetic_replicator.GeneticReplicatorCategory;
import com.denfop.integration.jei.genetic_replicator.GeneticReplicatorHandler;
import com.denfop.integration.jei.genetic_replicator.GeneticReplicatorRecipeWrapper;
import com.denfop.integration.jei.genetic_transposer.GeneticTransposerCategory;
import com.denfop.integration.jei.genetic_transposer.GeneticTransposerHandler;
import com.denfop.integration.jei.genetic_transposer.GeneticTransposerWrapper;
import com.denfop.integration.jei.geneticstabilizer.GeneticStabilizerCategory;
import com.denfop.integration.jei.geneticstabilizer.GeneticStabilizerHandler;
import com.denfop.integration.jei.geneticstabilizer.GeneticStabilizerRecipeWrapper;
import com.denfop.integration.jei.genhelium.GenHeliumCategory;
import com.denfop.integration.jei.genhelium.GenHeliumHandler;
import com.denfop.integration.jei.genhelium.GenHeliumWrapper;
import com.denfop.integration.jei.genhydrogen.GenHydCategory;
import com.denfop.integration.jei.genhydrogen.GenHydHandler;
import com.denfop.integration.jei.genhydrogen.GenHydWrapper;
import com.denfop.integration.jei.genlava.GenLavaCategory;
import com.denfop.integration.jei.genlava.GenLavaHandler;
import com.denfop.integration.jei.genlava.GenLavaWrapper;
import com.denfop.integration.jei.genmatter.GenMatterCategory;
import com.denfop.integration.jei.genmatter.GenMatterHandler;
import com.denfop.integration.jei.genmatter.GenMatterWrapper;
import com.denfop.integration.jei.genneutronium.GenNeuCategory;
import com.denfop.integration.jei.genneutronium.GenNeuHandler;
import com.denfop.integration.jei.genneutronium.GenNeuWrapper;
import com.denfop.integration.jei.genobs.GenObsCategory;
import com.denfop.integration.jei.genobs.GenObsHandler;
import com.denfop.integration.jei.genobs.GenObsWrapper;
import com.denfop.integration.jei.genpetrol.GenPetrolCategory;
import com.denfop.integration.jei.genpetrol.GenPetrolHandler;
import com.denfop.integration.jei.genpetrol.GenPetrolWrapper;
import com.denfop.integration.jei.genrad1.GenRad1Category;
import com.denfop.integration.jei.genrad1.GenRad1Handler;
import com.denfop.integration.jei.genrad1.GenRad1Wrapper;
import com.denfop.integration.jei.genradiation.GenRadCategory;
import com.denfop.integration.jei.genradiation.GenRadHandler;
import com.denfop.integration.jei.genradiation.GenRadWrapper;
import com.denfop.integration.jei.genred.GenRedCategory;
import com.denfop.integration.jei.genred.GenRedHandler;
import com.denfop.integration.jei.genred.GenRedWrapper;
import com.denfop.integration.jei.gense.GenSECategory;
import com.denfop.integration.jei.gense.GenSEHandler;
import com.denfop.integration.jei.gense.GenSEWrapper;
import com.denfop.integration.jei.genstar.GenStarCategory;
import com.denfop.integration.jei.genstar.GenStarHandler;
import com.denfop.integration.jei.genstar.GenStarRecipeManager;
import com.denfop.integration.jei.genstone.GenStoneCategory;
import com.denfop.integration.jei.genstone.GenStoneHandler;
import com.denfop.integration.jei.genstone.GenStoneRecipeWrapper;
import com.denfop.integration.jei.geothermal.GeoThermalCategory;
import com.denfop.integration.jei.geothermal.GeoThermalHandler;
import com.denfop.integration.jei.geothermal.GeoThermalWrapper;
import com.denfop.integration.jei.graphite_handler.GraphiteCategory;
import com.denfop.integration.jei.graphite_handler.GraphiteHandler;
import com.denfop.integration.jei.graphite_handler.GraphiteRecipeWrapper;
import com.denfop.integration.jei.handlerho.HandlerHOCategory;
import com.denfop.integration.jei.handlerho.HandlerHOHandler;
import com.denfop.integration.jei.handlerho.HandlerHORecipeWrapper;
import com.denfop.integration.jei.heavyanvil.HeavyAnvilCategory;
import com.denfop.integration.jei.heavyanvil.HeavyAnvilHandler;
import com.denfop.integration.jei.heavyanvil.HeavyAnvilWrapper;
import com.denfop.integration.jei.impalloysmelter.ImpAlloySmelterCategory;
import com.denfop.integration.jei.impalloysmelter.ImpAlloySmelterHandler;
import com.denfop.integration.jei.impalloysmelter.ImpAlloySmelterRecipeWrapper;
import com.denfop.integration.jei.imprefiner.ImpRefinerCategory;
import com.denfop.integration.jei.imprefiner.ImpRefinerHandler;
import com.denfop.integration.jei.imprefiner.ImpRefinerRecipeWrapper;
import com.denfop.integration.jei.incubator.IncubatorCategory;
import com.denfop.integration.jei.incubator.IncubatorHandler;
import com.denfop.integration.jei.incubator.IncubatorRecipeWrapper;
import com.denfop.integration.jei.industrialorepurifier.IndustrialOrePurifierCategory;
import com.denfop.integration.jei.industrialorepurifier.IndustrialOrePurifierHandler;
import com.denfop.integration.jei.industrialorepurifier.IndustrialOrePurifierRecipeWrapper;
import com.denfop.integration.jei.inoculator.InoculatorCategory;
import com.denfop.integration.jei.inoculator.InoculatorHandler;
import com.denfop.integration.jei.inoculator.InoculatorRecipeWrapper;
import com.denfop.integration.jei.insulator.InsulatorCategory;
import com.denfop.integration.jei.insulator.InsulatorHandler;
import com.denfop.integration.jei.insulator.InsulatorRecipeWrapper;
import com.denfop.integration.jei.itemdivider.ItemDividerCategory;
import com.denfop.integration.jei.itemdivider.ItemDividerHandler;
import com.denfop.integration.jei.itemdivider.ItemDividerRecipeWrapper;
import com.denfop.integration.jei.itemdividerfluid.ItemDividerFluidCategory;
import com.denfop.integration.jei.itemdividerfluid.ItemDividerFluidHandler;
import com.denfop.integration.jei.itemdividerfluid.ItemDividerFluidRecipeWrapper;
import com.denfop.integration.jei.laser.LaserCategory;
import com.denfop.integration.jei.laser.LaserHandler;
import com.denfop.integration.jei.laser.LaserRecipeWrapper;
import com.denfop.integration.jei.macerator.MaceratorCategory;
import com.denfop.integration.jei.macerator.MaceratorHandler;
import com.denfop.integration.jei.macerator.MaceratorWrapper;
import com.denfop.integration.jei.mattery_factory.MatteryCategory;
import com.denfop.integration.jei.mattery_factory.MatteryHandler;
import com.denfop.integration.jei.mattery_factory.MatteryRecipeWrapper;
import com.denfop.integration.jei.microchip.MicrochipCategory;
import com.denfop.integration.jei.microchip.MicrochipHandler;
import com.denfop.integration.jei.microchip.MicrochipRecipeWrapper;
import com.denfop.integration.jei.mini_smeltery.MiniSmelteryCategory;
import com.denfop.integration.jei.mini_smeltery.MiniSmelteryHandler;
import com.denfop.integration.jei.mini_smeltery.MiniSmelteryWrapper;
import com.denfop.integration.jei.modularator.ModulatorCategory;
import com.denfop.integration.jei.modularator.ModulatorCategory1;
import com.denfop.integration.jei.modularator.ModulatorHandler;
import com.denfop.integration.jei.modularator.ModulatorWrapper;
import com.denfop.integration.jei.modularator.ModulatorWrapper1;
import com.denfop.integration.jei.molecular.MolecularTransformerCategory;
import com.denfop.integration.jei.molecular.MolecularTransformerHandler;
import com.denfop.integration.jei.molecular.MolecularTransformerRecipeWrapper;
import com.denfop.integration.jei.moon_spotter.MoonSpooterCategory;
import com.denfop.integration.jei.moon_spotter.MoonSpooterHandler;
import com.denfop.integration.jei.moon_spotter.MoonSpooterRecipeWrapper;
import com.denfop.integration.jei.multiblock.MultiBlockCategory;
import com.denfop.integration.jei.multiblock.MultiBlockHandler;
import com.denfop.integration.jei.multiblock.MultiBlockWrapper;
import com.denfop.integration.jei.mutatron.MutatronCategory;
import com.denfop.integration.jei.mutatron.MutatronHandler;
import com.denfop.integration.jei.mutatron.MutatronRecipeWrapper;
import com.denfop.integration.jei.neutronseparator.NeutronSeparatorCategory;
import com.denfop.integration.jei.neutronseparator.NeutronSeparatorHandler;
import com.denfop.integration.jei.neutronseparator.NeutronSeparatorRecipeWrapper;
import com.denfop.integration.jei.oilpump.OilPumpCategory;
import com.denfop.integration.jei.oilpump.OilPumpHandler;
import com.denfop.integration.jei.oilpump.OilPumpWrapper;
import com.denfop.integration.jei.oilpurifier.OilPurifierCategory;
import com.denfop.integration.jei.oilpurifier.OilPurifierHandler;
import com.denfop.integration.jei.oilpurifier.OilPurifierRecipeWrapper;
import com.denfop.integration.jei.orewashing.OreWashingCategory;
import com.denfop.integration.jei.orewashing.OreWashingHandler;
import com.denfop.integration.jei.orewashing.OreWashingWrapper;
import com.denfop.integration.jei.painting.PaintingCategory;
import com.denfop.integration.jei.painting.PaintingHandler;
import com.denfop.integration.jei.painting.PaintingWrapper;
import com.denfop.integration.jei.pellets.PelletsCategory;
import com.denfop.integration.jei.pellets.PelletsHandler;
import com.denfop.integration.jei.pellets.PelletsWrapper;
import com.denfop.integration.jei.peralloysmelter.PerAlloySmelterCategory;
import com.denfop.integration.jei.peralloysmelter.PerAlloySmelterHandler;
import com.denfop.integration.jei.peralloysmelter.PerAlloySmelterRecipeWrapper;
import com.denfop.integration.jei.plasticcratorplate.PlasticCreatorPlateCategory;
import com.denfop.integration.jei.plasticcratorplate.PlasticCreatorPlateHandler;
import com.denfop.integration.jei.plasticcratorplate.PlasticCreatorPlateWrapper;
import com.denfop.integration.jei.plasticcreator.PlasticCreatorCategory;
import com.denfop.integration.jei.plasticcreator.PlasticCreatorHandler;
import com.denfop.integration.jei.plasticcreator.PlasticCreatorWrapper;
import com.denfop.integration.jei.polymerizer.PolymerizerCategory;
import com.denfop.integration.jei.polymerizer.PolymerizerHandler;
import com.denfop.integration.jei.polymerizer.PolymerizerWrapper;
import com.denfop.integration.jei.positronconverter.PositronConverterCategory;
import com.denfop.integration.jei.positronconverter.PositronConverterHandler;
import com.denfop.integration.jei.positronconverter.PositronConverterWrapper;
import com.denfop.integration.jei.primal_laser.PrimalLaserCategory;
import com.denfop.integration.jei.primal_laser.PrimalLaserHandler;
import com.denfop.integration.jei.primal_laser.PrimalLaserRecipeWrapper;
import com.denfop.integration.jei.primalfluidintergrator.PrimalFluidIntegratorCategory;
import com.denfop.integration.jei.primalfluidintergrator.PrimalFluidIntegratorHandler;
import com.denfop.integration.jei.primalfluidintergrator.PrimalFluidIntegratorRecipeWrapper;
import com.denfop.integration.jei.primalrolling.PrimalRollingCategory;
import com.denfop.integration.jei.primalrolling.PrimalRollingHandler;
import com.denfop.integration.jei.primalrolling.PrimalRollingWrapper;
import com.denfop.integration.jei.privatizer.PrivatizerCategory;
import com.denfop.integration.jei.privatizer.PrivatizerHandler;
import com.denfop.integration.jei.privatizer.PrivatizerWrapper;
import com.denfop.integration.jei.probeassembler.ProbeAssemblerCategory;
import com.denfop.integration.jei.probeassembler.ProbeAssemblerHandler;
import com.denfop.integration.jei.probeassembler.ProbeAssemblerRecipeWrapper;
import com.denfop.integration.jei.programming_table.ProgrammingTableCategory;
import com.denfop.integration.jei.programming_table.ProgrammingTableHandler;
import com.denfop.integration.jei.programming_table.ProgrammingTableRecipeWrapper;
import com.denfop.integration.jei.quantumminer.QuantumMinerCategory;
import com.denfop.integration.jei.quantumminer.QuantumMinerHandler;
import com.denfop.integration.jei.quantumminer.QuantumMinerWrapper;
import com.denfop.integration.jei.quantummolecular.QuantumMolecularCategory;
import com.denfop.integration.jei.quantummolecular.QuantumMolecularHandler;
import com.denfop.integration.jei.quantummolecular.QuantumMolecularRecipeWrapper;
import com.denfop.integration.jei.quarry.QuarryCategory;
import com.denfop.integration.jei.quarry.QuarryHandler;
import com.denfop.integration.jei.quarry.QuarryWrapper;
import com.denfop.integration.jei.quarry_comb.CMQuarryCategory;
import com.denfop.integration.jei.quarry_comb.CMQuarryWrapper;
import com.denfop.integration.jei.quarry_comb.СMQuarryHandler;
import com.denfop.integration.jei.quarry_mac.MQuarryCategory;
import com.denfop.integration.jei.quarry_mac.MQuarryHandler;
import com.denfop.integration.jei.quarry_mac.MQuarryWrapper;
import com.denfop.integration.jei.radioactiveorehandler.RadioactiveOreHandlerCategory;
import com.denfop.integration.jei.radioactiveorehandler.RadioactiveOreHandlerHandler;
import com.denfop.integration.jei.radioactiveorehandler.RadioactiveOreHandlerRecipeWrapper;
import com.denfop.integration.jei.recycler.RecyclerCategory;
import com.denfop.integration.jei.recycler.RecyclerHandler;
import com.denfop.integration.jei.recycler.RecyclerWrapper;
import com.denfop.integration.jei.refiner.RefinerCategory;
import com.denfop.integration.jei.refiner.RefinerHandler;
import com.denfop.integration.jei.refiner.RefinerRecipeWrapper;
import com.denfop.integration.jei.refractory_furnace.RefractoryFurnaceCategory;
import com.denfop.integration.jei.refractory_furnace.RefractoryFurnaceHandler;
import com.denfop.integration.jei.refractory_furnace.RefractoryFurnaceWrapper;
import com.denfop.integration.jei.refrigeratorfluids.RefrigeratorFluidsCategory;
import com.denfop.integration.jei.refrigeratorfluids.RefrigeratorFluidsHandler;
import com.denfop.integration.jei.refrigeratorfluids.RefrigeratorFluidsWrapper;
import com.denfop.integration.jei.replicator.ReplicatorCategory;
import com.denfop.integration.jei.replicator.ReplicatorHandler;
import com.denfop.integration.jei.replicator.ReplicatorWrapper;
import com.denfop.integration.jei.reversetransriptor.ReverseTransriptorCategory;
import com.denfop.integration.jei.reversetransriptor.ReverseTransriptorHandler;
import com.denfop.integration.jei.reversetransriptor.ReverseTransriptorWrapper;
import com.denfop.integration.jei.rna_collector.RNACollectorCategory;
import com.denfop.integration.jei.rna_collector.RNACollectorHandler;
import com.denfop.integration.jei.rna_collector.RNACollectorRecipeWrapper;
import com.denfop.integration.jei.rocketassembler.RocketAssemblerCategory;
import com.denfop.integration.jei.rocketassembler.RocketAssemblerHandler;
import com.denfop.integration.jei.rocketassembler.RocketAssemblerRecipeWrapper;
import com.denfop.integration.jei.rods_factory.RodFactoryCategory;
import com.denfop.integration.jei.rods_factory.RodFactoryHandler;
import com.denfop.integration.jei.rods_factory.RodFactoryRecipeWrapper;
import com.denfop.integration.jei.rolling.RollingCategory;
import com.denfop.integration.jei.rolling.RollingHandler;
import com.denfop.integration.jei.rolling.RollingWrapper;
import com.denfop.integration.jei.rotorrods.RotorsRodCategory;
import com.denfop.integration.jei.rotorrods.RotorsRodHandler;
import com.denfop.integration.jei.rotorrods.RotorsRodWrapper;
import com.denfop.integration.jei.rotors.RotorsCategory;
import com.denfop.integration.jei.rotors.RotorsHandler;
import com.denfop.integration.jei.rotors.RotorsWrapper;
import com.denfop.integration.jei.rotorsupgrade.RotorUpgradeCategory;
import com.denfop.integration.jei.rotorsupgrade.RotorUpgradeHandler;
import com.denfop.integration.jei.rotorsupgrade.RotorUpgradeWrapper;
import com.denfop.integration.jei.roverassembler.RoverAssemblerCategory;
import com.denfop.integration.jei.roverassembler.RoverAssemblerHandler;
import com.denfop.integration.jei.roverassembler.RoverAssemblerRecipeWrapper;
import com.denfop.integration.jei.rubbertree.RubberTreeCategory;
import com.denfop.integration.jei.rubbertree.RubberTreeHandler;
import com.denfop.integration.jei.rubbertree.RubberTreeWrapper;
import com.denfop.integration.jei.satelliteassembler.SatelliteAssemblerCategory;
import com.denfop.integration.jei.satelliteassembler.SatelliteAssemblerHandler;
import com.denfop.integration.jei.satelliteassembler.SatelliteAssemblerRecipeWrapper;
import com.denfop.integration.jei.sawmill.SawmillCategory;
import com.denfop.integration.jei.sawmill.SawmillHandler;
import com.denfop.integration.jei.sawmill.SawmillRecipeWrapper;
import com.denfop.integration.jei.scrap.ScrapCategory;
import com.denfop.integration.jei.scrap.ScrapHandler;
import com.denfop.integration.jei.scrap.ScrapRecipeWrapper;
import com.denfop.integration.jei.sharpener.SharpenerCategory;
import com.denfop.integration.jei.sharpener.SharpenerHandler;
import com.denfop.integration.jei.sharpener.SharpenerRecipeWrapper;
import com.denfop.integration.jei.siliconhandler.SiliconCategory;
import com.denfop.integration.jei.siliconhandler.SiliconHandler;
import com.denfop.integration.jei.siliconhandler.SiliconRecipeWrapper;
import com.denfop.integration.jei.singlefluidadapter.SingleFluidAdapterCategory;
import com.denfop.integration.jei.singlefluidadapter.SingleFluidAdapterHandler;
import com.denfop.integration.jei.singlefluidadapter.SingleFluidAdapterRecipeWrapper;
import com.denfop.integration.jei.smeltery_controller.SmelteryControllerCategory;
import com.denfop.integration.jei.smeltery_controller.SmelteryControllerHandler;
import com.denfop.integration.jei.smeltery_controller.SmelteryControllerRecipeWrapper;
import com.denfop.integration.jei.smelterycasting.SmelteryCastingCategory;
import com.denfop.integration.jei.smelterycasting.SmelteryCastingHandler;
import com.denfop.integration.jei.smelterycasting.SmelteryCastingRecipeWrapper;
import com.denfop.integration.jei.smelteryfurnace.SmelteryFurnaceCategory;
import com.denfop.integration.jei.smelteryfurnace.SmelteryFurnaceHandler;
import com.denfop.integration.jei.smelteryfurnace.SmelteryFurnaceRecipeWrapper;
import com.denfop.integration.jei.socket_factory.SocketCategory;
import com.denfop.integration.jei.socket_factory.SocketHandler;
import com.denfop.integration.jei.socket_factory.SocketRecipeWrapper;
import com.denfop.integration.jei.solidelectrolyzer.SolidElectrolyzerCategory;
import com.denfop.integration.jei.solidelectrolyzer.SolidElectrolyzerHandler;
import com.denfop.integration.jei.solidelectrolyzer.SolidElectrolyzerRecipeWrapper;
import com.denfop.integration.jei.solidfluidintegrator.SolidFluidIntegratorCategory;
import com.denfop.integration.jei.solidfluidintegrator.SolidFluidIntegratorHandler;
import com.denfop.integration.jei.solidfluidintegrator.SolidFluidIntegratorRecipeWrapper;
import com.denfop.integration.jei.solidmatters.MatterCategory;
import com.denfop.integration.jei.solidmatters.MatterHandler;
import com.denfop.integration.jei.solidmatters.MatterWrapper;
import com.denfop.integration.jei.solidmixer.SolidMixerCategory;
import com.denfop.integration.jei.solidmixer.SolidMixerHandler;
import com.denfop.integration.jei.solidmixer.SolidMixerRecipeWrapper;
import com.denfop.integration.jei.spacebody.SpaceBodyCategory;
import com.denfop.integration.jei.spacebody.SpaceBodyHandler;
import com.denfop.integration.jei.spacebody.SpaceBodyRecipeWrapper;
import com.denfop.integration.jei.squeezer.SqueezerCategory;
import com.denfop.integration.jei.squeezer.SqueezerHandler;
import com.denfop.integration.jei.squeezer.SqueezerWrapper;
import com.denfop.integration.jei.stamp.StampCategory;
import com.denfop.integration.jei.stamp.StampHandler;
import com.denfop.integration.jei.stamp.StampRecipeWrapper;
import com.denfop.integration.jei.sunnarium.SunnariumCategory;
import com.denfop.integration.jei.sunnarium.SunnariumHandler;
import com.denfop.integration.jei.sunnarium.SunnariumWrapper;
import com.denfop.integration.jei.sunnariumpanel.SannariumPanelCategory;
import com.denfop.integration.jei.sunnariumpanel.SannariumPanelHandler;
import com.denfop.integration.jei.sunnariumpanel.SannariumPanelWrapper;
import com.denfop.integration.jei.synthesis.SynthesisCategory;
import com.denfop.integration.jei.synthesis.SynthesisHandler;
import com.denfop.integration.jei.synthesis.SynthesisWrapper;
import com.denfop.integration.jei.triplesolidmixer.TripleSolidMixerCategory;
import com.denfop.integration.jei.triplesolidmixer.TripleSolidMixerHandler;
import com.denfop.integration.jei.triplesolidmixer.TripleSolidMixerRecipeWrapper;
import com.denfop.integration.jei.tuner.TunerCategory;
import com.denfop.integration.jei.tuner.TunerHandler;
import com.denfop.integration.jei.tuner.TunerWrapper;
import com.denfop.integration.jei.upgradeblock.UpgradeBlockCategory;
import com.denfop.integration.jei.upgradeblock.UpgradeBlockHandler;
import com.denfop.integration.jei.upgradeblock.UpgradeBlockWrapper;
import com.denfop.integration.jei.upgrademachine.UpgradeMachineCategory;
import com.denfop.integration.jei.upgrademachine.UpgradeMachineHandler;
import com.denfop.integration.jei.upgrademachine.UpgradeMachineRecipeWrapper;
import com.denfop.integration.jei.upgraderover.UpgradeRoverCategory;
import com.denfop.integration.jei.upgraderover.UpgradeRoverHandler;
import com.denfop.integration.jei.upgraderover.UpgradeRoverWrapper;
import com.denfop.integration.jei.vein.VeinCategory;
import com.denfop.integration.jei.vein.VeinHandler;
import com.denfop.integration.jei.vein.VeinWrapper;
import com.denfop.integration.jei.watergenerator.GenWaterCategory;
import com.denfop.integration.jei.watergenerator.GenWaterHandler;
import com.denfop.integration.jei.watergenerator.GenWaterWrapper;
import com.denfop.integration.jei.waterrotorrods.WaterRotorsCategory;
import com.denfop.integration.jei.waterrotorrods.WaterRotorsHandler;
import com.denfop.integration.jei.waterrotorrods.WaterRotorsWrapper;
import com.denfop.integration.jei.waterrotorsupgrade.WaterRotorUpgradeCategory;
import com.denfop.integration.jei.waterrotorsupgrade.WaterRotorUpgradeHandler;
import com.denfop.integration.jei.waterrotorsupgrade.WaterRotorUpgradeWrapper;
import com.denfop.integration.jei.welding.WeldingCategory;
import com.denfop.integration.jei.welding.WeldingHandler;
import com.denfop.integration.jei.welding.WeldingRecipeWrapper;
import com.denfop.integration.jei.wire_insulator.WireInsulatorCategory;
import com.denfop.integration.jei.wire_insulator.WireInsulatorHandler;
import com.denfop.integration.jei.wire_insulator.WireInsulatorRecipeWrapper;
import com.denfop.integration.jei.worldcollector.aer.AerCategory;
import com.denfop.integration.jei.worldcollector.aer.AerHandler;
import com.denfop.integration.jei.worldcollector.aer.AerWrapper;
import com.denfop.integration.jei.worldcollector.aqua.AquaCategory;
import com.denfop.integration.jei.worldcollector.aqua.AquaHandler;
import com.denfop.integration.jei.worldcollector.aqua.AquaWrapper;
import com.denfop.integration.jei.worldcollector.crystallize.CrystallizeCategory;
import com.denfop.integration.jei.worldcollector.crystallize.CrystallizeHandler;
import com.denfop.integration.jei.worldcollector.crystallize.CrystallizeWrapper;
import com.denfop.integration.jei.worldcollector.earth.EarthCategory;
import com.denfop.integration.jei.worldcollector.earth.EarthHandler;
import com.denfop.integration.jei.worldcollector.earth.EarthWrapper;
import com.denfop.integration.jei.worldcollector.end.EndCategory;
import com.denfop.integration.jei.worldcollector.end.EndHandler;
import com.denfop.integration.jei.worldcollector.end.EndWrapper;
import com.denfop.integration.jei.worldcollector.nether.NetherCategory;
import com.denfop.integration.jei.worldcollector.nether.NetherHandler;
import com.denfop.integration.jei.worldcollector.nether.NetherWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import java.util.Map;

@JEIPlugin
public final class JEICompat implements IModPlugin {

    private IIngredientRegistry itemRegistry;

    public static ItemStack getBlockStack(IMultiTileBlock block) {
        return TileBlockCreator.instance.get(block.getIDBlock()).getItemStack(block);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {

        registry.addRecipeCategories(new MolecularTransformerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new DoubleMolecularTransformerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new AlloySmelterCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new AdvAlloySmelterCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ImpAlloySmelterCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new PerAlloySmelterCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new MicrochipCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new EnrichCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SynthesisCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SannariumPanelCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SunnariumCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new BFCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new BlastFCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CokeOvenCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new RotorsRodCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new WaterRotorUpgradeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ExtractorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new PlasticCreatorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new RefrigeratorFluidsCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new PlasticCreatorPlateCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new UpgradeBlockCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new PaintingCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenStoneCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ModulatorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ModulatorCategory1(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new PrivatizerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new TunerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new FluidIntegratorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new MatterCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new AntiUpgradeBlockCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new VeinCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new OilPumpCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GasPumpCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new RotorUpgradeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new WeldingCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenStarCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new HandlerHOCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ElectrolyzerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenLavaCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenWaterCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenHeliumCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenNeuCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenPetrolCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenDieselCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenHydCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenObsCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new FishMCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new RotorsCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new WaterRotorsCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new RefinerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new AdvRefinerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ImpRefinerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new FarmerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ScrapCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CombMacCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new QuarryCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenSECategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ConverterCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new FQuarryCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ExtruderCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CuttingCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new RollingCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new MQuarryCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new AirColCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CMQuarryCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CrystallizeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new AerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new AquaCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new EarthCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new NetherCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new EndCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GearingCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CentrifugeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new MaceratorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CompressorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new RecyclerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new OreWashingCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GasGeneratorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CanningCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ScrapboxRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new BatteryCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SocketCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new MatteryCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new MoonSpooterCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new LaserCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new NeutronSeparatorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GraphiteCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new StampCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new RodFactoryCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new EnchantCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new MultiBlockCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new DepositsCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new EarthQuarryCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new AnvilCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new HeavyAnvilCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new UpgradeMachineCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new PrimalRollingCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new PolymerizerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new FluidSeparatorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SqueezerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new DryerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ItemDividerFluidCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new OilPurifierCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SolidElectrolyzerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GasCombinerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new FluidAdapterCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ItemDividerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new DistillerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenMatterCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new RubberTreeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ReplicatorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new PelletsCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GeneratorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenRedCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenRadCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenRad1Category(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ChemicalPlantCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GeoThermalCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new FluidMixerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new FluidSolidMixerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new HeaterFluidsCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GasChamberCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new PrimalFluidIntegratorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SiliconCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new PositronConverterCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SolidFluidIntegratorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SolidMixerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new TripleSolidMixerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SingleFluidAdapterCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ProgrammingTableCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new PrimalLaserCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ElectronicsCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CyclotronCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SmelteryFurnaceCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SmelteryControllerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SmelteryCastingCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SharpenerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new AlkalineEarthQuarryCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new RadioactiveOreHandlerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new IndustrialOrePurifierCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new QuantumMolecularCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new QuantumMinerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CropCrossoverCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CropCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new WireInsulatorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new RefractoryFurnaceCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new com.denfop.integration.jei.refractoryfurnace.RefractoryFurnaceCategory(registry
                .getJeiHelpers()
                .getGuiHelper()));
        registry.addRecipeCategories(new CrystalChargerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SawmillCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new BrewingCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenAddStoneCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new IncubatorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new InsulatorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new RNACollectorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new MutatronCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ReverseTransriptorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GeneticStabilizerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new com.denfop.integration.jei.bee_centrifuge.CentrifugeCategory(registry
                .getJeiHelpers()
                .getGuiHelper()));

        registry.addRecipeCategories(new GeneticReplicatorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new InoculatorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new MiniSmelteryCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GeneticTransposerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new RoverAssemblerCategory(registry.getJeiHelpers().getGuiHelper()));


        registry.addRecipeCategories(new ProbeAssemblerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SatelliteAssemblerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new RocketAssemblerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GasSensorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GasTurbineCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SpaceBodyCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SpaceColonyCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GasWellCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ApiaryCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new BeeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new AnalyzerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new UpgradeRoverCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ChargedRedstoneCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GeneticPolymizerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new BiomassCategory(registry.getJeiHelpers().getGuiHelper()));


    }

    public void register(IModRegistry registry) {
        itemRegistry = registry.getIngredientRegistry();
        registry.addRecipeHandlers(new BaseRecipeHandler());
        registry.addRecipeHandlers(new BaseShapelessHandler());
        registry.addRecipes(
                OreWashingHandler.getRecipes(),
                new OreWashingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCategoryCraftingItem(IUItem.scrapBox, Constants.MOD_ID + ".scrapbox");
        registry.addRecipeHandlers(new ScrapboxRecipeHandler());
        registry.addRecipes(ScrapboxRecipeWrapper.createRecipes());
        registry.handleRecipes(
                OreWashingHandler.class, OreWashingWrapper::new,
                BlockMoreMachine3.orewashing.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base3, 1, 8),
                new OreWashingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base3, 1, 9),
                new OreWashingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base3, 1, 10),
                new OreWashingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base3, 1, 11),
                new OreWashingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_orewashing),
                new OreWashingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.bio_orewashing),
                new OreWashingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                BatteryHandler.getRecipes(),
                new BatteryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                BatteryHandler.class, BatteryRecipeWrapper::new,
                BlockBaseMachine3.battery_factory.getName()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.battery_factory),
                new BatteryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                UpgradeMachineHandler.getRecipes(),
                new UpgradeMachineCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                UpgradeMachineHandler.class, UpgradeMachineRecipeWrapper::new,
                BlockBaseMachine3.upgrade_machine.getName()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.upgrade_machine),
                new UpgradeMachineCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                SocketHandler.getRecipes(),
                new SocketCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                SocketHandler.class, SocketRecipeWrapper::new,
                BlockBaseMachine3.socket_factory.getName()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.socket_factory),
                new SocketCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                MiniSmelteryHandler.getRecipes(),
                new MiniSmelteryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                MiniSmelteryHandler.class, MiniSmelteryWrapper::new,
                BlockMiniSmeltery.mini_smeltery.getName()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockMiniSmeltery.mini_smeltery),
                new MiniSmelteryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                GeneticTransposerHandler.getRecipes(),
                new GeneticTransposerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                GeneticTransposerHandler.class, GeneticTransposerWrapper::new,
                BlockBaseMachine3.genetic_transposer.getName()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.genetic_transposer),
                new GeneticTransposerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                GeneticPolymizerHandler.getRecipes(),
                new GeneticPolymizerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                GeneticPolymizerHandler.class, GeneticPolymizerWrapper::new,
                BlockBaseMachine3.genetic_polymerizer.getName()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.genetic_polymerizer),
                new GeneticPolymizerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                BiomassHandler.getRecipes(),
                new  BiomassCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                BiomassHandler.class, BiomassWrapper::new,
                BlockBaseMachine3.steam_bio_generator.getName()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_bio_generator),
                new BiomassCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                DepositsHandler.getRecipes(),
                new DepositsCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                DepositsHandler.class, DepositsWrapper::new,
                "deposists_iu"
        );

        registry.addRecipes(
                EarthQuarryHandler.getRecipes(),
                new EarthQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                EarthQuarryHandler.class, EarthQuarryWrapper::new,
                "earth_quarry_iu"
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockEarthQuarry.earth_controller),
                new EarthQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                AnvilHandler.getRecipes(),
                new AnvilCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                AnvilHandler.class, AnvilWrapper::new,
                BlockAnvil.block_anvil.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockAnvil.block_anvil),
                new AnvilCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                HeavyAnvilHandler.getRecipes(),
                new HeavyAnvilCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                HeavyAnvilHandler.class, HeavyAnvilWrapper::new,
                BlockStrongAnvil.block_strong_anvil.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockStrongAnvil.block_strong_anvil),
                new HeavyAnvilCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                RoverAssemblerHandler.getRecipes(),
                new RoverAssemblerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                RoverAssemblerHandler.class, RoverAssemblerRecipeWrapper::new,
                BlockBaseMachine3.rover_assembler.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.rover_assembler),
                new RoverAssemblerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                ProbeAssemblerHandler.getRecipes(),
                new ProbeAssemblerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                ProbeAssemblerHandler.class, ProbeAssemblerRecipeWrapper::new,
                BlockBaseMachine3.probe_assembler.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.probe_assembler),
                new ProbeAssemblerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                SatelliteAssemblerHandler.getRecipes(),
                new SatelliteAssemblerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                SatelliteAssemblerHandler.class, SatelliteAssemblerRecipeWrapper::new,
                BlockBaseMachine3.satellite_assembler.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.satellite_assembler),
                new SatelliteAssemblerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                RocketAssemblerHandler.getRecipes(),
                new RocketAssemblerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                RocketAssemblerHandler.class, RocketAssemblerRecipeWrapper::new,
                BlockBaseMachine3.rocket_assembler.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.rocket_assembler),
                new RocketAssemblerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                GasSensorHandler.getRecipes(),
                new GasSensorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                GasSensorHandler.class, GasSensorWrapper::new,
                "gas_sensor"
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.gasSensor),
                new GasSensorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                GasTurbineHandler.getRecipes(),
                new GasTurbineCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                GasTurbineHandler.class, GasTurbineWrapper::new,
                BlockGasTurbine.gas_turbine_controller.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockGasTurbine.gas_turbine_controller),
                new GasTurbineCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                GasWellHandler.getRecipes(),
                new  GasWellCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                GasWellHandler.class, GasWellWrapper::new,
                BlockGasWell.gas_well_controller.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockGasWell.gas_well_controller),
                new GasWellCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                ApiaryHandler.getRecipes(),
                new ApiaryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                ApiaryHandler.class, ApiaryWrapper::new,
                BlockApiary.apiary.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockApiary.apiary),
                new ApiaryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                AnalyzerHandler.getRecipes(),
                new  AnalyzerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                AnalyzerHandler.class,  AnalyzerWrapper::new,
                BlockBaseMachine2.analyzer.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine2.analyzer),
                new  AnalyzerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.wireless_mineral_quarry),
                new  AnalyzerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine.quantum_quarry),
                new  AnalyzerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine.imp_quantum_quarry),
                new  AnalyzerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine.adv_quantum_quarry),
                new  AnalyzerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine.per_quantum_quarry),
                new  AnalyzerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        if (Loader.isModLoaded("simplyquarries")){
            SimplyQuarriesJei.init(registry);
        }
        registry.addRecipes(
                BeeHandler.getRecipes(),
                new  BeeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                BeeHandler.class,  BeeWrapper::new,
                BlockHive.forest_hive.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockHive.forest_hive),
                new  BeeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockHive.winter_hive),
                new  BeeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockHive.tropical_hive),
                new  BeeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockHive.plains_hive),
                new  BeeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockHive.swamp_hive),
                new  BeeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                SpaceBodyHandler.getRecipes(),
                new SpaceBodyCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                SpaceBodyHandler.class, SpaceBodyRecipeWrapper::new,
                "spacebody_iu"
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.rover),
                new SpaceBodyCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.adv_rover),
                new SpaceBodyCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeCatalyst(
                new ItemStack(IUItem.imp_rover),
                new SpaceBodyCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.per_rover),
                new SpaceBodyCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeCatalyst(
                new ItemStack(IUItem.satellite),
                new SpaceBodyCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.adv_satellite),
                new SpaceBodyCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeCatalyst(
                new ItemStack(IUItem.imp_satellite),
                new SpaceBodyCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.per_satellite),
                new SpaceBodyCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.probe),
                new SpaceBodyCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.adv_probe),
                new SpaceBodyCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeCatalyst(
                new ItemStack(IUItem.imp_probe),
                new SpaceBodyCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.per_probe),
                new SpaceBodyCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.rocket),
                new SpaceBodyCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.adv_rocket),
                new SpaceBodyCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeCatalyst(
                new ItemStack(IUItem.imp_rocket),
                new SpaceBodyCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.per_rocket),
                new SpaceBodyCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                SpaceColonyHandler.getRecipes(),
                new SpaceColonyCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                SpaceColonyHandler.class, SpaceColonyRecipeWrapper::new,
                "spacecolony_iu"
        );
        for (int i = 7; i < 13; i++) {
            registry.addRecipeCatalyst(
                    new ItemStack(IUItem.colonial_building, 1, i),
                    new SpaceColonyCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
            );
        }
        registry.addRecipes(
                QuantumMinerHandler.getRecipes(),
                new QuantumMinerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                QuantumMinerHandler.class, QuantumMinerWrapper::new,
                BlockBaseMachine3.quantum_miner.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.quantum_miner),
                new QuantumMinerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                WireInsulatorHandler.getRecipes(),
                new WireInsulatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                WireInsulatorHandler.class, WireInsulatorRecipeWrapper::new,
                BlockBaseMachine3.electric_wire_insulator.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.electric_wire_insulator),
                new WireInsulatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_wire_insulator),
                new WireInsulatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockPrimalWireInsulator.primal_wire_insulator),
                new WireInsulatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                CrystalChargerHandler.getRecipes(),
                new CrystalChargerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                CrystalChargerHandler.class, CrystalChargerRecipeWrapper::new,
                BlockBaseMachine3.crystal_charge.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.crystal_charge),
                new CrystalChargerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_crystal_charge),
                new CrystalChargerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                SawmillHandler.getRecipes(),
                new SawmillCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                SawmillHandler.class, SawmillRecipeWrapper::new,
                BlockBaseMachine3.sawmill.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.sawmill),
                new SawmillCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                BrewingHandler.getRecipes(),
                new BrewingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                BrewingHandler.class, BrewingRecipeWrapper::new,
                BlockBaseMachine3.electric_brewing.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.electric_brewing),
                new BrewingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                IncubatorHandler.getRecipes(),
                new IncubatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                IncubatorHandler.class, IncubatorRecipeWrapper::new,
                BlockBaseMachine3.incubator.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.incubator),
                new IncubatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                InsulatorHandler.getRecipes(),
                new InsulatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                InsulatorHandler.class, InsulatorRecipeWrapper::new,
                BlockBaseMachine3.insulator.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.insulator),
                new InsulatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                RNACollectorHandler.getRecipes(),
                new RNACollectorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                RNACollectorHandler.class, RNACollectorRecipeWrapper::new,
                BlockBaseMachine3.rna_collector.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.rna_collector),
                new RNACollectorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                MutatronHandler.getRecipes(),
                new MutatronCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                MutatronHandler.class, MutatronRecipeWrapper::new,
                BlockBaseMachine3.mutatron.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.mutatron),
                new MutatronCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                ReverseTransriptorHandler.getRecipes(),
                new ReverseTransriptorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                ReverseTransriptorHandler.class, ReverseTransriptorWrapper::new,
                BlockBaseMachine3.reverse_transcriptor.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.reverse_transcriptor),
                new ReverseTransriptorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                GeneticStabilizerHandler.getRecipes(),
                new GeneticStabilizerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                GeneticStabilizerHandler.class, GeneticStabilizerRecipeWrapper::new,
                BlockBaseMachine3.genetic_stabilizer.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.genetic_stabilizer),
                new GeneticStabilizerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                InoculatorHandler.getRecipes(),
                new InoculatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                InoculatorHandler.class, InoculatorRecipeWrapper::new,
                BlockBaseMachine3.inoculator.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.inoculator),
                new InoculatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                GeneticReplicatorHandler.getRecipes(),
                new GeneticReplicatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                GeneticReplicatorHandler.class, GeneticReplicatorRecipeWrapper::new,
                BlockBaseMachine3.genetic_replicator.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.genetic_replicator),
                new GeneticReplicatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                com.denfop.integration.jei.bee_centrifuge.CentrifugeHandler.getRecipes(),
                new com.denfop.integration.jei.bee_centrifuge.CentrifugeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                com.denfop.integration.jei.bee_centrifuge.CentrifugeHandler.class,
                com.denfop.integration.jei.bee_centrifuge.CentrifugeRecipeWrapper::new,
                BlockBaseMachine3.centrifuge.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.centrifuge),
                new com.denfop.integration.jei.bee_centrifuge.CentrifugeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                com.denfop.integration.jei.refractoryfurnace.RefractoryFurnaceHandler.getRecipes(),
                new com.denfop.integration.jei.refractoryfurnace.RefractoryFurnaceCategory(registry
                        .getJeiHelpers()
                        .getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                com.denfop.integration.jei.refractoryfurnace.RefractoryFurnaceHandler.class,
                com.denfop.integration.jei.refractoryfurnace.RefractoryFurnaceWrapper::new,
                BlockRefractoryFurnace.refractory_furnace.getName()
        );


        registry.addRecipeCatalyst(
                getBlockStack(BlockRefractoryFurnace.refractory_furnace),
                new com.denfop.integration.jei.refractoryfurnace.RefractoryFurnaceCategory(registry
                        .getJeiHelpers()
                        .getGuiHelper()).getUid()
        );


        registry.addRecipes(
                RefractoryFurnaceHandler.getRecipes(),
                new RefractoryFurnaceCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                RefractoryFurnaceHandler.class, RefractoryFurnaceWrapper::new,
                BlockBaseMachine3.electric_refractory_furnace.getName()
        );


        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.electric_refractory_furnace),
                new RefractoryFurnaceCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                GenAddStoneHandler.getRecipes(),
                new GenAddStoneCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                GenAddStoneHandler.class, GenAddStoneRecipeWrapper::new,
                BlockBaseMachine3.gen_addition_stone.getName()
        );


        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.gen_addition_stone),
                new GenAddStoneCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                CropCrossoverHandler.getRecipes(),
                new CropCrossoverCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                CropCrossoverHandler.class, CropCrossoverWrapper::new,
                "iu.crop"
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.crop),
                new CropCrossoverCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                CropHandler.getRecipes(),
                new CropCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                CropHandler.class, CropWrapper::new,
                "iu.crop1"
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.crop),
                new CropCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                QuantumMolecularHandler.getRecipes(),
                new QuantumMolecularCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                QuantumMolecularHandler.class, QuantumMolecularRecipeWrapper::new,
                BlockBaseMachine3.quantum_transformer.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.quantum_transformer),
                new QuantumMolecularCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                PrimalRollingHandler.getRecipes(),
                new PrimalRollingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                PrimalRollingHandler.class, PrimalRollingWrapper::new,
                BlockBaseMachine3.rolling_machine.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.rolling_machine),
                new PrimalRollingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                MultiBlockHandler.getRecipes(),
                new MultiBlockCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                MultiBlockHandler.class, MultiBlockWrapper::new,
                "multiblock_iu"
        );
        for (Map.Entry<String, MultiBlockStructure> entry : MultiBlockSystem.getInstance().mapMultiBlocks.entrySet()) {
            registry.addRecipeCatalyst(
                    entry.getValue().itemStackList.get(0),
                    new MultiBlockCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
            );


        }


        registry.addRecipes(
                StampHandler.getRecipes(),
                new StampCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                StampHandler.class, StampRecipeWrapper::new,
                BlockBaseMachine3.stamp_mechanism.getName()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.stamp_mechanism),
                new StampCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                RodFactoryHandler.getRecipes(),
                new RodFactoryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                RodFactoryHandler.class, RodFactoryRecipeWrapper::new,
                BlockBaseMachine3.reactor_rod_factory.getName()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.reactor_rod_factory),
                new RodFactoryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                EnchantHandler.getRecipes(),
                new EnchantCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                EnchantHandler.class, EnchantRecipeWrapper::new,
                BlockBaseMachine3.enchanter_books.getName()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.enchanter_books),
                new EnchantCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                MatteryHandler.getRecipes(),
                new MatteryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                MatteryHandler.class, MatteryRecipeWrapper::new,
                BlockBaseMachine3.matter_factory.getName()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.matter_factory),
                new MatteryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                MoonSpooterHandler.getRecipes(),
                new MoonSpooterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                MoonSpooterHandler.class, MoonSpooterRecipeWrapper::new,
                BlockBaseMachine3.moon_spotter.getName()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.moon_spotter),
                new MoonSpooterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.night_converter),
                new MoonSpooterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                GraphiteHandler.getRecipes(),
                new GraphiteCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                GraphiteHandler.class, GraphiteRecipeWrapper::new,
                BlockBaseMachine3.graphite_handler.getName()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.graphite_handler),
                new GraphiteCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                LaserHandler.getRecipes(),
                new LaserCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                LaserHandler.class, LaserRecipeWrapper::new,
                BlockBaseMachine3.laser_polisher.getName()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.laser_polisher),
                new LaserCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                PositronConverterHandler.getRecipes(),
                new PositronConverterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                PositronConverterHandler.class, PositronConverterWrapper::new,
                BlockBaseMachine3.positronconverter.getName()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.positronconverter),
                new PositronConverterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                NeutronSeparatorHandler.getRecipes(),
                new NeutronSeparatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                NeutronSeparatorHandler.class, NeutronSeparatorRecipeWrapper::new,
                BlockBaseMachine3.neutronseparator.getName()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.neutronseparator),
                new NeutronSeparatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                GasGeneratorHandler.getRecipes(),
                new GasGeneratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                GasGeneratorHandler.class, GasGeneratorWrapper::new,
                BlockBaseMachine3.gas_generator.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.gas_generator),
                new GasGeneratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                CanningHandler.getRecipes(),
                new CanningCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                CanningHandler.class, CanningRecipeWrapper::new,
                BlockBaseMachine3.canner_iu.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.canner_iu),
                new CanningCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                CompressorHandler.getRecipes(),
                new CompressorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.handleRecipes(
                CompressorHandler.class, CompressorWrapper::new,
                BlockSimpleMachine.compressor_iu.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockCompressor.compressor),
                new CompressorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.simplemachine, 1, 1),
                new CompressorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base, 1, 3),
                new CompressorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base, 1, 4),
                new CompressorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_compressor),
                new CompressorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.bio_compressor),
                new CompressorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base, 1, 5),
                new CompressorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_compressor),
                new CompressorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                ExtractorHandler.getRecipes(),
                new ExtractorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                ExtractorHandler.class, ExtractorWrapper::new,
                BlockSimpleMachine.extractor_iu.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.simplemachine, 1, 3),
                new ExtractorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base, 1, 9),
                new ExtractorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base, 1, 10),
                new ExtractorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base, 1, 11),
                new ExtractorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_extractor),
                new ExtractorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.bio_extractor),
                new ExtractorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_extractor),
                new ExtractorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                CentrifugeHandler.getRecipes(),
                new CentrifugeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                CentrifugeHandler.class, CentrifugeWrapper::new,
                BlockMoreMachine3.centrifuge_iu.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base3, 1, 12),
                new CentrifugeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base3, 1, 13),
                new CentrifugeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base3, 1, 14),
                new CentrifugeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base3, 1, 15),
                new CentrifugeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_centrifuge),
                new CentrifugeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.bio_centrifuge),
                new CentrifugeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                MaceratorHandler.getRecipes(),
                new MaceratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                MaceratorHandler.class, MaceratorWrapper::new,
                BlockSimpleMachine.macerator_iu.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.simplemachine, 1, 0),
                new MaceratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockMacerator.macerator),
                new MaceratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base, 1, 0),
                new MaceratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base, 1, 1),
                new MaceratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base, 1, 2),
                new MaceratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_macerator),
                new MaceratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.bio_macerator),
                new MaceratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_macerator),
                new MaceratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeClickArea(GuiMolecularTransformer.class, 24, 76, 14, 20, BlockMolecular.molecular.getName());
        registry.addRecipes(
                MolecularTransformerHandler.getRecipes(),
                new MolecularTransformerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(MolecularTransformerHandler.class, MolecularTransformerRecipeWrapper::new,
                BlockMolecular.molecular.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.blockmolecular),
                new MolecularTransformerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeClickArea(
                GuiDoubleMolecularTransformer.class,
                24, 76, 14, 20,
                BlockDoubleMolecularTransfomer.double_transformer.getName()
        );
        registry.addRecipes(
                DoubleMolecularTransformerHandler.getRecipes(),
                new DoubleMolecularTransformerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(DoubleMolecularTransformerHandler.class, DoubleMolecularTransformerRecipeWrapper::new,
                BlockDoubleMolecularTransfomer.double_transformer.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.blockdoublemolecular),
                new DoubleMolecularTransformerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeClickArea(GuiAlloySmelter.class, 80, 35, 22, 14,
                BlockBaseMachine.alloy_smelter.getName()
        );
        registry.addRecipes(
                AlloySmelterHandler.getRecipes(),
                new AlloySmelterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(AlloySmelterHandler.class, AlloySmelterRecipeWrapper::new,
                BlockBaseMachine.alloy_smelter.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 4),
                new AlloySmelterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeClickArea(
                GuiRodManufacturer.class, 80, 35, 22, 14,
                BlockBaseMachine3.rods_manufacturer.getName()
        );
        registry.addRecipes(
                RotorsRodHandler.getRecipes(),
                new RotorsRodCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(RotorsRodHandler.class, RotorsRodWrapper::new,
                BlockBaseMachine3.rods_manufacturer.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine2, 1, 21),
                new RotorsRodCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                RotorsHandler.getRecipes(),
                new RotorsCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(RotorsHandler.class, RotorsWrapper::new,
                BlockBaseMachine3.rotor_assembler.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine2, 1, 17),
                new RotorsCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                WaterRotorsHandler.getRecipes(),
                new WaterRotorsCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                WaterRotorsHandler.class, WaterRotorsWrapper::new,
                BlockBaseMachine3.water_rotor_assembler.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.water_rotor_assembler),
                new WaterRotorsCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeClickArea(
                GuiAdvAlloySmelter.class, 80, 35, 22, 14,
                BlockBaseMachine1.adv_alloy_smelter.getName()
        );
        registry.addRecipes(
                AdvAlloySmelterHandler.getRecipes(),
                new AdvAlloySmelterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(AdvAlloySmelterHandler.class, AdvAlloySmelterRecipeWrapper::new,
                BlockBaseMachine1.adv_alloy_smelter.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine, 1, 3),
                new AdvAlloySmelterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                ImpAlloySmelterHandler.getRecipes(),
                new ImpAlloySmelterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(ImpAlloySmelterHandler.class, ImpAlloySmelterRecipeWrapper::new,
                BlockBaseMachine3.imp_alloy_smelter.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.imp_alloy_smelter),
                new ImpAlloySmelterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                PerAlloySmelterHandler.getRecipes(),
                new PerAlloySmelterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(PerAlloySmelterHandler.class, PerAlloySmelterRecipeWrapper::new,
                BlockBaseMachine3.per_alloy_smelter.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.per_alloy_smelter),
                new PerAlloySmelterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeClickArea(
                GuiGenerationMicrochip.class, 88, 18, 106 - 88, 34 - 18,
                BlockBaseMachine.generator_microchip.getName()
        );
        registry.addRecipes(
                MicrochipHandler.getRecipes(),
                new MicrochipCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(MicrochipHandler.class, MicrochipRecipeWrapper::new,
                BlockBaseMachine.generator_microchip.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 6),
                new MicrochipCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockSolderingMechanism.primal_soldering_mechanism),
                new MicrochipCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeClickArea(
                GuiFisher.class, 41, 45, 15, 13,
                BlockBaseMachine2.fisher.getName()
        );
        registry.addRecipes(
                FishMHandler.getRecipes(),
                new FishMCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                FishMHandler.class, FishMWrapper::new,
                BlockBaseMachine2.fisher.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine1, 1, 1),
                new FishMCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeClickArea(
                GuiEnriched.class, 67, 36, 15, 15,
                BlockBaseMachine1.enrichment.getName()
        );
        registry.addRecipes(
                EnrichHandler.getRecipes(),
                new EnrichCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(EnrichHandler.class, EnrichRecipeWrapper::new,
                BlockBaseMachine1.enrichment.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine, 1, 10),
                new EnrichCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeClickArea(
                GuiSynthesis.class, 82, 30, 25, 23,
                BlockBaseMachine1.synthesis.getName()
        );
        registry.addRecipes(
                SynthesisHandler.getRecipes(),
                new SynthesisCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(SynthesisHandler.class, SynthesisWrapper::new,
                BlockBaseMachine1.synthesis.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine, 1, 11),
                new SynthesisCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeClickArea(
                GuiSunnariumPanelMaker.class, 74, 34, 14, 14,
                BlockSunnariumPanelMaker.gen_sunnarium.getName()
        );
        registry.addRecipes(
                SannariumPanelHandler.getRecipes(),
                new SannariumPanelCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(SannariumPanelHandler.class, SannariumPanelWrapper::new,
                BlockSunnariumPanelMaker.gen_sunnarium.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.sunnariummaker, 1, 0),
                new SannariumPanelCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeClickArea(GuiSunnariumMaker.class, 55, 20, 17, 31,
                BlockSunnariumMaker.gen_sunnarium_plate.getName()
        );
        registry.addRecipes(
                SunnariumHandler.getRecipes(),
                new SunnariumCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(SunnariumHandler.class, SunnariumWrapper::new,
                BlockSunnariumMaker.gen_sunnarium_plate.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.sunnariumpanelmaker, 1, 0),
                new SunnariumCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeClickArea(
                GuiPlasticCreator.class, 80, 35, 22, 14,
                BlockBaseMachine2.plastic_creator.getName()
        );
        registry.addRecipes(
                PlasticCreatorHandler.getRecipes(),
                new PlasticCreatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                PlasticCreatorHandler.class, PlasticCreatorWrapper::new,
                BlockBaseMachine2.plastic_creator.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine1, 1, 11),
                new PlasticCreatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                RefrigeratorFluidsHandler.getRecipes(),
                new RefrigeratorFluidsCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                RefrigeratorFluidsHandler.class, RefrigeratorFluidsWrapper::new,
                BlockBaseMachine3.refrigerator_fluids.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.refrigerator_fluids),
                new RefrigeratorFluidsCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                PolymerizerHandler.getRecipes(),
                new PolymerizerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                PolymerizerHandler.class, PolymerizerWrapper::new,
                BlockBaseMachine3.polymerizer.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.polymerizer),
                new PolymerizerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipeClickArea(
                GuiPlasticPlateCreator.class, 80, 35, 22, 14,
                BlockBaseMachine2.plastic_plate_creator.getName()
        );
        registry.addRecipes(
                PlasticCreatorPlateHandler.getRecipes(),
                new PlasticCreatorPlateCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(PlasticCreatorPlateHandler.class, PlasticCreatorPlateWrapper::new,
                BlockBaseMachine2.plastic_plate_creator.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine1, 1, 13),
                new PlasticCreatorPlateCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                BlastFHandler.getRecipes(),
                new BlastFCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(BlastFHandler.class, BlastFWrapper::new,
                BlockBlastFurnace.blast_furnace_main.getName() + "1"
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.blastfurnace, 1, 0),
                new BlastFCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                CokeOvenHandler.getRecipes(),
                new CokeOvenCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(CokeOvenHandler.class, CokeOvenWrapper::new,
                BlockCokeOven.coke_oven_main.getName() + "1"
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.cokeoven, 1, 0),
                new CokeOvenCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.adv_cokeoven, 1, 0),
                new CokeOvenCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeClickArea(GuiUpgradeBlock.class, 81, 33, 27, 18,
                BlockUpgradeBlock.upgrade_block.getName()
        );
        registry.addRecipes(
                UpgradeBlockHandler.getRecipes(),
                new UpgradeBlockCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(UpgradeBlockHandler.class, UpgradeBlockWrapper::new,
                BlockUpgradeBlock.upgrade_block.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.upgradeblock, 1, 0),
                new UpgradeBlockCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                UpgradeRoverHandler.getRecipes(),
                new UpgradeRoverCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(UpgradeRoverHandler.class, UpgradeRoverWrapper::new,
                BlockBaseMachine3.upgrade_rover.getName()
        );
        registry.addRecipeCatalyst(
               JEICompat.getBlockStack( BlockBaseMachine3.upgrade_rover),
                new UpgradeRoverCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                ChargedRedstoneHandler.getRecipes(),
                new  ChargedRedstoneCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(ChargedRedstoneHandler.class, ChargedRedstoneWrapper::new,
                "charged_redstone"
        );

        registry.addRecipeClickArea(
                GuiPainting.class, 75, 34, 15, 15,
                BlockBaseMachine2.painter.getName()
        );
        registry.addRecipes(
                PaintingHandler.getRecipes(),
                new PaintingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(PaintingHandler.class, PaintingWrapper::new,
                BlockBaseMachine2.painter.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine1, 1, 3),
                new PaintingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                ModulatorHandler.getRecipes(),
                new ModulatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(ModulatorHandler.class, ModulatorWrapper::new,
                BlockBaseMachine.modulator.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 9),
                new ModulatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                OilPumpHandler.getRecipes(),
                new OilPumpCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                OilPumpHandler.class, OilPumpWrapper::new,
                BlockPetrolQuarry.petrol_quarry.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.oilgetter, 1),
                new OilPumpCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeCatalyst(
               JEICompat.getBlockStack(BlockBaseMachine3.wireless_oil_pump),
                new OilPumpCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                GasPumpHandler.getRecipes(),
                new GasPumpCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                GasPumpHandler.class, GasPumpWrapper::new,
                BlockBaseMachine3.gas_pump.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.gas_pump),
                new GasPumpCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.wireless_gas_pump),
                new GasPumpCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                AntiUpgradeBlockHandler.getRecipes(),
                new AntiUpgradeBlockCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(AntiUpgradeBlockHandler.class, AntiUpgradeBlockWrapper::new,
                BlockBaseMachine3.antiupgradeblock.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine2, 1, 8),
                new AntiUpgradeBlockCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                VeinHandler.getRecipes(),
                new VeinCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(VeinHandler.class, VeinWrapper::new,
                new ItemStack(IUItem.oilquarry).getUnlocalizedName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.oilquarry),
                new VeinCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                PrivatizerHandler.getRecipes(),
                new PrivatizerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(PrivatizerHandler.class, PrivatizerWrapper::new,
                BlockBaseMachine3.privatizer.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine2, 1, 2),
                new PrivatizerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                TunerHandler.getRecipes(),
                new TunerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(TunerHandler.class, TunerWrapper::new,
                BlockBaseMachine3.tuner.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine2, 1, 3),
                new TunerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                MatterHandler.getRecipes(),
                new MatterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(MatterHandler.class, MatterWrapper::new,
                BlockSolidMatter.solidmatter.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.solidmatter, 1, 0),
                new MatterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.solidmatter, 1, 1),
                new MatterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.solidmatter, 1, 2),
                new MatterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.solidmatter, 1, 3),
                new MatterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.solidmatter, 1, 4),
                new MatterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.solidmatter, 1, 5),
                new MatterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.solidmatter, 1, 6),
                new MatterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.solidmatter, 1, 7),
                new MatterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                ModulatorHandler.getRecipes(),
                new ModulatorCategory1(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(ModulatorHandler.class, ModulatorWrapper1::new,
                BlockBaseMachine.modulator.getName() + "1"
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 9),
                new ModulatorCategory1(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        //
        registry.addRecipes(
                BFHandler.getRecipes(),
                new BFCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                BFHandler.class, BFWrapper::new,
                BlockBlastFurnace.blast_furnace_main.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.blastfurnace, 1, 0),
                new BFCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        //
        registry.addRecipeClickArea(GuiGenStone.class, 64, 28, 16, 16,
                BlockBaseMachine.gen_stone.getName()
        );
        registry.addRecipes(
                GenStoneHandler.getRecipes(),
                new GenStoneCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(GenStoneHandler.class, GenStoneRecipeWrapper::new,
                BlockBaseMachine.gen_stone.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 7),
                new GenStoneCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeClickArea(GuiWitherMaker.class, 75, 33, 30, 21,
                BlockBaseMachine1.gen_wither.getName()
        );
        registry.addRecipes(
                GenStarHandler.getRecipes(),
                new GenStarCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(GenStarHandler.class, GenStarRecipeManager::new,
                BlockBaseMachine1.gen_wither.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine, 1, 13),
                new GenStarCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipeClickArea(
                GuiHandlerHeavyOre.class, 62, 37, 218 - 178, 14,
                BlockBaseMachine1.handler_ho.getName()
        );
        registry.addRecipes(
                HandlerHOHandler.getRecipes(),
                new HandlerHOCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(HandlerHOHandler.class, HandlerHORecipeWrapper::new,
                BlockBaseMachine1.handler_ho.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine, 1, 12),
                new HandlerHOCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.double_handlerho),
                new HandlerHOCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.triple_handlerho),
                new HandlerHOCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.quad_handlerho),
                new HandlerHOCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_handlerho),
                new HandlerHOCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_handler_ore),
                new HandlerHOCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeClickArea(GuiElectrolyzer.class, 33, 40, 22, 60,
                BlockBaseMachine2.electrolyzer_iu.getName()
        );
        registry.addRecipes(
                ElectrolyzerHandler.getRecipes(),
                new ElectrolyzerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(ElectrolyzerHandler.class, ElectrolyzerRecipeWrapper::new,
                BlockBaseMachine2.electrolyzer_iu.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine1, 1, 15),
                new ElectrolyzerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_electrolyzer),
                new ElectrolyzerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                GenLavaHandler.getRecipes(),
                new GenLavaCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(GenLavaHandler.class, GenLavaWrapper::new,
                BlockBaseMachine2.lava_gen.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine1, 1, 12),
                new GenLavaCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                GenWaterHandler.getRecipes(),
                new GenWaterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(GenWaterHandler.class, GenWaterWrapper::new,
                BlockBaseMachine3.watergenerator.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine2, 1, 9),
                new GenWaterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                GenHeliumHandler.getRecipes(),
                new GenHeliumCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(GenHeliumHandler.class, GenHeliumWrapper::new,
                BlockBaseMachine2.helium_generator.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine1, 1, 14),
                new GenHeliumCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                GenHydHandler.getRecipes(),
                new GenHydCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(GenHydHandler.class, GenHydWrapper::new,
                BlockBaseMachine2.gen_hyd.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine1, 1, 9),
                new GenHydCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        //
        registry.addRecipes(
                GenPetrolHandler.getRecipes(),
                new GenPetrolCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(GenPetrolHandler.class, GenPetrolWrapper::new,
                BlockBaseMachine2.gen_pet.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine1, 1, 5),
                new GenPetrolCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                GenDieselHandler.getRecipes(),
                new GenDieselCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(GenDieselHandler.class, GenDieselWrapper::new,
                BlockBaseMachine2.gen_disel.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine1, 1, 4),
                new GenDieselCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        //

        registry.addRecipes(
                GenNeuHandler.getRecipes(),
                new GenNeuCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(GenNeuHandler.class, GenNeuWrapper::new,
                BlockBaseMachine.neutron_generator.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 5),
                new GenNeuCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipeClickArea(GuiObsidianGenerator.class, 101, 34, 16, 16,
                BlockBaseMachine2.gen_obsidian.getName()
        );
        registry.addRecipes(
                GenObsHandler.getRecipes(),
                new GenObsCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(GenObsHandler.class, GenObsWrapper::new,
                BlockBaseMachine2.gen_obsidian.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine1, 1, 10),
                new GenObsCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipeClickArea(
                GuiOilRefiner.class, 10, 77, 42, 14,
                BlockRefiner.refiner.getName()
        );
        registry.addRecipes(
                RefinerHandler.getRecipes(),
                new RefinerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(RefinerHandler.class, RefinerRecipeWrapper::new,
                BlockRefiner.refiner.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.oilrefiner, 1, 0),
                new RefinerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                FluidIntegratorHandler.getRecipes(),
                new FluidIntegratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(FluidIntegratorHandler.class, FluidIntegratorRecipeWrapper::new,
                BlockBaseMachine3.fluid_integrator.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.fluid_integrator),
                new FluidIntegratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                ItemDividerFluidHandler.getRecipes(),
                new ItemDividerFluidCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(ItemDividerFluidHandler.class, ItemDividerFluidRecipeWrapper::new,
                BlockBaseMachine3.item_divider_to_fluid.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.item_divider_to_fluid),
                new ItemDividerFluidCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                SqueezerHandler.getRecipes(),
                new SqueezerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(SqueezerHandler.class, SqueezerWrapper::new,
                BlockSqueezer.squeezer.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockSqueezer.squeezer),
                new SqueezerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.electric_squeezer),
                new SqueezerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.steam_squeezer),
                new SqueezerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                DryerHandler.getRecipes(),
                new DryerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(DryerHandler.class, DryerWrapper::new,
                BlockDryer.dryer.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockDryer.dryer),
                new DryerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.electric_dryer),
                new DryerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.steamdryer),
                new DryerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                FluidSeparatorHandler.getRecipes(),
                new FluidSeparatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(FluidSeparatorHandler.class, FluidSeparatorRecipeWrapper::new,
                BlockBaseMachine3.fluid_separator.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.fluid_separator),
                new FluidSeparatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                SolidFluidIntegratorHandler.getRecipes(),
                new SolidFluidIntegratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(SolidFluidIntegratorHandler.class, SolidFluidIntegratorRecipeWrapper::new,
                BlockBaseMachine3.solid_fluid_integrator.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.solid_fluid_integrator),
                new SolidFluidIntegratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                SolidMixerHandler.getRecipes(),
                new SolidMixerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(SolidMixerHandler.class, SolidMixerRecipeWrapper::new,
                BlockBaseMachine3.solid_mixer.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.solid_mixer),
                new SolidMixerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                TripleSolidMixerHandler.getRecipes(),
                new TripleSolidMixerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(TripleSolidMixerHandler.class, TripleSolidMixerRecipeWrapper::new,
                BlockBaseMachine3.triple_solid_mixer.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.triple_solid_mixer),
                new TripleSolidMixerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                SingleFluidAdapterHandler.getRecipes(),
                new SingleFluidAdapterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(SingleFluidAdapterHandler.class, SingleFluidAdapterRecipeWrapper::new,
                BlockBaseMachine3.single_fluid_adapter.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.single_fluid_adapter),
                new SingleFluidAdapterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                ProgrammingTableHandler.getRecipes(),
                new ProgrammingTableCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(ProgrammingTableHandler.class, ProgrammingTableRecipeWrapper::new,
                BlockBaseMachine3.programming_table.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.programming_table),
                new ProgrammingTableCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockPrimalProgrammingTable.primal_programming_table),
                new ProgrammingTableCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                PrimalLaserHandler.getRecipes(),
                new PrimalLaserCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(PrimalLaserHandler.class, PrimalLaserRecipeWrapper::new,
                BlockPrimalLaserPolisher.primal_laser_polisher.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockPrimalLaserPolisher.primal_laser_polisher),
                new PrimalLaserCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                CyclotronHandler.getRecipes(),
                new CyclotronCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(CyclotronHandler.class, CyclotronWrapper::new,
                BlockCyclotron.cyclotron_controller.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockCyclotron.cyclotron_controller),
                new CyclotronCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                ElectronicsHandler.getRecipes(),
                new ElectronicsCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(ElectronicsHandler.class, ElectronicsRecipeWrapper::new,
                BlockBaseMachine3.electronic_assembler.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.electronic_assembler),
                new ElectronicsCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockElectronicsAssembler.electronics_assembler),
                new ElectronicsCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                HeaterFluidsHandler.getRecipes(),
                new HeaterFluidsCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(HeaterFluidsHandler.class, HeaterFluidsWrapper::new,
                BlockBaseMachine3.fluid_heater.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.fluid_heater),
                new HeaterFluidsCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockPrimalFluidHeater.primal_fluid_heater),
                new HeaterFluidsCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                PrimalFluidIntegratorHandler.getRecipes(),
                new PrimalFluidIntegratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.handleRecipes(PrimalFluidIntegratorHandler.class, PrimalFluidIntegratorRecipeWrapper::new,
                BlockPrimalFluidIntegrator.primal_fluid_integrator.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockPrimalFluidIntegrator.primal_fluid_integrator),
                new PrimalFluidIntegratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                SiliconHandler.getRecipes(),
                new SiliconCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.handleRecipes(SiliconHandler.class, SiliconRecipeWrapper::new,
                BlockBaseMachine3.silicon_crystal_handler.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.silicon_crystal_handler),
                new SiliconCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockPrimalSiliconCrystalHandler.primal_silicon_crystal_handler),
                new SiliconCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                GasChamberHandler.getRecipes(),
                new GasChamberCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(GasChamberHandler.class, GasChamberRecipeWrapper::new,
                BlockGasChamber.primal_gas_chamber.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockGasChamber.primal_gas_chamber),
                new GasChamberCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                OilPurifierHandler.getRecipes(),
                new OilPurifierCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(OilPurifierHandler.class, OilPurifierRecipeWrapper::new,
                BlockBaseMachine3.oil_purifier.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.oil_purifier),
                new OilPurifierCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                SmelteryFurnaceHandler.getRecipes(),
                new SmelteryFurnaceCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(SmelteryFurnaceHandler.class, SmelteryFurnaceRecipeWrapper::new,
                BlockSmeltery.smeltery_furnace.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockSmeltery.smeltery_furnace),
                new SmelteryFurnaceCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                SmelteryControllerHandler.getRecipes(),
                new SmelteryControllerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(SmelteryControllerHandler.class, SmelteryControllerRecipeWrapper::new,
                BlockSmeltery.smeltery_controller.getName() + "1"
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockSmeltery.smeltery_controller),
                new SmelteryControllerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                SmelteryCastingHandler.getRecipes(),
                new SmelteryCastingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(SmelteryCastingHandler.class, SmelteryCastingRecipeWrapper::new,

                BlockSmeltery.smeltery_casting.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockSmeltery.smeltery_casting),
                new SmelteryCastingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                AlkalineEarthQuarryHandler.getRecipes(),
                new AlkalineEarthQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(AlkalineEarthQuarryHandler.class, AlkalineEarthQuarryWrapper::new,

                BlockBaseMachine3.alkalineearthquarry.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.alkalineearthquarry),
                new AlkalineEarthQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                RadioactiveOreHandlerHandler.getRecipes(),
                new RadioactiveOreHandlerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(RadioactiveOreHandlerHandler.class, RadioactiveOreHandlerRecipeWrapper::new,

                BlockBaseMachine3.radioactive_handler_ore.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.radioactive_handler_ore),
                new RadioactiveOreHandlerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                IndustrialOrePurifierHandler.getRecipes(),
                new IndustrialOrePurifierCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(IndustrialOrePurifierHandler.class, IndustrialOrePurifierRecipeWrapper::new,

                BlockBaseMachine3.industrial_ore_purifier.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.industrial_ore_purifier),
                new IndustrialOrePurifierCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                SharpenerHandler.getRecipes(),
                new SharpenerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(SharpenerHandler.class, SharpenerRecipeWrapper::new,

                BlockBaseMachine3.steam_sharpener.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.steam_sharpener),
                new SharpenerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                SolidElectrolyzerHandler.getRecipes(),
                new SolidElectrolyzerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(SolidElectrolyzerHandler.class, SolidElectrolyzerRecipeWrapper::new,
                BlockBaseMachine3.solid_state_electrolyzer.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.solid_state_electrolyzer),
                new SolidElectrolyzerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                GasCombinerHandler.getRecipes(),
                new GasCombinerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(GasCombinerHandler.class, GasCombinerRecipeWrapper::new,
                BlockBaseMachine3.gas_combiner.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.gas_combiner),
                new GasCombinerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                FluidMixerHandler.getRecipes(),
                new FluidMixerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(FluidMixerHandler.class, FluidMixerRecipeWrapper::new,
                BlockBaseMachine3.fluid_mixer.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.fluid_mixer),
                new FluidMixerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                FluidSolidMixerHandler.getRecipes(),
                new FluidSolidMixerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(FluidSolidMixerHandler.class, FluidSolidMixerRecipeWrapper::new,
                BlockBaseMachine3.solid_fluid_mixer.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.solid_fluid_mixer),
                new FluidSolidMixerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.steam_solid_fluid_mixer),
                new FluidSolidMixerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                FluidAdapterHandler.getRecipes(),
                new FluidAdapterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(FluidAdapterHandler.class, FluidAdapterRecipeWrapper::new,
                BlockBaseMachine3.fluid_adapter.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.fluid_adapter),
                new FluidAdapterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                ItemDividerHandler.getRecipes(),
                new ItemDividerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(ItemDividerHandler.class, ItemDividerRecipeWrapper::new,
                BlockBaseMachine3.item_divider.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.item_divider),
                new ItemDividerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                DistillerHandler.getRecipes(),
                new DistillerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(DistillerHandler.class, DistillerWrapper::new,
                BlockBaseMachine3.solardestiller.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.solardestiller),
                new DistillerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.adv_solar_destiller),
                new DistillerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.imp_solar_destiller),
                new DistillerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.per_solar_destiller),
                new DistillerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_destiller),
                new DistillerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                ReplicatorHandler.getRecipes(),
                new ReplicatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(ReplicatorHandler.class, ReplicatorWrapper::new,
                BlockBaseMachine3.replicator_iu.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.replicator_iu),
                new ReplicatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.adv_replicator),
                new ReplicatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.imp_replicator),
                new ReplicatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.per_replicator),
                new ReplicatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_replicator),
                new ReplicatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                RubberTreeHandler.getRecipes(),
                new RubberTreeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(RubberTreeHandler.class, RubberTreeWrapper::new,
                "jei.raw_latex"
        );


        registry.addRecipes(
                PelletsHandler.getRecipes(),
                new PelletsCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(PelletsHandler.class, PelletsWrapper::new,
                BlockBaseMachine3.pallet_generator.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.pallet_generator),
                new PelletsCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                ChemicalPlantHandler.getRecipes(),
                new ChemicalPlantCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(ChemicalPlantHandler.class, ChemicalPlantWrapper::new,
                BlockChemicalPlant.chemical_plant_controller.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockChemicalPlant.chemical_plant_controller),
                new ChemicalPlantCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                GeoThermalHandler.getRecipes(),
                new GeoThermalCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(GeoThermalHandler.class, GeoThermalWrapper::new,
                BlockGeothermalPump.geothermal_controller.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockGeothermalPump.geothermal_controller),
                new GeoThermalCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                GenRad1Handler.getRecipes(),
                new GenRad1Category(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(GenRad1Handler.class, GenRad1Wrapper::new,
                BlockBaseMachine3.radiation_purifier.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.radiation_purifier),
                new GenRad1Category(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                GenRadHandler.getRecipes(),
                new GenRadCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(GenRadHandler.class, GenRadWrapper::new,
                BlockBaseMachine3.nuclear_waste_recycler.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.nuclear_waste_recycler),
                new GenRadCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                GeneratorHandler.getRecipes(),
                new GeneratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(GeneratorHandler.class, GeneratorWrapper::new,
                BlockBaseMachine3.geogenerator_iu.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.geogenerator_iu),
                new GeneratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine1.adv_geo),
                new GeneratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine1.imp_geo),
                new GeneratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine1.per_geo),
                new GeneratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_geogenerator),
                new GeneratorCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                GenRedHandler.getRecipes(),
                new GenRedCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.handleRecipes(GenRedHandler.class, GenRedWrapper::new,
                BlockBaseMachine3.redstone_generator.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.redstone_generator),
                new GenRedCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.adv_redstone_generator),
                new GenRedCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.imp_redstone_generator),
                new GenRedCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine3.per_redstone_generator),
                new GenRedCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_redstone_generator),
                new GenRedCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                GenMatterHandler.getRecipes(),
                new GenMatterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(GenMatterHandler.class, GenMatterWrapper::new,
                BlockSimpleMachine.generator_matter.getName()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockSimpleMachine.generator_matter),
                new GenMatterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine.per_matter),
                new GenMatterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine.imp_matter),
                new GenMatterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockBaseMachine.adv_matter),
                new GenMatterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_gen_matter),
                new GenMatterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

//

        registry.addRecipes(
                RotorUpgradeHandler.getRecipes(),
                new RotorUpgradeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.handleRecipes(RotorUpgradeHandler.class, RotorUpgradeWrapper::new,
                BlockBaseMachine3.rotor_modifier.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine2, 1, 18),
                new RotorUpgradeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        //
        registry.addRecipes(
                WaterRotorUpgradeHandler.getRecipes(),
                new WaterRotorUpgradeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(WaterRotorUpgradeHandler.class, WaterRotorUpgradeWrapper::new,
                BlockBaseMachine3.water_modifier.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine2, 1, 43),
                new WaterRotorUpgradeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        //
        registry.addRecipeClickArea(
                GuiAdvOilRefiner.class, 10, 77, 42, 14,
                BlockAdvRefiner.adv_refiner.getName()
        );
        registry.addRecipes(
                AdvRefinerHandler.getRecipes(),
                new AdvRefinerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(AdvRefinerHandler.class, AdvRefinerRecipeWrapper::new,
                BlockAdvRefiner.adv_refiner.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.oiladvrefiner, 1, 0),
                new AdvRefinerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        //
        registry.addRecipeClickArea(
                GuiImpOilRefiner.class, 10, 77, 42, 14,
                BlockBaseMachine3.imp_refiner.getName()
        );
        registry.addRecipes(
                ImpRefinerHandler.getRecipes(),
                new ImpRefinerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(ImpRefinerHandler.class, ImpRefinerRecipeWrapper::new,
                BlockBaseMachine3.imp_refiner.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.imp_refiner),
                new ImpRefinerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                FarmerHandler.getRecipes(),
                new FarmerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(FarmerHandler.class, FarmerRecipeWrapper::new,
                BlockMoreMachine3.farmer.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base3, 1, 0),
                new FarmerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base3, 1, 1),
                new FarmerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base3, 1, 2),
                new FarmerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base3, 1, 3),
                new FarmerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_fermer),
                new FarmerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                GearingHandler.getRecipes(),
                new GearingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.handleRecipes(GearingHandler.class, GearingWrapper::new,
                BlockMoreMachine3.gearing.getName()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockMoreMachine3.gearing),
                new GearingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockMoreMachine3.doublegearing),
                new GearingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockMoreMachine3.triplegearing),
                new GearingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockMoreMachine3.quadgearing),
                new GearingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_gearing),
                new GearingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.bio_gearing),
                new GearingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                ScrapHandler.getRecipes(),
                new ScrapCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(ScrapHandler.class, ScrapRecipeWrapper::new,
                BlockMoreMachine3.assamplerscrap.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base3, 1, 4),
                new ScrapCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base3, 1, 5),
                new ScrapCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base3, 1, 6),
                new ScrapCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base3, 1, 7),
                new ScrapCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_assembler),
                new ScrapCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                CombMacHandler.getRecipes(),
                new CombMacCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(CombMacHandler.class, CombMacRecipeWrapper::new,
                BlockMoreMachine1.comb_macerator.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base1, 1, 6),
                new CombMacCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base1, 1, 7),
                new CombMacCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base1, 1, 8),
                new CombMacCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base1, 1, 9),
                new CombMacCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_comb_mac),
                new CombMacCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                QuarryHandler.getRecipes(),
                new QuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(QuarryHandler.class, QuarryWrapper::new,
                BlockBaseMachine.quantum_quarry.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 8),
                new QuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 13),
                new QuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 14),
                new QuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 15),
                new QuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );


        registry.addRecipes(
                GenSEHandler.getRecipes(),
                new GenSECategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(GenSEHandler.class, GenSEWrapper::new,
                BlockSolarEnergy.se_gen.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.blockSE, 1, 0),
                new GenSECategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.imp_se_generator, 1, 0),
                new GenSECategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.adv_se_generator, 1, 0),
                new GenSECategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                AirColHandler.getRecipes(),
                new AirColCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(AirColHandler.class, AirColRecipeWrapper::new,
                BlockBaseMachine3.aircollector.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine2, 1, 11),
                new AirColCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeClickArea(GuiConverterSolidMatter.class, 43, 47, 111 - 78, 17,
                BlockConverterMatter.converter_matter.getName()
        );
        registry.addRecipes(
                ConverterHandler.getRecipes(),
                new ConverterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(ConverterHandler.class, ConverterWrapper::new,
                BlockConverterMatter.converter_matter.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.convertersolidmatter, 1, 0),
                new ConverterCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                FQuarryHandler.getRecipes(),
                new FQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(FQuarryHandler.class, FQuarryWrapper::new,
                BlockBaseMachine.quantum_quarry.getName() + "1"
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 8),
                new FQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 13),
                new FQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 14),
                new FQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 15),
                new FQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                MQuarryHandler.getRecipes(),
                new MQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(MQuarryHandler.class, MQuarryWrapper::new,
                BlockBaseMachine.quantum_quarry.getName() + "3"
        );

        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 8),
                new MQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 13),
                new MQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 14),
                new MQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 15),
                new MQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                СMQuarryHandler.getRecipes(),
                new CMQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(СMQuarryHandler.class, CMQuarryWrapper::new,
                BlockBaseMachine.quantum_quarry.getName() + "4"
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 8),
                new CMQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 13),
                new CMQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 14),
                new CMQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines, 1, 15),
                new CMQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                ExtruderHandler.getRecipes(),
                new ExtruderCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(ExtruderHandler.class, ExtruderWrapper::new,
                BlockMoreMachine2.extruder.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base2, 1, 4),
                new ExtruderCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base2, 1, 5),
                new ExtruderCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base2, 1, 6),
                new ExtruderCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base2, 1, 7),
                new ExtruderCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_extruding),
                new ExtruderCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.bio_extruder),
                new ExtruderCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_extruder),
                new ExtruderCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                CuttingHandler.getRecipes(),
                new CuttingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(CuttingHandler.class, CuttingWrapper::new,
                BlockMoreMachine2.cutting.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base2, 1, 8),
                new CuttingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base2, 1, 9),
                new CuttingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base2, 1, 10),
                new CuttingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base2, 1, 11),
                new CuttingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_cutting),
                new CuttingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.bio_cutting),
                new CuttingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_cutting),
                new CuttingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                RollingHandler.getRecipes(),
                new RollingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(RollingHandler.class, RollingWrapper::new,
                BlockMoreMachine2.rolling.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base2, 1, 0),
                new RollingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base2, 1, 1),
                new RollingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_rolling),
                new RollingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.rolling_machine),
                new RollingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.steam_rolling),
                new RollingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base2, 1, 2),
                new RollingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base2, 1, 3),
                new RollingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        //
        registry.addRecipeCatalyst(getBlockStack(BlockMoreMachine.double_furnace), "minecraft.smelting");

        registry.addRecipeCatalyst(getBlockStack(BlockMoreMachine.triple_furnace), "minecraft.smelting");
        registry.addRecipeCatalyst(getBlockStack(BlockMoreMachine.quad_furnace), "minecraft.smelting");
        registry.addRecipeCatalyst(getBlockStack(BlockSimpleMachine.furnace_iu), "minecraft.smelting");
        registry.addRecipeCatalyst(       getBlockStack(BlocksPhotonicMachine.photonic_furnace), "minecraft.smelting");
        registry.addRecipeCatalyst(       getBlockStack(BlockBaseMachine3.bio_furnace), "minecraft.smelting");


        final IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();


        registry.addRecipes(
                CrystallizeHandler.getRecipes(),
                new CrystallizeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(CrystallizeHandler.class, CrystallizeWrapper::new,
                BlockBaseMachine3.crystallize.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine2, 1, 39),
                new CrystallizeCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                NetherHandler.getRecipes(),
                new NetherCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(NetherHandler.class, NetherWrapper::new,
                BlockBaseMachine3.nether_assembler.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine2, 1, 37),
                new NetherCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                AerHandler.getRecipes(),
                new AerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(AerHandler.class, AerWrapper::new,
                BlockBaseMachine3.aer_assembler.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine2, 1, 34),
                new AerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                AquaHandler.getRecipes(),
                new AquaCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(AquaHandler.class, AquaWrapper::new,
                BlockBaseMachine3.aqua_assembler.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine2, 1, 35),
                new AquaCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                EarthHandler.getRecipes(),
                new EarthCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(EarthHandler.class, EarthWrapper::new,
                BlockBaseMachine3.earth_assembler.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine2, 1, 36),
                new EarthCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                EndHandler.getRecipes(),
                new EndCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(EndHandler.class, EndWrapper::new,
                BlockBaseMachine3.ender_assembler.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.basemachine2, 1, 23),
                new EndCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipeClickArea(
                GuiWelding.class, 80, 35, 22, 14,
                BlockBaseMachine3.welding.getName()
        );
        registry.addRecipes(
                WeldingHandler.getRecipes(),
                new WeldingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(WeldingHandler.class, WeldingRecipeWrapper::new,
                BlockBaseMachine3.welding.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockBaseMachine3.welding),
                new WeldingCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipes(
                RecyclerHandler.getRecipes(),
                new RecyclerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                RecyclerHandler.class, RecyclerWrapper::new,
                BlockSimpleMachine.recycler_iu.getName()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.simplemachine, 1, 5),
                new RecyclerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base1, 1, 0),
                new RecyclerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base1, 1, 1),
                new RecyclerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base1, 1, 2),
                new RecyclerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base1, 1, 3),
                new RecyclerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base1, 1, 4),
                new RecyclerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                new ItemStack(IUItem.machines_base1, 1, 5),
                new RecyclerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlocksPhotonicMachine.photonic_comb_recycler),
                new RecyclerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

    }

    public void onRuntimeAvailable(@Nonnull IJeiRuntime iJeiRuntime) {


    }


}
