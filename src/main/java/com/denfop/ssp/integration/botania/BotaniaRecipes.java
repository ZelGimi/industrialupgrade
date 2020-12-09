package com.denfop.ssp.integration.botania;

import com.denfop.ssp.SuperSolarPanels;
import com.denfop.ssp.items.SSP_Items;
import com.denfop.ssp.items.resource.CraftingThings;
import com.denfop.ssp.tiles.SSPBlock;
import ic2.api.item.IC2Items;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.Recipes;
import ic2.core.recipe.ColourCarryingRecipe;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

public class BotaniaRecipes {
	final static IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;

	public static void addCraftingRecipes() {
		BotaniaAPI.registerRuneAltarRecipe(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_energy), 12000, LibOreDict.RUNE[0], LibOreDict.RUNE[1], SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_PART), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.URANIUM_INGOT), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_INGOT), IC2Items.getItem("crafting", "alloy"), BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.elementium_plate));
		BotaniaAPI.registerRuneAltarRecipe(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_sun), 12000, LibOreDict.RUNE[2], LibOreDict.RUNE[3], SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_URANIUM), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_INGOT), IC2Items.getItem("crafting", "carbon_plate"), BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.elementium_plate));
		BotaniaAPI.registerRuneAltarRecipe(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_night), 12000, LibOreDict.RUNE[1], LibOreDict.RUNE[3], SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.dust), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_URANIUM), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_INGOT), IC2Items.getItem("crafting", "coal_chunk"), BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.manasteel_plate));

		addCompressorRecipe(input.forStack(new ItemStack(ModItems.manaResource, 1, 4)), BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.terrasteel_plate));
		addCompressorRecipe(input.forStack(new ItemStack(ModItems.manaResource, 1, 0)), BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.manasteel_plate));
		addCompressorRecipe(input.forStack(new ItemStack(ModItems.manaResource, 1, 7)), BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.elementium_plate));
		addShapedRecipe(new ItemStack(BotaniaItems.terrasteeldrill.getInstance()), " L ", "ODO", "COC",
				//IC2Items.getItem("crafting", "advanced_circuit")
				'O', IC2Items.getItem("upgrade", "overclocker"),
				'D', StackUtil.copyWithWildCard(IC2Items.getItem("diamond_drill")),
				'C', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.terrasteel_plate),
				'L', ModItems.terraPick);
		addShapedRecipe(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.terrasteel_core), "KLM", "DOD", "CHC",
				//IC2Items.getItem("crafting", "advanced_circuit")
				'C', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.elementium_plate)

				// SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ele)
				,
				'D', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_GLASS_PANE),
				'O', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.terrasteel_plate),
				'L', IC2Items.getItem("crafting", "alloy"),
				'K', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_night),
				'M', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_sun),
				'H', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_energy));
		addShapedRecipe(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.elementium_core), "KLM", "DOD", "CHC",
				//IC2Items.getItem("crafting", "advanced_circuit")
				'C', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.elementium_plate),

				// SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ele)
				'D', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_PART),
				'O', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.manasteel_core),
				'L', IC2Items.getItem("crafting", "advanced_circuit"),
				'K', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_night),
				'M', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_sun),
				'H', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_energy));
		//
		addShapedRecipe(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.manasteel_core), "KLM", "DOD", "CHC",
				//IC2Items.getItem("crafting", "advanced_circuit")
				'C', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.manasteel_plate),

				// SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ele)
				'D', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_PART),
				'O', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.advanced_core),
				'L', IC2Items.getItem("crafting", "advanced_circuit"),
				'K', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_night),
				'M', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_sun),
				'H', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_energy));
		//
		addShapedRecipe(SuperSolarPanels.machines1.getItemStack(BotaniaMain.manasteel_solar_panel), "BBB", "BAB", "BBB", 'A', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.manasteel_core), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.advanced_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines1.getItemStack(BotaniaMain.elementium_solar_panel), "BBB", "BAB", "BBB", 'A', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.elementium_core), 'B', SuperSolarPanels.machines1.getItemStack(BotaniaMain.manasteel_solar_panel));
		addShapedRecipe(SuperSolarPanels.machines1.getItemStack(BotaniaMain.terrasteel_solar_panel), "BBB", "BAB", "BBB", 'A', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.terrasteel_core), 'B', SuperSolarPanels.machines1.getItemStack(BotaniaMain.elementium_solar_panel));
		//
		addShapedColourRecipe(new ItemStack(BotaniaItems.dual_terrasteel_fuel_rod.getInstance()), "SQS",

				'S', new ItemStack(BotaniaItems.terrasteel_fuel_rod.getInstance()),
				//  Character.valueOf('C'), IC2Items.getItem("crafting", "advanced_circuit"),
				'Q', IC2Items.getItem("plate", "iron")
				// Character.valueOf('G'),
				// IC2Items.getItem("cable", "type:glass,insulation:0"),
				//  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
		);
		addShapedColourRecipe(new ItemStack(BotaniaItems.quad_terrasteel_fuel_rod.getInstance()), " S ", "CQC", " S ",

				'S', new ItemStack(BotaniaItems.dual_terrasteel_fuel_rod.getInstance()),
				'C', IC2Items.getItem("plate", "copper"),
				'Q', IC2Items.getItem("plate", "iron")
				// Character.valueOf('G'),
				// IC2Items.getItem("cable", "type:glass,insulation:0"),
				//  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
		);
		addShapedColourRecipe(new ItemStack(BotaniaItems.quad_terrasteel_fuel_rod.getInstance()), "SQS", "CQC", "SQS",

				'S', new ItemStack(BotaniaItems.terrasteel_fuel_rod.getInstance()),
				'C', IC2Items.getItem("plate", "copper"),
				'Q', IC2Items.getItem("plate", "iron")
				// Character.valueOf('G'),
				// IC2Items.getItem("cable", "type:glass,insulation:0"),
				//  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
		);
		addShapedColourRecipe(new ItemStack(BotaniaItems.eit_terrasteel_fuel_rod.getInstance()), "SQS", "CQC", "SQS",

				'S', new ItemStack(BotaniaItems.dual_terrasteel_fuel_rod.getInstance()),
				'C', IC2Items.getItem("plate", "copper"),
				'Q', IC2Items.getItem("plate", "iron")
				// Character.valueOf('G'),
				// IC2Items.getItem("cable", "type:glass,insulation:0"),
				//  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
		);
		addShapedColourRecipe(new ItemStack(BotaniaItems.eit_terrasteel_fuel_rod.getInstance()), " S ", "CQC", " S ",

				'S', new ItemStack(BotaniaItems.quad_terrasteel_fuel_rod.getInstance()),
				'C', IC2Items.getItem("plate", "copper"),
				'Q', IC2Items.getItem("plate", "iron")
				// Character.valueOf('G'),
				// IC2Items.getItem("cable", "type:glass,insulation:0"),
				//  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
		);
		addcanerRecipe(input.forStack(IC2Items.getItem("crafting", "fuel_rod"), 1), input.forStack(new ItemStack(ModItems.manaResource, 1, 4)), new ItemStack(BotaniaItems.terrasteel_fuel_rod.getInstance()));

		addcentrifugeRecipe(input.forStack(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.depleted_terrasteel_fuel_rod)), new ItemStack(ModItems.manaResource, 1, 4));
		addcentrifugeRecipe(input.forStack(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.depleted_dual_terrasteel_fuel_rod)), new ItemStack(ModItems.manaResource, 2, 4));
		addcentrifugeRecipe(input.forStack(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.depleted_quad_terrasteel_fuel_rod)), new ItemStack(ModItems.manaResource, 4, 4));
		addcentrifugeRecipe(input.forStack(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.depleted_eit_terrasteel_fuel_rod)), new ItemStack(ModItems.manaResource, 8, 4));
	}

	private static void addCompressorRecipe(final IRecipeInput input, final ItemStack output) {
		ic2.api.recipe.Recipes.compressor.addRecipe(input, null, false, output);
	}

	public static void addShapedRecipe(final ItemStack output, final Object... inputs) {
		Recipes.advRecipes.addRecipe(output, inputs);
	}

	private static void addShapedColourRecipe(final ItemStack output, final Object... inputs) {
		ColourCarryingRecipe.addAndRegister(output, inputs);
	}

	private static void addcanerRecipe(IRecipeInput input, IRecipeInput input1, ItemStack output) {
		ic2.api.recipe.Recipes.cannerBottle.addRecipe(input, input1, output, false);
	}

	private static void addcentrifugeRecipe(IRecipeInput input, ItemStack output) {
		ic2.api.recipe.Recipes.centrifuge.addRecipe(input, null, false, output);
	}

	private static void addExtrudingRecipe(IRecipeInput input, ItemStack output) {
		ic2.api.recipe.Recipes.metalformerExtruding.addRecipe(input, null, false, output);
	}

	public static void addShapelessRecipe(final ItemStack output, final Object... inputs) {
		Recipes.advRecipes.addShapelessRecipe(output, inputs);
	}
}
