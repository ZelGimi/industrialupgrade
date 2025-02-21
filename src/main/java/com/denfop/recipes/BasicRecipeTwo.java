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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

public class BasicRecipeTwo {

    public static ItemStack DEFAULT_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 21);

    public static ItemStack ADV_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 25);

    public static ItemStack IMP_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 23);

    public static ItemStack PER_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 24);
    public static ItemStack PHOTON_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 620);

    public static void recipe() {

        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 1), "BBB", "CAC", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateStainlessSteel",

                ('C'), new ItemStack(Items.DYE, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 4), "BBB", " A ", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateStainlessSteel",

                ('C'), new ItemStack(Items.DYE, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 2), "BBB", "CAC", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateStellite",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 3), "BBB", "CAC", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateStellite",

                ('C'), new ItemStack(Items.DYE, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 5), "BBB", " A ", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateStellite",

                ('C'), new ItemStack(Items.DYE, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.simplemachine, 1, 0), " B ", "DAE", "C H",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 72), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 69), 'H', getBlockStack(BlockBaseMachine3.steam_macerator)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.simplemachine, 1, 1), " B ", "DAE", "C H",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 72), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 63), 'H', getBlockStack(BlockBaseMachine3.steam_compressor)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.simplemachine, 1, 2), "B H", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 72), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        219
                ), ('H'), new ItemStack(IUItem.crafting_elements, 1, 35)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.simplemachine, 1, 3), " B ", "DAE", "C H",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 72), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 159), 'H', getBlockStack(BlockBaseMachine3.steam_extractor)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.simplemachine, 1, 5), " B ", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 72), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 33)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base2, 1, 0), " B ", "DAE", "C H",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 72), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 165), 'H', getBlockStack(BlockBaseMachine3.rolling_machine)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base2, 1, 4), " B ", "DAE", "C H",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 72), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 163), 'H', getBlockStack(BlockBaseMachine3.steam_extruder)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base2, 1, 8), " B ", "DAE", "C H",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 72), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 132), 'H', getBlockStack(BlockBaseMachine3.steam_cutting)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 4), "B F", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 72), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        33
                ), ('F'), new ItemStack(IUItem.crafting_elements, 1, 128)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 0), "B F", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 72), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        64
                ), ('F'), new ItemStack(IUItem.crafting_elements, 1, 61)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 16), " B ", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 72), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 218)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 12), " B ", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 70), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 39)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 8), "BGF", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 72), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        34
                ), ('F'), new ItemStack(IUItem.crafting_elements, 1, 27),
                ('G'), new ItemStack(IUItem.crafting_elements, 1, 154)
        );

        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base, 1, 0), "A A",
                "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 2),
                ('B'), new ItemStack(IUItem.simplemachine, 1, 0)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 1),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 78),
                ('B'),
                new ItemStack(IUItem.machines_base, 1, 0)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 2),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 103),
                ('B'),
                new ItemStack(IUItem.machines_base, 1, 1)
        ), 2);
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 0), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 623),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 622),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 52),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 602),
                ('B'), new ItemStack(IUItem.machines_base, 1, 2)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 1), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 623),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 622),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 52),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 608),
                ('B'), new ItemStack(IUItem.machines_base, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 2), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 623),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 622),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 52),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 615),
                ('B'), new ItemStack(IUItem.machines_base, 1, 11)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 3), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 623),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 622),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 52),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 617),
                ('B'), new ItemStack(IUItem.machines_base, 1, 8)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 4), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 623),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 622),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 52),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 609),
                ('B'), new ItemStack(IUItem.machines_base2, 1, 3)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 5), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 623),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 622),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 52),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 616),
                ('B'), new ItemStack(IUItem.machines_base2, 1, 7)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 6), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 623),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 622),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 52),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 612),
                ('B'), new ItemStack(IUItem.machines_base2, 1, 11)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 11), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 623),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 622),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 52),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 606),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 19)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 9), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 623),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 622),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 52),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 613),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 15)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 8), "AHA", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 623),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 622),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 52),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 607),
                ('H'),
                new ItemStack(IUItem.crafting_elements, 1, 605),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 7)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 7), "AHA", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 623),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 622),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 52),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 611),
                ('H'),
                new ItemStack(IUItem.crafting_elements, 1, 604),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 3)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 10), "AHA", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 623),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 622),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 52),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 610),
                ('H'),
                new ItemStack(IUItem.crafting_elements, 1, 603),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 11)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 11), " C ", "BAB", "   ",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 21),

                ('B'), "plateIron",

                ('C'), new ItemStack(IUItem.advBattery, 1, 32767)
        );
        Recipes.recipe.addShapelessRecipe(
                getBlockStack(BlockBaseMachine3.generator_iu),
                new ItemStack(IUItem.crafting_elements, 1, 11), IUItem.machine
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 55), "DCD", "BAB", "CCC",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 21),

                ('B'), "plateIron",

                ('C'), new ItemStack(IUItem.sunnarium, 1, 4),

                ('D'),
                "plateGermanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.chemicalPlant, 1, 2), "DBD", "CAC", "DBD",

                ('A'), new ItemStack(IUItem.blockResource, 1, 8),

                ('B'), "plateCobaltChrome",

                ('C'), "plateNiobiumTitanium",

                ('D'),
                new ItemStack(IUItem.synthetic_plate, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.geothermalpump, 1, 2), " B ", "CAC", " B ",

                ('A'), new ItemStack(IUItem.blockResource, 1, 8),

                ('B'), "plateWoods",

                ('C'), "platePermalloy",

                ('D'),
                new ItemStack(IUItem.synthetic_plate, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.chemicalPlant, 1, 1), " B ", "BAB", " B ",

                ('A'), new ItemStack(IUItem.chemicalPlant, 1, 2),

                ('B'), new ItemStack(IUItem.heat_exchange, 1),

                ('C'), "plateNiobiumTitanium",

                ('D'),
                new ItemStack(IUItem.synthetic_plate, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.geothermalpump, 1, 1), " B ", "BAB", " B ",

                ('A'), new ItemStack(IUItem.geothermalpump, 1, 2),

                ('B'), new ItemStack(IUItem.heat_exchange, 1),

                ('C'), "plateNiobiumTitanium",

                ('D'),
                new ItemStack(IUItem.synthetic_plate, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.chemicalPlant, 1, 6), "BBB", " A ", "   ",

                ('A'), new ItemStack(IUItem.chemicalPlant, 1, 2),

                ('B'), new ItemStack(IUItem.basemachine2, 1, 186),

                ('C'), "plateNiobiumTitanium",

                ('D'),
                new ItemStack(IUItem.synthetic_plate, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.geothermalpump, 1, 6), "BBB", " A ", "   ",

                ('A'), new ItemStack(IUItem.geothermalpump, 1, 2),

                ('B'), new ItemStack(IUItem.basemachine2, 1, 186),

                ('C'), "plateNiobiumTitanium",

                ('D'),
                new ItemStack(IUItem.synthetic_plate, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.chemicalPlant, 1, 0), "CDC", "BAB", "   ",

                ('A'), new ItemStack(IUItem.chemicalPlant, 1, 2),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 373),

                ('C'), IUItem.photoniy_ingot,

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.geothermalpump, 1, 0), "CDC", "BAB", "   ",

                ('A'), new ItemStack(IUItem.geothermalpump, 1, 2),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 373),

                ('C'), IUItem.photoniy_ingot,

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.chemicalPlant, 1, 5), "CDC", "BAB", "   ",

                ('A'), new ItemStack(IUItem.chemicalPlant, 1, 2),

                ('B'), new ItemStack(IUItem.alloygear, 1, 20),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 92),

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.chemicalPlant, 1, 3), " C ", "BAB", "   ",

                ('A'), new ItemStack(IUItem.chemicalPlant, 1, 2),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 373),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 372),

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.geothermalpump, 1, 3), " B ", "BAB", "   ",

                ('A'), new ItemStack(IUItem.geothermalpump, 1, 2),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 509),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 372),

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.geothermalpump, 1, 4), " C ", "BAB", "   ",

                ('A'), new ItemStack(IUItem.geothermalpump, 1, 2),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 373),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 372),

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.chemicalPlant, 1, 4), " C ", "BAB", " C ",

                ('A'), new ItemStack(IUItem.chemicalPlant, 1, 2),

                ('B'), new ItemStack(IUItem.item_pipes, 1, 1),

                ('C'), new ItemStack(IUItem.item_pipes, 1, 0),

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.geothermalpump, 1, 5), " C ", "BAB", " C ",

                ('A'), new ItemStack(IUItem.geothermalpump, 1, 2),

                ('B'), new ItemStack(IUItem.item_pipes, 1, 1),

                ('C'), new ItemStack(IUItem.item_pipes, 1, 0),

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 59), "BCB", "DAD", "ECE",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 21),

                ('B'), "doubleplateMuntsa",

                ('C'), "doubleplateAlcled",

                ('D'),
                "plateZinc",

                ('E'), "plateCaravky"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cyclotron, 1, 6), " C ", "DAD", " C ",

                ('A'), new ItemStack(IUItem.blockResource, 1, 8),

                ('B'), "doubleplateMuntsa",

                ('C'), "plateHafniumBoride",

                ('D'),
                "plateTantalumTungstenHafnium",

                ('E'), "plateCaravky"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cyclotron, 1, 1), " C ", "DAD", "   ",

                ('A'), new ItemStack(IUItem.cyclotron, 1, 6),

                ('B'), "doubleplateMuntsa",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 352),

                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 402),

                ('E'), "plateCaravky"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cyclotron, 1, 3), " C ", "DAD", "   ",

                ('A'), new ItemStack(IUItem.cyclotron, 1, 6),

                ('B'), "doubleplateMuntsa",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 43),

                ('D'),
                IUItem.cooling_mixture,

                ('E'), "plateCaravky"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cyclotron, 1, 2), " C ", "DAD", "   ",

                ('A'), new ItemStack(IUItem.cyclotron, 1, 6),

                ('B'), "doubleplateMuntsa",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 43),

                ('D'),
                IUItem.cryogenic_cooling_mixture,

                ('E'), "plateCaravky"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cyclotron, 1, 7), " C ", "DAD", "   ",

                ('A'), new ItemStack(IUItem.cyclotron, 1, 6),

                ('B'), "doubleplateMuntsa",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 155),

                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('E'), "plateCaravky"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cyclotron, 1, 4), " C ", " A ", " D ",

                ('A'), new ItemStack(IUItem.cyclotron, 1, 6),

                ('B'), "doubleplateMuntsa",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 470),

                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 44),

                ('E'), "plateCaravky"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cyclotron, 1, 8), " C ", " A ", " D ",

                ('A'), new ItemStack(IUItem.cyclotron, 1, 6),

                ('B'), "doubleplateMuntsa",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 110),

                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 44),

                ('E'), "plateCaravky"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cyclotron, 1, 0), "BCB", "DAD", "   ",

                ('A'), new ItemStack(IUItem.cyclotron, 1, 6),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 398),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 402),

                ('E'), "plateCaravky"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cyclotron, 1, 5), " C ", " A ", "   ",

                ('A'), new ItemStack(IUItem.cyclotron, 1, 6),

                ('B'), "doubleplateMuntsa",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 68),

                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 402),

                ('E'), "plateCaravky"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 65), "BBB", " A ", "CCC",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 21),

                ('B'), "plateGermanium",

                ('C'), "plateChromium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 22), "BCB", "BCB", " A ",
                ('A'), DEFAULT_SENSOR, ('B'), Blocks.GLASS,
                ('C'), IUItem.FluidCell
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 5), "BCB", "BCB", " A ",
                ('A'), ADV_SENSOR, ('B'), Blocks.GLASS,
                ('C'), IUItem.FluidCell
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 82), "BCB", "BCB", " A ",
                ('A'), IMP_SENSOR, ('B'), Blocks.GLASS,
                ('C'), IUItem.FluidCell
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 106), "BCB", "BCB", " A ",
                ('A'), PER_SENSOR, ('B'), Blocks.GLASS,
                ('C'), IUItem.FluidCell
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 630), "BCB", "BCB", " A ",
                ('A'), PHOTON_SENSOR, ('B'), Blocks.GLASS,
                ('C'), IUItem.FluidCell
        );
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 6),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 225),
                ('B'),
                new ItemStack(IUItem.simplemachine, 1, 2)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 7),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 221),
                ('B'),
                new ItemStack(IUItem.machines_base, 1, 6)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 8),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 223),
                ('B'),
                new ItemStack(IUItem.machines_base, 1, 7)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 9),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 160),
                ('B'),
                new ItemStack(IUItem.simplemachine, 1, 3)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 10),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 161),
                ('B'),
                new ItemStack(IUItem.machines_base, 1, 9)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 11),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 162),
                ('B'),
                new ItemStack(IUItem.machines_base, 1, 10)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 3),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 1),
                ('B'),
                new ItemStack(IUItem.simplemachine, 1, 1)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 4),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 77),
                ('B'),
                new ItemStack(IUItem.machines_base, 1, 3)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 5),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 102),
                ('B'),
                new ItemStack(IUItem.machines_base, 1, 4)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base1, 1, 0),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 9),
                ('B'),
                new ItemStack(IUItem.simplemachine, 1, 5)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base1, 1, 1),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 86),
                ('B'),
                new ItemStack(IUItem.machines_base1, 1, 0)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base1, 1, 2),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 110),
                ('B'),
                new ItemStack(IUItem.machines_base1, 1, 1)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 1),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 166),
                ('B'),
                new ItemStack(IUItem.machines_base2, 1, 0)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 2),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 167),
                ('B'),
                new ItemStack(IUItem.machines_base2, 1, 1)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 3),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 168),
                ('B'),
                new ItemStack(IUItem.machines_base2, 1, 2)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 5),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 125),
                ('B'),
                new ItemStack(IUItem.machines_base2, 1, 4)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 6),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 126),
                ('B'),
                new ItemStack(IUItem.machines_base2, 1, 5)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 7),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 127),
                ('B'),
                new ItemStack(IUItem.machines_base2, 1, 6)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 9),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 133),
                ('B'),
                new ItemStack(IUItem.machines_base2, 1, 8)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 10),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 134),
                ('B'),
                new ItemStack(IUItem.machines_base2, 1, 9)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 11),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 136),
                ('B'),
                new ItemStack(IUItem.machines_base2, 1, 10)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 17),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 224),
                ('B'),
                new ItemStack(IUItem.machines_base3, 1, 16)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 18),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 220),
                ('B'),
                new ItemStack(IUItem.machines_base3, 1, 17)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 19),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 222),
                ('B'),
                new ItemStack(IUItem.machines_base3, 1, 18)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 1),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 19),
                ('B'),
                new ItemStack(IUItem.machines_base3, 1, 0),
                ('G'),
                new ItemStack(IUItem.crafting_elements, 1, 18)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 2),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 95),
                ('B'),
                new ItemStack(IUItem.machines_base3, 1, 1),
                ('G'),
                new ItemStack(IUItem.crafting_elements, 1, 94)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 3),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 119),
                ('B'),
                new ItemStack(IUItem.machines_base3, 1, 2),
                ('G'),
                new ItemStack(IUItem.crafting_elements, 1, 118)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 5),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 9),
                ('B'),
                new ItemStack(IUItem.machines_base3, 1, 4),
                ('G'),
                new ItemStack(IUItem.crafting_elements, 1, 129)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 6),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 86),
                ('B'),
                new ItemStack(IUItem.machines_base3, 1, 5),
                ('G'),
                new ItemStack(IUItem.crafting_elements, 1, 130)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 7),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 110),
                ('B'),
                new ItemStack(IUItem.machines_base3, 1, 6),
                ('G'),
                new ItemStack(IUItem.crafting_elements, 1, 131)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 13),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 15),
                ('B'),
                new ItemStack(IUItem.machines_base3, 1, 12)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 14),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 91),
                ('B'),
                new ItemStack(IUItem.machines_base3, 1, 13)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 15),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 115),
                ('B'),
                new ItemStack(IUItem.machines_base3, 1, 14)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 9),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 12),
                ('B'),
                new ItemStack(IUItem.machines_base3, 1, 8),
                ('G'),
                new ItemStack(IUItem.crafting_elements, 1, 6)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 10),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 87),
                ('B'),
                new ItemStack(IUItem.machines_base3, 1, 9),
                ('G'),
                new ItemStack(IUItem.crafting_elements, 1, 83)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 11),
                "AGA",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 111),
                ('B'),
                new ItemStack(IUItem.machines_base3, 1, 10),
                ('G'),
                new ItemStack(IUItem.crafting_elements, 1, 107)
        ), 2);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base1, 1, 3),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 135),
                ('B'),
                new ItemStack(IUItem.machines_base1, 1, 2)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base1, 1, 4),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 146),
                ('B'),
                new ItemStack(IUItem.machines_base1, 1, 3)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base1, 1, 5),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 157),
                ('B'),
                new ItemStack(IUItem.machines_base1, 1, 4)
        ), 2);
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base1, 1, 6), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 137),
                ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 44),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 124),
                ('B'), new ItemStack(IUItem.machines_base, 1, 2)
        );
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base1, 1, 7),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 135),
                ('B'),
                new ItemStack(IUItem.machines_base1, 1, 6)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base1, 1, 8),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 146),
                ('B'),
                new ItemStack(IUItem.machines_base1, 1, 7)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines_base1, 1, 9),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 157),
                ('B'),
                new ItemStack(IUItem.machines_base1, 1, 8)
        ), 2);
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.pho_machine, 1, 20),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 623),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 622),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 157),
                ('B'),
                new ItemStack(IUItem.machines_base1, 1, 5)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.pho_machine, 1, 13),
                "A A",
                "DBE",
                "ACA",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 623),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 622),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 52),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 632),
                ('B'),
                new ItemStack(IUItem.machines_base1, 1, 9)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 45), "   ", "CAB", "   ",
                ('A'), IUItem.machine,
                ('C'), IUItem.elemotor, ('B'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        101
                )
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 46), "D D", "CAB", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 45),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        20
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 4), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 47), "D D", "CAB", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 46),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        96
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 81), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 48), "D D", "CAB", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 47),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 105), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 140)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 49), "   ", "CAB", "   ",
                ('A'), IUItem.advancedMachine,
                ('C'), IUItem.elemotor, ('B'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        170
                )
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 50), "D D", "CAB", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 49),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        20
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 171), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 51), "D D", "CAB", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 50),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        96
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 172), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 52), "D D", "CAB", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 51),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 173), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 140)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 14), "D D", "CAB", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 52),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        622
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 635), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 623)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 53), " E ", "CAB", " F ",
                ('A'), IUItem.advancedMachine,
                ('C'), IUItem.elemotor, ('B'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        58
                ),
                ('E'),
                new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        27
                ), ('F'), new ItemStack(IUItem.crafting_elements, 1, 154)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 54), "DED", "CAB", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 53),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        20
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 17), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 138),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 6)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 55), "DED", "CAB", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 54),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        96
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 93), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 139),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 83)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 56), "DED", "CAB", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 55),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 117), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 140),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 107)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 15), "DED", "CAB", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 56),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        622
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 634), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 623),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 603)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 57), " B ", " A ", "   ",
                ('A'), IUItem.advancedMachine,
                ('C'), IUItem.elemotor, ('B'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        79
                )
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 58), "DBD", " A ", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 57),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        20
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 176), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 59), "DBD", " A ", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 58),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        96
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 174), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 60), "DBD", " A ", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 59),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 175), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 140)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 19), "DBD", " A ", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 60),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 627), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 623)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 61), " B ", " A ", "   ",
                ('A'), IUItem.advancedMachine,
                ('C'), IUItem.elemotor, ('B'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        68
                )
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 62), "DBD", " A ", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 61),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        20
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 179), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 63), "DBD", " A ", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 62),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        96
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 177), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 64), "DBD", " A ", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 63),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 178), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 140)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 18), "DBD", " A ", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 64),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 626), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 623)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 65), " B ", " A ", "   ",
                ('A'), IUItem.machine,
                ('C'), IUItem.elemotor, ('B'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        196
                )
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 66), "DBD", " A ", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 65),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        20
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 199), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 67), "DBD", " A ", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 66),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        96
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 197), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 68), "DBD", " A ", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 67),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 198), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 140)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 17), "DBD", " A ", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 68),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 636), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 623)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 28), "   ", "CAB", "   ",
                ('A'), IUItem.machine,
                ('C'), IUItem.elemotor, ('B'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        71
                )
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 29), "D D", "CAB", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 28),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        20
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 3), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 30), "D D", "CAB", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 29),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        96
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 80), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 31), "D D", "CAB", "D D",
                ('A'), new ItemStack(IUItem.basemachine2, 1, 30),
                ('C'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        120
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 104), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 140)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines, 1, 4), " B ", "CAD", " E ",

                ('A'), IUItem.machine, ('B'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        74
                ),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 70), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'), IUItem.elemotor
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine, 1, 3), "F F", "BAD", "FEF",

                ('A'), new ItemStack(
                        IUItem.machines,
                        1,
                        4
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 235),
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 138), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 20)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 154), "F F", "BAD", "FEF",

                ('A'), new ItemStack(IUItem.basemachine, 1, 3), ('B'), new ItemStack(IUItem.crafting_elements, 1, 618),
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 139), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 96)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 155), "FGF", "BAD", "FEF",

                ('A'), new ItemStack(IUItem.basemachine2, 1, 154), ('B'), new ItemStack(IUItem.crafting_elements, 1, 619),
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 140), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 120), 'G', new ItemStack(IUItem.crafting_elements, 1, 44)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines, 1, 6), "B F", "CAD", " E ",

                ('A'), IUItem.machine, ('B'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        73
                ),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 70), ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('E'), IUItem.elemotor, ('F'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        51
                )
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 5), "DBD", " A ", " C ",

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 70),

                ('C'), IUItem.imp_motors_with_improved_bearings_,

                ('A'), IUItem.advancedMachine,

                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 11)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 6), "D D", "BAE", " C ",

                ('A'), IUItem.machine,

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 70),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 11),

                ('C'),
                IUItem.elemotor,
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 27)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines, 1, 7), " H ", "BAE", "DCF",

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 47),

                ('C'), IUItem.elemotor,
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 227),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 230),
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 231),
                ('H'), new ItemStack(IUItem.crafting_elements, 1, 11)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 10), "BAB", " C ", " D ",

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 96),

                ('C'), new ItemStack(IUItem.basemachine2, 1, 152),

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 11),

                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 72)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine, 1, 12), " D ", "BAC", "E H",

                ('A'), IUItem.machine, ('B'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        70
                ),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 44), ('E'),
                IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 205), 'H', getBlockStack(BlockBaseMachine3.steam_handler_ore)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 40), "BDB", " A ", "BEB",

                ('A'), new ItemStack(
                        IUItem.basemachine,
                        1,
                        12
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 138),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 204)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 41), "BDB", " A ", "BEB",

                ('A'), new ItemStack(
                        IUItem.basemachine2,
                        1,
                        40
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 139),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 202)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 42), "BDB", " A ", "BEB",

                ('A'), new ItemStack(
                        IUItem.basemachine2,
                        1,
                        41
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 140),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 203)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 16), "BDB", " A ", "BEB",

                ('A'), new ItemStack(
                        IUItem.basemachine2,
                        1,
                        42
                ), ('B'), new ItemStack(IUItem.crafting_elements, 1, 623),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 622),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 633)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine, 1, 11), " E ", "DAB", " C ",

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 47),

                ('C'), IUItem.elemotor,

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 100),

                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 99),

                ('A'), IUItem.advancedMachine
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine, 1, 10), "   ", "AEB", " C ",

                ('E'), IUItem.advancedMachine,

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),

                ('C'), IUItem.elemotor,

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 47),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 99)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.sunnariummaker), " E ", "BAC", " D ",

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 128),

                ('D'), IUItem.elemotor,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 79),

                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 47),

                ('A'), IUItem.machine
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.sunnariumpanelmaker), "CAB",

                ('D'), IUItem.carbonPlate,

                ('C'), new ItemStack(IUItem.blockSE),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 55),

                ('A'), new ItemStack(IUItem.sunnariummaker)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine, 1, 13), " B ", "DAE", " C ",

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 44),
                ('C'), IUItem.elemotor,
                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 28),
                ('A'), IUItem.advancedMachine
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine, 1, 15), "   ", "CBD", " A ",

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 29),

                ('A'), IUItem.elemotor,

                ('B'), IUItem.machine,

                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 11)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine, 1, 14), "CDC", "ABA", "CDC",

                ('D'),
                ("plateNichrome"),
                ('A'), new ItemStack(IUItem.impmagnet, 1, 32767),

                ('B'), new ItemStack(IUItem.basemachine, 1, 15),

                ('C'),
                new ItemStack(IUItem.compresscarbon)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine1, 1, 1), " B ", "DAD", "DCD",

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 121),

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),

                ('A'), IUItem.advancedMachine,

                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 217),

                ('C'), IUItem.elemotor
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine1, 1, 3), "   ", "BAD", " C ",

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 62),

                ('C'), IUItem.elemotor,

                ('A'), IUItem.machine,

                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 47)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 44), "   ", "BAD", "ECF",

                ('A'), IUItem.machine,

                ('C'), IUItem.elemotor,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('E'), Items.WATER_BUCKET,
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 243)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 43), " E ", "BAD", "FCG",

                ('A'), IUItem.machine,

                ('C'), IUItem.elemotor,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 66),
                ('G'), new ItemStack(IUItem.crafting_elements, 1, 67),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 101)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 8), "DED", "CAC", "CBC",

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 67),

                ('D'),
                ("plateNichrome"),
                ('C'),
                ("plateCaravky"),
                ('A'),
                IUItem.upgradeblock,

                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.upgradeblock), " E ", "CAD", " B ",

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 66),

                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('A'), IUItem.advancedMachine,

                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'), IUItem.elemotor
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 226), "BBB", "CAC", "DED",

                ('D'),
                ("plateAlumel"),
                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('A'), DEFAULT_SENSOR,

                ('B'),
                IUItem.iridiumPlate,

                ('C'), new ItemStack(IUItem.plastic_plate)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 227), "CCC", "BAB", "   ",

                ('A'), DEFAULT_SENSOR, ('B'), "gearAluminium", ('C'), Blocks.COBBLESTONE
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 229), "CCC", "BAB", "   ",

                ('A'), DEFAULT_SENSOR, ('B'), "gearZinc", ('C'), Blocks.OBSIDIAN
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 230), "CCC", "BAB", "   ",

                ('A'), DEFAULT_SENSOR, ('B'), "gearManganese", ('C'), Blocks.SAND
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 231), "CCC", "BAB", "   ",

                ('A'), DEFAULT_SENSOR, ('B'), "gearSpinel", ('C'), Blocks.GRAVEL
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 228),
                "CCC",
                "BAB",
                "   ",

                ('A'),
                DEFAULT_SENSOR,
                ('B'),
                Items.LAVA_BUCKET,
                ('C'),
                "plateRedbrass"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 234), " C ", "BAB", "   ",
                ('A'), ADV_SENSOR, ('B'), "plateIron", ('C'), new ItemStack(
                        IUItem.energy_crystal,
                        1,
                        32767
                )
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 232), " C ", "BAB", "   ",
                ('A'), IMP_SENSOR, ('B'), "plateIron", ('C'), new ItemStack(
                        IUItem.AdvlapotronCrystal,
                        1,
                        32767
                )
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 233), " C ", "BAB", "   ",
                ('A'), PER_SENSOR, ('B'), "plateIron", ('C'), new ItemStack(
                        IUItem.AdvlapotronCrystal,
                        1,
                        32767
                )
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 629), " C ", "BAB", "   ",
                ('A'), PHOTON_SENSOR, ('B'), "plateIron", ('C'), new ItemStack(
                        IUItem.AdvlapotronCrystal,
                        1,
                        32767
                )
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 235),
                "B B",
                "DAD",
                "CCC",
                ('A'),
                ADV_SENSOR,
                ('B'),
                "plateAlumel",
                ('C'),
                "plateFerromanganese",

                ('D'),
                "plateDuralumin"
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 618),
                "B B",
                "DAD",
                "CCC",
                ('A'),
                IMP_SENSOR,
                ('B'),
                "plateVanadoalumite",
                ('C'),
                "plateAluminiumSilicon",

                ('D'),
                "plateVitalium"
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 619),
                "B B",
                "DAD",
                "CCC",
                ('A'),
                PER_SENSOR,
                ('B'),
                "plateStainlessSteel",
                ('C'),
                "plateInconel",

                ('D'),
                "platePermalloy"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 236), "B B", "BAB", "B B",

                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid("iufluidbenz")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 237), "B B", "BAB", "B B",

                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid("iufluidhyd")
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 238),
                "BCB",
                "BAB",
                "BCB",

                ('A'),
                DEFAULT_SENSOR,
                ('B'),
                IUItem.FluidCell,
                ('C'),
                "plateVanadium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 239), "B B", "BAB", "B B",

                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid("iufluiddizel")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 240), "B B", "BAB", "B B",

                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid(FluidName.fluiduu_matter.getInstance())
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 243), "CDC", "BAB", "E E",

                ('A'), DEFAULT_SENSOR,

                ('B'), "plateChromium",

                ('C'), "plateCobalt",

                ('D'),
                "plateNichrome",

                ('E'), "plateAlcled"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 244), " D ", "BAC", "DDD",

                ('A'), DEFAULT_SENSOR, ('B'), new ItemStack(
                        IUItem.anode,
                        1,
                        32767
                ), ('C'), new ItemStack(IUItem.cathode, 1, 32767),
                ('D'),
                "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 241), " B ", "BAB", " B ",
                ('A'), DEFAULT_SENSOR, ('B'), "plateDenseIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 248), " B ", "BAB", " B ",
                ('A'), ADV_SENSOR, ('B'), "plateDenseIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 245), " B ", "BAB", " B ",
                ('A'), IMP_SENSOR, ('B'), "plateDenseIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 242), " B ", "BAB", " B ",
                ('A'), DEFAULT_SENSOR, ('B'), "plateLead"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 249), " A ", "ABA", " A ",
                ('A'), new ItemStack(IUItem.nanoBox), ('B'), ADV_SENSOR
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 246), "CAC", "ABA", "CAC",

                ('C'), new ItemStack(IUItem.photoniy),

                ('A'), new ItemStack(IUItem.quantumtool),

                ('B'), IMP_SENSOR
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 247), "CAC", "ABA", "CAC",

                ('C'), new ItemStack(IUItem.photoniy_ingot),

                ('A'), new ItemStack(IUItem.advQuantumtool),

                ('B'), PER_SENSOR
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 17), " F ", "BAD", " C ",

                ('A'), IUItem.machine,

                ('C'), IUItem.elemotor,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 71),
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 243)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 21), "   ", "BAD", "GCF",

                ('A'), IUItem.machine,

                ('C'), IUItem.elemotor,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 71),
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 243),
                ('G'), new ItemStack(IUItem.crafting_elements, 1, 128)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 18), " E ", "BAD", "FCG",

                ('A'), IUItem.machine,

                ('C'), IUItem.elemotor,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 66),
                ('G'), new ItemStack(IUItem.crafting_elements, 1, 67),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 71)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine1), " E ", "BAB", "DCD",

                ('D'), new ItemStack(IUItem.module_stack),

                ('A'), new ItemStack(IUItem.simplemachine, 1, 6),

                ('B'), new ItemStack(IUItem.core, 1, 5),

                ('E'),
                new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        240
                ), ('C'), new ItemStack(IUItem.crafting_elements, 1, 56)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 11), " C ", " A ", " B ",

                ('A'), IUItem.machine,

                ('B'), IUItem.elemotor,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 112)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine1, 1, 2), "   ", "CAD", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.elemotor,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 226),

                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 158)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine1, 1, 4), "   ", "CAD", " B ",

                ('A'),
                getBlockStack(BlockBaseMachine3.generator_iu),
                ('B'), IUItem.elemotor,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 239)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine1, 1, 5), "   ", "CAD", " B ",

                ('A'),
                getBlockStack(BlockBaseMachine3.generator_iu),
                ('B'), IUItem.elemotor,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 236)
        );
        Recipes.recipe.addRecipe(
                getBlockStack(BlockBaseMachine3.pump_iu), " E ", "CAD", "B H",

                ('A'), IUItem.machine,

                ('B'), IUItem.elemotor,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 154),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 241), 'H', getBlockStack(BlockBaseMachine3.steam_pump)
        );
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine1, 1, 6),
                "F F",
                "CAE",
                "FBF",

                ('A'),
                getBlockStack(BlockBaseMachine3.pump_iu),
                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 20),

                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 6),

                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 248),
                ('F'),
                new ItemStack(IUItem.crafting_elements, 1, 138)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine1, 1, 7),
                "F F",
                "CAE",
                "FBF",

                ('A'),
                new ItemStack(IUItem.basemachine1, 1, 6),

                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 96),

                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 83),

                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 245),
                ('F'),
                new ItemStack(IUItem.crafting_elements, 1, 139)
        ), 1);
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine1, 1, 9), "   ", "CAD", " B ",

                ('A'),
                getBlockStack(BlockBaseMachine3.generator_iu),
                ('B'), IUItem.elemotor,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 237)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine1, 1, 8), "CCC", " A ", " B ",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 35),

                ('A'), IUItem.machine,

                ('B'), IUItem.elemotor
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.oilrefiner), "DDD", "FBE", " C ",

                ('A'), new ItemStack(IUItem.fluidCell),

                ('B'), IUItem.advancedMachine,

                ('C'), IUItem.elemotor,

                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 43),

                ('E'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        98
                ), ('F'), new ItemStack(IUItem.crafting_elements, 1, 33)
        );
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.oiladvrefiner),
                " A ",
                " B ",
                " C ",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 9),

                ('B'),
                IUItem.oilrefiner,

                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 20)
        ), 0);
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine1, 1, 10), " C ", "DAE", " B ",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 229),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 154),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('B'),
                IUItem.elemotor,

                ('A'), IUItem.machine
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine1, 1, 11), " E ", "CAB", " D ",

                ('D'), IUItem.elemotor,

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 47),

                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 27),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 65)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine1, 1, 13), " E ", "CAB", " D ",

                ('D'), IUItem.elemotor,

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 44),

                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 27),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 65)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine1, 1, 12), "   ", "CAD", " B ",

                ('A'),
                getBlockStack(BlockBaseMachine3.generator_iu),
                ('B'), IUItem.elemotor,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 228)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine1, 1, 14), "   ", "CAD", " B ",

                ('A'),
                getBlockStack(BlockBaseMachine3.generator_iu),
                ('B'), IUItem.elemotor,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 238)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine1, 1, 15), " E ", "CAD", "B H",

                ('A'), IUItem.machine,

                ('B'), IUItem.elemotor,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 154),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 244), 'H', getBlockStack(BlockBaseMachine3.steam_electrolyzer)
        );


        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines, 1, 10),
                "BAB",
                " D ",
                "B B",

                ('D'),
                getBlockStack(BlockBaseMachine3.generator_iu),
                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 234),

                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 138)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines, 1, 11),
                "BAB",
                " D ",
                "B B",

                ('D'),
                new ItemStack(IUItem.machines, 1, 10),

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 232),

                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 139)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines, 1, 12),
                "BAB",
                " D ",
                "B B",

                ('D'),
                new ItemStack(IUItem.machines, 1, 11),

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 233),

                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 140)
        ), 2);
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.pho_machine, 1, 24),
                "BAB",
                " D ",
                "B B",

                ('D'),
                new ItemStack(IUItem.machines, 1, 12),

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 629),

                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 623)
        );
        Recipes.recipe.addRecipe(
                getBlockStack(BlockBaseMachine3.geogenerator_iu), "   ", "ABC", "   ",

                ('B'), IUItem.machine,

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 11),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 22)
        );
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine, 1, 4),
                "B B",
                "ADC",
                "B B",

                ('D'),
                getBlockStack(BlockBaseMachine3.geogenerator_iu),
                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 234),

                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 5),

                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 138)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine, 1, 5),
                "B B",
                "ADC",
                "B B",

                ('D'),
                new ItemStack(IUItem.basemachine, 1, 4),

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 232),

                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 82),

                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 139)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.basemachine, 1, 6),
                "B B",
                "ADC",
                "B B",

                ('D'),
                new ItemStack(IUItem.basemachine, 1, 5),

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 233),

                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 106),

                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 140)
        ), 2);
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.pho_machine, 1, 22),
                "B B",
                "ADC",
                "B B",

                ('D'),
                new ItemStack(IUItem.basemachine, 1, 6),

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 629),

                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 630),

                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 623)
        );
        Recipes.recipe.addRecipe(
                getBlockStack(BlockBaseMachine3.solar_iu), "ABA", "BDB", " E ",

                ('E'), getBlockStack(BlockBaseMachine3.generator_iu), ('D'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        37
                ),
                ('A'), "dustCoal", ('B'),
                Blocks.GLASS
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 9), "   ", "CAD", " B ",

                ('A'), IUItem.machine,

                ('B'), IUItem.elemotor,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 101),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 11)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blockmolecular), "MXM", "ABA", "MXM",
                ('M'), IUItem.advancedMachine,
                ('X'), new ItemStack(IUItem.tranformer, 1, 10), ('A'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),
                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 75)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blockdoublemolecular), "BDB", "CAC", "BEB",

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('B'), IUItem.advancedMachine,

                ('A'), new ItemStack(IUItem.blockmolecular),

                ('D'),
                new ItemStack(IUItem.tranformer, 1, 1),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 10)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 4), "   ", "BAB", "DCE",

                ('A'), IUItem.advancedMachine,

                ('B'), new ItemStack(IUItem.entitymodules, 1, 1),

                ('C'), IUItem.elemotor,

                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 59),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 54)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blockSE), " B ", " A ", "   ",

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 79),

                ('A'),
                getBlockStack(BlockBaseMachine3.solar_iu)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_se_generator), "   ", "CAC", " B ",

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 234),

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 176),
                ('C'), new ItemStack(IUItem.blockSE)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 12), " A ", " D ", " B ",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 56),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 175),

                ('C'),
                ("doubleplateDuralumin"),
                ('E'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('D'), IUItem.imp_se_generator
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.combinersolidmatter), "ABC", "DJE", "FGH",

                ('J'), new ItemStack(IUItem.crafting_elements, 1, 56),

                ('H'), new ItemStack(IUItem.crafting_elements, 1, 145),

                ('G'), new ItemStack(IUItem.crafting_elements, 1, 147),

                ('F'),
                new ItemStack(IUItem.crafting_elements, 1, 148),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 149),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 150),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 151),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 152),

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 153)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.imp_se_generator), "   ", "CAC", " B ",

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 232),

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 174),
                ('C'), new ItemStack(IUItem.adv_se_generator)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 145), " B ", "BAB", " B ",

                ('A'), DEFAULT_SENSOR,
                ('B'), new ItemStack(IUItem.matter, 1, 2)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 147), " B ", "BAB", " B ",

                ('A'), DEFAULT_SENSOR,
                ('B'), new ItemStack(IUItem.matter, 1, 7)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 148), " B ", "BAB", " B ",

                ('A'), DEFAULT_SENSOR,
                ('B'), new ItemStack(IUItem.matter, 1, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 149), " B ", "BAB", " B ",

                ('A'), DEFAULT_SENSOR,
                ('B'), new ItemStack(IUItem.matter, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 150), " B ", "BAB", " B ",

                ('A'), DEFAULT_SENSOR,
                ('B'), new ItemStack(IUItem.matter, 1, 3)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 151), " B ", "BAB", " B ",

                ('A'), DEFAULT_SENSOR,
                ('B'), new ItemStack(IUItem.matter, 1, 0)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 152), " B ", "BAB", " B ",

                ('A'), DEFAULT_SENSOR,
                ('B'), new ItemStack(IUItem.matter, 1, 6)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 153), " B ", "BAB", " B ",

                ('A'), DEFAULT_SENSOR,
                ('B'), new ItemStack(IUItem.matter, 1, 5)
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 251),
                "BCB",
                "BAB",
                "BCB",

                ('A'),
                DEFAULT_SENSOR,
                ('B'),
                ModUtils.getCellFromFluid("iufluidneft"),
                ('C'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.oilgetter), " A ", " B ", " D ",

                ('D'), IUItem.elemotor,

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 251),

                ('B'), IUItem.machine
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.convertersolidmatter, 1), "E F", "ABC", " D ",

                ('F'), new ItemStack(IUItem.crafting_elements, 1, 44),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 75),

                ('A'),
                getBlockStack(BlockBaseMachine3.replicator_iu),
                ('C'),
                getBlockStack(BlockBaseMachine3.scanner_iu),
                ('D'), IUItem.combinersolidmatter,

                ('B'), IUItem.advancedMachine
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 23), " C ", " D ", " A ",

                ('D'), IUItem.blockmolecular,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 10),

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 152)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 39), " C ", " D ", " A ",

                ('D'), IUItem.blockmolecular,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 10),

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 151)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 37), " C ", " D ", " A ",

                ('D'), IUItem.blockmolecular,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 10),

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 150)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 36), " C ", " D ", " A ",

                ('D'), IUItem.blockmolecular,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 10),

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 153)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 35), " C ", " D ", " A ",

                ('D'), IUItem.blockmolecular,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 10),

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 145)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 34), " C ", " D ", " A ",

                ('D'), IUItem.blockmolecular,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 10),

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 147)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.veinsencor, 1), " BC", "BDB", "BAB",

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                ('B'), "plateIron", ('C'), Items.REDSTONE, ('D'),
                "plateChromium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 169), "CED", "ABA", "CFD",

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),

                ('B'), "plateIron",

                ('C'), Items.REDSTONE,

                ('D'),
                new ItemStack(Items.DYE, 1, 4),

                ('E'), IUItem.advnanobox,

                ('F'),
                getBlockStack(BlockBaseMachine3.energy_controller)
        );
        Recipes.recipe.addShapelessRecipe(
                getBlockStack(BlockBaseMachine3.limiter),
                new ItemStack(IUItem.crafting_elements, 1, 169), IUItem.advancedMachine
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 261), " C ", "BAB", " D ",
                ('A'), ADV_SENSOR, ('B'), "plateIron", ('C'), new ItemStack(
                        IUItem.energy_crystal,
                        1,
                        32767
                ),
                ('D'),
                Blocks.REDSTONE_BLOCK
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 628), " C ", "BAB", " D ",
                ('A'), PHOTON_SENSOR, ('B'), "plateIron", ('C'), new ItemStack(
                        IUItem.energy_crystal,
                        1,
                        32767
                ),
                ('D'),
                Blocks.REDSTONE_BLOCK
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 259), " C ", "BAB", " D ",
                ('A'), IMP_SENSOR, ('B'), "plateIron", ('C'), new ItemStack(
                        IUItem.AdvlapotronCrystal,
                        1,
                        32767
                ),
                ('D'),
                Blocks.REDSTONE_BLOCK
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 260), " C ", "BAB", " D ",
                ('A'), PER_SENSOR, ('B'), "plateIron", ('C'), new ItemStack(
                        IUItem.AdvlapotronCrystal,
                        1,
                        32767
                ),
                ('D'),
                Blocks.REDSTONE_BLOCK
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 258), " C ", "BAB", " D ",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 21),

                ('B'), "plateIron",

                ('C'), new ItemStack(IUItem.advBattery, 1, 32767),
                ('D'),
                Blocks.REDSTONE_BLOCK
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 71), "BAB", " D ", "B B",

                ('D'), new ItemStack(IUItem.basemachine2, 1, 70),

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 261),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 72), "BAB", " D ", "B B",

                ('D'), new ItemStack(IUItem.basemachine2, 1, 71),

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 259),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 73), "BAB", " D ", "B B",

                ('D'), new ItemStack(IUItem.basemachine2, 1, 72),

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 260),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 140)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 23), "BAB", " D ", "B B",

                ('D'), new ItemStack(IUItem.basemachine2, 1, 73),

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 628),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 623)
        );
        Recipes.recipe.addShapelessRecipe(
                getBlockStack(BlockBaseMachine3.redstone_generator),
                new ItemStack(IUItem.crafting_elements, 1, 258),    getBlockStack(BlockBaseMachine3.generator_iu)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 206), "ABA", "ACA", "ABA",

                ('B'), IUItem.insulatedTinCableItem,

                ('A'),
                ("plankWood"),
                ('C'), DEFAULT_SENSOR
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 207), "   ", "ABA", "   ",

                ('A'), IUItem.insulatedCopperCableItem,

                ('B'), DEFAULT_SENSOR
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 208), " A ", "DBC", " A ",

                ('B'), DEFAULT_SENSOR,

                ('A'), IUItem.insulatedGoldCableItem,

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),

                ('C'),
                new ItemStack(IUItem.advBattery, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 209), " A ", "DBC", " A ",

                ('B'), DEFAULT_SENSOR,

                ('A'), IUItem.insulatedIronCableItem,

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),

                ('C'),
                new ItemStack(IUItem.lapotron_crystal, 1, 32767)
        );
        for (int i = 0; i < 3; i++) {
            Recipes.recipe.addShapelessRecipe(
                    new ItemStack(IUItem.tranformer, 1, i + 8),
                    new ItemStack(IUItem.tranformer, 1, i + 7), new ItemStack(
                            IUItem.crafting_elements,
                            1,
                            207 + i
                    )
            );
        }
        Recipes.recipe.addShapelessRecipe(
                new ItemStack(IUItem.tranformer, 1, 7),
                IUItem.machine, new ItemStack(IUItem.crafting_elements, 1, 206)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 16),
                "ADA",
                "CBC",
                "ADA",

                ('A'),
                new ItemStack(IUItem.core, 1, 5),
                ('B'),
                new ItemStack(IUItem.module_schedule),
                ('C'),
                IUItem.compressIridiumplate,
                (
                        'D'),
                "doubleplateInvar"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 262), "CCC", "BAB", "DED",

                ('B'), "gearAlumel",

                ('C'), "plateAluminumbronze",

                ('D'), "plateTitanium",

                ('E'),
                "plateIron",

                ('A'), DEFAULT_SENSOR
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 40),
                " B ",
                "ACA",
                "ADA",
                ('A'),
                "plateRedbrass",
                ('B'),
                "plateIron",
                ('C'),
                "plateTitanium",
                ('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 74), " E ", "ABD", " C ",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 47),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 70),

                ('B'), IUItem.machine,

                ('C'),
                IUItem.elemotor,

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 262)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 256), "CDE", "BAB", "FFF",

                ('A'), ADV_SENSOR,

                ('C'), Items.DIAMOND_AXE,

                ('D'), Items.DIAMOND_PICKAXE,

                ('E'),
                Items.DIAMOND_SHOVEL,

                ('B'), "gearIridium",

                ('F'), "gearMagnesium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 252), "CDE", "BAB", "FFF",

                ('A'), IMP_SENSOR,

                ('C'), Items.DIAMOND_AXE,

                ('D'), Items.DIAMOND_PICKAXE,

                ('E'),
                Items.DIAMOND_SHOVEL,

                ('B'), "gearIridium",

                ('F'), "gearMagnesium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 254), "CDE", "BAB", "FFF",

                ('A'), PER_SENSOR,

                ('C'), Items.DIAMOND_AXE,

                ('D'), Items.DIAMOND_PICKAXE,

                ('E'),
                Items.DIAMOND_SHOVEL,

                ('B'), "gearIridium",

                ('F'), "gearMagnesium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 631), "CDE", "BAB", "FFF",

                ('A'), PHOTON_SENSOR,

                ('C'), Items.DIAMOND_AXE,

                ('D'), Items.DIAMOND_PICKAXE,

                ('E'),
                Items.DIAMOND_SHOVEL,

                ('B'), "gearIridium",

                ('F'), "gearMagnesium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 257), "CEC", "ABA", "DED",

                ('D'),
                ("gemCurium"),
                ('C'),
                ("doubleplateCaravky"),
                ('E'),
                ("plateDuralumin"),
                ('A'),
                new ItemStack(IUItem.core, 1, 8),

                ('B'), ADV_SENSOR
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 253), "CEC", "ABA", "DED",

                ('D'),
                ("doubleplateGermanium"),
                ('C'),
                ("doubleplateAlcled"),
                ('E'),
                ("plateVitalium"),
                ('A'),
                new ItemStack(IUItem.core, 1, 11),

                ('B'), IMP_SENSOR
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 255), "CEC", "ABA", "DED",

                ('D'), new ItemStack(IUItem.doublecompressIridiumplate),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('E'),
                ("plateTitanium"),
                ('A'),
                new ItemStack(IUItem.core, 1, 12),

                ('B'), PER_SENSOR
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 250), "EDE", "BAB", "CCC",

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('D'), DEFAULT_SENSOR,

                ('C'), new ItemStack(IUItem.compressIridiumplate),

                ('B'),
                new ItemStack(IUItem.advQuantumtool),

                ('A'), new ItemStack(IUItem.core, 1, 7)
        );
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(new ItemStack(IUItem.machines, 1, 13), "C C", " B ",
                "CAC",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 257),
                ('B'), new ItemStack(IUItem.machines, 1, 8),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 138)
        ), 0);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(new ItemStack(IUItem.machines, 1, 14), "C C", " B "
                , "CAC",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 253),
                ('B'), new ItemStack(IUItem.machines, 1, 13),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 139)
        ), 1);
        TileEntityUpgradeMachineFactory.addRecipe(Recipes.recipe.addRecipe(
                new ItemStack(IUItem.machines, 1, 15),
                "C C",
                " B ",
                "CAC",

                ('A'),
                new ItemStack(IUItem.crafting_elements, 1, 255),
                ('B'),
                new ItemStack(IUItem.machines, 1, 14),
                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 140)
        ), 2);
        if (!Loader.isModLoaded("simplyquarries")) {
            Recipes.recipe.addRecipe(new ItemStack(IUItem.machines, 1, 8), "   ", " A ", " B ",

                    ('A'), new ItemStack(IUItem.crafting_elements, 1, 250),
                    ('B'), IUItem.advancedMachine
            );
        }
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blockadmin, 1), "   ", "BAB", "ABA",
                ('B'), new ItemStack(IUItem.excitednucleus, 1, 13), ('A'), new ItemStack(
                        IUItem.blockpanel,
                        1,
                        13
                )
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.perfect_drill), "ACB", "F F", "ECE",

                ('E'), new ItemStack(IUItem.advQuantumtool),

                ('F'), IUItem.overclockerUpgrade1,

                ('A'), new ItemStack(IUItem.spectralaxe, 1, 32767),

                ('B'),
                new ItemStack(IUItem.spectraldrill, 1, 32767),

                ('C'), TileGenerationMicrochip.getLevelCircuit(new ItemStack(IUItem.basecircuit, 1, 21), 11)
        );
        Recipes.recipe.addRecipe(
                ModUtils.setSize(IUItem.glassFiberCableItem, 6), "AAA", "BCB", "AAA",

                ('A'), new ItemStack(Blocks.GLASS),

                ('B'), IUItem.energiumDust,

                ('C'), "dustSilver"
        );
        Recipes.recipe.addRecipe(IUItem.coil, "AAA", "ABA", "AAA",

                ('A'), IUItem.copperCableItem,

                ('B'), Items.IRON_INGOT
        );
        Recipes.recipe.addRecipe(IUItem.powerunit, "ABD", "ACE", "ABD",

                ('A'), new ItemStack(IUItem.reBattery, 1, 32767),

                ('B'), IUItem.copperCableItem,

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),

                ('D'),
                "casingIron",

                ('E'), IUItem.elemotor
        );
        Recipes.recipe.addRecipe(IUItem.powerunitsmall, " BD", "ACE", " BD",

                ('A'), new ItemStack(IUItem.advBattery, 1, 32767),

                ('B'), IUItem.copperCableItem,

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),

                ('D'),
                "casingIron",

                ('E'), IUItem.elemotor
        );
        Recipes.recipe.addRecipe(IUItem.reBattery
                , "AEA", "CDC", "CDC",

                ('A'), IUItem.insulatedTinCableItem,

                ('C'), "casingAluminiumLithium",

                ('D'), new ItemStack(IUItem.charged_redstone),'E', new ItemStack(IUItem.charged_quartz)
        );
        Recipes.recipe.addRecipe(IUItem.advBattery
                , "AEA", "CDC", "CBC",

                ('A'), IUItem.insulatedCopperCableItem,

                ('C'), IUItem.casingbronze,

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 476),

                ('B'),
                "dustLead",'E', new ItemStack(IUItem.charged_redstone, 1)
        );

        Recipes.recipe.addRecipe(IUItem.energyStorageUpgrade, "FEF", "BDB", "ACA",

                ('A'), "plateCarbon",

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),

                ('D'), new ItemStack(IUItem.advBattery, 1, 32767),

                ('B'),
                IUItem.insulatedCopperCableItem, 'E', IUItem.upgrade_casing, 'F', "plateObsidian"
        );
        Recipes.recipe.addShapelessRecipe(
                new ItemStack(IUItem.frequency_transmitter),
                IUItem.insulatedCopperCableItem, TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
        );
        Recipes.recipe.addRecipe(
                ModUtils.setSize(IUItem.suBattery, 5), " A ", " B ", " C ",

                ('A'), IUItem.insulatedCopperCableItem,

                ('B'), Items.REDSTONE,

                ('C'), "dustCoal"
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

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 268),

                ('B'), IUItem.advancedMachine
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 268), "AEA", "CBC", "ADA",

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),

                ('C'), IUItem.glassFiberCableItem,

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 270),

                ('B'),
                DEFAULT_SENSOR,

                ('E'), IUItem.frequency_transmitter
        );
        Recipes.recipe.addRecipe(
                getBlockStack(BlockBaseMachine3.canner_iu), "A D", "EBF", " C ",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 265),

                ('B'), IUItem.machine,

                ('C'), IUItem.elemotor,

                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 27),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 47),

                ('F'), new ItemStack(IUItem.crafting_elements, 1, 154)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 265), "CCC", "BAB", "DDD",

                ('A'), DEFAULT_SENSOR,

                ('B'), "gearNickel",

                ('C'), "plateTin",

                ('D'),
                "gearSilver"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 263), " C ", "BAB", "DDD",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 21),

                ('B'), "doubleplateNichrome",

                ('C'), new ItemStack(IUItem.energy_crystal, 1, 32767),

                ('D'),
                ModUtils.getCellFromFluid(FluidName.fluidgas.getInstance())
        );
        Recipes.recipe.addShapelessRecipe(
                getBlockStack(BlockBaseMachine3.gas_generator),
                new ItemStack(IUItem.crafting_elements, 1, 263), IUItem.machine
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.lap_energystorage_upgrade,
                IUItem.energyStorageUpgrade, new ItemStack(IUItem.reBattery, 1, 32767)
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.adv_lap_energystorage_upgrade,
                IUItem.lap_energystorage_upgrade, new ItemStack(IUItem.energy_crystal, 1, 32767)
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.imp_lap_energystorage_upgrade,
                IUItem.adv_lap_energystorage_upgrade, new ItemStack(IUItem.lapotron_crystal, 1, 32767)
        );
        Recipes.recipe.addShapelessRecipe(
                IUItem.per_lap_energystorage_upgrade,
                IUItem.imp_lap_energystorage_upgrade, new ItemStack(IUItem.AdvlapotronCrystal, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 264), " C ", "BAB", "DDD",

                ('A'), DEFAULT_SENSOR,

                ('B'), "doubleplateNichrome",

                ('C'), IUItem.tinCan,

                ('D'),
                "plateVanadoalumite"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.coolingsensor, 1), "CBC", "BDB", "BAB",

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                ('B'), "plateIron", ('C'), new ItemStack(
                        IUItem.coolpipes,
                        1,
                        3
                ), ('D'),
                "doubleplateMikhail"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heatsensor, 1), "CBC", "BDB", "BAB",

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                ('B'), "plateIron", ('C'), new ItemStack(
                        IUItem.pipes,
                        1,
                        3
                ), ('D'),
                new ItemStack(IUItem.preciousblock)
        );
        Recipes.recipe.addShapelessRecipe(
                ModUtils.setSize(IUItem.copperCableItem, 2), "craftingToolWireCutter", "plateCopper");
        Recipes.recipe.addShapelessRecipe(
                ModUtils.setSize(IUItem.tinCableItem, 3), "craftingToolWireCutter", "plateTin");
        Recipes.recipe.addShapelessRecipe(
                ModUtils.setSize(IUItem.goldCableItem, 4), "craftingToolWireCutter", "plateGold");
        Recipes.recipe.addRecipe(
                ModUtils.setSize(IUItem.reinforcedGlass, 7),
                "BAB", "BBB", "BAB", ('B'), Blocks.GLASS, ('A'), IUItem.advancedAlloy
        );

        Recipes.recipe.addRecipe(IUItem.hazmat_helmet
                , "AAA", "ABA", " C ",

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 40),

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 478),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 97),

                ('D'),
                Blocks.IRON_BARS
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 97)
                , "AAA", "ABA", "CAC",

                ('B'), Blocks.GLASS,

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 501),

                ('C'), "plateCarbon",

                ('D'),
                Blocks.IRON_BARS
        );
        Recipes.recipe.addRecipe(IUItem.hazmat_chestplate
                , "B B", "BBB", "BBB",

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 478),

                ('A'), new ItemStack(Items.DYE, 1, 14)
        );
        Recipes.recipe.addRecipe(IUItem.hazmat_leggings
                , "BBB", "B B", "B B",

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 478),

                ('A'), new ItemStack(Items.DYE, 1, 14)
        );
        Recipes.recipe.addRecipe(
                IUItem.rubber_boots
                ,
                "   ", "B B", "B B", ('B'), new ItemStack(IUItem.crafting_elements, 1, 478), ('A'), new ItemStack(
                        Items.DYE,
                        1,
                        14
                )
        );
        Recipes.recipe.addRecipe(
                IUItem.sprayer,
                "B  ", " B ", " AB", ('B'), "casingIron", ('A'), IUItem.fluidCell
        );
        Recipes.recipe.addRecipe(IUItem.treetap
                , " B ", "BBB", "B  ", ('B'), "plankWood");
        Recipes.recipe.addRecipe(IUItem.wrench
                , "B B", "BBB", " B ", ('B'), "ingotBronze");
        Recipes.recipe.addShapelessRecipe(IUItem.electric_wrench
                , IUItem.wrench, IUItem.powerunitsmall);
        Recipes.recipe.addShapelessRecipe(IUItem.electric_treetap
                , IUItem.treetap, IUItem.powerunitsmall);
        Recipes.recipe.addRecipe(
                IUItem.chainsaw
                ,
                " BB", "BBB", "AB ", ('B'), "plateIron", ('A'), IUItem.powerunit
        );
        Recipes.recipe.addRecipe(
                IUItem.drill
                ,
                "   ", " B ", " A ", ('B'), new ItemStack(IUItem.crafting_elements, 1, 508), ('A'), IUItem.powerunit
        );
        Recipes.recipe.addRecipe(IUItem.diamond_drill
                , "   ", " B ", "BAB",

                ('B'), "gemDiamond",

                ('A'), new ItemStack(IUItem.drill, 1, 32767)
        );




        Recipes.recipe.addRecipe(IUItem.nightvision
                , " E ", "CDC", "ABA",

                ('A'), IUItem.rubber,

                ('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),

                ('C'), "plateCarbon",

                ('D'),
                IUItem.reinforcedGlass,

                ('E'), new ItemStack(IUItem.advBattery, 1, 32767)


        );
        Recipes.recipe.addRecipe(
                IUItem.lappack
                ,
                "CAC", "BAB", "A A", ('B'), new ItemStack(IUItem.energy_crystal, 1, 32767),

                ('A'), "casingIron",
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
        );
        Recipes.recipe.addRecipe(
                IUItem.lapotron_crystal
                ,
                "ACA", "ABA", "ACA", ('B'), new ItemStack(IUItem.energy_crystal, 1, 32767),

                ('A'), "dustLapis",
                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4)
        );
        Recipes.recipe.addRecipe(
                IUItem.charging_re_battery
                ,
                "ABA", "B B", "ABA", ('B'), new ItemStack(IUItem.advBattery, 1, 32767),

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)
        );
        Recipes.recipe.addRecipe(
                IUItem.advanced_charging_re_battery
                ,
                " B ", "BCB", " B ", ('B'), new ItemStack(IUItem.advBattery, 1, 32767),


                ('C'), new ItemStack(IUItem.charging_re_battery, 1, 32767)
        );
        Recipes.recipe.addRecipe(
                IUItem.charging_energy_crystal
                ,
                " B ", "BCB", " B ", ('B'), new ItemStack(IUItem.energy_crystal, 1, 32767),


                ('C'), new ItemStack(IUItem.advanced_charging_re_battery, 1, 32767)
        );
        Recipes.recipe.addRecipe(
                IUItem.charging_lapotron_crystal
                ,
                " B ", "BCB", " B ", ('B'), new ItemStack(IUItem.lapotron_crystal, 1, 32767),


                ('C'), new ItemStack(IUItem.charging_energy_crystal, 1, 32767)
        );
        Recipes.recipe.addRecipe(IUItem.ForgeHammer
                , "AA ", "ABB", "AA ", ('B'), "stickWood",
                ('A'), "ingotIron"
        );
        Recipes.recipe.addRecipe(IUItem.ForgeHammer
                , " AA", "BBA", " AA", ('B'), "stickWood",
                ('A'), "ingotIron"
        );
        Recipes.recipe.addRecipe(IUItem.cutter
                , "A A", " A ", "B B", ('B'), "ingotCopper",
                ('A'), "plateIron"
        );
        Recipes.recipe.addRecipe(
                IUItem.advanced_batpack
                ,
                "BAB", "BCB", "B B", ('B'), new ItemStack(IUItem.advBattery, 1, 32767),

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1), ('C'), "casingCopper"
        );
        Recipes.recipe.addRecipe(
                IUItem.batpack
                ,
                "BAB", "BCB", "B B", ('B'), new ItemStack(IUItem.advBattery, 1, 32767),

                ('A'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1), ('C'), "plankWood"
        );
        Recipes.recipe.addRecipe(IUItem.nanosaber
                , "BA ", "BA ", "CDC", ('B'), Items.GLOWSTONE_DUST,
                ('A'), IUItem.advancedAlloy, ('C'), "plateCarbon", ('D'),
                new ItemStack(IUItem.energy_crystal, 1, 32767)
        );


        Recipes.recipe.addRecipe(IUItem.mixedMetalIngot, "AAA", "BBB", "CCC",
                ('A'), "plateIron", ('B'), "plateCopper", ('C'), "plateTin"
        );
        Recipes.recipe.addRecipe(IUItem.rawcrystalmemory, "ABA", "BAB", "ABA",
                ('A'), "dustSiliconDioxide", ('B'), "dustObsidian"
        );
        Recipes.recipe.addRecipe(IUItem.carbonFiber, "AA ", "AA ", "   ",
                ('A'), "dustCoal"
        );
        Recipes.recipe.addShapelessRecipe(IUItem.carbonMesh, IUItem.carbonFiber, IUItem.carbonFiber);
        Recipes.recipe.addRecipe(IUItem.copperboiler, "AAA", "A A", "AAA",
                ('A'), "casingCopper"
        );
        Recipes.recipe.addRecipe(IUItem.plantBall, "AAA", "A A", "AAA",
                ('A'), Items.REEDS
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
                ('A'), "treeLeaves"
        );
        Recipes.recipe.addRecipe(IUItem.plantBall, "AAA", "A A", "AAA",
                ('A'), Items.WHEAT_SEEDS
        );
        Recipes.recipe.addRecipe(IUItem.plantBall, "AAA", "A A", "AAA",
                ('A'), "treeLeaves"
        );
        Recipes.recipe.addRecipe(IUItem.plantBall, "AAA", "A A", "AAA",
                ('A'), "treeSapling"
        );
        Recipes.recipe.addRecipe(IUItem.fluidCell, " A ", "ABA", " A ",

                ('A'), "casingTin",

                ('B'),
                Item.getItemFromBlock(Blocks.GLASS_PANE)
        );
        Recipes.recipe.addRecipe(IUItem.coalBall, "AAA", "ABA", "AAA",
                ('A'), "dustCoal", ('B'), Items.FLINT
        );
        Recipes.recipe.addRecipe(IUItem.coal_chunk, "AAA", "ABA", "AAA",
                ('A'), IUItem.compressedCoalBall, ('B'), Blocks.OBSIDIAN
        );

        Recipes.recipe.addRecipe(IUItem.cfPowder, "ACA", "ABA", "ACA",
                ('A'), "dustStone", ('B'), Items.CLAY_BALL,
                ('C'), Blocks.SAND
        );

        Recipes.recipe.addRecipe(IUItem.heatconducto, "ABA", "ABA", "ABA",
                ('A'), IUItem.rubber, ('B'), "plateCopper"
        );
        Recipes.recipe.addRecipe(IUItem.ejectorUpgrade, "ABA", " D ", "ACA",
                ('A'), "plateTin", ('B'), Blocks.PISTON, 'C', IUItem.upgrade_casing, 'D', IUItem.motors_with_improved_bearings_
        );
        Recipes.recipe.addRecipe(
                IUItem.pullingUpgrade,
                "ABA",
                " D ",
                "ACA",
                ('A'),
                "plateTin",
                ('B'),
                Blocks.STICKY_PISTON,
                'C',
                IUItem.upgrade_casing,
                'D',
                IUItem.motors_with_improved_bearings_
        );
        Recipes.recipe.addRecipe(IUItem.fluidEjectorUpgrade, "ADA", " B ", "ACA",
                ('A'), "plateTin", ('B'), IUItem.elemotor, 'C', IUItem.upgrade_casing, 'D', IUItem.fluidCell
        );
        Recipes.recipe.addRecipe(IUItem.fluidpullingUpgrade, "ACA", " B ", "ADA",
                ('A'), "plateTin", ('B'), IUItem.elemotor, ('C'), new ItemStack(
                        IUItem.treetap,
                        1,
                        32767
                ), 'D', IUItem.upgrade_casing
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.gasChamber), "ABA", "BBB", "CAC",
                ('A'), new ItemStack(IUItem.crafting_elements, 1, 601), ('B'), "plateIron", ('C'), "gearInvar"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 464)
                , "AAA", "AAA", "AAA", ('A'), new ItemStack(IUItem.crafting_elements, 1, 457));
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 465)
                , "AAA", "AAA", "AAA", ('A'), new ItemStack(IUItem.crafting_elements, 1, 464));

        Recipes.recipe.addRecipe(IUItem.bronze_helmet
                , "AAA", "A A", "   ", ('A'), "ingotBronze");
        Recipes.recipe.addRecipe(IUItem.bronze_leggings
                , "AAA", "A A", "A A", ('A'), "ingotBronze");
        Recipes.recipe.addRecipe(IUItem.bronze_boots
                , "   ", "A A", "A A", ('A'), "ingotBronze");
        Recipes.recipe.addRecipe(IUItem.bronze_chestplate
                , "A A", "AAA", "AAA", ('A'), "ingotBronze");


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

        Recipes.recipe.addRecipe(ModUtils.setSize(IUItem.energiumDust, 9), "ABA", "BAB", "ABA",

                ('A'), Items.REDSTONE,

                ('B'), IUItem.diamondDust
        );

        Recipes.recipe.addRecipe(
                IUItem.advancedMachine,
                "ABA",
                "CDC",
                "ABA",
                ('A'),
                "plateSteel",
                ('B'),
                IUItem.advancedAlloy,
                ('C'),
                "plateCarbon",
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
                "doubleplateFerromanganese",
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
                "doubleplateFerromanganese",
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
                "doubleplateFerromanganese",
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
                "doubleplateFerromanganese",
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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 437), " AA", "BCA", "BB ", 'A', "plateSteel", 'B',
                IUItem.advancedAlloy, 'C', "stickInvar"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 337), " AA", "A A", "AA ", 'A', "plateIron");
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 339), " AA", "A A", "AA ", 'A', "plateGold");
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 305), " AA", "A A", "AA ", 'A', "gemDiamond");
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 341), " AA", "A A", "AA ", 'A', "gemEmerald");


        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 433), "BAB", "ABA", "BAB",

                ('A'), "plateZinc",


                ('B'), "plateIridium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 367),
                " A ", "ABA", "CCC", 'A', "plateBeryllium", 'B', "plateIron", 'C', "plateBor"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 415),
                " A ", "ABA", "CCC", 'A', "plateGold", 'B', new ItemStack(IUItem.crafting_elements, 1, 367), 'C', "plateElectrum"
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 380),
                " A ",
                "ABA",
                "CCC",
                'A',
                "plateMikhail",
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 415),
                'C',
                "plateVanadium"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 396),
                " A ", "ABA", "CCC", 'A', "plateCadmium", 'B', new ItemStack(IUItem.crafting_elements, 1, 380), 'C', "plateSpinel"
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 336),
                "CDC",
                "BAB",
                "CDC",
                'A',
                new ItemStack(IUItem.core, 1, 2),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 414),
                'C',
                "ingotOsmium",
                'D',
                "platePlatinum"
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 335),
                "CDC",
                "BAB",
                "CDC",
                'A',
                new ItemStack(IUItem.core, 1, 2),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 414),
                'C',
                "ingotOsmium",
                'D',
                "plateTitanium"
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 334),
                "CDC",
                "BAB",
                "CDC",
                'A',
                new ItemStack(IUItem.core, 1, 2),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 414),
                'C',
                "ingotOsmium",
                'D',
                "plateZinc"
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 333),
                "CDC",
                "BAB",
                "CDC",
                'A',
                new ItemStack(IUItem.core, 1, 2),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 414),
                'C',
                "ingotOsmium",
                'D',
                "plateTungsten"
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 427),
                "BCB",
                "DAD",
                " ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 435),
                'B',
                IUItem.nanoBox,
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 5),
                'D',
                "plateDenseGold"
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 374),
                "BCB",
                "DAD",
                " ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 427),
                'B',
                IUItem.quantumtool,
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                'D',
                "doubleplateOsmium"
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 403),
                "BCB",
                "DAD",
                " ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 374),
                'B',
                IUItem.spectral_box,
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                'D',
                "doubleplateOsmium"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 428),
                "BCB", "DAD", " ", 'A', new ItemStack(IUItem.crafting_elements, 1, 436), 'B', IUItem.nanoBox, 'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),
                'D', "plateDenseGold"
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 379),
                "BCB",
                "DAD",
                " ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 428),
                'B',
                IUItem.quantumtool,
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),
                'D',
                "doubleplateOsmium"
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 404),
                "BCB",
                "DAD",
                " ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 379),
                'B',
                IUItem.spectral_box,
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),
                'D',
                "doubleplateOsmium"
        );
        // water reactor
        // 366
        // 420
        // 378
        // 405
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 0),
                "AA ", "AA ", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 366)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 1),
                "AA ", "AA ", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 420)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 2),
                "AA ", "AA ", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 378)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 3),
                "AA ", "AA ", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 405)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 4),
                "  ", "BAB", "CDC", 'A', new ItemStack(IUItem.water_reactors_component, 1, 0), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 322), 'C',
                new ItemStack(IUItem.crafting_elements, 1, 414), 'D', new ItemStack(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 5),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 4)
                , 'B', new ItemStack(IUItem.crafting_elements, 1, 420), 'C',
                new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 6),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 5), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 378), 'C',
                new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 7),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 6), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 405), 'C',
                new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 8),
                "EDE", "BAB", "FCF", 'A', new ItemStack(IUItem.water_reactors_component, 1, 0), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 435), 'C',
                new ItemStack(IUItem.crafting_elements, 1, 342), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 322)
                , 'E', new ItemStack(IUItem.crafting_elements, 1, 414), 'F', new ItemStack(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 9),
                "BCB", "DAD", "BEB", 'A', new ItemStack(IUItem.water_reactors_component, 1, 8), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 420), 'C',
                new ItemStack(IUItem.crafting_elements, 1, 427), 'D', new ItemStack(IUItem.crafting_elements, 1, 426), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 10),
                "BCB", "DAD", "BEB", 'A', new ItemStack(IUItem.water_reactors_component, 1, 9), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 378), 'C',
                new ItemStack(IUItem.crafting_elements, 1, 374)
                , 'D', new ItemStack(IUItem.crafting_elements, 1, 373), 'E', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 11),
                "BCB", "DAD", "BEB", 'A', new ItemStack(IUItem.water_reactors_component, 1, 10), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 405), 'C',
                new ItemStack(IUItem.crafting_elements, 1, 403), 'D', new ItemStack(IUItem.crafting_elements, 1, 397), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.water_reactors_component, 1, 12),
                "CBC",
                "BAB",
                " D ",
                'A',
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                'B',
                IUItem.fluidpullingUpgrade,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 414),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 13),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 12)
                , 'B', new ItemStack(IUItem.crafting_elements, 1, 420)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.water_reactors_component, 1, 14),
                "BDB",
                "CAC",
                "B B",
                'A',
                new ItemStack(IUItem.water_reactors_component, 1, 13),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 378),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 373),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 15),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 14), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 405)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );


        BasicRecipeThree.recipe();
    }

    public static ItemStack getBlockStack(IMultiTileBlock block) {
        return TileBlockCreator.instance.get(block.getIDBlock()).getItemStack(block);
    }

}
