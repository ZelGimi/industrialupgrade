package com.denfop.ssp;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.fluid.neutron.FluidRegister;
import com.denfop.ssp.items.SSPItems;
import com.denfop.ssp.items.resource.CraftingThings;
import com.denfop.ssp.molecular.IMolecularTransformerRecipeManager;
import com.denfop.ssp.molecular.MTRecipe;
import com.denfop.ssp.tiles.SSPBlock;
import ic2.api.item.IC2Items;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.Recipes;
import ic2.core.recipe.ColourCarryingRecipe;
import ic2.core.util.ConfigUtil;
import ic2.core.util.StackUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.oredict.OreDictionary;

import java.text.ParseException;

public final class SPPRecipes {
	static void addCraftingRecipes() {
		final IRecipeInputFactory input = Recipes.inputFactory;
		final IRecipeInputFactory input1 = Recipes.inputFactory;
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.redcomponent), "AAA", "BBB", "AAA", 'A', IC2Items.getItem("glass", "reinforced"), 'B', Items.REDSTONE);

		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.bluecomponent), "AAA", "BBB", "AAA", 'A', IC2Items.getItem("glass", "reinforced"), 'B', new ItemStack(Items.DYE, 1, 4));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.solarsplitter), "ABC", "ABC", "ABC", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.redcomponent), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.greencomponent), 'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.bluecomponent));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.greencomponent), "A  ", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_GLASS_PANE));
		addShapelessRecipe(new ItemStack(SSPItems.SPECTRAL_SOLAR_HELMET.getInstance()), SSPItems.ULTIMATE_HYBRID_SOLAR_HELMET.getInstance(), SuperSolarPanels.machines.getItemStack(SSPBlock.spectral_solar_panel), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.spectralcore));
		addShapelessRecipe(new ItemStack(SSPItems.SINGULAR_SOLAR_HELMET.getInstance()), SSPItems.SPECTRAL_SOLAR_HELMET.getInstance(), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_IRON_PLATE), "III", "IPI", "III",

				'I', "plateIron",
				'P', "ingotIridium");
		//
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ultimate_core), "IPI",

				'I', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY),
				'P', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_ALLOY));
		//
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.advanced_core), "IPI",

				'I', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM),
				'P', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.URANIUM_INGOT));
		//
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.hybrid_core), "IPI",

				'I', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM),
				'P', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_ALLOY));
		//
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.REINFORCED_IRIDIUM_IRON_PLATE), "ACA", "CIC", "ACA",

				'A', IC2Items.getItem("crafting", "alloy"),
				'C', IC2Items.getItem("crafting", "carbon_plate"),
				'I', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_IRON_PLATE));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_REINFORCED_PLATE), "RSR", "LIL", "RDR",

				'R', Items.REDSTONE,
				'S', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_PART),
				'L', new ItemStack(Items.DYE, 1, 4),
				'I',
				SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.REINFORCED_IRIDIUM_IRON_PLATE),
				'D', Items.DIAMOND);


		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.advanced_solar_panel), "PDP", "ASA", "CIC",

				'P', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_GLASS_PANE),
				'A', IC2Items.getItem("crafting", "alloy"),
				'S', IC2Items.getItem("te", "solar_generator"),
				'C',
				IC2Items.getItem("crafting", "advanced_circuit"),
				'D',
				SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.advanced_core),

				'I', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_REINFORCED_PLATE));


		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.hybrid_solar_panel), "PLP", "IAI", "CSC",

				'P', IC2Items.getItem("crafting", "carbon_plate"),
				'L', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.hybrid_core),
				'I', IC2Items.getItem("crafting", "iridium"),
				'A',
				SuperSolarPanels.machines.getItemStack(SSPBlock.advanced_solar_panel),
				'C', IC2Items.getItem("crafting", "advanced_circuit"),
				'S', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM));


		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.ultimate_solar_panel), " S ", "SCS", " S ",

				'S', SuperSolarPanels.machines.getItemStack(SSPBlock.hybrid_solar_panel),
				'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ultimate_core));
		addShapelessRecipe(StackUtil.setSize(SuperSolarPanels.machines.getItemStack(SSPBlock.hybrid_solar_panel), 4), SuperSolarPanels.machines
				.getItemStack(SSPBlock.ultimate_solar_panel));


		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QUANTUM_CORE), "ANA", "NEN", "ANA",

				'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY),
				'N', Items.NETHER_STAR,
				'E', Items.ENDER_EYE);
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.quantum_solar_panel), " S ", "SQS", " S ",

				'S', SuperSolarPanels.machines.getItemStack(SSPBlock.ultimate_solar_panel),
				'Q', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QUANTUM_CORE));


		addShapedRecipe(new ItemStack(SSPItems.ADVANCED_SOLAR_HELMET.getInstance()), " S ", "CNC", "GTG",

				'S', SuperSolarPanels.machines.getItemStack(SSPBlock.advanced_solar_panel),
				'C', IC2Items.getItem("crafting", "advanced_circuit"),
				'N', IC2Items.getItem("nano_helmet"),
				'G',
				IC2Items.getItem("cable", "type:gold,insulation:2"),
				'T', IC2Items.getItem("te", "lv_transformer"));

		addShapedColourRecipe(new ItemStack(SSPItems.HYBRID_SOLAR_HELMET.getInstance()), " S ", "CQC", "GTG",

				'S', SuperSolarPanels.machines.getItemStack(SSPBlock.hybrid_solar_panel),
				'C', IC2Items.getItem("crafting", "advanced_circuit"),
				'Q', IC2Items.getItem("quantum_helmet"),
				'G',
				IC2Items.getItem("cable", "type:glass,insulation:0"),
				'T', IC2Items.getItem("te", "hv_transformer"));

		addShapedColourRecipe(new ItemStack(SSPItems.ULTIMATE_HYBRID_SOLAR_HELMET.getInstance()), " S ", "CQC", "GTG",

				'S', SuperSolarPanels.machines.getItemStack(SSPBlock.ultimate_solar_panel),
				'C', IC2Items.getItem("crafting", "advanced_circuit"),
				'Q', IC2Items.getItem("quantum_helmet"),
				'G',
				IC2Items.getItem("cable", "type:glass,insulation:0"),
				'T', IC2Items.getItem("te", "hv_transformer"));
		addShapelessRecipe(new ItemStack(SSPItems.ULTIMATE_HYBRID_SOLAR_HELMET.getInstance()), SSPItems.HYBRID_SOLAR_HELMET
				.getInstance(), SuperSolarPanels.machines
				.getItemStack(SSPBlock.ultimate_solar_panel));


		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_URANIUM), " G ", "GUG", " G ",

				'G', Items.GLOWSTONE_DUST,
				'U', "ingotUranium");
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_GLASS_PANE), "GGG", "UDU", "GGG",

				'G', IC2Items.getItem("glass", "reinforced"),
				'U', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_URANIUM),
				'D', Items.GLOWSTONE_DUST);
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM), "UUU", "USU", "UUU",

				'U', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_URANIUM),
				'S', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY), " S ", "SAS", " S ",

				'S', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM),
				'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_ALLOY));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_ALLOY), "III", "ISI", "III",

				'I', IC2Items.getItem("crafting", "iridium"),
				'S', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM), "SSS", "SSS", "SSS",

				'S', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_PART));

		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.enderquantumcomponent), "ABA", "BCB", "ABA", 'A', IC2Items.getItem("crafting", "iridium"), 'B', Items.ENDER_EYE, 'C', Items.NETHER_STAR);
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.spectral_solar_panel), " B ", "BAB", " B ", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.spectralcore), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.quantum_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.singular_solar_panel), " B ", "BAB", " B ", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.proton_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.admin_solar_panel), " B ", "BAB", " B ", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore2), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.singular_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.photonic_solar_panel), " B ", "BAB", " B ", 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.admin_solar_panel), 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore1));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore), "ABA", "DCD", "ABA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.enderquantumcomponent), 'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy_ingot), 'B', IC2Items.getItem("crafting", "iridium"), 'D', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY));
		addCompressorRecipe(input.forStack(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy), 9), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy_ingot));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.spectralcore), "CBC", "BAB", "CBC", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QUANTUM_CORE), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy), 'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.solarsplitter));
		addShapedRecipe(new ItemStack(SSPItems.GRAVI_CHESTPLATE.getInstance()), "CBC", "CAC", " H ", 'A', IC2Items.getItem("quantum_chestplate"), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6), 'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3), 'H', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
		addShapedRecipe(new ItemStack(SSPItems.QUANTUM_LEGGINGS.getInstance()), "CBC", "CAC", " H ", 'A', IC2Items.getItem("quantum_leggings"), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6), 'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3), 'H', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
		addShapedRecipe(new ItemStack(SSPItems.QUANTUM_BOOSTS.getInstance()), "CBC", "CAC", " H ", 'A', IC2Items.getItem("quantum_boots"), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6), 'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3), 'H', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3), "ACA", "CBC", "ACA", 'A', IC2Items.getItem("crafting", "carbon_plate"), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nanobox), 'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems4));
		addShapedRecipe(new ItemStack(SSPItems.QUANTUM_HELMET.getInstance()), "CBC", "CAC", " H ", 'A', IC2Items.getItem("quantum_helmet"), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6), 'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3), 'H', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));

		//
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy4), "ABA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.spectralcore));

		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy3), "ABA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy4), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy_ingot));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy2), "ABA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy3), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore));
		addShapedRecipe(new ItemStack(SSPItems.DRILL.getInstance()), "ODO", "COC",
				//IC2Items.getItem("crafting", "advanced_circuit")
				'O', IC2Items.getItem("upgrade", "overclocker"),
				'D', StackUtil.copyWithWildCard(IC2Items.getItem("diamond_drill")),
				'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore2), "ABA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy3), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore1), "ABA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy2), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore2));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nanobox), "ACA", "CBC", "ACA", 'A', IC2Items.getItem("crafting", "carbon_plate"), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_IRON_PLATE), 'C', IC2Items.getItem("crafting", "alloy"));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore2), "ABA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy3), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore1), "ABA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy2), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore2));
		addCompressorRecipe(input.forStack(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_INGOT), 9), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems2));
		addCompressorRecipe(input.forStack(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems2), 9), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems4));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6), " A ", "ABA", " A ", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nanobox), 'B', IC2Items.getItem("crafting", "advanced_circuit"));
		addCompressorRecipe(input.forStack(IC2Items.getItem("dust", "energium"), 9), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.dust));
		//     addShapedRecipe(new ItemStack(SSPItems.spectralsaber.getInstance()), "A","AB ", "ACD", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3),'B',  Items.DIAMOND_SWORD, 'C' ,SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5),'D' ,SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.dust));
		addShapedRecipe(new ItemStack(SSPItems.QUANTUM_SABER.getInstance()), "O  ", "OD ", "OBC",

				'O', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nanobox),
				'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.dust),
				'D', StackUtil.copyWithWildCard(IC2Items.getItem("nano_saber")),
				'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6));
		addShapedRecipe(new ItemStack(SSPItems.SPECTRAL_SABER.getInstance()), "O  ", "OD ", "OBC",

				'O', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3),
				'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.dust),
				'D', new ItemStack(SSPItems.QUANTUM_SABER.getInstance()),
				'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
		//
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5), "ACA", "CBC", "ACA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6), 'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems2));

		addCompressorRecipe(input.forStack(IC2Items.getItem("misc_resource", "iridium_ore")), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_INGOT));
		addExtrudingRecipe(input.forStack(IC2Items.getItem("crafting", "iridium")), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_INGOT));
		addCompressorRecipe(input.forStack(IC2Items.getItem("resource", "uranium_ore")), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.URANIUM_INGOT));
		addCompressorRecipe(input.forStack(IC2Items.getItem("crushed", "uranium")), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.URANIUM_INGOT));
		addCompressorRecipe(input.forStack(IC2Items.getItem("purified", "uranium")), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.URANIUM_INGOT));
		addCompressorRecipe(input.forStack(IC2Items.getItem("nuclear", "uranium")), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.URANIUM_INGOT));
