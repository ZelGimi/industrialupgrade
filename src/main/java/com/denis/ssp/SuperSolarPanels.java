package com.Denfop.ssp;

import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import ic2.core.util.ReflectionUtil;
import net.minecraft.client.renderer.color.ItemColors;
import java.util.Map;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fluids.FluidRegistry;

import com.Denfop.ssp.fluid.Neutron.BlockRegister;
import com.Denfop.ssp.fluid.Neutron.FluidRegister;
import com.Denfop.ssp.gui.ProgressBars;
import com.Denfop.ssp.integration.avaritia.AvaritiaMod;
import com.Denfop.ssp.integration.botania.BotaniaItems;
import com.Denfop.ssp.integration.botania.BotaniaMain;
import com.Denfop.ssp.integration.botania.BotaniaRecipes;
import com.Denfop.ssp.integration.thaumcraft.ThaumcraftMain;
import com.Denfop.ssp.integration.wirelesssolarpanel.SWSP_Recipes;
import com.Denfop.ssp.items.SSP_Items;
import com.Denfop.ssp.items.resource.CraftingThings;
import com.Denfop.ssp.keyboard.SSPKeys;
import com.Denfop.ssp.proxy.CommonProxy;
import com.Denfop.ssp.tiles.SSPBlock;
import com.Denfop.ssp.tiles.TileEntityMolecularAssembler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import ic2.core.init.Localization;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import ic2.core.item.armor.jetpack.JetpackAttachmentRecipe;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import ic2.core.block.ITeBlock;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import ic2.core.block.TeBlockRegistry;
import ic2.core.init.Localization;
import ic2.core.item.armor.jetpack.JetpackAttachmentRecipe;
import ic2.api.event.TeBlockFinalCallEvent;
import ic2.core.block.BlockTileEntity;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
@Mod(modid = "super_solar_panels", name = "Super Solar Panels", dependencies = "required-after:ic2@[2.8.170,);", version = "1.3.0", acceptedMinecraftVersions = "[1.12,1.12.2]")
public final class SuperSolarPanels
{

	  public static boolean seasonal = false;
    public static final String MODID = "super_solar_panels";
	private static final String Urane = null;
	public static final CreativeTabs SSPtab = (CreativeTabs)new SSPSourceTab("SSPtab");
    public static Logger log;
    
    public static boolean avaritiaLoaded = false;
    public static boolean botaniaLoaded = false;
    public static boolean wirelesLoaded = false;
    public static  boolean thaumcraftLoaded = false;
    public static BlockTileEntity machines;
    @SidedProxy(clientSide = "com.Denfop.ssp.proxy.ClientProxy", serverSide = "com.Denfop.ssp.proxy.CommonProxy")
    public static CommonProxy proxy;
   
    public static SuperSolarPanels instance;
	public static BlockTileEntity machines1;
	public static BlockTileEntity machines2;
    @SubscribeEvent
    public static void register(final TeBlockFinalCallEvent event) {
    	 botaniaLoaded = Loader.isModLoaded("botania");
    	 avaritiaLoaded = Loader.isModLoaded("avaritia");
        TeBlockRegistry.addAll((Class)SSPBlock.class, SSPBlock.IDENTITY);
        TeBlockRegistry.setDefaultMaterial(SSPBlock.IDENTITY, Material.ROCK);
        if (SuperSolarPanels.botaniaLoaded) {
        	 TeBlockRegistry.addAll((Class)BotaniaMain.class, BotaniaMain.IDENTITY1);
             TeBlockRegistry.setDefaultMaterial(BotaniaMain.IDENTITY1, Material.ROCK);
     		
	    } 
        if(avaritiaLoaded) {
     		TeBlockRegistry.addAll((Class)AvaritiaMod.class, AvaritiaMod.IDENTITY2);
            TeBlockRegistry.setDefaultMaterial(AvaritiaMod.IDENTITY2, Material.ROCK);
     	}

    }
    static { FluidRegistry.enableUniversalBucket(); }

    
    @Mod.EventHandler
    public void load(final FMLPreInitializationEvent event) {
    	 botaniaLoaded = Loader.isModLoaded("botania");
    	 avaritiaLoaded = Loader.isModLoaded("avaritia");
    	 thaumcraftLoaded  = Loader.isModLoaded("thaumcraft");
    	  if (botaniaLoaded) {
    		  SuperSolarPanels.machines1 = TeBlockRegistry.get(BotaniaMain.IDENTITY1);
    		  
    		  BotaniaItems.buildItems(event.getSide());
    	    } 
if(thaumcraftLoaded) {
	ThaumcraftMain.init();
}
    	  if(avaritiaLoaded) {
    		  SuperSolarPanels.machines2 = TeBlockRegistry.get(AvaritiaMod.IDENTITY2);
       		AvaritiaMod.buildItems(event.getSide());
       	}
    	  proxy.preInit(event);
    	 SuperSolarPanels.log = event.getModLog();
        Configs.loadConfig(event.getSuggestedConfigurationFile(), event.getSide().isClient());
        SuperSolarPanels.machines = TeBlockRegistry.get(SSPBlock.IDENTITY);
        if (event.getSide().isClient())
            setupRenderingGuf(); 
        SSP_Items.buildItems(event.getSide());
       FluidRegister.register();
       BlockRegister.register();
        SSPKeys.addFlyKey();
        SPPRecipes.addMolecularTransformerRecipes();
        GameRegistry.registerWorldGenerator(new SSPWorldDecorator(), 0);
        OreDictionary.registerOre("enderquantumcomponent", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.enderquantumcomponent)); 
    OreDictionary.registerOre("solarsplitter", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.solarsplitter)); 
