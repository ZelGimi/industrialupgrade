// 
// Decompiled by Procyon v0.5.36
// 

package com.Denfop.ssp;

import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraft.init.Items;
import ic2.core.util.ReflectionUtil;
import net.minecraft.client.renderer.color.ItemColors;
import java.util.Map;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraftforge.client.event.ColorHandlerEvent;

import com.Denfop.ssp.items.CraftingThings;
import com.Denfop.ssp.tiles.SSPBlock;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import ic2.core.init.Localization;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.entity.player.EntityPlayer;
import ic2.core.item.armor.jetpack.JetpackAttachmentRecipe;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import ic2.core.block.ITeBlock;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
@Mod(modid = "super_solar_panels", name = "Super Solar Panels", dependencies = "required-after:advanced_solar_panels@[4.3.0,);", version = "1.0.0", acceptedMinecraftVersions = "[1.12,1.12.2]")
public final class SuperSolarPanels
{
    public static final String MODID = "super_solar_panels";
	private static final String Urane = null;
    public static Logger log;
    public static BlockTileEntity machines;
    
    @SubscribeEvent
    public static void register(final TeBlockFinalCallEvent event) {
        TeBlockRegistry.addAll((Class)SSPBlock.class, SSPBlock.IDENTITY);
        TeBlockRegistry.setDefaultMaterial(SSPBlock.IDENTITY, Material.ROCK);
    }
    
    @Mod.EventHandler
    public void load(final FMLPreInitializationEvent event) {
        SuperSolarPanels.log = event.getModLog();
        Configs1.loadConfig(event.getSuggestedConfigurationFile(), event.getSide().isClient());
        SuperSolarPanels.machines = TeBlockRegistry.get(SSPBlock.IDENTITY);
        SSP_Items.buildItems(event.getSide());
        SSPKeys.addFlyKey();
        OreDictionary.registerOre("enderquantumcomponent", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.enderquantumcomponent)); 
    OreDictionary.registerOre("solarsplitter", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.solarsplitter)); 
OreDictionary.registerOre("bluecomponent", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.bluecomponent)); 
OreDictionary.registerOre("greencomponent", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.greencomponent)); 
OreDictionary.registerOre("redcomponent", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.redcomponent)); 
    OreDictionary.registerOre("singularcore", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.singularcore)); 
OreDictionary.registerOre("photoniy", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy)); 
OreDictionary.registerOre("photoniy_ingot", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.photoniy_ingot)); 
OreDictionary.registerOre("spectralcore", SSP_Items.CRAFTING.getItemStack(CraftingThings.CraftingTypes.spectralcore)); }
   
    private void registerJetpackBlacklist() {
        JetpackAttachmentRecipe.blacklistedItems.add(SSP_Items.Quantum_chestplate.getInstance());
        JetpackAttachmentRecipe.blacklistedItems.add(SSP_Items.Quantum_leggins.getInstance());
        JetpackAttachmentRecipe.blacklistedItems.add(SSP_Items.Quantum_boosts.getInstance());
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        SSPBlock.buildDummies();
        SPPRecipes.addCraftingRecipes();
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void doColourThings(final ColorHandlerEvent.Item event) {
        final ItemColors colours = event.getItemColors();
        final IItemColor armourColouring = (IItemColor) ((Map)ReflectionUtil.getFieldValue(ReflectionUtil.getField((Class)ItemColors.class, (Class)Map.class), (Object)colours)).get(Items.LEATHER_BOOTS.delegate);
        colours.registerItemColorHandler(armourColouring, new Item[] { SSP_Items.Spectral_SOLAR_HELMET.getInstance() });
        colours.registerItemColorHandler(armourColouring, new Item[] { SSP_Items.Singular_SOLAR_HELMET.getInstance() });
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
    }

    public static ResourceLocation getIdentifier(final String name) {
        return new ResourceLocation("supersolarpanels", name);
    }

   
    
}
