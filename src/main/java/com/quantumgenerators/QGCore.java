package com.quantumgenerators;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.TileBlockCreator;
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
@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, dependencies = Constants.MOD_DEPS, version = Constants.MOD_VERSION, acceptedMinecraftVersions = "[1.12,1.12.2]", certificateFingerprint = Constants.MOD_CERTIFICATE)
public final class QGCore {


    public static BlockTileEntity qg;


    public static ResourceLocation getIdentifier(final String name) {
        return new ResourceLocation(Constants.MOD_ID, name);
    }

    @Mod.EventHandler
    public void load(final FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        qg = TileBlockCreator.instance.create(BlockQG.class);
        if (event.getSide() == Side.CLIENT) {
            qg.registerModels();
        }
    }


    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        Recipes.recipe.addRecipe(new ItemStack(this.qg, 1, 0), new Object[]{
                "CBC", "BAB", "DBD",

                Character.valueOf('A'), new ItemStack(IUItem.blockpanel, 1, 7), Character.valueOf('B'), new ItemStack(
                IUItem.core,
                1,
                8
        ),
                Character.valueOf('C'), new ItemStack(IUItem.advQuantumtool), Character.valueOf('D'),
                IUItem.circuitSpectral});
        Recipes.recipe.addRecipe(new ItemStack(this.qg, 1, 1), new Object[]{" B ", "BAB", " B ",

                Character.valueOf('A'), new ItemStack(this.qg, 1, 0), Character.valueOf('B'), new ItemStack(IUItem.core, 1, 9)});
        Recipes.recipe.addRecipe(new ItemStack(this.qg, 1, 2), new Object[]{" B ", "BAB", " B ",

                Character.valueOf('A'), new ItemStack(this.qg, 1, 1), Character.valueOf('B'), new ItemStack(IUItem.core, 1, 10)});
        Recipes.recipe.addRecipe(new ItemStack(this.qg, 1, 3), new Object[]{" B ", "BAB", " B ",

                Character.valueOf('A'), new ItemStack(this.qg, 1, 2), Character.valueOf('B'), new ItemStack(IUItem.core, 1, 11)});
        Recipes.recipe.addRecipe(new ItemStack(this.qg, 1, 4), new Object[]{" B ", "BAB", " B ",

                Character.valueOf('A'), new ItemStack(this.qg, 1, 3), Character.valueOf('B'), new ItemStack(IUItem.core, 1, 12)});
        Recipes.recipe.addRecipe(new ItemStack(this.qg, 1, 5), new Object[]{" B ", "BAB", " B ",

                Character.valueOf('A'), new ItemStack(this.qg, 1, 4), Character.valueOf('B'), new ItemStack(IUItem.core, 1, 13)});
    }


    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {

    }


}