OreDictionary.registerOre("bluecomponent", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.bluecomponent)); 
OreDictionary.registerOre("greencomponent", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.greencomponent)); 
OreDictionary.registerOre("redcomponent", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.redcomponent)); 
    OreDictionary.registerOre("singularcore", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore)); 
OreDictionary.registerOre("photoniy", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy)); 
OreDictionary.registerOre("photoniy_ingot", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy_ingot)); 
OreDictionary.registerOre("spectralcore", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.spectralcore)); 

OreDictionary.registerOre("ingotUranium", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.URANIUM_INGOT));
OreDictionary.registerOre("ingotIridium", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_INGOT));
//OreDictionary.registerOre("craftingSolarPanelHV", ModernSolarPanels.machines.getItemStack((ITeBlock)TEs.ultimate_solar_panel));
OreDictionary.registerOre("craftingSunnariumPart", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_PART));
OreDictionary.registerOre("craftingSunnarium", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM));

    } 

    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
    	 botaniaLoaded = Loader.isModLoaded("botania");
    	 proxy.init(event);
    	 SSPBlock.buildDummies();
        SPPRecipes.addCraftingRecipes();
        ProgressBars.addStyles();
        if (botaniaLoaded) {
        	BotaniaRecipes.addCraftingRecipes();
        	BotaniaMain.buildDummies();
  	    
        }
        avaritiaLoaded = Loader.isModLoaded("avaritia");
        wirelesLoaded= Loader.isModLoaded("wirelesstools");
     	if (wirelesLoaded) {
     		SWSP_Recipes.addCraftingRecipes();
  		  
  		  
  	    } 
     	if(avaritiaLoaded) {
     		AvaritiaMod.buildDummies();
     	}
        TileEntityMolecularAssembler.MolecularOutput.registerNetwork();
    }
  
 
    @SideOnly(Side.CLIENT)
    private static void setupRenderingGuf() {
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMolecularAssembler.class, (TileEntitySpecialRenderer)new PrettyMolecularTransformerTESR());
      ForgeHooksClient.registerTESRItemStack((Item)machines.getItem(), SSPBlock.molecular_transformer.getId(), SSPBlock.molecular_transformer.getTeClass());
  
    }
   
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void doColourThings(final ColorHandlerEvent.Item event) {
        final ItemColors colours = event.getItemColors();
        final IItemColor armourColouring = (IItemColor) ((Map)ReflectionUtil.getFieldValue(ReflectionUtil.getField((Class)ItemColors.class, (Class)Map.class), (Object)colours)).get(Items.LEATHER_BOOTS.delegate);
        colours.registerItemColorHandler(armourColouring, new Item[] { SSP_Items.Spectral_SOLAR_HELMET.getInstance() });
        colours.registerItemColorHandler(armourColouring, new Item[] { SSP_Items.Singular_SOLAR_HELMET.getInstance() });
         colours.registerItemColorHandler(armourColouring, new Item[] { SSP_Items.HYBRID_SOLAR_HELMET.getInstance(), SSP_Items.ULTIMATE_HYBRID_SOLAR_HELMET.getInstance() });

    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
    	 proxy.postInit(event);
    }

    public static ResourceLocation getIdentifier(final String name) {
        return new ResourceLocation("super_solar_panels", name);
    }

 
    
    
}
