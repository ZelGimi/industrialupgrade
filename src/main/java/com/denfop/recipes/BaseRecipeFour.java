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
import net.minecraftforge.fml.ModList;

import static com.denfop.register.RegisterOreDictionary.spaceElementList;

public class BaseRecipeFour {

    public static Item DEFAULT_SENSOR;

    public static Item ADV_SENSOR;

    public static Item IMP_SENSOR;

    public static Item PER_SENSOR ;
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

                ('B'), "forge:plates/Draconid",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 77),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 372),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 563)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_per_pressure), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_imp_pressure),

                ('B'), "forge:plates/Woods",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 102),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 398),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 585)


        );

        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_exchanger), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_casing),

                ('B'), "forge:plates/Neodymium",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 39),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 414),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 582),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 581)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_adv_exchanger), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_exchanger),

                ('B'), "forge:plates/MolybdenumSteel",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 15),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 426),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 599),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 579)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_imp_exchanger), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_adv_exchanger),

                ('B'), "forge:plates/Draconid",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 91),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 373),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 583),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 563)


        );
        Recipes.recipe.addRecipe(ModUtils.setSize(getBlockStack(BlockSteamTurbine.steam_turbine_per_exchanger), 1), "DCD", "BAB",
                "FEF",

                ('A'), getBlockStack(BlockSteamTurbine.steam_turbine_imp_exchanger),

                ('B'), "forge:plates/Woods",

                ('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 115),

                ('D'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 402),

                ('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 597),

                ('F'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 585)


        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.bee_frame_template), "AAA", "ABA",
                "AAA",

                ('A'),Items.STICK,

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

                ('A'), "forge:gears/Polonium",

                ('B'), IUItem.bee_frame_template,
                'C', "forge:plates/vanady",
                'D', "forge:casings/Copper"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 1), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Polonium",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "forge:plates/vanady",
                'D', "forge:casings/Copper"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 2), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Polonium",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "forge:plates/vanady",
                'D', "forge:casings/Copper"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 9), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Spinel",

                ('B'), IUItem.bee_frame_template,
                'C', "forge:plates/Obsidian",
                'D', "forge:casings/Cobalt"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 10), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Spinel",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "forge:plates/Obsidian",
                'D', "forge:casings/Cobalt"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 11), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Spinel",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "forge:plates/Obsidian",
                'D', "forge:casings/Cobalt"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 15), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Molybdenum",

                ('B'), IUItem.bee_frame_template,
                'C', "forge:plates/Tantalum",
                'D', "forge:casings/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 16), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Molybdenum",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "forge:plates/Tantalum",
                'D', "forge:casings/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 17), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Molybdenum",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "forge:plates/Tantalum",
                'D', "forge:casings/Silver"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 12), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Barium",

                ('B'), IUItem.bee_frame_template,
                'C', "forge:plates/Gadolinium",
                'D', "forge:casings/vanady"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 13), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Barium",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "forge:plates/Gadolinium",
                'D', "forge:casings/vanady"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 14), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Barium",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "forge:plates/Gadolinium",
                'D', "forge:casings/vanady"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 18), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Gallium",

                ('B'), IUItem.bee_frame_template,
                'C', "forge:plates/Lead",
                'D', "forge:casings/Aluminium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 19), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Gallium",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "forge:plates/Lead",
                'D', "forge:casings/Aluminium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 20), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Gallium",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "forge:plates/Lead",
                'D', "forge:casings/Aluminium"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 21), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Germanium",

                ('B'), IUItem.bee_frame_template,
                'C', "forge:plates/Iron",
                'D', "forge:casings/Bronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 22), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Germanium",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "forge:plates/Iron",
                'D', "forge:casings/Bronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 23), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Germanium",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "forge:plates/Iron",
                'D', "forge:casings/Bronze"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 3), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Muntsa",

                ('B'), IUItem.bee_frame_template,
                'C', "forge:plates/Osmium",
                'D', "forge:casings/Gold"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 4), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Muntsa",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "forge:plates/Osmium",
                'D', "forge:casings/Gold"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 5), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Muntsa",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "forge:plates/Osmium",
                'D', "forge:casings/Gold"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 6), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Aluminumbronze",

                ('B'), IUItem.bee_frame_template,
                'C', "forge:plates/Mithril",
                'D', "forge:casings/Yttrium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 7), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Aluminumbronze",

                ('B'), IUItem.adv_bee_frame_template,
                'C', "forge:plates/Mithril",
                'D', "forge:casings/Yttrium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.frame, 1, 8), "CDC", "ABA",
                " D ",

                ('A'), "forge:gears/Aluminumbronze",

                ('B'), IUItem.imp_bee_frame_template,
                'C', "forge:plates/Mithril",
                'D', "forge:casings/Yttrium"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.double_molecular), " B ", "CAC",
                " B ",

                ('A'), IUItem.module_schedule,

                ('B'), "forge:plates/Woods",

                ('C'), "forge:plates/Bloodstone"


        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.quad_molecular), " B ", "CAC",
                " B ",

                ('A'), IUItem.module_schedule,

                ('B'), "forge:plates/Permalloy",

                ('C'), "forge:plates/Draconid"


        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.booze_mug), "AA ", "AAB",
                "AA ",

                ('A'), "planks"
                , 'B', Items.STICK

        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.nightpipes), " B ", "CAC",
                " B ",

                ('A'), IUItem.scable
                , 'B', "forge:plates/Mithril", 'C', IUItem.synthetic_rubber

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
                'D', "forge:plates/Permalloy"


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
                ('A'), "forge:plates/BerylliumBronze"
                , 'B', ItemStackHelper.fromData(IUItem.cokeoven, 1, 0),
                'C', "forge:plates/Permalloy"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_cokeoven, 1, 1), " A ", "CBC",
                " A ",
                ('A'), "forge:plates/BerylliumBronze"
                , 'B', ItemStackHelper.fromData(IUItem.cokeoven, 1, 1),
                'C', "forge:plates/Permalloy"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_cokeoven, 1, 2), " A ", "CBC",
                " A ",
                ('A'), "forge:plates/BerylliumBronze"
                , 'B', ItemStackHelper.fromData(IUItem.cokeoven, 1, 2),
                'C', "forge:plates/Permalloy"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_cokeoven, 1, 3), " A ", "CBC",
                " A ",
                ('A'), "forge:plates/BerylliumBronze"
                , 'B', ItemStackHelper.fromData(IUItem.cokeoven, 1, 3),
                'C', "forge:plates/Permalloy"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_cokeoven, 1, 4), " A ", "CBC",
                " A ",
                ('A'), "forge:plates/BerylliumBronze"
                , 'B', ItemStackHelper.fromData(IUItem.cokeoven, 1, 4),
                'C', "forge:plates/Permalloy"
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.adv_cokeoven, 1, 5), " A ", "CBC",
                " A ",
                ('A'), "forge:plates/BerylliumBronze"
                , 'B', ItemStackHelper.fromData(IUItem.cokeoven, 1, 5),
                'C', "forge:plates/Permalloy"
        );

        for (int i = 0; i < spaceElementList.size();i++) {
            String s = spaceElementList.get(i);
            Recipes.recipe.addRecipe(   IUItem.preciousblock.getItem(3+i), "AAA", "AAA",
                    "AAA",
                    ('A'), "forge:ingots/" + s
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
                ('A'), "forge:plates/Steel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.lightning_rod, 1, 4), "AAA", " A ",
                " A ",
                ('A'), "forge:plates/Steel"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.lightning_rod, 1, 1), " A ", "ABA",
                " A ",
                ('A'), "forge:casings/Steel", 'B', Blocks.BRICKS
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.lightning_rod, 1, 5), " A ", " A ",
                "ABA",
                ('A'), "forge:plates/Steel", 'B', ItemStackHelper.fromData(IUItem.lightning_rod, 1, 1)
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.lightning_rod, 1, 2), "ACA", "ACA",
                "CBC",
                ('A'), "forge:plates/Steel", 'B', ItemStackHelper.fromData(IUItem.lightning_rod, 1, 1), 'C', IUItem.charged_redstone
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.lightning_rod, 1, 0),
                "",
                "ACA",
                "DBD",
                ('A'),
                "forge:plates/Steel",
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
                ('A'), "forge:plates/Platinum", 'B', IUItem.copperCableItem
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.tomeResearch), "   ", "ABA",
                " C ",
                ('A'), "forge:plates/Meteoric", 'B', Items.BOOK, 'C', "forge:gems/Ruby"
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.pollutionDevice), "AAA", "ABA",
                " C ",
                ('A'), "forge:plates/Iron", 'B', TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2), 'C', "forge:gems/Americium"
        );

    Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ruby_axe), "AA", "AB ",
                " B ",
                ('A'), "forge:gems/Ruby", 'B', Items.STICK
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.sapphire_axe), "AA", "AB ",
                " B ",
                ('A'), "forge:gems/Sapphire", 'B', Items.STICK
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.topaz_axe), "AA", "AB ",
                " B ",
                ('A'), "forge:gems/Topaz", 'B', Items.STICK
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ruby_pickaxe), "AAA", " B ",
                " B ",
                ('A'), "forge:gems/Ruby", 'B', Items.STICK
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.sapphire_pickaxe), "AAA", " B ",
                " B ",
                ('A'), "forge:gems/Sapphire", 'B', Items.STICK
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.topaz_pickaxe), "AAA", " B ",
                " B ",
                ('A'), "forge:gems/Topaz", 'B', Items.STICK
        );

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.ruby_shovel), " A ", " B ",
                " B ",
                ('A'), "forge:gems/Ruby", 'B', Items.STICK
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.sapphire_shovel), " A ", " B ",
                " B ",
                ('A'), "forge:gems/Sapphire", 'B', Items.STICK
        );
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.topaz_shovel), " A ", " B ",
                " B ",
                ('A'), "forge:gems/Topaz", 'B', Items.STICK
        );

        Recipes.recipe.addRecipe(getBlockStack(BlockBaseMachine3.steam_generator), " A ", " B ",
                " C ",
                ('A'), getBlockStack(BlockBaseMachine3.steam_converter), 'B', ItemStackHelper.fromData(IUItem.blockResource, 1, 8),
                'C', ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276)
        );
        Recipes.recipe.addShapelessRecipe(new ItemStack(IUItem.book.getItem()),
                Items.BOOK,
               Items.COPPER_INGOT
        );
        for (int i = 0; i < BlockRaws.Type.values().length; i++) {
            Recipes.recipe.addRecipe(ItemStackHelper.fromData(IUItem.rawsBlock.getItem(i),1), "AAA", "AAA", "AAA",

                    ('A'),"forge:raw_materials/"+BlockRaws.Type.values()[i].name()
            );
            for (int ii = 0; ii < ItemRawMetals.Types.values().length; ii++) {
                if (IUItem.rawMetals.getRegistryObject(ii) != null && IUItem.rawMetals.getStack(ii).getTags()[0].equals("forge:raw_materials/"+BlockRaws.Type.values()[i].name())){
                    Recipes.recipe.addShapelessRecipe(ItemStackHelper.fromData(IUItem.rawMetals.getStack(ii),9), ItemStackHelper.fromData(IUItem.rawsBlock.getItem(i),1));
                    break;
                }
            }
         }
        Recipes.recipe.addShapelessRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 4, 773),

                Items.CLAY_BALL, Items.CLAY_BALL, "forge:nuggets/spinel","forge:nuggets/spinel",
                "forge:nuggets/yttrium", "forge:nuggets/yttrium",Blocks.SAND, Blocks.GRAVEL
        );
        Recipes.recipe.addShapelessRecipe(ItemStackHelper.fromData(IUItem.crafting_elements, 4, 770),

                Items.CLAY_BALL, Items.CLAY_BALL, "forge:ingots/magnesium","forge:ingots/magnesium",Blocks.SAND, Blocks.GRAVEL
        );
        Recipes.recipe.addRecipe(IUItem.pipette.getItem(1), "  A", "BA ",
                "CCB",
                ('A'), ItemStackHelper.fromData(IUItem.blockResource, 1, 13), 'B', "forge:plates/yttrium",
                'C', "forge:plates/spinel"
        );
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.blockRubberWoods.getItem(0),4), ItemStackHelper.fromData(IUItem.rubWood, 1));
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.blockRubberWoods.getItem(1),4), ItemStackHelper.fromData(IUItem.swampRubWood, 1));
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.blockRubberWoods.getItem(2),4), ItemStackHelper.fromData(IUItem.tropicalRubWood, 1));

        Recipes.recipe.addRecipe(IUItem.smallFluidCell.getItem(1), " A ", "ABA",
                " A ",
                ('A'),"forge:casings/copper", 'B', Blocks.GLASS_PANE
        );
        Recipes.recipe.addRecipe(IUItem.reinforcedFluidCell.getItem(1), " A ", "ABA",
                " A ",
                ('A'),"forge:casings/tungsten", 'B', Blocks.GLASS_PANE
        );
        Recipes.recipe.addRecipe(IUItem.latexPipette, "  A", "DB ",
                "CD ",
                ('A'),"forge:casings/steel", 'B', Blocks.GLASS_PANE,'C', IUItem.treetap,'D',"forge:casings/cobalt"
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
