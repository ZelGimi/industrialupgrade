package com.denfop.ssp;

import com.denfop.ssp.fluid.neutron.FluidRegister;
import com.denfop.ssp.items.SSP_Items;
import com.denfop.ssp.items.resource.CraftingThings;
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

final class SPPRecipes
{
    static void addCraftingRecipes() {
    	final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
    	final IRecipeInputFactory input1 = ic2.api.recipe.Recipes.inputFactory;
    	addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.redcomponent), "AAA", "BBB", "AAA" ,'A', IC2Items.getItem("glass", "reinforced"), 'B', Items.REDSTONE);
        
    	addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.bluecomponent), "AAA", "BBB", "AAA", 'A', IC2Items.getItem("glass", "reinforced"), 'B', new ItemStack(Items.DYE, 1, 4));
         addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.solarsplitter), "ABC", "ABC", "ABC", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.redcomponent), 'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.greencomponent), 'C', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.bluecomponent));
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.greencomponent), "A  ", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_GLASS_PANE));
        addShapelessRecipe(new ItemStack(SSP_Items.Spectral_SOLAR_HELMET.getInstance()), SSP_Items.ULTIMATE_HYBRID_SOLAR_HELMET.getInstance(), SuperSolarPanels.machines.getItemStack(SSPBlock.spectral_solar_panel) , SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.spectralcore));
        addShapelessRecipe(new ItemStack(SSP_Items.Singular_SOLAR_HELMET.getInstance()), SSP_Items.Spectral_SOLAR_HELMET.getInstance() , SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore));
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_IRON_PLATE), "III", "IPI", "III",

                'I', "plateIron",
                'P', "ingotIridium");
          //
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ultimate_core), "IPI",

                  'I', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY),
                  'P', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_ALLOY));
          //
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.advanced_core), "IPI",

                  'I', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM),
                  'P',SSP_Items.CRAFTING.getItemStack( CraftingThings.CraftingTypes.URANIUM_INGOT));
          //
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.hybrid_core), "IPI",

                  'I', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM),
                  'P', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_ALLOY ));
          //
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.REINFORCED_IRIDIUM_IRON_PLATE), "ACA", "CIC", "ACA",

                  'A', IC2Items.getItem("crafting", "alloy"),
                  'C', IC2Items.getItem("crafting", "carbon_plate"),
                  'I', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_IRON_PLATE));
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_REINFORCED_PLATE), "RSR", "LIL", "RDR",

                  'R', Items.REDSTONE,
                  'S', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_PART),
                  'L', new ItemStack(Items.DYE, 1, 4),
                  'I',
                  SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.REINFORCED_IRIDIUM_IRON_PLATE),
                  'D', Items.DIAMOND);
        
                
                addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.advanced_solar_panel), "PDP", "ASA", "CIC",

                        'P', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_GLASS_PANE),
                        'A', IC2Items.getItem("crafting", "alloy"),
                        'S', IC2Items.getItem("te", "solar_generator"),
                        'C',
                        IC2Items.getItem("crafting", "advanced_circuit"),
                        'D',
                        SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.advanced_core),

                        'I', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_REINFORCED_PLATE));
               
         
              addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.hybrid_solar_panel), "PLP", "IAI", "CSC",

                      'P', IC2Items.getItem("crafting", "carbon_plate"),
                      'L', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.hybrid_core),
                      'I', IC2Items.getItem("crafting", "iridium"),
                      'A',
                      SuperSolarPanels.machines.getItemStack(SSPBlock.advanced_solar_panel),
                      'C', IC2Items.getItem("crafting", "advanced_circuit"),
                      'S', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM));
            
           
           
             
        
            addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.ultimate_solar_panel), " S ", "SCS", " S ",

                    'S', SuperSolarPanels.machines.getItemStack(SSPBlock.hybrid_solar_panel),
                    'C', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ultimate_core));
            addShapelessRecipe(StackUtil.setSize(SuperSolarPanels.machines.getItemStack(SSPBlock.hybrid_solar_panel), 4), SuperSolarPanels.machines
                  .getItemStack(SSPBlock.ultimate_solar_panel));
         
          
            addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QUANTUM_CORE), "ANA", "NEN", "ANA",

                    'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY),
                    'N', Items.NETHER_STAR,
                    'E', Items.ENDER_EYE);
            addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.quantum_solar_panel), " S ", "SQS", " S ",

                    'S', SuperSolarPanels.machines.getItemStack(SSPBlock.ultimate_solar_panel),
                    'Q', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QUANTUM_CORE));
          
          
            addShapedRecipe(new ItemStack(SSP_Items.ADVANCED_SOLAR_HELMET.getInstance()), " S ", "CNC", "GTG",

                    'S', SuperSolarPanels.machines.getItemStack(SSPBlock.advanced_solar_panel),
                    'C', IC2Items.getItem("crafting", "advanced_circuit"),
                    'N', IC2Items.getItem("nano_helmet"),
                    'G',
                    IC2Items.getItem("cable", "type:gold,insulation:2"),
                    'T', IC2Items.getItem("te", "lv_transformer"));
          
            addShapedColourRecipe(new ItemStack(SSP_Items.HYBRID_SOLAR_HELMET.getInstance()), " S ", "CQC", "GTG",

                    'S', SuperSolarPanels.machines.getItemStack(SSPBlock.hybrid_solar_panel),
                    'C', IC2Items.getItem("crafting", "advanced_circuit"),
                    'Q', IC2Items.getItem("quantum_helmet"),
                    'G',
                    IC2Items.getItem("cable", "type:glass,insulation:0"),
                    'T', IC2Items.getItem("te", "hv_transformer"));
         
            addShapedColourRecipe(new ItemStack(SSP_Items.ULTIMATE_HYBRID_SOLAR_HELMET.getInstance()), " S ", "CQC", "GTG",

                    'S', SuperSolarPanels.machines.getItemStack(SSPBlock.ultimate_solar_panel),
                    'C', IC2Items.getItem("crafting", "advanced_circuit"),
                    'Q', IC2Items.getItem("quantum_helmet"),
                    'G',
                    IC2Items.getItem("cable", "type:glass,insulation:0"),
                    'T', IC2Items.getItem("te", "hv_transformer"));
            addShapelessRecipe(new ItemStack(SSP_Items.ULTIMATE_HYBRID_SOLAR_HELMET.getInstance()), SSP_Items.HYBRID_SOLAR_HELMET
                  .getInstance(), SuperSolarPanels.machines
                  .getItemStack(SSPBlock.ultimate_solar_panel));
           
          
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_URANIUM), " G ", "GUG", " G ",

                  'G', Items.GLOWSTONE_DUST,
                  'U', "ingotUranium");
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_GLASS_PANE), "GGG", "UDU", "GGG",

                  'G', IC2Items.getItem("glass", "reinforced"),
                  'U', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_URANIUM),
                  'D', Items.GLOWSTONE_DUST);
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM), "UUU", "USU", "UUU",

                  'U', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_URANIUM),
                  'S', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM));
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY), " S ", "SAS", " S ",

                  'S', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM),
                  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_ALLOY));
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_ALLOY), "III", "ISI", "III",

                  'I', IC2Items.getItem("crafting", "iridium"),
                  'S', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM));
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM), "SSS", "SSS", "SSS",

                  'S', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_PART));
      
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.enderquantumcomponent), "ABA", "BCB", "ABA", 'A', IC2Items.getItem("crafting", "iridium"), 'B', Items.ENDER_EYE, 'C', Items.NETHER_STAR );
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.spectral_solar_panel), " B ", "BAB", " B ", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.spectralcore), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.quantum_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.singular_solar_panel), " B ", "BAB", " B ", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.proton_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.admin_solar_panel), " B ", "BAB", " B ", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore2), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.singular_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.photonic_solar_panel), " B ", "BAB", " B ", 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.admin_solar_panel), 'A',SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore1));
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore), "ABA","DCD","ABA", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.enderquantumcomponent),'C', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy_ingot),'B', IC2Items.getItem("crafting", "iridium"),'D', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY) );
        addCompressorRecipe(input.forStack(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy), 9), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy_ingot));
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.spectralcore), "CBC","BAB","CBC", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QUANTUM_CORE),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy),'C',SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.solarsplitter) );
        addShapedRecipe(new ItemStack(SSP_Items.GRAVI_CHESTPLATE.getInstance()), "CBC","CAC"," H ", 'A', IC2Items.getItem("quantum_chestplate"),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6),'C' ,SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3),'H' ,SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
        addShapedRecipe(new ItemStack(SSP_Items.Quantum_leggins.getInstance()), "CBC","CAC"," H ", 'A', IC2Items.getItem("quantum_leggings"),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6),'C' ,SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3),'H' ,SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
        addShapedRecipe(new ItemStack(SSP_Items.Quantum_boosts.getInstance()), "CBC","CAC"," H ", 'A', IC2Items.getItem("quantum_boots"),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6),'C' ,SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3),'H' ,SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3), "ACA","CBC", "ACA", 'A', IC2Items.getItem("crafting", "carbon_plate"),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nanobox), 'C' ,SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems4));
        addShapedRecipe(new ItemStack(SSP_Items.Quantum_HELMET.getInstance()), "CBC","CAC"," H ", 'A', IC2Items.getItem("quantum_helmet"),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6),'C' ,SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3),'H' ,SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
        
        //
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy4),  "ABA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY), 'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.spectralcore));
        
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy3), "ABA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy4), 'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy_ingot));
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy2),  "ABA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy3), 'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore));
        addShapedRecipe(new ItemStack(SSP_Items.drill.getInstance()), "ODO", "COC",
                //IC2Items.getItem("crafting", "advanced_circuit")
                'O', IC2Items.getItem("upgrade", "overclocker"),
                'D', StackUtil.copyWithWildCard(IC2Items.getItem("diamond_drill")),
                'C', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6));
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore2),  "ABA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy3), 'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore));
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore1),  "ABA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy2), 'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore2));
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nanobox), "ACA","CBC", "ACA", 'A', IC2Items.getItem("crafting", "carbon_plate"),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_IRON_PLATE), 'C' ,IC2Items.getItem("crafting", "alloy"));
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore2),  "ABA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy3), 'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore));
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore1),  "ABA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy2), 'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore2));
        addCompressorRecipe(input.forStack(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_INGOT), 9), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems2));
        addCompressorRecipe(input.forStack(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems2), 9), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems4));
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6), " A ","ABA", " A ", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nanobox),'B', IC2Items.getItem("crafting", "advanced_circuit"));
        addCompressorRecipe(input.forStack( IC2Items.getItem("dust", "energium"), 9), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.dust));
   //     addShapedRecipe(new ItemStack(SSP_Items.spectralsaber.getInstance()), "A","AB ", "ACD", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3),'B',  Items.DIAMOND_SWORD, 'C' ,SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5),'D' ,SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.dust));
        addShapedRecipe(new ItemStack(SSP_Items.quantumsaber.getInstance()), "O  ", "OD ","OBC",

                'O', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nanobox),
                'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.dust),
                'D', StackUtil.copyWithWildCard(IC2Items.getItem("nano_saber")),
                'C', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6));
        addShapedRecipe(new ItemStack(SSP_Items.spectralsaber.getInstance()), "O  ", "OD ","OBC",

                'O', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3),
                'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.dust),
                'D', new ItemStack(SSP_Items.quantumsaber.getInstance()),
                'C', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
        //
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5), "ACA","CBC", "ACA", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6), 'C' ,SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems2));
        
        addCompressorRecipe(input.forStack(IC2Items.getItem("misc_resource", "iridium_ore")),SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_INGOT));
        addExtrudingRecipe(input.forStack(IC2Items.getItem("crafting", "iridium")), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_INGOT));
        addCompressorRecipe(input.forStack(IC2Items.getItem("resource", "uranium_ore")), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.URANIUM_INGOT));
        addCompressorRecipe(input.forStack(IC2Items.getItem("crushed", "uranium")), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.URANIUM_INGOT));
        addCompressorRecipe(input.forStack(IC2Items.getItem("purified", "uranium")), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.URANIUM_INGOT));
        addCompressorRecipe(input.forStack(IC2Items.getItem("nuclear", "uranium")), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.URANIUM_INGOT));
