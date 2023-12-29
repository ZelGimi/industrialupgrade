package com.denfop.integration.jei;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.api.multiblock.MultiBlockSystem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockAnvil;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.mechanism.*;
import com.denfop.gui.*;
import com.denfop.integration.jei.advalloysmelter.AdvAlloySmelterCategory;
import com.denfop.integration.jei.advalloysmelter.AdvAlloySmelterHandler;
import com.denfop.integration.jei.advalloysmelter.AdvAlloySmelterRecipeWrapper;
import com.denfop.integration.jei.advrefiner.AdvRefinerCategory;
import com.denfop.integration.jei.advrefiner.AdvRefinerHandler;
import com.denfop.integration.jei.advrefiner.AdvRefinerRecipeWrapper;
import com.denfop.integration.jei.aircollector.AirColCategory;
import com.denfop.integration.jei.aircollector.AirColHandler;
import com.denfop.integration.jei.aircollector.AirColRecipeWrapper;
import com.denfop.integration.jei.alloysmelter.AlloySmelterCategory;
import com.denfop.integration.jei.alloysmelter.AlloySmelterHandler;
import com.denfop.integration.jei.alloysmelter.AlloySmelterRecipeWrapper;
import com.denfop.integration.jei.antiupgradeblock.AntiUpgradeBlockCategory;
import com.denfop.integration.jei.antiupgradeblock.AntiUpgradeBlockHandler;
import com.denfop.integration.jei.antiupgradeblock.AntiUpgradeBlockWrapper;
import com.denfop.integration.jei.anvil.AnvilCategory;
import com.denfop.integration.jei.anvil.AnvilHandler;
import com.denfop.integration.jei.anvil.AnvilWrapper;
import com.denfop.integration.jei.battery_factory.BatteryCategory;
import com.denfop.integration.jei.battery_factory.BatteryHandler;
import com.denfop.integration.jei.battery_factory.BatteryRecipeWrapper;
import com.denfop.integration.jei.bf.BlastFCategory;
import com.denfop.integration.jei.bf.BlastFHandler;
import com.denfop.integration.jei.bf.BlastFWrapper;
import com.denfop.integration.jei.blastfurnace.BFCategory;
import com.denfop.integration.jei.blastfurnace.BFHandler;
import com.denfop.integration.jei.blastfurnace.BFWrapper;
import com.denfop.integration.jei.canning.CanningCategory;
import com.denfop.integration.jei.canning.CanningHandler;
import com.denfop.integration.jei.canning.CanningRecipeWrapper;
import com.denfop.integration.jei.centrifuge.CentrifugeCategory;
import com.denfop.integration.jei.centrifuge.CentrifugeHandler;
import com.denfop.integration.jei.centrifuge.CentrifugeWrapper;
import com.denfop.integration.jei.combmac.CombMacCategory;
import com.denfop.integration.jei.combmac.CombMacHandler;
import com.denfop.integration.jei.combmac.CombMacRecipeWrapper;
import com.denfop.integration.jei.compressor.CompressorCategory;
import com.denfop.integration.jei.compressor.CompressorHandler;
import com.denfop.integration.jei.compressor.CompressorWrapper;
import com.denfop.integration.jei.convertermatter.ConverterCategory;
import com.denfop.integration.jei.convertermatter.ConverterHandler;
import com.denfop.integration.jei.convertermatter.ConverterWrapper;
import com.denfop.integration.jei.cutting.CuttingCategory;
import com.denfop.integration.jei.cutting.CuttingHandler;
import com.denfop.integration.jei.cutting.CuttingWrapper;
import com.denfop.integration.jei.deposits.DepositsCategory;
import com.denfop.integration.jei.deposits.DepositsHandler;
import com.denfop.integration.jei.deposits.DepositsWrapper;
import com.denfop.integration.jei.deposits.EarthQuarryWrapper;
import com.denfop.integration.jei.doublemolecular.DoubleMolecularTransformerCategory;
import com.denfop.integration.jei.doublemolecular.DoubleMolecularTransformerHandler;
import com.denfop.integration.jei.doublemolecular.DoubleMolecularTransformerRecipeWrapper;
import com.denfop.integration.jei.earthquarry.EarthQuarryCategory;
import com.denfop.integration.jei.earthquarry.EarthQuarryHandler;
import com.denfop.integration.jei.electrolyzer.ElectrolyzerCategory;
import com.denfop.integration.jei.electrolyzer.ElectrolyzerHandler;
import com.denfop.integration.jei.electrolyzer.ElectrolyzerRecipeWrapper;
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
import com.denfop.integration.jei.fquarry.FQuarryCategory;
import com.denfop.integration.jei.fquarry.FQuarryHandler;
import com.denfop.integration.jei.fquarry.FQuarryWrapper;
import com.denfop.integration.jei.gasgenerator.GasGeneratorCategory;
import com.denfop.integration.jei.gasgenerator.GasGeneratorHandler;
import com.denfop.integration.jei.gasgenerator.GasGeneratorWrapper;
import com.denfop.integration.jei.gearing.GearingCategory;
import com.denfop.integration.jei.gearing.GearingHandler;
import com.denfop.integration.jei.gearing.GearingWrapper;
import com.denfop.integration.jei.gendiesel.GenDieselCategory;
import com.denfop.integration.jei.gendiesel.GenDieselHandler;
import com.denfop.integration.jei.gendiesel.GenDieselWrapper;
import com.denfop.integration.jei.genhelium.GenHeliumCategory;
import com.denfop.integration.jei.genhelium.GenHeliumHandler;
import com.denfop.integration.jei.genhelium.GenHeliumWrapper;
import com.denfop.integration.jei.genhydrogen.GenHydCategory;
import com.denfop.integration.jei.genhydrogen.GenHydHandler;
import com.denfop.integration.jei.genhydrogen.GenHydWrapper;
import com.denfop.integration.jei.genlava.GenLavaCategory;
import com.denfop.integration.jei.genlava.GenLavaHandler;
import com.denfop.integration.jei.genlava.GenLavaWrapper;
import com.denfop.integration.jei.genneutronium.GenNeuCategory;
import com.denfop.integration.jei.genneutronium.GenNeuHandler;
import com.denfop.integration.jei.genneutronium.GenNeuWrapper;
import com.denfop.integration.jei.genobs.GenObsCategory;
import com.denfop.integration.jei.genobs.GenObsHandler;
import com.denfop.integration.jei.genobs.GenObsWrapper;
import com.denfop.integration.jei.genpetrol.GenPetrolCategory;
import com.denfop.integration.jei.genpetrol.GenPetrolHandler;
import com.denfop.integration.jei.genpetrol.GenPetrolWrapper;
import com.denfop.integration.jei.gense.GenSECategory;
import com.denfop.integration.jei.gense.GenSEHandler;
import com.denfop.integration.jei.gense.GenSEWrapper;
import com.denfop.integration.jei.genstar.GenStarCategory;
import com.denfop.integration.jei.genstar.GenStarHandler;
import com.denfop.integration.jei.genstar.GenStarRecipeManager;
import com.denfop.integration.jei.genstone.GenStoneCategory;
import com.denfop.integration.jei.genstone.GenStoneHandler;
import com.denfop.integration.jei.genstone.GenStoneRecipeWrapper;
import com.denfop.integration.jei.graphite_handler.GraphiteCategory;
import com.denfop.integration.jei.graphite_handler.GraphiteHandler;
import com.denfop.integration.jei.graphite_handler.GraphiteRecipeWrapper;
import com.denfop.integration.jei.handlerho.HandlerHOCategory;
import com.denfop.integration.jei.handlerho.HandlerHOHandler;
import com.denfop.integration.jei.handlerho.HandlerHORecipeWrapper;
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
import com.denfop.integration.jei.modularator.*;
import com.denfop.integration.jei.molecular.MolecularTransformerCategory;
import com.denfop.integration.jei.molecular.MolecularTransformerHandler;
import com.denfop.integration.jei.molecular.MolecularTransformerRecipeWrapper;
import com.denfop.integration.jei.moon_spotter.MoonSpooterCategory;
import com.denfop.integration.jei.moon_spotter.MoonSpooterHandler;
import com.denfop.integration.jei.moon_spotter.MoonSpooterRecipeWrapper;
import com.denfop.integration.jei.multiblock.MultiBlockCategory;
import com.denfop.integration.jei.multiblock.MultiBlockHandler;
import com.denfop.integration.jei.multiblock.MultiBlockWrapper;
import com.denfop.integration.jei.oilpump.OilPumpCategory;
import com.denfop.integration.jei.oilpump.OilPumpHandler;
import com.denfop.integration.jei.oilpump.OilPumpWrapper;
import com.denfop.integration.jei.orewashing.OreWashingCategory;
import com.denfop.integration.jei.orewashing.OreWashingHandler;
import com.denfop.integration.jei.orewashing.OreWashingWrapper;
import com.denfop.integration.jei.painting.PaintingCategory;
import com.denfop.integration.jei.painting.PaintingHandler;
import com.denfop.integration.jei.painting.PaintingWrapper;
import com.denfop.integration.jei.plasticcratorplate.PlasticCreatorPlateCategory;
import com.denfop.integration.jei.plasticcratorplate.PlasticCreatorPlateHandler;
import com.denfop.integration.jei.plasticcratorplate.PlasticCreatorPlateWrapper;
import com.denfop.integration.jei.plasticcreator.PlasticCreatorCategory;
import com.denfop.integration.jei.plasticcreator.PlasticCreatorHandler;
import com.denfop.integration.jei.plasticcreator.PlasticCreatorWrapper;
import com.denfop.integration.jei.privatizer.PrivatizerCategory;
import com.denfop.integration.jei.privatizer.PrivatizerHandler;
import com.denfop.integration.jei.privatizer.PrivatizerWrapper;
import com.denfop.integration.jei.quarry.QuarryCategory;
import com.denfop.integration.jei.quarry.QuarryHandler;
import com.denfop.integration.jei.quarry.QuarryWrapper;
import com.denfop.integration.jei.quarry_comb.CMQuarryCategory;
import com.denfop.integration.jei.quarry_comb.CMQuarryWrapper;
import com.denfop.integration.jei.quarry_comb.СMQuarryHandler;
import com.denfop.integration.jei.quarry_mac.MQuarryCategory;
import com.denfop.integration.jei.quarry_mac.MQuarryHandler;
import com.denfop.integration.jei.quarry_mac.MQuarryWrapper;
import com.denfop.integration.jei.recycler.RecyclerCategory;
import com.denfop.integration.jei.recycler.RecyclerHandler;
import com.denfop.integration.jei.recycler.RecyclerWrapper;
import com.denfop.integration.jei.refiner.RefinerCategory;
import com.denfop.integration.jei.refiner.RefinerHandler;
import com.denfop.integration.jei.refiner.RefinerRecipeWrapper;
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
import com.denfop.integration.jei.scrap.ScrapCategory;
import com.denfop.integration.jei.scrap.ScrapHandler;
import com.denfop.integration.jei.scrap.ScrapRecipeWrapper;
import com.denfop.integration.jei.socket_factory.SocketCategory;
import com.denfop.integration.jei.socket_factory.SocketHandler;
import com.denfop.integration.jei.socket_factory.SocketRecipeWrapper;
import com.denfop.integration.jei.solidmatters.MatterCategory;
import com.denfop.integration.jei.solidmatters.MatterHandler;
import com.denfop.integration.jei.solidmatters.MatterWrapper;
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
import com.denfop.integration.jei.tuner.TunerCategory;
import com.denfop.integration.jei.tuner.TunerHandler;
import com.denfop.integration.jei.tuner.TunerWrapper;
import com.denfop.integration.jei.upgradeblock.UpgradeBlockCategory;
import com.denfop.integration.jei.upgradeblock.UpgradeBlockHandler;
import com.denfop.integration.jei.upgradeblock.UpgradeBlockWrapper;
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
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Map;