//
		addCompressorRecipe(input.forStack(IC2Items.getItem("crafting", "carbon_plate"), 9), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.compresscarbon));
		addCompressorRecipe(input.forStack(IC2Items.getItem("crafting", "alloy"), 9), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.compresscarbonultra));
		addExtrudingRecipe(input.forStack(IC2Items.getItem("crafting", "coal_chunk"), 9), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.coal_chunk));
		addCompressorRecipe(input.forStack(IC2Items.getItem("nuclear", "plutonium")), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.protonshard));
		addCompressorRecipe(input.forStack(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.protonshard), 18), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.proton));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.protoncore), " B ", "ACA", " B ", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.proton), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy4), 'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.spectralcore));

		FluidStack fluidStack = new FluidStack(FluidRegister.Neutron, 1);
		if (fluidStack.getFluid() != null)
			addCompressorRecipe(input.forStack(FluidUtil.getFilledBucket(fluidStack)), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.neutronshard));
		else
			SuperSolarPanels.log.error("Neutron Fluid is null, possible duplication");

		addCompressorRecipe(input.forStack(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.neutronshard), 9), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.neutron));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.neutroncore), " A ", "ABA", " A ", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.neutron), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore2));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.neutronium_solar_panel), " B ", "BAB", " B ", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.neutroncore), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.photonic_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.proton_solar_panel), " B ", "BAB", " B ", 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.spectral_solar_panel), 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.protoncore));

		//FluidRegister.Neutron
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.nutronfabricator), " A ", "BCB", " A ", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.enderquantumcomponent), 'C', IC2Items.getItem("te", "matter_generator"), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.transformator), "BCB", 'C', IC2Items.getItem("te", "ev_transformer"), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.transformator1), "BCB", 'C', SuperSolarPanels.machines.getItemStack(SSPBlock.transformator), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.transformator2), "BCB", 'C', SuperSolarPanels.machines.getItemStack(SSPBlock.transformator1), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.transformator3), "BCB", 'C', SuperSolarPanels.machines.getItemStack(SSPBlock.transformator2), 'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
		//
		//
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.ultimatemfsu), " B ", "BCB", " B ", 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.advancedmfsu), 'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.quantummfsu), " B ", "BCB", " B ", 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.ultimatemfsu), 'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));


		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.advancedmfsu), " B ", "BCB", " B ",

				'B', new ItemStack(SSPItems.ADVANCED_CRYSTAL.getInstance(), 1, 0),
				'C', IC2Items.getItem("te", "mfsu"));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.advancedmfsu), " B ", "BCB", " B ",

				'B', new ItemStack(SSPItems.ADVANCED_CRYSTAL.getInstance(), 1, 26),
				'C', IC2Items.getItem("te", "mfsu"));
		addShapedRecipe(new ItemStack(SSPItems.ADVANCED_CRYSTAL.getInstance()), " A ", "DCD", " A ",
				'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nanobox),
				'D', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3),
				'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.dust));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.molecular_transformer), "MTM", "CcC", "MTM",

				'M', IC2Items.getItem("resource", "advanced_machine"),
				'T', IC2Items.getItem("te", "ev_transformer"),
				'C', IC2Items.getItem("crafting", "advanced_circuit"),
				'c',
				IC2Items.getItem("nuclear", "plutonium"));

