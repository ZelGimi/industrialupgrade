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
                            .get(i)
            );
            Recipes.recipe.addShapelessRecipe(
                    new ItemStack(IUItem.casing, 2, i),
                    "craftingToolForgeHammer", "plate" + RegisterOreDictionary.list_string
                            .get(i)
            );
        }

        Recipes.recipe.addShapelessRecipe(IUItem.plateiron, "craftingToolForgeHammer", "ingotIron");
        Recipes.recipe.addShapelessRecipe(
                ModUtils.setSize(IUItem.casingiron, 2),
                "craftingToolForgeHammer", "plateIron"
        );
        Recipes.recipe.addShapelessRecipe(IUItem.plateadviron, "craftingToolForgeHammer", "ingotSteel");
        Recipes.recipe.addShapelessRecipe(
                ModUtils.setSize(IUItem.casingadviron, 2),
                "craftingToolForgeHammer", "plateSteel"
        );
        Recipes.recipe.addShapelessRecipe(IUItem.platecopper, "craftingToolForgeHammer", "ingotCopper");
        Recipes.recipe.addShapelessRecipe(
                ModUtils.setSize(IUItem.casingcopper, 2),
                "craftingToolForgeHammer", "plateCopper"
        );
        Recipes.recipe.addShapelessRecipe(IUItem.platetin, "craftingToolForgeHammer", "ingotTin");
        Recipes.recipe.addShapelessRecipe(
                ModUtils.setSize(IUItem.casingtin, 2),
                "craftingToolForgeHammer", "plateTin"
        );
        Recipes.recipe.addShapelessRecipe(IUItem.platelead, "craftingToolForgeHammer", "ingotLead");
        Recipes.recipe.addShapelessRecipe(
                ModUtils.setSize(IUItem.casinglead, 2),
                "craftingToolForgeHammer", "plateLead"
        );
        Recipes.recipe.addShapelessRecipe(IUItem.plategold, "craftingToolForgeHammer", "ingotGold");
        Recipes.recipe.addShapelessRecipe(
                ModUtils.setSize(IUItem.casinggold, 2),
                "craftingToolForgeHammer", "plateGold"
        );
        Recipes.recipe.addShapelessRecipe(IUItem.platebronze, "craftingToolForgeHammer", "ingotBronze");
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
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.advIronIngot, 9), IUItem.advironblock);
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.copperIngot, 9), IUItem.copperBlock);
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.tinIngot, 9), IUItem.tinBlock);
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.leadIngot, 9), IUItem.leadBlock);
        Recipes.recipe.addShapelessRecipe(ModUtils.setSize(IUItem.bronzeIngot, 9), IUItem.bronzeBlock);
        Recipes.recipe.addShapelessRecipe(new ItemStack(IUItem.itemiu, 9, 2), IUItem.uraniumBlock);
        Recipes.recipe.addRecipe(IUItem.reactorprotonDual, "SQS",
                ('S'), IUItem.reactorprotonSimple,
                ('Q'), ("plateIron")
        );
        Recipes.recipe.addRecipe(IUItem.reactorprotonQuad, "SQS", "CQC", "SQS",

                ('S'), IUItem.reactorprotonSimple,
                ('Q'), ("plateIron"), ('C'), ("plateCopper")
        );
        Recipes.recipe.addRecipe(IUItem.reactorprotonQuad, "SQS",

                ('S'), IUItem.reactorprotonDual, ('Q'),
                ("plateIron"), ('C'), ("plateCopper")
        );
        Recipes.recipe.addRecipe(IUItem.reactorprotoneit, "SQS",

                ('S'), IUItem.reactorprotonQuad, ('Q'),
                ("plateIron"), ('C'), ("plateCopper")
        );
        Recipes.recipe.addRecipe(IUItem.efReader, " A ", "BCB", "B B",

                ('A'), Items.GLOWSTONE_DUST, ('B'),
                IUItem.insulatedCopperCableItem, ('C'), IUItem.electronicCircuit
        );

        Recipes.recipe.addRecipe(IUItem.reactorprotoneit, "SQS", "CQC", "SQS",

                ('S'), IUItem.reactorprotonDual,
                ('Q'), ("plateIron"), ('C'), ("plateCopper")
        );
        Recipes.recipe.addRecipe(IUItem.reactortoriyDual, "SQS",
                ('S'), IUItem.reactortoriySimple,
                ('Q'), ("plateIron")
        );
        Recipes.recipe.addRecipe(IUItem.reactortoriyQuad, "SQS", "CQC", "SQS",

                ('S'), IUItem.reactortoriySimple,
                ('Q'), ("plateIron"), ('C'), ("plateCopper")
        );
        Recipes.recipe.addRecipe(IUItem.reactortoriyQuad, "SQS",

                ('S'), IUItem.reactortoriyDual, ('Q'),
                ("plateIron"), ('C'), ("plateCopper")
        );
        Recipes.recipe.addRecipe(IUItem.reactoramericiumDual, "SQS",
                ('S'), IUItem.reactoramericiumSimple,
                ('Q'), ("plateIron")
        );
        Recipes.recipe.addRecipe(IUItem.reactoramericiumQuad, "SQS", "CQC", "SQS",

                ('S'), IUItem.reactoramericiumSimple,
                ('Q'), ("plateIron"), ('C'), ("plateCopper")
        );
        Recipes.recipe.addRecipe(IUItem.reactoramericiumQuad, "SQS",

                ('S'), IUItem.reactoramericiumDual, ('Q'),
                ("plateIron"), ('C'), ("plateCopper")
        );
        Recipes.recipe.addRecipe(IUItem.reactorneptuniumDual, "SQS",
                ('S'), IUItem.reactorneptuniumSimple,
                ('Q'), ("plateIron")
        );
        Recipes.recipe.addRecipe(IUItem.reactorneptuniumQuad, "SQS", "CQC", "SQS",

                ('S'), IUItem.reactorneptuniumSimple,
                ('Q'), ("plateIron"), ('C'), ("plateCopper")
        );
        Recipes.recipe.addRecipe(IUItem.reactorneptuniumQuad, "SQS",

                ('S'), IUItem.reactorneptuniumDual, ('Q'),
                ("plateIron"), ('C'), ("plateCopper")
        );
        Recipes.recipe.addRecipe(IUItem.reactorcuriumDual, "SQS",
                ('S'), IUItem.reactorcuriumSimple,
                ('Q'), ("plateIron")
        );
        Recipes.recipe.addRecipe(IUItem.reactorcuriumQuad, "SQS", "CQC", "SQS",

                ('S'), IUItem.reactorcuriumSimple,
                ('Q'), ("plateIron"), ('C'), ("plateCopper")
        );
        Recipes.recipe.addRecipe(IUItem.reactorcuriumQuad, "SQS",

                ('S'), IUItem.reactorcuriumDual, ('Q'),
                ("plateIron"), ('C'), ("plateCopper")
        );
        Recipes.recipe.addRecipe(IUItem.reactorcaliforniaDual, "SQS",
                ('S'), IUItem.reactorcaliforniaSimple,
                ('Q'), ("plateIron")
        );
        Recipes.recipe.addRecipe(IUItem.reactorcaliforniaQuad, "SQS", "CQC", "SQS",

                ('S'), IUItem.reactorcaliforniaSimple,
                ('Q'), ("plateIron"), ('C'), ("plateCopper")
        );
        Recipes.recipe.addRecipe(IUItem.reactorcaliforniaQuad, "SQS",

                ('S'), IUItem.reactorcaliforniaDual, ('Q'),
                ("plateIron"), ('C'), ("plateCopper")
        );
        Recipes.recipe.addRecipe(IUItem.reactormendeleviumDual, "SQS",
                ('S'), IUItem.reactormendeleviumSimple,
                ('Q'), ("plateSilver")
        );
        Recipes.recipe.addRecipe(IUItem.reactormendeleviumQuad, "SQS", "CQC", "SQS",

                ('S'), IUItem.reactormendeleviumSimple,
                ('Q'), ("plateSilver"), ('C'), ("plateDuralumin")
        );
        Recipes.recipe.addRecipe(IUItem.reactormendeleviumQuad, "SQS",

                ('S'), IUItem.reactormendeleviumDual, ('Q'),
                ("plateSilver"), ('C'), ("plateDuralumin")
        );
        Recipes.recipe.addRecipe(IUItem.reactorberkeliumDual, "SQS",
                ('S'), IUItem.reactorberkeliumSimple,
                ('Q'), ("plateSilver")
        );
        Recipes.recipe.addRecipe(IUItem.reactorberkeliumQuad, "SQS", "CQC", "SQS",

                ('S'), IUItem.reactorberkeliumSimple,
                ('Q'), ("plateSilver"), ('C'), ("plateDuralumin")
        );
        Recipes.recipe.addRecipe(IUItem.reactorberkeliumQuad, "SQS",

                ('S'), IUItem.reactorberkeliumDual, ('Q'),
                ("plateSilver"), ('C'), ("plateDuralumin")
        );
        Recipes.recipe.addRecipe(IUItem.reactoreinsteiniumDual, "SQS",
                ('S'), IUItem.reactoreinsteiniumSimple,
                ('Q'), ("plateSilver")
        );
        Recipes.recipe.addRecipe(IUItem.reactoreinsteiniumQuad, "SQS", "CQC", "SQS",

                ('S'), IUItem.reactoreinsteiniumSimple,
                ('Q'), ("plateSilver"), ('C'), ("plateDuralumin")
        );
        Recipes.recipe.addRecipe(IUItem.reactoreinsteiniumQuad, "SQS",

                ('S'), IUItem.reactoreinsteiniumDual, ('Q'),
                ("plateSilver"), ('C'), ("plateDuralumin")
        );
        Recipes.recipe.addRecipe(IUItem.reactoruran233Dual, "SQS",
                ('S'), IUItem.reactoruran233Simple,
                ('Q'), ("plateSilver")
        );
        Recipes.recipe.addRecipe(IUItem.reactoruran233Quad, "SQS", "CQC", "SQS",

                ('S'), IUItem.reactoruran233Simple,
                ('Q'), ("plateSilver"), ('C'), ("plateDuralumin")
        );
        Recipes.recipe.addRecipe(IUItem.reactoruran233Quad, "SQS",

                ('S'), IUItem.reactoruran233Dual, ('Q'),
                ("plateSilver"), ('C'), ("plateDuralumin")
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
                ('A'), new ItemStack(Items.DYE)
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
                ("plateCobalt"),
                ('C'),
                new ItemStack(IUItem.hazmat_helmet, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.hazmatchest), "AAA", "BCB", "DDD",

                ('A'),
                ("plateMikhail"),
                ('B'),
                ("platePlatinum"),
                ('D'),
                ("plateCobalt"),
                ('C'),
                new ItemStack(IUItem.hazmat_chestplate, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.hazmatleggins), "AAA", "BCB", "DDD",

                ('A'),
                ("plateMikhail"),
                ('B'),
                ("platePlatinum"),
                ('D'),
                ("plateCobalt"),
                ('C'),
                new ItemStack(IUItem.hazmat_leggings, 1, 32767)
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.hazmatboosts), "AAA", "BCB", "DDD",

                ('A'),
                ("plateMikhail"),
                ('B'),
                ("platePlatinum"),
                ('D'),
                ("plateCobalt"),
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
        Recipes.recipe.addRecipe(new ItemStack(IUItem.spawnermodules, 1, 8), "ECE", "BAB", "DCD",

                ('A'), new ItemStack(IUItem.module_schedule), ('E'), new ItemStack(IUItem.core, 5),
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
                ('A'), Items.STRING, ('B'), "plateCobalt"
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
        BasicRecipeTwo.recipe();
    }

    public static ItemStack getBlockStack(IMultiTileBlock block) {
        return TileBlockCreator.instance.get(block.getIdentifier()).getItemStack(block);
    }

}
