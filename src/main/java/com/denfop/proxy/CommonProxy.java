package com.denfop.proxy;

import com.denfop.Config;
import com.denfop.IUItem;
import com.denfop.IULoots;
import com.denfop.Ic2Items;
import com.denfop.api.IModelRegister;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.research.BaseResearchSystem;
import com.denfop.api.research.ResearchSystem;
import com.denfop.api.space.BaseSpaceSystem;
import com.denfop.api.space.SpaceInit;
import com.denfop.api.space.SpaceNet;
import com.denfop.blocks.BlocksItems;
import com.denfop.blocks.mechanism.BlockAdminPanel;
import com.denfop.blocks.mechanism.BlockAdvChamber;
import com.denfop.blocks.mechanism.BlockAdvRefiner;
import com.denfop.blocks.mechanism.BlockAdvSolarEnergy;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.blocks.mechanism.BlockCable;
import com.denfop.blocks.mechanism.BlockCombinerSolid;
import com.denfop.blocks.mechanism.BlockConverterMatter;
import com.denfop.blocks.mechanism.BlockCoolPipes;
import com.denfop.blocks.mechanism.BlockDoubleMolecularTransfomer;
import com.denfop.blocks.mechanism.BlockImpChamber;
import com.denfop.blocks.mechanism.BlockImpSolarEnergy;
import com.denfop.blocks.mechanism.BlockMolecular;
import com.denfop.blocks.mechanism.BlockMoreMachine;
import com.denfop.blocks.mechanism.BlockMoreMachine1;
import com.denfop.blocks.mechanism.BlockMoreMachine2;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.blocks.mechanism.BlockPerChamber;
import com.denfop.blocks.mechanism.BlockPetrolQuarry;
import com.denfop.blocks.mechanism.BlockPipes;
import com.denfop.blocks.mechanism.BlockQCable;
import com.denfop.blocks.mechanism.BlockQuarryVein;
import com.denfop.blocks.mechanism.BlockRefiner;
import com.denfop.blocks.mechanism.BlockSCable;
import com.denfop.blocks.mechanism.BlockSimpleMachine;
import com.denfop.blocks.mechanism.BlockSintezator;
import com.denfop.blocks.mechanism.BlockSolarEnergy;
import com.denfop.blocks.mechanism.BlockSolidMatter;
import com.denfop.blocks.mechanism.BlockSunnariumMaker;
import com.denfop.blocks.mechanism.BlockSunnariumPanelMaker;
import com.denfop.blocks.mechanism.BlockTank;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.blocks.mechanism.BlockUpgradeBlock;
import com.denfop.blocks.mechanism.IUChargepadStorage;
import com.denfop.blocks.mechanism.IUStorage;
import com.denfop.blocks.mechanism.SSPBlock;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.CoolComponent;
import com.denfop.componets.QEComponent;
import com.denfop.componets.SEComponent;
import com.denfop.events.EventUpdate;
import com.denfop.events.IUEventHandler;
import com.denfop.integration.avaritia.AvaritiaIntegration;
import com.denfop.integration.avaritia.BlockAvaritiaSolarPanel;
import com.denfop.integration.botania.BlockBotSolarPanel;
import com.denfop.integration.botania.BotaniaIntegration;
import com.denfop.integration.de.BlockDESolarPanel;
import com.denfop.integration.de.DraconicIntegration;
import com.denfop.integration.exnihilo.ExNihiloIntegration;
import com.denfop.integration.forestry.FIntegration;
import com.denfop.integration.mets.METSIntegration;
import com.denfop.integration.projecte.ProjectEIntegration;
import com.denfop.integration.thaumcraft.BlockThaumSolarPanel;
import com.denfop.integration.thaumcraft.ThaumcraftIntegration;
import com.denfop.items.CellType;
import com.denfop.items.ItemUpgradePanelKit;
import com.denfop.items.book.core.CoreBook;
import com.denfop.recipemanager.ObsidianRecipeManager;
import com.denfop.recipes.BasicRecipe;
import com.denfop.recipes.CannerRecipe;
import com.denfop.recipes.CentrifugeRecipe;
import com.denfop.recipes.CompressorRecipe;
import com.denfop.recipes.FurnaceRecipes;
import com.denfop.recipes.MaceratorRecipe;
import com.denfop.recipes.MetalFormerRecipe;
import com.denfop.recipes.OreWashingRecipe;
import com.denfop.register.Register;
import com.denfop.register.RegisterOreDictionary;
import com.denfop.tiles.base.TileEntityConverterSolidMatter;
import com.denfop.tiles.base.TileEntityDoubleMolecular;
import com.denfop.tiles.base.TileEntityMolecularTransformer;
import com.denfop.tiles.base.TileEntityObsidianGenerator;
import com.denfop.tiles.base.TileEntityPainting;
import com.denfop.tiles.base.TileEntitySunnariumMaker;
import com.denfop.tiles.base.TileEntityUpgradeBlock;
import com.denfop.tiles.mechanism.EnumUpgradesMultiMachine;
import com.denfop.tiles.mechanism.TileEntityAdvAlloySmelter;
import com.denfop.tiles.mechanism.TileEntityAlloySmelter;
import com.denfop.tiles.mechanism.TileEntityAssamplerScrap;
import com.denfop.tiles.mechanism.TileEntityCombMacerator;
import com.denfop.tiles.mechanism.TileEntityEnrichment;
import com.denfop.tiles.mechanism.TileEntityFermer;
import com.denfop.tiles.mechanism.TileEntityGenerationMicrochip;
import com.denfop.tiles.mechanism.TileEntityGenerationStone;
import com.denfop.tiles.mechanism.TileEntityHandlerHeavyOre;
import com.denfop.tiles.mechanism.TileEntityPlasticCreator;
import com.denfop.tiles.mechanism.TileEntityPlasticPlateCreator;
import com.denfop.tiles.mechanism.TileEntitySunnariumPanelMaker;
import com.denfop.tiles.mechanism.TileEntitySynthesis;
import com.denfop.tiles.mechanism.TileEntityWitherMaker;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.utils.CraftManagerUtils;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.TemperatureMechanismUtils;
import com.denfop.world.WorldGenOres;
import ic2.api.recipe.IBasicMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.MachineRecipe;
import ic2.core.IC2;
import ic2.core.block.comp.Components;
import ic2.core.block.machine.tileentity.TileEntityMatter;
import ic2.core.util.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CommonProxy implements IGuiHandler {


    public static void sendPlayerMessage(EntityPlayer player, String text) {
        if (IC2.platform.isSimulating()) {
            IC2.platform.messagePlayer(
                    player,
                    text
            );
        }
    }

    public void preInit(FMLPreInitializationEvent event) {
        final BlocksItems init = new BlocksItems();
        init.init();
        Register.init();
        SSPBlock.buildDummies();
        IUStorage.buildDummies();
        BlockMoreMachine.buildDummies();
        BlockMoreMachine1.buildDummies();
        BlockMoreMachine2.buildDummies();
        BlockMoreMachine3.buildDummies();
        IUChargepadStorage.buildDummies();
        BlockBaseMachine.buildDummies();
        BlockSolidMatter.buildDummies();
        BlockCombinerSolid.buildDummies();

        BlockSolarEnergy.buildDummies();
        BlockAdvSolarEnergy.buildDummies();
        BlockImpSolarEnergy.buildDummies();
        BlockMolecular.buildDummies();
        BlockSintezator.buildDummies();
        BlockSunnariumMaker.buildDummies();
        BlockSunnariumPanelMaker.buildDummies();
        BlockRefiner.buildDummies();
        BlockAdvRefiner.buildDummies();
        BlockUpgradeBlock.buildDummies();
        BlockPetrolQuarry.buildDummies();
        BlockAdvChamber.buildDummies();
        BlockImpChamber.buildDummies();
        BlockPerChamber.buildDummies();
        BlockPerChamber.buildDummies();
        BlockBaseMachine1.buildDummies();
        BlockBaseMachine2.buildDummies();
        BlockBaseMachine3.buildDummies();
        BlockTransformer.buildDummies();
        BlockDoubleMolecularTransfomer.buildDummies();
        BlockAdminPanel.buildDummies();
        BlockCable.buildDummies();
        BlockSCable.buildDummies();
        BlockQCable.buildDummies();
        BlockPipes.buildDummies();
        BlockTank.buildDummies();
        BlockSimpleMachine.buildDummies();
        BlockConverterMatter.buildDummies();
        BlockCoolPipes.buildDummies();
        BlockQuarryVein.buildDummies();
        if (Config.AvaritiaLoaded) {
            BlockAvaritiaSolarPanel.buildDummies();
        }
        if (Config.BotaniaLoaded) {
            BlockBotSolarPanel.buildDummies();
        }
        if (Config.DraconicLoaded) {
            BlockDESolarPanel.buildDummies();
        }
        if (Config.Thaumcraft) {
            BlockThaumSolarPanel.buildDummies();
        }
        if (Loader.isModLoaded("exnihilocreatio")) {
            ExNihiloIntegration.init();
        }
        if (Config.DraconicLoaded && Config.Draconic) {
            DraconicIntegration.init();
        }
        if (Config.thaumcraft && Config.Thaumcraft) {
            ThaumcraftIntegration.init();
        }
        if (Config.AvaritiaLoaded && Config.Avaritia) {
            AvaritiaIntegration.init();
        }

        if (Config.BotaniaLoaded && Config.Botania) {
            BotaniaIntegration.init();
        }
        ListInformationUtils.init();
        WorldGenOres.init();
        RegisterOreDictionary.oredict();
        CellType.register();
        IULoots.init();
        EnumSolarPanels.registerTile();
        ItemUpgradePanelKit.EnumSolarPanelsKit.registerkit();
        IUItem.register_mineral();
        Recipes.mechanism = new TemperatureMechanismUtils();
        TileEntityAssamplerScrap.init();
        TileEntityHandlerHeavyOre.init();
        TileEntityFermer.init();
        TileEntityEnrichment.init();
        TileEntitySynthesis.init();
        TileEntityAlloySmelter.init();
        TileEntityAdvAlloySmelter.init();
        TileEntityCombMacerator.init();
        TileEntityMolecularTransformer.init();
        TileEntityGenerationMicrochip.init();
        TileEntityGenerationStone.init();
        TileEntityConverterSolidMatter.init();
        TileEntityWitherMaker.init();
        TileEntitySunnariumMaker.init();
        TileEntityPainting.init();
        TileEntitySunnariumPanelMaker.init();
        TileEntityUpgradeBlock.init();
        TileEntityMatter.addAmplifier(new ItemStack(IUItem.doublescrapBox), 1, 405000);
        TileEntityDoubleMolecular.init();
        TileEntityObsidianGenerator.init();
        TileEntityPlasticCreator.init();
        TileEntityPlasticPlateCreator.init();
    }

    public void init(FMLInitializationEvent event) {

        if (!Config.disableUpdateCheck) {
            MinecraftForge.EVENT_BUS.register(new EventUpdate());
        }
        SpaceNet.instance = new BaseSpaceSystem();
        SpaceInit.init();
        if (Config.experiment) {
            ResearchSystem.instance = new BaseResearchSystem();

        }

    }

    public void postInit(FMLPostInitializationEvent event) {
        IUEventHandler sspEventHandler = new IUEventHandler();
        MinecraftForge.EVENT_BUS.register(sspEventHandler);

        if (Loader.isModLoaded("forestry")) {
            FIntegration.init();
        }
        if (Loader.isModLoaded("projecte") && Config.ProjectE) {
            ProjectEIntegration.init();
        }
        CoreBook.init();
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.mfeUnit));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.mfsukit));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.mfsUnit));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.batBox));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.cesuUnit));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.advancedCircuit));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.advancedCircuit));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.metalformer));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.macerator));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.extractor));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.electroFurnace));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.recycler));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.massFabricator));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.compressor));


        if (Components.getId(AdvEnergy.class) == null) {
            Components.register(AdvEnergy.class, "AdvEnergy");
        }
        if (Components.getId(QEComponent.class) == null) {
            Components.register(QEComponent.class, "QEComponent");
        }
        if (Components.getId(CoolComponent.class) == null) {
            Components.register(CoolComponent.class, "CoolComponent");
        }
        if (Components.getId(SEComponent.class) == null) {
            Components.register(SEComponent.class, "SEComponent");
        }
        EnumUpgradesMultiMachine.register();
        if (Loader.isModLoaded("mets")) {
            METSIntegration.init();
        }
    }

    public void registerRecipe() {
        BasicRecipe.recipe();
        if (Config.BotaniaLoaded && Config.Botania) {
            BotaniaIntegration.recipe();
        }
        if (Config.DraconicLoaded && Config.Draconic) {
            DraconicIntegration.Recipes();
        }
        if (Config.AvaritiaLoaded && Config.Avaritia) {
            AvaritiaIntegration.recipe();
        }
        CompressorRecipe.recipe();
        CannerRecipe.recipe();
        FurnaceRecipes.recipe();
        CentrifugeRecipe.init();
        MaceratorRecipe.recipe();
        MetalFormerRecipe.init();
        OreWashingRecipe.init();
        writeRecipe(ic2.api.recipe.Recipes.macerator, "macerator");
        writeRecipe(ic2.api.recipe.Recipes.compressor, "compressor");
        writeRecipe(ic2.api.recipe.Recipes.extractor, "extractor");
        writeRecipe(ic2.api.recipe.Recipes.metalformerCutting, "cutting");
        writeRecipe(ic2.api.recipe.Recipes.metalformerExtruding, "extruding");
        writeRecipe(ic2.api.recipe.Recipes.metalformerRolling, "rolling");
        writeRecipe(ic2.api.recipe.Recipes.recycler, "recycler");
        writeRecipe();

    }

    private void writeRecipe() {
        net.minecraft.item.crafting.FurnaceRecipes recipes = net.minecraft.item.crafting.FurnaceRecipes.instance();
        final Map<ItemStack, ItemStack> map = recipes.getSmeltingList();
        ItemStack output;
        ItemStack input;
        final IRecipeInputFactory inputFactory = ic2.api.recipe.Recipes.inputFactory;

        for (Map.Entry<ItemStack, ItemStack> entry : map.entrySet()) {
            output = entry.getValue();
            input = entry.getKey();
            if (input.isEmpty()) {
                continue;
            }
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setFloat("experience", recipes.getSmeltingExperience(output) * (float) StackUtil.getSize(output));
            Recipes.recipes.addRecipe(
                    "furnace",
                    new BaseMachineRecipe(
                            new Input(
                                    inputFactory.forStack(input)
                            ),
                            new RecipeOutput(nbt, output)
                    )
            );
        }
    }

    private void writeRecipe(IBasicMachineRecipeManager recipeManager, String name) {

        if (!name.equals("recycler")) {
            final Iterable<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> recipe = recipeManager.getRecipes();
            for (final MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe1 : recipe) {
                List<ItemStack> list = (List<ItemStack>) recipe1.getOutput();
                if (!list.get(0).isItemEqual(Ic2Items.iridiumOre)) {
                    Recipes.recipes.addRecipe(
                            name,
                            new BaseMachineRecipe(
                                    new Input(
                                            recipe1.getInput()
                                    ),
                                    new RecipeOutput(recipe1.getMetaData(), list)
                            )
                    );
                } else if (!name.equals("compressor")) {
                    Recipes.recipes.addRecipe(
                            name,
                            new BaseMachineRecipe(
                                    new Input(
                                            recipe1.getInput()
                                    ),
                                    new RecipeOutput(recipe1.getMetaData(), list)
                            )
                    );
                }
            }
        } else {

            if (ic2.api.recipe.Recipes.recyclerWhitelist.isEmpty()) {


            } else {

                for (final IRecipeInput stack : ic2.api.recipe.Recipes.recyclerBlacklist) {
                    Recipes.recipes.addRecipe(
                            name,
                            new BaseMachineRecipe(
                                    new Input(
                                            stack
                                    ),
                                    new RecipeOutput(null, Ic2Items.scrap)
                            )
                    );
                }
            }

        }
    }

    public boolean addIModelRegister(IModelRegister modelRegister) {
        return false;
    }


    @Nullable
    @Override
    public Object getServerGuiElement(
            final int ID,
            final EntityPlayer player,
            final World world,
            final int x,
            final int y,
            final int z
    ) {
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(
            final int ID,
            final EntityPlayer player,
            final World world,
            final int x,
            final int y,
            final int z
    ) {
        return null;
    }

    public boolean isSimulating() {
        return !FMLCommonHandler.instance().getEffectiveSide().isClient();
    }

    public void profilerStartSection(final String section) {
    }

    public void profilerEndStartSection(final String section) {
    }

    public void profilerEndSection() {
    }

    public void regrecipemanager() {
        Recipes.obsidianGenerator = new ObsidianRecipeManager();
    }

}