//
        addCompressorRecipe(input.forStack(IC2Items.getItem("crafting", "carbon_plate"), 9), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.compresscarbon));
        addCompressorRecipe(input.forStack(IC2Items.getItem("crafting", "alloy"), 9), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.compresscarbonultra));
        addExtrudingRecipe(input.forStack(IC2Items.getItem("crafting", "coal_chunk"), 9),  SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.coal_chunk));
        addCompressorRecipe(input.forStack(IC2Items.getItem("nuclear", "plutonium")), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.protonshard));
        addCompressorRecipe(input.forStack(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.protonshard), 18),  SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.proton));
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.protoncore), " B ", "ACA", " B ", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.proton), 'B',  SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.EnrichedSunnariumAlloy4), 'C',SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.spectralcore) );
      addCompressorRecipe(input.forStack(FluidUtil.getFilledBucket(new FluidStack(FluidRegister.Neutron, 1))),  SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.neutronshard));
        addCompressorRecipe(input.forStack(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.neutronshard), 9),  SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.neutron));
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.neutroncore), " A ", "ABA", " A ", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.neutron),  'B',SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore2) );
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.neutronium_solar_panel), " B ", "BAB", " B ", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.neutroncore), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.photonic_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.proton_solar_panel), " B ", "BAB", " B ", 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.spectral_solar_panel), 'A',SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.protoncore));
        
        //FluidRegister.Neutron
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.nutronfabricator),  " A ",  "BCB"," A ", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.enderquantumcomponent), 'C', IC2Items.getItem("te", "matter_generator"),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.transformator),    "BCB",  'C', IC2Items.getItem("te", "ev_transformer"),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.transformator1),    "BCB", 'C', SuperSolarPanels.machines.getItemStack(SSPBlock.transformator),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.transformator2),   "BCB", 'C', SuperSolarPanels.machines.getItemStack(SSPBlock.transformator1),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.transformator3),   "BCB",  'C', SuperSolarPanels.machines.getItemStack(SSPBlock.transformator2),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
        //
 //
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.ultimatemfsu), " B ",  "BCB"," B ", 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.advancedmfsu),'C', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.quantummfsu), " B ",  "BCB", " B ", 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.ultimatemfsu),'C', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
      

        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.advancedmfsu), " B ", "BCB", " B ",

                'B', new ItemStack(SSP_Items.ADVANCED_crystal.getInstance(),1,0),
                'C', IC2Items.getItem("te", "mfsu"));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.advancedmfsu), " B ", "BCB", " B ",

                'B', new ItemStack(SSP_Items.ADVANCED_crystal.getInstance(),1,26),
                'C', IC2Items.getItem("te", "mfsu"));
