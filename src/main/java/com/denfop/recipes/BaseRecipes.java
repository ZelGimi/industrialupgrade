package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.agriculture.CropNetwork;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.blocks.mechanism.BlockPrimalFluidHeater;
import com.denfop.register.RegisterOreDictionary;
import com.denfop.tiles.mechanism.TileEntityUpgradeMachineFactory;
import com.denfop.tiles.mechanism.TileGenerationMicrochip;
import com.denfop.utils.ModUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class BaseRecipes {

    public static ItemStack DEFAULT_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 21);

    public static ItemStack ADV_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 25);

    public static ItemStack IMP_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 23);

    public static ItemStack PER_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 24);

    public static ItemStack PHOTON_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 620);

    public static void init() {

        Recipes.recipe.addShapelessRecipe(
                IUItem.bronzeBlock,
                "ingotBronze",
                "ingotBronze",
                "ingotBronze",
                "ingotBronze",
                "ingotBronze",
                "ingotBronze",
                "ingotBronze",
                "ingotBronze",
                "ingotBronze"
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.copperBlock,
                "ingotCopper",
                "ingotCopper",
                "ingotCopper",
                "ingotCopper",
                "ingotCopper",
                "ingotCopper",
                "ingotCopper",
                "ingotCopper",
                "ingotCopper"
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.tinBlock,
                "ingotTin", "ingotTin", "ingotTin", "ingotTin", "ingotTin", "ingotTin", "ingotTin", "ingotTin", "ingotTin"
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.leadBlock,
                "ingotLead",
                "ingotLead",
                "ingotLead",
                "ingotLead",
                "ingotLead",
                "ingotLead",
                "ingotLead",
                "ingotLead",
                "ingotLead"
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.uraniumBlock,
                "ingotUranium",
                "ingotUranium",
                "ingotUranium",
                "ingotUranium",
                "ingotUranium",
                "ingotUranium",
                "ingotUranium",
                "ingotUranium",
                "ingotUranium"
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.advironblock,
                "ingotSteel",
                "ingotSteel",
                "ingotSteel",
                "ingotSteel",
                "ingotSteel",
                "ingotSteel",
                "ingotSteel",
                "ingotSteel",
                "ingotSteel"
        );
        Recipes.recipe.addShapelessRecipe(
                new ItemStack(IUItem.block1, 1, 3),
                "ingotOsmium",
                "ingotOsmium",
                "ingotOsmium",
                "ingotOsmium",
                "ingotOsmium",
                "ingotOsmium",
                "ingotOsmium",
                "ingotOsmium",
                "ingotOsmium"
        );
        Recipes.recipe.addShapelessRecipe(
                new ItemStack(IUItem.block1, 1, 4),
                "ingotTantalum",
                "ingotTantalum",
                "ingotTantalum",
                "ingotTantalum",
                "ingotTantalum",
                "ingotTantalum",
                "ingotTantalum",
                "ingotTantalum",
                "ingotTantalum"
        );
        Recipes.recipe.addShapelessRecipe(
                new ItemStack(IUItem.block1, 1, 5),
                "ingotCadmium",
                "ingotCadmium",
                "ingotCadmium",
                "ingotCadmium",
                "ingotCadmium",
                "ingotCadmium",
                "ingotCadmium",
                "ingotCadmium",
                "ingotCadmium"
        );
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.advIronIngot, 9), IUItem.advironblock);
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.copperIngot, 9), IUItem.copperBlock);
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.tinIngot, 9), IUItem.tinBlock);
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.leadIngot, 9), IUItem.leadBlock);
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.bronzeIngot, 9), IUItem.bronzeBlock);
        Recipes.recipe.addShapelessRecipe(new ItemStack(IUItem.itemiu, 9, 2), IUItem.uraniumBlock);


        Recipes.recipe.addRecipe(IUItem.efReader, " A ", "BCB", "B B",

                ('A'), Items.GLOWSTONE_DUST, ('B'),
                IUItem.insulatedCopperCableItem, ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
        );

        Recipes.recipe.addRecipe(IUItem.module_infinity_water, "BBB", "CAC", "DED",

                ('A'), IUItem.module_schedule, ('B'),
                ModUtils.getCellFromFluid(FluidRegistry.WATER), ('C'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5), 'D', IUItem.quantumtool, 'E',
                "doubleplateIridium"
        );
        Recipes.recipe.addRecipe(IUItem.module_separate, "BBB", "CAC", "DED",

                ('A'), IUItem.module_storage, ('B'),
                "plateCarbon", ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5), 'D', "gearElectrum", 'E',
                "doubleplateAluminumbronze"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.ruby_boots), "   ", "A A", "A A",
                ('A'), ("gemRuby")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.sapphire_boots), "   ", "A A", "A A",
                ('A'), ("gemSapphire")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.topaz_boots), "   ", "A A", "A A",
                ('A'), ("gemTopaz")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.ruby_chestplate), "A A", "AAA", "AAA",
                ('A'), ("gemRuby")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blockResource, 1, 11), "AAA", "AAA", "AAA",
                ('A'), IUItem.peat_balls
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.sapphire_chestplate), "A A", "AAA", "AAA",
                ('A'), ("gemSapphire")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.topaz_chestplate), "A A", "AAA", "AAA",
                ('A'), ("gemTopaz")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.ruby_leggings), "AAA", "A A", "A A",
                ('A'), ("gemRuby")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.sapphire_leggings), "AAA", "A A", "A A",
                ('A'), ("gemSapphire")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.topaz_leggings), "AAA", "A A", "A A",
                ('A'), ("gemTopaz")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.ruby_helmet), "AAA", "A A", "   ",
                ('A'), ("gemRuby")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.sapphire_helmet), "AAA", "A A", "   ",
                ('A'), ("gemSapphire")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.topaz_helmet), "AAA", "A A", "   ",
                ('A'), ("gemTopaz")
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricblock, 1, 2), "ABA", "CCC", "AAA",
                ('A'), ("plankWood"), ('C'), new ItemStack(IUItem.advBattery, 1, 32767),

                ('B'), IUItem.tinCableItem
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricblock, 1, 5), "ABA", "CCC", "ADA",

                ('A'),
                ("plateBronze"),
                ('C'), new ItemStack(IUItem.reBattery, 1, 32767),

                ('B'), IUItem.insulatedCopperCableItem, 'D', new ItemStack(IUItem.electricblock, 1, 2)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricblock, 1, 3), "ACA", "CBC", "ACA",

                ('A'),
                ("doubleplateAluminumbronze"),
                ('C'), new ItemStack(IUItem.energy_crystal, 1, 32767),

                ('B'), new ItemStack(IUItem.electricblock, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricblock, 1, 4), "CDC", "CAC", "CBC",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),

                ('A'), new ItemStack(IUItem.electricblock, 1, 3),

                ('C'), new ItemStack(IUItem.lapotron_crystal, 1, 32767),

                ('B'),
                IUItem.advancedMachine
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricblock, 1, 0), "CDC", "DAD", "CDC",

                ('D'), new ItemStack(IUItem.photoniy_ingot),

                ('A'), new ItemStack(IUItem.electricblock, 1, 4),

                ('C'), new ItemStack(IUItem.lapotron_crystal, 1, 32767),

                ('B'),
                IUItem.advancedMachine
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricblock, 1, 1), "CDC", "DAD", "CDC",

                ('D'), new ItemStack(IUItem.photoniy_ingot),

                ('A'), new ItemStack(IUItem.electricblock, 1, 0),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricblock, 1, 6), "CDC", "DAD", "CDC",

                ('D'), new ItemStack(IUItem.core, 1, 4),

                ('A'), new ItemStack(IUItem.electricblock, 1, 1),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricblock, 1, 7), "CDC", "DAD", "CDC",

                ('D'), new ItemStack(IUItem.core, 1, 8),

                ('A'), new ItemStack(IUItem.electricblock, 1, 6),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricblock, 1, 8), "CBC", "DAD", "CBC",

                ('D'), new ItemStack(IUItem.core, 1, 9),

                ('A'), new ItemStack(IUItem.electricblock, 1, 7),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'),
                new ItemStack(IUItem.AdvlapotronCrystal, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricblock, 1, 9), "EBE", "DAD", "CBC",

                ('D'), new ItemStack(IUItem.core, 1, 11),

                ('E'), new ItemStack(IUItem.compressIridiumplate),

                ('A'), new ItemStack(IUItem.electricblock, 1, 8),

                ('C'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), new ItemStack(IUItem.AdvlapotronCrystal, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricblock, 1, 10), "EBE", "DAD", "CBC",

                ('D'), new ItemStack(IUItem.core, 1, 12),

                ('E'), new ItemStack(IUItem.doublecompressIridiumplate),

                ('A'), new ItemStack(IUItem.electricblock, 1, 9),

                ('C'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), new ItemStack(IUItem.AdvlapotronCrystal, 1, 32767)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.voltage_sensor_for_mechanism), "AAA", "BCB", "   ",

                ('A'), new ItemStack(IUItem.nanoBox),

                ('B'), "plateTantalumTungstenHafnium",

                ('C'), new ItemStack(IUItem.efReader)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.polonium_palladium_composite), "AAA", "BBB", "CCC",

                ('A'), "platePolonium",

                ('B'), "platePalladium",

                ('C'), IUItem.advancedAlloy
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.medium_current_converter_to_low), "AAA", "BDB", "CEC",

                ('A'), IUItem.graphene_plate,

                ('B'), new ItemStack(IUItem.cable, 1, 14),

                ('C'), IUItem.polonium_palladium_composite,

                ('D'), IUItem.voltage_sensor_for_mechanism,

                ('E'), IUItem.motors_with_improved_bearings_
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.high_current_converter_to_low), "AAA", "BDB", "CEC",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 320),

                ('B'), new ItemStack(IUItem.cable, 1, 13),

                ('C'), "plateZeliber",

                ('D'), IUItem.medium_current_converter_to_low,

                ('E'), IUItem.adv_motors_with_improved_bearings_
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.extreme_current_converter_to_low), "AAA", "BDB", "CEC",

                ('A'), IUItem.synthetic_plate,

                ('B'), new ItemStack(IUItem.cable, 1, 0),

                ('C'), "plateStainlessSteel",

                ('D'), IUItem.high_current_converter_to_low,

                ('E'), IUItem.imp_motors_with_improved_bearings_
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.motors_with_improved_bearings_), " A ", "BDB", "CEC",

                ('A'), IUItem.compressed_redstone,

                ('B'), IUItem.electronic_stabilizers,

                ('C'), IUItem.graphene_wire,

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 276),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 588)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.electronic_stabilizers), "A A", "B B", "A A",

                ('A'), "plateIron",

                ('B'), "plateLapis"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_motors_with_improved_bearings_), " A ", "BDB", "CEC",

                ('A'), IUItem.compressed_redstone,

                ('B'), IUItem.electronic_stabilizers,

                ('C'), IUItem.graphene_wire,

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 20),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 598)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.imp_motors_with_improved_bearings_), " A ", "BDB", "CEC",

                ('A'), IUItem.compressed_redstone,

                ('B'), IUItem.electronic_stabilizers,

                ('C'), IUItem.graphene_wire,

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 96),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 592)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.advnanobox), " C ", "CAC", " C ",
                ('C'), new ItemStack(IUItem.photoniy, 1), ('A'), new ItemStack(IUItem.nanoBox)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.advQuantumtool), "CDC", "BAB", "CFC",

                ('F'), new ItemStack(IUItem.compressIridiumplate),

                ('B'),
                ("doubleplateIridium"),
                ('D'),
                ("casingIridium"),
                ('C'),
                new ItemStack(IUItem.photoniy_ingot, 1),

                ('A'), new ItemStack(IUItem.advnanobox)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nanoBox), "ACA", "BAB", "ACA",

                ('B'), IUItem.carbonPlate,

                ('C'), new ItemStack(IUItem.compresscarbon, 1),

                ('A'), new ItemStack(IUItem.iudust, 1, 24)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.quantumtool), "FDF", "BAB", "CDC",

                ('F'),
                ("doubleplateIridium"),
                ('B'), new ItemStack(IUItem.compressAlloy, 1),

                ('D'), new ItemStack(IUItem.compresscarbon, 1),

                ('C'),
                IUItem.iridiumOre,

                ('A'), new ItemStack(IUItem.nanoBox)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.magnet), "A B", "CDC", " C ",

                ('B'),
                ("ingotFerromanganese"),
                ('D'), new ItemStack(IUItem.advBattery, 1, 32767),

                ('C'),
                ("doubleplateMikhail"),
                ('A'),
                ("ingotVitalium")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.impmagnet), "B B", "CDC", " C ",

                ('B'),
                ("ingotDuralumin"),
                ('D'), new ItemStack(IUItem.magnet, 1, 32767),

                ('C'),
                ("doubleplateInvar")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.AdvlapotronCrystal), "BCB", "CDC", "BCB",

                ('B'), new ItemStack(IUItem.lapotron_crystal, 1, 32767),

                ('D'), new ItemStack(IUItem.photoniy_ingot),

                ('C'), new ItemStack(IUItem.advQuantumtool, 1)
        );
        Recipes.recipe.addRecipe(IUItem.tranformerUpgrade, " A ", "DBD", " C ",

                ('B'), IUItem.high_current_converter_to_low,

                ('A'), new ItemStack(IUItem.tranformer, 1, 9),

                ('D'), IUItem.insulatedGoldCableItem,

                ('C'),
                IUItem.upgrade_casing
        );
        Recipes.recipe.addRecipe(IUItem.tranformerUpgrade1, " A ", "DBD", " C ",

                ('B'), IUItem.extreme_current_converter_to_low,

                ('A'), new ItemStack(IUItem.tranformer, 1, 0),

                ('D'), IUItem.insulatedGoldCableItem,

                ('C'),
                IUItem.upgrade_casing
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.nanopickaxe), "ADB", "EFE", "CHC",
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 524), ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 523), 'C',
                ("doubleplateFerromanganese"), ('D'), new ItemStack(Items.DIAMOND_PICKAXE), ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                ('A'), new ItemStack(IUItem.crafting_elements, 1, 520), 'H', "stickMolybdenum"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nanoaxe), "ADB", "EFE", "CHC",
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 524), ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 522), 'C',
                ("doubleplateFerromanganese"), ('D'), new ItemStack(Items.DIAMOND_AXE), ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                ('A'), new ItemStack(IUItem.crafting_elements, 1, 519), 'H', "stickMolybdenum"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nanoshovel), "ADB", "EFE", "CHC",
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 524), ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 521), 'C',
                ("doubleplateFerromanganese"), ('D'), new ItemStack(Items.DIAMOND_SHOVEL), ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                ('A'), new ItemStack(IUItem.crafting_elements, 1, 518), 'H', "stickMolybdenum"
        );
        //
        Recipes.recipe.addRecipe(new ItemStack(IUItem.quantumpickaxe), "BDA", "EFE", "CHC",
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 516), ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 512), 'C',
                ("doubleplateMuntsa"), ('D'), new ItemStack(IUItem.nanopickaxe), ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                ('A'), new ItemStack(IUItem.crafting_elements, 1, 515), 'H', "stickElectrum"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.quantumaxe), "ADB", "EFE", "CHC",
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 516), ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 514), 'C',
                ("doubleplateMuntsa"), ('D'), new ItemStack(IUItem.nanoaxe), ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                ('A'), new ItemStack(IUItem.crafting_elements, 1, 511), 'H', "stickElectrum"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.quantumshovel), "ADB", "EFE", "CHC",
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 516), ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 513), 'C',
                ("doubleplateMuntsa"), ('D'), new ItemStack(IUItem.nanoshovel), ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                ('A'), new ItemStack(IUItem.crafting_elements, 1, 510), 'H', "stickElectrum"
        );
        //
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spectralpickaxe), "ADB", "EFE", "CHC",
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 532), ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 531), 'C',
                IUItem.iridiumPlate, ('D'), new ItemStack(IUItem.quantumpickaxe), ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                ('A'), new ItemStack(IUItem.crafting_elements, 1, 528), 'H', "stickIridium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spectralaxe), "ADB", "EFE", "CHC",
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 532), ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 530), 'C',
                IUItem.iridiumPlate, ('D'), new ItemStack(IUItem.quantumaxe), ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                ('A'), new ItemStack(IUItem.crafting_elements, 1, 527), 'H', "stickIridium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spectralshovel), "ADB", "EFE", "CHC",
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 532), ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 529), 'C',
                IUItem.iridiumPlate, ('D'), new ItemStack(IUItem.quantumshovel), ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                ('A'), new ItemStack(IUItem.crafting_elements, 1, 526), 'H', "stickIridium"
        );
        //

        Recipes.recipe.addRecipe(new ItemStack(IUItem.impBatChargeCrystal), "BCB", "BAB", "BCB",

                ('B'), new ItemStack(IUItem.AdvlapotronCrystal, 1, 32767),

                ('A'), new ItemStack(IUItem.charging_lapotron_crystal, 1, 32767),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.perBatChargeCrystal), "DCD", "BAB", "ECE",

                ('E'),
                ("doubleplateVitalium"),
                ('D'), IUItem.iridiumPlate,

                ('B'), new ItemStack(IUItem.AdvlapotronCrystal, 1, 32767),

                ('A'),
                new ItemStack(IUItem.impBatChargeCrystal, 1, 32767),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.perfect_drill), "ACB", "FDF", "ECE",

                ('E'), new ItemStack(IUItem.adv_spectral_box),

                ('F'), IUItem.overclockerUpgrade1,

                ('A'), new ItemStack(IUItem.spectralaxe, 1, 32767),

                ('B'),
                new ItemStack(IUItem.spectralshovel, 1, 32767),

                ('D'), new ItemStack(IUItem.spectralpickaxe, 1, 32767),

                ('C'), TileGenerationMicrochip.getLevelCircuit(new ItemStack(IUItem.basecircuit, 1, 21), 11)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.ult_vajra), "CAC", "DBD", "", 'A', new ItemStack(IUItem.vajra), 'B',
                new ItemStack(IUItem.perfect_drill), 'C', "plateNimonic", 'D', "plateSuperalloyRene"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.vajra), "ACB", "FDF", "ECE",

                ('E'), new ItemStack(IUItem.advQuantumtool),

                ('F'), IUItem.overclockerUpgrade1,

                ('A'), new ItemStack(IUItem.spectralaxe, 1, 32767),

                ('B'),
                new ItemStack(IUItem.spectralshovel, 1, 32767),

                ('D'), new ItemStack(IUItem.spectralpickaxe, 1, 32767),

                ('C'), TileGenerationMicrochip.getLevelCircuit(new ItemStack(IUItem.basecircuit, 1, 21), 12)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.quantumSaber), "AB ", "AC ", "DEB",
                ('C'), new ItemStack(IUItem.nanosaber, 1, 32767), ('E'), new ItemStack(IUItem.lapotron_crystal, 1, 32767),
                ('D'), new ItemStack(Blocks.GLOWSTONE),
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), ('A'), new ItemStack(IUItem.compresscarbon)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spectralSaber), "AB ", "AC ", "DEB",

                ('C'), new ItemStack(IUItem.quantumSaber, 1, 32767),

                ('E'), new ItemStack(IUItem.AdvlapotronCrystal, 1, 32767),

                ('D'), new ItemStack(Blocks.GLOWSTONE),

                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('A'), new ItemStack(IUItem.compressIridiumplate)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.GraviTool), "ABA", "CDE", "FGF",

                ('G'), new ItemStack(IUItem.purifier, 1, 32767),

                ('F'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),

                ('D'), new ItemStack(IUItem.energy_crystal, 1, 32767),

                ('E'),
                new ItemStack(IUItem.electric_treetap, 1, 32767),

                ('C'), new ItemStack(IUItem.electric_wrench, 1, 32767),

                ('B'), new ItemStack(IUItem.electricHoe
                        .getItem(), 1, 32767),
                ('A'),
                ("doubleplateMuntsa")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.purifier), "   ", " B ", "A  ",
                ('A'), new ItemStack(IUItem.powerunitsmall.getItem(), 1, 11), ('B'), "wool"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.itemiu, 1, 3), "MDM", "M M", "MDM",
                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4), ('M'), new ItemStack(
                        IUItem.itemiu,
                        1,
                        1
                )
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.nano_bow),
                "C C",
                "BAB",
                "EDE",
                ('E'),
                IUItem.advnanobox,
                ('D'),
                new ItemStack(IUItem.reBattery, 1, 32767),
                ('C'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('B'),
                IUItem.carbonPlate,
                ('A'),
                new ItemStack(Items.BOW)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.quantum_bow), "ABA", "CDC", "EBE",

                ('E'),
                ("doubleplateAlcled"),
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('D'), new ItemStack(IUItem.nano_bow, 1, 32767),

                ('A'),
                IUItem.iridiumPlate,

                ('B'), new ItemStack(IUItem.advQuantumtool)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spectral_bow), "ABA", "CDC", "EBE",

                ('E'),
                ("doubleplateDuralumin"),
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('D'), new ItemStack(IUItem.quantum_bow, 1, 32767),

                ('A'),
                new ItemStack(IUItem.compressIridiumplate),

                ('B'), new ItemStack(IUItem.adv_spectral_box)
        );
        int[] meta = {3, 4, 0, 1, 6, 7, 8, 9, 10};
        int j;
        for (j = 0; j < 9; j++) {
            Recipes.recipe.addShapelessRecipe(
                    new ItemStack(IUItem.UpgradeKit, 1, j),
                    IUItem.wrench, new ItemStack(IUItem.electricblock, 1, meta[j])
            );
        }
        Recipes.recipe.addRecipe(new ItemStack(IUItem.solidmatter, 1, 0), "ABA", "CDC", "EBE",

                ('E'), new ItemStack(IUItem.core, 1, 5),

                ('D'), new ItemStack(IUItem.machines, 1, 3),

                ('C'),
                ("plateCobalt"),
                ('B'),
                ("casingElectrum"),
                ('A'),
                ("casingIridium")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.solidmatter, 1, 1), "ABA", "CDC", "EBE",

                ('E'), new ItemStack(IUItem.core, 1, 5),

                ('D'), new ItemStack(IUItem.machines, 1, 3),

                ('C'),
                ("platePlatinum"),
                ('B'),
                ("casingMagnesium"),
                ('A'),
                ("casingCobalt")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.solidmatter, 1, 2), "ABA", "CDC", "EBE",

                ('E'), new ItemStack(IUItem.core, 1, 5),

                ('D'), new ItemStack(IUItem.machines, 1, 3),

                ('C'),
                ("doubleplateAlcled"),
                ('B'),
                ("casingChromium"),
                ('A'),
                ("casingMikhail")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.solidmatter, 1, 3), "ABA", "CDC", "EBE",

                ('E'), new ItemStack(IUItem.core, 1, 5),

                ('D'), new ItemStack(IUItem.machines, 1, 3),

                ('C'),
                ("doubleplateDuralumin"),
                ('B'),
                ("casingCaravky"),
                ('A'),
                ("casingVanadium")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.solidmatter, 1, 4), "ABA", "CDC", "EBE",

                ('E'), new ItemStack(IUItem.core, 1, 5),

                ('D'), new ItemStack(IUItem.machines, 1, 3),

                ('C'),
                ("doubleplateManganese"),
                ('B'),
                ("casingSpinel"),
                ('A'),
                ("casingAluminium")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.solidmatter, 1, 5), "ABA", "CDC", "EBE",

                ('E'), new ItemStack(IUItem.core, 1, 5),

                ('D'), new ItemStack(IUItem.machines, 1, 3),

                ('C'),
                ("doubleplateTitanium"),
                ('B'),
                ("casingInvar"),
                ('A'),
                ("casingTungsten")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.solidmatter, 1, 6), "ABA", "CDC", "EBE",

                ('E'), new ItemStack(IUItem.core, 1, 5),

                ('D'), new ItemStack(IUItem.machines, 1, 3),

                ('C'),
                ("doubleplateRedbrass"),
                ('B'),
                ("casingChromium"),
                ('A'),
                ("casingManganese")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.solidmatter, 1, 7), "ABA", "CDC", "EBE",

                ('E'), new ItemStack(IUItem.core, 1, 5),

                ('D'), new ItemStack(IUItem.machines, 1, 3),

                ('C'),
                ("doubleplateAlumel"),
                ('B'),
                ("casingCobalt"),
                ('A'),
                ("casingVanadium")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module_schedule), "ABA", "EDE", "CBC",

                ('D'),
                ("ingotCaravky"),
                ('E'),
                ("plateZinc"),
                ('C'), new ItemStack(IUItem.plastic_plate),

                ('B'),
                new ItemStack(IUItem.plastic_plate),

                ('A'),
                ("plateVanadium")
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricJetpack), "ADA", "ACA", "B B", ('A'), IUItem.casingiron,
                ('B'), Items.GLOWSTONE_DUST, ('C'), new ItemStack(IUItem.electricblock, 1, 2), ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module7, 1, 1), "ABA", "BCB", "DDD",

                ('D'), new ItemStack(IUItem.doublecompressIridiumplate),

                ('C'), new ItemStack(IUItem.advQuantumtool),

                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('A'),
                new ItemStack(IUItem.core, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module7, 1, 2), "ABA", "BCB", "DDD",

                ('D'), new ItemStack(IUItem.doublecompressIridiumplate),

                ('C'), new ItemStack(IUItem.advnanobox),

                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('A'),
                new ItemStack(IUItem.core, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module7, 1, 3), "AAA", "BCB", "EFE",

                ('F'),
                ("doubleplateAlcled"),
                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('C'), new ItemStack(IUItem.module_schedule),

                ('B'),
                ("doubleplateVitalium"),
                ('A'), new ItemStack(IUItem.compresscarbon)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module7, 1, 4), "AAA", "BCB", "EFE",

                ('F'),
                ("doubleplateDuralumin"),
                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('C'), new ItemStack(IUItem.module_schedule),

                ('B'),
                ("doubleplateVanadoalumite"),
                ('A'), new ItemStack(IUItem.compressAlloy)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module7, 1, 5), "ABA",
                ('B'), IUItem.module8, ('A'), new ItemStack(IUItem.module7, 1, 3)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module7, 1, 6), "AB",
                ('B'), IUItem.module8, ('A'), new ItemStack(IUItem.module7, 1, 3)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module7, 1, 7), "ABA",
                ('B'), IUItem.module8, ('A'), new ItemStack(IUItem.module7, 1, 8)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module7, 1, 8), "ABA",
                ('B'), new ItemStack(IUItem.module7, 1, 4), ('A'), new ItemStack(IUItem.module7, 1, 6)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9), "BCA", "DED", "BCA",

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('D'),
                ("doubleplateSilver"),
                ('C'),
                ("doubleplateElectrum"),
                ('B'),
                ("doubleplateRedbrass"),
                ('A'),
                ("doubleplateAlcled")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 1), "ABA", "DED", "ABA",

                ('E'), IUItem.overclockerUpgrade1,

                ('D'), new ItemStack(IUItem.core, 1, 1),

                ('B'),
                ("doubleplateVanadoalumite"),
                ('A'),
                ("doubleplateAlcled")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 2), "ABA", "CEC", "ABA",

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('C'), new ItemStack(IUItem.module9, 1, 1),

                ('B'), new ItemStack(IUItem.nanoBox, 1),

                ('A'),
                new ItemStack(IUItem.core, 1, 2)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 3), "ABA", "CEC", "ABA",

                ('E'), new ItemStack(IUItem.quantumtool, 1),

                ('C'), new ItemStack(IUItem.module9, 1, 2),

                ('B'), new ItemStack(IUItem.photoniy_ingot, 1),

                ('A'),
                new ItemStack(IUItem.core, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 4), "ABA", "CEC", "ABA",

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('C'), new ItemStack(IUItem.module9, 1, 3),

                ('B'), new ItemStack(IUItem.advQuantumtool, 1),

                ('A'),
                new ItemStack(IUItem.core, 1, 6)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 5), "ABA", "BCB", "ABA",

                ('C'), new ItemStack(IUItem.doublecompressIridiumplate),

                ('B'), new ItemStack(IUItem.module9, 1, 4),

                ('A'), new ItemStack(IUItem.core, 1, 7)
        );
        Recipes.recipe.addRecipe(IUItem.module8, "AAA", "BCB", "DED",

                ('E'), new ItemStack(IUItem.compressIridiumplate),

                ('D'),
                ("doubleplatePlatinum"),
                ('C'), new ItemStack(IUItem.block, 1, 2),

                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),

                ('A'),
                ("plateZinc")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module7, 1, 9),
                "DBD",
                "BCB",
                "ABA",

                ('C'),
                new ItemStack(IUItem.purifier, 1, 32767),

                ('B'),
                new ItemStack(IUItem.nanoBox),

                ('A'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),
                'D',
                new ItemStack(IUItem.crafting_elements, 1,
                        543
                )
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 9), "ABA", "BCB", "ABA",

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), new ItemStack(IUItem.photoniy, 1),

                ('A'), new ItemStack(IUItem.core, 1, 3)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 10), "ABA", "BCB", "ABA",

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), new ItemStack(IUItem.photoniy_ingot, 1),

                ('A'), new ItemStack(IUItem.module9, 1, 9)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 11), "ABA", "BCB", "ABA",

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), new ItemStack(IUItem.photoniy_ingot, 1),

                ('A'), new ItemStack(IUItem.module9, 1, 10)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 12), "ABA", "BCB", "ABA",
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9), ('B'), new ItemStack(
                        Blocks.REDSTONE_BLOCK,
                        1
                ), ('A'), new ItemStack(Items.PAPER, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 6), "ABA", "BCB", "ABA",

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('B'), new ItemStack(IUItem.photoniy, 1),

                ('A'), new ItemStack(IUItem.core, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 7), "ABA", "DCD", "ABA",

                ('D'), new ItemStack(IUItem.module9, 1, 6),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), new ItemStack(IUItem.photoniy, 1),

                ('A'),
                new ItemStack(IUItem.core, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 8), "ABA", "DCD", "ABA",

                ('D'), new ItemStack(IUItem.module9, 1, 7),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), new ItemStack(IUItem.doublecompressIridiumplate, 1),

                ('A'),
                new ItemStack(IUItem.core, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 13), "A A", " C ", "A A",
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9), ('A'), new ItemStack(Items.PAPER, 1)
        );
        Recipes.recipe.addRecipe(IUItem.module1, "AAA", "BCB", "EDE",

                ('E'), new ItemStack(IUItem.plastic_plate),

                ('D'),
                ("doubleplateVitalium"),
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'),
                ("plateCobalt"),
                ('A'),
                ("plateElectrum")
        );
        Recipes.recipe.addShapelessRecipe(new ItemStack(IUItem.fertilizer, 16),
                new ItemStack(IUItem.iudust, 1, 69), new ItemStack(IUItem.iudust, 1, 70), new ItemStack(IUItem.iudust, 1, 66)
        );
        Recipes.recipe.addRecipe(IUItem.module2, "AAA", "BCB", "EDE",

                ('E'), new ItemStack(IUItem.plastic_plate),

                ('D'),
                ("doubleplateVitalium"),
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'),
                ("doubleplateRedbrass"),
                ('A'),
                ("doubleplateFerromanganese")
        );
        Recipes.recipe.addRecipe(IUItem.module3, "AAA", "BCB", "EDE",

                ('E'), new ItemStack(IUItem.plastic_plate),

                ('D'),
                ("doubleplateVitalium"),
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'),
                ("doubleplateAlumel"),
                ('A'),
                ("plateFerromanganese")
        );
        Recipes.recipe.addRecipe(IUItem.module4, "AAA", "BCB", "EDE",

                ('E'), new ItemStack(IUItem.plastic_plate),

                ('D'),
                ("doubleplateVitalium"),
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'),
                ("doubleplateMuntsa"),
                ('A'), new ItemStack(IUItem.doublecompressIridiumplate, 1)
        );
        for (j = 0; j < 7; j++) {
            Recipes.recipe.addRecipe(new ItemStack(IUItem.module5, 1, j), "BBB", "ACA", "ADA",

                    ('A'), new ItemStack(IUItem.lens, 1, j),

                    ('B'), new ItemStack(IUItem.doublecompressIridiumplate),

                    ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                    ('D'),
                    new ItemStack(IUItem.advQuantumtool)
            );
        }
        for (j = 0; j < 14; j++) {
            ItemStack stack1 = new ItemStack(IUItem.module6, 1, j);
            Recipes.recipe.addShapelessRecipe(stack1, new ItemStack(IUItem.blockpanel, 1, j));
        }

        for (int i = 0; i < RegisterOreDictionary.list_string.size(); i++) {
            if (i < 16) {
                Recipes.recipe.addShapelessRecipe(
                        new ItemStack(IUItem.iuingot, 9, i),
                        new ItemStack(IUItem.block, 1, i)
                );
            } else {
                Recipes.recipe.addShapelessRecipe(
                        new ItemStack(IUItem.iuingot, 9, i),
                        new ItemStack(IUItem.block1, 1, i - 16)
                );

            }
            Recipes.recipe.addShapelessRecipe(
                    new ItemStack(IUItem.nugget, 9, i),
                    new ItemStack(IUItem.iuingot, 1, i)
            );
            if (i < 16) {
                Recipes.recipe.addRecipe(new ItemStack(IUItem.block, 1, i),
                        "AAA", "AAA", "AAA", 'A', "ingot" + RegisterOreDictionary.list_string.get(i)
                );
            } else {
                Recipes.recipe.addRecipe(new ItemStack(IUItem.block1, 1, i - 16),
                        "AAA", "AAA", "AAA", 'A', "ingot" + RegisterOreDictionary.list_string.get(i)
                );

            }
            Recipes.recipe.addRecipe(new ItemStack(IUItem.iuingot, 1, i),
                    "AAA", "AAA", "AAA", 'A', new ItemStack(IUItem.nugget, 1, i)
            );


        }

        Recipes.recipe.addShapelessRecipe(
                new ItemStack(IUItem.iuingot, 9, 25),
                new ItemStack(IUItem.block1, 1, 3)
        );
        Recipes.recipe.addShapelessRecipe(
                new ItemStack(IUItem.nugget, 9, 19),
                new ItemStack(IUItem.iuingot, 1, 25)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.iuingot, 1, 25),
                "AAA", "AAA", "AAA", 'A', new ItemStack(IUItem.nugget, 1, 19)
        );


        Recipes.recipe.addShapelessRecipe(
                new ItemStack(IUItem.iuingot, 9, 26),
                new ItemStack(IUItem.block1, 1, 4)
        );
        Recipes.recipe.addShapelessRecipe(
                new ItemStack(IUItem.nugget, 9, 20),
                new ItemStack(IUItem.iuingot, 1, 26)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.iuingot, 1, 26),
                "AAA", "AAA", "AAA", 'A', new ItemStack(IUItem.nugget, 1, 20)
        );


        Recipes.recipe.addShapelessRecipe(
                new ItemStack(IUItem.iuingot, 9, 27),
                new ItemStack(IUItem.block1, 1, 5)
        );
        Recipes.recipe.addShapelessRecipe(
                new ItemStack(IUItem.nugget, 9, 21),
                new ItemStack(IUItem.iuingot, 1, 27)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.iuingot, 1, 27),
                "AAA", "AAA", "AAA", 'A', new ItemStack(IUItem.nugget, 1, 21)
        );

        for (j = 0; j < RegisterOreDictionary.list_string1.size(); j++) {
            if (j >= 16) {
                Recipes.recipe.addShapelessRecipe(
                        new ItemStack(IUItem.alloysingot, 9, j),
                        new ItemStack(IUItem.alloysblock1, 1, j - 16)
                );
                Recipes.recipe.addRecipe(new ItemStack(IUItem.alloysblock1, 1, j - 16), "AAA", "AAA", "AAA",
                        ('A'), ("ingot" + RegisterOreDictionary.list_string1.get(j))
                );
            } else {
                Recipes.recipe.addShapelessRecipe(
                        new ItemStack(IUItem.alloysingot, 9, j),
                        new ItemStack(IUItem.alloysblock, 1, j)
                );
                Recipes.recipe.addRecipe(new ItemStack(IUItem.alloysblock, 1, j), "AAA", "AAA", "AAA",
                        ('A'), ("ingot" + RegisterOreDictionary.list_string1.get(j))
                );
            }
            Recipes.recipe.addShapelessRecipe(
                    new ItemStack(IUItem.alloysnugget, 9, j),
                    new ItemStack(IUItem.alloysingot, 1, j)
            );

            Recipes.recipe.addRecipe(new ItemStack(IUItem.alloysingot, 1, j), "AAA", "AAA", "AAA",
                    ('A'), new ItemStack(IUItem.alloysnugget, 1, j)
            );
        }

        for (j = 0; j < RegisterOreDictionary.list_baseore1.size(); j++) {

            Recipes.recipe.addShapelessRecipe(
                    new ItemStack(IUItem.iuingot, 9, j + 28),
                    new ItemStack(IUItem.block2, 1, j)
            );
            Recipes.recipe.addRecipe(new ItemStack(IUItem.block2, 1, j), "AAA", "AAA", "AAA",
                    ('A'), ("ingot" + RegisterOreDictionary.list_baseore1.get(j))
            );

            Recipes.recipe.addShapelessRecipe(
                    new ItemStack(IUItem.nugget, 9, j + 22),
                    new ItemStack(IUItem.iuingot, 1, j + 28)
            );

            Recipes.recipe.addRecipe(new ItemStack(IUItem.iuingot, 1, j + 28), "AAA", "AAA", "AAA",
                    ('A'), new ItemStack(IUItem.nugget, 1, j + 22)
            );


        }
        Recipes.recipe.addRecipe(new ItemStack(IUItem.canister), "BBA", " AA", " AA",
                ('A'), "plateChromium", 'B', "plateTitanium"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.cable, 1, 0), " A ", "BBB", " C ",
                ('A'), IUItem.glassFiberCableItem, ('B'), new ItemStack(IUItem.itemiu, 1, 0), 'C',
                new ItemStack(IUItem.synthetic_rubber)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cable, 1, 1), " A ", "BCB", " A ",

                ('C'),
                ("ingotCobalt"),
                ('A'), new ItemStack(IUItem.cable, 1, 0),

                ('B'), IUItem.denseplatetin
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cable, 1, 2), " A ", "BCB", " A ",

                ('C'), IUItem.denseplatetin,

                ('A'), new ItemStack(IUItem.cable, 1, 1),

                ('B'), IUItem.advancedAlloy
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cable, 1, 3), "DAD", "BCB", "DAD",

                ('D'), IUItem.denseplategold,

                ('C'), IUItem.advancedAlloy,

                ('A'), new ItemStack(IUItem.cable, 1, 2),

                ('B'),
                IUItem.denseplatelead
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cable, 1, 4), "DAD", "BCB", "DAD",

                ('D'),
                ("ingotRedbrass"),
                ('C'), IUItem.carbonPlate,

                ('A'), new ItemStack(IUItem.cable, 1, 3),

                ('B'),
                ("ingotSpinel")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cable, 1, 5), " A ", "BCB", " A ",

                ('C'),
                ("doubleplateVitalium"),
                ('A'), new ItemStack(IUItem.cable, 1, 4),

                ('B'), IUItem.denseplateadviron
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cable, 1, 6), "DAD", "BCB", "DAD",

                ('D'), IUItem.carbonPlate,

                ('C'),
                ("ingotAlcled"),
                ('A'), new ItemStack(IUItem.cable, 1, 5),

                ('B'),
                ("ingotDuralumin")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cable, 1, 7), "A A", "BCB", "A A",

                ('C'), new ItemStack(IUItem.photoniy_ingot),

                ('B'), new ItemStack(IUItem.photoniy),

                ('A'), new ItemStack(IUItem.cable, 1, 6)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cable, 1, 8), "BBB", "AAA", "BBB",
                ('A'), new ItemStack(IUItem.photoniy_ingot), ('B'), new ItemStack(IUItem.cable, 1, 7)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cable, 1, 9), "BBB", "ACA", "BBB",

                ('C'), new ItemStack(IUItem.basecircuit, 1, 10),

                ('A'), new ItemStack(IUItem.photoniy_ingot),

                ('B'), new ItemStack(IUItem.cable, 1, 8)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basecircuit, 1, 15), "BAB", "ADA", "CAC",

                ('D'), new ItemStack(Items.FLINT),

                ('C'), new ItemStack(Items.REDSTONE),

                ('A'),
                ("plateIron"),
                ('B'),
                new ItemStack(Items.GOLD_INGOT)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basecircuit, 1, 16), "BBB", "BAB", "BBB",
                ('B'), new ItemStack(IUItem.stik, 1, 15), ('A'), new ItemStack(IUItem.basecircuit, 1, 15)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basecircuit, 1, 17), "CCC", "A A", "DDD",
                ('D'), ("plateSilver"), ('C'), IUItem.insulatedCopperCableItem,

                ('A'), new ItemStack(IUItem.basecircuit, 1, 15)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.cable, 1, 10), "BBB", "ACA", "BBB",
                ('C'), ("doubleplateVanadoalumite"), ('A'), new ItemStack(IUItem.basecircuit, 1, 11),
                ('B'), new ItemStack(IUItem.cable, 1, 9)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 27), " D ", "ABA", " D ",
                ('D'), ("plateGermanium"), ('B'), new ItemStack(Blocks.CHEST),
                ('A'), new ItemStack(IUItem.quantumtool)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_lappack), "ABA", "CEC", "ADA",

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('D'), new ItemStack(IUItem.lapotron_crystal, 1, 32767),

                ('C'),
                ("plateVanadoalumite"),
                ('B'),
                new ItemStack(IUItem.lappack, 1, 32767),

                ('A'),
                ("plateAlcled")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.imp_lappack), "ABA", "CEC", "ABA",

                ('E'), new ItemStack(IUItem.adv_lappack, 1, 32767),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),

                ('B'), new ItemStack(IUItem.AdvlapotronCrystal, 1, 32767),

                ('A'),
                ("doubleplateFerromanganese")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.per_lappack), "ABA", "CEC", "ABA",

                ('E'), new ItemStack(IUItem.imp_lappack, 1, 32767),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'), new ItemStack(IUItem.compressIridiumplate, 1),

                ('A'),
                ("doubleplateVitalium")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.advancedSolarHelmet), " A ", "BCB", "DED",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('E'), new ItemStack(IUItem.compressAlloy),

                ('C'), new ItemStack(IUItem.adv_nano_helmet, 1, 32767),

                ('B'),
                new ItemStack(IUItem.compresscarbon),

                ('A'), new ItemStack(IUItem.blockpanel)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.hybridSolarHelmet), " A ", "BCB", "DED",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('E'), new ItemStack(IUItem.compressAlloy),

                ('C'), new ItemStack(IUItem.quantum_helmet, 1, 32767),

                ('B'),
                IUItem.iridiumPlate,

                ('A'), new ItemStack(IUItem.blockpanel, 1, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.ultimateSolarHelmet), " A ", "DCD", "BEB",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('E'), new ItemStack(IUItem.compressAlloy),

                ('C'), new ItemStack(IUItem.hybridSolarHelmet, 1, 32767),

                ('B'),
                IUItem.iridiumPlate,

                ('A'), new ItemStack(IUItem.blockpanel, 1, 2)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewind, 1, 0), " A ", "ABA", " A ",

                ('A'), "logWood",

                ('B'), "plankWood"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewind, 1, 1), " A ", "ABA", " A ",

                ('A'), "blockBronze",

                ('B'), new ItemStack(IUItem.rotor_wood, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewind, 1, 2), " A ", "ABA", " A ",

                ('A'), "blockIron",

                ('B'), new ItemStack(IUItem.rotor_bronze, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewind, 1, 3), " A ", "ABA", " A ",

                ('A'), "plateSteel",

                ('B'), new ItemStack(IUItem.rotor_iron, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewind, 1, 4), " A ", "ABA", " A ",

                ('A'), "plateCarbon",

                ('B'), new ItemStack(IUItem.rotor_steel, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewind, 1, 5), "CAC", "ABA", "CAC",

                ('C'), "doubleplateIridium",

                ('A'), IUItem.iridiumPlate,

                ('B'), new ItemStack(IUItem.rotor_carbon, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewind, 1, 6), "CAC", "ABA", "CAC",

                ('C'), IUItem.compressIridiumplate,

                ('A'), new ItemStack(IUItem.compresscarbon),

                ('B'), new ItemStack(IUItem.iridium
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewind, 1, 7), "DCD", "ABA", "DCD",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('C'), new ItemStack(IUItem.compressIridiumplate),

                ('A'), new ItemStack(IUItem.advnanobox),

                ('B'),
                new ItemStack(IUItem.compressiridium
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewind, 1, 8), "DCD", "ABA", " C ",

                ('D'), new ItemStack(IUItem.excitednucleus, 1, 5),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('A'), new ItemStack(IUItem.quantumtool),

                ('B'),
                new ItemStack(IUItem.spectral
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewind, 1, 10), "DCD", "CBC", " C ",

                ('D'), new ItemStack(IUItem.excitednucleus, 1, 6),

                ('C'), new ItemStack(IUItem.quantumtool),

                ('B'), new ItemStack(IUItem.myphical
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewind, 1, 9), "DCD", "CBC", "ACA",

                ('D'), new ItemStack(IUItem.neutroniumingot),

                ('A'), IUItem.iridiumPlate,

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('B'),
                new ItemStack(IUItem.photon
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewind, 1, 11), "DCD", "CBC", " C ",

                ('D'), new ItemStack(IUItem.excitednucleus, 1, 5),

                ('C'), new ItemStack(IUItem.advQuantumtool),

                ('B'), new ItemStack(IUItem.neutron
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewind, 1, 12), "ACA", "CBC", "ACA",

                ('A'), new ItemStack(IUItem.doublecompressIridiumplate),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), new ItemStack(IUItem.barionrotor
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewind, 1, 13), "ECE", "CBC", "ACA",

                ('E'), TileGenerationMicrochip.getLevelCircuit(new ItemStack(IUItem.basecircuit, 1, 21), 11),

                ('A'), new ItemStack(IUItem.doublecompressIridiumplate),

                ('C'), new ItemStack(IUItem.photoniy_ingot),

                ('B'),
                new ItemStack(IUItem.adronrotor
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spectralSolarHelmet), " A ", "DCD", "BEB",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('E'), new ItemStack(IUItem.compressAlloy),

                ('C'), new ItemStack(IUItem.ultimateSolarHelmet, 1, 32767),

                ('B'),
                new ItemStack(IUItem.doublecompressIridiumplate, 1),

                ('A'), new ItemStack(IUItem.blockpanel, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.singularSolarHelmet), " A ", "DCD", "BDB",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('E'), new ItemStack(IUItem.compressAlloy),

                ('C'), new ItemStack(IUItem.spectralSolarHelmet, 1, 32767),

                ('B'),
                new ItemStack(IUItem.doublecompressIridiumplate, 1),

                ('A'), new ItemStack(IUItem.blockpanel, 1, 6)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines, 1, 5), "BCB", "DAD", "BCB",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('C'), new ItemStack(IUItem.doublecompressIridiumplate),

                ('B'), new ItemStack(IUItem.photoniy_ingot),

                ('A'),
                new ItemStack(IUItem.machines, 1, 3)
        );
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines, 1, 1),
                "DED",
                "BCB",
                "AAA",

                ('E'),
                new ItemStack(IUItem.core, 1, 5),

                ('D'),
                ("doubleplateAlumel"),
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('C'),
                new ItemStack(IUItem.simplemachine, 1, 6),

                ('A'),
                new ItemStack(IUItem.quantumtool)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines, 1, 2),
                "DED",
                "BCB",
                "AAA",

                ('E'),
                new ItemStack(IUItem.core, 1, 7),

                ('D'),
                ("doubleplateVitalium"),
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('C'),
                new ItemStack(IUItem.machines, 1, 1),

                ('A'),
                new ItemStack(IUItem.advQuantumtool)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines, 1, 3),
                "DED",
                "BCB",
                "AFA",

                ('F'),
                new ItemStack(IUItem.doublecompressIridiumplate),

                ('E'),
                new ItemStack(IUItem.core, 1, 8),

                ('D'),
                ("doubleplateDuralumin"),
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('C'),
                new ItemStack(IUItem.machines, 1, 2),

                ('A'),
                new ItemStack(IUItem.advQuantumtool)
        ), 2);
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.pho_machine, 1, 12),
                "DED",
                "BCB",
                "AFA",

                ('F'),
                new ItemStack(IUItem.doublecompressIridiumplate),

                ('E'),
                new ItemStack(IUItem.core, 1, 10),

                ('D'),
                ("doubleplateDuralumin"),
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(new ItemStack(IUItem.basecircuit, 1, 21), 11),

                ('C'),
                new ItemStack(IUItem.machines, 1, 3),

                ('A'),
                new ItemStack(IUItem.adv_spectral_box)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blocksintezator), "ABA", "BCB", "ABA",

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('A'), IUItem.advancedMachine,

                ('B'), new ItemStack(IUItem.core, 1, 8)
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
                TileGenerationMicrochip.getLevelCircuit(new ItemStack(IUItem.basecircuit, 1, 21), 11)};
        int k;
        for (k = 0; k < 11; k++) {
            Recipes.recipe.addRecipe(
                    new ItemStack(IUItem.chargepadelectricblock, 1, meta3[k]),
                    "ABA",
                    "CDC",
                    ('B'),
                    Blocks.STONE_PRESSURE_PLATE,
                    ('A'),
                    stacks3[k],
                    ('D'),
                    new ItemStack(IUItem.electricblock, 1, meta2[k]),
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
                TileGenerationMicrochip.getLevelCircuit(new ItemStack(IUItem.basecircuit, 1, 21), 11)};
        for (k = 0; k < 7; k++) {
            if (k < 3) {
                if (k == 0) {
                    Recipes.recipe.addShapelessRecipe(
                            new ItemStack(IUItem.tranformer, 1, k),
                            new ItemStack(IUItem.tranformer, 1, 10), new ItemStack(
                                    IUItem.crafting_elements,
                                    1,
                                    210
                            )
                    );
                    Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 210), " A ", "BCD", " A ",

                            ('D'), new ItemStack(IUItem.lapotron_crystal, 1, 32767),

                            ('C'), DEFAULT_SENSOR,

                            ('B'), stacks3[k],

                            ('A'),
                            new ItemStack(IUItem.cable, 1, k)
                    );
                } else {
                    Recipes.recipe.addShapelessRecipe(
                            new ItemStack(IUItem.tranformer, 1, k),
                            new ItemStack(
                                    IUItem.tranformer,
                                    1,
                                    k - 1
                            ), new ItemStack(IUItem.crafting_elements, 1, 210 + k)
                    );
                    Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 210 + k), " A ", "BCD", " A ",

                            ('D'), new ItemStack(IUItem.AdvlapotronCrystal, 1, 32767),

                            ('C'), DEFAULT_SENSOR,

                            ('B'), stacks3[k],

                            ('A'),
                            new ItemStack(IUItem.cable, 1, k)
                    );
                }
            } else {
                Recipes.recipe.addShapelessRecipe(
                        new ItemStack(IUItem.tranformer, 1, k),
                        new ItemStack(IUItem.tranformer, 1, k - 1), new ItemStack(
                                IUItem.crafting_elements,
                                1,
                                210 + k
                        )
                );
                Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 210 + k), " A ", "BCD", " A ",

                        ('D'), new ItemStack(IUItem.AdvlapotronCrystal, 1, 32767),

                        ('C'), DEFAULT_SENSOR,

                        ('B'), stacks3[k],

                        ('A'),
                        new ItemStack(IUItem.cable, 1, k)
                );
            }
        }
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines, 1, 9), "ACA", "BEB", "ADA",

                ('E'), IUItem.advancedMachine,

                ('D'), new ItemStack(IUItem.module9, 1, 13),

                ('C'), new ItemStack(IUItem.module9, 1, 12),

                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('A'), new ItemStack(IUItem.core, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.photonglass, 1, 0), "CCC", "ABA", "CCC",
                ('B'), new ItemStack(IUItem.itemiu, 1, 1), ('C'), new ItemStack(
                        IUItem.stik,
                        1,
                        0
                ), ('A'), new ItemStack(IUItem.sunnarium, 1, 2)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.photonglass, 1, 1), "CCC", "ABA", "CCC",

                ('B'), new ItemStack(IUItem.photonglass, 1, 0),

                ('C'), new ItemStack(IUItem.stik, 1, 6),

                ('A'), new ItemStack(IUItem.sunnariumpanel, 1, 0)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.photonglass, 1, 2), "CCC", "ABA", "CCC",

                ('B'), new ItemStack(IUItem.photonglass, 1, 1),

                ('C'), new ItemStack(IUItem.stik, 1, 9),

                ('A'), new ItemStack(IUItem.sunnariumpanel, 1, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.photonglass, 1, 3), "CCC", "ABA", "CCC",

                ('B'), new ItemStack(IUItem.photonglass, 1, 2),

                ('C'), new ItemStack(IUItem.stik, 1, 11),

                ('A'), new ItemStack(IUItem.sunnariumpanel, 1, 2)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.photonglass, 1, 4), "CCC", "ABA", "CCC",

                ('B'), new ItemStack(IUItem.photonglass, 1, 3),

                ('C'), new ItemStack(IUItem.stik, 1, 13),

                ('A'), new ItemStack(IUItem.sunnariumpanel, 1, 3)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.photonglass, 1, 5), "CEC", "ABA", "CEC",

                ('E'), new ItemStack(IUItem.proton),

                ('B'), new ItemStack(IUItem.photonglass, 1, 4),

                ('C'), new ItemStack(IUItem.stik, 1, 16),

                ('A'),
                new ItemStack(IUItem.sunnariumpanel, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.photonglass, 1, 6), "CCC", "ABA", "CCC",

                ('B'), new ItemStack(IUItem.photonglass, 1, 5),

                ('C'), new ItemStack(IUItem.stik, 1, 4),

                ('A'), new ItemStack(IUItem.sunnariumpanel, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.photonglass, 1, 7), "CCC", "ABA", "CCC",

                ('B'), new ItemStack(IUItem.photonglass, 1, 6),

                ('C'), new ItemStack(IUItem.stik, 1, 12),

                ('A'), new ItemStack(IUItem.sunnariumpanel, 1, 6)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.photonglass, 1, 8), "CCC", "ABA", "CCC",

                ('B'), new ItemStack(IUItem.photonglass, 1, 7),

                ('C'), new ItemStack(IUItem.stik, 1, 11),

                ('A'), new ItemStack(IUItem.sunnariumpanel, 1, 7)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.photonglass, 1, 9), "CDC", "ABA", "CDC",

                ('D'), new ItemStack(IUItem.neutroniumingot, 1),

                ('B'), new ItemStack(IUItem.photonglass, 1, 8),

                ('C'), new ItemStack(IUItem.stik, 1, 18),

                ('A'),
                new ItemStack(IUItem.sunnariumpanel, 1, 8)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.photonglass, 1, 10), "CCC", "ABA", "CCC",

                ('B'), new ItemStack(IUItem.photonglass, 1, 9),

                ('C'), new ItemStack(IUItem.stik, 1, 15),

                ('A'), new ItemStack(IUItem.sunnariumpanel, 1, 9)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.photonglass, 1, 11), "CCC", "ABA", "CCC",

                ('B'), new ItemStack(IUItem.photonglass, 1, 10),

                ('C'), new ItemStack(IUItem.stik, 1, 2),

                ('A'), new ItemStack(IUItem.sunnariumpanel, 1, 10)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.photonglass, 1, 12), "CCC", "ABA", "CCC",

                ('B'), new ItemStack(IUItem.photonglass, 1, 11),

                ('C'), new ItemStack(IUItem.stik, 1, 6),

                ('A'), new ItemStack(IUItem.sunnariumpanel, 1, 11)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.photonglass, 1, 13), "CCC", "ABA", "CCC",

                ('B'), new ItemStack(IUItem.photonglass, 1, 12),

                ('C'), new ItemStack(IUItem.stik, 1, 5),

                ('A'), new ItemStack(IUItem.sunnariumpanel, 1, 12)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basecircuit, 1, 6), "CCC", "ABA", "DDD",

                ('D'),
                ("plateBronze"),
                ('C'), IUItem.carbonPlate,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 543),

                ('A'),
                new ItemStack(IUItem.basecircuit, 1, 0)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basecircuit, 1, 7), "CCC", "ABA", "DDD",
                ('D'), ("plateSteel"), ('C'), IUItem.carbonPlate, ('B'), new ItemStack(IUItem.crafting_elements, 1, 549),

                ('A'),
                new ItemStack(IUItem.basecircuit, 1, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basecircuit, 1, 8), "CCC", "ABA", "DDD",

                ('D'),
                ("plateSpinel"),
                ('C'), IUItem.carbonPlate,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 555),

                ('A'),
                new ItemStack(IUItem.basecircuit, 1, 2)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basecircuit, 1, 20), "CCC", "ABA", "DDD",

                ('D'),
                ("plateSuperalloyRene"),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 479),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 558),

                ('A'),
                new ItemStack(IUItem.basecircuit, 1, 18)
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.basecircuit, 1, 3), "BBB", "BAB", "BBB",
                ('B'), new ItemStack(IUItem.stik, 1, 10), ('A'), new ItemStack(IUItem.basecircuit, 1, 0)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basecircuit, 1, 4), "BBB", "BAB", "BBB",
                ('B'), new ItemStack(IUItem.stik, 1, 14), ('A'), new ItemStack(IUItem.basecircuit, 1, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basecircuit, 1, 5), "BBB", "BAB", "BBB",
                ('B'), new ItemStack(IUItem.stik, 1, 16), ('A'), new ItemStack(IUItem.basecircuit, 1, 2)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basecircuit, 1, 13), "BBB", "BAB", "BBB",
                ('B'), new ItemStack(IUItem.stik, 1, 6), ('A'), new ItemStack(IUItem.basecircuit, 1, 12)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basecircuit, 1, 14), "CCC", "ABA", "DDD",

                ('D'),
                ("platePlatinum"),
                ('C'), IUItem.carbonPlate,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 533),

                ('A'),
                new ItemStack(IUItem.basecircuit, 1, 12)
        );
        ItemStack[] circuit = {
                new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        30
                ),
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                new ItemStack(IUItem.excitednucleus, 1, 1),
                new ItemStack(IUItem.excitednucleus, 1, 2),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),
                new ItemStack(IUItem.excitednucleus, 1, 4),
                new ItemStack(IUItem.excitednucleus, 1, 5),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),
                new ItemStack(IUItem.excitednucleus, 1, 7),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                new ItemStack(IUItem.excitednucleus, 1, 9),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),
                new ItemStack(IUItem.excitednucleus, 1, 11),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                new ItemStack(IUItem.excitednucleus, 1, 12),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8)};
        ItemStack[] iridium = {
                new ItemStack(IUItem.sunnarium, 1, 1), IUItem.iridiumOre, IUItem.iridiumPlate, IUItem.iridiumPlate, new ItemStack(
                IUItem.compressIridiumplate), new ItemStack(IUItem.compressIridiumplate), new ItemStack(IUItem.compressIridiumplate), new ItemStack(
                IUItem.doublecompressIridiumplate), new ItemStack(IUItem.doublecompressIridiumplate), new ItemStack(IUItem.doublecompressIridiumplate),
                new ItemStack(IUItem.doublecompressIridiumplate), new ItemStack(IUItem.doublecompressIridiumplate), new ItemStack(
                IUItem.doublecompressIridiumplate), new ItemStack(IUItem.doublecompressIridiumplate)};
        int m;
        for (m = 0; m < 14; m++) {
            if (m != 0) {
                Recipes.recipe.addRecipe(new ItemStack(IUItem.blockpanel, 1, m), "ABA", "CDC", "DED",

                        ('A'), new ItemStack(IUItem.solar_night_day_glass, 1, m),

                        ('B'), new ItemStack(IUItem.excitednucleus, 1, m),

                        ('C'), iridium[m],

                        ('D'),
                        new ItemStack(IUItem.blockpanel, 1, m - 1),

                        ('E'), circuit[m]
                );
                Recipes.recipe.addRecipe(new ItemStack(IUItem.upgradepanelkit, 1, m), "ABA", "C C", "DED",

                        ('A'), new ItemStack(IUItem.solar_night_day_glass, 1, m),

                        ('B'), new ItemStack(IUItem.excitednucleus, 1, m),

                        ('C'), iridium[m],

                        ('D'),
                        new ItemStack(IUItem.blockpanel, 1, m - 1),

                        ('E'), circuit[m]
                );
            } else {
                Recipes.recipe.addRecipe(new ItemStack(IUItem.blockpanel, 1, m), "ABA", "CDC", "DED",

                        ('A'), new ItemStack(IUItem.solar_night_day_glass, 1, m),

                        ('B'), new ItemStack(IUItem.excitednucleus, 1, m),

                        ('C'), iridium[m],

                        ('D'),
                        getBlockStack(BlockBaseMachine3.solar_iu),
                        ('E'), circuit[m]
                );
                Recipes.recipe.addRecipe(new ItemStack(IUItem.upgradepanelkit, 1, m), "ABA", "C C", "DED",

                        ('A'), new ItemStack(IUItem.solar_night_day_glass, 1, m),

                        ('B'), new ItemStack(IUItem.excitednucleus, 1, m),

                        ('C'), iridium[m],

                        ('D'),
                        getBlockStack(BlockBaseMachine3.solar_iu),
                        ('E'), circuit[m]
                );
            }
        }
        for (m = 0; m < 3; m++) {
            Recipes.recipe.addRecipe(new ItemStack(IUItem.preciousblock, 1, m), "AAA", "AAA", "AAA",
                    ('A'), new ItemStack(IUItem.preciousgem, 1, m)
            );
            Recipes.recipe.addShapelessRecipe(
                    new ItemStack(IUItem.preciousgem, 9, m),
                    new ItemStack(IUItem.preciousblock, 1, m)
            );
        }
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module7), " C ", "BAB", "DCD",

                ('D'),
                ("plateCaravky"),
                ('C'),
                ("plateElectrum"),
                ('B'),
                ("plateInvar"),
                ('A'),
                new ItemStack(IUItem.module_schedule)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.paints), "AAA", "A A", "AAA",
                ('A'), new ItemStack(Blocks.GLASS)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.paints, 1, 1),
                "AB ", ('A'), new ItemStack(IUItem.paints), ('B'), new ItemStack(Items.DYE, 1, 12)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.paints, 1, 2), "AB ",

                ('A'), new ItemStack(IUItem.paints),

                ('B'), new ItemStack(Items.DYE, 1, 11)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.paints, 1, 3), "AB ",

                ('A'), new ItemStack(IUItem.paints),

                ('B'), new ItemStack(Items.DYE, 1, 2)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.paints, 1, 4), "AB ",

                ('A'), new ItemStack(IUItem.paints),

                ('B'), new ItemStack(Items.DYE, 1, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.paints, 1, 5), "AB ",

                ('A'), new ItemStack(IUItem.paints),

                ('B'), new ItemStack(Items.DYE, 1, 14)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.paints, 1, 6), "AB ",

                ('A'), new ItemStack(IUItem.paints),

                ('B'), new ItemStack(Items.DYE, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.paints, 1, 7), "AB ",

                ('A'), new ItemStack(IUItem.paints),

                ('B'), new ItemStack(Items.DYE, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.paints, 1, 8), "AB ",

                ('A'), new ItemStack(IUItem.paints),

                ('B'), new ItemStack(Items.DYE, 1, 6)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.paints, 1, 9), "AB ",

                ('A'), new ItemStack(IUItem.paints),

                ('B'), new ItemStack(Items.DYE, 1, 15)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.paints, 1, 10), "AB ",

                ('A'), new ItemStack(IUItem.paints),

                ('B'), new ItemStack(Items.DYE)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.paints, 1, 11), "AB ",

                ('A'), new ItemStack(IUItem.paints),

                ('B'), new ItemStack(Items.DYE, 1, 10)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.quarrymodule), "BAB", "DCD",

                ('D'),
                ("ingotGermanium"),
                ('C'), new ItemStack(IUItem.module_schedule),

                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 158)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.analyzermodule), "BAB", "DCD",

                ('D'),
                ("ingotGermanium"),
                ('C'), new ItemStack(IUItem.module_schedule),

                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('A'), new ItemStack(IUItem.basemachine1, 1, 2)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machinekit, 1, 0), "ABA", "D D", "EEE",

                ('E'), new ItemStack(IUItem.nanoBox),

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('B'),
                ("doubleplateAluminium"),
                ('A'),
                ("doubleplateAlumel")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machinekit, 1, 1), "ABA", "D D", "EEE",

                ('E'), new ItemStack(IUItem.quantumtool),

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('B'),
                ("doubleplatePlatinum"),
                ('A'),
                ("doubleplateVitalium")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machinekit, 1, 2), "ABA", "D D", "EEE",

                ('E'), new ItemStack(IUItem.advQuantumtool),

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('B'),
                ("doubleplateSpinel"),
                ('A'),
                ("doubleplateManganese")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.hazmathelmet), "AAA", "BCB", "DDD",

                ('A'),
                ("plateMikhail"),
                ('B'),
                ("platePlatinum"),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 0),
                ('C'),
                new ItemStack(IUItem.hazmat_helmet, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.hazmatchest), "AAA", "BCB", "DDD",

                ('A'),
                ("plateMikhail"),
                ('B'),
                ("platePlatinum"),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 0),
                ('C'),
                new ItemStack(IUItem.hazmat_chestplate, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.hazmatleggins), "AAA", "BCB", "DDD",

                ('A'),
                ("plateMikhail"),
                ('B'),
                ("platePlatinum"),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 0),
                ('C'),
                new ItemStack(IUItem.hazmat_leggings, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.hazmatboosts), "AAA", "BCB", "DDD",

                ('A'),
                ("plateMikhail"),
                ('B'),
                ("platePlatinum"),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 0),
                ('C'),
                new ItemStack(IUItem.rubber_boots, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.entitymodules), "ABA", "DCD", "EBE",

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),

                ('B'), new ItemStack(IUItem.alloyscasing, 1, 2),

                ('C'), new ItemStack(IUItem.module_schedule),

                ('D'),
                ("ingotInvar"),
                ('E'), new ItemStack(IUItem.alloyscasing, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.entitymodules, 1, 1), "ABA", "DCD", "EBE",

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('B'), new ItemStack(IUItem.adv_spectral_box),

                ('C'), new ItemStack(IUItem.module_schedule),

                ('D'),
                ("doubleplateGermanium"),
                ('E'), new ItemStack(IUItem.alloyscasing, 1, 9)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spawnermodules, 1, 0), "ABA",

                ('A'), new ItemStack(IUItem.module9, 1, 6),

                ('B'),
                ("doubleplateNichrome")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spawnermodules, 1, 1), "ABA", "DCD", "EEE",

                ('E'), new ItemStack(IUItem.nanoBox),

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('B'),
                ("doubleplateAluminium"),
                ('A'),
                ("doubleplateAlumel"),
                ('C'), new ItemStack(IUItem.spawnermodules, 1, 0)
        );
        Recipes.recipe.addRecipe(new ItemStack(Blocks.TNT, 16), "BCB", "CAC", "BCB",

                ('A'),
                new ItemStack(IUItem.iudust, 1, 72),
                ('C'), new ItemStack(Blocks.SAND, 1, 0),
                ('B'), new ItemStack(Items.GUNPOWDER, 1, 0)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spawnermodules, 1, 2), "ABA", "DCD", "EEE",

                ('E'), new ItemStack(IUItem.quantumtool),

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('B'),
                ("doubleplatePlatinum"),
                ('A'),
                ("doubleplateVitalium"),
                ('C'), new ItemStack(IUItem.spawnermodules, 1, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spawnermodules, 1, 3), "ABA",

                ('B'), new ItemStack(IUItem.module9, 1, 3),

                ('A'),
                ("doubleplateNichrome")
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.spawnermodules, 1, 4),
                " C ", "ABA", " C ", ('B'), new ItemStack(IUItem.module_schedule, 1),
                ('A'), ("doubleplateNichrome"), ('C'), new ItemStack(Items.EXPERIENCE_BOTTLE)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spawnermodules, 1, 5), "ABA", "DCD", "EEE",

                ('E'), new ItemStack(IUItem.nanoBox),

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),

                ('B'),
                ("doubleplateAluminium"),
                ('A'),
                ("doubleplateAlumel"),
                ('C'), new ItemStack(IUItem.spawnermodules, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.oilquarry), "C G", "ABA", " D ",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 53),

                ('G'), new ItemStack(IUItem.crafting_elements, 1, 251),

                ('D'), IUItem.elemotor,

                ('A'),
                IUItem.advancedAlloy,

                ('B'), IUItem.machine
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 192), " G ", " B ", " D ",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 53),

                ('G'), new ItemStack(IUItem.crafting_elements, 1, 263),

                ('D'), IUItem.elemotor,

                ('A'),
                IUItem.advancedAlloy,

                ('B'), IUItem.machine
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cathode), "B B", "BAB", "B B",

                ('A'), new ItemStack(IUItem.fluidCell, 1, 0),

                ('B'),
                ("plateAlumel")
        );
        for (int i = 0; i < 30; i++) {
            Recipes.recipe.addShapelessRecipe(new ItemStack(IUItem.spaceItem, 1, i),
                    new ItemStack(IUItem.spaceItem, 1, i + 30), new ItemStack(IUItem.spaceItem, 1, i + 30),
                    new ItemStack(IUItem.spaceItem, 1, i + 30), new ItemStack(IUItem.spaceItem, 1, i + 30)
            );
            if (i < 16) {
                Recipes.recipe.addShapelessRecipe(new ItemStack(IUItem.space_cobblestone, 1, i),
                        new ItemStack(IUItem.spaceItem, 1, i), new ItemStack(IUItem.spaceItem, 1, i),
                        new ItemStack(IUItem.spaceItem, 1, i), new ItemStack(IUItem.spaceItem, 1, i)
                );
            } else {
                Recipes.recipe.addShapelessRecipe(new ItemStack(IUItem.space_cobblestone1, 1, i - 16),
                        new ItemStack(IUItem.spaceItem, 1, i), new ItemStack(IUItem.spaceItem, 1, i),
                        new ItemStack(IUItem.spaceItem, 1, i), new ItemStack(IUItem.spaceItem, 1, i)
                );
            }
        }
        Recipes.recipe.addRecipe(new ItemStack(IUItem.molot), "BBB", "BAB", " A ",

                ('A'), Items.STICK,

                ('B'),
                ("plateFerromanganese")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.steelHammer), "BBB", "BAB", " A ",

                ('A'), Items.STICK,

                ('B'),
                ("plateSteel")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.ironHammer), "CBC", "BAB", " A ",

                ('A'), Items.STICK,

                ('B'),
                ("plateIron"), 'C', "plateDenseIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.anode), "B B", "BAB", "B B",

                ('A'), new ItemStack(IUItem.fluidCell, 1, 0),

                ('B'),
                ("plateMuntsa")
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.expmodule),
                "   ", "ABA", "   ", ('A'), new ItemStack(IUItem.module_schedule),
                ('B'), new ItemStack(Items.EXPERIENCE_BOTTLE)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module_stack), "DCD", "BAB", " C ",

                ('D'),
                ("plateAlumel"),
                ('A'), new ItemStack(IUItem.module_schedule),

                ('B'), IUItem.overclockerUpgrade,

                ('C'),
                IUItem.tranformerUpgrade
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module_quickly), "   ", "ABA", "   ",

                ('A'), IUItem.overclockerUpgrade1,

                ('B'), new ItemStack(IUItem.module_schedule)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nanodrill), "EHE", "ABC", " D ",

                ('E'), new ItemStack(IUItem.nanoBox),

                ('D'), new ItemStack(IUItem.diamond_drill),

                ('B'), new ItemStack(IUItem.advnanobox), 'H', new ItemStack(IUItem.crafting_elements, 1, 517),

                ('A'),
                new ItemStack(IUItem.nanopickaxe, 1, 32767),

                ('C'), new ItemStack(IUItem.nanoshovel, 1, 32767)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.quantumdrill),
                "EHE",
                "ABC",
                " D ",

                ('E'),
                new ItemStack(IUItem.quantumtool),

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                'H',
                new ItemStack(IUItem.crafting_elements, 1, 509),

                ('B'),
                new ItemStack(IUItem.quantumtool),

                ('A'),
                new ItemStack(IUItem.quantumpickaxe, 1, 32767),

                ('C'),
                new ItemStack(IUItem.quantumshovel, 1, 32767)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.spectraldrill),
                "EHE",
                "ABC",
                " D ",

                ('E'),
                new ItemStack(IUItem.spectral_box),

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                'H',
                new ItemStack(IUItem.crafting_elements, 1, 525),

                ('B'),
                new ItemStack(IUItem.advQuantumtool),

                ('A'),
                new ItemStack(IUItem.spectralpickaxe, 1, 32767),

                ('C'),
                new ItemStack(IUItem.spectralshovel, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.quantumdrill), "THT", "CDC", "BFB",

                ('T'),
                ("doubleplateMuntsa"),
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 516),

                ('B'), new ItemStack(IUItem.advQuantumtool, 1),
                'H', new ItemStack(IUItem.crafting_elements, 1, 509),
                ('D'),
                new ItemStack(IUItem.nanodrill, 1, 32767),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spectraldrill), "THT", "CDC", "BFB",

                ('T'), IUItem.iridiumPlate,

                ('F'), new ItemStack(IUItem.crafting_elements, 1, 532),

                ('B'), new ItemStack(IUItem.adv_spectral_box, 1),
                'H', new ItemStack(IUItem.crafting_elements, 1, 525),
                ('D'),
                new ItemStack(IUItem.quantumdrill, 1, 32767),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.bags), "BCB", "BAB", "B B",

                ('C'), new ItemStack(IUItem.suBattery, 1, 32767),

                ('B'), new ItemStack(Items.LEATHER),

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_bags, 1), "BCB", "BAB", "B B",

                ('C'), new ItemStack(IUItem.advBattery, 1, 32767),

                ('B'), IUItem.carbonPlate,

                ('A'), new ItemStack(IUItem.bags, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.imp_bags, 1), "BCB", "BAB", "B B",

                ('C'), new ItemStack(IUItem.reBattery, 1, 32767),

                ('B'), new ItemStack(IUItem.compresscarbon),

                ('A'), new ItemStack(IUItem.adv_bags, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.advjetpack), "BCB", "CDC", "BFB",

                ('F'), new ItemStack(IUItem.suBattery, 1, 32767),

                ('B'),
                ("doubleplateFerromanganese"),
                ('D'), new ItemStack(IUItem.electricJetpack, 1, 32767),

                ('C'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.impjetpack), "TCT", "CDC", "BFB",

                ('T'),
                ("doubleplateMuntsa"),
                ('F'), new ItemStack(IUItem.reBattery, 1, 32767),

                ('B'), new ItemStack(IUItem.quantumtool, 1),

                ('D'),
                new ItemStack(IUItem.advjetpack, 1, 32767),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.perjetpack), "TCT", "CDC", "BFB",

                ('T'), IUItem.iridiumPlate,

                ('F'), new ItemStack(IUItem.advBattery, 1, 32767),

                ('B'), new ItemStack(IUItem.advQuantumtool, 1),

                ('D'),
                new ItemStack(IUItem.impjetpack, 1, 32767),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 2), "   ", "ABC", "   ",

                ('A'), new ItemStack(IUItem.module7),

                ('B'), IUItem.machine,

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 3), "   ", "ABC", "   ",

                ('A'), IUItem.module8,

                ('B'), IUItem.machine,

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
        );

        Recipes.recipe.addRecipe(IUItem.phase_module, "DDD", "BAC",

                ('D'), new ItemStack(IUItem.alloysdoubleplate, 1, 7),

                ('C'), IUItem.module2,

                ('B'), IUItem.module1,

                ('A'), new ItemStack(IUItem.module_schedule)
        );
        Recipes.recipe.addRecipe(IUItem.moonlinse_module, "DDD", "BAB", "CEC",

                ('D'), new ItemStack(IUItem.lens, 1, 4),

                ('E'), new ItemStack(IUItem.alloysdoubleplate, 1, 6),

                ('C'), new ItemStack(IUItem.alloysdoubleplate, 1, 4),

                ('B'),
                new ItemStack(IUItem.alloysdoubleplate, 1, 3),

                ('A'), new ItemStack(IUItem.module_schedule)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.tank), "BAB", "CAC", "BAB",

                ('C'), new ItemStack(IUItem.alloysplate, 1, 4),

                ('A'), IUItem.fluidCell,

                ('B'), new ItemStack(IUItem.denseplateiron
                        .getItem(), 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.tank, 1, 1), "C C", "BAB", "C C",

                ('C'), new ItemStack(IUItem.alloysplate, 1, 2),

                ('A'), new ItemStack(IUItem.tank),

                ('B'), new ItemStack(IUItem.alloysplate, 1, 6)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.tank, 1, 2), "CBC", "CAC", "CBC",

                ('B'), IUItem.photoniy,

                ('C'), new ItemStack(IUItem.nanoBox),

                ('A'), new ItemStack(IUItem.tank, 1, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.tank, 1, 3), "CBC", "CAC", "CBC",

                ('B'), IUItem.photoniy_ingot,

                ('A'), new ItemStack(IUItem.tank, 1, 2),

                ('C'), new ItemStack(IUItem.advnanobox)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module_storage, 1), "AAA", "CBC", "DDD",

                ('D'), ("plateManganese"), ('C'), ("plateNickel"), ('A'),
                ("plateInvar"),
                ('B'),
                new ItemStack(IUItem.module_schedule)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machinekit, 1, 3), " A ", "BDC", "   ",

                ('A'), new ItemStack(IUItem.machinekit, 1, 0),

                ('B'), new ItemStack(IUItem.machinekit, 1, 1),

                ('C'), new ItemStack(IUItem.machinekit, 1, 2),

                ('D'),
                ("plateManganese")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 7), "DCD", "ABA", "DCD",

                ('A'), new ItemStack(IUItem.impmagnet, 1, 32767),

                ('B'), new ItemStack(IUItem.basemachine, 1, 14),

                ('C'),
                ("plateElectrum"),
                ('D'),
                ("plateNickel")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.leadbox), "AAA", "ABA", "   ",

                ('A'),
                ("ingotLead"),
                ('B'), new ItemStack(Blocks.CHEST)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.katana, 1, 0), "AD ", "AB ", "CC ",

                ('A'), new ItemStack(IUItem.compressAlloy),

                ('C'), new ItemStack(Blocks.GLOWSTONE),

                ('B'), new ItemStack(IUItem.lapotron_crystal, 1, 32767),

                ('D'),
                new ItemStack(IUItem.nanosaber, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.coolupgrade, 1, 0), "ACA", "DBD", "ACA",

                ('A'),
                ModUtils.getCellFromFluid(FluidName.fluidazot.getInstance()),
                ('B'), new ItemStack(IUItem.module_schedule), 'C', "plateAdamantium", 'D', "plateStellite"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.coolupgrade, 1, 1), "ACA", "DBD", "ACA",

                ('A'),
                ModUtils.getCellFromFluid(FluidName.fluidhyd.getInstance()),
                ('B'), new ItemStack(IUItem.module_schedule), 'C', "plateAdamantium", 'D', "plateStellite"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.coolupgrade, 1, 2), "ACA", "DBD", "ACA",

                ('A'),
                ModUtils.getCellFromFluid(FluidName.fluidHelium.getInstance()),
                ('B'), new ItemStack(IUItem.module_schedule), 'C', "plateAdamantium", 'D', "plateStellite"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.autoheater), "CAC", "DBD", "C C",

                ('A'), new ItemStack(IUItem.basemachine2, 1, 5),

                ('B'), new ItemStack(IUItem.module_schedule), 'C', "plateInconel", 'D', "plateMithril"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.scable), " C ", "ABA", " C ",

                ('B'), IUItem.copperCableItem,

                ('A'), new ItemStack(IUItem.sunnarium, 1, 3),

                ('C'), IUItem.rubber
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.qcable, 2), " C ", "ABA", " C ",

                ('B'), IUItem.glassFiberCableItem,

                ('A'), new ItemStack(IUItem.proton),

                ('C'), IUItem.iridiumOre
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.simplemachine, 1, 6), "ABA", "CDC", "ABA",

                ('A'), Items.GLOWSTONE_DUST,

                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),

                ('C'), IUItem.advancedMachine,

                ('D'),
                new ItemStack(IUItem.lapotron_crystal, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 14), " D ", "ABA", " C ",

                ('A'), new ItemStack(IUItem.core, 1, 5),

                ('B'), new ItemStack(IUItem.module_schedule),

                ('D'), new ItemStack(IUItem.machines_base, 1, 2),

                ('C'),
                ("doubleplateMuntsa")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 15), " D ", "ABA", " C ",

                ('A'), new ItemStack(IUItem.core, 1, 5),

                ('B'), new ItemStack(IUItem.module_schedule),

                ('D'), new ItemStack(IUItem.machines_base1, 1, 9),

                ('C'),
                ("doubleplateVanadoalumite")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.upgrade_speed_creation), "   ", "ABA", "   ",

                ('A'), IUItem.overclockerUpgrade_1,

                ('B'), new ItemStack(IUItem.module_schedule)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blastfurnace, 1, 5), " A ", "ABA", " A ",

                ('B'), Blocks.BRICK_BLOCK,

                ('A'),
                ("ingotMagnesium")
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.blastfurnace, 1, 2), " A ", "ABA", " A ",

                ('B'), getBlockStack(BlockPrimalFluidHeater.primal_fluid_heater),
                ('A'), new ItemStack(IUItem.blastfurnace, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blastfurnace, 1, 1), "CAC", "ABA", "CAC",

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 44),
                ('A'), new ItemStack(IUItem.blastfurnace, 1, 5),
                'C', "gearInvar"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blastfurnace, 1, 4), " A ", "ABA", " A ",

                ('B'),
                getBlockStack(BlockBaseMachine3.steel_tank),
                ('A'), new ItemStack(IUItem.blastfurnace, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blastfurnace, 1, 3), "CAC", "ABA", "CAC",

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 44),
                ('A'), new ItemStack(IUItem.blastfurnace, 1, 5),
                'C', "gearElectrum"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blastfurnace, 1, 0), " A ", "ABA", " A ",

                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),

                ('A'), new ItemStack(IUItem.blastfurnace, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spectral_box), "D D", "ABA", "C C",

                ('D'), new ItemStack(IUItem.coal_chunk1),

                ('C'), IUItem.iridiumPlate,

                ('A'),
                ("doubleplateSpinel"),
                ('B'),
                new ItemStack(IUItem.quantumtool)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_spectral_box), "DAD", "ABA", "CEC",

                ('E'), new ItemStack(IUItem.coal_chunk1),

                ('D'), new ItemStack(IUItem.photoniy_ingot),

                ('C'), new ItemStack(IUItem.doublecompressIridiumplate),

                ('A'),
                ("doubleplateSpinel"),
                ('B'), new ItemStack(IUItem.advQuantumtool)
        );
        Recipes.recipe.addRecipe(IUItem.transformerUpgrade, " A ", "DBD", " C ",

                ('B'), IUItem.medium_current_converter_to_low,

                ('A'), new ItemStack(IUItem.tranformer, 1, 8),

                ('D'), IUItem.insulatedGoldCableItem,

                ('C'),
                IUItem.upgrade_casing
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.windmeter, 1, 0), " A ", "ABA", " AC",

                ('A'), IUItem.casingtin, ('B'), IUItem.casingbronze, ('C'), IUItem.powerunitsmall
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotorupgrade_schemes), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.plastic_plate),
                ('D'), "doubleplateVanadoalumite", ('B'), new ItemStack(IUItem.photoniy_ingot), ('C'),
                "plateOrichalcum"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 0), "A A", "CBC", "A A",

                ('A'), new ItemStack(IUItem.advnanobox),
                ('B'), new ItemStack(IUItem.rotorupgrade_schemes), ('C'), "casingNichrome"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 1), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.advQuantumtool), ('B'), new ItemStack(IUItem.core, 1, 3),
                ('C'), new ItemStack(IUItem.rotors_upgrade, 1, 0), ('D'),
                "casingRedbrass"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 2), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.adv_spectral_box), ('B'), new ItemStack(IUItem.core, 1, 5),
                ('C'), new ItemStack(IUItem.rotors_upgrade, 1, 1), ('D'),
                "casingMuntsa"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 3), "A A", "CBC", "A A",

                ('A'), new ItemStack(IUItem.advnanobox),
                ('B'), new ItemStack(IUItem.rotorupgrade_schemes), ('C'), IUItem.iridiumPlate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 4), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.advQuantumtool), ('B'), new ItemStack(IUItem.core, 1, 4),
                ('C'), new ItemStack(IUItem.rotors_upgrade, 1, 3), ('D'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 5), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.adv_spectral_box), ('B'), new ItemStack(IUItem.core, 1, 6),
                ('C'), new ItemStack(IUItem.rotors_upgrade, 1, 4), ('D'),
                IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 6), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.advnanobox),
                ('B'), new ItemStack(IUItem.rotorupgrade_schemes), ('C'), IUItem.iridiumPlate, ('D'),
                "casingDuralumin"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 7), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.advQuantumtool), ('B'), new ItemStack(IUItem.core, 1, 4),
                ('C'), new ItemStack(IUItem.rotors_upgrade, 1, 6), ('D'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 8), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.adv_spectral_box), ('B'), new ItemStack(IUItem.core, 1, 6),
                ('C'), new ItemStack(IUItem.rotors_upgrade, 1, 7), ('D'),
                IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 9), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.adv_spectral_box),
                ('B'), new ItemStack(IUItem.rotorupgrade_schemes), ('C'), IUItem.compressIridiumplate, ('D'),
                "doubleplateAlumel"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 10), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.core, 1, 5),
                ('B'), new ItemStack(IUItem.rotorupgrade_schemes), ('C'), IUItem.compressIridiumplate, ('D'),
                "doubleplateInvar"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electric_hoe), "AA ", " A ", " B ",

                ('A'), "plateIron",
                ('B'), IUItem.powerunitsmall
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spawnermodules, 1, 8), "ECE", "BAB", "DCD",

                ('A'), new ItemStack(IUItem.module_schedule), ('E'), new ItemStack(IUItem.core, 1, 5),
                ('B'), new ItemStack(IUItem.quantumtool), ('C'),
                IUItem.iridiumPlate, ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.volcano_hazmat_helmet, 1), "ACA", "BAB", "A A",

                ('A'), "plateCarbon",
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 478), ('C'),
                "gemTopaz"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.volcano_hazmat_leggings, 1), "ACA", "B B", "A A",

                ('A'), "plateCarbon",
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 478), ('C'),
                "gemTopaz"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.volcano_rubber_boots, 1), "A A", "C C", "B B",

                ('A'), "plateCarbon",
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 478), ('C'),
                "gemTopaz"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.volcano_hazmat_chestplate, 1), "AAA", "BCB", "AAA",

                ('A'), "plateCarbon",
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 478), ('C'),
                "gemTopaz"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 38), "CBC", "ADA", "EBE",

                ('E'), IUItem.adv_spectral_box,

                ('D'), IUItem.advancedMachine,

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('B'),
                ("doubleplateMuntsa"),
                ('A'),
                ("doubleplateAluminumbronze")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 32), " C ", "CDC", "ECE",

                ('C'), IUItem.insulatedCopperCableItem,

                ('D'), IUItem.advancedMachine,

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 33), " C ", "CDC", "ECE",

                ('C'), IUItem.insulatedCopperCableItem,

                ('D'), IUItem.advancedMachine,

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 11), "ADA", "CBC", "DED",

                ('A'), new ItemStack(IUItem.advnanobox),
                ('B'), new ItemStack(IUItem.rotorupgrade_schemes), ('C'), IUItem.iridiumPlate, ('D'),
                "doubleplatePlatinum", ('E'), new ItemStack(IUItem.core, 1, 3)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 12), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.advQuantumtool), ('B'), new ItemStack(IUItem.core, 1, 4),
                ('C'), new ItemStack(IUItem.rotors_upgrade, 1, 11), ('D'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 13), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.adv_spectral_box), ('B'), new ItemStack(IUItem.core, 1, 6),
                ('C'), new ItemStack(IUItem.rotors_upgrade, 1, 12), ('D'),
                IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 14), "ADA", "CBC", "DED",

                ('A'), new ItemStack(IUItem.advnanobox),
                ('B'), new ItemStack(IUItem.rotorupgrade_schemes), ('C'), IUItem.iridiumPlate, ('D'),
                "doubleplateVitalium", ('E'), new ItemStack(IUItem.core, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 15), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.advQuantumtool), ('B'), new ItemStack(IUItem.core, 1, 5),
                ('C'), new ItemStack(IUItem.rotors_upgrade, 1, 14), ('D'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 16), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.adv_spectral_box), ('B'), new ItemStack(IUItem.core, 1, 6),
                ('C'), new ItemStack(IUItem.rotors_upgrade, 1, 15), ('D'),
                IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 17), "ADA", "CBC", "DED",

                ('A'), new ItemStack(IUItem.advnanobox),

                ('B'), new ItemStack(IUItem.rotorupgrade_schemes),

                ('C'), IUItem.iridiumPlate,

                ('D'),
                "doubleplateRedbrass",

                ('E'), new ItemStack(IUItem.core, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 18), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.advQuantumtool), ('B'), new ItemStack(IUItem.core, 1, 5),
                ('C'), new ItemStack(IUItem.rotors_upgrade, 1, 17), ('D'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotors_upgrade, 1, 19), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.adv_spectral_box), ('B'), new ItemStack(IUItem.core, 1, 6),
                ('C'), new ItemStack(IUItem.rotors_upgrade, 1, 18), ('D'),
                IUItem.doublecompressIridiumplate
        );
        recipe_machines();
    }

    public static void craft_modules(int meta, int meta1, int meta2) {
        Recipes.recipe.addShapelessRecipe(
                new ItemStack(IUItem.crafting_elements, 1, meta),
                new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        meta1
                ), new ItemStack(IUItem.crafting_elements, 1, meta2)
        );
    }

    public static void recipe_machines() {
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 21), "CCC", "BAB", "DBD",
                ('A'), new ItemStack(IUItem.crafting_elements, 1, 42), ('B'), "ingotGold", ('C'), "ingotIron",
                'D', new ItemStack(IUItem.crafting_elements, 1, 650)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 20), "ABA", "ECD", "  ",

                ('A'), "doubleplateElectrum", ('B'), "doubleplateAlumel", ('C'), IUItem.elemotor, ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 16), 'E', new ItemStack(IUItem.crafting_elements, 1, 614)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 96), " B ", "ACA", " D ",

                ('A'), "doubleplatePlatinum",

                ('B'), "doubleplateVitalium",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 20),

                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 92)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 120), " B ", "ACA", " D ",

                ('A'), "doubleplateSpinel", ('B'), "doubleplateManganese", ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        96
                ), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 116)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 60), "AAA", " B ", "   ",

                ('A'), new ItemStack(IUItem.advBattery, 1, 32767), ('B'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        21
                )
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 614), "AAA", " B ", "   ",

                ('A'), new ItemStack(IUItem.reBattery, 1, 32767), ('B'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        21
                )
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 16), "AAA", "CBC", "   ",

                ('A'), "plateBronze", ('C'), "plateTin", ('B'), new ItemStack(IUItem.crafting_elements, 1, 42)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 92), "AAA", "CBC", "   ",

                ('A'), "platePlatinum", ('C'), "plateCobalt", ('B'), new ItemStack(IUItem.crafting_elements, 1, 16)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 116), "AAA", "CBC", "   ",

                ('A'), "plateSpinel", ('C'), "plateManganese", ('B'), new ItemStack(IUItem.crafting_elements, 1, 92)
        );
        Recipes.recipe.addRecipe(IUItem.elemotor, " A ", "BCB", " D ",
                ('A'), IUItem.casingtin, ('B'), IUItem.coil, ('C'), new ItemStack(IUItem.crafting_elements, 1, 60),
                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 25), "CCC", "DAD", "EBE",
                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 21), ('C'), new ItemStack(
                        IUItem.nanoBox),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 16), 'E', new ItemStack(IUItem.crafting_elements, 1, 651)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 23), "CCC", "DAD", "EBE",
                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8), ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 25), ('C'), new ItemStack(
                        IUItem.quantumtool),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 92), 'E', new ItemStack(IUItem.crafting_elements, 1, 652)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 24), "CCC", "DAD", "EBE",
                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10), ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 23), ('C'), new ItemStack(
                        IUItem.spectral_box),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 116), 'E', new ItemStack(IUItem.crafting_elements, 1, 653)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 42), "AAA", "CBC", "   ",

                ('B'), new ItemStack(IUItem.cirsuitQuantum
                        .getItem(), 1, 17),
                ('C'), Items.REDSTONE,

                ('A'), "ingotTungsten"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 41), "AAA", "BCB", "AAA",
                ('A'), Items.FLINT, ('B'), "ingotIron", ('C'), "ingotTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 46), " A ", "ABA", "ABA",
                ('A'), "plateIron", ('B'), "plateAluminium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 35), " B ", "BAB", "B B",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 46),

                ('B'), "plateChromium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 72), " B ", "BAB", "CCC",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 46),

                ('B'), "plateMikhail",

                ('C'), "plateCobalt"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 70), " B ", "BAB", "CCC",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 46),

                ('B'), "plateZinc",

                ('C'), "plateMagnesium"
        );
        craft_modules(69, 41, 21);
        craft_modules(63, 76, 21);
        craft_modules(159, 141, 21);
        craft_modules(163, 142, 21);
        craft_modules(132, 144, 21);
        craft_modules(165, 164, 21);

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 142), "AAA", "BCB", " D ",

                ('A'), "platePlatinum", ('C'), "plateTitanium",
                ('B'), "plateCobalt", ('D'),
                "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 200), "AAA", "BCB", "   ",

                ('A'), "plateZinc", ('B'), "plateTitanium",
                ('C'), "plateAluminium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 141), "ABB", "C D", " AA",

                ('A'), "plateCobalt", ('B'), "plateNickel",
                ('C'), "plateIron", ('D'),
                "plateTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 144), "AAA", " B ", " C ",

                ('A'), "plateNickel", ('B'), "plateCobalt",
                ('C'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 76), "AAA", " B ", "AAA",

                ('A'), "plateIron", ('B'), "plateTungsten"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 164), "BAB", "B B", "BAB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 200),
                ('B'), "plateTungsten"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 2, 137), "BA ", "AB ", "   ",
                ('A'), "plateSteel", 'B', new ItemStack(IUItem.crafting_elements, 1, 501)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.blockResource, 1, 12), "AAA", "A A", "AAA",
                ('A'), "plateAluminumbronze"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 138), "BA ", "CC ", "   ",
                ('A'), "plateElectrum", ('B'), new ItemStack(IUItem.crafting_elements, 1, 137), 'C', IUItem.plastic_plate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 139), "BA ", "CC ", "   ",
                ('A'), new ItemStack(IUItem.crafting_elements, 1, 479), ('B'), new ItemStack(IUItem.crafting_elements, 1, 138),
                'C', "casingBloodstone"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 140), "BC ", "AA ", "   ",
                ('A'), "casingDraconid", ('B'), new ItemStack(IUItem.crafting_elements, 1, 139), 'C', "plateWoods"
        );
        Recipes.recipe.addShapelessRecipe(Blocks.STICKY_PISTON, Blocks.PISTON, IUItem.latex);
        Recipes.recipe.addRecipe(IUItem.machine, "AA ", "AA ", "   ",
                ('A'), new ItemStack(IUItem.crafting_elements, 1, 137)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 121), "AAA", "ABA", "AAA",
                ('A'), "plateCobalt", ('B'), Items.STRING
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 8, 122), "AAA", "BBB", "CCC",
                ('A'), "plateTungsten", ('B'), "plateTitanium", 'C', new ItemStack(IUItem.crafting_elements, 1, 480)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 201), "AAA", "BBB", "   ",
                ('A'), "plateTitanium", ('B'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 2, 0), "AAA", "ABA", "AAA",
                ('A'), new ItemStack(IUItem.crafting_elements, 1, 444), ('B'), "plateCobalt"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 180), "ABA", "   ", "   ",
                ('A'), "plateCopper", ('B'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 181), " A ", "ABA", "   ",
                ('A'), "plateCopper", ('B'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 182), " A ", " B ", " A ",
                ('A'), "plateCopper", ('B'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 183), "   ", "ABA", " A ",
                ('A'), "plateCopper", ('B'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 184), "   ", "A A", " A ",
                ('A'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 185), " A ", "A A", "   ",
                ('A'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 186), " A ", "A  ", " A ",
                ('A'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 187), " A ", "  A", " A ",
                ('A'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 188), "ABA", "   ", "   ",
                ('A'), "plateDuralumin", ('B'), "plateSilver"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 191), " A ", "ABA", "   ",
                ('A'), "plateDuralumin", ('B'), "plateSilver"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 189), " A ", " B ", " A ",
                ('A'), "plateDuralumin", ('B'), "plateSilver"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 190), "   ", "ABA", " A ",
                ('A'), "plateDuralumin", ('B'), "plateSilver"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 194), " A ", "A A", "   ",
                ('A'), "plateSpinel"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 195), "   ", "A A", " A ",
                ('A'), "plateSpinel"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 193), " A ", "A  ", " A ",
                ('A'), "plateSpinel"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 192), " A ", "  A", " A ",
                ('A'), "plateSpinel"
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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 73), "CCC", "BAB", "D D",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 21),

                ('B'), "plateTin",

                ('C'), "plateChromium",

                ('D'),
                "plateGold"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 37), "BBB", " A ", "   ",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 21),

                ('B'), "plateElectrum"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 30), "BBB", " A ", "   ",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 21),

                ('B'), "plateInvar"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 74), "B B", "DAD", "CCC",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 21),

                ('B'), "plateDenseSteel",

                ('C'), "plateIron",

                ('D'),
                "plateCopper"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 28), "BBB", "CAC", " C ",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 21),

                ('B'), new ItemStack(Items.SKULL, 1, 1),

                ('C'), Blocks.SOUL_SAND
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 29), "   ", "BA ", "   ",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 21),

                ('B'), new ItemStack(IUItem.magnet, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 44), " B ", "BAB", " B ",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 21),

                ('B'), "plateTin"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 47), " B ", "ABA", " B ",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 44),

                ('B'), "plateTin"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 49), " B ", "CBC", " C ",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 47),

                ('B'), "plateTin",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 51), " B ", "ABA", " B ",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 47),

                ('B'), "plateTin"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 52), " B ", "ABA", " B ",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 51),

                ('B'), "plateTin"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 27), "CBC", "CBC", "CAC",
                ('A'), DEFAULT_SENSOR,
                ('C'), IUItem.FluidCell, ('B'), "gearMagnesium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 6), "CBC", "CBC", "CAC",
                ('A'), ADV_SENSOR,
                ('C'), IUItem.FluidCell, ('B'), "gearMagnesium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 83), "CBC", "CBC", "CAC",
                ('A'), IMP_SENSOR,
                ('C'), IUItem.FluidCell, ('B'), "gearMagnesium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 107), "CBC", "CBC", "CAC",
                ('A'), PER_SENSOR,
                ('C'), IUItem.FluidCell, ('B'), "gearMagnesium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 603), "CBC", "CBC", "CAC",
                ('A'), PHOTON_SENSOR,
                ('C'), IUItem.FluidCell, ('B'), "gearMagnesium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 31), "CCC", "BAB", "   ",
                ('A'), DEFAULT_SENSOR,
                ('B'), "gearVanadium", ('C'), "gearIridium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 7), "CCC", "BAB", "   ",
                ('A'), ADV_SENSOR,
                ('B'), "gearVanadium", ('C'), "gearIridium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 84), "CCC", "BAB", "   ",
                ('A'), IMP_SENSOR,
                ('B'), "gearVanadium", ('C'), "gearIridium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 108), "CCC", "BAB", "   ",
                ('A'), PER_SENSOR,
                ('B'), "gearVanadium", ('C'), "gearIridium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 33), "DCD", "BAB", "EFE",
                ('A'), DEFAULT_SENSOR,
                ('B'), "plateVanadium", ('D'), "gearManganese", ('C'),
                "casingNickel", ('E'), "casingMikhail", ('F'), "doubleplateTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 9), "DCD", "BAB", "EFE",
                ('A'), ADV_SENSOR,
                ('B'), "plateVanadium", ('D'), "gearManganese", ('C'),
                "casingNickel", ('E'), "casingMikhail", ('F'), "doubleplateTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 86), "DCD", "BAB", "EFE",
                ('A'), IMP_SENSOR,
                ('B'), "plateVanadium", ('D'), "gearManganese", ('C'),
                "casingNickel", ('E'), "casingMikhail", ('F'), "doubleplateTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 110), "DCD", "BAB", "EFE",
                ('A'), PER_SENSOR,
                ('B'), "plateVanadium", ('D'), "gearManganese", ('C'),
                "casingNickel", ('E'), "casingMikhail", ('F'), "doubleplateTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 607), "DCD", "BAB", "EFE",
                ('A'), PHOTON_SENSOR,
                ('B'), "plateVanadium", ('D'), "gearManganese", ('C'),
                "casingNickel", ('E'), "casingMikhail", ('F'), "doubleplateTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 34), "CBC", "BCB", " A ",
                ('A'), DEFAULT_SENSOR, ('B'), "plateManganese",
                ('C'), "gearAluminium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 12), "CBC", "BCB", " A ",
                ('A'), ADV_SENSOR, ('B'), "plateManganese",
                ('C'), "gearAluminium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 87), "CBC", "BCB", " A ",
                ('A'), IMP_SENSOR, ('B'), "plateManganese",
                ('C'), "gearAluminium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 111), "CBC", "BCB", " A ",
                ('A'), PER_SENSOR, ('B'), "plateManganese",
                ('C'), "gearAluminium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 610), "CBC", "BCB", " A ",
                ('A'), PHOTON_SENSOR, ('B'), "plateManganese",
                ('C'), "gearAluminium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 36), " B ", "BAB", " B ",
                ('A'), DEFAULT_SENSOR, ('B'), "ingotUranium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 13), " B ", "BAB", " B ",
                ('A'), ADV_SENSOR, ('B'), "ingotUranium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 88), " B ", "BAB", " B ",
                ('A'), IMP_SENSOR, ('B'), "ingotUranium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 113), " B ", "BAB", " B ",
                ('A'), PER_SENSOR, ('B'), "ingotUranium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 39), "CCC", "BAB", "BBB",
                ('A'), DEFAULT_SENSOR, ('B'), "plateMuntsa", ('C'), "plateNichrome"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 15), "CCC", "BAB", "BBB",
                ('A'), ADV_SENSOR, ('B'), "plateMuntsa", ('C'), "plateNichrome"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 91), "CCC", "BAB", "BBB",
                ('A'), IMP_SENSOR, ('B'), "plateMuntsa", ('C'), "plateNichrome"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 115), "CCC", "BAB", "BBB",
                ('A'), PER_SENSOR, ('B'), "plateMuntsa", ('C'), "plateNichrome"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 613), "CCC", "BAB", "BBB",
                ('A'), PHOTON_SENSOR, ('B'), "plateMuntsa", ('C'), "plateNichrome"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 43), "BCB", "BAB", "BCB",

                ('A'), DEFAULT_SENSOR,

                ('B'), IUItem.FluidCell,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 655)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 45), "BCB", "BAB", "BCB",

                ('A'), DEFAULT_SENSOR,

                ('B'), IUItem.FluidCell,

                ('C'), "plateAlumel"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 48), "BCB", "BAB", "BCB",

                ('A'), DEFAULT_SENSOR,

                ('B'), IUItem.FluidCell,

                ('C'), "plateFerromanganese"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 50), "BCB", "BAB", "BCB",

                ('A'), DEFAULT_SENSOR,

                ('B'), IUItem.FluidCell,

                ('C'), "plateAluminumbronze"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 53), " B ", "CAD", " E ",

                ('A'), DEFAULT_SENSOR,

                ('B'), "oreIron",

                ('C'), "oreGold",

                ('D'),
                "oreEmerald",

                ('E'), "oreDiamond"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 54), "BBB", "CAC", "EEE",
                ('A'), DEFAULT_SENSOR, ('B'), new ItemStack(Items.SKULL, 1, 1),

                ('C'), "plateAluminumbronze",

                ('E'),
                "plateAlumel"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 56), "FBG", "CAD", "HEJ",
                ('A'), DEFAULT_SENSOR,
                ('B'), "plateAlcled", ('C'), "plateAlumel",
                ('D'),
                "plateVitalium", ('E'), "plateRedbrass",
                ('F'), "plateMuntsa", ('G'), "plateNichrome",
                ('H'), "plateVanadoalumite", ('J'),
                "plateDuralumin"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 57), "   ", " AB", "   ",
                ('A'), DEFAULT_SENSOR, ('B'), new ItemStack(IUItem.module7, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 58), "EDE", "BBB", "CAC",

                ('A'), new ItemStack(IUItem.electricblock, 1, 3),

                ('B'),
                getBlockStack(BlockBaseMachine3.teleporter_iu),
                ('C'), new ItemStack(IUItem.tranformer, 1, 9),

                ('D'),
                DEFAULT_SENSOR,

                ('E'), IUItem.reinforcedStone
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 17), "EDE", "BBB", "CAC",

                ('A'), new ItemStack(IUItem.electricblock, 1, 5),

                ('B'), new ItemStack(IUItem.core, 1, 5),

                ('C'), new ItemStack(IUItem.tranformer, 1, 1),

                ('D'),
                ADV_SENSOR,

                ('E'), IUItem.reinforcedStone
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 93), "EDE", "BBB", "CAC",

                ('A'), new ItemStack(IUItem.electricblock, 1, 6),

                ('B'), new ItemStack(IUItem.core, 1, 6),

                ('C'), new ItemStack(IUItem.tranformer, 1, 3),

                ('D'),
                IMP_SENSOR,

                ('E'), IUItem.reinforcedStone
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 117), "EDE", "BBB", "CAC",

                ('A'), new ItemStack(IUItem.electricblock, 1, 6),

                ('B'), new ItemStack(IUItem.core, 1, 6),

                ('C'), new ItemStack(IUItem.tranformer, 1, 7),

                ('D'),
                PER_SENSOR,

                ('E'), IUItem.reinforcedStone
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 634), "EDE", "BBB", "CAC",

                ('A'), new ItemStack(IUItem.electricblock, 1, 7),

                ('B'), new ItemStack(IUItem.core, 1, 8),

                ('C'), new ItemStack(IUItem.tranformer, 1, 7),

                ('D'),
                PHOTON_SENSOR,

                ('E'), IUItem.reinforcedStone
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 61), "DDD", "CAC", "BBB",
                ('A'), DEFAULT_SENSOR, ('B'), Blocks.DIRT, ('C'), Items.STICK,
                ('D'),
                Blocks.GLASS
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 18), "DDD", "CAC", "BBB",
                ('A'), ADV_SENSOR, ('B'), Blocks.DIRT, ('C'), Items.STICK,
                ('D'),
                Blocks.GLASS
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 94), "DDD", "CAC", "BBB",
                ('A'), IMP_SENSOR, ('B'), Blocks.DIRT, ('C'), Items.STICK,
                ('D'),
                Blocks.GLASS
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 118), "DDD", "CAC", "BBB",
                ('A'), PER_SENSOR, ('B'), Blocks.DIRT, ('C'), Items.STICK,
                ('D'),
                Blocks.GLASS
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 604), "DDD", "CAC", "BBB",
                ('A'), PHOTON_SENSOR, ('B'), Blocks.DIRT, ('C'), Items.STICK,
                ('D'),
                Blocks.GLASS
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 62), "CBC", "BAB", "CBC",
                ('A'), DEFAULT_SENSOR, ('B'), IUItem.paints, ('C'), IUItem.carbonPlate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 64), "BCD", "EAF", "GHJ",
                ('A'), DEFAULT_SENSOR,
                ('B'), Blocks.SAPLING, ('C'), Blocks.YELLOW_FLOWER,
                ('D'),
                Blocks.RED_FLOWER, ('E'), Items.NETHER_WART,
                ('F'), Blocks.VINE, ('G'), Blocks.WATERLILY,
                ('H'), Blocks.BROWN_MUSHROOM, ('J'),
                Blocks.RED_MUSHROOM
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 19), "BCD", "EAF", "GHJ",
                ('A'), ADV_SENSOR,
                ('B'), Blocks.SAPLING, ('C'), Blocks.YELLOW_FLOWER,
                ('D'),
                Blocks.RED_FLOWER, ('E'), Items.NETHER_WART,
                ('F'), Blocks.VINE, ('G'), Blocks.WATERLILY,
                ('H'), Blocks.BROWN_MUSHROOM, ('J'),
                Blocks.RED_MUSHROOM
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 95), "BCD", "EAF", "GHJ",
                ('A'), IMP_SENSOR,
                ('B'), Blocks.SAPLING, ('C'), Blocks.YELLOW_FLOWER,
                ('D'),
                Blocks.RED_FLOWER, ('E'), Items.NETHER_WART,
                ('F'), Blocks.VINE, ('G'), Blocks.WATERLILY,
                ('H'), Blocks.BROWN_MUSHROOM, ('J'),
                Blocks.RED_MUSHROOM
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 119), "BCD", "EAF", "GHJ",
                ('A'), PER_SENSOR,
                ('B'), Blocks.SAPLING, ('C'), Blocks.YELLOW_FLOWER,
                ('D'),
                Blocks.RED_FLOWER, ('E'), Items.NETHER_WART,
                ('F'), Blocks.VINE, ('G'), Blocks.WATERLILY,
                ('H'), Blocks.BROWN_MUSHROOM, ('J'),
                Blocks.RED_MUSHROOM
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 611), "BCD", "EAF", "GHJ",
                ('A'), PHOTON_SENSOR,
                ('B'), Blocks.SAPLING, ('C'), Blocks.YELLOW_FLOWER,
                ('D'),
                Blocks.RED_FLOWER, ('E'), Items.NETHER_WART,
                ('F'), Blocks.VINE, ('G'), Blocks.WATERLILY,
                ('H'), Blocks.BROWN_MUSHROOM, ('J'),
                Blocks.RED_MUSHROOM
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 66), "DDD", "BAB", "   ",
                ('A'), DEFAULT_SENSOR, ('B'), IUItem.overclockerUpgrade,
                ('D'), "plateFerromanganese"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 67), "DDD", "BAB", "   ",
                ('A'), DEFAULT_SENSOR, ('B'), IUItem.overclockerUpgrade,
                ('D'), "plateVitalium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 68), "CCC", "BAB", " B ",
                ('A'), DEFAULT_SENSOR, ('B'), new ItemStack(IUItem.core, 1, 4),

                ('C'), "doubleplateMuntsa"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 179), "CCC", "BAB", " B ",
                ('A'), ADV_SENSOR, ('B'), new ItemStack(IUItem.core, 1, 5),

                ('C'), "doubleplateMuntsa"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 177), "CCC", "BAB", " B ",
                ('A'), IMP_SENSOR, ('B'), new ItemStack(IUItem.core, 1, 6),

                ('C'), "doubleplateMuntsa"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 178), "CCC", "BAB", " B ",
                ('A'), PER_SENSOR, ('B'), new ItemStack(IUItem.core, 1, 7),

                ('C'), "doubleplateMuntsa"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 626), "CCC", "BAB", " B ",
                ('A'), PHOTON_SENSOR, ('B'), new ItemStack(IUItem.core, 1, 7),

                ('C'), "doubleplateMuntsa"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 71), "CCC", "BAB", "DED",

                ('A'), DEFAULT_SENSOR,

                ('C'), "gearGermanium",

                ('B'), "doubleplateIridium",

                ('D'),
                "gearRedbrass",

                ('E'), "doubleplateVitalium"
        );
        Recipes.recipe.addRecipe(IUItem.iridiumPlate, "ABA", "BCB", "ABA",

                ('A'), IUItem.iridiumOre,

                ('C'), "gemDiamond",

                ('B'), IUItem.advancedAlloy
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 3), "CCC", "BAB", "DED",
                ('A'), ADV_SENSOR, ('C'), "gearGermanium", ('B'), "doubleplateIridium",
                ('D'),
                "gearRedbrass", ('E'), IUItem.iridiumOre
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 80), "CCC", "BAB", "DED",
                ('A'), IMP_SENSOR, ('C'), "gearGermanium", ('B'), "doubleplateIridium",
                ('D'),
                "gearRedbrass", ('E'), IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 104), "CCC", "BAB", "DED",
                ('A'), PER_SENSOR, ('C'), "gearGermanium", ('B'), "doubleplateIridium",
                ('D'),
                "gearRedbrass", ('E'), IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 75), "CCC", " A ", " B ",
                ('A'), DEFAULT_SENSOR, ('B'), new ItemStack(IUItem.itemiu, 1, 3),
                ('C'), "plateCobalt"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 10), "CCC", "BAB", "   ",
                ('A'), ADV_SENSOR, ('B'), new ItemStack(IUItem.itemiu, 1, 3),
                ('C'), "plateSilver"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 98), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidneft.getInstance()),
                ('C'), "gearMagnesium", ('D'),
                "plateTitanium"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 98), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidsour_light_oil.getInstance()),
                ('C'), "gearMagnesium", ('D'),
                "plateTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 98), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidsour_medium_oil.getInstance()),
                ('C'), "gearMagnesium", ('D'),
                "plateTitanium"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 98), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidsour_heavy_oil.getInstance()),
                ('C'), "gearMagnesium", ('D'),
                "plateTitanium"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 98), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidsweet_medium_oil.getInstance()),
                ('C'), "gearMagnesium", ('D'),
                "plateTitanium"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 98), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidsweet_heavy_oil.getInstance()),
                ('C'), "gearMagnesium", ('D'),
                "plateTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 99), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), IUItem.Uran238,
                ('C'), "gearVanadium", ('D'),
                "plateChromium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 100), "CBC", "BAB", "CBC",
                ('A'), DEFAULT_SENSOR, ('B'), IUItem.iridiumPlate,
                ('C'), IUItem.photoniy_ingot
        );
        Recipes.recipe.addRecipe(
                ModUtils.setSize(IUItem.overclockerUpgrade, 1),
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
                "plateTin"

        );
        Recipes.recipe.addRecipe(
                IUItem.reactorCoolantTriple,
                " A ",
                "ABA",
                " A ",
                'B',
                IUItem.helium_cooling_mixture,
                'A',
                "plateNichrome"

        );
        Recipes.recipe.addRecipe(
                IUItem.reactorCoolantSix,
                " A ",
                "ABA",
                " A ",
                'B',
                IUItem.cryogenic_cooling_mixture,
                'A',
                "plateVitalium"

        );

        Recipes.recipe.addRecipe(
                ModUtils.setSize(IUItem.overclockerUpgrade_1, 1),
                "CCC",
                "ABA",
                "EDE",
                'C',
                IUItem.reactorCoolantTriple,
                'A',
                new ItemStack(IUItem.cable, 1, 13),
                'B',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),
                'D', ModUtils.setSize(IUItem.overclockerUpgrade, 1), 'E', IUItem.advnanobox

        );
        Recipes.recipe.addRecipe(
                ModUtils.setSize(IUItem.overclockerUpgrade1, 1),
                "CCC",
                "ABA",
                "EDE",
                'C',
                IUItem.reactorCoolantSix,
                'A',
                new ItemStack(IUItem.cable, 1, 1),
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
                new ItemStack(IUItem.crafting_elements, 1, 90),
                'A',
                IUItem.machine,
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 66),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 44)

        );
        Recipes.recipe.addRecipe(
                IUItem.connect_item,
                "A A",
                " A ", "C C", 'C',
                "ingotChromium",
                'A',
                "ingotTitanium"

        );
        Recipes.recipe.addRecipe(
                IUItem.facadeItem,
                "B B",
                "CAC",
                "D D",
                'C',
                "ingotChromium",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B', "ingotIron", 'C', "ingotSteel", 'D', "ingotGermanium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nuclear_bomb),
                "ABA", "BAB", "ABA", 'A', new ItemStack(Blocks.TNT), 'B', new ItemStack(IUItem.nuclear_res, 1)
        );


        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.pump),
                "A A",
                "CBC",
                "A A",
                'A',
                "plateIron",
                'C',
                "ingotIron",
                'B',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_pump),
                "A A", "CBC", "A A", 'A', "plateCarbon", 'C', new ItemStack(IUItem.crafting_elements, 1, 344), 'B', IUItem.pump
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 641),
                " AB", "ACD", "BD ", 'A', new ItemStack(IUItem.crafting_elements, 1, 640), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 639), 'B', "ingotSuperalloyRene", 'C', new ItemStack(IUItem.core, 1, 7)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 195),
                "AFB", "ECE", "BDA",
                'A', "plateOsmiridium",
                'D', new ItemStack(IUItem.core, 1, 5),
                'F', new ItemStack(IUItem.neutronium),
                'B', "plateNiobiumTitanium",
                'C', new ItemStack(IUItem.blockdoublemolecular),
                'E', TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 194),
                "ADA", "BCB", "EFE",
                'A', "plateTantalumTungstenHafnium",
                'D', new ItemStack(IUItem.crafting_elements, 1, 68),
                'F', new ItemStack(IUItem.basemachine2, 1, 189),
                'B', "plateNiobiumTitanium",
                'C', new ItemStack(IUItem.basemachine2, 1, 38),
                'E', "plateDuralumin"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.imp_pump),
                "A A", "CBC", "A A", 'A', "gemTopaz", 'C', "ingotGold", 'B', IUItem.adv_pump
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.per_pump),
                "A A", "CBC", "A A", 'A', "gemDiamond", 'C', "plateOsmium", 'B', IUItem.imp_pump
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.fan),
                "ACA", "CBC", "ACA", 'A', "plateIron", 'C', "plateTitanium", 'B',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_fan),
                "ACA", "CBC", "ACA", 'A', "plateElectrum", 'C', "plateTitanium", 'B', IUItem.fan
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.imp_fan),
                "ACA", "CBC", "ACA", 'A', "platePlatinum", 'C', "plateTitanium", 'B', IUItem.adv_fan
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.per_fan),
                "ACA", "CBC", "ACA", 'A', "plateCadmium", 'C', "plateTitanium", 'B', IUItem.imp_fan
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.simple_capacitor_item),
                "AAA", "BBB", "AAA", 'A', "ingotIron", 'B', "plateInvar"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_capacitor_item),
                "AAA", "BCB", "AAA", 'A', "plateCarbon", 'B', "plateObsidian", 'C', IUItem.simple_capacitor_item
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.imp_capacitor_item),
                "AAA", "BCB", "AAA", 'A', "plateSteel", 'B', "plateTitanium", 'C', IUItem.adv_capacitor_item
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.per_capacitor_item),
                "AAA", "BCB", "AAA", 'A', "plateFerromanganese", 'B', "plateGermanium", 'C', IUItem.imp_capacitor_item
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.simple_exchanger_item),
                "ABA", "ABA", "AAA", 'A', "plateIron", 'B', IUItem.reactor_plate
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_exchanger_item),
                "ABA", "ABA", "AAA", 'A', "plateAluminium", 'B', IUItem.adv_reactor_plate
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.imp_exchanger_item),
                "ABA", "ABA", "AAA", 'A', "plateAluminumbronze", 'B', IUItem.imp_reactor_plate
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.per_exchanger_item),
                "ABA", "ABA", "AAA", 'A', "plateDuralumin", 'B', IUItem.per_reactor_plate
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.nuclear_res, 1, 8),
                "ABA", "BCB", "ABA", 'A', IUItem.quad_uranium_fuel_rod, 'B', "plateLead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.nuclear_res, 1, 9),
                "ABA", "BCB", "ABA", 'A', IUItem.quad_mox_fuel_rod, 'B', "plateLead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets, 1, 0),
                "ABA", "BCB", "ABA", 'A', IUItem.reactoramericiumQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets, 1, 1),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorneptuniumQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets, 1, 2),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorcuriumQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets, 1, 3),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorcaliforniaQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets, 1, 4),
                "ABA", "BCB", "ABA", 'A', IUItem.reactortoriyQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets, 1, 5),
                "ABA", "BCB", "ABA", 'A', IUItem.reactormendeleviumQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets, 1, 6),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorberkeliumQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets, 1, 7),
                "ABA", "BCB", "ABA", 'A', IUItem.reactoreinsteiniumQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets, 1, 8),
                "ABA", "BCB", "ABA", 'A', IUItem.reactoruran233Quad, 'B', "plateLead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets, 1, 9),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorprotonQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets, 1, 10),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorfermiumQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets, 1, 11),
                "ABA", "BCB", "ABA", 'A', IUItem.reactornobeliumQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets, 1, 12),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorlawrenciumQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.radiationModule, 1, 0),
                "BCB",
                "DED",
                "BAB",
                'A',
                IUItem.module_schedule,
                'B',
                "plateTantalum",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                "plateNickel",
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 1),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 0), 'B', "plateCadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3), 'D',
                "plateNickel", 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 2),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 1), 'B', "doubleplateCadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5), 'D',
                "plateNickel", 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 3),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 2), 'B', "doubleplateDuralumin", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'D',
                "plateNickel", 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.radiationModule, 1, 4),
                "BCB",
                "DED",
                "BAB",
                'A',
                IUItem.module_schedule,
                'B',
                "plateTantalum",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                new ItemStack(IUItem.radiationresources, 1, 0),
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 5),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 4), 'B', "plateCadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3), 'D',
                new ItemStack(IUItem.radiationresources, 1, 0), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 6),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 5), 'B', "doubleplateCadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'D',
                new ItemStack(IUItem.radiationresources, 1, 0), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 7),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 6), 'B', "doubleplateDuralumin", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'D',
                new ItemStack(IUItem.radiationresources, 1, 0), 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.radiationModule, 1, 8),
                "BCB",
                "DED",
                "BAB",
                'A',
                IUItem.module_schedule,
                'B',
                "plateTantalum",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                "doubleplateAlumel",
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 9),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 8), 'B', "plateCadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4), 'D',
                "doubleplateAlumel", 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 10),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 9), 'B', "doubleplateCadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'D',
                "doubleplateAlumel", 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 11),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 10), 'B', "doubleplateDuralumin", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'D',
                "doubleplateAlumel", 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.radiationModule, 1, 12),
                "BCB",
                "DED",
                "BAB",
                'A',
                IUItem.module_schedule,
                'B',
                "plateTantalum",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                new ItemStack(IUItem.vent),
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 13),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 12), 'B', "plateCadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4), 'D',
                new ItemStack(IUItem.adv_Vent), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 14),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 13), 'B', "doubleplateCadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'D',
                new ItemStack(IUItem.imp_Vent), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 15),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 14), 'B', "doubleplateDuralumin", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'D',
                new ItemStack(IUItem.per_Vent), 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.radiationModule, 1, 16),
                "BCB",
                "DED",
                "BAB",
                'A',
                IUItem.module_schedule,
                'B',
                "plateTantalum",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                new ItemStack(IUItem.componentVent),
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 17),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 16), 'B', "plateCadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4), 'D',
                new ItemStack(IUItem.adv_componentVent), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 18),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 17), 'B', "doubleplateCadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'D',
                new ItemStack(IUItem.imp_componentVent), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 19),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 18), 'B', "doubleplateDuralumin", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'D',
                new ItemStack(IUItem.per_componentVent), 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.radiationModule, 1, 20),
                "BCB",
                "DED",
                "BAB",
                'A',
                IUItem.module_schedule,
                'B',
                "plateTantalum",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                new ItemStack(IUItem.heat_exchange),
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 21),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 20), 'B', "plateCadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4), 'D',
                new ItemStack(IUItem.adv_heat_exchange), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 22),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 21), 'B', "doubleplateCadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'D',
                new ItemStack(IUItem.imp_heat_exchange), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 23),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 22), 'B', "doubleplateDuralumin", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'D',
                new ItemStack(IUItem.per_heat_exchange), 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.radiationModule, 1, 24),
                "BCB",
                "DED",
                "BAB",
                'A',
                IUItem.module_schedule,
                'B',
                "plateTantalum",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                new ItemStack(IUItem.capacitor),
                'E',
                IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 25),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 24), 'B', "plateCadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3), 'D',
                new ItemStack(IUItem.adv_capacitor), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 26),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 25), 'B', "doubleplateCadmium", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'D',
                new ItemStack(IUItem.imp_capacitor), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule, 1, 27),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule, 1, 26), 'B', "doubleplateDuralumin", 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8), 'D',
                new ItemStack(IUItem.per_capacitor), 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 323),
                "BCB", "BAB", "DDD", 'A', DEFAULT_SENSOR, 'B', "plateSteel", 'D', "ingotMikhail", 'C', "gearOsmium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 325),
                " C ", "BAB", "DCD", 'A', DEFAULT_SENSOR, 'B', new ItemStack(IUItem.nuclear_res), 'D', "plateCarbon", 'C',
                "plateLapis"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 328),
                "ECE", "BAB", "DCD", 'A', DEFAULT_SENSOR, 'B', "plateIron", 'D', "plateSilver", 'C',
                "plateOsmium", 'E', IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 440),
                "   ", "BAB", "DCD", 'A', DEFAULT_SENSOR, 'B', new ItemStack(IUItem.crafting_elements, 1, 386), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 320), 'C',
                "plateTantalum"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 441),
                "CDC", "BAB", "BDB", 'A', DEFAULT_SENSOR, 'B', new ItemStack(IUItem.itemiu, 1, 2), 'D',
                "plateDenseLead", 'C',
                "plateSpinel"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 442),
                "CDC", "BAB", "BDB", 'A', DEFAULT_SENSOR, 'B', new ItemStack(IUItem.sunnariumpanel, 1, 0), 'D',
                new ItemStack(IUItem.sunnarium, 1, 3), 'C',
                new ItemStack(IUItem.crafting_elements, 1, 319)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 354),
                "BAB", "BAB", "BDB", 'A', "gemRuby", 'B', "plateTitanium", 'D',
                "dustRedstone"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 1),
                " ", "CAD", " B ", 'A', "machineBlock", 'B', IUItem.elemotor, 'D', new ItemStack(IUItem.crafting_elements, 1, 36),
                'C', new ItemStack(IUItem.module7, 1, 9)
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 20),
                " ",
                "CAD",
                " B ",
                'A',
                "machineBlockAdvanced",
                'B',
                IUItem.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 36),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 226)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 91),
                " ", " A ", " B ", 'A', IUItem.blockpanel, 'B', IUItem.module_schedule
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 94),
                " F ",
                "CAD",
                " B ",
                'A',
                "machineBlock",
                'B',
                IUItem.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 51),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 47),
                'F',
                new ItemStack(IUItem.crafting_elements, 1, 323)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 93),
                "DEF",
                "CAC",
                " B ",
                'A',
                "machineBlock",
                'B',
                IUItem.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 51),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 44),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 433),
                'F',
                new ItemStack(IUItem.crafting_elements, 1, 323)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 95),
                "   ",
                "CAD",
                " B ",
                'A',
                "machineBlock",
                'B',
                IUItem.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 442),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 98),
                "   ",
                "CAD",
                " B ",
                'A',
                "machineBlock",
                'B',
                IUItem.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 30),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 96),
                "CDC",
                "CAC",
                "B H",
                'A',
                "machineBlock",
                'B',
                IUItem.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 354),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 44), 'H', getBlockStack(BlockBaseMachine3.steam_sharpener)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 99),
                " E ",
                "DAC",
                " B ",
                'A',
                "machineBlock",
                'B',
                IUItem.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 328),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 51),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 97),
                "   ",
                "DAC",
                " B ",
                'A',
                "machineBlock",
                'B',
                IUItem.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 440),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 49)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 100),
                " E ",
                "DAC",
                " B ",
                'A',
                "machineBlock",
                'B',
                IUItem.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 49),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 51),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 325)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 112),
                " B ",
                " A ",
                "  ",
                'A',
                new ItemStack(IUItem.blockResource, 1, 9),
                'B',
                new ItemStack(IUItem.module7, 1)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 118),
                " B ",
                "CAC",
                "CDC",
                'A',
                new ItemStack(IUItem.blockResource, 1, 8),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 469),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 52),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 469),
                "BBB",
                "CAC",
                "DBD",
                'A',
                DEFAULT_SENSOR,
                'B',
                "plateOsmium",
                'C',
                "plateNeodymium",
                'D',
                "plateObsidian"
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 101),
                " E ",
                "DAC",
                " B ",
                'A',
                "machineBlock",
                'B',
                IUItem.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 44),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 36),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 441)
        );


        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 113),
                " B ",
                " A ",
                " C ",
                'A',
                "machineBlock",
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 243),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 47),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 36)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 114),
                "B B",
                " A ",
                "B B",
                'A',
                new ItemStack(IUItem.basemachine2, 1, 113),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 115),
                "B B",
                " A ",
                "B B",
                'A',
                new ItemStack(IUItem.basemachine2, 1, 114),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 116),
                "B B",
                " A ",
                "B B",
                'A',
                new ItemStack(IUItem.basemachine2, 1, 115),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 140)
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 117),
                " A ",
                "BCD",
                " E ",
                'A',
                new ItemStack(IUItem.blockResource, 1, 7),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 471),
                'C',
                new ItemStack(IUItem.blockResource, 1, 9),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 99),
                'E',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 119),
                "   ",
                "BCD",
                "   ",
                'A',
                new ItemStack(IUItem.blockResource, 1, 7),
                'B',
                new ItemStack(IUItem.expmodule),
                'C',
                new ItemStack(IUItem.blockResource, 1, 9),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 35),
                'E',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 123),
                " A ",
                " B ",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 458),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.blockResource, 1, 9),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 35),
                'E',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 122),
                " A ",
                " B ",
                " C ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 458),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 27),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 35),
                'E',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 121),
                " A ",
                " B ",
                " C ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 458),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 60),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 35),
                'E',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 120),
                "   ",
                "ABC",
                " D ",
                'A',
                new ItemStack(IUItem.recipe_schedule),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 128),
                'D',
                new ItemStack(IUItem.motors_with_improved_bearings_),
                'E',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 471),
                " A ",
                "BCB",
                "DDD",
                'A',
                new ItemStack(IUItem.nuclear_bomb),
                'B',
                new ItemStack(IUItem.radiationresources, 1, 3),
                'C',
                DEFAULT_SENSOR,
                'D',
                new ItemStack(IUItem.alloysplate, 1, 18)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 129),
                "   ",
                "ABC",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 202),
                'B',
                new ItemStack(IUItem.blockResource, 1, 9),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 90),
                'D',
                new ItemStack(IUItem.motors_with_improved_bearings_),
                'E',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 128),
                "ACE",
                " B ",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 202),
                'B',
                new ItemStack(IUItem.blockResource, 1, 9),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 90),
                'D',
                new ItemStack(IUItem.motors_with_improved_bearings_),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 251)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 193),
                "ACE",
                " B ",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 202),
                'B',
                new ItemStack(IUItem.blockResource, 1, 9),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 90),
                'D',
                new ItemStack(IUItem.motors_with_improved_bearings_),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 263)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 126),
                "AAA",
                "CBE",
                " D ",
                'A',
                "plateInconel",
                'B',
                new ItemStack(IUItem.blockResource, 1, 9),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 90),
                'D',
                new ItemStack(IUItem.motors_with_improved_bearings_),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 614)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 650),
                " A ",
                "CBC",
                "C C",
                'A',
                new ItemStack(IUItem.blockResource, 1, 13),
                'B',
                new ItemStack(Items.REDSTONE),
                'C',
                "casingNichrome"
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 651),
                " A ",
                "CBC",
                "C C",
                'A',
                new ItemStack(IUItem.blockResource, 1, 13),
                'B',
                new ItemStack(Items.REDSTONE),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 505)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 652),
                " A ",
                "CBC",
                "C C",
                'A',
                new ItemStack(IUItem.blockResource, 1, 13),
                'B',
                new ItemStack(Items.REDSTONE),
                'C',
                "casingOrichalcum"
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 653),
                " A ",
                "CBC",
                "C C",
                'A',
                new ItemStack(IUItem.blockResource, 1, 13),
                'B',
                new ItemStack(Items.REDSTONE),
                'C',
                "casingInconel"
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 654),
                " A ",
                "CBC",
                "C C",
                'A',
                new ItemStack(IUItem.blockResource, 1, 13),
                'B',
                new ItemStack(Items.REDSTONE),
                'C',
                "casingAdamantium"
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 130),
                "AAA",
                "CBE",
                " D ",
                'A',
                "plateInconel",
                'B',
                new ItemStack(IUItem.blockResource, 1, 9),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 242),
                'D',
                new ItemStack(IUItem.reactorData),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 90)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 140),
                "AEA",
                "CBG",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.basemachine2, 1, 153),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 72),
                'G',
                new ItemStack(IUItem.crafting_elements, 1, 27)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 162),
                "   ",
                "CBG",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 469),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 72),
                'G',
                new ItemStack(IUItem.bags)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 150),
                " A ",
                "CBG",
                "E D",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 27),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'D',
                getBlockStack(BlockBaseMachine3.steamdryer),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'G',
                new ItemStack(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 151),
                "   ",
                "CBG",
                "A D",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'D',
                getBlockStack(BlockBaseMachine3.steam_squeezer),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 72),
                'G',
                new ItemStack(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 156),
                "CAC",
                "CBC",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 267),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 72),
                'G',
                new ItemStack(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 156),
                "CAC",
                "CBC",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 267),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 72),
                'G',
                new ItemStack(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 157),
                "EAC",
                "CBC",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 267),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 44),
                'G',
                new ItemStack(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 158),
                " A ",
                " B ",
                " D ",
                'A',
                new ItemStack(IUItem.primalFluidHeater),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 44),
                'G',
                new ItemStack(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 159),
                "CAC",
                "CBC",
                " D ",
                'A',
                new ItemStack(IUItem.primalSiliconCrystal),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 495),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 44),
                'G',
                new ItemStack(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 173),
                "A E",
                "CBC",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 27),
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 65)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 176),
                " A ",
                "EBC",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 267),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 44),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 51),
                'G',
                new ItemStack(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 127),
                "   ",
                "EBC",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 267),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                IUItem.ejectorUpgrade,
                'D',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'E',
                IUItem.pullingUpgrade,
                'G',
                new ItemStack(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 26),
                " A ",
                "EBC",
                " D ",
                'A',
                new ItemStack(IUItem.gasChamber),
                'B',
                new ItemStack(IUItem.blockResource, 1, 9),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 263),
                'D',
                new ItemStack(IUItem.motors_with_improved_bearings_),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 267),
                'G',
                new ItemStack(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 133),
                "A A",
                "ABA",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 365),
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                new ItemStack(IUItem.basemachine2, 1, 130),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 100)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 134),
                "A A",
                "ABA",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 363),
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                new ItemStack(IUItem.basemachine2, 1, 130),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 100)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 137),
                "   ",
                "CBA",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 54),
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 29),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 100)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 138),
                " E ",
                "CBA",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 44),
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                new ItemStack(IUItem.blockResource, 1, 9),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 52),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 459)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 135),
                "EEE",
                " B ",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 44),
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                new ItemStack(IUItem.basemachine2, 1, 33),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 52),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.cutter, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 625),
                "B B",
                "CAC",
                "   ",
                'A',
                new ItemStack(IUItem.spectral_box),
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                new ItemStack(IUItem.alloysdoubleplate, 1, 5),
                'C',
                new ItemStack(IUItem.alloysdoubleplate, 1, 14),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.cutter, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 623),
                "AC ",
                "BB ",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 140),
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                "plateNimonic",
                'C',
                "plateSuperalloyRene",
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.cutter, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 621),
                "AAA",
                "BCB",
                "   ",
                'A',
                new ItemStack(IUItem.plate, 1, 44),
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                new ItemStack(IUItem.alloysplate, 1, 5),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 116),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.cutter, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 622),
                " A ",
                "CDC",
                " B ",
                'A',
                "plateTantalum",
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 621),
                'C',
                "plateArsenic",
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 120),
                'E',
                new ItemStack(IUItem.cutter, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 620),
                "AAA",
                "BDB",
                "ECE",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 625),
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 621),
                'C',
                new ItemStack(IUItem.basecircuit, 1, 21),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 24),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 654)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 132),
                "CAC",
                "CBC",
                "DDD",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 88),
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                new ItemStack(IUItem.blockResource, 1, 9),
                'C',
                new ItemStack(IUItem.plate, 1, 44),
                'D',
                new ItemStack(IUItem.plate, 1, 29),
                'E',
                new ItemStack(IUItem.cutter, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 136),
                " E ",
                " B ",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 44),
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 52),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 29)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 459),
                " E ",
                "ABA",
                "   ",
                'A',
                new ItemStack(IUItem.machinekit),
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                DEFAULT_SENSOR,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 52),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                IUItem.overclockerUpgrade
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 177),
                "ACE",
                "FBC",
                " DH",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 27),
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 267), 'H',
                new ItemStack(IUItem.crafting_elements, 1, 265)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 470),
                "AAA",
                "CBC",
                "   ",
                'A',
                "plateNimonic",
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                DEFAULT_SENSOR,
                'C',
                new ItemStack(IUItem.alloygear, 1, 18),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 100)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 131),
                " C ",
                " B ",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 27),
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                new ItemStack(IUItem.basemachine2, 1, 130),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 470),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 100)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 174),
                "   ",
                "EBA",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 267),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 44),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 51),
                'G',
                new ItemStack(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 161),
                " A ",
                " B ",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 68),
                'B',
                new ItemStack(IUItem.blockResource, 1, 9),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 495),
                'D',
                new ItemStack(IUItem.imp_motors_with_improved_bearings_),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 44),
                'G',
                new ItemStack(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 160), "   ", "DAD", "BCB",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('C'), IUItem.iridiumPlate,

                ('B'), new ItemStack(IUItem.photoniy),

                ('A'),
                new ItemStack(IUItem.convertersolidmatter)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 125),
                "   ",
                "CBE",
                " D ",
                'A',
                "plateInconel",
                'B',
                new ItemStack(IUItem.blockResource, 1, 9),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 240),
                'D',
                new ItemStack(IUItem.imp_motors_with_improved_bearings_),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 90)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 141),
                "AFA",
                "CBE",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'F', new ItemStack(IUItem.crafting_elements, 1, 47),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 154),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 27)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 142),
                "AFA",
                "CBE",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 154),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 27)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 143),
                "AAA",
                "CBE",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 601),
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 154),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 27)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 144),
                "AFA",
                "CBE",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 601),
                'F', new ItemStack(IUItem.crafting_elements, 1, 154),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 98),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 79)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 145),
                "ACE",
                "FBC",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 27),
                'F', new ItemStack(IUItem.crafting_elements, 1, 47),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 100)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 146),
                "ACE",
                "FBC",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 27),
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 276),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 65)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 147),
                " A ",
                " B ",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 47),
                'F', new ItemStack(IUItem.crafting_elements, 1, 44),
                'B',
                getBlockStack(BlockBaseMachine2.electrolyzer_iu),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'D',
                IUItem.motors_with_improved_bearings_,
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 65)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 148),
                "CAC",
                "EBF",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 251),
                'F', new ItemStack(IUItem.crafting_elements, 1, 154),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'D',
                IUItem.motors_with_improved_bearings_,
                'E',
                new ItemStack(IUItem.module7, 1, 9)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 149),
                "CAC",
                "EBF",
                " D ",
                'A',
                IUItem.antisoilpollution,
                'F', new ItemStack(IUItem.crafting_elements, 1, 154),
                'B',
                new ItemStack(IUItem.blockResource, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'D',
                IUItem.motors_with_improved_bearings_,
                'E',
                new ItemStack(IUItem.module7, 1, 9)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 458),
                "   ",
                "BCB",
                "DDD",
                'A',
                new ItemStack(IUItem.nuclear_bomb),
                'B',
                new ItemStack(IUItem.alloysplate, 1, 6),
                'C',
                DEFAULT_SENSOR,
                'D',
                new ItemStack(IUItem.alloysplate, 1, 30)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 102),
                "   ", "BAC", " D ", 'A', "machineBlockAdvanced", 'B', new ItemStack(IUItem.crafting_elements, 1, 439), 'C',
                new ItemStack(IUItem.crafting_elements, 1, 36), 'D', IUItem.elemotor

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 104),
                "FEF", "BAC", " D ", 'A', "machineBlockAdvanced", 'B', new ItemStack(IUItem.crafting_elements, 1, 47), 'C',
                new ItemStack(IUItem.crafting_elements, 1, 35), 'D', IUItem.elemotor, 'E', Items.ENCHANTED_BOOK, 'F',
                "doubleplateCadmium"

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 105),
                " E ", "BAC", " D ", 'A', "machineBlock", 'B', new ItemStack(IUItem.crafting_elements, 1, 44), 'C',
                new ItemStack(IUItem.crafting_elements, 1, 43), 'D', IUItem.elemotor, 'E', IUItem.coolant

        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 103),
                "FBG",
                "EAC",
                " D ",
                'A',
                "machineBlockAdvanced",
                'B',
                new ItemStack(IUItem.nuclear_res, 1, 8),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 31),
                'D',
                IUItem.elemotor,
                'E',
                new ItemStack(IUItem.crafting_elements, 1
                        , 36),
                'F',
                new ItemStack(IUItem.crafting_elements, 1, 51),
                'G',
                new ItemStack(IUItem.crafting_elements, 1, 47)

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.earthQuarry, 1, 2),
                "C C", " B ", "A A", 'B', "machineBlock", 'A', "plateStainlessSteel", 'C', "plateMolybdenumSteel"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.earthQuarry, 1),
                "DBD",
                " A ",
                " C ",
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 42),
                'A',
                new ItemStack(IUItem.earthQuarry, 1, 2)
                ,
                'C'
                ,
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                "stickZinc"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.earthQuarry, 1, 1),
                "DBD",
                "EAE",
                " C ",
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 226),
                'A',
                new ItemStack(IUItem.earthQuarry, 1, 2)
                ,
                'C'
                ,
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'D',
                "gearOsmium",
                'E',
                "plateCarbon"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.earthQuarry, 1, 3),
                " E ", "DAD", "CBC", 'B', new ItemStack(IUItem.oilquarry), 'A', new ItemStack(IUItem.earthQuarry, 1, 2)
                , 'C'
                , "plateTitanium", 'D', "plateSteel", 'E', "plateIridium"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.earthQuarry, 1, 4),
                "   ", "DAD", "CBC", 'B', new ItemStack(Blocks.CHEST), 'A', new ItemStack(IUItem.earthQuarry, 1, 2)
                , 'C'
                , "plateGold", 'D', "plateTin"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.earthQuarry, 1, 5),
                "BDB", "DAD", "BDB", 'B', new ItemStack(IUItem.item_pipes, 1, 1), 'D', new ItemStack(IUItem.item_pipes), 'A',
                new ItemStack(IUItem.earthQuarry, 1, 2)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.anvil),
                "AAA", " A ", "BBB", 'A', "blockIron", 'B', "blockTitanium"
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 2, 364),
                "AB ", "BA ", "   ", 'A', "plateLead", 'B', "plateTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 2, 363),
                "AB ", "BA ", "CC ", 'A', "plateLead", 'B', "plateTungsten", 'C', new ItemStack(IUItem.crafting_elements, 1, 479)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 2, 365),
                "AB ", "BA ", "   ", 'A', "plateLead", 'B', "plateLithium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 2, 366),
                "AB ", "BC ", "   ", 'A', "plateLead", 'B', "platePlatinum", 'C', "plateTantalum"
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 420),
                "AA ",
                "BC ",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 366),
                'B',
                "plateElectrum",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 453)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 378),
                "AA ",
                "BC ",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 420),
                'B',
                "plateCobalt",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 453)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 405),
                "AA ",
                "BC ",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 378),
                'B',
                "plateMagnesium",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 453)
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 419),
                "AA ",
                "BC ",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 365),
                'B',
                "plateElectrum",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 453)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 377),
                "AA ",
                "BC ",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 419),
                'B',
                "plateCobalt",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 453)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 394),
                "AA ",
                "BC ",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 377),
                'B',
                "plateMagnesium",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 453)
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 417),
                "AA ",
                "BC ",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 363),
                'B',
                "plateElectrum",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 453)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 375),
                "AA ",
                "BC ",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 417),
                'B',
                "plateCobalt",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 453)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 392),
                "AA ",
                "BC ",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 375),
                'B',
                "plateMagnesium",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 453)
        );


        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 418),
                "AA ",
                "BC ",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 364),
                'B',
                "plateElectrum",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 453)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 376),
                "AA ",
                "BC ",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 418),
                'B',
                "plateCobalt",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 453)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 2, 393),
                "AA ", "BC ", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 376), 'B', "plateMagnesium", 'C',
                new ItemStack(IUItem.crafting_elements, 1, 453)
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 439),
                "CEC ",
                "EAE",
                " B ",
                'A',
                DEFAULT_SENSOR,
                'B',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                'C',
                "plateBronze",
                'E',
                "doubleplateOsmium"
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 435),
                "BСB",
                "BAB",
                "BDB",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B',
                "platePlatinum",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 42),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 366)
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 436),
                "BСB",
                "BAB",
                "BDB",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B',
                "plateZinc",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 42),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 365)
        );


        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 342),
                "CBC",
                "BAB",
                "DBD",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B',
                "plateLithium",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 386),
                'D',
                "plateObsidian"
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 324),
                "AAA",
                " B ",
                "DCD",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'A',
                "plateCobalt",
                'B',
                IUItem.advancedAlloy,
                'D',
                "plateCarbon"
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 322),
                "ABA",
                "EFE",
                "DCD",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'A',
                "plateTungsten",
                'B',
                IUItem.advancedAlloy,
                'D',
                "plateBronze",
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 445),
                'F',
                "plateTin"
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 385),
                "A A",
                "EFG",
                "DCD",
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'A',
                "plateSteel",
                'D',
                "plateBor",
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 445),
                'F',
                "plateCarbon",
                'G',
                new ItemStack(IUItem.crafting_elements, 1, 446)
        );


        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 321),
                "B B",
                "DAD",
                "B B",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 320),
                'D',
                "plateCadmium"
        );


        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 327),
                "BCB",
                "DAD",
                "BCB",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B',
                "plateCarbon",
                'D',
                "plateOsmium",
                'C',
                "plateElectrum"
        );


        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 326),
                "BCB",
                "BAB",
                "DCD",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'B',
                "plateCobalt",
                'D',
                "plateVanadoalumite",
                'C',
                "plateManganese"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 356),
                " B ", " A ", " B ", 'A', new ItemStack(IUItem.crafting_elements, 1, 294), 'B', "plateIron"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 424),
                "AAA", "ABA", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 356), 'B', "plateElectrum"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 371),
                "AAA", "ABA", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 424), 'B', "platePlatinum"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 397),
                "AAA", "ABA", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 371), 'B', "plateSpinel"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 387),
                " A ", "ABA", " A ", 'A', new ItemStack(IUItem.crafting_elements, 1, 356), 'B', "doubleplateGermanium"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 425),
                " A ", "ABA", " A ", 'A', new ItemStack(IUItem.crafting_elements, 1, 387), 'B', "doubleplateAlumel"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 372),
                " A ", "ABA", " A ", 'A', new ItemStack(IUItem.crafting_elements, 1, 425), 'B', "doubleplateVitalium"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 398),
                " A ", "ABA", " A ", 'A', new ItemStack(IUItem.crafting_elements, 1, 372), 'B', "doubleplateFerromanganese"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 362),
                "CDC", "BAB", "CDC", 'A', new ItemStack(IUItem.itemiu, 1, 3), 'B', new ItemStack(IUItem.neutroniumingot), 'C',
                "plateGermanium", 'D', "plateOsmium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 101), "CCC", "BAB", "DED",

                ('A'), DEFAULT_SENSOR,

                ('C'), "gearGermanium",

                ('B'), "doubleplateGermanium",

                ('D'),
                "gearNichrome",

                ('E'), "doubleplateVitalium"
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 4),
                "CCC",
                "BAB",
                "DED",
                ('A'),
                ADV_SENSOR,
                ('C'),
                "gearGermanium",
                ('B'),
                "doubleplateGermanium",
                ('D'),
                "gearNichrome",
                ('E'),
                IUItem.iridiumOre
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 81),
                "CCC",
                "BAB",
                "DED",
                ('A'),
                IMP_SENSOR,
                ('C'),
                "gearGermanium",
                ('B'),
                "doubleplateGermanium",
                ('D'),
                "gearNichrome",
                ('E'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 105),
                "CCC",
                "BAB",
                "DED",
                ('A'),
                PER_SENSOR,
                ('C'),
                "gearGermanium",
                ('B'),
                "doubleplateGermanium",
                ('D'),
                "gearNichrome",
                ('E'),
                IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 112), "BBB", "CAC", "   ",

                ('A'), DEFAULT_SENSOR,

                ('B'), IUItem.photoniy_ingot,

                ('C'), "gearZinc"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 124), "CBC", "BAB", " B ",

                ('A'), DEFAULT_SENSOR,

                ('B'), "doubleplateVitalium",

                ('C'), "doubleplateInvar"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 135), "CBC", "BAB", " B ",
                ('A'), ADV_SENSOR, ('B'), "doubleplateVitalium",
                ('C'), "doubleplateInvar"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 146), "CBC", "BAB", " B ",
                ('A'), IMP_SENSOR, ('B'), "doubleplateVitalium",
                ('C'), "doubleplateInvar"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 157), "CBC", "BAB", " B ",
                ('A'), PER_SENSOR, ('B'), "doubleplateVitalium",
                ('C'), "doubleplateInvar"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 632), "CBC", "BAB", " B ",
                ('A'), PHOTON_SENSOR, ('B'), "doubleplateVitalium",
                ('C'), "doubleplateInvar"
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(0).getStack(),
                new ItemStack(Items.WHEAT_SEEDS),
                new ItemStack(Items.WHEAT_SEEDS)
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(17).getStack(),
                CropNetwork.instance.getCrop(17).getDrop().get(0),
                CropNetwork.instance.getCrop(17).getDrop().get(0)
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(1).getStack(),
                new ItemStack(Items.REEDS),
                new ItemStack(Items.REEDS)
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(4).getStack(),
                CropNetwork.instance.getCrop(4).getDrop().get(0),
                CropNetwork.instance.getCrop(4).getDrop().get(0)
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(81).getStack(),
                CropNetwork.instance.getCrop(81).getDrop().get(0),
                CropNetwork.instance.getCrop(81).getDrop().get(0)
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(5).getStack(),
                CropNetwork.instance.getCrop(5).getDrop().get(0),
                CropNetwork.instance.getCrop(5).getDrop().get(0)
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(6).getStack(),
                CropNetwork.instance.getCrop(6).getDrop().get(0),
                CropNetwork.instance.getCrop(6).getDrop().get(0)
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(7).getStack(),
                CropNetwork.instance.getCrop(7).getDrop().get(0),
                CropNetwork.instance.getCrop(7).getDrop().get(0)
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(8).getStack(),
                CropNetwork.instance.getCrop(8).getDrop().get(0),
                CropNetwork.instance.getCrop(8).getDrop().get(0)
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(9).getStack(),
                CropNetwork.instance.getCrop(9).getDrop().get(0),
                CropNetwork.instance.getCrop(9).getDrop().get(0)
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(10).getStack(),
                CropNetwork.instance.getCrop(10).getDrop().get(0),
                CropNetwork.instance.getCrop(10).getDrop().get(0)
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(11).getStack(),
                CropNetwork.instance.getCrop(11).getDrop().get(0),
                CropNetwork.instance.getCrop(11).getDrop().get(0)
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(12).getStack(),
                CropNetwork.instance.getCrop(12).getDrop().get(0),
                CropNetwork.instance.getCrop(12).getDrop().get(0)
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(13).getStack(),
                CropNetwork.instance.getCrop(13).getDrop().get(0),
                CropNetwork.instance.getCrop(13).getDrop().get(0)
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(14).getStack(),
                CropNetwork.instance.getCrop(14).getDrop().get(0),
                CropNetwork.instance.getCrop(14).getDrop().get(0)
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(15).getStack(),
                CropNetwork.instance.getCrop(15).getDrop().get(0),
                CropNetwork.instance.getCrop(15).getDrop().get(0)
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(18).getStack(),
                CropNetwork.instance.getCrop(18).getDrop().get(0),
                CropNetwork.instance.getCrop(18).getDrop().get(0)
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(19).getStack(),
                CropNetwork.instance.getCrop(19).getDrop().get(0),
                CropNetwork.instance.getCrop(19).getDrop().get(0)
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(23).getStack(),
                CropNetwork.instance.getCrop(23).getDrop().get(0),
                CropNetwork.instance.getCrop(23).getDrop().get(0)
        );
        Recipes.recipe.addShapelessRecipe(
                CropNetwork.instance.getCrop(24).getStack(),
                CropNetwork.instance.getCrop(24).getDrop().get(0),
                CropNetwork.instance.getCrop(24).getDrop().get(0)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crop, 4), "A A", "A A", "   ",

                ('A'), "stickWood"


        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 128), "DCD", "BAB", " E ",

                ('A'), DEFAULT_SENSOR,

                ('C'), "workbench",

                ('B'), "platePlatinum",

                ('D'),
                "plateTin",

                ('E'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 129), "DCD", "BAB", " E ",
                ('A'), ADV_SENSOR, ('C'), "workbench",
                ('B'), "platePlatinum", ('D'),
                "plateTin", ('E'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 130), "DCD", "BAB", " E ",
                ('A'), IMP_SENSOR, ('C'), "workbench",
                ('B'), "platePlatinum", ('D'),
                "plateTin", ('E'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 131), "DCD", "BAB", " E ",
                ('A'), PER_SENSOR, ('C'), "workbench",
                ('B'), "platePlatinum", ('D'),
                "plateTin", ('E'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 605), "DCD", "BAB", " E ",
                ('A'), PHOTON_SENSOR, ('C'), "workbench",
                ('B'), "platePlatinum", ('D'),
                "plateTin", ('E'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 154), "CCC", "BAB", "   ",

                ('A'), DEFAULT_SENSOR,

                ('C'), IUItem.FluidCell,

                ('B'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 155), "CCC", "BAB", "   ",

                ('A'), DEFAULT_SENSOR,

                ('C'),
                ModUtils.getCellFromFluid(FluidName.fluidcoolant.getInstance()),
                ('B'), "plateNickel"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 156), " C ", "BAB", " D ",
                ('A'), DEFAULT_SENSOR, ('B'), "plateGermanium",
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 60), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 11)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 158), "CDE", "BAB", "FFF",

                ('A'), DEFAULT_SENSOR,

                ('C'), Items.DIAMOND_AXE,

                ('D'), Items.DIAMOND_PICKAXE,

                ('E'),
                Items.DIAMOND_SHOVEL,

                ('B'), "gearIridium",

                ('F'), "gearMagnesium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 170), "DCD", "BAB", " C ",
                ('A'), DEFAULT_SENSOR, ('B'), Blocks.DAYLIGHT_DETECTOR,
                ('C'), IUItem.advancedMachine, ('D'),
                IUItem.reinforcedGlass
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 171), "DCD", "BAB", " C ",
                ('A'), ADV_SENSOR, ('B'), new ItemStack(IUItem.core, 1, 3),
                ('C'), IUItem.advancedMachine, ('D'),
                IUItem.reinforcedGlass
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 172), "DCD", "BAB", " C ",
                ('A'), IMP_SENSOR, ('B'), new ItemStack(IUItem.core, 1, 4),
                ('C'), IUItem.advancedMachine, ('D'),
                IUItem.reinforcedGlass
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 173), "DCD", "BAB", " C ",
                ('A'), PER_SENSOR, ('B'), new ItemStack(IUItem.core, 1, 5),
                ('C'), IUItem.advancedMachine, ('D'),
                IUItem.reinforcedGlass
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 635), "DCD", "BAB", " C ",
                ('A'), PHOTON_SENSOR, ('B'), new ItemStack(IUItem.core, 1, 5),
                ('C'), IUItem.advancedMachine, ('D'),
                IUItem.reinforcedGlass
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 90),
                "BBB",
                "DAD",
                "EEE",
                ('A'),
                DEFAULT_SENSOR,
                ('B'),
                "gearMikhail",
                ('D'),
                "gearPlatinum",
                ('C'),
                IUItem.carbonPlate,
                ('E'),
                "doubleplateRedbrass"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 79), "CCC", "BAB", "EDE",

                ('A'), DEFAULT_SENSOR,

                ('B'), IUItem.advancedAlloy,

                ('C'), IUItem.carbonPlate,

                ('D'),
                IUItem.toriy,

                ('E'), "plateBronze"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 176), "CCC", "BAB", "EDE",

                ('A'), ADV_SENSOR,

                ('B'), IUItem.advancedAlloy,

                ('C'), IUItem.iridiumPlate,

                ('D'),
                new ItemStack(IUItem.radiationresources, 1, 4),

                ('E'), "plateBronze"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 174), "CCC", "BAB", "EDE",

                ('A'), IMP_SENSOR,

                ('B'), IUItem.advancedAlloy,

                ('C'), IUItem.iridiumPlate,

                ('D'),
                new ItemStack(IUItem.radiationresources, 1, 4),

                ('E'), "plateBronze"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 175), "CCC", "BAB", "EDE",

                ('A'), PER_SENSOR,

                ('B'), IUItem.advancedAlloy,

                ('C'), IUItem.iridiumPlate,

                ('D'),
                new ItemStack(IUItem.radiationresources, 1, 4),

                ('E'), "plateBronze"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 627), "CCC", "BAB", "EDE",

                ('A'), PHOTON_SENSOR,

                ('B'), IUItem.advancedAlloy,

                ('C'), IUItem.iridiumPlate,

                ('D'),
                new ItemStack(IUItem.radiationresources, 1, 4),

                ('E'), "plateBronze"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 217), " C ", "BAB", "DDD",

                ('A'), DEFAULT_SENSOR,

                ('B'), "plateAlumel",

                ('C'), Items.FISHING_ROD,

                ('D'),
                "gearMuntsa"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 219), " C ", "BAB", "DDD",
                ('A'), DEFAULT_SENSOR, ('B'), "plateIron", ('D'), Items.REDSTONE,
                ('C'),
                Blocks.FURNACE
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 225), " C ", "BAB", "DDD",
                ('A'), ADV_SENSOR, ('B'), "plateIron", ('D'), Items.REDSTONE,
                ('C'),
                Blocks.FURNACE
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 221), " C ", "BAB", "DDD",
                ('A'), IMP_SENSOR, ('B'), "plateIron", ('D'), Items.REDSTONE,
                ('C'),
                Blocks.FURNACE
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 223), " C ", "BAB", "DDD",
                ('A'), PER_SENSOR, ('B'), "plateIron", ('D'), Items.REDSTONE,
                ('C'),
                Blocks.FURNACE
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 617), " C ", "BAB", "DDD",
                ('A'), PHOTON_SENSOR, ('B'), "plateIron", ('D'), Items.REDSTONE,
                ('C'),
                Blocks.FURNACE
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 218), "CCC", "BAB", "DDD",
                ('A'), DEFAULT_SENSOR, ('B'), "plateIron", ('C'), "plateTin",
                ('D'),
                "plateCopper"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 224), "CCC", "BAB", "DDD",
                ('A'), ADV_SENSOR, ('B'), "plateIron", ('C'), "plateTin",
                ('D'),
                "plateCopper"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 220), "CCC", "BAB", "DDD",
                ('A'), IMP_SENSOR, ('B'), "plateIron", ('C'), "plateTin",
                ('D'),
                "plateCopper"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 222), "CCC", "BAB", "DDD",
                ('A'), PER_SENSOR, ('B'), "plateIron", ('C'), "plateTin",
                ('D'),
                "plateCopper"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 606), "CCC", "BAB", "DDD",
                ('A'), PHOTON_SENSOR, ('B'), "plateIron", ('C'), "plateTin",
                ('D'),
                "plateCopper"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 205), "CCC", "BAB", "DED",
                ('A'), DEFAULT_SENSOR,
                ('B'), "gearCobalt", ('C'), IUItem.advancedAlloy, ('D'),
                "plateTitanium", 'E', IUItem.plastic_plate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 204), "CCC", "BAB", "DED",
                ('A'), ADV_SENSOR,
                ('B'), "gearCobalt", ('C'), IUItem.advancedAlloy, ('D'),
                "plateTitanium", 'E', IUItem.plastic_plate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 202), "CCC", "BAB", "DED",
                ('A'), IMP_SENSOR,
                ('B'), "gearCobalt", ('C'), IUItem.advancedAlloy, ('D'),
                "plateTitanium", 'E', IUItem.plastic_plate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 203), "CCC", "BAB", "DED",
                ('A'), PER_SENSOR,
                ('B'), "gearCobalt", ('C'), IUItem.advancedAlloy, ('D'),
                "plateTitanium", 'E', IUItem.plastic_plate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 633), "CCC", "BAB", "DED",
                ('A'), PHOTON_SENSOR,
                ('B'), "gearCobalt", ('C'), IUItem.advancedAlloy, ('D'),
                "plateTitanium", 'E', IUItem.plastic_plate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 38), "CCC", "BAB", "DDD",

                ('A'), DEFAULT_SENSOR,

                ('B'), "doubleplateGermanium",

                ('C'), "gearVitalium",

                ('D'),
                "gearAlcled"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 14), "CCC", "BAB", "DDD",
                ('A'), ADV_SENSOR, ('B'), "doubleplateGermanium",
                ('C'), "gearVitalium", ('D'),
                "gearAlcled"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 89), "CCC", "BAB", "DDD",
                ('A'), IMP_SENSOR, ('B'), "doubleplateGermanium",
                ('C'), "gearVitalium", ('D'),
                "gearAlcled"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 114), "CCC", "BAB", "DDD",

                ('A'), PER_SENSOR,

                ('B'), "doubleplateGermanium",

                ('C'), "gearVitalium",

                ('D'),
                "gearAlcled"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.universal_cable, 1, 1), " A ", "BCB", " A ",

                ('C'),
                ("ingotCobalt"),
                ('A'), new ItemStack(IUItem.universal_cable, 1, 0),

                ('B'), IUItem.denseplatetin
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.universal_cable, 1, 2), " A ", "BCB", " A ",

                ('C'), IUItem.denseplatetin,

                ('A'), new ItemStack(IUItem.universal_cable, 1, 1),

                ('B'), IUItem.advancedAlloy
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.universal_cable, 1, 3), "DAD", "BCB", "DAD",

                ('D'), IUItem.denseplategold,

                ('C'), IUItem.advancedAlloy,

                ('A'), new ItemStack(IUItem.universal_cable, 1, 2),

                ('B'),
                IUItem.denseplatelead
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.universal_cable, 1, 4), "DAD", "BCB", "DAD",

                ('D'),
                ("ingotRedbrass"),
                ('C'), IUItem.carbonPlate,

                ('A'), new ItemStack(IUItem.universal_cable, 1, 3),

                ('B'),
                ("ingotSpinel")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.universal_cable, 1, 5), " A ", "BCB", " A ",

                ('C'),
                ("doubleplateVitalium"),
                ('A'), new ItemStack(IUItem.universal_cable, 1, 4),

                ('B'), IUItem.denseplateadviron
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.universal_cable, 1, 6), "DAD", "BCB", "DAD",

                ('D'), IUItem.carbonPlate,

                ('C'),
                ("ingotAlcled"),
                ('A'), new ItemStack(IUItem.universal_cable, 1, 5),

                ('B'),
                ("ingotDuralumin")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.universal_cable, 1, 7), "A A", "BCB", "A A",

                ('C'), new ItemStack(IUItem.photoniy_ingot),

                ('B'), new ItemStack(IUItem.photoniy),

                ('A'), new ItemStack(IUItem.universal_cable, 1, 6)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.universal_cable, 1, 8), "BBB", "AAA", "BBB",
                ('A'), new ItemStack(IUItem.photoniy_ingot), ('B'), new ItemStack(
                        IUItem.universal_cable,
                        1,
                        7
                )
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.universal_cable, 1, 9), "BBB", "ACA", "BBB",

                ('C'), new ItemStack(IUItem.basecircuit, 1, 10),

                ('A'), new ItemStack(IUItem.photoniy_ingot),

                ('B'), new ItemStack(IUItem.universal_cable, 1, 8)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.cokeoven, 1, 1), "CAC", "ABA", "CAC",

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 44),
                ('A'), new ItemStack(IUItem.cokeoven, 1, 5),
                'C', "gearInvar"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cokeoven, 1, 4), " A ", "ABA", " A ",

                ('B'),
                getBlockStack(BlockBaseMachine3.steel_tank),
                ('A'), new ItemStack(IUItem.cokeoven, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cokeoven, 1, 3), "CAC", "ABA", "CAC",

                ('B'), getBlockStack(BlockBaseMachine3.steel_tank),
                ('A'), new ItemStack(IUItem.cokeoven, 1, 5),
                'C', "gearElectrum"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.recipe_schedule), "AB ", "BAB", "BBA",

                ('B'), "plateAluminiumSilicon",
                ('A'), "plateInvar"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cokeoven, 1, 5), " B ", "CAC", " B ",

                ('C'), "casingAluminiumSilicon",

                ('A'), new ItemStack(Blocks.BRICK_BLOCK),

                ('B'), "casingHafniumCarbide"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cokeoven, 1, 2), " A ", " B ", "   ",

                ('C'), "plateAluminiumSilicon",

                ('A'), new ItemStack(IUItem.primalFluidHeater),

                ('B'), new ItemStack(IUItem.cokeoven, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cokeoven, 1, 0), "DED", "CAC", " B ",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 387),

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),

                ('B'), new ItemStack(IUItem.cokeoven, 1, 5),
                'D', new ItemStack(IUItem.crafting_elements, 1, 320),
                'E', new ItemStack(IUItem.itemiu, 1, 0)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 190), " ", "ABC", " D ",

                'A', new ItemStack(IUItem.crafting_elements, 1, 36),
                'B', new ItemStack(IUItem.blockResource, 1, 8),
                'C', new ItemStack(IUItem.crafting_elements, 1, 441),
                'D', new ItemStack(IUItem.motors_with_improved_bearings_)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.antiairpollution), "CCC", "ABA", " D ",

                'A', IUItem.polonium_palladium_composite,
                'B', new ItemStack(IUItem.module_schedule),
                'C', new ItemStack(IUItem.alloysplate, 1, 5),
                'D', new ItemStack(IUItem.crafting_elements, 1, 20)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.antisoilpollution), "CCC", "ABA", " D ",

                'A', IUItem.polonium_palladium_composite,
                'B', new ItemStack(IUItem.module_schedule),
                'C', new ItemStack(IUItem.alloysplate, 1, 11),
                'D', new ItemStack(IUItem.crafting_elements, 1, 20)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.antiairpollution1), "CCC", "ABA", "ADA",

                'A', IUItem.polonium_palladium_composite,
                'B', new ItemStack(IUItem.module_schedule),
                'C', new ItemStack(IUItem.alloysdoubleplate, 1, 5),
                'D', new ItemStack(IUItem.crafting_elements, 1, 96)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.antisoilpollution1), "CCC", "ABA", "ADA",

                'A', IUItem.polonium_palladium_composite,
                'B', new ItemStack(IUItem.module_schedule),
                'C', new ItemStack(IUItem.alloysdoubleplate, 1, 11),
                'D', new ItemStack(IUItem.crafting_elements, 1, 96)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 191), "   ", "ABC", " D ",

                'A', new ItemStack(IUItem.crafting_elements, 1, 36),
                'B', new ItemStack(IUItem.blockResource, 1, 9),
                'C', new ItemStack(IUItem.crafting_elements, 1, 441),
                'D', IUItem.motors_with_improved_bearings_,
                'E', new ItemStack(IUItem.module7, 1, 9)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 139), " B ", " A ", " C ",

                'A', new ItemStack(IUItem.oiladvrefiner),
                'B', new ItemStack(IUItem.crafting_elements, 1, 86),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96),
                'D', IUItem.motors_with_improved_bearings_,
                'E', new ItemStack(IUItem.module7, 1, 9)
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 569), "AAA", "BDB", "AAA",

                'B', new ItemStack(IUItem.crafting_elements, 1, 568), 'A', "plateIron", 'C', "plateZinc", 'D',
                new ItemStack(IUItem.crafting_elements, 1, 581)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 600), "AAA", "BDB", "AAA",

                'B', new ItemStack(IUItem.crafting_elements, 1, 567), 'A', "plateElectrum", 'C', "plateZinc", 'D',
                new ItemStack(IUItem.crafting_elements, 1, 579)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 591), "AAA", "BDB", "AAA",

                'B', new ItemStack(IUItem.crafting_elements, 1, 565), 'A', "platePlatinum", 'C', "plateZinc", 'D',
                new ItemStack(IUItem.crafting_elements, 1, 563)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 561), "AAA", "BDB", "AAA",

                'B', new ItemStack(IUItem.crafting_elements, 1, 565), 'A', "plateHafniumBoride", 'C', "plateZinc", 'D',
                new ItemStack(IUItem.crafting_elements, 1, 585)
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 570), "A ", "BAB", "BBB",

                'B', "plateIron", 'A', new ItemStack(IUItem.iudust, 1, 60)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 573), "A ", "BAB", "BBB",

                'B', "plateGold", 'A', new ItemStack(IUItem.iudust, 1, 60)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 590), "A ", "BAB", "BBB",

                'B', "platePlatinum", 'A', "plateNeodymium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 559), "A ", "BAB", "BBB",

                'B', "gemDiamond", 'A', "plateNichrome"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 576), "ACA", "BCB", "ACA",

                'B', "plateCarbon", 'A', "plateBronze", 'C', "dustRedstone"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 566), "ACA", "BCB", "ACA",

                'B', "plateOsmium", 'A', "plateHafniumCarbide", 'C', "dustRedstone"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 584), "ACA", "BCB", "ACA",

                'B', "plateNiobium", 'A', "plateMolybdenumSteel", 'C', "dustRedstone"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 564), "ACA", "BCB", "ACA",

                'B', "plateBerylliumBronze", 'A', "plateWoods", 'C', "dustRedstone"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 572), "AAA", "ABA", "AAA",

                'B', "plateTin", 'A', "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 577), "CAC", "ABA", "CAC",

                'B', new ItemStack(IUItem.crafting_elements, 1, 572), 'A', "plateCobalt", 'C', "plateChromium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 575), "AAA", "ABA", "AAA",

                'B', new ItemStack(IUItem.crafting_elements, 1, 577), 'A', "plateYttriumAluminiumGarnet", 'C', "plateChromium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 587), "AAA", "ABA", "AAA",

                'B', new ItemStack(IUItem.crafting_elements, 1, 575), 'A', "plateInconel", 'C', "plateChromium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 580), "ABC", "BBC", "ABC",

                'B', "plateCopper", 'A', "ingotChromium", 'C', new ItemStack(IUItem.cable, 1, 11)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 560), "ABC", "BBC", "ABC",

                'B', "plateAluminumbronze", 'A', "ingotChromium", 'C', new ItemStack(IUItem.cable, 1, 11)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 589), "ABC", "BBC", "ABC",

                'B', "platePermalloy", 'A', "ingotChromium", 'C', new ItemStack(IUItem.cable, 1, 11)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 596), "ABC", "BBC", "ABC",

                'B', "plateStellite", 'A', "ingotChromium", 'C', new ItemStack(IUItem.cable, 1, 11)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 571), " BB", " BB", "A  ",

                'B', "plateIron", 'A', "ingotIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 562), " BB", " BB", "A  ",

                'B', "plateBronze", 'A', "ingotIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 595), " BB", " BB", "A  ",

                'B', "plateSteel", 'A', "ingotIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 586), " BB", " BB", "A  ",

                'B', "plateStainlessSteel", 'A', "ingotIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 508), " B ", "BBB", "BBB",

                'B', "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 517), " B ", "BBB", "ACA",

                'B', "plateCarbon", 'C', new ItemStack(IUItem.crafting_elements, 1, 508), 'A', IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 509), " B ", "BBB", "ACA",

                'B', new ItemStack(IUItem.crafting_elements, 1, 285), 'C', new ItemStack(IUItem.crafting_elements, 1, 517), 'A',
                IUItem.quantumtool
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 525), " B ", "BBB", "ACA",

                'B', "plateSuperalloyHaynes", 'C', new ItemStack(IUItem.crafting_elements, 1, 509), 'A', IUItem.spectral_box
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 518), "  A", " AB", "ABB",

                'B', "plateCarbon", 'A', "plateRedbrass"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 510), "  A", " AB", "ABB",

                'B', "plateStainlessSteel", 'A', "plateNiobiumTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 526), "  A", " AB", "ABB",

                'B', "plateAluminiumLithium", 'A', "plateAlcled"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 521), "A  ", "BA ", "BBA",

                'B', "plateCarbon", 'A', "plateRedbrass"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 513), "A  ", "BA ", "BBA",

                'B', "plateStainlessSteel", 'A', "plateNiobiumTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 529), "A  ", "BA ", "BBA",

                'B', "plateAluminiumLithium", 'A', "plateAlcled"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 522), "BA ", " BA", "   ",

                'B', "plateCarbon", 'A', "plateRedbrass"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 514), "BA ", " BA", "   ",

                'B', "plateStainlessSteel", 'A', "plateNiobiumTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 530), "BA ", " BA", "   ",

                'B', "plateAluminiumLithium", 'A', "plateAlcled"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 519), " AB", "AB ", "   ",

                'B', "plateCarbon", 'A', "plateRedbrass"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 511), " AB", "AB ", "   ",

                'B', "plateStainlessSteel", 'A', "plateNiobiumTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 527), " AB", "AB ", "   ",

                'B', "plateAluminiumLithium", 'A', "plateAlcled"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 520), " AB", "ABB", "AB ",

                'B', "plateCarbon", 'A', "plateRedbrass"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 512), " AB", "ABB", "AB ",

                'B', "plateStainlessSteel", 'A', "plateNiobiumTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 528), " AB", "ABB", "AB ",

                'B', "plateAluminiumLithium", 'A', "plateAlcled"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 523), "BA ", "BBA", " BA",

                'B', "plateCarbon", 'A', "plateRedbrass"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 515), "BA ", "BBA", " BA",

                'B', "plateStainlessSteel", 'A', "plateNiobiumTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 531), "BA ", "BBA", " BA",

                'B', "plateAluminiumLithium", 'A', "plateAlcled"
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 524),
                "CBC",
                "BAB",
                "CDC",

                'B',
                "plateCarbon",
                'C',
                "plateRedbrass",
                'A',
                IUItem.energy_crystal,
                'D',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 516),
                "CBC",
                "BAB",
                "CDC",

                'B',
                "plateStainlessSteel",
                'C',
                "plateNiobiumTitanium",
                'A',
                IUItem.lapotron_crystal,
                'D',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 532),
                "CBC",
                "BAB",
                "CDC",

                'B',
                "plateAluminiumLithium",
                'C',
                "plateAlcled",
                'A',
                IUItem.AdvlapotronCrystal,
                'D',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 460), "AAA", "AAA", "AAA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 463)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 454), "AAA", "AAA", "AAA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 461)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 456), "AAA", "AAA", "AAA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 462)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nuclear_res, 1, 10), "AAA", "AAA", "AAA",
                'A', new ItemStack(IUItem.nuclear_res, 1, 13)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nuclear_res, 1, 17), " B ", "BAB", " B ",
                'A', new ItemStack(IUItem.nuclear_res, 1, 10), 'B', IUItem.stoneDust
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nuclear_res, 1, 10), "AAA", "AAA", "AAA",
                'A', new ItemStack(IUItem.nuclear_res, 1, 13)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nuclear_res, 1, 17), " B ", "BAB", " B ",
                'A', new ItemStack(IUItem.nuclear_res, 1, 10), 'B', IUItem.stoneDust
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nuclear_res, 1, 11), "AAA", "AAA", "AAA",
                'A', new ItemStack(IUItem.nuclear_res, 1, 14)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nuclear_res, 1, 18), " B ", "BAB", " B ",
                'A', new ItemStack(IUItem.nuclear_res, 1, 11), 'B', IUItem.stoneDust
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nuclear_res, 1, 12), "AAA", "AAA", "AAA",
                'A', new ItemStack(IUItem.nuclear_res, 1, 15)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nuclear_res, 1, 20), " B ", "BAB", " B ",
                'A', new ItemStack(IUItem.nuclear_res, 1, 12), 'B', IUItem.stoneDust
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nuclear_res, 1, 19), "AAA", "AAA", "AAA",
                'A', new ItemStack(IUItem.nuclear_res, 1, 16)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.humus), "AAA", "ABA", "AAA",
                'A', new ItemStack(Blocks.DIRT), 'B', IUItem.apatite_cube
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basecircuit, 1, 19), "AAA", "ABA", "AAA",
                'B', new ItemStack(IUItem.basecircuit, 1, 18), 'A', "stickArsenic"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 489), "DCC", "CBB", "AA ",
                'B', "plateZirconium", 'A', "plateCadmium", 'C', "plateFerromanganese", 'D', "plateNiobium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 491), " C ", "BAB", " C ",
                'A', new ItemStack(IUItem.crafting_elements, 1, 488), 'B', "plateGadolinium", 'C', "plateBarium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 486), " C ", "BAB", " C ",
                'A', new ItemStack(IUItem.crafting_elements, 1, 491), 'B', "plateRedbrass", 'C', "plateDuralumin"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.ObsidianForgeHammer), "ABA", "ACA", " C ",
                'A', "plateObsidian", 'B', "plateSteel", 'C', "stickWood"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 485), " C ", "BAB", " C ",
                'A', new ItemStack(IUItem.crafting_elements, 1, 486), 'B', "plateTantalumTungstenHafnium", 'C',
                "plateOsmiridium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 490), " C ", "BAB", " C ",
                'A', new ItemStack(IUItem.crafting_elements, 1, 485), 'B', "plateZeliber", 'C',
                "plateNitenol"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 588), "CCB", "CAA", "CCB",
                'A', "plateThallium", 'B', "plateStrontium", 'C',
                "plateNiobium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 598), " CB", "CDA", " CB",
                'A', "plateNitenol", 'B', new ItemStack(IUItem.crafting_elements, 1, 581), 'C',
                new ItemStack(IUItem.crafting_elements, 1, 568), 'D', new ItemStack(IUItem.crafting_elements, 1, 588)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 592),
                "ECB",
                "CDA",
                "ECB",
                'A',
                "plateBerylliumBronze",
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 563),
                'E',
                "plateTantalumTungstenHafnium",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 565),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 598)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 594), "ECB", "CDA", "ECB",
                'A', "plateInconel", 'B', new ItemStack(IUItem.crafting_elements, 1, 585), 'E', "plateStainlessSteel",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 565), 'D', new ItemStack(IUItem.crafting_elements, 1, 592)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blockMacerator), "C C", "BAB", "BCB",
                'A', new ItemStack(IUItem.crafting_elements, 1, 41), 'B', "plateFerromanganese", 'E', "plateStainlessSteel",
                'C',
                "plateInvar", 'D', new ItemStack(IUItem.crafting_elements, 1, 592)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blockCompressor), "C C", "BAB", "BCB",
                'A', new ItemStack(IUItem.crafting_elements, 1, 76), 'B', "plateFerromanganese", 'E', "plateStainlessSteel",
                'C',
                "plankWood", 'D', new ItemStack(IUItem.crafting_elements, 1, 76)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.dryer), "CBC", "CBC", "CBC",
                'A', new ItemStack(IUItem.crafting_elements, 1, 76), 'B', "blockGlass", 'E', "plateStainlessSteel",
                'C',
                "plankWood", 'D', new ItemStack(IUItem.crafting_elements, 1, 76)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.squeezer), "CEC", "CBC", "CDC",
                'A', new ItemStack(IUItem.crafting_elements, 1, 76), 'B', "blockGlass", 'E', IUItem.treetap,
                'C',
                "plankWood", 'D', new ItemStack(IUItem.crafting_elements, 1, 601)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.fluidIntegrator), "A A", "ACA", "EDE",
                'A', "plateElectrum", 'B', "blockGlass", 'E', "plateFerromanganese",
                'C',
                "plateInvar", 'D', new ItemStack(IUItem.crafting_elements, 1, 601)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.primalFluidHeater), "BCB", "BCB", "AAA",
                'A', "plateAluminumbronze", 'C', new ItemStack(IUItem.crafting_elements, 1, 601), 'B', "plateBronze"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.primalSiliconCrystal), "AAA", "ABA", "AAA",
                'A', "plateIron", 'B', "blockGlass"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.primalPolisher), " B ", "ADA", "ACA",
                'A', "plateTitanium", 'B', "blockGlass", 'D', "plateIron", 'C', new ItemStack(IUItem.crafting_elements, 1, 354)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.programming_table), " B ", "ADA", "ACA",
                'A', "plateTitanium", 'B', "blockGlass", 'D', "plateCarbon", 'C', new ItemStack(IUItem.crafting_elements, 1, 354)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.solderingMechanism), " B ", "ADA", "ACA",
                'A', "plateTitanium", 'B', "blockGlass", 'D', "plateIron", 'C', new ItemStack(IUItem.solderingIron)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electronics_assembler), " B ", "ADA", "ACA",
                'A', "plateTitanium", 'B', "blockGlass", 'D', "plateIron", 'C', new ItemStack(IUItem.basecircuit, 1, 17)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.primal_pump), "   ", "DAD", "DCD",
                'A', "blockGlass", 'D', "plateIron", 'C', new ItemStack(IUItem.basemachine2, 1, 185)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 3, 478), "   ", "DAD", "D D",
                'A', "dyeYellow", 'D', IUItem.rubber
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radioprotector), "BBB", " A ", "A  ",
                'A', "plateAluminiumSilicon", 'B', "plateNitenol"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.steamPipe, 6), "BBB", "AAA", "BBB",
                'A', "platePolonium", 'B', "casingAluminumbronze"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.biopipes, 6), "BBB", "AAA", "BBB",
                'A', "plateTantalum", 'B', "casingThallium"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_cathode), " A ", "ABA", "CCC",
                'A', "plateTantalumTungstenHafnium", 'B', IUItem.cathode, 'C', new ItemStack(IUItem.iudust, 1, 63)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_anode), " A ", "ABA", "CCC",
                'A', "plateOsmiridium", 'B', IUItem.anode, 'C', new ItemStack(IUItem.iudust, 1, 63)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.ironMesh), " BB", " AA", " BB",
                'A', "string", 'B', "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.steelMesh), " BB", " BB", " BA",
                'A', IUItem.ironMesh, 'B', "plateSteel"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.boridehafniumMesh), " BB", " BB", " BA",
                'A', IUItem.steelMesh, 'B', "plateHafniumBoride"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.vanadiumaluminumMesh), " BB", " BB", " BA",
                'A', IUItem.boridehafniumMesh, 'B', "plateVanadoalumite"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.steleticMesh), " BB", " BB", " BA",
                'A', IUItem.vanadiumaluminumMesh, 'B', "plateStellite"
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.gasSensor),
                "DCD",
                "BAB",
                "  ",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),
                'B',
                "platePolonium",
                'C',
                "plateZirconium",
                'D',
                "plateNiobiumTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.reactorData), "DCD", "BAB", "  ",
                'A', TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7), 'B', "plateNimonic", 'C', "plateWoods",
                'D', "plateZeliber"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 32), "CBC", "BAB", "EDE", 'A', DEFAULT_SENSOR,
                'B', "gearInvar", 'C', "gemSapphire", 'D', "gearFerromanganese", 'E', "plateFerromanganese"

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 123), "CBC", "BAB", "EDE", 'A', DEFAULT_SENSOR,
                'B', "gearElectrum", 'C', "gemTopaz", 'D', "gearAluminumbronze", 'E', "plateAluminumbronze"

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 163), " A ", " B ", " C ", 'A',
                new ItemStack(IUItem.crafting_elements, 1, 32),
                'B', IUItem.primalFluidHeater, 'C', new ItemStack(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 164), " A ", "DBC", "   ", 'A',
                new ItemStack(IUItem.crafting_elements, 1, 32),
                'B', new ItemStack(IUItem.basemachine2, 1, 163), 'C', new ItemStack(IUItem.crafting_elements, 1, 63)
                , 'D', new ItemStack(IUItem.crafting_elements, 1, 123)

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 166), "DAE", " B ", " C ", 'A',
                new ItemStack(IUItem.crafting_elements, 1, 32), 'D', new ItemStack(IUItem.crafting_elements, 1, 123), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 69),
                'B', IUItem.blockMacerator, 'C', new ItemStack(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 167), "DAE", " B ", " C ", 'A',
                new ItemStack(IUItem.crafting_elements, 1, 32), 'D', new ItemStack(IUItem.crafting_elements, 1, 123), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 63),
                'B', IUItem.blockCompressor, 'C', new ItemStack(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 168), "DAE", " B ", " C ", 'A',
                new ItemStack(IUItem.crafting_elements, 1, 32), 'D', new ItemStack(IUItem.crafting_elements, 1, 123), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 159),
                'B', IUItem.squeezer, 'C', new ItemStack(IUItem.blockResource, 1, 12)

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 189), "ABC", "DE ", " G ", 'A',
                new ItemStack(IUItem.crafting_elements, 1, 230), 'B', new ItemStack(IUItem.crafting_elements, 1, 61), 'C',
                new ItemStack(IUItem.crafting_elements, 1, 231),
                'D', new ItemStack(IUItem.crafting_elements, 1, 47), 'E', new ItemStack(IUItem.blockResource, 1, 8), 'G',
                new ItemStack(IUItem.crafting_elements, 1, 276)

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 169), "DAE", " B ", " C ", 'A',
                new ItemStack(IUItem.crafting_elements, 1, 32), 'D', new ItemStack(IUItem.crafting_elements, 1, 123), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 163),
                'B', IUItem.anvil, 'C', new ItemStack(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 170), "DAE", " B ", " C ", 'A',
                new ItemStack(IUItem.crafting_elements, 1, 32), 'D', new ItemStack(IUItem.crafting_elements, 1, 123), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 132),
                'B', new ItemStack(IUItem.basemachine2, 1, 124), 'C', new ItemStack(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 188), "D A", " B ", " C ", 'A',
                new ItemStack(IUItem.crafting_elements, 1, 32), 'D', new ItemStack(IUItem.crafting_elements, 1, 123), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 132),
                'B', IUItem.primalPolisher, 'C', new ItemStack(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 181), "DEA", " B ", " C ", 'A',
                new ItemStack(IUItem.crafting_elements, 1, 32), 'D', new ItemStack(IUItem.crafting_elements, 1, 123), 'E',
                new ItemStack(IUItem.advBattery),
                'B', getBlockStack(BlockBaseMachine3.oak_tank), 'C', new ItemStack(IUItem.blockResource, 1, 12)

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 180), "DEA", "BBB", "FCF", 'A',
                new ItemStack(IUItem.crafting_elements, 1, 32), 'D', new ItemStack(IUItem.crafting_elements, 1, 123), 'E',
                new ItemStack(IUItem.advBattery), 'F', new ItemStack(IUItem.alloysplate, 1, 31),
                'B', getBlockStack(BlockBaseMachine3.oak_tank), 'C', new ItemStack(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 187), "DAE", " B ", " C ", 'A',
                new ItemStack(IUItem.crafting_elements, 1, 32), 'D', new ItemStack(IUItem.crafting_elements, 1, 123), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 241),
                'B', IUItem.primal_pump, 'C', new ItemStack(IUItem.blockResource, 1, 12)

        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 267), "ECE", "ABA", "ECE", 'B',
                DEFAULT_SENSOR, 'A', "doubleplateYttrium", 'C', "doubleplateStrontium", 'E', "plateNiobium"

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 182), "DAE", "BBB", " C ", 'A',
                new ItemStack(IUItem.crafting_elements, 1, 32), 'D', new ItemStack(IUItem.crafting_elements, 1, 123), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 267),
                'B', new ItemStack(IUItem.crafting_elements, 1, 601), 'C', new ItemStack(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 165), "D A", " B ", " C ", 'A',
                new ItemStack(IUItem.crafting_elements, 1, 32), 'D', new ItemStack(IUItem.crafting_elements, 1, 123), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 132),
                'B', IUItem.dryer, 'C', new ItemStack(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 172), "D A", " B ", " C ", 'A',
                new ItemStack(IUItem.crafting_elements, 1, 32), 'D', new ItemStack(IUItem.crafting_elements, 1, 123), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 132),
                'B', IUItem.squeezer, 'C', new ItemStack(IUItem.blockResource, 1, 12)

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 171), "AAA", "ABA", " C ", 'A',
                "plateSteel", 'C', getBlockStack(BlockBaseMachine3.steel_tank),
                'B', new ItemStack(IUItem.basemachine2, 1, 163)

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 178), "AAA", "ABA", "CCC", 'A',
                new ItemStack(IUItem.crafting_elements, 1, 501), 'C', IUItem.cultivated_peat_balls,
                'B', new ItemStack(IUItem.basemachine2, 1, 171)

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 179), "DAE", "FBF", "HCH", 'A',
                new ItemStack(IUItem.crafting_elements, 1, 32), 'D', new ItemStack(IUItem.crafting_elements, 1, 123), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 219), 'F', "plateTitanium", 'H', "plateBismuth",
                'B', new ItemStack(IUItem.crafting_elements, 1, 51), 'C', new ItemStack(IUItem.blockResource, 1, 12)

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.proton_energy_coupler), "ABA", "BCB", "ABA", 'A',
                new ItemStack(IUItem.per_heat_exchange), 'B', new ItemStack(IUItem.per_Vent), 'C',
                new ItemStack(IUItem.proton)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_proton_energy_coupler), "ABA", "BCB", "ABA", 'A',
                new ItemStack(IUItem.proton), 'B', new ItemStack(IUItem.radiationresources, 1, 2), 'C',
                new ItemStack(IUItem.proton_energy_coupler)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.imp_proton_energy_coupler), "ABA", "BCB", "ABA", 'A',
                new ItemStack(IUItem.advQuantumtool), 'B', "doubleplateNimonic", 'C',
                new ItemStack(IUItem.adv_proton_energy_coupler)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.per_proton_energy_coupler), "ABA", "BCB", "ABA", 'A',
                new ItemStack(IUItem.adv_spectral_box), 'B', "doubleplateSuperalloyHaynes", 'C',
                new ItemStack(IUItem.imp_proton_energy_coupler)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.neutron_protector), "ABA", "BCB", "ABA", 'A',
                new ItemStack(IUItem.proton_energy_coupler), 'B', IUItem.iridiumPlate, 'C',
                new ItemStack(IUItem.neutroniumingot)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_neutron_protector), "ABA", "BCB", "ABA", 'A',
                new ItemStack(IUItem.neutroniumingot), 'B', new ItemStack(IUItem.radiationresources, 1, 3), 'C',
                new ItemStack(IUItem.neutron_protector)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.imp_neutron_protector), "ABA", "BCB", "ABA", 'A',
                new ItemStack(IUItem.advQuantumtool), 'B', "doubleplateNimonic", 'C',
                new ItemStack(IUItem.adv_neutron_protector)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.per_neutron_protector), "ABA", "BCB", "ABA", 'A',
                new ItemStack(IUItem.adv_spectral_box), 'B', "doubleplateSuperalloyRene", 'C',
                new ItemStack(IUItem.imp_neutron_protector)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.upgrade_casing), "ABA", "BCB", "ABA", 'A',
                new ItemStack(IUItem.graphene_plate), 'B', "plateSteel", 'C',
                new ItemStack(IUItem.crafting_elements, 1, 479)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 143), "CBC", "BAB", "CBC", 'A',
                new ItemStack(IUItem.module_schedule), 'B', "plateAluminiumLithium", 'C', "plateTantalumTungstenHafnium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 656),
                "EEE",
                "BAB",
                "CDC",
                'A',
                DEFAULT_SENSOR,
                'B',
                "plateBeryllium",
                'C',
                "plateTantalumTungstenHafnium",
                'D',
                "gearCobaltChrome",
                'E',
                "casingBismuth"
        );
        BasicRecipeTwo.recipe();
    }

    public static ItemStack getBlockStack(IMultiTileBlock block) {
        return TileBlockCreator.instance.get(block.getIDBlock()).getItemStack(block);
    }

}
