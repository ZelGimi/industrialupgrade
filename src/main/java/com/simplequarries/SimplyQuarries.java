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

@SuppressWarnings({"ALL", "UnnecessaryFullyQualifiedName"})
@Mod.EventBusSubscriber
@Mod(modid = SQConstants.MOD_ID, name = SQConstants.MOD_NAME, dependencies = SQConstants.MOD_DEPS, version = SQConstants.MOD_VERSION, acceptedMinecraftVersions = "[1.12,1.12.2]", certificateFingerprint = SQConstants.MOD_CERTIFICATE)
public final class SimplyQuarries {


    public Block quarry;

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

        register(BlockQuarry.class, BlockQuarry.IDENTITY);


    }


    public static ResourceLocation getIdentifier(final String name) {
        return new ResourceLocation(SQConstants.MOD_ID, name);
    }

    @Mod.EventHandler
    public void load(final FMLPreInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(this);
        BlockQuarry.buildDummies();
        quarry = TeBlockRegistry.get(BlockQuarry.IDENTITY).setCreativeTab(IUCore.IUTab);
    }


    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines, 1, 8),
                "   ",
                " A ",
                " B ", 'A', new ItemStack(IUItem.crafting_elements, 1, 250),
                'B', new ItemStack(quarry, 1)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(quarry, 1),
                " D ",
                "ABE",
                " C ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 158),
                'B',
                Ic2Items.advancedMachine,
                'C',
                Ic2Items.elemotor,
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 72),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 44)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(quarry, 1, 1),
                "D D",
                "ABE",
                "DCD",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 256),
                'B',
                new ItemStack(quarry, 1),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 20),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 138),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 47)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(quarry, 1, 2),
                "D D",
                "ABE",
                "DCD",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 252),
                'B',
                new ItemStack(quarry, 1, 1),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 96),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 139),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 49)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(quarry, 1, 3),
                "D D",
                "ABE",
                "DCD",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 254),
                'B',
                new ItemStack(quarry, 1, 2),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 120),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 140),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 51)

        );
    }


    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {

    }


}
