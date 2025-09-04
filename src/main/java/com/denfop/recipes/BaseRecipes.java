package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.BlockEntityGenerationMicrochip;
import com.denfop.blockentity.mechanism.BlockEntityUpgradeMachineFactory;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.mechanism.BlockBaseMachine2Entity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.blocks.mechanism.BlockPrimalFluidHeaterEntity;
import com.denfop.componets.Fluids;
import com.denfop.register.RegisterOreDictionary;
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
                "forge:ingots/Bronze",
                "forge:ingots/Bronze",
                "forge:ingots/Bronze",
                "forge:ingots/Bronze",
                "forge:ingots/Bronze",
                "forge:ingots/Bronze",
                "forge:ingots/Bronze",
                "forge:ingots/Bronze",
                "forge:ingots/Bronze"
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.copperBlock,
                "forge:ingots/Copper",
                "forge:ingots/Copper",
                "forge:ingots/Copper",
                "forge:ingots/Copper",
                "forge:ingots/Copper",
                "forge:ingots/Copper",
                "forge:ingots/Copper",
                "forge:ingots/Copper",
                "forge:ingots/Copper"
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.tinBlock,
                "forge:ingots/Tin", "forge:ingots/Tin", "forge:ingots/Tin", "forge:ingots/Tin", "forge:ingots/Tin", "forge:ingots/Tin", "forge:ingots/Tin", "forge:ingots/Tin", "forge:ingots/Tin"
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.leadBlock,
                "forge:ingots/Lead",
                "forge:ingots/Lead",
                "forge:ingots/Lead",
                "forge:ingots/Lead",
                "forge:ingots/Lead",
                "forge:ingots/Lead",
                "forge:ingots/Lead",
                "forge:ingots/Lead",
                "forge:ingots/Lead"
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.uraniumBlock,
                "forge:ingots/Uranium",
                "forge:ingots/Uranium",
                "forge:ingots/Uranium",
                "forge:ingots/Uranium",
                "forge:ingots/Uranium",
                "forge:ingots/Uranium",
                "forge:ingots/Uranium",
                "forge:ingots/Uranium",
                "forge:ingots/Uranium"
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.advironblock,
                "forge:ingots/Steel",
                "forge:ingots/Steel",
                "forge:ingots/Steel",
                "forge:ingots/Steel",
                "forge:ingots/Steel",
                "forge:ingots/Steel",
                "forge:ingots/Steel",
                "forge:ingots/Steel",
                "forge:ingots/Steel"
        );
        Recipes.recipe.addShapelessRecipe(
                ItemStackHelper.fromData(IUItem.block1.getItem(3), 1),
                "forge:ingots/Osmium",
                "forge:ingots/Osmium",
                "forge:ingots/Osmium",
                "forge:ingots/Osmium",
                "forge:ingots/Osmium",
                "forge:ingots/Osmium",
                "forge:ingots/Osmium",
                "forge:ingots/Osmium",
                "forge:ingots/Osmium"
        );
        Recipes.recipe.addShapelessRecipe(
                ItemStackHelper.fromData(IUItem.block1.getItem(4), 1),
                "forge:ingots/Tantalum",
                "forge:ingots/Tantalum",
                "forge:ingots/Tantalum",
                "forge:ingots/Tantalum",
                "forge:ingots/Tantalum",
                "forge:ingots/Tantalum",
                "forge:ingots/Tantalum",
                "forge:ingots/Tantalum",
                "forge:ingots/Tantalum"
        );
        Recipes.recipe.addShapelessRecipe(
                ItemStackHelper.fromData(IUItem.block1.getItem(5), 1),
                "forge:ingots/Cadmium",
                "forge:ingots/Cadmium",
                "forge:ingots/Cadmium",
                "forge:ingots/Cadmium",
                "forge:ingots/Cadmium",
                "forge:ingots/Cadmium",
                "forge:ingots/Cadmium",
                "forge:ingots/Cadmium",
                "forge:ingots/Cadmium"
        );
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.advIronIngot, 9), IUItem.advironblock);
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.copperIngot, 9), IUItem.copperBlock);
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.tinIngot, 9), IUItem.tinBlock);
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.leadIngot, 9), IUItem.leadBlock);
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.bronzeIngot, 9), IUItem.bronzeBlock);
        Recipes.recipe.addShapelessRecipe(ItemStackHelper.fromData(IUItem.itemiu.getStack(2), 9), IUItem.uraniumBlock);


        Recipes.recipe.addRecipe(IUItem.efReader.getItemStack(), " A ", "BCB", "B B",

                ('A'), Items.GLOWSTONE_DUST, ('B'),
                IUItem.insulatedCopperCableItem, ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
        );

        Recipes.recipe.addRecipe(IUItem.module_infinity_water.getItemStack(), "BBB", "CAC", "DED",

                ('A'), IUItem.module_schedule, ('B'),
                ModUtils.getCellFromFluid(Fluids.WATER), ('C'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5), 'D', IUItem.quantumtool, 'E',
                "forge:doubleplate/Iridium"
        );
        Recipes.recipe.addRecipe(IUItem.module_separate.getItemStack(), "BBB", "CAC", "DED",

                ('A'), IUItem.module_storage, ('B'),
                "forge:plates/Carbon", ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5), 'D', "forge:gears/Electrum", 'E',
                "forge:doubleplate/Aluminumbronze"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ruby_boots.getItem()), "   ", "A A", "A A",
                ('A'), ("forge:gems/Ruby")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.sapphire_boots.getItem()), "   ", "A A", "A A",
                ('A'), ("forge:gems/Sapphire")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.topaz_boots.getItem()), "   ", "A A", "A A",
                ('A'), ("forge:gems/Topaz")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ruby_chestplate.getItem()), "A A", "AAA", "AAA",
                ('A'), ("forge:gems/Ruby")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blockResource.getItem(11), 1), "AAA", "AAA", "AAA",
                ('A'), IUItem.peat_balls
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.sapphire_chestplate.getItem()), "A A", "AAA", "AAA",
                ('A'), ("forge:gems/Sapphire")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.topaz_chestplate.getItem()), "A A", "AAA", "AAA",
                ('A'), ("forge:gems/Topaz")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ruby_leggings.getItem()), "AAA", "A A", "A A",
                ('A'), ("forge:gems/Ruby")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.sapphire_leggings.getItem()), "AAA", "A A", "A A",
                ('A'), ("forge:gems/Sapphire")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.topaz_leggings.getItem()), "AAA", "A A", "A A",
                ('A'), ("forge:gems/Topaz")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ruby_helmet.getItem()), "AAA", "A A", "   ",
                ('A'), ("forge:gems/Ruby")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.sapphire_helmet.getItem()), "AAA", "A A", "   ",
                ('A'), ("forge:gems/Sapphire")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.topaz_helmet.getItem()), "AAA", "A A", "   ",
                ('A'), ("forge:gems/Topaz")
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 2), "ABA", "CCC", "AAA",
                ('A'), ("planks"), ('C'), ItemStackHelper.fromData(IUItem.advBattery.getItem(), 1),

                ('B'), IUItem.tinCableItem
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 5), "ABA", "CCC", "ADA",

                ('A'),
                ("forge:plates/Bronze"),
                ('C'), ItemStackHelper.fromData(IUItem.reBattery, 1),

                ('B'), IUItem.insulatedCopperCableItem, 'D', ItemStackHelper.fromData(IUItem.electricblock, 1, 2)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 3), "ACA", "CBC", "ACA",

                ('A'),
                ("forge:doubleplate/Aluminumbronze"),
                ('C'), ItemStackHelper.fromData(IUItem.energy_crystal, 1),

                ('B'), ItemStackHelper.fromData(IUItem.electricblock, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 4), "CDC", "CAC", "CBC",

                ('D'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),

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

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 6), "CDC", "DAD", "CDC",

                ('D'), ItemStackHelper.fromData(IUItem.core, 1, 4),

                ('A'), ItemStackHelper.fromData(IUItem.electricblock, 1, 1),

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 7), "CDC", "DAD", "CDC",

                ('D'), ItemStackHelper.fromData(IUItem.core, 1, 8),

                ('A'), ItemStackHelper.fromData(IUItem.electricblock, 1, 6),

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 8), "CBC", "DAD", "CBC",

                ('D'), ItemStackHelper.fromData(IUItem.core, 1, 9),

                ('A'), ItemStackHelper.fromData(IUItem.electricblock, 1, 7),

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'),
                ItemStackHelper.fromData(IUItem.AdvlapotronCrystal, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 9), "EBE", "DAD", "CBC",

                ('D'), ItemStackHelper.fromData(IUItem.core, 1, 11),

                ('E'), ItemStackHelper.fromData(IUItem.compressIridiumplate),

                ('A'), ItemStackHelper.fromData(IUItem.electricblock, 1, 8),

                ('C'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), ItemStackHelper.fromData(IUItem.AdvlapotronCrystal, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricblock, 1, 10), "EBE", "DAD", "CBC",

                ('D'), ItemStackHelper.fromData(IUItem.core, 1, 12),

                ('E'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('A'), ItemStackHelper.fromData(IUItem.electricblock, 1, 9),

                ('C'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), ItemStackHelper.fromData(IUItem.AdvlapotronCrystal, 1)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.voltage_sensor_for_mechanism), "AAA", "BCB", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.nanoBox),

                ('B'), "forge:plates/TantalumTungstenHafnium",

                ('C'), ItemStackHelper.fromData(IUItem.efReader)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.polonium_palladium_composite), "AAA", "BBB", "CCC",

                ('A'), "forge:plates/Polonium",

                ('B'), "forge:plates/Palladium",

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

                ('C'), "forge:plates/Zeliber",

                ('D'), IUItem.medium_current_converter_to_low,

                ('E'), IUItem.adv_motors_with_improved_bearings_
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.extreme_current_converter_to_low), "AAA", "BDB", "CEC",

                ('A'), IUItem.synthetic_plate,

                ('B'), ItemStackHelper.fromData(IUItem.cable, 1, 0),

                ('C'), "forge:plates/StainlessSteel",

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

                ('A'), "forge:plates/Iron",

                ('B'), "forge:plates/Lapis"
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
                ("forge:doubleplate/Iridium"),
                ('D'),
                ("forge:casings/Iridium"),
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
                ("forge:doubleplate/Iridium"),
                ('B'), ItemStackHelper.fromData(IUItem.compressAlloy, 1),

                ('D'), ItemStackHelper.fromData(IUItem.compresscarbon, 1),

                ('C'),
                IUItem.iridiumOre,

                ('A'), ItemStackHelper.fromData(IUItem.nanoBox)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.magnet), "A B", "CDC", " C ",

                ('B'),
                ("forge:ingots/Ferromanganese"),
                ('D'), ItemStackHelper.fromData(IUItem.advBattery, 1),

                ('C'),
                ("forge:doubleplate/Mikhail"),
                ('A'),
                ("forge:ingots/Vitalium")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.impmagnet), "B B", "CDC", " C ",

                ('B'),
                ("forge:ingots/Duralumin"),
                ('D'), ItemStackHelper.fromData(IUItem.magnet, 1),

                ('C'),
                ("forge:doubleplate/Invar")
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
                ("forge:doubleplate/Ferromanganese"), ('D'), new ItemStack(Items.DIAMOND_PICKAXE), ('E'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 520), 'H', "forge:rods/Molybdenum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nanoaxe), "ADB", "EFE", "CHC",
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 524), ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 522), 'C',
                ("forge:doubleplate/Ferromanganese"), ('D'), new ItemStack(Items.DIAMOND_AXE), ('E'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 519), 'H', "forge:rods/Molybdenum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nanoshovel), "ADB", "EFE", "CHC",
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 524), ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 521), 'C',
                ("forge:doubleplate/Ferromanganese"), ('D'), new ItemStack(Items.DIAMOND_SHOVEL), ('E'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 518), 'H', "forge:rods/Molybdenum"
        );
        //
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.quantumpickaxe), "BDA", "EFE", "CHC",
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 516), ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 512), 'C',
                ("forge:doubleplate/Muntsa"), ('D'), ItemStackHelper.fromData(IUItem.nanopickaxe), ('E'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 515), 'H', "forge:rods/Electrum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.quantumaxe), "ADB", "EFE", "CHC",
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 516), ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 514), 'C',
                ("forge:doubleplate/Muntsa"), ('D'), ItemStackHelper.fromData(IUItem.nanoaxe), ('E'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 511), 'H', "forge:rods/Electrum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.quantumshovel), "ADB", "EFE", "CHC",
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 516), ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 513), 'C',
                ("forge:doubleplate/Muntsa"), ('D'), ItemStackHelper.fromData(IUItem.nanoshovel), ('E'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 510), 'H', "forge:rods/Electrum"
        );
        //
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spectralpickaxe), "ADB", "EFE", "CHC",
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 532), ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 531), 'C',
                IUItem.iridiumPlate, ('D'), ItemStackHelper.fromData(IUItem.quantumpickaxe), ('E'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 528), 'H', "forge:rods/Iridium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spectralaxe), "ADB", "EFE", "CHC",
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 532), ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 530), 'C',
                IUItem.iridiumPlate, ('D'), ItemStackHelper.fromData(IUItem.quantumaxe), ('E'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 527), 'H', "forge:rods/Iridium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spectralshovel), "ADB", "EFE", "CHC",
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 532), ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 529), 'C',
                IUItem.iridiumPlate, ('D'), ItemStackHelper.fromData(IUItem.quantumshovel), ('E'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 526), 'H', "forge:rods/Iridium"
        );
        //

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.impBatChargeCrystal), "BCB", "BAB", "BCB",

                ('B'), ItemStackHelper.fromData(IUItem.AdvlapotronCrystal, 1),

                ('A'), ItemStackHelper.fromData(IUItem.charging_lapotron_crystal, 1),

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.perBatChargeCrystal), "DCD", "BAB", "ECE",

                ('E'),
                ("forge:doubleplate/Vitalium"),
                ('D'), IUItem.iridiumPlate,

                ('B'), ItemStackHelper.fromData(IUItem.AdvlapotronCrystal, 1),

                ('A'),
                ItemStackHelper.fromData(IUItem.impBatChargeCrystal, 1),

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.perfect_drill), "ACB", "FDF", "ECE",

                ('E'), ItemStackHelper.fromData(IUItem.adv_spectral_box),

                ('F'), IUItem.overclockerUpgrade1,

                ('A'), ItemStackHelper.fromData(IUItem.spectralaxe, 1),

                ('B'),
                ItemStackHelper.fromData(IUItem.spectralshovel, 1),

                ('D'), ItemStackHelper.fromData(IUItem.spectralpickaxe, 1),

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(ItemStackHelper.fromData(IUItem.basecircuit, 1, 21), 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ult_vajra), "CAC", "DBD", "", 'A', ItemStackHelper.fromData(IUItem.vajra), 'B',
                ItemStackHelper.fromData(IUItem.perfect_drill), 'C', "forge:plates/Nimonic", 'D', "forge:plates/SuperalloyRene"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.vajra), "ACB", "FDF", "ECE",

                ('E'), ItemStackHelper.fromData(IUItem.advQuantumtool),

                ('F'), IUItem.overclockerUpgrade1,

                ('A'), ItemStackHelper.fromData(IUItem.spectralaxe, 1),

                ('B'),
                ItemStackHelper.fromData(IUItem.spectralshovel, 1),

                ('D'), ItemStackHelper.fromData(IUItem.spectralpickaxe, 1),

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(ItemStackHelper.fromData(IUItem.basecircuit, 1, 21), 12)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.quantumSaber), "AB ", "AC ", "DEB",
                ('C'), ItemStackHelper.fromData(IUItem.nanosaber, 1), ('E'), ItemStackHelper.fromData(IUItem.lapotron_crystal, 1),
                ('D'), new ItemStack(Blocks.GLOWSTONE),
                ('B'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), ('A'), ItemStackHelper.fromData(IUItem.compresscarbon)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spectralSaber), "AB ", "AC ", "DEB",

                ('C'), ItemStackHelper.fromData(IUItem.quantumSaber, 1),

                ('E'), ItemStackHelper.fromData(IUItem.AdvlapotronCrystal, 1),

                ('D'), new ItemStack(Blocks.GLOWSTONE),

                ('B'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('A'), ItemStackHelper.fromData(IUItem.compressIridiumplate)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.GraviTool), "ABA", "CDE", "FGF",

                ('G'), ItemStackHelper.fromData(IUItem.purifier, 1),

                ('F'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),

                ('D'), ItemStackHelper.fromData(IUItem.energy_crystal, 1),

                ('E'),
                ItemStackHelper.fromData(IUItem.electric_treetap, 1),

                ('C'), ItemStackHelper.fromData(IUItem.electric_wrench, 1),

                ('B'), ItemStackHelper.fromData(IUItem.electric_hoe
                        .getItem(), 1),
                ('A'),
                ("forge:doubleplate/Muntsa")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.purifier), "   ", " B ", "A  ",
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 11), ('B'), "wool"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.itemiu, 1, 3), "MDM", "M M", "MDM",
                ('D'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4), ('M'), ItemStackHelper.fromData(
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('B'),
                IUItem.carbonPlate,
                ('A'),
                new ItemStack(Items.BOW)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.quantum_bow), "ABA", "CDC", "EBE",

                ('E'),
                ("forge:doubleplate/Alcled"),
                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('D'), ItemStackHelper.fromData(IUItem.nano_bow, 1),

                ('A'),
                IUItem.iridiumPlate,

                ('B'), ItemStackHelper.fromData(IUItem.advQuantumtool)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spectral_bow), "ABA", "CDC", "EBE",

                ('E'),
                ("forge:doubleplate/Duralumin"),
                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

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
                ("forge:plates/Cobalt"),
                ('B'),
                ("forge:casings/Electrum"),
                ('A'),
                ("forge:casings/Iridium")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.solidmatter, 1, 1), "ABA", "CDC", "EBE",

                ('E'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('D'), ItemStackHelper.fromData(IUItem.machines, 1, 3),

                ('C'),
                ("forge:plates/Platinum"),
                ('B'),
                ("forge:casings/Magnesium"),
                ('A'),
                ("forge:casings/Cobalt")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.solidmatter, 1, 2), "ABA", "CDC", "EBE",

                ('E'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('D'), ItemStackHelper.fromData(IUItem.machines, 1, 3),

                ('C'),
                ("forge:doubleplate/Alcled"),
                ('B'),
                ("forge:casings/Chromium"),
                ('A'),
                ("forge:casings/Mikhail")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.solidmatter, 1, 3), "ABA", "CDC", "EBE",

                ('E'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('D'), ItemStackHelper.fromData(IUItem.machines, 1, 3),

                ('C'),
                ("forge:doubleplate/Duralumin"),
                ('B'),
                ("forge:casings/Caravky"),
                ('A'),
                ("forge:casings/vanadium")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.solidmatter, 1, 4), "ABA", "CDC", "EBE",

                ('E'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('D'), ItemStackHelper.fromData(IUItem.machines, 1, 3),

                ('C'),
                ("forge:doubleplate/Manganese"),
                ('B'),
                ("forge:casings/Spinel"),
                ('A'),
                ("forge:casings/Aluminium")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.solidmatter, 1, 5), "ABA", "CDC", "EBE",

                ('E'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('D'), ItemStackHelper.fromData(IUItem.machines, 1, 3),

                ('C'),
                ("forge:doubleplate/Titanium"),
                ('B'),
                ("forge:casings/Invar"),
                ('A'),
                ("forge:casings/Tungsten")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.solidmatter, 1, 6), "ABA", "CDC", "EBE",

                ('E'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('D'), ItemStackHelper.fromData(IUItem.machines, 1, 3),

                ('C'),
                ("forge:doubleplate/Redbrass"),
                ('B'),
                ("forge:casings/Chromium"),
                ('A'),
                ("forge:casings/Manganese")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.solidmatter, 1, 7), "ABA", "CDC", "EBE",

                ('E'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('D'), ItemStackHelper.fromData(IUItem.machines, 1, 3),

                ('C'),
                ("forge:doubleplate/Alumel"),
                ('B'),
                ("forge:casings/Cobalt"),
                ('A'),
                ("forge:casings/vanadium")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module_schedule), "ABA", "EDE", "CBC",

                ('D'),
                ("forge:ingots/Caravky"),
                ('E'),
                ("forge:plates/Zinc"),
                ('C'), ItemStackHelper.fromData(IUItem.plastic_plate),

                ('B'),
                ItemStackHelper.fromData(IUItem.plastic_plate),

                ('A'),
                ("forge:plates/vanadium")
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electricJetpack), "ADA", "ACA", "B B", ('A'), IUItem.casingiron,
                ('B'), Items.GLOWSTONE_DUST, ('C'), ItemStackHelper.fromData(IUItem.electricblock, 1, 2), ('D'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module7, 1, 1), "ABA", "BCB", "DDD",

                ('D'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('C'), ItemStackHelper.fromData(IUItem.advQuantumtool),

                ('B'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('A'),
                ItemStackHelper.fromData(IUItem.core, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module7, 1, 2), "ABA", "BCB", "DDD",

                ('D'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('C'), ItemStackHelper.fromData(IUItem.advnanobox),

                ('B'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('A'),
                ItemStackHelper.fromData(IUItem.core, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module7, 1, 3), "AAA", "BCB", "EFE",

                ('F'),
                ("forge:doubleplate/Alcled"),
                ('E'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('C'), ItemStackHelper.fromData(IUItem.module_schedule),

                ('B'),
                ("forge:doubleplate/Vitalium"),
                ('A'), ItemStackHelper.fromData(IUItem.compresscarbon)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module7, 1, 4), "AAA", "BCB", "EFE",

                ('F'),
                ("forge:doubleplate/Duralumin"),
                ('E'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('C'), ItemStackHelper.fromData(IUItem.module_schedule),

                ('B'),
                ("forge:doubleplate/Vanadoalumite"),
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

                ('E'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('D'),
                ("forge:doubleplate/Silver"),
                ('C'),
                ("forge:doubleplate/Electrum"),
                ('B'),
                ("forge:doubleplate/Redbrass"),
                ('A'),
                ("forge:doubleplate/Alcled")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 1), "ABA", "DED", "ABA",

                ('E'), IUItem.overclockerUpgrade1,

                ('D'), ItemStackHelper.fromData(IUItem.core, 1, 1),

                ('B'),
                ("forge:doubleplate/Vanadoalumite"),
                ('A'),
                ("forge:doubleplate/Alcled")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 2), "ABA", "CEC", "ABA",

                ('E'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

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

                ('E'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

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
                ("forge:doubleplate/Platinum"),
                ('C'), ItemStackHelper.fromData(IUItem.block, 1, 2),

                ('B'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),

                ('A'),
                ("forge:plates/Zinc")
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        543
                )
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 9), "ABA", "BCB", "ABA",

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), ItemStackHelper.fromData(IUItem.photoniy, 1),

                ('A'), ItemStackHelper.fromData(IUItem.core, 1, 3)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 10), "ABA", "BCB", "ABA",

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), ItemStackHelper.fromData(IUItem.photoniy_ingot, 1),

                ('A'), ItemStackHelper.fromData(IUItem.module9, 1, 9)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 11), "ABA", "BCB", "ABA",

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), ItemStackHelper.fromData(IUItem.photoniy_ingot, 1),

                ('A'), ItemStackHelper.fromData(IUItem.module9, 1, 10)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 12), "ABA", "BCB", "ABA",
                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9), ('B'), new ItemStack(
                        Blocks.REDSTONE_BLOCK,
                        1
                ), ('A'), new ItemStack(Items.PAPER, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 6), "ABA", "BCB", "ABA",

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('B'), ItemStackHelper.fromData(IUItem.photoniy, 1),

                ('A'), ItemStackHelper.fromData(IUItem.core, 1, 4)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 7), "ABA", "DCD", "ABA",

                ('D'), ItemStackHelper.fromData(IUItem.module9, 1, 6),

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), ItemStackHelper.fromData(IUItem.photoniy, 1),

                ('A'),
                ItemStackHelper.fromData(IUItem.core, 1, 4)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 8), "ABA", "DCD", "ABA",

                ('D'), ItemStackHelper.fromData(IUItem.module9, 1, 7),

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate, 1),

                ('A'),
                ItemStackHelper.fromData(IUItem.core, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 13), "A A", " C ", "A A",
                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9), ('A'), new ItemStack(Items.PAPER, 1)
        );
        Recipes.recipe.addRecipe(IUItem.module1, "AAA", "BCB", "EDE",

                ('E'), ItemStackHelper.fromData(IUItem.plastic_plate),

                ('D'),
                ("forge:doubleplate/Vitalium"),
                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'),
                ("forge:plates/Cobalt"),
                ('A'),
                ("forge:plates/Electrum")
        );
        Recipes.recipe.addShapelessRecipe(ItemStackHelper.fromData(IUItem.fertilizer, 16),
                ItemStackHelper.fromData(IUItem.iudust, 1, 69), ItemStackHelper.fromData(IUItem.iudust, 1, 70), ItemStackHelper.fromData(IUItem.iudust, 1, 66)
        );
        Recipes.recipe.addRecipe(IUItem.module2, "AAA", "BCB", "EDE",

                ('E'), ItemStackHelper.fromData(IUItem.plastic_plate),

                ('D'),
                ("forge:doubleplate/Vitalium"),
                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'),
                ("forge:doubleplate/Redbrass"),
                ('A'),
                ("forge:doubleplate/Ferromanganese")
        );
        Recipes.recipe.addRecipe(IUItem.module3, "AAA", "BCB", "EDE",

                ('E'), ItemStackHelper.fromData(IUItem.plastic_plate),

                ('D'),
                ("forge:doubleplate/Vitalium"),
                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'),
                ("forge:doubleplate/Alumel"),
                ('A'),
                ("forge:plates/Ferromanganese")
        );
        Recipes.recipe.addRecipe(IUItem.module4, "AAA", "BCB", "EDE",

                ('E'), ItemStackHelper.fromData(IUItem.plastic_plate),

                ('D'),
                ("forge:doubleplate/Vitalium"),
                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'),
                ("forge:doubleplate/Muntsa"),
                ('A'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate, 1)
        );
        for (j = 0; j < 7; j++) {
            Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module5, 1, j), "BBB", "ACA", "ADA",

                    ('A'), ItemStackHelper.fromData(IUItem.lens, 1, j),

                    ('B'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                    ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

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
                        "AAA", "AAA", "AAA", 'A', "forge:ingots/" + RegisterOreDictionary.list_string.get(i)
                );
            } else {
                Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.block1, 1, i - 16),
                        "AAA", "AAA", "AAA", 'A', "forge:ingots/" + RegisterOreDictionary.list_string.get(i)
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
                        ('A'), ("forge:ingots/" + RegisterOreDictionary.list_string1.get(j))
                );
            } else {
                Recipes.recipe.addShapelessRecipe(
                        ItemStackHelper.fromData(IUItem.alloysingot, 9, j),
                        ItemStackHelper.fromData(IUItem.alloysblock, 1, j)
                );
                Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.alloysblock, 1, j), "AAA", "AAA", "AAA",
                        ('A'), ("forge:ingots/" + RegisterOreDictionary.list_string1.get(j))
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
                    ('A'), ("forge:ingots/" + RegisterOreDictionary.list_baseore1.get(j))
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
                ('A'), "forge:plates/Chromium", 'B', "forge:plates/Titanium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cable, 1, 0), " A ", "BBB", " C ",
                ('A'), IUItem.glassFiberCableItem, ('B'), ItemStackHelper.fromData(IUItem.itemiu, 1, 0), 'C',
                ItemStackHelper.fromData(IUItem.synthetic_rubber)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cable, 1, 1), " A ", "BCB", " A ",

                ('C'),
                ("forge:ingots/Cobalt"),
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
                ("forge:ingots/Redbrass"),
                ('C'), IUItem.carbonPlate,

                ('A'), ItemStackHelper.fromData(IUItem.cable, 1, 3),

                ('B'),
                ("forge:ingots/Spinel")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cable, 1, 5), " A ", "BCB", " A ",

                ('C'),
                ("forge:doubleplate/Vitalium"),
                ('A'), ItemStackHelper.fromData(IUItem.cable, 1, 4),

                ('B'), IUItem.denseplateadviron
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cable, 1, 6), "DAD", "BCB", "DAD",

                ('D'), IUItem.carbonPlate,

                ('C'),
                ("forge:ingots/Alcled"),
                ('A'), ItemStackHelper.fromData(IUItem.cable, 1, 5),

                ('B'),
                ("forge:ingots/Duralumin")
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
                ("forge:plates/Iron"),
                ('B'),
                new ItemStack(Items.GOLD_INGOT)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basecircuit, 1, 16), "BBB", "BAB", "BBB",
                ('B'), ItemStackHelper.fromData(IUItem.stik, 1, 15), ('A'), ItemStackHelper.fromData(IUItem.basecircuit, 1, 15)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basecircuit, 1, 17), "CCC", "A A", "DDD",
                ('D'), ("forge:plates/Silver"), ('C'), IUItem.insulatedCopperCableItem,

                ('A'), ItemStackHelper.fromData(IUItem.basecircuit, 1, 15)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cable, 1, 10), "BBB", "ACA", "BBB",
                ('C'), ("forge:doubleplate/Vanadoalumite"), ('A'), ItemStackHelper.fromData(IUItem.basecircuit, 1, 11),
                ('B'), ItemStackHelper.fromData(IUItem.cable, 1, 9)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 27), " D ", "ABA", " D ",
                ('D'), ("forge:plates/Germanium"), ('B'), new ItemStack(Blocks.CHEST),
                ('A'), ItemStackHelper.fromData(IUItem.quantumtool)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_lappack), "ABA", "CEC", "ADA",

                ('E'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('D'), ItemStackHelper.fromData(IUItem.lapotron_crystal, 1),

                ('C'),
                ("forge:plates/Vanadoalumite"),
                ('B'),
                ItemStackHelper.fromData(IUItem.lappack, 1),

                ('A'),
                ("forge:plates/Alcled")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.imp_lappack), "ABA", "CEC", "ABA",

                ('E'), ItemStackHelper.fromData(IUItem.adv_lappack, 1),

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),

                ('B'), ItemStackHelper.fromData(IUItem.AdvlapotronCrystal, 1),

                ('A'),
                ("forge:doubleplate/Ferromanganese")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.per_lappack), "ABA", "CEC", "ABA",

                ('E'), ItemStackHelper.fromData(IUItem.imp_lappack, 1),

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'), ItemStackHelper.fromData(IUItem.compressIridiumplate, 1),

                ('A'),
                ("forge:doubleplate/Vitalium")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.advancedSolarHelmet), " A ", "BCB", "DED",

                ('D'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('E'), ItemStackHelper.fromData(IUItem.compressAlloy),

                ('C'), ItemStackHelper.fromData(IUItem.adv_nano_helmet, 1),

                ('B'),
                ItemStackHelper.fromData(IUItem.compresscarbon),

                ('A'), ItemStackHelper.fromData(IUItem.blockpanel)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.hybridSolarHelmet), " A ", "BCB", "DED",

                ('D'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('E'), ItemStackHelper.fromData(IUItem.compressAlloy),

                ('C'), ItemStackHelper.fromData(IUItem.quantum_helmet, 1),

                ('B'),
                IUItem.iridiumPlate,

                ('A'), ItemStackHelper.fromData(IUItem.blockpanel, 1, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ultimateSolarHelmet), " A ", "DCD", "BEB",

                ('D'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

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

                ('A'), "forge:storage_blocks/Bronze",

                ('B'), ItemStackHelper.fromData(IUItem.rotor_wood, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 2), " A ", "ABA", " A ",

                ('A'), "forge:storage_blocks/Iron",

                ('B'), ItemStackHelper.fromData(IUItem.rotor_bronze, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 3), " A ", "ABA", " A ",

                ('A'), "forge:plates/Steel",

                ('B'), ItemStackHelper.fromData(IUItem.rotor_iron, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 4), " A ", "ABA", " A ",

                ('A'), "forge:plates/Carbon",

                ('B'), ItemStackHelper.fromData(IUItem.rotor_steel, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 5), "CAC", "ABA", "CAC",

                ('C'), "forge:doubleplate/Iridium",

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

                ('D'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('C'), ItemStackHelper.fromData(IUItem.compressIridiumplate),

                ('A'), ItemStackHelper.fromData(IUItem.advnanobox),

                ('B'),
                ItemStackHelper.fromData(IUItem.compressiridium
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 8), "DCD", "ABA", " C ",

                ('D'), ItemStackHelper.fromData(IUItem.excitednucleus, 1, 5),

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

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

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

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

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), ItemStackHelper.fromData(IUItem.barionrotor
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewind, 1, 13), "ECE", "CBC", "ACA",

                ('E'), BlockEntityGenerationMicrochip.getLevelCircuit(ItemStackHelper.fromData(IUItem.basecircuit, 1, 21), 11),

                ('A'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('C'), ItemStackHelper.fromData(IUItem.photoniy_ingot),

                ('B'),
                ItemStackHelper.fromData(IUItem.adronrotor
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spectralSolarHelmet), " A ", "DCD", "BEB",

                ('D'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('E'), ItemStackHelper.fromData(IUItem.compressAlloy),

                ('C'), ItemStackHelper.fromData(IUItem.ultimateSolarHelmet, 1),

                ('B'),
                ItemStackHelper.fromData(IUItem.doublecompressIridiumplate, 1),

                ('A'), ItemStackHelper.fromData(IUItem.blockpanel, 1, 4)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.singularSolarHelmet), " A ", "DCD", "BDB",

                ('D'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('E'), ItemStackHelper.fromData(IUItem.compressAlloy),

                ('C'), ItemStackHelper.fromData(IUItem.spectralSolarHelmet, 1),

                ('B'),
                ItemStackHelper.fromData(IUItem.doublecompressIridiumplate, 1),

                ('A'), ItemStackHelper.fromData(IUItem.blockpanel, 1, 6)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines, 1, 5), "BCB", "DAD", "BCB",

                ('D'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('C'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('B'), ItemStackHelper.fromData(IUItem.photoniy_ingot),

                ('A'),
                ItemStackHelper.fromData(IUItem.machines, 1, 3)
        );
        BlockEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines, 1, 1),
                "DED",
                "BCB",
                "AAA",

                ('E'),
                ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('D'),
                ("forge:doubleplate/Alumel"),
                ('B'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('C'),
                ItemStackHelper.fromData(IUItem.simplemachine, 1, 6),

                ('A'),
                ItemStackHelper.fromData(IUItem.quantumtool)
        ), 0);
        BlockEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines, 1, 2),
                "DED",
                "BCB",
                "AAA",

                ('E'),
                ItemStackHelper.fromData(IUItem.core, 1, 7),

                ('D'),
                ("forge:doubleplate/Vitalium"),
                ('B'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('C'),
                ItemStackHelper.fromData(IUItem.machines, 1, 1),

                ('A'),
                ItemStackHelper.fromData(IUItem.advQuantumtool)
        ), 1);
        BlockEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines, 1, 3),
                "DED",
                "BCB",
                "AFA",

                ('F'),
                ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('E'),
                ItemStackHelper.fromData(IUItem.core, 1, 8),

                ('D'),
                ("forge:doubleplate/Duralumin"),
                ('B'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

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
                ("forge:doubleplate/Duralumin"),
                ('B'),
                BlockEntityGenerationMicrochip.getLevelCircuit(ItemStackHelper.fromData(IUItem.basecircuit, 1, 21), 11),

                ('C'),
                ItemStackHelper.fromData(IUItem.machines, 1, 3),

                ('A'),
                ItemStackHelper.fromData(IUItem.adv_spectral_box)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blocksintezator), "ABA", "BCB", "ABA",

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),
                BlockEntityGenerationMicrochip.getLevelCircuit(ItemStackHelper.fromData(IUItem.basecircuit, 1, 21), 11)};
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
        stacks3 = new ItemStack[]{BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),
                BlockEntityGenerationMicrochip.getLevelCircuit(ItemStackHelper.fromData(IUItem.basecircuit, 1, 21), 11)};
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

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
                ("forge:plates/Bronze"),
                ('C'), IUItem.carbonPlate,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 543),

                ('A'),
                ItemStackHelper.fromData(IUItem.basecircuit, 1, 0)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basecircuit, 1, 7), "CCC", "ABA", "DDD",
                ('D'), ("forge:plates/Steel"), ('C'), IUItem.carbonPlate, ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 549),

                ('A'),
                ItemStackHelper.fromData(IUItem.basecircuit, 1, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basecircuit, 1, 8), "CCC", "ABA", "DDD",

                ('D'),
                ("forge:plates/Spinel"),
                ('C'), IUItem.carbonPlate,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 555),

                ('A'),
                ItemStackHelper.fromData(IUItem.basecircuit, 1, 2)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basecircuit, 1, 20), "CCC", "ABA", "DDD",

                ('D'),
                ("forge:plates/SuperalloyRene"),
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
                ("forge:plates/Platinum"),
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                ItemStackHelper.fromData(IUItem.excitednucleus, 1, 1),
                ItemStackHelper.fromData(IUItem.excitednucleus, 1, 2),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),
                ItemStackHelper.fromData(IUItem.excitednucleus, 1, 4),
                ItemStackHelper.fromData(IUItem.excitednucleus, 1, 5),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),
                ItemStackHelper.fromData(IUItem.excitednucleus, 1, 7),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                ItemStackHelper.fromData(IUItem.excitednucleus, 1, 9),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),
                ItemStackHelper.fromData(IUItem.excitednucleus, 1, 11),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                ItemStackHelper.fromData(IUItem.excitednucleus, 1, 12),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8)};
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
                        getBlockStack(BlockBaseMachine3Entity.solar_iu),
                        ('E'), circuit[m]
                );
                Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.upgradepanelkit, 1, m), "ABA", "C C", "DED",

                        ('A'), ItemStackHelper.fromData(IUItem.solar_night_day_glass, 1, m),

                        ('B'), ItemStackHelper.fromData(IUItem.excitednucleus, 1, m),

                        ('C'), iridium[m],

                        ('D'),
                        getBlockStack(BlockBaseMachine3Entity.solar_iu),
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
                ("forge:plates/Caravky"),
                ('C'),
                ("forge:plates/Electrum"),
                ('B'),
                ("forge:plates/Invar"),
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
                ("forge:ingots/Germanium"),
                ('C'), ItemStackHelper.fromData(IUItem.module_schedule),

                ('B'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 158)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.analyzermodule), "BAB", "DCD",

                ('D'),
                ("forge:ingots/Germanium"),
                ('C'), ItemStackHelper.fromData(IUItem.module_schedule),

                ('B'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('A'), ItemStackHelper.fromData(IUItem.basemachine1, 1, 2)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machinekit, 1, 0), "ABA", "D D", "EEE",

                ('E'), ItemStackHelper.fromData(IUItem.nanoBox),

                ('D'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('B'),
                ("forge:doubleplate/Aluminium"),
                ('A'),
                ("forge:doubleplate/Alumel")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machinekit, 1, 1), "ABA", "D D", "EEE",

                ('E'), ItemStackHelper.fromData(IUItem.quantumtool),

                ('D'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('B'),
                ("forge:doubleplate/Platinum"),
                ('A'),
                ("forge:doubleplate/Vitalium")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machinekit, 1, 2), "ABA", "D D", "EEE",

                ('E'), ItemStackHelper.fromData(IUItem.advQuantumtool),

                ('D'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('B'),
                ("forge:doubleplate/Spinel"),
                ('A'),
                ("forge:doubleplate/Manganese")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.hazmathelmet), "AAA", "BCB", "DDD",

                ('A'),
                ("forge:plates/Mikhail"),
                ('B'),
                ("forge:plates/Platinum"),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 0),
                ('C'),
                ItemStackHelper.fromData(IUItem.hazmat_helmet, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.hazmatchest), "AAA", "BCB", "DDD",

                ('A'),
                ("forge:plates/Mikhail"),
                ('B'),
                ("forge:plates/Platinum"),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 0),
                ('C'),
                ItemStackHelper.fromData(IUItem.hazmat_chestplate, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.hazmatleggins), "AAA", "BCB", "DDD",

                ('A'),
                ("forge:plates/Mikhail"),
                ('B'),
                ("forge:plates/Platinum"),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 0),
                ('C'),
                ItemStackHelper.fromData(IUItem.hazmat_leggings, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.hazmatboosts), "AAA", "BCB", "DDD",

                ('A'),
                ("forge:plates/Mikhail"),
                ('B'),
                ("forge:plates/Platinum"),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 0),
                ('C'),
                ItemStackHelper.fromData(IUItem.rubber_boots, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.entitymodules), "ABA", "DCD", "EBE",

                ('A'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),

                ('B'), ItemStackHelper.fromData(IUItem.alloyscasing, 1, 2),

                ('C'), ItemStackHelper.fromData(IUItem.module_schedule),

                ('D'),
                ("forge:ingots/Invar"),
                ('E'), ItemStackHelper.fromData(IUItem.alloyscasing, 1, 4)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.entitymodules, 1, 1), "ABA", "DCD", "EBE",

                ('A'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('B'), ItemStackHelper.fromData(IUItem.adv_spectral_box),

                ('C'), ItemStackHelper.fromData(IUItem.module_schedule),

                ('D'),
                ("forge:doubleplate/Germanium"),
                ('E'), ItemStackHelper.fromData(IUItem.alloyscasing, 1, 9)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spawnermodules, 1, 0), "ABA",

                ('A'), ItemStackHelper.fromData(IUItem.module9, 1, 6),

                ('B'),
                ("forge:doubleplate/Nichrome")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spawnermodules, 1, 1), "ABA", "DCD", "EEE",

                ('E'), ItemStackHelper.fromData(IUItem.nanoBox),

                ('D'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('B'),
                ("forge:doubleplate/Aluminium"),
                ('A'),
                ("forge:doubleplate/Alumel"),
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

                ('D'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('B'),
                ("forge:doubleplate/Platinum"),
                ('A'),
                ("forge:doubleplate/Vitalium"),
                ('C'), ItemStackHelper.fromData(IUItem.spawnermodules, 1, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spawnermodules, 1, 3), "ABA",

                ('B'), ItemStackHelper.fromData(IUItem.module9, 1, 3),

                ('A'),
                ("forge:doubleplate/Nichrome")
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.spawnermodules, 1, 4),
                " C ", "ABA", " C ", ('B'), ItemStackHelper.fromData(IUItem.module_schedule, 1),
                ('A'), ("forge:doubleplate/Nichrome"), ('C'), new ItemStack(Items.EXPERIENCE_BOTTLE)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spawnermodules, 1, 5), "ABA", "DCD", "EEE",

                ('E'), ItemStackHelper.fromData(IUItem.nanoBox),

                ('D'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),

                ('B'),
                ("forge:doubleplate/Aluminium"),
                ('A'),
                ("forge:doubleplate/Alumel"),
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
                ("forge:plates/Alumel")
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
                ("forge:plates/Ferromanganese")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.steelHammer), "BBB", "BAB", " A ",

                ('A'), Items.STICK,

                ('B'),
                ("forge:plates/Steel")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ironHammer), "CBC", "BAB", " A ",

                ('A'), Items.STICK,

                ('B'),
                ("forge:plates/Iron"), 'C', "forge:platedense/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.anode), "B B", "BAB", "B B",

                ('A'), ItemStackHelper.fromData(IUItem.fluidCell, 1),

                ('B'),
                ("forge:plates/Muntsa")
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.expmodule),
                "   ", "ABA", "   ", ('A'), ItemStackHelper.fromData(IUItem.module_schedule),
                ('B'), new ItemStack(Items.EXPERIENCE_BOTTLE)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module_stack), "DCD", "BAB", " C ",

                ('D'),
                ("forge:plates/Alumel"),
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
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
                ("forge:doubleplate/Muntsa"),
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 516),

                ('B'), ItemStackHelper.fromData(IUItem.advQuantumtool, 1),
                'H', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 509),
                ('D'),
                ItemStackHelper.fromData(IUItem.nanodrill, 1),

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spectraldrill), "THT", "CDC", "BFB",

                ('T'), IUItem.iridiumPlate,

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 532),

                ('B'), ItemStackHelper.fromData(IUItem.adv_spectral_box, 1),
                'H', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 525),
                ('D'),
                ItemStackHelper.fromData(IUItem.quantumdrill, 1),

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.bags), "BCB", "BAB", "B B",

                ('C'), ItemStackHelper.fromData(IUItem.suBattery, 1),

                ('B'), new ItemStack(Items.LEATHER),

                ('A'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
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
                ("forge:doubleplate/Ferromanganese"),
                ('D'), ItemStackHelper.fromData(IUItem.electricJetpack, 1),

                ('C'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.impjetpack), "TCT", "CDC", "BFB",

                ('T'),
                ("forge:doubleplate/Muntsa"),
                ('F'), ItemStackHelper.fromData(IUItem.reBattery, 1),

                ('B'), ItemStackHelper.fromData(IUItem.quantumtool, 1),

                ('D'),
                ItemStackHelper.fromData(IUItem.advjetpack, 1),

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.perjetpack), "TCT", "CDC", "BFB",

                ('T'), IUItem.iridiumPlate,

                ('F'), ItemStackHelper.fromData(IUItem.advBattery, 1),

                ('B'), ItemStackHelper.fromData(IUItem.advQuantumtool, 1),

                ('D'),
                ItemStackHelper.fromData(IUItem.impjetpack, 1),

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 2), "   ", "ABC", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.module7),

                ('B'), IUItem.machine,

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 3), "   ", "ABC", "   ",

                ('A'), IUItem.module8,

                ('B'), IUItem.machine,

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
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

                ('D'), ("forge:plates/Manganese"), ('C'), ("forge:plates/Nickel"), ('A'),
                ("forge:plates/Invar"),
                ('B'),
                ItemStackHelper.fromData(IUItem.module_schedule)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machinekit, 1, 3), " A ", "BDC", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.machinekit, 1, 0),

                ('B'), ItemStackHelper.fromData(IUItem.machinekit, 1, 1),

                ('C'), ItemStackHelper.fromData(IUItem.machinekit, 1, 2),

                ('D'),
                ("forge:plates/Manganese")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 7), "DCD", "ABA", "DCD",

                ('A'), ItemStackHelper.fromData(IUItem.impmagnet, 1),

                ('B'), ItemStackHelper.fromData(IUItem.basemachine, 1, 14),

                ('C'),
                ("forge:plates/Electrum"),
                ('D'),
                ("forge:plates/Nickel")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.leadbox), "AAA", "ABA", "   ",

                ('A'),
                ("forge:ingots/Lead"),
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
                ModUtils.getCellFromFluid(FluidName.fluidnitrogen.getInstance().get()),
                ('B'), ItemStackHelper.fromData(IUItem.module_schedule), 'C', "forge:plates/Adamantium", 'D', "forge:plates/Stellite"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.coolupgrade, 1, 1), "ACA", "DBD", "ACA",

                ('A'),
                ModUtils.getCellFromFluid(FluidName.fluidhydrogen.getInstance().get()),
                ('B'), ItemStackHelper.fromData(IUItem.module_schedule), 'C', "forge:plates/Adamantium", 'D', "forge:plates/Stellite"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.coolupgrade, 1, 2), "ACA", "DBD", "ACA",

                ('A'),
                ModUtils.getCellFromFluid(FluidName.fluidhelium.getInstance().get()),
                ('B'), ItemStackHelper.fromData(IUItem.module_schedule), 'C', "forge:plates/Adamantium", 'D', "forge:plates/Stellite"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.autoheater), "CAC", "DBD", "C C",

                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 5),

                ('B'), ItemStackHelper.fromData(IUItem.module_schedule), 'C', "forge:plates/Inconel", 'D', "forge:plates/Mithril"
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

                ('B'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),

                ('C'), IUItem.advancedMachine,

                ('D'),
                ItemStackHelper.fromData(IUItem.lapotron_crystal, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 14), " D ", "ABA", " C ",

                ('A'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('B'), ItemStackHelper.fromData(IUItem.module_schedule),

                ('D'), ItemStackHelper.fromData(IUItem.machines_base, 1, 2),

                ('C'),
                ("forge:doubleplate/Muntsa")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.module9, 1, 15), " D ", "ABA", " C ",

                ('A'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('B'), ItemStackHelper.fromData(IUItem.module_schedule),

                ('D'), ItemStackHelper.fromData(IUItem.machines_base1, 1, 9),

                ('C'),
                ("forge:doubleplate/Vanadoalumite")
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

                ('B'), getBlockStack(BlockPrimalFluidHeaterEntity.primal_fluid_heater),
                ('A'), ItemStackHelper.fromData(IUItem.blastfurnace, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blastfurnace, 1, 1), "CAC", "ABA", "CAC",

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('A'), ItemStackHelper.fromData(IUItem.blastfurnace, 1, 5),
                'C', "forge:gears/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blastfurnace, 1, 4), " A ", "ABA", " A ",

                ('B'),
                getBlockStack(BlockBaseMachine3Entity.steel_tank),
                ('A'), ItemStackHelper.fromData(IUItem.blastfurnace, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blastfurnace, 1, 3), "CAC", "ABA", "CAC",

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('A'), ItemStackHelper.fromData(IUItem.blastfurnace, 1, 5),
                'C', "forge:gears/Electrum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blastfurnace, 1, 0), " A ", "ABA", " A ",

                ('B'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),

                ('A'), ItemStackHelper.fromData(IUItem.blastfurnace, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spectral_box), "D D", "ABA", "C C",

                ('D'), ItemStackHelper.fromData(IUItem.coal_chunk1),

                ('C'), IUItem.iridiumPlate,

                ('A'),
                ("forge:doubleplate/Spinel"),
                ('B'),
                ItemStackHelper.fromData(IUItem.quantumtool)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_spectral_box), "DAD", "ABA", "CEC",

                ('E'), ItemStackHelper.fromData(IUItem.coal_chunk1),

                ('D'), ItemStackHelper.fromData(IUItem.photoniy_ingot),

                ('C'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('A'),
                ("forge:doubleplate/Spinel"),
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
                ('D'), "forge:doubleplate/Vanadoalumite", ('B'), ItemStackHelper.fromData(IUItem.photoniy_ingot), ('C'),
                "forge:plates/Orichalcum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 0), "A A", "CBC", "A A",

                ('A'), ItemStackHelper.fromData(IUItem.advnanobox),
                ('B'), ItemStackHelper.fromData(IUItem.rotorupgrade_schemes), ('C'), "forge:casings/Nichrome"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 1), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.advQuantumtool), ('B'), ItemStackHelper.fromData(IUItem.core, 1, 3),
                ('C'), ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 0), ('D'),
                "forge:casings/Redbrass"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 2), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.adv_spectral_box), ('B'), ItemStackHelper.fromData(IUItem.core, 1, 5),
                ('C'), ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 1), ('D'),
                "forge:casings/Muntsa"
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
                "forge:casings/Duralumin"
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
                "forge:doubleplate/Alumel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 10), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.core, 1, 5),
                ('B'), ItemStackHelper.fromData(IUItem.rotorupgrade_schemes), ('C'), IUItem.compressIridiumplate, ('D'),
                "forge:doubleplate/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electric_hoe), "AA ", " A ", " B ",

                ('A'), "forge:plates/Iron",
                ('B'), IUItem.powerunitsmall
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spawnermodules, 1, 8), "ECE", "BAB", "DCD",

                ('A'), ItemStackHelper.fromData(IUItem.module_schedule), ('E'), ItemStackHelper.fromData(IUItem.core, 1, 5),
                ('B'), ItemStackHelper.fromData(IUItem.quantumtool), ('C'),
                IUItem.iridiumPlate, ('D'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.volcano_hazmat_helmet, 1), "ACA", "BAB", "A A",

                ('A'), "forge:plates/Carbon",
                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 478), ('C'),
                "forge:gems/Topaz"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.volcano_hazmat_leggings, 1), "ACA", "B B", "A A",

                ('A'), "forge:plates/Carbon",
                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 478), ('C'),
                "forge:gems/Topaz"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.volcano_rubber_boots, 1), "A A", "C C", "B B",

                ('A'), "forge:plates/Carbon",
                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 478), ('C'),
                "forge:gems/Topaz"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.volcano_hazmat_chestplate, 1), "AAA", "BCB", "AAA",

                ('A'), "forge:plates/Carbon",
                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 478), ('C'),
                "forge:gems/Topaz"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 38), "CBC", "ADA", "EBE",

                ('E'), IUItem.adv_spectral_box,

                ('D'), IUItem.advancedMachine,

                ('C'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('B'),
                ("forge:doubleplate/Muntsa"),
                ('A'),
                ("forge:doubleplate/Aluminumbronze")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 32), " C ", "CDC", "ECE",

                ('C'), IUItem.insulatedCopperCableItem,

                ('D'), IUItem.advancedMachine,

                ('E'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 33), " C ", "CDC", "ECE",

                ('C'), IUItem.insulatedCopperCableItem,

                ('D'), IUItem.advancedMachine,

                ('E'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rotors_upgrade, 1, 11), "ADA", "CBC", "DED",

                ('A'), ItemStackHelper.fromData(IUItem.advnanobox),
                ('B'), ItemStackHelper.fromData(IUItem.rotorupgrade_schemes), ('C'), IUItem.iridiumPlate, ('D'),
                "forge:doubleplate/Platinum", ('E'), ItemStackHelper.fromData(IUItem.core, 1, 3)
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
                "forge:doubleplate/Vitalium", ('E'), ItemStackHelper.fromData(IUItem.core, 1, 4)
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
                "forge:doubleplate/Redbrass",

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
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 42), ('B'), "forge:ingots/Gold", ('C'), "forge:ingots/Iron",
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 650)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20), "ABA", "ECD", "  ",

                ('A'), "forge:doubleplate/Electrum", ('B'), "forge:doubleplate/Alumel", ('C'), IUItem.elemotor, ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 16), 'E', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 614)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96), " B ", "ACA", " D ",

                ('A'), "forge:doubleplate/Platinum",

                ('B'), "forge:doubleplate/Vitalium",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),

                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 92)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120), " B ", "ACA", " D ",

                ('A'), "forge:doubleplate/Spinel", ('B'), "forge:doubleplate/Manganese", ('C'), ItemStackHelper.fromData(
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

                ('A'), "forge:plates/Bronze", ('C'), "forge:plates/Tin", ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 42)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 92), "AAA", "CBC", "   ",

                ('A'), "forge:plates/Platinum", ('C'), "forge:plates/Cobalt", ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 16)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 116), "AAA", "CBC", "   ",

                ('A'), "forge:plates/Spinel", ('C'), "forge:plates/Manganese", ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 92)
        );
        Recipes.recipe.addRecipe(IUItem.elemotor, " A ", "BCB", " D ",
                ('A'), IUItem.casingtin, ('B'), IUItem.coil, ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 60),
                ('D'),
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 25), "CCC", "DAD", "EBE",
                ('B'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 21), ('C'), ItemStackHelper.fromData(
                        IUItem.nanoBox),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 16), 'E', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 651)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 23), "CCC", "DAD", "EBE",
                ('B'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8), ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 25), ('C'), ItemStackHelper.fromData(
                        IUItem.quantumtool),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 92), 'E', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 652)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 24), "CCC", "DAD", "EBE",
                ('B'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10), ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 23), ('C'), ItemStackHelper.fromData(
                        IUItem.spectral_box),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 116), 'E', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 653)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 42), "AAA", "CBC", "   ",

                ('B'), ItemStackHelper.fromData(IUItem.basecircuit, 1, 17),
                ('C'), Items.REDSTONE,

                ('A'), "forge:ingots/Tungsten"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 41), "AAA", "BCB", "AAA",
                ('A'), Items.FLINT, ('B'), "forge:ingots/Iron", ('C'), "forge:ingots/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 46), " A ", "ABA", "ABA",
                ('A'), "forge:plates/Iron", ('B'), "forge:plates/Aluminium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 35), " B ", "BAB", "B B",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 46),

                ('B'), "forge:plates/Chromium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72), " B ", "BAB", "CCC",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 46),

                ('B'), "forge:plates/Mikhail",

                ('C'), "forge:plates/Cobalt"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 70), " B ", "BAB", "CCC",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 46),

                ('B'), "forge:plates/Zinc",

                ('C'), "forge:plates/Magnesium"
        );
        craft_modules(69, 41, 21);
        craft_modules(63, 76, 21);
        craft_modules(159, 141, 21);
        craft_modules(163, 142, 21);
        craft_modules(132, 144, 21);
        craft_modules(165, 164, 21);

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 142), "AAA", "BCB", " D ",

                ('A'), "forge:plates/Platinum", ('C'), "forge:plates/Titanium",
                ('B'), "forge:plates/Cobalt", ('D'),
                "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 200), "AAA", "BCB", "   ",

                ('A'), "forge:plates/Zinc", ('B'), "forge:plates/Titanium",
                ('C'), "forge:plates/Aluminium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 141), "ABB", "C D", " AA",

                ('A'), "forge:plates/Cobalt", ('B'), "forge:plates/Nickel",
                ('C'), "forge:plates/Iron", ('D'),
                "forge:plates/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 144), "AAA", " B ", " C ",

                ('A'), "forge:plates/Nickel", ('B'), "forge:plates/Cobalt",
                ('C'), "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 76), "AAA", " B ", "AAA",

                ('A'), "forge:plates/Iron", ('B'), "forge:plates/Tungsten"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 164), "BAB", "B B", "BAB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 200),
                ('B'), "forge:plates/Tungsten"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 137), "BA ", "AB ", "   ",
                ('A'), "forge:plates/Steel", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 501)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blockResource, 1, 12), "AAA", "A A", "AAA",
                ('A'), "forge:plates/Aluminumbronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138), "BA ", "CC ", "   ",
                ('A'), "forge:plates/Electrum", ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 137), 'C', IUItem.plastic_plate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139), "BA ", "CC ", "   ",
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 479), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                'C', "forge:casings/Bloodstone"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140), "BC ", "AA ", "   ",
                ('A'), "forge:casings/Draconid", ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139), 'C', "forge:plates/Woods"
        );
        Recipes.recipe.addShapelessRecipe(Blocks.STICKY_PISTON, Blocks.PISTON, IUItem.latex);
        Recipes.recipe.addRecipe(IUItem.machine, "AA ", "AA ", "   ",
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 137)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 121), "AAA", "ABA", "AAA",
                ('A'), "forge:plates/Cobalt", ('B'), Items.STRING
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 8, 122), "AAA", "BBB", "CCC",
                ('A'), "forge:plates/Tungsten", ('B'), "forge:plates/Titanium", 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 480)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 201), "AAA", "BBB", "   ",
                ('A'), "forge:plates/Titanium", ('B'), "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 0), "AAA", "ABA", "AAA",
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 444), ('B'), "forge:plates/Cobalt"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 180), "ABA", "   ", "   ",
                ('A'), "forge:plates/Copper", ('B'), "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 181), " A ", "ABA", "   ",
                ('A'), "forge:plates/Copper", ('B'), "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 182), " A ", " B ", " A ",
                ('A'), "forge:plates/Copper", ('B'), "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 183), "   ", "ABA", " A ",
                ('A'), "forge:plates/Copper", ('B'), "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 184), "   ", "A A", " A ",
                ('A'), "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 185), " A ", "A A", "   ",
                ('A'), "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 186), " A ", "A  ", "AA ",
                ('A'), "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 187), " A ", "  A", " A ",
                ('A'), "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 188), "ABA", "   ", "   ",
                ('A'), "forge:plates/Duralumin", ('B'), "forge:plates/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 191), " A ", "ABA", "   ",
                ('A'), "forge:plates/Duralumin", ('B'), "forge:plates/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 189), " A ", " B ", " A ",
                ('A'), "forge:plates/Duralumin", ('B'), "forge:plates/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 190), "   ", "ABA", " A ",
                ('A'), "forge:plates/Duralumin", ('B'), "forge:plates/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 194), " A ", "A A", "   ",
                ('A'), "forge:plates/Spinel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 195), "   ", "A A", " A ",
                ('A'), "forge:plates/Spinel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 193), " A ", "A  ", "AA ",
                ('A'), "forge:plates/Spinel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 192), " A ", "  A", " A ",
                ('A'), "forge:plates/Spinel"
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

                ('B'), "forge:plates/Tin",

                ('C'), "forge:plates/Chromium",

                ('D'),
                "forge:plates/Gold"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 37), "BBB", " A ", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 21),

                ('B'), "forge:plates/Electrum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 30), "BBB", " A ", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 21),

                ('B'), "forge:plates/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 74), "B B", "DAD", "CCC",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 21),

                ('B'), "forge:platedense/Steel",

                ('C'), "forge:plates/Iron",

                ('D'),
                "forge:plates/Copper"
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

                ('B'), "forge:plates/Tin"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47), " B ", "ABA", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),

                ('B'), "forge:plates/Tin"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49), " B ", "CBC", " C ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),

                ('B'), "forge:plates/Tin",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51), " B ", "ABA", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),

                ('B'), "forge:plates/Tin"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52), " B ", "ABA", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),

                ('B'), "forge:plates/Tin"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27), "CBC", "CBC", "CAC",
                ('A'), DEFAULT_SENSOR,
                ('C'), IUItem.FluidCell, ('B'), "forge:gears/Magnesium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 6), "CBC", "CBC", "CAC",
                ('A'), ADV_SENSOR,
                ('C'), IUItem.FluidCell, ('B'), "forge:gears/Magnesium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 83), "CBC", "CBC", "CAC",
                ('A'), IMP_SENSOR,
                ('C'), IUItem.FluidCell, ('B'), "forge:gears/Magnesium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 107), "CBC", "CBC", "CAC",
                ('A'), PER_SENSOR,
                ('C'), IUItem.FluidCell, ('B'), "forge:gears/Magnesium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 603), "CBC", "CBC", "CAC",
                ('A'), PHOTON_SENSOR,
                ('C'), IUItem.FluidCell, ('B'), "forge:gears/Magnesium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 31), "CCC", "BAB", "   ",
                ('A'), DEFAULT_SENSOR,
                ('B'), "forge:gears/vanadium", ('C'), "forge:gears/Iridium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 7), "CCC", "BAB", "   ",
                ('A'), ADV_SENSOR,
                ('B'), "forge:gears/vanadium", ('C'), "forge:gears/Iridium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 84), "CCC", "BAB", "   ",
                ('A'), IMP_SENSOR,
                ('B'), "forge:gears/vanadium", ('C'), "forge:gears/Iridium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 108), "CCC", "BAB", "   ",
                ('A'), PER_SENSOR,
                ('B'), "forge:gears/vanadium", ('C'), "forge:gears/Iridium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 33), "DCD", "BAB", "EFE",
                ('A'), DEFAULT_SENSOR,
                ('B'), "forge:plates/vanadium", ('D'), "forge:gears/Manganese", ('C'),
                "forge:casings/Nickel", ('E'), "forge:casings/Mikhail", ('F'), "forge:doubleplate/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 9), "DCD", "BAB", "EFE",
                ('A'), ADV_SENSOR,
                ('B'), "forge:plates/vanadium", ('D'), "forge:gears/Manganese", ('C'),
                "forge:casings/Nickel", ('E'), "forge:casings/Mikhail", ('F'), "forge:doubleplate/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 86), "DCD", "BAB", "EFE",
                ('A'), IMP_SENSOR,
                ('B'), "forge:plates/vanadium", ('D'), "forge:gears/Manganese", ('C'),
                "forge:casings/Nickel", ('E'), "forge:casings/Mikhail", ('F'), "forge:doubleplate/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 110), "DCD", "BAB", "EFE",
                ('A'), PER_SENSOR,
                ('B'), "forge:plates/vanadium", ('D'), "forge:gears/Manganese", ('C'),
                "forge:casings/Nickel", ('E'), "forge:casings/Mikhail", ('F'), "forge:doubleplate/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 607), "DCD", "BAB", "EFE",
                ('A'), PHOTON_SENSOR,
                ('B'), "forge:plates/vanadium", ('D'), "forge:gears/Manganese", ('C'),
                "forge:casings/Nickel", ('E'), "forge:casings/Mikhail", ('F'), "forge:doubleplate/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 34), "CBC", "BCB", " A ",
                ('A'), DEFAULT_SENSOR, ('B'), "forge:plates/Manganese",
                ('C'), "forge:gears/Aluminium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 12), "CBC", "BCB", " A ",
                ('A'), ADV_SENSOR, ('B'), "forge:plates/Manganese",
                ('C'), "forge:gears/Aluminium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 87), "CBC", "BCB", " A ",
                ('A'), IMP_SENSOR, ('B'), "forge:plates/Manganese",
                ('C'), "forge:gears/Aluminium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 111), "CBC", "BCB", " A ",
                ('A'), PER_SENSOR, ('B'), "forge:plates/Manganese",
                ('C'), "forge:gears/Aluminium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 610), "CBC", "BCB", " A ",
                ('A'), PHOTON_SENSOR, ('B'), "forge:plates/Manganese",
                ('C'), "forge:gears/Aluminium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 36), " B ", "BAB", " B ",
                ('A'), DEFAULT_SENSOR, ('B'), "forge:ingots/Uranium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 13), " B ", "BAB", " B ",
                ('A'), ADV_SENSOR, ('B'), "forge:ingots/Uranium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 88), " B ", "BAB", " B ",
                ('A'), IMP_SENSOR, ('B'), "forge:ingots/Uranium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 113), " B ", "BAB", " B ",
                ('A'), PER_SENSOR, ('B'), "forge:ingots/Uranium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 39), "CCC", "BAB", "BBB",
                ('A'), DEFAULT_SENSOR, ('B'), "forge:plates/Muntsa", ('C'), "forge:plates/Nichrome"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 15), "CCC", "BAB", "BBB",
                ('A'), ADV_SENSOR, ('B'), "forge:plates/Muntsa", ('C'), "forge:plates/Nichrome"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 91), "CCC", "BAB", "BBB",
                ('A'), IMP_SENSOR, ('B'), "forge:plates/Muntsa", ('C'), "forge:plates/Nichrome"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 115), "CCC", "BAB", "BBB",
                ('A'), PER_SENSOR, ('B'), "forge:plates/Muntsa", ('C'), "forge:plates/Nichrome"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 613), "CCC", "BAB", "BBB",
                ('A'), PHOTON_SENSOR, ('B'), "forge:plates/Muntsa", ('C'), "forge:plates/Nichrome"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43), "BCB", "BAB", "BCB",

                ('A'), DEFAULT_SENSOR,

                ('B'), IUItem.FluidCell,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 655)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 45), "BCB", "BAB", "BCB",

                ('A'), DEFAULT_SENSOR,

                ('B'), IUItem.FluidCell,

                ('C'), "forge:plates/Alumel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 48), "BCB", "BAB", "BCB",

                ('A'), DEFAULT_SENSOR,

                ('B'), IUItem.FluidCell,

                ('C'), "forge:plates/Ferromanganese"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 50), "BCB", "BAB", "BCB",

                ('A'), DEFAULT_SENSOR,

                ('B'), IUItem.FluidCell,

                ('C'), "forge:plates/Aluminumbronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 53), " B ", "CAD", " E ",

                ('A'), DEFAULT_SENSOR,

                ('B'), "forge:ores/Iron",

                ('C'), "forge:ores/Gold",

                ('D'),
                "forge:ores/Emerald",

                ('E'), "forge:ores/Diamond"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 54), "BBB", "CAC", "EEE",
                ('A'), DEFAULT_SENSOR, ('B'), new ItemStack(Items.WITHER_SKELETON_SKULL),

                ('C'), "forge:plates/Aluminumbronze",

                ('E'),
                "forge:plates/Alumel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 56), "FBG", "CAD", "HEJ",
                ('A'), DEFAULT_SENSOR,
                ('B'), "forge:plates/Alcled", ('C'), "forge:plates/Alumel",
                ('D'),
                "forge:plates/Vitalium", ('E'), "forge:plates/Redbrass",
                ('F'), "forge:plates/Muntsa", ('G'), "forge:plates/Nichrome",
                ('H'), "forge:plates/Vanadoalumite", ('J'),
                "forge:plates/Duralumin"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 57), "   ", " AB", "   ",
                ('A'), DEFAULT_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.module7, 1, 4)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 58), "EDE", "BBB", "CAC",

                ('A'), ItemStackHelper.fromData(IUItem.electricblock, 1, 3),

                ('B'),
                getBlockStack(BlockBaseMachine3Entity.teleporter_iu),
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
                ('D'), "forge:plates/Ferromanganese"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 67), "DDD", "BAB", "   ",
                ('A'), DEFAULT_SENSOR, ('B'), IUItem.overclockerUpgrade,
                ('D'), "forge:plates/Vitalium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 68), "CCC", "BAB", " B ",
                ('A'), DEFAULT_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.core, 1, 4),

                ('C'), "forge:doubleplate/Muntsa"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 179), "CCC", "BAB", " B ",
                ('A'), ADV_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('C'), "forge:doubleplate/Muntsa"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 177), "CCC", "BAB", " B ",
                ('A'), IMP_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.core, 1, 6),

                ('C'), "forge:doubleplate/Muntsa"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 178), "CCC", "BAB", " B ",
                ('A'), PER_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.core, 1, 7),

                ('C'), "forge:doubleplate/Muntsa"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 626), "CCC", "BAB", " B ",
                ('A'), PHOTON_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.core, 1, 7),

                ('C'), "forge:doubleplate/Muntsa"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 71), "CCC", "BAB", "DED",

                ('A'), DEFAULT_SENSOR,

                ('C'), "forge:gears/Germanium",

                ('B'), "forge:doubleplate/Iridium",

                ('D'),
                "forge:gears/Redbrass",

                ('E'), "forge:doubleplate/Vitalium"
        );
        Recipes.recipe.addRecipe(IUItem.iridiumPlate, "ABA", "BCB", "ABA",

                ('A'), IUItem.iridiumOre,

                ('C'), "forge:gems/Diamond",

                ('B'), IUItem.advancedAlloy
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 3), "CCC", "BAB", "DED",
                ('A'), ADV_SENSOR, ('C'), "forge:gears/Germanium", ('B'), "forge:doubleplate/Iridium",
                ('D'),
                "forge:gears/Redbrass", ('E'), IUItem.iridiumOre
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 80), "CCC", "BAB", "DED",
                ('A'), IMP_SENSOR, ('C'), "forge:gears/Germanium", ('B'), "forge:doubleplate/Iridium",
                ('D'),
                "forge:gears/Redbrass", ('E'), IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 104), "CCC", "BAB", "DED",
                ('A'), PER_SENSOR, ('C'), "forge:gears/Germanium", ('B'), "forge:doubleplate/Iridium",
                ('D'),
                "forge:gears/Redbrass", ('E'), IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 75), "CCC", " A ", " B ",
                ('A'), DEFAULT_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.itemiu, 1, 3),
                ('C'), "forge:plates/Cobalt"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 10), "CCC", "BAB", "   ",
                ('A'), ADV_SENSOR, ('B'), ItemStackHelper.fromData(IUItem.itemiu, 1, 3),
                ('C'), "forge:plates/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 98), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidpetroleum.getInstance().get()),
                ('C'), "forge:gears/Magnesium", ('D'),
                "forge:plates/Titanium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 98), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidsour_light_oil.getInstance().get()),
                ('C'), "forge:gears/Magnesium", ('D'),
                "forge:plates/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 98), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidsour_medium_oil.getInstance().get()),
                ('C'), "forge:gears/Magnesium", ('D'),
                "forge:plates/Titanium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 98), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidsour_heavy_oil.getInstance().get()),
                ('C'), "forge:gears/Magnesium", ('D'),
                "forge:plates/Titanium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 98), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidsweet_medium_oil.getInstance().get()),
                ('C'), "forge:gears/Magnesium", ('D'),
                "forge:plates/Titanium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 98), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidsweet_heavy_oil.getInstance().get()),
                ('C'), "forge:gears/Magnesium", ('D'),
                "forge:plates/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 99), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), IUItem.Uran238,
                ('C'), "forge:gears/vanadium", ('D'),
                "forge:plates/Chromium"
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
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
                "forge:plates/Tin"

        );
        Recipes.recipe.addRecipe(
                IUItem.reactorCoolantTriple,
                " A ",
                "ABA",
                " A ",
                'B',
                IUItem.helium_cooling_mixture,
                'A',
                "forge:plates/Nichrome"

        );
        Recipes.recipe.addRecipe(
                IUItem.reactorCoolantSix,
                " A ",
                "ABA",
                " A ",
                'B',
                IUItem.cryogenic_cooling_mixture,
                'A',
                "forge:plates/Vitalium"

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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),
                'D', ModUtils.setSize(IUItem.overclockerUpgrade_1, 1), 'E', IUItem.advQuantumtool

        );

        Recipes.recipe.addRecipe(
                getBlockStack(BlockBaseMachine3Entity.facademechanism),
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
                "forge:ingots/Chromium",
                'A',
                "forge:ingots/Titanium"

        );
        Recipes.recipe.addRecipe(
                IUItem.facadeItem.getItemStack(),
                "B B",
                "CAC",
                "D D",
                'C',
                "forge:ingots/Chromium",
                'A',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B', "forge:ingots/Iron", 'D', "forge:ingots/Germanium"

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
                "forge:plates/Iron",
                'C',
                "forge:ingots/Iron",
                'B',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_pump),
                "A A", "CBC", "A A", 'A', "forge:plates/Carbon", 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 344), 'B', IUItem.pump
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 641),
                " AB", "ACD", "BD ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 640), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 639), 'B', "forge:ingots/SuperalloyRene", 'C', ItemStackHelper.fromData(IUItem.core, 1, 7)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 195),
                "AFB", "ECE", "BDA",
                'A', "forge:plates/Osmiridium",
                'D', ItemStackHelper.fromData(IUItem.core, 1, 5),
                'F', ItemStackHelper.fromData(IUItem.neutronium),
                'B', "forge:plates/NiobiumTitanium",
                'C', ItemStackHelper.fromData(IUItem.blockdoublemolecular),
                'E', BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 194),
                "ADA", "BCB", "EFE",
                'A', "forge:plates/TantalumTungstenHafnium",
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 68),
                'F', ItemStackHelper.fromData(IUItem.basemachine2, 1, 189),
                'B', "forge:plates/NiobiumTitanium",
                'C', ItemStackHelper.fromData(IUItem.basemachine2, 1, 38),
                'E', "forge:plates/Duralumin"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.imp_pump),
                "A A", "CBC", "A A", 'A', "forge:gems/Topaz", 'C', "forge:ingots/Gold", 'B', IUItem.adv_pump
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.per_pump),
                "A A", "CBC", "A A", 'A', "forge:gems/Diamond", 'C', "forge:plates/Osmium", 'B', IUItem.imp_pump
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.fan),
                "ACA", "CBC", "ACA", 'A', "forge:plates/Iron", 'C', "forge:plates/Titanium", 'B',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_fan),
                "ACA", "CBC", "ACA", 'A', "forge:plates/Electrum", 'C', "forge:plates/Titanium", 'B', IUItem.fan
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.imp_fan),
                "ACA", "CBC", "ACA", 'A', "forge:plates/Platinum", 'C', "forge:plates/Titanium", 'B', IUItem.adv_fan
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.per_fan),
                "ACA", "CBC", "ACA", 'A', "forge:plates/Cadmium", 'C', "forge:plates/Titanium", 'B', IUItem.imp_fan
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.simple_capacitor_item),
                "AAA", "BBB", "AAA", 'A', "forge:ingots/Iron", 'B', "forge:plates/Invar"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_capacitor_item),
                "AAA", "BCB", "AAA", 'A', "forge:plates/Carbon", 'B', "forge:plates/Obsidian", 'C', IUItem.simple_capacitor_item
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.imp_capacitor_item),
                "AAA", "BCB", "AAA", 'A', "forge:plates/Steel", 'B', "forge:plates/Titanium", 'C', IUItem.adv_capacitor_item
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.per_capacitor_item),
                "AAA", "BCB", "AAA", 'A', "forge:plates/Ferromanganese", 'B', "forge:plates/Germanium", 'C', IUItem.imp_capacitor_item
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.simple_exchanger_item),
                "ABA", "ABA", "AAA", 'A', "forge:plates/Iron", 'B', IUItem.reactor_plate
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_exchanger_item),
                "ABA", "ABA", "AAA", 'A', "forge:plates/Aluminium", 'B', IUItem.adv_reactor_plate
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.imp_exchanger_item),
                "ABA", "ABA", "AAA", 'A', "forge:plates/Aluminumbronze", 'B', IUItem.imp_reactor_plate
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.per_exchanger_item),
                "ABA", "ABA", "AAA", 'A', "forge:plates/Duralumin", 'B', IUItem.per_reactor_plate
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 8),
                "ABA", "BCB", "ABA", 'A', IUItem.quad_uranium_fuel_rod, 'B', "forge:plates/Lead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 9),
                "ABA", "BCB", "ABA", 'A', IUItem.quad_mox_fuel_rod, 'B', "forge:plates/Lead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 0),
                "ABA", "BCB", "ABA", 'A', IUItem.reactoramericiumQuad, 'B', "forge:plates/Lead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 1),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorneptuniumQuad, 'B', "forge:plates/Lead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 2),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorcuriumQuad, 'B', "forge:plates/Lead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 3),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorcaliforniaQuad, 'B', "forge:plates/Lead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 4),
                "ABA", "BCB", "ABA", 'A', IUItem.reactortoriyQuad, 'B', "forge:plates/Lead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 5),
                "ABA", "BCB", "ABA", 'A', IUItem.reactormendeleviumQuad, 'B', "forge:plates/Lead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 6),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorberkeliumQuad, 'B', "forge:plates/Lead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 7),
                "ABA", "BCB", "ABA", 'A', IUItem.reactoreinsteiniumQuad, 'B', "forge:plates/Lead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 8),
                "ABA", "BCB", "ABA", 'A', IUItem.reactoruran233Quad, 'B', "forge:plates/Lead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 9),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorprotonQuad, 'B', "forge:plates/Lead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 10),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorfermiumQuad, 'B', "forge:plates/Lead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 11),
                "ABA", "BCB", "ABA", 'A', IUItem.reactornobeliumQuad, 'B', "forge:plates/Lead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pellets, 1, 12),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorlawrenciumQuad, 'B', "forge:plates/Lead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.radiationModule, 1, 0),
                "BCB",
                "DED",
                "BAB",
                'A',
                IUItem.module_schedule,
                'B',
                "forge:plates/Tantalum",
                'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                "forge:plates/Nickel",
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 1),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 0), 'B', "forge:plates/Cadmium", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3), 'D',
                "forge:plates/Nickel", 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 2),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 1), 'B', "forge:doubleplate/Cadmium", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5), 'D',
                "forge:plates/Nickel", 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 3),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 2), 'B', "forge:doubleplate/Duralumin", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'D',
                "forge:plates/Nickel", 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.radiationModule, 1, 4),
                "BCB",
                "DED",
                "BAB",
                'A',
                IUItem.module_schedule,
                'B',
                "forge:plates/Tantalum",
                'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                ItemStackHelper.fromData(IUItem.radiationresources, 1, 0),
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 5),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 4), 'B', "forge:plates/Cadmium", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3), 'D',
                ItemStackHelper.fromData(IUItem.radiationresources, 1, 0), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 6),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 5), 'B', "forge:doubleplate/Cadmium", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'D',
                ItemStackHelper.fromData(IUItem.radiationresources, 1, 0), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 7),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 6), 'B', "forge:doubleplate/Duralumin", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'D',
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
                "forge:plates/Tantalum",
                'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                "forge:doubleplate/Alumel",
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 9),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 8), 'B', "forge:plates/Cadmium", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4), 'D',
                "forge:doubleplate/Alumel", 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 10),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 9), 'B', "forge:doubleplate/Cadmium", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'D',
                "forge:doubleplate/Alumel", 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 11),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 10), 'B', "forge:doubleplate/Duralumin", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'D',
                "forge:doubleplate/Alumel", 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.radiationModule, 1, 12),
                "BCB",
                "DED",
                "BAB",
                'A',
                IUItem.module_schedule,
                'B',
                "forge:plates/Tantalum",
                'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                ItemStackHelper.fromData(IUItem.vent),
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 13),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 12), 'B', "forge:plates/Cadmium", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4), 'D',
                ItemStackHelper.fromData(IUItem.adv_Vent), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 14),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 13), 'B', "forge:doubleplate/Cadmium", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'D',
                ItemStackHelper.fromData(IUItem.imp_Vent), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 15),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 14), 'B', "forge:doubleplate/Duralumin", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'D',
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
                "forge:plates/Tantalum",
                'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                ItemStackHelper.fromData(IUItem.componentVent),
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 17),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 16), 'B', "forge:plates/Cadmium", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4), 'D',
                ItemStackHelper.fromData(IUItem.adv_componentVent), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 18),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 17), 'B', "forge:doubleplate/Cadmium", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'D',
                ItemStackHelper.fromData(IUItem.imp_componentVent), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 19),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 18), 'B', "forge:doubleplate/Duralumin", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'D',
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
                "forge:plates/Tantalum",
                'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                ItemStackHelper.fromData(IUItem.heat_exchange),
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 21),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 20), 'B', "forge:plates/Cadmium", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4), 'D',
                ItemStackHelper.fromData(IUItem.adv_heat_exchange), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 22),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 21), 'B', "forge:doubleplate/Cadmium", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'D',
                ItemStackHelper.fromData(IUItem.imp_heat_exchange), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 23),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 22), 'B', "forge:doubleplate/Duralumin", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'D',
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
                "forge:plates/Tantalum",
                'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                ItemStackHelper.fromData(IUItem.capacitor),
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 25),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 24), 'B', "forge:plates/Cadmium", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3), 'D',
                ItemStackHelper.fromData(IUItem.adv_capacitor), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 26),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 25), 'B', "forge:doubleplate/Cadmium", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'D',
                ItemStackHelper.fromData(IUItem.imp_capacitor), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radiationModule, 1, 27),
                "BCB", "DED", "BAB", 'A', ItemStackHelper.fromData(IUItem.radiationModule, 1, 26), 'B', "forge:doubleplate/Duralumin", 'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8), 'D',
                ItemStackHelper.fromData(IUItem.per_capacitor), 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 323),
                "BCB", "BAB", "DDD", 'A', DEFAULT_SENSOR, 'B', "forge:plates/Steel", 'D', "forge:ingots/Mikhail", 'C', "forge:gears/Osmium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 325),
                " C ", "BAB", "DCD", 'A', DEFAULT_SENSOR, 'B', ItemStackHelper.fromData(IUItem.nuclear_res), 'D', "forge:plates/Carbon", 'C',
                "forge:plates/Lapis"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 328),
                "ECE", "BAB", "DCD", 'A', DEFAULT_SENSOR, 'B', "forge:plates/Iron", 'D', "forge:plates/Silver", 'C',
                "forge:plates/Osmium", 'E', IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 440),
                "   ", "BAB", "DCD", 'A', DEFAULT_SENSOR, 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 386), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 320), 'C',
                "forge:plates/Tantalum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 441),
                "CDC", "BAB", "BDB", 'A', DEFAULT_SENSOR, 'B', ItemStackHelper.fromData(IUItem.itemiu, 1, 2), 'D',
                "forge:platedense/Lead", 'C',
                "forge:plates/Spinel"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 442),
                "CDC", "BAB", "BDB", 'A', DEFAULT_SENSOR, 'B', ItemStackHelper.fromData(IUItem.sunnariumpanel, 1, 0), 'D',
                ItemStackHelper.fromData(IUItem.sunnarium, 1, 3), 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 319)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 354),
                "BAB", "BAB", "BDB", 'A', "forge:gems/Ruby", 'B', "forge:plates/Titanium", 'D',
                "forge:dusts/Redstone"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 1),
                " ", "CAD", " B ", 'A', "forge:machineBlock", 'B', IUItem.elemotor, 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 36),
                'C', ItemStackHelper.fromData(IUItem.module7, 1, 9)
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 20),
                " ",
                "CAD",
                " B ",
                'A',
                "forge:machineBlockAdvanced",
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
                "forge:machineBlock",
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
                "forge:machineBlock",
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
                "forge:machineBlock",
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
                "forge:machineBlock",
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
                "forge:machineBlock",
                'B',
                IUItem.elemotor,
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 354),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44), 'H', getBlockStack(BlockBaseMachine3Entity.steam_sharpener)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 99),
                " E ",
                "DAC",
                " B ",
                'A',
                "forge:machineBlock",
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
                "forge:machineBlock",
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
                "forge:machineBlock",
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
                "forge:plates/Osmium",
                'C',
                "forge:plates/Neodymium",
                'D',
                "forge:plates/Obsidian"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 101),
                " E ",
                "DAC",
                " B ",
                'A',
                "forge:machineBlock",
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
                "forge:machineBlock",
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
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
                "forge:plates/Inconel",
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
                "forge:casings/Nichrome"
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
                "forge:casings/Orichalcum"
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
                "forge:casings/Inconel"
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
                "forge:casings/Adamantium"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 130),
                "AAA",
                "CBE",
                " D ",
                'A',
                "forge:plates/Inconel",
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
                getBlockStack(BlockBaseMachine3Entity.steamdryer),
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
                getBlockStack(BlockBaseMachine3Entity.steam_squeezer),
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
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
                "forge:plates/Nimonic",
                'C',
                "forge:plates/SuperalloyRene",
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
                "forge:plates/Tantalum",
                'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 621),
                'C',
                "forge:plates/Arsenic",
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
                "forge:plates/Nimonic",
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

                ('D'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

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
                "forge:plates/Inconel",
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
                getBlockStack(BlockBaseMachine2Entity.electrolyzer_iu),
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
                "   ", "BAC", " D ", 'A', "forge:machineBlockAdvanced", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 439), 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 36), 'D', IUItem.elemotor

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 104),
                "FEF", "BAC", " D ", 'A', "forge:machineBlockAdvanced", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47), 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 35), 'D', IUItem.elemotor, 'E', Items.ENCHANTED_BOOK, 'F',
                "forge:doubleplate/Cadmium"

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 105),
                " E ", "BAC", " D ", 'A', "forge:machineBlock", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44), 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43), 'D', IUItem.elemotor, 'E', IUItem.coolant

        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 103),
                "FBG",
                "EAC",
                " D ",
                'A',
                "forge:machineBlockAdvanced",
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
                "C C", " B ", "A A", 'B', "forge:machineBlock", 'A', "forge:plates/StainlessSteel", 'C', "forge:plates/MolybdenumSteel"
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                "forge:rods/Zinc"
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                "forge:gears/Osmium",
                'E',
                "forge:plates/Carbon"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.earthQuarry, 1, 3),
                " E ", "DAD", "CBC", 'B', ItemStackHelper.fromData(IUItem.oilquarry), 'A', ItemStackHelper.fromData(IUItem.earthQuarry, 1, 2)
                , 'C'
                , "forge:plates/Titanium", 'D', "forge:plates/Steel", 'E', "forge:plates/Iridium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.earthQuarry, 1, 4),
                "   ", "DAD", "CBC", 'B', new ItemStack(Blocks.CHEST), 'A', ItemStackHelper.fromData(IUItem.earthQuarry, 1, 2)
                , 'C'
                , "forge:plates/Gold", 'D', "forge:plates/Tin"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.earthQuarry, 1, 5),
                "BDB", "DAD", "BDB", 'B', ItemStackHelper.fromData(IUItem.item_pipes, 1, 1), 'D', ItemStackHelper.fromData(IUItem.item_pipes), 'A',
                ItemStackHelper.fromData(IUItem.earthQuarry, 1, 2)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.anvil),
                "AAA", " A ", "BBB", 'A', "forge:storage_blocks/Iron", 'B', "forge:storage_blocks/Titanium"
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 364),
                "AB ", "BA ", "   ", 'A', "forge:plates/Lead", 'B', "forge:plates/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 363),
                "AB ", "BA ", "CC ", 'A', "forge:plates/Lead", 'B', "forge:plates/Tungsten", 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 479)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 365),
                "AB ", "BA ", "   ", 'A', "forge:plates/Lead", 'B', "forge:plates/Lithium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 366),
                "AB ", "BC ", "   ", 'A', "forge:plates/Lead", 'B', "forge:plates/Platinum", 'C', "forge:plates/Tantalum"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 2, 420),
                "AA ",
                "BC ",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 366),
                'B',
                "forge:plates/Electrum",
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
                "forge:plates/Cobalt",
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
                "forge:plates/Magnesium",
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
                "forge:plates/Electrum",
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
                "forge:plates/Cobalt",
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
                "forge:plates/Magnesium",
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
                "forge:plates/Electrum",
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
                "forge:plates/Cobalt",
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
                "forge:plates/Magnesium",
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
                "forge:plates/Electrum",
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
                "forge:plates/Cobalt",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 453)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 393),
                "AA ", "BC ", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 376), 'B', "forge:plates/Magnesium", 'C',
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                'C',
                "forge:plates/Bronze",
                'E',
                "forge:doubleplate/Osmium"
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 435),
                "BCB",
                "BAB",
                "BDB",
                'A',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B',
                "forge:plates/Platinum",
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B',
                "forge:plates/Zinc",
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
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B',
                "forge:plates/Lithium",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 386),
                'D',
                "forge:plates/Obsidian"
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 324),
                "AAA",
                " B ",
                "DCD",
                'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'A',
                "forge:plates/Cobalt",
                'B',
                IUItem.advancedAlloy,
                'D',
                "forge:plates/Carbon"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 322),
                "ABA",
                "EFE",
                "DCD",
                'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'A',
                "forge:plates/Tungsten",
                'B',
                IUItem.advancedAlloy,
                'D',
                "forge:plates/Bronze",
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 445),
                'F',
                "forge:plates/Tin"
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 385),
                "A A",
                "EFG",
                "DCD",
                'C',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'A',
                "forge:plates/Steel",
                'D',
                "forge:plates/Bor",
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 445),
                'F',
                "forge:plates/Carbon",
                'G',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 446)
        );


        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 321),
                "B B",
                "DAD",
                "B B",
                'A',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 320),
                'D',
                "forge:plates/Cadmium"
        );


        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 327),
                "BCB",
                "DAD",
                "BCB",
                'A',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B',
                "forge:plates/Carbon",
                'D',
                "forge:plates/Osmium",
                'C',
                "forge:plates/Electrum"
        );


        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 326),
                "BCB",
                "BAB",
                "DCD",
                'A',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B',
                "forge:plates/Cobalt",
                'D',
                "forge:plates/Vanadoalumite",
                'C',
                "forge:plates/Manganese"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356),
                " B ", " A ", " B ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 294), 'B', "forge:plates/Iron"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424),
                "AAA", "ABA", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356), 'B', "forge:plates/Electrum"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371),
                "AAA", "ABA", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424), 'B', "forge:plates/Platinum"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397),
                "AAA", "ABA", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371), 'B', "forge:plates/Spinel"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 387),
                " A ", "ABA", " A ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356), 'B', "forge:doubleplate/Germanium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 425),
                " A ", "ABA", " A ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 387), 'B', "forge:doubleplate/Alumel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 372),
                " A ", "ABA", " A ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 425), 'B', "forge:doubleplate/Vitalium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 398),
                " A ", "ABA", " A ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 372), 'B', "forge:doubleplate/Ferromanganese"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 362),
                "CDC", "BAB", "CDC", 'A', ItemStackHelper.fromData(IUItem.itemiu, 1, 3), 'B', ItemStackHelper.fromData(IUItem.neutroniumingot), 'C',
                "forge:plates/Germanium", 'D', "forge:plates/Osmium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 101), "CCC", "BAB", "DED",

                ('A'), DEFAULT_SENSOR,

                ('C'), "forge:gears/Germanium",

                ('B'), "forge:doubleplate/Germanium",

                ('D'),
                "forge:gears/Nichrome",

                ('E'), "forge:doubleplate/Vitalium"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 4),
                "CCC",
                "BAB",
                "DED",
                ('A'),
                ADV_SENSOR,
                ('C'),
                "forge:gears/Germanium",
                ('B'),
                "forge:doubleplate/Germanium",
                ('D'),
                "forge:gears/Nichrome",
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
                "forge:gears/Germanium",
                ('B'),
                "forge:doubleplate/Germanium",
                ('D'),
                "forge:gears/Nichrome",
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
                "forge:gears/Germanium",
                ('B'),
                "forge:doubleplate/Germanium",
                ('D'),
                "forge:gears/Nichrome",
                ('E'),
                IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 112), "BBB", "CAC", "   ",

                ('A'), DEFAULT_SENSOR,

                ('B'), IUItem.photoniy_ingot,

                ('C'), "forge:gears/Zinc"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 124), "CBC", "BAB", " B ",

                ('A'), DEFAULT_SENSOR,

                ('B'), "forge:doubleplate/Vitalium",

                ('C'), "forge:doubleplate/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 135), "CBC", "BAB", " B ",
                ('A'), ADV_SENSOR, ('B'), "forge:doubleplate/Vitalium",
                ('C'), "forge:doubleplate/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 146), "CBC", "BAB", " B ",
                ('A'), IMP_SENSOR, ('B'), "forge:doubleplate/Vitalium",
                ('C'), "forge:doubleplate/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 157), "CBC", "BAB", " B ",
                ('A'), PER_SENSOR, ('B'), "forge:doubleplate/Vitalium",
                ('C'), "forge:doubleplate/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 632), "CBC", "BAB", " B ",
                ('A'), PHOTON_SENSOR, ('B'), "forge:doubleplate/Vitalium",
                ('C'), "forge:doubleplate/Invar"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crop, 4), "A A", "A A", "   ",

                ('A'), Items.STICK


        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 128), "DCD", "BAB", " E ",

                ('A'), DEFAULT_SENSOR,

                ('C'), Blocks.CRAFTING_TABLE,

                ('B'), "forge:plates/Platinum",

                ('D'),
                "forge:plates/Tin",

                ('E'), "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 129), "DCD", "BAB", " E ",
                ('A'), ADV_SENSOR, ('C'), Blocks.CRAFTING_TABLE,
                ('B'), "forge:plates/Platinum", ('D'),
                "forge:plates/Tin", ('E'), "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 130), "DCD", "BAB", " E ",
                ('A'), IMP_SENSOR, ('C'), Blocks.CRAFTING_TABLE,
                ('B'), "forge:plates/Platinum", ('D'),
                "forge:plates/Tin", ('E'), "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 131), "DCD", "BAB", " E ",
                ('A'), PER_SENSOR, ('C'), Blocks.CRAFTING_TABLE,
                ('B'), "forge:plates/Platinum", ('D'),
                "forge:plates/Tin", ('E'), "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 605), "DCD", "BAB", " E ",
                ('A'), PHOTON_SENSOR, ('C'), Blocks.CRAFTING_TABLE,
                ('B'), "forge:plates/Platinum", ('D'),
                "forge:plates/Tin", ('E'), "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 154), "CCC", "BAB", "   ",

                ('A'), DEFAULT_SENSOR,

                ('C'), IUItem.FluidCell,

                ('B'), "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 155), "CCC", "BAB", "   ",

                ('A'), DEFAULT_SENSOR,

                ('C'),
                ModUtils.getCellFromFluid(FluidName.fluidcoolant.getInstance().get()),
                ('B'), "forge:plates/Nickel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 156), " C ", "BAB", " D ",
                ('A'), DEFAULT_SENSOR, ('B'), "forge:plates/Germanium",
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 60), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 158), "CDE", "BAB", "FFF",

                ('A'), DEFAULT_SENSOR,

                ('C'), Items.DIAMOND_AXE,

                ('D'), Items.DIAMOND_PICKAXE,

                ('E'),
                Items.DIAMOND_SHOVEL,

                ('B'), "forge:gears/Iridium",

                ('F'), "forge:gears/Magnesium"
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
                "forge:gears/Mikhail",
                ('D'),
                "forge:gears/Platinum",
                ('C'),
                IUItem.carbonPlate,
                ('E'),
                "forge:doubleplate/Redbrass"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 79), "CCC", "BAB", "EDE",

                ('A'), DEFAULT_SENSOR,

                ('B'), IUItem.advancedAlloy,

                ('C'), IUItem.carbonPlate,

                ('D'),
                IUItem.toriy,

                ('E'), "forge:plates/Bronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 176), "CCC", "BAB", "EDE",

                ('A'), ADV_SENSOR,

                ('B'), IUItem.advancedAlloy,

                ('C'), IUItem.iridiumPlate,

                ('D'),
                ItemStackHelper.fromData(IUItem.radiationresources, 1, 4),

                ('E'), "forge:plates/Bronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 174), "CCC", "BAB", "EDE",

                ('A'), IMP_SENSOR,

                ('B'), IUItem.advancedAlloy,

                ('C'), IUItem.iridiumPlate,

                ('D'),
                ItemStackHelper.fromData(IUItem.radiationresources, 1, 4),

                ('E'), "forge:plates/Bronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 175), "CCC", "BAB", "EDE",

                ('A'), PER_SENSOR,

                ('B'), IUItem.advancedAlloy,

                ('C'), IUItem.iridiumPlate,

                ('D'),
                ItemStackHelper.fromData(IUItem.radiationresources, 1, 4),

                ('E'), "forge:plates/Bronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 627), "CCC", "BAB", "EDE",

                ('A'), PHOTON_SENSOR,

                ('B'), IUItem.advancedAlloy,

                ('C'), IUItem.iridiumPlate,

                ('D'),
                ItemStackHelper.fromData(IUItem.radiationresources, 1, 4),

                ('E'), "forge:plates/Bronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 217), " C ", "BAB", "DDD",

                ('A'), DEFAULT_SENSOR,

                ('B'), "forge:plates/Alumel",

                ('C'), Items.FISHING_ROD,

                ('D'),
                "forge:gears/Muntsa"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 219), " C ", "BAB", "DDD",
                ('A'), DEFAULT_SENSOR, ('B'), "forge:plates/Iron", ('D'), Items.REDSTONE,
                ('C'),
                Blocks.FURNACE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 225), " C ", "BAB", "DDD",
                ('A'), ADV_SENSOR, ('B'), "forge:plates/Iron", ('D'), Items.REDSTONE,
                ('C'),
                Blocks.FURNACE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 221), " C ", "BAB", "DDD",
                ('A'), IMP_SENSOR, ('B'), "forge:plates/Iron", ('D'), Items.REDSTONE,
                ('C'),
                Blocks.FURNACE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 223), " C ", "BAB", "DDD",
                ('A'), PER_SENSOR, ('B'), "forge:plates/Iron", ('D'), Items.REDSTONE,
                ('C'),
                Blocks.FURNACE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 617), " C ", "BAB", "DDD",
                ('A'), PHOTON_SENSOR, ('B'), "forge:plates/Iron", ('D'), Items.REDSTONE,
                ('C'),
                Blocks.FURNACE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 218), "CCC", "BAB", "DDD",
                ('A'), DEFAULT_SENSOR, ('B'), "forge:plates/Iron", ('C'), "forge:plates/Tin",
                ('D'),
                "forge:plates/Copper"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 224), "CCC", "BAB", "DDD",
                ('A'), ADV_SENSOR, ('B'), "forge:plates/Iron", ('C'), "forge:plates/Tin",
                ('D'),
                "forge:plates/Copper"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 220), "CCC", "BAB", "DDD",
                ('A'), IMP_SENSOR, ('B'), "forge:plates/Iron", ('C'), "forge:plates/Tin",
                ('D'),
                "forge:plates/Copper"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 222), "CCC", "BAB", "DDD",
                ('A'), PER_SENSOR, ('B'), "forge:plates/Iron", ('C'), "forge:plates/Tin",
                ('D'),
                "forge:plates/Copper"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 606), "CCC", "BAB", "DDD",
                ('A'), PHOTON_SENSOR, ('B'), "forge:plates/Iron", ('C'), "forge:plates/Tin",
                ('D'),
                "forge:plates/Copper"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 205), "CCC", "BAB", "DED",
                ('A'), DEFAULT_SENSOR,
                ('B'), "forge:gears/Cobalt", ('C'), IUItem.advancedAlloy, ('D'),
                "forge:plates/Titanium", 'E', IUItem.plastic_plate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 204), "CCC", "BAB", "DED",
                ('A'), ADV_SENSOR,
                ('B'), "forge:gears/Cobalt", ('C'), IUItem.advancedAlloy, ('D'),
                "forge:plates/Titanium", 'E', IUItem.plastic_plate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 202), "CCC", "BAB", "DED",
                ('A'), IMP_SENSOR,
                ('B'), "forge:gears/Cobalt", ('C'), IUItem.advancedAlloy, ('D'),
                "forge:plates/Titanium", 'E', IUItem.plastic_plate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 203), "CCC", "BAB", "DED",
                ('A'), PER_SENSOR,
                ('B'), "forge:gears/Cobalt", ('C'), IUItem.advancedAlloy, ('D'),
                "forge:plates/Titanium", 'E', IUItem.plastic_plate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 633), "CCC", "BAB", "DED",
                ('A'), PHOTON_SENSOR,
                ('B'), "forge:gears/Cobalt", ('C'), IUItem.advancedAlloy, ('D'),
                "forge:plates/Titanium", 'E', IUItem.plastic_plate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 38), "CCC", "BAB", "DDD",

                ('A'), DEFAULT_SENSOR,

                ('B'), "forge:doubleplate/Germanium",

                ('C'), "forge:gears/Vitalium",

                ('D'),
                "forge:gears/Alcled"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 14), "CCC", "BAB", "DDD",
                ('A'), ADV_SENSOR, ('B'), "forge:doubleplate/Germanium",
                ('C'), "forge:gears/Vitalium", ('D'),
                "forge:gears/Alcled"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 89), "CCC", "BAB", "DDD",
                ('A'), IMP_SENSOR, ('B'), "forge:doubleplate/Germanium",
                ('C'), "forge:gears/Vitalium", ('D'),
                "forge:gears/Alcled"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 114), "CCC", "BAB", "DDD",

                ('A'), PER_SENSOR,

                ('B'), "forge:doubleplate/Germanium",

                ('C'), "forge:gears/Vitalium",

                ('D'),
                "forge:gears/Alcled"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.universal_cable, 1, 1), " A ", "BCB", " A ",

                ('C'),
                ("forge:ingots/Cobalt"),
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
                ("forge:ingots/Redbrass"),
                ('C'), IUItem.carbonPlate,

                ('A'), ItemStackHelper.fromData(IUItem.universal_cable, 1, 3),

                ('B'),
                ("forge:ingots/Spinel")
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.universal_cable, 1, 5), " A ", "BCB", " A ",

                ('C'),
                ("forge:doubleplate/Vitalium"),
                ('A'), ItemStackHelper.fromData(IUItem.universal_cable, 1, 4),

                ('B'), IUItem.denseplateadviron
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.universal_cable, 1, 6), "DAD", "BCB", "DAD",

                ('D'), IUItem.carbonPlate,

                ('C'),
                ("forge:ingots/Alcled"),
                ('A'), ItemStackHelper.fromData(IUItem.universal_cable, 1, 5),

                ('B'),
                ("forge:ingots/Duralumin")
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
                'C', "forge:gears/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cokeoven, 1, 4), " A ", "ABA", " A ",

                ('B'),
                getBlockStack(BlockBaseMachine3Entity.steel_tank),
                ('A'), ItemStackHelper.fromData(IUItem.cokeoven, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cokeoven, 1, 3), "CAC", "ABA", "CAC",

                ('B'), getBlockStack(BlockBaseMachine3Entity.steel_tank),
                ('A'), ItemStackHelper.fromData(IUItem.cokeoven, 1, 5),
                'C', "forge:gears/Electrum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.recipe_schedule), "AB ", "BAB", "BBA",

                ('B'), "forge:plates/AluminiumSilicon",
                ('A'), "forge:plates/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cokeoven, 1, 5), " B ", "CAC", " B ",

                ('C'), "forge:casings/AluminiumSilicon",

                ('A'), new ItemStack(Blocks.BRICKS),

                ('B'), "forge:casings/HafniumCarbide"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cokeoven, 1, 2), " A ", " B ", "   ",

                ('C'), "forge:plates/AluminiumSilicon",

                ('A'), ItemStackHelper.fromData(IUItem.primalFluidHeater),

                ('B'), ItemStackHelper.fromData(IUItem.cokeoven, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cokeoven, 1, 0), "DED", "CAC", " B ",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 387),

                ('A'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),

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

                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 568), 'A', "forge:plates/Iron", 'C', "forge:plates/Zinc", 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 581)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 600), "AAA", "BDB", "AAA",

                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 567), 'A', "forge:plates/Electrum", 'C', "forge:plates/Zinc", 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 579)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 591), "AAA", "BDB", "AAA",

                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 565), 'A', "forge:plates/Platinum", 'C', "forge:plates/Zinc", 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 563)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 561), "AAA", "BDB", "AAA",

                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 565), 'A', "forge:plates/HafniumBoride", 'C', "forge:plates/Zinc", 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 585)
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 570), "A ", "BAB", "BBB",

                'B', "forge:plates/Iron", 'A', ItemStackHelper.fromData(IUItem.iudust, 1, 60)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 573), "A ", "BAB", "BBB",

                'B', "forge:plates/Gold", 'A', ItemStackHelper.fromData(IUItem.iudust, 1, 60)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 590), "A ", "BAB", "BBB",

                'B', "forge:plates/Platinum", 'A', "forge:plates/Neodymium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 559), "A ", "BAB", "BBB",

                'B', "forge:gems/Diamond", 'A', "forge:plates/Nichrome"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 576), "ACA", "BCB", "ACA",

                'B', "forge:plates/Carbon", 'A', "forge:plates/Bronze", 'C', "forge:dusts/Redstone"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 566), "ACA", "BCB", "ACA",

                'B', "forge:plates/Osmium", 'A', "forge:plates/HafniumCarbide", 'C', "forge:dusts/Redstone"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 584), "ACA", "BCB", "ACA",

                'B', "forge:plates/Niobium", 'A', "forge:plates/MolybdenumSteel", 'C', "forge:dusts/Redstone"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 564), "ACA", "BCB", "ACA",

                'B', "forge:plates/BerylliumBronze", 'A', "forge:plates/Woods", 'C', "forge:dusts/Redstone"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 572), "AAA", "ABA", "AAA",

                'B', "forge:plates/Tin", 'A', "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 577), "CAC", "ABA", "CAC",

                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 572), 'A', "forge:plates/Cobalt", 'C', "forge:plates/Chromium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 575), "AAA", "ABA", "AAA",

                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 577), 'A', "forge:plates/YttriumAluminiumGarnet", 'C', "forge:plates/Chromium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 587), "AAA", "ABA", "AAA",

                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 575), 'A', "forge:plates/Inconel", 'C', "forge:plates/Chromium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 580), "ABC", "BBC", "ABC",

                'B', "forge:plates/Copper", 'A', "forge:ingots/Chromium", 'C', ItemStackHelper.fromData(IUItem.cable, 1, 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 560), "ABC", "BBC", "ABC",

                'B', "forge:plates/Aluminumbronze", 'A', "forge:ingots/Chromium", 'C', ItemStackHelper.fromData(IUItem.cable, 1, 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 589), "ABC", "BBC", "ABC",

                'B', "forge:plates/Permalloy", 'A', "forge:ingots/Chromium", 'C', ItemStackHelper.fromData(IUItem.cable, 1, 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 596), "ABC", "BBC", "ABC",

                'B', "forge:plates/Stellite", 'A', "forge:ingots/Chromium", 'C', ItemStackHelper.fromData(IUItem.cable, 1, 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 571), " BB", " BB", "A  ",

                'B', "forge:plates/Iron", 'A', "forge:ingots/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 562), " BB", " BB", "A  ",

                'B', "forge:plates/Bronze", 'A', "forge:ingots/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 595), " BB", " BB", "A  ",

                'B', "forge:plates/Steel", 'A', "forge:ingots/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 586), " BB", " BB", "A  ",

                'B', "forge:plates/StainlessSteel", 'A', "forge:ingots/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 508), " B ", "BBB", "BBB",

                'B', "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 517), " B ", "BBB", "ACA",

                'B', "forge:plates/Carbon", 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 508), 'A', IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 509), " B ", "BBB", "ACA",

                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 285), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 517), 'A',
                IUItem.quantumtool
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 525), " B ", "BBB", "ACA",

                'B', "forge:plates/SuperalloyHaynes", 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 509), 'A', IUItem.spectral_box
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 518), " AA", " AB", "ABB",

                'B', "forge:plates/Carbon", 'A', "forge:plates/Redbrass"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 510), " AA", " AB", "ABB",

                'B', "forge:plates/StainlessSteel", 'A', "forge:plates/NiobiumTitanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 526), " AA", " AB", "ABB",

                'B', "forge:plates/AluminiumLithium", 'A', "forge:plates/Alcled"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 521), "A  ", "BA ", "BBA",

                'B', "forge:plates/Carbon", 'A', "forge:plates/Redbrass"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 513), "A  ", "BA ", "BBA",

                'B', "forge:plates/StainlessSteel", 'A', "forge:plates/NiobiumTitanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 529), "A  ", "BA ", "BBA",

                'B', "forge:plates/AluminiumLithium", 'A', "forge:plates/Alcled"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 522), "BA ", " BA", "   ",

                'B', "forge:plates/Carbon", 'A', "forge:plates/Redbrass"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 514), "BA ", " BA", "   ",

                'B', "forge:plates/StainlessSteel", 'A', "forge:plates/NiobiumTitanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 530), "BA ", " BA", "   ",

                'B', "forge:plates/AluminiumLithium", 'A', "forge:plates/Alcled"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 519), "AAB", "AB ", "   ",

                'B', "forge:plates/Carbon", 'A', "forge:plates/Redbrass"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 511), "AAB", "AB ", "   ",

                'B', "forge:plates/StainlessSteel", 'A', "forge:plates/NiobiumTitanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 527), "AAB", "AB ", "   ",

                'B', "forge:plates/AluminiumLithium", 'A', "forge:plates/Alcled"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 520), "AAB", "ABB", "AB ",

                'B', "forge:plates/Carbon", 'A', "forge:plates/Redbrass"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 512), "AAB", "ABB", "AB ",

                'B', "forge:plates/StainlessSteel", 'A', "forge:plates/NiobiumTitanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 528), "AAB", "ABB", "AB ",

                'B', "forge:plates/AluminiumLithium", 'A', "forge:plates/Alcled"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 523), "BA ", "BBA", " BA",

                'B', "forge:plates/Carbon", 'A', "forge:plates/Redbrass"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 515), "BA ", "BBA", " BA",

                'B', "forge:plates/StainlessSteel", 'A', "forge:plates/NiobiumTitanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 531), "BA ", "BBA", " BA",

                'B', "forge:plates/AluminiumLithium", 'A', "forge:plates/Alcled"
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 524),
                "CBC",
                "BAB",
                "CDC",

                'B',
                "forge:plates/Carbon",
                'C',
                "forge:plates/Redbrass",
                'A',
                IUItem.energy_crystal,
                'D',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 516),
                "CBC",
                "BAB",
                "CDC",

                'B',
                "forge:plates/StainlessSteel",
                'C',
                "forge:plates/NiobiumTitanium",
                'A',
                IUItem.lapotron_crystal,
                'D',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 532),
                "CBC",
                "BAB",
                "CDC",

                'B',
                "forge:plates/AluminiumLithium",
                'C',
                "forge:plates/Alcled",
                'A',
                IUItem.AdvlapotronCrystal,
                'D',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9)
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
                'B', ItemStackHelper.fromData(IUItem.basecircuit, 1, 18), 'A', "forge:rods/Arsenic"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 489), "DCC", "CBB", "AA ",
                'B', "forge:plates/Zirconium", 'A', "forge:plates/Cadmium", 'C', "forge:plates/Ferromanganese", 'D', "forge:plates/Niobium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 491), " C ", "BAB", " C ",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 488), 'B', "forge:plates/Gadolinium", 'C', "forge:plates/Barium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 486), " C ", "BAB", " C ",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 491), 'B', "forge:plates/Redbrass", 'C', "forge:plates/Duralumin"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ObsidianForgeHammer), "ABA", "ACA", " C ",
                'A', "forge:plates/Obsidian", 'B', "forge:plates/Steel", 'C', Items.STICK
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 485), " C ", "BAB", " C ",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 486), 'B', "forge:plates/TantalumTungstenHafnium", 'C',
                "forge:plates/Osmiridium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 490), " C ", "BAB", " C ",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 485), 'B', "forge:plates/Zeliber", 'C',
                "forge:plates/Nitenol"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 588), "CCB", "CAA", "CCB",
                'A', "forge:plates/Thallium", 'B', "forge:plates/Strontium", 'C',
                "forge:plates/Niobium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 598), " CB", "CDA", " CB",
                'A', "forge:plates/Nitenol", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 581), 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 568), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 588)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 592),
                "ECB",
                "CDA",
                "ECB",
                'A',
                "forge:plates/BerylliumBronze",
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 563),
                'E',
                "forge:plates/TantalumTungstenHafnium",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 565),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 598)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 594), "ECB", "CDA", "ECB",
                'A', "forge:plates/Inconel", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 585), 'E', "forge:plates/StainlessSteel",
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 565), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 592)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blockMacerator), "C C", "BAB", "BCB",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 41), 'B', "forge:plates/Ferromanganese", 'E', "forge:plates/StainlessSteel",
                'C',
                "forge:plates/Invar", 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 592)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blockCompressor), "C C", "BAB", "BCB",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 76), 'B', "forge:plates/Ferromanganese", 'E', "forge:plates/StainlessSteel",
                'C',
                "planks", 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 76)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.dryer), "CBC", "CBC", "CBC",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 76), 'B', Blocks.GLASS, 'E', "forge:plates/StainlessSteel",
                'C',
                "planks", 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 76)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.squeezer), "CEC", "CBC", "CDC",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 76), 'B', Blocks.GLASS, 'E', IUItem.treetap,
                'C',
                "planks", 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 601)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.fluidIntegrator), "A A", "ACA", "EDE",
                'A', "forge:plates/Electrum", 'B', Blocks.GLASS, 'E', "forge:plates/Ferromanganese",
                'C',
                "forge:plates/Invar", 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 601)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.primalFluidHeater), "BCB", "BCB", "AAA",
                'A', "forge:plates/Aluminumbronze", 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 601), 'B', "forge:plates/Bronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.primalSiliconCrystal), "AAA", "ABA", "AAA",
                'A', "forge:plates/Iron", 'B', Blocks.GLASS
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.primalPolisher), " B ", "ADA", "ACA",
                'A', "forge:plates/Titanium", 'B', Blocks.GLASS, 'D', "forge:plates/Iron", 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 354)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.programming_table), " B ", "ADA", "ACA",
                'A', "forge:plates/Titanium", 'B', Blocks.GLASS, 'D', "forge:plates/Carbon", 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 354)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.solderingMechanism), " B ", "ADA", "ACA",
                'A', "forge:plates/Titanium", 'B', Blocks.GLASS, 'D', "forge:plates/Iron", 'C', ItemStackHelper.fromData(IUItem.solderingIron)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.electronics_assembler), " B ", "ADA", "ACA",
                'A', "forge:plates/Titanium", 'B', Blocks.GLASS, 'D', "forge:plates/Iron", 'C', ItemStackHelper.fromData(IUItem.basecircuit, 1, 17)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.primal_pump), "   ", "DAD", "DCD",
                'A', Blocks.GLASS, 'D', "forge:plates/Iron", 'C', ItemStackHelper.fromData(IUItem.basemachine2, 1, 185)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 3, 478), "   ", "DAD", "D D",
                'A', "forge:dyes/Yellow", 'D', IUItem.rubber
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.radioprotector), "BBB", " A ", "A  ",
                'A', "forge:plates/AluminiumSilicon", 'B', "forge:plates/Nitenol"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.steamPipe, 6), "BBB", "AAA", "BBB",
                'A', "forge:plates/Polonium", 'B', "forge:casings/Aluminumbronze"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.biopipes, 6), "BBB", "AAA", "BBB",
                'A', "forge:plates/Tantalum", 'B', "forge:casings/Thallium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_cathode), " A ", "ABA", "CCC",
                'A', "forge:plates/TantalumTungstenHafnium", 'B', IUItem.cathode, 'C', ItemStackHelper.fromData(IUItem.iudust, 1, 63)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_anode), " A ", "ABA", "CCC",
                'A', "forge:plates/Osmiridium", 'B', IUItem.anode, 'C', ItemStackHelper.fromData(IUItem.iudust, 1, 63)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ironMesh), " BB", " AA", " BB",
                'A', Items.STRING, 'B', "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.steelMesh), " BB", " BB", " BA",
                'A', IUItem.ironMesh, 'B', "forge:plates/Steel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.boridehafniumMesh), " BB", " BB", " BA",
                'A', IUItem.steelMesh, 'B', "forge:plates/HafniumBoride"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.vanadiumaluminumMesh), " BB", " BB", " BA",
                'A', IUItem.boridehafniumMesh, 'B', "forge:plates/Vanadoalumite"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.steleticMesh), " BB", " BB", " BA",
                'A', IUItem.vanadiumaluminumMesh, 'B', "forge:plates/Stellite"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.gasSensor),
                "DCD",
                "BAB",
                "  ",
                'A',
                BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),
                'B',
                "forge:plates/Polonium",
                'C',
                "forge:plates/Zirconium",
                'D',
                "forge:plates/NiobiumTitanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.reactorData), "DCD", "BAB", "  ",
                'A', BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'B', "forge:plates/Nimonic", 'C', "forge:plates/Woods",
                'D', "forge:plates/Zeliber"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32), "CBC", "BAB", "EDE", 'A', DEFAULT_SENSOR,
                'B', "forge:gears/Invar", 'C', "forge:gems/Sapphire", 'D', "forge:gears/Ferromanganese", 'E', "forge:plates/Ferromanganese"

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 123), "CBC", "BAB", "EDE", 'A', DEFAULT_SENSOR,
                'B', "forge:gears/Electrum", 'C', "forge:gems/Topaz", 'D', "forge:gears/Aluminumbronze", 'E', "forge:plates/Aluminumbronze"

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
                'B', getBlockStack(BlockBaseMachine3Entity.oak_tank), 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12)

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 180), "DEA", "BBB", "FCF", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 123), 'E',
                ItemStackHelper.fromData(IUItem.advBattery), 'F', ItemStackHelper.fromData(IUItem.alloysplate, 1, 31),
                'B', getBlockStack(BlockBaseMachine3Entity.oak_tank), 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 187), "DAE", " B ", " C ", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 123), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 241),
                'B', IUItem.primal_pump, 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12)

        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 267), "ECE", "ABA", "ECE", 'B',
                DEFAULT_SENSOR, 'A', "forge:doubleplate/Yttrium", 'C', "forge:doubleplate/Strontium", 'E', "forge:plates/Niobium"

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
                "forge:plates/Steel", 'C', getBlockStack(BlockBaseMachine3Entity.steel_tank),
                'B', ItemStackHelper.fromData(IUItem.basemachine2, 1, 163)

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 178), "AAA", "ABA", "CCC", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 501), 'C', IUItem.cultivated_peat_balls,
                'B', ItemStackHelper.fromData(IUItem.basemachine2, 1, 171)

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 179), "DAE", "FBF", "HCH", 'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 32), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 123), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 219), 'F', "forge:plates/Titanium", 'H', "forge:plates/Bismuth",
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
                ItemStackHelper.fromData(IUItem.advQuantumtool), 'B', "forge:doubleplate/Nimonic", 'C',
                ItemStackHelper.fromData(IUItem.adv_proton_energy_coupler)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.per_proton_energy_coupler), "ABA", "BCB", "ABA", 'A',
                ItemStackHelper.fromData(IUItem.adv_spectral_box), 'B', "forge:doubleplate/SuperalloyHaynes", 'C',
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
                ItemStackHelper.fromData(IUItem.advQuantumtool), 'B', "forge:doubleplate/Nimonic", 'C',
                ItemStackHelper.fromData(IUItem.adv_neutron_protector)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.per_neutron_protector), "ABA", "BCB", "ABA", 'A',
                ItemStackHelper.fromData(IUItem.adv_spectral_box), 'B', "forge:doubleplate/SuperalloyRene", 'C',
                ItemStackHelper.fromData(IUItem.imp_neutron_protector)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.upgrade_casing), "ABA", "BCB", "ABA", 'A',
                ItemStackHelper.fromData(IUItem.graphene_plate), 'B', "forge:plates/Steel", 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 479)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 143), "CBC", "BAB", "CBC", 'A',
                ItemStackHelper.fromData(IUItem.module_schedule), 'B', "forge:plates/AluminiumLithium", 'C', "forge:plates/TantalumTungstenHafnium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 656),
                "EEE",
                "BAB",
                "CDC",
                'A',
                DEFAULT_SENSOR,
                'B',
                "forge:plates/Beryllium",
                'C',
                "forge:plates/TantalumTungstenHafnium",
                'D',
                "forge:gears/CobaltChrome",
                'E',
                "forge:casings/Bismuth"
        );

        BasicRecipeTwo.recipe();
    }

    public static ItemStack getBlockStack(MultiBlockEntity block) {
        return TileBlockCreator.instance.get(block.getIDBlock()).getItemStack();
    }
}
