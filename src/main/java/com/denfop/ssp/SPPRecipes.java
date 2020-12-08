package com.denfop.ssp;

import java.util.List;
import net.minecraftforge.oredict.OreDictionary;
import ic2.core.util.ConfigUtil;
import java.text.ParseException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.Recipes;
import net.minecraftforge.fml.common.ModContainer;
import ic2.core.init.Rezepte;
import java.util.function.Consumer;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Loader;
import ic2.core.recipe.ColourCarryingRecipe;
import net.minecraft.block.Block;
import ic2.core.util.StackUtil;
import net.minecraft.init.Blocks;
import ic2.core.block.BlockTexGlass.GlassType;
import ic2.core.block.ITeBlock;

import com.denfop.ssp.fluid.Neutron.BlockRegister;
import com.denfop.ssp.fluid.Neutron.FluidNeutron;
import com.denfop.ssp.fluid.Neutron.FluidRegister;
import com.denfop.ssp.items.SSP_Items;
import com.denfop.ssp.items.resource.CraftingThings;
import com.denfop.ssp.items.resource.CraftingThings;
import com.denfop.ssp.tiles.SSPBlock;

import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import ic2.api.item.IC2Items;
import net.minecraft.item.crafting.IRecipe;
import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.Item;
import ic2.core.recipe.ArmorDyeingRecipe;

