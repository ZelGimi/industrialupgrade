package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.blocks.mechanism.BlockPrimalFluidHeater;
import com.denfop.componets.Fluids;
import com.denfop.register.RegisterOreDictionary;
import com.denfop.tiles.mechanism.TileEntityUpgradeMachineFactory;
import com.denfop.tiles.mechanism.TileGenerationMicrochip;
import com.denfop.utils.ModUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class BaseRecipes {

    public static Item DEFAULT_SENSOR = IUItem.crafting_elements.getItemFromMeta(21);

    public static Item ADV_SENSOR = IUItem.crafting_elements.getItemFromMeta(25);

    public static Item IMP_SENSOR = IUItem.crafting_elements.getItemFromMeta(23);

    public static Item PER_SENSOR = IUItem.crafting_elements.getItemFromMeta(24);

    public static Item PHOTON_SENSOR = IUItem.crafting_elements.getItemFromMeta(620);

    public static void init() {
        DEFAULT_SENSOR = IUItem.crafting_elements.getItemFromMeta(21);

        ADV_SENSOR = IUItem.crafting_elements.getItemFromMeta(25);

        IMP_SENSOR = IUItem.crafting_elements.getItemFromMeta(23);

        PER_SENSOR = IUItem.crafting_elements.getItemFromMeta(24);

        PHOTON_SENSOR = IUItem.crafting_elements.getItemFromMeta(620);

        Recipes.recipe.addShapelessRecipe(
                IUItem.bronzeBlock,
                "c:ingots/Bronze",
                "c:ingots/Bronze",
                "c:ingots/Bronze",
                "c:ingots/Bronze",
                "c:ingots/Bronze",
                "c:ingots/Bronze",
                "c:ingots/Bronze",
                "c:ingots/Bronze",
                "c:ingots/Bronze"
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.copperBlock,
                "c:ingots/Copper",
                "c:ingots/Copper",
                "c:ingots/Copper",
                "c:ingots/Copper",
                "c:ingots/Copper",
                "c:ingots/Copper",
                "c:ingots/Copper",
                "c:ingots/Copper",
                "c:ingots/Copper"
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.tinBlock,
                "c:ingots/Tin", "c:ingots/Tin", "c:ingots/Tin", "c:ingots/Tin", "c:ingots/Tin", "c:ingots/Tin", "c:ingots/Tin", "c:ingots/Tin", "c:ingots/Tin"
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.leadBlock,
                "c:ingots/Lead",
                "c:ingots/Lead",
                "c:ingots/Lead",
                "c:ingots/Lead",
                "c:ingots/Lead",
                "c:ingots/Lead",
                "c:ingots/Lead",
                "c:ingots/Lead",
                "c:ingots/Lead"
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.uraniumBlock,
                "c:ingots/Uranium",
                "c:ingots/Uranium",
                "c:ingots/Uranium",
                "c:ingots/Uranium",
                "c:ingots/Uranium",
                "c:ingots/Uranium",
                "c:ingots/Uranium",
                "c:ingots/Uranium",
                "c:ingots/Uranium"
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.advironblock,
                "c:ingots/Steel",
                "c:ingots/Steel",
                "c:ingots/Steel",
                "c:ingots/Steel",
                "c:ingots/Steel",
                "c:ingots/Steel",
                "c:ingots/Steel",
                "c:ingots/Steel",
                "c:ingots/Steel"
        );
        Recipes.recipe.addShapelessRecipe(
                ItemStackHelper.fromData(IUItem.block1.getItem(3), 1),
                "c:ingots/Osmium",
                "c:ingots/Osmium",
                "c:ingots/Osmium",
                "c:ingots/Osmium",
                "c:ingots/Osmium",
                "c:ingots/Osmium",
                "c:ingots/Osmium",
                "c:ingots/Osmium",
                "c:ingots/Osmium"
        );
        Recipes.recipe.addShapelessRecipe(
                ItemStackHelper.fromData(IUItem.block1.getItem(4), 1),
                "c:ingots/Tantalum",
                "c:ingots/Tantalum",
                "c:ingots/Tantalum",
                "c:ingots/Tantalum",
                "c:ingots/Tantalum",
                "c:ingots/Tantalum",
                "c:ingots/Tantalum",
                "c:ingots/Tantalum",
                "c:ingots/Tantalum"
        );
        Recipes.recipe.addShapelessRecipe(
                ItemStackHelper.fromData(IUItem.block1.getItem(5), 1),
                "c:ingots/Cadmium",
                "c:ingots/Cadmium",
                "c:ingots/Cadmium",
                "c:ingots/Cadmium",
                "c:ingots/Cadmium",
                "c:ingots/Cadmium",
                "c:ingots/Cadmium",
                "c:ingots/Cadmium",
                "c:ingots/Cadmium"
        );
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.advIronIngot, 9), IUItem.advironblock);
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.copperIngot, 9), IUItem.copperBlock);
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.tinIngot, 9), IUItem.tinBlock);
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.leadIngot, 9), IUItem.leadBlock);
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.bronzeIngot, 9), IUItem.bronzeBlock);
        Recipes.recipe.addShapelessRecipe(ItemStackHelper.fromData(IUItem.itemiu.getStack(2), 9), IUItem.uraniumBlock);


        Recipes.recipe.addRecipe(IUItem.efReader.getItemStack(), " A ", "BCB", "B B",

                ('A'), Items.GLOWSTONE_DUST, ('B'),
                IUItem.insulatedCopperCableItem, ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
        );

        Recipes.recipe.addRecipe(IUItem.module_infinity_water.getItemStack(), "BBB", "CAC", "DED",

                ('A'), IUItem.module_schedule, ('B'),
                ModUtils.getCellFromFluid(Fluids.WATER), ('C'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5), 'D', IUItem.quantumtool, 'E',
                "c:doubleplate/Iridium"
        );
        Recipes.recipe.addRecipe(IUItem.module_separate.getItemStack(), "BBB", "CAC", "DED",

                ('A'), IUItem.module_storage, ('B'),
                "c:plates/Carbon", ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5), 'D', "c:gears/Electrum", 'E',
                "c:doubleplate/Aluminumbronze"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ruby_boots.getItem()), "   ", "A A", "A A",
                ('A'), ("c:gems/Ruby")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.sapphire_boots.getItem()), "   ", "A A", "A A",
                ('A'), ("c:gems/Sapphire")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.topaz_boots.getItem()), "   ", "A A", "A A",
                ('A'), ("c:gems/Topaz")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ruby_chestplate.getItem()), "A A", "AAA", "AAA",
                ('A'), ("c:gems/Ruby")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blockResource.getItem(11), 1), "AAA", "AAA", "AAA",
                ('A'), IUItem.peat_balls
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.sapphire_chestplate.getItem()), "A A", "AAA", "AAA",
                ('A'), ("c:gems/Sapphire")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.topaz_chestplate.getItem()), "A A", "AAA", "AAA",
                ('A'), ("c:gems/Topaz")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ruby_leggings.getItem()), "AAA", "A A", "A A",
                ('A'), ("c:gems/Ruby")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.sapphire_leggings.getItem()), "AAA", "A A", "A A",
                ('A'), ("c:gems/Sapphire")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.topaz_leggings.getItem()), "AAA", "A A", "A A",
                ('A'), ("c:gems/Topaz")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ruby_helmet.getItem()), "AAA", "A A", "   ",
                ('A'), ("c:gems/Ruby")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.sapphire_helmet.getItem()), "AAA", "A A", "   ",
                ('A'), ("c:gems/Sapphire")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.topaz_helmet.getItem()), "AAA", "A A", "   ",
                ('A'), ("c:gems/Topaz")
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 2), "ABA", "CCC", "AAA",
                ('A'), ("planks"), ('C'), ItemStackHelper.fromData(IUItem.advBattery.getItem(), 1),

                ('B'), IUItem.tinCableItem
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 5), "ABA", "CCC", "ADA",

                ('A'),
                ("c:plates/Bronze"),
                ('C'), ItemStackHelper.fromData(IUItem.reBattery, 1),

                ('B'), IUItem.insulatedCopperCableItem, 'D', ItemStackHelper.fromData(IUItem.electricblock, 1, 2)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 3), "ACA", "CBC", "ACA",

                ('A'),
                ("c:doubleplate/Aluminumbronze"),
                ('C'), ItemStackHelper.fromData(IUItem.energy_crystal, 1),

                ('B'), ItemStackHelper.fromData(IUItem.electricblock, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 4), "CDC", "CAC", "CBC",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),

                ('A'), ItemStackHelper.fromData(IUItem.electricblock, 1, 3),

                ('C'), ItemStackHelper.fromData(IUItem.lapotron_crystal, 1),

                ('B'),
                IUItem.advancedMachine
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 0), "CDC", "DAD", "CDC",

                ('D'), ItemStackHelper.fromData(IUItem.photoniy_ingot),

                ('A'), ItemStackHelper.fromData(IUItem.electricblock, 1, 4),

                ('C'), ItemStackHelper.fromData(IUItem.lapotron_crystal, 1),

                ('B'),
                IUItem.advancedMachine
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 1), "CDC", "DAD", "CDC",

                ('D'), ItemStackHelper.fromData(IUItem.photoniy_ingot),

                ('A'), ItemStackHelper.fromData(IUItem.electricblock, 1, 0),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 6), "CDC", "DAD", "CDC",

                ('D'), ItemStackHelper.fromData(IUItem.core, 1, 4),

                ('A'), ItemStackHelper.fromData(IUItem.electricblock, 1, 1),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 7), "CDC", "DAD", "CDC",

                ('D'), ItemStackHelper.fromData(IUItem.core, 1, 8),

                ('A'), ItemStackHelper.fromData(IUItem.electricblock, 1, 6),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 8), "CBC", "DAD", "CBC",

                ('D'), ItemStackHelper.fromData(IUItem.core, 1, 9),

                ('A'), ItemStackHelper.fromData(IUItem.electricblock, 1, 7),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'),
                ItemStackHelper.fromData(IUItem.AdvlapotronCrystal, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 9), "EBE", "DAD", "CBC",

                ('D'), ItemStackHelper.fromData(IUItem.core, 1, 11),

                ('E'), ItemStackHelper.fromData(IUItem.compressIridiumplate),

                ('A'), ItemStackHelper.fromData(IUItem.electricblock, 1, 8),

                ('C'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), ItemStackHelper.fromData(IUItem.AdvlapotronCrystal, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 10), "EBE", "DAD", "CBC",

                ('D'), ItemStackHelper.fromData(IUItem.core, 1, 12),

                ('E'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('A'), ItemStackHelper.fromData(IUItem.electricblock, 1, 9),

                ('C'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), ItemStackHelper.fromData(IUItem.AdvlapotronCrystal, 1)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.voltage_sensor_for_mechanism), "AAA", "BCB", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.nanoBox),

                ('B'), "c:plates/TantalumTungstenHafnium",

                ('C'), ItemStackHelper.fromData(IUItem.efReader)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.polonium_palladium_composite), "AAA", "BBB", "CCC",

                ('A'), "c:plates/Polonium",

                ('B'), "c:plates/Palladium",

                ('C'), IUItem.advancedAlloy
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.medium_current_converter_to_low), "AAA", "BDB", "CEC",

                ('A'), IUItem.graphene_plate,

                ('B'), ItemStackHelper.fromData(IUItem.cable, 1, 14),

                ('C'), IUItem.polonium_palladium_composite,

                ('D'), IUItem.voltage_sensor_for_mechanism,

                ('E'), IUItem.motors_with_improved_bearings_
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.high_current_converter_to_low), "AAA", "BDB", "CEC",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 320),

                ('B'), ItemStackHelper.fromData(IUItem.cable, 1, 13),

                ('C'), "c:plates/Zeliber",

                ('D'), IUItem.medium_current_converter_to_low,

                ('E'), IUItem.adv_motors_with_improved_bearings_
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.extreme_current_converter_to_low), "AAA", "BDB", "CEC",

                ('A'), IUItem.synthetic_plate,

                ('B'), ItemStackHelper.fromData(IUItem.cable, 1, 0),

                ('C'), "c:plates/StainlessSteel",

                ('D'), IUItem.high_current_converter_to_low,

                ('E'), IUItem.imp_motors_with_improved_bearings_
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.motors_with_improved_bearings_), " A ", "BDB", "CEC",

                ('A'), IUItem.compressed_redstone,

                ('B'), IUItem.electronic_stabilizers,

                ('C'), IUItem.graphene_wire,

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 588)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electronic_stabilizers), "A A", "B B", "A A",

                ('A'), "c:plates/Iron",

                ('B'), "c:plates/Lapis"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_motors_with_improved_bearings_), " A ", "BDB", "CEC",

                ('A'), IUItem.compressed_redstone,

                ('B'), IUItem.electronic_stabilizers,

                ('C'), IUItem.graphene_wire,

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 598)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.imp_motors_with_improved_bearings_), " A ", "BDB", "CEC",

                ('A'), IUItem.compressed_redstone,

                ('B'), IUItem.electronic_stabilizers,

                ('C'), IUItem.graphene_wire,

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 592)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.advnanobox), " C ", "CAC", " C ",
                ('C'), ItemStackHelper.fromData(IUItem.photoniy, 1), ('A'), ItemStackHelper.fromData(IUItem.nanoBox)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.advQuantumtool), "CDC", "BAB", "CFC",

                ('F'), ItemStackHelper.fromData(IUItem.compressIridiumplate),

                ('B'),
                ("c:doubleplate/Iridium"),
                ('D'),
                ("c:casings/Iridium"),
                ('C'),
                ItemStackHelper.fromData(IUItem.photoniy_ingot, 1),

                ('A'), ItemStackHelper.fromData(IUItem.advnanobox)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nanoBox), "ACA", "BAB", "ACA",

                ('B'), IUItem.carbonPlate,

                ('C'), ItemStackHelper.fromData(IUItem.compresscarbon, 1),

                ('A'), ItemStackHelper.fromData(IUItem.iudust, 1, 24)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.quantumtool), "FDF", "BAB", "CDC",

                ('F'),
                ("c:doubleplate/Iridium"),
                ('B'), ItemStackHelper.fromData(IUItem.compressAlloy, 1),

                ('D'), ItemStackHelper.fromData(IUItem.compresscarbon, 1),

                ('C'),
                IUItem.iridiumOre,

                ('A'), ItemStackHelper.fromData(IUItem.nanoBox)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.magnet), "A B", "CDC", " C ",

                ('B'),
                ("c:ingots/Ferromanganese"),
                ('D'), ItemStackHelper.fromData(IUItem.advBattery, 1),

                ('C'),
                ("c:doubleplate/Mikhail"),
                ('A'),
                ("c:ingots/Vitalium")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.impmagnet), "B B", "CDC", " C ",

                ('B'),
                ("c:ingots/Duralumin"),
                ('D'), ItemStackHelper.fromData(IUItem.magnet, 1),

                ('C'),
                ("c:doubleplate/Invar")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.AdvlapotronCrystal), "BCB", "CDC", "BCB",

                ('B'), ItemStackHelper.fromData(IUItem.lapotron_crystal, 1),

                ('D'), ItemStackHelper.fromData(IUItem.photoniy_ingot),

                ('C'), ItemStackHelper.fromData(IUItem.advQuantumtool, 1)
        );
        Recipes.recipe.addRecipe(IUItem.tranformerUpgrade, " A ", "DBD", " C ",

                ('B'), IUItem.high_current_converter_to_low,

                ('A'), ItemStackHelper.fromData(IUItem.tranformer, 1, 9),

                ('D'), IUItem.insulatedGoldCableItem,

                ('C'),
                IUItem.upgrade_casing
        );
        Recipes.recipe.addRecipe(IUItem.tranformerUpgrade1, " A ", "DBD", " C ",

                ('B'), IUItem.extreme_current_converter_to_low,

                ('A'), ItemStackHelper.fromData(IUItem.tranformer, 1, 0),

                ('D'), IUItem.insulatedGoldCableItem,

                ('C'),
                IUItem.upgrade_casing
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nanopickaxe), "ADB", "EFE", "CHC",
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 524), ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 523), 'C',
                ("c:doubleplate/Ferromanganese"), ('D'), new ItemStack(Items.DIAMOND_PICKAXE), ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 520), 'H', "c:rods/Molybdenum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nanoaxe), "ADB", "EFE", "CHC",
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 524), ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 522), 'C',
                ("c:doubleplate/Ferromanganese"), ('D'), new ItemStack(Items.DIAMOND_AXE), ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 519), 'H', "c:rods/Molybdenum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nanoshovel), "ADB", "EFE", "CHC",
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 524), ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 521), 'C',
                ("c:doubleplate/Ferromanganese"), ('D'), new ItemStack(Items.DIAMOND_SHOVEL), ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 518), 'H', "c:rods/Molybdenum"
        );
        //
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.quantumpickaxe), "BDA", "EFE", "CHC",
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 516), ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 512), 'C',
                ("c:doubleplate/Muntsa"), ('D'), ItemStackHelper.fromData(IUItem.nanopickaxe), ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 515), 'H', "c:rods/Electrum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.quantumaxe), "ADB", "EFE", "CHC",
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 516), ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 514), 'C',
                ("c:doubleplate/Muntsa"), ('D'), ItemStackHelper.fromData(IUItem.nanoaxe), ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 511), 'H', "c:rods/Electrum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.quantumshovel), "ADB", "EFE", "CHC",
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 516), ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 513), 'C',
                ("c:doubleplate/Muntsa"), ('D'), ItemStackHelper.fromData(IUItem.nanoshovel), ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 510), 'H', "c:rods/Electrum"
        );
        //
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spectralpickaxe), "ADB", "EFE", "CHC",
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 532), ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 531), 'C',
                IUItem.iridiumPlate, ('D'), ItemStackHelper.fromData(IUItem.quantumpickaxe), ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 528), 'H', "c:rods/Iridium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spectralaxe), "ADB", "EFE", "CHC",
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 532), ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 530), 'C',
                IUItem.iridiumPlate, ('D'), ItemStackHelper.fromData(IUItem.quantumaxe), ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 527), 'H', "c:rods/Iridium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spectralshovel), "ADB", "EFE", "CHC",
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 532), ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 529), 'C',
                IUItem.iridiumPlate, ('D'), ItemStackHelper.fromData(IUItem.quantumshovel), ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 526), 'H', "c:rods/Iridium"
        );
        //

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.impBatChargeCrystal), "BCB", "BAB", "BCB",

                ('B'), ItemStackHelper.fromData(IUItem.AdvlapotronCrystal, 1),

                ('A'), ItemStackHelper.fromData(IUItem.charging_lapotron_crystal, 1),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.perBatChargeCrystal), "DCD", "BAB", "ECE",

                ('E'),
                ("c:doubleplate/Vitalium"),
                ('D'), IUItem.iridiumPlate,

                ('B'), ItemStackHelper.fromData(IUItem.AdvlapotronCrystal, 1),

                ('A'),
                ItemStackHelper.fromData(IUItem.impBatChargeCrystal, 1),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.perfect_drill), "ACB", "FDF", "ECE",

                ('E'), ItemStackHelper.fromData(IUItem.adv_spectral_box),

                ('F'), IUItem.overclockerUpgrade1,

                ('A'), ItemStackHelper.fromData(IUItem.spectralaxe, 1),

                ('B'),
                ItemStackHelper.fromData(IUItem.spectralshovel, 1),

                ('D'), ItemStackHelper.fromData(IUItem.spectralpickaxe, 1),

                ('C'), TileGenerationMicrochip.getLevelCircuit(ItemStackHelper.fromData(IUItem.basecircuit, 1, 21), 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ult_vajra), "CAC", "DBD", "", 'A', ItemStackHelper.fromData(IUItem.vajra), 'B',
                ItemStackHelper.fromData(IUItem.perfect_drill), 'C', "c:plates/Nimonic", 'D', "c:plates/SuperalloyRene"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.vajra), "ACB", "FDF", "ECE",

                ('E'), ItemStackHelper.fromData(IUItem.advQuantumtool),

                ('F'), IUItem.overclockerUpgrade1,

                ('A'), ItemStackHelper.fromData(IUItem.spectralaxe, 1),

                ('B'),
                ItemStackHelper.fromData(IUItem.spectralshovel, 1),

                ('D'), ItemStackHelper.fromData(IUItem.spectralpickaxe, 1),

                ('C'), TileGenerationMicrochip.getLevelCircuit(ItemStackHelper.fromData(IUItem.basecircuit, 1, 21), 12)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.quantumSaber), "AB ", "AC ", "DEB",
                ('C'), ItemStackHelper.fromData(IUItem.nanosaber, 1), ('E'), ItemStackHelper.fromData(IUItem.lapotron_crystal, 1),
                ('D'), new ItemStack(Blocks.GLOWSTONE),
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), ('A'), ItemStackHelper.fromData(IUItem.compresscarbon)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spectralSaber), "AB ", "AC ", "DEB",

                ('C'), ItemStackHelper.fromData(IUItem.quantumSaber, 1),

                ('E'), ItemStackHelper.fromData(IUItem.AdvlapotronCrystal, 1),

                ('D'), new ItemStack(Blocks.GLOWSTONE),

                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('A'), ItemStackHelper.fromData(IUItem.compressIridiumplate)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.GraviTool), "ABA", "CDE", "FGF",

                ('G'), ItemStackHelper.fromData(IUItem.purifier, 1),

                ('F'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),

                ('D'), ItemStackHelper.fromData(IUItem.energy_crystal, 1),

                ('E'),
                ItemStackHelper.fromData(IUItem.electric_treetap, 1),

                ('C'), ItemStackHelper.fromData(IUItem.electric_wrench, 1),

                ('B'), ItemStackHelper.fromData(IUItem.electric_hoe
                        .getItem(), 1),
                ('A'),
                ("c:doubleplate/Muntsa")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.purifier), "   ", " B ", "A  ",
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 11), ('B'), "wool"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.itemiu, 1, 3), "MDM", "M M", "MDM",
                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4), ('M'), ItemStackHelper.fromData(
                        IUItem.itemiu,
                        1,
                        1
                )
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.nano_bow),
                "C C",
                "BAB",
                "EDE",
                ('E'),
                IUItem.advnanobox,
                ('D'),
                ItemStackHelper.fromData(IUItem.reBattery, 1),
                ('C'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('B'),
                IUItem.carbonPlate,
                ('A'),
                new ItemStack(Items.BOW)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.quantum_bow), "ABA", "CDC", "EBE",

                ('E'),
                ("c:doubleplate/Alcled"),
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('D'), ItemStackHelper.fromData(IUItem.nano_bow, 1),

                ('A'),
                IUItem.iridiumPlate,

                ('B'), ItemStackHelper.fromData(IUItem.advQuantumtool)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spectral_bow), "ABA", "CDC", "EBE",

                ('E'),
                ("c:doubleplate/Duralumin"),
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('D'), ItemStackHelper.fromData(IUItem.quantum_bow, 1),

                ('A'),
                ItemStackHelper.fromData(IUItem.compressIridiumplate),

                ('B'), ItemStackHelper.fromData(IUItem.adv_spectral_box)
        );
        int[] meta = {3, 4, 0, 1, 6, 7, 8, 9, 10};
        int j;
        for (j = 0; j < 9; j++) {
            Recipes.recipe.addShapelessRecipe(
                    ItemStackHelper.fromData(IUItem.UpgradeKit, 1, j),
                    IUItem.wrench, ItemStackHelper.fromData(IUItem.electricblock, 1, meta[j])
            );
        }
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.solidmatter, 1, 0), "ABA", "CDC", "EBE",

                ('E'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('D'), ItemStackHelper.fromData(IUItem.machines, 1, 3),

                ('C'),
                ("c:plates/Cobalt"),
                ('B'),
                ("c:casings/Electrum"),
                ('A'),
                ("c:casings/Iridium")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.solidmatter, 1, 1), "ABA", "CDC", "EBE",

                ('E'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('D'), ItemStackHelper.fromData(IUItem.machines, 1, 3),

                ('C'),
                ("c:plates/Platinum"),
                ('B'),
                ("c:casings/Magnesium"),
                ('A'),
                ("c:casings/Cobalt")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.solidmatter, 1, 2), "ABA", "CDC", "EBE",

                ('E'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('D'), ItemStackHelper.fromData(IUItem.machines, 1, 3),

                ('C'),
                ("c:doubleplate/Alcled"),
                ('B'),
                ("c:casings/Chromium"),
                ('A'),
                ("c:casings/Mikhail")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.solidmatter, 1, 3), "ABA", "CDC", "EBE",

                ('E'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('D'), ItemStackHelper.fromData(IUItem.machines, 1, 3),

                ('C'),
                ("c:doubleplate/Duralumin"),
                ('B'),
                ("c:casings/Caravky"),
                ('A'),
                ("c:casings/vanady")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.solidmatter, 1, 4), "ABA", "CDC", "EBE",

                ('E'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('D'), ItemStackHelper.fromData(IUItem.machines, 1, 3),

                ('C'),
                ("c:doubleplate/Manganese"),
                ('B'),
                ("c:casings/Spinel"),
                ('A'),
                ("c:casings/Aluminium")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.solidmatter, 1, 5), "ABA", "CDC", "EBE",

                ('E'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('D'), ItemStackHelper.fromData(IUItem.machines, 1, 3),

                ('C'),
                ("c:doubleplate/Titanium"),
                ('B'),
                ("c:casings/Invar"),
                ('A'),
                ("c:casings/Tungsten")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.solidmatter, 1, 6), "ABA", "CDC", "EBE",

                ('E'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('D'), ItemStackHelper.fromData(IUItem.machines, 1, 3),

                ('C'),
                ("c:doubleplate/Redbrass"),
                ('B'),
                ("c:casings/Chromium"),
                ('A'),
                ("c:casings/Manganese")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.solidmatter, 1, 7), "ABA", "CDC", "EBE",

                ('E'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('D'), ItemStackHelper.fromData(IUItem.machines, 1, 3),

                ('C'),
                ("c:doubleplate/Alumel"),
                ('B'),
                ("c:casings/Cobalt"),
                ('A'),
                ("c:casings/vanady")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module_schedule), "ABA", "EDE", "CBC",

                ('D'),
                ("c:ingots/Caravky"),
                ('E'),
                ("c:plates/Zinc"),
                ('C'), ItemStackHelper.fromData(IUItem.plastic_plate),

                ('B'),
                ItemStackHelper.fromData(IUItem.plastic_plate),

                ('A'),
                ("c:plates/vanady")
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricJetpack), "ADA", "ACA", "B B", ('A'), IUItem.casingiron,
                ('B'), Items.GLOWSTONE_DUST, ('C'), ItemStackHelper.fromData(IUItem.electricblock, 1, 2), ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module7, 1, 1), "ABA", "BCB", "DDD",

                ('D'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('C'), ItemStackHelper.fromData(IUItem.advQuantumtool),

                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('A'),
                ItemStackHelper.fromData(IUItem.core, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module7, 1, 2), "ABA", "BCB", "DDD",

                ('D'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('C'), ItemStackHelper.fromData(IUItem.advnanobox),

                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('A'),
                ItemStackHelper.fromData(IUItem.core, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module7, 1, 3), "AAA", "BCB", "EFE",

                ('F'),
                ("c:doubleplate/Alcled"),
                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('C'), ItemStackHelper.fromData(IUItem.module_schedule),

                ('B'),
                ("c:doubleplate/Vitalium"),
                ('A'), ItemStackHelper.fromData(IUItem.compresscarbon)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module7, 1, 4), "AAA", "BCB", "EFE",

                ('F'),
                ("c:doubleplate/Duralumin"),
                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('C'), ItemStackHelper.fromData(IUItem.module_schedule),

                ('B'),
                ("c:doubleplate/Vanadoalumite"),
                ('A'), ItemStackHelper.fromData(IUItem.compressAlloy)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module7, 1, 5), "ABA",
                ('B'), IUItem.module8, ('A'), ItemStackHelper.fromData(IUItem.module7, 1, 3)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module7, 1, 6), "AB",
                ('B'), IUItem.module8, ('A'), ItemStackHelper.fromData(IUItem.module7, 1, 3)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module7, 1, 7), "ABA",
                ('B'), IUItem.module8, ('A'), ItemStackHelper.fromData(IUItem.module7, 1, 8)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module7, 1, 8), "ABA",
                ('B'), ItemStackHelper.fromData(IUItem.module7, 1, 4), ('A'), ItemStackHelper.fromData(IUItem.module7, 1, 6)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9), "BCA", "DED", "BCA",

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('D'),
                ("c:doubleplate/Silver"),
                ('C'),
                ("c:doubleplate/Electrum"),
                ('B'),
                ("c:doubleplate/Redbrass"),
                ('A'),
                ("c:doubleplate/Alcled")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 1), "ABA", "DED", "ABA",

                ('E'), IUItem.overclockerUpgrade1,

                ('D'), ItemStackHelper.fromData(IUItem.core, 1, 1),

                ('B'),
                ("c:doubleplate/Vanadoalumite"),
                ('A'),
                ("c:doubleplate/Alcled")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 2), "ABA", "CEC", "ABA",

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('C'), ItemStackHelper.fromData(IUItem.module9, 1, 1),

                ('B'), ItemStackHelper.fromData(IUItem.nanoBox, 1),

                ('A'),
                ItemStackHelper.fromData(IUItem.core, 1, 2)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 3), "ABA", "CEC", "ABA",

                ('E'), ItemStackHelper.fromData(IUItem.quantumtool, 1),

                ('C'), ItemStackHelper.fromData(IUItem.module9, 1, 2),

                ('B'), ItemStackHelper.fromData(IUItem.photoniy_ingot, 1),

                ('A'),
                ItemStackHelper.fromData(IUItem.core, 1, 4)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 4), "ABA", "CEC", "ABA",

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('C'), ItemStackHelper.fromData(IUItem.module9, 1, 3),

                ('B'), ItemStackHelper.fromData(IUItem.advQuantumtool, 1),

                ('A'),
                ItemStackHelper.fromData(IUItem.core, 1, 6)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 5), "ABA", "BCB", "ABA",

                ('C'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('B'), ItemStackHelper.fromData(IUItem.module9, 1, 4),

                ('A'), ItemStackHelper.fromData(IUItem.core, 1, 7)
        );
        Recipes.recipe.addRecipe(IUItem.module8, "AAA", "BCB", "DED",

                ('E'), ItemStackHelper.fromData(IUItem.compressIridiumplate),

                ('D'),
                ("c:doubleplate/Platinum"),
                ('C'), ItemStackHelper.fromData(IUItem.block, 1, 2),

                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),

                ('A'),
                ("c:plates/Zinc")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module7, 1, 9),
                "DBD",
                "BCB",
                "ABA",

                ('C'),
                ItemStackHelper.fromData(IUItem.purifier, 1),

                ('B'),
                ItemStackHelper.fromData(IUItem.nanoBox),

                ('A'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        543
                )
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 9), "ABA", "BCB", "ABA",

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), ItemStackHelper.fromData(IUItem.photoniy, 1),

                ('A'), ItemStackHelper.fromData(IUItem.core, 1, 3)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 10), "ABA", "BCB", "ABA",

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), ItemStackHelper.fromData(IUItem.photoniy_ingot, 1),

                ('A'), ItemStackHelper.fromData(IUItem.module9, 1, 9)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 11), "ABA", "BCB", "ABA",

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), ItemStackHelper.fromData(IUItem.photoniy_ingot, 1),

                ('A'), ItemStackHelper.fromData(IUItem.module9, 1, 10)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 12), "ABA", "BCB", "ABA",
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9), ('B'), new ItemStack(
                        Blocks.REDSTONE_BLOCK,
                        1
                ), ('A'), new ItemStack(Items.PAPER, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 6), "ABA", "BCB", "ABA",

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('B'), ItemStackHelper.fromData(IUItem.photoniy, 1),

                ('A'), ItemStackHelper.fromData(IUItem.core, 1, 4)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 7), "ABA", "DCD", "ABA",

                ('D'), ItemStackHelper.fromData(IUItem.module9, 1, 6),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), ItemStackHelper.fromData(IUItem.photoniy, 1),

                ('A'),
                ItemStackHelper.fromData(IUItem.core, 1, 4)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 8), "ABA", "DCD", "ABA",

                ('D'), ItemStackHelper.fromData(IUItem.module9, 1, 7),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate, 1),

                ('A'),
                ItemStackHelper.fromData(IUItem.core, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 13), "A A", " C ", "A A",
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9), ('A'), new ItemStack(Items.PAPER, 1)
        );
        Recipes.recipe.addRecipe(IUItem.module1, "AAA", "BCB", "EDE",

                ('E'), ItemStackHelper.fromData(IUItem.plastic_plate),

                ('D'),
                ("c:doubleplate/Vitalium"),
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'),
                ("c:plates/Cobalt"),
                ('A'),
                ("c:plates/Electrum")
        );
        Recipes.recipe.addShapelessRecipe(ItemStackHelper.fromData(IUItem.fertilizer, 16),
                ItemStackHelper.fromData(IUItem.iudust, 1, 69), ItemStackHelper.fromData(IUItem.iudust, 1, 70), ItemStackHelper.fromData(IUItem.iudust, 1, 66)
        );
        Recipes.recipe.addRecipe(IUItem.module2, "AAA", "BCB", "EDE",

                ('E'), ItemStackHelper.fromData(IUItem.plastic_plate),

                ('D'),
                ("c:doubleplate/Vitalium"),
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'),
                ("c:doubleplate/Redbrass"),
                ('A'),
                ("c:doubleplate/Ferromanganese")
        );
        Recipes.recipe.addRecipe(IUItem.module3, "AAA", "BCB", "EDE",

                ('E'), ItemStackHelper.fromData(IUItem.plastic_plate),

                ('D'),
                ("c:doubleplate/Vitalium"),
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'),
                ("c:doubleplate/Alumel"),
                ('A'),
                ("c:plates/Ferromanganese")
        );
        Recipes.recipe.addRecipe(IUItem.module4, "AAA", "BCB", "EDE",

                ('E'), ItemStackHelper.fromData(IUItem.plastic_plate),

                ('D'),
                ("c:doubleplate/Vitalium"),
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'),
                ("c:doubleplate/Muntsa"),
                ('A'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate, 1)
        );
        for (j = 0; j < 7; j++) {
            Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module5, 1, j), "BBB", "ACA", "ADA",

                    ('A'), ItemStackHelper.fromData(IUItem.lens, 1, j),

                    ('B'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                    ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                    ('D'),
                    ItemStackHelper.fromData(IUItem.advQuantumtool)
            );
        }
        for (j = 0; j < 14; j++) {
            ItemStack stack1 = ItemStackHelper.fromData(IUItem.module6, 1, j);
            Recipes.recipe.addShapelessRecipe(stack1, ItemStackHelper.fromData(IUItem.blockpanel, 1, j));
        }

        for (int i = 0; i < RegisterOreDictionary.list_string.size(); i++) {
            if (i < 16) {
                Recipes.recipe.addShapelessRecipe(
                        ItemStackHelper.fromData(IUItem.iuingot, 9, i),
                        ItemStackHelper.fromData(IUItem.block, 1, i)
                );
            } else {
                Recipes.recipe.addShapelessRecipe(
                        ItemStackHelper.fromData(IUItem.iuingot, 9, i),
                        ItemStackHelper.fromData(IUItem.block1, 1, i - 16)
                );

            }
            Recipes.recipe.addShapelessRecipe(
                    ItemStackHelper.fromData(IUItem.nugget, 9, i),
                    ItemStackHelper.fromData(IUItem.iuingot, 1, i)
            );
            if (i < 16) {
                Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.block, 1, i),
                        "AAA", "AAA", "AAA", 'A', "c:ingots/" + RegisterOreDictionary.list_string.get(i)
                );
            } else {
                Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.block1, 1, i - 16),
                        "AAA", "AAA", "AAA", 'A', "c:ingots/" + RegisterOreDictionary.list_string.get(i)
                );

            }
            Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.iuingot, 1, i),
                    "AAA", "AAA", "AAA", 'A', ItemStackHelper.fromData(IUItem.nugget, 1, i)
            );


        }

        Recipes.recipe.addShapelessRecipe(
                ItemStackHelper.fromData(IUItem.iuingot, 9, 25),
                ItemStackHelper.fromData(IUItem.block1, 1, 3)
        );
        Recipes.recipe.addShapelessRecipe(
                ItemStackHelper.fromData(IUItem.nugget, 9, 19),
                ItemStackHelper.fromData(IUItem.iuingot, 1, 25)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.iuingot, 1, 25),
                "AAA", "AAA", "AAA", 'A', ItemStackHelper.fromData(IUItem.nugget, 1, 19)
        );


        Recipes.recipe.addShapelessRecipe(
                ItemStackHelper.fromData(IUItem.iuingot, 9, 26),
                ItemStackHelper.fromData(IUItem.block1, 1, 4)
        );
        Recipes.recipe.addShapelessRecipe(
                ItemStackHelper.fromData(IUItem.nugget, 9, 20),
                ItemStackHelper.fromData(IUItem.iuingot, 1, 26)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.iuingot, 1, 26),
                "AAA", "AAA", "AAA", 'A', ItemStackHelper.fromData(IUItem.nugget, 1, 20)
        );


        Recipes.recipe.addShapelessRecipe(
                ItemStackHelper.fromData(IUItem.iuingot, 9, 27),
                ItemStackHelper.fromData(IUItem.block1, 1, 5)
        );
        Recipes.recipe.addShapelessRecipe(
                ItemStackHelper.fromData(IUItem.nugget, 9, 21),
                ItemStackHelper.fromData(IUItem.iuingot, 1, 27)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.iuingot, 1, 27),
                "AAA", "AAA", "AAA", 'A', ItemStackHelper.fromData(IUItem.nugget, 1, 21)
        );

        for (j = 0; j < RegisterOreDictionary.list_string1.size(); j++) {
            if (j >= 16) {
                Recipes.recipe.addShapelessRecipe(
                        ItemStackHelper.fromData(IUItem.alloysingot, 9, j),
                        ItemStackHelper.fromData(IUItem.alloysblock1, 1, j - 16)
                );
                Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.alloysblock1, 1, j - 16), "AAA", "AAA", "AAA",
                        ('A'), ("c:ingots/" + RegisterOreDictionary.list_string1.get(j))
                );
            } else {
                Recipes.recipe.addShapelessRecipe(
                        ItemStackHelper.fromData(IUItem.alloysingot, 9, j),
                        ItemStackHelper.fromData(IUItem.alloysblock, 1, j)
                );
                Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.alloysblock, 1, j), "AAA", "AAA", "AAA",
                        ('A'), ("c:ingots/" + RegisterOreDictionary.list_string1.get(j))
                );
            }
            Recipes.recipe.addShapelessRecipe(
                    ItemStackHelper.fromData(IUItem.alloysnugget, 9, j),
                    ItemStackHelper.fromData(IUItem.alloysingot, 1, j)
            );

            Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.alloysingot, 1, j), "AAA", "AAA", "AAA",
                    ('A'), ItemStackHelper.fromData(IUItem.alloysnugget, 1, j)
            );
        }

        for (j = 0; j < RegisterOreDictionary.list_baseore1.size(); j++) {

            Recipes.recipe.addShapelessRecipe(
                    ItemStackHelper.fromData(IUItem.iuingot, 9, j + 28),
                    ItemStackHelper.fromData(IUItem.block2, 1, j)
            );
            Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.block2, 1, j), "AAA", "AAA", "AAA",
                    ('A'), ("c:ingots/" + RegisterOreDictionary.list_baseore1.get(j))
            );

            Recipes.recipe.addShapelessRecipe(
                    ItemStackHelper.fromData(IUItem.nugget, 9, j + 22),
                    ItemStackHelper.fromData(IUItem.iuingot, 1, j + 28)
            );

            Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.iuingot, 1, j + 28), "AAA", "AAA", "AAA",
                    ('A'), ItemStackHelper.fromData(IUItem.nugget, 1, j + 22)
            );


        }
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.canister), "BBA", " AA", " AA",
                ('A'), "c:plates/Chromium", 'B', "c:plates/Titanium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cable, 1, 0), " A ", "BBB", " C ",
                ('A'), IUItem.glassFiberCableItem, ('B'), ItemStackHelper.fromData(IUItem.itemiu, 1, 0), 'C',
                ItemStackHelper.fromData(IUItem.synthetic_rubber)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cable, 1, 1), " A ", "BCB", " A ",

                ('C'),
                ("c:ingots/Cobalt"),
                ('A'), ItemStackHelper.fromData(IUItem.cable, 1, 0),

                ('B'), IUItem.denseplatetin
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cable, 1, 2), " A ", "BCB", " A ",

                ('C'), IUItem.denseplatetin,

                ('A'), ItemStackHelper.fromData(IUItem.cable, 1, 1),

                ('B'), IUItem.advancedAlloy
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cable, 1, 3), "DAD", "BCB", "DAD",

                ('D'), IUItem.denseplategold,

                ('C'), IUItem.advancedAlloy,

                ('A'), ItemStackHelper.fromData(IUItem.cable, 1, 2),

                ('B'),
                IUItem.denseplatelead
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cable, 1, 4), "DAD", "BCB", "DAD",

                ('D'),
                ("c:ingots/Redbrass"),
                ('C'), IUItem.carbonPlate,

                ('A'), ItemStackHelper.fromData(IUItem.cable, 1, 3),

                ('B'),
                ("c:ingots/Spinel")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cable, 1, 5), " A ", "BCB", " A ",

                ('C'),
                ("c:doubleplate/Vitalium"),
                ('A'), ItemStackHelper.fromData(IUItem.cable, 1, 4),

                ('B'), IUItem.denseplateadviron
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cable, 1, 6), "DAD", "BCB", "DAD",

                ('D'), IUItem.carbonPlate,

                ('C'),
                ("c:ingots/Alcled"),
                ('A'), ItemStackHelper.fromData(IUItem.cable, 1, 5),

                ('B'),
                ("c:ingots/Duralumin")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cable, 1, 7), "A A", "BCB", "A A",

                ('C'), ItemStackHelper.fromData(IUItem.photoniy_ingot),

                ('B'), ItemStackHelper.fromData(IUItem.photoniy),

                ('A'), ItemStackHelper.fromData(IUItem.cable, 1, 6)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cable, 1, 8), "BBB", "AAA", "BBB",
                ('A'), ItemStackHelper.fromData(IUItem.photoniy_ingot), ('B'), ItemStackHelper.fromData(IUItem.cable, 1, 7)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cable, 1, 9), "BBB", "ACA", "BBB",

                ('C'), ItemStackHelper.fromData(IUItem.basecircuit, 1, 10),

                ('A'), ItemStackHelper.fromData(IUItem.photoniy_ingot),

                ('B'), ItemStackHelper.fromData(IUItem.cable, 1, 8)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basecircuit, 1, 15), "BAB", "ADA", "CAC",

                ('D'), new ItemStack(Items.FLINT),

                ('C'), new ItemStack(Items.REDSTONE),

                ('A'),
                ("c:plates/Iron"),
                ('B'),
                new ItemStack(Items.GOLD_INGOT)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basecircuit, 1, 16), "BBB", "BAB", "BBB",
                ('B'), ItemStackHelper.fromData(IUItem.stik, 1, 15), ('A'), ItemStackHelper.fromData(IUItem.basecircuit, 1, 15)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basecircuit, 1, 17), "CCC", "A A", "DDD",
                ('D'), ("c:plates/Silver"), ('C'), IUItem.insulatedCopperCableItem,

                ('A'), ItemStackHelper.fromData(IUItem.basecircuit, 1, 15)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cable, 1, 10), "BBB", "ACA", "BBB",
                ('C'), ("c:doubleplate/Vanadoalumite"), ('A'), ItemStackHelper.fromData(IUItem.basecircuit, 1, 11),
                ('B'), ItemStackHelper.fromData(IUItem.cable, 1, 9)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 27), " D ", "ABA", " D ",
                ('D'), ("c:plates/Germanium"), ('B'), new ItemStack(Blocks.CHEST),
                ('A'), ItemStackHelper.fromData(IUItem.quantumtool)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_lappack), "ABA", "CEC", "ADA",

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('D'), ItemStackHelper.fromData(IUItem.lapotron_crystal, 1),

                ('C'),
                ("c:plates/Vanadoalumite"),
                ('B'),
                ItemStackHelper.fromData(IUItem.lappack, 1),

                ('A'),
                ("c:plates/Alcled")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.imp_lappack), "ABA", "CEC", "ABA",

                ('E'), ItemStackHelper.fromData(IUItem.adv_lappack, 1),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),

                ('B'), ItemStackHelper.fromData(IUItem.AdvlapotronCrystal, 1),

                ('A'),
                ("c:doubleplate/Ferromanganese")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.per_lappack), "ABA", "CEC", "ABA",

                ('E'), ItemStackHelper.fromData(IUItem.imp_lappack, 1),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'), ItemStackHelper.fromData(IUItem.compressIridiumplate, 1),

                ('A'),
                ("c:doubleplate/Vitalium")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.advancedSolarHelmet), " A ", "BCB", "DED",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('E'), ItemStackHelper.fromData(IUItem.compressAlloy),

                ('C'), ItemStackHelper.fromData(IUItem.adv_nano_helmet, 1),

                ('B'),
                ItemStackHelper.fromData(IUItem.compresscarbon),

                ('A'), ItemStackHelper.fromData(IUItem.blockpanel)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.hybridSolarHelmet), " A ", "BCB", "DED",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('E'), ItemStackHelper.fromData(IUItem.compressAlloy),

                ('C'), ItemStackHelper.fromData(IUItem.quantum_helmet, 1),

                ('B'),
                IUItem.iridiumPlate,

                ('A'), ItemStackHelper.fromData(IUItem.blockpanel, 1, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ultimateSolarHelmet), " A ", "DCD", "BEB",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('E'), ItemStackHelper.fromData(IUItem.compressAlloy),

                ('C'), ItemStackHelper.fromData(IUItem.hybridSolarHelmet, 1),

                ('B'),
                IUItem.iridiumPlate,

                ('A'), ItemStackHelper.fromData(IUItem.blockpanel, 1, 2)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 0), " A ", "ABA", " A ",

                ('A'), "logs",

                ('B'), "planks"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 1), " A ", "ABA", " A ",

                ('A'), "c:storage_blocks/Bronze",

                ('B'), ItemStackHelper.fromData(IUItem.rotor_wood, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 2), " A ", "ABA", " A ",

                ('A'), "c:storage_blocks/Iron",

                ('B'), ItemStackHelper.fromData(IUItem.rotor_bronze, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 3), " A ", "ABA", " A ",

                ('A'), "c:plates/Steel",

                ('B'), ItemStackHelper.fromData(IUItem.rotor_iron, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 4), " A ", "ABA", " A ",

                ('A'), "c:plates/Carbon",

                ('B'), ItemStackHelper.fromData(IUItem.rotor_steel, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 5), "CAC", "ABA", "CAC",

                ('C'), "c:doubleplate/Iridium",

                ('A'), IUItem.iridiumPlate,

                ('B'), ItemStackHelper.fromData(IUItem.rotor_carbon, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 6), "CAC", "ABA", "CAC",

                ('C'), IUItem.compressIridiumplate,

                ('A'), ItemStackHelper.fromData(IUItem.compresscarbon),

                ('B'), ItemStackHelper.fromData(IUItem.iridium
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 7), "DCD", "ABA", "DCD",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('C'), ItemStackHelper.fromData(IUItem.compressIridiumplate),

                ('A'), ItemStackHelper.fromData(IUItem.advnanobox),

                ('B'),
                ItemStackHelper.fromData(IUItem.compressiridium
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 8), "DCD", "ABA", " C ",

                ('D'), ItemStackHelper.fromData(IUItem.excitednucleus, 1, 5),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('A'), ItemStackHelper.fromData(IUItem.quantumtool),

                ('B'),
                ItemStackHelper.fromData(IUItem.spectral
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 10), "DCD", "CBC", " C ",

                ('D'), ItemStackHelper.fromData(IUItem.excitednucleus, 1, 6),

                ('C'), ItemStackHelper.fromData(IUItem.quantumtool),

                ('B'), ItemStackHelper.fromData(IUItem.myphical
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 9), "DCD", "CBC", "ACA",

                ('D'), ItemStackHelper.fromData(IUItem.neutroniumingot),

                ('A'), IUItem.iridiumPlate,

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('B'),
                ItemStackHelper.fromData(IUItem.photon
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 11), "DCD", "CBC", " C ",

                ('D'), ItemStackHelper.fromData(IUItem.excitednucleus, 1, 5),

                ('C'), ItemStackHelper.fromData(IUItem.advQuantumtool),

                ('B'), ItemStackHelper.fromData(IUItem.neutron
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 12), "ACA", "CBC", "ACA",

                ('A'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), ItemStackHelper.fromData(IUItem.barionrotor
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 13), "ECE", "CBC", "ACA",

                ('E'), TileGenerationMicrochip.getLevelCircuit(ItemStackHelper.fromData(IUItem.basecircuit, 1, 21), 11),

                ('A'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('C'), ItemStackHelper.fromData(IUItem.photoniy_ingot),

                ('B'),
                ItemStackHelper.fromData(IUItem.adronrotor
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spectralSolarHelmet), " A ", "DCD", "BEB",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('E'), ItemStackHelper.fromData(IUItem.compressAlloy),

                ('C'), ItemStackHelper.fromData(IUItem.ultimateSolarHelmet, 1),

                ('B'),
                ItemStackHelper.fromData(IUItem.doublecompressIridiumplate, 1),

                ('A'), ItemStackHelper.fromData(IUItem.blockpanel, 1, 4)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.singularSolarHelmet), " A ", "DCD", "BDB",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('E'), ItemStackHelper.fromData(IUItem.compressAlloy),

                ('C'), ItemStackHelper.fromData(IUItem.spectralSolarHelmet, 1),

                ('B'),
                ItemStackHelper.fromData(IUItem.doublecompressIridiumplate, 1),

                ('A'), ItemStackHelper.fromData(IUItem.blockpanel, 1, 6)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines, 1, 5), "BCB", "DAD", "BCB",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('C'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('B'), ItemStackHelper.fromData(IUItem.photoniy_ingot),

                ('A'),
                ItemStackHelper.fromData(IUItem.machines, 1, 3)
        );
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines, 1, 1),
                "DED",
                "BCB",
                "AAA",

                ('E'),
                ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('D'),
                ("c:doubleplate/Alumel"),
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('C'),
                ItemStackHelper.fromData(IUItem.simplemachine, 1, 6),

                ('A'),
                ItemStackHelper.fromData(IUItem.quantumtool)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines, 1, 2),
                "DED",
                "BCB",
                "AAA",

                ('E'),
                ItemStackHelper.fromData(IUItem.core, 1, 7),

                ('D'),
                ("c:doubleplate/Vitalium"),
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('C'),
                ItemStackHelper.fromData(IUItem.machines, 1, 1),

                ('A'),
                ItemStackHelper.fromData(IUItem.advQuantumtool)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines, 1, 3),
                "DED",
                "BCB",
                "AFA",

                ('F'),
                ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('E'),
                ItemStackHelper.fromData(IUItem.core, 1, 8),

                ('D'),
                ("c:doubleplate/Duralumin"),
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('C'),
                ItemStackHelper.fromData(IUItem.machines, 1, 2),

                ('A'),
                ItemStackHelper.fromData(IUItem.advQuantumtool)
        ), 2);
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.pho_machine, 1, 12),
                "DED",
                "BCB",
                "AFA",

                ('F'),
                ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('E'),
                ItemStackHelper.fromData(IUItem.core, 1, 10),

                ('D'),
                ("c:doubleplate/Duralumin"),
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(ItemStackHelper.fromData(IUItem.basecircuit, 1, 21), 11),

                ('C'),
                ItemStackHelper.fromData(IUItem.machines, 1, 3),

                ('A'),
                ItemStackHelper.fromData(IUItem.adv_spectral_box)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blocksintezator), "ABA", "BCB", "ABA",

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('A'), IUItem.advancedMachine,

                ('B'), ItemStackHelper.fromData(IUItem.core, 1, 8)
        );
        int[] meta2 = {
                2, 5, 3, 4, 0, 1, 6, 7, 8, 9,
                10};
        int[] meta3 = {
                2, 3, 4, 5, 0, 1, 6, 7, 8, 9,
                10};
        ItemStack[] stacks3 = {
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),
                TileGenerationMicrochip.getLevelCircuit(ItemStackHelper.fromData(IUItem.basecircuit, 1, 21), 11)};
        int k;
        for (k = 0; k < 11; k++) {
            Recipes.recipe.addRecipe(
                    ItemStackHelper.fromData(IUItem.chargepadelectricblock, 1, meta3[k]),
                    "ABA",
                    "CDC",
                    ('B'),
                    Blocks.STONE_PRESSURE_PLATE,
                    ('A'),
                    stacks3[k],
                    ('D'),
                    ItemStackHelper.fromData(IUItem.electricblock, 1, meta2[k]),
                    ('C'),
                    IUItem.rubber
            );
        }
        stacks3 = new ItemStack[]{TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),
                TileGenerationMicrochip.getLevelCircuit(ItemStackHelper.fromData(IUItem.basecircuit, 1, 21), 11)};
        for (k = 0; k < 7; k++) {
            if (k < 3) {
                if (k == 0) {
                    Recipes.recipe.addShapelessRecipe(
                            ItemStackHelper.fromData(IUItem.tranformer, 1, k),
                            ItemStackHelper.fromData(IUItem.tranformer, 1, 10), ItemStackHelper.fromData(
                                    IUItem.crafting_elements,
                                    1,
                                    210
                            )
                    );
                    Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 210), " A ", "BCD", " A ",

                            ('D'), ItemStackHelper.fromData(IUItem.lapotron_crystal, 1),

                            ('C'), DEFAULT_SENSOR,

                            ('B'), stacks3[k],

                            ('A'),
                            ItemStackHelper.fromData(IUItem.cable, 1, k)
                    );
                } else {
                    Recipes.recipe.addShapelessRecipe(
                            ItemStackHelper.fromData(IUItem.tranformer, 1, k),
                            ItemStackHelper.fromData(
                                    IUItem.tranformer,
                                    1,
                                    k - 1
                            ), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 210 + k)
                    );
                    Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 210 + k), " A ", "BCD", " A ",

                            ('D'), ItemStackHelper.fromData(IUItem.AdvlapotronCrystal, 1),

                            ('C'), DEFAULT_SENSOR,

                            ('B'), stacks3[k],

                            ('A'),
                            ItemStackHelper.fromData(IUItem.cable, 1, k)
                    );
                }
            } else {
                Recipes.recipe.addShapelessRecipe(
                        ItemStackHelper.fromData(IUItem.tranformer, 1, k),
                        ItemStackHelper.fromData(IUItem.tranformer, 1, k - 1), ItemStackHelper.fromData(
                                IUItem.crafting_elements,
                                1,
                                210 + k
                        )
                );
                Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 210 + k), " A ", "BCD", " A ",

                        ('D'), ItemStackHelper.fromData(IUItem.AdvlapotronCrystal, 1),

                        ('C'), DEFAULT_SENSOR,

                        ('B'), stacks3[k],

                        ('A'),
                        ItemStackHelper.fromData(IUItem.cable, 1, k)
                );
            }
        }
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines, 1, 9), "ACA", "BEB", "ADA",

                ('E'), IUItem.advancedMachine,

                ('D'), ItemStackHelper.fromData(IUItem.module9, 1, 13),

                ('C'), ItemStackHelper.fromData(IUItem.module9, 1, 12),

                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('A'), ItemStackHelper.fromData(IUItem.core, 1, 4)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.photonglass, 1, 0), "CCC", "ABA", "CCC",
                ('B'), ItemStackHelper.fromData(IUItem.itemiu, 1, 1), ('C'), ItemStackHelper.fromData(
                        IUItem.stik,
                        1,
                        0
                ), ('A'), ItemStackHelper.fromData(IUItem.sunnarium, 1, 2)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.photonglass, 1, 1), "CCC", "ABA", "CCC",

                ('B'), ItemStackHelper.fromData(IUItem.photonglass, 1, 0),

                ('C'), ItemStackHelper.fromData(IUItem.stik, 1, 6),

                ('A'), ItemStackHelper.fromData(IUItem.sunnariumpanel, 1, 0)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.photonglass, 1, 2), "CCC", "ABA", "CCC",

                ('B'), ItemStackHelper.fromData(IUItem.photonglass, 1, 1),

                ('C'), ItemStackHelper.fromData(IUItem.stik, 1, 9),

                ('A'), ItemStackHelper.fromData(IUItem.sunnariumpanel, 1, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.photonglass, 1, 3), "CCC", "ABA", "CCC",

                ('B'), ItemStackHelper.fromData(IUItem.photonglass, 1, 2),

                ('C'), ItemStackHelper.fromData(IUItem.stik, 1, 11),

                ('A'), ItemStackHelper.fromData(IUItem.sunnariumpanel, 1, 2)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.photonglass, 1, 4), "CCC", "ABA", "CCC",

                ('B'), ItemStackHelper.fromData(IUItem.photonglass, 1, 3),

                ('C'), ItemStackHelper.fromData(IUItem.stik, 1, 13),

                ('A'), ItemStackHelper.fromData(IUItem.sunnariumpanel, 1, 3)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.photonglass, 1, 5), "CEC", "ABA", "CEC",

                ('E'), ItemStackHelper.fromData(IUItem.proton),

                ('B'), ItemStackHelper.fromData(IUItem.photonglass, 1, 4),

                ('C'), ItemStackHelper.fromData(IUItem.stik, 1, 16),

                ('A'),
                ItemStackHelper.fromData(IUItem.sunnariumpanel, 1, 4)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.photonglass, 1, 6), "CCC", "ABA", "CCC",

                ('B'), ItemStackHelper.fromData(IUItem.photonglass, 1, 5),

                ('C'), ItemStackHelper.fromData(IUItem.stik, 1, 4),

                ('A'), ItemStackHelper.fromData(IUItem.sunnariumpanel, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.photonglass, 1, 7), "CCC", "ABA", "CCC",

                ('B'), ItemStackHelper.fromData(IUItem.photonglass, 1, 6),

                ('C'), ItemStackHelper.fromData(IUItem.stik, 1, 12),

                ('A'), ItemStackHelper.fromData(IUItem.sunnariumpanel, 1, 6)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.photonglass, 1, 8), "CCC", "ABA", "CCC",

                ('B'), ItemStackHelper.fromData(IUItem.photonglass, 1, 7),

                ('C'), ItemStackHelper.fromData(IUItem.stik, 1, 11),

                ('A'), ItemStackHelper.fromData(IUItem.sunnariumpanel, 1, 7)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.photonglass, 1, 9), "CDC", "ABA", "CDC",

                ('D'), ItemStackHelper.fromData(IUItem.neutroniumingot, 1),

                ('B'), ItemStackHelper.fromData(IUItem.photonglass, 1, 8),

                ('C'), ItemStackHelper.fromData(IUItem.stik, 1, 18),

                ('A'),
                ItemStackHelper.fromData(IUItem.sunnariumpanel, 1, 8)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.photonglass, 1, 10), "CCC", "ABA", "CCC",

                ('B'), ItemStackHelper.fromData(IUItem.photonglass, 1, 9),

                ('C'), ItemStackHelper.fromData(IUItem.stik, 1, 15),

                ('A'), ItemStackHelper.fromData(IUItem.sunnariumpanel, 1, 9)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.photonglass, 1, 11), "CCC", "ABA", "CCC",

                ('B'), ItemStackHelper.fromData(IUItem.photonglass, 1, 10),

                ('C'), ItemStackHelper.fromData(IUItem.stik, 1, 2),

                ('A'), ItemStackHelper.fromData(IUItem.sunnariumpanel, 1, 10)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.photonglass, 1, 12), "CCC", "ABA", "CCC",

                ('B'), ItemStackHelper.fromData(IUItem.photonglass, 1, 11),

                ('C'), ItemStackHelper.fromData(IUItem.stik, 1, 6),

                ('A'), ItemStackHelper.fromData(IUItem.sunnariumpanel, 1, 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.photonglass, 1, 13), "CCC", "ABA", "CCC",

                ('B'), ItemStackHelper.fromData(IUItem.photonglass, 1, 12),

                ('C'), ItemStackHelper.fromData(IUItem.stik, 1, 5),

                ('A'), ItemStackHelper.fromData(IUItem.sunnariumpanel, 1, 12)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basecircuit, 1, 6), "CCC", "ABA", "DDD",

                ('D'),
                ("c:plates/Bronze"),
                ('C'), IUItem.carbonPlate,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 543),

                ('A'),
                ItemStackHelper.fromData(IUItem.basecircuit, 1, 0)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basecircuit, 1, 7), "CCC", "ABA", "DDD",
                ('D'), ("c:plates/Steel"), ('C'), IUItem.carbonPlate, ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 549),

                ('A'),
                ItemStackHelper.fromData(IUItem.basecircuit, 1, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basecircuit, 1, 8), "CCC", "ABA", "DDD",

                ('D'),
                ("c:plates/Spinel"),
                ('C'), IUItem.carbonPlate,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 555),

                ('A'),
                ItemStackHelper.fromData(IUItem.basecircuit, 1, 2)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basecircuit, 1, 20), "CCC", "ABA", "DDD",

                ('D'),
                ("c:plates/SuperalloyRene"),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 479),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 558),

                ('A'),
                ItemStackHelper.fromData(IUItem.basecircuit, 1, 18)
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basecircuit, 1, 3), "BBB", "BAB", "BBB",
                ('B'), ItemStackHelper.fromData(IUItem.stik, 1, 10), ('A'), ItemStackHelper.fromData(IUItem.basecircuit, 1, 0)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basecircuit, 1, 4), "BBB", "BAB", "BBB",
                ('B'), ItemStackHelper.fromData(IUItem.stik, 1, 14), ('A'), ItemStackHelper.fromData(IUItem.basecircuit, 1, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basecircuit, 1, 5), "BBB", "BAB", "BBB",
                ('B'), ItemStackHelper.fromData(IUItem.stik, 1, 16), ('A'), ItemStackHelper.fromData(IUItem.basecircuit, 1, 2)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basecircuit, 1, 13), "BBB", "BAB", "BBB",
                ('B'), ItemStackHelper.fromData(IUItem.stik, 1, 6), ('A'), ItemStackHelper.fromData(IUItem.basecircuit, 1, 12)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basecircuit, 1, 14), "CCC", "ABA", "DDD",

                ('D'),
                ("c:plates/Platinum"),
                ('C'), IUItem.carbonPlate,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 533),

                ('A'),
                ItemStackHelper.fromData(IUItem.basecircuit, 1, 12)
        );
        ItemStack[] circuit = {
                ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        30
                ),
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                ItemStackHelper.fromData(IUItem.excitednucleus, 1, 1),
                ItemStackHelper.fromData(IUItem.excitednucleus, 1, 2),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),
                ItemStackHelper.fromData(IUItem.excitednucleus, 1, 4),
                ItemStackHelper.fromData(IUItem.excitednucleus, 1, 5),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),
                ItemStackHelper.fromData(IUItem.excitednucleus, 1, 7),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                ItemStackHelper.fromData(IUItem.excitednucleus, 1, 9),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),
                ItemStackHelper.fromData(IUItem.excitednucleus, 1, 11),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                ItemStackHelper.fromData(IUItem.excitednucleus, 1, 12),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8)};
        ItemStack[] iridium = {
                ItemStackHelper.fromData(IUItem.sunnarium, 1, 1), IUItem.iridiumOre, IUItem.iridiumPlate, IUItem.iridiumPlate, ItemStackHelper.fromData(
                IUItem.compressIridiumplate), ItemStackHelper.fromData(IUItem.compressIridiumplate), ItemStackHelper.fromData(IUItem.compressIridiumplate), ItemStackHelper.fromData(
                IUItem.doublecompressIridiumplate), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),
                ItemStackHelper.fromData(IUItem.doublecompressIridiumplate), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate), ItemStackHelper.fromData(
                IUItem.doublecompressIridiumplate), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate)};
        int m;
        for (m = 0; m < 14; m++) {
            if (m != 0) {
                Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blockpanel, 1, m), "ABA", "CDC", "DED",

                        ('A'), ItemStackHelper.fromData(IUItem.solar_night_day_glass, 1, m),

                        ('B'), ItemStackHelper.fromData(IUItem.excitednucleus, 1, m),

                        ('C'), iridium[m],

                        ('D'),
                        ItemStackHelper.fromData(IUItem.blockpanel, 1, m - 1),

                        ('E'), circuit[m]
                );
                Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.upgradepanelkit, 1, m), "ABA", "C C", "DED",

                        ('A'), ItemStackHelper.fromData(IUItem.solar_night_day_glass, 1, m),

                        ('B'), ItemStackHelper.fromData(IUItem.excitednucleus, 1, m),

                        ('C'), iridium[m],

                        ('D'),
                        ItemStackHelper.fromData(IUItem.blockpanel, 1, m - 1),

                        ('E'), circuit[m]
                );
            } else {
                Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blockpanel, 1, m), "ABA", "CDC", "DED",

                        ('A'), ItemStackHelper.fromData(IUItem.solar_night_day_glass, 1, m),

                        ('B'), ItemStackHelper.fromData(IUItem.excitednucleus, 1, m),

                        ('C'), iridium[m],

                        ('D'),
                        getBlockStack(BlockBaseMachine3.solar_iu),
                        ('E'), circuit[m]
                );
                Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.upgradepanelkit, 1, m), "ABA", "C C", "DED",

                        ('A'), ItemStackHelper.fromData(IUItem.solar_night_day_glass, 1, m),

                        ('B'), ItemStackHelper.fromData(IUItem.excitednucleus, 1, m),

                        ('C'), iridium[m],

                        ('D'),
                        getBlockStack(BlockBaseMachine3.solar_iu),
                        ('E'), circuit[m]
                );
            }
        }
        for (m = 0; m < 3; m++) {
            Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.preciousblock, 1, m), "AAA", "AAA", "AAA",
                    ('A'), ItemStackHelper.fromData(IUItem.preciousgem, 1, m)
            );
            Recipes.recipe.addShapelessRecipe(
                    ItemStackHelper.fromData(IUItem.preciousgem, 9, m),
                    ItemStackHelper.fromData(IUItem.preciousblock, 1, m)
            );
        }
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module7), " C ", "BAB", "DCD",

                ('D'),
                ("c:plates/Caravky"),
                ('C'),
                ("c:plates/Electrum"),
                ('B'),
                ("c:plates/Invar"),
                ('A'),
                ItemStackHelper.fromData(IUItem.module_schedule)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.paints), "AAA", "A A", "AAA",
                ('A'), new ItemStack(Blocks.GLASS)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.paints, 1, 1),
                "AB ", ('A'), ItemStackHelper.fromData(IUItem.paints), ('B'), new ItemStack(Items.LIGHT_BLUE_DYE)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.paints, 1, 2), "AB ",

                ('A'), ItemStackHelper.fromData(IUItem.paints),

                ('B'), new ItemStack(Items.YELLOW_DYE)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.paints, 1, 3), "AB ",

                ('A'), ItemStackHelper.fromData(IUItem.paints),

                ('B'), new ItemStack(Items.GREEN_DYE)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.paints, 1, 4), "AB ",

                ('A'), ItemStackHelper.fromData(IUItem.paints),

                ('B'), new ItemStack(Items.RED_DYE)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.paints, 1, 5), "AB ",

                ('A'), ItemStackHelper.fromData(IUItem.paints),

                ('B'), new ItemStack(Items.ORANGE_DYE)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.paints, 1, 6), "AB ",

                ('A'), ItemStackHelper.fromData(IUItem.paints),

                ('B'), new ItemStack(Items.BLUE_DYE)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.paints, 1, 7), "AB ",

                ('A'), ItemStackHelper.fromData(IUItem.paints),

                ('B'), new ItemStack(Items.PURPLE_DYE)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.paints, 1, 8), "AB ",

                ('A'), ItemStackHelper.fromData(IUItem.paints),

                ('B'), new ItemStack(Items.CYAN_DYE)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.paints, 1, 9), "AB ",

                ('A'), ItemStackHelper.fromData(IUItem.paints),

                ('B'), new ItemStack(Items.WHITE_DYE)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.paints, 1, 10), "AB ",

                ('A'), ItemStackHelper.fromData(IUItem.paints),

                ('B'), new ItemStack(Items.LIGHT_GRAY_DYE)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.paints, 1, 11), "AB ",

                ('A'), ItemStackHelper.fromData(IUItem.paints),

                ('B'), new ItemStack(Items.LIME_DYE)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.quarrymodule), "BAB", "DCD",

                ('D'),
                ("c:ingots/Germanium"),
                ('C'), ItemStackHelper.fromData(IUItem.module_schedule),

                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 158)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.analyzermodule), "BAB", "DCD",

                ('D'),
                ("c:ingots/Germanium"),
                ('C'), ItemStackHelper.fromData(IUItem.module_schedule),

                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('A'), ItemStackHelper.fromData(IUItem.basemachine1, 1, 2)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machinekit, 1, 0), "ABA", "D D", "EEE",

                ('E'), ItemStackHelper.fromData(IUItem.nanoBox),

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('B'),
                ("c:doubleplate/Aluminium"),
                ('A'),
                ("c:doubleplate/Alumel")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machinekit, 1, 1), "ABA", "D D", "EEE",

                ('E'), ItemStackHelper.fromData(IUItem.quantumtool),

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('B'),
                ("c:doubleplate/Platinum"),
                ('A'),
                ("c:doubleplate/Vitalium")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machinekit, 1, 2), "ABA", "D D", "EEE",

                ('E'), ItemStackHelper.fromData(IUItem.advQuantumtool),

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('B'),
                ("c:doubleplate/Spinel"),
                ('A'),
                ("c:doubleplate/Manganese")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.hazmathelmet), "AAA", "BCB", "DDD",

                ('A'),
                ("c:plates/Mikhail"),
                ('B'),
                ("c:plates/Platinum"),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 0),
                ('C'),
                ItemStackHelper.fromData(IUItem.hazmat_helmet, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.hazmatchest), "AAA", "BCB", "DDD",

                ('A'),
                ("c:plates/Mikhail"),
                ('B'),
                ("c:plates/Platinum"),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 0),
                ('C'),
                ItemStackHelper.fromData(IUItem.hazmat_chestplate, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.hazmatleggins), "AAA", "BCB", "DDD",

                ('A'),
                ("c:plates/Mikhail"),
                ('B'),
                ("c:plates/Platinum"),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 0),
                ('C'),
                ItemStackHelper.fromData(IUItem.hazmat_leggings, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.hazmatboosts), "AAA", "BCB", "DDD",

                ('A'),
                ("c:plates/Mikhail"),
                ('B'),
                ("c:plates/Platinum"),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 0),
                ('C'),
                ItemStackHelper.fromData(IUItem.rubber_boots, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.entitymodules), "ABA", "DCD", "EBE",

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),

                ('B'), ItemStackHelper.fromData(IUItem.alloyscasing, 1, 2),

                ('C'), ItemStackHelper.fromData(IUItem.module_schedule),

                ('D'),
                ("c:ingots/Invar"),
                ('E'), ItemStackHelper.fromData(IUItem.alloyscasing, 1, 4)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.entitymodules, 1, 1), "ABA", "DCD", "EBE",

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('B'), ItemStackHelper.fromData(IUItem.adv_spectral_box),

                ('C'), ItemStackHelper.fromData(IUItem.module_schedule),

                ('D'),
                ("c:doubleplate/Germanium"),
                ('E'), ItemStackHelper.fromData(IUItem.alloyscasing, 1, 9)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spawnermodules, 1, 0), "ABA",

                ('A'), ItemStackHelper.fromData(IUItem.module9, 1, 6),

                ('B'),
                ("c:doubleplate/Nichrome")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spawnermodules, 1, 1), "ABA", "DCD", "EEE",

                ('E'), ItemStackHelper.fromData(IUItem.nanoBox),

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('B'),
                ("c:doubleplate/Aluminium"),
                ('A'),
                ("c:doubleplate/Alumel"),
                ('C'), ItemStackHelper.fromData(IUItem.spawnermodules, 1, 0)
        );
        Recipes.recipe.addRecipe(new ItemStack(Blocks.TNT, 16), "BCB", "CAC", "BCB",

                ('A'),
                ItemStackHelper.fromData(IUItem.iudust, 1, 72),
                ('C'), new ItemStack(Blocks.SAND, 1),
                ('B'), new ItemStack(Items.GUNPOWDER, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spawnermodules, 1, 2), "ABA", "DCD", "EEE",

                ('E'), ItemStackHelper.fromData(IUItem.quantumtool),

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('B'),
                ("c:doubleplate/Platinum"),
                ('A'),
                ("c:doubleplate/Vitalium"),
                ('C'), ItemStackHelper.fromData(IUItem.spawnermodules, 1, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spawnermodules, 1, 3), "ABA",

                ('B'), ItemStackHelper.fromData(IUItem.module9, 1, 3),

                ('A'),
                ("c:doubleplate/Nichrome")
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.spawnermodules, 1, 4),
                " C ", "ABA", " C ", ('B'), ItemStackHelper.fromData(IUItem.module_schedule, 1),
                ('A'), ("c:doubleplate/Nichrome"), ('C'), new ItemStack(Items.EXPERIENCE_BOTTLE)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spawnermodules, 1, 5), "ABA", "DCD", "EEE",

                ('E'), ItemStackHelper.fromData(IUItem.nanoBox),

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),

                ('B'),
                ("c:doubleplate/Aluminium"),
                ('A'),
                ("c:doubleplate/Alumel"),
                ('C'), ItemStackHelper.fromData(IUItem.spawnermodules, 1, 4)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.oilquarry), "C G", "ABA", " D ",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 53),

                ('G'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 251),

                ('D'), IUItem.elemotor,

                ('A'),
                IUItem.advancedAlloy,

                ('B'), IUItem.machine
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 192), " G ", " B ", " D ",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 53),

                ('G'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 263),

                ('D'), IUItem.elemotor,

                ('A'),
                IUItem.advancedAlloy,

                ('B'), IUItem.machine
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cathode), "B B", "BAB", "B B",

                ('A'), ItemStackHelper.fromData(IUItem.fluidCell, 1),

                ('B'),
                ("c:plates/Alumel")
        );
        for (int i = 0; i < 30; i++) {
            Recipes.recipe.addShapelessRecipe(ItemStackHelper.fromData(IUItem.spaceItem, 1, i),
                    ItemStackHelper.fromData(IUItem.spaceItem, 1, i + 30), ItemStackHelper.fromData(IUItem.spaceItem, 1, i + 30),
                    ItemStackHelper.fromData(IUItem.spaceItem, 1, i + 30), ItemStackHelper.fromData(IUItem.spaceItem, 1, i + 30)
            );
            if (i < 16) {
                Recipes.recipe.addShapelessRecipe(ItemStackHelper.fromData(IUItem.space_cobblestone, 1, i),
                        ItemStackHelper.fromData(IUItem.spaceItem, 1, i), ItemStackHelper.fromData(IUItem.spaceItem, 1, i),
                        ItemStackHelper.fromData(IUItem.spaceItem, 1, i), ItemStackHelper.fromData(IUItem.spaceItem, 1, i)
                );
            } else {
                Recipes.recipe.addShapelessRecipe(ItemStackHelper.fromData(IUItem.space_cobblestone1, 1, i - 16),
                        ItemStackHelper.fromData(IUItem.spaceItem, 1, i), ItemStackHelper.fromData(IUItem.spaceItem, 1, i),
                        ItemStackHelper.fromData(IUItem.spaceItem, 1, i), ItemStackHelper.fromData(IUItem.spaceItem, 1, i)
                );
            }
        }
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.molot), "BBB", "BAB", " A ",

                ('A'), Items.STICK,

                ('B'),
                ("c:plates/Ferromanganese")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.steelHammer), "BBB", "BAB", " A ",

                ('A'), Items.STICK,

                ('B'),
                ("c:plates/Steel")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ironHammer), "CBC", "BAB", " A ",

                ('A'), Items.STICK,

                ('B'),
                ("c:plates/Iron"), 'C', "c:platedense/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.anode), "B B", "BAB", "B B",

                ('A'), ItemStackHelper.fromData(IUItem.fluidCell, 1),

                ('B'),
                ("c:plates/Muntsa")
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.expmodule),
                "   ", "ABA", "   ", ('A'), ItemStackHelper.fromData(IUItem.module_schedule),
                ('B'), new ItemStack(Items.EXPERIENCE_BOTTLE)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module_stack), "DCD", "BAB", " C ",

                ('D'),
                ("c:plates/Alumel"),
                ('A'), ItemStackHelper.fromData(IUItem.module_schedule),

                ('B'), IUItem.overclockerUpgrade,

                ('C'),
                IUItem.tranformerUpgrade
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module_quickly), "   ", "ABA", "   ",

                ('A'), IUItem.overclockerUpgrade1,

                ('B'), ItemStackHelper.fromData(IUItem.module_schedule)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nanodrill), "EHE", "ABC", " D ",

                ('E'), ItemStackHelper.fromData(IUItem.nanoBox),

                ('D'), ItemStackHelper.fromData(IUItem.diamond_drill),

                ('B'), ItemStackHelper.fromData(IUItem.advnanobox), 'H', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 517),

                ('A'),
                ItemStackHelper.fromData(IUItem.nanopickaxe, 1),

                ('C'), ItemStackHelper.fromData(IUItem.nanoshovel, 1)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.quantumdrill),
                "EHE",
                "ABC",
                " D ",

                ('E'),
                ItemStackHelper.fromData(IUItem.quantumtool),

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                'H',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 509),

                ('B'),
                ItemStackHelper.fromData(IUItem.quantumtool),

                ('A'),
                ItemStackHelper.fromData(IUItem.quantumpickaxe, 1),

                ('C'),
                ItemStackHelper.fromData(IUItem.quantumshovel, 1)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.spectraldrill),
                "EHE",
                "ABC",
                " D ",

                ('E'),
                ItemStackHelper.fromData(IUItem.spectral_box),

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                'H',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 525),

                ('B'),
                ItemStackHelper.fromData(IUItem.advQuantumtool),

                ('A'),
                ItemStackHelper.fromData(IUItem.spectralpickaxe, 1),

                ('C'),
                ItemStackHelper.fromData(IUItem.spectralshovel, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.quantumdrill), "THT", "CDC", "BFB",

                ('T'),
                ("c:doubleplate/Muntsa"),
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 516),

                ('B'), ItemStackHelper.fromData(IUItem.advQuantumtool, 1),
                'H', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 509),
                ('D'),
                ItemStackHelper.fromData(IUItem.nanodrill, 1),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spectraldrill), "THT", "CDC", "BFB",

                ('T'), IUItem.iridiumPlate,

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 532),

                ('B'), ItemStackHelper.fromData(IUItem.adv_spectral_box, 1),
                'H', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 525),
                ('D'),
                ItemStackHelper.fromData(IUItem.quantumdrill, 1),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.bags), "BCB", "BAB", "B B",

                ('C'), ItemStackHelper.fromData(IUItem.suBattery, 1),

                ('B'), new ItemStack(Items.LEATHER),

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_bags, 1), "BCB", "BAB", "B B",

                ('C'), ItemStackHelper.fromData(IUItem.advBattery, 1),

                ('B'), IUItem.carbonPlate,

                ('A'), ItemStackHelper.fromData(IUItem.bags, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.imp_bags, 1), "BCB", "BAB", "B B",

                ('C'), ItemStackHelper.fromData(IUItem.reBattery, 1),

                ('B'), ItemStackHelper.fromData(IUItem.compresscarbon),

                ('A'), ItemStackHelper.fromData(IUItem.adv_bags, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.advjetpack), "BCB", "CDC", "BFB",

                ('F'), ItemStackHelper.fromData(IUItem.suBattery, 1),

                ('B'),
                ("c:doubleplate/Ferromanganese"),
                ('D'), ItemStackHelper.fromData(IUItem.electricJetpack, 1),

                ('C'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.impjetpack), "TCT", "CDC", "BFB",

                ('T'),
                ("c:doubleplate/Muntsa"),
                ('F'), ItemStackHelper.fromData(IUItem.reBattery, 1),

                ('B'), ItemStackHelper.fromData(IUItem.quantumtool, 1),

                ('D'),
                ItemStackHelper.fromData(IUItem.advjetpack, 1),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.perjetpack), "TCT", "CDC", "BFB",

                ('T'), IUItem.iridiumPlate,

                ('F'), ItemStackHelper.fromData(IUItem.advBattery, 1),

                ('B'), ItemStackHelper.fromData(IUItem.advQuantumtool, 1),

                ('D'),
                ItemStackHelper.fromData(IUItem.impjetpack, 1),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 2), "   ", "ABC", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.module7),

                ('B'), IUItem.machine,

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 3), "   ", "ABC", "   ",

                ('A'), IUItem.module8,

                ('B'), IUItem.machine,

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
        );

        Recipes.recipe.addRecipe(IUItem.phase_module, "DDD", "BAC",

                ('D'), ItemStackHelper.fromData(IUItem.alloysdoubleplate, 1, 7),

                ('C'), IUItem.module2,

                ('B'), IUItem.module1,

                ('A'), ItemStackHelper.fromData(IUItem.module_schedule)
        );
        Recipes.recipe.addRecipe(IUItem.moonlinse_module, "DDD", "BAB", "CEC",

                ('D'), ItemStackHelper.fromData(IUItem.lens, 1, 4),

                ('E'), ItemStackHelper.fromData(IUItem.alloysdoubleplate, 1, 6),

                ('C'), ItemStackHelper.fromData(IUItem.alloysdoubleplate, 1, 4),

                ('B'),
                ItemStackHelper.fromData(IUItem.alloysdoubleplate, 1, 3),

                ('A'), ItemStackHelper.fromData(IUItem.module_schedule)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.tank), "BAB", "CAC", "BAB",

                ('C'), ItemStackHelper.fromData(IUItem.alloysplate, 1, 4),

                ('A'), IUItem.fluidCell,

                ('B'), ItemStackHelper.fromData(IUItem.doubleplate, 1, 4)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.tank, 1, 1), "C C", "BAB", "C C",

                ('C'), ItemStackHelper.fromData(IUItem.alloysplate, 1, 2),

                ('A'), ItemStackHelper.fromData(IUItem.tank),

                ('B'), ItemStackHelper.fromData(IUItem.alloysplate, 1, 6)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.tank, 1, 2), "CBC", "CAC", "CBC",

                ('B'), IUItem.photoniy,

                ('C'), ItemStackHelper.fromData(IUItem.nanoBox),

                ('A'), ItemStackHelper.fromData(IUItem.tank, 1, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.tank, 1, 3), "CBC", "CAC", "CBC",

                ('B'), IUItem.photoniy_ingot,

                ('A'), ItemStackHelper.fromData(IUItem.tank, 1, 2),

                ('C'), ItemStackHelper.fromData(IUItem.advnanobox)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module_storage, 1), "AAA", "CBC", "DDD",

                ('D'), ("c:plates/Manganese"), ('C'), ("c:plates/Nickel"), ('A'),
                ("c:plates/Invar"),
                ('B'),
                ItemStackHelper.fromData(IUItem.module_schedule)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machinekit, 1, 3), " A ", "BDC", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.machinekit, 1, 0),

                ('B'), ItemStackHelper.fromData(IUItem.machinekit, 1, 1),

                ('C'), ItemStackHelper.fromData(IUItem.machinekit, 1, 2),

                ('D'),
                ("c:plates/Manganese")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 7), "DCD", "ABA", "DCD",

                ('A'), ItemStackHelper.fromData(IUItem.impmagnet, 1),

                ('B'), ItemStackHelper.fromData(IUItem.basemachine, 1, 14),

                ('C'),
                ("c:plates/Electrum"),
                ('D'),
                ("c:plates/Nickel")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.leadbox), "AAA", "ABA", "   ",

                ('A'),
                ("c:ingots/Lead"),
                ('B'), new ItemStack(Blocks.CHEST)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.katana, 1), "AD ", "AB ", "CC ",

                ('A'), ItemStackHelper.fromData(IUItem.compressAlloy),

                ('C'), new ItemStack(Blocks.GLOWSTONE),

                ('B'), ItemStackHelper.fromData(IUItem.lapotron_crystal, 1),

                ('D'),
                ItemStackHelper.fromData(IUItem.nanosaber, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.coolupgrade, 1, 0), "ACA", "DBD", "ACA",

                ('A'),
                ModUtils.getCellFromFluid(FluidName.fluidazot.getInstance().get()),
                ('B'), ItemStackHelper.fromData(IUItem.module_schedule), 'C', "c:plates/Adamantium", 'D', "c:plates/Stellite"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.coolupgrade, 1, 1), "ACA", "DBD", "ACA",

                ('A'),
                ModUtils.getCellFromFluid(FluidName.fluidhyd.getInstance().get()),
                ('B'), ItemStackHelper.fromData(IUItem.module_schedule), 'C', "c:plates/Adamantium", 'D', "c:plates/Stellite"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.coolupgrade, 1, 2), "ACA", "DBD", "ACA",

                ('A'),
                ModUtils.getCellFromFluid(FluidName.fluidHelium.getInstance().get()),
                ('B'), ItemStackHelper.fromData(IUItem.module_schedule), 'C', "c:plates/Adamantium", 'D', "c:plates/Stellite"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.autoheater), "CAC", "DBD", "C C",

                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 5),

                ('B'), ItemStackHelper.fromData(IUItem.module_schedule), 'C', "c:plates/Inconel", 'D', "c:plates/Mithril"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.scable), " C ", "ABA", " C ",

                ('B'), IUItem.copperCableItem,

                ('A'), ItemStackHelper.fromData(IUItem.sunnarium, 1, 3),

                ('C'), IUItem.rubber
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.qcable, 2), " C ", "ABA", " C ",

                ('B'), IUItem.glassFiberCableItem,

                ('A'), ItemStackHelper.fromData(IUItem.proton),

                ('C'), IUItem.iridiumOre
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.simplemachine, 1, 6), "ABA", "CDC", "ABA",

                ('A'), Items.GLOWSTONE_DUST,

                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),

                ('C'), IUItem.advancedMachine,

                ('D'),
                ItemStackHelper.fromData(IUItem.lapotron_crystal, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 14), " D ", "ABA", " C ",

                ('A'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('B'), ItemStackHelper.fromData(IUItem.module_schedule),

                ('D'), ItemStackHelper.fromData(IUItem.machines_base, 1, 2),

                ('C'),
                ("c:doubleplate/Muntsa")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 15), " D ", "ABA", " C ",

                ('A'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('B'), ItemStackHelper.fromData(IUItem.module_schedule),

                ('D'), ItemStackHelper.fromData(IUItem.machines_base1, 1, 9),

                ('C'),
                ("c:doubleplate/Vanadoalumite")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.upgrade_speed_creation), "   ", "ABA", "   ",

                ('A'), IUItem.overclockerUpgrade_1,

                ('B'), ItemStackHelper.fromData(IUItem.module_schedule)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blastfurnace, 1, 5), "AA ", "AA ", "   ",


                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 771)
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blastfurnace, 1, 2), " A ", "ABA", " A ",

                ('B'), getBlockStack(BlockPrimalFluidHeater.primal_fluid_heater),
                ('A'), ItemStackHelper.fromData(IUItem.blastfurnace, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blastfurnace, 1, 1), "CAC", "ABA", "CAC",

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('A'), ItemStackHelper.fromData(IUItem.blastfurnace, 1, 5),
                'C', "c:gears/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blastfurnace, 1, 4), " A ", "ABA", " A ",

                ('B'),
                getBlockStack(BlockBaseMachine3.steel_tank),
                ('A'), ItemStackHelper.fromData(IUItem.blastfurnace, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blastfurnace, 1, 3), "CAC", "ABA", "CAC",

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('A'), ItemStackHelper.fromData(IUItem.blastfurnace, 1, 5),
                'C', "c:gears/Electrum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blastfurnace, 1, 0), " A ", "ABA", " A ",

                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),

                ('A'), ItemStackHelper.fromData(IUItem.blastfurnace, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spectral_box), "D D", "ABA", "C C",

                ('D'), ItemStackHelper.fromData(IUItem.coal_chunk1),

                ('C'), IUItem.iridiumPlate,

                ('A'),
                ("c:doubleplate/Spinel"),
                ('B'),
                ItemStackHelper.fromData(IUItem.quantumtool)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_spectral_box), "DAD", "ABA", "CEC",

                ('E'), ItemStackHelper.fromData(IUItem.coal_chunk1),

                ('D'), ItemStackHelper.fromData(IUItem.photoniy_ingot),

                ('C'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('A'),
                ("c:doubleplate/Spinel"),
                ('B'), ItemStackHelper.fromData(IUItem.advQuantumtool)
        );
        Recipes.recipe.addRecipe(IUItem.transformerUpgrade, " A ", "DBD", " C ",

                ('B'), IUItem.medium_current_converter_to_low,

                ('A'), ItemStackHelper.fromData(IUItem.tranformer, 1, 8),

                ('D'), IUItem.insulatedGoldCableItem,

                ('C'),
                IUItem.upgrade_casing
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.windmeter, 1), " A ", "ABA", " AC",

                ('A'), IUItem.casingtin, ('B'), IUItem.casingbronze, ('C'), IUItem.powerunitsmall
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotorupgrade_schemes), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.plastic_plate),
                ('D'), "c:doubleplate/Vanadoalumite", ('B'), ItemStackHelper.fromData(IUItem.photoniy_ingot), ('C'),
                "c:plates/Orichalcum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 0), "A A", "CBC", "A A",

                ('A'), ItemStackHelper.fromData(IUItem.advnanobox),
                ('B'), ItemStackHelper.fromData(IUItem.rotorupgrade_schemes), ('C'), "c:casings/Nichrome"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 1), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.advQuantumtool), ('B'), ItemStackHelper.fromData(IUItem.core, 1, 3),
                ('C'), ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 0), ('D'),
                "c:casings/Redbrass"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 2), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.adv_spectral_box), ('B'), ItemStackHelper.fromData(IUItem.core, 1, 5),
                ('C'), ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 1), ('D'),
                "c:casings/Muntsa"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 3), "A A", "CBC", "A A",

                ('A'), ItemStackHelper.fromData(IUItem.advnanobox),
                ('B'), ItemStackHelper.fromData(IUItem.rotorupgrade_schemes), ('C'), IUItem.iridiumPlate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 4), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.advQuantumtool), ('B'), ItemStackHelper.fromData(IUItem.core, 1, 4),
                ('C'), ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 3), ('D'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 5), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.adv_spectral_box), ('B'), ItemStackHelper.fromData(IUItem.core, 1, 6),
                ('C'), ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 4), ('D'),
                IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 6), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.advnanobox),
                ('B'), ItemStackHelper.fromData(IUItem.rotorupgrade_schemes), ('C'), IUItem.iridiumPlate, ('D'),
                "c:casings/Duralumin"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 7), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.advQuantumtool), ('B'), ItemStackHelper.fromData(IUItem.core, 1, 4),
                ('C'), ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 6), ('D'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 8), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.adv_spectral_box), ('B'), ItemStackHelper.fromData(IUItem.core, 1, 6),
                ('C'), ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 7), ('D'),
                IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 9), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.adv_spectral_box),
                ('B'), ItemStackHelper.fromData(IUItem.rotorupgrade_schemes), ('C'), IUItem.compressIridiumplate, ('D'),
                "c:doubleplate/Alumel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 10), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.core, 1, 5),
                ('B'), ItemStackHelper.fromData(IUItem.rotorupgrade_schemes), ('C'), IUItem.compressIridiumplate, ('D'),
                "c:doubleplate/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electric_hoe), "AA ", " A ", " B ",

                ('A'), "c:plates/Iron",
                ('B'), IUItem.powerunitsmall
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spawnermodules, 1, 8), "ECE", "BAB", "DCD",

                ('A'), ItemStackHelper.fromData(IUItem.module_schedule), ('E'), ItemStackHelper.fromData(IUItem.core, 1, 5),
                ('B'), ItemStackHelper.fromData(IUItem.quantumtool), ('C'),
                IUItem.iridiumPlate, ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.volcano_hazmat_helmet, 1), "ACA", "BAB", "A A",

                ('A'), "c:plates/Carbon",
                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 478), ('C'),
                "c:gems/Topaz"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.volcano_hazmat_leggings, 1), "ACA", "B B", "A A",

                ('A'), "c:plates/Carbon",
                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 478), ('C'),
                "c:gems/Topaz"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.volcano_rubber_boots, 1), "A A", "C C", "B B",

                ('A'), "c:plates/Carbon",
                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 478), ('C'),
                "c:gems/Topaz"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.volcano_hazmat_chestplate, 1), "AAA", "BCB", "AAA",

                ('A'), "c:plates/Carbon",
                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 478), ('C'),
                "c:gems/Topaz"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 38), "CBC", "ADA", "EBE",

                ('E'), IUItem.adv_spectral_box,

                ('D'), IUItem.advancedMachine,

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('B'),
                ("c:doubleplate/Muntsa"),
                ('A'),
                ("c:doubleplate/Aluminumbronze")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 32), " C ", "CDC", "ECE",

                ('C'), IUItem.insulatedCopperCableItem,

                ('D'), IUItem.advancedMachine,

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 33), " C ", "CDC", "ECE",

                ('C'), IUItem.insulatedCopperCableItem,

                ('D'), IUItem.advancedMachine,

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 11), "ADA", "CBC", "DED",

                ('A'), ItemStackHelper.fromData(IUItem.advnanobox),
                ('B'), ItemStackHelper.fromData(IUItem.rotorupgrade_schemes), ('C'), IUItem.iridiumPlate, ('D'),
                "c:doubleplate/Platinum", ('E'), ItemStackHelper.fromData(IUItem.core, 1, 3)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 12), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.advQuantumtool), ('B'), ItemStackHelper.fromData(IUItem.core, 1, 4),
                ('C'), ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 11), ('D'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 13), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.adv_spectral_box), ('B'), ItemStackHelper.fromData(IUItem.core, 1, 6),
                ('C'), ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 12), ('D'),
                IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 14), "ADA", "CBC", "DED",

                ('A'), ItemStackHelper.fromData(IUItem.advnanobox),
                ('B'), ItemStackHelper.fromData(IUItem.rotorupgrade_schemes), ('C'), IUItem.iridiumPlate, ('D'),
                "c:doubleplate/Vitalium", ('E'), ItemStackHelper.fromData(IUItem.core, 1, 4)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 15), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.advQuantumtool), ('B'), ItemStackHelper.fromData(IUItem.core, 1, 5),
                ('C'), ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 14), ('D'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 16), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.adv_spectral_box), ('B'), ItemStackHelper.fromData(IUItem.core, 1, 6),
                ('C'), ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 15), ('D'),
                IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 17), "ADA", "CBC", "DED",

                ('A'), ItemStackHelper.fromData(IUItem.advnanobox),

                ('B'), ItemStackHelper.fromData(IUItem.rotorupgrade_schemes),

                ('C'), IUItem.iridiumPlate,

                ('D'),
                "c:doubleplate/Redbrass",

                ('E'), ItemStackHelper.fromData(IUItem.core, 1, 4)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 18), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.advQuantumtool), ('B'), ItemStackHelper.fromData(IUItem.core, 1, 5),
                ('C'), ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 17), ('D'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 19), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.adv_spectral_box), ('B'), ItemStackHelper.fromData(IUItem.core, 1, 6),
                ('C'), ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 18), ('D'),
                IUItem.doublecompressIridiumplate
        );
        recipe_machines();
    }

    public static void craft_modules(int meta, int meta1, int meta2) {
        Recipes.recipe.addShapelessRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, meta),
                ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        meta1
                ), ItemStackHelper.fromData(IUItem.crafting_elements, 1, meta2)
        );
    }

    public static void recipe_machines() {
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 21), "CCC", "BAB", "DBD",
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 42), ('B'), "c:ingots/Gold", ('C'), "c:ingots/Iron",
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 650)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20), "ABA", "ECD", "  ",

                ('A'), "c:doubleplate/Electrum", ('B'), "c:doubleplate/Alumel", ('C'), IUItem.elemotor, ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 16), 'E', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 614)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96), " B ", "ACA", " D ",

                ('A'), "c:doubleplate/Platinum",

                ('B'), "c:doubleplate/Vitalium",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),

                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 92)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120), " B ", "ACA", " D ",

                ('A'), "c:doubleplate/Spinel", ('B'), "c:doubleplate/Manganese", ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        96
                ), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 116)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 60), "AAA", " B ", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.advBattery, 1), ('B'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        21
                )
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 614), "AAA", " B ", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.reBattery, 1), ('B'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        21
                )
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 16), "AAA", "CBC", "   ",

                ('A'), "c:plates/Bronze", ('C'), "c:plates/Tin", ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 42)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 92), "AAA", "CBC", "   ",

                ('A'), "c:plates/Platinum", ('C'), "c:plates/Cobalt", ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 16)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 116), "AAA", "CBC", "   ",

                ('A'), "c:plates/Spinel", ('C'), "c:plates/Manganese", ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 92)
        );
        Recipes.recipe.addRecipe(IUItem.elemotor, " A ", "BCB", " D ",
                ('A'), IUItem.casingtin, ('B'), IUItem.coil, ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 60),
                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 25), "CCC", "DAD", "EBE",
                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 21), ('C'), ItemStackHelper.fromData(
                        IUItem.nanoBox),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 16), 'E', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 651)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 23), "CCC", "DAD", "EBE",
                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8), ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 25), ('C'), ItemStackHelper.fromData(
                        IUItem.quantumtool),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 92), 'E', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 652)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 24), "CCC", "DAD", "EBE",
                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10), ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 23), ('C'), ItemStackHelper.fromData(
                        IUItem.spectral_box),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 116), 'E', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 653)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 42), "AAA", "CBC", "   ",

                ('B'), ItemStackHelper.fromData(IUItem.basecircuit, 1, 17),
                ('C'), Items.REDSTONE,

                ('A'), "c:ingots/Tungsten"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 41), "AAA", "BCB", "AAA",
                ('A'), Items.FLINT, ('B'), "c:ingots/Iron", ('C'), "c:ingots/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 46), " A ", "ABA", "ABA",
                ('A'), "c:plates/Iron", ('B'), "c:plates/Aluminium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 35), " B ", "BAB", "B B",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 46),

                ('B'), "c:plates/Chromium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72), " B ", "BAB", "CCC",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 46),

                ('B'), "c:plates/Mikhail",

                ('C'), "c:plates/Cobalt"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 70), " B ", "BAB", "CCC",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 46),

                ('B'), "c:plates/Zinc",

                ('C'), "c:plates/Magnesium"
        );
        craft_modules(69, 41, 21);
        craft_modules(63, 76, 21);
        craft_modules(159, 141, 21);
        craft_modules(163, 142, 21);
        craft_modules(132, 144, 21);
        craft_modules(165, 164, 21);

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 142), "AAA", "BCB", " D ",

                ('A'), "c:plates/Platinum", ('C'), "c:plates/Titanium",
                ('B'), "c:plates/Cobalt", ('D'),
                "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 200), "AAA", "BCB", "   ",

                ('A'), "c:plates/Zinc", ('B'), "c:plates/Titanium",
                ('C'), "c:plates/Aluminium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 141), "ABB", "C D", " AA",

                ('A'), "c:plates/Cobalt", ('B'), "c:plates/Nickel",
                ('C'), "c:plates/Iron", ('D'),
                "c:plates/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 144), "AAA", " B ", " C ",

                ('A'), "c:plates/Nickel", ('B'), "c:plates/Cobalt",
                ('C'), "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 76), "AAA", " B ", "AAA",

                ('A'), "c:plates/Iron", ('B'), "c:plates/Tungsten"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 164), "BAB", "B B", "BAB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 200),
                ('B'), "c:plates/Tungsten"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 137), "BA ", "AB ", "   ",
                ('A'), "c:plates/Steel", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 501)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blockResource, 1, 12), "AAA", "A A", "AAA",
                ('A'), "c:plates/Aluminumbronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138), "BA ", "CC ", "   ",
                ('A'), "c:plates/Electrum", ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 137), 'C', IUItem.plastic_plate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139), "BA ", "CC ", "   ",
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 479), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                'C', "c:casings/Bloodstone"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140), "BC ", "AA ", "   ",
                ('A'), "c:casings/Draconid", ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139), 'C', "c:plates/Woods"
        );
        Recipes.recipe.addShapelessRecipe(Blocks.STICKY_PISTON, Blocks.PISTON, IUItem.latex);
        Recipes.recipe.addRecipe(IUItem.machine, "AA ", "AA ", "   ",
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 137)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 121), "AAA", "ABA", "AAA",
                ('A'), "c:plates/Cobalt", ('B'), Items.STRING
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 8, 122), "AAA", "BBB", "CCC",
                ('A'), "c:plates/Tungsten", ('B'), "c:plates/Titanium", 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 480)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 201), "AAA", "BBB", "   ",
                ('A'), "c:plates/Titanium", ('B'), "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 0), "AAA", "ABA", "AAA",
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 444), ('B'), "c:plates/Cobalt"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 180), "ABA", "   ", "   ",
                ('A'), "c:plates/Copper", ('B'), "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 181), " A ", "ABA", "   ",
                ('A'), "c:plates/Copper", ('B'), "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 182), " A ", " B ", " A ",
                ('A'), "c:plates/Copper", ('B'), "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 183), "   ", "ABA", " A ",
                ('A'), "c:plates/Copper", ('B'), "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 184), "   ", "A A", " A ",
                ('A'), "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 185), " A ", "A A", "   ",
                ('A'), "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 186), " A ", "A  ", " A ",
                ('A'), "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 187), " A ", "  A", " A ",
                ('A'), "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 188), "ABA", "   ", "   ",
                ('A'), "c:plates/Duralumin", ('B'), "c:plates/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 191), " A ", "ABA", "   ",
                ('A'), "c:plates/Duralumin", ('B'), "c:plates/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 189), " A ", " B ", " A ",
                ('A'), "c:plates/Duralumin", ('B'), "c:plates/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 190), "   ", "ABA", " A ",
                ('A'), "c:plates/Duralumin", ('B'), "c:plates/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 194), " A ", "A A", "   ",
                ('A'), "c:plates/Spinel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 195), "   ", "A A", " A ",
                ('A'), "c:plates/Spinel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 193), " A ", "A  ", " A ",
                ('A'), "c:plates/Spinel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 192), " A ", "  A", " A ",
                ('A'), "c:plates/Spinel"
        );
        craft_modules(196, 201, 21);
        craft_modules(199, 201, 25);
        craft_modules(197, 201, 23);
        craft_modules(198, 201, 24);
        craft_modules(636, 201, 620);
        craft_modules(166, 164, 25);
        craft_modules(167, 164, 23);
        craft_modules(168, 164, 24);
        craft_modules(133, 144, 25);
        craft_modules(134, 144, 23);
        craft_modules(136, 144, 24);
        craft_modules(160, 141, 25);
        craft_modules(161, 141, 23);
        craft_modules(162, 141, 24);
        craft_modules(125, 142, 25);
        craft_modules(126, 142, 23);
        craft_modules(127, 142, 24);
        craft_modules(1, 76, 25);
        craft_modules(77, 76, 23);
        craft_modules(102, 76, 24);

        craft_modules(2, 41, 25);
        craft_modules(78, 41, 23);
        craft_modules(103, 41, 24);
        craft_modules(602, 41, 620);

        craft_modules(608, 76, 620);
        craft_modules(609, 164, 620);
        craft_modules(612, 144, 620);
        craft_modules(615, 141, 620);
        craft_modules(616, 142, 620);
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 73), "CCC", "BAB", "D D",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 21),

                ('B'), "c:plates/Tin",

                ('C'), "c:plates/Chromium",

                ('D'),
                "c:plates/Gold"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 37), "BBB", " A ", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 21),

                ('B'), "c:plates/Electrum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 30), "BBB", " A ", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 21),

                ('B'), "c:plates/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 74), "B B", "DAD", "CCC",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 21),

                ('B'), "c:platedense/Steel",

                ('C'), "c:plates/Iron",

                ('D'),
                "c:plates/Copper"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 28), "BBB", "CAC", " C ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 21),

                ('B'), new ItemStack(Items.WITHER_SKELETON_SKULL),

                ('C'), Blocks.SOUL_SAND
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 29), "   ", "BA ", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 21),

                ('B'), ItemStackHelper.fromData(IUItem.magnet, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44), " B ", "BAB", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 21),

                ('B'), "c:plates/Tin"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47), " B ", "ABA", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),

                ('B'), "c:plates/Tin"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49), " B ", "CBC", " C ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),

                ('B'), "c:plates/Tin",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51), " B ", "ABA", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),

                ('B'), "c:plates/Tin"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52), " B ", "ABA", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),

                ('B'), "c:plates/Tin"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27), "CBC", "CBC", "CAC",
                ('A'), DEFAULT_SENSOR,
                ('C'), IUItem.FluidCell, ('B'), "c:gears/Magnesium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 6), "CBC", "CBC", "CAC",
                ('A'), ADV_SENSOR,
                ('C'), IUItem.FluidCell, ('B'), "c:gears/Magnesium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 83), "CBC", "CBC", "CAC",
                ('A'), IMP_SENSOR,
                ('C'), IUItem.FluidCell, ('B'), "c:gears/Magnesium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 107), "CBC", "CBC", "CAC",
                ('A'), PER_SENSOR,
                ('C'), IUItem.FluidCell, ('B'), "c:gears/Magnesium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 603), "CBC", "CBC", "CAC",
                ('A'), PHOTON_SENSOR,
                ('C'), IUItem.FluidCell, ('B'), "c:gears/Magnesium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 31), "CCC", "BAB", "   ",
                ('A'), DEFAULT_SENSOR,
                ('B'), "c:gears/vanady", ('C'), "c:gears/Iridium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 7), "CCC", "BAB", "   ",
                ('A'), ADV_SENSOR,
                ('B'), "c:gears/vanady", ('C'), "c:gears/Iridium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 84), "CCC", "BAB", "   ",
                ('A'), IMP_SENSOR,
                ('B'), "c:gears/vanady", ('C'), "c:gears/Iridium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 108), "CCC", "BAB", "   ",
                ('A'), PER_SENSOR,
                ('B'), "c:gears/vanady", ('C'), "c:gears/Iridium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 33), "DCD", "BAB", "EFE",
                ('A'), DEFAULT_SENSOR,
                ('B'), "c:plates/vanady", ('D'), "c:gears/Manganese", ('C'),
                "c:casings/Nickel", ('E'), "c:casings/Mikhail", ('F'), "c:doubleplate/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 9), "DCD", "BAB", "EFE",
                ('A'), ADV_SENSOR,
                ('B'), "c:plates/vanady", ('D'), "c:gears/Manganese", ('C'),
                "c:casings/Nickel", ('E'), "c:casings/Mikhail", ('F'), "c:doubleplate/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 86), "DCD", "BAB", "EFE",
                ('A'), IMP_SENSOR,
                ('B'), "c:plates/vanady", ('D'), "c:gears/Manganese", ('C'),
                "c:casings/Nickel", ('E'), "c:casings/Mikhail", ('F'), "c:doubleplate/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 110), "DCD", "BAB", "EFE",
                ('A'), PER_SENSOR,
                ('B'), "c:plates/vanady", ('D'), "c:gears/Manganese", ('C'),
                "c:casings/Nickel", ('E'), "c:casings/Mikhail", ('F'), "c:doubleplate/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 607), "DCD", "BAB", "EFE",
                ('A'), PHOTON_SENSOR,
                ('B'), "c:plates/vanady", ('D'), "c:gears/Manganese", ('C'),
                "c:casings/Nickel", ('E'), "c:casings/Mikhail", ('F'), "c:doubleplate/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 34), "CBC", "BCB", " A ",
                ('A'), DEFAULT_SENSOR, ('B'), "c:plates/Manganese",
                ('C'), "c:gears/Aluminium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 12), "CBC", "BCB", " A ",
                ('A'), ADV_SENSOR, ('B'), "c:plates/Manganese",
                ('C'), "c:gears/Aluminium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 87), "CBC", "BCB", " A ",
                ('A'), IMP_SENSOR, ('B'), "c:plates/Manganese",
                ('C'), "c:gears/Aluminium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 111), "CBC", "BCB", " A ",
                ('A'), PER_SENSOR, ('B'), "c:plates/Manganese",
                ('C'), "c:gears/Aluminium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 610), "CBC", "BCB", " A ",
                ('A'), PHOTON_SENSOR, ('B'), "c:plates/Manganese",
                ('C'), "c:gears/Aluminium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 36), " B ", "BAB", " B ",
                ('A'), DEFAULT_SENSOR, ('B'), "c:ingots/Uranium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 13), " B ", "BAB", " B ",
                ('A'), ADV_SENSOR, ('B'), "c:ingots/Uranium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 88), " B ", "BAB", " B ",
                ('A'), IMP_SENSOR, ('B'), "c:ingots/Uranium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 113), " B ", "BAB", " B ",
                ('A'), PER_SENSOR, ('B'), "c:ingots/Uranium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 39), "CCC", "BAB", "BBB",
                ('A'), DEFAULT_SENSOR, ('B'), "c:plates/Muntsa", ('C'), "c:plates/Nichrome"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 15), "CCC", "BAB", "BBB",
                ('A'), ADV_SENSOR, ('B'), "c:plates/Muntsa", ('C'), "c:plates/Nichrome"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 91), "CCC", "BAB", "BBB",
                ('A'), IMP_SENSOR, ('B'), "c:plates/Muntsa", ('C'), "c:plates/Nichrome"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 115), "CCC", "BAB", "BBB",
                ('A'), PER_SENSOR, ('B'), "c:plates/Muntsa", ('C'), "c:plates/Nichrome"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 613), "CCC", "BAB", "BBB",
                ('A'), PHOTON_SENSOR, ('B'), "c:plates/Muntsa", ('C'), "c:plates/Nichrome"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43), "BCB", "BAB", "BCB",

                ('A'), DEFAULT_SENSOR,

                ('B'), IUItem.FluidCell,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 655)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 45), "BCB", "BAB", "BCB",

                ('A'), DEFAULT_SENSOR,

                ('B'), IUItem.FluidCell,

                ('C'), "c:plates/Alumel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 48), "BCB", "BAB", "BCB",

                ('A'), DEFAULT_SENSOR,

                ('B'), IUItem.FluidCell,

                ('C'), "c:plates/Ferromanganese"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 50), "BCB", "BAB", "BCB",

                ('A'), DEFAULT_SENSOR,

                ('B'), IUItem.FluidCell,

                ('C'), "c:plates/Aluminumbronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 53), " B ", "CAD", " E ",

                ('A'), DEFAULT_SENSOR,

                ('B'), "c:ores/Iron",

                ('C'), "c:ores/Gold",

                ('D'),
                "c:ores/Emerald",

                ('E'), "c:ores/Diamond"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 54), "BBB", "CAC", "EEE",
                ('A'), DEFAULT_SENSOR, ('B'), new ItemStack(Items.WITHER_SKELETON_SKULL),

                ('C'), "c:plates/Aluminumbronze",

                ('E'),
                "c:plates/Alumel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 56), "FBG", "CAD", "HEJ",
                ('A'), DEFAULT_SENSOR,
                ('B'), "c:plates/Alcled", ('C'), "c:plates/Alumel",
                ('D'),
                "c:plates/Vitalium", ('E'), "c:plates/Redbrass",
                ('F'), "c:plates/Muntsa", ('G'), "c:plates/Nichrome",
                ('H'), "c:plates/Vanadoalumite", ('J'),
                "c:plates/Duralumin"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 57), "   ", " AB", "   ",
                ('A'), DEFAULT_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.module7, 1, 4)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 58), "EDE", "BBB", "CAC",

                ('A'), ItemStackHelper.fromData(IUItem.electricblock, 1, 3),

                ('B'),
                getBlockStack(BlockBaseMachine3.teleporter_iu),
                ('C'), ItemStackHelper.fromData(IUItem.tranformer, 1, 9),

                ('D'),
                DEFAULT_SENSOR,

                ('E'), IUItem.reinforcedStone
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 17), "EDE", "BBB", "CAC",

                ('A'), ItemStackHelper.fromData(IUItem.electricblock, 1, 5),

                ('B'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('C'), ItemStackHelper.fromData(IUItem.tranformer, 1, 1),

                ('D'),
                ADV_SENSOR,

                ('E'), IUItem.reinforcedStone
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 93), "EDE", "BBB", "CAC",

                ('A'), ItemStackHelper.fromData(IUItem.electricblock, 1, 6),

                ('B'), ItemStackHelper.fromData(IUItem.core, 1, 6),

                ('C'), ItemStackHelper.fromData(IUItem.tranformer, 1, 3),

                ('D'),
                IMP_SENSOR,

                ('E'), IUItem.reinforcedStone
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 117), "EDE", "BBB", "CAC",

                ('A'), ItemStackHelper.fromData(IUItem.electricblock, 1, 6),

                ('B'), ItemStackHelper.fromData(IUItem.core, 1, 6),

                ('C'), ItemStackHelper.fromData(IUItem.tranformer, 1, 7),

                ('D'),
                PER_SENSOR,

                ('E'), IUItem.reinforcedStone
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 634), "EDE", "BBB", "CAC",

                ('A'), ItemStackHelper.fromData(IUItem.electricblock, 1, 7),

                ('B'), ItemStackHelper.fromData(IUItem.core, 1, 8),

                ('C'), ItemStackHelper.fromData(IUItem.tranformer, 1, 7),

                ('D'),
                PHOTON_SENSOR,

                ('E'), IUItem.reinforcedStone
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 61), "DDD", "CAC", "BBB",
                ('A'), DEFAULT_SENSOR, ('B'), Blocks.DIRT, ('C'), Items.STICK,
                ('D'),
                Blocks.GLASS
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 18), "DDD", "CAC", "BBB",
                ('A'), ADV_SENSOR, ('B'), Blocks.DIRT, ('C'), Items.STICK,
                ('D'),
                Blocks.GLASS
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 94), "DDD", "CAC", "BBB",
                ('A'), IMP_SENSOR, ('B'), Blocks.DIRT, ('C'), Items.STICK,
                ('D'),
                Blocks.GLASS
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 118), "DDD", "CAC", "BBB",
                ('A'), PER_SENSOR, ('B'), Blocks.DIRT, ('C'), Items.STICK,
                ('D'),
                Blocks.GLASS
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 604), "DDD", "CAC", "BBB",
                ('A'), PHOTON_SENSOR, ('B'), Blocks.DIRT, ('C'), Items.STICK,
                ('D'),
                Blocks.GLASS
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 62), "CBC", "BAB", "CBC",
                ('A'), DEFAULT_SENSOR, ('B'), IUItem.paints, ('C'), IUItem.carbonPlate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 64), "BCD", "EAF", "GHJ",
                ('A'), DEFAULT_SENSOR,
                ('B'), Blocks.OAK_SAPLING, ('C'), Blocks.DANDELION,
                ('D'),
                Blocks.POPPY, ('E'), Items.NETHER_WART,
                ('F'), Blocks.VINE, ('G'), Blocks.LILY_PAD,
                ('H'), Blocks.BROWN_MUSHROOM, ('J'),
                Blocks.RED_MUSHROOM
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 19), "BCD", "EAF", "GHJ",
                ('A'), ADV_SENSOR,
                ('B'), Blocks.OAK_SAPLING, ('C'), Blocks.DANDELION,
                ('D'),
                Blocks.POPPY, ('E'), Items.NETHER_WART,
                ('F'), Blocks.VINE, ('G'), Blocks.LILY_PAD,
                ('H'), Blocks.BROWN_MUSHROOM, ('J'),
                Blocks.RED_MUSHROOM
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 95), "BCD", "EAF", "GHJ",
                ('A'), IMP_SENSOR,
                ('B'), Blocks.OAK_SAPLING, ('C'), Blocks.DANDELION,
                ('D'),
                Blocks.POPPY, ('E'), Items.NETHER_WART,
                ('F'), Blocks.VINE, ('G'), Blocks.LILY_PAD,
                ('H'), Blocks.BROWN_MUSHROOM, ('J'),
                Blocks.RED_MUSHROOM
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 119), "BCD", "EAF", "GHJ",
                ('A'), PER_SENSOR,
                ('B'), Blocks.OAK_SAPLING, ('C'), Blocks.DANDELION,
                ('D'),
                Blocks.POPPY, ('E'), Items.NETHER_WART,
                ('F'), Blocks.VINE, ('G'), Blocks.LILY_PAD,
                ('H'), Blocks.BROWN_MUSHROOM, ('J'),
                Blocks.RED_MUSHROOM
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 611), "BCD", "EAF", "GHJ",
                ('A'), PHOTON_SENSOR,
                ('B'), Blocks.OAK_SAPLING, ('C'), Blocks.DANDELION,
                ('D'),
                Blocks.POPPY, ('E'), Items.NETHER_WART,
                ('F'), Blocks.VINE, ('G'), Blocks.LILY_PAD,
                ('H'), Blocks.BROWN_MUSHROOM, ('J'),
                Blocks.RED_MUSHROOM
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 66), "DDD", "BAB", "   ",
                ('A'), DEFAULT_SENSOR, ('B'), IUItem.overclockerUpgrade,
                ('D'), "c:plates/Ferromanganese"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 67), "DDD", "BAB", "   ",
                ('A'), DEFAULT_SENSOR, ('B'), IUItem.overclockerUpgrade,
                ('D'), "c:plates/Vitalium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 68), "CCC", "BAB", " B ",
                ('A'), DEFAULT_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.core, 1, 4),

                ('C'), "c:doubleplate/Muntsa"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 179), "CCC", "BAB", " B ",
                ('A'), ADV_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('C'), "c:doubleplate/Muntsa"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 177), "CCC", "BAB", " B ",
                ('A'), IMP_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.core, 1, 6),

                ('C'), "c:doubleplate/Muntsa"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 178), "CCC", "BAB", " B ",
                ('A'), PER_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.core, 1, 7),

                ('C'), "c:doubleplate/Muntsa"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 626), "CCC", "BAB", " B ",
                ('A'), PHOTON_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.core, 1, 7),

                ('C'), "c:doubleplate/Muntsa"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 71), "CCC", "BAB", "DED",

                ('A'), DEFAULT_SENSOR,

                ('C'), "c:gears/Germanium",

                ('B'), "c:doubleplate/Iridium",

                ('D'),
                "c:gears/Redbrass",

                ('E'), "c:doubleplate/Vitalium"
        );
        Recipes.recipe.addRecipe(IUItem.iridiumPlate, "ABA", "BCB", "ABA",

                ('A'), IUItem.iridiumOre,

                ('C'), "c:gems/Diamond",

                ('B'), IUItem.advancedAlloy
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 3), "CCC", "BAB", "DED",
                ('A'), ADV_SENSOR, ('C'), "c:gears/Germanium", ('B'), "c:doubleplate/Iridium",
                ('D'),
                "c:gears/Redbrass", ('E'), IUItem.iridiumOre
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 80), "CCC", "BAB", "DED",
                ('A'), IMP_SENSOR, ('C'), "c:gears/Germanium", ('B'), "c:doubleplate/Iridium",
                ('D'),
                "c:gears/Redbrass", ('E'), IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 104), "CCC", "BAB", "DED",
                ('A'), PER_SENSOR, ('C'), "c:gears/Germanium", ('B'), "c:doubleplate/Iridium",
                ('D'),
                "c:gears/Redbrass", ('E'), IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 75), "CCC", " A ", " B ",
                ('A'), DEFAULT_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.itemiu, 1, 3),
                ('C'), "c:plates/Cobalt"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 10), "CCC", "BAB", "   ",
                ('A'), ADV_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.itemiu, 1, 3),
                ('C'), "c:plates/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 98), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidneft.getInstance().get()),
                ('C'), "c:gears/Magnesium", ('D'),
                "c:plates/Titanium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 98), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidsour_light_oil.getInstance().get()),
                ('C'), "c:gears/Magnesium", ('D'),
                "c:plates/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 98), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidsour_medium_oil.getInstance().get()),
                ('C'), "c:gears/Magnesium", ('D'),
                "c:plates/Titanium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 98), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidsour_heavy_oil.getInstance().get()),
                ('C'), "c:gears/Magnesium", ('D'),
                "c:plates/Titanium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 98), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidsweet_medium_oil.getInstance().get()),
                ('C'), "c:gears/Magnesium", ('D'),
                "c:plates/Titanium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 98), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidsweet_heavy_oil.getInstance().get()),
                ('C'), "c:gears/Magnesium", ('D'),
                "c:plates/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 99), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), IUItem.Uran238,
                ('C'), "c:gears/vanady", ('D'),
                "c:plates/Chromium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 100), "CBC", "BAB", "CBC",
                ('A'), DEFAULT_SENSOR, ('B'), IUItem.iridiumPlate,
                ('C'), IUItem.photoniy_ingot
        );
        Recipes.recipe.addRecipe(
                ModUtils.setSize(IUItem.overclockerUpgrade, 2),
                "CCC",
                "ABA",
                " D ",
                'C',
                IUItem.reactorCoolantSimple,
                'A',
                IUItem.insulatedCopperCableItem,
                'B',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D', IUItem.upgrade_casing

        );
        Recipes.recipe.addRecipe(
                IUItem.reactorCoolantSimple,
                " A ",
                "ABA",
                " A ",
                'B',
                IUItem.cooling_mixture,
                'A',
                "c:plates/Tin"

        );
        Recipes.recipe.addRecipe(
                IUItem.reactorCoolantTriple,
                " A ",
                "ABA",
                " A ",
                'B',
                IUItem.helium_cooling_mixture,
                'A',
                "c:plates/Nichrome"

        );
        Recipes.recipe.addRecipe(
                IUItem.reactorCoolantSix,
                " A ",
                "ABA",
                " A ",
                'B',
                IUItem.cryogenic_cooling_mixture,
                'A',
                "c:plates/Vitalium"

        );

        Recipes.recipe.addRecipe(
                ModUtils.setSize(IUItem.overclockerUpgrade_1, 2),
                "CCC",
                "ABA",
                "DED",
                'C',
                IUItem.reactorCoolantTriple,
                'A',
                ItemStackHelper.fromData(IUItem.cable, 1, 13),
                'B',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),
                'D', ModUtils.setSize(IUItem.overclockerUpgrade, 1), 'E', IUItem.advnanobox

        );
        Recipes.recipe.addRecipe(
                ModUtils.setSize(IUItem.overclockerUpgrade1, 2),
                "CCC",
                "ABA",
                "DED",
                'C',
                IUItem.reactorCoolantSix,
                'A',
                ItemStackHelper.fromData(IUItem.cable, 1, 1),
                'B',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),
                'D', ModUtils.setSize(IUItem.overclockerUpgrade_1, 1), 'E', IUItem.advQuantumtool

        );

        Recipes.recipe.addRecipe(
                getBlockStack(BlockBaseMachine3.facademechanism),
                " C ",
                "BAB",
                " D ",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 90),
                'A',
                IUItem.machine,
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 66),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44)

        );
        Recipes.recipe.addRecipe(
                IUItem.connect_item.getItemStack(),
                "A A",
                " A ", "C C", 'C',
                "c:ingots/Chromium",
                'A',
                "c:ingots/Titanium"

        );
        Recipes.recipe.addRecipe(
                IUItem.facadeItem.getItemStack(),
                "B B",
                "CAC",
                "D D",
                'C',
                "c:ingots/Chromium",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B', "c:ingots/Iron", 'D', "c:ingots/Germanium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nuclear_bomb),
                "ABA", "BAB", "ABA", 'A', new ItemStack(Blocks.TNT), 'B', ItemStackHelper.fromData(IUItem.nuclear_res, 1)
        );


        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.pump),
                "A A",
                "CBC",
                "A A",
                'A',
                "c:plates/Iron",
                'C',
                "c:ingots/Iron",
                'B',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_pump),
                "A A", "CBC", "A A", 'A', "c:plates/Carbon", 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 344), 'B', IUItem.pump
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 641),
                " AB", "ACD", "BD ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 640), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 639), 'B', "c:ingots/SuperalloyRene", 'C', ItemStackHelper.fromData(IUItem.core, 1, 7)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 195),
                "AFB", "ECE", "BDA",
                'A', "c:plates/Osmiridium",
                'D', ItemStackHelper.fromData(IUItem.core, 1, 5),
                'F', ItemStackHelper.fromData(IUItem.neutronium),
                'B', "c:plates/NiobiumTitanium",
                'C', ItemStackHelper.fromData(IUItem.blockdoublemolecular),
                'E', TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 194),
                "ADA", "BCB", "EFE",
                'A', "c:plates/TantalumTungstenHafnium",
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 68),
                'F', ItemStackHelper.fromData(IUItem.basemachine2, 1, 189),
                'B', "c:plates/NiobiumTitanium",
                'C', ItemStackHelper.fromData(IUItem.basemachine2, 1, 38),
                'E', "c:plates/Duralumin"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.imp_pump),
                "A A", "CBC", "A A", 'A', "c:gems/Topaz", 'C', "c:ingots/Gold", 'B', IUItem.adv_pump
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.per_pump),
                "A A", "CBC", "A A", 'A', "c:gems/Diamond", 'C', "c:plates/Osmium", 'B', IUItem.imp_pump
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.fan),
                "ACA", "CBC", "ACA", 'A', "c:plates/Iron", 'C', "c:plates/Titanium", 'B',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_fan),
                "ACA", "CBC", "ACA", 'A', "c:plates/Electrum", 'C', "c:plates/Titanium", 'B', IUItem.fan
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.imp_fan),
                "ACA", "CBC", "ACA", 'A', "c:plates/Platinum", 'C', "c:plates/Titanium", 'B', IUItem.adv_fan
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.per_fan),
                "ACA", "CBC", "ACA", 'A', "c:plates/Cadmium", 'C', "c:plates/Titanium", 'B', IUItem.imp_fan
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.simple_capacitor_item),
                "AAA", "BBB", "AAA", 'A', "c:ingots/Iron", 'B', "c:plates/Invar"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_capacitor_item),
                "AAA", "BCB", "AAA", 'A', "c:plates/Carbon", 'B', "c:plates/Obsidian", 'C', IUItem.simple_capacitor_item
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.imp_capacitor_item),
                "AAA", "BCB", "AAA", 'A', "c:plates/Steel", 'B', "c:plates/Titanium", 'C', IUItem.adv_capacitor_item
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.per_capacitor_item),
                "AAA", "BCB", "AAA", 'A', "c:plates/Ferromanganese", 'B', "c:plates/Germanium", 'C', IUItem.imp_capacitor_item
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.simple_exchanger_item),
                "ABA", "ABA", "AAA", 'A', "c:plates/Iron", 'B', IUItem.reactor_plate
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_exchanger_item),
                "ABA", "ABA", "AAA", 'A', "c:plates/Aluminium", 'B', IUItem.adv_reactor_plate
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.imp_exchanger_item),
                "ABA", "ABA", "AAA", 'A', "c:plates/Aluminumbronze", 'B', IUItem.imp_reactor_plate
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.per_exchanger_item),
                "ABA", "ABA", "AAA", 'A', "c:plates/Duralumin", 'B', IUItem.per_reactor_plate
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 8),
                "ABA", "BCB", "ABA", 'A', IUItem.quad_uranium_fuel_rod, 'B', "c:plates/Lead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 9),
                "ABA", "BCB", "ABA", 'A', IUItem.quad_mox_fuel_rod, 'B', "c:plates/Lead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 0),
                "ABA", "BCB", "ABA", 'A', IUItem.reactoramericiumQuad, 'B', "c:plates/Lead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 1),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorneptuniumQuad, 'B', "c:plates/Lead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 2),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorcuriumQuad, 'B', "c:plates/Lead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 3),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorcaliforniaQuad, 'B', "c:plates/Lead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 4),
                "ABA", "BCB", "ABA", 'A', IUItem.reactortoriyQuad, 'B', "c:plates/Lead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 5),
                "ABA", "BCB", "ABA", 'A', IUItem.reactormendeleviumQuad, 'B', "c:plates/Lead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 6),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorberkeliumQuad, 'B', "c:plates/Lead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 7),
                "ABA", "BCB", "ABA", 'A', IUItem.reactoreinsteiniumQuad, 'B', "c:plates/Lead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 8),
                "ABA", "BCB", "ABA", 'A', IUItem.reactoruran233Quad, 'B', "c:plates/Lead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 9),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorprotonQuad, 'B', "c:plates/Lead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 10),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorfermiumQuad, 'B', "c:plates/Lead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 11),
                "ABA", "BCB", "ABA", 'A', IUItem.reactornobeliumQuad, 'B', "c:plates/Lead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 12),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorlawrenciumQuad, 'B', "c:plates/Lead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.radiationModule, 1, 0),
                "BCB",
                "DED",
                "BAB",
                'A',
                IUItem.module_schedule,
                'B',
                "c:plates/Tantalum",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                "c:plates/Nickel",
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 1),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 0), 'B', "c:plates/Cadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3), 'D',
                "c:plates/Nickel", 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 2),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 1), 'B', "c:doubleplate/Cadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5), 'D',
                "c:plates/Nickel", 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 3),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 2), 'B', "c:doubleplate/Duralumin", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'D',
                "c:plates/Nickel", 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.radiationModule, 1, 4),
                "BCB",
                "DED",
                "BAB",
                'A',
                IUItem.module_schedule,
                'B',
                "c:plates/Tantalum",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                ItemStackHelper.fromData(IUItem.radiationresources, 1, 0),
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 5),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 4), 'B', "c:plates/Cadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3), 'D',
                ItemStackHelper.fromData(IUItem.radiationresources, 1, 0), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 6),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 5), 'B', "c:doubleplate/Cadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'D',
                ItemStackHelper.fromData(IUItem.radiationresources, 1, 0), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 7),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 6), 'B', "c:doubleplate/Duralumin", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'D',
                ItemStackHelper.fromData(IUItem.radiationresources, 1, 0), 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.radiationModule, 1, 8),
                "BCB",
                "DED",
                "BAB",
                'A',
                IUItem.module_schedule,
                'B',
                "c:plates/Tantalum",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                "c:doubleplate/Alumel",
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 9),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 8), 'B', "c:plates/Cadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4), 'D',
                "c:doubleplate/Alumel", 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 10),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 9), 'B', "c:doubleplate/Cadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'D',
                "c:doubleplate/Alumel", 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 11),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 10), 'B', "c:doubleplate/Duralumin", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'D',
                "c:doubleplate/Alumel", 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.radiationModule, 1, 12),
                "BCB",
                "DED",
                "BAB",
                'A',
                IUItem.module_schedule,
                'B',
                "c:plates/Tantalum",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                ItemStackHelper.fromData(IUItem.vent),
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 13),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 12), 'B', "c:plates/Cadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4), 'D',
                ItemStackHelper.fromData(IUItem.adv_Vent), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 14),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 13), 'B', "c:doubleplate/Cadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'D',
                ItemStackHelper.fromData(IUItem.imp_Vent), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 15),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 14), 'B', "c:doubleplate/Duralumin", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'D',
                ItemStackHelper.fromData(IUItem.per_Vent), 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.radiationModule, 1, 16),
                "BCB",
                "DED",
                "BAB",
                'A',
                IUItem.module_schedule,
                'B',
                "c:plates/Tantalum",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                ItemStackHelper.fromData(IUItem.componentVent),
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 17),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 16), 'B', "c:plates/Cadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4), 'D',
                ItemStackHelper.fromData(IUItem.adv_componentVent), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 18),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 17), 'B', "c:doubleplate/Cadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'D',
                ItemStackHelper.fromData(IUItem.imp_componentVent), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 19),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 18), 'B', "c:doubleplate/Duralumin", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'D',
                ItemStackHelper.fromData(IUItem.per_componentVent), 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.radiationModule, 1, 20),
                "BCB",
                "DED",
                "BAB",
                'A',
                IUItem.module_schedule,
                'B',
                "c:plates/Tantalum",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                ItemStackHelper.fromData(IUItem.heat_exchange),
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 21),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 20), 'B', "c:plates/Cadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4), 'D',
                ItemStackHelper.fromData(IUItem.adv_heat_exchange), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 22),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 21), 'B', "c:doubleplate/Cadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'D',
                ItemStackHelper.fromData(IUItem.imp_heat_exchange), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 23),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 22), 'B', "c:doubleplate/Duralumin", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'D',
                ItemStackHelper.fromData(IUItem.per_heat_exchange), 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.radiationModule, 1, 24),
                "BCB",
                "DED",
                "BAB",
                'A',
                IUItem.module_schedule,
                'B',
                "c:plates/Tantalum",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                ItemStackHelper.fromData(IUItem.capacitor),
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 25),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 24), 'B', "c:plates/Cadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3), 'D',
                ItemStackHelper.fromData(IUItem.adv_capacitor), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 26),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 25), 'B', "c:doubleplate/Cadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'D',
                ItemStackHelper.fromData(IUItem.imp_capacitor), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 27),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 26), 'B', "c:doubleplate/Duralumin", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8), 'D',
                ItemStackHelper.fromData(IUItem.per_capacitor), 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 323),
                "BCB", "BAB", "DDD", 'A', DEFAULT_SENSOR, 'B', "c:plates/Steel", 'D', "c:ingots/Mikhail", 'C', "c:gears/Osmium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 325),
                " C ", "BAB", "DCD", 'A', DEFAULT_SENSOR, 'B', ItemStackHelper.fromData(IUItem.nuclear_res), 'D', "c:plates/Carbon", 'C',
                "c:plates/Lapis"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 328),
                "ECE", "BAB", "DCD", 'A', DEFAULT_SENSOR, 'B', "c:plates/Iron", 'D', "c:plates/Silver", 'C',
                "c:plates/Osmium", 'E', IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 440),
                "   ", "BAB", "DCD", 'A', DEFAULT_SENSOR, 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 386), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 320), 'C',
                "c:plates/Tantalum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 441),
                "CDC", "BAB", "BDB", 'A', DEFAULT_SENSOR, 'B', ItemStackHelper.fromData(IUItem.itemiu, 1, 2), 'D',
                "c:platedense/Lead", 'C',
                "c:plates/Spinel"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 442),
                "CDC", "BAB", "BDB", 'A', DEFAULT_SENSOR, 'B', ItemStackHelper.fromData(IUItem.sunnariumpanel, 1, 0), 'D',
                ItemStackHelper.fromData(IUItem.sunnarium, 1, 3), 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 319)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 354),
                "BAB", "BAB", "BDB", 'A', "c:gems/Ruby", 'B', "c:plates/Titanium", 'D',
                "c:dusts/Redstone"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 1),
                " ", "CAD", " B ", 'A', "c:machineBlock", 'B', IUItem.elemotor, 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 36),
                'C', ItemStackHelper.fromData(IUItem.module7, 1, 9)
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 20),
                " ",
                "CAD",
                " B ",
                'A',
                "c:machineBlockAdvanced",
                'B',
                IUItem.elemotor,
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 36),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 226)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 91),
                " ", " A ", " B ", 'A', IUItem.blockpanel, 'B', IUItem.module_schedule
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 94),
                " F ",
                "CAD",
                " B ",
                'A',
                "c:machineBlock",
                'B',
                IUItem.elemotor,
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                'F',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 323)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 93),
                "DEF",
                "CAC",
                " B ",
                'A',
                "c:machineBlock",
                'B',
                IUItem.elemotor,
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 433),
                'F',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 323)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 95),
                "   ",
                "CAD",
                " B ",
                'A',
                "c:machineBlock",
                'B',
                IUItem.elemotor,
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 442),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 98),
                "   ",
                "CAD",
                " B ",
                'A',
                "c:machineBlock",
                'B',
                IUItem.elemotor,
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 30),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 96),
                "CDC",
                "CAC",
                "B H",
                'A',
                "c:machineBlock",
                'B',
                IUItem.elemotor,
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 354),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44), 'H', getBlockStack(BlockBaseMachine3.steam_sharpener)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 99),
                " E ",
                "DAC",
                " B ",
                'A',
                "c:machineBlock",
                'B',
                IUItem.elemotor,
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 328),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 97),
                "   ",
                "DAC",
                " B ",
                'A',
                "c:machineBlock",
                'B',
                IUItem.elemotor,
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 440),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 100),
                " E ",
                "DAC",
                " B ",
                'A',
                "c:machineBlock",
                'B',
                IUItem.elemotor,
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 325)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 112),
                " B ",
                " A ",
                "  ",
                'A',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 9),
                'B',
                ItemStackHelper.fromData(IUItem.module7, 1)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 118),
                " B ",
                "CAC",
                "CDC",
                'A',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 469),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 469),
                "BBB",
                "CAC",
                "DBD",
                'A',
                DEFAULT_SENSOR,
                'B',
                "c:plates/Osmium",
                'C',
                "c:plates/Neodymium",
                'D',
                "c:plates/Obsidian"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 101),
                " E ",
                "DAC",
                " B ",
                'A',
                "c:machineBlock",
                'B',
                IUItem.elemotor,
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 36),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 441)
        );


        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 113),
                " B ",
                " A ",
                " C ",
                'A',
                "c:machineBlock",
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 243),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 36)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 114),
                "B B",
                " A ",
                "B B",
                'A',
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 113),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 115),
                "B B",
                " A ",
                "B B",
                'A',
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 114),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 116),
                "B B",
                " A ",
                "B B",
                'A',
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 115),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140)
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 117),
                " A ",
                "BCD",
                " E ",
                'A',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 7),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 471),
                'C',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 9),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 99),
                'E',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 119),
                "   ",
                "BCD",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 7),
                'B',
                ItemStackHelper.fromData(IUItem.expmodule),
                'C',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 9),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 35),
                'E',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 123),
                " A ",
                " B ",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 458),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 9),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 35),
                'E',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 122),
                " A ",
                " B ",
                " C ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 458),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 35),
                'E',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 121),
                " A ",
                " B ",
                " C ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 458),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 60),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 35),
                'E',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 120),
                "   ",
                "ABC",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.recipe_schedule),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 128),
                'D',
                ItemStackHelper.fromData(IUItem.motors_with_improved_bearings_),
                'E',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 471),
                " A ",
                "BCB",
                "DDD",
                'A',
                ItemStackHelper.fromData(IUItem.nuclear_bomb),
                'B',
                ItemStackHelper.fromData(IUItem.radiationresources, 1, 3),
                'C',
                DEFAULT_SENSOR,
                'D',
                ItemStackHelper.fromData(IUItem.alloysplate, 1, 18)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 129),
                "   ",
                "ABC",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 202),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 9),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 90),
                'D',
                ItemStackHelper.fromData(IUItem.motors_with_improved_bearings_),
                'E',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 128),
                "ACE",
                " B ",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 202),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 9),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 90),
                'D',
                ItemStackHelper.fromData(IUItem.motors_with_improved_bearings_),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 251)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 193),
                "ACE",
                " B ",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 202),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 9),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 90),
                'D',
                ItemStackHelper.fromData(IUItem.motors_with_improved_bearings_),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 263)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 126),
                "AAA",
                "CBE",
                " D ",
                'A',
                "c:plates/Inconel",
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 9),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 90),
                'D',
                ItemStackHelper.fromData(IUItem.motors_with_improved_bearings_),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 614)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 2, 650),
                " A ",
                "CBC",
                "C C",
                'A',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 13),
                'B',
                new ItemStack(Items.REDSTONE),
                'C',
                "c:casings/Nichrome"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 2, 651),
                " A ",
                "CBC",
                "C C",
                'A',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 13),
                'B',
                new ItemStack(Items.REDSTONE),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 505)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 2, 652),
                " A ",
                "CBC",
                "C C",
                'A',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 13),
                'B',
                new ItemStack(Items.REDSTONE),
                'C',
                "c:casings/Orichalcum"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 2, 653),
                " A ",
                "CBC",
                "C C",
                'A',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 13),
                'B',
                new ItemStack(Items.REDSTONE),
                'C',
                "c:casings/Inconel"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 2, 654),
                " A ",
                "CBC",
                "C C",
                'A',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 13),
                'B',
                new ItemStack(Items.REDSTONE),
                'C',
                "c:casings/Adamantium"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 130),
                "AAA",
                "CBE",
                " D ",
                'A',
                "c:plates/Inconel",
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 9),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 242),
                'D',
                ItemStackHelper.fromData(IUItem.reactorData),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 90)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 140),
                "AEA",
                "CBG",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 153),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72),
                'G',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 162),
                "   ",
                "CBG",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 469),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72),
                'G',
                ItemStackHelper.fromData(IUItem.bags)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 150),
                " A ",
                "CBG",
                "E D",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'D',
                getBlockStack(BlockBaseMachine3.steamdryer),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'G',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 151),
                "   ",
                "CBG",
                "A D",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'D',
                getBlockStack(BlockBaseMachine3.steam_squeezer),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72),
                'G',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 156),
                "CAC",
                "CBC",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 267),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72),
                'G',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 156),
                "CAC",
                "CBC",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 267),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72),
                'G',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 157),
                "EAC",
                "CBC",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 267),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'G',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 158),
                " A ",
                " B ",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.primalFluidHeater),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'G',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 159),
                "CAC",
                "CBC",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.primalSiliconCrystal),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 495),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'G',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 173),
                "A E",
                "CBC",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 65)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 176),
                " A ",
                "EBC",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 267),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                'G',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 127),
                "   ",
                "EBC",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 267),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                IUItem.ejectorUpgrade,
                'D',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'E',
                IUItem.pullingUpgrade,
                'G',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 26),
                " A ",
                "EBC",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.gasChamber),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 9),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 263),
                'D',
                ItemStackHelper.fromData(IUItem.motors_with_improved_bearings_),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 267),
                'G',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 133),
                "A A",
                "ABA",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 365),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 130),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 100)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 134),
                "A A",
                "ABA",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 363),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 130),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 100)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 137),
                "   ",
                "CBA",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 54),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 29),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 100)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 138),
                " E ",
                "CBA",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 9),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 459)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 135),
                "EEE",
                " B ",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 33),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.cutter, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 625),
                "B B",
                "CAC",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.spectral_box),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                ItemStackHelper.fromData(IUItem.alloysdoubleplate, 1, 5),
                'C',
                ItemStackHelper.fromData(IUItem.alloysdoubleplate, 1, 14),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.cutter, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623),
                "AC ",
                "BB ",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                "c:plates/Nimonic",
                'C',
                "c:plates/SuperalloyRene",
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.cutter, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 621),
                "AAA",
                "BCB",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.plate, 1, 44),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                ItemStackHelper.fromData(IUItem.alloysplate, 1, 5),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 116),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.cutter, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 622),
                " A ",
                "CDC",
                " B ",
                'A',
                "c:plates/Tantalum",
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 621),
                'C',
                "c:plates/Arsenic",
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                'E',
                ItemStackHelper.fromData(IUItem.cutter, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 620),
                "AAA",
                "BDB",
                "ECE",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 625),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 621),
                'C',
                ItemStackHelper.fromData(IUItem.basecircuit, 1, 21),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 24),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 654)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 132),
                "CAC",
                "CBC",
                "DDD",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 88),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 9),
                'C',
                ItemStackHelper.fromData(IUItem.plate, 1, 44),
                'D',
                ItemStackHelper.fromData(IUItem.plate, 1, 29),
                'E',
                ItemStackHelper.fromData(IUItem.cutter, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 136),
                " E ",
                " B ",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 29)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 459),
                " E ",
                "ABA",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.machinekit),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                DEFAULT_SENSOR,
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                IUItem.overclockerUpgrade
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 177),
                "ACE",
                "FBC",
                " DH",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 267), 'H',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 265)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 470),
                "AAA",
                "CBC",
                "   ",
                'A',
                "c:plates/Nimonic",
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                DEFAULT_SENSOR,
                'C',
                ItemStackHelper.fromData(IUItem.alloygear, 1, 18),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 100)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 131),
                " C ",
                " B ",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 130),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 470),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 100)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 174),
                "   ",
                "EBA",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 267),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                'G',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 161),
                " A ",
                " B ",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 68),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 9),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 495),
                'D',
                ItemStackHelper.fromData(IUItem.imp_motors_with_improved_bearings_),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'G',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 160), "   ", "DAD", "BCB",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('C'), IUItem.iridiumPlate,

                ('B'), ItemStackHelper.fromData(IUItem.photoniy),

                ('A'),
                ItemStackHelper.fromData(IUItem.convertersolidmatter)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 125),
                "   ",
                "CBE",
                " D ",
                'A',
                "c:plates/Inconel",
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 9),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 240),
                'D',
                ItemStackHelper.fromData(IUItem.imp_motors_with_improved_bearings_),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 90)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 141),
                "AFA",
                "CBE",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 154),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 142),
                "AFA",
                "CBE",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 154),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 143),
                "AAA",
                "CBE",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 601),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 154),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 144),
                "AFA",
                "CBE",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 601),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 154),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 98),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 79)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 145),
                "ACE",
                "FBC",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 100)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 146),
                "ACE",
                "FBC",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 65)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 147),
                " A ",
                " B ",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                getBlockStack(BlockBaseMachine2.electrolyzer_iu),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'D',
                IUItem.motors_with_improved_bearings_,
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 65)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 148),
                "CAC",
                "EBF",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 251),
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 154),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'D',
                IUItem.motors_with_improved_bearings_,
                'E',
                ItemStackHelper.fromData(IUItem.module7, 1, 9)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 149),
                "CAC",
                "EBF",
                " D ",
                'A',
                IUItem.antisoilpollution,
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 154),
                'B',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'D',
                IUItem.motors_with_improved_bearings_,
                'E',
                ItemStackHelper.fromData(IUItem.module7, 1, 9)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 458),
                "   ",
                "BCB",
                "DDD",
                'A',
                ItemStackHelper.fromData(IUItem.nuclear_bomb),
                'B',
                ItemStackHelper.fromData(IUItem.alloysplate, 1, 6),
                'C',
                DEFAULT_SENSOR,
                'D',
                ItemStackHelper.fromData(IUItem.alloysplate, 1, 30)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 102),
                "   ", "BAC", " D ", 'A', "c:machineBlockAdvanced", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 439), 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 36), 'D', IUItem.elemotor

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 104),
                "FEF", "BAC", " D ", 'A', "c:machineBlockAdvanced", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47), 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 35), 'D', IUItem.elemotor, 'E', Items.ENCHANTED_BOOK, 'F',
                "c:doubleplate/Cadmium"

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 105),
                " E ", "BAC", " D ", 'A', "c:machineBlock", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44), 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43), 'D', IUItem.elemotor, 'E', IUItem.coolant

        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 103),
                "FBG",
                "EAC",
                " D ",
                'A',
                "c:machineBlockAdvanced",
                'B',
                ItemStackHelper.fromData(IUItem.nuclear_res, 1, 8),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 31),
                'D',
                IUItem.elemotor,
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1
                        , 36),
                'F',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                'G',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47)

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.earthQuarry, 1, 2),
                "C C", " B ", "A A", 'B', "c:machineBlock", 'A', "c:plates/StainlessSteel", 'C', "c:plates/MolybdenumSteel"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.earthQuarry, 1),
                "DBD",
                " A ",
                " C ",
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 42),
                'A',
                ItemStackHelper.fromData(IUItem.earthQuarry, 1, 2)
                ,
                'C'
                ,
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                "c:rods/Zinc"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.earthQuarry, 1, 1),
                "DBD",
                "EAE",
                " C ",
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 226),
                'A',
                ItemStackHelper.fromData(IUItem.earthQuarry, 1, 2)
                ,
                'C'
                ,
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                "c:gears/Osmium",
                'E',
                "c:plates/Carbon"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.earthQuarry, 1, 3),
                " E ", "DAD", "CBC", 'B', ItemStackHelper.fromData(IUItem.oilquarry), 'A', ItemStackHelper.fromData(IUItem.earthQuarry, 1, 2)
                , 'C'
                , "c:plates/Titanium", 'D', "c:plates/Steel", 'E', "c:plates/Iridium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.earthQuarry, 1, 4),
                "   ", "DAD", "CBC", 'B', new ItemStack(Blocks.CHEST), 'A', ItemStackHelper.fromData(IUItem.earthQuarry, 1, 2)
                , 'C'
                , "c:plates/Gold", 'D', "c:plates/Tin"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.earthQuarry, 1, 5),
                "BDB", "DAD", "BDB", 'B', ItemStackHelper.fromData(IUItem.item_pipes, 1, 1), 'D', ItemStackHelper.fromData(IUItem.item_pipes), 'A',
                ItemStackHelper.fromData(IUItem.earthQuarry, 1, 2)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.anvil),
                "AAA", " A ", "BBB", 'A', "c:storage_blocks/Iron", 'B', "c:storage_blocks/Titanium"
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 364),
                "AB ", "BA ", "   ", 'A', "c:plates/Lead", 'B', "c:plates/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 363),
                "AB ", "BA ", "CC ", 'A', "c:plates/Lead", 'B', "c:plates/Tungsten", 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 479)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 365),
                "AB ", "BA ", "   ", 'A', "c:plates/Lead", 'B', "c:plates/Lithium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 366),
                "AB ", "BC ", "   ", 'A', "c:plates/Lead", 'B', "c:plates/Platinum", 'C', "c:plates/Tantalum"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 2, 420),
                "AA ",
                "BC ",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 366),
                'B',
                "c:plates/Electrum",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 453)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 2, 378),
                "AA ",
                "BC ",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 420),
                'B',
                "c:plates/Cobalt",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 453)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 2, 405),
                "AA ",
                "BC ",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 378),
                'B',
                "c:plates/Magnesium",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 453)
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 2, 419),
                "AA ",
                "BC ",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 365),
                'B',
                "c:plates/Electrum",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 453)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 2, 377),
                "AA ",
                "BC ",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 419),
                'B',
                "c:plates/Cobalt",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 453)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 2, 394),
                "AA ",
                "BC ",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 377),
                'B',
                "c:plates/Magnesium",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 453)
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 2, 417),
                "AA ",
                "BC ",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 363),
                'B',
                "c:plates/Electrum",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 453)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 2, 375),
                "AA ",
                "BC ",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 417),
                'B',
                "c:plates/Cobalt",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 453)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 2, 392),
                "AA ",
                "BC ",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 375),
                'B',
                "c:plates/Magnesium",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 453)
        );


        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 2, 418),
                "AA ",
                "BC ",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 364),
                'B',
                "c:plates/Electrum",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 453)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 2, 376),
                "AA ",
                "BC ",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 418),
                'B',
                "c:plates/Cobalt",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 453)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 393),
                "AA ", "BC ", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 376), 'B', "c:plates/Magnesium", 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 453)
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 439),
                "CEC",
                "EAE",
                " B ",
                'A',
                DEFAULT_SENSOR,
                'B',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                'C',
                "c:plates/Bronze",
                'E',
                "c:doubleplate/Osmium"
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 435),
                "BCB",
                "BAB",
                "BDB",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B',
                "c:plates/Platinum",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 42),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 366)
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 436),
                "BCB",
                "BAB",
                "BDB",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B',
                "c:plates/Zinc",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 42),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 365)
        );


        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 342),
                "CBC",
                "BAB",
                "DBD",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B',
                "c:plates/Lithium",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 386),
                'D',
                "c:plates/Obsidian"
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 324),
                "AAA",
                " B ",
                "DCD",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'A',
                "c:plates/Cobalt",
                'B',
                IUItem.advancedAlloy,
                'D',
                "c:plates/Carbon"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 322),
                "ABA",
                "EFE",
                "DCD",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'A',
                "c:plates/Tungsten",
                'B',
                IUItem.advancedAlloy,
                'D',
                "c:plates/Bronze",
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 445),
                'F',
                "c:plates/Tin"
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 385),
                "A A",
                "EFG",
                "DCD",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'A',
                "c:plates/Steel",
                'D',
                "c:plates/Bor",
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 445),
                'F',
                "c:plates/Carbon",
                'G',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 446)
        );


        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 321),
                "B B",
                "DAD",
                "B B",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 320),
                'D',
                "c:plates/Cadmium"
        );


        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 327),
                "BCB",
                "DAD",
                "BCB",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B',
                "c:plates/Carbon",
                'D',
                "c:plates/Osmium",
                'C',
                "c:plates/Electrum"
        );


        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 326),
                "BCB",
                "BAB",
                "DCD",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B',
                "c:plates/Cobalt",
                'D',
                "c:plates/Vanadoalumite",
                'C',
                "c:plates/Manganese"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356),
                " B ", " A ", " B ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 294), 'B', "c:plates/Iron"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424),
                "AAA", "ABA", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356), 'B', "c:plates/Electrum"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371),
                "AAA", "ABA", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424), 'B', "c:plates/Platinum"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397),
                "AAA", "ABA", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371), 'B', "c:plates/Spinel"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 387),
                " A ", "ABA", " A ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356), 'B', "c:doubleplate/Germanium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 425),
                " A ", "ABA", " A ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 387), 'B', "c:doubleplate/Alumel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 372),
                " A ", "ABA", " A ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 425), 'B', "c:doubleplate/Vitalium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 398),
                " A ", "ABA", " A ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 372), 'B', "c:doubleplate/Ferromanganese"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 362),
                "CDC", "BAB", "CDC", 'A', ItemStackHelper.fromData(IUItem.itemiu, 1, 3), 'B', ItemStackHelper.fromData(IUItem.neutroniumingot), 'C',
                "c:plates/Germanium", 'D', "c:plates/Osmium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 101), "CCC", "BAB", "DED",

                ('A'), DEFAULT_SENSOR,

                ('C'), "c:gears/Germanium",

                ('B'), "c:doubleplate/Germanium",

                ('D'),
                "c:gears/Nichrome",

                ('E'), "c:doubleplate/Vitalium"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 4),
                "CCC",
                "BAB",
                "DED",
                ('A'),
                ADV_SENSOR,
                ('C'),
                "c:gears/Germanium",
                ('B'),
                "c:doubleplate/Germanium",
                ('D'),
                "c:gears/Nichrome",
                ('E'),
                IUItem.iridiumOre
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 81),
                "CCC",
                "BAB",
                "DED",
                ('A'),
                IMP_SENSOR,
                ('C'),
                "c:gears/Germanium",
                ('B'),
                "c:doubleplate/Germanium",
                ('D'),
                "c:gears/Nichrome",
                ('E'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 105),
                "CCC",
                "BAB",
                "DED",
                ('A'),
                PER_SENSOR,
                ('C'),
                "c:gears/Germanium",
                ('B'),
                "c:doubleplate/Germanium",
                ('D'),
                "c:gears/Nichrome",
                ('E'),
                IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 112), "BBB", "CAC", "   ",

                ('A'), DEFAULT_SENSOR,

                ('B'), IUItem.photoniy_ingot,

                ('C'), "c:gears/Zinc"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 124), "CBC", "BAB", " B ",

                ('A'), DEFAULT_SENSOR,

                ('B'), "c:doubleplate/Vitalium",

                ('C'), "c:doubleplate/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 135), "CBC", "BAB", " B ",
                ('A'), ADV_SENSOR, ('B'), "c:doubleplate/Vitalium",
                ('C'), "c:doubleplate/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 146), "CBC", "BAB", " B ",
                ('A'), IMP_SENSOR, ('B'), "c:doubleplate/Vitalium",
                ('C'), "c:doubleplate/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 157), "CBC", "BAB", " B ",
                ('A'), PER_SENSOR, ('B'), "c:doubleplate/Vitalium",
                ('C'), "c:doubleplate/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 632), "CBC", "BAB", " B ",
                ('A'), PHOTON_SENSOR, ('B'), "c:doubleplate/Vitalium",
                ('C'), "c:doubleplate/Invar"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crop, 4), "A A", "A A", "   ",

                ('A'), Items.STICK


        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 128), "DCD", "BAB", " E ",

                ('A'), DEFAULT_SENSOR,

                ('C'), Blocks.CRAFTING_TABLE,

                ('B'), "c:plates/Platinum",

                ('D'),
                "c:plates/Tin",

                ('E'), "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 129), "DCD", "BAB", " E ",
                ('A'), ADV_SENSOR, ('C'), Blocks.CRAFTING_TABLE,
                ('B'), "c:plates/Platinum", ('D'),
                "c:plates/Tin", ('E'), "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 130), "DCD", "BAB", " E ",
                ('A'), IMP_SENSOR, ('C'), Blocks.CRAFTING_TABLE,
                ('B'), "c:plates/Platinum", ('D'),
                "c:plates/Tin", ('E'), "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 131), "DCD", "BAB", " E ",
                ('A'), PER_SENSOR, ('C'), Blocks.CRAFTING_TABLE,
                ('B'), "c:plates/Platinum", ('D'),
                "c:plates/Tin", ('E'), "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 605), "DCD", "BAB", " E ",
                ('A'), PHOTON_SENSOR, ('C'), Blocks.CRAFTING_TABLE,
                ('B'), "c:plates/Platinum", ('D'),
                "c:plates/Tin", ('E'), "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 154), "CCC", "BAB", "   ",

                ('A'), DEFAULT_SENSOR,

                ('C'), IUItem.FluidCell,

                ('B'), "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 155), "CCC", "BAB", "   ",

                ('A'), DEFAULT_SENSOR,

                ('C'),
                ModUtils.getCellFromFluid(FluidName.fluidcoolant.getInstance().get()),
                ('B'), "c:plates/Nickel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 156), " C ", "BAB", " D ",
                ('A'), DEFAULT_SENSOR, ('B'), "c:plates/Germanium",
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 60), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 158), "CDE", "BAB", "FFF",

                ('A'), DEFAULT_SENSOR,

                ('C'), Items.DIAMOND_AXE,

                ('D'), Items.DIAMOND_PICKAXE,

                ('E'),
                Items.DIAMOND_SHOVEL,

                ('B'), "c:gears/Iridium",

                ('F'), "c:gears/Magnesium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 170), "DCD", "BAB", " C ",
                ('A'), DEFAULT_SENSOR, ('B'), Blocks.DAYLIGHT_DETECTOR,
                ('C'), IUItem.advancedMachine, ('D'),
                IUItem.reinforcedGlass
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 171), "DCD", "BAB", " C ",
                ('A'), ADV_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.core, 1, 3),
                ('C'), IUItem.advancedMachine, ('D'),
                IUItem.reinforcedGlass
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 172), "DCD", "BAB", " C ",
                ('A'), IMP_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.core, 1, 4),
                ('C'), IUItem.advancedMachine, ('D'),
                IUItem.reinforcedGlass
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 173), "DCD", "BAB", " C ",
                ('A'), PER_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.core, 1, 5),
                ('C'), IUItem.advancedMachine, ('D'),
                IUItem.reinforcedGlass
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 635), "DCD", "BAB", " C ",
                ('A'), PHOTON_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.core, 1, 5),
                ('C'), IUItem.advancedMachine, ('D'),
                IUItem.reinforcedGlass
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 90),
                "BBB",
                "DAD",
                "EEE",
                ('A'),
                DEFAULT_SENSOR,
                ('B'),
                "c:gears/Mikhail",
                ('D'),
                "c:gears/Platinum",
                ('C'),
                IUItem.carbonPlate,
                ('E'),
                "c:doubleplate/Redbrass"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 79), "CCC", "BAB", "EDE",

                ('A'), DEFAULT_SENSOR,

                ('B'), IUItem.advancedAlloy,

                ('C'), IUItem.carbonPlate,

                ('D'),
                IUItem.toriy,

                ('E'), "c:plates/Bronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 176), "CCC", "BAB", "EDE",

                ('A'), ADV_SENSOR,

                ('B'), IUItem.advancedAlloy,

                ('C'), IUItem.iridiumPlate,

                ('D'),
                ItemStackHelper.fromData(IUItem.radiationresources, 1, 4),

                ('E'), "c:plates/Bronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 174), "CCC", "BAB", "EDE",

                ('A'), IMP_SENSOR,

                ('B'), IUItem.advancedAlloy,

                ('C'), IUItem.iridiumPlate,

                ('D'),
                ItemStackHelper.fromData(IUItem.radiationresources, 1, 4),

                ('E'), "c:plates/Bronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 175), "CCC", "BAB", "EDE",

                ('A'), PER_SENSOR,

                ('B'), IUItem.advancedAlloy,

                ('C'), IUItem.iridiumPlate,

                ('D'),
                ItemStackHelper.fromData(IUItem.radiationresources, 1, 4),

                ('E'), "c:plates/Bronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 627), "CCC", "BAB", "EDE",

                ('A'), PHOTON_SENSOR,

                ('B'), IUItem.advancedAlloy,

                ('C'), IUItem.iridiumPlate,

                ('D'),
                ItemStackHelper.fromData(IUItem.radiationresources, 1, 4),

                ('E'), "c:plates/Bronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 217), " C ", "BAB", "DDD",

                ('A'), DEFAULT_SENSOR,

                ('B'), "c:plates/Alumel",

                ('C'), Items.FISHING_ROD,

                ('D'),
                "c:gears/Muntsa"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 219), " C ", "BAB", "DDD",
                ('A'), DEFAULT_SENSOR, ('B'), "c:plates/Iron", ('D'), Items.REDSTONE,
                ('C'),
                Blocks.FURNACE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 225), " C ", "BAB", "DDD",
                ('A'), ADV_SENSOR, ('B'), "c:plates/Iron", ('D'), Items.REDSTONE,
                ('C'),
                Blocks.FURNACE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 221), " C ", "BAB", "DDD",
                ('A'), IMP_SENSOR, ('B'), "c:plates/Iron", ('D'), Items.REDSTONE,
                ('C'),
                Blocks.FURNACE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 223), " C ", "BAB", "DDD",
                ('A'), PER_SENSOR, ('B'), "c:plates/Iron", ('D'), Items.REDSTONE,
                ('C'),
                Blocks.FURNACE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 617), " C ", "BAB", "DDD",
                ('A'), PHOTON_SENSOR, ('B'), "c:plates/Iron", ('D'), Items.REDSTONE,
                ('C'),
                Blocks.FURNACE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 218), "CCC", "BAB", "DDD",
                ('A'), DEFAULT_SENSOR, ('B'), "c:plates/Iron", ('C'), "c:plates/Tin",
                ('D'),
                "c:plates/Copper"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 224), "CCC", "BAB", "DDD",
                ('A'), ADV_SENSOR, ('B'), "c:plates/Iron", ('C'), "c:plates/Tin",
                ('D'),
                "c:plates/Copper"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 220), "CCC", "BAB", "DDD",
                ('A'), IMP_SENSOR, ('B'), "c:plates/Iron", ('C'), "c:plates/Tin",
                ('D'),
                "c:plates/Copper"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 222), "CCC", "BAB", "DDD",
                ('A'), PER_SENSOR, ('B'), "c:plates/Iron", ('C'), "c:plates/Tin",
                ('D'),
                "c:plates/Copper"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 606), "CCC", "BAB", "DDD",
                ('A'), PHOTON_SENSOR, ('B'), "c:plates/Iron", ('C'), "c:plates/Tin",
                ('D'),
                "c:plates/Copper"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 205), "CCC", "BAB", "DED",
                ('A'), DEFAULT_SENSOR,
                ('B'), "c:gears/Cobalt", ('C'), IUItem.advancedAlloy, ('D'),
                "c:plates/Titanium", 'E', IUItem.plastic_plate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 204), "CCC", "BAB", "DED",
                ('A'), ADV_SENSOR,
                ('B'), "c:gears/Cobalt", ('C'), IUItem.advancedAlloy, ('D'),
                "c:plates/Titanium", 'E', IUItem.plastic_plate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 202), "CCC", "BAB", "DED",
                ('A'), IMP_SENSOR,
                ('B'), "c:gears/Cobalt", ('C'), IUItem.advancedAlloy, ('D'),
                "c:plates/Titanium", 'E', IUItem.plastic_plate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 203), "CCC", "BAB", "DED",
                ('A'), PER_SENSOR,
                ('B'), "c:gears/Cobalt", ('C'), IUItem.advancedAlloy, ('D'),
                "c:plates/Titanium", 'E', IUItem.plastic_plate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 633), "CCC", "BAB", "DED",
                ('A'), PHOTON_SENSOR,
                ('B'), "c:gears/Cobalt", ('C'), IUItem.advancedAlloy, ('D'),
                "c:plates/Titanium", 'E', IUItem.plastic_plate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 38), "CCC", "BAB", "DDD",

                ('A'), DEFAULT_SENSOR,

                ('B'), "c:doubleplate/Germanium",

                ('C'), "c:gears/Vitalium",

                ('D'),
                "c:gears/Alcled"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 14), "CCC", "BAB", "DDD",
                ('A'), ADV_SENSOR, ('B'), "c:doubleplate/Germanium",
                ('C'), "c:gears/Vitalium", ('D'),
                "c:gears/Alcled"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 89), "CCC", "BAB", "DDD",
                ('A'), IMP_SENSOR, ('B'), "c:doubleplate/Germanium",
                ('C'), "c:gears/Vitalium", ('D'),
                "c:gears/Alcled"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 114), "CCC", "BAB", "DDD",

                ('A'), PER_SENSOR,

                ('B'), "c:doubleplate/Germanium",

                ('C'), "c:gears/Vitalium",

                ('D'),
                "c:gears/Alcled"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.universal_cable, 1, 1), " A ", "BCB", " A ",

                ('C'),
                ("c:ingots/Cobalt"),
                ('A'), ItemStackHelper.fromData(IUItem.universal_cable, 1, 0),

                ('B'), IUItem.denseplatetin
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.universal_cable, 1, 2), " A ", "BCB", " A ",

                ('C'), IUItem.denseplatetin,

                ('A'), ItemStackHelper.fromData(IUItem.universal_cable, 1, 1),

                ('B'), IUItem.advancedAlloy
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.universal_cable, 1, 3), "DAD", "BCB", "DAD",

                ('D'), IUItem.denseplategold,

                ('C'), IUItem.advancedAlloy,

                ('A'), ItemStackHelper.fromData(IUItem.universal_cable, 1, 2),

                ('B'),
                IUItem.denseplatelead
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.universal_cable, 1, 4), "DAD", "BCB", "DAD",

                ('D'),
                ("c:ingots/Redbrass"),
                ('C'), IUItem.carbonPlate,

                ('A'), ItemStackHelper.fromData(IUItem.universal_cable, 1, 3),

                ('B'),
                ("c:ingots/Spinel")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.universal_cable, 1, 5), " A ", "BCB", " A ",

                ('C'),
                ("c:doubleplate/Vitalium"),
                ('A'), ItemStackHelper.fromData(IUItem.universal_cable, 1, 4),

                ('B'), IUItem.denseplateadviron
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.universal_cable, 1, 6), "DAD", "BCB", "DAD",

                ('D'), IUItem.carbonPlate,

                ('C'),
                ("c:ingots/Alcled"),
                ('A'), ItemStackHelper.fromData(IUItem.universal_cable, 1, 5),

                ('B'),
                ("c:ingots/Duralumin")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.universal_cable, 1, 7), "A A", "BCB", "A A",

                ('C'), ItemStackHelper.fromData(IUItem.photoniy_ingot),

                ('B'), ItemStackHelper.fromData(IUItem.photoniy),

                ('A'), ItemStackHelper.fromData(IUItem.universal_cable, 1, 6)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.universal_cable, 1, 8), "BBB", "AAA", "BBB",
                ('A'), ItemStackHelper.fromData(IUItem.photoniy_ingot), ('B'), ItemStackHelper.fromData(
                        IUItem.universal_cable,
                        1,
                        7
                )
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.universal_cable, 1, 9), "BBB", "ACA", "BBB",

                ('C'), ItemStackHelper.fromData(IUItem.basecircuit, 1, 10),

                ('A'), ItemStackHelper.fromData(IUItem.photoniy_ingot),

                ('B'), ItemStackHelper.fromData(IUItem.universal_cable, 1, 8)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cokeoven, 1, 1), "CAC", "ABA", "CAC",

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('A'), ItemStackHelper.fromData(IUItem.cokeoven, 1, 5),
                'C', "c:gears/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cokeoven, 1, 4), " A ", "ABA", " A ",

                ('B'),
                getBlockStack(BlockBaseMachine3.steel_tank),
                ('A'), ItemStackHelper.fromData(IUItem.cokeoven, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cokeoven, 1, 3), "CAC", "ABA", "CAC",

                ('B'), getBlockStack(BlockBaseMachine3.steel_tank),
                ('A'), ItemStackHelper.fromData(IUItem.cokeoven, 1, 5),
                'C', "c:gears/Electrum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.recipe_schedule), "AB ", "BAB", "BBA",

                ('B'), "c:plates/AluminiumSilicon",
                ('A'), "c:plates/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cokeoven, 1, 5), " B ", "CAC", " B ",

                ('C'), "c:casings/AluminiumSilicon",

                ('A'), new ItemStack(Blocks.BRICKS),

                ('B'), "c:casings/HafniumCarbide"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cokeoven, 1, 2), " A ", " B ", "   ",

                ('C'), "c:plates/AluminiumSilicon",

                ('A'), ItemStackHelper.fromData(IUItem.primalFluidHeater),

                ('B'), ItemStackHelper.fromData(IUItem.cokeoven, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cokeoven, 1, 0), "DED", "CAC", " B ",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 387),

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),

                ('B'), ItemStackHelper.fromData(IUItem.cokeoven, 1, 5),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 320),
                'E', ItemStackHelper.fromData(IUItem.itemiu, 1, 0)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 190), " ", "ABC", " D ",

                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 36),
                'B', ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 441),
                'D', ItemStackHelper.fromData(IUItem.motors_with_improved_bearings_)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.antiairpollution), "CCC", "ABA", " D ",

                'A', IUItem.polonium_palladium_composite,
                'B', ItemStackHelper.fromData(IUItem.module_schedule),
                'C', ItemStackHelper.fromData(IUItem.alloysplate, 1, 5),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.antisoilpollution), "CCC", "ABA", " D ",

                'A', IUItem.polonium_palladium_composite,
                'B', ItemStackHelper.fromData(IUItem.module_schedule),
                'C', ItemStackHelper.fromData(IUItem.alloysplate, 1, 11),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.antiairpollution1), "CCC", "ABA", "ADA",

                'A', IUItem.polonium_palladium_composite,
                'B', ItemStackHelper.fromData(IUItem.module_schedule),
                'C', ItemStackHelper.fromData(IUItem.alloysdoubleplate, 1, 5),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.antisoilpollution1), "CCC", "ABA", "ADA",

                'A', IUItem.polonium_palladium_composite,
                'B', ItemStackHelper.fromData(IUItem.module_schedule),
                'C', ItemStackHelper.fromData(IUItem.alloysdoubleplate, 1, 11),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 191), "   ", "ABC", " D ",

                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 36),
                'B', ItemStackHelper.fromData(IUItem.blockResource, 1, 9),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 441),
                'D', IUItem.motors_with_improved_bearings_,
                'E', ItemStackHelper.fromData(IUItem.module7, 1, 9)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 139), " B ", " A ", " C ",

                'A', ItemStackHelper.fromData(IUItem.oiladvrefiner),
                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 86),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                'D', IUItem.motors_with_improved_bearings_,
                'E', ItemStackHelper.fromData(IUItem.module7, 1, 9)
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 569), "AAA", "BDB", "AAA",

                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 568), 'A', "c:plates/Iron", 'C', "c:plates/Zinc", 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 581)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 600), "AAA", "BDB", "AAA",

                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 567), 'A', "c:plates/Electrum", 'C', "c:plates/Zinc", 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 579)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 591), "AAA", "BDB", "AAA",

                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 565), 'A', "c:plates/Platinum", 'C', "c:plates/Zinc", 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 563)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 561), "AAA", "BDB", "AAA",

                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 565), 'A', "c:plates/HafniumBoride", 'C', "c:plates/Zinc", 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 585)
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 570), "A ", "BAB", "BBB",

                'B', "c:plates/Iron", 'A', ItemStackHelper.fromData(IUItem.iudust, 1, 60)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 573), "A ", "BAB", "BBB",

                'B', "c:plates/Gold", 'A', ItemStackHelper.fromData(IUItem.iudust, 1, 60)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 590), "A ", "BAB", "BBB",

                'B', "c:plates/Platinum", 'A', "c:plates/Neodymium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 559), "A ", "BAB", "BBB",

                'B', "c:gems/Diamond", 'A', "c:plates/Nichrome"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 576), "ACA", "BCB", "ACA",

                'B', "c:plates/Carbon", 'A', "c:plates/Bronze", 'C', "c:dusts/Redstone"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 566), "ACA", "BCB", "ACA",

                'B', "c:plates/Osmium", 'A', "c:plates/HafniumCarbide", 'C', "c:dusts/Redstone"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 584), "ACA", "BCB", "ACA",

                'B', "c:plates/Niobium", 'A', "c:plates/MolybdenumSteel", 'C', "c:dusts/Redstone"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 564), "ACA", "BCB", "ACA",

                'B', "c:plates/BerylliumBronze", 'A', "c:plates/Woods", 'C', "c:dusts/Redstone"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 572), "AAA", "ABA", "AAA",

                'B', "c:plates/Tin", 'A', "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 577), "CAC", "ABA", "CAC",

                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 572), 'A', "c:plates/Cobalt", 'C', "c:plates/Chromium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 575), "AAA", "ABA", "AAA",

                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 577), 'A', "c:plates/YttriumAluminiumGarnet", 'C', "c:plates/Chromium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 587), "AAA", "ABA", "AAA",

                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 575), 'A', "c:plates/Inconel", 'C', "c:plates/Chromium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 580), "ABC", "BBC", "ABC",

                'B', "c:plates/Copper", 'A', "c:ingots/Chromium", 'C', ItemStackHelper.fromData(IUItem.cable, 1, 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 560), "ABC", "BBC", "ABC",

                'B', "c:plates/Aluminumbronze", 'A', "c:ingots/Chromium", 'C', ItemStackHelper.fromData(IUItem.cable, 1, 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 589), "ABC", "BBC", "ABC",

                'B', "c:plates/Permalloy", 'A', "c:ingots/Chromium", 'C', ItemStackHelper.fromData(IUItem.cable, 1, 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 596), "ABC", "BBC", "ABC",

                'B', "c:plates/Stellite", 'A', "c:ingots/Chromium", 'C', ItemStackHelper.fromData(IUItem.cable, 1, 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 571), " BB", " BB", "A  ",

                'B', "c:plates/Iron", 'A', "c:ingots/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 562), " BB", " BB", "A  ",

                'B', "c:plates/Bronze", 'A', "c:ingots/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 595), " BB", " BB", "A  ",

                'B', "c:plates/Steel", 'A', "c:ingots/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 586), " BB", " BB", "A  ",

                'B', "c:plates/StainlessSteel", 'A', "c:ingots/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 508), " B ", "BBB", "BBB",

                'B', "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 517), " B ", "BBB", "ACA",

                'B', "c:plates/Carbon", 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 508), 'A', IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 509), " B ", "BBB", "ACA",

                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 285), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 517), 'A',
                IUItem.quantumtool
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 525), " B ", "BBB", "ACA",

                'B', "c:plates/SuperalloyHaynes", 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 509), 'A', IUItem.spectral_box
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 518), " AA", " AB", "ABB",

                'B', "c:plates/Carbon", 'A', "c:plates/Redbrass"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 510), " AA", " AB", "ABB",

                'B', "c:plates/StainlessSteel", 'A', "c:plates/NiobiumTitanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 526), " AA", " AB", "ABB",

                'B', "c:plates/AluminiumLithium", 'A', "c:plates/Alcled"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 521), "A  ", "BA ", "BBA",

                'B', "c:plates/Carbon", 'A', "c:plates/Redbrass"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 513), "A  ", "BA ", "BBA",

                'B', "c:plates/StainlessSteel", 'A', "c:plates/NiobiumTitanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 529), "A  ", "BA ", "BBA",

                'B', "c:plates/AluminiumLithium", 'A', "c:plates/Alcled"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 522), "BA ", " BA", "   ",

                'B', "c:plates/Carbon", 'A', "c:plates/Redbrass"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 514), "BA ", " BA", "   ",

                'B', "c:plates/StainlessSteel", 'A', "c:plates/NiobiumTitanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 530), "BA ", " BA", "   ",

                'B', "c:plates/AluminiumLithium", 'A', "c:plates/Alcled"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 519), "AAB", "AB ", "   ",

                'B', "c:plates/Carbon", 'A', "c:plates/Redbrass"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 511), "AAB", "AB ", "   ",

                'B', "c:plates/StainlessSteel", 'A', "c:plates/NiobiumTitanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 527), "AAB", "AB ", "   ",

                'B', "c:plates/AluminiumLithium", 'A', "c:plates/Alcled"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 520), "AAB", "ABB", "AB ",

                'B', "c:plates/Carbon", 'A', "c:plates/Redbrass"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 512), "AAB", "ABB", "AB ",

                'B', "c:plates/StainlessSteel", 'A', "c:plates/NiobiumTitanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 528), "AAB", "ABB", "AB ",

                'B', "c:plates/AluminiumLithium", 'A', "c:plates/Alcled"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 523), "BA ", "BBA", " BA",

                'B', "c:plates/Carbon", 'A', "c:plates/Redbrass"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 515), "BA ", "BBA", " BA",

                'B', "c:plates/StainlessSteel", 'A', "c:plates/NiobiumTitanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 531), "BA ", "BBA", " BA",

                'B', "c:plates/AluminiumLithium", 'A', "c:plates/Alcled"
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 524),
                "CBC",
                "BAB",
                "CDC",

                'B',
                "c:plates/Carbon",
                'C',
                "c:plates/Redbrass",
                'A',
                IUItem.energy_crystal,
                'D',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 516),
                "CBC",
                "BAB",
                "CDC",

                'B',
                "c:plates/StainlessSteel",
                'C',
                "c:plates/NiobiumTitanium",
                'A',
                IUItem.lapotron_crystal,
                'D',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 532),
                "CBC",
                "BAB",
                "CDC",

                'B',
                "c:plates/AluminiumLithium",
                'C',
                "c:plates/Alcled",
                'A',
                IUItem.AdvlapotronCrystal,
                'D',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 460), "AAA", "AAA", "AAA",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 463)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 454), "AAA", "AAA", "AAA",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 461)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 456), "AAA", "AAA", "AAA",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 462)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 10), "AAA", "AAA", "AAA",
                'A', ItemStackHelper.fromData(IUItem.nuclear_res, 1, 13)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 17), " B ", "BAB", " B ",
                'A', ItemStackHelper.fromData(IUItem.nuclear_res, 1, 10), 'B', IUItem.stoneDust
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 10), "AAA", "AAA", "AAA",
                'A', ItemStackHelper.fromData(IUItem.nuclear_res, 1, 13)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 17), " B ", "BAB", " B ",
                'A', ItemStackHelper.fromData(IUItem.nuclear_res, 1, 10), 'B', IUItem.stoneDust
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 11), "AAA", "AAA", "AAA",
                'A', ItemStackHelper.fromData(IUItem.nuclear_res, 1, 14)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 18), " B ", "BAB", " B ",
                'A', ItemStackHelper.fromData(IUItem.nuclear_res, 1, 11), 'B', IUItem.stoneDust
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 12), "AAA", "AAA", "AAA",
                'A', ItemStackHelper.fromData(IUItem.nuclear_res, 1, 15)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 20), " B ", "BAB", " B ",
                'A', ItemStackHelper.fromData(IUItem.nuclear_res, 1, 12), 'B', IUItem.stoneDust
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 19), "AAA", "AAA", "AAA",
                'A', ItemStackHelper.fromData(IUItem.nuclear_res, 1, 16)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.humus), "AAA", "ABA", "AAA",
                'A', new ItemStack(Blocks.DIRT), 'B', IUItem.apatite_cube
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basecircuit, 1, 19), "AAA", "ABA", "AAA",
                'B', ItemStackHelper.fromData(IUItem.basecircuit, 1, 18), 'A', "c:rods/Arsenic"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 489), "DCC", "CBB", "AA ",
                'B', "c:plates/Zirconium", 'A', "c:plates/Cadmium", 'C', "c:plates/Ferromanganese", 'D', "c:plates/Niobium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 491), " C ", "BAB", " C ",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 488), 'B', "c:plates/Gadolinium", 'C', "c:plates/Barium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 486), " C ", "BAB", " C ",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 491), 'B', "c:plates/Redbrass", 'C', "c:plates/Duralumin"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ObsidianForgeHammer), "ABA", "ACA", " C ",
                'A', "c:plates/Obsidian", 'B', "c:plates/Steel", 'C', Items.STICK
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 485), " C ", "BAB", " C ",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 486), 'B', "c:plates/TantalumTungstenHafnium", 'C',
                "c:plates/Osmiridium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 490), " C ", "BAB", " C ",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 485), 'B', "c:plates/Zeliber", 'C',
                "c:plates/Nitenol"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 588), "CCB", "CAA", "CCB",
                'A', "c:plates/Thallium", 'B', "c:plates/Strontium", 'C',
                "c:plates/Niobium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 598), " CB", "CDA", " CB",
                'A', "c:plates/Nitenol", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 581), 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 568), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 588)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 592),
                "ECB",
                "CDA",
                "ECB",
                'A',
                "c:plates/BerylliumBronze",
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 563),
                'E',
                "c:plates/TantalumTungstenHafnium",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 565),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 598)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 594), "ECB", "CDA", "ECB",
                'A', "c:plates/Inconel", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 585), 'E', "c:plates/StainlessSteel",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 565), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 592)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blockMacerator), "C C", "BAB", "BCB",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 41), 'B', "c:plates/Ferromanganese", 'E', "c:plates/StainlessSteel",
                'C',
                "c:plates/Invar", 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 592)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blockCompressor), "C C", "BAB", "BCB",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 76), 'B', "c:plates/Ferromanganese", 'E', "c:plates/StainlessSteel",
                'C',
                "planks", 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 76)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.dryer), "CBC", "CBC", "CBC",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 76), 'B', Blocks.GLASS, 'E', "c:plates/StainlessSteel",
                'C',
                "planks", 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 76)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.squeezer), "CEC", "CBC", "CDC",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 76), 'B', Blocks.GLASS, 'E', IUItem.treetap,
                'C',
                "planks", 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 601)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.fluidIntegrator), "A A", "ACA", "EDE",
                'A', "c:plates/Electrum", 'B', Blocks.GLASS, 'E', "c:plates/Ferromanganese",
                'C',
                "c:plates/Invar", 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 601)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.primalFluidHeater), "BCB", "BCB", "AAA",
                'A', "c:plates/Aluminumbronze", 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 601), 'B', "c:plates/Bronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.primalSiliconCrystal), "AAA", "ABA", "AAA",
                'A', "c:plates/Iron", 'B', Blocks.GLASS
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.primalPolisher), " B ", "ADA", "ACA",
                'A', "c:plates/Titanium", 'B', Blocks.GLASS, 'D', "c:plates/Iron", 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 354)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.programming_table), " B ", "ADA", "ACA",
                'A', "c:plates/Titanium", 'B', Blocks.GLASS, 'D', "c:plates/Carbon", 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 354)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.solderingMechanism), " B ", "ADA", "ACA",
                'A', "c:plates/Titanium", 'B', Blocks.GLASS, 'D', "c:plates/Iron", 'C', ItemStackHelper.fromData(IUItem.solderingIron)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electronics_assembler), " B ", "ADA", "ACA",
                'A', "c:plates/Titanium", 'B', Blocks.GLASS, 'D', "c:plates/Iron", 'C', ItemStackHelper.fromData(IUItem.basecircuit, 1, 17)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.primal_pump), "   ", "DAD", "DCD",
                'A', Blocks.GLASS, 'D', "c:plates/Iron", 'C', ItemStackHelper.fromData(IUItem.basemachine2, 1, 185)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 3, 478), "   ", "DAD", "D D",
                'A', "c:dyes/Yellow", 'D', IUItem.rubber
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radioprotector), "BBB", " A ", "A  ",
                'A', "c:plates/AluminiumSilicon", 'B', "c:plates/Nitenol"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.steamPipe, 6), "BBB", "AAA", "BBB",
                'A', "c:plates/Polonium", 'B', "c:casings/Aluminumbronze"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.biopipes, 6), "BBB", "AAA", "BBB",
                'A', "c:plates/Tantalum", 'B', "c:casings/Thallium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_cathode), " A ", "ABA", "CCC",
                'A', "c:plates/TantalumTungstenHafnium", 'B', IUItem.cathode, 'C', ItemStackHelper.fromData(IUItem.iudust, 1, 63)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_anode), " A ", "ABA", "CCC",
                'A', "c:plates/Osmiridium", 'B', IUItem.anode, 'C', ItemStackHelper.fromData(IUItem.iudust, 1, 63)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ironMesh), " BB", " AA", " BB",
                'A', Items.STRING, 'B', "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.steelMesh), " BB", " BB", " BA",
                'A', IUItem.ironMesh, 'B', "c:plates/Steel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.boridehafniumMesh), " BB", " BB", " BA",
                'A', IUItem.steelMesh, 'B', "c:plates/HafniumBoride"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.vanadiumaluminumMesh), " BB", " BB", " BA",
                'A', IUItem.boridehafniumMesh, 'B', "c:plates/Vanadoalumite"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.steleticMesh), " BB", " BB", " BA",
                'A', IUItem.vanadiumaluminumMesh, 'B', "c:plates/Stellite"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.gasSensor),
                "DCD",
                "BAB",
                "  ",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),
                'B',
                "c:plates/Polonium",
                'C',
                "c:plates/Zirconium",
                'D',
                "c:plates/NiobiumTitanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.reactorData), "DCD", "BAB", "  ",
                'A', TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'B', "c:plates/Nimonic", 'C', "c:plates/Woods",
                'D', "c:plates/Zeliber"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32), "CBC", "BAB", "EDE", 'A', DEFAULT_SENSOR,
                'B', "c:gears/Invar", 'C', "c:gems/Sapphire", 'D', "c:gears/Ferromanganese", 'E', "c:plates/Ferromanganese"

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 123), "CBC", "BAB", "EDE", 'A', DEFAULT_SENSOR,
                'B', "c:gears/Electrum", 'C', "c:gems/Topaz", 'D', "c:gears/Aluminumbronze", 'E', "c:plates/Aluminumbronze"

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 163), " A ", " B ", " C ", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32),
                'B', IUItem.primalFluidHeater, 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 164), " A ", "DBC", "   ", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32),
                'B', ItemStackHelper.fromData(IUItem.basemachine2, 1, 163), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 63)
                , 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 123)

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 166), "DAE", " B ", " C ", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 123), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 69),
                'B', IUItem.blockMacerator, 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 167), "DAE", " B ", " C ", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 123), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 63),
                'B', IUItem.blockCompressor, 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 168), "DAE", " B ", " C ", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 123), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 159),
                'B', IUItem.squeezer, 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12)

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 189), "ABC", "DE ", " G ", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 230), 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 61), 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 231),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47), 'E', ItemStackHelper.fromData(IUItem.blockResource, 1, 8), 'G',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276)

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 169), "DAE", " B ", " C ", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 123), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 163),
                'B', IUItem.anvil, 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 170), "DAE", " B ", " C ", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 123), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 132),
                'B', ItemStackHelper.fromData(IUItem.basemachine2, 1, 124), 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 188), "D A", " B ", " C ", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 123), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 132),
                'B', IUItem.primalPolisher, 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 181), "DEA", " B ", " C ", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 123), 'E',
                ItemStackHelper.fromData(IUItem.advBattery),
                'B', getBlockStack(BlockBaseMachine3.oak_tank), 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12)

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 180), "DEA", "BBB", "FCF", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 123), 'E',
                ItemStackHelper.fromData(IUItem.advBattery), 'F', ItemStackHelper.fromData(IUItem.alloysplate, 1, 31),
                'B', getBlockStack(BlockBaseMachine3.oak_tank), 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 187), "DAE", " B ", " C ", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 123), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 241),
                'B', IUItem.primal_pump, 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12)

        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 267), "ECE", "ABA", "ECE", 'B',
                DEFAULT_SENSOR, 'A', "c:doubleplate/Yttrium", 'C', "c:doubleplate/Strontium", 'E', "c:plates/Niobium"

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 182), "DAE", "BBB", " C ", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 123), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 267),
                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 601), 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 165), "D A", " B ", " C ", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 123), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 132),
                'B', IUItem.dryer, 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 172), "D A", " B ", " C ", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 123), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 132),
                'B', IUItem.squeezer, 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12)

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 171), "AAA", "ABA", " C ", 'A',
                "c:plates/Steel", 'C', getBlockStack(BlockBaseMachine3.steel_tank),
                'B', ItemStackHelper.fromData(IUItem.basemachine2, 1, 163)

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 178), "AAA", "ABA", "CCC", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 501), 'C', IUItem.cultivated_peat_balls,
                'B', ItemStackHelper.fromData(IUItem.basemachine2, 1, 171)

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 179), "DAE", "FBF", "HCH", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 123), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 219), 'F', "c:plates/Titanium", 'H', "c:plates/Bismuth",
                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51), 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.proton_energy_coupler), "ABA", "BCB", "ABA", 'A',
                ItemStackHelper.fromData(IUItem.per_heat_exchange), 'B', ItemStackHelper.fromData(IUItem.per_Vent), 'C',
                ItemStackHelper.fromData(IUItem.proton)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_proton_energy_coupler), "ABA", "BCB", "ABA", 'A',
                ItemStackHelper.fromData(IUItem.proton), 'B', ItemStackHelper.fromData(IUItem.radiationresources, 1, 2), 'C',
                ItemStackHelper.fromData(IUItem.proton_energy_coupler)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.imp_proton_energy_coupler), "ABA", "BCB", "ABA", 'A',
                ItemStackHelper.fromData(IUItem.advQuantumtool), 'B', "c:doubleplate/Nimonic", 'C',
                ItemStackHelper.fromData(IUItem.adv_proton_energy_coupler)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.per_proton_energy_coupler), "ABA", "BCB", "ABA", 'A',
                ItemStackHelper.fromData(IUItem.adv_spectral_box), 'B', "c:doubleplate/SuperalloyHaynes", 'C',
                ItemStackHelper.fromData(IUItem.imp_proton_energy_coupler)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.neutron_protector), "ABA", "BCB", "ABA", 'A',
                ItemStackHelper.fromData(IUItem.proton_energy_coupler), 'B', IUItem.iridiumPlate, 'C',
                ItemStackHelper.fromData(IUItem.neutroniumingot)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_neutron_protector), "ABA", "BCB", "ABA", 'A',
                ItemStackHelper.fromData(IUItem.neutroniumingot), 'B', ItemStackHelper.fromData(IUItem.radiationresources, 1, 3), 'C',
                ItemStackHelper.fromData(IUItem.neutron_protector)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.imp_neutron_protector), "ABA", "BCB", "ABA", 'A',
                ItemStackHelper.fromData(IUItem.advQuantumtool), 'B', "c:doubleplate/Nimonic", 'C',
                ItemStackHelper.fromData(IUItem.adv_neutron_protector)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.per_neutron_protector), "ABA", "BCB", "ABA", 'A',
                ItemStackHelper.fromData(IUItem.adv_spectral_box), 'B', "c:doubleplate/SuperalloyRene", 'C',
                ItemStackHelper.fromData(IUItem.imp_neutron_protector)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.upgrade_casing), "ABA", "BCB", "ABA", 'A',
                ItemStackHelper.fromData(IUItem.graphene_plate), 'B', "c:plates/Steel", 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 479)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 143), "CBC", "BAB", "CBC", 'A',
                ItemStackHelper.fromData(IUItem.module_schedule), 'B', "c:plates/AluminiumLithium", 'C', "c:plates/TantalumTungstenHafnium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 656),
                "EEE",
                "BAB",
                "CDC",
                'A',
                DEFAULT_SENSOR,
                'B',
                "c:plates/Beryllium",
                'C',
                "c:plates/TantalumTungstenHafnium",
                'D',
                "c:gears/CobaltChrome",
                'E',
                "c:casings/Bismuth"
        );

        BasicRecipeTwo.recipe();
    }

    public static ItemStack getBlockStack(IMultiTileBlock block) {
        return TileBlockCreator.instance.get(block.getIDBlock()).getItemStack();
    }
}
