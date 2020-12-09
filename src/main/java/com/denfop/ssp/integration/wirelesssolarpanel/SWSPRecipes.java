package com.denfop.ssp.integration.wirelesssolarpanel;

import com.denfop.ssp.SuperSolarPanels;
import com.denfop.ssp.items.SSPItems;
import com.denfop.ssp.items.resource.CraftingThings;
import com.denfop.ssp.tiles.SSPBlock;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import ru.wirelesstools.MainWV;
import ru.wirelesstools.WP_Items;
import ru.wirelesstools.items.ItemsForCraft;
import ru.wirelesstools.tileentities.TileEntitiesWV;

public class SWSPRecipes {

	public static void addCraftingRecipes() {
		addShapedRecipe(MainWV.machines.getItemStack(TileEntitiesWV.singular_solar_panel_personal), "AAA", "ABA", "AAA",
				'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore),

				'A', MainWV.machines.getItemStack(TileEntitiesWV.spectral_solar_panel_personal));
		addShapedRecipe(MainWV.machines.getItemStack(TileEntitiesWV.absorbing_solar_panel_personal), "AAA", "ABA", "AAA",
				'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore2),
				'A', MainWV.machines.getItemStack(TileEntitiesWV.singular_solar_panel_personal));
		addShapedRecipe(MainWV.machines.getItemStack(TileEntitiesWV.photonic_solar_panel_personal), "AAA", "ABA", "AAA",
				'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore1),
				'A', MainWV.machines.getItemStack(TileEntitiesWV.absorbing_solar_panel_personal));
		addShapedRecipe(MainWV.machines.getItemStack(TileEntitiesWV.neutron_solar_panel_personal), "AAA", "ABA", "AAA",
				'B', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.neutroncore),
				'A', MainWV.machines.getItemStack(TileEntitiesWV.photonic_solar_panel_personal));
		addShapedRecipe(MainWV.machines.getItemStack(TileEntitiesWV.spectral_solar_panel_personal), "ACA", "ABA", "AAA",

				'A', SuperSolarPanels.machines.getItemStack(SSPBlock.quantum_solar_panel),
				'B', WP_Items.CRAFTING.getItemStack(ItemsForCraft.CraftingTypes.wirelessmodule),
				'C', SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.spectralcore));
	}

	private static void addShapedRecipe(ItemStack output, Object... inputs) {
		Recipes.advRecipes.addRecipe(output, inputs);
	}

}
