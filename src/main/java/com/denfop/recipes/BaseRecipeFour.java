package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.blocks.mechanism.BlockImpSolarEnergy;
import com.denfop.blocks.mechanism.BlockSteamTurbine;
import com.denfop.tiles.mechanism.TileGenerationMicrochip;
import com.denfop.utils.ModUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static com.denfop.register.RegisterOreDictionary.spaceElementList;

public class BaseRecipeFour {

    public static ItemStack DEFAULT_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 21);

    public static ItemStack ADV_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 25);

    public static ItemStack IMP_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 23);

    public static ItemStack PER_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 24);

    public static ItemStack PHOTON_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 620);

    public static ItemStack getBlockStack(IMultiTileBlock block) {
        return TileBlockCreator.instance.get(block.getIDBlock()).getItemStack(block);
    }

    public static void init() {
        Recipes.recipe.addRecipe(
                ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_imp_pressure), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_adv_pressure),

                ('B'), "plateDraconid",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 77),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 373),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 372),

                ('F'), new ItemStack(IUItem.crafting_elements, 1, 563)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_per_pressure), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_imp_pressure),

                ('B'), "plateWoods",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 102),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 402),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 398),

                ('F'), new ItemStack(IUItem.crafting_elements, 1, 585)


        );

        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_exchanger), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_casing),

                ('B'), "plateNeodymium",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 39),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 414),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 582),

                ('F'), new ItemStack(IUItem.crafting_elements, 1, 581)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_adv_exchanger), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_exchanger),

                ('B'), "plateMolybdenumSteel",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 15),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 426),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 599),

                ('F'), new ItemStack(IUItem.crafting_elements, 1, 579)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_imp_exchanger), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_adv_exchanger),

                ('B'), "plateDraconid",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 91),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 373),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 583),

                ('F'), new ItemStack(IUItem.crafting_elements, 1, 563)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_per_exchanger), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_imp_exchanger),

                ('B'), "plateWoods",

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 115),

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 402),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 597),

                ('F'), new ItemStack(IUItem.crafting_elements, 1, 585)


        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.bee_frame_template), "AAA", "ABA",
                "AAA",

                ('A'), "stickWood",

                ('B'), "plankWood"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_bee_frame_template), "AAA", "ABA",
                "AAA",

                ('A'), new ItemStack(IUItem.wax_stick),

                ('B'), "plankWood"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.imp_bee_frame_template), "AAA", "ABA",
                "AAA",

                ('A'), new ItemStack(IUItem.polished_stick),

                ('B'), "plankWood"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 0), "CDC", "ABA",
                " D ",

                ('A'), "gearPolonium",

                ('B'), IUItem.bee_frame_template,
                'C', "plateVanadium",
                'D', "casingCopper"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 1), "CDC", "ABA",
                " D ",

                ('A'), "gearPolonium",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "plateVanadium",
                'D', "casingCopper"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 2), "CDC", "ABA",
                " D ",

                ('A'), "gearPolonium",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "plateVanadium",
                'D', "casingCopper"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 9), "CDC", "ABA",
                " D ",

                ('A'), "gearSpinel",

                ('B'), IUItem.bee_frame_template,
                'C', "plateObsidian",
                'D', "casingCobalt"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 10), "CDC", "ABA",
                " D ",

                ('A'), "gearSpinel",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "plateObsidian",
                'D', "casingCobalt"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 11), "CDC", "ABA",
                " D ",

                ('A'), "gearSpinel",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "plateObsidian",
                'D', "casingCobalt"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 15), "CDC", "ABA",
                " D ",

                ('A'), "gearMolybdenum",

                ('B'), IUItem.bee_frame_template,
                'C', "plateTantalum",
                'D', "casingSilver"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 16), "CDC", "ABA",
                " D ",

                ('A'), "gearMolybdenum",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "plateTantalum",
                'D', "casingSilver"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 17), "CDC", "ABA",
                " D ",

                ('A'), "gearMolybdenum",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "plateTantalum",
                'D', "casingSilver"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 12), "CDC", "ABA",
                " D ",

                ('A'), "gearBarium",

                ('B'), IUItem.bee_frame_template,
                'C', "plateGadolinium",
                'D', "casingVanadium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 13), "CDC", "ABA",
                " D ",

                ('A'), "gearBarium",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "plateGadolinium",
                'D', "casingVanadium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 14), "CDC", "ABA",
                " D ",

                ('A'), "gearBarium",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "plateGadolinium",
                'D', "casingVanadium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 18), "CDC", "ABA",
                " D ",

                ('A'), "gearGallium",

                ('B'), IUItem.bee_frame_template,
                'C', "plateLead",
                'D', "casingAluminium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 19), "CDC", "ABA",
                " D ",

                ('A'), "gearGallium",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "plateLead",
                'D', "casingAluminium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 20), "CDC", "ABA",
                " D ",

                ('A'), "gearGallium",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "plateLead",
                'D', "casingAluminium"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 21), "CDC", "ABA",
                " D ",

                ('A'), "gearGermanium",

                ('B'), IUItem.bee_frame_template,
                'C', "plateIron",
                'D', "casingBronze"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 22), "CDC", "ABA",
                " D ",

                ('A'), "gearGermanium",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "plateIron",
                'D', "casingBronze"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 23), "CDC", "ABA",
                " D ",

                ('A'), "gearGermanium",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "plateIron",
                'D', "casingBronze"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 3), "CDC", "ABA",
                " D ",

                ('A'), "gearMuntsa",

                ('B'), IUItem.bee_frame_template,
                'C', "plateOsmium",
                'D', "casingGold"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 4), "CDC", "ABA",
                " D ",

                ('A'), "gearMuntsa",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "plateOsmium",
                'D', "casingGold"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 5), "CDC", "ABA",
                " D ",

                ('A'), "gearMuntsa",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "plateOsmium",
                'D', "casingGold"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 6), "CDC", "ABA",
                " D ",

                ('A'), "gearAluminumbronze",

                ('B'), IUItem.bee_frame_template,
                'C', "plateMithril",
                'D', "casingYttrium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 7), "CDC", "ABA",
                " D ",

                ('A'), "gearAluminumbronze",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "plateMithril",
                'D', "casingYttrium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.frame, 1, 8), "CDC", "ABA",
                " D ",

                ('A'), "gearAluminumbronze",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "plateMithril",
                'D', "casingYttrium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.double_molecular), " B ", "CAC",
                " B ",

                ('A'), IUItem.module_schedule,

                ('B'), "plateWoods",

                ('C'), "plateBloodstone"


        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.quad_molecular), " B ", "CAC",
                " B ",

                ('A'), IUItem.module_schedule,

                ('B'), "platePermalloy",

                ('C'), "plateDraconid"


        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.booze_mug), "AA ", "AAB",
                "AA ",

                ('A'), "plankWood"
                , 'B', "stickWood"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nightpipes), " B ", "CAC",
                " B ",

                ('A'), IUItem.scable
                , 'B', "plateMithril", 'C', IUItem.synthetic_rubber

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.night_transformer), " C ", "DAE",
                " B ",

                ('A'), getBlockStack(BlockImpSolarEnergy.imp_se_gen)
                , 'B', IUItem.imp_motors_with_improved_bearings_,
                'C', IUItem.advancedMachine,
                'D', new ItemStack(IUItem.crafting_elements, 1, 30),
                'E', new ItemStack(IUItem.crafting_elements, 1, 68)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.night_converter), "DCD", " A ",
                "DBD",

                ('A'), getBlockStack(BlockBaseMachine3.moon_spotter)
                , 'B', IUItem.imp_motors_with_improved_bearings_,
                'C', IUItem.advancedMachine,
                'D', "platePermalloy"


        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.peat_generator), " C ", " A ",
                " B ",
                ('A'), getBlockStack(BlockBaseMachine3.steam_peat_generator)
                , 'B', IUItem.motors_with_improved_bearings_,
                'C', IUItem.advancedMachine
        );
        Recipes.recipe.addRecipe(IUItem.relocator, "ABA", "BCB",
                "ABA",
                ('A'), IUItem.iridiumPlate
                , 'B', Items.ENDER_PEARL,
                'C', getBlockStack(BlockBaseMachine3.teleporter_iu)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_cokeoven, 1, 0), " A ", "CBC",
                " A ",
                ('A'), "plateBerylliumBronze"
                , 'B', new ItemStack(IUItem.cokeoven, 1, 0),
                'C', "platePermalloy"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_cokeoven, 1, 1), " A ", "CBC",
                " A ",
                ('A'), "plateBerylliumBronze"
                , 'B', new ItemStack(IUItem.cokeoven, 1, 1),
                'C', "platePermalloy"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_cokeoven, 1, 2), " A ", "CBC",
                " A ",
                ('A'), "plateBerylliumBronze"
                , 'B', new ItemStack(IUItem.cokeoven, 1, 2),
                'C', "platePermalloy"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_cokeoven, 1, 3), " A ", "CBC",
                " A ",
                ('A'), "plateBerylliumBronze"
                , 'B', new ItemStack(IUItem.cokeoven, 1, 3),
                'C', "platePermalloy"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_cokeoven, 1, 4), " A ", "CBC",
                " A ",
                ('A'), "plateBerylliumBronze"
                , 'B', new ItemStack(IUItem.cokeoven, 1, 4),
                'C', "platePermalloy"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_cokeoven, 1, 5), " A ", "CBC",
                " A ",
                ('A'), "plateBerylliumBronze"
                , 'B', new ItemStack(IUItem.cokeoven, 1, 5),
                'C', "platePermalloy"
        );
        for (String s : spaceElementList) {
            Recipes.recipe.addRecipe("block" + s, "AAA", "AAA",
                    "AAA",
                    ('A'), "ingot" + s
            );

        }
        for (int i = 0; i < spaceElementList.size(); i++) {
            Recipes.recipe.addShapelessRecipe(
                    ModUtils.setSize(new ItemStack(IUItem.iuingot, 1, 43 + i), 9),
                    new ItemStack(IUItem.preciousblock, 1, 3 + i)
            );
        }

        Recipes.recipe.addRecipe(new ItemStack(IUItem.lightning_rod, 1, 3), "A A", "A A",
                "A A",
                ('A'), "plateSteel"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.lightning_rod, 1, 4), "AAA", " A ",
                " A ",
                ('A'), "plateSteel"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.lightning_rod, 1, 1), " A ", "ABA",
                " A ",
                ('A'), "casingSteel", 'B', Blocks.BRICK_BLOCK
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.lightning_rod, 1, 5), " A ", " A ",
                "ABA",
                ('A'), "plateSteel", 'B', new ItemStack(IUItem.lightning_rod, 1, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.lightning_rod, 1, 2), "ACA", "ACA",
                "CBC",
                ('A'), "plateSteel", 'B', new ItemStack(IUItem.lightning_rod, 1, 1), 'C', IUItem.charged_redstone
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.lightning_rod, 1, 0),
                "",
                "ACA",
                "DBD",
                ('A'),
                "plateSteel",
                'B',
                new ItemStack(IUItem.lightning_rod, 1, 1),
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.ampere_storage), " A ", " B ",
                " C ",
                ('A'), IUItem.reBattery, 'B', IUItem.advancedMachine,
                'C', new ItemStack(IUItem.crafting_elements, 1,
                        276
                )
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.gen_bio), "   ", "ABD",
                " C ",
                ('A'), new ItemStack(IUItem.crafting_elements, 1,
                        27
                ), 'B', IUItem.machine,
                'C', new ItemStack(IUItem.crafting_elements, 1,
                        276
                ), 'D', new ItemStack(IUItem.crafting_elements, 1,
                        64
                )
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_generator), " A ", " B ",
                " C ",
                ('A'), getBlockStack(BlockBaseMachine3.steam_bio_generator), 'B', IUItem.machine,
                'C', new ItemStack(IUItem.crafting_elements, 1,
                        276
                ), 'D', new ItemStack(IUItem.crafting_elements, 1,
                        64
                )
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.amperepipes), "   ", "ABA",
                "   ",
                ('A'), "platePlatinum", 'B', IUItem.copperCableItem
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.tomeResearch), "   ", "ABA",
                " C ",
                ('A'), "plateMeteoric", 'B', Items.BOOK, 'C', "gemRuby"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pollutionDevice), "AAA", "ABA",
                " C ",
                ('A'), "plateIron", 'B', TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2), 'C', "gemAmericium"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.ruby_axe), "AA", "AB ",
                " B ",
                ('A'), "gemRuby", 'B', "stickWood"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.sapphire_axe), "AA", "AB ",
                " B ",
                ('A'), "gemSapphire", 'B', "stickWood"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.topaz_axe), "AA", "AB ",
                " B ",
                ('A'), "gemTopaz", 'B', "stickWood"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.ruby_pickaxe), "AAA", " B ",
                " B ",
                ('A'), "gemRuby", 'B', "stickWood"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.sapphire_pickaxe), "AAA", " B ",
                " B ",
                ('A'), "gemSapphire", 'B', "stickWood"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.topaz_pickaxe), "AAA", " B ",
                " B ",
                ('A'), "gemTopaz", 'B', "stickWood"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.ruby_shovel), " A ", " B ",
                " B ",
                ('A'), "gemRuby", 'B', "stickWood"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.sapphire_shovel), " A ", " B ",
                " B ",
                ('A'), "gemSapphire", 'B', "stickWood"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.topaz_shovel), " A ", " B ",
                " B ",
                ('A'), "gemTopaz", 'B', "stickWood"
        );

        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.steam_generator), " A ", " B ",
                " C ",
                ('A'), getBlockStack(BlockBaseMachine3.steam_converter), 'B', new ItemStack(IUItem.blockResource, 1, 8),
                'C', new ItemStack(IUItem.crafting_elements, 1, 276)
        );
    }

}
