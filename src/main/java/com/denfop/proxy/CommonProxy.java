package com.denfop.proxy;

import cofh.api.fluid.IFluidContainerItem;
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
import com.denfop.blocks.mechanism.BlockBlastFurnace;
import com.denfop.blocks.mechanism.BlockCable;
import com.denfop.blocks.mechanism.BlockChargepadStorage;
import com.denfop.blocks.mechanism.BlockCombinerSolid;
import com.denfop.blocks.mechanism.BlockConverterMatter;
import com.denfop.blocks.mechanism.BlockCoolPipes;
import com.denfop.blocks.mechanism.BlockDoubleMolecularTransfomer;
import com.denfop.blocks.mechanism.BlockEnergyStorage;
import com.denfop.blocks.mechanism.BlockExpCable;
import com.denfop.blocks.mechanism.BlockHeatColdPipes;
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
import com.denfop.blocks.mechanism.BlockSolarPanels;
import com.denfop.blocks.mechanism.BlockSolidMatter;
import com.denfop.blocks.mechanism.BlockSunnariumMaker;
import com.denfop.blocks.mechanism.BlockSunnariumPanelMaker;
import com.denfop.blocks.mechanism.BlockTank;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.blocks.mechanism.BlockUniversalCable;
import com.denfop.blocks.mechanism.BlockUpgradeBlock;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.CoolComponent;
import com.denfop.componets.EXPComponent;
import com.denfop.componets.HeatComponent;
import com.denfop.componets.ProcessMultiComponent;
import com.denfop.componets.QEComponent;
import com.denfop.componets.RFComponent;
import com.denfop.componets.SEComponent;
import com.denfop.events.EventUpdate;
import com.denfop.events.IUEventHandler;
import com.denfop.events.Ic2IntegrationHandler;
import com.denfop.integration.ae.AEIntegration;
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
import com.denfop.integration.thermal.ThermalExpansionIntegration;
import com.denfop.items.CellType;
import com.denfop.items.book.core.CoreBook;
import com.denfop.items.upgradekit.ItemUpgradePanelKit;
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
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.utils.CraftManagerUtils;
import com.denfop.utils.ElectricItemManager;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.MatterRecipe;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Precision;
import com.denfop.world.WorldGenOres;
import com.google.common.collect.Lists;
import forestry.api.core.IToolPipette;
import ic2.api.item.ElectricItem;
import ic2.api.recipe.IBasicMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.MachineRecipe;
import ic2.core.IC2;
import ic2.core.block.comp.Components;
import ic2.core.block.machine.tileentity.TileEntityMatter;
import ic2.core.util.LogCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonProxy implements IGuiHandler {


    public static boolean ae2;
    public static boolean gc;

    public static void sendPlayerMessage(EntityPlayer player, String text) {
        if (IC2.platform.isSimulating()) {
            IC2.platform.messagePlayer(
                    player,
                    text
            );
        }
    }

    public void preInit(FMLPreInitializationEvent event) {
        ElectricItem.rawManager = new ElectricItemManager();
        final BlocksItems init = new BlocksItems();

        init.init();
        Register.init();
        BlockSolarPanels.buildDummies();
        BlockEnergyStorage.buildDummies();
        BlockMoreMachine.buildDummies();
        BlockMoreMachine1.buildDummies();
        BlockMoreMachine2.buildDummies();
        BlockMoreMachine3.buildDummies();
        BlockChargepadStorage.buildDummies();
        BlockBaseMachine.buildDummies();
        BlockSolidMatter.buildDummies();
        BlockCombinerSolid.buildDummies();
        BlockBlastFurnace.buildDummies();
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
        BlockExpCable.buildDummies();
        BlockQCable.buildDummies();
        BlockPipes.buildDummies();
        BlockTank.buildDummies();
        BlockSimpleMachine.buildDummies();
        BlockConverterMatter.buildDummies();
        BlockCoolPipes.buildDummies();
        BlockQuarryVein.buildDummies();
        BlockHeatColdPipes.buildDummies();
        BlockUniversalCable.buildDummies();
        ae2 = Loader.isModLoaded("appliedenergistics2");
        gc = Loader.isModLoaded("galacticraftcore");

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
        TileEntityMatter.addAmplifier(new ItemStack(IUItem.doublescrapBox), 1, 405000);
        Recipes.recipes.initializationRecipes();
    }

    public void init(FMLInitializationEvent event) {

        if (!Config.disableUpdateCheck) {
            MinecraftForge.EVENT_BUS.register(new EventUpdate());
        }
        SpaceNet.instance = new BaseSpaceSystem();
        SpaceInit.init();

        ResearchSystem.instance = new BaseResearchSystem();


    }

    public void postInit(FMLPostInitializationEvent event) {
        BlockAdvChamber.adv_chamber.setPlaceHandler(Ic2IntegrationHandler.advReactorChamberPlace);
        BlockImpChamber.imp_chamber.setPlaceHandler(Ic2IntegrationHandler.impReactorChamberPlace);
        BlockPerChamber.per_chamber.setPlaceHandler(Ic2IntegrationHandler.perReactorChamberPlace);

        IUEventHandler sspEventHandler = new IUEventHandler();
        MinecraftForge.EVENT_BUS.register(sspEventHandler);
        Map<List<List<ItemStack>>, MatterRecipe> itemStackMap1 = new HashMap<>();
        long startTime = System.nanoTime();
        List<MatterRecipe> matterRecipeList = new ArrayList<>();
        IC2.log.debug(LogCategory.General, "Checking recipes %d ",
                ForgeRegistries.RECIPES.getValuesCollection().size()
        );
        for (IRecipe r : Lists.newArrayList(ForgeRegistries.RECIPES.getValuesCollection())) {
            List<List<ItemStack>> itemStackList = new ArrayList<>();
            for (Ingredient ingredient : r.getIngredients()) {
                List<ItemStack> itemStackList1;
                if (ingredient instanceof IRecipeInput) {
                    itemStackList1 = ((IRecipeInput) ingredient).getInputs();
                } else {
                    itemStackList1 = Arrays.asList(ingredient.getMatchingStacks());
                }
                if (!itemStackList1.isEmpty()) {
                    itemStackList.add(itemStackList1);
                }
            }
            final ItemStack output = r.getRecipeOutput();
            final NBTTagCompound nbt = ModUtils.nbtOrNull(output);

            if ((nbt != null && nbt.hasKey("RSControl")) || output.getItem() instanceof IFluidContainerItem || output.getItem() instanceof IToolPipette) {
                continue;
            }
            if (Recipes.recipes.getRecipeOutput("converter", false, output) != null) {
                continue;
            }
            MatterRecipe matterRecipe = new MatterRecipe(r.getRecipeOutput());
            matterRecipeList.add(matterRecipe);
            itemStackMap1.put(itemStackList, matterRecipe);
        }
        IC2.log.debug(LogCategory.General, "Finished checking recipes for converter matter after %d ms.",
                (System.nanoTime() - startTime) / 1000000L
        );
        startTime = System.nanoTime();
        Map<List<List<ItemStack>>, MatterRecipe> itemStackMap3 = new HashMap<>();
        final long startTime1 = System.nanoTime();
        for (int i = 0; i < 1; i++) {
            for (Map.Entry<List<List<ItemStack>>, MatterRecipe> entry : itemStackMap1.entrySet()) {
                List<List<ItemStack>> list = entry.getKey();
                final ItemStack output = entry.getValue().getStack();
                MatterRecipe matterRecipe = entry.getValue();
                if (matterRecipe.can()) {
                    continue;
                }
                List<List<ItemStack>> list2 = new ArrayList<>();
                for (List<ItemStack> list1 : list) {
                    boolean need_continue = false;
                    for (ItemStack stack : list1) {
                        BaseMachineRecipe recipe = Recipes.recipes.getRecipeOutput("converter", false, stack);
                        if (recipe == null) {
                            continue;
                        }
                        need_continue = true;
                        matterRecipe.addMatter(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_0") / output.getCount());
                        matterRecipe.addSun(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_1") / output.getCount());
                        matterRecipe.addAqua(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_2") / output.getCount());
                        matterRecipe.addNether(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_3") / output.getCount());
                        matterRecipe.addNight(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_4") / output.getCount());
                        matterRecipe.addEarth(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_5") / output.getCount());
                        matterRecipe.addEnd(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_6") / output.getCount());
                        matterRecipe.addAer(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_7") / output.getCount());
                        list2.add(list1);
                        break;
                    }
                    if (!need_continue) {
                        break;
                    }

                }
                list.removeIf(list2::contains);
                if (list.isEmpty()) {
                    matterRecipe.setCan(true);
                    itemStackMap3.put(list, matterRecipe);
                }

            }
            List<MatterRecipe> matterRecipeList1 = new ArrayList<>();
            matterRecipeList.forEach(matterRecipe1 -> {
                if (matterRecipe1.can()) {
                    TileEntityConverterSolidMatter.addrecipe(matterRecipe1.getStack(),
                            Precision.round(matterRecipe1.getMatter(), 2),
                            Precision.round(matterRecipe1.getSun(), 2)
                            ,
                            Precision.round(matterRecipe1.getAqua(), 2),
                            Precision.round(matterRecipe1.getNether(), 2),
                            Precision.round(matterRecipe1.getNight(), 2),
                            Precision.round(matterRecipe1.getEarth(), 2),
                            Precision.round(matterRecipe1.getEnd(), 2),
                            Precision.round(matterRecipe1.getAer(), 2)
                    );
                    matterRecipeList1.add(matterRecipe1);
                }

            });
            matterRecipeList.removeAll(matterRecipeList1);
            itemStackMap3.forEach((key, value) -> itemStackMap1.remove(key));
            IC2.log.debug(LogCategory.General, "Finished  %d stage recipes for converter matter after %d ms. ", i,
                    (System.nanoTime() - startTime) / 1000000L
            );
            startTime = System.nanoTime();
        }

        IC2.log.debug(LogCategory.General, "Finished adding recipes for converter matter after %d ms.",
                (System.nanoTime() - startTime1) / 1000000L
        );
        matterRecipeList.clear();
        itemStackMap1.clear();
        IUItem.machineRecipe = Recipes.recipes.getRecipeStack("converter");
        writeRecipe(ic2.api.recipe.Recipes.macerator, "macerator");
        writeRecipe(ic2.api.recipe.Recipes.compressor, "compressor");
        writeRecipe(ic2.api.recipe.Recipes.extractor, "extractor");
        writeRecipe(ic2.api.recipe.Recipes.metalformerCutting, "cutting");
        writeRecipe(ic2.api.recipe.Recipes.metalformerExtruding, "extruding");
        writeRecipe(ic2.api.recipe.Recipes.metalformerRolling, "rolling");
        writeRecipe(ic2.api.recipe.Recipes.recycler, "recycler");
        writeRecipe(ic2.api.recipe.Recipes.oreWashing, "orewashing");
        writeRecipe(ic2.api.recipe.Recipes.centrifuge, "centrifuge");
        writeRecipe();
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        for (int i = 0; i < 8; i++) {
            Recipes.recipes.addRecipe(
                    "matter",
                    new BaseMachineRecipe(
                            new Input(
                                    input.forStack(new ItemStack(IUItem.matter, 1, i))
                            ),
                            new RecipeOutput(null, new ItemStack(IUItem.matter, 1, i))
                    )
            );
        }
        if (Loader.isModLoaded("forestry")) {
            FIntegration.init();
        }
        if (Loader.isModLoaded("projecte") && Config.ProjectE) {
            ProjectEIntegration.init();
        }

        CoreBook.init();
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.mfeUnit));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.mfsukit));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.windmeter));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.windmeter));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.WindKineticGenerator));

        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.mfsUnit));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.batBox));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.cesuUnit));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.advancedCircuit));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.advancedCircuit));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.electronicCircuit));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.electronicCircuit));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.lvTransformer));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.mvTransformer));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.hvTransformer));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.evTransformer));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.ChargepadbatBox));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.ChargepadcesuUnit));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.ChargepadmfeUnit));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.ChargepadmfsUnit));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.transformerUpgrade));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.teslaCoil));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.replicator));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.blastfurnace));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.jetpack));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.advminer));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.inductionFurnace));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.electrolyzer));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.advminer));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.miner));

        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.metalformer));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.macerator));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.extractor));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.electroFurnace));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.recycler));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.massFabricator));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.compressor));

        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.tank));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.tank1));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.tank2));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.tank3));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.iridiumDrill));

        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.bronzerotor));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.carbonrotor));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.ironrotor));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.woodrotor));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.steelrotor));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.elemotor));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.elemotor));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.machine));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.generator));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.generator));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.centrifuge));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.orewashingplant));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.solardestiller));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.scanner));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.pump));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.reactorChamber));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.nuclearReactor));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.geothermalGenerator));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.solarPanel));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(Ic2Items.reactorVent));

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
        if (Components.getId(EXPComponent.class) == null) {
            Components.register(EXPComponent.class, "EXPComponent");
        }
        if (Components.getId(HeatComponent.class) == null) {
            Components.register(HeatComponent.class, "HeatComponent");
        }
        if (Components.getId(RFComponent.class) == null) {
            Components.register(RFComponent.class, "RFComponent");
        }
        if (Components.getId(ProcessMultiComponent.class) == null) {
            Components.register(ProcessMultiComponent.class, "ProcessMultiComponent");
        }

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
        if (Loader.isModLoaded("appliedenergistics2")) {
            AEIntegration.init();
        }
        if (Loader.isModLoaded("thermalexpansion")) {
            ThermalExpansionIntegration.init();
        }
        CompressorRecipe.recipe();
        CannerRecipe.recipe();
        FurnaceRecipes.recipe();
        CentrifugeRecipe.init();
        MaceratorRecipe.recipe();
        MetalFormerRecipe.init();
        OreWashingRecipe.init();


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
            try {
                nbt.setFloat("experience", recipes.getSmeltingExperience(output));
            } catch (Exception e) {
                nbt.setFloat("experience", 0.1F);

            }
            Recipes.recipes.addRecipe(
                    "furnace",
                    new BaseMachineRecipe(
                            new Input(
                                    inputFactory.forStack(input)
                            ),
                            new RecipeOutput(nbt, output)
                    )
            );
            // Recipes.recipes.getMap_recipe_managers_itemStack("furnace").add(new RecipeInputStack(input));
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