final class SPPRecipes
{
    static void addCraftingRecipes() {
    	final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
    	final IRecipeInputFactory input1 = ic2.api.recipe.Recipes.inputFactory;
    	addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.redcomponent), "AAA", "BBB", "AAA" ,'A', IC2Items.getItem("glass", "reinforced"), 'B', Items.REDSTONE);
        
    	addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.bluecomponent), "AAA", "BBB", "AAA", 'A', IC2Items.getItem("glass", "reinforced"), 'B', new ItemStack(Items.DYE, 1, 4));
         addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.solarsplitter), "ABC", "ABC", "ABC", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.redcomponent), 'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.greencomponent), 'C', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.bluecomponent));
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.greencomponent), "A  ", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_GLASS_PANE));
        addShapelessRecipe(new ItemStack(SSP_Items.Spectral_SOLAR_HELMET.getInstance()), SSP_Items.ULTIMATE_HYBRID_SOLAR_HELMET.getInstance(), SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.spectral_solar_panel) , SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.spectralcore));
        addShapelessRecipe(new ItemStack(SSP_Items.Singular_SOLAR_HELMET.getInstance()), SSP_Items.Spectral_SOLAR_HELMET.getInstance() , SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore));
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_IRON_PLATE), new Object[] { "III", "IPI", "III", 
                
                Character.valueOf('I'), "plateIron", 
                Character.valueOf('P'), "ingotIridium" });
          //
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ultimate_core), new Object[] {  "IPI", 
                  
          		  Character.valueOf('I'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY), 
                    Character.valueOf('P'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_ALLOY) });
          //
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.advanced_core), new Object[] {  "IPI", 
                  
                  Character.valueOf('I'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM), 
                  Character.valueOf('P'),SSP_Items.CRAFTING.getItemStack( CraftingThings.CraftingTypes.URANIUM_INGOT) });
          //
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.hybrid_core), new Object[] {  "IPI", 
                  
                  Character.valueOf('I'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM) , 
                  Character.valueOf('P'),  SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_ALLOY )});
          //
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.REINFORCED_IRIDIUM_IRON_PLATE), new Object[] { "ACA", "CIC", "ACA", 
                
                Character.valueOf('A'), IC2Items.getItem("crafting", "alloy"), 
                Character.valueOf('C'), IC2Items.getItem("crafting", "carbon_plate"), 
                Character.valueOf('I'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRIDIUM_IRON_PLATE) });
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_REINFORCED_PLATE), new Object[] { 
                "RSR", "LIL", "RDR", 
                
                Character.valueOf('R'), Items.REDSTONE, 
                Character.valueOf('S'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_PART), 
                Character.valueOf('L'), new ItemStack(Items.DYE, 1, 4), 
                Character.valueOf('I'), 
                SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.REINFORCED_IRIDIUM_IRON_PLATE), 
                Character.valueOf('D'), Items.DIAMOND });
        
                
                addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.advanced_solar_panel), new Object[] { 
                      "PDP", "ASA", "CIC", 
                      
                      Character.valueOf('P'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_GLASS_PANE), 
                      Character.valueOf('A'), IC2Items.getItem("crafting", "alloy"), 
                      Character.valueOf('S'), IC2Items.getItem("te", "solar_generator"), 
                      Character.valueOf('C'), 
                      IC2Items.getItem("crafting", "advanced_circuit"), 
                      Character.valueOf('D'), 
                      SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.advanced_core), 
                      
                      Character.valueOf('I'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_REINFORCED_PLATE) });
               
         
              addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.hybrid_solar_panel), new Object[] { 
                    "PLP", "IAI", "CSC", 
                    
                    Character.valueOf('P'), IC2Items.getItem("crafting", "carbon_plate"), 
                    Character.valueOf('L'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.hybrid_core), 
                    Character.valueOf('I'), IC2Items.getItem("crafting", "iridium"), 
                    Character.valueOf('A'), 
                    SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.advanced_solar_panel), 
                    Character.valueOf('C'), IC2Items.getItem("crafting", "advanced_circuit"), 
                    Character.valueOf('S'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM) }); 
            
           
           
             
        
            addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.ultimate_solar_panel), new Object[] { " S ", "SCS", " S ", 
                  
                  Character.valueOf('S'), SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.hybrid_solar_panel), 
                  Character.valueOf('C'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ultimate_core) });
            addShapelessRecipe(StackUtil.setSize(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.hybrid_solar_panel), 4), new Object[] { SuperSolarPanels.machines
                  .getItemStack((ITeBlock)SSPBlock.ultimate_solar_panel) });
         
          
            addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QUANTUM_CORE), new Object[] { "ANA", "NEN", "ANA", 
                  
                  Character.valueOf('A'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY), 
                  Character.valueOf('N'), Items.NETHER_STAR, 
                  Character.valueOf('E'), Items.ENDER_EYE });
            addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.quantum_solar_panel), new Object[] { " S ", "SQS", " S ", 
                  
                  Character.valueOf('S'), SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.ultimate_solar_panel), 
                  Character.valueOf('Q'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QUANTUM_CORE) });
          
          
            addShapedRecipe(new ItemStack(SSP_Items.ADVANCED_SOLAR_HELMET.getInstance()), new Object[] { 
                  " S ", "CNC", "GTG", 
                  
                  Character.valueOf('S'), SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.advanced_solar_panel), 
                  Character.valueOf('C'), IC2Items.getItem("crafting", "advanced_circuit"), 
                  Character.valueOf('N'), IC2Items.getItem("nano_helmet"), 
                  Character.valueOf('G'), 
                  IC2Items.getItem("cable", "type:gold,insulation:2"), 
                  Character.valueOf('T'), IC2Items.getItem("te", "lv_transformer") }); 
          
            addShapedColourRecipe(new ItemStack(SSP_Items.HYBRID_SOLAR_HELMET.getInstance()), new Object[] { 
                  " S ", "CQC", "GTG", 
                  
                  Character.valueOf('S'), SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.hybrid_solar_panel), 
                  Character.valueOf('C'), IC2Items.getItem("crafting", "advanced_circuit"), 
                  Character.valueOf('Q'), IC2Items.getItem("quantum_helmet"), 
                  Character.valueOf('G'), 
                  IC2Items.getItem("cable", "type:glass,insulation:0"), 
                  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer") }); 
         
            addShapedColourRecipe(new ItemStack(SSP_Items.ULTIMATE_HYBRID_SOLAR_HELMET.getInstance()), new Object[] { 
                  " S ", "CQC", "GTG", 
                  
                  Character.valueOf('S'), SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.ultimate_solar_panel), 
                  Character.valueOf('C'), IC2Items.getItem("crafting", "advanced_circuit"), 
                  Character.valueOf('Q'), IC2Items.getItem("quantum_helmet"), 
                  Character.valueOf('G'), 
                  IC2Items.getItem("cable", "type:glass,insulation:0"), 
                  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer") });
            addShapelessRecipe(new ItemStack(SSP_Items.ULTIMATE_HYBRID_SOLAR_HELMET.getInstance()), new Object[] { SSP_Items.HYBRID_SOLAR_HELMET
                  .getInstance(), SuperSolarPanels.machines
                  .getItemStack((ITeBlock)SSPBlock.ultimate_solar_panel) });
           
          
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_URANIUM), new Object[] { " G ", "GUG", " G ", 
                
                Character.valueOf('G'), Items.GLOWSTONE_DUST, 
                Character.valueOf('U'), "ingotUranium" });
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_GLASS_PANE), new Object[] { "GGG", "UDU", "GGG", 
                
                Character.valueOf('G'), IC2Items.getItem("glass", "reinforced"), 
                Character.valueOf('U'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_URANIUM), 
                Character.valueOf('D'), Items.GLOWSTONE_DUST });
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM), new Object[] { "UUU", "USU", "UUU", 
                
                Character.valueOf('U'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_URANIUM), 
                Character.valueOf('S'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM) });
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY), new Object[] { " S ", "SAS", " S ", 
                
                Character.valueOf('S'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM), 
                Character.valueOf('A'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_ALLOY) });
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_ALLOY), new Object[] { "III", "ISI", "III", 
                
                Character.valueOf('I'), IC2Items.getItem("crafting", "iridium"), 
                Character.valueOf('S'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM) });
          addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM), new Object[] { "SSS", "SSS", "SSS", 
                
                Character.valueOf('S'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_PART) });
      
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.enderquantumcomponent), "ABA", "BCB", "ABA", 'A', IC2Items.getItem("crafting", "iridium"), 'B', Items.ENDER_EYE, 'C', Items.NETHER_STAR );
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.spectral_solar_panel), " B ", "BAB", " B ", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.spectralcore), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.quantum_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.singular_solar_panel), " B ", "BAB", " B ", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.proton_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.admin_solar_panel), " B ", "BAB", " B ", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore2), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.singular_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.photonic_solar_panel), " B ", "BAB", " B ", 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.admin_solar_panel), 'A',SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantcore1));
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
        addShapedRecipe(new ItemStack(SSP_Items.drill.getInstance()), new Object[] { "ODO", "COC", 
      //IC2Items.getItem("crafting", "advanced_circuit")          
                Character.valueOf('O'), IC2Items.getItem("upgrade", "overclocker"), 
                Character.valueOf('D'), StackUtil.copyWithWildCard(IC2Items.getItem("diamond_drill")), 
                Character.valueOf('C'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6)}); 
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
        addShapedRecipe(new ItemStack(SSP_Items.quantumsaber.getInstance()), new Object[] { "O  ", "OD ","OBC" ,
               
               Character.valueOf('O'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nanobox), 
               Character.valueOf('B'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.dust),
               Character.valueOf('D'), StackUtil.copyWithWildCard(IC2Items.getItem("nano_saber")), 
               Character.valueOf('C'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6) }); 
        addShapedRecipe(new ItemStack(SSP_Items.spectralsaber.getInstance()), new Object[] { "O  ", "OD ","OBC" ,
                
                Character.valueOf('O'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3), 
                Character.valueOf('B'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.dust),
                Character.valueOf('D'), new ItemStack(SSP_Items.quantumsaber.getInstance()), 
                Character.valueOf('C'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5) }); 
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
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.neutronium_solar_panel), " B ", "BAB", " B ", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.neutroncore), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.photonic_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.proton_solar_panel), " B ", "BAB", " B ", 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.spectral_solar_panel), 'A',SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.protoncore));
        
        //FluidRegister.Neutron
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.nutronfabricator),  " A ",  "BCB"," A ", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.enderquantumcomponent), 'C', IC2Items.getItem("te", "matter_generator"),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.transformator),    "BCB",  'C', IC2Items.getItem("te", "ev_transformer"),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.transformator1),    "BCB", 'C', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.transformator),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.transformator2),   "BCB", 'C', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.transformator1),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.transformator3),   "BCB",  'C', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.transformator2),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
        //
 //
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.ultimatemfsu), " B ",  "BCB"," B ", 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.advancedmfsu),'C', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.quantummfsu), " B ",  "BCB", " B ", 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.ultimatemfsu),'C', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems5));
      

        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.advancedmfsu), new Object[] { " B ",   "BCB", " B ",
        		
                Character.valueOf('B'), new ItemStack(SSP_Items.ADVANCED_crystal.getInstance(),1,0), 
                Character.valueOf('C'), IC2Items.getItem("te", "mfsu") }); 
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.advancedmfsu), new Object[] { " B ",   "BCB", " B ",
        		
                Character.valueOf('B'), new ItemStack(SSP_Items.ADVANCED_crystal.getInstance(),1,26), 
                Character.valueOf('C'), IC2Items.getItem("te", "mfsu") }); 
