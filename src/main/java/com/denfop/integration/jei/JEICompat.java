package com.denfop.integration.jei;

import com.denfop.IUItem;
import com.denfop.blocks.mechanism.BlockAdvRefiner;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.blocks.mechanism.BlockConverterMatter;
import com.denfop.blocks.mechanism.BlockDoubleMolecularTransfomer;
import com.denfop.blocks.mechanism.BlockMolecular;
import com.denfop.blocks.mechanism.BlockMoreMachine;
import com.denfop.blocks.mechanism.BlockMoreMachine1;
import com.denfop.blocks.mechanism.BlockMoreMachine2;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.blocks.mechanism.BlockRefiner;
import com.denfop.blocks.mechanism.BlockSolarEnergy;
import com.denfop.blocks.mechanism.BlockSunnariumMaker;
import com.denfop.blocks.mechanism.BlockSunnariumPanelMaker;
import com.denfop.blocks.mechanism.BlockUpgradeBlock;
import com.denfop.gui.GUIAdvAlloySmelter;
import com.denfop.gui.GUIAdvOilRefiner;
import com.denfop.gui.GUIAlloySmelter;
import com.denfop.gui.GUIConverterSolidMatter;
import com.denfop.gui.GUIElectrolyzer;
import com.denfop.gui.GUIEnriched;
import com.denfop.gui.GUIGenStone;
import com.denfop.gui.GUIGenerationMicrochip;
import com.denfop.gui.GUIHandlerHeavyOre;
import com.denfop.gui.GUIObsidianGenerator;
import com.denfop.gui.GUIOilRefiner;
import com.denfop.gui.GUIPainting;
import com.denfop.gui.GUIPlasticCreator;
import com.denfop.gui.GUIPlasticPlateCreator;
import com.denfop.gui.GUISunnariumMaker;
import com.denfop.gui.GUISunnariumPanelMaker;
import com.denfop.gui.GUISynthesis;
import com.denfop.gui.GUIUpgradeBlock;
import com.denfop.gui.GUIWitherMaker;
import com.denfop.gui.GuiDoubleMolecularTransformer;
import com.denfop.gui.GuiMolecularTransformer;
import com.denfop.integration.jei.advalloysmelter.AdvAlloySmelterCategory;
import com.denfop.integration.jei.advalloysmelter.AdvAlloySmelterHandler;
import com.denfop.integration.jei.advalloysmelter.AdvAlloySmelterRecipeWrapper;
import com.denfop.integration.jei.advrefiner.AdvRefinerCategory;
import com.denfop.integration.jei.advrefiner.AdvRefinerHandler;
import com.denfop.integration.jei.advrefiner.AdvRefinerRecipeWrapper;
import com.denfop.integration.jei.alloysmelter.AlloySmelterCategory;
import com.denfop.integration.jei.alloysmelter.AlloySmelterHandler;
import com.denfop.integration.jei.alloysmelter.AlloySmelterRecipeWrapper;
import com.denfop.integration.jei.combmac.CombMacCategory;
import com.denfop.integration.jei.combmac.CombMacHandler;
import com.denfop.integration.jei.combmac.CombMacRecipeWrapper;
import com.denfop.integration.jei.convertermatter.ConverterCategory;
import com.denfop.integration.jei.convertermatter.ConverterHandler;
import com.denfop.integration.jei.convertermatter.ConverterWrapper;
import com.denfop.integration.jei.cutting.CuttingCategory;
import com.denfop.integration.jei.cutting.CuttingHandler;
import com.denfop.integration.jei.cutting.CuttingWrapper;
import com.denfop.integration.jei.doublemolecular.DoubleMolecularTransformerCategory;
import com.denfop.integration.jei.doublemolecular.DoubleMolecularTransformerHandler;
import com.denfop.integration.jei.doublemolecular.DoubleMolecularTransformerRecipeWrapper;
import com.denfop.integration.jei.electrolyzer.ElectrolyzerCategory;
import com.denfop.integration.jei.electrolyzer.ElectrolyzerHandler;
import com.denfop.integration.jei.electrolyzer.ElectrolyzerRecipeWrapper;
import com.denfop.integration.jei.enrichment.EnrichCategory;
import com.denfop.integration.jei.enrichment.EnrichHandler;
import com.denfop.integration.jei.enrichment.EnrichRecipeWrapper;
import com.denfop.integration.jei.extruder.ExtruderCategory;
import com.denfop.integration.jei.extruder.ExtruderHandler;
import com.denfop.integration.jei.extruder.ExtruderWrapper;
import com.denfop.integration.jei.farmer.FarmerCategory;
import com.denfop.integration.jei.farmer.FarmerHandler;
import com.denfop.integration.jei.farmer.FarmerRecipeWrapper;
import com.denfop.integration.jei.fquarry.FQuarryCategory;
import com.denfop.integration.jei.fquarry.FQuarryHandler;
import com.denfop.integration.jei.fquarry.FQuarryWrapper;
import com.denfop.integration.jei.genhelium.GenHeliumCategory;
import com.denfop.integration.jei.genhelium.GenHeliumHandler;
import com.denfop.integration.jei.genhelium.GenHeliumWrapper;
import com.denfop.integration.jei.genlava.GenLavaCategory;
import com.denfop.integration.jei.genlava.GenLavaHandler;
import com.denfop.integration.jei.genlava.GenLavaWrapper;
import com.denfop.integration.jei.genneutronium.GenNeuCategory;
import com.denfop.integration.jei.genneutronium.GenNeuHandler;
import com.denfop.integration.jei.genneutronium.GenNeuWrapper;
import com.denfop.integration.jei.genobs.GenObsCategory;
import com.denfop.integration.jei.genobs.GenObsHandler;
import com.denfop.integration.jei.genobs.GenObsWrapper;
import com.denfop.integration.jei.gense.GenSECategory;
import com.denfop.integration.jei.gense.GenSEHandler;
import com.denfop.integration.jei.gense.GenSEWrapper;
import com.denfop.integration.jei.genstar.GenStarCategory;
import com.denfop.integration.jei.genstar.GenStarHandler;
import com.denfop.integration.jei.genstar.GenStarRecipeManager;
import com.denfop.integration.jei.genstone.GenStoneCategory;
import com.denfop.integration.jei.genstone.GenStoneHandler;
import com.denfop.integration.jei.genstone.GenStoneRecipeWrapper;
import com.denfop.integration.jei.handlerho.HandlerHOCategory;
import com.denfop.integration.jei.handlerho.HandlerHOHandler;
import com.denfop.integration.jei.handlerho.HandlerHORecipeWrapper;
import com.denfop.integration.jei.microchip.MicrochipCategory;
import com.denfop.integration.jei.microchip.MicrochipHandler;
import com.denfop.integration.jei.microchip.MicrochipRecipeWrapper;
import com.denfop.integration.jei.molecular.MolecularTransformerCategory;
import com.denfop.integration.jei.molecular.MolecularTransformerHandler;
import com.denfop.integration.jei.molecular.MolecularTransformerRecipeWrapper;
import com.denfop.integration.jei.painting.PaintingCategory;
import com.denfop.integration.jei.painting.PaintingHandler;
import com.denfop.integration.jei.painting.PaintingWrapper;
import com.denfop.integration.jei.plasticcratorplate.PlasticCreatorPlateCategory;
import com.denfop.integration.jei.plasticcratorplate.PlasticCreatorPlateHandler;
import com.denfop.integration.jei.plasticcratorplate.PlasticCreatorPlateWrapper;
import com.denfop.integration.jei.plasticcreator.PlasticCrreatorCategory;
import com.denfop.integration.jei.plasticcreator.PlasticCrreatorHandler;
import com.denfop.integration.jei.plasticcreator.PlasticCrreatorWrapper;
import com.denfop.integration.jei.quarry.QuarryCategory;
import com.denfop.integration.jei.quarry.QuarryHandler;
import com.denfop.integration.jei.quarry.QuarryWrapper;
import com.denfop.integration.jei.refiner.RefinerCategory;
import com.denfop.integration.jei.refiner.RefinerHandler;
import com.denfop.integration.jei.refiner.RefinerRecipeWrapper;
import com.denfop.integration.jei.rolling.RollingCategory;
import com.denfop.integration.jei.rolling.RollingHandler;
import com.denfop.integration.jei.rolling.RollingWrapper;
import com.denfop.integration.jei.scrap.ScrapCategory;
import com.denfop.integration.jei.scrap.ScrapHandler;
import com.denfop.integration.jei.scrap.ScrapRecipeWrapper;
import com.denfop.integration.jei.sunnarium.SunnariumCategory;
import com.denfop.integration.jei.sunnarium.SunnariumHandler;
import com.denfop.integration.jei.sunnarium.SunnariumWrapper;
import com.denfop.integration.jei.sunnariumpanel.SannariumPanelCategory;
import com.denfop.integration.jei.sunnariumpanel.SannariumPanelHandler;
import com.denfop.integration.jei.sunnariumpanel.SannariumPanelWrapper;
import com.denfop.integration.jei.synthesis.SynthesisCategory;
import com.denfop.integration.jei.synthesis.SynthesisHandler;
import com.denfop.integration.jei.synthesis.SynthesisWrapper;
import com.denfop.integration.jei.upgradeblock.UpgradeBlockCategory;
import com.denfop.integration.jei.upgradeblock.UpgradeBlockHandler;
import com.denfop.integration.jei.upgradeblock.UpgradeBlockWrapper;
import ic2.api.recipe.Recipes;
import ic2.core.block.ITeBlock;
import ic2.core.block.TeBlockRegistry;
import ic2.core.gui.IClickHandler;
import ic2.core.gui.RecipeButton;
import ic2.core.ref.TeBlock;
import ic2.jeiIntegration.recipe.machine.DynamicCategory;
import ic2.jeiIntegration.recipe.machine.IORecipeCategory;
import ic2.jeiIntegration.recipe.machine.MetalFormerCategory;
import ic2.jeiIntegration.recipe.machine.RecyclerCategory;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipesGui;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Arrays;

