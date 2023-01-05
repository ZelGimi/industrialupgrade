package com.quantumgenerators;

import com.denfop.IUCore;
import com.denfop.IUItem;
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

@SuppressWarnings({"ALL", "UnnecessaryFullyQualifiedName"})
@Mod.EventBusSubscriber
@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, dependencies = Constants.MOD_DEPS, version = Constants.MOD_VERSION, acceptedMinecraftVersions = "[1.12,1.12.2]", certificateFingerprint = Constants.MOD_CERTIFICATE)
public final class QGCore {


    public Block qg;


    public static <E extends Enum<E> & ITeBlock> void register(Class<E> enumClass, ResourceLocation ref) {
        TeBlockRegistry.addAll(enumClass, ref);
        TeBlockRegistry.setDefaultMaterial(ref, Material.ROCK);
        TeBlockRegistry.addCreativeRegisterer((list, block, itemblock, tab) -> {
            if (tab == CreativeTabs.SEARCH || tab == IUCore.IUTab) {
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

        register(BlockQG.class, BlockQG.IDENTITY);


    }


    public static ResourceLocation getIdentifier(final String name) {
        return new ResourceLocation(Constants.MOD_ID, name);
    }

    @Mod.EventHandler
    public void load(final FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        BlockQG.buildDummies();
        qg = TeBlockRegistry.get(BlockQG.IDENTITY).setCreativeTab(IUCore.IUTab);
    }


    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        Recipes.advRecipes.addRecipe(new ItemStack(qg, 1, 0),
                "CBC",
                "BAB",
                "DBD", 'A', new ItemStack(IUItem.blockpanel, 1, 7), 'B', new ItemStack(IUItem.core, 1, 8),
                'C', new ItemStack(IUItem.advQuantumtool), 'D', IUItem.circuitSpectral

        );
        Recipes.advRecipes.addRecipe(new ItemStack(qg, 1, 1),
                " B ",
                "BAB",
                " B ", 'A', new ItemStack(qg, 1, 0), 'B', new ItemStack(IUItem.core, 1, 9)

        );
        Recipes.advRecipes.addRecipe(new ItemStack(qg, 1, 2),
                " B ",
                "BAB",
                " B ", 'A', new ItemStack(qg, 1, 1), 'B', new ItemStack(IUItem.core, 1, 10)

        );
        Recipes.advRecipes.addRecipe(new ItemStack(qg, 1, 3),
                " B ",
                "BAB",
                " B ", 'A', new ItemStack(qg, 1, 2), 'B', new ItemStack(IUItem.core, 1, 11)

        );
        Recipes.advRecipes.addRecipe(new ItemStack(qg, 1, 4),
                " B ",
                "BAB",
                " B ", 'A', new ItemStack(qg, 1, 3), 'B', new ItemStack(IUItem.core, 1, 12)

        );
        Recipes.advRecipes.addRecipe(new ItemStack(qg, 1, 5),
                " B ",
                "BAB",
                " B ", 'A', new ItemStack(qg, 1, 4), 'B', new ItemStack(IUItem.core, 1, 13)

        );
    }


    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {

    }


}
