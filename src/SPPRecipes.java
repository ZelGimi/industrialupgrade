// 
// Decompiled by Procyon v0.5.36
// 

package com.Denfop.ssp;

import java.util.List;
import net.minecraftforge.oredict.OreDictionary;
import ic2.core.util.ConfigUtil;
import java.text.ParseException;
import net.minecraft.nbt.NBTTagCompound;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.Recipes;
import net.minecraftforge.fml.common.ModContainer;
import ic2.core.init.Rezepte;
import java.util.function.Consumer;
import net.minecraftforge.fml.common.Loader;
import ic2.core.recipe.ColourCarryingRecipe;
import net.minecraft.block.Block;
import ic2.core.util.StackUtil;
import net.minecraft.init.Blocks;
import ic2.core.block.BlockTexGlass.GlassType;
import ic2.core.block.ITeBlock;
import com.Denfop.ssp.items.CraftingThings;
import com.Denfop.ssp.tiles.SSPBlock;
import com.chocohead.advsolar.ASP_Items;
import com.chocohead.advsolar.AdvancedSolarPanels;
import com.chocohead.advsolar.IMolecularTransformerRecipeManager;
import com.chocohead.advsolar.items.ItemCraftingThings;
import com.chocohead.advsolar.tiles.TEs;
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
    	addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.redcomponent), "AAA", "BBB", "AAA" ,'A', IC2Items.getItem("glass", "reinforced"), 'B', Items.REDSTONE);
        
    	addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.bluecomponent), "AAA", "BBB", "AAA", 'A', IC2Items.getItem("glass", "reinforced"), 'B', new ItemStack(Items.DYE, 1, 4));
         addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.solarsplitter), "ABC", "ABC", "ABC", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.redcomponent), 'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.greencomponent), 'C', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.bluecomponent));
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.greencomponent), "A  ", 'A', ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.IRRADIANT_GLASS_PANE));
        addShapelessRecipe(new ItemStack(SSP_Items.Spectral_SOLAR_HELMET.getInstance()), ASP_Items.ULTIMATE_HYBRID_SOLAR_HELMET.getInstance(), SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.spectral_solar_panel) , SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.spectralcore));
        addShapelessRecipe(new ItemStack(SSP_Items.Singular_SOLAR_HELMET.getInstance()), SSP_Items.Spectral_SOLAR_HELMET.getInstance() , SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore));
        
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.enderquantumcomponent), "ABA", "BCB", "ABA", 'A', IC2Items.getItem("crafting", "iridium"), 'B', Items.ENDER_EYE, 'C', Items.NETHER_STAR );
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.spectral_solar_panel), "BBB", "BAB", "BBB", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.solarsplitter), 'B', AdvancedSolarPanels.machines.getItemStack((ITeBlock)TEs.quantum_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.singular_solar_panel), "BBB", "BAB", "BBB", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore), 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.spectral_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.admin_solar_panel), "BBB", "BBB", "BBB", 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.singular_solar_panel));
        addShapedRecipe(SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.photonic_solar_panel), "BBB", "BBB", "BBB", 'B', SuperSolarPanels.machines.getItemStack((ITeBlock)SSPBlock.admin_solar_panel));
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore), "ABA","DCD","ABA", 'A', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.enderquantumcomponent),'C', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy_ingot),'B', IC2Items.getItem("crafting", "iridium"),'D', ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.ENRICHED_SUNNARIUM_ALLOY) );
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy), " B ","BAB"," B ", 'A', ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.QUANTUM_CORE),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.enderquantumcomponent));
        addCompressorRecipe(input.forStack(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy), 9), SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy_ingot));
        addShapedRecipe(SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.spectralcore), " B ","BAB"," B ", 'A', ASP_Items.CRAFTING.getItemStack(ItemCraftingThings.CraftingTypes.QUANTUM_CORE),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy));
        addShapedRecipe(new ItemStack(SSP_Items.Quantum_chestplate.getInstance()), " B ","BAB"," B ", 'A', IC2Items.getItem("quantum_chestplate"),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy));
        addShapedRecipe(new ItemStack(SSP_Items.Quantum_leggins.getInstance()), " B ","BAB"," B ", 'A', IC2Items.getItem("quantum_leggings"),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy));
        addShapedRecipe(new ItemStack(SSP_Items.Quantum_boosts.getInstance()), " B ","BAB"," B ", 'A', IC2Items.getItem("quantum_boots"),'B', SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy));
        
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
