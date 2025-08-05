package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockRaws;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.blocks.mechanism.BlockImpSolarEnergy;
import com.denfop.blocks.mechanism.BlockSteamTurbine;
import com.denfop.items.resource.ItemRawMetals;
import com.denfop.tiles.mechanism.TileGenerationMicrochip;
import com.denfop.utils.ModUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.ModList;

import static com.denfop.register.RegisterOreDictionary.spaceElementList;

public class BaseRecipeFour {

    public static Item DEFAULT_SENSOR;

    public static Item ADV_SENSOR;

    public static Item IMP_SENSOR;

    public static Item PER_SENSOR;
    public static Item PHOTON_SENSOR;

    public static ItemStack getBlockStack(IMultiTileBlock block) {
        return TileBlockCreator.instance.get(block.getIDBlock()).getItemStack();
    }

    public static void init() {
        DEFAULT_SENSOR = IUItem.crafting_elements.getItemFromMeta(21);

        ADV_SENSOR = IUItem.crafting_elements.getItemFromMeta(25);

        IMP_SENSOR = IUItem.crafting_elements.getItemFromMeta(23);

        PER_SENSOR = IUItem.crafting_elements.getItemFromMeta(24);

        PHOTON_SENSOR = IUItem.crafting_elements.getItemFromMeta(620);
        Recipes.recipe.addRecipe(
                ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_imp_pressure), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_adv_pressure),

