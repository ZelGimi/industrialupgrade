package com.denfop.ssp;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.common.Constants;
import com.denfop.ssp.common.SSPSourceTab;
import com.denfop.ssp.fluid.neutron.BlockRegister;
import com.denfop.ssp.fluid.neutron.FluidRegister;
import com.denfop.ssp.gui.ProgressBars;
import com.denfop.ssp.items.SSPItems;
import com.denfop.ssp.items.resource.CraftingThings;
import com.denfop.ssp.keyboard.SSPKeys;
import com.denfop.ssp.molecular.PrettyMolecularTransformerTESR;
import com.denfop.ssp.molecular.TileEntityMolecularAssembler;
import com.denfop.ssp.proxy.CommonProxy;
import com.denfop.ssp.tiles.SSPBlock;
import ic2.api.event.TeBlockFinalCallEvent;
import ic2.core.block.BlockTileEntity;
import ic2.core.block.TeBlockRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderExceptionModCrash;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

import java.util.logging.Level;

@SuppressWarnings({"ALL", "UnnecessaryFullyQualifiedName"})
@Mod.EventBusSubscriber
@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, dependencies = Constants.MOD_DEPS, version = Constants.MOD_VERSION, acceptedMinecraftVersions = "[1.12,1.12.2]", certificateFingerprint = Constants.MOD_CERTIFICATE)
public final class SuperSolarPanels {

	public static final CreativeTabs SSPTab = new SSPSourceTab("SSPSourceTab");
	public static Logger log;

	public static BlockTileEntity machines;
	@SidedProxy(clientSide = "com.denfop.ssp.proxy.ClientProxy", serverSide = "com.denfop.ssp.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static SuperSolarPanels instance;

	static {
		FluidRegistry.enableUniversalBucket();
	}

	@SubscribeEvent
	public static void register(final TeBlockFinalCallEvent event) {
		if (Loader.isModLoaded("advanced_solar_panels"))
			throw new LoaderExceptionModCrash("SuperSolarPanels incompatible with AdvancedSolarPanels. sorry", new Throwable());

		TeBlockRegistry.addAll(SSPBlock.class, SSPBlock.IDENTITY);
		TeBlockRegistry.setDefaultMaterial(SSPBlock.IDENTITY, Material.ROCK);
	}

	public static ResourceLocation getIdentifier(final String name) {
		return new ResourceLocation(Constants.MOD_ID, name);
	}

	@Mod.EventHandler
	public void load(final FMLPreInitializationEvent event) {
		proxy.preInit(event);
		SuperSolarPanels.log = event.getModLog();
		Configs.loadConfig(event.getSuggestedConfigurationFile(), event.getSide().isClient());
		SuperSolarPanels.machines = TeBlockRegistry.get(SSPBlock.IDENTITY);
		if (event.getSide().isClient())
			setupRenderingGuf();
		SSPItems.buildItems(event.getSide());
		FluidRegister.register();
		BlockRegister.register();
		SSPKeys.addFlyKey();
		SPPRecipes.addMolecularTransformerRecipes();
		OreDictionary.registerOre("enderquantumcomponent", SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.enderquantumcomponent));
		OreDictionary.registerOre("solarsplitter", SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.solarsplitter));
		OreDictionary.registerOre("bluecomponent", SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.bluecomponent));
		OreDictionary.registerOre("greencomponent", SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.greencomponent));
		OreDictionary.registerOre("redcomponent", SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.redcomponent));
		OreDictionary.registerOre("singularcore", SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore));
		OreDictionary.registerOre("photoniy", SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy));
		OreDictionary.registerOre("photoniy_ingot", SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy_ingot));
		OreDictionary.registerOre("spectralcore", SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.spectralcore));

		OreDictionary.registerOre("ingotUranium", SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.URANIUM_INGOT));
		OreDictionary.registerOre("ingotIridium", SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_INGOT));

		OreDictionary.registerOre("craftingSunnariumPart", SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_PART));
		OreDictionary.registerOre("craftingSunnarium", SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM));

	}

	@SideOnly(Side.CLIENT)
	private static void setupRenderingGuf() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMolecularAssembler.class, new PrettyMolecularTransformerTESR());
		ForgeHooksClient.registerTESRItemStack(machines.getItem(), SSPBlock.molecular_transformer.getId(), SSPBlock.molecular_transformer.getTeClass());
	}

	@Mod.EventHandler
	public void init(final FMLInitializationEvent event) {
		proxy.init(event);
		SSPBlock.buildDummies();
		SPPRecipes.addCraftingRecipes();
		ProgressBars.addStyles();
		TileEntityMolecularAssembler.MolecularOutput.registerNetwork();
	}

	@Mod.EventHandler
	public void postInit(final FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@Mod.EventHandler
	public void init(final FMLFingerprintViolationEvent event) {
		java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
				"Invalid fingerprint detected! The file " + event.getSource().getName() +
						" may have been tampered with. This version will NOT be supported by the author!");
	}
}
