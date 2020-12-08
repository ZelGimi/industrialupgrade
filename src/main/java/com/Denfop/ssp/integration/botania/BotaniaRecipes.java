package com.Denfop.ssp.integration.botania;

import com.Denfop.ssp.SuperSolarPanels;
import com.Denfop.ssp.items.SSP_Items;
import com.Denfop.ssp.items.resource.CraftingThings;
import com.Denfop.ssp.tiles.SSPBlock;

import ic2.api.item.IC2Items;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.Recipes;
import ic2.core.block.ITeBlock;
import ic2.core.recipe.ColourCarryingRecipe;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

public class BotaniaRecipes {
   	final static IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
	public static void addCraftingRecipes() {
		 BotaniaAPI.registerRuneAltarRecipe(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_energy), 12000, new Object[] { LibOreDict.RUNE[0], LibOreDict.RUNE[1], SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_PART), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.URANIUM_INGOT), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_INGOT), IC2Items.getItem("crafting", "alloy"),BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.elementium_plate) });
		 BotaniaAPI.registerRuneAltarRecipe(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_sun),12000, new Object[] { LibOreDict.RUNE[2], LibOreDict.RUNE[3], SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_URANIUM), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_INGOT), IC2Items.getItem("crafting", "carbon_plate"), BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.elementium_plate)});
		 BotaniaAPI.registerRuneAltarRecipe(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_night), 12000, new Object[] { LibOreDict.RUNE[1], LibOreDict.RUNE[3],SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.dust), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_URANIUM), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_INGOT), IC2Items.getItem("crafting", "coal_chunk"), BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.manasteel_plate)});
		
        addCompressorRecipe(input.forStack(new ItemStack(ModItems.manaResource, 1, 4)), BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.terrasteel_plate));
        addCompressorRecipe(input.forStack(new ItemStack(ModItems.manaResource, 1, 0)), BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.manasteel_plate));
        addCompressorRecipe(input.forStack(new ItemStack(ModItems.manaResource, 1, 7)), BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.elementium_plate));
        addShapedRecipe(new ItemStack(BotaniaItems.terrasteeldrill.getInstance()), new Object[] { " L ", "ODO", "COC", 
        	      //IC2Items.getItem("crafting", "advanced_circuit")          
        	                Character.valueOf('O'), IC2Items.getItem("upgrade", "overclocker"), 
        	                Character.valueOf('D'), StackUtil.copyWithWildCard(IC2Items.getItem("diamond_drill")), 
        	                Character.valueOf('C'), BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.terrasteel_plate)
        	               , Character.valueOf('L'),ModItems.terraPick}); 
        addShapedRecipe(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.terrasteel_core), new Object[] { "KLM", "DOD", "CHC", 
      	      //IC2Items.getItem("crafting", "advanced_circuit")          
      	                     Character.valueOf('C'), BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.elementium_plate)
           	           
      	               // SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ele)
      	                , 
      	                Character.valueOf('D'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_GLASS_PANE), 
      	                Character.valueOf('O'), BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.terrasteel_plate)
      	               , Character.valueOf('L'),IC2Items.getItem("crafting", "alloy")
      	             , Character.valueOf('K'),BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_night),
      	           Character.valueOf('M'),BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_sun),
      	         Character.valueOf('H'),BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_energy)}); 
        addShapedRecipe(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.elementium_core), new Object[] { "KLM", "DOD", "CHC", 
        	      //IC2Items.getItem("crafting", "advanced_circuit")          
        	                     Character.valueOf('C'), BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.elementium_plate)
             	           
        	               // SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ele)
        	                , 
        	                Character.valueOf('D'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_PART), 
        	                Character.valueOf('O'), BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.manasteel_core)
        	               , Character.valueOf('L'),IC2Items.getItem("crafting", "advanced_circuit")
        	             , Character.valueOf('K'),BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_night),
        	           Character.valueOf('M'),BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_sun),
        	         Character.valueOf('H'),BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_energy)}); 
	    //
        addShapedRecipe(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.manasteel_core), new Object[] { "KLM", "DOD", "CHC", 
      	      //IC2Items.getItem("crafting", "advanced_circuit")          
      	                     Character.valueOf('C'), BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.manasteel_plate)
           	           
      	               // SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ele)
      	                , 
      	                Character.valueOf('D'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_PART), 
      	                Character.valueOf('O'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.advanced_core)
      	               , Character.valueOf('L'),IC2Items.getItem("crafting", "advanced_circuit")
      	             , Character.valueOf('K'),BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_night),
      	           Character.valueOf('M'),BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_sun),
      	         Character.valueOf('H'),BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.rune_energy)}); 
	    //
        addShapedRecipe(SuperSolarPanels.machines1.getItemStack((ITeBlock)BotaniaMain.manasteel_solar_panel), "BBB", "BAB", "BBB", 'A', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.manasteel_core), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.advanced_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines1.getItemStack((ITeBlock)BotaniaMain.elementium_solar_panel), "BBB", "BAB", "BBB", 'A', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.elementium_core), 'B', SuperSolarPanels.machines1.getItemStack((ITeBlock)BotaniaMain.manasteel_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines1.getItemStack((ITeBlock)BotaniaMain.terrasteel_solar_panel), "BBB", "BAB", "BBB", 'A', BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.terrasteel_core), 'B', SuperSolarPanels.machines1.getItemStack((ITeBlock)BotaniaMain.elementium_solar_panel));
    //
        addShapedColourRecipe(new ItemStack(BotaniaItems.dual_terrasteel_fuel_rod.getInstance()), new Object[] { 
                "SQS",
               
               Character.valueOf('S'), new ItemStack(BotaniaItems.terrasteel_fuel_rod.getInstance()), 
             //  Character.valueOf('C'), IC2Items.getItem("crafting", "advanced_circuit"), 
               Character.valueOf('Q'), IC2Items.getItem("plate","iron"), 
              // Character.valueOf('G'), 
              // IC2Items.getItem("cable", "type:glass,insulation:0"), 
             //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
               });
       addShapedColourRecipe(new ItemStack(BotaniaItems.quad_terrasteel_fuel_rod.getInstance()), new Object[] { 
               " S ", "CQC"," S ",
              
              Character.valueOf('S'), new ItemStack(BotaniaItems.dual_terrasteel_fuel_rod.getInstance()), 
            Character.valueOf('C'), IC2Items.getItem("plate","copper"), 
              Character.valueOf('Q'), IC2Items.getItem("plate","iron"), 
             // Character.valueOf('G'), 
             // IC2Items.getItem("cable", "type:glass,insulation:0"), 
            //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
              });
       addShapedColourRecipe(new ItemStack(BotaniaItems.quad_terrasteel_fuel_rod.getInstance()), new Object[] { 
               "SQS", "CQC","SQS",
              
              Character.valueOf('S'), new ItemStack(BotaniaItems.terrasteel_fuel_rod.getInstance()), 
            Character.valueOf('C'), IC2Items.getItem("plate","copper"), 
              Character.valueOf('Q'), IC2Items.getItem("plate","iron"), 
             // Character.valueOf('G'), 
             // IC2Items.getItem("cable", "type:glass,insulation:0"), 
            //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
              });
       addShapedColourRecipe(new ItemStack(BotaniaItems.eit_terrasteel_fuel_rod.getInstance()), new Object[] { 
               "SQS", "CQC","SQS",
              
              Character.valueOf('S'), new ItemStack(BotaniaItems.dual_terrasteel_fuel_rod.getInstance()), 
            Character.valueOf('C'), IC2Items.getItem("plate","copper"), 
              Character.valueOf('Q'), IC2Items.getItem("plate","iron"), 
             // Character.valueOf('G'), 
             // IC2Items.getItem("cable", "type:glass,insulation:0"), 
            //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
              });
       addShapedColourRecipe(new ItemStack(BotaniaItems.eit_terrasteel_fuel_rod.getInstance()), new Object[] { 
               " S ", "CQC"," S ",
              
              Character.valueOf('S'), new ItemStack(BotaniaItems.quad_terrasteel_fuel_rod.getInstance()), 
            Character.valueOf('C'), IC2Items.getItem("plate","copper"), 
              Character.valueOf('Q'), IC2Items.getItem("plate","iron"), 
             // Character.valueOf('G'), 
             // IC2Items.getItem("cable", "type:glass,insulation:0"), 
            //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
              });
       addcanerRecipe(input.forStack(IC2Items.getItem("crafting", "fuel_rod"), 1), input.forStack(new ItemStack(ModItems.manaResource, 1, 4)), new ItemStack(BotaniaItems.terrasteel_fuel_rod.getInstance()));
       
       addcentrifugeRecipe(input.forStack(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.depleted_terrasteel_fuel_rod)),new ItemStack(ModItems.manaResource, 1, 4));
       addcentrifugeRecipe(input.forStack(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.depleted_dual_terrasteel_fuel_rod)),new ItemStack(ModItems.manaResource, 2, 4));
       addcentrifugeRecipe(input.forStack(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.depleted_quad_terrasteel_fuel_rod)),new ItemStack(ModItems.manaResource, 4, 4));
       addcentrifugeRecipe(input.forStack(BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.depleted_eit_terrasteel_fuel_rod)),new ItemStack(ModItems.manaResource, 8, 4));
		}
	  private static void addcanerRecipe(IRecipeInput input,IRecipeInput input1, ItemStack output) {
	        ic2.api.recipe.Recipes.cannerBottle.addRecipe(input, input1,  output , false);
	      }
	    private static void addcentrifugeRecipe(IRecipeInput input, ItemStack output) {
	        ic2.api.recipe.Recipes.centrifuge.addRecipe(input, null,false,new ItemStack[] { output }) ;
	      }
	    private static void addExtrudingRecipe(IRecipeInput input, ItemStack output) {
	        ic2.api.recipe.Recipes.metalformerExtruding.addRecipe(input, null, false, new ItemStack[] { output });
	      }
	    private static void addShapedColourRecipe(final ItemStack output, final Object... inputs) {
	        ColourCarryingRecipe.addAndRegister(output, inputs);
	    }
	    private static void addCompressorRecipe(final IRecipeInput input, final ItemStack output) {
	        ic2.api.recipe.Recipes.compressor.addRecipe(input, (NBTTagCompound)null, false, new ItemStack[] { output });
	    }
	  
	    
	    public static void addShapedRecipe(final ItemStack output, final Object... inputs) {
	        Recipes.advRecipes.addRecipe(output, inputs);
	    }
	    public static void addShapelessRecipe(final ItemStack output, final Object... inputs) {
	        Recipes.advRecipes.addShapelessRecipe(output, inputs);
	    }
}