addShapedRecipe(new ItemStack(SSP_Items.ADVANCED_crystal.getInstance()), new Object[] { " A ",   "DCD", " A ",
		Character.valueOf('A'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nanobox), 
                Character.valueOf('D'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems3), 
              Character.valueOf('C'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.dust) }); 
addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.molecular_transformer), new Object[] { 
        "MTM", "CcC", "MTM", 
        
        Character.valueOf('M'), IC2Items.getItem("resource", "advanced_machine"), 
        Character.valueOf('T'), IC2Items.getItem("te", "ev_transformer"), 
        Character.valueOf('C'), IC2Items.getItem("crafting", "advanced_circuit"), 
        Character.valueOf('c'), 
        IC2Items.getItem("nuclear", "plutonium") });
 
//
 addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), new Object[] {  "USU",  
        
        Character.valueOf('U'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 
        Character.valueOf('S'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse) });
 addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.dust), new Object[] {  "UUU",  "UUU", "UUU", 
	        
	        Character.valueOf('U'), IC2Items.getItem("dust", "energium") });

//
  
  addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM), new Object[] { "UUU", "USU", "UUU", 
        
        Character.valueOf('U'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.IRRADIANT_URANIUM), 
        Character.valueOf('S'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM) });
  addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY), new Object[] { " S ", "SAS", " S ", 
        
        Character.valueOf('S'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.ENRICHED_SUNNARIUM), 
        Character.valueOf('A'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_ALLOY) });
  addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_ALLOY), new Object[] { "III", "ISI", "III", 
        
        Character.valueOf('I'), IC2Items.getItem("crafting", "iridium"), 
        Character.valueOf('S'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM) });
  addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM), new Object[] { "SSS", "SSS", "SSS", 
        
        Character.valueOf('S'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.SUNNARIUM_PART) });
       //
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.spectral_solar_panelsun),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.spectral_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.singular_solar_panelsun), "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.singular_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.admin_solar_panelsun), "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.admin_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.photonic_solar_panelsun),  "BA",  'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.photonic_solar_panel), 'A',SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.spectral_solar_panelmoon),  "BA", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.spectral_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.singular_solar_panelmoon),  "BA", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.singular_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.admin_solar_panelmoon),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.admin_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.photonic_solar_panelmoon),  "BA",  'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.photonic_solar_panel), 'A',SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.spectral_solar_panelrain), "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.spectral_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.singular_solar_panelrain),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.singular_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.admin_solar_panelrain),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.admin_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.photonic_solar_panelrain),  "BA",  'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.photonic_solar_panel), 'A',SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse));
  
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.proton_solar_panelsun),  "BA", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.proton_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.neutronium_solar_panelsun),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.neutronium_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.proton_solar_panelmoon), "BA", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.proton_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.neutronium_solar_panelmoon),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.neutronium_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.proton_solar_panelrain),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.proton_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.neutronium_solar_panelrain),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.neutronium_solar_panel));
       
  
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.advanced_solar_panelsun),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.advanced_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.hybrid_solar_panelsun),  "BA", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.hybrid_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.advanced_solar_panelmoon), "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.advanced_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.hybrid_solar_panelmoon),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.hybrid_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.advanced_solar_panelrain), "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.advanced_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.hybrid_solar_panelrain),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.hybrid_solar_panel));
       
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.ultimate_solar_panelsun),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.ultimate_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.quantum_solar_panelsun),  "BA", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.sunlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.quantum_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.ultimate_solar_panelmoon), "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.ultimate_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.quantum_solar_panelmoon),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.nightlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.quantum_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.ultimate_solar_panelrain), "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.ultimate_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.quantum_solar_panelrain),  "BA",  'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.rainlinse), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.quantum_solar_panel));
      //
        addcanerRecipe(input.forStack(IC2Items.getItem("crafting", "fuel_rod"), 1), input1.forStack(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.proton)), new ItemStack(SSP_Items.proton_fuel_rod.getInstance()));
        addcentrifugeRecipe(input.forStack(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_proton_fuel_rod)),SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.proton),SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.protonshard));
        addcentrifugeRecipe(input.forStack(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_dual_proton_fuel_rod)),SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_proton_fuel_rod),SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_proton_fuel_rod));
        addcentrifugeRecipe(input.forStack(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_quad_proton_fuel_rod)),SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_dual_proton_fuel_rod),SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_dual_proton_fuel_rod));
        addcentrifugeRecipe(input.forStack(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_eit_proton_fuel_rod)),SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_quad_proton_fuel_rod),SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_quad_proton_fuel_rod));
        addShapedColourRecipe(new ItemStack(SSP_Items.dual_proton_fuel_rod.getInstance()), new Object[] { 
                 "SQS",
                
                Character.valueOf('S'), new ItemStack(SSP_Items.proton_fuel_rod.getInstance()), 
              //  Character.valueOf('C'), IC2Items.getItem("crafting", "advanced_circuit"), 
                Character.valueOf('Q'), IC2Items.getItem("plate","iron"), 
               // Character.valueOf('G'), 
               // IC2Items.getItem("cable", "type:glass,insulation:0"), 
              //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
                });
        addShapedColourRecipe(new ItemStack(SSP_Items.quad_proton_fuel_rod.getInstance()), new Object[] { 
                " S ", "CQC"," S ",
               
               Character.valueOf('S'), new ItemStack(SSP_Items.dual_proton_fuel_rod.getInstance()), 
             Character.valueOf('C'), IC2Items.getItem("plate","copper"), 
               Character.valueOf('Q'), IC2Items.getItem("plate","iron"), 
              // Character.valueOf('G'), 
              // IC2Items.getItem("cable", "type:glass,insulation:0"), 
             //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
               });
        addShapedColourRecipe(new ItemStack(SSP_Items.quad_proton_fuel_rod.getInstance()), new Object[] { 
                "SQS", "CQC","SQS",
               
               Character.valueOf('S'), new ItemStack(SSP_Items.proton_fuel_rod.getInstance()), 
             Character.valueOf('C'), IC2Items.getItem("plate","copper"), 
               Character.valueOf('Q'), IC2Items.getItem("plate","iron"), 
              // Character.valueOf('G'), 
              // IC2Items.getItem("cable", "type:glass,insulation:0"), 
             //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
               });
        addShapedColourRecipe(new ItemStack(SSP_Items.eit_proton_fuel_rod.getInstance()), new Object[] { 
                "SQS", "CQC","SQS",
               
               Character.valueOf('S'), new ItemStack(SSP_Items.dual_proton_fuel_rod.getInstance()), 
             Character.valueOf('C'), IC2Items.getItem("plate","copper"), 
               Character.valueOf('Q'), IC2Items.getItem("plate","iron"), 
              // Character.valueOf('G'), 
              // IC2Items.getItem("cable", "type:glass,insulation:0"), 
             //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
               });
        addShapedColourRecipe(new ItemStack(SSP_Items.eit_proton_fuel_rod.getInstance()), new Object[] { 
                " S ", "CQC"," S ",
               
               Character.valueOf('S'), new ItemStack(SSP_Items.quad_proton_fuel_rod.getInstance()), 
             Character.valueOf('C'), IC2Items.getItem("plate","copper"), 
               Character.valueOf('Q'), IC2Items.getItem("plate","iron"), 
              // Character.valueOf('G'), 
              // IC2Items.getItem("cable", "type:glass,insulation:0"), 
             //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
               });
        addShapedColourRecipe(new ItemStack(SSP_Items.iridium.getInstance()), new Object[] { 
                //" CQ",
                "QSQ",
                //"QC ",
               
              Character.valueOf('S'), IC2Items.getItem("rotor_carbon"), 
          //   Character.valueOf('C'), IC2Items.getItem("crafting","carbon_rotor_blade"), 
               Character.valueOf('Q'), IC2Items.getItem("misc_resource","iridium_ore"), 
              // Character.valueOf('G'), 
              // IC2Items.getItem("cable", "type:glass,insulation:0"), 
             //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
               });
        addShapedColourRecipe(new ItemStack(SSP_Items.compressiridium.getInstance()), new Object[] { 
                  " S ",
                  "CQC",
                  " S ",
                 
                 Character.valueOf('S'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.compresscarbon), 
               Character.valueOf('C'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.quantumitems6),
                 Character.valueOf('Q'),new ItemStack(SSP_Items.iridium.getInstance()), 
                // Character.valueOf('G'), 
                // IC2Items.getItem("cable", "type:glass,insulation:0"), 
               //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
                 });
        addShapedColourRecipe(new ItemStack(SSP_Items.spectral.getInstance()), new Object[] { 
                " S ",
                "CQC",
                " S ",
               
               Character.valueOf('S'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems2), 
             Character.valueOf('C'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.compresscarbonultra),
               Character.valueOf('Q'),new ItemStack(SSP_Items.compressiridium.getInstance()), 
              // Character.valueOf('G'), 
              // IC2Items.getItem("cable", "type:glass,insulation:0"), 
             //  Character.valueOf('T'), IC2Items.getItem("te", "hv_transformer")
               });
        addShapedColourRecipe(new ItemStack(SSP_Items.twelve_heat_storage.getInstance()), new Object[] { 
                "TCT",
                "TGT",
                "TCT",
               
            //   Character.valueOf('S'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems2), 
             Character.valueOf('C'), IC2Items.getItem("hex_heat_storage"),
            //   Character.valueOf('Q'),new ItemStack(SSP_Items.compressiridium.getInstance()), 
              Character.valueOf('G'), 
               IC2Items.getItem("plate", "iron"), 
               Character.valueOf('T'), IC2Items.getItem("plate", "tin")
               });
        addShapedColourRecipe(new ItemStack(SSP_Items.max_heat_storage.getInstance()), new Object[] { 
                "TCT",
                "TGT",
                "TCT",
               
            //   Character.valueOf('S'), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.QuantumItems2), 
             Character.valueOf('C'), new ItemStack(SSP_Items.twelve_heat_storage.getInstance()),
            //   Character.valueOf('Q'),new ItemStack(SSP_Items.compressiridium.getInstance()), 
              Character.valueOf('G'), 
               IC2Items.getItem("plate", "iron"), 
               Character.valueOf('T'), IC2Items.getItem("plate", "tin")
               });
    }
    //addRecipe(IRecipeInput paramIRecipeInput1, IRecipeInput paramIRecipeInput2, ItemStack paramItemStack);
    private static void addcanerRecipe(IRecipeInput input,IRecipeInput input1, ItemStack output) {
        ic2.api.recipe.Recipes.cannerBottle.addRecipe(input, input1,  output , false);
      }
    private static void addcentrifugeRecipe(IRecipeInput input, ItemStack output, ItemStack itemStack) {
        ic2.api.recipe.Recipes.centrifuge.addRecipe(input, null,false,new ItemStack[] { output , itemStack}) ;
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
        SuperSolarPanels.log.info("Load complete, successfully loaded {} out of {}.", Integer.valueOf(successes), Integer.valueOf(Configs.MTRecipes.length));
      }
      
      private static boolean decodeLine(int number, String[] parts) throws ParseException {
        int energy;
        IRecipeInput input = ConfigUtil.asRecipeInputWithAmount(parts[0].trim());
        if (input == null) {
        	SuperSolarPanels.log.warn("Skipping line {} as the input ({}) cannot be resolved", Integer.valueOf(number), parts[0].trim());
          return false;
        } 
        ItemStack output = ConfigUtil.asStackWithAmount(parts[1].trim());
        if (output == null) {
          String attempt = parts[1].trim();
          if (attempt.startsWith("OreDict:")) {
            NonNullList<ItemStack> nonNullList = OreDictionary.getOres(attempt.substring(attempt.indexOf(':') + 1).trim());
            if (!nonNullList.isEmpty()) {
              output = nonNullList.get(0);
              SuperSolarPanels.log.debug("Continued on line {} as the output ({}) could be resolved to {}", Integer.valueOf(number), attempt, output);
            } 
          } 
          if (output == null) {
        	  SuperSolarPanels.log.warn("Skipping line {} as the output ({}) cannot be resolved", Integer.valueOf(number), attempt);
            return false;
          } 
        } 
        try {
          energy = Integer.parseInt(parts[2].trim());
        } catch (NumberFormatException e) {
        	SuperSolarPanels.log.warn("Skipping line {} as the energy ({}) cannot be resolved to a number", Integer.valueOf(number), parts[2].trim());
          return false;
        } 
        if (!IMolecularTransformerRecipeManager.RECIPES.addRecipe(input, energy, output, false)) {
        	SuperSolarPanels.log.warn("Skipping line {} as the recipe is a duplicate", Integer.valueOf(number));
          return false;
        } 
        return true;
      }
}
