package com.powerutils;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.QEComponent;
import ic2.api.event.TeBlockFinalCallEvent;
import ic2.api.recipe.Recipes;
import ic2.core.block.ITeBlock;
import ic2.core.block.TeBlockRegistry;
import ic2.core.block.comp.Components;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"ALL", "UnnecessaryFullyQualifiedName"})
@Mod.EventBusSubscriber
@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, dependencies = Constants.MOD_DEPS, version = Constants.MOD_VERSION, acceptedMinecraftVersions = "[1.12,1.12.2]", certificateFingerprint = Constants.MOD_CERTIFICATE)
public final class PowerUtils {


    public static final List<IModelRender> modelList = new ArrayList<>();
    public Block itemPowerConverter;

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

        register(BlockPowerConverter.class, BlockPowerConverter.IDENTITY);
    }

    public static ResourceLocation getIdentifier(final String name) {
        return new ResourceLocation(Constants.MOD_ID, name);
    }

    public static void addIModelRegister(IModelRender puItemBase) {
        modelList.add(puItemBase);
    }


    @Mod.EventHandler
    public void load(final FMLPreInitializationEvent event) {
        PowerItem.init();
        if (event.getSide() == Side.CLIENT) {
            for (IModelRender register : modelList) {
                register.registerModels();
            }
        }
        itemPowerConverter = TeBlockRegistry.get(BlockPowerConverter.IDENTITY).setCreativeTab(IUCore.SSPTab);

        MinecraftForge.EVENT_BUS.register(this);
        PowerConfig.loadConfig(event.getSuggestedConfigurationFile(), event.getSide().isClient());
        BlockPowerConverter.buildDummies();
    }


    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        Components.register(AdvEnergy.class, "AdvEnergy");
        Components.register(QEComponent.class, "QEComponent");
        Recipes.advRecipes.addRecipe(
                new ItemStack(PowerItem.module_ic),
                "ABA",
                "CDC",
                "ABA",
                Character.valueOf('A'),
                Ic2Items.copperCableItem,
                Character.valueOf('B'),
                Ic2Items.mvTransformer,
                Character.valueOf('C'),
                Ic2Items.electronicCircuit,
                Character.valueOf('D'),
                new ItemStack(IUItem.electricblock, 1, 3)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(PowerItem.module_rf), "ABA", "BDB", "ABA", Character.valueOf('A'),
                OreDictionary.getOres("ingotElectrum"), Character.valueOf('B'), OreDictionary.getOres("plateCaravky"),
                Character.valueOf('D'),
                new ItemStack(IUItem.module7, 1, 4)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(PowerItem.module_fe),
                "ABA", "CDC", "ABA", Character.valueOf('A'), new ItemStack(IUItem.basecircuit),
                Character.valueOf('B'), Ic2Items.hvTransformer, Character.valueOf('C'), new ItemStack(IUItem.basecircuit, 1, 4),
                Character.valueOf('D'),
                new ItemStack(IUItem.core, 1, 1)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(PowerItem.module_te),
                "ABA",
                "CDC",
                "ABA",
                Character.valueOf('A'),
                new ItemStack(IUItem.sunnarium, 1, 3),
                Character.valueOf('B'),
                new ItemStack(IUItem.photoniy),
                Character.valueOf('C'),
                new ItemStack(IUItem.preciousgem, 1
                        , 2),
                Character.valueOf('D'),
                new ItemStack(IUItem.core, 1, 1)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(itemPowerConverter),
                "ABA",
                "CDE",
                "ABA",
                Character.valueOf('A'),
                Ic2Items.advancedAlloy,
                Character.valueOf('B'),
                Ic2Items.advancedCircuit,
                Character.valueOf('C'),
                new ItemStack(PowerItem.module_ic),
                Character.valueOf('D'),
                Ic2Items.machine,
                Character.valueOf('E'),
                new ItemStack(PowerItem.module_rf)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(itemPowerConverter, 1, 1),
                "ABA",
                "CDE",
                "ABA",
                Character.valueOf('A'),
                Ic2Items.advancedAlloy,
                Character.valueOf('B'),
                Ic2Items.advancedCircuit,
                Character.valueOf('C'),
                new ItemStack(PowerItem.module_ic),
                Character.valueOf('D'),
                Ic2Items.machine,
                Character.valueOf('E'),
                new ItemStack(PowerItem.module_fe)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(itemPowerConverter, 1, 2),
                "ABA",
                "CDE",
                "ABA",
                Character.valueOf('A'),
                Ic2Items.advancedAlloy,
                Character.valueOf('B'),
                Ic2Items.advancedCircuit,
                Character.valueOf('C'),
                new ItemStack(PowerItem.module_ic),
                Character.valueOf('D'),
                Ic2Items.machine,
                Character.valueOf('E'),
                new ItemStack(PowerItem.module_te)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(itemPowerConverter, 1, 3),
                "ABA",
                "CDE",
                "ABA",
                Character.valueOf('A'),
                Ic2Items.advancedAlloy,
                Character.valueOf('B'),
                Ic2Items.advancedCircuit,
                Character.valueOf('C'),
                new ItemStack(PowerItem.module_ic),
                Character.valueOf('D'),
                Ic2Items.machine,
                Character.valueOf('E'),
                new ItemStack(PowerItem.module_qe)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(PowerItem.module_qe),
                "ABA",
                "CDC",
                "ABA",
                Character.valueOf('A'),
                new ItemStack(IUItem.sunnarium, 1, 3),
                Character.valueOf('B'),
                new ItemStack(IUItem.quantumtool),
                Character.valueOf('C'),
                new ItemStack(IUItem.radiationresources, 1
                        , 2),
                Character.valueOf('D'),
                new ItemStack(IUItem.core, 1, 3)
        );
    }


    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {

    }


}
