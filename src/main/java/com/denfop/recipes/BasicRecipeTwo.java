package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
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

    public static void recipe() {
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 101), "CCC", "BAB", "DED",

                ('A'), DEFAULT_SENSOR,

                ('C'), "gearGermanium",

                ('B'), "doubleplateGermanium",

                ('D'),
                "gearNichrome",

                ('E'), IUItem.compressIridiumplate
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
                IUItem.compressIridiumplate
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
                IUItem.compressIridiumplate
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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 154), "CCC", "BAB", "   ",

                ('A'), DEFAULT_SENSOR,

                ('C'), IUItem.FluidCell,

                ('B'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 155), "CCC", "BAB", "   ",

                ('A'), DEFAULT_SENSOR,

                ('C'),
                ModUtils.getCellFromFluid("ic2coolant"),
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
                IUItem.toriy,

                ('E'), "plateBronze"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 174), "CCC", "BAB", "EDE",

                ('A'), IMP_SENSOR,

                ('B'), IUItem.advancedAlloy,

                ('C'), IUItem.iridiumPlate,

                ('D'),
                IUItem.toriy,

                ('E'), "plateBronze"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 175), "CCC", "BAB", "EDE",

                ('A'), PER_SENSOR,

                ('B'), IUItem.advancedAlloy,

                ('C'), IUItem.iridiumPlate,

                ('D'),
                IUItem.toriy,

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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 205), "CCC", "BAB", "D D",
                ('A'), DEFAULT_SENSOR,
                ('B'), "gearCobalt", ('C'), IUItem.advancedAlloy, ('D'),
                "plateTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 204), "CCC", "BAB", "D D",
                ('A'), ADV_SENSOR,
                ('B'), "gearCobalt", ('C'), IUItem.advancedAlloy, ('D'),
                "plateTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 202), "CCC", "BAB", "D D",
                ('A'), IMP_SENSOR,
                ('B'), "gearCobalt", ('C'), IUItem.advancedAlloy, ('D'),
                "plateTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 203), "CCC", "BAB", "D D",
                ('A'), PER_SENSOR,
                ('B'), "gearCobalt", ('C'), IUItem.advancedAlloy, ('D'),
                "plateTitanium"
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
        int i;
        for (i = 0; i < 11; i++) {
            Recipes.recipe.addShapelessRecipe(
                    new ItemStack(IUItem.universal_cable, 1, i),
                    new ItemStack(IUItem.heatcold_pipes, 1, 4), new ItemStack(
                            IUItem.cable,
                            1,
                            i
                    ), new ItemStack(IUItem.expcable), new ItemStack(IUItem.scable), new ItemStack(IUItem.qcable)
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

                ('C'), IUItem.compressIridiumplate,

                ('A'), IUItem.iridiumPlate,

                ('B'), new ItemStack(IUItem.water_rotor_carbon, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewater, 1, 6), "CAC", "ABA", "CAC",

                ('C'), IUItem.doublecompressIridiumplate,

                ('A'), new ItemStack(IUItem.compresscarbon),

                ('B'), new ItemStack(IUItem.water_iridium
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewater, 1, 7), "DCD", "ABA", "DCD",

                ('D'), IUItem.circuitSpectral,

                ('C'), new ItemStack(IUItem.compressIridiumplate),

                ('A'), new ItemStack(IUItem.advnanobox),

                ('B'),
                new ItemStack(IUItem.water_compressiridium
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewater, 1, 8), "DCD", "ABA", " C ",

                ('D'), new ItemStack(IUItem.excitednucleus, 1, 5),

                ('C'), IUItem.cirsuitQuantum,

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

                ('C'), IUItem.cirsuitQuantum,

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

                ('C'), IUItem.circuitSpectral,

                ('B'), new ItemStack(IUItem.water_barionrotor
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewater, 1, 13), "ECE", "CBC", "ACA",

                ('E'), IUItem.circuitSpectral,

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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 4, 0), "BBB", "CAC", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateTin",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 4, 1), "BBB", "CAC", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateTin",

                ('C'), new ItemStack(Items.DYE, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 4, 2), "BBB", "CAC", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateCobalt",

                ('C'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.item_pipes, 4, 3), "BBB", "CAC", "BBB",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 122),

                ('B'), "plateCobalt",

                ('C'), new ItemStack(Items.DYE, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.simplemachine, 1, 0), " B ", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 72), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 69)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.simplemachine, 1, 1), " B ", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 72), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 63)
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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.simplemachine, 1, 3), " B ", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 72), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 159)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.simplemachine, 1, 5), " B ", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 72), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 33)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base2, 1, 0), " B ", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 72), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 165)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base2, 1, 4), " B ", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 72), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 163)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base2, 1, 8), " B ", "DAE", " C ",

                ('A'), IUItem.machine, ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 72), ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 44),
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 132)
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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base, 1, 0), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 2),
                ('B'), new ItemStack(IUItem.simplemachine, 1, 0)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base, 1, 1), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 78),
                ('B'), new ItemStack(IUItem.machines_base, 1, 0)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base, 1, 2), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 103),
                ('B'), new ItemStack(IUItem.machines_base, 1, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base, 1, 6), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 225),
                ('B'), new ItemStack(IUItem.simplemachine, 1, 2)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base, 1, 7), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 221),
                ('B'), new ItemStack(IUItem.machines_base, 1, 6)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base, 1, 8), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 223),
                ('B'), new ItemStack(IUItem.machines_base, 1, 7)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base, 1, 9), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 160),
                ('B'), new ItemStack(IUItem.simplemachine, 1, 3)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base, 1, 10), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 161),
                ('B'), new ItemStack(IUItem.machines_base, 1, 9)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base, 1, 11), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 162),
                ('B'), new ItemStack(IUItem.machines_base, 1, 10)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base, 1, 3), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 1),
                ('B'), new ItemStack(IUItem.simplemachine, 1, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base, 1, 4), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 77),
                ('B'), new ItemStack(IUItem.machines_base, 1, 3)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base, 1, 5), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 102),
                ('B'), new ItemStack(IUItem.machines_base, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base1, 1, 0), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 9),
                ('B'), new ItemStack(IUItem.simplemachine, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base1, 1, 1), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 86),
                ('B'), new ItemStack(IUItem.machines_base1, 1, 0)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base1, 1, 2), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 110),
                ('B'), new ItemStack(IUItem.machines_base1, 1, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base2, 1, 1), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 166),
                ('B'), new ItemStack(IUItem.machines_base2, 1, 0)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base2, 1, 2), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 167),
                ('B'), new ItemStack(IUItem.machines_base2, 1, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base2, 1, 3), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 168),
                ('B'), new ItemStack(IUItem.machines_base2, 1, 2)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base2, 1, 5), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 125),
                ('B'), new ItemStack(IUItem.machines_base2, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base2, 1, 6), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 126),
                ('B'), new ItemStack(IUItem.machines_base2, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base2, 1, 7), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 127),
                ('B'), new ItemStack(IUItem.machines_base2, 1, 6)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base2, 1, 9), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 133),
                ('B'), new ItemStack(IUItem.machines_base2, 1, 8)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base2, 1, 10), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 134),
                ('B'), new ItemStack(IUItem.machines_base2, 1, 9)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base2, 1, 11), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 136),
                ('B'), new ItemStack(IUItem.machines_base2, 1, 10)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 17), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 224),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 16)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 18), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 220),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 17)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 19), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 222),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 18)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 1), "AGA", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 19),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 0),
                ('G'), new ItemStack(IUItem.crafting_elements, 1, 18)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 2), "AGA", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 95),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 1),
                ('G'), new ItemStack(IUItem.crafting_elements, 1, 94)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 3), "AGA", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 119),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 2),
                ('G'), new ItemStack(IUItem.crafting_elements, 1, 118)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 5), "AGA", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 9),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 4),
                ('G'), new ItemStack(IUItem.crafting_elements, 1, 129)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 6), "AGA", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 86),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 5),
                ('G'), new ItemStack(IUItem.crafting_elements, 1, 130)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 7), "AGA", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 110),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 6),
                ('G'), new ItemStack(IUItem.crafting_elements, 1, 131)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 13), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 15),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 12)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 14), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 91),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 13)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 15), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 115),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 14)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 9), "AGA", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 12),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 8),
                ('G'), new ItemStack(IUItem.crafting_elements, 1, 6)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 10), "AGA", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 87),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 9),
                ('G'), new ItemStack(IUItem.crafting_elements, 1, 83)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base3, 1, 11), "AGA", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 111),
                ('B'), new ItemStack(IUItem.machines_base3, 1, 10),
                ('G'), new ItemStack(IUItem.crafting_elements, 1, 107)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base1, 1, 3), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 135),
                ('B'), new ItemStack(IUItem.machines_base1, 1, 2)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base1, 1, 4), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 146),
                ('B'), new ItemStack(IUItem.machines_base1, 1, 3)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base1, 1, 5), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 157),
                ('B'), new ItemStack(IUItem.machines_base1, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base1, 1, 6), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 137),
                ('C'), IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 44),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 124),
                ('B'), new ItemStack(IUItem.machines_base, 1, 2)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base1, 1, 7), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 138),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 20),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 135),
                ('B'), new ItemStack(IUItem.machines_base1, 1, 6)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base1, 1, 8), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 139),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 96),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 49),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 146),
                ('B'), new ItemStack(IUItem.machines_base1, 1, 7)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines_base1, 1, 9), "A A", "DBE", "ACA",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 140),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 120),
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 51),
                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 157),
                ('B'), new ItemStack(IUItem.machines_base1, 1, 8)
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

                ('C'), IUItem.elemotor,

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

                ('D'), IUItem.elemotor,

                ('C'), IUItem.machine,

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 11),

                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 72)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine, 1, 12), " D ", "BAC", " E ",

                ('A'), IUItem.machine, ('B'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        70
                ),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 44), ('E'),
                IUItem.elemotor,
                ('D'), new ItemStack(IUItem.crafting_elements, 1, 205)
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

                ('D'), IUItem.advancedCircuit,

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

                ('E'), IUItem.electronicCircuit,

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

                ('B'), IUItem.advancedCircuit
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.upgradeblock), " E ", "CAD", " B ",

                ('D'), new ItemStack(IUItem.crafting_elements, 1, 66),

                ('B'), IUItem.cirsuitQuantum,

                ('A'), IUItem.advancedMachine,

                ('C'),
                new ItemStack(IUItem.crafting_elements, 1, 47),
                ('E'), IUItem.elemotor
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 226), "BBB", "CAC", "DED",

                ('D'),
                ("plateAlumel"),
                ('E'), IUItem.cirsuitQuantum,

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

                ('A'), DEFAULT_SENSOR, ('B'), ModUtils.getCellFromFluid("ic2uu_matter")
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
                getBlockStack(BlockBaseMachine3.pump_iu), " E ", "CAD", " B ",

                ('A'), IUItem.machine,

                ('B'), IUItem.elemotor,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 154),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 241)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine1, 1, 6), "F F", "CAE", "FBF",

                ('A'),
                getBlockStack(BlockBaseMachine3.pump_iu),
                ('B'), new ItemStack(IUItem.crafting_elements, 1, 20),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 6),

                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 248),
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine1, 1, 7), "F F", "CAE", "FBF",

                ('A'), new ItemStack(IUItem.basemachine1, 1, 6),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 96),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 83),

                ('E'),
                new ItemStack(IUItem.crafting_elements, 1, 245),
                ('F'), new ItemStack(IUItem.crafting_elements, 1, 139)
        );
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

                ('A'), new ItemStack(IUItem.cell_all),

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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.oiladvrefiner), " A ", " B ", " C ",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 9),

                ('B'), IUItem.oilrefiner,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 20)
        );
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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine1, 1, 15), " E ", "CAD", " B ",

                ('A'), IUItem.machine,

                ('B'), IUItem.elemotor,

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 27),

                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 154),

                ('E'), new ItemStack(IUItem.crafting_elements, 1, 244)
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines, 1, 10), "BAB", " D ", "B B",

                ('D'),
                getBlockStack(BlockBaseMachine3.generator_iu),
                ('A'), new ItemStack(IUItem.crafting_elements, 1, 234),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines, 1, 11), "BAB", " D ", "B B",

                ('D'), new ItemStack(IUItem.machines, 1, 10),

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 232),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines, 1, 12), "BAB", " D ", "B B",

                ('D'), new ItemStack(IUItem.machines, 1, 11),

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 233),

                ('B'), new ItemStack(IUItem.crafting_elements, 1, 140)
        );
        Recipes.recipe.addRecipe(
                getBlockStack(BlockBaseMachine3.geogenerator_iu), "   ", "ABC", "   ",

                ('B'), IUItem.machine,

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 11),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 22)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine, 1, 4), "B B", "ADC", "B B",

                ('D'),
                getBlockStack(BlockBaseMachine3.geogenerator_iu),
                ('A'), new ItemStack(IUItem.crafting_elements, 1, 234),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 5),

                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine, 1, 5), "B B", "ADC", "B B",

                ('D'), new ItemStack(IUItem.basemachine, 1, 4),

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 232),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 82),

                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine, 1, 6), "B B", "ADC", "B B",

                ('D'), new ItemStack(IUItem.basemachine, 1, 5),

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 233),

                ('C'), new ItemStack(IUItem.crafting_elements, 1, 106),

                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 140)
        );
        Recipes.recipe.addRecipe(
                getBlockStack(BlockBaseMachine3.solar_iu), "ABA", "BDB", " E ",

                ('E'), getBlockStack(BlockBaseMachine3.generator_iu), ('D'), new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        37
                ),
                ('A'), "coalDust", ('B'),
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
                ('X'), new ItemStack(IUItem.tranformer, 1, 10), ('A'), IUItem.advancedCircuit,
                ('B'),
                new ItemStack(IUItem.crafting_elements, 1, 75)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blockdoublemolecular), "BDB", "CAC", "BEB",

                ('C'), IUItem.cirsuitQuantum,

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
                IUItem.cirsuitQuantum,

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

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 251), "BCB", "BAB", "BCB",

                ('A'), DEFAULT_SENSOR,
                ('B'), ModUtils.getCellFromFluid("iufluidneft"), ('C'), IUItem.advancedCircuit
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

                ('A'), IUItem.electronicCircuit,
                ('B'), "plateIron", ('C'), Items.REDSTONE, ('D'),
                "plateChromium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 169), "CED", "ABA", "CFD",

                ('A'), IUItem.circuitNano,

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

                ('C'), new ItemStack(IUItem.reBattery, 1, 32767),
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
        Recipes.recipe.addShapelessRecipe(
                getBlockStack(BlockBaseMachine3.redstone_generator),
                new ItemStack(IUItem.crafting_elements, 1, 258), IUItem.machine
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

                ('D'), IUItem.electronicCircuit,

                ('C'),
                new ItemStack(IUItem.advBattery, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 209), " A ", "DBC", " A ",

                ('B'), DEFAULT_SENSOR,

                ('A'), IUItem.insulatedIronCableItem,

                ('D'), IUItem.advancedCircuit,

                ('C'),
                new ItemStack(IUItem.lapotron_crystal, 1, 32767)
        );
        for (i = 0; i < 3; i++) {
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
                IUItem.electronicCircuit
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

                ('C'), IUItem.circuitSpectral,

                ('E'),
                ("plateTitanium"),
                ('A'),
                new ItemStack(IUItem.core, 1, 12),

                ('B'), PER_SENSOR
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 250), "EDE", "BAB", "CCC",

                ('E'), IUItem.cirsuitQuantum,

                ('D'), DEFAULT_SENSOR,

                ('C'), new ItemStack(IUItem.compressIridiumplate),

                ('B'),
                new ItemStack(IUItem.advQuantumtool),

                ('A'), new ItemStack(IUItem.core, 1, 7)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines, 1, 13), "C C", " A ", "CBC",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 257),
                ('B'), new ItemStack(IUItem.machines, 1, 8),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines, 1, 14), "C C", " A ", "CBC",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 253),
                ('B'), new ItemStack(IUItem.machines, 1, 13),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines, 1, 15), "C C", " A ", "CBC",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 255),
                ('B'), new ItemStack(IUItem.machines, 1, 14),
                ('C'), new ItemStack(IUItem.crafting_elements, 1, 140)
        );
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

                ('C'), IUItem.circuitSpectral
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

                ('C'), IUItem.electronicCircuit,

                ('D'),
                "casingIron",

                ('E'), IUItem.elemotor
        );
        Recipes.recipe.addRecipe(IUItem.powerunitsmall, " BD", "ACE", " BD",

                ('A'), new ItemStack(IUItem.reBattery, 1, 32767),

                ('B'), IUItem.copperCableItem,

                ('C'), IUItem.electronicCircuit,

                ('D'),
                "casingIron",

                ('E'), IUItem.elemotor
        );
        Recipes.recipe.addRecipe(IUItem.reBattery
                , " A ", "CDC", "CDC",

                ('A'), IUItem.insulatedTinCableItem,

                ('C'), IUItem.casingtin,

                ('D'), Items.REDSTONE
        );
        Recipes.recipe.addRecipe(IUItem.advBattery
                , "ACA", "CDC", "CBC",

                ('A'), IUItem.insulatedCopperCableItem,

                ('C'), IUItem.casingbronze,

                ('D'), "dustSulfur",

                ('B'),
                "dustLead"
        );

        Recipes.recipe.addRecipe(IUItem.energyStorageUpgrade, "AAA", "BDB", "ACA",

                ('A'), "plankWood",

                ('C'), IUItem.electronicCircuit,

                ('D'), new ItemStack(IUItem.reBattery, 1, 32767),

                ('B'),
                IUItem.insulatedCopperCableItem
        );
        Recipes.recipe.addShapelessRecipe(
                new ItemStack(IUItem.frequency_transmitter),
                IUItem.insulatedCopperCableItem, IUItem.electronicCircuit
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
                IUItem.advancedCircuit
        );
        Recipes.recipe.addRecipe(
                getBlockStack(BlockBaseMachine3.teleporter_iu), " A ", " B ", "   ",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 268),

                ('B'), IUItem.advancedMachine
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 268), "AEA", "CBC", "ADA",

                ('A'), IUItem.advancedCircuit,

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
                IUItem.energyStorageUpgrade, new ItemStack(IUItem.advBattery, 1, 32767)
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

                ('A'), IUItem.electronicCircuit,
                ('B'), "plateIron", ('C'), new ItemStack(
                        IUItem.coolpipes,
                        1,
                        3
                ), ('D'),
                "doubleplateMikhail"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.heatsensor, 1), "CBC", "BDB", "BAB",

                ('A'), IUItem.electronicCircuit,
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
                , " A ", "BCB", "BDB",

                ('B'), IUItem.rubber,

                ('A'), new ItemStack(Items.DYE, 1, 14),

                ('C'), Blocks.GLASS,

                ('D'),
                Blocks.IRON_BARS
        );
        Recipes.recipe.addRecipe(IUItem.hazmat_chestplate
                , "B B", "BAB", "BAB",

                ('B'), IUItem.rubber,

                ('A'), new ItemStack(Items.DYE, 1, 14)
        );
        Recipes.recipe.addRecipe(IUItem.hazmat_leggings
                , "BAB", "B B", "B B",

                ('B'), IUItem.rubber,

                ('A'), new ItemStack(Items.DYE, 1, 14)
        );
        Recipes.recipe.addRecipe(
                IUItem.rubber_boots
                ,
                "B B", "B B", "BAB", ('B'), IUItem.rubber, ('A'), new ItemStack(
                        Items.DYE,
                        1,
                        14
                )
        );
        Recipes.recipe.addRecipe(
                IUItem.sprayer,
                "B  ", " B ", " AB", ('B'), "casingIron", ('A'), IUItem.cell_all
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
                " B ", "BBB", "BAB", ('B'), "plateIron", ('A'), IUItem.powerunit
        );
        Recipes.recipe.addRecipe(IUItem.diamond_drill
                , "   ", " B ", "BAB",

                ('B'), "gemDiamond",

                ('A'), new ItemStack(IUItem.drill, 1, 32767)
        );


        Recipes.recipe.addShapelessRecipe(
                IUItem.insulatedCopperCableItem,
                IUItem.copperCableItem, IUItem.rubber
        );
        Recipes.recipe.addShapelessRecipe(IUItem.insulatedGoldCableItem, IUItem.goldCableItem, IUItem.rubber);
        Recipes.recipe.addShapelessRecipe(IUItem.insulatedIronCableItem, IUItem.ironCableItem, IUItem.rubber);
        Recipes.recipe.addShapelessRecipe(IUItem.insulatedTinCableItem, IUItem.tinCableItem, IUItem.rubber);

        Recipes.recipe.addRecipe(IUItem.nightvision
                , " E ", "CDC", "ABA",

                ('A'), IUItem.rubber,

                ('B'), IUItem.advancedCircuit,

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
                ('C'), IUItem.advancedCircuit
        );
        Recipes.recipe.addRecipe(
                IUItem.lapotron_crystal
                ,
                "ACA", "ABA", "ACA", ('B'), new ItemStack(IUItem.energy_crystal, 1, 32767),

                ('A'), "dustLapis",
                ('C'), IUItem.advancedCircuit
        );
        Recipes.recipe.addRecipe(
                IUItem.charging_re_battery
                ,
                "ABA", "B B", "ABA", ('B'), new ItemStack(IUItem.reBattery, 1, 32767),

                ('A'), IUItem.electronicCircuit
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

                ('A'), IUItem.electronicCircuit, ('C'), "casingCopper"
        );
        Recipes.recipe.addRecipe(
                IUItem.batpack
                ,
                "BAB", "BCB", "B B", ('B'), new ItemStack(IUItem.reBattery, 1, 32767),

                ('A'), IUItem.electronicCircuit, ('C'), "plankWood"
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
                ('A'), Item.getItemFromBlock(Blocks.CACTUS)
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
        Recipes.recipe.addRecipe(IUItem.cell_all, " A ", "ABA", " A ",

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
        Recipes.recipe.addRecipe(IUItem.ejectorUpgrade, "A A", " B ", "A A",
                ('A'), "plateTin", ('B'), Blocks.PISTON
        );
        Recipes.recipe.addRecipe(IUItem.pullingUpgrade, "A A", " B ", "A A",
                ('A'), "plateTin", ('B'), Blocks.STICKY_PISTON
        );
        Recipes.recipe.addRecipe(IUItem.fluidEjectorUpgrade, "A A", " B ", "A A",
                ('A'), "plateTin", ('B'), IUItem.elemotor
        );
        Recipes.recipe.addRecipe(IUItem.fluidpullingUpgrade, "ACA", " B ", "A A",
                ('A'), "plateTin", ('B'), IUItem.elemotor, ('C'), new ItemStack(
                        IUItem.treetap,
                        1,
                        32767
                )
        );


        Recipes.recipe.addRecipe(IUItem.bronze_helmet
                , "AAA", "A A", "   ", ('A'), "ingotBronze");
        Recipes.recipe.addRecipe(IUItem.bronze_leggings
                , "AAA", "A A", "A A", ('A'), "ingotBronze");
        Recipes.recipe.addRecipe(IUItem.bronze_boots
                , "   ", "A A", "A A", ('A'), "ingotBronze");
        Recipes.recipe.addRecipe(IUItem.bronze_chestplate
                , "A A", "AAA", "AAA", ('A'), "ingotBronze");

        ItemStack topRod = new ItemStack(IUItem.crafting_elements, 1, 185);
        ItemStack bottomRod = new ItemStack(IUItem.crafting_elements, 1, 184);
        ItemStack leftRod = new ItemStack(IUItem.crafting_elements, 1, 186);
        ItemStack rightRod = new ItemStack(IUItem.crafting_elements, 1, 187);
        Recipes.recipe.addRecipe(IUItem.reactorprotonSimple, " A ", "DBE", " C ",

                ('A'), topRod,
                ('B'), IUItem.proton,
                ('C'), bottomRod,
                ('D'),
                leftRod,
                ('E'), rightRod
        );
        Recipes.recipe.addRecipe(IUItem.reactorcaliforniaSimple, " A ", "DBE", " C ",

                ('A'), topRod,
                ('B'), new ItemStack(IUItem.radiationresources, 1, 3),
                ('C'), bottomRod,
                ('D'),
                leftRod,
                ('E'), rightRod
        );
        Recipes.recipe.addRecipe(IUItem.reactorcuriumSimple, " A ", "DBE", " C ",

                ('A'), topRod,
                ('B'), new ItemStack(IUItem.radiationresources, 1, 2),
                ('C'), bottomRod,
                ('D'),
                leftRod,
                ('E'), rightRod
        );
        Recipes.recipe.addRecipe(IUItem.reactorneptuniumSimple, " A ", "DBE", " C ",

                ('A'), topRod,
                ('B'), new ItemStack(IUItem.radiationresources, 1, 1),
                ('C'), bottomRod,
                ('D'),
                leftRod,
                ('E'), rightRod
        );
        Recipes.recipe.addRecipe(IUItem.uranium_fuel_rod, " A ", "DBE", " C ",

                ('A'), topRod,
                ('B'), IUItem.UranFuel,
                ('C'), bottomRod,
                ('D'),
                leftRod,
                ('E'), rightRod
        );
        Recipes.recipe.addRecipe(IUItem.mox_fuel_rod, " A ", "DBE", " C ",

                ('A'), topRod,
                ('B'), IUItem.mox,
                ('C'), bottomRod,
                ('D'),
                leftRod,
                ('E'), rightRod
        );
        ItemStack topRod1 = new ItemStack(IUItem.crafting_elements, 1, 194);
        ItemStack bottomRod1 = new ItemStack(IUItem.crafting_elements, 1, 195);
        ItemStack leftRod1 = new ItemStack(IUItem.crafting_elements, 1, 193);
        ItemStack rightRod1 = new ItemStack(IUItem.crafting_elements, 1, 192);
        Recipes.recipe.addRecipe(IUItem.reactormendeleviumSimple, " A ", "DBE", " C ",

                ('A'), topRod1,
                ('B'), new ItemStack(IUItem.radiationresources, 1, 5),
                ('C'), bottomRod1,
                ('D'),
                leftRod1,
                ('E'), rightRod1
        );
        Recipes.recipe.addRecipe(IUItem.reactorberkeliumSimple, " A ", "DBE", " C ",

                ('A'), topRod1,
                ('B'), new ItemStack(IUItem.radiationresources, 1, 6),
                ('C'), bottomRod1,
                ('D'),
                leftRod1,
                ('E'), rightRod1
        );
        Recipes.recipe.addRecipe(IUItem.reactoreinsteiniumSimple, " A ", "DBE", " C ",

                ('A'), topRod1,
                ('B'), new ItemStack(IUItem.radiationresources, 1, 7),
                ('C'), bottomRod1,
                ('D'),
                leftRod1,
                ('E'), rightRod1
        );
        Recipes.recipe.addRecipe(IUItem.reactoruran233Simple, " A ", "DBE", " C ",

                ('A'), topRod1,
                ('B'), new ItemStack(IUItem.radiationresources, 1, 8),
                ('C'), bottomRod1,
                ('D'),
                leftRod1,
                ('E'), rightRod1
        );
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
        Recipes.recipe.addRecipe(IUItem.dual_uranium_fuel_rod, "SQS",
                ('S'), IUItem.uranium_fuel_rod,
                ('Q'), ("plateIron")
        );
        Recipes.recipe.addRecipe(IUItem.quad_uranium_fuel_rod, "SQS", "CQC", "SQS",

                ('S'), IUItem.uranium_fuel_rod,
                ('Q'), ("plateIron"), ('C'), (
                        "plateCopper")
        );
        Recipes.recipe.addRecipe(IUItem.quad_uranium_fuel_rod, "SQS",

                ('S'), IUItem.dual_uranium_fuel_rod, ('Q'),
                ("plateIron"), ('C'), ("plateCopper")
        );
        Recipes.recipe.addRecipe(IUItem.dual_mox_fuel_rod, "SQS",
                ('S'), IUItem.mox_fuel_rod,
                ('Q'), ("plateIron")
        );
        Recipes.recipe.addRecipe(IUItem.quad_mox_fuel_rod, "SQS", "CQC", "SQS",

                ('S'), IUItem.mox_fuel_rod,
                ('Q'), ("plateIron"), ('C'), (
                        "plateCopper")
        );
        Recipes.recipe.addRecipe(IUItem.quad_mox_fuel_rod, "SQS",

                ('S'), IUItem.dual_mox_fuel_rod, ('Q'),
                ("plateIron"), ('C'), ("plateCopper")
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
                IUItem.circuitNano,
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
                IUItem.circuitNano,
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
                IUItem.circuitNano,
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
                IUItem.circuitNano,
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
                IUItem.cirsuitQuantum,
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
                IUItem.cirsuitQuantum,
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
                IUItem.cirsuitQuantum,
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
                IUItem.cirsuitQuantum,
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
                IUItem.circuitSpectral,
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
                IUItem.circuitSpectral,
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
                IUItem.circuitSpectral,
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
                IUItem.circuitSpectral,
                ('C'),
                IUItem.AdvlapotronCrystal,
                ('D'),
                IUItem.doublecompressIridiumplate, 'E', IUItem.adv_spectral_box, 'F', IUItem.compressAlloy
        );
    }

    public static ItemStack getBlockStack(IMultiTileBlock block) {
        return TileBlockCreator.instance.get(block.getIdentifier()).getItemStack(block);
    }

}