@JEIPlugin
public final class JEICompat implements IModPlugin {

    private IIngredientRegistry itemRegistry;

    public static ItemStack getBlockStack(IMultiTileBlock block) {
        return TileBlockCreator.instance.get(block.getIdentifier()).getItemStack(block);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {

        registry.addRecipeCategories(new MolecularTransformerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new DoubleMolecularTransformerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new AlloySmelterCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new AdvAlloySmelterCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new MicrochipCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new EnrichCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SynthesisCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SannariumPanelCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SunnariumCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new BFCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new BlastFCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new RotorsRodCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new WaterRotorUpgradeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ExtractorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new PlasticCreatorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new PlasticCreatorPlateCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new UpgradeBlockCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new PaintingCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenStoneCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ModulatorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ModulatorCategory1(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new PrivatizerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new TunerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new MatterCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new AntiUpgradeBlockCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new VeinCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new OilPumpCategory(registry.getJeiHelpers().getGuiHelper()));
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
        registry.addRecipeCategories(new GraphiteCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new StampCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new RodFactoryCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new EnchantCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new MultiBlockCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new DepositsCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new EarthQuarryCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new AnvilCategory(registry.getJeiHelpers().getGuiHelper()));
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
                new  AnvilCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                AnvilHandler.class,  AnvilWrapper::new,
                BlockAnvil.block_anvil.getName()
        );
        registry.addRecipeCatalyst(
                getBlockStack(BlockAnvil.block_anvil),
                new AnvilCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.addRecipes(
                MultiBlockHandler.getRecipes(),
                new MultiBlockCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                MultiBlockHandler.class,  MultiBlockWrapper::new,
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
                new  EnchantCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );

        registry.handleRecipes(
                EnchantHandler.class,  EnchantRecipeWrapper::new,
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
                new ItemStack(IUItem.machines_base, 1, 5),
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


        registry.addRecipeClickArea(GuiMolecularTransformer.class, 23, 48, 10, 15, BlockMolecular.molecular.getName());
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
                23,
                48,
                10,
                15,
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

        registry.addRecipeClickArea(GuiWitherMaker.class, 81, 16, 40, 18,
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
                GuiHandlerHeavyOre.class, 48, 31, 44, 14,
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
        registry.addRecipeClickArea(GuiElectrolyzer.class, 33, 15, 22, 35,
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
                GuiOilRefiner.class, 33, 15, 22, 35,
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
                GuiAdvOilRefiner.class, 33, 15, 22, 35,
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
        registry.addRecipeClickArea(GuiConverterSolidMatter.class, 78, 50, 111 - 78, 17,
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

    }

    public void onRuntimeAvailable(@Nonnull IJeiRuntime iJeiRuntime) {


    }


}
