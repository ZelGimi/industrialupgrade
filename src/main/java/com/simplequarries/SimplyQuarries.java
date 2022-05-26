package com.simplequarries;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Ic2Items;
import ic2.api.event.TeBlockFinalCallEvent;
import ic2.api.recipe.Recipes;
import ic2.core.block.ITeBlock;
import ic2.core.block.TeBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings({"ALL", "UnnecessaryFullyQualifiedName"})
@Mod.EventBusSubscriber
@Mod(modid = SQConstants.MOD_ID, name = SQConstants.MOD_NAME, dependencies = SQConstants.MOD_DEPS, version = SQConstants.MOD_VERSION, acceptedMinecraftVersions = "[1.12,1.12.2]", certificateFingerprint = SQConstants.MOD_CERTIFICATE)
public final class SimplyQuarries {


    public Block quarry;

    public static <E extends Enum<E> & ITeBlock> void register(Class<E> enumClass, ResourceLocation ref) {
        TeBlockRegistry.addAll(enumClass, ref);
        TeBlockRegistry.setDefaultMaterial(ref, Material.ROCK);
        TeBlockRegistry.addCreativeRegisterer((list, block, itemblock, tab) -> {
            if (tab == CreativeTabs.SEARCH || tab == IUCore.SSPTab) {
                block.getAllTypes().forEach(type -> {
                    if (type.hasItem()) {
                        list.add(block.getItemStack(type));
                    }
                });
            }
        }, ref);
    }

    @SubscribeEvent
    public static void register(final TeBlockFinalCallEvent event) {

         register(BlockQuarry.class, BlockQuarry.IDENTITY);


    }


    public static ResourceLocation getIdentifier(final String name) {
        return new ResourceLocation(SQConstants.MOD_ID, name);
    }

    @Mod.EventHandler
    public void load(final FMLPreInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(this);
        SQConfig.loadConfig(event.getSuggestedConfigurationFile(), event.getSide().isClient());
        BlockQuarry.buildDummies();
        quarry = TeBlockRegistry.get(BlockQuarry.IDENTITY).setCreativeTab(IUCore.SSPTab);
    }


    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {

        Recipes.advRecipes.addRecipe(new ItemStack(quarry, 1),
                "DED",
                "ABA",
                "CCC",'A', new ItemStack(IUItem.core,1,3),'B', Ic2Items.advminer,'C', new ItemStack(IUItem.quantumtool),'D',
                IUItem.cirsuitQuantum,'E',new ItemStack(IUItem.sunnarium,1,1)

        );

        Recipes.advRecipes.addRecipe(new ItemStack(quarry, 1, 1),
                "ABA",
                "DCD",
                "EEE",
                'C',  new ItemStack(quarry),
                'E',
                new ItemStack(IUItem.nanoBox),
                'D',
                (IUItem.QuantumItems9),
                'B',
                OreDictionary.getOres("doubleplateAluminium"),
                'A',
                OreDictionary.getOres("doubleplateAlumel")
        );


        Recipes.advRecipes.addRecipe(new ItemStack(quarry, 1, 2),
                "ABA",
                "DCD",
                "EEE",  'C',  new ItemStack(quarry,1,1),
                'E',
                new ItemStack(IUItem.quantumtool),
                'D',
                (IUItem.cirsuitQuantum),
                'B',
                OreDictionary.getOres("doubleplatePlatinum"),
                'A',
                OreDictionary.getOres("doubleplateVitalium")
        );


        Recipes.advRecipes.addRecipe(new ItemStack(quarry, 1, 3),
                "ABA",
                "DCD",
                "EEE",'C',  new ItemStack(quarry,1,2),
                'E',
                new ItemStack(IUItem.advQuantumtool),
                'D',
                (IUItem.circuitSpectral),
                'B',
                OreDictionary.getOres("doubleplateSpinel"),
                'A',
                OreDictionary.getOres("doubleplateManganese")
        );
    }


    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {

    }


}
