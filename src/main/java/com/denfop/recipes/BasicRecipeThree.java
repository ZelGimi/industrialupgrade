package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockAnvil;
import com.denfop.blocks.BlockStrongAnvil;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.mechanism.*;
import com.denfop.tiles.mechanism.TileGenerationMicrochip;
import com.denfop.utils.ModUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class BasicRecipeThree {

    public static Item DEFAULT_SENSOR;

    public static Item ADV_SENSOR;

    public static Item IMP_SENSOR;

    public static Item PER_SENSOR ;
    public static Item PHOTON_SENSOR;

    public static ItemStack getBlockStack(IMultiTileBlock block) {
        return TileBlockCreator.instance.get(block.getIDBlock()).getItemStack();
    }

    public static void recipe() {
        DEFAULT_SENSOR = IUItem.crafting_elements.getItemFromMeta(21);

        ADV_SENSOR = IUItem.crafting_elements.getItemFromMeta(25);

        IMP_SENSOR = IUItem.crafting_elements.getItemFromMeta(23);

        PER_SENSOR = IUItem.crafting_elements.getItemFromMeta(24);

        PHOTON_SENSOR = IUItem.crafting_elements.getItemFromMeta(620);
        Recipes.recipe.addRecipe(getBlockStack(BlockSmeltery.smeltery_controller), " C ", "DBD", "   ",

                ('A'), ItemStackHelper.fromData(Items.COMPARATOR), ('B'), getBlockStack(BlockSmeltery.smeltery_casing), 'C',
                Blocks.REDSTONE_BLOCK, 'D', ItemStackHelper.fromData(Items.REPEATER)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockSmeltery.smeltery_casting), " C ", "CBC", " C ",

                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 185), ('B'), getBlockStack(BlockSmeltery.smeltery_furnace), 'C',
                "forge:plates/Thallium", 'D', "forge:plates/Strontium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 568), " A ", "CCC", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.plate, 1, 31), ('B'), "forge:ingots/Invar", 'C',
                "forge:rods/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 567), " A ", "CCC", " B ",

                ('A'), "forge:ingots/Germanium", ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 504), 'C',
                "forge:rods/Electrum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 565), "BAB", "CCC", "BAB",

                ('A'), ItemStackHelper.fromData(IUItem.alloysingot, 1, 31), ('B'), "forge:rods/Silver", 'C',
                "forge:rods/Zinc"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 581), " B ", "AAA", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.cable, 1, 12), ('B'), "forge:plates/Copper", 'C',
                "forge:rods/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 579), " B ", "AAA", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.cable, 1, 12), ('B'), "forge:plates/Lapis", 'C',
                "forge:rods/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 563), " B ", "AAA", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.cable, 1, 12), ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 480), 'C',
                "forge:rods/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 585), " B ", "AAA", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.cable, 1, 12), ('B'), ItemStackHelper.fromData(IUItem.alloysingot, 1, 25), 'C',
                "forge:rods/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 582), "BBB", "A A", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356), ('B'), ItemStackHelper.fromData(IUItem.cable, 1, 11), 'C',
                "forge:rods/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 599), "CBC", "BAB", "CBC",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 582), ('B'), ItemStackHelper.fromData(IUItem.alloysplate, 1, 9), ('C'),
                ItemStackHelper.fromData(IUItem.plate, 1, 34)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 583), "CBC", "BAB", "CBC",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 599), ('B'), ItemStackHelper.fromData(IUItem.plate, 1, 36), ('C'),
                ItemStackHelper.fromData(IUItem.plate, 1, 33)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 597), "BBB", "BAB", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 583), ('B'), ItemStackHelper.fromData(IUItem.alloysplate, 1, 21), ('C'),
                ItemStackHelper.fromData(IUItem.plate, 1, 33)
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 20),
                "CBC",
                "BAB",
                " D ",
                'A',
                ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 0),
                'B',
                IUItem.fluidEjectorUpgrade,
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 21),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 20), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 420)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 22),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 21), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 378)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 23),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 22), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 405)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );


        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 16),
                "CDC",
                "BAB",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 0),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 326),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 17),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 16), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 420)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 18),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 17), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 378)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 19),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 18), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 405)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 24),
                "CDC",
                "BAB",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 0),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 321),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 25),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 24), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 420)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 26),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 25), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 378)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 27),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 26), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 405)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 28),
                "CDC",
                "BAB",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 0),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 327),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 29),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 28), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 420)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 30),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 29), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 378)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 31),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 30), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 405)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 36),
                "CDC",
                "BAB",
                "   ",
                'A',
                ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 0),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 336),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 37),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 36), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 420)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 38),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 37), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 378)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 39),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 38), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 405)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 32),
                "DBD", " A ", " C ", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 0), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 367), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 387)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 33),
                "BDB", "EAE", "BCB", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 32), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 420), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 45), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 415), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 425)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 34),
                "BDB", "EAE", "BCB", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 33), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 378), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 48), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 380), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 372)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 35),
                "BDB", "EAE", "BCB", 'A', ItemStackHelper.fromData(IUItem.water_reactors_component, 1, 34), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 405), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 50), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 396), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 398)
        );
        // heat reactor
        // 363
        // 417
        // 375
        // 392
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 24),
                "AA ", "AA ", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 363)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 25),
                "AA ", "AA ", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 417)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 26),
                "AA ", "AA ", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 375)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 27),
                "AA ", "AA ", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 392)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 0),
                "CDC", "EAE", " B ", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 24),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356),
                'B', IUItem.elemotor, 'E', "forge:doubleplate/Tungsten"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 1),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 0), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 420)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 2),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 1), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 375)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 3),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 2), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 392)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 4),
                "EGF", "CAC", "DBD", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 24),
                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 324),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D', "forge:plates/Beryllium", 'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 321), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 342), 'G', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 365)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 5),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 4), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 420)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 6),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 5), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 375)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 7),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 6), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 392)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 8),
                "   ", "CAC", "DBD", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 24), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 322), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 9),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 8), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 420)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 10),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 9), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 375)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 11),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 10), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 392)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 12),
                "DCD", "BAB", "   ", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 24), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 333), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 13),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 12), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 420)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 14),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 13), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 375)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 15),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 14), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 392)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 16),
                "CBC", "DAD", "FEF", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 24), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 385), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414), 'E', getBlockStack(BlockBaseMachine3.cooling), 'F',
                "forge:plates/Beryllium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 17),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 16), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 420)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 18),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 17), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 375)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 19),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 18), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 392)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 20),
                "EFE", "CAC", "DGD", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 24), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 436), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356), 'E', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 320), 'F',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 326), 'G', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 357)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 21),
                "BDB", "CAC", "BEB", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 20), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 417)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 410)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 22),
                "BDB", "CAC", "BEB", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 21), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 375)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 310)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 23),
                "BDB", "CAC", "BEB", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 22), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 392)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 368)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 28),
                "  ", "BAB", "CDC", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 24),
                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 327),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D', "forge:doubleplate/Titanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 29),
                "BDB", "CAC", "BEB", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 28), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 417)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 410)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 30),
                "BDB", "CAC", "BEB", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 29), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 375)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 310)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 31),
                "BDB", "CAC", "BEB", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 30), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 392)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 368)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 32),
                "DBD", " A ", " C ", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 24), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 367), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 387)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 33),
                "BDB", "EAE", "BCB", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 32), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 417), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 45), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 415), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 425)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 34),
                "BDB", "EAE", "BCB", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 33), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 375), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 48), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 380), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 372)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 35),
                "BDB", "EAE", "BCB", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 34), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 392), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 50), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 396), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 398)
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 36),
                "DCD", "EAE", "FBF", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 24), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356),  'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 446), 'F', "forge:plates/Carbon"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 37),
                "BDB", "CAC", "BEB", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 36), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 417)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 410)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 38),
                "BDB", "CAC", "BEB", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 37), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 375)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 310)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.heat_reactor, 1, 39),
                "BDB", "CAC", "BEB", 'A', ItemStackHelper.fromData(IUItem.heat_reactor, 1, 38), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 392)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 368)
        );

        // gas reactor
        // 364
        // 418
        // 376
        // 393
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 12),
                "AA ", "AA ", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 364)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 13),
                "AA ", "AA ", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 418)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 14),
                "AA ", "AA ", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 376)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 15),
                "AA ", "AA ", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 393)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 4),
                "E F", "CAC", "DBD", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 12),
                'B', getBlockStack(BlockBaseMachine3.cooling),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D', "forge:plates/Bor", 'E', IUItem.coolingsensor, 'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 385)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 5),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 4), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 418)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 6),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 5), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 376)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 7),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 6), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 393)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 8),
                "EFE", "CAC", "DBD", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 12),
                'B', IUItem.fan,
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D', "forge:plates/Bor", 'F', IUItem.elemotor, 'E', "forge:plates/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 9),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 8), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 418)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 10),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 9), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 376)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 11),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 10), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 393)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 16),
                "EFE", "CAC", "DBD", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 12),
                'B', getBlockStack(BlockSimpleMachine.compressor_iu),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D', "forge:plates/Bor", 'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 322), 'E', "forge:plates/Cadmium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 17),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 16), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 418)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 18),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 17), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 376)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 19),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 18), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 393)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 20),
                "EFE", "CAC", "DBD", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 12),
                'B', getBlockStack(BlockBaseMachine3.refrigerator_coolant),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 320), 'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 326), 'E',
                "forge:plates/Carbon"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 21),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 20), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 418)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 22),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 21), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 376)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 23),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 22), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 393)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.gas_reactor, 1, 24),
                "EGF",
                "CAC",
                "DBD",
                'A',
                ItemStackHelper.fromData(IUItem.gas_reactor, 1, 12),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 324),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 320),
                'F',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 321),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 342),
                'G',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 25),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 24), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 418)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 26),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 25), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 376)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 27),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 26), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 393)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 28),
                "EFE", "CAC", "DBD", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 12),
                'B', IUItem.pump,
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D', "forge:plates/Bor", 'F', IUItem.elemotor, 'E', "forge:plates/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 29),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 28), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 418)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 30),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 29), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 376)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 31),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 30), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 393)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 32),
                "  ", "BAB", "CDC", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 12),
                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 327),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D', "forge:doubleplate/Chromium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 33),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 32), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 418)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 34),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 33), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 376)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 35),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 34), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 393)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 36),
                "CDC", "BAB", "   ", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 12), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 335)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 37),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 36), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 418)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 38),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 37), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 376)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 39),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 38), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 393)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 0),
                "DBD", " A ", " C ", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 12), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 367), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 387)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 1),
                "BDB", "EAE", "BCB", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 0), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 418), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 45), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 415), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 425)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 2),
                "BDB", "EAE", "BCB", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 1), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 376), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 48), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 380), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 372)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.gas_reactor, 1, 3),
                "BDB", "EAE", "BCB", 'A', ItemStackHelper.fromData(IUItem.gas_reactor, 1, 2), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 393), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 50), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 396), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 398)
        );


        // graphite reactor
        // 365
        // 419
        // 377
        // 394
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 24),
                "AA ", "AA ", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 365)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 25),
                "AA ", "AA ", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 419)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 26),
                "AA ", "AA ", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 377)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 27),
                "AA ", "AA ", "   ", 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 394)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 0),
                "CBC", "FAF", "DBE", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 24), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 436), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 342), 'E', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 322), 'F',
                "forge:plates/Lithium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 1),
                "BDB", "CAC", "BEB", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 0), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 419)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 428)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 2),
                "BDB", "CAC", "BEB", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 1), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 377)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 379)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 3),
                "BDB", "CAC", "BEB", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 2), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 394)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 404)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 4),
                "EFE", "CAC", "DGD", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 24), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 436), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356), 'E', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 320), 'F',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 326), 'G', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 357)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 5),
                "BDB", "CAC", "BEB", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 4), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 419)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 410)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 6),
                "BDB", "CAC", "BEB", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 5), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 377)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 310)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 7),
                "BDB", "CAC", "BEB", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 6), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 394)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 368)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 8),
                "EFE", "CAC", "DBD", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 24), 'B',
                ItemStackHelper.fromData(IUItem.simple_exchanger_item, 1), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356), 'E', "forge:plates/Carbon", 'F', "forge:doubleplate/Nichrome"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 9),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 8), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 419)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 10),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 9), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 377)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 11),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 10), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 394)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 12),
                "   ", "CAC", "DBD", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 24), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 322), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 13),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 12), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 419)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 14),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 13), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 377)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 15),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 14), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 394)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 16),
                "DBD", " A ", " C ", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 24), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 367), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 387)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 17),
                "BDB", "EAE", "BCB", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 16), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 419), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 45), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 415), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 425)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 18),
                "BDB", "EAE", "BCB", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 17), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 377), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 48), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 380), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 372)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 19),
                "BDB", "EAE", "BCB", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 18), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 394), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 50), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 396), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 398)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 20),
                "DCD", "BAB", "   ", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 24), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 334), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 21),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 20), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 419)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 22),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 21), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 377)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 23),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 22), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 394)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 28),
                "DBD",
                "CAC",
                "EFE",
                'A',
                ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 24),
                'B',
                ItemStackHelper.fromData(IUItem.capacitor),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),
                'E',
                "forge:plates/Steel",
                'F',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 29),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 28), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 419)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 30),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 29), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 377)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 31),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 30), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 394)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 32),
                "CDC", "BAB", "   ", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 24), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 327), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 33),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 32), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 419)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 34),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 33), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 377)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 35),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 34), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 394)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 36),
                "DBD", "CAC", "EFG", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 24), 'B',
                ItemStackHelper.fromData(IUItem.coolingsensor, 1), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 324), 'F',
                getBlockStack(BlockBaseMachine3.cooling), 'G',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 321)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 37),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 36), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 419)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 38),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 37), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 377)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 39),
                "BDB", "CAC", "B B", 'A', ItemStackHelper.fromData(IUItem.graphite_reactor, 1, 38), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 394)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.planner, 1),
                "AAA", "CBC", " D ", 'A', "forge:plates/Alcled", 'B',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', "forge:plates/Gold"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.deplanner, 1),
                "AAA", "CBC", " D ", 'A', "forge:plates/YttriumAluminiumGarnet", 'B',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373), 'D', "forge:plates/Palladium"
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 106),
                "DFE", "CAC", " B ", 'A', IUItem.advancedMachine, 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 60)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 243), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 130), 'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 107),
                "FEF", "CAD", "GBG", 'A', ItemStackHelper.fromData(IUItem.basemachine2, 1, 3), 'B',
                ItemStackHelper.fromData(IUItem.basemachine2, 1, 2)
                , 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 60), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 155), 'F', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402), 'G',
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 4), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.advQuantumtool), ('B'), ItemStackHelper.fromData(
                        IUItem.core,
                        1,
                        4
                ),
                ('C'), ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 3), ('D'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 5), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.adv_spectral_box), ('B'), ItemStackHelper.fromData(
                        IUItem.core,
                        1,
                        6
                ),
                ('C'), ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 4), ('D'),
                IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 6),
                "ADA",
                "CBC",
                "ADA",

                ('A'),
                ItemStackHelper.fromData(IUItem.advnanobox),
                ('B'),
                ItemStackHelper.fromData(IUItem.module_schedule),
                ('C'),
                IUItem.iridiumPlate,
                (
                        'D'),
                "forge:casings/Duralumin"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 7), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.advQuantumtool), ('B'), ItemStackHelper.fromData(
                        IUItem.core,
                        1,
                        4
                ),
                ('C'), ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 6), ('D'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 8), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.adv_spectral_box), ('B'), ItemStackHelper.fromData(
                        IUItem.core,
                        1,
                        6
                ),
                ('C'), ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 7), ('D'),
                IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 9),
                "ADA",
                "CBC",
                "ADA",

                ('A'),
                ItemStackHelper.fromData(IUItem.adv_spectral_box),
                ('B'),
                ItemStackHelper.fromData(IUItem.module_schedule),
                ('C'),
                IUItem.compressIridiumplate,
                (
                        'D'),
                "forge:doubleplate/Alumel"
        );
        Recipes.recipe.addRecipe(
                ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 10),
                "ADA",
                "CBC",
                "DED",

                ('A'),
                ItemStackHelper.fromData(IUItem.advnanobox),
                ('B'),
                ItemStackHelper.fromData(IUItem.module_schedule),
                ('C'),
                IUItem.iridiumPlate,
                (
                        'D'),
                "forge:doubleplate/Platinum",
                ('E'),
                ItemStackHelper.fromData(IUItem.core, 1, 3)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 11), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.advQuantumtool), ('B'), ItemStackHelper.fromData(
                        IUItem.core,
                        1,
                        4
                ),
                ('C'), ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 10), ('D'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 12), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.adv_spectral_box), ('B'), ItemStackHelper.fromData(
                        IUItem.core,
                        1,
                        6
                ),
                ('C'), ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 11), ('D'),
                IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 13), "ADA", "CBC", "DED",

                ('A'), ItemStackHelper.fromData(IUItem.advnanobox),

                ('B'), ItemStackHelper.fromData(IUItem.module_schedule),

                ('C'), IUItem.iridiumPlate,

                ('D'),
                "forge:doubleplate/Redbrass",

                ('E'), ItemStackHelper.fromData(IUItem.core, 1, 4)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 14), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.advQuantumtool), ('B'), ItemStackHelper.fromData(
                        IUItem.core,
                        1,
                        5
                ),
                ('C'), ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 13), ('D'),
                IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 15), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.adv_spectral_box), ('B'), ItemStackHelper.fromData(
                        IUItem.core,
                        1,
                        6
                ),
                ('C'), ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 14), ('D'),
                IUItem.doublecompressIridiumplate
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 0), "BBB", "CAC", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 122),

                ('B'), "forge:plates/StainlessSteel",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 124), "B B", "A A", "CCC",

                ('C'), "planks",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 164),

                ('B'), "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 185), "ABA", "BBB", "AAA",

                ('A'), "planks",

                ('B'), ItemStackHelper.fromData(IUItem.fluidCell)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 186), "CCC", "ABA", "CCC",

                ('A'), "forge:plates/Steel", ('C'), "forge:plates/Iron",

                ('B'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 185)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 496), "AAA", "ABA", "AAA",

                ('A'), "forge:plates/Titanium", ('B'), "forge:ingots/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 497), "AAA", "ABA", "AAA",

                ('A'), "forge:plates/Invar", ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 496)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockSmeltery.smeltery_casing), " C ", "ABA", " C ",

                ('A'), "forge:casings/Spinel", ('B'), ItemStackHelper.fromData(Blocks.BRICKS), 'C', "forge:casings/Yttrium"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockSmeltery.smeltery_tank), " C ", "DAD", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 185), ('B'), getBlockStack(BlockSmeltery.smeltery_casing), 'C',
                "forge:plates/Polonium", 'D', "forge:plates/Barium"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockSmeltery.smeltery_fuel_tank), " C ", "ABA", " D ",

                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 185), ('B'), getBlockStack(BlockSmeltery.smeltery_casing), 'C',
                Items.LAVA_BUCKET, 'D', "forge:plates/Strontium"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockSmeltery.smeltery_furnace), " C ", " B ", "   ",

                ('A'), ItemStackHelper.fromData(IUItem.basemachine2, 1, 185), ('B'), getBlockStack(BlockSmeltery.smeltery_casing), 'C',
                Blocks.FURNACE, 'D', "forge:plates/Strontium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 601), "B B", "BAB", "BBB",

                'B', "forge:plates/Electrum", 'A', getBlockStack(BlockBaseMachine3.oak_tank)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 578), "B B", "BAB", "BAB",

                'B', "forge:plates/Iron", 'A', "forge:plates/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.laser), " A ", " A ", " B ",

                'B', "forge:plates/Gold", 'A', "forge:dusts/Redstone"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.solderingIron), " A ", " C ", " B ",

                'B', "forge:plates/Gold", 'A', "forge:gems/Quartz", 'C', "forge:ingots/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 183), " B ", "CAD", " E ",

                'A', ItemStackHelper.fromData(IUItem.blockResource, 1, 9),
                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 73),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 66),
                'E', ItemStackHelper.fromData(IUItem.motors_with_improved_bearings_)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 108), "CBC", " A ", "CEC",

                'A', ItemStackHelper.fromData(IUItem.basemachine2, 1, 7),
                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 124),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 137),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 66),
                'E', ItemStackHelper.fromData(IUItem.motors_with_improved_bearings_)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 109), "CBC", " A ", "C C",

                'A', ItemStackHelper.fromData(IUItem.basemachine2, 1, 108),
                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 135),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 66),
                'E', ItemStackHelper.fromData(IUItem.motors_with_improved_bearings_)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 110), "CBC", " A ", "C C",

                'A', ItemStackHelper.fromData(IUItem.basemachine2, 1, 109),
                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 146),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 66),
                'E', ItemStackHelper.fromData(IUItem.motors_with_improved_bearings_)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 111), "CBC", " A ", "C C",

                'A', ItemStackHelper.fromData(IUItem.basemachine2, 1, 110),
                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 157),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 66),
                'E', ItemStackHelper.fromData(IUItem.motors_with_improved_bearings_)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pho_machine, 1, 21), "CBC", " A ", "C C",

                'A', ItemStackHelper.fromData(IUItem.basemachine2, 1, 111),
                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 632),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 66),
                'E', ItemStackHelper.fromData(IUItem.motors_with_improved_bearings_)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 184), " B ", "CAD", " E ",

                'A', ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 73),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),
                'E', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 153), " B ", " A ", " C ",

                'A', ItemStackHelper.fromData(IUItem.blockResource, 1, 9),
                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276)
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.basemachine2, 1, 152), "B D", " A ", " C ",

                'A', ItemStackHelper.fromData(IUItem.basemachine2, 1, 153),
                'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27)
        );

        int i;
        for (i = 0; i < 11; i++) {
            Recipes.recipe.addRecipe(
                    ItemStackHelper.fromData(IUItem.universal_cable, 1, i), "CAC", "ABA", "CAC",
                    'B', ItemStackHelper.fromData(
                            IUItem.cable,
                            1,
                            i
                    ), 'A', "wool", 'C', IUItem.synthetic_plate
            );
        }
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewater, 1, 0), "CAC", "ABA", " A ",

                ('A'), "logs",

                ('B'), "planks",
                ('C'), ItemStackHelper.fromData(Items.LAPIS_LAZULI)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewater, 1, 1), " A ", "ABA", " A ",

                ('A'), "forge:storage_blocks/Bronze",

                ('B'), ItemStackHelper.fromData(IUItem.water_rotor_wood, 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewater, 1, 2), " A ", "ABA", " A ",

                ('A'), "forge:storage_blocks/Iron",

                ('B'), ItemStackHelper.fromData(IUItem.water_rotor_bronze, 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewater, 1, 3), " A ", "ABA", " A ",

                ('A'), "forge:plates/Steel",

                ('B'), ItemStackHelper.fromData(IUItem.water_rotor_iron, 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewater, 1, 4), " A ", "ABA", " A ",

                ('A'), "forge:plates/Carbon",

                ('B'), ItemStackHelper.fromData(IUItem.water_rotor_steel, 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewater, 1, 5), "CAC", "ABA", "CAC",

                ('C'), "forge:doubleplate/Iridium",

                ('A'), IUItem.iridiumPlate,

                ('B'), ItemStackHelper.fromData(IUItem.water_rotor_carbon, 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewater, 1, 6), "CAC", "ABA", "CAC",

                ('C'), IUItem.compressIridiumplate,

                ('A'), ItemStackHelper.fromData(IUItem.compresscarbon),

                ('B'), ItemStackHelper.fromData(IUItem.water_iridium
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewater, 1, 7), "DCD", "ABA", "DCD",

                ('D'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('C'), ItemStackHelper.fromData(IUItem.compressIridiumplate),

                ('A'), ItemStackHelper.fromData(IUItem.advnanobox),

                ('B'),
                ItemStackHelper.fromData(IUItem.water_compressiridium
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewater, 1, 8), "DCD", "ABA", " C ",

                ('D'), ItemStackHelper.fromData(IUItem.excitednucleus, 1, 5),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('A'), ItemStackHelper.fromData(IUItem.quantumtool),

                ('B'),
                ItemStackHelper.fromData(IUItem.water_spectral
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewater, 1, 10), "DCD", "CBC", " C ",

                ('D'), ItemStackHelper.fromData(IUItem.excitednucleus, 1, 6),

                ('C'), ItemStackHelper.fromData(IUItem.quantumtool),

                ('B'), ItemStackHelper.fromData(IUItem.water_myphical
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewater, 1, 9), "DCD", "CBC", "ACA",

                ('D'), ItemStackHelper.fromData(IUItem.neutroniumingot),

                ('A'), IUItem.iridiumPlate,

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('B'),
                ItemStackHelper.fromData(IUItem.water_photon
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewater, 1, 11), "DCD", "CBC", " C ",

                ('D'), ItemStackHelper.fromData(IUItem.excitednucleus, 1, 5),

                ('C'), ItemStackHelper.fromData(IUItem.advQuantumtool),

                ('B'), ItemStackHelper.fromData(IUItem.water_neutron
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewater, 1, 12), "ACA", "CBC", "ACA",

                ('A'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10),

                ('B'), ItemStackHelper.fromData(IUItem.water_barionrotor
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.corewater, 1, 13), "ECE", "CBC", "ACA",

                ('E'), TileGenerationMicrochip.getLevelCircuit(ItemStackHelper.fromData(IUItem.basecircuit, 1, 21), 11),

                ('A'), ItemStackHelper.fromData(IUItem.doublecompressIridiumplate),

                ('C'), ItemStackHelper.fromData(IUItem.photoniy_ingot),

                ('B'),
                ItemStackHelper.fromData(IUItem.water_adronrotor
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 0), "A A", "CBC", "A A",

                ('A'), ItemStackHelper.fromData(IUItem.advnanobox),
                ('B'), ItemStackHelper.fromData(IUItem.module_schedule), ('C'), "forge:casings/Nichrome"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 1), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.advQuantumtool), ('B'), ItemStackHelper.fromData(
                        IUItem.core,
                        1,
                        3
                ),
                ('C'), ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 0), ('D'),
                "forge:casings/Redbrass"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 2), "ADA", "CBC", "ADA",

                ('A'), ItemStackHelper.fromData(IUItem.adv_spectral_box), ('B'), ItemStackHelper.fromData(
                        IUItem.core,
                        1,
                        5
                ),
                ('C'), ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 1), ('D'),
                "forge:casings/Muntsa"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.water_rotors_upgrade, 1, 3), "A A", "CBC", "A A",

                ('A'), ItemStackHelper.fromData(IUItem.advnanobox),
                ('B'), ItemStackHelper.fromData(IUItem.module_schedule), ('C'), IUItem.iridiumPlate
        );


        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 574), "C C", "BAB", "BAB",

                'B', "forge:plates/Tungsten", 'A', "forge:plates/Invar", 'C', "forge:plates/Zinc"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 593), "CAC", "BDB", "BAB",

                'B', "forge:plates/Thallium", 'A', "forge:plates/Hafnium", 'C', "forge:plates/Zinc", 'D', "forge:dusts/Redstone"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockPrimalWireInsulator.primal_wire_insulator), "C C", "ACA", "BAB",

                'B', "logs", 'A', "planks", 'C', "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockStrongAnvil.block_strong_anvil), "CCC", "ADA", "   ",

                'A', "forge:plates/Silver", 'C', "forge:storage_blocks/Iridium", 'D', getBlockStack(BlockAnvil.block_anvil)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockMiniSmeltery.mini_smeltery),
                "ACA",
                "EDE",
                "BBB",

                'A',
                "forge:gears/Ferromanganese",
                'B',
                getBlockStack(BlockSmeltery.smeltery_casing),
                'C',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        496
                ),
                'D',
                getBlockStack(BlockBaseMachine3.oak_tank),
                'E',
                "forge:plates/Iridium"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockRefractoryFurnace.refractory_furnace), "ACA", "EDE", "EBE",

                'A', "forge:gears/Invar", 'B', "forge:storage_blocks/Tungsten", 'C', Blocks.FURNACE,
                'D',
                getBlockStack(BlockBaseMachine3.steel_tank), 'E', "forge:plates/Steel"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.steam_storage), " A ", "ABA", " A ",

                'A', "forge:casings/Aluminumbronze", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        601
                )
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.steam_crystal_charge), "AED", " B ", " C ",

                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        123
                ), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        32
                ), 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        601
                ), 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12), 'E', IUItem.charged_redstone
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.steam_fluid_heater), "AED", " B ", " C ",

                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        123
                ), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        32
                ), 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        601
                ), 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12), 'E', IUItem.primalFluidHeater
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.steam_wire_insulator),
                "AED",
                " B ",
                " C ",

                'A',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        123
                ),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        32
                ),
                'B',
                getBlockStack(BlockPrimalWireInsulator.primal_wire_insulator),
                'C',
                ItemStackHelper.fromData(IUItem.blockResource, 1, 12),
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        163
                )
        );

        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.steam_rolling), "AED", " B ", " C ",

                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        123
                ), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        32
                ), 'B', getBlockStack(BlockStrongAnvil.block_strong_anvil), 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        165
                )
        );

        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.steam_quarry), "AED", "BCB", "   ",

                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        123
                ), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        32
                ), 'B', getBlockStack(BlockBaseMachine3.quarry_pipe), 'C', ItemStackHelper.fromData(IUItem.blockResource, 1, 12), 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        158
                )
        );

        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.adv_steam_quarry), "ACA", " B ", "A A",
                'B', getBlockStack(BlockBaseMachine3.steam_quarry), 'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        138
                ), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        256
                )

        );

        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockBaseMachine3.quarry_pipe), 16), "A A", "A A", "A A",
                'A', "forge:plates/Iron"

        );

        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockBaseMachine3.steam_bio_generator), 1), " D ", "BCB", " A ",
                'A', ItemStackHelper.fromData(IUItem.blockResource, 1, 14), 'C', getBlockStack(BlockBaseMachine3.steam_peat_generator), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        295
                ), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        64
                )

        );

        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamBoiler.steam_boiler_casing), 1), "CAC", "ABA", "CAC",
                'A', "forge:casings/Steel", 'C', "forge:casings/Aluminumbronze", 'B', "forge:storage_blocks/Iridium"
        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamBoiler.steam_boiler_tank), 1), " B ", " A ", " C ",
                'A', getBlockStack(BlockSteamBoiler.steam_boiler_casing), 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        601
                ), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        154
                )
        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamBoiler.steam_boiler_heater), 1), " B ", " A ", " C ",
                'A', getBlockStack(BlockSteamBoiler.steam_boiler_casing), 'B',
                getBlockStack(BlockPrimalFluidHeater.primal_fluid_heater), 'C', "forge:gears/Invar"
        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamBoiler.steam_boiler_heat_exchanger), 1),
                " B ",
                "CAC",
                " B ",
                'A',
                getBlockStack(BlockSteamBoiler.steam_boiler_casing),
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        356
                ),
                'C',
                "forge:gears/Invar"
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
                "forge:plates/Osmium",
                'D',
                "forge:casings/Bismuth"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 709), "AAA", "   ", "AAA",
                'A', "forge:plates/Tungsten"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 720), "AAA", "   ", "AAA",
                'A', "forge:plates/Muntsa"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 717), "AAA", "   ", "AAA",
                'A', "forge:plates/StainlessSteel"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 715), "AAA", "   ", "AAA",
                'A', "forge:plates/Nitenol"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 726), "AA ", "BB ", "   ",
                'A', "forge:plates/Titanium", 'B', "forge:plates/advancedAlloy"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 727), "AA ", "BB ", "   ",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 504), 'B', "forge:plates/advancedAlloy"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 707), "AA ", "BB ", "   ",
                'A', "forge:plates/Alumel", 'B', "forge:plates/advancedAlloy"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 711), "AA ", "BB ", "   ",
                'A', ItemStackHelper.fromData(IUItem.plate, 1, 48), 'B', "forge:plates/advancedAlloy"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 714), "AAA", "BBA", "AAA",
                'A', "forge:plates/Molybdenum", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 0)

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 725), "AAA", "BBA", "AAA",
                'A', "forge:plates/Steel", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 0)

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 721), "AAA", "BBA", "AAA",
                'A', "forge:plates/Orichalcum", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 0)

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 708), "AAA", "BBA", "AAA",
                'A', "forge:plates/BerylliumBronze", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 0)

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 716), "AAA", " A ", " A ",
                'A', "forge:plates/Neodymium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 718), "AAA", " A ", " A ",
                'A', "forge:plates/Inconel"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 719), "AAA", " A ", " A ",
                'A', "forge:plates/NiobiumTitanium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 712), "AAA", " A ", " A ",
                'A', "forge:plates/HafniumCarbide"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 710), "CBC", "BAB", "CBC",
                'A', "forge:plates/Mikhail", 'B', "forge:plates/Iridium", 'C', "forge:plates/Invar"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 713), "ABA", "BCB", "ABA",
                'A', "forge:nuggets/Electrum", 'B', "forge:plates/Iron", 'C', "forge:ingots/Gold"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 722), " AA", "BCA", " DD",
                'A', "forge:plates/Mikhail", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 121), 'C', "forge:plates/Platinum", 'D', "forge:plates/Lapis"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 723), " A ", "ABA", " A ",
                'A', "forge:plates/Chromium", 'B', "forge:plates/Thallium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 724), "ABA", "BCB", "ABA",
                'A', "forge:plates/advancedAlloy", 'B', "forge:plates/Iron", 'C', "forge:gems/Sapphire"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 695), "D D", "CBC", "BAB",
                'A', IUItem.overclockerUpgrade, 'B', "forge:plates/Iridium", 'C', "forge:plates/Tantalum", 'D', "forge:plates/Thallium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 702), "D D", "CBC", "BAB",
                'A', IUItem.overclockerUpgrade, 'B', "forge:plates/Osmiridium", 'C', "forge:plates/Tantalum", 'D', "forge:plates/Thallium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 692), "D D", "CBC", "BAB",
                'A', IUItem.overclockerUpgrade, 'B', "forge:plates/Vanadoalumite", 'C', "forge:plates/Tantalum", 'D', "forge:plates/Thallium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 699), "D D", "CBC", "BAB",
                'A', IUItem.overclockerUpgrade, 'B', "forge:plates/StainlessSteel", 'C', "forge:plates/Tantalum", 'D', "forge:plates/Thallium"

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 704), "CBC", "BAB", "CBC",
                'A', ItemStackHelper.fromData(IUItem.solar_day_glass, 1, 0), 'B', "forge:plates/Osmium", 'C', "forge:plates/Lead"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 697), "CBC", "BAB", "CBC",
                'A', ItemStackHelper.fromData(IUItem.solar_day_glass, 1, 2), 'B', "forge:plates/Osmium", 'C', "forge:plates/Lithium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 696), "CBC", "BAB", "CBC",
                'A', ItemStackHelper.fromData(IUItem.solar_day_glass, 1, 4), 'B', "forge:plates/Osmium", 'C', "forge:plates/AluminiumLithium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 693), "  B", " BB", "BBA",
                'A', ItemStackHelper.fromData(Items.SHIELD), 'B', "forge:plates/Vitalium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 700), "  B", " BB", "BBA",
                'A', ItemStackHelper.fromData(Items.SHIELD), 'B', "forge:plates/Nimonic"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 703), "  B", " BB", "BBA",
                'A', ItemStackHelper.fromData(Items.SHIELD), 'B', "forge:plates/Permalloy"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 706), "  B", " BB", "BBA",
                'A', ItemStackHelper.fromData(Items.SHIELD), 'B', "forge:plates/Steel"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 690), "BBB", "ABA", "BBB",
                'A', "forge:plates/Alcled", 'B', "forge:plates/Aluminumbronze"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 691), "C B", " BD", "BDA",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 504), 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 480), 'C',
                "forge:plates/Tungsten", 'D', "forge:plates/HafniumBoride"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 694), "C C", "ACA", "BAB",
                'A', "forge:plates/TantalumTungstenHafnium", 'B', "forge:nuggets/Iridium", 'C', "forge:plates/Mikhail"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 698), " AA", "ABA", "AA ",
                'A', "forge:plates/CobaltChrome", 'B', "forge:plates/Vitalium", 'C', "forge:plates/Mikhail"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 701), " AC", "ABA", "CA ",
                'A', "forge:plates/Osmiridium", 'B', ItemStackHelper.fromData(IUItem.blockResource, 1, 13), 'C', "forge:plates/Iridium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 705), "ACC", "ABB", " CC",
                'A', Items.REDSTONE, 'B', ItemStackHelper.fromData(IUItem.blockResource, 1, 13), 'C', "forge:plates/Arsenic"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 729), "A A", "AB ", " AA",
                'A', "forge:plates/Aluminium", 'B', "forge:rods/Aluminium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 735), "A A", "AB ", " AA",
                'A', "forge:plates/Duralumin", 'B', "forge:rods/Cadmium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 728), "A A", "AB ", " AA",
                'A', "forge:plates/Alcled", 'B', "forge:rods/Thallium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 760), "A A", "AB ", " AA",
                'A', "forge:plates/SuperalloyRene", 'B', "forge:rods/Barium"

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 745),
                "BDD",
                "CAE",
                "BCC",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),
                'B',
                ItemStackHelper.fromData(IUItem.cable, 1, 11),
                'C',
                ItemStackHelper.fromData(IUItem.graphene_wire),
                'D',
                "forge:plates/Zinc",
                'E',
                "forge:plates/Tin"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 763), "BDD", "CAE", "BCC",
                'A', TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4), 'B', ItemStackHelper.fromData(IUItem.cable, 1, 11),
                'C', ItemStackHelper.fromData(IUItem.graphene_wire), 'D', "forge:plates/Nitenol", 'E', "forge:plates/Alumel"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 751), "BDD", "CAE", "BCC",
                'A', TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6), 'B', ItemStackHelper.fromData(IUItem.cable, 1, 11),
                'C', ItemStackHelper.fromData(IUItem.graphene_wire), 'D', ItemStackHelper.fromData(IUItem.plate, 1, 50), 'E', "forge:plates/Vitalium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 757),
                "BDD",
                "CAE",
                "BCC",
                'A',
                TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),
                'B',
                ItemStackHelper.fromData(IUItem.cable, 1, 11),
                'C',
                ItemStackHelper.fromData(IUItem.graphene_wire),
                'D',
                ItemStackHelper.fromData(IUItem.plate, 1, 51),
                'E',
                "forge:plates/YttriumAluminiumGarnet"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 742), "AAB", "ABC", "BCC",
                'A', "forge:plates/Osmium", 'B', "forge:plates/Steel", 'C', "forge:plates/Lead"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 761), "AAB", "ABC", "BCC",
                'A', "forge:plates/Osmium", 'B', "forge:plates/Barium", 'C', "forge:plates/Zeliber"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 748), "AAB", "ABC", "BCC",
                'A', "forge:plates/Osmium", 'B', "forge:plates/NiobiumTitanium", 'C', "forge:plates/HafniumCarbide"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 754), "AAB", "ABC", "BCC",
                'A', "forge:plates/Osmium", 'B', "forge:plates/AluminiumLithium", 'C', "forge:plates/MolybdenumSteel"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 740), " AA", "AAA", "AA ",
                'A', "forge:plates/Neodymium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 741), " AA", "AAA", "AA ",
                'A', "forge:plates/Nimonic"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 767), " AA", "AAA", "AA ",
                'A', "forge:plates/Ferromanganese"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 736), " AA", "AAA", "AA ",
                'A', "forge:plates/YttriumAluminiumGarnet"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 746), "AAB", "ABA", "C C",
                'A', "forge:plates/Tungsten", 'B', Items.REDSTONE, 'C', "forge:plates/Osmium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 730), "AAB", "ABA", "C C",
                'A', "forge:plates/Tungsten", 'B', Items.REDSTONE, 'C', "forge:plates/GalliumArsenic"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 737), "AAB", "ABA", "C C",
                'A', "forge:plates/Tungsten", 'B', Items.REDSTONE, 'C', "forge:plates/YttriumAluminiumGarnet"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 759), "AAB", "ABA", "C C",
                'A', "forge:plates/Tungsten", 'B', Items.REDSTONE, 'C', "forge:plates/Stellite"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 743), " A ", "ABA", "BBB",
                'A', "forge:plates/Yttrium", 'B', "forge:plates/Steel"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 762), " A ", "ABA", "BBB",
                'A', "forge:plates/Vitalium", 'B', "forge:plates/Barium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 749), " A ", "ABA", "BBB",
                'A', "forge:plates/Redbrass", 'B', "forge:plates/HafniumCarbide"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 755), " A ", "ABA", "BBB",
                'A', "forge:plates/BerylliumBronze", 'B', ItemStackHelper.fromData(IUItem.plate, 1, 51)

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 744), "BAB", "C C", "BAB",
                'A', "forge:plates/HafniumBoride", 'B', "forge:plates/Iron", 'C', "forge:plates/Tin"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 764), "BAB", "C C", "BAB",
                'A', ItemStackHelper.fromData(IUItem.plate, 1, 49), 'B', "forge:plates/Alumel", 'C', "forge:plates/BerylliumBronze"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 750), "BAB", "C C", "BAB",
                'A', "forge:plates/CobaltChrome", 'B', ItemStackHelper.fromData(IUItem.plate, 1, 50), 'C', "forge:plates/Osmiridium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 756), "BAB", "C C", "BAB",
                'A', "forge:plates/Woods", 'B', "forge:plates/MolybdenumSteel", 'C', "forge:plates/Vanadoalumite"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 731), "BAB", "ABA", "BAB",
                'A', "forge:plates/Titanium", 'B', Items.REDSTONE

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 732), " AA", " BA", "   ",
                'A', "forge:plates/Steel", 'B', "forge:plates/Tungsten"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 733), " AB", "ABA", " A ",
                'A', "forge:plates/Vitalium", 'B', "forge:plates/Hafnium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 734), "ABA", "CBC", "CBC",
                'A', "forge:plates/Thallium", 'B', "forge:plates/Silver", 'C', "forge:plates/Tantalum"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 738), "BAB", "BBB", "C C",
                'A', "forge:plates/Polonium", 'B', "forge:plates/Tantalum", 'C', "forge:nuggets/Gallium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 739), " AB", "ABA", " A ",
                'A', "forge:plates/CobaltChrome", 'B', "forge:plates/NiobiumTitanium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 747), " A ", "BBB", "BCB",
                'A', "forge:plates/Bismuth", 'B', "forge:plates/CobaltChrome", 'C', "forge:plates/Thallium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 752), " A ", "ABA", " AC",
                'A', "forge:plates/Manganese", 'B', "forge:plates/Yttrium", 'C', "forge:plates/Barium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 753), "ABB", "BAC", " BC",
                'A', "forge:plates/Barium", 'B', "forge:plates/Electrum", 'C', "forge:plates/GalliumArsenic"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 758), "ABA", "AAA", "CBC",
                'A', "forge:plates/Chromium", 'B', "forge:plates/Mikhail", 'C', "forge:plates/Spinel"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 766), " AB", "ABC", " BC",
                'A', "forge:plates/AluminiumSilicon", 'B', "forge:plates/Vitalium", 'C', "forge:plates/CobaltChrome"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 768), "ABC", "BAB", "CBA",
                'A', "forge:plates/Hafnium", 'B', "forge:plates/Alcled", 'C', "forge:plates/AluminiumSilicon"

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockWindTurbine.wind_turbine_casing_1), " B ", "BAB", " B ",
                'A', Blocks.BRICKS, 'B'
                , "forge:plates/StainlessSteel"
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
                , "forge:plates/Nimonic", 'C', TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10), 'D',
                "forge:plates/Nitenol", 'E', getBlockStack(BlockBaseMachine3.per_wind_generator)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockWindTurbine.wind_turbine_socket), "DCD", "BAB", " E ",
                'A', getBlockStack(BlockWindTurbine.wind_turbine_casing_1), 'B'
                , ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 596), 'D',
                IUItem.plastic_plate, 'E', "forge:plates/SuperalloyHaynes"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockWindTurbine.wind_turbine_stabilizer), "DCD", "BAB", " E ",
                'A', getBlockStack(BlockWindTurbine.wind_turbine_casing_1), 'B'
                , ItemStackHelper.fromData(IUItem.electronic_stabilizers), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 599), 'D',
                IUItem.photoniy_ingot, 'E', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 594)
        );


        Recipes.recipe.addRecipe(getBlockStack(BlockHydroTurbine.hydro_turbine_casing_1), " B ", "BAB", " B ",
                'A', Blocks.BRICKS, 'B'
                , "forge:plates/Inconel"
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
                , "forge:plates/Nimonic", 'C', TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 10), 'D',
                "forge:plates/Nitenol", 'E', getBlockStack(BlockBaseMachine3.per_water_generator)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockHydroTurbine.hydro_turbine_socket), "DCD", "BAB", " E ",
                'A', getBlockStack(BlockHydroTurbine.hydro_turbine_casing_1), 'B'
                , ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 596), 'D',
                IUItem.plastic_plate, 'E', "forge:plates/SuperalloyHaynes"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockHydroTurbine.hydro_turbine_stabilizer), "DCD", "BAB", " E ",
                'A', getBlockStack(BlockHydroTurbine.hydro_turbine_casing_1), 'B'
                , ItemStackHelper.fromData(IUItem.electronic_stabilizers), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 599), 'D',
                IUItem.photoniy_ingot, 'E', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 594)
        );

        Recipes.recipe.addRecipe(getBlockStack(BlockBarrel.barrel), "ABA", "ABC", "ABA",
                'A', "logs", 'B'
                , "planks", 'C', Blocks.STONE_BUTTON
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockApiary.apiary), "AAA", "B B", "AAA",
                'A', "slabs", 'B'
                , IUItem.rubWood.getItem(0)
        );

        Recipes.recipe.addRecipe(getBlockStack(BlockGasTurbine.gas_turbine_casing), " B ", "BAB", " B ",
                'A', Blocks.BRICKS, 'B'
                , ItemStackHelper.fromData(IUItem.casing, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasTurbine.gas_turbine_airbearings), " B ", "BAB", " B ",
                'B', ItemStackHelper.fromData(IUItem.windrod, 1, 2), 'A'
                , getBlockStack(BlockGasTurbine.gas_turbine_casing)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasTurbine.gas_turbine_tank), "DBC", " A ", "   ",
                'A', getBlockStack(BlockGasTurbine.gas_turbine_casing), 'B'
                , ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 154),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 263)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasTurbine.gas_turbine_socket), "CDC", "BAB", "   ",
                'A', getBlockStack(BlockGasTurbine.gas_turbine_casing), 'B'
                , ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 582),
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 574)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasTurbine.gas_turbine_blower), " A ", " B ", " C ",
                'A', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 112), 'B'
                , getBlockStack(BlockGasTurbine.gas_turbine_casing), 'C', ItemStackHelper.fromData(IUItem.plate, 1, 50)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasTurbine.gas_turbine_recuperator), " B ", "BAB", " B ",
                'B', ItemStackHelper.fromData(IUItem.water_rod, 1, 2), 'A'
                , getBlockStack(BlockGasTurbine.gas_turbine_casing)
        );
        Recipes.recipe.addRecipe(IUItem.net, "ABA", "AAA", " A ",
                'B', "wool", 'A'
                ,Items.STICK
        );
        Recipes.recipe.addRecipe(IUItem.agricultural_analyzer, "BBB", "CAC", "   ",
                'B', "forge:plates/Iron", 'A'
                , TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1), 'C', IUItem.crops
        );
        Recipes.recipe.addRecipe(IUItem.bee_analyzer, "BBB", "CAC", "   ",
                'B', "forge:plates/Iron", 'A'
                , TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1), 'C', IUItem.jarBees
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasTurbine.gas_turbine_controller), "EAE", "CBC", " D ",
                'A', TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8), 'B'
                , getBlockStack(BlockGasTurbine.gas_turbine_casing), 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 585)
                , 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 589), 'E', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397)
        );

        Recipes.recipe.addRecipe(getBlockStack(BlockGasWell.gas_well_casing), " C ", "BAB", " C ",
                'A', Blocks.BRICKS, 'B'
                , ItemStackHelper.fromData(IUItem.casing, 1, 49), 'C', "forge:plates/Vanadoalumite"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasWell.gas_well_transport),
                "BDB", "DAD", "BDB", 'B', ItemStackHelper.fromData(IUItem.item_pipes, 1, 27), 'D', ItemStackHelper.fromData(IUItem.item_pipes, 1, 28),
                'A',
                getBlockStack(BlockGasWell.gas_well_casing)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasWell.gas_well_drill),
                " E ", "DAD", "CBC", 'B', ItemStackHelper.fromData(IUItem.oilquarry), 'A', getBlockStack(BlockGasWell.gas_well_casing)
                , 'C'
                , "forge:plates/Titanium", 'D', "forge:plates/Steel", 'E', "forge:plates/Iridium"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasWell.gas_well_analyzer),
                "DBD",
                "EAE",
                " C ",
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 226),
                'A',
                getBlockStack(BlockGasWell.gas_well_casing)
                ,
                'C'
                ,
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),
                'D',
                "forge:gears/Osmium",
                'E',
                "forge:plates/Carbon"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasWell.gas_well_controller),
                "DBD",
                " A ",
                " C ",
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 42),
                'A',
                getBlockStack(BlockGasWell.gas_well_casing)
                ,
                'C'
                ,
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 4),
                'D',
                "forge:rods/Zinc"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasWell.gas_well_socket),
                "DBD",
                " A ",
                " C ",
                'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 592),
                'A',
                getBlockStack(BlockGasWell.gas_well_casing)
                ,
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 614),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockGasWell.gas_well_tank),
                " E ", "DAD", "CBC", 'B', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43), 'A',
                getBlockStack(BlockGasWell.gas_well_casing)
                , 'C'
                , "forge:plates/Gold", 'D', "forge:plates/Tin", 'E', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27)
        );
        Recipes.recipe.addRecipe(IUItem.spaceupgrademodule_schedule, " A ", "ABA", " A ",
                'B', IUItem.module_schedule, 'A'
                , ItemStackHelper.fromData(IUItem.plate, 1, 49)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spaceupgrademodule, 1, 3),
                " B ",
                "CAC",
                " E ",
                'A',
                ItemStackHelper.fromData(IUItem.spaceupgrademodule_schedule, 1),
                'B',
                "forge:plates/Molybdenum",
                'C',
                "forge:plates/Cadmium",
                'E',
                "forge:plates/Lapis"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spaceupgrademodule, 1, 1), " B ", "CAC", " E ",
                'A', ItemStackHelper.fromData(IUItem.spaceupgrademodule_schedule, 1), 'B', ItemStackHelper.fromData(IUItem.plate, 1, 46), 'C',
                "forge:plates/Redbrass", 'E', "forge:plates/Palladium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spaceupgrademodule, 1, 2),
                " B ",
                "CAC",
                " E ",
                'A',
                ItemStackHelper.fromData(IUItem.spaceupgrademodule_schedule, 1),
                'B',
                "forge:plates/HafniumCarbide",
                'C',
                "forge:plates/Osmiridium",
                'E',
                "forge:plates/NiobiumTitanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spaceupgrademodule, 1, 0),
                " B ",
                "CAC",
                " E ",
                'A',
                ItemStackHelper.fromData(IUItem.spaceupgrademodule_schedule, 1),
                'B',
                "forge:plates/Woods",
                'C',
                "forge:plates/BerylliumBronze",
                'E',
                "forge:plates/Nitenol"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spaceupgrademodule, 1, 5), " B ", "CAC", " E ",
                'A', ItemStackHelper.fromData(IUItem.spaceupgrademodule_schedule, 1), 'B', "forge:plates/AluminiumLithium", 'C',
                ItemStackHelper.fromData(IUItem.plate, 1, 47), 'E', "forge:plates/Duralumin"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spaceupgrademodule, 1, 4),
                " B ",
                "CAC",
                " E ",
                'A',
                ItemStackHelper.fromData(IUItem.spaceupgrademodule_schedule, 1),
                'B',
                "forge:plates/Obsidian",
                'C',
                "forge:plates/Muntsa",
                'E',
                "forge:plates/Germanium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.spaceupgrademodule, 1, 6),
                " B ",
                "CAC",
                " E ",
                'A',
                ItemStackHelper.fromData(IUItem.spaceupgrademodule_schedule, 1),
                'B',
                "forge:plates/Alcled",
                'C',
                "forge:plates/Inconel",
                'E',
                ItemStackHelper.fromData(IUItem.plate, 1, 47)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 657), "BEB", "CAC", "EEE",
                'A', DEFAULT_SENSOR, 'C', "forge:doubleplate/Thallium", 'B', "forge:gears/Alcled", 'E', "forge:plates/Chromium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 658),
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
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 659), "BEB", "CAC", "DED",
                'A', DEFAULT_SENSOR, 'C', IUItem.polonium_palladium_composite, 'B', "forge:gears/CobaltChrome", 'E', "forge:plates/Zeliber", 'D',
                ItemStackHelper.fromData(IUItem.plate, 1, 50)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 660), "BBB", "CAC", "   ",
                'A', DEFAULT_SENSOR, 'C', "forge:gears/Palladium", 'B', Blocks.GRANITE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 661), "BDB", "CAC", " H ",
                'A', DEFAULT_SENSOR, 'C', Items.GLOWSTONE_DUST, 'B', Items.REDSTONE, 'D', Items.GLASS_BOTTLE, 'H', Items.GUNPOWDER
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 662), "BBB", "CAC", " D ",
                'A', DEFAULT_SENSOR, 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426), 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 320), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 597)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 663), "BBB", "CAC", "   ",
                'A', DEFAULT_SENSOR, 'C', "forge:gears/Osmiridium", 'B', Blocks.CACTUS
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 664), "DBD", "CAC", "   ",
                'A', DEFAULT_SENSOR, 'C', "forge:gears/Vanadoalumite", 'B', Items.MILK_BUCKET, 'D', Items.BEEF
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 665), "DBD", "CAC", "   ",
                'A', DEFAULT_SENSOR, 'C', "forge:gears/Zirconium", 'B', Items.EGG, 'D', Items.CHICKEN
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 675), "DDD", "CAC", "   ",
                'A', DEFAULT_SENSOR, 'C', "forge:gears/Vitalium", 'D', Items.PORKCHOP
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 666), "DBD", "CAC", " E ",
                'A', DEFAULT_SENSOR, 'C', "forge:plates/advancedAlloy", 'B', IUItem.royal_jelly, 'D', "forge:plates/Carbon", 'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 295)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 667), "DBD", "CAC", " E ",
                'A', DEFAULT_SENSOR, 'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 480), 'B', IUItem.honeycomb, 'D', "forge:plates/Bor",
                'E',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 295)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 668), " B ", "CAC", " D ",
                'A', DEFAULT_SENSOR, 'C', "forge:plates/Stellite", 'B', "forge:plates/HafniumCarbide", 'D', "forge:gems/Sapphire"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 669), "DBD", "CAC", "DBD",
                'A', DEFAULT_SENSOR, 'C', "forge:gears/Nichrome", 'B', "forge:gears/Magnesium", 'D', "forge:gears/Zirconium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 670), "DBD", "CAC", "   ",
                'A', DEFAULT_SENSOR, 'C', "forge:gears/Spinel", 'B', "wool", 'D', Items.MUTTON
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 671), "EBE", "CAC", "DBD",
                'A', DEFAULT_SENSOR, 'C', ItemStackHelper.fromData(IUItem.iudust, 1, 74), 'B', ItemStackHelper.fromData(IUItem.iudust, 1, 40), 'D',
                "forge:plates/Ferromanganese", 'E', "forge:plates/Aluminumbronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 672), " B ", "CAC", " B ",
                'A', DEFAULT_SENSOR, 'C', "forge:plates/Steel", 'B', "forge:plates/Invar"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 673), "BCB", "CAC", "   ",
                'A', DEFAULT_SENSOR, 'C', "forge:gears/Zinc", 'B', "forge:plates/Tin"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 674), "BCB", "DAD", "   ",
                'A', DEFAULT_SENSOR, 'C', "saplings", 'B', "forge:plates/Strontium", 'D', "forge:plates/Hafnium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 676), "BCB", "DAD", " C ",
                'A', DEFAULT_SENSOR, 'C', ItemStackHelper.fromData(IUItem.plate, 1, 47), 'B', ItemStackHelper.fromData(IUItem.plate, 1, 50), 'D',
                "forge:plates/AluminiumLithium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 677), "BCB", "DAD", " C ",
                'A', DEFAULT_SENSOR, 'C', "forge:plates/Molybdenum", 'B', "forge:plates/Bismuth", 'D',
                "forge:plates/Duralumin"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 765), "ABB", "AB ", "B  ",
                'A', "forge:plates/Thallium", 'B', "forge:plates/Niobium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678), "C B", "CAB", "   ",
                'A', DEFAULT_SENSOR, 'C', "forge:plates/Nichrome", 'B', "forge:plates/Tungsten"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 679), "CBC", "DAD", " B ",
                'A', DEFAULT_SENSOR, 'C', "forge:plates/HafniumCarbide", 'B', "forge:plates/Neodymium", 'D', "forge:plates/Lapis"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 680), "CBC", "DAD", " B ",
                'A', DEFAULT_SENSOR, 'C', "forge:plates/Obsidian", 'B', "forge:gears/AluminiumSilicon", 'D', "forge:plates/Osmiridium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 681), "CBC", "DAD", "EBE",
                'A', DEFAULT_SENSOR, 'C', "forge:plates/vanady", 'B', "forge:plates/Mikhail", 'D', "forge:plates/Vitalium", 'E', "forge:plates/Chromium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 683),
                "CBC",
                "DAD",
                "EBE",
                'A',
                DEFAULT_SENSOR,
                'C',
                "forge:plates/BerylliumBronze",
                'B',
                "forge:plates/Zeliber",
                'D',
                "forge:plates/Nichrome",
                'E',
                "forge:plates/Aluminumbronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 682), "CBC", "DAD", " B ",
                'A', DEFAULT_SENSOR, 'C', "forge:plates/Thallium", 'B', ItemStackHelper.fromData(IUItem.plate, 1, 41), 'D', "forge:plates/Stellite"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 684),
                "CBC",
                "DAD",
                "EBE",
                'A',
                DEFAULT_SENSOR,
                'C',
                "forge:plates/AluminiumSilicon",
                'B',
                "forge:plates/Ferromanganese",
                'D',
                "forge:plates/Titanium",
                'E',
                "forge:plates/Molybdenum"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 685), "CBC", "DAD", "EBE",
                'A', DEFAULT_SENSOR, 'C', "forge:gears/Permalloy", 'B', "forge:gears/CobaltChrome", 'D', "forge:gears/Tantalum", 'E', "forge:casings/Cadmium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 686), "CBC", "DAD", "   ",
                'A', DEFAULT_SENSOR, 'C', "forge:gears/Cobalt", 'B', Items.IRON_AXE, 'D', "forge:plates/Bronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 687), "CBC", "DAD", "   ",
                'A', DEFAULT_SENSOR, 'C', "forge:plates/Carbon", 'B', "forge:plates/advancedAlloy", 'D', "forge:plates/Bor"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 688), "CBC", "DAD", "EBE",
                'A', DEFAULT_SENSOR, 'C', "forge:plates/TantalumTungstenHafnium", 'B', "forge:plates/Osmiridium", 'D', "forge:gems/Ruby", 'E', "forge:gems/Topaz"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 689), "CBC", "DAD", "   ",
                'A', DEFAULT_SENSOR, 'C', "forge:gears/Barium", 'B', IUItem.fertilizer, 'D', "forge:plates/Steel"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.electric_wire_insulator), " A ", "BC ", " D ",
                'A', getBlockStack(BlockBaseMachine3.steam_wire_insulator), 'C', IUItem.machine, 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.crystal_charge), " A ", "BC ", " D ",
                'A', getBlockStack(BlockBaseMachine3.steam_crystal_charge), 'C', IUItem.machine, 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.ampere_generator), " A ", " C ", " D ",
                'A', getBlockStack(BlockBaseMachine3.steam_ampere_generator), 'C', IUItem.machine, 'B',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44), 'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.electric_refractory_furnace), " A ", "BCE", " D ",
                'A', getBlockStack(BlockBaseMachine3.steam_ampere_generator), 'C', IUItem.machine, 'B',
                getBlockStack(BlockMiniSmeltery.mini_smeltery), 'D',
                IUItem.motors_with_improved_bearings_, 'E', getBlockStack(BlockRefractoryFurnace.refractory_furnace)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 6), "BBB", "CAC", "BBB",

                ('A'), "forge:plates/Iron",

                ('B'), "planks",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 7), "BBB", "CAC", "BBB",

                ('A'), "forge:plates/Iron",

                ('B'), "planks",

                ('C'), ItemStackHelper.fromData(Items.LAPIS_LAZULI)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 8), "BBB", " A ", "BBB",

                ('A'), "forge:plates/Iron",

                ('B'), "planks"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 9), "BBB", "CAC", "BBB",

                ('A'), "forge:plates/Gold",

                ('B'), "forge:plates/Iron",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 10), "BBB", "CAC", "BBB",

                ('A'), "forge:plates/Gold",

                ('B'), "forge:plates/Iron",

                ('C'), ItemStackHelper.fromData(Items.LAPIS_LAZULI)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 11), "BBB", " A ", "BBB",

                ('A'), "forge:plates/Gold",

                ('B'), "forge:plates/Iron"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 12), "BBB", "CAC", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 122),

                ('B'), "forge:plates/Steel",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 13), "BBB", "CAC", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 122),

                ('B'), "forge:plates/Steel",

                ('C'), ItemStackHelper.fromData(Items.LAPIS_LAZULI)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 14), "BBB", " A ", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 122),

                ('B'), "forge:plates/Steel"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 15), "BBB", "CAC", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 122),

                ('B'), "forge:plates/Aluminumbronze",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 16), "BBB", "CAC", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 122),

                ('B'), "forge:plates/Aluminumbronze",

                ('C'), ItemStackHelper.fromData(Items.LAPIS_LAZULI)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 17), "BBB", " A ", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 122),

                ('B'), "forge:plates/Aluminumbronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 27), "BBB", "CAC", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 122),

                ('B'), "forge:plates/Zeliber",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 29), "BBB", "CAC", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 122),

                ('B'), "forge:plates/Zeliber",

                ('C'), ItemStackHelper.fromData(Items.LAPIS_LAZULI)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 28), "BBB", " A ", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 122),

                ('B'), "forge:plates/Zeliber"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 18), "BBB", "CAC", "BBB",

                ('A'), "forge:plates/Cobalt",

                ('B'), "planks",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 20), "BBB", " A ", "BBB",

                ('A'), "forge:plates/Cobalt",

                ('B'), "planks"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 19), "BBB", "CAC", "BBB",

                ('A'), "forge:plates/Cobalt",

                ('B'), "planks",

                ('C'), ItemStackHelper.fromData(Items.LAPIS_LAZULI)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 21), "BBB", "CAC", "BBB",

                ('A'), "forge:plates/Nichrome",

                ('B'), "forge:plates/Cobalt",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 22), "BBB", " A ", "BBB",

                ('A'), "forge:plates/Nichrome",

                ('B'), "forge:plates/Cobalt"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 23), "BBB", "CAC", "BBB",

                ('A'), "forge:plates/Nichrome",

                ('B'), "forge:plates/Cobalt",

                ('C'), ItemStackHelper.fromData(Items.LAPIS_LAZULI)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 24), "BBB", "CAC", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 122),

                ('B'), "forge:plates/CobaltChrome",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 25), "BBB", " A ", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 122),

                ('B'), "forge:plates/CobaltChrome"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.item_pipes, 6, 26), "BBB", "CAC", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 122),

                ('B'), "forge:plates/CobaltChrome",

                ('C'), ItemStackHelper.fromData(Items.LAPIS_LAZULI)
        );

        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.chicken_farm), " C ", " A ", " B ",

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 665)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.sheep_farm), " C ", " A ", " B ",

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 670)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.cow_farm), " C ", " A ", " B ",

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 664)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.pig_farm), " C ", " A ", " B ",

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 675)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.sapling_gardener), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 672),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 674)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.tree_breaker), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 686),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 674)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.cactus_farm), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 663),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 672)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.electric_brewing), " D ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 661),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 658),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.sawmill), "   ", "DAE", " B ",

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 669),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 673),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.gen_addition_stone), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 660),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.plant_fertilizer), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 689),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 61)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.plant_collector), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 64),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 243)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.plant_gardener), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 64),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 672)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.field_cleaner), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 671),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 672)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.weeder), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),

                ('C'), ItemStackHelper.fromData(IUItem.module7, 1, 9),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 64)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.field_cleaner), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 671),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 672)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.apothecary_bee), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 666),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 656)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.collector_product_bee), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 666),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 667)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.incubator), "H D", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 662),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 659),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),
                ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.insulator), "H D", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 662),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 676),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),
                ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.rna_collector), "H D", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 685),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 659),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),
                ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.mutatron), " D ", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 682),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 676),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.reverse_transcriptor), " D ", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 668),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 685),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.genetic_stabilizer), "H D", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 685),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 682),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),
                ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.centrifuge), "   ", "CAH", " B ",

                ('A'), IUItem.machine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 669),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 682),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),
                ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.genetic_replicator), "H D", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 659),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 682),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),
                ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 676)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.inoculator), " D ", "CAH", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 659),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 676)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.genome_extractor), "H D", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 659),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 67),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),
                ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 676)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.genetic_polymerizer), "HFD", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 659),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 676),
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.genetic_transposer), "HFD", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 659),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),
                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 676),
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 682)
        );


        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.shield), "   ", "CAE", " B ",

                ('A'), IUItem.machine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 54),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 439)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.hologram_space), " C ", " A ", " B ",

                ('A'), IUItem.machine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 657)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.upgrade_rover), " H ", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 66),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 679),

                ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.research_table_space), " H ", "CAE", " B ",

                ('A'), IUItem.rubWood.getItem(0),

                ('B'), "forge:gears/Invar",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 683),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),

                ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.probe_assembler), " H ", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 677),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 243),

                ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.rocket_assembler), " H ", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 684),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 243),

                ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.satellite_assembler), " H ", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 688),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 243),

                ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.rover_assembler), " H ", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 681),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 243),

                ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.rocket_launch_pad), "   ", "CAE", " B ",

                ('A'), IUItem.advancedMachine,

                ('B'), IUItem.motors_with_improved_bearings_,

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 684),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 680)


        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.research_lens, 1, 0), " B ", "BAB", " B ",

                ('A'), Blocks.GLASS,

                ('B'), "forge:plates/Cobalt"


        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.research_lens, 1, 1), " B ", "BAB", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.research_lens, 1, 0),

                ('B'), ItemStackHelper.fromData(IUItem.plate, 1, 46)


        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.research_lens, 1, 2), " B ", "BAB", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.research_lens, 1, 1),

                ('B'), ItemStackHelper.fromData(IUItem.plate, 1, 50)


        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.research_lens, 1, 3), " B ", "BAB", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.research_lens, 1, 2),

                ('B'), ItemStackHelper.fromData(IUItem.plate, 1, 47)


        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.research_lens, 1, 4), " B ", "BAB", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.research_lens, 1, 3),

                ('B'), ItemStackHelper.fromData(IUItem.plate, 1, 48)


        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.research_lens, 1, 5), " B ", "BAB", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.research_lens, 1, 4),

                ('B'), "forge:plates/Stellite"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.research_lens, 1, 6), " B ", "BAB", " B ",

                ('A'), ItemStackHelper.fromData(IUItem.research_lens, 1, 5),

                ('B'), "forge:plates/Nimonic"

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 0), "BBB", "CAD", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/Steel",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/Gadolinium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 1), "BBB", "CAD", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/MolybdenumSteel",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/Gadolinium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 2), "BEB", "CAD", "BEB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/Woods",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/Gadolinium",
                'E', "forge:plates/Permalloy"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 3), "BEB", "CAD", "BEB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/Nimonic",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/Gadolinium",
                'E', "forge:plates/SuperalloyHaynes"

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 4), "BBB", "CAD", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/Steel",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/Cadmium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 5), "BBB", "CAD", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/MolybdenumSteel",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/Cadmium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 6), "BEB", "CAD", "BEB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/Woods",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/Cadmium",
                'E', "forge:plates/Permalloy"

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 7), "BBB", "CAD", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/Steel",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/AluminiumLithium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 8), "BBB", "CAD", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/MolybdenumSteel",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/AluminiumLithium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 9), "BEB", "CAD", "BEB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/Woods",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/AluminiumLithium",
                'E', "forge:plates/Permalloy"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 10), "BBB", "CAD", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/Steel",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/Duralumin"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 11), "BBB", "CAD", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/MolybdenumSteel",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/Duralumin"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 12), "BEB", "CAD", "BEB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/Woods",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/Duralumin",
                'E', "forge:plates/Permalloy"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 13), "BBB", "CAD", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/Steel",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/Spinel"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 14), "BBB", "CAD", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/MolybdenumSteel",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/Spinel"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 15), "BEB", "CAD", "BEB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/Woods",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/Spinel",
                'E', "forge:plates/Permalloy"

        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 16), "BBB", "CAD", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/Steel",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', ItemStackHelper.fromData(IUItem.photonglass, 1, 0)

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 17), "BBB", "CAD", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/MolybdenumSteel",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', ItemStackHelper.fromData(IUItem.photonglass, 1, 2)

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 18), "BEB", "CAD", "BEB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/Woods",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', ItemStackHelper.fromData(IUItem.photonglass, 1, 4),
                'E', "forge:plates/Permalloy"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 19), "BBB", "CAD", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/Steel",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/Mikhail"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 24), "BBB", "CAD", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/MolybdenumSteel",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/Mikhail"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 25), "BEB", "CAD", "BEB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/Woods",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/Mikhail",
                'E', "forge:plates/Permalloy"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 20), "BBB", "CAD", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/Steel",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/Polonium"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 21), "BBB", "CAD", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/Steel",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/BerylliumBronze"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 22), "BBB", "CAD", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/MolybdenumSteel",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/BerylliumBronze"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 23), "BEB", "CAD", "BEB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/Woods",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/BerylliumBronze",
                'E', "forge:plates/Permalloy"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.colonial_building, 1, 23), "BEB", "CAD", "BEB",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13),

                ('B'), "forge:plates/Woods",
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 678),
                'D', "forge:gears/BerylliumBronze",
                'E', "forge:plates/Permalloy"

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.blockResource, 1, 14), "BBB", "BAB", "BBB",

                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 295),

                ('B'), "forge:plates/Duralumin"

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_macerator), " E ", "BAC", " D ",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 14),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 64),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 69)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_gearing), " E ", "BAC", " D ",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 14),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 64),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 218)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_extractor), " E ", "BAC", " D ",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 14),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 64),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 159)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_compressor), " E ", "BAC", " D ",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 14),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 64),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 63)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_extruder), " E ", "BAC", " D ",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 14),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 64),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 163)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_cutting), " E ", "BAC", " D ",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 14),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 64),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 132)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_rolling), " E ", "BAC", " D ",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 14),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 64),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 165)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_furnace), " E ", "BAC", " D ",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 14),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 64),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 219)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_orewashing), " E ", "BAC", " D ",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 14),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 64),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 34)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_centrifuge), "E H", "BAC", " D ",

                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 14),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 44),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 64),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 39),
                ('H'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 70)

        );

        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.single_multi_crop), " E ", "BAC", " D ",

                ('A'), getBlockStack(BlockMoreMachine3.farmer),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27),

                ('D'), IUItem.motors_with_improved_bearings_,
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 689)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.dual_multi_crop), "B B", "FAC", "BDB",

                ('A'), getBlockStack(BlockBaseMachine3.single_multi_crop),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 6),

                ('D'), IUItem.adv_motors_with_improved_bearings_,
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51)


        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.triple_multi_crop), "BEB", "FAC", "BDB",

                ('A'), getBlockStack(BlockBaseMachine3.dual_multi_crop),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 83),

                ('D'), IUItem.imp_motors_with_improved_bearings_,
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 51),
                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 47)


        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.quad_multi_crop), "B B", "FAC", "BDB",

                ('A'), getBlockStack(BlockBaseMachine3.triple_multi_crop),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 107),

                ('D'), IUItem.imp_motors_with_improved_bearings_,
                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 52)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_casing_glass), 5), "ABA", "BAB",
                "ABA",

                ('A'), IUItem.glass,

                ('B'), "forge:casings/Titanium"


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_casing), 2), "AB ", "BA ",
                "   ",

                ('A'), "forge:plates/Tungsten",

                ('B'), "forge:plates/Duralumin"


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_rod), 1), "   ", "AAA",
                "   ",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_casing)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_socket), 1), "CDC", "BAB",
                "EFE",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_casing),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 585),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 583),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 592),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 561)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_controller_rod), 1), "CDC", "BAB",
                "EFE",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_casing),

                ('B'), getBlockStack(BlockSteamTurbine.steam_turbine_rod),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 581),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 574),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 479),

                ('F'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7)


        );

        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_controller), 1), "CDC", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_casing),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356),

                ('D'), "forge:plates/Neodymium",

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitNano, 6),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 581)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_adv_controller), 1), "CDC", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_controller),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424),

                ('D'), "forge:plates/MolybdenumSteel",

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 7),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 579)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_imp_controller), 1), "CDC", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_adv_controller),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371),

                ('D'), "forge:plates/Draconid",

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.cirsuitQuantum, 8),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 563)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_per_controller), 1), "CDC", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_imp_controller),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397),

                ('D'), "forge:plates/Woods",

                ('E'), TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral, 9),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 585)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_tank), 1), "C D", "BAB",
                " ",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_casing),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 367),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 43),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 27)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_adv_tank), 1), "C D", "BAB",
                " ",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_tank),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 415),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 45),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 6)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_imp_tank), 1), "C D", "BAB",
                " ",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_adv_tank),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 380),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 48),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 83)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_per_tank), 1), "C D", "BAB",
                " ",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_imp_tank),

                ('B'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 396),

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 50),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 107)
        );

        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_coolant), 1), "DCD", "BAB",
                " E ",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_casing),

                ('B'), "forge:plates/Neodymium",

                ('C'), ItemStackHelper.fromData(IUItem.coolingsensor),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356),

                ('E'), getBlockStack(BlockBaseMachine3.fluid_cooling)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_adv_coolant), 1), "DCD", "BAB",
                " E ",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_coolant),

                ('B'), "forge:plates/MolybdenumSteel",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 581),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 424),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 582)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_imp_coolant), 1), "DCD", "BAB",
                " E ",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_adv_coolant),

                ('B'), "forge:plates/Draconid",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 579),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 371),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 599)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_per_coolant), 1), "DCD", "BAB",
                " E ",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_imp_coolant),

                ('B'), "forge:plates/Woods",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 563),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 397),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 597)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_pressure), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_casing),

                ('B'), "forge:plates/Neodymium",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 63),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 387),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 581)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_adv_pressure), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_pressure),

                ('B'), "forge:plates/MolybdenumSteel",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 1),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 425),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 579)


        );
        BaseRecipeFour.init();

    }


}