//
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), "USU",

				'U', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse),
				'S', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.dust), "UUU", "UUU", "UUU",

				'U', IC2Items.getItem("dust", "energium"));

//

		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM), "UUU", "USU", "UUU",

				'U', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_URANIUM),
				'S', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY), " S ", "SAS", " S ",

				'S', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM),
				'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_ALLOY));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_ALLOY), "III", "ISI", "III",

				'I', IC2Items.getItem("crafting", "iridium"),
				'S', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM));
		addShapedRecipe(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM), "SSS", "SSS", "SSS",

				'S', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_PART));
		//
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.spectral_solar_panelsun), "BA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.spectral_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.singular_solar_panelsun), "BA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.singular_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.admin_solar_panelsun), "BA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.admin_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.photonic_solar_panelsun), "BA", 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.photonic_solar_panel), 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.spectral_solar_panelrain), "BA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.spectral_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.singular_solar_panelrain), "BA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.singular_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.admin_solar_panelrain), "BA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.admin_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.photonic_solar_panelrain), "BA", 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.photonic_solar_panel), 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse));

		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.proton_solar_panelsun), "BA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.proton_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.neutronium_solar_panelsun), "BA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.neutronium_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.proton_solar_panelrain), "BA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.proton_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.neutronium_solar_panelrain), "BA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.neutronium_solar_panel));


		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.advanced_solar_panelsun), "BA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.advanced_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.hybrid_solar_panelsun), "BA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.hybrid_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.advanced_solar_panelrain), "BA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.advanced_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.hybrid_solar_panelrain), "BA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.hybrid_solar_panel));

		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.ultimate_solar_panelsun), "BA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.ultimate_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.quantum_solar_panelsun), "BA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.quantum_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.ultimate_solar_panelrain), "BA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.ultimate_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.quantum_solar_panelrain), "BA", 'A', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.quantum_solar_panel));
		//
		addcanerRecipe(input.forStack(IC2Items.getItem("crafting", "fuel_rod"), 1), input1.forStack(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.proton)), new ItemStack(SSPItems.PROTON_FUEL_ROD.getInstance()));
		addcentrifugeRecipe(input.forStack(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_proton_fuel_rod)), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.proton), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.protonshard));
		addcentrifugeRecipe(input.forStack(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_dual_proton_fuel_rod)), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_proton_fuel_rod), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_proton_fuel_rod));
		addcentrifugeRecipe(input.forStack(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_quad_proton_fuel_rod)), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_dual_proton_fuel_rod), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_dual_proton_fuel_rod));
		addcentrifugeRecipe(input.forStack(SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_eit_proton_fuel_rod)), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_quad_proton_fuel_rod), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_quad_proton_fuel_rod));
		addShapedColourRecipe(new ItemStack(SSPItems.DUAL_PROTON_FUEL_ROD.getInstance()), "SQS",

				'S', new ItemStack(SSPItems.PROTON_FUEL_ROD.getInstance()),
				//  Character.valueOf('C'), IC2Items.getItem("crafting", "advanced_circuit"),
				'Q', IC2Items.getItem("plate", "iron")
				// Character.valueOf('G'),
				// IC2Items.getItem("cable", "type:glass,insulation:0"),
				//  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
		);
		addShapedColourRecipe(new ItemStack(SSPItems.QUAD_PROTON_FUEL_ROD.getInstance()), " S ", "CQC", " S ",

				'S', new ItemStack(SSPItems.DUAL_PROTON_FUEL_ROD.getInstance()),
				'C', IC2Items.getItem("plate", "copper"),
				'Q', IC2Items.getItem("plate", "iron")
				// Character.valueOf('G'),
				// IC2Items.getItem("cable", "type:glass,insulation:0"),
				//  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
		);
		addShapedColourRecipe(new ItemStack(SSPItems.QUAD_PROTON_FUEL_ROD.getInstance()), "SQS", "CQC", "SQS",

				'S', new ItemStack(SSPItems.PROTON_FUEL_ROD.getInstance()),
				'C', IC2Items.getItem("plate", "copper"),
				'Q', IC2Items.getItem("plate", "iron")
				// Character.valueOf('G'),
				// IC2Items.getItem("cable", "type:glass,insulation:0"),
				//  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
		);
		addShapedColourRecipe(new ItemStack(SSPItems.EIT_PROTON_FUEL_ROD.getInstance()), "SQS", "CQC", "SQS",

				'S', new ItemStack(SSPItems.DUAL_PROTON_FUEL_ROD.getInstance()),
				'C', IC2Items.getItem("plate", "copper"),
				'Q', IC2Items.getItem("plate", "iron")
				// Character.valueOf('G'),
				// IC2Items.getItem("cable", "type:glass,insulation:0"),
				//  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
		);
		addShapedColourRecipe(new ItemStack(SSPItems.EIT_PROTON_FUEL_ROD.getInstance()), " S ", "CQC", " S ",

				'S', new ItemStack(SSPItems.QUAD_PROTON_FUEL_ROD.getInstance()),
				'C', IC2Items.getItem("plate", "copper"),
				'Q', IC2Items.getItem("plate", "iron")
				// Character.valueOf('G'),
				// IC2Items.getItem("cable", "type:glass,insulation:0"),
				//  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
		);
		addShapedColourRecipe(new ItemStack(SSPItems.IRIDIUM.getInstance()), //" CQ",
				"QSQ",
				//"QC ",

				'S', IC2Items.getItem("rotor_carbon"),
				//   Character.valueOf('C'), IC2Items.getItem("crafting","carbon_rotor_blade"),
				'Q', IC2Items.getItem("misc_resource", "iridium_ore")
				// Character.valueOf('G'),
				// IC2Items.getItem("cable", "type:glass,insulation:0"),
				//  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
		);
		addShapedColourRecipe(new ItemStack(SSPItems.COMPRESSIRIDIUM.getInstance()), " S ",
				"CQC",
				" S ",

				'S', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.compresscarbon),
				'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6),
				'Q', new ItemStack(SSPItems.IRIDIUM.getInstance())
				// Character.valueOf('G'),
				// IC2Items.getItem("cable", "type:glass,insulation:0"),
				//  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
		);
		addShapedColourRecipe(new ItemStack(SSPItems.SPECTRAL.getInstance()), " S ",
				"CQC",
				" S ",

				'S', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems2),
				'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.compresscarbonultra),
				'Q', new ItemStack(SSPItems.COMPRESSIRIDIUM.getInstance())
				// Character.valueOf('G'),
				// IC2Items.getItem("cable", "type:glass,insulation:0"),
				//  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
		);
		addShapedColourRecipe(new ItemStack(SSPItems.TWELVE_HEAT_STORAGE.getInstance()), "TCT",
				"TGT",
				"TCT",

				//   Character.valueOf('S'), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems2),
				'C', IC2Items.getItem("hex_heat_storage"),
				//   Character.valueOf('Q'),new ItemStack(SSPItems.compressiridium.getInstance()),
				'G',
				IC2Items.getItem("plate", "iron"),
				'T', IC2Items.getItem("plate", "tin"));
		addShapedColourRecipe(new ItemStack(SSPItems.MAX_HEAT_STORAGE.getInstance()), "TCT",
				"TGT",
				"TCT",

				//   Character.valueOf('S'), SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems2),
				'C', new ItemStack(SSPItems.TWELVE_HEAT_STORAGE.getInstance()),
				//   Character.valueOf('Q'),new ItemStack(SSPItems.compressiridium.getInstance()),
				'G',
				IC2Items.getItem("plate", "iron"),
				'T', IC2Items.getItem("plate", "tin"));
	}

	public static void addShapedRecipe(final ItemStack output, final Object... inputs) {
		Recipes.advRecipes.addRecipe(output, inputs);
	}

	public static void addShapelessRecipe(final ItemStack output, final Object... inputs) {
		Recipes.advRecipes.addShapelessRecipe(output, inputs);
	}

	private static void addShapedColourRecipe(final ItemStack output, final Object... inputs) {
		ColourCarryingRecipe.addAndRegister(output, inputs);
	}

	private static void addCompressorRecipe(final IRecipeInput input, final ItemStack output) {
		Recipes.compressor.addRecipe(input, null, false, output);
	}

	private static void addExtrudingRecipe(IRecipeInput input, ItemStack output) {
		Recipes.metalformerExtruding.addRecipe(input, null, false, output);
	}

	//addRecipe(IRecipeInput paramIRecipeInput1, IRecipeInput paramIRecipeInput2, ItemStack paramItemStack);
	private static void addcanerRecipe(IRecipeInput input, IRecipeInput input1, ItemStack output) {
		Recipes.cannerBottle.addRecipe(input, input1, output, false);
	}

	private static void addcentrifugeRecipe(IRecipeInput input, ItemStack output, ItemStack itemStack) {
		Recipes.centrifuge.addRecipe(input, null, false, output, itemStack);
	}

	static void addMolecularTransformerRecipes() {
		SuperSolarPanels.log.info("Loading Molecular Transformer recipes from file");
		int successes = 0;
		for (MTRecipe recipe : Configs.MTRecipes) {
			try {
				if (decodeLine(recipe.lineNumber, recipe.parts))
					successes++;
			} catch (ParseException e) {
				SuperSolarPanels.log.warn("Skipping line " + recipe.lineNumber + " due to an error parsing", e);
			}
		}
		SuperSolarPanels.log.info("Load complete, successfully loaded {} out of {}.", successes, Configs.MTRecipes.length);
	}

	private static boolean decodeLine(int number, String[] parts) throws ParseException {
		int energy;
		IRecipeInput input = ConfigUtil.asRecipeInputWithAmount(parts[0].trim());
		if (input == null) {
			SuperSolarPanels.log.warn("Skipping line {} as the input ({}) cannot be resolved", number, parts[0].trim());
			return false;
		}
		ItemStack output = ConfigUtil.asStackWithAmount(parts[1].trim());
		if (output == null) {
			String attempt = parts[1].trim();
			if (attempt.startsWith("OreDict:")) {
				NonNullList<ItemStack> nonNullList = OreDictionary.getOres(attempt.substring(attempt.indexOf(':') + 1).trim());
				if (!nonNullList.isEmpty()) {
					output = nonNullList.get(0);
					SuperSolarPanels.log.debug("Continued on line {} as the output ({}) could be resolved to {}", number, attempt, output);
				}
			}
			if (output == null) {
				SuperSolarPanels.log.warn("Skipping line {} as the output ({}) cannot be resolved", number, attempt);
				return false;
			}
		}
		try {
			energy = Integer.parseInt(parts[2].trim());
		} catch (NumberFormatException e) {
			SuperSolarPanels.log.warn("Skipping line {} as the energy ({}) cannot be resolved to a number", number, parts[2].trim());
			return false;
		}
		if (!IMolecularTransformerRecipeManager.RECIPES.addRecipe(input, energy, output, false)) {
			SuperSolarPanels.log.warn("Skipping line {} as the recipe is a duplicate", number);
			return false;
		}
		return true;
	}
}