                ('B'), "c:plates/Draconid",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 77),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 372),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 563)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_per_pressure), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_imp_pressure),

                ('B'), "c:plates/Woods",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 102),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 398),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 585)


        );

        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_exchanger), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_casing),

                ('B'), "c:plates/Neodymium",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 39),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 582),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 581)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_adv_exchanger), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_exchanger),

                ('B'), "c:plates/MolybdenumSteel",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 15),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 599),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 579)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_imp_exchanger), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_adv_exchanger),

                ('B'), "c:plates/Draconid",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 91),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 583),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 563)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_per_exchanger), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_imp_exchanger),

                ('B'), "c:plates/Woods",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 115),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 597),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 585)


        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.bee_frame_template), "AAA", "ABA",
                "AAA",

                ('A'), Items.STICK,

                ('B'), "planks"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_bee_frame_template), "AAA", "ABA",
                "AAA",

                ('A'), ItemStackHelper.fromData(IUItem.wax_stick),

                ('B'), "planks"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.imp_bee_frame_template), "AAA", "ABA",
                "AAA",

                ('A'), ItemStackHelper.fromData(IUItem.polished_stick),

                ('B'), "planks"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 0), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Polonium",

                ('B'), IUItem.bee_frame_template,
                'C', "c:plates/vanady",
                'D', "c:casings/Copper"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 1), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Polonium",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "c:plates/vanady",
                'D', "c:casings/Copper"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 2), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Polonium",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "c:plates/vanady",
                'D', "c:casings/Copper"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 9), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Spinel",

                ('B'), IUItem.bee_frame_template,
                'C', "c:plates/Obsidian",
                'D', "c:casings/Cobalt"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 10), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Spinel",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "c:plates/Obsidian",
                'D', "c:casings/Cobalt"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 11), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Spinel",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "c:plates/Obsidian",
                'D', "c:casings/Cobalt"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 15), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Molybdenum",

                ('B'), IUItem.bee_frame_template,
                'C', "c:plates/Tantalum",
                'D', "c:casings/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 16), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Molybdenum",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "c:plates/Tantalum",
                'D', "c:casings/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 17), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Molybdenum",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "c:plates/Tantalum",
                'D', "c:casings/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 12), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Barium",

                ('B'), IUItem.bee_frame_template,
                'C', "c:plates/Gadolinium",
                'D', "c:casings/vanady"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 13), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Barium",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "c:plates/Gadolinium",
                'D', "c:casings/vanady"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 14), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Barium",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "c:plates/Gadolinium",
                'D', "c:casings/vanady"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 18), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Gallium",

                ('B'), IUItem.bee_frame_template,
                'C', "c:plates/Lead",
                'D', "c:casings/Aluminium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 19), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Gallium",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "c:plates/Lead",
                'D', "c:casings/Aluminium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 20), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Gallium",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "c:plates/Lead",
                'D', "c:casings/Aluminium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 21), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Germanium",

                ('B'), IUItem.bee_frame_template,
                'C', "c:plates/Iron",
                'D', "c:casings/Bronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 22), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Germanium",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "c:plates/Iron",
                'D', "c:casings/Bronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 23), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Germanium",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "c:plates/Iron",
                'D', "c:casings/Bronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 3), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Muntsa",

                ('B'), IUItem.bee_frame_template,
                'C', "c:plates/Osmium",
                'D', "c:casings/Gold"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 4), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Muntsa",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "c:plates/Osmium",
                'D', "c:casings/Gold"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 5), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Muntsa",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "c:plates/Osmium",
                'D', "c:casings/Gold"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 6), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Aluminumbronze",

                ('B'), IUItem.bee_frame_template,
                'C', "c:plates/Mithril",
                'D', "c:casings/Yttrium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 7), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Aluminumbronze",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "c:plates/Mithril",
                'D', "c:casings/Yttrium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 8), "CDC", "ABA",
                " D ",

                ('A'), "c:gears/Aluminumbronze",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "c:plates/Mithril",
                'D', "c:casings/Yttrium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.double_molecular), " B ", "CAC",
                " B ",

                ('A'), IUItem.module_schedule,

                ('B'), "c:plates/Woods",

                ('C'), "c:plates/Bloodstone"


        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.quad_molecular), " B ", "CAC",
                " B ",

                ('A'), IUItem.module_schedule,

                ('B'), "c:plates/Permalloy",

                ('C'), "c:plates/Draconid"


        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.booze_mug), "AA ", "AAB",
                "AA ",

                ('A'), "planks"
                , 'B', Items.STICK

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nightpipes), " B ", "CAC",
                " B ",

                ('A'), IUItem.scable
                , 'B', "c:plates/Mithril", 'C', IUItem.synthetic_rubber

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.night_transformer), " C ", "DAE",
                " B ",

                ('A'), getBlockStack(BlockImpSolarEnergy.imp_se_gen)
                , 'B', IUItem.imp_motors_with_improved_bearings_,
                'C', IUItem.advancedMachine,
                'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 30),
                'E', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 68)

        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.night_converter), "DCD", " A ",
                "DBD",

                ('A'), getBlockStack(BlockBaseMachine3.moon_spotter)
                , 'B', IUItem.imp_motors_with_improved_bearings_,
                'C', IUItem.advancedMachine,
                'D', "c:plates/Permalloy"


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
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_cokeoven, 1, 0), " A ", "CBC",
                " A ",
                ('A'), "c:plates/BerylliumBronze"
                , 'B', ItemStackHelper.fromData(IUItem.cokeoven, 1, 0),
                'C', "c:plates/Permalloy"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_cokeoven, 1, 1), " A ", "CBC",
                " A ",
                ('A'), "c:plates/BerylliumBronze"
                , 'B', ItemStackHelper.fromData(IUItem.cokeoven, 1, 1),
                'C', "c:plates/Permalloy"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_cokeoven, 1, 2), " A ", "CBC",
                " A ",
                ('A'), "c:plates/BerylliumBronze"
                , 'B', ItemStackHelper.fromData(IUItem.cokeoven, 1, 2),
                'C', "c:plates/Permalloy"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_cokeoven, 1, 3), " A ", "CBC",
                " A ",
                ('A'), "c:plates/BerylliumBronze"
                , 'B', ItemStackHelper.fromData(IUItem.cokeoven, 1, 3),
                'C', "c:plates/Permalloy"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_cokeoven, 1, 4), " A ", "CBC",
                " A ",
                ('A'), "c:plates/BerylliumBronze"
                , 'B', ItemStackHelper.fromData(IUItem.cokeoven, 1, 4),
                'C', "c:plates/Permalloy"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_cokeoven, 1, 5), " A ", "CBC",
                " A ",
                ('A'), "c:plates/BerylliumBronze"
                , 'B', ItemStackHelper.fromData(IUItem.cokeoven, 1, 5),
                'C', "c:plates/Permalloy"
        );

        for (int i = 0; i < spaceElementList.size(); i++) {
            String s = spaceElementList.get(i);
            Recipes.recipe.addRecipe(IUItem.preciousblock.getItem(3 + i), "AAA", "AAA",
                    "AAA",
                    ('A'), "c:ingots/" + s
            );

        }
        for (int i = 0; i < spaceElementList.size(); i++) {
            Recipes.recipe.addShapelessRecipe(
                    ModUtils.setSize(ItemStackHelper.fromData(IUItem.iuingot, 1, 43 + i), 9),
                    ItemStackHelper.fromData(IUItem.preciousblock, 1, 3 + i)
            );
        }

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.lightning_rod, 1, 3), "A A", "A A",
                "A A",
                ('A'), "c:plates/Steel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.lightning_rod, 1, 4), "AAA", " A ",
                " A ",
                ('A'), "c:plates/Steel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.lightning_rod, 1, 1), " A ", "ABA",
                " A ",
                ('A'), "c:casings/Steel", 'B', Blocks.BRICKS
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.lightning_rod, 1, 5), " A ", " A ",
                "ABA",
                ('A'), "c:plates/Steel", 'B', ItemStackHelper.fromData(IUItem.lightning_rod, 1, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.lightning_rod, 1, 2), "ACA", "ACA",
                "CBC",
                ('A'), "c:plates/Steel", 'B', ItemStackHelper.fromData(IUItem.lightning_rod, 1, 1), 'C', IUItem.charged_redstone
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.lightning_rod, 1, 0),
                "",
                "ACA",
                "DBD",
                ('A'),
                "c:plates/Steel",
                'B',
                ItemStackHelper.fromData(IUItem.lightning_rod, 1, 1),
                'C',
                TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),
                'D',
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 356)
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.ampere_storage), " A ", " B ",
                " C ",
                ('A'), IUItem.reBattery, 'B', IUItem.advancedMachine,
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        276
                )
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.gen_bio), "   ", "ABD",
                " C ",
                ('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        27
                ), 'B', IUItem.machine,
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        276
                ), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        64
                )
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.bio_generator), " A ", " B ",
                " C ",
                ('A'), getBlockStack(BlockBaseMachine3.steam_bio_generator), 'B', IUItem.machine,
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        276
                ), 'D', ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                        64
                )
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.amperepipes), "   ", "ABA",
                "   ",
                ('A'), "c:plates/Platinum", 'B', IUItem.copperCableItem
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.tomeResearch), "   ", "ABA",
                " C ",
                ('A'), "c:plates/Meteoric", 'B', Items.BOOK, 'C', "c:gems/Ruby"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pollutionDevice), "AAA", "ABA",
                " C ",
                ('A'), "c:plates/Iron", 'B', TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2), 'C', "c:gems/Americium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ruby_axe), "AA", "AB ",
                " B ",
                ('A'), "c:gems/Ruby", 'B', Items.STICK
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.sapphire_axe), "AA", "AB ",
                " B ",
                ('A'), "c:gems/Sapphire", 'B', Items.STICK
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.topaz_axe), "AA", "AB ",
                " B ",
                ('A'), "c:gems/Topaz", 'B', Items.STICK
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ruby_pickaxe), "AAA", " B ",
                " B ",
                ('A'), "c:gems/Ruby", 'B', Items.STICK
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.sapphire_pickaxe), "AAA", " B ",
                " B ",
                ('A'), "c:gems/Sapphire", 'B', Items.STICK
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.topaz_pickaxe), "AAA", " B ",
                " B ",
                ('A'), "c:gems/Topaz", 'B', Items.STICK
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ruby_shovel), " A ", " B ",
                " B ",
                ('A'), "c:gems/Ruby", 'B', Items.STICK
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.sapphire_shovel), " A ", " B ",
                " B ",
                ('A'), "c:gems/Sapphire", 'B', Items.STICK
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.topaz_shovel), " A ", " B ",
                " B ",
                ('A'), "c:gems/Topaz", 'B', Items.STICK
        );

        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.steam_generator), " A ", " B ",
                " C ",
                ('A'), getBlockStack(BlockBaseMachine3.steam_converter), 'B', ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276)
        );
        for (int i = 0; i < BlockRaws.Type.values().length; i++) {
            Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rawsBlock.getItem(i), 1), "AAA", "AAA", "AAA",

                    ('A'), "c:raw_materials/" + BlockRaws.Type.values()[i].name()
            );
            for (int ii = 0; ii < ItemRawMetals.Types.values().length; ii++) {
                if (IUItem.rawMetals.getRegistryObject(ii) != null && IUItem.rawMetals.getStack(ii).getTags()[0].equals("c:raw_materials/" + BlockRaws.Type.values()[i].name())) {
                    Recipes.recipe.addShapelessRecipe(ItemStackHelper.fromData(IUItem.rawMetals.getStack(ii), 9), ItemStackHelper.fromData(IUItem.rawsBlock.getItem(i), 1));
                    break;
                }
            }
        }
        Recipes.recipe.addRecipe(IUItem.pipette.getItem(1), "  A", "BA ",
                "CCB",
                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13), 'B', "c:plates/yttrium",
                'C', "c:plates/spinel"
        );
        Recipes.recipe.addShapelessRecipe(new ItemStack(IUItem.book.getItem()),
                Items.BOOK,
                Items.COPPER_INGOT
        );
        Recipes.recipe.addShapelessRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 4, 773),
                Items.CLAY_BALL, Items.CLAY_BALL, "c:nuggets/spinel", "c:nuggets/spinel",
                "c:nuggets/yttrium", "c:nuggets/yttrium", Blocks.SAND, Blocks.GRAVEL
        );
        Recipes.recipe.addShapelessRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 4, 770),
                Items.CLAY_BALL, Items.CLAY_BALL, "c:ingots/magnesium", "c:ingots/magnesium", Blocks.SAND, Blocks.GRAVEL
        );
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.blockRubberWoods.getItem(0), 4), ItemStackHelper.fromData(IUItem.rubWood, 1));
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.blockRubberWoods.getItem(1), 4), ItemStackHelper.fromData(IUItem.swampRubWood, 1));
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.blockRubberWoods.getItem(2), 4), ItemStackHelper.fromData(IUItem.tropicalRubWood, 1));
        Recipes.recipe.addRecipe(IUItem.smallFluidCell.getItem(1), " A ", "ABA",
                " A ",
                ('A'),"c:casings/copper", 'B', Blocks.GLASS_PANE
        );
        Recipes.recipe.addRecipe(IUItem.reinforcedFluidCell.getItem(1), " A ", "ABA",
                " A ",
                ('A'),"c:casings/tungsten", 'B', Blocks.GLASS_PANE
        );
        Recipes.recipe.addRecipe(IUItem.latexPipette, "  A", "DB ",
                "CD ",
                ('A'),"c:casings/steel", 'B', Blocks.GLASS_PANE,'C', IUItem.treetap,'D',"c:casings/cobalt"
        );
        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.auto_latex_collector), "A  ", "CBC",
                "CCC",
                ('A'),"planks", 'B',IUItem.latexPipette.getItem(),'C', "terracotta"
        );
        if (ModList.get().isLoaded("powerutils")) {
            RecipesPowerUtils.register();
        }
        if (ModList.get().isLoaded("simplyquarries")) {
            RecipeSimplyQuarries.register();
        }
        if (ModList.get().isLoaded("quantumgenerators")) {
            RecipeQuantumGenerators.register();
        }
    }

}
