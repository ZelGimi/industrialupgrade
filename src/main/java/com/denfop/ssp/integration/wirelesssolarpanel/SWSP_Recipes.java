package com.denfop.ssp.integration.wirelesssolarpanel;

import com.denfop.ssp.SuperSolarPanels;
import com.denfop.ssp.items.SSP_Items;
import com.denfop.ssp.items.resource.CraftingThings;
import com.denfop.ssp.tiles.SSPBlock;

import ic2.api.item.IC2Items;
import ic2.api.recipe.Recipes;
import ic2.core.block.ITeBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import ru.wirelesstools.MainWV;
import ru.wirelesstools.WP_Items;
import ru.wirelesstools.items.ItemsForCraft;
import ru.wirelesstools.tileentities.TileEntitiesWV;

public class SWSP_Recipes {

	public static void addCraftingRecipes() {
		 addShapedRecipe(MainWV.machines.getItemStack((ITeBlock)TileEntitiesWV.singular_solar_panel_personal), new Object[] { "AAA", "ABA", "AAA", 
				 Character.valueOf('B'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore) ,
		      
		          Character.valueOf('A'), MainWV.machines.getItemStack((ITeBlock)TileEntitiesWV.spectral_solar_panel_personal) });
		    addShapedRecipe(MainWV.machines.getItemStack((ITeBlock)TileEntitiesWV.absorbing_solar_panel_personal), new Object[] { "AAA", "ABA", "AAA", 
		    		 Character.valueOf('B'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore2) ,
		          Character.valueOf('A'), MainWV.machines.getItemStack((ITeBlock)TileEntitiesWV.singular_solar_panel_personal) });
		    addShapedRecipe(MainWV.machines.getItemStack((ITeBlock)TileEntitiesWV.photonic_solar_panel_personal), new Object[] { "AAA", "ABA", "AAA", 
		    		 Character.valueOf('B'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore1) ,
		          Character.valueOf('A'), MainWV.machines.getItemStack((ITeBlock)TileEntitiesWV.absorbing_solar_panel_personal) });
		    addShapedRecipe(MainWV.machines.getItemStack((ITeBlock)TileEntitiesWV.neutron_solar_panel_personal), new Object[] { "AAA", "ABA", "AAA", 
		    		 Character.valueOf('B'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.neutroncore) ,
		          Character.valueOf('A'), MainWV.machines.getItemStack((ITeBlock)TileEntitiesWV.photonic_solar_panel_personal) });
		    addShapedRecipe(MainWV.machines.getItemStack((ITeBlock)TileEntitiesWV.spectral_solar_panel_personal), new Object[] { "ACA", "ABA", "AAA", 
		            
		            Character.valueOf('A'), SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.quantum_solar_panel), 
		            Character.valueOf('B'), WP_Items.CRAFTING.getItemStack(ItemsForCraft.CraftingTypes.wirelessmodule),
		            Character.valueOf('C'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.spectralcore) });
	}
	private static void addShapedRecipe(ItemStack output, Object... inputs) {
	    Recipes.advRecipes.addRecipe(output, inputs);
	  }
	
}