addShapedRecipe(new ItemStack(SSP_Items.ADVANCED_crystal.getInstance()), " A ", "DCD", " A ",
        'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nanobox),
        'D', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3),
        'C', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.dust));
addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.molecular_transformer), "MTM", "CcC", "MTM",

        'M', IC2Items.getItem("resource", "advanced_machine"),
        'T', IC2Items.getItem("te", "ev_transformer"),
        'C', IC2Items.getItem("crafting", "advanced_circuit"),
        'c',
        IC2Items.getItem("nuclear", "plutonium"));
 
//
 addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), "USU",

         'U', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse),
         'S', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse));
 addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.dust), "UUU", "UUU", "UUU",

         'U', IC2Items.getItem("dust", "energium"));

//
  
  addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM), "UUU", "USU", "UUU",

          'U', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_URANIUM),
          'S', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM));
  addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY), " S ", "SAS", " S ",

          'S', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM),
          'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_ALLOY));
  addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_ALLOY), "III", "ISI", "III",

          'I', IC2Items.getItem("crafting", "iridium"),
          'S', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM));
  addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM), "SSS", "SSS", "SSS",

          'S', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_PART));
       //
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.spectral_solar_panelsun),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.spectral_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.singular_solar_panelsun), "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.singular_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.admin_solar_panelsun), "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.admin_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.photonic_solar_panelsun),  "BA",  'B', SuperSolarPanels.machines.getItemStack(SSPBlock.photonic_solar_panel), 'A',SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.spectral_solar_panelmoon),  "BA", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.spectral_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.singular_solar_panelmoon),  "BA", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.singular_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.admin_solar_panelmoon),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.admin_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.photonic_solar_panelmoon),  "BA",  'B', SuperSolarPanels.machines.getItemStack(SSPBlock.photonic_solar_panel), 'A',SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.spectral_solar_panelrain), "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.spectral_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.singular_solar_panelrain),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.singular_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.admin_solar_panelrain),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.admin_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.photonic_solar_panelrain),  "BA",  'B', SuperSolarPanels.machines.getItemStack(SSPBlock.photonic_solar_panel), 'A',SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse));
  
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.proton_solar_panelsun),  "BA", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.proton_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.neutronium_solar_panelsun),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.neutronium_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.proton_solar_panelmoon), "BA", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.proton_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.neutronium_solar_panelmoon),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.neutronium_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.proton_solar_panelrain),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.proton_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.neutronium_solar_panelrain),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.neutronium_solar_panel));
       
  
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.advanced_solar_panelsun),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.advanced_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.hybrid_solar_panelsun),  "BA", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.hybrid_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.advanced_solar_panelmoon), "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.advanced_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.hybrid_solar_panelmoon),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.hybrid_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.advanced_solar_panelrain), "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.advanced_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.hybrid_solar_panelrain),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.hybrid_solar_panel));
       
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.ultimate_solar_panelsun),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.ultimate_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.quantum_solar_panelsun),  "BA", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.quantum_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.ultimate_solar_panelmoon), "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.ultimate_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.quantum_solar_panelmoon),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.quantum_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.ultimate_solar_panelrain), "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.ultimate_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack(SSPBlock.quantum_solar_panelrain),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack(SSPBlock.quantum_solar_panel));
      //
        addcanerRecipe(input.forStack(IC2Items.getItem("crafting", "fuel_rod"), 1), input1.forStack(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.proton)), new ItemStack(SSP_Items.proton_fuel_rod.getInstance()));
        addcentrifugeRecipe(input.forStack(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_proton_fuel_rod)),SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.proton),SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.protonshard));
        addcentrifugeRecipe(input.forStack(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_dual_proton_fuel_rod)),SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_proton_fuel_rod),SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_proton_fuel_rod));
        addcentrifugeRecipe(input.forStack(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_quad_proton_fuel_rod)),SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_dual_proton_fuel_rod),SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_dual_proton_fuel_rod));
        addcentrifugeRecipe(input.forStack(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_eit_proton_fuel_rod)),SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_quad_proton_fuel_rod),SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_quad_proton_fuel_rod));
        addShapedColourRecipe(new ItemStack(SSP_Items.dual_proton_fuel_rod.getInstance()), "SQS",

                'S', new ItemStack(SSP_Items.proton_fuel_rod.getInstance()),
                //  Character.valueOf('C'), IC2Items.getItem("crafting", "advanced_circuit"),
                'Q', IC2Items.getItem("plate","iron")
                // Character.valueOf('G'),
                // IC2Items.getItem("cable", "type:glass,insulation:0"),
                //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
        );
        addShapedColourRecipe(new ItemStack(SSP_Items.quad_proton_fuel_rod.getInstance()), " S ", "CQC"," S ",

                'S', new ItemStack(SSP_Items.dual_proton_fuel_rod.getInstance()),
                'C', IC2Items.getItem("plate","copper"),
                'Q', IC2Items.getItem("plate","iron")
                // Character.valueOf('G'),
                // IC2Items.getItem("cable", "type:glass,insulation:0"),
                //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
        );
        addShapedColourRecipe(new ItemStack(SSP_Items.quad_proton_fuel_rod.getInstance()), "SQS", "CQC","SQS",

                'S', new ItemStack(SSP_Items.proton_fuel_rod.getInstance()),
                'C', IC2Items.getItem("plate","copper"),
                'Q', IC2Items.getItem("plate","iron")
                // Character.valueOf('G'),
                // IC2Items.getItem("cable", "type:glass,insulation:0"),
                //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
        );
        addShapedColourRecipe(new ItemStack(SSP_Items.eit_proton_fuel_rod.getInstance()), "SQS", "CQC","SQS",

                'S', new ItemStack(SSP_Items.dual_proton_fuel_rod.getInstance()),
                'C', IC2Items.getItem("plate","copper"),
                'Q', IC2Items.getItem("plate","iron")
                // Character.valueOf('G'),
                // IC2Items.getItem("cable", "type:glass,insulation:0"),
                //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
        );
        addShapedColourRecipe(new ItemStack(SSP_Items.eit_proton_fuel_rod.getInstance()), " S ", "CQC"," S ",

                'S', new ItemStack(SSP_Items.quad_proton_fuel_rod.getInstance()),
                'C', IC2Items.getItem("plate","copper"),
                'Q', IC2Items.getItem("plate","iron")
                // Character.valueOf('G'),
                // IC2Items.getItem("cable", "type:glass,insulation:0"),
                //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
        );
        addShapedColourRecipe(new ItemStack(SSP_Items.iridium.getInstance()), //" CQ",
                "QSQ",
                //"QC ",

                'S', IC2Items.getItem("rotor_carbon"),
                //   Character.valueOf('C'), IC2Items.getItem("crafting","carbon_rotor_blade"),
                'Q', IC2Items.getItem("misc_resource","iridium_ore")
                // Character.valueOf('G'),
                // IC2Items.getItem("cable", "type:glass,insulation:0"),
                //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
        );
        addShapedColourRecipe(new ItemStack(SSP_Items.compressiridium.getInstance()), " S ",
                "CQC",
                " S ",

                'S', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.compresscarbon),
                'C', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6),
                'Q',new ItemStack(SSP_Items.iridium.getInstance())
                // Character.valueOf('G'),
                // IC2Items.getItem("cable", "type:glass,insulation:0"),
                //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
        );
        addShapedColourRecipe(new ItemStack(SSP_Items.spectral.getInstance()), " S ",
                "CQC",
                " S ",

                'S', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems2),
                'C', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.compresscarbonultra),
                'Q',new ItemStack(SSP_Items.compressiridium.getInstance())
                // Character.valueOf('G'),
                // IC2Items.getItem("cable", "type:glass,insulation:0"),
                //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
        );
        addShapedColourRecipe(new ItemStack(SSP_Items.twelve_heat_storage.getInstance()), "TCT",
                "TGT",
                "TCT",

                //   Character.valueOf('S'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems2),
                'C', IC2Items.getItem("hex_heat_storage"),
                //   Character.valueOf('Q'),new ItemStack(SSP_Items.compressiridium.getInstance()),
                'G',
                IC2Items.getItem("plate", "iron"),
                'T', IC2Items.getItem("plate", "tin"));
        addShapedColourRecipe(new ItemStack(SSP_Items.max_heat_storage.getInstance()), "TCT",
                "TGT",
                "TCT",

                //   Character.valueOf('S'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems2),
                'C', new ItemStack(SSP_Items.twelve_heat_storage.getInstance()),
                //   Character.valueOf('Q'),new ItemStack(SSP_Items.compressiridium.getInstance()),
                'G',
                IC2Items.getItem("plate", "iron"),
                'T', IC2Items.getItem("plate", "tin"));
    }
    //addRecipe(IRecipeInput paramIRecipeInput1, IRecipeInput paramIRecipeInput2, ItemStack paramItemStack);
    private static void addcanerRecipe(IRecipeInput input,IRecipeInput input1, ItemStack output) {
        ic2.api.recipe.Recipes.cannerBottle.addRecipe(input, input1,  output , false);
      }
    private static void addcentrifugeRecipe(IRecipeInput input, ItemStack output, ItemStack itemStack) {
        ic2.api.recipe.Recipes.centrifuge.addRecipe(input, null,false, output, itemStack) ;
      }
    private static void addExtrudingRecipe(IRecipeInput input, ItemStack output) {
        ic2.api.recipe.Recipes.metalformerExtruding.addRecipe(input, null, false, output);
      }
    private static void addShapedColourRecipe(final ItemStack output, final Object... inputs) {
        ColourCarryingRecipe.addAndRegister(output, inputs);
    }
    private static void addCompressorRecipe(final IRecipeInput input, final ItemStack output) {
        ic2.api.recipe.Recipes.compressor.addRecipe(input, null, false, output);
    }
  
    
    public static void addShapedRecipe(final ItemStack output, final Object... inputs) {
        Recipes.advRecipes.addRecipe(output, inputs);
    }
    public static void addShapelessRecipe(final ItemStack output, final Object... inputs) {
        Recipes.advRecipes.addShapelessRecipe(output, inputs);
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
