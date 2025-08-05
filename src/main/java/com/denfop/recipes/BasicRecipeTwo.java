package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.tiles.mechanism.TileEntityUpgradeMachineFactory;
import com.denfop.tiles.mechanism.TileGenerationMicrochip;
import com.denfop.utils.ModUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class BasicRecipeTwo {

    public static Item DEFAULT_SENSOR;

    public static Item ADV_SENSOR;

    public static Item IMP_SENSOR;

    public static Item PER_SENSOR;
    public static Item PHOTON_SENSOR;

    public static void recipe() {
        DEFAULT_SENSOR = IUItem.crafting_elements.getItemFromMeta(21);

        ADV_SENSOR = IUItem.crafting_elements.getItemFromMeta(25);

        IMP_SENSOR = IUItem.crafting_elements.getItemFromMeta(23);

        PER_SENSOR = IUItem.crafting_elements.getItemFromMeta(24);

        PHOTON_SENSOR = IUItem.crafting_elements.getItemFromMeta(620);
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 1), "BBB", "CAC", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 122),

                ('B'), "c:plates/StainlessSteel",

                ('C'), ItemStackHelper.fromData(Items.LAPIS_LAZULI)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 4), "BBB", " A ", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 122),

                ('B'), "c:plates/StainlessSteel",

                ('C'), ItemStackHelper.fromData(Items.LAPIS_LAZULI)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 2), "BBB", "CAC", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 122),

                ('B'), "c:plates/Stellite",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 3), "BBB", "CAC", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 122),

                ('B'), "c:plates/Stellite",

                ('C'), ItemStackHelper.fromData(Items.LAPIS_LAZULI)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 5), "BBB", " A ", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 122),

                ('B'), "c:plates/Stellite",

                ('C'), ItemStackHelper.fromData(Items.LAPIS_LAZULI)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.simplemachine, 1, 0), " B ", "DAE", "C H",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72), ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 69), 'H', getBlockStack(BlockBaseMachine3.steam_macerator)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.simplemachine, 1, 1), " B ", "DAE", "C H",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72), ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 63), 'H', getBlockStack(BlockBaseMachine3.steam_compressor)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.simplemachine, 1, 2), "B H", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72), ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('B'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        219
                ), ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 35)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.simplemachine, 1, 3), " B ", "DAE", "C H",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72), ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 159), 'H', getBlockStack(BlockBaseMachine3.steam_extractor)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.simplemachine, 1, 5), " B ", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72), ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 33)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines_base2, 1, 0), " B ", "DAE", "C H",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72), ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 165), 'H', getBlockStack(BlockBaseMachine3.rolling_machine)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines_base2, 1, 4), " B ", "DAE", "C H",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72), ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 163), 'H', getBlockStack(BlockBaseMachine3.steam_extruder)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines_base2, 1, 8), " B ", "DAE", "C H",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72), ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 132), 'H', getBlockStack(BlockBaseMachine3.steam_cutting)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines_base3, 1, 4), "B F", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72), ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('B'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        33
                ), ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 128)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines_base3, 1, 0), "B F", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72), ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('B'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        64
                ), ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 61)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines_base3, 1, 16), " B ", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72), ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 218)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines_base3, 1, 12), " B ", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 70), ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 39)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines_base3, 1, 8), "BGF", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72), ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('B'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        34
                ), ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),
                ('G'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 154)
        );

        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines_base, 1, 0), "A A",
                "DBE", "ACA",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 2),
                ('B'), ItemStackHelper.fromData(IUItem.simplemachine, 1, 0)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 1),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 78),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base, 1, 0)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 2),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 103),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base, 1, 1)
        ), 2);
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 0), "A A", "DBE", "ACA",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 622),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 602),
                ('B'), ItemStackHelper.fromData(IUItem.machines_base, 1, 2)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 1), "A A", "DBE", "ACA",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 622),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 608),
                ('B'), ItemStackHelper.fromData(IUItem.machines_base, 1, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 2), "A A", "DBE", "ACA",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 622),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 615),
                ('B'), ItemStackHelper.fromData(IUItem.machines_base, 1, 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 3), "A A", "DBE", "ACA",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 622),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 617),
                ('B'), ItemStackHelper.fromData(IUItem.machines_base, 1, 8)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 4), "A A", "DBE", "ACA",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 622),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 609),
                ('B'), ItemStackHelper.fromData(IUItem.machines_base2, 1, 3)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 5), "A A", "DBE", "ACA",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 622),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 616),
                ('B'), ItemStackHelper.fromData(IUItem.machines_base2, 1, 7)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 6), "A A", "DBE", "ACA",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 622),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 612),
                ('B'), ItemStackHelper.fromData(IUItem.machines_base2, 1, 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 11), "A A", "DBE", "ACA",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 622),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 606),
                ('B'), ItemStackHelper.fromData(IUItem.machines_base3, 1, 19)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 9), "A A", "DBE", "ACA",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 622),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 613),
                ('B'), ItemStackHelper.fromData(IUItem.machines_base3, 1, 15)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 8), "AHA", "DBE", "ACA",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 622),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 607),
                ('H'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 605),
                ('B'), ItemStackHelper.fromData(IUItem.machines_base3, 1, 7)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 7), "AHA", "DBE", "ACA",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 622),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 611),
                ('H'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 604),
                ('B'), ItemStackHelper.fromData(IUItem.machines_base3, 1, 3)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 10), "AHA", "DBE", "ACA",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 622),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 610),
                ('H'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 603),
                ('B'), ItemStackHelper.fromData(IUItem.machines_base3, 1, 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 11), " C ", "BAB", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 21),

                ('B'), "c:plates/Iron",

                ('C'), ItemStackHelper.fromData(IUItem.advBattery, 1)
        );
        Recipes.recipe.addShapelessRecipe(
                getBlockStack(BlockBaseMachine3.generator_iu),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 11), IUItem.machine
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 55), "DCD", "BAB", "CCC",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 21),

                ('B'), "c:plates/Iron",

                ('C'), ItemStackHelper.fromData(IUItem.sunnarium, 1, 4),

                ('D'),
                "c:plates/Germanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.chemicalPlant, 1, 2), "DBD", "CAC", "DBD",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 8),

                ('B'), "c:plates/CobaltChrome",

                ('C'), "c:plates/NiobiumTitanium",

                ('D'),
                ItemStackHelper.fromData(IUItem.synthetic_plate, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.geothermalpump, 1, 2), " B ", "CAC", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 8),

                ('B'), "c:plates/Woods",

                ('C'), "c:plates/Permalloy",

                ('D'),
                ItemStackHelper.fromData(IUItem.synthetic_plate, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.chemicalPlant, 1, 1), " B ", "BAB", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.chemicalPlant, 1, 2),

                ('B'), ItemStackHelper.fromData(IUItem.heat_exchange, 1),

                ('C'), "c:plates/NiobiumTitanium",

                ('D'),
                ItemStackHelper.fromData(IUItem.synthetic_plate, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.geothermalpump, 1, 1), " B ", "BAB", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.geothermalpump, 1, 2),

                ('B'), ItemStackHelper.fromData(IUItem.heat_exchange, 1),

                ('C'), "c:plates/NiobiumTitanium",

                ('D'),
                ItemStackHelper.fromData(IUItem.synthetic_plate, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.chemicalPlant, 1, 6), "BBB", " A ", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.chemicalPlant, 1, 2),

                ('B'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 186),

                ('C'), "c:plates/NiobiumTitanium",

                ('D'),
                ItemStackHelper.fromData(IUItem.synthetic_plate, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.geothermalpump, 1, 6), "BBB", " A ", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.geothermalpump, 1, 2),

                ('B'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 186),

                ('C'), "c:plates/NiobiumTitanium",

                ('D'),
                ItemStackHelper.fromData(IUItem.synthetic_plate, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.chemicalPlant, 1, 0), "CDC", "BAB", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.chemicalPlant, 1, 2),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373),

                ('C'), IUItem.photoniy_ingot,

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.geothermalpump, 1, 0), "CDC", "BAB", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.geothermalpump, 1, 2),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373),

                ('C'), IUItem.photoniy_ingot,

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.chemicalPlant, 1, 5), "CDC", "BAB", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.chemicalPlant, 1, 2),

                ('B'), ItemStackHelper.fromData(IUItem.alloygear, 1, 20),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 92),

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.chemicalPlant, 1, 3), " C ", "BAB", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.chemicalPlant, 1, 2),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 372),

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.geothermalpump, 1, 3), " B ", "BAB", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.geothermalpump, 1, 2),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 509),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 372),

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.geothermalpump, 1, 4), " C ", "BAB", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.geothermalpump, 1, 2),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 372),

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.chemicalPlant, 1, 4), " C ", "BAB", " C ",

                ('A'), ItemStackHelper.fromData(IUItem.chemicalPlant, 1, 2),

                ('B'), ItemStackHelper.fromData(IUItem.item_pipes, 1, 1),

                ('C'), ItemStackHelper.fromData(IUItem.item_pipes, 1, 0),

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.geothermalpump, 1, 5), " C ", "BAB", " C ",

                ('A'), ItemStackHelper.fromData(IUItem.geothermalpump, 1, 2),

                ('B'), ItemStackHelper.fromData(IUItem.item_pipes, 1, 1),

                ('C'), ItemStackHelper.fromData(IUItem.item_pipes, 1, 0),

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 59), "BCB", "DAD", "ECE",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 21),

                ('B'), "c:doubleplate/Muntsa",

                ('C'), "c:doubleplate/Alcled",

                ('D'),
                "c:plates/Zinc",

                ('E'), "c:plates/Caravky"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cyclotron, 1, 6), " C ", "DAD", " C ",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 8),

                ('B'), "c:doubleplate/Muntsa",

                ('C'), "c:plates/HafniumBoride",

                ('D'),
                "c:plates/TantalumTungstenHafnium",

                ('E'), "c:plates/Caravky"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cyclotron, 1, 1), " C ", "DAD", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.cyclotron, 1, 6),

                ('B'), "c:doubleplate/Muntsa",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 352),

                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402),

                ('E'), "c:plates/Caravky"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cyclotron, 1, 3), " C ", "DAD", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.cyclotron, 1, 6),

                ('B'), "c:doubleplate/Muntsa",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),

                ('D'),
                IUItem.cooling_mixture,

                ('E'), "c:plates/Caravky"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cyclotron, 1, 2), " C ", "DAD", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.cyclotron, 1, 6),

                ('B'), "c:doubleplate/Muntsa",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),

                ('D'),
                IUItem.cryogenic_cooling_mixture,

                ('E'), "c:plates/Caravky"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cyclotron, 1, 7), " C ", "DAD", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.cyclotron, 1, 6),

                ('B'), "c:doubleplate/Muntsa",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 155),

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('E'), "c:plates/Caravky"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cyclotron, 1, 4), " C ", " A ", " D ",

                ('A'), ItemStackHelper.fromData(IUItem.cyclotron, 1, 6),

                ('B'), "c:doubleplate/Muntsa",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 470),

                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),

                ('E'), "c:plates/Caravky"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cyclotron, 1, 8), " C ", " A ", " D ",

                ('A'), ItemStackHelper.fromData(IUItem.cyclotron, 1, 6),

                ('B'), "c:doubleplate/Muntsa",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 110),

                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),

                ('E'), "c:plates/Caravky"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cyclotron, 1, 0), "BCB", "DAD", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.cyclotron, 1, 6),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 398),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402),

                ('E'), "c:plates/Caravky"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.cyclotron, 1, 5), " C ", " A ", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.cyclotron, 1, 6),

                ('B'), "c:doubleplate/Muntsa",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 68),

                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402),

                ('E'), "c:plates/Caravky"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 65), "BBB", " A ", "CCC",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 21),

                ('B'), "c:plates/Germanium",

                ('C'), "c:plates/Chromium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 22), "BCB", "BCB", " A ",
                ('A'), DEFAULT_SENSOR, ('B'), Blocks.GLASS,
                ('C'), IUItem.FluidCell
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 5), "BCB", "BCB", " A ",
                ('A'), ADV_SENSOR, ('B'), Blocks.GLASS,
                ('C'), IUItem.FluidCell
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 82), "BCB", "BCB", " A ",
                ('A'), IMP_SENSOR, ('B'), Blocks.GLASS,
                ('C'), IUItem.FluidCell
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 106), "BCB", "BCB", " A ",
                ('A'), PER_SENSOR, ('B'), Blocks.GLASS,
                ('C'), IUItem.FluidCell
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 630), "BCB", "BCB", " A ",
                ('A'), PHOTON_SENSOR, ('B'), Blocks.GLASS,
                ('C'), IUItem.FluidCell
        );
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 6),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 225),
                ('B'),
                ItemStackHelper.fromData(IUItem.simplemachine, 1, 2)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 7),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 221),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base, 1, 6)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 8),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 223),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base, 1, 7)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 9),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 160),
                ('B'),
                ItemStackHelper.fromData(IUItem.simplemachine, 1, 3)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 10),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 161),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base, 1, 9)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 11),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 162),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base, 1, 10)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 3),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 1),
                ('B'),
                ItemStackHelper.fromData(IUItem.simplemachine, 1, 1)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 4),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 77),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base, 1, 3)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base, 1, 5),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 102),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base, 1, 4)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 0),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 9),
                ('B'),
                ItemStackHelper.fromData(IUItem.simplemachine, 1, 5)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 1),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 86),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 0)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 2),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 110),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 1)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 1),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 166),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 0)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 2),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 167),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 1)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 3),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 168),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 2)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 5),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 125),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 4)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 6),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 126),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 5)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 7),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 127),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 6)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 9),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 133),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 8)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 10),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 134),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 9)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 11),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 136),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base2, 1, 10)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 17),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 224),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 16)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 18),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 220),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 17)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 19),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 222),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 18)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 1),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 19),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 0),
                ('G'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 18)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 2),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 95),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 1),
                ('G'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 94)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 3),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 119),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 2),
                ('G'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 118)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 5),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 9),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 4),
                ('G'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 129)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 6),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 86),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 5),
                ('G'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 130)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 7),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 110),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 6),
                ('G'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 131)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 13),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 15),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 12)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 14),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 91),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 13)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 15),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 115),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 14)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 9),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 12),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 8),
                ('G'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 6)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 10),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 87),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 9),
                ('G'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 83)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 11),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 111),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base3, 1, 10),
                ('G'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 107)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 3),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 135),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 2)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 4),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 146),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 3)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 5),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 157),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 4)
        ), 2);
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines_base1, 1, 6), "A A", "DBE", "ACA",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 137),
                ('C'), IUItem.elemotor,
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 124),
                ('B'), ItemStackHelper.fromData(IUItem.machines_base, 1, 2)
        );
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 7),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 135),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 6)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 8),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 146),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 7)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 9),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 157),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 8)
        ), 2);
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.pho_machine, 1, 20),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 622),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 157),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 5)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.pho_machine, 1, 13),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 622),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52),
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 632),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines_base1, 1, 9)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 45), "   ", "CAB", "   ",
                ('A'), IUItem.machine,
                ('C'), IUItem.elemotor, ('B'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        101
                )
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 46), "D D", "CAB", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 45),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        20
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 4), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 47), "D D", "CAB", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 46),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        96
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 81), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 48), "D D", "CAB", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 47),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 105), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 49), "   ", "CAB", "   ",
                ('A'), IUItem.advancedMachine,
                ('C'), IUItem.elemotor, ('B'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        170
                )
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 50), "D D", "CAB", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 49),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        20
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 171), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 51), "D D", "CAB", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 50),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        96
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 172), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 52), "D D", "CAB", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 51),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 173), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 14), "D D", "CAB", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 52),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        622
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 635), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 53), " E ", "CAB", " F ",
                ('A'), IUItem.advancedMachine,
                ('C'), IUItem.elemotor, ('B'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        58
                ),
                ('E'),
                ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        27
                ), ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 154)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 54), "DED", "CAB", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 53),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        20
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 17), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 6)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 55), "DED", "CAB", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 54),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        96
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 93), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 83)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 56), "DED", "CAB", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 55),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 117), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 107)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 15), "DED", "CAB", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 56),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        622
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 634), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 603)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 57), " B ", " A ", "   ",
                ('A'), IUItem.advancedMachine,
                ('C'), IUItem.elemotor, ('B'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        79
                )
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 58), "DBD", " A ", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 57),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        20
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 176), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 59), "DBD", " A ", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 58),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        96
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 174), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 60), "DBD", " A ", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 59),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 175), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 19), "DBD", " A ", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 60),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 627), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 61), " B ", " A ", "   ",
                ('A'), IUItem.advancedMachine,
                ('C'), IUItem.elemotor, ('B'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        68
                )
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 62), "DBD", " A ", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 61),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        20
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 179), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 63), "DBD", " A ", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 62),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        96
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 177), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 64), "DBD", " A ", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 63),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 178), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 18), "DBD", " A ", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 64),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 626), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 65), " B ", " A ", "   ",
                ('A'), IUItem.machine,
                ('C'), IUItem.elemotor, ('B'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        196
                )
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 66), "DBD", " A ", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 65),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        20
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 199), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 67), "DBD", " A ", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 66),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        96
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 197), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 68), "DBD", " A ", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 67),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 198), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 17), "DBD", " A ", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 68),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 636), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 28), "   ", "CAB", "   ",
                ('A'), IUItem.machine,
                ('C'), IUItem.elemotor, ('B'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        71
                )
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 29), "D D", "CAB", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 28),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        20
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 3), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 30), "D D", "CAB", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 29),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        96
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 80), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 31), "D D", "CAB", "D D",
                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 30),
                ('C'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 104), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines, 1, 4), " B ", "CAD", " E ",

                ('A'), IUItem.machine, ('B'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        74
                ),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 70), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'), IUItem.elemotor
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine, 1, 3), "F F", "BAD", "FEF",

                ('A'), ItemStackHelper.fromData(
                        IUItem.machines,
                        1,
                        4
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 235),
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 154), "F F", "BAD", "FEF",

                ('A'), ItemStackHelper.fromData(IUItem.basemachine, 1, 3), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 618),
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 155), "FGF", "BAD", "FEF",

                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 154), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 619),
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120), 'G', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines, 1, 6), "B F", "CAD", " E ",

                ('A'), IUItem.machine, ('B'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        73
                ),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 70), ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('E'), IUItem.elemotor, ('F'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        51
                )
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 5), "DBD", " A ", " C ",

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 70),

                ('C'), IUItem.imp_motors_with_improved_bearings_,

                ('A'), IUItem.advancedMachine,

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 6), "D D", "BAE", " C ",

                ('A'), IUItem.machine,

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 70),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 11),

                ('C'),
                IUItem.elemotor,
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines, 1, 7), " H ", "BAE", "DCF",

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),

                ('C'), IUItem.elemotor,
                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 227),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 230),
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 231),
                ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 10), "BAB", " C ", "EDE",

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),

                ('C'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 152),

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 11),

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72), 'E', "c:plates/Permalloy"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine, 1, 12), " D ", "BAC", "E H",

                ('A'), IUItem.machine, ('B'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        70
                ),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44), ('E'),
                IUItem.elemotor,
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 205), 'H', getBlockStack(BlockBaseMachine3.steam_handler_ore)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 40), "BDB", " A ", "BEB",

                ('A'), ItemStackHelper.fromData(
                        IUItem.basemachine,
                        1,
                        12
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 204)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 41), "BDB", " A ", "BEB",

                ('A'), ItemStackHelper.fromData(
                        IUItem.basemachine2,
                        1,
                        40
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 202)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 42), "BDB", " A ", "BEB",

                ('A'), ItemStackHelper.fromData(
                        IUItem.basemachine2,
                        1,
                        41
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 120),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 203)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 16), "BDB", " A ", "BEB",

                ('A'), ItemStackHelper.fromData(
                        IUItem.basemachine2,
                        1,
                        42
                ), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 622),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 633)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine, 1, 11), " E ", "DAB", " C ",

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),

                ('C'), IUItem.elemotor,

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 100),

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 99),

                ('A'), IUItem.advancedMachine
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine, 1, 10), "   ", "AEB", " C ",

                ('E'), IUItem.advancedMachine,

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),

                ('C'), IUItem.elemotor,

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 99)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.sunnariummaker), " E ", "BAC", " D ",

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 128),

                ('D'), IUItem.elemotor,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 79),

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),

                ('A'), IUItem.machine
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.sunnariumpanelmaker), "CAB",

                ('D'), IUItem.carbonPlate,

                ('C'), ItemStackHelper.fromData(IUItem.blockSE),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 55),

                ('A'), ItemStackHelper.fromData(IUItem.sunnariummaker)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine, 1, 13), " B ", "DAE", " C ",

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('C'), IUItem.elemotor,
                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 28),
                ('A'), IUItem.advancedMachine
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine, 1, 15), "   ", "CBD", " A ",

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 29),

                ('A'), IUItem.elemotor,

                ('B'), IUItem.machine,

                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine, 1, 14), "CDC", "ABA", "CDC",

                ('D'),
                ("c:plates/Nichrome"),
                ('A'), ItemStackHelper.fromData(IUItem.impmagnet, 1),

                ('B'), ItemStackHelper.fromData(IUItem.basemachine, 1, 15),

                ('C'),
                ItemStackHelper.fromData(IUItem.compresscarbon)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine1, 1, 1), " B ", "DAD", "DCD",

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 121),

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),

                ('A'), IUItem.advancedMachine,

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 217),

                ('C'), IUItem.elemotor
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine1, 1, 3), "   ", "BAD", " C ",

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 62),

                ('C'), IUItem.elemotor,

                ('A'), IUItem.machine,

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 44), "   ", "BAD", "ECF",

                ('A'), IUItem.machine,

                ('C'), IUItem.elemotor,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('E'), Items.WATER_BUCKET,
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 243)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 43), " E ", "BAD", "FCG",

                ('A'), IUItem.machine,

                ('C'), IUItem.elemotor,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 66),
                ('G'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 67),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 101)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 8), "DED", "CAC", "CBC",

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 67),

                ('D'),
                ("c:plates/Nichrome"),
                ('C'),
                ("c:plates/Caravky"),
                ('A'),
                IUItem.upgradeblock,

                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.upgradeblock), " E ", "CAD", " B ",

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 66),

                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('A'), IUItem.advancedMachine,

                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'), IUItem.elemotor
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 226), "BBB", "CAC", "DED",

                ('D'),
                ("c:plates/Alumel"),
                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('A'), DEFAULT_SENSOR,

                ('B'),
                IUItem.iridiumPlate,

                ('C'), ItemStackHelper.fromData(IUItem.plastic_plate)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 227), "CCC", "BAB", "   ",

                ('A'), DEFAULT_SENSOR, ('B'), "c:gears/Aluminium", ('C'), Blocks.COBBLESTONE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 229), "CCC", "BAB", "   ",

                ('A'), DEFAULT_SENSOR, ('B'), "c:gears/Zinc", ('C'), Blocks.OBSIDIAN
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 230), "CCC", "BAB", "   ",

                ('A'), DEFAULT_SENSOR, ('B'), "c:gears/Manganese", ('C'), Blocks.SAND
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 231), "CCC", "BAB", "   ",

                ('A'), DEFAULT_SENSOR, ('B'), "c:gears/Spinel", ('C'), Blocks.GRAVEL
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 228),
                "CCC",
                "BAB",
                "   ",

                ('A'),
                DEFAULT_SENSOR,
                ('B'),
                Items.LAVA_BUCKET,
                ('C'),
                "c:plates/Redbrass"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 234), " C ", "BAB", "   ",
                ('A'), ADV_SENSOR, ('B'), "c:plates/Iron", ('C'), ItemStackHelper.fromData(
                        IUItem.energy_crystal,
                        1,
                        32767
                )
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 232), " C ", "BAB", "   ",
                ('A'), IMP_SENSOR, ('B'), "c:plates/Iron", ('C'), ItemStackHelper.fromData(
                        IUItem.AdvlapotronCrystal,
                        1,
                        32767
                )
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 233), " C ", "BAB", "   ",
                ('A'), PER_SENSOR, ('B'), "c:plates/Iron", ('C'), ItemStackHelper.fromData(
                        IUItem.AdvlapotronCrystal,
                        1,
                        32767
                )
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 629), " C ", "BAB", "   ",
                ('A'), PHOTON_SENSOR, ('B'), "c:plates/Iron", ('C'), ItemStackHelper.fromData(
                        IUItem.AdvlapotronCrystal,
                        1,
                        32767
                )
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 235),
                "B B",
                "DAD",
                "CCC",
                ('A'),
                ADV_SENSOR,
                ('B'),
                "c:plates/Alumel",
                ('C'),
                "c:plates/Ferromanganese",

                ('D'),
                "c:plates/Duralumin"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 618),
                "B B",
                "DAD",
                "CCC",
                ('A'),
                IMP_SENSOR,
                ('B'),
                "c:plates/Vanadoalumite",
                ('C'),
                "c:plates/AluminiumSilicon",

                ('D'),
                "c:plates/Vitalium"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 619),
                "B B",
                "DAD",
                "CCC",
                ('A'),
                PER_SENSOR,
                ('B'),
                "c:plates/StainlessSteel",
                ('C'),
                "c:plates/Inconel",

                ('D'),
                "c:plates/Permalloy"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 236), "B B", "BAB", "B B",

                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidbenz.getInstance().get())
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 237), "B B", "BAB", "B B",

                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluidhyd.getInstance().get())
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 238),
                "BCB",
                "BAB",
                "BCB",

                ('A'),
                DEFAULT_SENSOR,
                ('B'),
                IUItem.FluidCell,
                ('C'),
                "c:plates/vanady"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 239), "B B", "BAB", "B B",

                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluiddizel.getInstance().get())
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 240), "B B", "BAB", "B B",

                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluiduu_matter.getInstance().get())
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 243), "CDC", "BAB", "E E",

                ('A'), DEFAULT_SENSOR,

                ('B'), "c:plates/Chromium",

                ('C'), "c:plates/Cobalt",

                ('D'),
                "c:plates/Nichrome",

                ('E'), "c:plates/Alcled"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 244), " D ", "BAC", "DDD",

                ('A'), DEFAULT_SENSOR, ('B'), ItemStackHelper.fromData(
                        IUItem.anode,
                        1,
                        32767
                ), ('C'), ItemStackHelper.fromData(IUItem.cathode, 1, 32767),
                ('D'),
                "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 241), " B ", "BAB", " B ",
                ('A'), DEFAULT_SENSOR, ('B'), "c:plateDense/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 248), " B ", "BAB", " B ",
                ('A'), ADV_SENSOR, ('B'), "c:plateDense/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 245), " B ", "BAB", " B ",
                ('A'), IMP_SENSOR, ('B'), "c:plateDense/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 242), " B ", "BAB", " B ",
                ('A'), DEFAULT_SENSOR, ('B'), "c:plates/Lead"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 249), " A ", "ABA", " A ",
                ('A'), ItemStackHelper.fromData(IUItem.nanoBox), ('B'), ADV_SENSOR
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 246), "CAC", "ABA", "CAC",

                ('C'), ItemStackHelper.fromData(IUItem.photoniy),

                ('A'), ItemStackHelper.fromData(IUItem.quantumtool),

                ('B'), IMP_SENSOR
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 247), "CAC", "ABA", "CAC",

                ('C'), ItemStackHelper.fromData(IUItem.photoniy_ingot),

                ('A'), ItemStackHelper.fromData(IUItem.advQuantumtool),

                ('B'), PER_SENSOR
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 17), " F ", "BAD", " C ",

                ('A'), IUItem.machine,

                ('C'), IUItem.elemotor,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 71),
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 243)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 21), "   ", "BAD", "GCF",

                ('A'), IUItem.machine,

                ('C'), IUItem.elemotor,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 71),
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 243),
                ('G'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 128)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 18), " E ", "BAD", "FCG",

                ('A'), IUItem.machine,

                ('C'), IUItem.elemotor,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 66),
                ('G'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 67),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 71)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine1), " E ", "BAB", "DCD",

                ('D'), ItemStackHelper.fromData(IUItem.module_stack),

                ('A'), ItemStackHelper.fromData(IUItem.simplemachine, 1, 6),

                ('B'), ItemStackHelper.fromData(IUItem.core, 1, 5),

                ('E'),
                ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        240
                ), ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 56)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 11), " C ", " A ", " B ",

                ('A'), IUItem.machine,

                ('B'), IUItem.elemotor,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 112)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine1, 1, 2), "   ", "CAD", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.elemotor,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 226),

                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 158)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine1, 1, 4), "   ", "CAD", " B ",

                ('A'),
                getBlockStack(BlockBaseMachine3.generator_iu),
                ('B'), IUItem.elemotor,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 239)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine1, 1, 5), "   ", "CAD", " B ",

                ('A'),
                getBlockStack(BlockBaseMachine3.generator_iu),
                ('B'), IUItem.elemotor,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 236)
        );
        Recipes.recipe.addRecipe(
                getBlockStack(BlockBaseMachine3.pump_iu), " E ", "CAD", "B H",

                ('A'), IUItem.machine,

                ('B'), IUItem.elemotor,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 154),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 241), 'H', getBlockStack(BlockBaseMachine3.steam_pump)
        );
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine1, 1, 6),
                "F F",
                "CAE",
                "FBF",

                ('A'),
                getBlockStack(BlockBaseMachine3.pump_iu),
                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),

                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 6),

                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 248),
                ('F'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine1, 1, 7),
                "F F",
                "CAE",
                "FBF",

                ('A'),
                ItemStackHelper.fromData(IUItem.basemachine1, 1, 6),

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 96),

                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 83),

                ('E'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 245),
                ('F'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139)
        ), 1);
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine1, 1, 9), "   ", "CAD", " B ",

                ('A'),
                getBlockStack(BlockBaseMachine3.generator_iu),
                ('B'), IUItem.elemotor,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 237)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine1, 1, 8), "CCC", " A ", " B ",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 35),

                ('A'), IUItem.machine,

                ('B'), IUItem.elemotor
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.oilrefiner), "DDD", "FBE", " C ",

                ('A'), ItemStackHelper.fromData(IUItem.fluidCell),

                ('B'), IUItem.advancedMachine,

                ('C'), IUItem.elemotor,

                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),

                ('E'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        98
                ), ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 33)
        );
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.oiladvrefiner),
                " A ",
                " B ",
                " C ",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 9),

                ('B'),
                IUItem.oilrefiner,

                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20)
        ), 0);
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine1, 1, 10), " C ", "DAE", " B ",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 229),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 154),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('B'),
                IUItem.elemotor,

                ('A'), IUItem.machine
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine1, 1, 11), " E ", "CAB", " D ",

                ('D'), IUItem.elemotor,

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),

                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 65)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine1, 1, 13), " E ", "CAB", " D ",

                ('D'), IUItem.elemotor,

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),

                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 65)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine1, 1, 12), "   ", "CAD", " B ",

                ('A'),
                getBlockStack(BlockBaseMachine3.generator_iu),
                ('B'), IUItem.elemotor,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 228)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine1, 1, 14), "   ", "CAD", " B ",

                ('A'),
                getBlockStack(BlockBaseMachine3.generator_iu),
                ('B'), IUItem.elemotor,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 238)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine1, 1, 15), " E ", "CAD", "B H",

                ('A'), IUItem.machine,

                ('B'), IUItem.elemotor,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 154),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 244), 'H', getBlockStack(BlockBaseMachine3.steam_electrolyzer)
        );


        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines, 1, 10),
                "BAB",
                " D ",
                "B B",

                ('D'),
                getBlockStack(BlockBaseMachine3.generator_iu),
                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 234),

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines, 1, 11),
                "BAB",
                " D ",
                "B B",

                ('D'),
                ItemStackHelper.fromData(IUItem.machines, 1, 10),

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 232),

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines, 1, 12),
                "BAB",
                " D ",
                "B B",

                ('D'),
                ItemStackHelper.fromData(IUItem.machines, 1, 11),

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 233),

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140)
        ), 2);
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.pho_machine, 1, 24),
                "BAB",
                " D ",
                "B B",

                ('D'),
                ItemStackHelper.fromData(IUItem.machines, 1, 12),

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 629),

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623)
        );
        Recipes.recipe.addRecipe(
                getBlockStack(BlockBaseMachine3.geogenerator_iu), "   ", "ABC", "   ",

                ('B'), IUItem.machine,

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 11),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 22)
        );
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine, 1, 4),
                "B B",
                "ADC",
                "B B",

                ('D'),
                getBlockStack(BlockBaseMachine3.geogenerator_iu),
                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 234),

                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 5),

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine, 1, 5),
                "B B",
                "ADC",
                "B B",

                ('D'),
                ItemStackHelper.fromData(IUItem.basemachine, 1, 4),

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 232),

                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 82),

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.basemachine, 1, 6),
                "B B",
                "ADC",
                "B B",

                ('D'),
                ItemStackHelper.fromData(IUItem.basemachine, 1, 5),

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 233),

                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 106),

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140)
        ), 2);
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.pho_machine, 1, 22),
                "B B",
                "ADC",
                "B B",

                ('D'),
                ItemStackHelper.fromData(IUItem.basemachine, 1, 6),

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 629),

                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 630),

                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623)
        );
        Recipes.recipe.addRecipe(
                getBlockStack(BlockBaseMachine3.solar_iu), "ABA", "BDB", " E ",

                ('E'), getBlockStack(BlockBaseMachine3.generator_iu), ('D'), ItemStackHelper.fromData(
                        IUItem.crafting_elements,
                        1,
                        37
                ),
                ('A'), "c:dusts/Coal", ('B'),
                Blocks.GLASS
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 9), "   ", "CAD", " B ",

                ('A'), IUItem.machine,

                ('B'), IUItem.elemotor,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 101),
                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 11)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blockmolecular), "MXM", "ABA", "MXM",
                ('M'), IUItem.advancedMachine,
                ('X'), ItemStackHelper.fromData(IUItem.tranformer, 1, 10), ('A'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),
                ('B'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 75)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blockdoublemolecular), "BDB", "CAC", "BEB",

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('B'), IUItem.advancedMachine,

                ('A'), ItemStackHelper.fromData(IUItem.blockmolecular),

                ('D'),
                ItemStackHelper.fromData(IUItem.tranformer, 1, 1),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 10)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 4), "   ", "BAB", "DCE",

                ('A'), IUItem.advancedMachine,

                ('B'), ItemStackHelper.fromData(IUItem.entitymodules, 1, 1),

                ('C'), IUItem.elemotor,

                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 59),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 54)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blockSE), " B ", " A ", "   ",

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 79),

                ('A'),
                getBlockStack(BlockBaseMachine3.solar_iu)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_se_generator), "   ", "CAC", " B ",

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 234),

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 176),
                ('C'), ItemStackHelper.fromData(IUItem.blockSE)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 12), " A ", " D ", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 56),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 175),

                ('C'),
                ("c:doubleplate/Duralumin"),
                ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('D'), IUItem.imp_se_generator
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.combinersolidmatter), "ABC", "DJE", "FGH",

                ('J'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 56),

                ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 145),

                ('G'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 147),

                ('F'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 148),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 149),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 150),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 151),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 152),

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 153)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.imp_se_generator), "   ", "CAC", " B ",

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 232),

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 174),
                ('C'), ItemStackHelper.fromData(IUItem.adv_se_generator)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 145), " B ", "BAB", " B ",

                ('A'), DEFAULT_SENSOR,
                ('B'), ItemStackHelper.fromData(IUItem.matter, 1, 2)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 147), " B ", "BAB", " B ",

                ('A'), DEFAULT_SENSOR,
                ('B'), ItemStackHelper.fromData(IUItem.matter, 1, 7)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 148), " B ", "BAB", " B ",

                ('A'), DEFAULT_SENSOR,
                ('B'), ItemStackHelper.fromData(IUItem.matter, 1, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 149), " B ", "BAB", " B ",

                ('A'), DEFAULT_SENSOR,
                ('B'), ItemStackHelper.fromData(IUItem.matter, 1, 4)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 150), " B ", "BAB", " B ",

                ('A'), DEFAULT_SENSOR,
                ('B'), ItemStackHelper.fromData(IUItem.matter, 1, 3)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 151), " B ", "BAB", " B ",

                ('A'), DEFAULT_SENSOR,
                ('B'), ItemStackHelper.fromData(IUItem.matter, 1, 0)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 152), " B ", "BAB", " B ",

                ('A'), DEFAULT_SENSOR,
                ('B'), ItemStackHelper.fromData(IUItem.matter, 1, 6)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 153), " B ", "BAB", " B ",

                ('A'), DEFAULT_SENSOR,
                ('B'), ItemStackHelper.fromData(IUItem.matter, 1, 5)
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 251),
                "BCB",
                "BAB",
                "BCB",

                ('A'),
                DEFAULT_SENSOR,
                ('B'),
                ModUtils.getCellFromFluid(FluidName.fluidneft.getInstance().get()),
                ('C'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3)
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 251),
                "BCB",
                "BAB",
                "BCB",

                ('A'),
                DEFAULT_SENSOR,
                ('B'),
                ModUtils.getCellFromFluid(FluidName.fluidsour_light_oil.getInstance().get()),
                ('C'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 251),
                "BCB",
                "BAB",
                "BCB",

                ('A'),
                DEFAULT_SENSOR,
                ('B'),
                ModUtils.getCellFromFluid(FluidName.fluidsour_medium_oil.getInstance().get()),
                ('C'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 251),
                "BCB",
                "BAB",
                "BCB",

                ('A'),
                DEFAULT_SENSOR,
                ('B'),
                ModUtils.getCellFromFluid(FluidName.fluidsour_heavy_oil.getInstance().get()),
                ('C'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3)
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 251),
                "BCB",
                "BAB",
                "BCB",

                ('A'),
                DEFAULT_SENSOR,
                ('B'),
                ModUtils.getCellFromFluid(FluidName.fluidsweet_medium_oil.getInstance().get()),
                ('C'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3)
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 251),
                "BCB",
                "BAB",
                "BCB",

                ('A'),
                DEFAULT_SENSOR,
                ('B'),
                ModUtils.getCellFromFluid(FluidName.fluidsweet_heavy_oil.getInstance().get()),
                ('C'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.oilgetter), " A ", " B ", " D ",

                ('D'), IUItem.elemotor,

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 251),

                ('B'), IUItem.machine
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.convertersolidmatter, 1), "E F", "ABC", " D ",

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 75),

                ('A'),
                getBlockStack(BlockBaseMachine3.replicator_iu),
                ('C'),
                getBlockStack(BlockBaseMachine3.scanner_iu),
                ('D'), IUItem.combinersolidmatter,

                ('B'), IUItem.advancedMachine
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 23), " C ", " D ", " A ",

                ('D'), IUItem.blockmolecular,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 10),

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 152)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 39), " C ", " D ", " A ",

                ('D'), IUItem.blockmolecular,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 10),

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 151)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 37), " C ", " D ", " A ",

                ('D'), IUItem.blockmolecular,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 10),

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 150)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 36), " C ", " D ", " A ",

                ('D'), IUItem.blockmolecular,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 10),

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 153)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 35), " C ", " D ", " A ",

                ('D'), IUItem.blockmolecular,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 10),

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 145)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 34), " C ", " D ", " A ",

                ('D'), IUItem.blockmolecular,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 10),

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 147)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.veinsencor, 1), " BC", "BDB", "BAB",

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                ('B'), "c:plates/Iron", ('C'), Items.REDSTONE, ('D'),
                "c:plates/Chromium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 169), "CED", "ABA", "CFD",

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('B'), "c:plates/Iron",

                ('C'), Items.REDSTONE,

                ('D'),
                ItemStackHelper.fromData(Items.LAPIS_LAZULI),

                ('E'), IUItem.advnanobox,

                ('F'),
                getBlockStack(BlockBaseMachine3.energy_controller)
        );
        Recipes.recipe.addShapelessRecipe(
                getBlockStack(BlockBaseMachine3.limiter),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 169), IUItem.advancedMachine
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 261), " C ", "BAB", " D ",
                ('A'), ADV_SENSOR, ('B'), "c:plates/Iron", ('C'), ItemStackHelper.fromData(
                        IUItem.energy_crystal,
                        1,
                        32767
                ),
                ('D'),
                Blocks.REDSTONE_BLOCK
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 628), " C ", "BAB", " D ",
                ('A'), PHOTON_SENSOR, ('B'), "c:plates/Iron", ('C'), ItemStackHelper.fromData(
                        IUItem.energy_crystal,
                        1,
                        32767
                ),
                ('D'),
                Blocks.REDSTONE_BLOCK
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 259), " C ", "BAB", " D ",
                ('A'), IMP_SENSOR, ('B'), "c:plates/Iron", ('C'), ItemStackHelper.fromData(
                        IUItem.AdvlapotronCrystal,
                        1,
                        32767
                ),
                ('D'),
                Blocks.REDSTONE_BLOCK
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 260), " C ", "BAB", " D ",
                ('A'), PER_SENSOR, ('B'), "c:plates/Iron", ('C'), ItemStackHelper.fromData(
                        IUItem.AdvlapotronCrystal,
                        1,
                        32767
                ),
                ('D'),
                Blocks.REDSTONE_BLOCK
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 258), " C ", "BAB", " D ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 21),

                ('B'), "c:plates/Iron",

                ('C'), ItemStackHelper.fromData(IUItem.advBattery, 1, 32767),
                ('D'),
                Blocks.REDSTONE_BLOCK
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 71), "BAB", " D ", "B B",

                ('D'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 70),

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 261),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 72), "BAB", " D ", "B B",

                ('D'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 71),

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 259),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 73), "BAB", " D ", "B B",

                ('D'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 72),

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 260),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 23), "BAB", " D ", "B B",

                ('D'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 73),

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 628),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623)
        );
        Recipes.recipe.addShapelessRecipe(
                getBlockStack(BlockBaseMachine3.redstone_generator),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 258), getBlockStack(BlockBaseMachine3.generator_iu)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 206), "ABA", "ACA", "ABA",

                ('B'), IUItem.insulatedTinCableItem,

                ('A'),
                ("planks"),
                ('C'), DEFAULT_SENSOR
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 207), "   ", "ABA", "   ",

                ('A'), IUItem.insulatedCopperCableItem,

                ('B'), DEFAULT_SENSOR
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 208), " A ", "DBC", " A ",

                ('B'), DEFAULT_SENSOR,

                ('A'), IUItem.insulatedGoldCableItem,

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),

                ('C'),
                ItemStackHelper.fromData(IUItem.advBattery, 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 209), " A ", "DBC", " A ",

                ('B'), DEFAULT_SENSOR,

                ('A'), IUItem.insulatedIronCableItem,

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),

                ('C'),
                ItemStackHelper.fromData(IUItem.lapotron_crystal, 1, 32767)
        );
        for (int i = 0; i < 3; i++) {
            Recipes.recipe.addShapelessRecipe(
                    ItemStackHelper.fromData(IUItem.tranformer, 1, i + 8),
                    ItemStackHelper.fromData(IUItem.tranformer, 1, i + 7), ItemStackHelper.fromData(
                            IUItem.crafting_elements,
                            1,
                            207 + i
                    )
            );
        }
        Recipes.recipe.addShapelessRecipe(
                ItemStackHelper.fromData(IUItem.tranformer, 1, 7),
                IUItem.machine, ItemStackHelper.fromData(IUItem.crafting_elements, 1, 206)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 16),
                "ADA",
                "CBC",
                "ADA",

                ('A'),
                ItemStackHelper.fromData(IUItem.core, 1, 5),
                ('B'),
                ItemStackHelper.fromData(IUItem.module_schedule),
                ('C'),
                IUItem.compressIridiumplate,
                (
                        'D'),
                "c:doubleplate/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 262), "CCC", "BAB", "DED",

                ('B'), "c:gears/Alumel",

                ('C'), "c:plates/Aluminumbronze",

                ('D'), "c:plates/Titanium",

                ('E'),
                "c:plates/Iron",

                ('A'), DEFAULT_SENSOR
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 40),
                " B ",
                "ACA",
                "ADA",
                ('A'),
                "c:plates/Redbrass",
                ('B'),
                "c:plates/Iron",
                ('C'),
                "c:plates/Titanium",
                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 74), " E ", "ABD", " C ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 70),

                ('B'), IUItem.machine,

                ('C'),
                IUItem.elemotor,

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 262)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 256), "CDE", "BAB", "FFF",

                ('A'), ADV_SENSOR,

                ('C'), Items.DIAMOND_AXE,

                ('D'), Items.DIAMOND_PICKAXE,

                ('E'),
                Items.DIAMOND_SHOVEL,

                ('B'), "c:gears/Iridium",

                ('F'), "c:gears/Magnesium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 252), "CDE", "BAB", "FFF",

                ('A'), IMP_SENSOR,

                ('C'), Items.DIAMOND_AXE,

                ('D'), Items.DIAMOND_PICKAXE,

                ('E'),
                Items.DIAMOND_SHOVEL,

                ('B'), "c:gears/Iridium",

                ('F'), "c:gears/Magnesium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 254), "CDE", "BAB", "FFF",

                ('A'), PER_SENSOR,

                ('C'), Items.DIAMOND_AXE,

                ('D'), Items.DIAMOND_PICKAXE,

                ('E'),
                Items.DIAMOND_SHOVEL,

                ('B'), "c:gears/Iridium",

                ('F'), "c:gears/Magnesium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 631), "CDE", "BAB", "FFF",

                ('A'), PHOTON_SENSOR,

                ('C'), Items.DIAMOND_AXE,

                ('D'), Items.DIAMOND_PICKAXE,

                ('E'),
                Items.DIAMOND_SHOVEL,

                ('B'), "c:gears/Iridium",

                ('F'), "c:gears/Magnesium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 257), "CEC", "ABA", "DED",

                ('D'),
                ("c:gems/Curium"),
                ('C'),
                ("c:doubleplate/Caravky"),
                ('E'),
                ("c:plates/Duralumin"),
                ('A'),
                ItemStackHelper.fromData(IUItem.core, 1, 8),

                ('B'), ADV_SENSOR
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 253), "CEC", "ABA", "DED",

                ('D'),
                ("c:doubleplate/Germanium"),
                ('C'),
                ("c:doubleplate/Alcled"),
                ('E'),
                ("c:plates/Vitalium"),
                ('A'),
                ItemStackHelper.fromData(IUItem.core, 1, 11),

                ('B'), IMP_SENSOR
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 255), "CEC", "ABA", "DED",

                ('D'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('E'),
                ("c:plates/Titanium"),
                ('A'),
                ItemStackHelper.fromData(IUItem.core, 1, 12),

                ('B'), PER_SENSOR
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 250), "EDE", "BAB", "CCC",

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('D'), DEFAULT_SENSOR,

                ('C'), ItemStackHelper.fromData(IUItem.compressIridiumplate),

                ('B'),
                ItemStackHelper.fromData(IUItem.advQuantumtool),

                ('A'), ItemStackHelper.fromData(IUItem.core, 1, 7)
        );
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines, 1, 13), "C C", " B ",
                "CAC",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 257),
                ('B'), ItemStackHelper.fromData(IUItem.machines, 1, 8),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines, 1, 14), "C C", " B "
                , "CAC",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 253),
                ('B'), ItemStackHelper.fromData(IUItem.machines, 1, 13),
                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.machines, 1, 15),
                "C C",
                " B ",
                "CAC",

                ('A'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 255),
                ('B'),
                ItemStackHelper.fromData(IUItem.machines, 1, 14),
                ('C'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140)
        ), 2);
        // if (!Loader.isModLoaded("simplyquarries")) {
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.machines, 1, 8), "   ", " A ", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 250),
                ('B'), IUItem.advancedMachine
        );
        //  }
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blockadmin, 1), "   ", "BAB", "ABA",
                ('B'), ItemStackHelper.fromData(IUItem.excitednucleus, 1, 13), ('A'), ItemStackHelper.fromData(
                        IUItem.blockpanel,
                        1,
                        13
                )
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.perfect_drill), "ACB", "F F", "ECE",

                ('E'), ItemStackHelper.fromData(IUItem.advQuantumtool),

                ('F'), IUItem.overclockerUpgrade1,

                ('A'), ItemStackHelper.fromData(IUItem.spectralaxe, 1, 32767),

                ('B'),
                ItemStackHelper.fromData(IUItem.spectraldrill, 1, 32767),

                ('C'), TileGenerationMicrochip.getLevelCircuit(ItemStackHelper.fromData(IUItem.basecircuit, 1, 21), 11)
        );
        Recipes.recipe.addRecipe(
                ModUtils.setSize(IUItem.glassFiberCableItem, 6), "AAA", "BCB", "AAA",

                ('A'), ItemStackHelper.fromData(Blocks.GLASS),

                ('B'), IUItem.energiumDust,

                ('C'), "c:dusts/Silver"
        );
        Recipes.recipe.addRecipe(IUItem.coil, "AAA", "ABA", "AAA",

                ('A'), IUItem.copperCableItem,

                ('B'), Items.IRON_INGOT
        );
        Recipes.recipe.addRecipe(IUItem.powerunit, "ABD", "ACE", "ABD",

                ('A'), ItemStackHelper.fromData(IUItem.reBattery, 1, 32767),

                ('B'), IUItem.copperCableItem,

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),

                ('D'),
                "c:casings/Iron",

                ('E'), IUItem.elemotor
        );
        Recipes.recipe.addRecipe(IUItem.powerunitsmall, " BD", "ACE", " BD",

                ('A'), ItemStackHelper.fromData(IUItem.advBattery, 1),

                ('B'), IUItem.copperCableItem,

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),

                ('D'),
                "c:casings/Iron",

                ('E'), IUItem.elemotor
        );
        Recipes.recipe.addRecipe(IUItem.reBattery.getItemStack()
                , "AEA", "CDC", "CDC",

                ('A'), IUItem.insulatedTinCableItem,

                ('C'), "c:casings/AluminiumLithium",

                ('D'), ItemStackHelper.fromData(IUItem.charged_redstone), 'E', ItemStackHelper.fromData(IUItem.charged_quartz)
        );
        Recipes.recipe.addRecipe(IUItem.advBattery.getItemStack()
                , "AEA", "CDC", "CBC",

                ('A'), IUItem.insulatedCopperCableItem,

                ('C'), IUItem.casingbronze,

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 476),

                ('B'),
                "c:dusts/Lead", 'E', ItemStackHelper.fromData(IUItem.charged_redstone, 1)
        );

        Recipes.recipe.addRecipe(IUItem.energyStorageUpgrade, "FEF", "BDB", "ACA",

                ('A'), "c:plates/Carbon",

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),

                ('D'), ItemStackHelper.fromData(IUItem.advBattery, 1),

                ('B'),
                IUItem.insulatedCopperCableItem, 'E', IUItem.upgrade_casing, 'F', "c:plates/Obsidian"
        );
        Recipes.recipe.addShapelessRecipe(
                ItemStackHelper.fromData(IUItem.frequency_transmitter),
                IUItem.insulatedCopperCableItem, TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
        );
        Recipes.recipe.addRecipe(
                ModUtils.setSize(IUItem.suBattery.getItemStack(), 5), " A ", " B ", " C ",

                ('A'), IUItem.insulatedCopperCableItem,

                ('B'), Items.REDSTONE,

                ('C'), "c:dusts/Coal"
        );


        Recipes.recipe.addRecipe(
                getBlockStack(BlockBaseMachine3.pattern_storage_iu), "AAA", "CDC", " B ",

                ('A'), IUItem.reinforcedStone,

                ('C'), IUItem.crystalMemory,

                ('D'), IUItem.advancedMachine,

                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
        );
        Recipes.recipe.addRecipe(
                getBlockStack(BlockBaseMachine3.teleporter_iu), " A ", " B ", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 268),

                ('B'), IUItem.advancedMachine
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 268), "AEA", "CBC", "ADA",

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),

                ('C'), IUItem.glassFiberCableItem,

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 270),

                ('B'),
                DEFAULT_SENSOR,

                ('E'), IUItem.frequency_transmitter
        );
        Recipes.recipe.addRecipe(
                getBlockStack(BlockBaseMachine3.canner_iu), "A D", "EBF", " C ",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 265),

                ('B'), IUItem.machine,

                ('C'), IUItem.elemotor,

                ('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 154)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 265), "CCC", "BAB", "DDD",

                ('A'), DEFAULT_SENSOR,

                ('B'), "c:gears/Nickel",

                ('C'), "c:plates/Tin",

                ('D'),
                "c:gears/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 263), " C ", "BAB", "DDD",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 21),

                ('B'), "c:doubleplate/Nichrome",

                ('C'), ItemStackHelper.fromData(IUItem.energy_crystal, 1, 32767),

                ('D'),
                ModUtils.getCellFromFluid(FluidName.fluidgas.getInstance().get())
        );
        Recipes.recipe.addShapelessRecipe(
                getBlockStack(BlockBaseMachine3.gas_generator),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 263), IUItem.machine
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.lap_energystorage_upgrade,
                IUItem.energyStorageUpgrade, ItemStackHelper.fromData(IUItem.reBattery, 1, 32767)
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.adv_lap_energystorage_upgrade,
                IUItem.lap_energystorage_upgrade, ItemStackHelper.fromData(IUItem.energy_crystal, 1, 32767)
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.imp_lap_energystorage_upgrade,
                IUItem.adv_lap_energystorage_upgrade, ItemStackHelper.fromData(IUItem.lapotron_crystal, 1, 32767)
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.per_lap_energystorage_upgrade,
                IUItem.imp_lap_energystorage_upgrade, ItemStackHelper.fromData(IUItem.AdvlapotronCrystal, 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 264), " C ", "BAB", "DDD",

                ('A'), DEFAULT_SENSOR,

                ('B'), "c:doubleplate/Nichrome",

                ('C'), IUItem.tinCan,

                ('D'),
                "c:plates/Vanadoalumite"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.coolingsensor, 1), "CBC", "BDB", "BAB",

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                ('B'), "c:plates/Iron", ('C'), ItemStackHelper.fromData(
                        IUItem.coolpipes,
                        1,
                        3
                ), ('D'),
                "c:doubleplate/Mikhail"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heatsensor, 1), "CBC", "BDB", "BAB",

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                ('B'), "c:plates/Iron", ('C'), ItemStackHelper.fromData(
                        IUItem.pipes,
                        1,
                        3
                ), ('D'),
                ItemStackHelper.fromData(IUItem.preciousblock)
        );

        Recipes.recipe.addRecipe(
                ModUtils.setSize(IUItem.reinforcedGlass, 7),
                "BAB", "BBB", "BAB", ('B'), Blocks.GLASS, ('A'), IUItem.advancedAlloy
        );

        Recipes.recipe.addRecipe(IUItem.hazmat_helmet.getItemStack()
                , "AAA", "ABA", " C ",

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 40),

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 478),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 97),

                ('D'),
                Blocks.IRON_BARS
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 97)
                , "AAA", "ABA", "CAC",

                ('B'), Blocks.GLASS,

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 501),

                ('C'), "c:plates/Carbon",

                ('D'),
                Blocks.IRON_BARS
        );
        Recipes.recipe.addRecipe(IUItem.hazmat_chestplate.getItemStack()
                , "B B", "BBB", "BBB",

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 478),

                ('A'), ItemStackHelper.fromData(Items.ORANGE_DYE)
        );
        Recipes.recipe.addRecipe(IUItem.hazmat_leggings.getItemStack()
                , "BBB", "B B", "B B",

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 478),

                ('A'), ItemStackHelper.fromData(Items.ORANGE_DYE)
        );
        Recipes.recipe.addRecipe(
                IUItem.rubber_boots.getItemStack()
                ,
                "   ", "B B", "B B", ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 478), ('A'), ItemStackHelper.fromData(
                        Items.ORANGE_DYE
                )
        );
        Recipes.recipe.addRecipe(
                IUItem.sprayer,
                "B  ", " B ", " AB", ('B'), "c:casings/Iron", ('A'), IUItem.fluidCell
        );
        Recipes.recipe.addRecipe(IUItem.treetap
                , " B ", "BBB", "B  ", ('B'), "planks");
        Recipes.recipe.addRecipe(IUItem.wrench
                , "B B", "BBB", " B ", ('B'), "c:ingots/Bronze");
        Recipes.recipe.addShapelessRecipe(IUItem.electric_wrench
                , IUItem.wrench, IUItem.powerunitsmall);
        Recipes.recipe.addShapelessRecipe(IUItem.electric_treetap
                , IUItem.treetap, IUItem.powerunitsmall);
        Recipes.recipe.addRecipe(
                IUItem.chainsaw
                ,
                " BB", "BBB", "AB ", ('B'), "c:plates/Iron", ('A'), IUItem.powerunit
        );
        Recipes.recipe.addRecipe(
                IUItem.drill
                ,
                "   ", " B ", " A ", ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 508), ('A'), IUItem.powerunit
        );
        Recipes.recipe.addRecipe(IUItem.diamond_drill
                , "   ", " B ", "BAB",

                ('B'), "c:gems/Diamond",

                ('A'), ItemStackHelper.fromData(IUItem.drill, 1, 32767)
        );


        Recipes.recipe.addRecipe(IUItem.nightvision
                , " E ", "CDC", "ABA",

                ('A'), IUItem.rubber,

                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),

                ('C'), "c:plates/Carbon",

                ('D'),
                IUItem.reinforcedGlass,

                ('E'), ItemStackHelper.fromData(IUItem.advBattery, 1, 32767)


        );
        Recipes.recipe.addRecipe(
                IUItem.lappack
                ,
                "CAC", "BAB", "A A", ('B'), ItemStackHelper.fromData(IUItem.energy_crystal, 1, 32767),

                ('A'), "c:casings/Iron",
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
        );
        Recipes.recipe.addRecipe(
                IUItem.lapotron_crystal
                ,
                "ACA", "ABA", "ACA", ('B'), ItemStackHelper.fromData(IUItem.energy_crystal, 1, 32767),

                ('A'), "c:dusts/Lapis",
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
        );
        Recipes.recipe.addRecipe(
                IUItem.charging_re_battery
                ,
                "ABA", "B B", "ABA", ('B'), ItemStackHelper.fromData(IUItem.advBattery, 1, 32767),

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
        );
        Recipes.recipe.addRecipe(
                IUItem.advanced_charging_re_battery
                ,
                " B ", "BCB", " B ", ('B'), ItemStackHelper.fromData(IUItem.reBattery, 1, 32767),


                ('C'), ItemStackHelper.fromData(IUItem.charging_re_battery, 1, 32767)
        );
        Recipes.recipe.addRecipe(
                IUItem.charging_energy_crystal
                ,
                " B ", "BCB", " B ", ('B'), ItemStackHelper.fromData(IUItem.energy_crystal, 1, 32767),


                ('C'), ItemStackHelper.fromData(IUItem.advanced_charging_re_battery, 1, 32767)
        );
        Recipes.recipe.addRecipe(
                IUItem.charging_lapotron_crystal
                ,
                " B ", "BCB", " B ", ('B'), ItemStackHelper.fromData(IUItem.lapotron_crystal, 1, 32767),


                ('C'), ItemStackHelper.fromData(IUItem.charging_energy_crystal, 1, 32767)
        );
        Recipes.recipe.addRecipe(IUItem.ForgeHammer
                , "AA ", "ABB", "AA ", ('B'), Items.STICK,
                ('A'), "c:ingots/Iron"
        );
        Recipes.recipe.addRecipe(IUItem.ForgeHammer
                , " AA", "BBA", " AA", ('B'), Items.STICK,
                ('A'), "c:ingots/Iron"
        );
        Recipes.recipe.addRecipe(IUItem.cutter
                , "A A", " A ", "B B", ('B'), "c:ingots/Copper",
                ('A'), "c:plates/Iron"
        );
        Recipes.recipe.addRecipe(
                IUItem.advanced_batpack
                ,
                "BAB", "BCB", "B B", ('B'), ItemStackHelper.fromData(IUItem.advBattery, 1, 32767),

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1), ('C'), "c:casings/Copper"
        );
        Recipes.recipe.addRecipe(
                IUItem.batpack
                ,
                "BAB", "BCB", "B B", ('B'), ItemStackHelper.fromData(IUItem.advBattery, 1, 32767),

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1), ('C'), "planks"
        );
        Recipes.recipe.addRecipe(IUItem.nanosaber
                , "BA ", "BA ", "CDC", ('B'), Items.GLOWSTONE_DUST,
                ('A'), IUItem.advancedAlloy, ('C'), "c:plates/Carbon", ('D'),
                ItemStackHelper.fromData(IUItem.energy_crystal, 1, 32767)
        );


        Recipes.recipe.addRecipe(IUItem.mixedMetalIngot, "AAA", "BBB", "CCC",
                ('A'), "c:plates/Iron", ('B'), "c:plates/Copper", ('C'), "c:plates/Tin"
        );
        Recipes.recipe.addRecipe(IUItem.rawcrystalmemory, "ABA", "BAB", "ABA",
                ('A'), "c:dusts/Silicon_Dioxide", ('B'), "c:dusts/Obsidian"
        );
        Recipes.recipe.addRecipe(IUItem.carbonFiber, "AA ", "AA ", "   ",
                ('A'), "c:dusts/Coal"
        );
        Recipes.recipe.addShapelessRecipe(IUItem.carbonMesh, IUItem.carbonFiber, IUItem.carbonFiber);
        Recipes.recipe.addRecipe(IUItem.copperboiler, "AAA", "A A", "AAA",
                ('A'), "c:casings/Copper"
        );
        Recipes.recipe.addRecipe(IUItem.plantBall, "AAA", "A A", "AAA",
                ('A'), Items.SUGAR_CANE
        );
        Recipes.recipe.addRecipe(IUItem.plantBall, "AAA", "A A", "AAA",
                ('A'), Items.WHEAT
        );
        Recipes.recipe.addRecipe(IUItem.plantBall, "AAA", "A A", "AAA",
                ('A'), Blocks.CACTUS
        );
        Recipes.recipe.addRecipe(IUItem.plantBall, "AAA", "A A", "AAA",
                ('A'), IUItem.crops
        );
        Recipes.recipe.addRecipe(IUItem.plantBall, "AAA", "A A", "AAA",
                ('A'), "leaves"
        );
        Recipes.recipe.addRecipe(IUItem.plantBall, "AAA", "A A", "AAA",
                ('A'), Items.WHEAT_SEEDS
        );

        Recipes.recipe.addRecipe(IUItem.plantBall, "AAA", "A A", "AAA",
                ('A'), "Saplings"
        );
        Recipes.recipe.addRecipe(ModUtils.setSize(IUItem.fluidCell.getItemStack(), 3), " A ", "ABA", " A ",

                ('A'), "c:casings/Tin",

                ('B'),
                Blocks.GLASS_PANE
        );
        Recipes.recipe.addRecipe(IUItem.coalBall, "AAA", "ABA", "AAA",
                ('A'), "c:dusts/Coal", ('B'), Items.FLINT
        );
        Recipes.recipe.addRecipe(IUItem.coal_chunk, "AAA", "ABA", "AAA",
                ('A'), IUItem.compressedCoalBall, ('B'), Blocks.OBSIDIAN
        );

        Recipes.recipe.addRecipe(IUItem.cfPowder, "ACA", "ABA", "ACA",
                ('A'), "c:dusts/Stone", ('B'), Items.CLAY_BALL,
                ('C'), Blocks.SAND
        );

        Recipes.recipe.addRecipe(IUItem.heatconducto, "ABA", "ABA", "ABA",
                ('A'), IUItem.rubber, ('B'), "c:plates/Copper"
        );
        Recipes.recipe.addRecipe(IUItem.ejectorUpgrade, "ABA", " D ", "ACA",
                ('A'), "c:plates/Tin", ('B'), Blocks.PISTON, 'C', IUItem.upgrade_casing, 'D', IUItem.motors_with_improved_bearings_
        );
        Recipes.recipe.addRecipe(
                IUItem.pullingUpgrade,
                "ABA",
                " D ",
                "ACA",
                ('A'),
                "c:plates/Tin",
                ('B'),
                Blocks.STICKY_PISTON,
                'C',
                IUItem.upgrade_casing,
                'D',
                IUItem.motors_with_improved_bearings_
        );
        Recipes.recipe.addRecipe(IUItem.fluidEjectorUpgrade, "ADA", " B ", "ACA",
                ('A'), "c:plates/Tin", ('B'), IUItem.elemotor, 'C', IUItem.upgrade_casing, 'D', IUItem.fluidCell
        );
        Recipes.recipe.addRecipe(IUItem.fluidpullingUpgrade, "ACA", " B ", "ADA",
                ('A'), "c:plates/Tin", ('B'), IUItem.elemotor, ('C'), ItemStackHelper.fromData(
                        IUItem.treetap,
                        1,
                        32767
                ), 'D', IUItem.upgrade_casing
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gasChamber), "ABA", "BBB", "CAC",
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 601), ('B'), "c:plates/Iron", ('C'), "c:gears/Invar"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 464)
                , "AAA", "AAA", "AAA", ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 457));
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 465)
                , "AAA", "AAA", "AAA", ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 464));

        Recipes.recipe.addRecipe(IUItem.bronze_helmet
                , "AAA", "A A", "   ", ('A'), "c:ingots/Bronze");
        Recipes.recipe.addRecipe(IUItem.bronze_leggings
                , "AAA", "A A", "A A", ('A'), "c:ingots/Bronze");
        Recipes.recipe.addRecipe(IUItem.bronze_boots
                , "   ", "A A", "A A", ('A'), "c:ingots/Bronze");
        Recipes.recipe.addRecipe(IUItem.bronze_chestplate
                , "A A", "AAA", "AAA", ('A'), "c:ingots/Bronze");


        Recipes.recipe.addRecipe(IUItem.UranFuel, "AAA", "BBB", "AAA",

                ('A'), IUItem.Uran238,
                ('B'), IUItem.smallUran235
        );
        Recipes.recipe.addRecipe(IUItem.Uran235, "BBB", "BBB", "BBB",

                ('B'), IUItem.smallUran235
        );
        Recipes.recipe.addRecipe(IUItem.mox, "AAA", "BBB", "AAA",

                ('A'), IUItem.Uran238,
                ('B'), IUItem.Plutonium
        );

        Recipes.recipe.addRecipe(ModUtils.setSize(IUItem.energiumDust, 9), "ABA", "BCB", "ABA",

                ('A'), IUItem.charged_redstone,

                ('B'), "c:dusts/diamond", 'C', Items.QUARTZ
        );

        Recipes.recipe.addRecipe(
                IUItem.advancedMachine,
                "ABA",
                "CDC",
                "ABA",
                ('A'),
                "c:plates/Steel",
                ('B'),
                IUItem.advancedAlloy,
                ('C'),
                "c:plates/Carbon",
                ('D'),
                IUItem.machine
        );
        Recipes.recipe.addRecipe(
                IUItem.nano_helmet,
                "DCD",
                "BAB",
                "EDE",
                ('A'),
                IUItem.nightvision,
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                ('C'),
                IUItem.energy_crystal,
                ('D'),
                IUItem.carbonPlate, 'E', IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(
                IUItem.nano_chestplate,
                "DCD",
                "BAB",
                "EDE",
                ('A'),
                Items.DIAMOND_CHESTPLATE,
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                ('C'),
                IUItem.energy_crystal,
                ('D'),
                IUItem.carbonPlate, 'E', IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(
                IUItem.nano_leggings,
                "DCD",
                "BAB",
                "EDE",
                ('A'),
                Items.DIAMOND_LEGGINGS,
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                ('C'),
                IUItem.energy_crystal,
                ('D'),
                IUItem.carbonPlate, 'E', IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(
                IUItem.nano_boots,
                "DCD",
                "BAB",
                "EDE",
                ('A'),
                Items.DIAMOND_BOOTS,
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                ('C'),
                IUItem.energy_crystal,
                ('D'),
                IUItem.carbonPlate, 'E', IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(
                IUItem.adv_nano_helmet,
                "DCD",
                "BAB",
                "EFE",
                ('A'),
                IUItem.nano_helmet,
                ('B'),
                "c:doubleplate/Ferromanganese",
                ('C'),
                IUItem.lapotron_crystal,
                ('D'),
                IUItem.compresscarbon, 'E', IUItem.advnanobox, 'F', IUItem.compresscarbon
        );
        Recipes.recipe.addRecipe(
                IUItem.adv_nano_chestplate,
                "DCD",
                "BAB",
                "EFE",
                ('A'),
                IUItem.nano_chestplate,
                ('B'),
                "c:doubleplate/Ferromanganese",
                ('C'),
                IUItem.lapotron_crystal,
                ('D'),
                IUItem.compresscarbon, 'E', IUItem.advnanobox, 'F', IUItem.electricJetpack
        );
        Recipes.recipe.addRecipe(
                IUItem.adv_nano_boots,
                "DCD",
                "BAB",
                "EFE",
                ('A'),
                IUItem.nano_boots,
                ('B'),
                "c:doubleplate/Ferromanganese",
                ('C'),
                IUItem.lapotron_crystal,
                ('D'),
                IUItem.compresscarbon, 'E', IUItem.advnanobox, 'F', IUItem.compresscarbon
        );
        Recipes.recipe.addRecipe(
                IUItem.adv_nano_leggings,
                "DCD",
                "BAB",
                "EFE",
                ('A'),
                IUItem.nano_leggings,
                ('B'),
                "c:doubleplate/Ferromanganese",
                ('C'),
                IUItem.lapotron_crystal,
                ('D'),
                IUItem.compresscarbon, 'E', IUItem.advnanobox, 'F', IUItem.compresscarbon
        );
        Recipes.recipe.addRecipe(
                IUItem.quantum_helmet,
                "DCD",
                "BAB",
                "EFE",
                ('A'),
                IUItem.adv_nano_helmet,
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                ('C'),
                IUItem.AdvlapotronCrystal,
                ('D'),
                IUItem.iridiumPlate, 'E', IUItem.advQuantumtool, 'F', IUItem.hazmat_helmet
        );
        Recipes.recipe.addRecipe(
                IUItem.quantum_chestplate,
                "DCD",
                "BAB",
                "EFE",
                ('A'),
                IUItem.adv_nano_chestplate,
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                ('C'),
                IUItem.AdvlapotronCrystal,
                ('D'),
                IUItem.iridiumPlate, 'E', IUItem.advQuantumtool, 'F', IUItem.impjetpack
        );
        Recipes.recipe.addRecipe(
                IUItem.quantum_leggings,
                "DCD",
                "BAB",
                "EFE",
                ('A'),
                IUItem.adv_nano_leggings,
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                ('C'),
                IUItem.AdvlapotronCrystal,
                ('D'),
                IUItem.iridiumPlate, 'E', IUItem.advQuantumtool, 'F', IUItem.adv_bags
        );
        Recipes.recipe.addRecipe(
                IUItem.quantum_boots,
                "DCD",
                "BAB",
                "EFE",
                ('A'),
                IUItem.adv_nano_boots,
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                ('C'),
                IUItem.AdvlapotronCrystal,
                ('D'),
                IUItem.iridiumPlate, 'E', IUItem.advQuantumtool, 'F', IUItem.rubber_boots
        );
        Recipes.recipe.addRecipe(
                IUItem.spectral_helmet,
                "DCD",
                "BAB",
                "EFE",
                ('A'),
                IUItem.quantum_helmet,
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                ('C'),
                IUItem.AdvlapotronCrystal,
                ('D'),
                IUItem.doublecompressIridiumplate, 'E', IUItem.adv_spectral_box, 'F', IUItem.hazmathelmet
        );
        Recipes.recipe.addRecipe(
                IUItem.spectral_chestplate,
                "DCD",
                "BAB",
                "EFE",
                ('A'),
                IUItem.quantum_chestplate,
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                ('C'),
                IUItem.AdvlapotronCrystal,
                ('D'),
                IUItem.doublecompressIridiumplate, 'E', IUItem.adv_spectral_box, 'F', IUItem.impjetpack
        );
        Recipes.recipe.addRecipe(
                IUItem.spectral_leggings,
                "DCD",
                "BAB",
                "EFE",
                ('A'),
                IUItem.quantum_leggings,
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                ('C'),
                IUItem.AdvlapotronCrystal,
                ('D'),
                IUItem.doublecompressIridiumplate, 'E', IUItem.adv_spectral_box, 'F', IUItem.impmagnet
        );
        Recipes.recipe.addRecipe(
                IUItem.spectral_boots,
                "DCD",
                "BAB",
                "EFE",
                ('A'),
                IUItem.quantum_boots,
                ('B'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                ('C'),
                IUItem.AdvlapotronCrystal,
                ('D'),
                IUItem.doublecompressIridiumplate, 'E', IUItem.adv_spectral_box, 'F', IUItem.compressAlloy
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 437), " AA", "BCA", "BB ", 'A', "c:plates/Steel", 'B',
                IUItem.advancedAlloy, 'C', "c:rods/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 337), " AA", "A A", "AA ", 'A', "c:plates/Iron");
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 339), " AA", "A A", "AA ", 'A', "c:plates/Gold");
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 305), " AA", "A A", "AA ", 'A', "c:gems/Diamond");
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 341), " AA", "A A", "AA ", 'A', "c:gems/Emerald");


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 433), "BAB", "ABA", "BAB",

                ('A'), "c:plates/Zinc",


                ('B'), "c:plates/Iridium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 367),
                " A ", "ABA", "CCC", 'A', "c:plates/Beryllium", 'B', "c:plates/Iron", 'C', "c:plates/Bor"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 415),
                " A ", "ABA", "CCC", 'A', "c:plates/Gold", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 367), 'C', "c:plates/Electrum"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 380),
                " A ",
                "ABA",
                "CCC",
                'A',
                "c:plates/Mikhail",
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 415),
                'C',
                "c:plates/vanady"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 396),
                " A ", "ABA", "CCC", 'A', "c:plates/Cadmium", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 380), 'C', "c:plates/Spinel"
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 336),
                "CDC",
                "BAB",
                "CDC",
                'A',
                ItemStackHelper.fromData(IUItem.core, 1, 2),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'C',
                "c:ingots/Osmium",
                'D',
                "c:plates/Platinum"
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 335),
                "CDC",
                "BAB",
                "CDC",
                'A',
                ItemStackHelper.fromData(IUItem.core, 1, 2),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'C',
                "c:ingots/Osmium",
                'D',
                "c:plates/Titanium"
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 334),
                "CDC",
                "BAB",
                "CDC",
                'A',
                ItemStackHelper.fromData(IUItem.core, 1, 2),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'C',
                "c:ingots/Osmium",
                'D',
                "c:plates/Zinc"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 333),
                "CDC",
                "BAB",
                "CDC",
                'A',
                ItemStackHelper.fromData(IUItem.core, 1, 2),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'C',
                "c:ingots/Osmium",
                'D',
                "c:plates/Tungsten"
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 427),
                "BCB",
                "DAD",
                " ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 435),
                'B',
                IUItem.nanoBox,
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                'D',
                "c:plateDense/Gold"
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 374),
                "BCB",
                "DAD",
                " ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 427),
                'B',
                IUItem.quantumtool,
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                'D',
                "c:doubleplate/Osmium"
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 403),
                "BCB",
                "DAD",
                " ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 374),
                'B',
                IUItem.spectral_box,
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                'D',
                "c:doubleplate/Osmium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 428),
                "BCB", "DAD", " ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 436), 'B', IUItem.nanoBox, 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),
                'D', "c:plateDense/Gold"
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 379),
                "BCB",
                "DAD",
                " ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 428),
                'B',
                IUItem.quantumtool,
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                'D',
                "c:doubleplate/Osmium"
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 404),
                "BCB",
                "DAD",
                " ",
                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 379),
                'B',
                IUItem.spectral_box,
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                'D',
                "c:doubleplate/Osmium"
        );
        // water reactor
        // 366
        // 420
        // 378
        // 405
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 0),
                "AA ", "AA ", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 366)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 1),
                "AA ", "AA ", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 420)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 2),
                "AA ", "AA ", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 378)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 3),
                "AA ", "AA ", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 405)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 4),
                "  ", "BAB", "CDC", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 0), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 322), 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 5),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 4)
                , 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 420), 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 6),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 5), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 378), 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 7),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 6), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 405), 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 8),
                "EDE", "BAB", "FCF", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 0), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 435), 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 342), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 322)
                , 'E', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414), 'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 9),
                "BCB", "DAD", "BEB", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 8), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 420), 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 427), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 10),
                "BCB", "DAD", "BEB", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 9), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 378), 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 374)
                , 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'E', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 11),
                "BCB", "DAD", "BEB", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 10), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 405), 'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 403), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 12),
                "CBC",
                "BAB",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 0),
                'B',
                IUItem.fluidpullingUpgrade,
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 13),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 12)
                , 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 420)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 14),
                "BDB",
                "CAC",
                "B B",
                'A',
                ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 13),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 378),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 15),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 14), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 405)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        BasicRecipeThree.recipe();
    }

    public static ItemStack getBlockStack(IMultiTileBlock block) {
        return TileBlockCreator.instance.get(block.getIDBlock()).getItemStack();
    }

}
