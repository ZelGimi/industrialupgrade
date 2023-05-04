package com.denfop.integration.botania;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.items.IUItemBase;
import com.denfop.items.energy.instruments.EnumTypeInstruments;
import com.denfop.items.energy.instruments.EnumVarietyInstruments;
import com.denfop.items.energy.instruments.ItemEnergyInstruments;
import com.denfop.tiles.mechanism.dual.TileEntitySolidCanner;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.Recipes;
import ic2.core.block.TeBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

public class BotaniaIntegration {

    public static Block blockBotSolarPanel;
    public static Item manasteel_plate;
    public static Item manasteel_core;
    public static Item elementium_plate;
    public static Item elementium_core;
    public static Item terrasteel_plate;
    public static Item terrasteel_core;
    public static ItemStack reactorterastrellSimple;
    public static ItemStack reactorDepletedterastrellSimple;
    public static ItemStack reactorterastrellQuad;
    public static ItemStack reactorterastrellDual;
    public static ItemStack reactorDepletedterastrellQuad;
    public static ItemStack reactorDepletedterastrellDual;
    public static Item rune_sun;
    public static Item rune_night;
    public static Item rune_energy;

    public static Item teraDDrill;

    public static void init() {
        blockBotSolarPanel = TeBlockRegistry.get(BlockBotSolarPanel.IDENTITY).setCreativeTab(IUCore.IUTab);
        manasteel_plate = new IUItemBase("manasteel_plate");
        manasteel_core = new IUItemBase("manasteel_core");
        elementium_plate = new IUItemBase("elementium_plate");
        elementium_core = new IUItemBase("elementium_core");
        terrasteel_plate = new IUItemBase("terrasteel_plate");
        terrasteel_core = new IUItemBase("terrasteel_core");
        rune_sun = new IUItemBase("rune_sun");
        rune_night = new IUItemBase("rune_night");
        rune_energy = new IUItemBase("rune_energy");
        teraDDrill = new ItemEnergyInstruments(EnumTypeInstruments.DRILL, EnumVarietyInstruments.QUANTUM, "teraDDrill");
    }

