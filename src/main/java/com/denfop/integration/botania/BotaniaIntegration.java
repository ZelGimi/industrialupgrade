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
                new ItemStack(elementium_plate), new ItemStack(IUItem.compresscarbonultra)
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


        addcompressor(reactorDepletedterastrellSimple, new ItemStack(ModItems.manaResource, 1, 4)
        );
        addcompressor(reactorDepletedterastrellDual, new ItemStack(ModItems.manaResource, 2, 4)
        );
        addcompressor(reactorDepletedterastrellQuad, new ItemStack(ModItems.manaResource, 4, 4)
        );
        //

        ItemStack topRod = new ItemStack(IUItem.crafting_elements, 1, 185);
        ItemStack bottomRod = new ItemStack(IUItem.crafting_elements, 1, 186);

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
