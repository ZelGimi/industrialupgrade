package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockAnvil;
import com.denfop.blocks.BlockStrongAnvil;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.mechanism.BlockApiary;
import com.denfop.blocks.mechanism.BlockBarrel;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.blocks.mechanism.BlockGasTurbine;
import com.denfop.blocks.mechanism.BlockGasWell;
import com.denfop.blocks.mechanism.BlockHydroTurbine;
import com.denfop.blocks.mechanism.BlockMiniSmeltery;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.blocks.mechanism.BlockPrimalFluidHeater;
import com.denfop.blocks.mechanism.BlockPrimalWireInsulator;
import com.denfop.blocks.mechanism.BlockRefractoryFurnace;
import com.denfop.blocks.mechanism.BlockSimpleMachine;
import com.denfop.blocks.mechanism.BlockSmeltery;
import com.denfop.blocks.mechanism.BlockSteamBoiler;
import com.denfop.blocks.mechanism.BlockSteamTurbine;
import com.denfop.blocks.mechanism.BlockWindTurbine;
import com.denfop.tiles.mechanism.TileGenerationMicrochip;
import com.denfop.utils.ModUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class BasicRecipeThree {

    public static ItemStack DEFAULT_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 21);

    public static ItemStack ADV_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 25);

    public static ItemStack IMP_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 23);

    public static ItemStack PER_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 24);
    public static ItemStack PHOTON_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 620);

    public static ItemStack getBlockStack(IMultiTileBlock block) {
        return TileBlockCreator.instance.get(block.getIDBlock()).getItemStack(block);
    }

    public static void recipe() {
        Recipes.recipe.addRecipe(getBlockStack(BlockSmeltery.smeltery_controller), " C ", "DBD", "   ",

                ('A'), new ItemStack(Items.COMPARATOR), ('B'), getBlockStack(BlockSmeltery.smeltery_casing), 'C',
                Blocks.REDSTONE_BLOCK, 'D', new ItemStack(Items.REPEATER)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockSmeltery.smeltery_casting), " C ", "CBC", " C ",

                ('A'), new ItemStack(IUItem.basemachine2, 1, 185), ('B'), getBlockStack(BlockSmeltery.smeltery_furnace), 'C',
                "plateThallium", 'D', "plateStrontium"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 2, 568), " A ", "CCC", " B ",

                ('A'), new ItemStack(IUItem.plate, 1, 31), ('B'), "ingotInvar", 'C',
                "stickSilver"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 567), " A ", "CCC", " B ",

                ('A'), "ingotGermanium", ('B'), new ItemStack(IUItem.crafting_elements, 1, 504), 'C',
                "stickElectrum"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 565), "BAB", "CCC", "BAB",

                ('A'), new ItemStack(IUItem.alloysingot, 1, 31), ('B'), "stickSilver", 'C',
                "stickZinc"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 581), " B ", "AAA", " B ",

                ('A'), new ItemStack(IUItem.cable, 1, 12), ('B'), "plateCopper", 'C',
                "stickSilver"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 579), " B ", "AAA", " B ",

                ('A'), new ItemStack(IUItem.cable, 1, 12), ('B'), "plateLapis", 'C',
                "stickSilver"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 563), " B ", "AAA", " B ",

                ('A'), new ItemStack(IUItem.cable, 1, 12), ('B'), new ItemStack(IUItem.crafting_elements, 1, 480), 'C',
                "stickSilver"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 585), " B ", "AAA", " B ",

                ('A'), new ItemStack(IUItem.cable, 1, 12), ('B'), new ItemStack(IUItem.alloysingot, 1, 25), 'C',
                "stickSilver"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 582), "BBB", "A A", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 356), ('B'), new ItemStack(IUItem.cable, 1, 11), 'C',
                "stickSilver"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 599), "CBC", "BAB", "CBC",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 582), ('B'), new ItemStack(IUItem.alloysplate, 1, 9), ('C'),
                new ItemStack(IUItem.plate, 1, 34)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 583), "CBC", "BAB", "CBC",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 599), ('B'), new ItemStack(IUItem.plate, 1, 36), ('C'),
                new ItemStack(IUItem.plate, 1, 33)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 597), "BBB", "BAB", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 583), ('B'), new ItemStack(IUItem.alloysplate, 1, 21), ('C'),
                new ItemStack(IUItem.plate, 1, 33)
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.water_reactors_component, 1, 20),
                "CBC",
                "BAB",
                " D ",
                'A',
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                'B',
                IUItem.fluidEjectorUpgrade,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 414),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 21),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 20), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 420)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 22),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 21), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 378)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 23),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 22), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 405)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );


        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.water_reactors_component, 1, 16),
                "CDC",
                "BAB",
                "   ",
                'A',
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 326),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 414),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 17),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 16), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 420)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 18),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 17), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 378)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 19),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 18), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 405)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.water_reactors_component, 1, 24),
                "CDC",
                "BAB",
                "   ",
                'A',
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 321),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 414),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 25),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 24), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 420)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 26),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 25), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 378)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 27),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 26), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 405)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.water_reactors_component, 1, 28),
                "CDC",
                "BAB",
                "   ",
                'A',
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 327),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 414),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 29),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 28), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 420)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 30),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 29), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 378)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 31),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 30), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 405)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.water_reactors_component, 1, 36),
                "CDC",
                "BAB",
                "   ",
                'A',
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 336),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 414),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 37),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 36), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 420)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 38),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 37), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 378)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 39),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.water_reactors_component, 1, 38), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 405)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 32),
                "DBD", " A ", " C ", 'A', new ItemStack(IUItem.water_reactors_component, 1, 0), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 43), 'C', new ItemStack(IUItem.crafting_elements, 1, 367), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 387)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 33),
                "BDB", "EAE", "BCB", 'A', new ItemStack(IUItem.water_reactors_component, 1, 32), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 420), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 45), 'C', new ItemStack(IUItem.crafting_elements, 1, 415), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 425)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 34),
                "BDB", "EAE", "BCB", 'A', new ItemStack(IUItem.water_reactors_component, 1, 33), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 378), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 48), 'C', new ItemStack(IUItem.crafting_elements, 1, 380), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 372)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_reactors_component, 1, 35),
                "BDB", "EAE", "BCB", 'A', new ItemStack(IUItem.water_reactors_component, 1, 34), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 405), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 50), 'C', new ItemStack(IUItem.crafting_elements, 1, 396), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 398)
        );
        // heat reactor
        // 363
        // 417
        // 375
        // 392
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 24),
                "AA ", "AA ", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 363)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 25),
                "AA ", "AA ", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 417)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 26),
                "AA ", "AA ", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 375)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 27),
                "AA ", "AA ", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 392)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 0),
                "CDC", "EAE", " B ", 'A', new ItemStack(IUItem.heat_reactor, 1, 24),
                'C', new ItemStack(IUItem.crafting_elements, 1, 414),
                'D', new ItemStack(IUItem.crafting_elements, 1, 356),
                'B', IUItem.elemotor, 'E', "doubleplateTungsten"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 1),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.heat_reactor, 1, 0), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 420)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 2),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.heat_reactor, 1, 1), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 375)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 3),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.heat_reactor, 1, 2), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 392)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 4),
                "EGF", "CAC", "DBD", 'A', new ItemStack(IUItem.heat_reactor, 1, 24),
                'B', new ItemStack(IUItem.crafting_elements, 1, 324),
                'C', new ItemStack(IUItem.crafting_elements, 1, 414),
                'D', "plateBeryllium", 'F', new ItemStack(IUItem.crafting_elements, 1, 321), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 342), 'G', new ItemStack(IUItem.crafting_elements, 1, 365)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 5),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.heat_reactor, 1, 4), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 420)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 6),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.heat_reactor, 1, 5), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 375)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 7),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.heat_reactor, 1, 6), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 392)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 8),
                "   ", "CAC", "DBD", 'A', new ItemStack(IUItem.heat_reactor, 1, 24), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 322), 'C', new ItemStack(IUItem.crafting_elements, 1, 414),
                'D', new ItemStack(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 9),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.heat_reactor, 1, 8), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 420)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 10),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.heat_reactor, 1, 9), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 375)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 11),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.heat_reactor, 1, 10), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 392)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 12),
                "DCD", "BAB", "   ", 'A', new ItemStack(IUItem.heat_reactor, 1, 24), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 333), 'C', new ItemStack(IUItem.crafting_elements, 1, 356), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 414)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 13),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.heat_reactor, 1, 12), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 420)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 14),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.heat_reactor, 1, 13), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 375)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 15),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.heat_reactor, 1, 14), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 392)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 16),
                "CBC", "DAD", "FEF", 'A', new ItemStack(IUItem.heat_reactor, 1, 24), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 385), 'C', new ItemStack(IUItem.crafting_elements, 1, 356), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 414), 'E', getBlockStack(BlockBaseMachine3.cooling), 'F',
                "plateBeryllium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 17),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.heat_reactor, 1, 16), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 420)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 18),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.heat_reactor, 1, 17), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 375)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 19),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.heat_reactor, 1, 18), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 392)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 20),
                "EFE", "CAC", "DGD", 'A', new ItemStack(IUItem.heat_reactor, 1, 24), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 436), 'C', new ItemStack(IUItem.crafting_elements, 1, 414),
                'D', new ItemStack(IUItem.crafting_elements, 1, 356), 'E', new ItemStack(IUItem.crafting_elements, 1, 320), 'F',
                new ItemStack(IUItem.crafting_elements, 1, 326), 'G', new ItemStack(IUItem.crafting_elements, 1, 357)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 21),
                "BDB", "CAC", "BEB", 'A', new ItemStack(IUItem.heat_reactor, 1, 20), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 417)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 410)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 22),
                "BDB", "CAC", "BEB", 'A', new ItemStack(IUItem.heat_reactor, 1, 21), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 375)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 310)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 23),
                "BDB", "CAC", "BEB", 'A', new ItemStack(IUItem.heat_reactor, 1, 22), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 392)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 368)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 28),
                "  ", "BAB", "CDC", 'A', new ItemStack(IUItem.heat_reactor, 1, 24),
                'B', new ItemStack(IUItem.crafting_elements, 1, 327),
                'C', new ItemStack(IUItem.crafting_elements, 1, 414),
                'D', "doubleplateTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 29),
                "BDB", "CAC", "BEB", 'A', new ItemStack(IUItem.heat_reactor, 1, 28), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 417)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 410)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 30),
                "BDB", "CAC", "BEB", 'A', new ItemStack(IUItem.heat_reactor, 1, 29), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 375)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 310)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 31),
                "BDB", "CAC", "BEB", 'A', new ItemStack(IUItem.heat_reactor, 1, 30), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 392)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 368)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 32),
                "DBD", " A ", " C ", 'A', new ItemStack(IUItem.heat_reactor, 1, 24), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 43), 'C', new ItemStack(IUItem.crafting_elements, 1, 367), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 387)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 33),
                "BDB", "EAE", "BCB", 'A', new ItemStack(IUItem.heat_reactor, 1, 32), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 417), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 45), 'C', new ItemStack(IUItem.crafting_elements, 1, 415), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 425)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 34),
                "BDB", "EAE", "BCB", 'A', new ItemStack(IUItem.heat_reactor, 1, 33), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 375), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 48), 'C', new ItemStack(IUItem.crafting_elements, 1, 380), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 372)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 35),
                "BDB", "EAE", "BCB", 'A', new ItemStack(IUItem.heat_reactor, 1, 34), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 392), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 50), 'C', new ItemStack(IUItem.crafting_elements, 1, 396), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 398)
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 36),
                "DCD", "EAE", "FBF", 'A', new ItemStack(IUItem.heat_reactor, 1, 24), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 43), 'C', new ItemStack(IUItem.crafting_elements, 1, 414), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 356), 'B', IUItem.elemotor, 'E',
                new ItemStack(IUItem.crafting_elements, 1, 446), 'F', "plateCarbon"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 37),
                "BDB", "CAC", "BEB", 'A', new ItemStack(IUItem.heat_reactor, 1, 36), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 417)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 410)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 38),
                "BDB", "CAC", "BEB", 'A', new ItemStack(IUItem.heat_reactor, 1, 37), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 375)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 310)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heat_reactor, 1, 39),
                "BDB", "CAC", "BEB", 'A', new ItemStack(IUItem.heat_reactor, 1, 38), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 392)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 368)
        );

        // gas reactor
        // 364
        // 418
        // 376
        // 393
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 12),
                "AA ", "AA ", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 364)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 13),
                "AA ", "AA ", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 418)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 14),
                "AA ", "AA ", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 376)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 15),
                "AA ", "AA ", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 393)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 4),
                "E F", "CAC", "DBD", 'A', new ItemStack(IUItem.gas_reactor, 1, 12),
                'B', getBlockStack(BlockBaseMachine3.cooling),
                'C', new ItemStack(IUItem.crafting_elements, 1, 414),
                'D', "plateBor", 'E', IUItem.coolingsensor, 'F', new ItemStack(IUItem.crafting_elements, 1, 385)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 5),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 4), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 418)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 6),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 5), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 376)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 7),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 6), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 393)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 8),
                "EFE", "CAC", "DBD", 'A', new ItemStack(IUItem.gas_reactor, 1, 12),
                'B', IUItem.fan,
                'C', new ItemStack(IUItem.crafting_elements, 1, 414),
                'D', "plateBor", 'F', IUItem.elemotor, 'E', "plateInvar"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 9),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 8), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 418)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 10),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 9), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 376)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 11),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 10), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 393)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 16),
                "EFE", "CAC", "DBD", 'A', new ItemStack(IUItem.gas_reactor, 1, 12),
                'B', getBlockStack(BlockSimpleMachine.compressor_iu),
                'C', new ItemStack(IUItem.crafting_elements, 1, 414),
                'D', "plateBor", 'F', new ItemStack(IUItem.crafting_elements, 1, 322), 'E', "plateCadmium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 17),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 16), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 418)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 18),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 17), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 376)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 19),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 18), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 393)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 20),
                "EFE", "CAC", "DBD", 'A', new ItemStack(IUItem.gas_reactor, 1, 12),
                'B', getBlockStack(BlockBaseMachine3.fluid_cooling),
                'C', new ItemStack(IUItem.crafting_elements, 1, 414),
                'D', new ItemStack(IUItem.crafting_elements, 1, 320), 'F', new ItemStack(IUItem.crafting_elements, 1, 326), 'E',
                "plateCarbon"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 21),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 20), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 418)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 22),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 21), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 376)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 23),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 22), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 393)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.gas_reactor, 1, 24),
                "EGF",
                "CAC",
                "DBD",
                'A',
                new ItemStack(IUItem.gas_reactor, 1, 12),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 324),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 414),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 320),
                'F',
                new ItemStack(IUItem.crafting_elements, 1, 321),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 342),
                'G',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 25),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 24), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 418)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 26),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 25), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 376)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 27),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 26), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 393)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 28),
                "EFE", "CAC", "DBD", 'A', new ItemStack(IUItem.gas_reactor, 1, 12),
                'B', IUItem.pump,
                'C', new ItemStack(IUItem.crafting_elements, 1, 414),
                'D', "plateBor", 'F', IUItem.elemotor, 'E', "plateInvar"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 29),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 28), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 418)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 30),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 29), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 376)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 31),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 30), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 393)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 32),
                "  ", "BAB", "CDC", 'A', new ItemStack(IUItem.gas_reactor, 1, 12),
                'B', new ItemStack(IUItem.crafting_elements, 1, 327),
                'C', new ItemStack(IUItem.crafting_elements, 1, 414),
                'D', "doubleplateChromium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 33),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 32), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 418)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 34),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 33), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 376)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 35),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 34), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 393)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 36),
                "CDC", "BAB", "   ", 'A', new ItemStack(IUItem.gas_reactor, 1, 12), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 335)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 414), 'D', new ItemStack(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 37),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 36), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 418)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 38),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 37), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 376)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 39),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.gas_reactor, 1, 38), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 393)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 0),
                "DBD", " A ", " C ", 'A', new ItemStack(IUItem.gas_reactor, 1, 12), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 43), 'C', new ItemStack(IUItem.crafting_elements, 1, 367), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 387)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 1),
                "BDB", "EAE", "BCB", 'A', new ItemStack(IUItem.gas_reactor, 1, 0), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 418), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 45), 'C', new ItemStack(IUItem.crafting_elements, 1, 415), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 425)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 2),
                "BDB", "EAE", "BCB", 'A', new ItemStack(IUItem.gas_reactor, 1, 1), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 376), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 48), 'C', new ItemStack(IUItem.crafting_elements, 1, 380), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 372)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.gas_reactor, 1, 3),
                "BDB", "EAE", "BCB", 'A', new ItemStack(IUItem.gas_reactor, 1, 2), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 393), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 50), 'C', new ItemStack(IUItem.crafting_elements, 1, 396), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 398)
        );


        // graphite reactor
        // 365
        // 419
        // 377
        // 394
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 24),
                "AA ", "AA ", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 365)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 25),
                "AA ", "AA ", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 419)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 26),
                "AA ", "AA ", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 377)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 27),
                "AA ", "AA ", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 394)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 0),
                "CBC", "FAF", "DBE", 'A', new ItemStack(IUItem.graphite_reactor, 1, 24), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 436), 'C', new ItemStack(IUItem.crafting_elements, 1, 414),
                'D', new ItemStack(IUItem.crafting_elements, 1, 342), 'E', new ItemStack(IUItem.crafting_elements, 1, 322), 'F',
                "plateLithium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 1),
                "BDB", "CAC", "BEB", 'A', new ItemStack(IUItem.graphite_reactor, 1, 0), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 419)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 428)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 2),
                "BDB", "CAC", "BEB", 'A', new ItemStack(IUItem.graphite_reactor, 1, 1), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 377)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 379)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 3),
                "BDB", "CAC", "BEB", 'A', new ItemStack(IUItem.graphite_reactor, 1, 2), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 394)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 404)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 4),
                "EFE", "CAC", "DGD", 'A', new ItemStack(IUItem.graphite_reactor, 1, 24), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 436), 'C', new ItemStack(IUItem.crafting_elements, 1, 414),
                'D', new ItemStack(IUItem.crafting_elements, 1, 356), 'E', new ItemStack(IUItem.crafting_elements, 1, 320), 'F',
                new ItemStack(IUItem.crafting_elements, 1, 326), 'G', new ItemStack(IUItem.crafting_elements, 1, 357)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 5),
                "BDB", "CAC", "BEB", 'A', new ItemStack(IUItem.graphite_reactor, 1, 4), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 419)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 410)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 6),
                "BDB", "CAC", "BEB", 'A', new ItemStack(IUItem.graphite_reactor, 1, 5), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 377)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 310)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 7),
                "BDB", "CAC", "BEB", 'A', new ItemStack(IUItem.graphite_reactor, 1, 6), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 394)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 368)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 8),
                "EFE", "CAC", "DBD", 'A', new ItemStack(IUItem.graphite_reactor, 1, 24), 'B',
                new ItemStack(IUItem.simple_exchanger_item, 1), 'C', new ItemStack(IUItem.crafting_elements, 1, 414),
                'D', new ItemStack(IUItem.crafting_elements, 1, 356), 'E', "plateCarbon", 'F', "doubleplateNichrome"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 9),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.graphite_reactor, 1, 8), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 419)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 10),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.graphite_reactor, 1, 9), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 377)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 11),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.graphite_reactor, 1, 10), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 394)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 12),
                "   ", "CAC", "DBD", 'A', new ItemStack(IUItem.graphite_reactor, 1, 24), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 322), 'C', new ItemStack(IUItem.crafting_elements, 1, 414),
                'D', new ItemStack(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 13),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.graphite_reactor, 1, 12), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 419)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 14),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.graphite_reactor, 1, 13), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 377)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 15),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.graphite_reactor, 1, 14), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 394)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 16),
                "DBD", " A ", " C ", 'A', new ItemStack(IUItem.graphite_reactor, 1, 24), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 43), 'C', new ItemStack(IUItem.crafting_elements, 1, 367), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 387)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 17),
                "BDB", "EAE", "BCB", 'A', new ItemStack(IUItem.graphite_reactor, 1, 16), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 419), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 45), 'C', new ItemStack(IUItem.crafting_elements, 1, 415), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 425)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 18),
                "BDB", "EAE", "BCB", 'A', new ItemStack(IUItem.graphite_reactor, 1, 17), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 377), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 48), 'C', new ItemStack(IUItem.crafting_elements, 1, 380), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 372)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 19),
                "BDB", "EAE", "BCB", 'A', new ItemStack(IUItem.graphite_reactor, 1, 18), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 394), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 50), 'C', new ItemStack(IUItem.crafting_elements, 1, 396), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 398)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 20),
                "DCD", "BAB", "   ", 'A', new ItemStack(IUItem.graphite_reactor, 1, 24), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 334), 'C', new ItemStack(IUItem.crafting_elements, 1, 356), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 414)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 21),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.graphite_reactor, 1, 20), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 419)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 22),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.graphite_reactor, 1, 21), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 377)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 23),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.graphite_reactor, 1, 22), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 394)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.graphite_reactor, 1, 28),
                "DBD",
                "CAC",
                "EFE",
                'A',
                new ItemStack(IUItem.graphite_reactor, 1, 24),
                'B',
                new ItemStack(IUItem.capacitor),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 356),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 414),
                'E',
                "plateSteel",
                'F',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 29),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.graphite_reactor, 1, 28), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 419)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 30),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.graphite_reactor, 1, 29), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 377)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 31),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.graphite_reactor, 1, 30), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 394)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 32),
                "CDC", "BAB", "   ", 'A', new ItemStack(IUItem.graphite_reactor, 1, 24), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 327), 'C', new ItemStack(IUItem.crafting_elements, 1, 356), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 414)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 33),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.graphite_reactor, 1, 32), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 419)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 34),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.graphite_reactor, 1, 33), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 377)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 35),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.graphite_reactor, 1, 34), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 394)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 36),
                "DBD", "CAC", "EFG", 'A', new ItemStack(IUItem.graphite_reactor, 1, 24), 'B',
                new ItemStack(IUItem.coolingsensor, 1), 'C', new ItemStack(IUItem.crafting_elements, 1, 356), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 414), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 324), 'F',
                getBlockStack(BlockBaseMachine3.cooling), 'G',
                new ItemStack(IUItem.crafting_elements, 1, 321)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 37),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.graphite_reactor, 1, 36), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 419)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'D', new ItemStack(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 38),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.graphite_reactor, 1, 37), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 377)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', new ItemStack(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.graphite_reactor, 1, 39),
                "BDB", "CAC", "B B", 'A', new ItemStack(IUItem.graphite_reactor, 1, 38), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 394)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 397), 'D', new ItemStack(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.planner, 1),
                "AAA", "CBC", " D ", 'A', "plateAlcled", 'B',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', "plateGold"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.deplanner, 1),
                "AAA", "CBC", " D ", 'A', "plateYttriumAluminiumGarnet", 'B',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 373), 'D', "platePalladium"
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 106),
                "DFE", "CAC", " B ", 'A', IUItem.advancedMachine, 'B',
                new ItemStack(IUItem.crafting_elements, 1, 60)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 51), 'D', new ItemStack(IUItem.crafting_elements, 1, 243), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 130), 'F', new ItemStack(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 107),
                "FEF", "CAD", "GBG", 'A', new ItemStack(IUItem.basemachine2, 1, 3), 'B',
                new ItemStack(IUItem.basemachine2, 1, 2)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 60), 'D', new ItemStack(IUItem.crafting_elements, 1, 27), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 155), 'F', new ItemStack(IUItem.crafting_elements, 1, 402), 'G',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_rotors_upgrade, 1, 4), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.advQuantumtool), ('B'), new ItemStack(
                        IUItem.core,
                        1,
                        4
                ),
                ('C'), new ItemStack(IUItem.water_rotors_upgrade, 1, 3), ('D'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_rotors_upgrade, 1, 5), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.adv_spectral_box), ('B'), new ItemStack(
                        IUItem.core,
                        1,
                        6
                ),
                ('C'), new ItemStack(IUItem.water_rotors_upgrade, 1, 4), ('D'),
                IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 6),
                "ADA",
                "CBC",
                "ADA",

                ('A'),
                new ItemStack(IUItem.advnanobox),
                ('B'),
                new ItemStack(IUItem.module_schedule),
                ('C'),
                IUItem.iridiumPlate,
                (
                        'D'),
                "casingDuralumin"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_rotors_upgrade, 1, 7), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.advQuantumtool), ('B'), new ItemStack(
                        IUItem.core,
                        1,
                        4
                ),
                ('C'), new ItemStack(IUItem.water_rotors_upgrade, 1, 6), ('D'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_rotors_upgrade, 1, 8), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.adv_spectral_box), ('B'), new ItemStack(
                        IUItem.core,
                        1,
                        6
                ),
                ('C'), new ItemStack(IUItem.water_rotors_upgrade, 1, 7), ('D'),
                IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 9),
                "ADA",
                "CBC",
                "ADA",

                ('A'),
                new ItemStack(IUItem.adv_spectral_box),
                ('B'),
                new ItemStack(IUItem.module_schedule),
                ('C'),
                IUItem.compressIridiumplate,
                (
                        'D'),
                "doubleplateAlumel"
        );
        Recipes.recipe.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 10),
                "ADA",
                "CBC",
                "DED",

                ('A'),
                new ItemStack(IUItem.advnanobox),
                ('B'),
                new ItemStack(IUItem.module_schedule),
                ('C'),
                IUItem.iridiumPlate,
                (
                        'D'),
                "doubleplatePlatinum",
                ('E'),
                new ItemStack(IUItem.core, 1, 3)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_rotors_upgrade, 1, 11), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.advQuantumtool), ('B'), new ItemStack(
                        IUItem.core,
                        1,
                        4
                ),
                ('C'), new ItemStack(IUItem.water_rotors_upgrade, 1, 10), ('D'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_rotors_upgrade, 1, 12), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.adv_spectral_box), ('B'), new ItemStack(
                        IUItem.core,
                        1,
                        6
                ),
                ('C'), new ItemStack(IUItem.water_rotors_upgrade, 1, 11), ('D'),
                IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_rotors_upgrade, 1, 13), "ADA", "CBC", "DED",

                ('A'), new ItemStack(IUItem.advnanobox),

                ('B'), new ItemStack(IUItem.module_schedule),

                ('C'), IUItem.iridiumPlate,

                ('D'),
                "doubleplateRedbrass",

                ('E'), new ItemStack(IUItem.core, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_rotors_upgrade, 1, 14), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.advQuantumtool), ('B'), new ItemStack(
                        IUItem.core,
                        1,
                        5
                ),
                ('C'), new ItemStack(IUItem.water_rotors_upgrade, 1, 13), ('D'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_rotors_upgrade, 1, 15), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.adv_spectral_box), ('B'), new ItemStack(
                        IUItem.core,
                        1,
                        6
                ),
                ('C'), new ItemStack(IUItem.water_rotors_upgrade, 1, 14), ('D'),
                IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 0), "BBB", "CAC", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateStainlessSteel",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 124), "B B", "A A", "CCC",

                ('C'), "plankWood",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 164),

                ('B'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 185), "ABA", "BBB", "AAA",

                ('A'), "plankWood",

                ('B'), new ItemStack(IUItem.fluidCell)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 186), "CCC", "ABA", "CCC",

                ('A'), "plateSteel", ('C'), "plateIron",

                ('B'), new ItemStack(IUItem.basemachine2, 1, 185)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 496), "AAA", "ABA", "AAA",

                ('A'), "plateTitanium", ('B'), "ingotIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 497), "AAA", "ABA", "AAA",

                ('A'), "plateInvar", ('B'), new ItemStack(IUItem.crafting_elements, 1, 496)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockSmeltery.smeltery_casing), " C ", "ABA", " C ",

                ('A'), "casingSpinel", ('B'), new ItemStack(Blocks.BRICK_BLOCK), 'C', "casingYttrium"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockSmeltery.smeltery_tank), " C ", "DAD", " B ",

                ('A'), new ItemStack(IUItem.basemachine2, 1, 185), ('B'), getBlockStack(BlockSmeltery.smeltery_casing), 'C',
                "platePolonium", 'D', "plateBarium"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockSmeltery.smeltery_fuel_tank), " C ", "ABA", " D ",

                ('A'), new ItemStack(IUItem.basemachine2, 1, 185), ('B'), getBlockStack(BlockSmeltery.smeltery_casing), 'C',
                Items.LAVA_BUCKET, 'D', "plateStrontium"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockSmeltery.smeltery_furnace), " C ", " B ", "   ",

                ('A'), new ItemStack(IUItem.basemachine2, 1, 185), ('B'), getBlockStack(BlockSmeltery.smeltery_casing), 'C',
                Blocks.FURNACE, 'D', "plateStrontium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 601), "B B", "BAB", "BBB",

                'B', "plateElectrum", 'A', getBlockStack(BlockBaseMachine3.oak_tank)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 578), "B B", "BAB", "BAB",

                'B', "plateIron", 'A', "plateInvar"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.laser), " A ", " A ", " B ",

                'B', "plateGold", 'A', "dustRedstone"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.solderingIron), " A ", " C ", " B ",

                'B', "plateGold", 'A', "gemQuartz", 'C', "ingotIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 183), " B ", "CAD", " E ",

                'A', new ItemStack(IUItem.blockResource, 1, 9),
                'B', new ItemStack(IUItem.crafting_elements, 1, 44),
                'C', new ItemStack(IUItem.crafting_elements, 1, 73),
                'D', new ItemStack(IUItem.crafting_elements, 1, 66),
                'E', new ItemStack(IUItem.motors_with_improved_bearings_)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 108), "CBC", " A ", "CEC",

                'A', new ItemStack(IUItem.basemachine2, 1, 7),
                'B', new ItemStack(IUItem.crafting_elements, 1, 124),
                'C', new ItemStack(IUItem.crafting_elements, 1, 137),
                'D', new ItemStack(IUItem.crafting_elements, 1, 66),
                'E', new ItemStack(IUItem.motors_with_improved_bearings_)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 109), "CBC", " A ", "C C",

                'A', new ItemStack(IUItem.basemachine2, 1, 108),
                'B', new ItemStack(IUItem.crafting_elements, 1, 135),
                'C', new ItemStack(IUItem.crafting_elements, 1, 138),
                'D', new ItemStack(IUItem.crafting_elements, 1, 66),
                'E', new ItemStack(IUItem.motors_with_improved_bearings_)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 110), "CBC", " A ", "C C",

                'A', new ItemStack(IUItem.basemachine2, 1, 109),
                'B', new ItemStack(IUItem.crafting_elements, 1, 146),
                'C', new ItemStack(IUItem.crafting_elements, 1, 139),
                'D', new ItemStack(IUItem.crafting_elements, 1, 66),
                'E', new ItemStack(IUItem.motors_with_improved_bearings_)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 111), "CBC", " A ", "C C",

                'A', new ItemStack(IUItem.basemachine2, 1, 110),
                'B', new ItemStack(IUItem.crafting_elements, 1, 157),
                'C', new ItemStack(IUItem.crafting_elements, 1, 140),
                'D', new ItemStack(IUItem.crafting_elements, 1, 66),
                'E', new ItemStack(IUItem.motors_with_improved_bearings_)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pho_machine, 1, 21), "CBC", " A ", "C C",

                'A', new ItemStack(IUItem.basemachine2, 1, 111),
                'B', new ItemStack(IUItem.crafting_elements, 1, 632),
                'C', new ItemStack(IUItem.crafting_elements, 1, 623),
                'D', new ItemStack(IUItem.crafting_elements, 1, 66),
                'E', new ItemStack(IUItem.motors_with_improved_bearings_)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 184), " B ", "CAD", " E ",

                'A', new ItemStack(IUItem.blockResource, 1, 8),
                'B', new ItemStack(IUItem.crafting_elements, 1, 73),
                'C', new ItemStack(IUItem.crafting_elements, 1, 51),
                'D', new ItemStack(IUItem.crafting_elements, 1, 44),
                'E', new ItemStack(IUItem.crafting_elements, 1, 276)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 153), " B ", " A ", " C ",

                'A', new ItemStack(IUItem.blockResource, 1, 9),
                'B', new ItemStack(IUItem.crafting_elements, 1, 72),
                'C', new ItemStack(IUItem.crafting_elements, 1, 276)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 152), "B D", " A ", " C ",

                'A', new ItemStack(IUItem.basemachine2, 1, 153),
                'B', new ItemStack(IUItem.crafting_elements, 1, 43),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20),
                'D', new ItemStack(IUItem.crafting_elements, 1, 27)
        );

        int i;
        for (i = 0; i < 11; i++) {
            Recipes.recipe.addRecipe(
                    new ItemStack(IUItem.universal_cable, 1, i), "CAC", "ABA", "CAC",
                    'B', new ItemStack(
                            IUItem.cable,
                            1,
                            i
                    ), 'A', "wool", 'C', IUItem.synthetic_plate
            );
        }
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewater, 1, 0), "CAC", "ABA", " A ",

                ('A'), "logWood",

                ('B'), "plankWood",
                ('C'), new ItemStack(Items.DYE, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewater, 1, 1), " A ", "ABA", " A ",

                ('A'), "blockBronze",

                ('B'), new ItemStack(IUItem.water_rotor_wood, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewater, 1, 2), " A ", "ABA", " A ",

                ('A'), "blockIron",

                ('B'), new ItemStack(IUItem.water_rotor_bronze, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewater, 1, 3), " A ", "ABA", " A ",

                ('A'), "plateSteel",

                ('B'), new ItemStack(IUItem.water_rotor_iron, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewater, 1, 4), " A ", "ABA", " A ",

                ('A'), "plateCarbon",

                ('B'), new ItemStack(IUItem.water_rotor_steel, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewater, 1, 5), "CAC", "ABA", "CAC",

                ('C'), "doubleplateIridium",

                ('A'), IUItem.iridiumPlate,

                ('B'), new ItemStack(IUItem.water_rotor_carbon, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewater, 1, 6), "CAC", "ABA", "CAC",

                ('C'), IUItem.compressIridiumplate,

                ('A'), new ItemStack(IUItem.compresscarbon),

                ('B'), new ItemStack(IUItem.water_iridium
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewater, 1, 7), "DCD", "ABA", "DCD",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('C'), new ItemStack(IUItem.compressIridiumplate),

                ('A'), new ItemStack(IUItem.advnanobox),

                ('B'),
                new ItemStack(IUItem.water_compressiridium
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewater, 1, 8), "DCD", "ABA", " C ",

                ('D'), new ItemStack(IUItem.excitednucleus, 1, 5),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('A'), new ItemStack(IUItem.quantumtool),

                ('B'),
                new ItemStack(IUItem.water_spectral
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewater, 1, 10), "DCD", "CBC", " C ",

                ('D'), new ItemStack(IUItem.excitednucleus, 1, 6),

                ('C'), new ItemStack(IUItem.quantumtool),

                ('B'), new ItemStack(IUItem.water_myphical
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewater, 1, 9), "DCD", "CBC", "ACA",

                ('D'), new ItemStack(IUItem.neutroniumingot),

                ('A'), IUItem.iridiumPlate,

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'),
                new ItemStack(IUItem.water_photon
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewater, 1, 11), "DCD", "CBC", " C ",

                ('D'), new ItemStack(IUItem.excitednucleus, 1, 5),

                ('C'), new ItemStack(IUItem.advQuantumtool),

                ('B'), new ItemStack(IUItem.water_neutron
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewater, 1, 12), "ACA", "CBC", "ACA",

                ('A'), new ItemStack(IUItem.doublecompressIridiumplate),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), new ItemStack(IUItem.water_barionrotor
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewater, 1, 13), "ECE", "CBC", "ACA",

                ('E'), TileGenerationMicrochip.getLevelCircuit(new ItemStack(IUItem.basecircuit, 1, 21), 11),

                ('A'), new ItemStack(IUItem.doublecompressIridiumplate),

                ('C'), new ItemStack(IUItem.photoniy_ingot),

                ('B'),
                new ItemStack(IUItem.water_adronrotor
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_rotors_upgrade, 1, 0), "A A", "CBC", "A A",

                ('A'), new ItemStack(IUItem.advnanobox),
                ('B'), new ItemStack(IUItem.module_schedule), ('C'), "casingNichrome"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_rotors_upgrade, 1, 1), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.advQuantumtool), ('B'), new ItemStack(
                        IUItem.core,
                        1,
                        3
                ),
                ('C'), new ItemStack(IUItem.water_rotors_upgrade, 1, 0), ('D'),
                "casingRedbrass"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_rotors_upgrade, 1, 2), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.adv_spectral_box), ('B'), new ItemStack(
                        IUItem.core,
                        1,
                        5
                ),
                ('C'), new ItemStack(IUItem.water_rotors_upgrade, 1, 1), ('D'),
                "casingMuntsa"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.water_rotors_upgrade, 1, 3), "A A", "CBC", "A A",

                ('A'), new ItemStack(IUItem.advnanobox),
                ('B'), new ItemStack(IUItem.module_schedule), ('C'), IUItem.iridiumPlate
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 574), "C C", "BAB", "BAB",

                'B', "plateTungsten", 'A', "plateInvar", 'C', "plateZinc"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 593), "CAC", "BDB", "BAB",

                'B', "plateThallium", 'A', "plateHafnium", 'C', "plateZinc", 'D', "dustRedstone"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockPrimalWireInsulator.primal_wire_insulator), "C C", "ACA", "BAB",

                'B', "logWood", 'A', "plankWood", 'C', "plateIron"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockStrongAnvil.block_strong_anvil), "CCC", "ADA", "   ",

                'A', "plateSilver", 'C', "blockIridium", 'D', getBlockStack(BlockAnvil.block_anvil)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockMiniSmeltery.mini_smeltery),
                "ACA",
                "EDE",
                "BBB",

                'A',
                "gearFerromanganese",
                'B',
                getBlockStack(BlockSmeltery.smeltery_casing),
                'C',
                new ItemStack(IUItem.crafting_elements, 1,
                        496
                ),
                'D',
                getBlockStack(BlockBaseMachine3.oak_tank),
                'E',
                "plateIridium"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockRefractoryFurnace.refractory_furnace), "ACA", "EDE", "EBE",

                'A', "gearInvar", 'B', "blockTungsten", 'C', Blocks.FURNACE,
                'D',
                getBlockStack(BlockBaseMachine3.steel_tank), 'E', "plateSteel"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.steam_storage), " A ", "ABA", " A ",

                'A', "casingAluminumbronze", 'B', new ItemStack(IUItem.crafting_elements, 1,
                        601
                )
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.steam_crystal_charge), "AED", " B ", " C ",

                'A', new ItemStack(IUItem.crafting_elements, 1,
                        123
                ), 'D', new ItemStack(IUItem.crafting_elements, 1,
                        32
                ), 'B', new ItemStack(IUItem.crafting_elements, 1,
                        601
                ), 'C', new ItemStack(IUItem.blockResource, 1, 12), 'E', IUItem.charged_redstone
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.steam_fluid_heater), "AED", " B ", " C ",

                'A', new ItemStack(IUItem.crafting_elements, 1,
                        123
                ), 'D', new ItemStack(IUItem.crafting_elements, 1,
                        32
                ), 'B', new ItemStack(IUItem.crafting_elements, 1,
                        601
                ), 'C', new ItemStack(IUItem.blockResource, 1, 12), 'E', IUItem.primalFluidHeater
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.steam_wire_insulator),
                "AED",
                " B ",
                " C ",

                'A',
                new ItemStack(IUItem.crafting_elements, 1,
                        123
                ),
                'D',
                new ItemStack(IUItem.crafting_elements, 1,
                        32
                ),
                'B',
                getBlockStack(BlockPrimalWireInsulator.primal_wire_insulator),
                'C',
                new ItemStack(IUItem.blockResource, 1, 12),
                'E',
                new ItemStack(IUItem.crafting_elements, 1,
                        163
                )
        );

        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.steam_rolling), "AED", " B ", " C ",

                'A', new ItemStack(IUItem.crafting_elements, 1,
                        123
                ), 'D', new ItemStack(IUItem.crafting_elements, 1,
                        32
                ), 'B', getBlockStack(BlockStrongAnvil.block_strong_anvil), 'C', new ItemStack(IUItem.blockResource, 1, 12), 'E',
                new ItemStack(IUItem.crafting_elements, 1,
                        165
                )
        );

        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.steam_quarry), "AED", "BCB", "   ",

                'A', new ItemStack(IUItem.crafting_elements, 1,
                        123
                ), 'D', new ItemStack(IUItem.crafting_elements, 1,
                        32
                ), 'B', getBlockStack(BlockBaseMachine3.quarry_pipe), 'C', new ItemStack(IUItem.blockResource, 1, 12), 'E',
                new ItemStack(IUItem.crafting_elements, 1,
                        158
                )
        );

        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.adv_steam_quarry), "ACA", " B ", "A A",
                'B', getBlockStack(BlockBaseMachine3.steam_quarry), 'A', new ItemStack(IUItem.crafting_elements, 1,
                        138
                ), 'C', new ItemStack(IUItem.crafting_elements, 1,
                        256
                )

        );

        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockBaseMachine3.quarry_pipe), 16), "A A", "A A", "A A",
                'A', "plateIron"

        );

        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockBaseMachine3.steam_bio_generator), 1), " D ", "BCB", " A ",
                'A', new ItemStack(IUItem.blockResource, 1, 14), 'C', getBlockStack(BlockBaseMachine3.steam_peat_generator), 'B',
                new ItemStack(IUItem.crafting_elements, 1,
                        295
                ), 'D',
                new ItemStack(IUItem.crafting_elements, 1,
                        64
                )

        );

        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamBoiler.steam_boiler_casing), 1), "CAC", "ABA", "CAC",
                'A', "casingSteel", 'C', "casingAluminumbronze", 'B', "blockIridium"
        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamBoiler.steam_boiler_tank), 1), " B ", " A ", " C ",
                'A', getBlockStack(BlockSteamBoiler.steam_boiler_casing), 'B', new ItemStack(IUItem.crafting_elements, 1,
                        601
                ), 'C', new ItemStack(IUItem.crafting_elements, 1,
                        154
                )
        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamBoiler.steam_boiler_heater), 1), " B ", " A ", " C ",
                'A', getBlockStack(BlockSteamBoiler.steam_boiler_casing), 'B',
                getBlockStack(BlockPrimalFluidHeater.primal_fluid_heater), 'C', "gearInvar"
        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamBoiler.steam_boiler_heat_exchanger), 1),
                " B ",
                "CAC",
                " B ",
                'A',
                getBlockStack(BlockSteamBoiler.steam_boiler_casing),
                'B',
                new ItemStack(IUItem.crafting_elements, 1,
                        356
                ),
                'C',
                "gearInvar"
        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamBoiler.steam_boiler_controller), 1),
                "CBC",
                "DAD",
                "   ",
                'A',
                getBlockStack(BlockSteamBoiler.steam_boiler_casing),
                'B',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1),
                'C',
                "plateOsmium",
                'D',
                "casingBismuth"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 709), "AAA", "   ", "AAA",
                'A', "plateTungsten"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 720), "AAA", "   ", "AAA",
                'A', "plateMuntsa"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 717), "AAA", "   ", "AAA",
                'A', "plateStainlessSteel"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 715), "AAA", "   ", "AAA",
                'A', "plateNitenol"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 726), "AA ", "BB ", "   ",
                'A', "plateTitanium", 'B', "plateadvancedAlloy"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 727), "AA ", "BB ", "   ",
                'A', new ItemStack(IUItem.crafting_elements, 1, 504), 'B', "plateadvancedAlloy"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 707), "AA ", "BB ", "   ",
                'A', "plateAlumel", 'B', "plateadvancedAlloy"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 711), "AA ", "BB ", "   ",
                'A', new ItemStack(IUItem.plate, 1, 48), 'B', "plateadvancedAlloy"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 2, 714), "AAA", "BBA", "AAA",
                'A', "plateMolybdenum", 'B', new ItemStack(IUItem.crafting_elements, 1, 0)

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 2, 725), "AAA", "BBA", "AAA",
                'A', "plateSteel", 'B', new ItemStack(IUItem.crafting_elements, 1, 0)

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 2, 721), "AAA", "BBA", "AAA",
                'A', "plateOrichalcum", 'B', new ItemStack(IUItem.crafting_elements, 1, 0)

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 2, 708), "AAA", "BBA", "AAA",
                'A', "plateBerylliumBronze", 'B', new ItemStack(IUItem.crafting_elements, 1, 0)

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 716), "AAA", " A ", " A ",
                'A', "plateNeodymium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 718), "AAA", " A ", " A ",
                'A', "plateInconel"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 719), "AAA", " A ", " A ",
                'A', "plateNiobiumTitanium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 712), "AAA", " A ", " A ",
                'A', "plateHafniumCarbide"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 710), "CBC", "BAB", "CBC",
                'A', "plateMikhail", 'B', "plateIridium", 'C', "plateInvar"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 713), "ABA", "BCB", "ABA",
                'A', "nuggetElectrum", 'B', "plateIron", 'C', "ingotGold"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 722), " AA", "BCA", " DD",
                'A', "plateMikhail", 'B', new ItemStack(IUItem.crafting_elements, 1, 121), 'C', "platePlatinum", 'D', "plateLapis"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 723), " A ", "ABA", " A ",
                'A', "plateChromium", 'B', "plateThallium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 724), "ABA", "BCB", "ABA",
                'A', "plateadvancedAlloy", 'B', "plateIron", 'C', "gemSapphire"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 695), "D D", "CBC", "BAB",
                'A', IUItem.overclockerUpgrade, 'B', "plateIridium", 'C', "plateTantalum", 'D', "plateThallium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 702), "D D", "CBC", "BAB",
                'A', IUItem.overclockerUpgrade, 'B', "plateOsmiridium", 'C', "plateTantalum", 'D', "plateThallium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 692), "D D", "CBC", "BAB",
                'A', IUItem.overclockerUpgrade, 'B', "plateVanadoalumite", 'C', "plateTantalum", 'D', "plateThallium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 699), "D D", "CBC", "BAB",
                'A', IUItem.overclockerUpgrade, 'B', "plateStainlessSteel", 'C', "plateTantalum", 'D', "plateThallium"

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 704), "CBC", "BAB", "CBC",
                'A', new ItemStack(IUItem.solar_day_glass, 1, 0), 'B', "plateOsmium", 'C', "plateLead"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 697), "CBC", "BAB", "CBC",
                'A', new ItemStack(IUItem.solar_day_glass, 1, 2), 'B', "plateOsmium", 'C', "plateLithium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 696), "CBC", "BAB", "CBC",
                'A', new ItemStack(IUItem.solar_day_glass, 1, 4), 'B', "plateOsmium", 'C', "plateAluminiumLithium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 693), "  B", " BB", "BBA",
                'A', new ItemStack(Items.SHIELD), 'B', "plateVitalium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 700), "  B", " BB", "BBA",
                'A', new ItemStack(Items.SHIELD), 'B', "plateNimonic"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 703), "  B", " BB", "BBA",
                'A', new ItemStack(Items.SHIELD), 'B', "platePermalloy"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 706), "  B", " BB", "BBA",
                'A', new ItemStack(Items.SHIELD), 'B', "plateSteel"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 690), "BBB", "ABA", "BBB",
                'A', "plateAlcled", 'B', "plateAluminumbronze"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 691), "C B", " BD", "BDA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 504), 'B', new ItemStack(IUItem.crafting_elements, 1, 480), 'C',
                "plateTungsten", 'D', "plateHafniumBoride"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 694), "C C", "ACA", "BAB",
                'A', "plateTantalumTungstenHafnium", 'B', "nuggetIridium", 'C', "plateMikhail"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 698), " AA", "ABA", "AA ",
                'A', "plateCobaltChrome", 'B', "plateVitalium", 'C', "plateMikhail"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 701), " AC", "ABA", "CA ",
                'A', "plateOsmiridium", 'B', new ItemStack(IUItem.blockResource, 1, 13), 'C', "plateIridium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 705), "ACC", "ABB", " CC",
                'A', Items.REDSTONE, 'B', new ItemStack(IUItem.blockResource, 1, 13), 'C', "plateArsenic", 'B', "plateSteel"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 729), "A A", "AB ", " AA",
                'A', "plateAluminium", 'B', "stickAluminium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 735), "A A", "AB ", " AA",
                'A', "plateDuralumin", 'B', "stickCadmium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 728), "A A", "AB ", " AA",
                'A', "plateAlcled", 'B', "stickThallium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 760), "A A", "AB ", " AA",
                'A', "plateSuperalloyRene", 'B', "stickBarium"

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 745),
                "BDD",
                "CAE",
                "BCC",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'B',
                new ItemStack(IUItem.cable, 1, 11),
                'C',
                new ItemStack(IUItem.graphene_wire),
                'D',
                "plateZinc",
                'E',
                "plateTin"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 763), "BDD", "CAE", "BCC",
                'A', TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4), 'B', new ItemStack(IUItem.cable, 1, 11),
                'C', new ItemStack(IUItem.graphene_wire), 'D', "plateNitenol", 'E', "plateAlumel"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 751), "BDD", "CAE", "BCC",
                'A', TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'B', new ItemStack(IUItem.cable, 1, 11),
                'C', new ItemStack(IUItem.graphene_wire), 'D', new ItemStack(IUItem.plate, 1, 50), 'E', "plateVitalium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 757),
                "BDD",
                "CAE",
                "BCC",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),
                'B',
                new ItemStack(IUItem.cable, 1, 11),
                'C',
                new ItemStack(IUItem.graphene_wire),
                'D',
                new ItemStack(IUItem.plate, 1, 51),
                'E',
                "plateYttriumAluminiumGarnet"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 742), "AAB", "ABC", "BCC",
                'A', "plateOsmium", 'B', "plateSteel", 'C', "plateLead"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 761), "AAB", "ABC", "BCC",
                'A', "plateOsmium", 'B', "plateBarium", 'C', "plateZeliber"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 748), "AAB", "ABC", "BCC",
                'A', "plateOsmium", 'B', "plateNiobiumTitanium", 'C', "plateHafniumCarbide"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 754), "AAB", "ABC", "BCC",
                'A', "plateOsmium", 'B', "plateAluminiumLithium", 'C', "plateMolybdenumSteel"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 740), " AA", "AAA", "AA ",
                'A', "plateNeodymium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 741), " AA", "AAA", "AA ",
                'A', "plateNimonic"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 767), " AA", "AAA", "AA ",
                'A', "plateFerromanganese"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 736), " AA", "AAA", "AA ",
                'A', "plateYttriumAluminiumGarnet"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 746), "AAB", "ABA", "C C",
                'A', "plateTungsten", 'B', Items.REDSTONE, 'C', "plateOsmium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 730), "AAB", "ABA", "C C",
                'A', "plateTungsten", 'B', Items.REDSTONE, 'C', "plateGalliumArsenic"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 737), "AAB", "ABA", "C C",
                'A', "plateTungsten", 'B', Items.REDSTONE, 'C', "plateYttriumAluminiumGarnet"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 759), "AAB", "ABA", "C C",
                'A', "plateTungsten", 'B', Items.REDSTONE, 'C', "plateStellite"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 743), " A ", "ABA", "BBB",
                'A', "plateYttrium", 'B', "plateSteel"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 762), " A ", "ABA", "BBB",
                'A', "plateVitalium", 'B', "plateBarium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 749), " A ", "ABA", "BBB",
                'A', "plateRedbrass", 'B', "plateHafniumCarbide"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 755), " A ", "ABA", "BBB",
                'A', "plateBerylliumBronze", 'B', new ItemStack(IUItem.plate, 1, 51)

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 744), "BAB", "C C", "BAB",
                'A', "plateHafniumBoride", 'B', "plateIron", 'C', "plateTin"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 764), "BAB", "C C", "BAB",
                'A', new ItemStack(IUItem.plate, 1, 49), 'B', "plateAlumel", 'C', "plateBerylliumBronze"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 750), "BAB", "C C", "BAB",
                'A', "plateCobaltChrome", 'B', new ItemStack(IUItem.plate, 1, 50), 'C', "plateOsmiridium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 756), "BAB", "C C", "BAB",
                'A', "plateWoods", 'B', "plateMolybdenumSteel", 'C', "plateVanadoalumite"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 731), "BAB", "ABA", "BAB",
                'A', "plateTitanium", 'B', Items.REDSTONE

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 732), " AA", " BA", "   ",
                'A', "plateSteel", 'B', "plateTungsten"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 733), " AB", "ABA", " A ",
                'A', "plateVitalium", 'B', "plateHafnium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 734), "ABA", "CBC", "CBC",
                'A', "plateThallium", 'B', "plateSilver", 'C', "plateTantalum"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 738), "BAB", "BBB", "C C",
                'A', "platePolonium", 'B', "plateTantalum", 'C', "nuggetGallium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 739), " AB", "ABA", " A ",
                'A', "plateCobaltChrome", 'B', "plateNiobiumTitanium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 747), " A ", "BBB", "BCB",
                'A', "plateBismuth", 'B', "plateCobaltChrome", 'C', "plateThallium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 752), " A ", "ABA", " AC",
                'A', "plateManganese", 'B', "plateYttrium", 'C', "plateBarium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 753), "ABB", "BAC", " BC",
                'A', "plateBarium", 'B', "plateElectrum", 'C', "plateGalliumArsenic"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 758), "ABA", "AAA", "CBC",
                'A', "plateChromium", 'B', "plateMikhail", 'C', "plateSpinel"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 766), " AB", "ABC", " BC",
                'A', "plateAluminiumSilicon", 'B', "plateVitalium", 'C', "plateCobaltChrome"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 768), "ABC", "BAB", "CBA",
                'A', "plateHafnium", 'B', "plateAlcled", 'C', "plateAluminiumSilicon"

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockWindTurbine.wind_turbine_casing_1), " B ", "BAB", " B ",
                'A', Blocks.BRICK_BLOCK, 'B'
                , "plateStainlessSteel"
        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockWindTurbine.wind_turbine_casing_2), 4), "A  ", "AA ", "AAA",
                'A', getBlockStack(BlockWindTurbine.wind_turbine_casing_1)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockWindTurbine.wind_turbine_casing_3), "   ", "BAB", "   ",
                'A', getBlockStack(BlockWindTurbine.wind_turbine_casing_1), 'B'
                , getBlockStack(BlockWindTurbine.wind_turbine_casing_2)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockWindTurbine.wind_turbine_controller), "DCD", "BAB", " E ",
                'A', getBlockStack(BlockWindTurbine.wind_turbine_casing_1), 'B'
                , "plateNimonic", 'C', TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10), 'D',
                "plateNitenol", 'E', getBlockStack(BlockBaseMachine3.per_wind_generator)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockWindTurbine.wind_turbine_socket), "DCD", "BAB", " E ",
                'A', getBlockStack(BlockWindTurbine.wind_turbine_casing_1), 'B'
                , new ItemStack(IUItem.crafting_elements, 1, 402), 'C', new ItemStack(IUItem.crafting_elements, 1, 596), 'D',
                IUItem.plastic_plate, 'E', "plateSuperalloyHaynes"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockWindTurbine.wind_turbine_stabilizer), "DCD", "BAB", " E ",
                'A', getBlockStack(BlockWindTurbine.wind_turbine_casing_1), 'B'
                , new ItemStack(IUItem.electronic_stabilizers), 'C', new ItemStack(IUItem.crafting_elements, 1, 599), 'D',
                IUItem.photoniy_ingot, 'E', new ItemStack(IUItem.crafting_elements, 1, 594)
        );


        Recipes.recipe.addRecipe(getBlockStack(BlockHydroTurbine.hydro_turbine_casing_1), " B ", "BAB", " B ",
                'A', Blocks.BRICK_BLOCK, 'B'
                , "plateInconel"
        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockHydroTurbine.hydro_turbine_casing_2), 4),
                "A  ",
                "AA ",
                "AAA",
                'A',
                getBlockStack(BlockHydroTurbine.hydro_turbine_casing_1)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockHydroTurbine.hydro_turbine_casing_3), "   ", "BAB", "   ",
                'A', getBlockStack(BlockHydroTurbine.hydro_turbine_casing_1), 'B'
                , getBlockStack(BlockHydroTurbine.hydro_turbine_casing_2)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockHydroTurbine.hydro_turbine_controller), "DCD", "BAB", " E ",
                'A', getBlockStack(BlockHydroTurbine.hydro_turbine_casing_1), 'B'
                , "plateNimonic", 'C', TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10), 'D',
                "plateNitenol", 'E', getBlockStack(BlockBaseMachine3.per_water_generator)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockHydroTurbine.hydro_turbine_socket), "DCD", "BAB", " E ",
                'A', getBlockStack(BlockHydroTurbine.hydro_turbine_casing_1), 'B'
                , new ItemStack(IUItem.crafting_elements, 1, 402), 'C', new ItemStack(IUItem.crafting_elements, 1, 596), 'D',
                IUItem.plastic_plate, 'E', "plateSuperalloyHaynes"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockHydroTurbine.hydro_turbine_stabilizer), "DCD", "BAB", " E ",
                'A', getBlockStack(BlockHydroTurbine.hydro_turbine_casing_1), 'B'
                , new ItemStack(IUItem.electronic_stabilizers), 'C', new ItemStack(IUItem.crafting_elements, 1, 599), 'D',
                IUItem.photoniy_ingot, 'E', new ItemStack(IUItem.crafting_elements, 1, 594)
        );

        Recipes.recipe.addRecipe(getBlockStack(BlockBarrel.barrel), "ABA", "ABC", "ABA",
                'A', "logWood", 'B'
                , "plankWood", 'C', Blocks.STONE_BUTTON
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockApiary.apiary), "AAA", "B B", "AAA",
                'A', "slabWood", 'B'
                , "woodRubber"
        );

        Recipes.recipe.addRecipe(getBlockStack(BlockGasTurbine.gas_turbine_casing), " B ", "BAB", " B ",
                'A', Blocks.BRICK_BLOCK, 'B'
                , new ItemStack(IUItem.casing, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasTurbine.gas_turbine_airbearings), " B ", "BAB", " B ",
                'B', new ItemStack(IUItem.windrod, 1, 2), 'A'
                , getBlockStack(BlockGasTurbine.gas_turbine_casing)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasTurbine.gas_turbine_tank), "DBC", " A ", "   ",
                'A', getBlockStack(BlockGasTurbine.gas_turbine_casing), 'B'
                , new ItemStack(IUItem.crafting_elements, 1, 27), 'C', new ItemStack(IUItem.crafting_elements, 1, 154),
                'D', new ItemStack(IUItem.crafting_elements, 1, 263)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasTurbine.gas_turbine_socket), "CDC", "BAB", "   ",
                'A', getBlockStack(BlockGasTurbine.gas_turbine_casing), 'B'
                , new ItemStack(IUItem.crafting_elements, 1, 402), 'C', new ItemStack(IUItem.crafting_elements, 1, 582),
                'D', new ItemStack(IUItem.crafting_elements, 1, 574)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasTurbine.gas_turbine_blower), " A ", " B ", " C ",
                'A', new ItemStack(IUItem.crafting_elements, 1, 112), 'B'
                , getBlockStack(BlockGasTurbine.gas_turbine_casing), 'C', new ItemStack(IUItem.plate, 1, 50)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasTurbine.gas_turbine_recuperator), " B ", "BAB", " B ",
                'B', new ItemStack(IUItem.water_rod, 1, 2), 'A'
                , getBlockStack(BlockGasTurbine.gas_turbine_casing)
        );
        Recipes.recipe.addRecipe(IUItem.net, "ABA", "AAA", " A ",
                'B', "wool", 'A'
                , "stickWood"
        );
        Recipes.recipe.addRecipe(IUItem.agricultural_analyzer, "BBB", "CAC", "   ",
                'B', "plateIron", 'A'
                , TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1), 'C', IUItem.crops
        );
        Recipes.recipe.addRecipe(IUItem.bee_analyzer, "BBB", "CAC", "   ",
                'B', "plateIron", 'A'
                , TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1), 'C', IUItem.jarBees
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasTurbine.gas_turbine_controller), "EAE", "СBС", " D ",
                'A', TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8), 'B'
                , getBlockStack(BlockGasTurbine.gas_turbine_casing), 'C', new ItemStack(IUItem.crafting_elements, 1, 585)
                , 'D', new ItemStack(IUItem.crafting_elements, 1, 589), 'E', new ItemStack(IUItem.crafting_elements, 1, 397)
        );

        Recipes.recipe.addRecipe(getBlockStack(BlockGasWell.gas_well_casing), " C ", "BAB", " C ",
                'A', Blocks.BRICK_BLOCK, 'B'
                , new ItemStack(IUItem.casing, 1, 49), 'C', "plateVanadoalumite"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasWell.gas_well_transport),
                "BDB", "DAD", "BDB", 'B', new ItemStack(IUItem.item_pipes, 1, 27), 'D', new ItemStack(IUItem.item_pipes, 1, 28),
                'A',
                getBlockStack(BlockGasWell.gas_well_casing)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasWell.gas_well_drill),
                " E ", "DAD", "CBC", 'B', new ItemStack(IUItem.oilquarry), 'A', getBlockStack(BlockGasWell.gas_well_casing)
                , 'C'
                , "plateTitanium", 'D', "plateSteel", 'E', "plateIridium"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasWell.gas_well_analyzer),
                "DBD",
                "EAE",
                " C ",
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 226),
                'A',
                getBlockStack(BlockGasWell.gas_well_casing)
                ,
                'C'
                ,
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),
                'D',
                "gearOsmium",
                'E',
                "plateCarbon"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasWell.gas_well_controller),
                "DBD",
                " A ",
                " C ",
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 42),
                'A',
                getBlockStack(BlockGasWell.gas_well_casing)
                ,
                'C'
                ,
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),
                'D',
                "stickZinc"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasWell.gas_well_socket),
                "DBD",
                " A ",
                " C ",
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 592),
                'A',
                getBlockStack(BlockGasWell.gas_well_casing)
                ,
                'C', new ItemStack(IUItem.crafting_elements, 1, 614),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 402)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasWell.gas_well_tank),
                " E ", "DAD", "CBC", 'B', new ItemStack(IUItem.crafting_elements, 1, 43), 'A',
                getBlockStack(BlockGasWell.gas_well_casing)
                , 'C'
                , "plateGold", 'D', "plateTin", 'E', new ItemStack(IUItem.crafting_elements, 1, 27)
        );
        Recipes.recipe.addRecipe(IUItem.spaceupgrademodule_schedule, " A ", "ABA", " A ",
                'B', IUItem.module_schedule, 'A'
                , new ItemStack(IUItem.plate, 1, 49)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spaceupgrademodule, 1, 3),
                " B ",
                "CAC",
                " E ",
                'A',
                new ItemStack(IUItem.spaceupgrademodule_schedule, 1),
                'B',
                "plateMolybdenum",
                'C',
                "plateCadmium",
                'E',
                "plateLapis"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spaceupgrademodule, 1, 1), " B ", "CAC", " E ",
                'A', new ItemStack(IUItem.spaceupgrademodule_schedule, 1), 'B', new ItemStack(IUItem.plate, 1, 46), 'C',
                "plateRedbrass", 'E', "platePalladium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spaceupgrademodule, 1, 2),
                " B ",
                "CAC",
                " E ",
                'A',
                new ItemStack(IUItem.spaceupgrademodule_schedule, 1),
                'B',
                "plateHafniumCarbide",
                'C',
                "plateOsmiridium",
                'E',
                "plateNiobiumTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spaceupgrademodule, 1, 0),
                " B ",
                "CAC",
                " E ",
                'A',
                new ItemStack(IUItem.spaceupgrademodule_schedule, 1),
                'B',
                "plateWoods",
                'C',
                "plateBerylliumBronze",
                'E',
                "plateNitenol"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spaceupgrademodule, 1, 5), " B ", "CAC", " E ",
                'A', new ItemStack(IUItem.spaceupgrademodule_schedule, 1), 'B', "plateAluminiumLithium", 'C',
                new ItemStack(IUItem.plate, 1, 47), 'E', "plateDuralumin"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spaceupgrademodule, 1, 4),
                " B ",
                "CAC",
                " E ",
                'A',
                new ItemStack(IUItem.spaceupgrademodule_schedule, 1),
                'B',
                "plateObsidian",
                'C',
                "plateMuntsa",
                'E',
                "plateGermanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spaceupgrademodule, 1, 6),
                " B ",
                "CAC",
                " E ",
                'A',
                new ItemStack(IUItem.spaceupgrademodule_schedule, 1),
                'B',
                "plateAlcled",
                'C',
                "plateInconel",
                'E',
                new ItemStack(IUItem.plate, 1, 47)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 657), "BEB", "CAC", "EEE",
                'A', DEFAULT_SENSOR, 'C', "doubleplateThallium", 'B', "gearAlcled", 'E', "plateChromium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 658),
                "BEB",
                "CAC",
                "DHD",
                'A',
                DEFAULT_SENSOR,
                'C',
                Items.BLAZE_ROD,
                'B',
                Items.SUGAR,
                'E',
                Items.GLASS_BOTTLE,
                'D',
                Items.NETHER_WART,
                'H',
                Items.ENDER_PEARL
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 659), "BEB", "CAC", "DED",
                'A', DEFAULT_SENSOR, 'C', IUItem.polonium_palladium_composite, 'B', "gearCobaltChrome", 'E', "plateZeliber", 'D',
                new ItemStack(IUItem.plate, 1, 50)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 660), "BBB", "CAC", "   ",
                'A', DEFAULT_SENSOR, 'C', "gearPalladium", 'B', "stoneGranit"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 661), "BDB", "CAC", " H ",
                'A', DEFAULT_SENSOR, 'C', Items.GLOWSTONE_DUST, 'B', Items.REDSTONE, 'D', Items.GLASS_BOTTLE, 'H', Items.GUNPOWDER
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 662), "BBB", "CAC", " D ",
                'A', DEFAULT_SENSOR, 'C', new ItemStack(IUItem.crafting_elements, 1, 426), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 320), 'D', new ItemStack(IUItem.crafting_elements, 1, 597)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 663), "BBB", "CAC", "   ",
                'A', DEFAULT_SENSOR, 'C', "gearOsmiridium", 'B', Blocks.CACTUS
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 664), "DBD", "CAC", "   ",
                'A', DEFAULT_SENSOR, 'C', "gearVanadoalumite", 'B', Items.MILK_BUCKET, 'D', Items.BEEF
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 665), "DBD", "CAC", "   ",
                'A', DEFAULT_SENSOR, 'C', "gearZirconium", 'B', Items.EGG, 'D', Items.CHICKEN
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 675), "DDD", "CAC", "   ",
                'A', DEFAULT_SENSOR, 'C', "gearVitalium", 'D', Items.PORKCHOP
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 666), "DBD", "CAC", " E ",
                'A', DEFAULT_SENSOR, 'C', "plateadvancedAlloy", 'B', IUItem.royal_jelly, 'D', "plateCarbon", 'E',
                new ItemStack(IUItem.crafting_elements, 1, 295)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 667), "DBD", "CAC", " E ",
                'A', DEFAULT_SENSOR, 'C', new ItemStack(IUItem.crafting_elements, 1, 480), 'B', IUItem.honeycomb, 'D', "plateBor",
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 295)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 668), " B ", "CAC", " D ",
                'A', DEFAULT_SENSOR, 'C', "plateStellite", 'B', "plateHafniumCarbide", 'D', "gemSapphire"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 669), "DBD", "CAC", "DBD",
                'A', DEFAULT_SENSOR, 'C', "gearNichrome", 'B', "gearMagnesium", 'D', "gearZirconium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 670), "DBD", "CAC", "   ",
                'A', DEFAULT_SENSOR, 'C', "gearSpinel", 'B', "wool", 'D', Items.MUTTON
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 671), "EBE", "CAC", "DBD",
                'A', DEFAULT_SENSOR, 'C', new ItemStack(IUItem.iudust, 1, 74), 'B', new ItemStack(IUItem.iudust, 1, 40), 'D',
                "plateFerromanganese", 'E', "plateAluminumbronze"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 672), " B ", "CAC", " B ",
                'A', DEFAULT_SENSOR, 'C', "plateSteel", 'B', "plateInvar"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 673), "BCB", "CAC", "   ",
                'A', DEFAULT_SENSOR, 'C', "gearZinc", 'B', "plateTin"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 674), "BCB", "DAD", "   ",
                'A', DEFAULT_SENSOR, 'C', "treeSapling", 'B', "plateStrontium", 'D', "plateHafnium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 676), "BCB", "DAD", " C ",
                'A', DEFAULT_SENSOR, 'C', new ItemStack(IUItem.plate, 1, 47), 'B', new ItemStack(IUItem.plate, 1, 50), 'D',
                "plateAluminiumLithium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 677), "BCB", "DAD", " C ",
                'A', DEFAULT_SENSOR, 'C', "plateMolybdenum", 'B', "plateBismuth", 'D',
                "plateDuralumin"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 765), "ABB", "AB ", "B  ",
                'A', "plateThallium", 'B', "plateNiobium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 678), "C B", "CAB", "   ",
                'A', DEFAULT_SENSOR, 'C', "plateNichrome", 'B', "plateTungsten"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 679), "CBC", "DAD", " B ",
                'A', DEFAULT_SENSOR, 'C', "plateHafniumCarbide", 'B', "plateNeodymium", 'D', "plateLapis"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 680), "CBC", "DAD", " B ",
                'A', DEFAULT_SENSOR, 'C', "plateObsidian", 'B', "gearAluminiumSilicon", 'D', "plateOsmiridium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 681), "CBC", "DAD", "EBE",
                'A', DEFAULT_SENSOR, 'C', "plateVanadium", 'B', "plateMikhail", 'D', "plateVitalium", 'E', "plateChromium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 683),
                "CBC",
                "DAD",
                "EBE",
                'A',
                DEFAULT_SENSOR,
                'C',
                "plateBerylliumBronze",
                'B',
                "plateZeliber",
                'D',
                "plateNichrome",
                'E',
                "plateAluminumbronze"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 682), "CBC", "DAD", " B ",
                'A', DEFAULT_SENSOR, 'C', "plateThallium", 'B', new ItemStack(IUItem.plate, 1, 41), 'D', "plateStellite"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 684),
                "CBC",
                "DAD",
                "EBE",
                'A',
                DEFAULT_SENSOR,
                'C',
                "plateAluminiumSilicon",
                'B',
                "plateFerromanganese",
                'D',
                "plateTitanium",
                'E',
                "plateMolybdenum"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 685), "CBC", "DAD", "EBE",
                'A', DEFAULT_SENSOR, 'C', "gearPermalloy", 'B', "gearCobaltChrome", 'D', "gearTantalum", 'E', "casingCadmium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 686), "CBC", "DAD", "   ",
                'A', DEFAULT_SENSOR, 'C', "gearCobalt", 'B', Items.IRON_AXE, 'D', "plateBronze"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 687), "CBC", "DAD", "   ",
                'A', DEFAULT_SENSOR, 'C', "plateCarbon", 'B', "plateadvancedAlloy", 'D', "plateBor"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 688), "CBC", "DAD", "EBE",
                'A', DEFAULT_SENSOR, 'C', "plateTantalumTungstenHafnium", 'B', "plateOsmiridium", 'D', "gemRuby", 'E', "gemTopaz"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 689), "CBC", "DAD", "   ",
                'A', DEFAULT_SENSOR, 'C', "gearBarium", 'B', IUItem.fertilizer, 'D', "plateSteel"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.electric_wire_insulator), " A ", "BC ", " D ",
                'A', getBlockStack(BlockBaseMachine3.steam_wire_insulator), 'C', IUItem.machine, 'B',
                new ItemStack(IUItem.crafting_elements, 1, 47), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 276)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.crystal_charge), " A ", "BC ", " D ",
                'A', getBlockStack(BlockBaseMachine3.steam_crystal_charge), 'C', IUItem.machine, 'B',
                new ItemStack(IUItem.crafting_elements, 1, 44), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 276)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.ampere_generator), " A ", " C ", " D ",
                'A', getBlockStack(BlockBaseMachine3.steam_ampere_generator), 'C', IUItem.machine, 'B',
                new ItemStack(IUItem.crafting_elements, 1, 44), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 276)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.electric_refractory_furnace), " A ", "BCE", " D ",
                'A', getBlockStack(BlockBaseMachine3.steam_ampere_generator), 'C', IUItem.machine, 'B',
                getBlockStack(BlockMiniSmeltery.mini_smeltery), 'D',
                IUItem.motors_with_improved_bearings_, 'E', getBlockStack(BlockRefractoryFurnace.refractory_furnace)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 6), "BBB", "CAC", "BBB",

                ('A'), "plateIron",

                ('B'), "plankWood",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 7), "BBB", "CAC", "BBB",

                ('A'), "plateIron",

                ('B'), "plankWood",

                ('C'), new ItemStack(Items.DYE, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 8), "BBB", " A ", "BBB",

                ('A'), "plateIron",

                ('B'), "plankWood"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 9), "BBB", "CAC", "BBB",

                ('A'), "plateGold",

                ('B'), "plateIron",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 10), "BBB", "CAC", "BBB",

                ('A'), "plateGold",

                ('B'), "plateIron",

                ('C'), new ItemStack(Items.DYE, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 11), "BBB", " A ", "BBB",

                ('A'), "plateGold",

                ('B'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 12), "BBB", "CAC", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateSteel",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 13), "BBB", "CAC", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateSteel",

                ('C'), new ItemStack(Items.DYE, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 14), "BBB", " A ", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateSteel"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 15), "BBB", "CAC", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateAluminumbronze",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 16), "BBB", "CAC", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateAluminumbronze",

                ('C'), new ItemStack(Items.DYE, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 17), "BBB", " A ", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateAluminumbronze"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 27), "BBB", "CAC", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateZeliber",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 29), "BBB", "CAC", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateZeliber",

                ('C'), new ItemStack(Items.DYE, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 28), "BBB", " A ", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateZeliber"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 18), "BBB", "CAC", "BBB",

                ('A'), "plateCobalt",

                ('B'), "plankWood",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 20), "BBB", " A ", "BBB",

                ('A'), "plateCobalt",

                ('B'), "plankWood"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 19), "BBB", "CAC", "BBB",

                ('A'), "plateCobalt",

                ('B'), "plankWood",

                ('C'), new ItemStack(Items.DYE, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 21), "BBB", "CAC", "BBB",

                ('A'), "plateNichrome",

                ('B'), "plateCobalt",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 22), "BBB", " A ", "BBB",

                ('A'), "plateNichrome",

                ('B'), "plateCobalt"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 23), "BBB", "CAC", "BBB",

                ('A'), "plateNichrome",

                ('B'), "plateCobalt",

                ('C'), new ItemStack(Items.DYE, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 24), "BBB", "CAC", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateCobaltChrome",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 25), "BBB", " A ", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateCobaltChrome"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 6, 26), "BBB", "CAC", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateCobaltChrome",

                ('C'), new ItemStack(Items.DYE, 1, 4)
        );

        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.chicken_farm), " C ", " A ", " B ",

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 276),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 665)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.sheep_farm), " C ", " A ", " B ",

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 276),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 670)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.cow_farm), " C ", " A ", " B ",

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 276),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 664)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.pig_farm), " C ", " A ", " B ",

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 276),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 675)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.sapling_gardener), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 276),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 672),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 674)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.tree_breaker), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 276),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 686),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 674)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.cactus_farm), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 276),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 663),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 672)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.electric_brewing), " D ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 276),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 661),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 658),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 47)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.sawmill), "   ", "DAE", " B ",

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 276),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 669),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 673),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.gen_addition_stone), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 276),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 660),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.plant_fertilizer), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 276),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 689),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 61)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.plant_collector), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 276),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 64),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 243)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.plant_gardener), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 276),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 64),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 672)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.field_cleaner), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 276),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 671),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 672)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.weeder), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 276),

                ('C'), new ItemStack(IUItem.module7, 1, 9),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 64)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.field_cleaner), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 276),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 671),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 672)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.apothecary_bee), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 276),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 666),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 656)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.collector_product_bee), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 276),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 666),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 667)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.incubator), "H D", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 662),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 659),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 27),
                ('H'), new ItemStack(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.insulator), "H D", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 662),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 676),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 27),
                ('H'), new ItemStack(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.rna_collector), "H D", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 685),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 659),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 27),
                ('H'), new ItemStack(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.mutatron), " D ", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 682),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 676),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 27)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.reverse_transcriptor), " D ", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 668),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 685),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 27)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.genetic_stabilizer), "H D", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 685),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 682),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 27),
                ('H'), new ItemStack(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.centrifuge), "   ", "CAH", " B ",

                ('A'), IUItem.machine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 669),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 682),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 27),
                ('H'), new ItemStack(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.genetic_replicator), "H D", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 659),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 682),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 27),
                ('H'), new ItemStack(IUItem.crafting_elements, 1, 676)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.inoculator), " D ", "CAH", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 659),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 47),
                ('H'), new ItemStack(IUItem.crafting_elements, 1, 676)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.genome_extractor), "H D", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 659),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 67),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 47),
                ('H'), new ItemStack(IUItem.crafting_elements, 1, 676)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.genetic_polymerizer), "HFD", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 659),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 27),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('H'), new ItemStack(IUItem.crafting_elements, 1, 676),
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.genetic_transposer), "HFD", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 659),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 27),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('H'), new ItemStack(IUItem.crafting_elements, 1, 676),
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 682)
        );


        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.shield), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 54),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 439)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.hologram_space), " C ", " A ", " B ",

                ('A'), IUItem.machine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 657)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.upgrade_rover), " H ", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 66),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 679),

                ('H'), new ItemStack(IUItem.crafting_elements, 1, 47)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.research_table_space), " H ", "CAE", " B ",

                ('A'), "woodRubber",

                ('B'), "gearInvar",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 683),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 678),

                ('H'), new ItemStack(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.probe_assembler), " H ", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 677),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 243),

                ('H'), new ItemStack(IUItem.crafting_elements, 1, 52)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.rocket_assembler), " H ", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 684),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 243),

                ('H'), new ItemStack(IUItem.crafting_elements, 1, 52)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.satellite_assembler), " H ", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 688),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 243),

                ('H'), new ItemStack(IUItem.crafting_elements, 1, 52)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.rover_assembler), " H ", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 681),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 243),

                ('H'), new ItemStack(IUItem.crafting_elements, 1, 52)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.rocket_launch_pad), "   ", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 684),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 680)


        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.research_lens, 1, 0), " B ", "BAB", " B ",

                ('A'), Blocks.GLASS,

                ('B'), "plateCobalt"


        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.research_lens, 1, 1), " B ", "BAB", " B ",

                ('A'), new ItemStack(IUItem.research_lens, 1, 0),

                ('B'), new ItemStack(IUItem.plate, 1, 46)


        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.research_lens, 1, 2), " B ", "BAB", " B ",

                ('A'), new ItemStack(IUItem.research_lens, 1, 1),

                ('B'), new ItemStack(IUItem.plate, 1, 50)


        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.research_lens, 1, 3), " B ", "BAB", " B ",

                ('A'), new ItemStack(IUItem.research_lens, 1, 2),

                ('B'), new ItemStack(IUItem.plate, 1, 47)


        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.research_lens, 1, 4), " B ", "BAB", " B ",

                ('A'), new ItemStack(IUItem.research_lens, 1, 3),

                ('B'), new ItemStack(IUItem.plate, 1, 48)


        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.research_lens, 1, 5), " B ", "BAB", " B ",

                ('A'), new ItemStack(IUItem.research_lens, 1, 4),

                ('B'), "plateStellite"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.research_lens, 1, 6), " B ", "BAB", " B ",

                ('A'), new ItemStack(IUItem.research_lens, 1, 5),

                ('B'), "plateNimonic"

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 0), "BBB", "CAD", "BBB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateSteel",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearGadolinium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 1), "BBB", "CAD", "BBB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateMolybdenumSteel",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearGadolinium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 2), "BEB", "CAD", "BEB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateWoods",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearGadolinium",
                'E', "platePermalloy"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 3), "BEB", "CAD", "BEB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateNimonic",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearGadolinium",
                'E', "plateSuperalloyHaynes"

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 4), "BBB", "CAD", "BBB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateSteel",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearCadmium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 5), "BBB", "CAD", "BBB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateMolybdenumSteel",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearCadmium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 6), "BEB", "CAD", "BEB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateWoods",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearCadmium",
                'E', "platePermalloy"

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 7), "BBB", "CAD", "BBB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateSteel",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearAluminiumLithium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 8), "BBB", "CAD", "BBB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateMolybdenumSteel",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearAluminiumLithium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 9), "BEB", "CAD", "BEB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateWoods",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearAluminiumLithium",
                'E', "platePermalloy"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 10), "BBB", "CAD", "BBB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateSteel",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearDuralumin"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 11), "BBB", "CAD", "BBB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateMolybdenumSteel",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearDuralumin"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 12), "BEB", "CAD", "BEB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateWoods",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearDuralumin",
                'E', "platePermalloy"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 13), "BBB", "CAD", "BBB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateSteel",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearSpinel"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 14), "BBB", "CAD", "BBB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateMolybdenumSteel",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearSpinel"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 15), "BEB", "CAD", "BEB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateWoods",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearSpinel",
                'E', "platePermalloy"

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 16), "BBB", "CAD", "BBB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateSteel",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', new ItemStack(IUItem.photonglass, 1, 0)

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 17), "BBB", "CAD", "BBB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateMolybdenumSteel",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', new ItemStack(IUItem.photonglass, 1, 2)

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 18), "BEB", "CAD", "BEB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateWoods",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', new ItemStack(IUItem.photonglass, 1, 4),
                'E', "platePermalloy"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 19), "BBB", "CAD", "BBB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateSteel",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearMikhail"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 24), "BBB", "CAD", "BBB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateMolybdenumSteel",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearMikhail"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 25), "BEB", "CAD", "BEB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateWoods",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearMikhail",
                'E', "platePermalloy"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 20), "BBB", "CAD", "BBB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateSteel",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearPolonium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 21), "BBB", "CAD", "BBB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateSteel",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearBerylliumBronze"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 22), "BBB", "CAD", "BBB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateMolybdenumSteel",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearBerylliumBronze"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 23), "BEB", "CAD", "BEB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateWoods",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearBerylliumBronze",
                'E', "platePermalloy"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.colonial_building, 1, 23), "BEB", "CAD", "BEB",

                ('A'), new ItemStack(IUItem.blockResource, 1, 13),

                ('B'), "plateWoods",
                'C', new ItemStack(IUItem.crafting_elements, 1, 678),
                'D', "gearBerylliumBronze",
                'E', "platePermalloy"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blockResource, 1, 14), "BBB", "BAB", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 295),

                ('B'), "plateDuralumin"

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_macerator), " E ", "BAC", " D ",

                ('A'), new ItemStack(IUItem.blockResource, 1, 14),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 44),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 64),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 69)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_gearing), " E ", "BAC", " D ",

                ('A'), new ItemStack(IUItem.blockResource, 1, 14),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 44),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 64),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 218)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_extractor), " E ", "BAC", " D ",

                ('A'), new ItemStack(IUItem.blockResource, 1, 14),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 44),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 64),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 159)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_compressor), " E ", "BAC", " D ",

                ('A'), new ItemStack(IUItem.blockResource, 1, 14),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 44),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 64),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 63)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_extruder), " E ", "BAC", " D ",

                ('A'), new ItemStack(IUItem.blockResource, 1, 14),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 44),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 64),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 163)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_cutting), " E ", "BAC", " D ",

                ('A'), new ItemStack(IUItem.blockResource, 1, 14),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 44),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 64),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 132)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_rolling), " E ", "BAC", " D ",

                ('A'), new ItemStack(IUItem.blockResource, 1, 14),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 44),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 64),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 165)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_furnace), " E ", "BAC", " D ",

                ('A'), new ItemStack(IUItem.blockResource, 1, 14),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 44),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 64),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 219)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_orewashing), " E ", "BAC", " D ",

                ('A'), new ItemStack(IUItem.blockResource, 1, 14),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 44),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 64),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 34)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_centrifuge), "E H", "BAC", " D ",

                ('A'), new ItemStack(IUItem.blockResource, 1, 14),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 44),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 64),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 39),
                ('H'), new ItemStack(IUItem.crafting_elements, 1, 70)

        );

        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.single_multi_crop), " E ", "BAC", " D ",

                ('A'), getBlockStack(BlockMoreMachine3.farmer),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 47),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'), IUItem.motors_with_improved_bearings_,
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 689)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.dual_multi_crop), "B B", "FAC", "BDB",

                ('A'), getBlockStack(BlockBaseMachine3.single_multi_crop),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 138),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 6),

                ('D'), IUItem.adv_motors_with_improved_bearings_,
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 51)


        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.triple_multi_crop), "BEB", "FAC", "BDB",

                ('A'), getBlockStack(BlockBaseMachine3.dual_multi_crop),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 139),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 83),

                ('D'), IUItem.imp_motors_with_improved_bearings_,
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'), new ItemStack(IUItem.crafting_elements, 1, 47)


        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.quad_multi_crop), "B B", "FAC", "BDB",

                ('A'), getBlockStack(BlockBaseMachine3.triple_multi_crop),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 140),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 107),

                ('D'), IUItem.imp_motors_with_improved_bearings_,
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 52)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_casing_glass), 5), "ABA", "BAB",
                "ABA",

                ('A'), IUItem.glass,

                ('B'), "casingTitanium"


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_casing), 2), "AB ", "BA ",
                "   ",

                ('A'), "plateTungsten",

                ('B'), "plateDuralumin"


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_rod), 1), "   ", "AAA",
                "   ",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_casing)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_socket), 1), "CDC", "BAB",
                "EFE",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_casing),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 402),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 585),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 583),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 592),

                ('F'), new ItemStack(IUItem.crafting_elements, 1, 561)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_controller_rod), 1), "CDC", "BAB",
                "EFE",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_casing),

                ('B'), getBlockStack(BlockSteamTurbine.steam_turbine_rod),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 581),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 574),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 479),

                ('F'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)


        );

        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_controller), 1), "CDC", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_casing),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 414),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 356),

                ('D'), "plateNeodymium",

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),

                ('F'), new ItemStack(IUItem.crafting_elements, 1, 581)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_adv_controller), 1), "CDC", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_controller),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 426),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 424),

                ('D'), "plateMolybdenumSteel",

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('F'), new ItemStack(IUItem.crafting_elements, 1, 579)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_imp_controller), 1), "CDC", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_adv_controller),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 373),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 371),

                ('D'), "plateDraconid",

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('F'), new ItemStack(IUItem.crafting_elements, 1, 563)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_per_controller), 1), "CDC", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_imp_controller),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 402),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 397),

                ('D'), "plateWoods",

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('F'), new ItemStack(IUItem.crafting_elements, 1, 585)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_tank), 1), "C D", "BAB",
                " ",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_casing),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 367),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 43),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 27)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_adv_tank), 1), "C D", "BAB",
                " ",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_tank),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 415),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 45),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 6)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_imp_tank), 1), "C D", "BAB",
                " ",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_adv_tank),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 380),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 48),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 83)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_per_tank), 1), "C D", "BAB",
                " ",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_imp_tank),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 396),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 50),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 107)
        );

        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_coolant), 1), "DCD", "BAB",
                " E ",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_casing),

                ('B'), "plateNeodymium",

                ('C'), new ItemStack(IUItem.coolingsensor),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 356),

                ('E'), getBlockStack(BlockBaseMachine3.fluid_cooling)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_adv_coolant), 1), "DCD", "BAB",
                " E ",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_coolant),

                ('B'), "plateMolybdenumSteel",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 581),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 424),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 582)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_imp_coolant), 1), "DCD", "BAB",
                " E ",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_adv_coolant),

                ('B'), "plateDraconid",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 579),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 371),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 599)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_per_coolant), 1), "DCD", "BAB",
                " E ",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_imp_coolant),

                ('B'), "plateWoods",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 563),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 397),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 597)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_pressure), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_casing),

                ('B'), "plateNeodymium",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 63),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 414),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 387),

                ('F'), new ItemStack(IUItem.crafting_elements, 1, 581)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_adv_pressure), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_pressure),

                ('B'), "plateMolybdenumSteel",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 1),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 426),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 425),

                ('F'), new ItemStack(IUItem.crafting_elements, 1, 579)


        );

        BaseRecipeFour.init();
    }


}
