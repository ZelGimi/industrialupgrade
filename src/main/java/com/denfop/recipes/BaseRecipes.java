package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.integration.exnihilo.ExNihiloIntegration;
import com.denfop.register.RegisterOreDictionary;
import com.denfop.utils.ModUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

public class BaseRecipes {

    public static ItemStack DEFAULT_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 21);

    public static ItemStack ADV_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 25);

    public static ItemStack IMP_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 23);

    public static ItemStack PER_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 24);

    public static void init() {
        if (Loader.isModLoaded("exnihilocreatio")) {
            for (int n = 0; n < IUItem.name_mineral1.size(); n++) {
                if (n != 6 && n != 7 && n != 11) {
                    Recipes.recipe.addRecipe(
                            new ItemStack(ExNihiloIntegration.gravel, 1, n),
                            "AA ", "AA ", "   ",
                            ('A'), new ItemStack(ExNihiloIntegration.gravel_crushed, 1, n)
                    );
                    Recipes.recipe.addRecipe(new ItemStack(ExNihiloIntegration.dust, 1, n), "AA ", "AA ", "   ",
                            ('A'), new ItemStack(ExNihiloIntegration.dust_crushed, 1, n)
                    );
                    Recipes.recipe.addRecipe(new ItemStack(ExNihiloIntegration.sand, 1, n), "AA ", "AA ", "   ",
                            ('A'), new ItemStack(ExNihiloIntegration.sand_crushed, 1, n)
                    );
                }
            }
        }
        for (int i = 0; i < 19; i++) {
            Recipes.recipe.addShapelessRecipe(
                    new ItemStack(IUItem.stik, 2, i),
                    "craftingToolWireCutter", "ingot" + RegisterOreDictionary.list_string
                            .get(i)
            );
            Recipes.recipe.addShapelessRecipe(
                    new ItemStack(IUItem.plate, 1, i),
                    "craftingToolForgeHammer", "ingot" + RegisterOreDictionary.list_string
                            .get(i), "ingot" + RegisterOreDictionary.list_string
                            .get(i)
            );
            Recipes.recipe.addShapelessRecipe(
                    new ItemStack(IUItem.casing, 2, i),
                    "craftingToolForgeHammer", "plate" + RegisterOreDictionary.list_string
                            .get(i)
            );
        }
        Recipes.recipe.addShapelessRecipe(
                new ItemStack(IUItem.plate, 1, 28),
                "craftingToolForgeHammer", "ingotOsmium", "ingotOsmium"
        );
        Recipes.recipe.addShapelessRecipe(
                new ItemStack(IUItem.plate, 1, 29),
                "craftingToolForgeHammer", "ingotTantalum", "ingotTantalum"
        );
        Recipes.recipe.addShapelessRecipe(
                new ItemStack(IUItem.plate, 1, 30),
                "craftingToolForgeHammer", "ingotCadmium", "ingotCadmium"
        );
        Recipes.recipe.addShapelessRecipe(
                new ItemStack(IUItem.casing, 2, 26),
                "craftingToolForgeHammer", "plateOsmium"
        );
        Recipes.recipe.addShapelessRecipe(
                new ItemStack(IUItem.casing, 2, 27),
                "craftingToolForgeHammer", "plateTantalum"
        );
        Recipes.recipe.addShapelessRecipe(
                new ItemStack(IUItem.casing, 2, 28),
                "craftingToolForgeHammer", "plateCadmium"
        );

        Recipes.recipe.addShapelessRecipe(IUItem.plateiron, "craftingToolForgeHammer", "ingotIron", "ingotIron");
        Recipes.recipe.addShapelessRecipe(
                ModUtils.setSize(IUItem.casingiron, 2),
                "craftingToolForgeHammer", "plateIron"
        );
        Recipes.recipe.addShapelessRecipe(IUItem.plateadviron, "craftingToolForgeHammer", "ingotSteel", "ingotSteel");
        Recipes.recipe.addShapelessRecipe(
                ModUtils.setSize(IUItem.casingadviron, 2),
                "craftingToolForgeHammer", "plateSteel"
        );
        Recipes.recipe.addShapelessRecipe(IUItem.platecopper, "craftingToolForgeHammer", "ingotCopper", "ingotCopper");
        Recipes.recipe.addShapelessRecipe(
                ModUtils.setSize(IUItem.casingcopper, 2),
                "craftingToolForgeHammer", "plateCopper"
        );
        Recipes.recipe.addShapelessRecipe(IUItem.platetin, "craftingToolForgeHammer", "ingotTin", "ingotTin");
        Recipes.recipe.addShapelessRecipe(
                ModUtils.setSize(IUItem.casingtin, 2),
                "craftingToolForgeHammer", "plateTin"
        );
        Recipes.recipe.addShapelessRecipe(IUItem.platelead, "craftingToolForgeHammer", "ingotLead", "ingotLead");
        Recipes.recipe.addShapelessRecipe(
                ModUtils.setSize(IUItem.casinglead, 2),
                "craftingToolForgeHammer", "plateLead"
        );
        Recipes.recipe.addShapelessRecipe(IUItem.plategold, "craftingToolForgeHammer", "ingotGold", "ingotGold");
        Recipes.recipe.addShapelessRecipe(
                ModUtils.setSize(IUItem.casinggold, 2),
                "craftingToolForgeHammer", "plateGold"
        );
        Recipes.recipe.addShapelessRecipe(IUItem.platebronze, "craftingToolForgeHammer", "ingotBronze", "ingotBronze");
        Recipes.recipe.addShapelessRecipe(
                ModUtils.setSize(IUItem.casingbronze, 2),
                "craftingToolForgeHammer", "plateBronze"
        );
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
                new ItemStack(IUItem.block1,1,3),
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
                new ItemStack(IUItem.block1,1,4),
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
                new ItemStack(IUItem.block1,1,5),
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
                IUItem.insulatedCopperCableItem, ('C'), IUItem.electronicCircuit
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
                ('A'), ("plankWood"), ('C'), new ItemStack(IUItem.reBattery, 1, 32767),

                ('B'), IUItem.tinCableItem
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricblock, 1, 5), "ABA", "CCC", "AAA",

                ('A'),
                ("plateBronze"),
                ('C'), new ItemStack(IUItem.advBattery, 1, 32767),

                ('B'), IUItem.insulatedCopperCableItem
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricblock, 1, 3), "ABA", "BCB", "ABA",

                ('A'),
                ("doubleplateAluminumbronze"),
                ('C'), new ItemStack(IUItem.energy_crystal, 1, 32767),

                ('B'), IUItem.machine
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricblock, 1, 4), "CDC", "CAC", "CBC",

                ('D'), IUItem.circuitNano,

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

                ('C'), IUItem.cirsuitQuantum
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricblock, 1, 6), "CDC", "DAD", "CDC",

                ('D'), new ItemStack(IUItem.core, 1, 4),

                ('A'), new ItemStack(IUItem.electricblock, 1, 1),

                ('C'), IUItem.circuitSpectral
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricblock, 1, 7), "CDC", "DAD", "CDC",

                ('D'), new ItemStack(IUItem.core, 1, 8),

                ('A'), new ItemStack(IUItem.electricblock, 1, 6),

                ('C'), IUItem.circuitSpectral
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricblock, 1, 8), "CBC", "DAD", "CBC",

                ('D'), new ItemStack(IUItem.core, 1, 9),

                ('A'), new ItemStack(IUItem.electricblock, 1, 7),

                ('C'), IUItem.cirsuitQuantum,

                ('B'),
                new ItemStack(IUItem.AdvlapotronCrystal, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricblock, 1, 9), "EBE", "DAD", "CBC",

                ('D'), new ItemStack(IUItem.core, 1, 11),

                ('E'), new ItemStack(IUItem.compressIridiumplate),

                ('A'), new ItemStack(IUItem.electricblock, 1, 8),

                ('C'),
                IUItem.circuitSpectral,

                ('B'), new ItemStack(IUItem.AdvlapotronCrystal, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricblock, 1, 10), "EBE", "DAD", "CBC",

                ('D'), new ItemStack(IUItem.core, 1, 12),

                ('E'), new ItemStack(IUItem.doublecompressIridiumplate),

                ('A'), new ItemStack(IUItem.electricblock, 1, 9),

                ('C'),
                IUItem.circuitSpectral,

                ('B'), new ItemStack(IUItem.AdvlapotronCrystal, 1, 32767)
        );
        Recipes.recipe.addRecipe(IUItem.overclockerUpgrade_1, "C C", " A ", "C C",

                ('C'), IUItem.circuitNano, ('A'), IUItem.overclockerUpgrade
        );
        Recipes.recipe.addRecipe(IUItem.overclockerUpgrade1, "C C", " A ", "C C",

                ('C'), IUItem.cirsuitQuantum, ('A'), IUItem.overclockerUpgrade_1
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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nanoBox), " C ", "BAB", " C ",

                ('B'), IUItem.carbonPlate,

                ('C'), new ItemStack(IUItem.compresscarbon, 1),

                ('A'), new ItemStack(IUItem.energy_crystal, 1, 32767)
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
        Recipes.recipe.addRecipe(IUItem.tranformerUpgrade, "BCB", "CDC", "BCB",

                ('B'), IUItem.circuitNano,

                ('D'), IUItem.transformerUpgrade,

                ('C'),
                ("plateVitalium")
        );
        Recipes.recipe.addRecipe(IUItem.tranformerUpgrade1, "BCB", "CDC", "BCB",

                ('B'), IUItem.cirsuitQuantum,

                ('D'), IUItem.tranformerUpgrade,

                ('C'),
                ("plateAlcled")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nanopickaxe), "ACA", "CDC", "BFB",
                ('F'), new ItemStack(IUItem.energy_crystal, 1, 32767), ('B'),
                ("doubleplateFerromanganese"), ('D'), new ItemStack(Items.DIAMOND_PICKAXE), ('C'),
                IUItem.circuitNano,
                ('A'), IUItem.advnanobox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nanoaxe), "ACA", "CDC", "BFB",

                ('F'), new ItemStack(IUItem.energy_crystal, 1, 32767),

                ('B'),

                ("doubleplateFerromanganese"),
                ('D'), new ItemStack(Items.DIAMOND_AXE),

                ('C'),
                IUItem.circuitNano,
                ('A'), IUItem.advnanobox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nanoshovel), "ACA", "CDC", "BFB",

                ('F'), new ItemStack(IUItem.energy_crystal, 1, 32767),

                ('B'),

                ("doubleplateFerromanganese"),
                ('D'), new ItemStack(Items.DIAMOND_SHOVEL),

                ('C'),
                IUItem.circuitNano,
                ('A'), IUItem.advnanobox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.quantumpickaxe), "TCT", "CDC", "BFB",

                ('T'),
                ("doubleplateMuntsa"),
                ('F'), new ItemStack(IUItem.lapotron_crystal, 1, 32767),

                ('B'), new ItemStack(IUItem.advQuantumtool, 1),

                ('D'),
                new ItemStack(IUItem.nanopickaxe, 1, 32767),

                ('C'), IUItem.cirsuitQuantum
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.quantumaxe), "TCT", "CDC", "BFB",

                ('T'),
                ("doubleplateMuntsa"),
                ('F'), new ItemStack(IUItem.lapotron_crystal, 1, 32767),

                ('B'), new ItemStack(IUItem.advQuantumtool, 1),

                ('D'),
                new ItemStack(IUItem.nanoaxe, 1, 32767),

                ('C'), IUItem.cirsuitQuantum
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.quantumshovel), "TCT", "CDC", "BFB",

                ('T'),
                ("doubleplateMuntsa"),
                ('F'), new ItemStack(IUItem.lapotron_crystal, 1, 32767),

                ('B'), new ItemStack(IUItem.advQuantumtool, 1),

                ('D'),
                new ItemStack(IUItem.nanoshovel, 1, 32767),

                ('C'), IUItem.cirsuitQuantum
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spectralpickaxe), "TCT", "CDC", "BFB",

                ('T'), IUItem.iridiumPlate,

                ('F'), new ItemStack(IUItem.impBatChargeCrystal, 1, 32767),

                ('B'), new ItemStack(IUItem.adv_spectral_box, 1),

                ('D'),
                new ItemStack(IUItem.quantumpickaxe, 1, 32767),

                ('C'), IUItem.circuitSpectral
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spectralaxe), "TCT", "CDC", "BFB",

                ('T'), IUItem.iridiumPlate,

                ('F'), new ItemStack(IUItem.impBatChargeCrystal, 1, 32767),

                ('B'), new ItemStack(IUItem.adv_spectral_box, 1),

                ('D'),
                new ItemStack(IUItem.quantumaxe, 1, 32767),

                ('C'), IUItem.circuitSpectral
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spectralshovel), "TCT", "CDC", "BFB",

                ('T'), IUItem.iridiumPlate,

                ('F'), new ItemStack(IUItem.impBatChargeCrystal, 1, 32767),

                ('B'), new ItemStack(IUItem.adv_spectral_box, 1),

                ('D'),
                new ItemStack(IUItem.quantumshovel, 1, 32767),

                ('C'), IUItem.circuitSpectral
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.impBatChargeCrystal), "BCB", "BAB", "BCB",

                ('B'), new ItemStack(IUItem.AdvlapotronCrystal, 1, 32767),

                ('A'), new ItemStack(IUItem.charging_lapotron_crystal, 1, 32767),

                ('C'), IUItem.cirsuitQuantum
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.perBatChargeCrystal), "DCD", "BAB", "ECE",

                ('E'),
                ("doubleplateVitalium"),
                ('D'), IUItem.iridiumPlate,

                ('B'), new ItemStack(IUItem.AdvlapotronCrystal, 1, 32767),

                ('A'),
                new ItemStack(IUItem.impBatChargeCrystal, 1, 32767),

                ('C'), IUItem.circuitSpectral
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.perfect_drill), "ACB", "FDF", "ECE",

                ('E'), new ItemStack(IUItem.advQuantumtool),

                ('F'), IUItem.overclockerUpgrade1,

                ('A'), new ItemStack(IUItem.spectralaxe, 1, 32767),

                ('B'),
                new ItemStack(IUItem.spectralshovel, 1, 32767),

                ('D'), new ItemStack(IUItem.spectralpickaxe, 1, 32767),

                ('C'), IUItem.circuitSpectral
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.quantumSaber), "AB ", "AC ", "DEB",
                ('C'), new ItemStack(IUItem.nanosaber, 1, 32767), ('E'), new ItemStack(IUItem.lapotron_crystal, 1, 32767),
                ('D'), new ItemStack(Blocks.GLOWSTONE),
                ('B'),
                IUItem.cirsuitQuantum, ('A'), new ItemStack(IUItem.compresscarbon)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spectralSaber), "AB ", "AC ", "DEB",

                ('C'), new ItemStack(IUItem.quantumSaber, 1, 32767),

                ('E'), new ItemStack(IUItem.AdvlapotronCrystal, 1, 32767),

                ('D'), new ItemStack(Blocks.GLOWSTONE),

                ('B'),
                IUItem.circuitSpectral,

                ('A'), new ItemStack(IUItem.compressIridiumplate)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.GraviTool), "ABA", "CDE", "FGF",

                ('G'), new ItemStack(IUItem.purifier, 1, 32767),

                ('F'), IUItem.circuitNano,

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
                ('A'), new ItemStack(IUItem.powerunitsmall.getItem(), 1, 11), ('B'), new ItemStack(Blocks.WOOL)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.itemiu, 1, 3), "MDM", "M M", "MDM",
                ('D'), IUItem.advancedCircuit, ('M'), new ItemStack(
                        IUItem.itemiu,
                        1,
                        1
                )
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nano_bow), "C C", "BAB", "EDE",
                ('E'), IUItem.advnanobox,
                ('D'), new ItemStack(IUItem.reBattery, 1, 32767), ('C'), IUItem.circuitNano,

                ('B'),
                IUItem.carbonPlate,
                ('A'), new ItemStack(Items.BOW)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.quantum_bow), "ABA", "CDC", "EBE",

                ('E'),
                ("doubleplateAlcled"),
                ('C'), IUItem.cirsuitQuantum,

                ('D'), new ItemStack(IUItem.nano_bow, 1, 32767),

                ('A'),
                IUItem.iridiumPlate,

                ('B'), new ItemStack(IUItem.advQuantumtool)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spectral_bow), "ABA", "CDC", "EBE",

                ('E'),
                ("doubleplateDuralumin"),
                ('C'), IUItem.circuitSpectral,

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
        Recipes.recipe.addRecipe(IUItem.cfPowder, "A", " A", " BC", ('A'), IUItem.casingiron,
                ('B'), IUItem.cell_all
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.electricJetpack), "ADA", "ACA", "B B", ('A'), IUItem.casingiron,
                ('B'), Items.GLOWSTONE_DUST, ('C'), new ItemStack(IUItem.electricblock, 1, 2), ('D'),
                IUItem.advancedCircuit
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module7, 1, 1), "ABA", "BCB", "DDD",

                ('D'), new ItemStack(IUItem.doublecompressIridiumplate),

                ('C'), new ItemStack(IUItem.advQuantumtool),

                ('B'), IUItem.circuitSpectral,

                ('A'),
                new ItemStack(IUItem.core, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module7, 1, 2), "ABA", "BCB", "DDD",

                ('D'), new ItemStack(IUItem.doublecompressIridiumplate),

                ('C'), new ItemStack(IUItem.advnanobox),

                ('B'), IUItem.circuitSpectral,

                ('A'),
                new ItemStack(IUItem.core, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module7, 1, 3), "AAA", "BCB", "EFE",

                ('F'),
                ("doubleplateAlcled"),
                ('E'), IUItem.circuitSpectral,

                ('C'), new ItemStack(IUItem.module_schedule),

                ('B'),
                ("doubleplateVitalium"),
                ('A'), new ItemStack(IUItem.compresscarbon)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module7, 1, 4), "AAA", "BCB", "EFE",

                ('F'),
                ("doubleplateDuralumin"),
                ('E'), IUItem.circuitNano,

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

                ('E'), IUItem.cirsuitQuantum,

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

                ('E'), IUItem.circuitNano,

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

                ('E'), IUItem.circuitSpectral,

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
                IUItem.circuitNano,

                ('A'),
                ("plateZinc")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module7, 1, 9), "ABA", "BCB", "ABA",

                ('C'), new ItemStack(IUItem.purifier, 1, 32767),

                ('B'), new ItemStack(IUItem.nanoBox),

                ('A'), IUItem.advancedCircuit
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 9), "ABA", "BCB", "ABA",

                ('C'), IUItem.circuitSpectral,

                ('B'), new ItemStack(IUItem.photoniy, 1),

                ('A'), new ItemStack(IUItem.core, 1, 3)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 10), "ABA", "BCB", "ABA",

                ('C'), IUItem.circuitSpectral,

                ('B'), new ItemStack(IUItem.photoniy_ingot, 1),

                ('A'), new ItemStack(IUItem.module9, 1, 9)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 11), "ABA", "BCB", "ABA",

                ('C'), IUItem.circuitSpectral,

                ('B'), new ItemStack(IUItem.photoniy_ingot, 1),

                ('A'), new ItemStack(IUItem.module9, 1, 10)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 12), "ABA", "BCB", "ABA",
                ('C'), IUItem.circuitSpectral, ('B'), new ItemStack(
                        Blocks.REDSTONE_BLOCK,
                        1
                ), ('A'), new ItemStack(Items.PAPER, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 6), "ABA", "BCB", "ABA",

                ('C'), IUItem.circuitSpectral,

                ('B'), new ItemStack(IUItem.photoniy, 1),

                ('A'), new ItemStack(IUItem.core, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 7), "ABA", "DCD", "ABA",

                ('D'), new ItemStack(IUItem.module9, 1, 6),

                ('C'), IUItem.circuitSpectral,

                ('B'), new ItemStack(IUItem.photoniy, 1),

                ('A'),
                new ItemStack(IUItem.core, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 8), "ABA", "DCD", "ABA",

                ('D'), new ItemStack(IUItem.module9, 1, 7),

                ('C'), IUItem.circuitSpectral,

                ('B'), new ItemStack(IUItem.doublecompressIridiumplate, 1),

                ('A'),
                new ItemStack(IUItem.core, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.module9, 1, 13), "A A", " C ", "A A",
                ('C'), IUItem.circuitSpectral, ('A'), new ItemStack(Items.PAPER, 1)
        );
        Recipes.recipe.addRecipe(IUItem.module1, "AAA", "BCB", "EDE",

                ('E'), new ItemStack(IUItem.plastic_plate),

                ('D'),
                ("doubleplateVitalium"),
                ('C'), IUItem.cirsuitQuantum,

                ('B'),
                ("plateCobalt"),
                ('A'),
                ("plateElectrum")
        );
        Recipes.recipe.addRecipe(IUItem.module2, "AAA", "BCB", "EDE",

                ('E'), new ItemStack(IUItem.plastic_plate),

                ('D'),
                ("doubleplateVitalium"),
                ('C'), IUItem.cirsuitQuantum,

                ('B'),
                ("doubleplateRedbrass"),
                ('A'),
                ("doubleplateFerromanganese")
        );
        Recipes.recipe.addRecipe(IUItem.module3, "AAA", "BCB", "EDE",

                ('E'), new ItemStack(IUItem.plastic_plate),

                ('D'),
                ("doubleplateVitalium"),
                ('C'), IUItem.cirsuitQuantum,

                ('B'),
                ("doubleplateAlumel"),
                ('A'),
                ("plateFerromanganese")
        );
        Recipes.recipe.addRecipe(IUItem.module4, "AAA", "BCB", "EDE",

                ('E'), new ItemStack(IUItem.plastic_plate),

                ('D'),
                ("doubleplateVitalium"),
                ('C'), IUItem.cirsuitQuantum,

                ('B'),
                ("doubleplateMuntsa"),
                ('A'), new ItemStack(IUItem.doublecompressIridiumplate, 1)
        );
        for (j = 0; j < 7; j++) {
            Recipes.recipe.addRecipe(new ItemStack(IUItem.module5, 1, j), "BBB", "ACA", "ADA",

                    ('A'), new ItemStack(IUItem.lens, 1, j),

                    ('B'), new ItemStack(IUItem.doublecompressIridiumplate),

                    ('C'), IUItem.circuitSpectral,

                    ('D'),
                    new ItemStack(IUItem.advQuantumtool)
            );
        }
        for (j = 0; j < 14; j++) {
            Recipes.recipe.addShapelessRecipe(
                    new ItemStack(IUItem.module6, 1, j),
                    new ItemStack(IUItem.blockpanel, 1, j)
            );
            ItemStack stack1 = new ItemStack(IUItem.module6, 1, j);
            Recipes.recipe.addShapelessRecipe(new ItemStack(IUItem.blockpanel, 1, j), stack1);
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
            Recipes.recipe.addRecipe(new ItemStack(IUItem.smalldust, 1, i),
                    "AAA", "AAA", "AAA", 'A', new ItemStack(IUItem.verysmalldust, 1, i)
            );
            Recipes.recipe.addRecipe(new ItemStack(IUItem.iudust, 1, i),
                    "AAA", "AAA", "AAA", 'A', new ItemStack(IUItem.smalldust, 1, i)
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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.smalldust, 1, 29),
                "AAA", "AAA", "AAA", 'A', new ItemStack(IUItem.verysmalldust, 1, 19)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.iudust, 1, 34),
                "AAA", "AAA", "AAA", 'A', new ItemStack(IUItem.smalldust, 1, 29)
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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.smalldust, 1, 30),
                "AAA", "AAA", "AAA", 'A', new ItemStack(IUItem.verysmalldust, 1, 20)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.iudust, 1, 35),
                "AAA", "AAA", "AAA", 'A', new ItemStack(IUItem.smalldust, 1, 30)
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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.smalldust, 1, 31),
                "AAA", "AAA", "AAA", 'A', new ItemStack(IUItem.verysmalldust, 1, 21)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.iudust, 1, 36),
                "AAA", "AAA", "AAA", 'A', new ItemStack(IUItem.smalldust, 1, 31)
        );
        for (j = 0; j < RegisterOreDictionary.list_string1.size(); j++) {
            Recipes.recipe.addShapelessRecipe(
                    new ItemStack(IUItem.alloysingot, 9, j),
                    new ItemStack(IUItem.alloysblock, 1, j)
            );
            Recipes.recipe.addShapelessRecipe(
                    new ItemStack(IUItem.alloysnugget, 9, j),
                    new ItemStack(IUItem.alloysingot, 1, j)
            );
            Recipes.recipe.addRecipe(new ItemStack(IUItem.alloysblock, 1, j), "AAA", "AAA", "AAA",
                    ('A'), ("ingot" + RegisterOreDictionary.list_string1.get(j))
            );
            Recipes.recipe.addRecipe(new ItemStack(IUItem.alloysingot, 1, j), "AAA", "AAA", "AAA",
                    ('A'), new ItemStack(IUItem.alloysnugget, 1, j)
            );
        }
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cable, 1, 0), " A ", "BBB", " A ",
                ('A'), IUItem.glassFiberCableItem, ('B'), new ItemStack(IUItem.itemiu, 1, 0)
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
        Recipes.recipe.addRecipe(IUItem.electronicCircuit, "EDE", "A A", "FBF",
                ('D'), new ItemStack(IUItem.basecircuit, 1, 17), ('B'), new ItemStack(IUItem.basecircuit, 1, 16),
                ('A'), new ItemStack(IUItem.basecircuit, 1, 15), ('E'),
                IUItem.insulatedCopperCableItem, ('F'), new ItemStack(Items.IRON_INGOT)
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

                ('E'), IUItem.circuitNano,

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

                ('C'), IUItem.circuitNano,

                ('B'), new ItemStack(IUItem.AdvlapotronCrystal, 1, 32767),

                ('A'),
                ("doubleplateFerromanganese")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.per_lappack), "ABA", "CEC", "ABA",

                ('E'), new ItemStack(IUItem.imp_lappack, 1, 32767),

                ('C'), IUItem.cirsuitQuantum,

                ('B'), new ItemStack(IUItem.compressIridiumplate, 1),

                ('A'),
                ("doubleplateVitalium")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.advancedSolarHelmet), " A ", "BCB", "DED",

                ('D'), IUItem.circuitNano,

                ('E'), new ItemStack(IUItem.compressAlloy),

                ('C'), new ItemStack(IUItem.adv_nano_helmet, 1, 32767),

                ('B'),
                new ItemStack(IUItem.compresscarbon),

                ('A'), new ItemStack(IUItem.blockpanel)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.hybridSolarHelmet), " A ", "BCB", "DED",

                ('D'), IUItem.cirsuitQuantum,

                ('E'), new ItemStack(IUItem.compressAlloy),

                ('C'), new ItemStack(IUItem.quantum_helmet, 1, 32767),

                ('B'),
                IUItem.iridiumPlate,

                ('A'), new ItemStack(IUItem.blockpanel, 1, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.ultimateSolarHelmet), " A ", "DCD", "BEB",

                ('D'), IUItem.cirsuitQuantum,

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

                ('C'), IUItem.compressIridiumplate,

                ('A'), IUItem.iridiumPlate,

                ('B'), new ItemStack(IUItem.rotor_carbon, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewind, 1, 6), "CAC", "ABA", "CAC",

                ('C'), IUItem.doublecompressIridiumplate,

                ('A'), new ItemStack(IUItem.compresscarbon),

                ('B'), new ItemStack(IUItem.iridium
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewind, 1, 7), "DCD", "ABA", "DCD",

                ('D'), IUItem.circuitSpectral,

                ('C'), new ItemStack(IUItem.compressIridiumplate),

                ('A'), new ItemStack(IUItem.advnanobox),

                ('B'),
                new ItemStack(IUItem.compressiridium
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewind, 1, 8), "DCD", "ABA", " C ",

                ('D'), new ItemStack(IUItem.excitednucleus, 1, 5),

                ('C'), IUItem.cirsuitQuantum,

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

                ('C'), IUItem.cirsuitQuantum,

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

                ('C'), IUItem.circuitSpectral,

                ('B'), new ItemStack(IUItem.barionrotor
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.corewind, 1, 13), "ECE", "CBC", "ACA",

                ('E'), IUItem.circuitSpectral,

                ('A'), new ItemStack(IUItem.doublecompressIridiumplate),

                ('C'), new ItemStack(IUItem.photoniy_ingot),

                ('B'),
                new ItemStack(IUItem.adronrotor
                        .getItem(), 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spectralSolarHelmet), " A ", "DCD", "BEB",

                ('D'), IUItem.cirsuitQuantum,

                ('E'), new ItemStack(IUItem.compressAlloy),

                ('C'), new ItemStack(IUItem.ultimateSolarHelmet, 1, 32767),

                ('B'),
                new ItemStack(IUItem.doublecompressIridiumplate, 1),

                ('A'), new ItemStack(IUItem.blockpanel, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.singularSolarHelmet), " A ", "DCD", "BDB",

                ('D'), IUItem.circuitSpectral,

                ('E'), new ItemStack(IUItem.compressAlloy),

                ('C'), new ItemStack(IUItem.spectralSolarHelmet, 1, 32767),

                ('B'),
                new ItemStack(IUItem.doublecompressIridiumplate, 1),

                ('A'), new ItemStack(IUItem.blockpanel, 1, 6)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines, 1, 5), "BCB", "DAD", "BCB",

                ('D'), IUItem.circuitSpectral,

                ('C'), new ItemStack(IUItem.doublecompressIridiumplate),

                ('B'), new ItemStack(IUItem.photoniy_ingot),

                ('A'),
                new ItemStack(IUItem.machines, 1, 3)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines, 1, 1), "DED", "BCB", "AAA",

                ('E'), new ItemStack(IUItem.core, 1, 5),

                ('D'),
                ("doubleplateAlumel"),
                ('B'), IUItem.cirsuitQuantum,

                ('C'),
                new ItemStack(IUItem.simplemachine, 1, 6),

                ('A'), new ItemStack(IUItem.quantumtool)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines, 1, 2), "DED", "BCB", "AAA",

                ('E'), new ItemStack(IUItem.core, 1, 7),

                ('D'),
                ("doubleplateVitalium"),
                ('B'), IUItem.cirsuitQuantum,

                ('C'),
                new ItemStack(IUItem.machines, 1, 1),

                ('A'), new ItemStack(IUItem.advQuantumtool)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machines, 1, 3), "DED", "BCB", "AFA",

                ('F'), new ItemStack(IUItem.doublecompressIridiumplate),

                ('E'), new ItemStack(IUItem.core, 1, 8),

                ('D'),
                ("doubleplateDuralumin"),
                ('B'),
                IUItem.circuitSpectral,

                ('C'), new ItemStack(IUItem.machines, 1, 2),

                ('A'), new ItemStack(IUItem.advQuantumtool)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blocksintezator), "ABA", "BCB", "ABA",

                ('C'), IUItem.circuitSpectral,

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
                IUItem.electronicCircuit, IUItem.electronicCircuit, IUItem.advancedCircuit, IUItem.advancedCircuit, IUItem.circuitNano, IUItem.circuitNano, IUItem.cirsuitQuantum, IUItem.cirsuitQuantum, IUItem.circuitSpectral, IUItem.circuitSpectral,
                IUItem.circuitSpectral};
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
        stacks3 = new ItemStack[]{IUItem.circuitNano, IUItem.circuitNano, IUItem.cirsuitQuantum, IUItem.cirsuitQuantum, IUItem.cirsuitQuantum, IUItem.circuitSpectral, IUItem.circuitSpectral};
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
                IUItem.cirsuitQuantum,

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

                ('B'), IUItem.advancedCircuit,

                ('A'),
                new ItemStack(IUItem.basecircuit, 1, 0)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basecircuit, 1, 7), "CCC", "ABA", "DDD",
                ('D'), ("plateSteel"), ('C'), IUItem.carbonPlate, ('B'), new ItemStack(IUItem.basecircuit, 1, 9),

                ('A'),
                new ItemStack(IUItem.basecircuit, 1, 1)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basecircuit, 1, 8), "CCC", "ABA", "DDD",

                ('D'),
                ("plateSpinel"),
                ('C'), IUItem.carbonPlate,

                ('B'), new ItemStack(IUItem.basecircuit, 1, 10),

                ('A'),
                new ItemStack(IUItem.basecircuit, 1, 2)
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

                ('B'), IUItem.electronicCircuit,

                ('A'),
                new ItemStack(IUItem.basecircuit, 1, 12)
        );
        ItemStack[] circuit = {
                new ItemStack(
                        IUItem.crafting_elements,
                        1,
                        30
                ), IUItem.electronicCircuit, IUItem.advancedCircuit, IUItem.advancedCircuit, IUItem.circuitNano, IUItem.circuitNano, IUItem.cirsuitQuantum, IUItem.cirsuitQuantum, IUItem.circuitSpectral, IUItem.circuitSpectral,
                new ItemStack(IUItem.core, 1, 10), new ItemStack(IUItem.core, 1, 11), new ItemStack(
                IUItem.core,
                1,
                12
        ), new ItemStack(IUItem.core, 1, 13)};
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

                        ('A'), new ItemStack(IUItem.photonglass, 1, m),

                        ('B'), new ItemStack(IUItem.excitednucleus, 1, m),

                        ('C'), iridium[m],

                        ('D'),
                        new ItemStack(IUItem.blockpanel, 1, m - 1),

                        ('E'), circuit[m]
                );
                Recipes.recipe.addRecipe(new ItemStack(IUItem.upgradepanelkit, 1, m), "ABA", "C C", "DED",

                        ('A'), new ItemStack(IUItem.photonglass, 1, m),

                        ('B'), new ItemStack(IUItem.excitednucleus, 1, m),

                        ('C'), iridium[m],

                        ('D'),
                        new ItemStack(IUItem.blockpanel, 1, m - 1),

                        ('E'), circuit[m]
                );
            } else {
                Recipes.recipe.addRecipe(new ItemStack(IUItem.blockpanel, 1, m), "ABA", "CDC", "DED",

                        ('A'), new ItemStack(IUItem.photonglass, 1, m),

                        ('B'), new ItemStack(IUItem.excitednucleus, 1, m),

                        ('C'), iridium[m],

                        ('D'),
                        getBlockStack(BlockBaseMachine3.solar_iu),
                        ('E'), circuit[m]
                );
                Recipes.recipe.addRecipe(new ItemStack(IUItem.upgradepanelkit, 1, m), "ABA", "C C", "DED",

                        ('A'), new ItemStack(IUItem.photonglass, 1, m),

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

                ('B'), IUItem.cirsuitQuantum,

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 158)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.analyzermodule), "BAB", "DCD",

                ('D'),
                ("ingotGermanium"),
                ('C'), new ItemStack(IUItem.module_schedule),

                ('B'), IUItem.cirsuitQuantum,

                ('A'), new ItemStack(IUItem.basemachine1, 1, 2)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machinekit, 1, 0), "ABA", "D D", "EEE",

                ('E'), new ItemStack(IUItem.nanoBox),

                ('D'), IUItem.circuitNano,

                ('B'),
                ("doubleplateAluminium"),
                ('A'),
                ("doubleplateAlumel")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machinekit, 1, 1), "ABA", "D D", "EEE",

                ('E'), new ItemStack(IUItem.quantumtool),

                ('D'), IUItem.cirsuitQuantum,

                ('B'),
                ("doubleplatePlatinum"),
                ('A'),
                ("doubleplateVitalium")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.machinekit, 1, 2), "ABA", "D D", "EEE",

                ('E'), new ItemStack(IUItem.advQuantumtool),

                ('D'), IUItem.circuitSpectral,

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

                ('A'), IUItem.advancedCircuit,

                ('B'), new ItemStack(IUItem.alloyscasing, 1, 2),

                ('C'), new ItemStack(IUItem.module_schedule),

                ('D'),
                ("ingotInvar"),
                ('E'), new ItemStack(IUItem.alloyscasing, 1, 4)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.entitymodules, 1, 1), "ABA", "DCD", "EBE",

                ('A'), IUItem.circuitSpectral,

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

                ('D'), IUItem.circuitNano,

                ('B'),
                ("doubleplateAluminium"),
                ('A'),
                ("doubleplateAlumel"),
                ('C'), new ItemStack(IUItem.spawnermodules, 1, 0)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spawnermodules, 1, 2), "ABA", "DCD", "EEE",

                ('E'), new ItemStack(IUItem.quantumtool),

                ('D'), IUItem.cirsuitQuantum,

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

                ('D'), IUItem.circuitNano,

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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.cathode), "B B", "BAB", "B B",

                ('A'), new ItemStack(IUItem.cell_all, 1, 0),

                ('B'),
                ("plateAlumel")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.anode), "B B", "BAB", "B B",

                ('A'), new ItemStack(IUItem.cell_all, 1, 0),

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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nanodrill), "EDE", "ABC", " D ",

                ('E'), new ItemStack(IUItem.nanoBox),

                ('D'), new ItemStack(IUItem.photoniy_ingot),

                ('B'), new ItemStack(IUItem.advnanobox),

                ('A'),
                new ItemStack(IUItem.nanopickaxe, 1, 32767),

                ('C'), new ItemStack(IUItem.nanoshovel, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.quantumdrill), "EDE", "ABC", " D ",

                ('E'), new ItemStack(IUItem.quantumtool),

                ('D'), IUItem.cirsuitQuantum,

                ('B'), new ItemStack(IUItem.quantumtool),

                ('A'),
                new ItemStack(IUItem.quantumpickaxe, 1, 32767),

                ('C'), new ItemStack(IUItem.quantumshovel, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spectraldrill), "EDE", "ABC", " D ",

                ('E'), new ItemStack(IUItem.spectral_box),

                ('D'), IUItem.circuitSpectral,

                ('B'), new ItemStack(IUItem.advQuantumtool),

                ('A'),
                new ItemStack(IUItem.spectralpickaxe, 1, 32767),

                ('C'), new ItemStack(IUItem.spectralshovel, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.quantumdrill), "TCT", "CDC", "BFB",

                ('T'),
                ("doubleplateMuntsa"),
                ('F'), new ItemStack(IUItem.lapotron_crystal, 1, 32767),

                ('B'), new ItemStack(IUItem.advQuantumtool, 1),

                ('D'),
                new ItemStack(IUItem.nanodrill, 1, 32767),

                ('C'), IUItem.cirsuitQuantum
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spectraldrill), "TCT", "CDC", "BFB",

                ('T'), IUItem.iridiumPlate,

                ('F'), new ItemStack(IUItem.impBatChargeCrystal, 1, 32767),

                ('B'), new ItemStack(IUItem.adv_spectral_box, 1),

                ('D'),
                new ItemStack(IUItem.quantumdrill, 1, 32767),

                ('C'), IUItem.circuitSpectral
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.bags), "BCB", "BAB", "B B",

                ('C'), new ItemStack(IUItem.suBattery, 1, 32767),

                ('B'), new ItemStack(Items.LEATHER),

                ('A'), IUItem.electronicCircuit
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_bags, 1), "BCB", "BAB", "B B",

                ('C'), new ItemStack(IUItem.reBattery, 1, 32767),

                ('B'), IUItem.carbonPlate,

                ('A'), new ItemStack(IUItem.bags, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.imp_bags, 1), "BCB", "BAB", "B B",

                ('C'), new ItemStack(IUItem.advBattery, 1, 32767),

                ('B'), new ItemStack(IUItem.compresscarbon),

                ('A'), new ItemStack(IUItem.adv_bags, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.advjetpack), "BCB", "CDC", "BFB",

                ('F'), new ItemStack(IUItem.suBattery, 1, 32767),

                ('B'),
                ("doubleplateFerromanganese"),
                ('D'), new ItemStack(IUItem.electricJetpack, 1, 32767),

                ('C'),
                IUItem.circuitNano
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.impjetpack), "TCT", "CDC", "BFB",

                ('T'),
                ("doubleplateMuntsa"),
                ('F'), new ItemStack(IUItem.reBattery, 1, 32767),

                ('B'), new ItemStack(IUItem.quantumtool, 1),

                ('D'),
                new ItemStack(IUItem.advjetpack, 1, 32767),

                ('C'), IUItem.cirsuitQuantum
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.perjetpack), "TCT", "CDC", "BFB",

                ('T'), IUItem.iridiumPlate,

                ('F'), new ItemStack(IUItem.advBattery, 1, 32767),

                ('B'), new ItemStack(IUItem.advQuantumtool, 1),

                ('D'),
                new ItemStack(IUItem.impjetpack, 1, 32767),

                ('C'), IUItem.circuitSpectral
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 2), "   ", "ABC", "   ",

                ('A'), new ItemStack(IUItem.module7),

                ('B'), IUItem.machine,

                ('C'), IUItem.electronicCircuit
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 3), "   ", "ABC", "   ",

                ('A'), IUItem.module8,

                ('B'), IUItem.machine,

                ('C'), IUItem.electronicCircuit
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

                ('A'), IUItem.cell_all,

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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.coolupgrade, 1, 0), "A A", " B ", "A A",

                ('A'),
                ModUtils.getCellFromFluid(FluidName.fluidazot.getInstance()),
                ('B'), new ItemStack(IUItem.module_schedule)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.coolupgrade, 1, 1), "A A", " B ", "A A",

                ('A'),
                ModUtils.getCellFromFluid(FluidName.fluidhyd.getInstance()),
                ('B'), new ItemStack(IUItem.module_schedule)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.coolupgrade, 1, 2), "A A", " B ", "A A",

                ('A'),
                ModUtils.getCellFromFluid(FluidName.fluidHelium.getInstance()),
                ('B'), new ItemStack(IUItem.module_schedule)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.autoheater), "AB ",

                ('A'), new ItemStack(IUItem.basemachine2, 1, 5),

                ('B'), new ItemStack(IUItem.module_schedule)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.scable), " C ", "ABA", " C ",

                ('B'), IUItem.copperCableItem,

                ('A'), new ItemStack(IUItem.sunnarium, 1, 3),

                ('C'), IUItem.rubber
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.qcable), " C ", "ABA", " C ",

                ('B'), IUItem.glassFiberCableItem,

                ('A'), new ItemStack(IUItem.proton),

                ('C'), IUItem.iridiumOre
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.simplemachine, 1, 6), "ABA", "CDC", "ABA",

                ('A'), Items.GLOWSTONE_DUST,

                ('B'), IUItem.advancedCircuit,

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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blastfurnace, 1, 1), " A ", "ABA", " A ",

                ('B'), IUItem.pullingUpgrade,
                ('A'), new ItemStack(IUItem.blastfurnace, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blastfurnace, 1, 3), " A ", "ABA", " A ",

                ('B'), IUItem.ejectorUpgrade,
                ('A'), new ItemStack(IUItem.blastfurnace, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blastfurnace, 1, 2), " A ", "ABA", " A ",

                ('B'), IUItem.copperboiler,
                ('A'), new ItemStack(IUItem.blastfurnace, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blastfurnace, 1, 4), " A ", "ABA", " A ",

                ('B'),
                getBlockStack(BlockBaseMachine3.canner_iu),
                ('A'), new ItemStack(IUItem.blastfurnace, 1, 5)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.blastfurnace, 1, 0), " A ", "ABA", " A ",

                ('B'), IUItem.electronicCircuit,

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
        Recipes.recipe.addRecipe(IUItem.transformerUpgrade, "AAA", "DBD", "ACA",

                ('B'), new ItemStack(IUItem.tranformer, 1, 8),

                ('A'), Blocks.GLASS,

                ('D'), IUItem.insulatedGoldCableItem,

                ('C'),
                IUItem.electronicCircuit
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.windmeter, 1, 0), " A ", "ABA", " AC",

                ('A'), IUItem.casingtin, ('B'), IUItem.casingbronze, ('C'), IUItem.powerunitsmall
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.rotorupgrade_schemes), "ADA", "CBC", "ADA",

                ('A'), new ItemStack(IUItem.plastic_plate),
                ('D'), "doubleplateVanadoalumite", ('B'), new ItemStack(IUItem.photoniy_ingot), ('C'),
                "plateManganese"
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

                ('A'), new ItemStack(IUItem.module_schedule), ('E'), new ItemStack(IUItem.core,1, 5),
                ('B'), new ItemStack(IUItem.quantumtool), ('C'),
                IUItem.iridiumPlate, ('D'), IUItem.cirsuitQuantum
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 38), "CBC", "ADA", "EBE",

                ('E'), IUItem.adv_spectral_box,

                ('D'), IUItem.advancedMachine,

                ('C'), IUItem.circuitSpectral,

                ('B'),
                ("doubleplateMuntsa"),
                ('A'),
                ("doubleplateAluminumbronze")
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 32), " C ", "CDC", "ECE",

                ('C'), IUItem.insulatedCopperCableItem,

                ('D'), IUItem.advancedMachine,

                ('E'), IUItem.circuitNano
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2, 1, 33), " C ", "CDC", "ECE",

                ('C'), IUItem.insulatedCopperCableItem,

                ('D'), IUItem.advancedMachine,

                ('E'), IUItem.cirsuitQuantum
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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 21), "CCC", "BAB", " B ",
                ('A'), new ItemStack(IUItem.crafting_elements, 1, 42), ('B'), "ingotGold", ('C'), "ingotIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 20), " B ", "ACA", " D ",

                ('A'), "doubleplateElectrum", ('B'), "doubleplateAlumel", ('C'), IUItem.elemotor, ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 16)
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
                IUItem.electronicCircuit
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 25), "CCC", "BAB", " D ",
                ('B'), IUItem.circuitNano, ('A'), new ItemStack(IUItem.crafting_elements, 1, 21), ('C'), new ItemStack(
                        IUItem.nanoBox),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 16)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 23), "CCC", "BAB", " D ",
                ('B'), IUItem.cirsuitQuantum, ('A'), new ItemStack(IUItem.crafting_elements, 1, 25), ('C'), new ItemStack(
                        IUItem.quantumtool),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 92)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 24), "CCC", "BAB", " D ",
                ('B'), IUItem.circuitSpectral, ('A'), new ItemStack(IUItem.crafting_elements, 1, 23), ('C'), new ItemStack(
                        IUItem.spectral_box),
                ('D'),
                new ItemStack(IUItem.crafting_elements, 1, 116)
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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 200), "AAA", "BCB", "   ",

                ('A'), "plateZinc", ('B'), "plateTitanium",
                ('C'), "plateAluminum"
        );
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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 2, 137), "AA ", "AA ", "   ",
                ('A'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 138), "BA ", "AA ", "   ",
                ('A'), "plateElectrum", ('B'), new ItemStack(IUItem.crafting_elements, 1, 137)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 139), "BA ", "AA ", "   ",
                ('A'), "plateCobalt", ('B'), new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 140), "BA ", "AA ", "   ",
                ('A'), "plateMagnesium", ('B'), new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.recipe.addShapelessRecipe(Blocks.STICKY_PISTON, Blocks.PISTON, IUItem.latex);
        Recipes.recipe.addRecipe(IUItem.machine, "AA ", "AA ", "   ",
                ('A'), new ItemStack(IUItem.crafting_elements, 1, 137)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 121), "AAA", "ABA", "AAA",
                ('A'), "plateCobalt", ('B'), Items.STRING
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 8, 122), "AAA", "BBB", "AAA",
                ('A'), "plateTungsten", ('B'), "plateTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 201), "AAA", "BBB", "   ",
                ('A'), "plateTitanium", ('B'), "plateIron"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 2, 0), "AAA", "ABA", "AAA",
                ('A'), new ItemStack(IUItem.crafting_elements,1,444), ('B'), "plateCobalt"
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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 11), " C ", "BAB", "   ",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 21),

                ('B'), "plateIron",

                ('C'), new ItemStack(IUItem.reBattery, 1, 32767)
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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 59), "BCB", "DAD", "ECE",

                ('A'), new ItemStack(IUItem.crafting_elements, 1, 21),

                ('B'), "doubleplateMuntsa",

                ('C'), "doubleplateAlcled",

                ('D'),
                "plateZinc",

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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 43), "BCB", "BAB", "BCB",

                ('A'), DEFAULT_SENSOR,

                ('B'), IUItem.FluidCell,

                ('C'), "plateAlcled"
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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 71), "CCC", "BAB", "DED",

                ('A'), DEFAULT_SENSOR,

                ('C'), "gearGermanium",

                ('B'), "doubleplateIridium",

                ('D'),
                "gearRedbrass",

                ('E'), IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(IUItem.iridiumPlate, "ABA", "BCB", "ABA",

                ('A'), IUItem.iridiumOre,

                ('C'), "gemDiamond",

                ('B'), IUItem.advancedAlloy
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 3), "CCC", "BAB", "DED",
                ('A'), ADV_SENSOR, ('C'), "gearGermanium", ('B'), "doubleplateIridium",
                ('D'),
                "gearRedbrass", ('E'), IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 80), "CCC", "BAB", "DED",
                ('A'), IMP_SENSOR, ('C'), "gearGermanium", ('B'), "doubleplateIridium",
                ('D'),
                "gearRedbrass", ('E'), IUItem.compressIridiumplate
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 104), "CCC", "BAB", "DED",
                ('A'), PER_SENSOR, ('C'), "gearGermanium", ('B'), "doubleplateIridium",
                ('D'),
                "gearRedbrass", ('E'), IUItem.compressIridiumplate
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
                ('A'), DEFAULT_SENSOR, ('B'), new ItemStack(IUItem.cell_all, 1, 5),
                ('C'), "gearMagnesium", ('D'),
                "plateTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 99), "CBC", "BAB", "DBD",
                ('A'), DEFAULT_SENSOR, ('B'), IUItem.Uran238,
                ('C'), "gearVanadium", ('D'),
                "plateChromium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 100), "CBC", "BAB", "CBC",
                ('A'), DEFAULT_SENSOR, ('B'), IUItem.doublecompressIridiumplate,
                ('C'), IUItem.photoniy_ingot
        );
        Recipes.recipe.addRecipe(
                ModUtils.setSize(IUItem.overclockerUpgrade, 2),
                "CCC",
                "ABA",
                'C',
                IUItem.reactorCoolantSimple,
                'A',
                IUItem.insulatedCopperCableItem,
                'B',
                IUItem.electronicCircuit

        );
        Recipes.recipe.addRecipe(
                IUItem.reactorCoolantSimple,
                " A ",
                "ABA",
                " A ",
                'B',
                ModUtils.getCellFromFluid(FluidName.fluidcoolant.getInstance()),
                'A',
                "plateTin"

        );
        Recipes.recipe.addRecipe(
                IUItem.reactorCoolantTriple,
                "AAA",
                "BBB",
                "AAA",
                'B',
                IUItem.reactorCoolantSimple,
                'A',
                "plateTin"

        );
        Recipes.recipe.addRecipe(
                IUItem.reactorCoolantSix,
                "ABA",
                "ACA",
                "ABA",
                'B',
                IUItem.reactorCoolantTriple,
                'A',
                "plateTin",
                'C', "plateIron"

        );
        Recipes.recipe.addRecipe(
                ModUtils.setSize(IUItem.overclockerUpgrade, 6),
                "CCC",
                "ABA",
                'C',
                IUItem.reactorCoolantTriple,
                'A',
                IUItem.insulatedCopperCableItem,
                'B',
                IUItem.electronicCircuit

        );
        Recipes.recipe.addRecipe(
                ModUtils.setSize(IUItem.overclockerUpgrade, 12),
                "CCC",
                "ABA",
                'C',
                IUItem.reactorCoolantSix,
                'A',
                IUItem.insulatedCopperCableItem,
                'B',
                IUItem.electronicCircuit

        );
        Recipes.recipe.addRecipe(
                getBlockStack(BlockBaseMachine3.facademechanism),
                " C ",
                "BAB",
                " D ",
                'C',
                new ItemStack(IUItem.crafting_elements,1,90),
                'A',
                IUItem.machine,
                'B',
                new ItemStack(IUItem.crafting_elements,1,66),
                'D',
                new ItemStack(IUItem.crafting_elements,1,44)

        );
        Recipes.recipe.addRecipe(
                IUItem.connect_item,
                "A A",
                " A ", "C C",'C',
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
               IUItem.electronicCircuit,
                'B', "ingotIron", 'C',"ingotSteel",'D',"ingotGermanium"

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.nuclear_bomb),
                "ABA", "BAB", "ABA", 'A', new ItemStack(Blocks.TNT), 'B', new ItemStack(IUItem.nuclear_res,1)
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.pump),
                "A A", "CBC", "A A", 'A', "plateIron", 'C', "ingotIron" ,'B', IUItem.electronicCircuit
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_pump),
                "A A", "CBC", "A A", 'A', "plateCarbon", 'C', new ItemStack(IUItem.crafting_elements,1,344) ,'B', IUItem.pump
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.imp_pump),
                "A A", "CBC", "A A", 'A', "gemTopaz", 'C', "ingotGold" ,'B', IUItem.adv_pump
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.per_pump),
                "A A", "CBC", "A A", 'A', "gemDiamond", 'C', "plateOsmium" ,'B', IUItem.imp_pump
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.fan),
                "ACA", "CBC", "ACA", 'A', "plateIron", 'C', "plateTitanium" ,'B', IUItem.electronicCircuit
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.adv_fan),
                "ACA", "CBC", "ACA", 'A', "plateElectrum", 'C', "plateTitanium" ,'B',IUItem.fan
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.imp_fan),
                "ACA", "CBC", "ACA", 'A', "platePlatinum", 'C', "plateTitanium" ,'B',IUItem.adv_fan
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.per_fan),
                "ACA", "CBC", "ACA", 'A', "plateCadmium", 'C', "plateTitanium" ,'B',IUItem.imp_fan
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
                "ABA", "ABA", "AAA", 'A', "plateAluminum", 'B', IUItem.adv_reactor_plate
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.imp_exchanger_item),
                "ABA", "ABA", "AAA", 'A', "plateAluminumbronze", 'B', IUItem.imp_reactor_plate
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.per_exchanger_item),
                "ABA", "ABA", "AAA", 'A', "plateDuralumin", 'B', IUItem.per_reactor_plate
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.nuclear_res,1,8),
                "ABA", "BCB", "ABA", 'A', IUItem.quad_uranium_fuel_rod, 'B', "plateLead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.nuclear_res,1,9),
                "ABA", "BCB", "ABA", 'A', IUItem.quad_mox_fuel_rod, 'B', "plateLead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets,1,0),
                "ABA", "BCB", "ABA", 'A', IUItem.reactoramericiumQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets,1,1),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorneptuniumQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets,1,2),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorcuriumQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets,1,3),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorcaliforniaQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets,1,4),
                "ABA", "BCB", "ABA", 'A', IUItem.reactortoriyQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets,1,5),
                "ABA", "BCB", "ABA", 'A', IUItem.reactormendeleviumQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets,1,6),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorberkeliumQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets,1,7),
                "ABA", "BCB", "ABA", 'A', IUItem.reactoreinsteiniumQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets,1,8),
                "ABA", "BCB", "ABA", 'A', IUItem.reactoruran233Quad, 'B', "plateLead", 'C', IUItem.radcable_item
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.pellets,1,9),
                "ABA", "BCB", "ABA", 'A', IUItem.reactorprotonQuad, 'B', "plateLead", 'C', IUItem.radcable_item
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,0),
                "BCB", "DED", "BAB", 'A', IUItem.module_schedule, 'B', "plateTantalum", 'C', IUItem.electronicCircuit,'D',
                "plateNickel", 'E', IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,1),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,0), 'B', "plateCadmium", 'C',
                IUItem.advancedCircuit,'D',
                "plateNickel", 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,2),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,1), 'B', "doubleplateCadmium", 'C',
                IUItem.circuitNano,'D',
                "plateNickel", 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,3),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,2), 'B', "doubleplateDuralumin", 'C',
                IUItem.cirsuitQuantum,'D',
                "plateNickel", 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,4),
                "BCB", "DED", "BAB", 'A', IUItem.module_schedule, 'B', "plateTantalum", 'C', IUItem.electronicCircuit,'D',
                new ItemStack(IUItem.radiationresources,1,0), 'E', IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,5),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,4), 'B', "plateCadmium", 'C',
                IUItem.advancedCircuit,'D',
                new ItemStack(IUItem.radiationresources,1,0), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,6),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,5), 'B', "doubleplateCadmium", 'C',
                IUItem.circuitNano,'D',
                new ItemStack(IUItem.radiationresources,1,0), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,7),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,6), 'B', "doubleplateDuralumin", 'C',
                IUItem.cirsuitQuantum,'D',
                new ItemStack(IUItem.radiationresources,1,0), 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,8),
                "BCB", "DED", "BAB", 'A', IUItem.module_schedule, 'B', "plateTantalum", 'C', IUItem.electronicCircuit,'D',
                "doubleplateAlumel", 'E', IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,9),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,8), 'B', "plateCadmium", 'C',
                IUItem.advancedCircuit,'D',
                "doubleplateAlumel", 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,10),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,9), 'B', "doubleplateCadmium", 'C',
                IUItem.circuitNano,'D',
                "doubleplateAlumel", 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,11),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,10), 'B', "doubleplateDuralumin", 'C',
                IUItem.cirsuitQuantum,'D',
                "doubleplateAlumel", 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,12),
                "BCB", "DED", "BAB", 'A', IUItem.module_schedule, 'B', "plateTantalum", 'C', IUItem.electronicCircuit,'D',
                new ItemStack(IUItem.vent), 'E', IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,13),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,12), 'B', "plateCadmium", 'C',
                IUItem.advancedCircuit,'D',
                new ItemStack(IUItem.adv_Vent), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,14),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,13), 'B', "doubleplateCadmium", 'C',
                IUItem.circuitNano,'D',
                new ItemStack(IUItem.imp_Vent), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,15),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,14), 'B', "doubleplateDuralumin", 'C',
                IUItem.cirsuitQuantum,'D',
                new ItemStack(IUItem.per_Vent), 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,16),
                "BCB", "DED", "BAB", 'A', IUItem.module_schedule, 'B', "plateTantalum", 'C', IUItem.electronicCircuit,'D',
                new ItemStack(IUItem.componentVent), 'E', IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,17),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,16), 'B', "plateCadmium", 'C',
                IUItem.advancedCircuit,'D',
                new ItemStack(IUItem.adv_componentVent), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,18),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,17), 'B', "doubleplateCadmium", 'C',
                IUItem.circuitNano,'D',
                new ItemStack(IUItem.imp_componentVent), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,19),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,18), 'B', "doubleplateDuralumin", 'C',
                IUItem.cirsuitQuantum,'D',
                new ItemStack(IUItem.per_componentVent), 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,20),
                "BCB", "DED", "BAB", 'A', IUItem.module_schedule, 'B', "plateTantalum", 'C', IUItem.electronicCircuit,'D',
                new ItemStack(IUItem.heat_exchange), 'E', IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,21),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,20), 'B', "plateCadmium", 'C',
                IUItem.advancedCircuit,'D',
                new ItemStack(IUItem.adv_heat_exchange), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,22),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,21), 'B', "doubleplateCadmium", 'C',
                IUItem.circuitNano,'D',
                new ItemStack(IUItem.imp_heat_exchange), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,23),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,22), 'B', "doubleplateDuralumin", 'C',
                IUItem.cirsuitQuantum,'D',
                new ItemStack(IUItem.per_heat_exchange), 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,24),
                "BCB", "DED", "BAB", 'A', IUItem.module_schedule, 'B', "plateTantalum", 'C', IUItem.electronicCircuit,'D',
                new ItemStack(IUItem.capacitor), 'E', IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,25),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,24), 'B', "plateCadmium", 'C',
                IUItem.advancedCircuit,'D',
                new ItemStack(IUItem.adv_capacitor), 'E', IUItem.quantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,26),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,25), 'B', "doubleplateCadmium", 'C',
                IUItem.circuitNano,'D',
                new ItemStack(IUItem.imp_capacitor), 'E', IUItem.advQuantumtool
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.radiationModule,1,27),
                "BCB", "DED", "BAB", 'A', new ItemStack(IUItem.radiationModule,1,26), 'B', "doubleplateDuralumin", 'C',
                IUItem.cirsuitQuantum,'D',
                new ItemStack(IUItem.per_capacitor), 'E', IUItem.spectral_box
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,323),
                "BCB", "BAB", "DDD", 'A', DEFAULT_SENSOR, 'B', "plateSteel", 'D',"ingotMikhail",'C', "gearOsmium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,325),
                " C ", "BAB", "DCD", 'A', DEFAULT_SENSOR, 'B', new ItemStack(IUItem.nuclear_res), 'D',"plateCarbon",'C',
                "plateLapis"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,328),
                "ECE", "BAB", "DCD", 'A', DEFAULT_SENSOR, 'B', "plateIron", 'D',"plateSilver",'C',
                "plateOsmium",'E', IUItem.nanoBox
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,440),
                "   ", "BAB", "DCD", 'A', DEFAULT_SENSOR, 'B', new ItemStack(IUItem.crafting_elements,1,386), 'D',
                new ItemStack(IUItem.crafting_elements,1,320),'C',
                "plateTantalum"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,441),
                "CDC", "BAB", "BDB", 'A', DEFAULT_SENSOR, 'B', new ItemStack(IUItem.itemiu,1,2), 'D',
               "plateDenseLead",'C',
                "plateSpinel"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,442),
                "CDC", "BAB", "BDB", 'A', DEFAULT_SENSOR, 'B', new ItemStack(IUItem.sunnariumpanel,1,0), 'D',
                new ItemStack(IUItem.sunnarium,1,3),'C',
                new ItemStack(IUItem.crafting_elements,1,319)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,354),
                "BAB", "BAB", "BDB", 'A', "gemRuby", 'B', "plateTitanium", 'D',
               "dustRedstone"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2,1,1),
                " ", "CAD", " B ", 'A', "machineBlock",'B', IUItem.elemotor, 'C', new ItemStack(IUItem.crafting_elements,1,36),
                'C', new ItemStack(IUItem.module7 ,1,9)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2,1,20),
                " ", "CAD", " B ", 'A', "machineBlockAdvanced",'B', IUItem.elemotor, 'C', new ItemStack(IUItem.crafting_elements,1,36),
                'D', new ItemStack(IUItem.crafting_elements,1,226)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2,1,91),
                " ", " A ", " B ", 'A', IUItem.blockpanel,'B', IUItem.module_schedule
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2,1,94),
                " F ", "CAD", " B ", 'A', "machineBlock",'B', IUItem.elemotor,'C', new ItemStack(IUItem.crafting_elements,1,51),
                'D', new ItemStack(IUItem.crafting_elements,1,47),  'F',
                new ItemStack(IUItem.crafting_elements,1,323)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2,1,93),
                "DEF", "CAC", " B ", 'A', "machineBlock",'B', IUItem.elemotor,'C', new ItemStack(IUItem.crafting_elements,1,51),
                'D', new ItemStack(IUItem.crafting_elements,1,44), 'E', new ItemStack(IUItem.crafting_elements,1,433),  'F',
                new ItemStack(IUItem.crafting_elements,1,323)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2,1,95),
                "   ", "CAD", " B ", 'A', "machineBlock",'B', IUItem.elemotor,'C', new ItemStack(IUItem.crafting_elements,1,442),
                'D', new ItemStack(IUItem.crafting_elements,1,44)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2,1,98),
                "   ", "CAD", " B ", 'A', "machineBlock",'B', IUItem.elemotor,'C', new ItemStack(IUItem.crafting_elements,1,30),
                'D', new ItemStack(IUItem.crafting_elements,1,44)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2,1,96),
                "CDC", "CAC", " B ", 'A', "machineBlock",'B', IUItem.elemotor,'C', new ItemStack(IUItem.crafting_elements,1,354),
                'D', new ItemStack(IUItem.crafting_elements,1,44)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2,1,99),
                " E ", "DAC", " B ", 'A', "machineBlock",'B', IUItem.elemotor,'C', new ItemStack(IUItem.crafting_elements,1,328),
                'D', new ItemStack(IUItem.crafting_elements,1,51), 'E', new ItemStack(IUItem.crafting_elements,1,44)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2,1,97),
                "   ", "DAC", " B ", 'A', "machineBlock",'B', IUItem.elemotor,'C', new ItemStack(IUItem.crafting_elements,1,440),
                'D', new ItemStack(IUItem.crafting_elements,1,49)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2,1,100),
                " E ", "DAC", " B ", 'A', "machineBlock",'B', IUItem.elemotor,'C', new ItemStack(IUItem.crafting_elements,1,49),
                'D', new ItemStack(IUItem.crafting_elements,1,51),'E',new ItemStack(IUItem.crafting_elements,1,325)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2,1,101),
                " E ", "DAC", " B ", 'A', "machineBlock",'B', IUItem.elemotor,'C', new ItemStack(IUItem.crafting_elements,1,44),
                'D', new ItemStack(IUItem.crafting_elements,1,36),'E',new ItemStack(IUItem.crafting_elements,1,441)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2,1,102),
                "   ", "BAC", " D ", 'A', "machineBlockAdvanced",'B', new ItemStack(IUItem.crafting_elements,1,439),'C',
                new ItemStack(IUItem.crafting_elements,1,36),'D', IUItem.elemotor

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2,1,104),
                "FEF", "BAC", " D ", 'A', "machineBlockAdvanced",'B', new ItemStack(IUItem.crafting_elements,1,47),'C',
                new ItemStack(IUItem.crafting_elements,1,35),'D', IUItem.elemotor,'E', Items.ENCHANTED_BOOK,'F',
                "doubleplateCadmium"

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2,1,105),
                " E ", "BAC", " D ", 'A', "machineBlock",'B', new ItemStack(IUItem.crafting_elements,1,44),'C',
                new ItemStack(IUItem.crafting_elements,1,43),'D', IUItem.elemotor,'E', IUItem.coolant

        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.basemachine2,1,103),
                "FBG", "EAC", " D ", 'A', "machineBlockAdvanced",'B', new ItemStack(IUItem.nuclear_res,1,8),'C',
                new ItemStack(IUItem.crafting_elements,1,31),'D', IUItem.elemotor,'E', new ItemStack(IUItem.crafting_elements,1
                        ,36),'F', new ItemStack(IUItem.crafting_elements,1,51),'G', new ItemStack(IUItem.crafting_elements,1,47)

        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.earthQuarry,1,2),
                "C C", " B ", "A A", 'B', "machineBlock",'A',"plateSteel",'C',"plateIron"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.earthQuarry,1),
                "DBD", " A ", " C ", 'B', new ItemStack(IUItem.crafting_elements,1,42),'A',new ItemStack(IUItem.earthQuarry,1,2)
                ,'C'
                ,IUItem.electronicCircuit,'D', "stickZinc"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.earthQuarry,1,1),
                "DBD", "EAE", " C ", 'B', new ItemStack(IUItem.crafting_elements,1,226),'A',new ItemStack(IUItem.earthQuarry,1,2)
                ,'C'
                ,IUItem.electronicCircuit,'D', "gearOsmium",'E',"plateCarbon"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.earthQuarry,1,3),
                " E ", "DAD", "CBC", 'B', new ItemStack(IUItem.oilquarry),'A',new ItemStack(IUItem.earthQuarry,1,2)
                ,'C'
                ,"plateTitanium",'D', "plateSteel",'E',"plateIridium"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.earthQuarry,1,4),
                "   ", "DAD", "CBC", 'B', new ItemStack(Blocks.CHEST),'A',new ItemStack(IUItem.earthQuarry,1,2)
                ,'C'
                ,"plateGold",'D', "plateTin"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.earthQuarry,1,5),
                "BDB", "DAD", "BDB", 'B', new ItemStack(IUItem.item_pipes,1,1), 'D', new ItemStack(IUItem.item_pipes),'A',
                new ItemStack(IUItem.earthQuarry,1,2)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.anvil),
                "AAA", " A ", "AAA", 'A', "blockIron"
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,2,364),
                "AB ", "BA ", "   ", 'A', "plateLead", 'B', "plateTitanium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,2,363),
                "AB ", "BA ", "   ", 'A', "plateLead", 'B', "plateTungsten"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,2,365),
                "AB ", "BA ", "   ", 'A', "plateLead", 'B', "plateLithium"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,2,366),
                "AB ", "BC ", "   ", 'A', "plateLead", 'B', "platePlatinum"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,420),
                "AB ", "BC ", "   ", 'A', new ItemStack(IUItem.crafting_elements,1,366), 'B', "plateElectrum",'C', new ItemStack(IUItem.crafting_elements,1,453)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,378),
                "AB ", "BC ", "   ", 'A', new ItemStack(IUItem.crafting_elements,1,420), 'B', "plateCobalt",'C', new ItemStack(IUItem.crafting_elements,1,453)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,405),
                "AB ", "BC ", "   ", 'A', new ItemStack(IUItem.crafting_elements,1,378), 'B', "plateMagnesium",'C', new ItemStack(IUItem.crafting_elements,1,453)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,419),
                "AB ", "BC ", "   ", 'A', new ItemStack(IUItem.crafting_elements,1,365), 'B', "plateElectrum",'C', new ItemStack(IUItem.crafting_elements,1,453)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,377),
                "AB ", "BC ", "   ", 'A', new ItemStack(IUItem.crafting_elements,1,419), 'B', "plateCobalt",'C', new ItemStack(IUItem.crafting_elements,1,453)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,394),
                "AB ", "BC ", "   ", 'A', new ItemStack(IUItem.crafting_elements,1,377), 'B', "plateMagnesium",'C', new ItemStack(IUItem.crafting_elements,1,453)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,417),
                "AB ", "BC ", "   ", 'A', new ItemStack(IUItem.crafting_elements,1,363), 'B', "plateElectrum",'C', new ItemStack(IUItem.crafting_elements,1,453)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,375),
                "AB ", "BC ", "   ", 'A', new ItemStack(IUItem.crafting_elements,1,417), 'B', "plateCobalt",'C', new ItemStack(IUItem.crafting_elements,1,453)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,392),
                "AB ", "BC ", "   ", 'A', new ItemStack(IUItem.crafting_elements,1,375), 'B', "plateMagnesium",'C',new ItemStack(IUItem.crafting_elements,1,453)
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,418),
                "AB ", "BC ", "   ", 'A', new ItemStack(IUItem.crafting_elements,1,364), 'B', "plateElectrum",'C',new ItemStack(IUItem.crafting_elements,1,453)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,376),
                "AB ", "BC ", "   ", 'A', new ItemStack(IUItem.crafting_elements,1,418), 'B', "plateCobalt",'C',new ItemStack(IUItem.crafting_elements,1,453)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,393),
                "AB ", "BC ", "   ", 'A', new ItemStack(IUItem.crafting_elements,1,376), 'B', "plateMagnesium",'C',
                new ItemStack(IUItem.crafting_elements,1,453)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,439),
                "CEC ", "EAE", "DBD", 'A',DEFAULT_SENSOR, 'B', IUItem.circuitNano,'C',
               "plateBronze",'E',"doubleplateOsmium"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,435),
                "BСB", "BAB", "BDB", 'A', IUItem.electronicCircuit, 'B', "platePlatinum",'C',
                new ItemStack(IUItem.crafting_elements,1,42), 'D', new ItemStack(IUItem.crafting_elements,1,366)
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,436),
                "BСB", "BAB", "BDB", 'A', IUItem.electronicCircuit, 'B', "plateZinc",'C',
                new ItemStack(IUItem.crafting_elements,1,42), 'D', new ItemStack(IUItem.crafting_elements,1,365)
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,342),
                "CBC", "BAB", "DBD", 'A', IUItem.electronicCircuit, 'B', "plateLithium",'C',
                new ItemStack(IUItem.crafting_elements,1,386), 'D', "plateObsidian"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,324),
                "AAA", " B ", "DCD", 'C', IUItem.electronicCircuit, 'A', "plateCobalt",'B',
                IUItem.advancedAlloy, 'D', "plateCarbon"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,322),
                "ABA", "EFE", "DCD", 'C', IUItem.electronicCircuit, 'A', "plateTungsten",'B',
                IUItem.advancedAlloy, 'D', "plateBronze",'E',new ItemStack(IUItem.crafting_elements,1,445),'F',"plateTin"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,385),
                "A A", "EFG", "DCD", 'C', IUItem.electronicCircuit, 'A', "plateSteel", 'D', "plateBor",'E',
                new ItemStack(IUItem.crafting_elements,1,445),'F',"plateCarbon",'G',
                new ItemStack(IUItem.crafting_elements,1,446)
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,321),
                "B B", "DAD", "B B", 'A', IUItem.electronicCircuit, 'B', new ItemStack(IUItem.crafting_elements,1,320),'D',
                "plateCadmium"
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,327),
                "BCB", "DAD", "BCB", 'A', IUItem.electronicCircuit, 'B', "plateCarbon",'D',
                "plateOsmium",'C', "plateElectrum"
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,326),
                "BCB", "BAB", "DCD", 'A', IUItem.electronicCircuit, 'B', "plateCobalt",'D',
                "plateVanadoalumite",'C', "plateManganese"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,356),
                " B ", " A ", " B ", 'A', new ItemStack(IUItem.crafting_elements,1,294), 'B', "plateIron"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,424),
                "AAA", "ABA", "   ", 'A', new ItemStack(IUItem.crafting_elements,1,356), 'B', "plateElectrum"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,371),
                "AAA", "ABA", "   ", 'A', new ItemStack(IUItem.crafting_elements,1,424), 'B', "platePlatinum"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,397),
                "AAA", "ABA", "   ", 'A', new ItemStack(IUItem.crafting_elements,1,371), 'B', "plateSpinel"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,387),
                " A ", "ABA", " A ", 'A', new ItemStack(IUItem.crafting_elements,1,356), 'B', "doubleplateGermanium"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,425),
                " A ", "ABA", " A ", 'A', new ItemStack(IUItem.crafting_elements,1,387), 'B', "doubleplateAlumel"
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,372),
                " A ", "ABA", " A ", 'A', new ItemStack(IUItem.crafting_elements,1,425), 'B', "doubleplateVitalium"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,398),
                " A ", "ABA", " A ", 'A', new ItemStack(IUItem.crafting_elements,1,372), 'B', "doubleplateFerromanganese"
        );

        Recipes.recipe.addRecipe(new ItemStack(IUItem.crafting_elements,1,362),
                "CDC", "BAB", "CDC", 'A', new ItemStack(IUItem.itemiu,1,3), 'B', new ItemStack(IUItem.neutroniumingot),'C',
                "plateGermanium",'D',"plateOsmium"
        );
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
        BasicRecipeTwo.recipe();
    }

    public static ItemStack getBlockStack(IMultiTileBlock block) {
        return TileBlockCreator.instance.get(block.getIdentifier()).getItemStack(block);
    }

}
