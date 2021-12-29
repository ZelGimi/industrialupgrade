package com.denfop.proxy;

import com.denfop.Config;
import com.denfop.IUItem;
import com.denfop.api.IBuildManager;
import com.denfop.api.IModelRegister;
import com.denfop.api.Recipes;
import com.denfop.blocks.FluidName;
import com.denfop.events.EventUpdate;
import com.denfop.events.IUEventHandler;
import com.denfop.integration.avaritia.AvaritiaIntegration;
import com.denfop.integration.botania.BotaniaIntegration;
import com.denfop.integration.compactsolar.CompactSolarIntegration;
import com.denfop.integration.de.DraconicIntegration;
import com.denfop.integration.exnihilo.ExNihiloIntegration;
import com.denfop.integration.forestry.FIntegration;
import com.denfop.integration.thaumcraft.ThaumcraftIntegration;
import com.denfop.integration.wireless.WirelessIntegration;
import com.denfop.recipemanager.ConverterSolidMatterRecipeManager;
import com.denfop.recipemanager.DoubleMachineRecipeManager;
import com.denfop.recipemanager.DoubleMolecularRecipeManager;
import com.denfop.recipemanager.FluidRecipeManager;
import com.denfop.recipemanager.GenStoneRecipeManager;
import com.denfop.recipemanager.GeneratorRecipeItemManager;
import com.denfop.recipemanager.GeneratorRecipeManager;
import com.denfop.recipemanager.GeneratorSunnariumRecipeManager;
import com.denfop.recipemanager.MicrochipRecipeManager;
import com.denfop.recipemanager.ObsidianRecipeManager;
import com.denfop.recipemanager.PlasticPlateRecipeManager;
import com.denfop.recipemanager.PlasticRecipeManager;
import com.denfop.recipemanager.SunnariumRecipeManager;
import com.denfop.recipemanager.TripleMachineRecipeManager;
import com.denfop.recipemanager.WitherMakerRecipeManager;
import com.denfop.recipes.BasicRecipe;
import com.denfop.recipes.CannerRecipe;
import com.denfop.recipes.CentrifugeRecipe;
import com.denfop.recipes.CompressorRecipe;
import com.denfop.recipes.FurnaceRecipes;
import com.denfop.recipes.MaceratorRecipe;
import com.denfop.recipes.MetalFormerRecipe;
import com.denfop.recipes.OreWashingRecipe;
import com.denfop.tiles.base.TileEntityConverterSolidMatter;
import com.denfop.tiles.base.TileEntityDoubleMolecular;
import com.denfop.tiles.base.TileEntityMolecularTransformer;
import com.denfop.tiles.base.TileEntityObsidianGenerator;
import com.denfop.tiles.base.TileEntityPainting;
import com.denfop.tiles.base.TileEntityUpgradeBlock;
import com.denfop.tiles.base.TileSunnariumMaker;
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
import com.denfop.utils.ModUtils;
import com.denfop.utils.TemperatureMechanism;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.core.IC2;
import ic2.core.block.machine.tileentity.TileEntityMatter;
import ic2.core.recipe.BasicMachineRecipeManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class CommonProxy implements IGuiHandler {

    public void preInit(FMLPreInitializationEvent event) {
        if(Loader.isModLoaded("exnihilocreatio"))
            ExNihiloIntegration.init();
        if (Config.DraconicLoaded && Config.Draconic) {
            DraconicIntegration.init();
        }
        if (Config.thaumcraft && Config.Thaumcraft)
            ThaumcraftIntegration.init();
        if (Config.AvaritiaLoaded && Config.Avaritia) {
            AvaritiaIntegration.init();
        }

        if (Config.BotaniaLoaded && Config.Botania) {
            BotaniaIntegration.init();
        }
    }

    public static void sendPlayerMessage(EntityPlayer player, String text) {
        if (IC2.platform.isSimulating())
        IC2.platform.messagePlayer(
                player,
                text
        );
    }

    public void init(FMLInitializationEvent event) {
        IUItem.register_mineral();
        Recipes.electrolyzer = new FluidRecipeManager();
        Recipes.oilrefiner = new FluidRecipeManager();
        Recipes.oiladvrefiner = new FluidRecipeManager();
        Recipes.heliumgenerator = new GeneratorRecipeManager();
        Recipes.lavagenrator = new GeneratorRecipeManager();
        Recipes.sunnarium = new GeneratorSunnariumRecipeManager();
        Recipes.sunnarium.addRecipe(null, new ItemStack(IUItem.sunnarium, 1, 4));
        Recipes.electrolyzer.addRecipe(new FluidStack(FluidRegistry.WATER, 1000), new FluidStack[]{new FluidStack(FluidName.fluidhyd.getInstance(),
                500),
                new FluidStack(FluidName.fluidoxy.getInstance(), 250)});
        Recipes.oilrefiner.addRecipe(new FluidStack(FluidName.fluidneft.getInstance(), 1000),
                new FluidStack[]{new FluidStack(FluidName.fluidbenz.getInstance(), 600), new FluidStack(FluidName.fluiddizel.getInstance(), 400)});
        Recipes.oiladvrefiner.addRecipe(new FluidStack(FluidName.fluidneft.getInstance(), 1000),
                new FluidStack[]{new FluidStack(FluidName.fluidpolyeth.getInstance(), 500), new FluidStack(FluidName.fluidpolyprop.getInstance(), 500)});

        NBTTagCompound nbt = ModUtils.nbt();
        nbt.setInteger("amount", 20000);
        NBTTagCompound nbt1 = ModUtils.nbt();
        nbt1.setInteger("amount", 1000000);
        Recipes.lavagenrator.addRecipe(nbt, new FluidStack(FluidRegistry.LAVA, 1000));
        Recipes.heliumgenerator.addRecipe(nbt1, new FluidStack(FluidName.fluidHelium.getInstance(), 1000));
        Recipes.neutroniumgenrator = new GeneratorRecipeManager();
        NBTTagCompound nbt2 = ModUtils.nbt();
        nbt2.setDouble("amount", Config.energy * 1000);
        Recipes.neutroniumgenrator.addRecipe(nbt2, new FluidStack(FluidName.fluidNeutron.getInstance(), 1000));
        Recipes.mattergenerator = new GeneratorRecipeItemManager();
        for (int i = 0; i < 8; i++) {
            final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
            Recipes.mattergenerator.addRecipe(input.forStack(new ItemStack(IUItem.matter, 1, i)), (int) Config.SolidMatterStorage,
                    new ItemStack(IUItem.matter, 1, i)
            );  }
        Recipes.mechanism = new TemperatureMechanism();
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
        TileSunnariumMaker.init();
        TileEntityPainting.init();
        TileEntitySunnariumPanelMaker.init();
        TileEntityUpgradeBlock.init();
        TileEntityMatter.addAmplifier(new ItemStack(IUItem.doublescrapBox), 1, 405000);
       TileEntityDoubleMolecular.init();
        TileEntityObsidianGenerator.init();
        TileEntityPlasticCreator.init();
        TileEntityPlasticPlateCreator.init();



        if (!Config.disableUpdateCheck) {
            MinecraftForge.EVENT_BUS.register(new EventUpdate());
        }



    }

    public void postInit(FMLPostInitializationEvent event) {
        IUEventHandler sspEventHandler = new IUEventHandler();
        MinecraftForge.EVENT_BUS.register(sspEventHandler);
        FMLCommonHandler.instance().bus().register(sspEventHandler);
        if (Loader.isModLoaded("compactsolars"))
            CompactSolarIntegration.init();
        if (Loader.isModLoaded("wirelesstools"))
            WirelessIntegration.init();
        if (Loader.isModLoaded("forestry"))
            FIntegration.init();
    }

    public void registerRecipe() {
       BasicRecipe.recipe();
        if (Config.BotaniaLoaded && Config.Botania)
            BotaniaIntegration.recipe();
        if (Config.DraconicLoaded && Config.Draconic)
            DraconicIntegration.Recipes();
        if (Config.AvaritiaLoaded && Config.Avaritia)
            AvaritiaIntegration.recipe();
        CompressorRecipe.recipe();
        CannerRecipe.recipe();
        FurnaceRecipes.recipe();
        CentrifugeRecipe.init();
        MaceratorRecipe.recipe();
        MetalFormerRecipe.init();
        OreWashingRecipe.init();



    }

    public boolean addIModelRegister(IModelRegister modelRegister) {
        return false;
    }

    private static final ArrayList<IBuildManager> buildList = new ArrayList();

    public boolean addIBuildManager(IBuildManager modelRegister) {
        if (!buildList.contains(modelRegister)) {
            return buildList.add(modelRegister);
        } else {
            return false;
        }
    }

    public int addArmor(final String armorName) {
        return 0;
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
        Recipes.Alloysmelter = new DoubleMachineRecipeManager();
        Recipes.Alloyadvsmelter = new TripleMachineRecipeManager();
        Recipes.createscrap = new BasicMachineRecipeManager();
        Recipes.macerator = new BasicMachineRecipeManager();
        Recipes.enrichment = new DoubleMachineRecipeManager();
        Recipes.doublemolecular = new DoubleMolecularRecipeManager();
        Recipes.fermer = new com.denfop.recipemanager.BasicMachineRecipeManager();
        Recipes.GenerationMicrochip = new MicrochipRecipeManager();
        Recipes.GenStone = new GenStoneRecipeManager();
        Recipes.handlerore = new BasicMachineRecipeManager();
        Recipes.plastic = new PlasticRecipeManager();
        Recipes.plasticplate = new PlasticPlateRecipeManager();
        Recipes.synthesis = new DoubleMachineRecipeManager();
        Recipes.sunnuriumpanel = new DoubleMachineRecipeManager();
        Recipes.withermaker = new WitherMakerRecipeManager();
        Recipes.molecular = new com.denfop.recipemanager.BasicMachineRecipeManager();
        Recipes.obsidianGenerator = new ObsidianRecipeManager();
        Recipes.upgrade = new DoubleMachineRecipeManager();
        Recipes.sunnurium = new SunnariumRecipeManager();
        Recipes.matterrecipe = new ConverterSolidMatterRecipeManager();
        Recipes.painting= new DoubleMachineRecipeManager();
    }

}
