package com.simplequarries;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.TileBlockCreator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@SuppressWarnings({"ALL", "UnnecessaryFullyQualifiedName"})
@Mod.EventBusSubscriber
@Mod(modid = SQConstants.MOD_ID, name = SQConstants.MOD_NAME, dependencies = SQConstants.MOD_DEPS, version = SQConstants.MOD_VERSION, acceptedMinecraftVersions = "[1.12,1.12.2]", certificateFingerprint = SQConstants.MOD_CERTIFICATE)
public final class SimplyQuarries {


    public static BlockTileEntity quarry;


    public static ResourceLocation getIdentifier(final String name) {
        return new ResourceLocation(SQConstants.MOD_ID, name);
    }

    @Mod.EventHandler
    public void load(final FMLPreInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(this);
        quarry = TileBlockCreator.instance.create(BlockQuarry.class);
        if (event.getSide() == Side.CLIENT) {
            quarry.registerModels();
        }
    }


    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines, 1, 8), new Object[]{"   ", " A ", " B ",

                Character.valueOf('A'), new ItemStack((Item) IUItem.crafting_elements, 1, 250),
                Character.valueOf('B'), new ItemStack(this.quarry, 1)});
        Recipes.recipe.addRecipe(new ItemStack(this.quarry, 1), new Object[]{
                " D ", "ABE", " C ",

                Character.valueOf('A'), new ItemStack((Item) IUItem.crafting_elements, 1, 158),

                Character.valueOf('B'), IUItem.advancedMachine,

                Character.valueOf('C'), IUItem.elemotor,

                Character.valueOf('D'),
                new ItemStack((Item) IUItem.crafting_elements, 1, 72),

                Character.valueOf('E'), new ItemStack((Item) IUItem.crafting_elements, 1, 44)});
        Recipes.recipe.addRecipe(new ItemStack(this.quarry, 1, 1), new Object[]{
                "D D", "ABE", "DCD",

                Character.valueOf('A'), new ItemStack((Item) IUItem.crafting_elements, 1, 256),

                Character.valueOf('B'), new ItemStack(this.quarry, 1),

                Character.valueOf('C'), new ItemStack((Item) IUItem.crafting_elements, 1, 20),

                Character.valueOf('D'),
                new ItemStack((Item) IUItem.crafting_elements, 1, 138),

                Character.valueOf('E'), new ItemStack((Item) IUItem.crafting_elements, 1, 47)});
        Recipes.recipe.addRecipe(new ItemStack(this.quarry, 1, 2), new Object[]{
                "D D", "ABE", "DCD",

                Character.valueOf('A'), new ItemStack((Item) IUItem.crafting_elements, 1, 252),

                Character.valueOf('B'), new ItemStack(this.quarry, 1, 1),

                Character.valueOf('C'), new ItemStack((Item) IUItem.crafting_elements, 1, 96),

                Character.valueOf('D'),
                new ItemStack((Item) IUItem.crafting_elements, 1, 139),

                Character.valueOf('E'), new ItemStack((Item) IUItem.crafting_elements, 1, 49)});
        Recipes.recipe.addRecipe(new ItemStack(this.quarry, 1, 3), new Object[]{
                "D D", "ABE", "DCD",

                Character.valueOf('A'), new ItemStack((Item) IUItem.crafting_elements, 1, 254),

                Character.valueOf('B'), new ItemStack(this.quarry, 1, 2),

                Character.valueOf('C'), new ItemStack((Item) IUItem.crafting_elements, 1, 120),

                Character.valueOf('D'),
                new ItemStack((Item) IUItem.crafting_elements, 1, 140),

                Character.valueOf('E'), new ItemStack((Item) IUItem.crafting_elements, 1, 51)});

        Recipes.recipe.addRecipe(new ItemStack(this.quarry, 1, 4), new Object[]{
                "D D", "ABE", "DCD",

                Character.valueOf('A'), new ItemStack((Item) IUItem.crafting_elements, 1, 631),

                Character.valueOf('B'), new ItemStack(this.quarry, 1, 3),

                Character.valueOf('C'), new ItemStack((Item) IUItem.crafting_elements, 1, 622),

                Character.valueOf('D'),
                new ItemStack((Item) IUItem.crafting_elements, 1, 623),

                Character.valueOf('E'), new ItemStack((Item) IUItem.crafting_elements, 1, 52)});
    }


    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {

    }


}
