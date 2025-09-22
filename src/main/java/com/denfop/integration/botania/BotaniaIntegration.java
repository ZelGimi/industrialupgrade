package com.denfop.integration.botania;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.items.IUItemBase;
import com.denfop.items.energy.instruments.EnumTypeInstruments;
import com.denfop.items.energy.instruments.EnumVarietyInstruments;
import com.denfop.items.energy.instruments.ItemEnergyInstruments;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.mechanism.TileGenerationMicrochip;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

public class BotaniaIntegration {

    public static BlockTileEntity blockBotSolarPanel;
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
        blockBotSolarPanel = TileBlockCreator.instance.create(BlockBotSolarPanel.class);
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
        final IInputHandler input = Recipes.inputFactory;

        BotaniaAPI.registerRuneAltarRecipe(new ItemStack(rune_energy, 1, 0), 12000,
                LibOreDict.RUNE[0], LibOreDict.RUNE[1], new ItemStack(IUItem.photoniy),
                new ItemStack(IUItem.itemiu, 1, 0), "ingotIridium",
                new ItemStack(elementium_plate), new ItemStack(IUItem.compressAlloy)
        );
        BotaniaAPI.registerRuneAltarRecipe(new ItemStack(rune_sun, 1, 0), 12000,
                LibOreDict.RUNE[4], LibOreDict.RUNE[3], new ItemStack(IUItem.photoniy_ingot),
                new ItemStack(IUItem.itemiu, 1, 0), "ingotIridium",
                new ItemStack(elementium_plate), new ItemStack(IUItem.compresscarbon)
        );
        BotaniaAPI.registerRuneAltarRecipe(
                new ItemStack(rune_night, 1, 0),
                12000,
                LibOreDict.RUNE[7],
                LibOreDict.RUNE[5],
                new ItemStack(IUItem.energiumDust.getItem(), 1, IUItem.energiumDust.getItemDamage()),
                new ItemStack(IUItem.energiumDust.getItem(), 1, IUItem.energiumDust.getItemDamage()),
                new ItemStack(IUItem.energiumDust.getItem(), 1, IUItem.energiumDust.getItemDamage()),
                new ItemStack(IUItem.energiumDust.getItem(), 1, IUItem.energiumDust.getItemDamage()),
                new ItemStack(IUItem.energiumDust.getItem(), 1, IUItem.energiumDust.getItemDamage()),
                new ItemStack(IUItem.energiumDust.getItem(), 1, IUItem.energiumDust.getItemDamage()),
                new ItemStack(IUItem.energiumDust.getItem(), 1, IUItem.energiumDust.getItemDamage()),
                new ItemStack(IUItem.energiumDust.getItem(), 1, IUItem.energiumDust.getItemDamage()),
                new ItemStack(IUItem.energiumDust.getItem(), 1, IUItem.energiumDust.getItemDamage()),
                new ItemStack(IUItem.itemiu, 1, 0),
                "ingotIridium",
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

        Recipes.recipe.addRecipe(new ItemStack(teraDDrill),
                " L ", "ODO", "COC", 'O', IUItem.overclockerUpgrade, 'D',
                new ItemStack(IUItem.diamond_drill), 'C',
                terrasteel_plate, 'L', ModItems.terraPick
        );
        Recipes.recipe.addRecipe(new ItemStack(terrasteel_core),
                "KLM", "DOD", "CHC", 'C', terrasteel_plate, 'D', new ItemStack(IUItem.itemiu, 1, 1), 'O',
                terrasteel_plate, 'L', IUItem.advancedAlloy, 'K', rune_night, 'M', rune_sun, 'H',
                rune_energy
        );
        Recipes.recipe.addRecipe(new ItemStack(elementium_core),
                "KLM", "DOD", "CHC", 'C', elementium_plate, 'D', IUItem.photoniy_ingot, 'O',
                manasteel_core, 'L',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3), 'K', rune_night, 'M', rune_sun, 'H',
                rune_energy
        );
        Recipes.recipe.addRecipe(
                new ItemStack(manasteel_core),
                "KLM",
                "DOD",
                "CHC",
                'C',
                manasteel_plate,
                'D',
                IUItem.photoniy_ingot,
                'O',
                IUItem.core,
                'L',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),
                'K',
                rune_night,
                'M',
                rune_sun,
                'H',
                rune_energy
        );
        Recipes.recipe.addRecipe(new ItemStack(blockBotSolarPanel, 1, 0), " B ", "BAB", " B ", 'A',
                manasteel_core, 'B', new ItemStack(IUItem.blockpanel, 1, 0)
        );
        Recipes.recipe.addRecipe(new ItemStack(blockBotSolarPanel, 1, 1), " B ", "BAB", " B ", 'A',
                elementium_core, 'B', new ItemStack(blockBotSolarPanel, 1, 0)
        );
        Recipes.recipe.addRecipe(new ItemStack(blockBotSolarPanel, 1, 2), " B ", "BAB", " B ", 'A',
                terrasteel_core, 'B', new ItemStack(blockBotSolarPanel, 1, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.upgradepanelkit, 1, 17), "   ", "BAB", " B ", 'A',
                manasteel_core, 'B', new ItemStack(IUItem.blockpanel, 1, 0)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.upgradepanelkit, 1, 18), "   ", "BAB", " B ", 'A',
                elementium_core, 'B', new ItemStack(blockBotSolarPanel, 1, 0)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.upgradepanelkit, 1, 19), "   ", "BAB", " B ", 'A',
                terrasteel_core, 'B', new ItemStack(blockBotSolarPanel, 1, 1)
        );

        //


    }

    public static void addcompressor(ItemStack input, ItemStack output) {

        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "compressor",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, output)
                )
        );


    }

}