@JEIPlugin
public final class JEICompat implements IModPlugin {
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

        registry.addRecipeCategories(new PlasticCrreatorCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new PlasticCreatorPlateCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new UpgradeBlockCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new PaintingCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenStoneCategory(registry.getJeiHelpers().getGuiHelper()));

        registry.addRecipeCategories(new GenStarCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new HandlerHOCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ElectrolyzerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenLavaCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenHeliumCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenNeuCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new GenObsCategory(registry.getJeiHelpers().getGuiHelper()));

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

    }
    public void register(IModRegistry registry) {
        registry.addRecipeClickArea(GuiMolecularTransformer.class, 23, 48, 10, 15, BlockMolecular.molecular.getName());
        registry.addRecipes(MolecularTransformerHandler.getRecipes(),
                new MolecularTransformerCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(MolecularTransformerHandler.class, MolecularTransformerRecipeWrapper::new,
                BlockMolecular.molecular.getName());
         registry.addRecipeCatalyst(new ItemStack(IUItem.blockmolecular),  new MolecularTransformerCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipeClickArea(GuiDoubleMolecularTransformer.class, 23, 48, 10, 15, BlockDoubleMolecularTransfomer.double_transformer.getName());
        registry.addRecipes(
                DoubleMolecularTransformerHandler.getRecipes(),
                new DoubleMolecularTransformerCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(DoubleMolecularTransformerHandler.class, DoubleMolecularTransformerRecipeWrapper::new,
                BlockDoubleMolecularTransfomer.double_transformer.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.blockdoublemolecular),
                new DoubleMolecularTransformerCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipeClickArea(GUIAlloySmelter.class, 80, 35, 22, 14,
                BlockBaseMachine.alloy_smelter.getName());
        registry.addRecipes(
                AlloySmelterHandler.getRecipes(),
                new AlloySmelterCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(AlloySmelterHandler.class, AlloySmelterRecipeWrapper::new,
                BlockBaseMachine.alloy_smelter.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines,1,4),
                new AlloySmelterCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipeClickArea(GUIAdvAlloySmelter.class, 80, 35, 22, 14,
                BlockBaseMachine1.adv_alloy_smelter.getName());
        registry.addRecipes(
                AdvAlloySmelterHandler.getRecipes(),
                new AdvAlloySmelterCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(AdvAlloySmelterHandler.class, AdvAlloySmelterRecipeWrapper::new,
                BlockBaseMachine1.adv_alloy_smelter.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.basemachine,1,3),
                new AdvAlloySmelterCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipeClickArea(GUIGenerationMicrochip.class, 88, 18, 106-88, 34-18,
                BlockBaseMachine.generator_microchip.getName());
        registry.addRecipes(
                MicrochipHandler.getRecipes(),
                new MicrochipCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(MicrochipHandler.class, MicrochipRecipeWrapper::new,
                BlockBaseMachine.generator_microchip.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines,1,6),
                new MicrochipCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipeClickArea(GUIEnriched.class, 67, 36, 15, 15,
                BlockBaseMachine1.enrichment.getName());
        registry.addRecipes(
                EnrichHandler.getRecipes(),
                new EnrichCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(EnrichHandler.class, EnrichRecipeWrapper::new,
                BlockBaseMachine1.enrichment.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.basemachine,1,10),
                new EnrichCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipeClickArea(GUISynthesis.class, 82, 30, 25, 23,
                BlockBaseMachine1.synthesis.getName());
        registry.addRecipes(
                SynthesisHandler.getRecipes(),
                new SynthesisCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(SynthesisHandler.class,SynthesisWrapper::new,
                BlockBaseMachine1.synthesis.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.basemachine,1,11),
                new SynthesisCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipeClickArea(GUISunnariumPanelMaker.class, 74, 34, 14, 14,
                BlockSunnariumPanelMaker.gen_sunnarium.getName());
        registry.addRecipes(
                SannariumPanelHandler.getRecipes(),
                new  SannariumPanelCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes( SannariumPanelHandler.class, SannariumPanelWrapper::new,
                BlockSunnariumPanelMaker.gen_sunnarium.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.sunnariummaker,1,0),
                new  SannariumPanelCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipeClickArea(GUISunnariumMaker.class, 55, 20, 17, 31,
                BlockSunnariumMaker.gen_sunnarium_plate.getName());
        registry.addRecipes(
                SunnariumHandler.getRecipes(),
                new  SunnariumCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes( SunnariumHandler.class,SunnariumWrapper::new,
                BlockSunnariumMaker.gen_sunnarium_plate.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.sunnariumpanelmaker,1,0),
                new  SunnariumCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipeClickArea(GUIPlasticCreator.class, 80, 35, 22, 14,
                BlockBaseMachine2.plastic_creator.getName());
        registry.addRecipes(
                PlasticCrreatorHandler.getRecipes(),
                new  PlasticCrreatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes( PlasticCrreatorHandler.class,PlasticCrreatorWrapper::new,
                BlockBaseMachine2.plastic_creator.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.basemachine1,1,11),
                new  PlasticCrreatorCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipeClickArea(GUIPlasticPlateCreator.class, 80, 35, 22, 14,
                BlockBaseMachine2.plastic_plate_creator.getName());
        registry.addRecipes(
                PlasticCreatorPlateHandler.getRecipes(),
                new  PlasticCreatorPlateCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes( PlasticCreatorPlateHandler.class, PlasticCreatorPlateWrapper::new,
                BlockBaseMachine2.plastic_plate_creator.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.basemachine1,1,13),
                new   PlasticCreatorPlateCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipeClickArea(GUIUpgradeBlock.class, 81, 33, 27, 18,
                BlockUpgradeBlock.upgrade_block.getName());
        registry.addRecipes(
                UpgradeBlockHandler.getRecipes(),
                new  UpgradeBlockCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes( UpgradeBlockHandler.class, UpgradeBlockWrapper::new,
                BlockUpgradeBlock.upgrade_block.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.upgradeblock,1,0),
                new   UpgradeBlockCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipeClickArea(GUIPainting.class, 75, 34, 15, 15,
                BlockBaseMachine2.painter.getName());
        registry.addRecipes(
                PaintingHandler.getRecipes(),
                new   PaintingCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(  PaintingHandler.class, PaintingWrapper::new,
                BlockBaseMachine2.painter.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.basemachine1,1,3),
                new   PaintingCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipeClickArea(GUIGenStone.class, 64, 28, 16, 16,
                BlockBaseMachine.gen_stone.getName());
        registry.addRecipes(
                GenStoneHandler.getRecipes(),
                new   GenStoneCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(  GenStoneHandler.class, GenStoneRecipeWrapper::new,
                BlockBaseMachine.gen_stone.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines,1,7),
                new   GenStoneCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipeClickArea(GUIWitherMaker.class, 81, 16, 40, 18,
                BlockBaseMachine1.gen_wither.getName());
        registry.addRecipes(
                GenStarHandler.getRecipes(),
                new   GenStarCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(  GenStarHandler.class, GenStarRecipeManager::new,
                BlockBaseMachine1.gen_wither.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.basemachine,1,13),
                new   GenStarCategory(registry.getJeiHelpers().getGuiHelper()).getUid());


        registry.addRecipeClickArea(GUIHandlerHeavyOre.class, 48, 31, 44, 14,
                BlockBaseMachine1.handler_ho.getName());
        registry.addRecipes(
               HandlerHOHandler.getRecipes(),
                new   HandlerHOCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(   HandlerHOHandler.class,  HandlerHORecipeWrapper::new,
                BlockBaseMachine1.handler_ho.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.basemachine,1,12),
                new    HandlerHOCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipeClickArea(GUIElectrolyzer.class, 33, 15, 22, 35,
                BlockBaseMachine2.electrolyzer_iu.getName());
        registry.addRecipes(
                ElectrolyzerHandler.getRecipes(),
                new   ElectrolyzerCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(   ElectrolyzerHandler.class,  ElectrolyzerRecipeWrapper::new,
                BlockBaseMachine2.electrolyzer_iu.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.basemachine1,1,15),
                new    ElectrolyzerCategory(registry.getJeiHelpers().getGuiHelper()).getUid());



        registry.addRecipes(
               GenLavaHandler.getRecipes(),
                new   GenLavaCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(  GenLavaHandler.class,  GenLavaWrapper::new,
                BlockBaseMachine2.lava_gen.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.basemachine1,1,12),
                new    GenLavaCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipes(
                GenHeliumHandler.getRecipes(),
                new   GenHeliumCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(  GenHeliumHandler.class,  GenHeliumWrapper::new,
                BlockBaseMachine2.helium_generator.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.basemachine1,1,14),
                new    GenHeliumCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipes(
                GenNeuHandler.getRecipes(),
                new  GenNeuCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(  GenNeuHandler.class, GenNeuWrapper::new,
                BlockBaseMachine.neutron_generator.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines,1,5),
                new    GenNeuCategory(registry.getJeiHelpers().getGuiHelper()).getUid());


        registry.addRecipeClickArea(GUIObsidianGenerator.class, 101, 34, 16, 16,
                BlockBaseMachine2.gen_obsidian.getName());
        registry.addRecipes(
                GenObsHandler.getRecipes(),
                new   GenObsCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(    GenObsHandler.class,   GenObsWrapper::new,
                BlockBaseMachine2.gen_obsidian.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.basemachine1,1,10),
                new     GenObsCategory(registry.getJeiHelpers().getGuiHelper()).getUid());


        registry.addRecipeClickArea(GUIOilRefiner.class, 33, 15, 22, 35,
                BlockRefiner.refiner.getName());
        registry.addRecipes(
                RefinerHandler.getRecipes(),
                new    RefinerCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(   RefinerHandler.class,  RefinerRecipeWrapper::new,
                BlockRefiner.refiner.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.oilrefiner,1,0),
                new     RefinerCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipeClickArea(GUIAdvOilRefiner.class, 33, 15, 22, 35,
                BlockAdvRefiner.adv_refiner.getName());
        registry.addRecipes(
                AdvRefinerHandler.getRecipes(),
                new     AdvRefinerCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(    AdvRefinerHandler.class,   AdvRefinerRecipeWrapper::new,
                BlockAdvRefiner.adv_refiner.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.oiladvrefiner,1,0),
                new      AdvRefinerCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipes(
                FarmerHandler.getRecipes(),
                new      FarmerCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(    FarmerHandler.class,    FarmerRecipeWrapper::new,
                BlockMoreMachine3.farmer.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base3,1,0),
                new       FarmerCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base3,1,1),
                new       FarmerCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base3,1,2),
                new       FarmerCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base3,1,3),
                new       FarmerCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipes(
                ScrapHandler.getRecipes(),
                new       ScrapCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(     ScrapHandler.class,     ScrapRecipeWrapper::new,
                BlockMoreMachine3.assamplerscrap.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base3,1,4),
                new        ScrapCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base3,1,5),
                new        ScrapCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base3,1,6),
                new        ScrapCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base3,1,7),
                new        ScrapCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipes(
                CombMacHandler.getRecipes(),
                new      CombMacCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(     CombMacHandler.class,     CombMacRecipeWrapper::new,
                BlockMoreMachine1.comb_macerator.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base1,1,6),
                new       CombMacCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base1,1,7),
                new       CombMacCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base1,1,8),
                new       CombMacCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base1,1,9),
                new       CombMacCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipes(
               QuarryHandler.getRecipes(),
                new      QuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(      QuarryHandler.class,     QuarryWrapper::new,
                BlockBaseMachine.quantum_quarry.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines,1,8),
                new       QuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines,1,13),
                new       QuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines,1,14),
                new       QuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines,1,15),
                new       QuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid());


        registry.addRecipes(
                GenSEHandler.getRecipes(),
                new      GenSECategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(      GenSEHandler.class,    GenSEWrapper::new,
                BlockSolarEnergy.se_gen.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.blockSE,1,0),
                new      GenSECategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.ImpblockSE,1,0),
                new      GenSECategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.AdvblockSE,1,0),
                new      GenSECategory(registry.getJeiHelpers().getGuiHelper()).getUid());


        registry.addRecipeClickArea(GUIConverterSolidMatter.class, 78, 50, 111-78, 17,
                BlockConverterMatter.converter_matter.getName());
        registry.addRecipes(
                ConverterHandler.getRecipes(),
                new    ConverterCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(    ConverterHandler.class,   ConverterWrapper::new,
                BlockConverterMatter.converter_matter.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.convertersolidmatter,1,0),
                new      ConverterCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.addRecipes(
                FQuarryHandler.getRecipes(),
                new      FQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(      FQuarryHandler.class,     FQuarryWrapper::new,
                BlockBaseMachine.quantum_quarry.getName()+"1");
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines,1,8),
                new       FQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines,1,13),
                new       FQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines,1,14),
                new       FQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines,1,15),
                new       FQuarryCategory(registry.getJeiHelpers().getGuiHelper()).getUid());


        registry.addRecipes(
               ExtruderHandler.getRecipes(),
                new       ExtruderCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(      ExtruderHandler.class,      ExtruderWrapper::new,
                BlockMoreMachine2.extruder.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base2,1,4),
                new       ExtruderCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base2,1,5),
                new       ExtruderCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base2,1,6),
                new        ExtruderCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base2,1,7),
                new        ExtruderCategory(registry.getJeiHelpers().getGuiHelper()).getUid());


        registry.addRecipes(
               CuttingHandler.getRecipes(),
                new       CuttingCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(      CuttingHandler.class,     CuttingWrapper::new,
                BlockMoreMachine2.cutting.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base2,1,8),
                new       CuttingCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base2,1,9),
                new       CuttingCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base2,1,10),
                new       CuttingCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base2,1,11),
                new        CuttingCategory(registry.getJeiHelpers().getGuiHelper()).getUid());


        registry.addRecipes(
                RollingHandler.getRecipes(),
                new       RollingCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        registry.handleRecipes(      RollingHandler.class,     RollingWrapper::new,
                BlockMoreMachine2.rolling.getName());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base2,1,0),
                new      RollingCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base2,1,1),
                new      RollingCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base2,1,2),
                new      RollingCategory(registry.getJeiHelpers().getGuiHelper()).getUid());
        registry.addRecipeCatalyst(new ItemStack(IUItem.machines_base2,1,3),
                new        RollingCategory(registry.getJeiHelpers().getGuiHelper()).getUid());

        this.addMachineRecipes(registry, new DynamicCategory(TeBlock.macerator, Recipes.macerator,
                registry.getJeiHelpers().getGuiHelper()),BlockMoreMachine.double_macerator);

        this.addMachineRecipes(registry, new DynamicCategory(TeBlock.macerator, Recipes.macerator,
                registry.getJeiHelpers().getGuiHelper()),BlockMoreMachine.triple_macerator);

        this.addMachineRecipes(registry, new DynamicCategory(TeBlock.macerator, Recipes.macerator,
                registry.getJeiHelpers().getGuiHelper()),BlockMoreMachine.quad_macerator);

        this.addMachineRecipes(registry, new DynamicCategory(TeBlock.extractor, Recipes.extractor,
                registry.getJeiHelpers().getGuiHelper()),BlockMoreMachine.double_extractor);

        this.addMachineRecipes(registry, new DynamicCategory(TeBlock.extractor, Recipes.extractor,
                registry.getJeiHelpers().getGuiHelper()),BlockMoreMachine.triple_extractor);

        this.addMachineRecipes(registry, new DynamicCategory(TeBlock.extractor, Recipes.extractor,
                registry.getJeiHelpers().getGuiHelper()),BlockMoreMachine.quad_extractor);
        this.addMachineRecipes(registry, new DynamicCategory(TeBlock.compressor, Recipes.compressor,
                registry.getJeiHelpers().getGuiHelper()),BlockMoreMachine.double_commpressor);

        this.addMachineRecipes(registry, new DynamicCategory(TeBlock.compressor, Recipes.compressor,
                registry.getJeiHelpers().getGuiHelper()),BlockMoreMachine.triple_commpressor);

        this.addMachineRecipes(registry, new DynamicCategory(TeBlock.compressor, Recipes.compressor,
                registry.getJeiHelpers().getGuiHelper()),BlockMoreMachine.quad_commpressor);
        registry.addRecipeCategoryCraftingItem(getBlockStack(BlockMoreMachine.double_furnace), "minecraft.smelting");
        registry.addRecipeCategoryCraftingItem(getBlockStack(BlockMoreMachine.triple_furnace), "minecraft.smelting");
        registry.addRecipeCategoryCraftingItem(getBlockStack(BlockMoreMachine.quad_furnace), "minecraft.smelting");

        this.addMachineRecipes(registry, new MetalFormerCategory(Recipes.metalformerExtruding, 0,
                registry.getJeiHelpers().getGuiHelper()),BlockMoreMachine.double_metalformer);
        this.addMachineRecipes(registry, new MetalFormerCategory(Recipes.metalformerRolling, 1,  registry.getJeiHelpers().getGuiHelper()), BlockMoreMachine.double_metalformer);
        this.addMachineRecipes(registry, new MetalFormerCategory(Recipes.metalformerCutting, 2,  registry.getJeiHelpers().getGuiHelper()), BlockMoreMachine.double_metalformer);
        this.addMachineRecipes(registry, new MetalFormerCategory(Recipes.metalformerExtruding, 0,
                registry.getJeiHelpers().getGuiHelper()), BlockMoreMachine.triple_metalformer);
        this.addMachineRecipes(registry, new MetalFormerCategory(Recipes.metalformerRolling, 1,  registry.getJeiHelpers().getGuiHelper()), BlockMoreMachine.triple_metalformer);
        this.addMachineRecipes(registry, new MetalFormerCategory(Recipes.metalformerCutting, 2,  registry.getJeiHelpers().getGuiHelper()), BlockMoreMachine.triple_metalformer);
        this.addMachineRecipes(registry, new MetalFormerCategory(Recipes.metalformerExtruding, 0,
                registry.getJeiHelpers().getGuiHelper()), BlockMoreMachine.quad_metalformer);
        this.addMachineRecipes(registry, new MetalFormerCategory(Recipes.metalformerRolling, 1,  registry.getJeiHelpers().getGuiHelper()),BlockMoreMachine.quad_metalformer);
        this.addMachineRecipes(registry, new MetalFormerCategory(Recipes.metalformerCutting, 2,  registry.getJeiHelpers().getGuiHelper()), BlockMoreMachine.quad_metalformer);

        final IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        this.addMachineRecipes(registry, new RecyclerCategory(guiHelper), BlockMoreMachine1.double_recycler);
        this.addMachineRecipes(registry, new RecyclerCategory(guiHelper), BlockMoreMachine1.triple_recycler);
        this.addMachineRecipes(registry, new RecyclerCategory(guiHelper), BlockMoreMachine1.quad_recycler);
        this.addMachineRecipes(registry, new RecyclerCategory(guiHelper), BlockMoreMachine1.double_comb_recycler);
        this.addMachineRecipes(registry, new RecyclerCategory(guiHelper), BlockMoreMachine1.triple_comb_recycler);
        this.addMachineRecipes(registry, new RecyclerCategory(guiHelper), BlockMoreMachine1.quad_comb_recycler);
    }
    public ItemStack getBlockStack(ITeBlock block) {
        return TeBlockRegistry.get(block.getIdentifier()).getItemStack(block);
    }
    private <T> void addMachineRecipes(IModRegistry registry, IORecipeCategory<T> category, ITeBlock block) {
        registry.addRecipeCategoryCraftingItem(getBlockStack(block), category.getUid());
    }

    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {


        if (FMLCommonHandler.instance().getSide().isClient()) {
            final IRecipesGui recipesGUI = jeiRuntime.getRecipesGui();
            (new Runnable() {
                public void run() {
                    RecipeButton.jeiRecipeListOpener = input -> {
                        assert input != null;

                        return (IClickHandler) button -> {
                            if (input.length > 0) {
                                recipesGUI.showCategories(Arrays.asList(input));
                            }

                        };
                    };
                }
            }).run();
        }

    }





}