    public static void recipe() {
        final IRecipeInputFactory input = Recipes.inputFactory;

        BotaniaAPI.registerRuneAltarRecipe(new ItemStack(rune_energy, 1, 0), 12000,
                LibOreDict.RUNE[0], LibOreDict.RUNE[1], new ItemStack(IUItem.photoniy),
                new ItemStack(IUItem.itemiu, 1, 0), new ItemStack(IUItem.iuingot, 1, 17),
                new ItemStack(elementium_plate), new ItemStack(IUItem.compresscarbonultra)
        );
        BotaniaAPI.registerRuneAltarRecipe(new ItemStack(rune_sun, 1, 0), 12000,
                LibOreDict.RUNE[4], LibOreDict.RUNE[3], new ItemStack(IUItem.photoniy_ingot),
                new ItemStack(IUItem.itemiu, 1, 0), new ItemStack(IUItem.iuingot, 1, 17),
                new ItemStack(elementium_plate), new ItemStack(IUItem.compresscarbon)
        );
        BotaniaAPI.registerRuneAltarRecipe(
                new ItemStack(rune_night, 1, 0),
                12000,
                LibOreDict.RUNE[7],
                LibOreDict.RUNE[5],
                new ItemStack(Ic2Items.energiumDust.getItem(), 1, Ic2Items.energiumDust.getItemDamage()),
                new ItemStack(Ic2Items.energiumDust.getItem(), 1, Ic2Items.energiumDust.getItemDamage()),
                new ItemStack(Ic2Items.energiumDust.getItem(), 1, Ic2Items.energiumDust.getItemDamage()),
                new ItemStack(Ic2Items.energiumDust.getItem(), 1, Ic2Items.energiumDust.getItemDamage()),
                new ItemStack(Ic2Items.energiumDust.getItem(), 1, Ic2Items.energiumDust.getItemDamage()),
                new ItemStack(Ic2Items.energiumDust.getItem(), 1, Ic2Items.energiumDust.getItemDamage()),
                new ItemStack(Ic2Items.energiumDust.getItem(), 1, Ic2Items.energiumDust.getItemDamage()),
                new ItemStack(Ic2Items.energiumDust.getItem(), 1, Ic2Items.energiumDust.getItemDamage()),
                new ItemStack(Ic2Items.energiumDust.getItem(), 1, Ic2Items.energiumDust.getItemDamage()),
                new ItemStack(IUItem.itemiu, 1, 0),
                new ItemStack(IUItem.iuingot, 1, 17),
                new ItemStack(manasteel_plate),
                new ItemStack(IUItem.coal_chunk1)
        );
        addcompressor(
                new ItemStack(ModItems.manaResource, 1, 0), new ItemStack(manasteel_plate)
        );
        addcompressor(
                new ItemStack(ModItems.manaResource, 1, 7), new ItemStack(elementium_plate)
        );
        addcompressor(
                new ItemStack(ModItems.manaResource, 1, 4), new ItemStack(terrasteel_plate)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(teraDDrill, 1, OreDictionary.WILDCARD_VALUE),
                " L ", "ODO", "COC", 'O', IUItem.overclockerUpgrade, 'D',
                new ItemStack(Ic2Items.diamondDrill.getItem(), 1, OreDictionary.WILDCARD_VALUE), 'C',
                terrasteel_plate, 'L', ModItems.terraPick
        );
        Recipes.advRecipes.addRecipe(new ItemStack(terrasteel_core),
                "KLM", "DOD", "CHC", 'C', terrasteel_plate, 'D', new ItemStack(IUItem.itemiu, 1, 1), 'O',
                terrasteel_plate, 'L', Ic2Items.advancedAlloy, 'K', rune_night, 'M', rune_sun, 'H',
                rune_energy
        );
        Recipes.advRecipes.addRecipe(new ItemStack(elementium_core),
                "KLM", "DOD", "CHC", 'C', elementium_plate, 'D', IUItem.photoniy_ingot, 'O',
                manasteel_core, 'L', Ic2Items.advancedCircuit, 'K', rune_night, 'M', rune_sun, 'H',
                rune_energy
        );
        Recipes.advRecipes.addRecipe(new ItemStack(manasteel_core),
                "KLM", "DOD", "CHC", 'C', manasteel_plate, 'D', IUItem.photoniy_ingot, 'O',
                IUItem.core, 'L', Ic2Items.advancedCircuit, 'K', rune_night, 'M', rune_sun,
                'H', rune_energy
        );
        Recipes.advRecipes.addRecipe(new ItemStack(blockBotSolarPanel, 1, 0), " B ", "BAB", " B ", 'A',
                manasteel_core, 'B', new ItemStack(IUItem.blockpanel, 1, 0)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(blockBotSolarPanel, 1, 1), " B ", "BAB", " B ", 'A',
                elementium_core, 'B', new ItemStack(blockBotSolarPanel, 1, 0)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(blockBotSolarPanel, 1, 2), " B ", "BAB", " B ", 'A',
                terrasteel_core, 'B', new ItemStack(blockBotSolarPanel, 1, 1)
        );

        Recipes.advRecipes.addRecipe(reactorterastrellDual, "SQS", 'S',
                reactorterastrellSimple, 'Q', "plateIron"
        );
        Recipes.advRecipes.addRecipe(reactorterastrellQuad, "SQS", "CQC", "SQS", 'S',
                reactorterastrellSimple, 'Q', "plateIron", 'C', "plateCopper"
        );

        Recipes.advRecipes.addRecipe(reactorterastrellQuad, "SQS", 'S',
                reactorterastrellDual, 'Q', "plateIron", 'C', "plateCopper"
        );

        TileEntitySolidCanner.addBottleRecipe(
                Ic2Items.fuelRod,
                new ItemStack(ModItems.manaResource, 1, 4),
                reactorterastrellSimple
        );
        addcompressor(reactorDepletedterastrellSimple, new ItemStack(ModItems.manaResource, 1, 4)
        );
        addcompressor(reactorDepletedterastrellDual, new ItemStack(ModItems.manaResource, 2, 4)
        );
        addcompressor(reactorDepletedterastrellQuad, new ItemStack(ModItems.manaResource, 4, 4)
        );
        //
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.upgradepanelkit, 1, 17), "   ", "BAB", " B ", 'A',
                manasteel_core, 'B', new ItemStack(IUItem.blockpanel, 1, 0)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.upgradepanelkit, 1, 18), "   ", "BAB", " B ", 'A',
                elementium_core, 'B', new ItemStack(blockBotSolarPanel, 1, 0)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.upgradepanelkit, 1, 19), "   ", "BAB", " B ", 'A',
                terrasteel_core, 'B', new ItemStack(blockBotSolarPanel, 1, 1)
        );

    }

    public static void addcompressor(ItemStack input, ItemStack output) {

        final IRecipeInputFactory input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "compressor",
                new BaseMachineRecipe(
                        new Input(
                                input1.forStack(input)
                        ),
                        new RecipeOutput(null, output)
                )
        );


    }

}
