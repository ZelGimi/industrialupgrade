package com.denfop.recipes;


import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.blocks.mechanism.BlockMoreMachine;
import com.denfop.integration.exnihilo.ExNihiloIntegration;
import com.denfop.items.resource.ItemIngots;
import com.denfop.register.RegisterOreDictionary;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.Recipes;
import ic2.core.block.ITeBlock;
import ic2.core.block.TeBlockRegistry;
import ic2.core.ref.BlockName;
import ic2.core.ref.TeBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class BasicRecipe {

    public static ItemStack DEFAULT_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 21);
    public static ItemStack ADV_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 25);
    public static ItemStack IMP_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 23);
    public static ItemStack PER_SENSOR = new ItemStack(IUItem.crafting_elements, 1, 24);

    public static void recipe() {
        if (Loader.isModLoaded("exnihilocreatio")) {
            for (int i = 0; i < IUItem.name_mineral1.size(); i++) {
                if (i != 6 && i != 7 && i != 11) {
                    Recipes.advRecipes.addRecipe(new ItemStack(ExNihiloIntegration.gravel, 1, i),
                            "AA ", "AA ", "   ", 'A', new ItemStack(ExNihiloIntegration.gravel_crushed, 1, i)
                    );
                    Recipes.advRecipes.addRecipe(new ItemStack(ExNihiloIntegration.dust, 1, i),
                            "AA ", "AA ", "   ", 'A', new ItemStack(ExNihiloIntegration.dust_crushed, 1, i)
                    );
                    Recipes.advRecipes.addRecipe(new ItemStack(ExNihiloIntegration.sand, 1, i),
                            "AA ", "AA ", "   ", 'A', new ItemStack(ExNihiloIntegration.sand_crushed, 1, i)
                    );
                }
            }
        }
        for (int i = 0; i < ItemIngots.ItemIngotsTypes.values().length; i++) {
            Recipes.advRecipes.addShapelessRecipe(new ItemStack(IUItem.stik, 2, i), "craftingToolWireCutter",
                    "ingot" + RegisterOreDictionary.list_string.get(i)
            );
            Recipes.advRecipes.addShapelessRecipe(new ItemStack(IUItem.plate, 1, i),
                    "craftingToolForgeHammer", "ingot" + RegisterOreDictionary.list_string.get(i)

            );

            Recipes.advRecipes.addShapelessRecipe(new ItemStack(IUItem.casing, 2, i), "craftingToolForgeHammer",
                    "plate" + RegisterOreDictionary.list_string.get(i)
            );
        }


        // TODO Recipes Proton Rods
        Recipes.advRecipes.addRecipe(
                IUItem.reactorprotonDual, "SQS", 'S',
                IUItem.reactorprotonSimple, 'Q', OreDictionary.getOres("plateIron")
        );
        Recipes.advRecipes.addRecipe(
                IUItem.reactorprotonQuad,
                "SQS", "CQC", "SQS", 'S', IUItem.reactorprotonSimple,
                'Q', OreDictionary.getOres("plateIron"), 'C', OreDictionary.getOres("plateCopper")
        );

        Recipes.advRecipes.addRecipe(
                IUItem.reactorprotonQuad,
                "SQS", 'S', IUItem.reactorprotonDual, 'Q',
                OreDictionary.getOres("plateIron"), 'C', OreDictionary.getOres("plateCopper")
        );
        Recipes.advRecipes.addRecipe(
                IUItem.reactorprotoneit,
                "SQS", 'S', IUItem.reactorprotonQuad, 'Q',
                OreDictionary.getOres("plateIron"), 'C', OreDictionary.getOres("plateCopper")
        );
        Recipes.advRecipes.addRecipe(
                IUItem.reactorprotoneit,
                "SQS", "CQC", "SQS", 'S', IUItem.reactorprotonDual,
                'Q', OreDictionary.getOres("plateIron"), 'C', OreDictionary.getOres("plateCopper")
        );
// TODO Recipes Toriy Rods
        Recipes.advRecipes.addRecipe(
                IUItem.reactortoriyDual, "SQS", 'S',
                IUItem.reactortoriySimple, 'Q', OreDictionary.getOres("plateIron")
        );
        Recipes.advRecipes.addRecipe(
                IUItem.reactortoriyQuad,
                "SQS", "CQC", "SQS", 'S', IUItem.reactortoriySimple,
                'Q', OreDictionary.getOres("plateIron"), 'C', OreDictionary.getOres("plateCopper")
        );

        Recipes.advRecipes.addRecipe(
                IUItem.reactortoriyQuad,
                "SQS", 'S', IUItem.reactortoriyDual, 'Q',
                OreDictionary.getOres("plateIron"), 'C', OreDictionary.getOres("plateCopper")
        );
//
        Recipes.advRecipes.addRecipe(
                IUItem.reactoramericiumDual, "SQS", 'S',
                IUItem.reactoramericiumSimple, 'Q', OreDictionary.getOres("plateIron")
        );
        Recipes.advRecipes.addRecipe(
                IUItem.reactoramericiumQuad,
                "SQS", "CQC", "SQS", 'S', IUItem.reactoramericiumSimple,
                'Q', OreDictionary.getOres("plateIron"), 'C', OreDictionary.getOres("plateCopper")
        );

        Recipes.advRecipes.addRecipe(
                IUItem.reactoramericiumQuad,
                "SQS", 'S', IUItem.reactoramericiumDual, 'Q',
                OreDictionary.getOres("plateIron"), 'C', OreDictionary.getOres("plateCopper")
        );

        //
        //
        Recipes.advRecipes.addRecipe(
                IUItem.reactorneptuniumDual, "SQS", 'S',
                IUItem.reactorneptuniumSimple, 'Q', OreDictionary.getOres("plateIron")
        );
        Recipes.advRecipes.addRecipe(
                IUItem.reactorneptuniumQuad,
                "SQS", "CQC", "SQS", 'S', IUItem.reactorneptuniumSimple,
                'Q', OreDictionary.getOres("plateIron"), 'C', OreDictionary.getOres("plateCopper")
        );

        Recipes.advRecipes.addRecipe(
                IUItem.reactorneptuniumQuad,
                "SQS", 'S', IUItem.reactorneptuniumDual, 'Q',
                OreDictionary.getOres("plateIron"), 'C', OreDictionary.getOres("plateCopper")
        );

        //
        //
        Recipes.advRecipes.addRecipe(
                IUItem.reactorcuriumDual, "SQS", 'S',
                IUItem.reactorcuriumSimple, 'Q', OreDictionary.getOres("plateIron")
        );
        Recipes.advRecipes.addRecipe(
                IUItem.reactorcuriumQuad,
                "SQS", "CQC", "SQS", 'S', IUItem.reactorcuriumSimple,
                'Q', OreDictionary.getOres("plateIron"), 'C', OreDictionary.getOres("plateCopper")
        );

        Recipes.advRecipes.addRecipe(
                IUItem.reactorcuriumQuad,
                "SQS", 'S', IUItem.reactorcuriumDual, 'Q',
                OreDictionary.getOres("plateIron"), 'C', OreDictionary.getOres("plateCopper")
        );

        //
        //
        Recipes.advRecipes.addRecipe(
                IUItem.reactorcaliforniaDual, "SQS", 'S',
                IUItem.reactorcaliforniaSimple, 'Q', OreDictionary.getOres("plateIron")
        );
        Recipes.advRecipes.addRecipe(
                IUItem.reactorcaliforniaQuad,
                "SQS", "CQC", "SQS", 'S', IUItem.reactorcaliforniaSimple,
                'Q', OreDictionary.getOres("plateIron"), 'C', OreDictionary.getOres("plateCopper")
        );

        Recipes.advRecipes.addRecipe(
                IUItem.reactorcaliforniaQuad,
                "SQS", 'S', IUItem.reactorcaliforniaDual, 'Q',
                OreDictionary.getOres("plateIron"), 'C', OreDictionary.getOres("plateCopper")
        );

        //
        //
        Recipes.advRecipes.addRecipe(
                IUItem.reactormendeleviumDual, "SQS", 'S',
                IUItem.reactormendeleviumSimple, 'Q', OreDictionary.getOres("plateSilver")
        );
        Recipes.advRecipes.addRecipe(
                IUItem.reactormendeleviumQuad,
                "SQS", "CQC", "SQS", 'S', IUItem.reactormendeleviumSimple,
                'Q', OreDictionary.getOres("plateSilver"), 'C', OreDictionary.getOres("plateDuralumin")
        );

        Recipes.advRecipes.addRecipe(
                IUItem.reactormendeleviumQuad,
                "SQS", 'S', IUItem.reactormendeleviumDual, 'Q',
                OreDictionary.getOres("plateSilver"), 'C', OreDictionary.getOres("plateDuralumin")
        );

        //
        Recipes.advRecipes.addRecipe(
                IUItem.reactorberkeliumDual, "SQS", 'S',
                IUItem.reactorberkeliumSimple, 'Q', OreDictionary.getOres("plateSilver")
        );
        Recipes.advRecipes.addRecipe(
                IUItem.reactorberkeliumQuad,
                "SQS", "CQC", "SQS", 'S', IUItem.reactorberkeliumSimple,
                'Q', OreDictionary.getOres("plateSilver"), 'C', OreDictionary.getOres("plateDuralumin")
        );

        Recipes.advRecipes.addRecipe(
                IUItem.reactorberkeliumQuad,
                "SQS", 'S', IUItem.reactorberkeliumDual, 'Q',
                OreDictionary.getOres("plateSilver"), 'C', OreDictionary.getOres("plateDuralumin")
        );

        //
        Recipes.advRecipes.addRecipe(
                IUItem.reactoreinsteiniumDual, "SQS", 'S',
                IUItem.reactoreinsteiniumSimple, 'Q', OreDictionary.getOres("plateSilver")
        );
        Recipes.advRecipes.addRecipe(
                IUItem.reactoreinsteiniumQuad,
                "SQS", "CQC", "SQS", 'S', IUItem.reactoreinsteiniumSimple,
                'Q', OreDictionary.getOres("plateSilver"), 'C', OreDictionary.getOres("plateDuralumin")
        );

        Recipes.advRecipes.addRecipe(
                IUItem.reactoreinsteiniumQuad,
                "SQS", 'S', IUItem.reactoreinsteiniumDual, 'Q',
                OreDictionary.getOres("plateSilver"), 'C', OreDictionary.getOres("plateDuralumin")
        );

        //
        Recipes.advRecipes.addRecipe(
                IUItem.reactoruran233Dual, "SQS", 'S',
                IUItem.reactoruran233Simple, 'Q', OreDictionary.getOres("plateSilver")
        );
        Recipes.advRecipes.addRecipe(
                IUItem.reactoruran233Quad,
                "SQS", "CQC", "SQS", 'S', IUItem.reactoruran233Simple,
                'Q', OreDictionary.getOres("plateSilver"), 'C', OreDictionary.getOres("plateDuralumin")
        );

        Recipes.advRecipes.addRecipe(
                IUItem.reactoruran233Quad,
                "SQS", 'S', IUItem.reactoruran233Dual, 'Q',
                OreDictionary.getOres("plateSilver"), 'C', OreDictionary.getOres("plateDuralumin")
        );

        //
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.ruby_boots),
                "   ", "A A", "A A", 'A', OreDictionary.getOres("gemRuby")
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.sapphire_boots),
                "   ", "A A", "A A", 'A', OreDictionary.getOres("gemSapphire")
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.topaz_boots),
                "   ", "A A", "A A", 'A', OreDictionary.getOres("gemTopaz")
        );

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.ruby_chestplate),
                "A A", "AAA", "AAA", 'A', OreDictionary.getOres("gemRuby")
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.sapphire_chestplate),
                "A A", "AAA", "AAA", 'A', OreDictionary.getOres("gemSapphire")
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.topaz_chestplate),
                "A A", "AAA", "AAA", 'A', OreDictionary.getOres("gemTopaz")
        );

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.ruby_leggings),
                "AAA", "A A", "A A", 'A', OreDictionary.getOres("gemRuby")
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.sapphire_leggings),
                "AAA", "A A", "A A", 'A', OreDictionary.getOres("gemSapphire")
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.topaz_leggings),
                "AAA", "A A", "A A", 'A', OreDictionary.getOres("gemTopaz")
        );

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.ruby_helmet),
                "AAA", "A A", "   ", 'A', OreDictionary.getOres("gemRuby")
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.sapphire_helmet),
                "AAA", "A A", "   ", 'A', OreDictionary.getOres("gemSapphire")
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.topaz_helmet),
                "AAA", "A A", "   ", 'A', OreDictionary.getOres("gemTopaz")
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.adv_nano_boots, 1, OreDictionary.WILDCARD_VALUE),
                " A ",
                "BCB",
                "DED",
                'A',
                IUItem.circuitNano,
                'B',
                OreDictionary.getOres("doubleplateRedbrass"),
                'C',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'D',
                new ItemStack(IUItem.compresscarbon),
                'E',
                new ItemStack(Ic2Items.nanoBoots.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.adv_nano_leggings, 1, OreDictionary.WILDCARD_VALUE),
                "DED",
                "BCB",
                "FAF",
                'A',
                IUItem.circuitNano,
                'B',
                OreDictionary.getOres("plateRedbrass"),
                'C',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'D',
                new ItemStack(IUItem.compresscarbon),
                'E',
                new ItemStack(Ic2Items.nanoLeggings.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'F',
                OreDictionary.getOres("plateAluminumbronze")
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.adv_nano_chestplate, 1, OreDictionary.WILDCARD_VALUE),
                "DBD",
                "CEC",
                "FAF",
                'A',
                new ItemStack(Ic2Items.electricJetpack.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.adv_lappack, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'D',
                new ItemStack(IUItem.compresscarbon),
                'E',
                new ItemStack(Ic2Items.nanoBodyarmor.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'F',
                new ItemStack(IUItem.compresscarbonultra)
        );


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.adv_nano_helmet, 1, OreDictionary.WILDCARD_VALUE),
                "DCD",
                "BEB",
                "FGF",
                'A',
                new ItemStack(Ic2Items.electricJetpack.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.compresscarbon, 1),
                'C',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'D',
                OreDictionary.getOres("doubleplateAlcled"),
                'E',
                new ItemStack(Ic2Items.nanoHelmet.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'F',
                OreDictionary.getOres("doubleplateFerromanganese"),
                'G',
                IUItem.circuitNano
        );

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.electricblock, 1, 2),
                "ABA", "CCC", "AAA", 'A', OreDictionary.getOres("plankWood"), 'C', new ItemStack(Ic2Items.reBattery.getItem(), 1,
                        OreDictionary.WILDCARD_VALUE
                ), 'B', Ic2Items.tinCableItem
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.electricblock, 1, 5),
                "ABA",
                "CCC",
                "AAA",
                'A',
                OreDictionary.getOres("plateBronze"),
                'C',
                new ItemStack(Ic2Items.advBattery.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                Ic2Items.insulatedCopperCableItem
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.electricblock, 1, 3),
                "ABA",
                "BCB",
                "ABA",
                'A',
                OreDictionary.getOres("doubleplateAluminumbronze"),
                'C',
                new ItemStack(Ic2Items.energyCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                Ic2Items.machine
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.electricblock, 1, 4),
                "CDC",
                "CAC",
                "CBC",
                'D',
                IUItem.circuitNano,
                'A',
                new ItemStack(IUItem.electricblock, 1, 3),
                'C',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                Ic2Items.advancedMachine
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.electricblock, 1, 0),
                "CDC",
                "DAD",
                "CDC",
                'D',
                new ItemStack(IUItem.photoniy_ingot),
                'A',
                new ItemStack(IUItem.electricblock, 1, 4),
                'C',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                Ic2Items.advancedMachine
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.electricblock, 1, 1),
                "CDC",
                "DAD",
                "CDC",
                'D',
                new ItemStack(IUItem.photoniy_ingot),
                'A',
                new ItemStack(IUItem.electricblock, 1, 0),
                'C',
                IUItem.cirsuitQuantum
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.electricblock, 1, 6),
                "CDC",
                "DAD",
                "CDC",
                'D',
                new ItemStack(IUItem.core, 1, 4),
                'A',
                new ItemStack(IUItem.electricblock, 1, 1),
                'C',
                IUItem.circuitSpectral
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.electricblock, 1, 7),
                "CDC",
                "DAD",
                "CDC",
                'D',
                new ItemStack(IUItem.core, 1, 8),
                'A',
                new ItemStack(IUItem.electricblock, 1, 6),
                'C',
                IUItem.circuitSpectral
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.electricblock, 1, 8),
                "CBC",
                "DAD",
                "CBC",
                'D',
                new ItemStack(IUItem.core, 1, 9),
                'A',
                new ItemStack(IUItem.electricblock, 1, 7),
                'C',
                IUItem.cirsuitQuantum,
                'B',
                new ItemStack(IUItem.lapotronCrystal, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.electricblock, 1, 9),
                "EBE",
                "DAD",
                "CBC",
                'D',
                new ItemStack(IUItem.core, 1, 11),
                'E',
                new ItemStack(IUItem.compressIridiumplate),
                'A',
                new ItemStack(IUItem.electricblock, 1, 8),
                'C',
                IUItem.circuitSpectral,
                'B',
                new ItemStack(IUItem.lapotronCrystal, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.electricblock, 1, 10),
                "EBE",
                "DAD",
                "CBC",
                'D',
                new ItemStack(IUItem.core, 1, 12),
                'E',
                new ItemStack(IUItem.doublecompressIridiumplate),
                'A',
                new ItemStack(IUItem.electricblock, 1, 9),
                'C',
                IUItem.circuitSpectral,
                'B',
                new ItemStack(IUItem.lapotronCrystal, 1, OreDictionary.WILDCARD_VALUE)
        );

        Recipes.advRecipes.addRecipe(
                IUItem.overclockerUpgrade,
                "C C", " A ", "C C", 'C', IUItem.circuitNano, 'A', Ic2Items.overclockerUpgrade
        );
        Recipes.advRecipes.addRecipe(
                IUItem.overclockerUpgrade1,
                "C C", " A ", "C C", 'C', IUItem.cirsuitQuantum, 'A', IUItem.overclockerUpgrade
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spectral_boots),
                "C C",
                " A ",
                "CDC",
                'D',
                new ItemStack(Ic2Items.quantumBoots.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'C',
                new ItemStack(IUItem.adv_spectral_box, 1),
                'A',
                new ItemStack(IUItem.lapotronCrystal, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spectral_boots),
                "C C",
                "BAB",
                "CDC",
                'B',
                Ic2Items.iridiumPlate,
                'D',
                new ItemStack(IUItem.adv_nano_boots, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                new ItemStack(IUItem.adv_spectral_box, 1),
                'A',
                new ItemStack(IUItem.lapotronCrystal, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spectral_leggings),
                "C C",
                " A ",
                "CDC",
                'D',
                new ItemStack(Ic2Items.quantumLeggings.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'C',
                new ItemStack(IUItem.adv_spectral_box, 1),
                'A',
                new ItemStack(IUItem.lapotronCrystal, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spectral_leggings),
                "CDC",
                "BAB",
                "C C",
                'B',
                Ic2Items.iridiumPlate,
                'D',
                new ItemStack(IUItem.adv_nano_leggings, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                new ItemStack(IUItem.adv_spectral_box, 1),
                'A',
                new ItemStack(IUItem.lapotronCrystal, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spectral_helmet),
                "C C",
                " A ",
                "CDC",
                'D',
                new ItemStack(Ic2Items.quantumHelmet.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'C',
                new ItemStack(IUItem.adv_spectral_box, 1),
                'A',
                new ItemStack(IUItem.lapotronCrystal, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spectral_helmet),
                "CDC",
                "BAB",
                "C C",
                'B',
                Ic2Items.iridiumPlate,
                'D',
                new ItemStack(IUItem.adv_nano_helmet, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                new ItemStack(IUItem.adv_spectral_box, 1),
                'A',
                new ItemStack(IUItem.lapotronCrystal, 1, OreDictionary.WILDCARD_VALUE)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spectral_chestplate),
                "CDC",
                "BAB",
                "C C",
                'B',
                Ic2Items.iridiumPlate,
                'D',
                new ItemStack(IUItem.adv_nano_chestplate, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                new ItemStack(IUItem.adv_spectral_box, 1),
                'A',
                new ItemStack(IUItem.lapotronCrystal, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spectral_chestplate),
                "CDC",
                "BAB",
                "C C",
                'B',
                OreDictionary.getOres("doubleplateVitalium"),
                'D',
                new ItemStack(Ic2Items.quantumBodyarmor.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'C',
                new ItemStack(IUItem.adv_spectral_box, 1),
                'A',
                new ItemStack(IUItem.lapotronCrystal, 1, OreDictionary.WILDCARD_VALUE)
        );

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.advnanobox),
                " C ", "CAC", " C ", 'C', new ItemStack(IUItem.photoniy, 1), 'A', new ItemStack(IUItem.nanoBox)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.advQuantumtool),
                "CDC",
                "BAB",
                "CFC",
                'F',
                new ItemStack(IUItem.compressIridiumplate),
                'B',
                OreDictionary.getOres("doubleplateIridium"),
                'D',
                OreDictionary.getOres("casingIridium"),
                'C',
                new ItemStack(IUItem.photoniy_ingot, 1),
                'A',
                new ItemStack(IUItem.advnanobox)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.nanoBox),
                " C ",
                "BAB",
                " C ",
                'B',
                Ic2Items.carbonPlate,
                'C',
                new ItemStack(IUItem.compresscarbon, 1),
                'A',
                new ItemStack(Ic2Items.energyCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.quantumtool),
                "FDF",
                "BAB",
                "CDC",
                'F',
                OreDictionary.getOres("doubleplateIridium"),
                'B',
                new ItemStack(IUItem.compresscarbonultra, 1),
                'D',
                new ItemStack(IUItem.compresscarbon, 1),
                'C',
                Ic2Items.iridiumOre,
                'A',
                new ItemStack(IUItem.nanoBox)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.magnet),
                "A B",
                "CDC",
                " C ",
                'B',
                OreDictionary.getOres("ingotFerromanganese"),
                'D',
                new ItemStack(Ic2Items.advBattery.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'C',
                OreDictionary.getOres("doubleplateMikhail"),
                'A',
                OreDictionary.getOres("ingotVitalium")
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.impmagnet),
                "B B",
                "CDC",
                " C ",
                'B',
                OreDictionary.getOres("ingotDuralumin"),
                'D',
                new ItemStack(IUItem.magnet, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                OreDictionary.getOres("doubleplateInvar")
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.lapotronCrystal),
                "BCB",
                "CDC",
                "BCB",
                'B',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'D',
                new ItemStack(IUItem.photoniy_ingot),
                'C',
                new ItemStack(IUItem.advQuantumtool, 1)
        );

        Recipes.advRecipes.addRecipe(
                IUItem.tranformerUpgrade,
                "BCB",
                "CDC",
                "BCB",
                'B',
                IUItem.circuitNano,
                'D',
                Ic2Items.transformerUpgrade,
                'C',
                OreDictionary.getOres("plateVitalium")
        );

        Recipes.advRecipes.addRecipe(
                IUItem.tranformerUpgrade1,
                "BCB",
                "CDC",
                "BCB",
                'B',
                IUItem.cirsuitQuantum,
                'D',
                IUItem.tranformerUpgrade,
                'C',
                OreDictionary.getOres("plateAlcled")
        );

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.nanopickaxe),
                "ACA", "CDC", "BFB", 'F', new ItemStack(Ic2Items.energyCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE), 'B'
                , OreDictionary.getOres("doubleplateFerromanganese"), 'D', new ItemStack(Items.DIAMOND_PICKAXE), 'C',
                IUItem.circuitNano, 'A', IUItem.advnanobox
        );

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.nanoaxe),
                "ACA",
                "CDC",
                "BFB",
                'F',
                new ItemStack(Ic2Items.energyCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B'
                ,
                OreDictionary.getOres("doubleplateFerromanganese"),
                'D',
                new ItemStack(Items.DIAMOND_AXE),
                'C',
                IUItem.circuitNano, 'A', IUItem.advnanobox
        );

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.nanoshovel),
                "ACA",
                "CDC",
                "BFB",
                'F',
                new ItemStack(Ic2Items.energyCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B'
                ,
                OreDictionary.getOres("doubleplateFerromanganese"),
                'D',
                new ItemStack(Items.DIAMOND_SHOVEL),
                'C',
                IUItem.circuitNano, 'A', IUItem.advnanobox
        );


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.quantumpickaxe),
                "TCT",
                "CDC",
                "BFB",
                'T',
                OreDictionary.getOres("doubleplateMuntsa"),
                'F',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.advQuantumtool, 1),
                'D',
                new ItemStack(IUItem.nanopickaxe, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                IUItem.cirsuitQuantum
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.quantumaxe),
                "TCT",
                "CDC",
                "BFB",
                'T',
                OreDictionary.getOres("doubleplateMuntsa"),
                'F',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.advQuantumtool, 1),
                'D',
                new ItemStack(IUItem.nanoaxe, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                IUItem.cirsuitQuantum
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.quantumshovel),
                "TCT",
                "CDC",
                "BFB",
                'T',
                OreDictionary.getOres("doubleplateMuntsa"),
                'F',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.advQuantumtool, 1),
                'D',
                new ItemStack(IUItem.nanoshovel, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                IUItem.cirsuitQuantum
        );


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spectralpickaxe),
                "TCT",
                "CDC",
                "BFB",
                'T',
                Ic2Items.iridiumPlate,
                'F',
                new ItemStack(IUItem.advBatChargeCrystal, 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.adv_spectral_box, 1),
                'D',
                new ItemStack(IUItem.quantumpickaxe, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                IUItem.circuitSpectral
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spectralaxe),
                "TCT",
                "CDC",
                "BFB",
                'T',
                Ic2Items.iridiumPlate,
                'F',
                new ItemStack(IUItem.advBatChargeCrystal, 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.adv_spectral_box, 1),
                'D',
                new ItemStack(IUItem.quantumaxe, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                IUItem.circuitSpectral
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spectralshovel),
                "TCT",
                "CDC",
                "BFB",
                'T',
                Ic2Items.iridiumPlate,
                'F',
                new ItemStack(IUItem.advBatChargeCrystal, 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.adv_spectral_box, 1),
                'D',
                new ItemStack(IUItem.quantumshovel, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                IUItem.circuitSpectral
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.advBatChargeCrystal),
                "BCB",
                "BAB",
                "BCB",
                'B',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'A',
                new ItemStack(Ic2Items.chargingLapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'C',
                IUItem.cirsuitQuantum
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.itemBatChargeCrystal),
                "DCD",
                "BAB",
                "ECE",
                'E',
                OreDictionary.getOres("doubleplateVitalium"),
                'D',
                Ic2Items.iridiumPlate,
                'B',
                new ItemStack(IUItem.lapotronCrystal, 1, OreDictionary.WILDCARD_VALUE),
                'A',
                new ItemStack(IUItem.advBatChargeCrystal, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                IUItem.circuitSpectral
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.perfect_drill),
                "ACB",
                "FDF",
                "ECE",
                'E',
                new ItemStack(IUItem.advQuantumtool),
                'F',
                IUItem.overclockerUpgrade1,
                'A',
                new ItemStack(IUItem.spectralaxe, 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.spectralshovel, 1, OreDictionary.WILDCARD_VALUE),
                'D',
                new ItemStack(IUItem.spectralpickaxe, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                IUItem.circuitSpectral
        );

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.quantumSaber),
                "AB ", "AC ", "DEB", 'C', new ItemStack(Ic2Items.nanoSaber.getItem(), 1, OreDictionary.WILDCARD_VALUE), 'E',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE), 'D',
                new ItemStack(Blocks.GLOWSTONE), 'B', IUItem.cirsuitQuantum, 'A', new ItemStack(IUItem.compresscarbon)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spectralSaber),
                "AB ",
                "AC ",
                "DEB",
                'C',
                new ItemStack(IUItem.quantumSaber, 1, OreDictionary.WILDCARD_VALUE),
                'E',
                new ItemStack(IUItem.lapotronCrystal, 1, OreDictionary.WILDCARD_VALUE),
                'D',
                new ItemStack(Blocks.GLOWSTONE),
                'B',
                IUItem.circuitSpectral,
                'A',
                new ItemStack(IUItem.compressIridiumplate)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.GraviTool),
                "ABA",
                "CDE",
                "FGF",
                'G',
                new ItemStack(IUItem.purifier, 1, OreDictionary.WILDCARD_VALUE),
                'F',
                IUItem.circuitNano,
                'D',
                new ItemStack(Ic2Items.energyCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'E',
                new ItemStack(Ic2Items.electricTreetap.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'C',
                new ItemStack(Ic2Items.electricWrench.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(Ic2Items.electricHoe.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'A',
                OreDictionary.getOres("doubleplateMuntsa")
        );

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.purifier),
                "   ", " B ", "A  ", 'A', new ItemStack(Ic2Items.powerunitsmall.getItem(), 1, 11), 'B', new ItemStack(Blocks.WOOL)
        );

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.itemiu, 1, 3),
                "MDM", "MXM", "MDM", 'D', Ic2Items.advancedCircuit, 'M', new ItemStack(IUItem.itemiu, 1, 1), 'X',
                new ItemStack(Ic2Items.reactorReflector.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );


        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.nano_bow),
                "C C", "BAB", "EDE", 'E',
                IUItem.advnanobox, 'D', new ItemStack(Ic2Items.reBattery.getItem(), 1, OreDictionary.WILDCARD_VALUE), 'C',
                IUItem.circuitNano,
                'B',
                Ic2Items.carbonPlate, 'A',
                new ItemStack(Items.BOW)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.quantum_bow),
                "ABA",
                "CDC",
                "EBE",
                'E',
                OreDictionary.getOres("doubleplateAlcled"),
                'C',
                IUItem.cirsuitQuantum,
                'D',
                new ItemStack(IUItem.nano_bow, 1, OreDictionary.WILDCARD_VALUE),
                'A',
                Ic2Items.iridiumPlate,
                'B',
                new ItemStack(IUItem.advQuantumtool)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spectral_bow),
                "ABA",
                "CDC",
                "EBE",
                'E',
                OreDictionary.getOres("doubleplateDuralumin"),
                'C',
                IUItem.circuitSpectral,
                'D',
                new ItemStack(IUItem.quantum_bow, 1, OreDictionary.WILDCARD_VALUE),
                'A',
                new ItemStack(IUItem.compressIridiumplate),
                'B',
                new ItemStack(IUItem.adv_spectral_box)
        );

        int[] meta = {3, 4, 0, 1, 6, 7, 8, 9, 10};
        for (int i = 0; i < 9; i++) {
            Recipes.advRecipes.addShapelessRecipe(new ItemStack(IUItem.UpgradeKit, 1, i),
                    Ic2Items.wrench, new ItemStack(IUItem.electricblock, 1, meta[i])
            );
        }

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.solidmatter, 1, 0),
                "ABA",
                "CDC",
                "EBE",
                'E',
                new ItemStack(IUItem.core, 1, 5),
                'D',
                new ItemStack(IUItem.machines, 1, 3),
                'C',
                OreDictionary.getOres("plateCobalt"),
                'B',
                OreDictionary.getOres("casingElectrum"),
                'A',
                OreDictionary.getOres("casingIridium")
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.solidmatter, 1, 1),
                "ABA",
                "CDC",
                "EBE",
                'E',
                new ItemStack(IUItem.core, 1, 5),
                'D',
                new ItemStack(IUItem.machines, 1, 3),
                'C',
                OreDictionary.getOres("platePlatinum"),
                'B',
                OreDictionary.getOres("casingMagnesium"),
                'A',
                OreDictionary.getOres("casingCobalt")
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.solidmatter, 1, 2),
                "ABA",
                "CDC",
                "EBE",
                'E',
                new ItemStack(IUItem.core, 1, 5),
                'D',
                new ItemStack(IUItem.machines, 1, 3),
                'C',
                OreDictionary.getOres("doubleplateAlcled"),
                'B',
                OreDictionary.getOres("casingChromium"),
                'A',
                OreDictionary.getOres("casingMikhail")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.solidmatter, 1, 3),
                "ABA",
                "CDC",
                "EBE",
                'E',
                new ItemStack(IUItem.core, 1, 5),
                'D',
                new ItemStack(IUItem.machines, 1, 3),
                'C',
                OreDictionary.getOres("doubleplateDuralumin"),
                'B',
                OreDictionary.getOres("casingCaravky"),
                'A',
                OreDictionary.getOres("casingVanadium")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.solidmatter, 1, 4),
                "ABA",
                "CDC",
                "EBE",
                'E',
                new ItemStack(IUItem.core, 1, 5),
                'D',
                new ItemStack(IUItem.machines, 1, 3),
                'C',
                OreDictionary.getOres("doubleplateManganese"),
                'B',
                OreDictionary.getOres("casingSpinel"),
                'A',
                OreDictionary.getOres("casingAluminium")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.solidmatter, 1, 5),
                "ABA",
                "CDC",
                "EBE",
                'E',
                new ItemStack(IUItem.core, 1, 5),
                'D',
                new ItemStack(IUItem.machines, 1, 3),
                'C',
                OreDictionary.getOres("doubleplateTitanium"),
                'B',
                OreDictionary.getOres("casingInvar"),
                'A',
                OreDictionary.getOres("casingTungsten")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.solidmatter, 1, 6),
                "ABA",
                "CDC",
                "EBE",
                'E',
                new ItemStack(IUItem.core, 1, 5),
                'D',
                new ItemStack(IUItem.machines, 1, 3),
                'C',
                OreDictionary.getOres("doubleplateRedbrass"),
                'B',
                OreDictionary.getOres("casingChromium"),
                'A',
                OreDictionary.getOres("casingManganese")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.solidmatter, 1, 7),
                "ABA",
                "CDC",
                "EBE",
                'E',
                new ItemStack(IUItem.core, 1, 5),
                'D',
                new ItemStack(IUItem.machines, 1, 3),
                'C',
                OreDictionary.getOres("doubleplateAlumel"),
                'B',
                OreDictionary.getOres("casingCobalt"),
                'A',
                OreDictionary.getOres("casingVanadium")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module_schedule),
                "ABA",
                "EDE",
                "CBC",
                'D',
                OreDictionary.getOres("ingotCaravky"),
                'E',
                OreDictionary.getOres("plateZinc"),
                'C',
                new ItemStack(IUItem.plastic_plate),
                'B',
                new ItemStack(IUItem.plastic_plate),
                'A',
                OreDictionary.getOres("plateVanadium")
        );


        Recipes.advRecipes.addRecipe(Ic2Items.electricJetpack, "ADA", "ACA", "B B", 'A',
                Ic2Items.casingiron, 'B', Items.GLOWSTONE_DUST, 'C', new ItemStack(IUItem.electricblock, 1, 2), 'D',
                Ic2Items.advancedCircuit
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module7, 1, 1),
                "ABA",
                "BCB",
                "DDD",
                'D',
                new ItemStack(IUItem.doublecompressIridiumplate),
                'C',
                new ItemStack(IUItem.advQuantumtool),
                'B',
                IUItem.circuitSpectral,
                'A',
                new ItemStack(IUItem.core, 1, 5)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module7, 1, 2),
                "ABA",
                "BCB",
                "DDD",
                'D',
                new ItemStack(IUItem.doublecompressIridiumplate),
                'C',
                new ItemStack(IUItem.advnanobox),
                'B',
                IUItem.circuitSpectral,
                'A',
                new ItemStack(IUItem.core, 1, 5)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module7, 1, 3),
                "AAA",
                "BCB",
                "EFE",
                'F',
                OreDictionary.getOres("doubleplateAlcled"),
                'E',
                IUItem.circuitSpectral,
                'C',
                new ItemStack(IUItem.module_schedule),
                'B',
                OreDictionary.getOres("doubleplateVitalium"),
                'A',
                new ItemStack(IUItem.compresscarbon)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module7, 1, 4),
                "AAA",
                "BCB",
                "EFE",
                'F',
                OreDictionary.getOres("doubleplateDuralumin"),
                'E',
                IUItem.circuitNano,
                'C',
                new ItemStack(IUItem.module_schedule),
                'B',
                OreDictionary.getOres("doubleplateVanadoalumite"),
                'A',
                new ItemStack(IUItem.compresscarbonultra)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.module7, 1, 5),
                "ABA", 'B', IUItem.module8, 'A', new ItemStack(IUItem.module7, 1, 3)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.module7, 1, 6),
                "AB", 'B', IUItem.module8, 'A', new ItemStack(IUItem.module7, 1, 3)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.module7, 1, 7),
                "ABA", 'B', IUItem.module8, 'A', new ItemStack(IUItem.module7, 1, 8)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.module7, 1, 8),
                "ABA", 'B', new ItemStack(IUItem.module7, 1, 4), 'A', new ItemStack(IUItem.module7, 1, 6)
        );


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module9),
                "BCA",
                "DED",
                "BCA",
                'E',
                IUItem.cirsuitQuantum,
                'D',
                OreDictionary.getOres("doubleplateSilver"),
                'C',
                OreDictionary.getOres("doubleplateElectrum"),
                'B',
                OreDictionary.getOres("doubleplateRedbrass"),
                'A',
                OreDictionary.getOres("doubleplateAlcled")
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module9, 1, 1),
                "ABA",
                "DED",
                "ABA",
                'E',
                IUItem.overclockerUpgrade1,
                'D',
                new ItemStack(IUItem.core, 1, 1),
                'B',
                OreDictionary.getOres("doubleplateVanadoalumite"),
                'A',
                OreDictionary.getOres("doubleplateAlcled")
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module9, 1, 2),
                "ABA",
                "CEC",
                "ABA",
                'E',
                IUItem.circuitNano,
                'C',
                new ItemStack(IUItem.module9, 1, 1),
                'B',
                new ItemStack(IUItem.nanoBox, 1),
                'A',
                new ItemStack(IUItem.core, 1, 2)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module9, 1, 3),
                "ABA",
                "CEC",
                "ABA",
                'E',
                new ItemStack(IUItem.quantumtool, 1),
                'C',
                new ItemStack(IUItem.module9, 1, 2),
                'B',
                new ItemStack(IUItem.photoniy_ingot, 1),
                'A',
                new ItemStack(IUItem.core, 1, 4)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module9, 1, 4),
                "ABA",
                "CEC",
                "ABA",
                'E',
                IUItem.circuitSpectral,
                'C',
                new ItemStack(IUItem.module9, 1, 3),
                'B',
                new ItemStack(IUItem.advQuantumtool, 1),
                'A',
                new ItemStack(IUItem.core, 1, 6)
        );


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module9, 1, 5),
                "ABA",
                "BCB",
                "ABA",
                'C',
                new ItemStack(IUItem.doublecompressIridiumplate),
                'B',
                new ItemStack(IUItem.module9, 1, 4),
                'A',
                new ItemStack(IUItem.core, 1, 7)
        );

        Recipes.advRecipes.addRecipe(
                IUItem.module8,
                "AAA",
                "BCB",
                "DED",
                'E',
                new ItemStack(IUItem.compressIridiumplate),
                'D',
                OreDictionary.getOres("doubleplatePlatinum"),
                'C',
                new ItemStack(IUItem.block, 1, 2),
                'B',
                IUItem.circuitNano,
                'A',
                OreDictionary.getOres("plateZinc")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module7, 1, 9),
                "ABA",
                "BCB",
                "ABA",
                'C',
                new ItemStack(IUItem.purifier, 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.nanoBox),
                'A',
                Ic2Items.advancedCircuit
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module9, 1, 9),
                "ABA",
                "BCB",
                "ABA",
                'C',
                IUItem.circuitSpectral,
                'B',
                new ItemStack(IUItem.photoniy, 1),
                'A',
                new ItemStack(IUItem.core, 1, 3)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module9, 1, 10),
                "ABA",
                "BCB",
                "ABA",
                'C',
                IUItem.circuitSpectral,
                'B',
                new ItemStack(IUItem.photoniy_ingot, 1),
                'A',
                new ItemStack(IUItem.module9, 1, 9)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module9, 1, 11),
                "ABA",
                "BCB",
                "ABA",
                'C',
                IUItem.circuitSpectral,
                'B',
                new ItemStack(IUItem.photoniy_ingot, 1),
                'A',
                new ItemStack(IUItem.module9, 1, 10)
        );

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.module9, 1, 12),
                "ABA", "BCB", "ABA", 'C', IUItem.circuitSpectral, 'B', new ItemStack(Blocks.REDSTONE_BLOCK, 1), 'A',
                new ItemStack(Items.PAPER, 1)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module9, 1, 6),
                "ABA",
                "BCB",
                "ABA",
                'C',
                IUItem.circuitSpectral,
                'B',
                new ItemStack(IUItem.photoniy, 1),
                'A',
                new ItemStack(IUItem.core, 1, 4)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module9, 1, 7),
                "ABA",
                "DCD",
                "ABA",
                'D',
                new ItemStack(IUItem.module9, 1, 6),
                'C',
                IUItem.circuitSpectral,
                'B',
                new ItemStack(IUItem.photoniy, 1),
                'A',
                new ItemStack(IUItem.core, 1, 4)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module9, 1, 8),
                "ABA",
                "DCD",
                "ABA",
                'D',
                new ItemStack(IUItem.module9, 1, 7),
                'C',
                IUItem.circuitSpectral,
                'B',
                new ItemStack(IUItem.doublecompressIridiumplate, 1),
                'A',
                new ItemStack(IUItem.core, 1, 5)
        );

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.module9, 1, 13),
                "A A", " C ", "A A", 'C', IUItem.circuitSpectral, 'A', new ItemStack(Items.PAPER, 1)
        );

        Recipes.advRecipes.addRecipe(
                IUItem.module1,
                "AAA",
                "BCB",
                "EDE",
                'E',
                new ItemStack(IUItem.plastic_plate),
                'D',
                OreDictionary.getOres("doubleplateVitalium"),
                'C',
                IUItem.cirsuitQuantum,
                'B',
                OreDictionary.getOres("plateCobalt"),
                'A',
                OreDictionary.getOres("plateElectrum")
        );
        Recipes.advRecipes.addRecipe(
                IUItem.module2,
                "AAA",
                "BCB",
                "EDE",
                'E',
                new ItemStack(IUItem.plastic_plate),
                'D',
                OreDictionary.getOres("doubleplateVitalium"),
                'C',
                IUItem.cirsuitQuantum,
                'B',
                OreDictionary.getOres("doubleplateRedbrass"),
                'A',
                OreDictionary.getOres("doubleplateFerromanganese")
        );

        Recipes.advRecipes.addRecipe(
                IUItem.module3,
                "AAA",
                "BCB",
                "EDE",
                'E',
                new ItemStack(IUItem.plastic_plate),
                'D',
                OreDictionary.getOres("doubleplateVitalium"),
                'C',
                IUItem.cirsuitQuantum,
                'B',
                OreDictionary.getOres("doubleplateAlumel"),
                'A',
                OreDictionary.getOres("plateFerromanganese")
        );

        Recipes.advRecipes.addRecipe(
                IUItem.module4,
                "AAA",
                "BCB",
                "EDE",
                'E',
                new ItemStack(IUItem.plastic_plate),
                'D',
                OreDictionary.getOres("doubleplateVitalium"),
                'C',
                IUItem.cirsuitQuantum,
                'B',
                OreDictionary.getOres("doubleplateMuntsa"),
                'A',
                new ItemStack(IUItem.doublecompressIridiumplate, 1)
        );
        for (int i = 0; i < 7; i++) {
            Recipes.advRecipes.addRecipe(
                    new ItemStack(IUItem.module5, 1, i),
                    "BBB",
                    "ACA",
                    "ADA",
                    'A',
                    new ItemStack(IUItem.lens, 1, i),
                    'B',
                    new ItemStack(IUItem.doublecompressIridiumplate),
                    'C',
                    IUItem.circuitSpectral,
                    'D',
                    new ItemStack(IUItem.advQuantumtool)
            );
        }

        for (int i = 0; i < 14; i++) {

            Recipes.advRecipes.addShapelessRecipe(
                    new ItemStack(IUItem.module6, 1, i),
                    new ItemStack(IUItem.blockpanel, 1, i)
            );
            ItemStack stack1 = new ItemStack(IUItem.module6, 1, i);

            Recipes.advRecipes.addShapelessRecipe(
                    new ItemStack(IUItem.blockpanel, 1, i),
                    stack1
            );
        }
        Recipes.advRecipes.addShapelessRecipe(new ItemStack(Ic2Items.reactorCondensatorLap.getItem(), 1, 1),
                new ItemStack(Ic2Items.reactorCondensatorLap.getItem(), 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Blocks.
                        LAPIS_BLOCK)
        );
        Recipes.advRecipes.addShapelessRecipe(
                new ItemStack(Ic2Items.reactorCondensator.getItem(), 1, 1),
                new ItemStack(Ic2Items.reactorCondensator.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                new ItemStack(Blocks.REDSTONE_BLOCK)
        );

        for (int i = 0; i < RegisterOreDictionary.list_string.size(); i++) {
            if (i < 16) {
                Recipes.advRecipes.addShapelessRecipe(
                        new ItemStack(IUItem.iuingot, 9, i),
                        new ItemStack(IUItem.block, 1, i)
                );
            } else {
                Recipes.advRecipes.addShapelessRecipe(
                        new ItemStack(IUItem.iuingot, 9, i),
                        new ItemStack(IUItem.block1, 1, i - 16)
                );

            }
            Recipes.advRecipes.addShapelessRecipe(
                    new ItemStack(IUItem.nugget, 9, i),
                    new ItemStack(IUItem.iuingot, 1, i)
            );
            List<ItemStack> lst = OreDictionary.getOres("ingot" + RegisterOreDictionary.list_string.get(i));
            if (i < 16) {
                for (ItemStack t : lst) {
                    t.setCount(1);
                }
                Recipes.advRecipes.addRecipe(new ItemStack(IUItem.block, 1, i),
                        "AAA", "AAA", "AAA", 'A', lst
                );
            } else {
                for (ItemStack t : lst) {
                    t.setCount(1);
                }
                Recipes.advRecipes.addRecipe(new ItemStack(IUItem.block1, 1, i - 16),
                        "AAA", "AAA", "AAA", 'A', lst
                );

            }
            Recipes.advRecipes.addRecipe(new ItemStack(IUItem.iuingot, 1, i),
                    "AAA", "AAA", "AAA", 'A', new ItemStack(IUItem.nugget, 1, i)
            );
            Recipes.advRecipes.addRecipe(new ItemStack(IUItem.smalldust, 1, i),
                    "AAA", "AAA", "AAA", 'A', new ItemStack(IUItem.verysmalldust, 1, i)
            );
            Recipes.advRecipes.addRecipe(new ItemStack(IUItem.iudust, 1, i),
                    "AAA", "AAA", "AAA", 'A', new ItemStack(IUItem.smalldust, 1, i)
            );

        }

        for (int i = 0; i < RegisterOreDictionary.list_string1.size(); i++) {
            Recipes.advRecipes.addShapelessRecipe(
                    new ItemStack(IUItem.alloysingot, 9, i),
                    new ItemStack(IUItem.alloysblock, 1, i)
            );
            Recipes.advRecipes.addShapelessRecipe(
                    new ItemStack(IUItem.alloysnugget, 9, i),
                    new ItemStack(IUItem.alloysingot, 1, i)
            );
            Recipes.advRecipes.addRecipe(new ItemStack(IUItem.alloysblock, 1, i),
                    "AAA", "AAA", "AAA", 'A', OreDictionary.getOres("ingot" + RegisterOreDictionary.list_string1.get(i))
            );
            Recipes.advRecipes.addRecipe(new ItemStack(IUItem.alloysingot, 1, i),
                    "AAA", "AAA", "AAA", 'A', new ItemStack(IUItem.alloysnugget, 1, i)
            );
        }
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.cable, 1, 0),
                " A ", "BBB", " A ", 'A', Ic2Items.glassFiberCableItem, 'B', new ItemStack(IUItem.itemiu, 1, 0)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.cable, 1, 1),
                " A ",
                "BCB",
                " A ",
                'C',
                OreDictionary.getOres("ingotCobalt"),
                'A',
                new ItemStack(IUItem.cable, 1, 0),
                'B',
                Ic2Items.denseplatetin
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.cable, 1, 2),
                " A ",
                "BCB",
                " A ",
                'C',
                Ic2Items.denseplatetin,
                'A',
                new ItemStack(IUItem.cable, 1, 1),
                'B',
                Ic2Items.advancedAlloy
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.cable, 1, 3),
                "DAD",
                "BCB",
                "DAD",
                'D',
                Ic2Items.denseplategold,
                'C',
                Ic2Items.advancedAlloy,
                'A',
                new ItemStack(IUItem.cable, 1, 2),
                'B',
                Ic2Items.denseplatelead
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.cable, 1, 4),
                "DAD",
                "BCB",
                "DAD",
                'D',
                OreDictionary.getOres("ingotRedbrass"),
                'C',
                Ic2Items.carbonPlate,
                'A',
                new ItemStack(IUItem.cable, 1, 3),
                'B',
                OreDictionary.getOres("ingotSpinel")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.cable, 1, 5),
                " A ",
                "BCB",
                " A ",
                'C',
                OreDictionary.getOres("doubleplateVitalium"),
                'A',
                new ItemStack(IUItem.cable, 1, 4),
                'B',
                Ic2Items.denseplateadviron
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.cable, 1, 6),
                "DAD",
                "BCB",
                "DAD",
                'D',
                Ic2Items.carbonPlate,
                'C',
                OreDictionary.getOres("ingotAlcled"),
                'A',
                new ItemStack(IUItem.cable, 1, 5),
                'B',
                OreDictionary.getOres("ingotDuralumin")
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.cable, 1, 7),
                "A A",
                "BCB",
                "A A",
                'C',
                new ItemStack(IUItem.photoniy_ingot),
                'B',
                new ItemStack(IUItem.photoniy),
                'A',
                new ItemStack(IUItem.cable, 1, 6)
        );

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.cable, 1, 8),
                "BBB", "AAA", "BBB", 'A', new ItemStack(IUItem.photoniy_ingot), 'B', new ItemStack(IUItem.cable, 1, 7)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.cable, 1, 9),
                "BBB",
                "ACA",
                "BBB",
                'C',
                new ItemStack(IUItem.basecircuit, 1, 10),
                'A',
                new ItemStack(IUItem.photoniy_ingot),
                'B',
                new ItemStack(IUItem.cable, 1, 8)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basecircuit, 1, 15),
                "BAB",
                "ADA",
                "CAC",
                'D',
                new ItemStack(Items.FLINT),
                'C',
                new ItemStack(Items.REDSTONE),
                'A',
                OreDictionary.getOres("plateIron"),
                'B',
                new ItemStack(Items.GOLD_INGOT)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.basecircuit, 1, 16),
                "BBB", "BAB", "BBB", 'B', new ItemStack(IUItem.stik, 1, 15), 'A', new ItemStack(IUItem.basecircuit, 1, 15)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.basecircuit, 1, 17),
                "CCC", "A A", "DDD", 'D', OreDictionary.getOres("plateSilver"), 'C', ModUtils.getCable(Ic2Items.copperCableItem
                        , 1),
                'A', new ItemStack(IUItem.basecircuit, 1, 15)
        );
        Recipes.advRecipes.addRecipe(Ic2Items.electronicCircuit,
                "EDE", "A A", "FBF", 'D', new ItemStack(IUItem.basecircuit, 1, 17), 'B', new ItemStack(IUItem.basecircuit, 1, 16),
                'A', new ItemStack(IUItem.basecircuit, 1, 15), 'E', ModUtils.getCable(Ic2Items.copperCableItem, 1), 'F',
                new ItemStack(Items.IRON_INGOT)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.cable, 1, 10),
                "BBB", "ACA", "BBB", 'C', OreDictionary.getOres("doubleplateVanadoalumite"), 'A',
                new ItemStack(IUItem.basecircuit, 1, 11), 'B', new ItemStack(IUItem.cable, 1, 9)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.basemachine2, 1, 27),
                " D ", "ABA", " D ", 'D', OreDictionary.getOres("plateGermanium"), 'B', new ItemStack(Blocks.CHEST),
                'A', new ItemStack(IUItem.quantumtool)
        );
        Recipes.advRecipes.addRecipe(
                IUItem.reactorCoolanttwelve,
                "BBB",
                "ACA",
                "BBB",
                'C',
                OreDictionary.getOres("plateIron"),
                'B',
                OreDictionary.getOres("plateTin"),
                'A',
                new ItemStack(Ic2Items.reactorCoolantSix.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                IUItem.reactorCoolantmax,
                "BBB",
                "ACA",
                "BBB",
                'C',
                OreDictionary.getOres("plateIron"),
                'B',
                OreDictionary.getOres("plateTin"),
                'A',
                new ItemStack(IUItem.reactorCoolanttwelve.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.adv_lappack),
                "ABA",
                "CEC",
                "ADA",
                'E',
                IUItem.circuitNano,
                'D',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'C',
                OreDictionary.getOres("plateVanadoalumite"),
                'B',
                new ItemStack(Ic2Items.energyPack.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'A',
                OreDictionary.getOres("plateAlcled")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.imp_lappack),
                "ABA",
                "CEC",
                "ABA",
                'E',
                new ItemStack(IUItem.adv_lappack, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                IUItem.circuitNano,
                'B',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'A',
                OreDictionary.getOres("doubleplateFerromanganese")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.per_lappack),
                "ABA",
                "CEC",
                "ABA",
                'E',
                new ItemStack(IUItem.imp_lappack, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                IUItem.cirsuitQuantum,
                'B',
                new ItemStack(IUItem.compressIridiumplate, 1),
                'A',
                OreDictionary.getOres("doubleplateVitalium")
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.advancedSolarHelmet),
                " A ",
                "BCB",
                "DED",
                'D',
                IUItem.circuitNano,
                'E',
                new ItemStack(IUItem.compresscarbonultra),
                'C',
                new ItemStack(IUItem.adv_nano_helmet, 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.compresscarbon),
                'A',
                new ItemStack(IUItem.blockpanel)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.hybridSolarHelmet),
                " A ",
                "BCB",
                "DED",
                'D',
                IUItem.cirsuitQuantum,
                'E',
                new ItemStack(IUItem.compresscarbonultra),
                'C',
                new ItemStack(Ic2Items.quantumHelmet.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                Ic2Items.iridiumPlate,
                'A',
                new ItemStack(IUItem.blockpanel, 1, 1)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.ultimateSolarHelmet),
                " A ",
                "DCD",
                "BEB",
                'D',
                IUItem.cirsuitQuantum,
                'E',
                new ItemStack(IUItem.compresscarbonultra),
                'C',
                new ItemStack(IUItem.hybridSolarHelmet, 1, OreDictionary.WILDCARD_VALUE),
                'B',
                Ic2Items.iridiumPlate,
                'A',
                new ItemStack(IUItem.blockpanel, 1, 2)
        );
//
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewind, 1, 0),
                " A ",
                "ABA",
                " A ",
                'A',
                "logWood",
                'B',
                "plankWood"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewind, 1, 1),
                " A ",
                "ABA",
                " A ",
                'A',
                "blockBronze",
                'B',
                new ItemStack(IUItem.rotor_wood, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewind, 1, 2),
                " A ",
                "ABA",
                " A ",
                'A',
                "blockIron",
                'B',
                new ItemStack(IUItem.rotor_bronze, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewind, 1, 3),
                " A ",
                "ABA",
                " A ",
                'A',
                "plateSteel",
                'B',
                new ItemStack(IUItem.rotor_iron, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewind, 1, 4),
                " A ",
                "ABA",
                " A ",
                'A',
                "plateCarbon",
                'B',
                new ItemStack(IUItem.rotor_steel, 1, OreDictionary.WILDCARD_VALUE)
        );
        //
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewind, 1, 5),
                "CAC",
                "ABA",
                "CAC", 'C',
                IUItem.compressIridiumplate,
                'A',
                Ic2Items.iridiumPlate,
                'B',
                new ItemStack(IUItem.rotor_carbon, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewind, 1, 6),
                "CAC",
                "ABA",
                "CAC", 'C',
                IUItem.doublecompressIridiumplate,
                'A',
                new ItemStack(IUItem.compresscarbon),
                'B',
                new ItemStack(IUItem.iridium.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewind, 1, 7),
                "DCD",
                "ABA",
                "DCD", 'D',
                IUItem.circuitSpectral,
                'C',
                new ItemStack(IUItem.compressIridiumplate),
                'A',
                new ItemStack(IUItem.advnanobox),
                'B',
                new ItemStack(IUItem.compressiridium.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewind, 1, 8),
                "DCD",
                "ABA",
                " C ", 'D',
                new ItemStack(IUItem.excitednucleus, 1, 5),
                'C',
                IUItem.cirsuitQuantum,
                'A',
                new ItemStack(IUItem.quantumtool),
                'B',
                new ItemStack(IUItem.spectral.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewind, 1, 10),
                "DCD",
                "CBC",
                " C ", 'D',
                new ItemStack(IUItem.excitednucleus, 1, 6),
                'C',
                new ItemStack(IUItem.quantumtool),
                'B',
                new ItemStack(IUItem.myphical.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewind, 1, 9),
                "DCD",
                "CBC",
                "ACA", 'D',
                new ItemStack(IUItem.neutroniumingot),
                'A',
                Ic2Items.iridiumPlate,
                'C',
                IUItem.cirsuitQuantum,
                'B',
                new ItemStack(IUItem.photon.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewind, 1, 11),
                "DCD",
                "CBC",
                " C ", 'D',
                new ItemStack(IUItem.excitednucleus, 1, 5),
                'C',
                new ItemStack(IUItem.advQuantumtool),
                'B',
                new ItemStack(IUItem.neutron.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewind, 1, 12),
                "ACA",
                "CBC",
                "ACA",
                'A',
                new ItemStack(IUItem.doublecompressIridiumplate),
                'C',
                IUItem.circuitSpectral,
                'B',
                new ItemStack(IUItem.barionrotor.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewind, 1, 13),
                "ECE",
                "CBC",
                "ACA", 'E',
                IUItem.circuitSpectral,
                'A',
                new ItemStack(IUItem.doublecompressIridiumplate),
                'C',
                new ItemStack(IUItem.photoniy_ingot),
                'B',
                new ItemStack(IUItem.adronrotor.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spectralSolarHelmet),
                " A ",
                "DCD",
                "BEB",
                'D',
                IUItem.cirsuitQuantum,
                'E',
                new ItemStack(IUItem.compresscarbonultra),
                'C',
                new ItemStack(IUItem.ultimateSolarHelmet, 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.doublecompressIridiumplate, 1),
                'A',
                new ItemStack(IUItem.blockpanel, 1, 4)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.singularSolarHelmet),
                " A ",
                "DCD",
                "BDB",
                'D',
                IUItem.circuitSpectral,
                'E',
                new ItemStack(IUItem.compresscarbonultra),
                'C',
                new ItemStack(IUItem.spectralSolarHelmet, 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.doublecompressIridiumplate, 1),
                'A',
                new ItemStack(IUItem.blockpanel, 1, 6)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines, 1, 5),
                "BCB",
                "DAD",
                "BCB",
                'D',
                IUItem.circuitSpectral,
                'C',
                new ItemStack(IUItem.doublecompressIridiumplate),
                'B',
                new ItemStack(IUItem.photoniy_ingot),
                'A',
                new ItemStack(IUItem.machines, 1, 3)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines, 1, 1),
                "DED",
                "BCB",
                "AAA",
                'E',
                new ItemStack(IUItem.core, 1, 5),
                'D',
                OreDictionary.getOres("doubleplateAlumel"),
                'B',
                IUItem.cirsuitQuantum,
                'C',
                new ItemStack(IUItem.simplemachine, 1, 6),
                'A',
                new ItemStack(IUItem.quantumtool)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines, 1, 2),
                "DED",
                "BCB",
                "AAA",
                'E',
                new ItemStack(IUItem.core, 1, 7),
                'D',
                OreDictionary.getOres("doubleplateVitalium"),
                'B',
                IUItem.cirsuitQuantum,
                'C',
                new ItemStack(IUItem.machines, 1, 1),
                'A',
                new ItemStack(IUItem.advQuantumtool)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines, 1, 3),
                "DED",
                "BCB",
                "AFA",
                'F',
                new ItemStack(IUItem.doublecompressIridiumplate),
                'E',
                new ItemStack(IUItem.core, 1, 8),
                'D',
                OreDictionary.getOres("doubleplateDuralumin"),
                'B',
                IUItem.circuitSpectral,
                'C',
                new ItemStack(IUItem.machines, 1, 2),
                'A',
                new ItemStack(IUItem.advQuantumtool)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.blocksintezator),
                "ABA",
                "BCB",
                "ABA",
                'C',
                IUItem.circuitSpectral,
                'A',
                Ic2Items.advancedMachine,
                'B',
                new ItemStack(IUItem.core, 1, 8)
        );





        int[] meta2 = {2, 5, 3, 4, 0, 1, 6, 7, 8, 9, 10};
        int[] meta3 = {2, 3, 4, 5, 0, 1, 6, 7, 8, 9, 10};
        ItemStack[] stacks3 = {Ic2Items.electronicCircuit, Ic2Items.electronicCircuit, Ic2Items.advancedCircuit, Ic2Items.advancedCircuit, IUItem.circuitNano, IUItem.circuitNano, IUItem.cirsuitQuantum, IUItem.cirsuitQuantum, IUItem.circuitSpectral, IUItem.circuitSpectral, IUItem.circuitSpectral};
        for (int i = 0; i < 11; i++) {
            Recipes.advRecipes.addRecipe(new ItemStack(IUItem.chargepadelectricblock, 1, meta3[i]),
                    "ABA", "CDC", 'B', Blocks.STONE_PRESSURE_PLATE, 'A', stacks3[i], 'D',
                    new ItemStack(IUItem.electricblock, 1, meta2[i]), 'C', Ic2Items.rubber
            );

        }
        stacks3 = new ItemStack[]{IUItem.circuitNano, IUItem.circuitNano, IUItem.cirsuitQuantum, IUItem.cirsuitQuantum, IUItem.cirsuitQuantum, IUItem.circuitSpectral, IUItem.circuitSpectral};

        for (int i = 0; i < 7; i++) {
            if (i < 3) {
                if (i == 0) {
                    Recipes.advRecipes.addShapelessRecipe(
                            new ItemStack(IUItem.tranformer, 1, i),
                            new ItemStack(IUItem.tranformer, 1, 10),
                            new ItemStack(IUItem.crafting_elements, 1, 210)
                    );
                    Recipes.advRecipes.addRecipe(
                            new ItemStack(IUItem.crafting_elements, 1, 210),
                            " A ",
                            "BCD",
                            " A ",
                            'D',
                            new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                            'C',
                            DEFAULT_SENSOR,
                            'B',
                            stacks3[i],
                            'A',
                            new ItemStack(IUItem.cable, 1, i)
                    );
                } else {
                    Recipes.advRecipes.addShapelessRecipe(
                            new ItemStack(IUItem.tranformer, 1, i),
                            new ItemStack(IUItem.tranformer, 1, i - 1),
                            new ItemStack(IUItem.crafting_elements, 1, 210 + i)
                    );
                    Recipes.advRecipes.addRecipe(
                            new ItemStack(IUItem.crafting_elements, 1, 210 + i),
                            " A ",
                            "BCD",
                            " A ",
                            'D',
                            new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                            'C',
                            DEFAULT_SENSOR,
                            'B',
                            stacks3[i],
                            'A',
                            new ItemStack(IUItem.cable, 1, i)
                    );

                }

            } else {
                Recipes.advRecipes.addShapelessRecipe(
                        new ItemStack(IUItem.tranformer, 1, i),
                        new ItemStack(IUItem.tranformer, 1, i - 1),
                        new ItemStack(IUItem.crafting_elements, 1, 210 + i)
                );
                Recipes.advRecipes.addRecipe(
                        new ItemStack(IUItem.crafting_elements, 1, 210 + i),
                        " A ",
                        "BCD",
                        " A ",
                        'D',
                        new ItemStack(IUItem.lapotronCrystal, 1, OreDictionary.WILDCARD_VALUE),
                        'C',
                        DEFAULT_SENSOR,
                        'B',
                        stacks3[i],
                        'A',
                        new ItemStack(IUItem.cable, 1, i)
                );

            }
        }


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines, 1, 9),
                "ACA",
                "BEB",
                "ADA",
                'E',
                Ic2Items.advancedMachine,
                'D',
                new ItemStack(IUItem.module9, 1, 13),
                'C',
                new ItemStack(IUItem.module9, 1, 12),
                'B',
                IUItem.cirsuitQuantum,
                'A',
                new ItemStack(IUItem.core, 1, 4)
        );


        Recipes.advRecipes.addRecipe(Ic2Items.CoffeBeans, "AAA", "ABA", "AAA", 'A', new ItemStack(Items.DYE, 1, 3), 'B',
                new ItemStack(Items.WHEAT_SEEDS)
        );


        if (!Loader.isModLoaded("simplyquarries")) {
            Recipes.advRecipes.addRecipe(
                    new ItemStack(IUItem.machines, 1, 8),
                    "EDE",
                    "BAB",
                    "CCC",
                    'E',
                    (IUItem.cirsuitQuantum),
                    'D',
                    Ic2Items.advancedMachine,
                    'C',
                    new ItemStack(IUItem.compressIridiumplate),
                    'B',
                    new ItemStack(IUItem.advQuantumtool),
                    'A',
                    new ItemStack(IUItem.core, 1, 7)
            );
        }

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.photonglass, 1, 0),
                "CCC", "ABA", "CCC", 'B', new ItemStack(IUItem.itemiu, 1, 1), 'C', new ItemStack(IUItem.stik, 1, 0), 'A',
                new ItemStack(IUItem.sunnarium, 1, 2)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.photonglass, 1, 1),
                "CCC",
                "ABA",
                "CCC",
                'B',
                new ItemStack(IUItem.photonglass, 1, 0),
                'C',
                new ItemStack(IUItem.stik, 1, 6),
                'A',
                new ItemStack(IUItem.sunnariumpanel, 1, 0)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.photonglass, 1, 2),
                "CCC",
                "ABA",
                "CCC",
                'B',
                new ItemStack(IUItem.photonglass, 1, 1),
                'C',
                new ItemStack(IUItem.stik, 1, 9),
                'A',
                new ItemStack(IUItem.sunnariumpanel, 1, 1)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.photonglass, 1, 3),
                "CCC",
                "ABA",
                "CCC",
                'B',
                new ItemStack(IUItem.photonglass, 1, 2),
                'C',
                new ItemStack(IUItem.stik, 1, 11),
                'A',
                new ItemStack(IUItem.sunnariumpanel, 1, 2)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.photonglass, 1, 4),
                "CCC",
                "ABA",
                "CCC",
                'B',
                new ItemStack(IUItem.photonglass, 1, 3),
                'C',
                new ItemStack(IUItem.stik, 1, 13),
                'A',
                new ItemStack(IUItem.sunnariumpanel, 1, 3)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.photonglass, 1, 5),
                "CEC",
                "ABA",
                "CEC",
                'E',
                new ItemStack(IUItem.proton),
                'B',
                new ItemStack(IUItem.photonglass, 1, 4),
                'C',
                new ItemStack(IUItem.stik, 1, 16),
                'A',
                new ItemStack(IUItem.sunnariumpanel, 1, 4)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.photonglass, 1, 6),
                "CCC",
                "ABA",
                "CCC",
                'B',
                new ItemStack(IUItem.photonglass, 1, 5),
                'C',
                new ItemStack(IUItem.stik, 1, 4),
                'A',
                new ItemStack(IUItem.sunnariumpanel, 1, 5)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.photonglass, 1, 7),
                "CCC",
                "ABA",
                "CCC",
                'B',
                new ItemStack(IUItem.photonglass, 1, 6),
                'C',
                new ItemStack(IUItem.stik, 1, 12),
                'A',
                new ItemStack(IUItem.sunnariumpanel, 1, 6)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.photonglass, 1, 8),
                "CCC",
                "ABA",
                "CCC",
                'B',
                new ItemStack(IUItem.photonglass, 1, 7),
                'C',
                new ItemStack(IUItem.stik, 1, 11),
                'A',
                new ItemStack(IUItem.sunnariumpanel, 1, 7)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.photonglass, 1, 9),
                "CDC",
                "ABA",
                "CDC",
                'D',
                new ItemStack(IUItem.neutroniumingot, 1),
                'B',
                new ItemStack(IUItem.photonglass, 1, 8),
                'C',
                new ItemStack(IUItem.stik, 1, 18),
                'A',
                new ItemStack(IUItem.sunnariumpanel, 1, 8)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.photonglass, 1, 10),
                "CCC",
                "ABA",
                "CCC",
                'B',
                new ItemStack(IUItem.photonglass, 1, 9),
                'C',
                new ItemStack(IUItem.stik, 1, 15),
                'A',
                new ItemStack(IUItem.sunnariumpanel, 1, 9)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.photonglass, 1, 11),
                "CCC",
                "ABA",
                "CCC",
                'B',
                new ItemStack(IUItem.photonglass, 1, 10),
                'C',
                new ItemStack(IUItem.stik, 1, 2),
                'A',
                new ItemStack(IUItem.sunnariumpanel, 1, 10)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.photonglass, 1, 12),
                "CCC",
                "ABA",
                "CCC",
                'B',
                new ItemStack(IUItem.photonglass, 1, 11),
                'C',
                new ItemStack(IUItem.stik, 1, 6),
                'A',
                new ItemStack(IUItem.sunnariumpanel, 1, 11)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.photonglass, 1, 13),
                "CCC",
                "ABA",
                "CCC",
                'B',
                new ItemStack(IUItem.photonglass, 1, 12),
                'C',
                new ItemStack(IUItem.stik, 1, 5),
                'A',
                new ItemStack(IUItem.sunnariumpanel, 1, 12)
        );

        //
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basecircuit, 1, 6),
                "CCC",
                "ABA",
                "DDD",
                'D',
                OreDictionary.getOres("plateBronze"),
                'C',
                Ic2Items.carbonPlate,
                'B',
                Ic2Items.advancedCircuit,
                'A',
                new ItemStack(IUItem.basecircuit, 1, 0)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.basecircuit, 1, 7),
                "CCC", "ABA", "DDD", 'D', OreDictionary.getOres("plateSteel"), 'C', Ic2Items.carbonPlate, 'B',
                new ItemStack(IUItem.basecircuit, 1, 9),
                'A', new ItemStack(IUItem.basecircuit, 1, 1)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basecircuit, 1, 8),
                "CCC",
                "ABA",
                "DDD",
                'D',
                OreDictionary.getOres("plateSpinel"),
                'C',
                Ic2Items.carbonPlate,
                'B',
                new ItemStack(IUItem.basecircuit, 1, 10),
                'A',
                new ItemStack(IUItem.basecircuit, 1, 2)
        );

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.basecircuit, 1, 3),
                "BBB", "BAB", "BBB", 'B', new ItemStack(IUItem.stik, 1, 10), 'A', new ItemStack(IUItem.basecircuit, 1, 0)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.basecircuit, 1, 4),
                "BBB", "BAB", "BBB", 'B', new ItemStack(IUItem.stik, 1, 14), 'A', new ItemStack(IUItem.basecircuit, 1, 1)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.basecircuit, 1, 5),
                "BBB", "BAB", "BBB", 'B', new ItemStack(IUItem.stik, 1, 16), 'A', new ItemStack(IUItem.basecircuit, 1, 2)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.basecircuit, 1, 13),
                "BBB", "BAB", "BBB", 'B', new ItemStack(IUItem.stik, 1, 6), 'A', new ItemStack(IUItem.basecircuit, 1, 12)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basecircuit, 1, 14),
                "CCC",
                "ABA",
                "DDD",
                'D',
                OreDictionary.getOres("platePlatinum"),
                'C',
                Ic2Items.carbonPlate,
                'B',
                Ic2Items.electronicCircuit,
                'A',
                new ItemStack(IUItem.basecircuit, 1, 12)
        );

        //
        ItemStack[] circuit = {new ItemStack(IUItem.crafting_elements,
                1,
                30), Ic2Items.electronicCircuit, Ic2Items.advancedCircuit,
                Ic2Items.advancedCircuit,
                (IUItem.circuitNano), (IUItem.circuitNano), (IUItem.cirsuitQuantum), (IUItem.cirsuitQuantum), (IUItem.circuitSpectral), (IUItem.circuitSpectral), new ItemStack(
                IUItem.core,
                1,
                10
        ), new ItemStack(IUItem.core, 1, 11), new ItemStack(IUItem.core, 1, 12), new ItemStack(IUItem.core, 1, 13)};
        ItemStack[] iridium = {new ItemStack(
                IUItem.sunnarium,
                1,
                1
        ), Ic2Items.iridiumOre, Ic2Items.iridiumPlate, Ic2Items.iridiumPlate, new ItemStack(IUItem.compressIridiumplate), new ItemStack(
                IUItem.compressIridiumplate), new ItemStack(IUItem.compressIridiumplate), new ItemStack(IUItem.doublecompressIridiumplate), new ItemStack(
                IUItem.doublecompressIridiumplate), new ItemStack(IUItem.doublecompressIridiumplate), new ItemStack(IUItem.doublecompressIridiumplate), new ItemStack(
                IUItem.doublecompressIridiumplate), new ItemStack(IUItem.doublecompressIridiumplate), new ItemStack(IUItem.doublecompressIridiumplate)};

        for (int i = 0; i < 14; i++) {
            if (i != 0) {
                Recipes.advRecipes.addRecipe(
                        new ItemStack(IUItem.blockpanel, 1, i),
                        "ABA",
                        "CDC",
                        "DED",
                        'A',
                        new ItemStack(IUItem.photonglass, 1, i),
                        'B',
                        new ItemStack(IUItem.excitednucleus, 1, i),
                        'C',
                        iridium[i],
                        'D',
                        new ItemStack(IUItem.blockpanel, 1, i - 1),
                        'E',
                        circuit[i]
                );
                Recipes.advRecipes.addRecipe(
                        new ItemStack(IUItem.upgradepanelkit, 1, i),
                        "ABA",
                        "C C",
                        "DED",
                        'A',
                        new ItemStack(IUItem.photonglass, 1, i),
                        'B',
                        new ItemStack(IUItem.excitednucleus, 1, i),
                        'C',
                        iridium[i],
                        'D',
                        new ItemStack(IUItem.blockpanel, 1, i - 1),
                        'E',
                        circuit[i]
                );
            } else {
                Recipes.advRecipes.addRecipe(
                        new ItemStack(IUItem.blockpanel, 1, i),
                        "ABA",
                        "CDC",
                        "DED",
                        'A',
                        new ItemStack(IUItem.photonglass, 1, i),
                        'B',
                        new ItemStack(IUItem.excitednucleus, 1, i),
                        'C',
                        iridium[i],
                        'D',
                        Ic2Items.solarPanel,
                        'E',
                        circuit[i]
                );
                Recipes.advRecipes.addRecipe(
                        new ItemStack(IUItem.upgradepanelkit, 1, i),
                        "ABA",
                        "C C",
                        "DED",
                        'A',
                        new ItemStack(IUItem.photonglass, 1, i),
                        'B',
                        new ItemStack(IUItem.excitednucleus, 1, i),
                        'C',
                        iridium[i],
                        'D',
                        Ic2Items.solarPanel,
                        'E',
                        circuit[i]
                );

            }
        }
        //
        for (int i = 0; i < 3; i++) {
            Recipes.advRecipes.addRecipe(new ItemStack(IUItem.preciousblock, 1, i),
                    "AAA", "AAA", "AAA", 'A', new ItemStack(IUItem.preciousgem, 1, i)
            );
            Recipes.advRecipes.addShapelessRecipe(
                    new ItemStack(IUItem.preciousgem, 9, i),
                    new ItemStack(IUItem.preciousblock, 1, i)
            );
        }

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module7),
                " C ",
                "BAB",
                "DCD",
                'D',
                OreDictionary.getOres("plateCaravky"),
                'C',
                OreDictionary.getOres("plateElectrum"),
                'B',
                OreDictionary.getOres("plateInvar"),
                'A',
                new ItemStack(IUItem.module_schedule)
        );

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.paints),
                "AAA", "A A", "AAA", 'A', new ItemStack(Items.IRON_INGOT)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.paints, 1, 1), "AB ", 'A', new ItemStack(IUItem.paints), 'B',
                new ItemStack(Items.DYE, 1, 12)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.paints, 1, 2),
                "AB ",
                'A',
                new ItemStack(IUItem.paints),
                'B',
                new ItemStack(Items.DYE, 1, 11)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.paints, 1, 3),
                "AB ",
                'A',
                new ItemStack(IUItem.paints),
                'B',
                new ItemStack(Items.DYE, 1, 2)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.paints, 1, 4),
                "AB ",
                'A',
                new ItemStack(IUItem.paints),
                'B',
                new ItemStack(Items.DYE, 1, 1)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.paints, 1, 5),
                "AB ",
                'A',
                new ItemStack(IUItem.paints),
                'B',
                new ItemStack(Items.DYE, 1, 14)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.paints, 1, 6),
                "AB ",
                'A',
                new ItemStack(IUItem.paints),
                'B',
                new ItemStack(Items.DYE, 1, 4)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.paints, 1, 7),
                "AB ",
                'A',
                new ItemStack(IUItem.paints),
                'B',
                new ItemStack(Items.DYE, 1, 5)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.paints, 1, 8),
                "AB ",
                'A',
                new ItemStack(IUItem.paints),
                'B',
                new ItemStack(Items.DYE, 1, 6)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.paints, 1, 9),
                "AB ",
                'A',
                new ItemStack(IUItem.paints),
                'B',
                new ItemStack(Items.DYE, 1, 15)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.paints, 1, 10),
                "AB ",
                'A',
                new ItemStack(IUItem.paints),
                'B',
                new ItemStack(Items.GLOWSTONE_DUST)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.paints, 1, 11),
                "AB ",
                'A',
                new ItemStack(IUItem.paints),
                'B',
                new ItemStack(Items.DYE, 1, 10)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.quarrymodule),
                "BAB",
                "DCD",
                'D',
                OreDictionary.getOres("ingotGermanium"),
                'C',
                new ItemStack(IUItem.module_schedule),
                'B',
                (IUItem.cirsuitQuantum),
                'A',
                new ItemStack(IUItem.crafting_elements,1,158)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.analyzermodule),
                "BAB",
                "DCD",
                'D',
                OreDictionary.getOres("ingotGermanium"),
                'C',
                new ItemStack(IUItem.module_schedule),
                'B',
                (IUItem.cirsuitQuantum),
                'A',
                new ItemStack(IUItem.basemachine1, 1, 2)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machinekit, 1, 0),
                "ABA",
                "D D",
                "EEE",
                'E',
                new ItemStack(IUItem.nanoBox),
                'D',
                (IUItem.circuitNano),
                'B',
                OreDictionary.getOres("doubleplateAluminium"),
                'A',
                OreDictionary.getOres("doubleplateAlumel")
        );


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machinekit, 1, 1),
                "ABA",
                "D D",
                "EEE",
                'E',
                new ItemStack(IUItem.quantumtool),
                'D',
                (IUItem.cirsuitQuantum),
                'B',
                OreDictionary.getOres("doubleplatePlatinum"),
                'A',
                OreDictionary.getOres("doubleplateVitalium")
        );


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machinekit, 1, 2),
                "ABA",
                "D D",
                "EEE",
                'E',
                new ItemStack(IUItem.advQuantumtool),
                'D',
                (IUItem.circuitSpectral),
                'B',
                OreDictionary.getOres("doubleplateSpinel"),
                'A',
                OreDictionary.getOres("doubleplateManganese")
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.hazmathelmet),
                "AAA",
                "BCB",
                "DDD",
                'A',
                OreDictionary.getOres("plateMikhail"),
                'B',
                OreDictionary.getOres("platePlatinum"),
                'D',
                OreDictionary.getOres("plateCobalt"),
                'C',
                new ItemStack(Ic2Items.hazmatHelmet.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.hazmatchest),
                "AAA",
                "BCB",
                "DDD",
                'A',
                OreDictionary.getOres("plateMikhail"),
                'B',
                OreDictionary.getOres("platePlatinum"),
                'D',
                OreDictionary.getOres("plateCobalt"),
                'C',
                new ItemStack(Ic2Items.hazmatChestplate.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.hazmatleggins),
                "AAA",
                "BCB",
                "DDD",
                'A',
                OreDictionary.getOres("plateMikhail"),
                'B',
                OreDictionary.getOres("platePlatinum"),
                'D',
                OreDictionary.getOres("plateCobalt"),
                'C',
                new ItemStack(Ic2Items.hazmatLeggings.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.hazmatboosts),
                "AAA",
                "BCB",
                "DDD",
                'A',
                OreDictionary.getOres("plateMikhail"),
                'B',
                OreDictionary.getOres("platePlatinum"),
                'D',
                OreDictionary.getOres("plateCobalt"),
                'C',
                new ItemStack(Ic2Items.hazmatBoots.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.entitymodules),
                "ABA",
                "DCD",
                "EBE",
                'A',
                Ic2Items.advancedCircuit,
                'B',
                new ItemStack(IUItem.alloyscasing, 1, 2),
                'C',
                new ItemStack(IUItem.module_schedule),
                'D',
                OreDictionary.getOres("ingotInvar"),
                'E',
                new ItemStack(IUItem.alloyscasing, 1, 4)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.entitymodules, 1, 1),
                "ABA",
                "DCD",
                "EBE",
                'A',
                (IUItem.circuitSpectral),
                'B',
                new ItemStack(IUItem.adv_spectral_box),
                'C',
                new ItemStack(IUItem.module_schedule),
                'D',
                OreDictionary.getOres("doubleplateGermanium"),
                'E',
                new ItemStack(IUItem.alloyscasing, 1, 9)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spawnermodules, 1, 0),
                "ABA",
                'A',
                new ItemStack(IUItem.module9, 1, 6),
                'B',
                OreDictionary.getOres("doubleplateNichrome")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spawnermodules, 1, 1),
                "ABA",
                "DCD",
                "EEE",
                'E',
                new ItemStack(IUItem.nanoBox),
                'D',
                (IUItem.circuitNano),
                'B',
                OreDictionary.getOres("doubleplateAluminium"),
                'A',
                OreDictionary.getOres("doubleplateAlumel"),
                'C',
                new ItemStack(IUItem.spawnermodules, 1, 0)
        );


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spawnermodules, 1, 2),
                "ABA",
                "DCD",
                "EEE",
                'E',
                new ItemStack(IUItem.quantumtool),
                'D',
                (IUItem.cirsuitQuantum),
                'B',
                OreDictionary.getOres("doubleplatePlatinum"),
                'A',
                OreDictionary.getOres("doubleplateVitalium"),
                'C',
                new ItemStack(IUItem.spawnermodules, 1, 1)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spawnermodules, 1, 3),
                "ABA",
                'B',
                new ItemStack(IUItem.module9, 1, 5),
                'A',
                OreDictionary.getOres("doubleplateNichrome")
        );

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.spawnermodules, 1, 4), " C ", "ABA", " C ", 'B',
                new ItemStack(IUItem.module_schedule, 1), 'A', OreDictionary.getOres("doubleplateNichrome"), 'C',
                new ItemStack(Items.EXPERIENCE_BOTTLE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spawnermodules, 1, 5),
                "ABA",
                "DCD",
                "EEE",
                'E',
                new ItemStack(IUItem.nanoBox),
                'D',
                (IUItem.circuitNano),
                'B',
                OreDictionary.getOres("doubleplateAluminium"),
                'A',
                OreDictionary.getOres("doubleplateAlumel"),
                'C',
                new ItemStack(IUItem.spawnermodules, 1, 4)
        );


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.oilquarry),
                "C G",
                "ABA",
                "FDF",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 53),
                'G',
                new ItemStack(IUItem.crafting_elements, 1, 251),
                'D',
                Ic2Items.elemotor,
                'A',
                Ic2Items.advancedAlloy,
                'B',
                Ic2Items.machine,
                'F',
                Ic2Items.miner_pipe
        );


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.cathode),
                "B B",
                "BAB",
                "B B",
                'A',
                new ItemStack(IUItem.cell_all, 1, 0),
                'B',
                OreDictionary.getOres("plateAlumel")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.anode),
                "B B",
                "BAB",
                "B B",
                'A',
                new ItemStack(IUItem.cell_all, 1, 0),
                'B',
                OreDictionary.getOres("plateMuntsa")
        );


     


        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.expmodule), "   ", "ABA", "   ", 'A',
                new ItemStack(IUItem.module_schedule), 'B', new ItemStack(Items.EXPERIENCE_BOTTLE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module_stack),
                "DCD",
                "BAB",
                " C ",
                'D',
                OreDictionary.getOres("plateAlumel"),
                'A',
                new ItemStack(IUItem.module_schedule),
                'B',
                Ic2Items.overclockerUpgrade,
                'C',
                IUItem.tranformerUpgrade
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module_quickly),
                "   ",
                "ABA",
                "   ",
                'A',
                IUItem.overclockerUpgrade1,
                'B',
                new ItemStack(IUItem.module_schedule)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.nanodrill),
                "EDE",
                "ABC",
                " D ",
                'E',
                new ItemStack(IUItem.nanoBox),
                'D',
                new ItemStack(IUItem.photoniy_ingot),
                'B',
                new ItemStack(IUItem.advnanobox),
                'A',
                new ItemStack(IUItem.nanopickaxe, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                new ItemStack(IUItem.nanoshovel, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.quantumdrill),
                "EDE",
                "ABC",
                " D ",
                'E',
                new ItemStack(IUItem.quantumtool),
                'D',
                (IUItem.cirsuitQuantum),
                'B',
                new ItemStack(IUItem.quantumtool),
                'A',
                new ItemStack(IUItem.quantumpickaxe, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                new ItemStack(IUItem.quantumshovel, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spectraldrill),
                "EDE",
                "ABC",
                " D ",
                'E',
                new ItemStack(IUItem.spectral_box),
                'D',
                (IUItem.circuitSpectral),
                'B',
                new ItemStack(IUItem.advQuantumtool),
                'A',
                new ItemStack(IUItem.spectralpickaxe, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                new ItemStack(IUItem.spectralshovel, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.quantumdrill),
                "TCT",
                "CDC",
                "BFB",
                'T',
                OreDictionary.getOres("doubleplateMuntsa"),
                'F',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.advQuantumtool, 1),
                'D',
                new ItemStack(IUItem.nanodrill, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                (IUItem.cirsuitQuantum)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spectraldrill),
                "TCT",
                "CDC",
                "BFB",
                'T',
                Ic2Items.iridiumPlate,
                'F',
                new ItemStack(IUItem.advBatChargeCrystal, 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.adv_spectral_box, 1),
                'D',
                new ItemStack(IUItem.quantumdrill, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                (IUItem.circuitSpectral)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.bags),
                "BCB",
                "BAB",
                "B B",
                'C',
                new ItemStack(Ic2Items.suBattery.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(Items.LEATHER),
                'A'
                ,
                Ic2Items.electronicCircuit
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.adv_bags, 1),
                "BCB",
                "BAB",
                "B B",
                'C',
                new ItemStack(Ic2Items.reBattery.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                Ic2Items.carbonPlate,
                'A',
                new ItemStack(IUItem.bags, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.imp_bags, 1),
                "BCB",
                "BAB",
                "B B",
                'C',
                new ItemStack(Ic2Items.advBattery.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.compresscarbon),
                'A',
                new ItemStack(IUItem.adv_bags, 1, OreDictionary.WILDCARD_VALUE)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.advjetpack),
                "BCB",
                "CDC",
                "BFB",
                'F',
                new ItemStack(Ic2Items.suBattery.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                OreDictionary.getOres("doubleplateFerromanganese"),
                'D',
                new ItemStack(Ic2Items.electricJetpack.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'C',
                (IUItem.circuitNano)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.impjetpack),
                "TCT",
                "CDC",
                "BFB",
                'T',
                OreDictionary.getOres("doubleplateMuntsa"),
                'F',
                new ItemStack(Ic2Items.reBattery.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.quantumtool, 1),
                'D',
                new ItemStack(IUItem.advjetpack, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                (IUItem.cirsuitQuantum)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.perjetpack),
                "TCT",
                "CDC",
                "BFB",
                'T',
                Ic2Items.iridiumPlate,
                'F',
                new ItemStack(Ic2Items.advBattery.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.advQuantumtool, 1),
                'D',
                new ItemStack(IUItem.impjetpack, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                (IUItem.circuitSpectral)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.reactorCondensatorDiamond),
                "   ",
                "AB ",
                "   ",
                'A',
                new ItemStack(IUItem.reactorCondensatorDiamond, 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(Blocks.DIAMOND_BLOCK)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.reactorCondensatorDiamond),
                "CDC",
                "ABA",
                "CEC",
                'A',
                new ItemStack(Ic2Items.reactorCondensatorLap.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(Blocks.DIAMOND_BLOCK), 'C',
                new ItemStack(Items.DIAMOND), 'D',
                new ItemStack(IUItem.reactorimpVent, 1, OreDictionary.WILDCARD_VALUE)
                , 'E',
                new ItemStack(IUItem.impheatswitch, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.advventspread, 1),
                "ABA",
                "DCD",
                "EEE",
                'E',
                new ItemStack(IUItem.nanoBox),
                'D',
                (IUItem.circuitNano),
                'C',
                new ItemStack(Ic2Items.reactorVentSpread.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                OreDictionary.getOres("doubleplateAluminium"),
                'A',
                OreDictionary.getOres("doubleplateAlumel")
        );


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.impventspread, 1),
                "ABA",
                "DCD",
                "EEE",
                'E',
                new ItemStack(IUItem.quantumtool),
                'D',
                (IUItem.cirsuitQuantum),
                'C',
                new ItemStack(IUItem.advventspread, 1, OreDictionary.WILDCARD_VALUE),
                'B',
                OreDictionary.getOres("doubleplatePlatinum"),
                'A',
                OreDictionary.getOres("doubleplateVitalium")
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.reactoradvVent, 1),
                "ABA",
                "DCD",
                "EEE",
                'E',
                new ItemStack(IUItem.nanoBox),
                'D',
                (IUItem.circuitNano),
                'C',
                new ItemStack(Ic2Items.reactorVentGold.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                OreDictionary.getOres("doubleplateAluminium"),
                'A',
                OreDictionary.getOres("doubleplateAlumel")
        );


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.reactorimpVent, 1),
                "ABA",
                "DCD",
                "EEE",
                'E',
                new ItemStack(IUItem.quantumtool),
                'D',
                (IUItem.cirsuitQuantum),
                'C',
                new ItemStack(IUItem.reactoradvVent, 1, OreDictionary.WILDCARD_VALUE),
                'B',
                OreDictionary.getOres("doubleplatePlatinum"),
                'A',
                OreDictionary.getOres("doubleplateVitalium")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.advheatswitch, 1),
                "ABA",
                "DCD",
                "EEE",
                'E',
                new ItemStack(IUItem.nanoBox),
                'D',
                (IUItem.circuitNano),
                'C',
                new ItemStack(Ic2Items.reactorHeatSwitch.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'B',
                OreDictionary.getOres("doubleplateAluminium"),
                'A',
                OreDictionary.getOres("doubleplateAlumel")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.impheatswitch, 1),
                "ABA",
                "DCD",
                "EEE",
                'E',
                new ItemStack(IUItem.quantumtool),
                'D',
                (IUItem.cirsuitQuantum),
                'C',
                new ItemStack(IUItem.advheatswitch, 1, OreDictionary.WILDCARD_VALUE),
                'B',
                OreDictionary.getOres("doubleplatePlatinum"),
                'A',
                OreDictionary.getOres("doubleplateVitalium")
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 2),
                "   ",
                "ABC",
                "   ",
                'A',
                new ItemStack(IUItem.module7),
                'B',
                Ic2Items.machine,
                'C',
                Ic2Items.electronicCircuit
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 3),
                "   ",
                "ABC",
                "   ",
                'A',
                (IUItem.module8),
                'B',
                Ic2Items.machine,
                'C',
                Ic2Items.electronicCircuit
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 1),
                " A ",
                "ABA",
                " A ",
                'A',
                Ic2Items.reactorChamber,
                'B',
                new ItemStack(IUItem.alloysblock, 1, 6)
        );



        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines, 1, 13),
                "CEC",
                "ABA",
                "DED",
                'D',
                OreDictionary.getOres("gemCurium"),
                'C',
                OreDictionary.getOres("doubleplateCaravky"),
                'E',
                OreDictionary.getOres("plateDuralumin"),
                'A',
                new ItemStack(IUItem.core, 1, 8),
                'B',
                new ItemStack(IUItem.machines, 1, 8)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines, 1, 14),
                "CEC",
                "ABA",
                "DED",
                'D',
                OreDictionary.getOres("doubleplateGermanium"),
                'C',
                OreDictionary.getOres("doubleplateAlcled"),
                'E',
                OreDictionary.getOres("plateVitalium"),
                'A',
                new ItemStack(IUItem.core, 1, 11),
                'B',
                new ItemStack(IUItem.machines, 1, 13)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines, 1, 15),
                "CEC",
                "ABA",
                "DED",
                'D',
                new ItemStack(IUItem.doublecompressIridiumplate),
                'C',
                (IUItem.circuitSpectral),
                'E',
                OreDictionary.getOres("plateTitanium"),
                'A',
                new ItemStack(IUItem.core, 1, 12),
                'B',
                new ItemStack(IUItem.machines, 1, 14)
        );

        Recipes.advRecipes.addRecipe(
                (IUItem.phase_module),
                "DDD",
                "BAC",
                'D',
                new ItemStack(IUItem.alloysdoubleplate, 1, 7),
                'C',
                (IUItem.module2),
                'B',
                (IUItem.module1),
                'A',
                new ItemStack(IUItem.module_schedule)
        );
        Recipes.advRecipes.addRecipe(
                (IUItem.moonlinse_module),
                "DDD",
                "BAB",
                "CEC",
                'D',
                new ItemStack(IUItem.lens, 1, 4),
                'E',
                new ItemStack(IUItem.alloysdoubleplate, 1, 6),
                'C',
                new ItemStack(IUItem.alloysdoubleplate, 1, 4),
                'B',
                new ItemStack(IUItem.alloysdoubleplate, 1, 3),
                'A',
                new ItemStack(IUItem.module_schedule)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.tank),
                "BAB",
                "CAC",
                "BAB",
                'C',
                new ItemStack(IUItem.alloysplate, 1, 4),
                'A',
                Ic2Items.cell,
                'B',
                new ItemStack(Ic2Items.denseplateiron.getItem(), 1, 4)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.tank, 1, 1),
                "C C",
                "BAB",
                "C C",
                'C',
                new ItemStack(IUItem.alloysplate, 1, 2),
                'A',
                new ItemStack(IUItem.tank),
                'B',
                new ItemStack(IUItem.alloysplate, 1, 6)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.tank, 1, 2),
                "CBC",
                "CAC",
                "CBC",
                'B',
                IUItem.photoniy,
                'C',
                new ItemStack(IUItem.nanoBox),
                'A',
                new ItemStack(IUItem.tank, 1, 1)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.tank, 1, 3),
                "CBC",
                "CAC",
                "CBC",
                'B',
                IUItem.photoniy_ingot,
                'A',
                new ItemStack(IUItem.tank, 1, 2),
                'C',
                new ItemStack(IUItem.advnanobox)
        );


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module_storage, 1),
                "AAA", "CBC", "DDD", 'D', OreDictionary.getOres("plateManganese"), 'C', OreDictionary.getOres("plateNickel"), 'A',
                OreDictionary.getOres("plateInvar"),
                'B',
                new ItemStack(IUItem.module_schedule)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machinekit, 1, 3),
                " A ",
                "BDC",
                "   ",
                'A',
                new ItemStack(IUItem.machinekit, 1, 0),
                'B',
                new ItemStack(IUItem.machinekit, 1, 1),
                'C',
                new ItemStack(IUItem.machinekit, 1, 2),
                'D',
                OreDictionary.getOres("plateManganese")
        );


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 7),
                "DCD",
                "ABA",
                "DCD",
                'A',
                new ItemStack(IUItem.impmagnet, 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.basemachine, 1, 14),
                'C',
                OreDictionary.getOres("plateElectrum"),
                'D',
                OreDictionary.getOres("plateNickel")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.leadbox),
                "AAA",
                "ABA",
                "   ",
                'A',
                OreDictionary.getOres("ingotLead"),
                'B',
                new ItemStack(Blocks.CHEST)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.katana, 1, 0),
                "AD ",
                "AB ",
                "CC ",
                'A',
                new ItemStack(IUItem.compresscarbonultra),
                'C',
                new ItemStack(Blocks.GLOWSTONE),
                'B',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                'D',
                new ItemStack(Ic2Items.nanoSaber.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.coolupgrade, 1, 0),
                "A A",
                " B ",
                "A A",
                'A',
                ModUtils.getCellFromFluid("iufluidazot"),
                'B',
                new ItemStack(IUItem.module_schedule)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.coolupgrade, 1, 1),
                "A A",
                " B ",
                "A A",
                'A',
                ModUtils.getCellFromFluid("iufluidhyd"),
                'B',
                new ItemStack(IUItem.module_schedule)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.coolupgrade, 1, 2),
                "A A",
                " B ",
                "A A",
                'A',
                ModUtils.getCellFromFluid("iufluidhelium"),
                'B',
                new ItemStack(IUItem.module_schedule)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.autoheater),
                "AB ",
                'A',
                new ItemStack(IUItem.basemachine2, 1, 5),
                'B',
                new ItemStack(IUItem.module_schedule)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.scable),
                " C ",
                "ABA",
                " C ",
                'B',
                Ic2Items.copperCableItem,
                'A',
                new ItemStack(IUItem.sunnarium, 1, 3),
                'C',
                Ic2Items.rubber

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.qcable),
                " C ",
                "ABA",
                " C ",
                'B',
                Ic2Items.glassFiberCableItem,
                'A',
                new ItemStack(IUItem.proton),
                'C',
                Ic2Items.iridiumOre

        );


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.simplemachine, 1, 6),
                "ABA",
                "CDC",
                "ABA",
                'A',
                Items.GLOWSTONE_DUST,
                'B',
                Ic2Items.advancedCircuit,
                'C',
                Ic2Items.advancedMachine,
                'D',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                Ic2Items.inductionFurnace,
                "AAA",
                "ACA",
                "ABA",
                'A',
                OreDictionary.getOres("ingotCopper"),
                'B',
                Ic2Items.advancedMachine,
                'C',
                new ItemStack(IUItem.simplemachine, 1, 2)

        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module9, 1, 14),
                " D ",
                "ABA",
                " C ",
                'A',
                new ItemStack(IUItem.core, 1, 5),
                'B',
                new ItemStack(IUItem.module_schedule),
                'D',
                new ItemStack(IUItem.machines_base, 1, 2),
                'C',
                OreDictionary.getOres("doubleplateMuntsa")
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.module9, 1, 15),
                " D ",
                "ABA",
                " C ",
                'A',
                new ItemStack(IUItem.core, 1, 5),
                'B',
                new ItemStack(IUItem.module_schedule),
                'D',
                new ItemStack(IUItem.machines_base1, 1, 9),
                'C',
                OreDictionary.getOres("doubleplateVanadoalumite")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.upgrade_speed_creation),
                "   ",
                "ABA",
                "   ",
                'A',
                IUItem.overclockerUpgrade,
                'B',
                new ItemStack(IUItem.module_schedule)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.blastfurnace, 1, 5),
                " A ",
                "ABA",
                " A ",
                'B',
                Blocks.BRICK_BLOCK,
                'A',
                OreDictionary.getOres("ingotMagnesium")


        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.blastfurnace, 1, 1),
                " A ",
                "ABA",
                " A ",
                'B',
                new ItemStack(Ic2Items.overclockerUpgrade.getItem(), 1, 6),
                'A',
                new ItemStack(IUItem.blastfurnace, 1, 5)


        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.blastfurnace, 1, 3),
                " A ",
                "ABA",
                " A ",
                'B',
                new ItemStack(Ic2Items.overclockerUpgrade.getItem(), 1, 4),
                'A',
                new ItemStack(IUItem.blastfurnace, 1, 5)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.blastfurnace, 1, 2),
                " A ",
                "ABA",
                " A ",
                'B',
                new ItemStack(Ic2Items.advancedAlloy.getItem(), 1, 8),
                'A',
                new ItemStack(IUItem.blastfurnace, 1, 5)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.blastfurnace, 1, 4),
                " A ",
                "ABA",
                " A ",
                'B',
                Ic2Items.canner,
                'A',
                new ItemStack(IUItem.blastfurnace, 1, 5)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.blastfurnace, 1, 0),
                " A ",
                "ABA",
                " A ",
                'B',
                Ic2Items.electronicCircuit,
                'A',
                new ItemStack(IUItem.blastfurnace, 1, 5)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spectral_box),
                "D D",
                "ABA",
                "C C",
                'D',
                new ItemStack(IUItem.coal_chunk1),
                'C',
                Ic2Items.iridiumPlate,
                'A',
                OreDictionary.getOres("doubleplateSpinel"),
                'B',
                new ItemStack(IUItem.quantumtool)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.adv_spectral_box),
                "DAD",
                "ABA",
                "CEC",
                'E',
                new ItemStack(IUItem.coal_chunk1),
                'D',
                new ItemStack(IUItem.photoniy_ingot),
                'C',
                new ItemStack(IUItem.doublecompressIridiumplate),
                'A',
                OreDictionary.getOres("doubleplateSpinel"),
                'B',
                new ItemStack(IUItem.advQuantumtool)

        );


        Recipes.advRecipes.addRecipe(
                Ic2Items.transformerUpgrade,
                "AAA",
                "DBD",
                "ACA",
                'B',
                new ItemStack(IUItem.tranformer, 1, 8),
                'A',
                Blocks.GLASS,
                'D',
                Ic2Items.insulatedGoldCableItem,
                'C',
                Ic2Items.electronicCircuit

        );
        Recipes.advRecipes.addRecipe(
                Ic2Items.teslaCoil,
                "AAA",
                "ABA",
                "CDC",
                'B',
                new ItemStack(IUItem.tranformer, 1, 8),
                'A',
                Items.REDSTONE,
                'D',
                Ic2Items.electronicCircuit,
                'C',
                Ic2Items.casingiron

        );
        Recipes.advRecipes.addRecipe(
                Ic2Items.iridiumDrill,
                " A ",
                "ABA",
                " C ",
                'A',
                Ic2Items.iridiumPlate,
                'B',
                new ItemStack(IUItem.quantumdrill, 1, OreDictionary.WILDCARD_VALUE),
                'C',
                new ItemStack(Ic2Items.energyCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE)

        );


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.windmeter, 1, 0),
                " A ",
                "ABA",
                " AC", 'A', Ic2Items.casingtin, 'B', Ic2Items.casingbronze, 'C', Ic2Items.powerunitsmall


        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotorupgrade_schemes),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.plastic_plate)
                , 'D', "doubleplateVanadoalumite", 'B', new ItemStack(IUItem.photoniy_ingot), 'C', "plateManganese"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 0),
                "A A", "CBC", "A A", 'A', new ItemStack(IUItem.advnanobox)
                , 'B', new ItemStack(IUItem.rotorupgrade_schemes), 'C', "casingNichrome"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 1),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.advQuantumtool), 'B', new ItemStack(IUItem.core, 1, 3)
                , 'C', new ItemStack(IUItem.rotors_upgrade, 1, 0), 'D', "casingRedbrass"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 2),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.adv_spectral_box), 'B', new ItemStack(IUItem.core, 1, 5)
                , 'C', new ItemStack(IUItem.rotors_upgrade, 1, 1), 'D', "casingMuntsa"
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 3),
                "A A", "CBC", "A A", 'A', new ItemStack(IUItem.advnanobox)
                , 'B', new ItemStack(IUItem.rotorupgrade_schemes), 'C', Ic2Items.iridiumPlate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 4),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.advQuantumtool), 'B', new ItemStack(IUItem.core, 1, 4)
                , 'C', new ItemStack(IUItem.rotors_upgrade, 1, 3), 'D', IUItem.compressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 5),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.adv_spectral_box), 'B', new ItemStack(IUItem.core, 1, 6)
                , 'C', new ItemStack(IUItem.rotors_upgrade, 1, 4), 'D', IUItem.doublecompressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 6),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.advnanobox)
                , 'B', new ItemStack(IUItem.rotorupgrade_schemes), 'C', Ic2Items.iridiumPlate, 'D', "casingDuralumin"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 7),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.advQuantumtool), 'B', new ItemStack(IUItem.core, 1, 4)
                , 'C', new ItemStack(IUItem.rotors_upgrade, 1, 6), 'D', IUItem.compressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 8),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.adv_spectral_box), 'B', new ItemStack(IUItem.core, 1, 6)
                , 'C', new ItemStack(IUItem.rotors_upgrade, 1, 7), 'D', IUItem.doublecompressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 9),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.adv_spectral_box)
                , 'B', new ItemStack(IUItem.rotorupgrade_schemes), 'C', IUItem.compressIridiumplate, 'D', "doubleplateAlumel"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 10),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.core, 1, 5)
                , 'B', new ItemStack(IUItem.rotorupgrade_schemes), 'C', IUItem.compressIridiumplate, 'D', "doubleplateInvar"
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.spawnermodules, 1, 8),
                "ECE", "BAB", "DCD", 'A', new ItemStack(IUItem.module_schedule), 'E', new ItemStack(IUItem.core, 5)
                , 'B', new ItemStack(IUItem.quantumtool), 'C', Ic2Items.iridiumPlate, 'D', IUItem.cirsuitQuantum
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 38),
                "CBC",
                "ADA",
                "EBE",
                'E',
                IUItem.adv_spectral_box,
                'D',
                Ic2Items.advancedMachine,
                'C',
                IUItem.circuitSpectral,
                'B',
                OreDictionary.getOres("doubleplateMuntsa"),
                'A',
                OreDictionary.getOres("doubleplateAluminumbronze")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 32),
                " C ",
                "CDC",
                "ECE",
                'C',
                ModUtils.getCable(Ic2Items.copperCableItem, 1),
                'D',
                Ic2Items.advancedMachine,
                'E',
                IUItem.circuitNano
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 33),
                " C ",
                "CDC",
                "ECE",
                'C',
                ModUtils.getCable(Ic2Items.copperCableItem, 1),
                'D',
                Ic2Items.advancedMachine,
                'E',
                IUItem.cirsuitQuantum
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 11),
                "ADA", "CBC", "DED", 'A', new ItemStack(IUItem.advnanobox)
                , 'B', new ItemStack(IUItem.rotorupgrade_schemes), 'C', Ic2Items.iridiumPlate, 'D', "doubleplatePlatinum", 'E',
                new ItemStack(IUItem.core, 1, 3)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 12),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.advQuantumtool), 'B', new ItemStack(IUItem.core, 1, 4)
                , 'C', new ItemStack(IUItem.rotors_upgrade, 1, 11), 'D', IUItem.compressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 13),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.adv_spectral_box), 'B', new ItemStack(IUItem.core, 1, 6)
                , 'C', new ItemStack(IUItem.rotors_upgrade, 1, 12), 'D', IUItem.doublecompressIridiumplate
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 14),
                "ADA", "CBC", "DED", 'A', new ItemStack(IUItem.advnanobox)
                , 'B', new ItemStack(IUItem.rotorupgrade_schemes), 'C', Ic2Items.iridiumPlate, 'D', "doubleplateVitalium", 'E',
                new ItemStack(IUItem.core, 1, 4)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 15),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.advQuantumtool), 'B', new ItemStack(IUItem.core, 1, 5)
                , 'C', new ItemStack(IUItem.rotors_upgrade, 1, 14), 'D', IUItem.compressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 16),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.adv_spectral_box), 'B', new ItemStack(IUItem.core, 1, 6)
                , 'C', new ItemStack(IUItem.rotors_upgrade, 1, 15), 'D', IUItem.doublecompressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 17),
                "ADA",
                "CBC",
                "DED",
                'A',
                new ItemStack(IUItem.advnanobox)
                ,
                'B',
                new ItemStack(IUItem.rotorupgrade_schemes),
                'C',
                Ic2Items.iridiumPlate,
                'D',
                "doubleplateRedbrass",
                'E',
                new ItemStack(IUItem.core, 1, 4)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 18),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.advQuantumtool), 'B', new ItemStack(IUItem.core, 1, 5)
                , 'C', new ItemStack(IUItem.rotors_upgrade, 1, 17), 'D', IUItem.compressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.rotors_upgrade, 1, 19),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.adv_spectral_box), 'B', new ItemStack(IUItem.core, 1, 6)
                , 'C', new ItemStack(IUItem.rotors_upgrade, 1, 18), 'D', IUItem.doublecompressIridiumplate
        );
        recipe_machines();
    }

    public static void craft_modules(int meta, int meta1, int meta2) {
        Recipes.advRecipes.addShapelessRecipe(new ItemStack(IUItem.crafting_elements, 1, meta),
                new ItemStack(IUItem.crafting_elements, 1, meta1), new ItemStack(IUItem.crafting_elements, 1, meta2)
        );
    }

    public static void recipe_machines() {

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 21),
                "CCC", "BAB", " B ", 'A', new ItemStack(IUItem.crafting_elements, 1, 42), 'B', "ingotGold", 'C', "ingotIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 20), " B ", "ACA", " D "
                , 'A', "doubleplateElectrum", 'B', "doubleplateAlumel", 'C', Ic2Items.elemotor, 'D',
                new ItemStack(IUItem.crafting_elements, 1, 16)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 96),
                " B ",
                "ACA",
                " D "
                ,
                'A',
                "doubleplatePlatinum",
                'B',
                "doubleplateVitalium",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 20),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 92)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 120), " B ", "ACA", " D "
                , 'A', "doubleplateSpinel", 'B', "doubleplateManganese", 'C', new ItemStack(IUItem.crafting_elements, 1, 96), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 116)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 60),
                "AAA", " B ", "   ", 'A', new ItemStack(Ic2Items.reBattery.getItem(), 1, OreDictionary.WILDCARD_VALUE), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 21)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 16),
                "AAA", "CBC", "   ", 'A', "plateBronze", 'C', "plateTin", 'B',
                new ItemStack(IUItem.crafting_elements, 1, 42)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 92),
                "AAA", "CBC", "   ", 'A', "platePlatinum", 'C', "plateCobalt", 'B',
                new ItemStack(IUItem.crafting_elements, 1, 16)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 116),
                "AAA", "CBC", "   ", 'A', "plateSpinel", 'C', "plateManganese", 'B',
                new ItemStack(IUItem.crafting_elements, 1, 92)
        );
        Recipes.advRecipes.addRecipe(
                Ic2Items.elemotor, " A ", "BCB", " D ", 'A', Ic2Items.casingtin, 'B', Ic2Items.coil, 'C',
                new ItemStack(IUItem.crafting_elements, 1, 60),'D', Ic2Items.electronicCircuit

        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 25),
                "CCC", "BAB", " D ", 'B', IUItem.circuitNano, 'A', new ItemStack(IUItem.crafting_elements, 1, 21), 'C',
                new ItemStack(IUItem.nanoBox), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 16)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 23),
                "CCC", "BAB", " D ", 'B', IUItem.cirsuitQuantum, 'A', new ItemStack(IUItem.crafting_elements, 1, 25), 'C',
                new ItemStack(IUItem.quantumtool), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 92)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 24),
                "CCC", "BAB", " D ", 'B', IUItem.circuitSpectral, 'A', new ItemStack(IUItem.crafting_elements, 1, 23), 'C',
                new ItemStack(IUItem.spectral_box), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 116)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 42),
                "AAA",
                "CBC",
                "   ",
                'B',
                new ItemStack(IUItem.cirsuitQuantum.getItem(), 1, 17),
                'C',
                Items.REDSTONE,
                'A',
                "ingotTungsten"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 41), "AAA", "BCB", "AAA", 'A', Items.FLINT, 'B', "ingotIron", 'C',
                "ingotTitanium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 46), " A ", "ABA", "ABA", 'A', "plateIron", 'B', "plateAluminium");
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 35),
                " B ",
                "BAB",
                "B B",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 46),
                'B',
                "plateChromium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 72),
                " B ",
                "BAB",
                "CCC",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 46),
                'B',
                "plateMikhail",
                'C',
                "plateCobalt"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 70),
                " B ",
                "BAB",
                "CCC",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 46),
                'B',
                "plateZinc",
                'C',
                "plateMagnesium"
        );

        craft_modules(69, 41, 21);
        craft_modules(63, 76, 21);
        craft_modules(159, 141, 21);
        craft_modules(163, 142, 21);
        craft_modules(132, 144, 21);
        craft_modules(165, 164, 21);

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 200),
                "AAA", "BCB", "   ", 'A', "plateZinc", 'B',
                "plateTitanium", 'C', "plateAluminum"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 142),
                "AAA", "BCB", " D ", 'A', "platePlatinum", 'C',
                "plateTitanium", 'B', "plateCobalt", 'D', "plateIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 200),
                "AAA", "BCB", "   ", 'A', "plateZinc", 'B',
                "plateTitanium", 'C', "plateAluminium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 141),
                "ABB", "C D", " AA", 'A', "plateCobalt", 'B', "plateNickel",
                'C', "plateIron", 'D', "plateTitanium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 144),
                "AAA", " B ", " C ", 'A', "plateNickel", 'B', "plateCobalt",
                'C', "plateIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 76),
                "AAA", " B ", "AAA", 'A', "plateIron", 'B', "plateTungsten"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 164),
                "BAB", "B B", "BAB", 'A', new ItemStack(IUItem.crafting_elements, 1, 200),
                'B', "plateTungsten"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 137), "AA ", "AA ", "   ", 'A', "plateIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 138), "BA ", "AA ", "   ", 'A', "plateElectrum", 'B'
                , new ItemStack(IUItem.crafting_elements, 1, 137)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 139), "BA ", "AA ", "   ", 'A', "plateCobalt", 'B'
                , new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 140), "BA ", "AA ", "   ", 'A', "plateMagnesium", 'B'
                , new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.advRecipes.addRecipe(
                Ic2Items.machine, "AA ", "AA ", "   ", 'A', new ItemStack(IUItem.crafting_elements, 1, 137)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 121), "AAA", "ABA", "AAA", 'A', "plateCobalt", 'B', Items.STRING
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 8, 122), "AAA", "BBB", "AAA", 'A', "plateTungsten", 'B', "plateTitanium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 201), "AAA", "BBB", "   ", 'A', "plateTitanium", 'B', "plateIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 2, 0), "AAA", "ABA", "AAA", 'A', Items.STRING, 'B', "plateCobalt"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 180), "ABA", "   ", "   ", 'A', "plateCopper", 'B', "plateIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 181), " A ", "ABA", "   ", 'A', "plateCopper", 'B', "plateIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 182), " A ", " B ", " A ", 'A', "plateCopper", 'B', "plateIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 183), "   ", "ABA", " A ", 'A', "plateCopper", 'B', "plateIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 184), " A ", "A A", "   ", 'A', "plateIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 185), "   ", "A A", " A ", 'A', "plateIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 186), " A ", "A  ", " A ", 'A', "plateIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 187), " A ", "  A", " A ", 'A', "plateIron"
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 188), "ABA", "   ", "   ", 'A', "plateDuralumin", 'B', "plateSilver"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 191), " A ", "ABA", "   ", 'A', "plateDuralumin", 'B', "plateSilver"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 189), " A ", " B ", " A ", 'A', "plateDuralumin", 'B', "plateSilver"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 190), "   ", "ABA", " A ", 'A', "plateDuralumin", 'B', "plateSilver"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 194), " A ", "A A", "   ", 'A', "plateSpinel"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 195), "   ", "A A", " A ", 'A', "plateSpinel"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 193), " A ", "A  ", " A ", 'A', "plateSpinel"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 192), " A ", "  A", " A ", 'A', "plateSpinel"
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

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 73),
                "CCC",
                "BAB",
                "D D",
                'A',
                new ItemStack(IUItem.crafting_elements, 1,
                        21
                ),
                'B',
                "plateTin",
                'C',
                "plateChromium",
                'D',
                "plateGold"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 37),
                "BBB",
                " A ",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1,
                        21
                ),
                'B',
                "plateElectrum"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 30),
                "BBB",
                " A ",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1,
                        21
                ),
                'B',
                "plateInvar"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 74),
                "B B",
                "DAD",
                "CCC",
                'A',
                new ItemStack(IUItem.crafting_elements, 1,
                        21
                ),
                'B',
                "plateDenseSteel",
                'C',
                "plateIron",
                'D',
                "plateCopper"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 28),
                "BBB",
                "CAC",
                " C ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1,
                        21
                ),
                'B',
                new ItemStack(Items.SKULL, 1, 1),
                'C',
                Blocks.SOUL_SAND
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 29),
                "   ",
                "BA ",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1,
                        21
                ),
                'B',
                new ItemStack(IUItem.magnet, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 44),
                " B ",
                "BAB",
                " B ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1,
                        21
                ),
                'B',
                "plateTin"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 47),
                " B ",
                "ABA",
                " B ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1,
                        44
                ),
                'B',
                "plateTin"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 49),
                " B ",
                "ABA",
                " C ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1,
                        47
                ),
                'B',
                "plateTin",
                'C',
                new ItemStack(IUItem.crafting_elements, 1,
                        44
                )
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 51),
                " B ",
                "ABA",
                " B ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1,
                        47
                ),
                'B',
                "plateTin"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 52),
                " B ",
                "ABA",
                " B ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1,
                        51
                ),
                'B',
                "plateTin"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 11),
                " C ",
                "BAB",
                "   ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1,
                        21
                ),
                'B',
                "plateIron",
                'C',
                new ItemStack(Ic2Items.reBattery.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addShapelessRecipe(
                Ic2Items.generator, new ItemStack(IUItem.crafting_elements, 1,
                        11
                ), Ic2Items.machine
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 55),
                "DCD",
                "BAB",
                "CCC",
                'A',
                new ItemStack(IUItem.crafting_elements, 1,
                        21
                ),
                'B',
                "plateIron",
                'C',
                new ItemStack(IUItem.sunnarium, 1, 4),
                'D',
                "plateGermanium"

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 59),
                "BCB",
                "DAD",
                "ECE",
                'A',
                new ItemStack(IUItem.crafting_elements, 1,
                        21
                ),
                'B',
                "doubleplateMuntsa",
                'C',
                "doubleplateAlcled",
                'D',
                "plateZinc",
                'E',
                "plateCaravky"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 65),
                "BBB",
                " A ",
                "CCC",
                'A',
                new ItemStack(IUItem.crafting_elements, 1,
                        21
                ),
                'B',
                "plateGermanium",
                'C',
                "plateChromium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 22), "BCB", "BCB", " A ", 'A', DEFAULT_SENSOR, 'B', Blocks.GLASS,
                'C', Ic2Items.FluidCell
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 5), "BCB", "BCB", " A ", 'A', ADV_SENSOR, 'B', Blocks.GLASS,
                'C', Ic2Items.FluidCell
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 82), "BCB", "BCB", " A ", 'A', IMP_SENSOR, 'B', Blocks.GLASS,
                'C', Ic2Items.FluidCell
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 106), "BCB", "BCB", " A ", 'A', PER_SENSOR, 'B', Blocks.GLASS,
                'C', Ic2Items.FluidCell
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 27), "CBC", "CBC", "CAC", 'A', DEFAULT_SENSOR,
                'C', Ic2Items.FluidCell, 'B', "gearMagnesium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 6), "CBC", "CBC", "CAC", 'A', ADV_SENSOR,
                'C', Ic2Items.FluidCell, 'B', "gearMagnesium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 83), "CBC", "CBC", "CAC", 'A', IMP_SENSOR,
                'C', Ic2Items.FluidCell, 'B', "gearMagnesium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 107), "CBC", "CBC", "CAC", 'A', PER_SENSOR,
                'C', Ic2Items.FluidCell, 'B', "gearMagnesium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 31), "CCC", "BAB", "   ", 'A', DEFAULT_SENSOR,
                'B', "gearVanadium", 'C', "gearIridium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 7), "CCC", "BAB", "   ", 'A', ADV_SENSOR,
                'B', "gearVanadium", 'C', "gearIridium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 84), "CCC", "BAB", "   ", 'A', IMP_SENSOR,
                'B', "gearVanadium", 'C', "gearIridium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 108), "CCC", "BAB", "   ", 'A', PER_SENSOR,
                'B', "gearVanadium", 'C', "gearIridium"
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 33), "DCD", "BAB", "EFE", 'A', DEFAULT_SENSOR,
                'B', "plateVanadium", 'D', "gearManganese", 'C', "casingNickel", 'E', "casingMikhail", 'F', "doubleplateTitanium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 9), "DCD", "BAB", "EFE", 'A', ADV_SENSOR,
                'B', "plateVanadium", 'D', "gearManganese", 'C', "casingNickel", 'E', "casingMikhail", 'F', "doubleplateTitanium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 86), "DCD", "BAB", "EFE", 'A', IMP_SENSOR,
                'B', "plateVanadium", 'D', "gearManganese", 'C', "casingNickel", 'E', "casingMikhail", 'F', "doubleplateTitanium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 110), "DCD", "BAB", "EFE", 'A', PER_SENSOR,
                'B', "plateVanadium", 'D', "gearManganese", 'C', "casingNickel", 'E', "casingMikhail", 'F', "doubleplateTitanium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 34), "CBC", "BCB", " A ", 'A', DEFAULT_SENSOR, 'B', "plateManganese",
                'C', "gearAluminium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 12), "CBC", "BCB", " A ", 'A', ADV_SENSOR, 'B', "plateManganese",
                'C', "gearAluminium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 87), "CBC", "BCB", " A ", 'A', IMP_SENSOR, 'B', "plateManganese",
                'C', "gearAluminium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 111), "CBC", "BCB", " A ", 'A', PER_SENSOR, 'B', "plateManganese",
                'C', "gearAluminium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 36), " B ", "BAB", " B ", 'A', DEFAULT_SENSOR, 'B', "ingotUranium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 13), " B ", "BAB", " B ", 'A', ADV_SENSOR, 'B', "ingotUranium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 88), " B ", "BAB", " B ", 'A', IMP_SENSOR, 'B', "ingotUranium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 113), " B ", "BAB", " B ", 'A', PER_SENSOR, 'B', "ingotUranium"
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 39), "CCC", "BAB", "BBB", 'A', DEFAULT_SENSOR, 'B', "plateMuntsa", 'C',
                "plateNichrome"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 15), "CCC", "BAB", "BBB", 'A', ADV_SENSOR, 'B', "plateMuntsa", 'C',
                "plateNichrome"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 91), "CCC", "BAB", "BBB", 'A', IMP_SENSOR, 'B', "plateMuntsa", 'C',
                "plateNichrome"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 115), "CCC", "BAB", "BBB", 'A', PER_SENSOR, 'B', "plateMuntsa", 'C',
                "plateNichrome"
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 43),
                "BCB",
                "BAB",
                "BCB",
                'A',
                DEFAULT_SENSOR,
                'B',
                Ic2Items.FluidCell,
                'C',
                "plateAlcled"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 45),
                "BCB",
                "BAB",
                "BCB",
                'A',
                DEFAULT_SENSOR,
                'B',
                Ic2Items.FluidCell,
                'C',
                "plateAlumel"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 48),
                "BCB",
                "BAB",
                "BCB",
                'A',
                DEFAULT_SENSOR,
                'B',
                Ic2Items.FluidCell,
                'C',
                "plateFerromanganese"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 50),
                "BCB",
                "BAB",
                "BCB",
                'A',
                DEFAULT_SENSOR,
                'B',
                Ic2Items.FluidCell,
                'C',
                "plateAluminumbronze"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 53),
                " B ",
                "CAD",
                " E ",
                'A',
                DEFAULT_SENSOR,
                'B',
                "oreIron",
                'C',
                "oreGold"
                ,
                'D',
                "oreEmerald",
                'E',
                "oreDiamond"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 54), "BBB", "CAC", "EEE", 'A', DEFAULT_SENSOR, 'B',
                new ItemStack(Items.SKULL, 1, 1),
                'C',
                "plateAluminumbronze"
                , 'E', "plateAlumel"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 56), "FBG", "CAD", "HEJ", 'A', DEFAULT_SENSOR,
                'B', "plateAlcled", 'C', "plateAlumel"
                , 'D', "plateVitalium", 'E', "plateRedbrass"
                , 'F', "plateMuntsa", 'G', "plateNichrome"
                , 'H', "plateVanadoalumite", 'J', "plateDuralumin"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 57), "   ", " AB", "   ", 'A', DEFAULT_SENSOR, 'B',
                new ItemStack(IUItem.module7, 1, 4)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 58),
                "EDE",
                "BBB",
                "CAC",
                'A',
                new ItemStack(IUItem.electricblock, 1, 3),
                'B',
                Ic2Items.teleporter,
                'C',
                new ItemStack(IUItem.tranformer, 1, 9),
                'D',
                DEFAULT_SENSOR,
                'E',
                Ic2Items.reinforcedStone
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 17),
                "EDE",
                "BBB",
                "CAC",
                'A',
                new ItemStack(IUItem.electricblock, 1, 5),
                'B',
                new ItemStack(IUItem.core, 1, 5),
                'C',
                new ItemStack(IUItem.tranformer, 1, 1),
                'D',
                ADV_SENSOR,
                'E',
                Ic2Items.reinforcedStone
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 93),
                "EDE",
                "BBB",
                "CAC",
                'A',
                new ItemStack(IUItem.electricblock, 1, 6),
                'B',
                new ItemStack(IUItem.core, 1, 6),
                'C',
                new ItemStack(IUItem.tranformer, 1, 3),
                'D',
                IMP_SENSOR,
                'E',
                Ic2Items.reinforcedStone
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 117),
                "EDE",
                "BBB",
                "CAC",
                'A',
                new ItemStack(IUItem.electricblock, 1, 6),
                'B',
                new ItemStack(IUItem.core, 1, 6),
                'C',
                new ItemStack(IUItem.tranformer, 1, 7),
                'D',
                PER_SENSOR,
                'E',
                Ic2Items.reinforcedStone
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 61), "DDD", "CAC", "BBB", 'A', DEFAULT_SENSOR, 'B', Blocks.DIRT, 'C',
                Items.STICK, 'D', Blocks.GLASS
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 18), "DDD", "CAC", "BBB", 'A', ADV_SENSOR, 'B', Blocks.DIRT, 'C',
                Items.STICK, 'D', Blocks.GLASS
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 94), "DDD", "CAC", "BBB", 'A', IMP_SENSOR, 'B', Blocks.DIRT, 'C',
                Items.STICK, 'D', Blocks.GLASS
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 118), "DDD", "CAC", "BBB", 'A', PER_SENSOR, 'B', Blocks.DIRT, 'C',
                Items.STICK, 'D', Blocks.GLASS
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 62), "CBC", "BAB", "CBC", 'A', DEFAULT_SENSOR, 'B', IUItem.paints, 'C',
                Ic2Items.carbonPlate
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 64), "BCD", "EAF", "GHJ", 'A', DEFAULT_SENSOR,
                'B', Blocks.SAPLING, 'C', Blocks.YELLOW_FLOWER,
                'D', Blocks.RED_FLOWER, 'E', Items.NETHER_WART,
                'F', Blocks.VINE, 'G', Blocks.WATERLILY,
                'H', Blocks.BROWN_MUSHROOM, 'J', Blocks.RED_MUSHROOM
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 19), "BCD", "EAF", "GHJ", 'A', ADV_SENSOR,
                'B', Blocks.SAPLING, 'C', Blocks.YELLOW_FLOWER,
                'D', Blocks.RED_FLOWER, 'E', Items.NETHER_WART,
                'F', Blocks.VINE, 'G', Blocks.WATERLILY,
                'H', Blocks.BROWN_MUSHROOM, 'J', Blocks.RED_MUSHROOM
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 95), "BCD", "EAF", "GHJ", 'A', IMP_SENSOR,
                'B', Blocks.SAPLING, 'C', Blocks.YELLOW_FLOWER,
                'D', Blocks.RED_FLOWER, 'E', Items.NETHER_WART,
                'F', Blocks.VINE, 'G', Blocks.WATERLILY,
                'H', Blocks.BROWN_MUSHROOM, 'J', Blocks.RED_MUSHROOM
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 119), "BCD", "EAF", "GHJ", 'A', PER_SENSOR,
                'B', Blocks.SAPLING, 'C', Blocks.YELLOW_FLOWER,
                'D', Blocks.RED_FLOWER, 'E', Items.NETHER_WART,
                'F', Blocks.VINE, 'G', Blocks.WATERLILY,
                'H', Blocks.BROWN_MUSHROOM, 'J', Blocks.RED_MUSHROOM
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 66), "DDD", "BAB", "   ", 'A', DEFAULT_SENSOR, 'B',
                Ic2Items.overclockerUpgrade, 'D', "plateFerromanganese"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 67), "DDD", "BAB", "   ", 'A', DEFAULT_SENSOR, 'B',
                Ic2Items.overclockerUpgrade, 'D', "plateVitalium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 68), "CCC", "BAB", " B ", 'A', DEFAULT_SENSOR, 'B',
                new ItemStack(IUItem.core, 1, 6),
                'C', "doubleplateMuntsa"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 179), "CCC", "BAB", " B ", 'A', ADV_SENSOR, 'B',
                new ItemStack(IUItem.core, 1, 8),
                'C', "doubleplateMuntsa"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 177), "CCC", "BAB", " B ", 'A', IMP_SENSOR, 'B',
                new ItemStack(IUItem.core, 1, 11),
                'C', "doubleplateMuntsa"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 178), "CCC", "BAB", " B ", 'A', PER_SENSOR, 'B',
                new ItemStack(IUItem.core, 1, 13),
                'C', "doubleplateMuntsa"
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 71),
                "CCC",
                "BAB",
                "DED",
                'A',
                DEFAULT_SENSOR,
                'C',
                "gearGermanium",
                'B',
                "doubleplateIridium",
                'D',
                "gearRedbrass",
                'E',
                IUItem.compressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 3), "CCC", "BAB", "DED", 'A', ADV_SENSOR, 'C', "gearGermanium", 'B',
                "doubleplateIridium", 'D', "gearRedbrass", 'E', IUItem.compressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 80), "CCC", "BAB", "DED", 'A', IMP_SENSOR, 'C', "gearGermanium", 'B',
                "doubleplateIridium", 'D', "gearRedbrass", 'E', IUItem.compressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 104), "CCC", "BAB", "DED", 'A', PER_SENSOR, 'C', "gearGermanium", 'B',
                "doubleplateIridium", 'D', "gearRedbrass", 'E', IUItem.compressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 75), "CCC", " A ", " B ", 'A', DEFAULT_SENSOR, 'B',
                new ItemStack(IUItem.itemiu, 1, 3), 'C', "plateCobalt"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 10), "CCC", "BAB", "   ", 'A', ADV_SENSOR, 'B',
                new ItemStack(IUItem.itemiu, 1, 3), 'C', "plateSilver"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 98), "CBC", "BAB", "DBD", 'A', DEFAULT_SENSOR, 'B',
                new ItemStack(IUItem.cell_all, 1, 5), 'C', "gearMagnesium", 'D', "plateTitanium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 99), "CBC", "BAB", "DBD", 'A', DEFAULT_SENSOR, 'B',
                Ic2Items.Uran238, 'C', "gearVanadium", 'D', "plateChromium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 100), "CBC", "BAB", "CBC", 'A', DEFAULT_SENSOR, 'B',
                IUItem.doublecompressIridiumplate, 'C', IUItem.photoniy_ingot
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 101),
                "CCC",
                "BAB",
                "DED",
                'A',
                DEFAULT_SENSOR,
                'C',
                "gearGermanium",
                'B',
                "doubleplateGermanium",
                'D',
                "gearNichrome",
                'E',
                IUItem.compressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 4), "CCC", "BAB", "DED", 'A', ADV_SENSOR, 'C', "gearGermanium", 'B',
                "doubleplateGermanium", 'D', "gearNichrome", 'E', IUItem.compressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 81), "CCC", "BAB", "DED", 'A', IMP_SENSOR, 'C', "gearGermanium", 'B',
                "doubleplateGermanium", 'D', "gearNichrome", 'E', IUItem.compressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 105), "CCC", "BAB", "DED", 'A', PER_SENSOR, 'C', "gearGermanium", 'B',
                "doubleplateGermanium", 'D', "gearNichrome", 'E', IUItem.compressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 112),
                "BBB",
                "CAC",
                "   ",
                'A',
                DEFAULT_SENSOR,
                'B',
                IUItem.photoniy_ingot,
                'C',
                "gearZinc"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 124),
                "CBC",
                "BAB",
                " B ",
                'A',
                DEFAULT_SENSOR,
                'B',
                "doubleplateVitalium",
                'C',
                "doubleplateInvar"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 135), "CBC", "BAB", " B ", 'A', ADV_SENSOR, 'B', "doubleplateVitalium",
                'C', "doubleplateInvar"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 146), "CBC", "BAB", " B ", 'A', IMP_SENSOR, 'B', "doubleplateVitalium",
                'C', "doubleplateInvar"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 157), "CBC", "BAB", " B ", 'A', PER_SENSOR, 'B', "doubleplateVitalium",
                'C', "doubleplateInvar"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 128),
                "DCD",
                "BAB",
                " E ",
                'A',
                DEFAULT_SENSOR,
                'C',
                Blocks.CRAFTING_TABLE
                ,
                'B',
                "platePlatinum",
                'D',
                "plateTin",
                'E',
                "plateIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 129), "DCD", "BAB", " E ", 'A', ADV_SENSOR, 'C', Blocks.CRAFTING_TABLE
                , 'B', "platePlatinum", 'D', "plateTin", 'E', "plateIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 130), "DCD", "BAB", " E ", 'A', IMP_SENSOR, 'C', Blocks.CRAFTING_TABLE
                , 'B', "platePlatinum", 'D', "plateTin", 'E', "plateIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 131), "DCD", "BAB", " E ", 'A', PER_SENSOR, 'C', Blocks.CRAFTING_TABLE
                , 'B', "platePlatinum", 'D', "plateTin", 'E', "plateIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 154),
                "CCC",
                "BAB",
                "   ",
                'A',
                DEFAULT_SENSOR,
                'C',
                Ic2Items.FluidCell,
                'B'
                ,
                "plateIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 155),
                "CCC",
                "BAB",
                "   ",
                'A',
                DEFAULT_SENSOR,
                'C',
                ModUtils.getCellFromFluid("ic2coolant"),
                'B'
                ,
                "plateNickel"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 156), " C ", "BAB", " D ", 'A', DEFAULT_SENSOR, 'B', "plateGermanium",
                'C', new ItemStack(IUItem.crafting_elements, 1, 60), 'D', new ItemStack(IUItem.crafting_elements, 1, 11)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 158),
                "CDE",
                "BAB",
                "FFF",
                'A',
                DEFAULT_SENSOR,
                'C',
                Items.DIAMOND_AXE,
                'D',
                Items.DIAMOND_PICKAXE,
                'E',
                Items.DIAMOND_SHOVEL,
                'B',
                "gearIridium",
                'F',
                "gearMagnesium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 170), "DCD", "BAB", " C ", 'A', DEFAULT_SENSOR, 'B',
                BlockName.te.getItemStack(
                        TeBlock.luminator_flat), 'C', Ic2Items.advancedMachine, 'D', Ic2Items.reinforcedGlass
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 171), "DCD", "BAB", " C ", 'A', ADV_SENSOR, 'B',
                new ItemStack(IUItem.core, 1, 3), 'C', Ic2Items.advancedMachine, 'D', Ic2Items.reinforcedGlass
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 172), "DCD", "BAB", " C ", 'A', IMP_SENSOR, 'B',
                new ItemStack(IUItem.core, 1, 4), 'C', Ic2Items.advancedMachine, 'D', Ic2Items.reinforcedGlass
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 173), "DCD", "BAB", " C ", 'A', PER_SENSOR, 'B',
                new ItemStack(IUItem.core, 1, 5), 'C', Ic2Items.advancedMachine, 'D', Ic2Items.reinforcedGlass
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 90), "BBB", "DAD", "EEE", 'A', DEFAULT_SENSOR, 'B', "gearMikhail", 'D',
                "gearPlatinum", 'C', Ic2Items.carbonPlate, 'E', "doubleplateRedbrass"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 79),
                "CCC",
                "BAB",
                "EDE",
                'A',
                DEFAULT_SENSOR,
                'B',
                Ic2Items.advancedAlloy,
                'C',
                Ic2Items.carbonPlate,
                'D',
                IUItem.toriy,
                'E',
                Ic2Items.platebronze
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 176),
                "CCC",
                "BAB",
                "EDE",
                'A',
                ADV_SENSOR,
                'B',
                Ic2Items.advancedAlloy,
                'C',
                Ic2Items.iridiumPlate,
                'D',
                IUItem.toriy,
                'E',
                Ic2Items.platebronze
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 174),
                "CCC",
                "BAB",
                "EDE",
                'A',
                IMP_SENSOR,
                'B',
                Ic2Items.advancedAlloy,
                'C',
                Ic2Items.iridiumPlate,
                'D',
                IUItem.toriy,
                'E',
                Ic2Items.platebronze
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 175),
                "CCC",
                "BAB",
                "EDE",
                'A',
                PER_SENSOR,
                'B',
                Ic2Items.advancedAlloy,
                'C',
                Ic2Items.iridiumPlate,
                'D',
                IUItem.toriy,
                'E',
                Ic2Items.platebronze
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 217),
                " C ",
                "BAB",
                "DDD",
                'A',
                DEFAULT_SENSOR,
                'B',
                "plateAlumel",
                'C',
                Items.FISHING_ROD,
                'D',
                "gearMuntsa"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 219), " C ", "BAB", "DDD", 'A', DEFAULT_SENSOR, 'B', "plateIron", 'D',
                Items.REDSTONE, 'C', Blocks.FURNACE
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 225), " C ", "BAB", "DDD", 'A', ADV_SENSOR, 'B', "plateIron", 'D',
                Items.REDSTONE, 'C', Blocks.FURNACE
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 221), " C ", "BAB", "DDD", 'A', IMP_SENSOR, 'B', "plateIron", 'D',
                Items.REDSTONE, 'C', Blocks.FURNACE
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 223), " C ", "BAB", "DDD", 'A', PER_SENSOR, 'B', "plateIron", 'D',
                Items.REDSTONE, 'C', Blocks.FURNACE
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 218), "CCC", "BAB", "DDD", 'A', DEFAULT_SENSOR, 'B', "plateIron", 'C',
                "plateTin", 'D', "plateCopper"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 224), "CCC", "BAB", "DDD", 'A', ADV_SENSOR, 'B', "plateIron", 'C',
                "plateTin", 'D', "plateCopper"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 220), "CCC", "BAB", "DDD", 'A', IMP_SENSOR, 'B', "plateIron", 'C',
                "plateTin", 'D', "plateCopper"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 222), "CCC", "BAB", "DDD", 'A', PER_SENSOR, 'B', "plateIron", 'C',
                "plateTin", 'D', "plateCopper"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 205), "CCC", "BAB", "D D", 'A', DEFAULT_SENSOR,
                'B', "gearCobalt", 'C', Ic2Items.advancedAlloy, 'D', "plateTitanium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 204), "CCC", "BAB", "D D", 'A', ADV_SENSOR,
                'B', "gearCobalt", 'C', Ic2Items.advancedAlloy, 'D', "plateTitanium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 202), "CCC", "BAB", "D D", 'A', IMP_SENSOR,
                'B', "gearCobalt", 'C', Ic2Items.advancedAlloy, 'D', "plateTitanium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 203), "CCC", "BAB", "D D", 'A', PER_SENSOR,
                'B', "gearCobalt", 'C', Ic2Items.advancedAlloy, 'D', "plateTitanium"
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 38),
                "CCC",
                "BAB",
                "DDD",
                'A',
                DEFAULT_SENSOR,
                'B',
                "doubleplateGermanium",
                'C',
                "gearVitalium",
                'D',
                "gearAlcled"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 14), "CCC", "BAB", "DDD", 'A', ADV_SENSOR, 'B', "doubleplateGermanium",
                'C', "gearVitalium", 'D', "gearAlcled"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 89), "CCC", "BAB", "DDD", 'A', IMP_SENSOR, 'B', "doubleplateGermanium",
                'C', "gearVitalium", 'D', "gearAlcled"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 114),
                "CCC",
                "BAB",
                "DDD",
                'A',
                PER_SENSOR,
                'B',
                "doubleplateGermanium",
                'C',
                "gearVitalium",
                'D',
                "gearAlcled"
        );
        for (int i = 0; i < 5; i++) {
            Recipes.advRecipes.addShapelessRecipe(
                    new ItemStack(IUItem.heatcold_pipes, 1, i),
                    new ItemStack(IUItem.pipes, 1, i),
                    new ItemStack(IUItem.coolpipes, 1, i)
            );
        }

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.universal_cable, 1, 1),
                " A ",
                "BCB",
                " A ",
                'C',
                OreDictionary.getOres("ingotCobalt"),
                'A',
                new ItemStack(IUItem.universal_cable, 1, 0),
                'B',
                Ic2Items.denseplatetin
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.universal_cable, 1, 2),
                " A ",
                "BCB",
                " A ",
                'C',
                Ic2Items.denseplatetin,
                'A',
                new ItemStack(IUItem.universal_cable, 1, 1),
                'B',
                Ic2Items.advancedAlloy
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.universal_cable, 1, 3),
                "DAD",
                "BCB",
                "DAD",
                'D',
                Ic2Items.denseplategold,
                'C',
                Ic2Items.advancedAlloy,
                'A',
                new ItemStack(IUItem.universal_cable, 1, 2),
                'B',
                Ic2Items.denseplatelead
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.universal_cable, 1, 4),
                "DAD",
                "BCB",
                "DAD",
                'D',
                OreDictionary.getOres("ingotRedbrass"),
                'C',
                Ic2Items.carbonPlate,
                'A',
                new ItemStack(IUItem.universal_cable, 1, 3),
                'B',
                OreDictionary.getOres("ingotSpinel")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.universal_cable, 1, 5),
                " A ",
                "BCB",
                " A ",
                'C',
                OreDictionary.getOres("doubleplateVitalium"),
                'A',
                new ItemStack(IUItem.universal_cable, 1, 4),
                'B',
                Ic2Items.denseplateadviron
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.universal_cable, 1, 6),
                "DAD",
                "BCB",
                "DAD",
                'D',
                Ic2Items.carbonPlate,
                'C',
                OreDictionary.getOres("ingotAlcled"),
                'A',
                new ItemStack(IUItem.universal_cable, 1, 5),
                'B',
                OreDictionary.getOres("ingotDuralumin")
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.universal_cable, 1, 7),
                "A A",
                "BCB",
                "A A",
                'C',
                new ItemStack(IUItem.photoniy_ingot),
                'B',
                new ItemStack(IUItem.photoniy),
                'A',
                new ItemStack(IUItem.universal_cable, 1, 6)
        );

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.universal_cable, 1, 8),
                "BBB", "AAA", "BBB", 'A', new ItemStack(IUItem.photoniy_ingot), 'B', new ItemStack(IUItem.universal_cable, 1, 7)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.universal_cable, 1, 9),
                "BBB",
                "ACA",
                "BBB",
                'C',
                new ItemStack(IUItem.basecircuit, 1, 10),
                'A',
                new ItemStack(IUItem.photoniy_ingot),
                'B',
                new ItemStack(IUItem.universal_cable, 1, 8)
        );

        for (int i = 0; i < 11; i++) {
            Recipes.advRecipes.addShapelessRecipe(
                    new ItemStack(IUItem.universal_cable, 1, i), new ItemStack(IUItem.heatcold_pipes, 1, 4),
                    new ItemStack(IUItem.cable, 1, i), new ItemStack(IUItem.expcable), new ItemStack(IUItem.scable),
                    new ItemStack(IUItem.qcable)
            );
        }

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewater, 1, 0),
                "CAC",
                "ABA",
                " A ",
                'A',
                "logWood",
                'B',
                "plankWood", 'C', new ItemStack(Items.DYE, 1, 4)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewater, 1, 1),
                " A ",
                "ABA",
                " A ",
                'A',
                "blockBronze",
                'B',
                new ItemStack(IUItem.water_rotor_wood, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewater, 1, 2),
                " A ",
                "ABA",
                " A ",
                'A',
                "blockIron",
                'B',
                new ItemStack(IUItem.water_rotor_bronze, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewater, 1, 3),
                " A ",
                "ABA",
                " A ",
                'A',
                "plateSteel",
                'B',
                new ItemStack(IUItem.water_rotor_iron, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewater, 1, 4),
                " A ",
                "ABA",
                " A ",
                'A',
                "plateCarbon",
                'B',
                new ItemStack(IUItem.water_rotor_steel, 1, OreDictionary.WILDCARD_VALUE)
        );
        //
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewater, 1, 5),
                "CAC",
                "ABA",
                "CAC", 'C',
                IUItem.compressIridiumplate,
                'A',
                Ic2Items.iridiumPlate,
                'B',
                new ItemStack(IUItem.water_rotor_carbon, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewater, 1, 6),
                "CAC",
                "ABA",
                "CAC", 'C',
                IUItem.doublecompressIridiumplate,
                'A',
                new ItemStack(IUItem.compresscarbon),
                'B',
                new ItemStack(IUItem.water_iridium.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewater, 1, 7),
                "DCD",
                "ABA",
                "DCD", 'D',
                IUItem.circuitSpectral,
                'C',
                new ItemStack(IUItem.compressIridiumplate),
                'A',
                new ItemStack(IUItem.advnanobox),
                'B',
                new ItemStack(IUItem.water_compressiridium.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewater, 1, 8),
                "DCD",
                "ABA",
                " C ", 'D',
                new ItemStack(IUItem.excitednucleus, 1, 5),
                'C',
                IUItem.cirsuitQuantum,
                'A',
                new ItemStack(IUItem.quantumtool),
                'B',
                new ItemStack(IUItem.water_spectral.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewater, 1, 10),
                "DCD",
                "CBC",
                " C ", 'D',
                new ItemStack(IUItem.excitednucleus, 1, 6),
                'C',
                new ItemStack(IUItem.quantumtool),
                'B',
                new ItemStack(IUItem.water_myphical.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewater, 1, 9),
                "DCD",
                "CBC",
                "ACA", 'D',
                new ItemStack(IUItem.neutroniumingot),
                'A',
                Ic2Items.iridiumPlate,
                'C',
                IUItem.cirsuitQuantum,
                'B',
                new ItemStack(IUItem.water_photon.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewater, 1, 11),
                "DCD",
                "CBC",
                " C ", 'D',
                new ItemStack(IUItem.excitednucleus, 1, 5),
                'C',
                new ItemStack(IUItem.advQuantumtool),
                'B',
                new ItemStack(IUItem.water_neutron.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewater, 1, 12),
                "ACA",
                "CBC",
                "ACA",
                'A',
                new ItemStack(IUItem.doublecompressIridiumplate),
                'C',
                IUItem.circuitSpectral,
                'B',
                new ItemStack(IUItem.water_barionrotor.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.corewater, 1, 13),
                "ECE",
                "CBC",
                "ACA", 'E',
                IUItem.circuitSpectral,
                'A',
                new ItemStack(IUItem.doublecompressIridiumplate),
                'C',
                new ItemStack(IUItem.photoniy_ingot),
                'B',
                new ItemStack(IUItem.water_adronrotor.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 0),
                "A A", "CBC", "A A", 'A', new ItemStack(IUItem.advnanobox)
                , 'B', new ItemStack(IUItem.module_schedule), 'C', "casingNichrome"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 1),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.advQuantumtool), 'B', new ItemStack(IUItem.core, 1, 3)
                , 'C', new ItemStack(IUItem.water_rotors_upgrade, 1, 0), 'D', "casingRedbrass"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 2),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.adv_spectral_box), 'B', new ItemStack(IUItem.core, 1, 5)
                , 'C', new ItemStack(IUItem.water_rotors_upgrade, 1, 1), 'D', "casingMuntsa"
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 3),
                "A A", "CBC", "A A", 'A', new ItemStack(IUItem.advnanobox)
                , 'B', new ItemStack(IUItem.module_schedule), 'C', Ic2Items.iridiumPlate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 4),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.advQuantumtool), 'B', new ItemStack(IUItem.core, 1, 4)
                , 'C', new ItemStack(IUItem.water_rotors_upgrade, 1, 3), 'D', IUItem.compressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 5),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.adv_spectral_box), 'B', new ItemStack(IUItem.core, 1, 6)
                , 'C', new ItemStack(IUItem.water_rotors_upgrade, 1, 4), 'D', IUItem.doublecompressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 6),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.advnanobox)
                , 'B', new ItemStack(IUItem.module_schedule), 'C', Ic2Items.iridiumPlate, 'D', "casingDuralumin"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 7),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.advQuantumtool), 'B', new ItemStack(IUItem.core, 1, 4)
                , 'C', new ItemStack(IUItem.water_rotors_upgrade, 1, 6), 'D', IUItem.compressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 8),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.adv_spectral_box), 'B', new ItemStack(IUItem.core, 1, 6)
                , 'C', new ItemStack(IUItem.water_rotors_upgrade, 1, 7), 'D', IUItem.doublecompressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 9),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.adv_spectral_box)
                , 'B', new ItemStack(IUItem.module_schedule), 'C', IUItem.compressIridiumplate, 'D', "doubleplateAlumel"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 10),
                "ADA", "CBC", "DED", 'A', new ItemStack(IUItem.advnanobox)
                , 'B', new ItemStack(IUItem.module_schedule), 'C', Ic2Items.iridiumPlate, 'D', "doubleplatePlatinum", 'E',
                new ItemStack(IUItem.core, 1, 3)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 11),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.advQuantumtool), 'B', new ItemStack(IUItem.core, 1, 4)
                , 'C', new ItemStack(IUItem.water_rotors_upgrade, 1, 10), 'D', IUItem.compressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 12),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.adv_spectral_box), 'B', new ItemStack(IUItem.core, 1, 6)
                , 'C', new ItemStack(IUItem.water_rotors_upgrade, 1, 11), 'D', IUItem.doublecompressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 13),
                "ADA",
                "CBC",
                "DED",
                'A',
                new ItemStack(IUItem.advnanobox)
                ,
                'B',
                new ItemStack(IUItem.module_schedule),
                'C',
                Ic2Items.iridiumPlate,
                'D',
                "doubleplateRedbrass",
                'E',
                new ItemStack(IUItem.core, 1, 4)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 14),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.advQuantumtool), 'B', new ItemStack(IUItem.core, 1, 5)
                , 'C', new ItemStack(IUItem.water_rotors_upgrade, 1, 13), 'D', IUItem.compressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.water_rotors_upgrade, 1, 15),
                "ADA", "CBC", "ADA", 'A', new ItemStack(IUItem.adv_spectral_box), 'B', new ItemStack(IUItem.core, 1, 6)
                , 'C', new ItemStack(IUItem.water_rotors_upgrade, 1, 14), 'D', IUItem.doublecompressIridiumplate
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.item_pipes, 4, 0),
                "BBB",
                "CAC",
                "BBB",
                'A',
                "plateIron",
                'B',
                "plateTin",
                'C',
                Items.REDSTONE
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.item_pipes, 4, 1), "BBB", "CAC", "BBB", 'A', "plateIron", 'B', "plateTin", 'C',
                new ItemStack(Items.DYE, 1, 4)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.item_pipes, 4, 2),
                "BBB",
                "CAC",
                "BBB",
                'A',
                "plateIron",
                'B',
                "plateCobalt",
                'C',
                Items.REDSTONE
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.item_pipes, 4, 3), "BBB", "CAC", "BBB", 'A', "plateIron", 'B', "plateCobalt", 'C',
                new ItemStack(Items.DYE, 1, 4)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.simplemachine, 1, 0), " B ", "DAE", " C ",
                'A', Ic2Items.machine, 'C', Ic2Items.elemotor,
                'D', new ItemStack(IUItem.crafting_elements, 1, 72), 'E', new ItemStack(IUItem.crafting_elements, 1, 44)
                , 'B', new ItemStack(IUItem.crafting_elements, 1, 69)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.simplemachine, 1, 1), " B ", "DAE", " C ",
                'A', Ic2Items.machine, 'C', Ic2Items.elemotor,
                'D', new ItemStack(IUItem.crafting_elements, 1, 72), 'E', new ItemStack(IUItem.crafting_elements, 1, 44)
                , 'B', new ItemStack(IUItem.crafting_elements, 1, 63)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.simplemachine, 1, 2), "B H", "DAE", " C ",
                'A', Ic2Items.machine, 'C', Ic2Items.elemotor,
                'D', new ItemStack(IUItem.crafting_elements, 1, 72), 'E', new ItemStack(IUItem.crafting_elements, 1, 44)
                , 'B', new ItemStack(IUItem.crafting_elements, 1, 219), 'H', new ItemStack(IUItem.crafting_elements, 1, 35)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.simplemachine, 1, 3), " B ", "DAE", " C ",
                'A', Ic2Items.machine, 'C', Ic2Items.elemotor,
                'D', new ItemStack(IUItem.crafting_elements, 1, 72), 'E', new ItemStack(IUItem.crafting_elements, 1, 44)
                , 'B', new ItemStack(IUItem.crafting_elements, 1, 159)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.simplemachine, 1, 5), " B ", "DAE", " C ",
                'A', Ic2Items.machine, 'C', Ic2Items.elemotor,
                'D', new ItemStack(IUItem.crafting_elements, 1, 72), 'E', new ItemStack(IUItem.crafting_elements, 1, 44)
                , 'B', new ItemStack(IUItem.crafting_elements, 1, 33)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 0), " B ", "DAE", " C ",
                'A', Ic2Items.machine, 'C', Ic2Items.elemotor,
                'D', new ItemStack(IUItem.crafting_elements, 1, 72), 'E', new ItemStack(IUItem.crafting_elements, 1, 44)
                , 'B', new ItemStack(IUItem.crafting_elements, 1, 165)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 4), " B ", "DAE", " C ",
                'A', Ic2Items.machine, 'C', Ic2Items.elemotor,
                'D', new ItemStack(IUItem.crafting_elements, 1, 72), 'E', new ItemStack(IUItem.crafting_elements, 1, 44)
                , 'B', new ItemStack(IUItem.crafting_elements, 1, 163)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 8), " B ", "DAE", " C ",
                'A', Ic2Items.machine, 'C', Ic2Items.elemotor,
                'D', new ItemStack(IUItem.crafting_elements, 1, 72), 'E', new ItemStack(IUItem.crafting_elements, 1, 44)
                , 'B', new ItemStack(IUItem.crafting_elements, 1, 132)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 4), "B F", "DAE", " C ",
                'A', Ic2Items.machine, 'C', Ic2Items.elemotor,
                'D', new ItemStack(IUItem.crafting_elements, 1, 72), 'E', new ItemStack(IUItem.crafting_elements, 1, 44)
                , 'B', new ItemStack(IUItem.crafting_elements, 1, 33), 'F', new ItemStack(IUItem.crafting_elements, 1, 128)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 0), "B F", "DAE", " C ",
                'A', Ic2Items.machine, 'C', Ic2Items.elemotor,
                'D', new ItemStack(IUItem.crafting_elements, 1, 72), 'E', new ItemStack(IUItem.crafting_elements, 1, 44)
                , 'B', new ItemStack(IUItem.crafting_elements, 1, 64), 'F', new ItemStack(IUItem.crafting_elements, 1, 61)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 16), " B ", "DAE", " C ",
                'A', Ic2Items.machine, 'C', Ic2Items.elemotor,
                'D', new ItemStack(IUItem.crafting_elements, 1, 72), 'E', new ItemStack(IUItem.crafting_elements, 1, 44)
                , 'B', new ItemStack(IUItem.crafting_elements, 1, 218)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 12), " B ", "DAE", " C ",
                'A', Ic2Items.machine, 'C', Ic2Items.elemotor,
                'D', new ItemStack(IUItem.crafting_elements, 1, 70), 'E', new ItemStack(IUItem.crafting_elements, 1, 44)
                , 'B', new ItemStack(IUItem.crafting_elements, 1, 39)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 8), "BGF", "DAE", " C ",
                'A', Ic2Items.machine, 'C', Ic2Items.elemotor,
                'D', new ItemStack(IUItem.crafting_elements, 1, 72), 'E', new ItemStack(IUItem.crafting_elements, 1, 44)
                , 'B', new ItemStack(IUItem.crafting_elements, 1, 34), 'F', new ItemStack(IUItem.crafting_elements, 1, 27)
                , 'G', new ItemStack(IUItem.crafting_elements, 1, 154)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 0), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 138),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20),
                'D', new ItemStack(IUItem.crafting_elements, 1, 47),
                'E', new ItemStack(IUItem.crafting_elements, 1, 2), // ниже менять
                'B', new ItemStack(IUItem.simplemachine, 1, 0)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 1), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 139),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96),
                'D', new ItemStack(IUItem.crafting_elements, 1, 49),
                'E', new ItemStack(IUItem.crafting_elements, 1, 78), // ниже менять
                'B', new ItemStack(IUItem.machines_base, 1, 0)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 2), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 140),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120),
                'D', new ItemStack(IUItem.crafting_elements, 1, 51),
                'E', new ItemStack(IUItem.crafting_elements, 1, 103), // ниже менять
                'B', new ItemStack(IUItem.machines_base, 1, 1)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 6), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 138),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20),
                'D', new ItemStack(IUItem.crafting_elements, 1, 47),
                'E', new ItemStack(IUItem.crafting_elements, 1, 225), // ниже менять
                'B', new ItemStack(IUItem.simplemachine, 1, 2)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 7), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 139),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96),
                'D', new ItemStack(IUItem.crafting_elements, 1, 49),
                'E', new ItemStack(IUItem.crafting_elements, 1, 221), // ниже менять
                'B', new ItemStack(IUItem.machines_base, 1, 6)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 8), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 140),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120),
                'D', new ItemStack(IUItem.crafting_elements, 1, 51),
                'E', new ItemStack(IUItem.crafting_elements, 1, 223), // ниже менять
                'B', new ItemStack(IUItem.machines_base, 1, 7)
        );


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 9), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 138),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20),
                'D', new ItemStack(IUItem.crafting_elements, 1, 47),
                'E', new ItemStack(IUItem.crafting_elements, 1, 160), // ниже менять
                'B', new ItemStack(IUItem.simplemachine, 1, 3)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 10), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 139),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96),
                'D', new ItemStack(IUItem.crafting_elements, 1, 49),
                'E', new ItemStack(IUItem.crafting_elements, 1, 161), // ниже менять
                'B', new ItemStack(IUItem.machines_base, 1, 9)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 11), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 140),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120),
                'D', new ItemStack(IUItem.crafting_elements, 1, 51),
                'E', new ItemStack(IUItem.crafting_elements, 1, 162), // ниже менять
                'B', new ItemStack(IUItem.machines_base, 1, 10)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 3), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 138),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20),
                'D', new ItemStack(IUItem.crafting_elements, 1, 47),
                'E', new ItemStack(IUItem.crafting_elements, 1, 1), // ниже менять
                'B', new ItemStack(IUItem.simplemachine, 1, 1)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 4), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 139),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96),
                'D', new ItemStack(IUItem.crafting_elements, 1, 49),
                'E', new ItemStack(IUItem.crafting_elements, 1, 77), // ниже менять
                'B', new ItemStack(IUItem.machines_base, 1, 3)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base, 1, 5), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 140),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120),
                'D', new ItemStack(IUItem.crafting_elements, 1, 51),
                'E', new ItemStack(IUItem.crafting_elements, 1, 102), // ниже менять
                'B', new ItemStack(IUItem.machines_base, 1, 4)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base1, 1, 0), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 138),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20),
                'D', new ItemStack(IUItem.crafting_elements, 1, 47),
                'E', new ItemStack(IUItem.crafting_elements, 1, 9), // ниже менять
                'B', new ItemStack(IUItem.simplemachine, 1, 5)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base1, 1, 1), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 139),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96),
                'D', new ItemStack(IUItem.crafting_elements, 1, 49),
                'E', new ItemStack(IUItem.crafting_elements, 1, 86), // ниже менять
                'B', new ItemStack(IUItem.machines_base1, 1, 0)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base1, 1, 2), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 140),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120),
                'D', new ItemStack(IUItem.crafting_elements, 1, 51),
                'E', new ItemStack(IUItem.crafting_elements, 1, 110), // ниже менять
                'B', new ItemStack(IUItem.machines_base1, 1, 1)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 1), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 138),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20),
                'D', new ItemStack(IUItem.crafting_elements, 1, 47),
                'E', new ItemStack(IUItem.crafting_elements, 1, 166), // ниже менять
                'B', new ItemStack(IUItem.machines_base2, 1, 0)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 2), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 139),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96),
                'D', new ItemStack(IUItem.crafting_elements, 1, 49),
                'E', new ItemStack(IUItem.crafting_elements, 1, 167), // ниже менять
                'B', new ItemStack(IUItem.machines_base2, 1, 1)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 3), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 140),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120),
                'D', new ItemStack(IUItem.crafting_elements, 1, 51),
                'E', new ItemStack(IUItem.crafting_elements, 1, 168), // ниже менять
                'B', new ItemStack(IUItem.machines_base2, 1, 2)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 5), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 138),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20),
                'D', new ItemStack(IUItem.crafting_elements, 1, 47),
                'E', new ItemStack(IUItem.crafting_elements, 1, 125), // ниже менять
                'B', new ItemStack(IUItem.machines_base2, 1, 4)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 6), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 139),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96),
                'D', new ItemStack(IUItem.crafting_elements, 1, 49),
                'E', new ItemStack(IUItem.crafting_elements, 1, 126), // ниже менять
                'B', new ItemStack(IUItem.machines_base2, 1, 5)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 7), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 140),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120),
                'D', new ItemStack(IUItem.crafting_elements, 1, 51),
                'E', new ItemStack(IUItem.crafting_elements, 1, 127), // ниже менять
                'B', new ItemStack(IUItem.machines_base2, 1, 6)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 9), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 138),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20),
                'D', new ItemStack(IUItem.crafting_elements, 1, 47),
                'E', new ItemStack(IUItem.crafting_elements, 1, 133), // ниже менять
                'B', new ItemStack(IUItem.machines_base2, 1, 8)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 10), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 139),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96),
                'D', new ItemStack(IUItem.crafting_elements, 1, 49),
                'E', new ItemStack(IUItem.crafting_elements, 1, 134), // ниже менять
                'B', new ItemStack(IUItem.machines_base2, 1, 9)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base2, 1, 11), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 140),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120),
                'D', new ItemStack(IUItem.crafting_elements, 1, 51),
                'E', new ItemStack(IUItem.crafting_elements, 1, 136), // ниже менять
                'B', new ItemStack(IUItem.machines_base2, 1, 10)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 17), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 138),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20),
                'D', new ItemStack(IUItem.crafting_elements, 1, 47),
                'E', new ItemStack(IUItem.crafting_elements, 1, 224), // ниже менять
                'B', new ItemStack(IUItem.machines_base3, 1, 16)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 18), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 139),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96),
                'D', new ItemStack(IUItem.crafting_elements, 1, 49),
                'E', new ItemStack(IUItem.crafting_elements, 1, 220), // ниже менять
                'B', new ItemStack(IUItem.machines_base3, 1, 17)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 19), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 140),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120),
                'D', new ItemStack(IUItem.crafting_elements, 1, 51),
                'E', new ItemStack(IUItem.crafting_elements, 1, 22), // ниже менять
                'B', new ItemStack(IUItem.machines_base3, 1, 18)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 1), "AGA", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 138),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20),
                'D', new ItemStack(IUItem.crafting_elements, 1, 47),
                'E', new ItemStack(IUItem.crafting_elements, 1, 19), // ниже менять
                'B', new ItemStack(IUItem.machines_base3, 1, 0),
                'G', new ItemStack(IUItem.crafting_elements, 1, 18)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 2), "AGA", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 139),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96),
                'D', new ItemStack(IUItem.crafting_elements, 1, 49),
                'E', new ItemStack(IUItem.crafting_elements, 1, 96), // ниже менять
                'B', new ItemStack(IUItem.machines_base3, 1, 1),
                'G', new ItemStack(IUItem.crafting_elements, 1, 119)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 3), "AGA", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 140),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120),
                'D', new ItemStack(IUItem.crafting_elements, 1, 51),
                'E', new ItemStack(IUItem.crafting_elements, 1, 119), // ниже менять
                'B', new ItemStack(IUItem.machines_base3, 1, 2),
                'G', new ItemStack(IUItem.crafting_elements, 1, 118)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 5), "AGA", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 138),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20),
                'D', new ItemStack(IUItem.crafting_elements, 1, 47),
                'E', new ItemStack(IUItem.crafting_elements, 1, 9), // ниже менять
                'B', new ItemStack(IUItem.machines_base3, 1, 4),
                'G', new ItemStack(IUItem.crafting_elements, 1, 129)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 6), "AGA", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 139),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96),
                'D', new ItemStack(IUItem.crafting_elements, 1, 49),
                'E', new ItemStack(IUItem.crafting_elements, 1, 86), // ниже менять
                'B', new ItemStack(IUItem.machines_base3, 1, 5),
                'G', new ItemStack(IUItem.crafting_elements, 1, 130)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 7), "AGA", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 140),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120),
                'D', new ItemStack(IUItem.crafting_elements, 1, 51),
                'E', new ItemStack(IUItem.crafting_elements, 1, 110), // ниже менять
                'B', new ItemStack(IUItem.machines_base3, 1, 6),
                'G', new ItemStack(IUItem.crafting_elements, 1, 131)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 13), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 138),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20),
                'D', new ItemStack(IUItem.crafting_elements, 1, 47),
                'E', new ItemStack(IUItem.crafting_elements, 1, 15), // ниже менять
                'B', new ItemStack(IUItem.machines_base3, 1, 12)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 14), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 139),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96),
                'D', new ItemStack(IUItem.crafting_elements, 1, 49),
                'E', new ItemStack(IUItem.crafting_elements, 1, 91), // ниже менять
                'B', new ItemStack(IUItem.machines_base3, 1, 13)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 15), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 140),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120),
                'D', new ItemStack(IUItem.crafting_elements, 1, 51),
                'E', new ItemStack(IUItem.crafting_elements, 1, 115), // ниже менять
                'B', new ItemStack(IUItem.machines_base3, 1, 14)
        );


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 9), "AGA", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 138),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20),
                'D', new ItemStack(IUItem.crafting_elements, 1, 47),
                'E', new ItemStack(IUItem.crafting_elements, 1, 12), // ниже менять
                'B', new ItemStack(IUItem.machines_base3, 1, 8),
                'G', new ItemStack(IUItem.crafting_elements, 1, 6)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 10), "AGA", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 139),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96),
                'D', new ItemStack(IUItem.crafting_elements, 1, 49),
                'E', new ItemStack(IUItem.crafting_elements, 1, 87), // ниже менять
                'B', new ItemStack(IUItem.machines_base3, 1, 9),
                'G', new ItemStack(IUItem.crafting_elements, 1, 83)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base3, 1, 11), "AGA", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 140),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120),
                'D', new ItemStack(IUItem.crafting_elements, 1, 51),
                'E', new ItemStack(IUItem.crafting_elements, 1, 111), // ниже менять
                'B', new ItemStack(IUItem.machines_base3, 1, 10),
                'G', new ItemStack(IUItem.crafting_elements, 1, 107)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base1, 1, 3), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 138),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20),
                'D', new ItemStack(IUItem.crafting_elements, 1, 47),
                'E', new ItemStack(IUItem.crafting_elements, 1, 135), // ниже менять
                'B', new ItemStack(IUItem.machines_base1, 1, 2)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base1, 1, 4), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 139),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96),
                'D', new ItemStack(IUItem.crafting_elements, 1, 49),
                'E', new ItemStack(IUItem.crafting_elements, 1, 146), // ниже менять
                'B', new ItemStack(IUItem.machines_base1, 1, 3)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base1, 1, 5), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 140),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120),
                'D', new ItemStack(IUItem.crafting_elements, 1, 51),
                'E', new ItemStack(IUItem.crafting_elements, 1, 157), // ниже менять
                'B', new ItemStack(IUItem.machines_base1, 1, 4)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base1, 1, 6), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 137),
                'C', Ic2Items.elemotor,
                'D', new ItemStack(IUItem.crafting_elements, 1, 44),
                'E', new ItemStack(IUItem.crafting_elements, 1, 124), // ниже менять
                'B', new ItemStack(IUItem.machines_base, 1, 2)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base1, 1, 7), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 138),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20),
                'D', new ItemStack(IUItem.crafting_elements, 1, 47),
                'E', new ItemStack(IUItem.crafting_elements, 1, 135), // ниже менять
                'B', new ItemStack(IUItem.machines_base1, 1, 6)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base1, 1, 8), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 139),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96),
                'D', new ItemStack(IUItem.crafting_elements, 1, 49),
                'E', new ItemStack(IUItem.crafting_elements, 1, 146), // ниже менять
                'B', new ItemStack(IUItem.machines_base1, 1, 7)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines_base1, 1, 9), "A A", "DBE", "ACA",
                'A', new ItemStack(IUItem.crafting_elements, 1, 140),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120),
                'D', new ItemStack(IUItem.crafting_elements, 1, 51),
                'E', new ItemStack(IUItem.crafting_elements, 1, 157), // ниже менять
                'B', new ItemStack(IUItem.machines_base1, 1, 8)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 45), "   ", "CAB", "   ", 'A', Ic2Items.machine,
                'C', Ic2Items.elemotor, 'B', new ItemStack(IUItem.crafting_elements, 1, 101)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 46), "D D", "CAB", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 45),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20), 'B', new ItemStack(IUItem.crafting_elements, 1, 4), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 47), "D D", "CAB", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 46),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96), 'B', new ItemStack(IUItem.crafting_elements, 1, 81), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 48), "D D", "CAB", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 47),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120), 'B', new ItemStack(IUItem.crafting_elements, 1, 105), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 140)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 49), "   ", "CAB", "   ", 'A', Ic2Items.advancedMachine,
                'C', Ic2Items.elemotor, 'B', new ItemStack(IUItem.crafting_elements, 1, 170)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 50), "D D", "CAB", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 49),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20), 'B', new ItemStack(IUItem.crafting_elements, 1, 171), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 51), "D D", "CAB", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 50),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96), 'B', new ItemStack(IUItem.crafting_elements, 1, 172), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 52), "D D", "CAB", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 51),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120), 'B', new ItemStack(IUItem.crafting_elements, 1, 173), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 140)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 53), " E ", "CAB", " F ", 'A', Ic2Items.advancedMachine,
                'C', Ic2Items.elemotor, 'B', new ItemStack(IUItem.crafting_elements, 1, 58)
                , 'E', new ItemStack(IUItem.crafting_elements, 1, 27), 'F', new ItemStack(IUItem.crafting_elements, 1, 154)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 54), "DED", "CAB", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 53),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20), 'B', new ItemStack(IUItem.crafting_elements, 1, 17), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 138)
                , 'E', new ItemStack(IUItem.crafting_elements, 1, 6)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 55), "DED", "CAB", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 54),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96), 'B', new ItemStack(IUItem.crafting_elements, 1, 93), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 139)
                , 'E', new ItemStack(IUItem.crafting_elements, 1, 83)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 56), "DED", "CAB", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 55),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120), 'B', new ItemStack(IUItem.crafting_elements, 1, 117), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 140)
                , 'E', new ItemStack(IUItem.crafting_elements, 1, 107)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 57), " B ", " A ", "   ", 'A', Ic2Items.advancedMachine,
                'C', Ic2Items.elemotor, 'B', new ItemStack(IUItem.crafting_elements, 1, 79)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 58), "DBD", " A ", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 57),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20), 'B', new ItemStack(IUItem.crafting_elements, 1, 176), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 59), "DBD", " A ", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 58),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96), 'B', new ItemStack(IUItem.crafting_elements, 1, 174), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 60), "DBD", " A ", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 59),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120), 'B', new ItemStack(IUItem.crafting_elements, 1, 175), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 140)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 61), " B ", " A ", "   ", 'A', Ic2Items.advancedMachine,
                'C', Ic2Items.elemotor, 'B', new ItemStack(IUItem.crafting_elements, 1, 68)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 62), "DBD", " A ", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 61),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20), 'B', new ItemStack(IUItem.crafting_elements, 1, 179), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 63), "DBD", " A ", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 62),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96), 'B', new ItemStack(IUItem.crafting_elements, 1, 177), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 64), "DBD", " A ", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 63),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120), 'B', new ItemStack(IUItem.crafting_elements, 1, 178), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 140)
        );


        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 65), " B ", " A ", "   ", 'A', Ic2Items.machine,
                'C', Ic2Items.elemotor, 'B', new ItemStack(IUItem.crafting_elements, 1, 196)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 66), "DBD", " A ", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 65),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20), 'B', new ItemStack(IUItem.crafting_elements, 1, 199), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 67), "DBD", " A ", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 66),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96), 'B', new ItemStack(IUItem.crafting_elements, 1, 197), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 68), "DBD", " A ", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 67),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120), 'B', new ItemStack(IUItem.crafting_elements, 1, 198), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 140)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 28), "   ", "CAB", "   ", 'A', Ic2Items.machine,
                'C', Ic2Items.elemotor, 'B', new ItemStack(IUItem.crafting_elements, 1, 71)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 29), "D D", "CAB", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 28),
                'C', new ItemStack(IUItem.crafting_elements, 1, 20), 'B', new ItemStack(IUItem.crafting_elements, 1, 3), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 30), "D D", "CAB", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 29),
                'C', new ItemStack(IUItem.crafting_elements, 1, 96), 'B', new ItemStack(IUItem.crafting_elements, 1, 80), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 31), "D D", "CAB", "D D", 'A', new ItemStack(IUItem.basemachine2, 1, 30),
                'C', new ItemStack(IUItem.crafting_elements, 1, 120), 'B', new ItemStack(IUItem.crafting_elements, 1, 104), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 140)
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines, 1, 4),
                " B ", "CAD", " E ",
                'A', Ic2Items.machine, 'B', new ItemStack(IUItem.crafting_elements, 1, 74)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 70), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 47), 'E', Ic2Items.elemotor

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine, 1, 3),
                "F F", "BAD", "FEF",
                'A', new ItemStack(IUItem.machines, 1, 4), 'B', new ItemStack(IUItem.crafting_elements, 1, 235)
                , 'F', new ItemStack(IUItem.crafting_elements, 1, 138), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 49), 'E', new ItemStack(IUItem.crafting_elements, 1, 20)

        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines, 1, 6),
                "B F", "CAD", " E ",
                'A', Ic2Items.machine, 'B', new ItemStack(IUItem.crafting_elements, 1, 73)
                , 'C', new ItemStack(IUItem.crafting_elements, 1, 70), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 44), 'E', Ic2Items.elemotor, 'F',
                new ItemStack(IUItem.crafting_elements, 1, 51)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 5),
                "DBD",
                " A ",
                " C ",
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 70),
                'C',
                Ic2Items.elemotor,
                'A',
                Ic2Items.advancedMachine,
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 11)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 6),
                "D D",
                "BAE",
                " C ",

                'A',
                Ic2Items.machine,
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 70),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 11),
                'C',
                Ic2Items.elemotor, 'E',
                new ItemStack(IUItem.crafting_elements, 1, 27)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines, 1, 7),
                " H ",
                "BAE",
                "DCF",

                'A',
                Ic2Items.machine,
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 47),
                'C',
                Ic2Items.elemotor, 'E',
                new ItemStack(IUItem.crafting_elements, 1, 227), 'D',
                new ItemStack(IUItem.crafting_elements, 1, 230), 'F',
                new ItemStack(IUItem.crafting_elements, 1, 231), 'H',
                new ItemStack(IUItem.crafting_elements, 1, 11)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 10),
                "BAB",
                " C ",
                " D ", 'D',
                Ic2Items.elemotor,
                'C',
                Ic2Items.machine,
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 11),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 72)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.basemachine, 1, 12),
                " D ", "BAC", " E ",
                'A', Ic2Items.machine, 'B', new ItemStack(IUItem.crafting_elements, 1, 70),
                'C', new ItemStack(IUItem.crafting_elements, 1, 44), 'E', Ic2Items.elemotor,
                'D', new ItemStack(IUItem.crafting_elements, 1, 205)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.basemachine2, 1, 40),
                "BDB", " A ", "BEB",
                'A', new ItemStack(IUItem.basemachine, 1, 12), 'B', new ItemStack(IUItem.crafting_elements, 1, 138),
                'E', new ItemStack(IUItem.crafting_elements, 1, 20),
                'D', new ItemStack(IUItem.crafting_elements, 1, 204)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.basemachine2, 1, 41),
                "BDB", " A ", "BEB",
                'A', new ItemStack(IUItem.basemachine2, 1, 40), 'B', new ItemStack(IUItem.crafting_elements, 1, 139),
                'E', new ItemStack(IUItem.crafting_elements, 1, 96),
                'D', new ItemStack(IUItem.crafting_elements, 1, 202)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.basemachine2, 1, 42),
                "BDB", " A ", "BEB",
                'A', new ItemStack(IUItem.basemachine2, 1, 41), 'B', new ItemStack(IUItem.crafting_elements, 1, 140),
                'E', new ItemStack(IUItem.crafting_elements, 1, 120),
                'D', new ItemStack(IUItem.crafting_elements, 1, 203)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine, 1, 11),
                " E ",
                "DAB",
                " C ",
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 47),
                'C',
                Ic2Items.elemotor,
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 100),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 99),
                'A',
                Ic2Items.advancedMachine
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine, 1, 10),
                "   ",
                "AEB",
                " C ",
                'E',
                Ic2Items.advancedMachine,
                'D',
                Ic2Items.advancedCircuit,
                'C',
                Ic2Items.elemotor,
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 47),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 99)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.sunnariummaker),
                " E ",
                "BAC",
                " D ",
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 128),
                'D',
                Ic2Items.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 79),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 47),
                'A',
                Ic2Items.machine
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.sunnariumpanelmaker),
                "CAB",

                'D',
                Ic2Items.carbonPlate,
                'C',
                new ItemStack(IUItem.blockSE),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 55),
                'A',
                new ItemStack(IUItem.sunnariummaker)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.basemachine, 1, 13),
                " B ",
                "DAE",
                " C ",
                'E', new ItemStack(IUItem.crafting_elements, 1, 51),
                'D', new ItemStack(IUItem.crafting_elements, 1, 44),
                'C', Ic2Items.elemotor,
                'B', new ItemStack(IUItem.crafting_elements, 1, 28),
                'A', Ic2Items.advancedMachine
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine, 1, 15),
                "   ",
                "CBD",
                " A ",
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 29),
                'A',
                Ic2Items.elemotor,
                'B',
                Ic2Items.machine,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 11)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine, 1, 14),
                "CDC",
                "ABA",
                "CDC",
                'D',
                OreDictionary.getOres("plateNichrome"),
                'A',
                new ItemStack(IUItem.impmagnet, 1, OreDictionary.WILDCARD_VALUE),
                'B',
                new ItemStack(IUItem.basemachine, 1, 15),
                'C',
                new ItemStack(IUItem.compresscarbon)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine1, 1, 1),
                " B ",
                "DAD",
                "DCD",
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 121),
                'E',
                Ic2Items.electronicCircuit,
                'A',
                Ic2Items.advancedMachine,
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 217),
                'C',
                Ic2Items.elemotor
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine1, 1, 3),
                "   ",
                "BAD",
                " C ",
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 62),
                'C',
                Ic2Items.elemotor,
                'A',
                Ic2Items.machine,
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 47)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 44),
                "   ",
                "BAD",
                "ECF",
                'A',
                Ic2Items.machine,
                'C',
                Ic2Items.elemotor,
                'B', new ItemStack(IUItem.crafting_elements, 1, 51),
                'D', new ItemStack(IUItem.crafting_elements, 1, 44),
                'E', Items.WATER_BUCKET,
                'F', new ItemStack(IUItem.crafting_elements, 1, 243)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 43),
                " E ",
                "BAD",
                "FCG",
                'A',
                Ic2Items.machine,
                'C',
                Ic2Items.elemotor,
                'B', new ItemStack(IUItem.crafting_elements, 1, 51),
                'D', new ItemStack(IUItem.crafting_elements, 1, 44),
                'F', new ItemStack(IUItem.crafting_elements, 1, 66),
                'G', new ItemStack(IUItem.crafting_elements, 1, 67),
                'E', new ItemStack(IUItem.crafting_elements, 1, 101)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 8),
                "DED",
                "CAC",
                "CBC",
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 67),
                'D',
                OreDictionary.getOres("plateNichrome"),
                'C',
                OreDictionary.getOres("plateCaravky"),
                'A',
                IUItem.upgradeblock,
                'B',
                Ic2Items.advancedCircuit
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.upgradeblock),
                "   ",
                " A ",
                " B ",
                'D',
                OreDictionary.getOres("doubleplateVanadoalumite"),
                'B',
                (IUItem.cirsuitQuantum),
                'A',
                Ic2Items.advancedMachine,
                'C',
                IUItem.quantumtool
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 226),
                "BBB",
                "CAC",
                "DED",
                'D',
                OreDictionary.getOres("plateAlumel"),
                'E',
                (IUItem.cirsuitQuantum),
                'A',
                DEFAULT_SENSOR,
                'B',
                Ic2Items.iridiumPlate,
                'C',
                new ItemStack(IUItem.plastic_plate)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 227),
                "CCC", "BAB", "   ",
                'A', DEFAULT_SENSOR, 'B', "gearAluminium", 'C', Blocks.COBBLESTONE
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 229),
                "CCC", "BAB", "   ",
                'A', DEFAULT_SENSOR, 'B', "gearZinc", 'C', Blocks.OBSIDIAN
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 230),
                "CCC", "BAB", "   ",
                'A', DEFAULT_SENSOR, 'B', "gearManganese", 'C', Blocks.SAND
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 231),
                "CCC", "BAB", "   ",
                'A', DEFAULT_SENSOR, 'B', "gearSpinel", 'C', Blocks.GRAVEL
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 228),
                "CCC", "BAB", "   ",
                'A', DEFAULT_SENSOR, 'B', Items.LAVA_BUCKET, 'C', "plateRedbrass"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 234), " C ", "BAB", "   ", 'A', ADV_SENSOR, 'B', "plateIron", 'C',
                new ItemStack(Ic2Items.energyCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 232), " C ", "BAB", "   ", 'A', IMP_SENSOR, 'B', "plateIron", 'C',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 233), " C ", "BAB", "   ", 'A', PER_SENSOR, 'B', "plateIron", 'C',
                new ItemStack(IUItem.lapotronCrystal, 1, OreDictionary.WILDCARD_VALUE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 235), "B B", "DAD", "CCC", 'A', ADV_SENSOR, 'B', "plateAlumel", 'C',
                "plateFerromanganese"
                , 'D'
                , "plateDuralumin"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 236),
                "B B", "BAB", "B B", 'A', DEFAULT_SENSOR, 'B', ModUtils.getCellFromFluid("iufluidbenz")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 237),
                "B B", "BAB", "B B", 'A', DEFAULT_SENSOR, 'B', ModUtils.getCellFromFluid("iufluidhyd")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 238),
                "BCB", "BAB", "BCB", 'A', DEFAULT_SENSOR, 'B', Ic2Items.FluidCell, 'C', "plateVanadium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 239),
                "B B", "BAB", "B B", 'A', DEFAULT_SENSOR, 'B', ModUtils.getCellFromFluid("iufluiddizel")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 240),
                "B B", "BAB", "B B", 'A', DEFAULT_SENSOR, 'B', ModUtils.getCellFromFluid("ic2uu_matter")
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 243),
                "CDC",
                "BAB",
                "E E",
                'A',
                DEFAULT_SENSOR,
                'B',
                "plateChromium",
                'C',
                "plateCobalt",
                'D',
                "plateNichrome",
                'E',
                "plateAlcled"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 244),
                " D ", "BAC", "DDD", 'A', DEFAULT_SENSOR, 'B', new ItemStack(IUItem.anode, 1, OreDictionary.WILDCARD_VALUE), 'C',
                new ItemStack(IUItem.cathode, 1, OreDictionary.WILDCARD_VALUE), 'D',
                "plateIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 241), " B ", "BAB", " B ", 'A', DEFAULT_SENSOR, 'B', "plateDenseIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 248), " B ", "BAB", " B ", 'A', ADV_SENSOR, 'B', "plateDenseIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 245), " B ", "BAB", " B ", 'A', IMP_SENSOR, 'B', "plateDenseIron"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 242), " B ", "BAB", " B ", 'A', DEFAULT_SENSOR, 'B', "plateLead"
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.crafting_elements, 1, 249),
                " A ", "ABA", " A ", 'A', new ItemStack(IUItem.nanoBox), 'B', ADV_SENSOR
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 246),
                "CAC",
                "ABA",
                "CAC",
                'C',
                new ItemStack(IUItem.photoniy),
                'A',
                new ItemStack(IUItem.quantumtool),
                'B',
                IMP_SENSOR
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 247),
                "CAC",
                "ABA",
                "CAC",
                'C',
                new ItemStack(IUItem.photoniy_ingot),
                'A',
                new ItemStack(IUItem.advQuantumtool),
                'B',
                PER_SENSOR
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 17),
                " F ",
                "BAD",
                " C ",
                'A',
                Ic2Items.machine,
                'C',
                Ic2Items.elemotor,
                'B', new ItemStack(IUItem.crafting_elements, 1, 51),
                'D', new ItemStack(IUItem.crafting_elements, 1, 44),
                'E', new ItemStack(IUItem.crafting_elements, 1, 71),
                'F', new ItemStack(IUItem.crafting_elements, 1, 243)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 21),
                "   ",
                "BAD",
                "GCF",
                'A',
                Ic2Items.machine,
                'C',
                Ic2Items.elemotor,
                'B', new ItemStack(IUItem.crafting_elements, 1, 51),
                'D', new ItemStack(IUItem.crafting_elements, 1, 47),
                'E', new ItemStack(IUItem.crafting_elements, 1, 71),
                'F', new ItemStack(IUItem.crafting_elements, 1, 243),
                'G', new ItemStack(IUItem.crafting_elements, 1, 128)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 18),
                " E ",
                "BAD",
                "FCG",
                'A',
                Ic2Items.machine,
                'C',
                Ic2Items.elemotor,
                'B', new ItemStack(IUItem.crafting_elements, 1, 51),
                'D', new ItemStack(IUItem.crafting_elements, 1, 44),
                'F', new ItemStack(IUItem.crafting_elements, 1, 66),
                'G', new ItemStack(IUItem.crafting_elements, 1, 67),
                'E', new ItemStack(IUItem.crafting_elements, 1, 71)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine1),
                " E ",
                "BAB",
                "DCD",
                'D',
                new ItemStack(IUItem.module_stack),
                'A',
                new ItemStack(IUItem.simplemachine,1,6),
                'B',
                new ItemStack(IUItem.core, 1, 5),
                'E', new ItemStack(IUItem.crafting_elements, 1, 240),'C', new ItemStack(IUItem.crafting_elements, 1, 56)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 11),
                " C ",
                " A ",
                " B ",
                'A',
                Ic2Items.machine,
                'B',
                Ic2Items.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 112)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine1, 1, 2),
                "   ",
                "CAD",
                " B ",
                'A',
                Ic2Items.advancedMachine,
                'B',
                Ic2Items.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 226),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 158)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine1, 1, 4),
                "   ",
                "CAD",
                " B ",
                'A',
                Ic2Items.generator,
                'B',
                Ic2Items.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 27),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 239)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine1, 1, 5),
                "   ",
                "CAD",
                " B ",
                'A',
                Ic2Items.generator,
                'B',
                Ic2Items.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 27),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 236)
        );
        Recipes.advRecipes.addRecipe(
                Ic2Items.pump,
                " E ",
                "CAD",
                " B ",
                'A',
                Ic2Items.machine,
                'B',
                Ic2Items.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 27),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 154),
                'E', new ItemStack(IUItem.crafting_elements, 1, 241)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine1, 1, 6),
                "F F",
                "CAE",
                "FBF",
                'A',
                Ic2Items.pump,
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 20),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 6),

                'E', new ItemStack(IUItem.crafting_elements, 1, 248),
                'F', new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine1, 1, 7),
                "F F",
                "CAE",
                "FBF",
                'A',
                new ItemStack(IUItem.basemachine1, 1, 6),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 96),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 83),

                'E', new ItemStack(IUItem.crafting_elements, 1, 245),
                'F', new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine1, 1, 9),
                "   ",
                "CAD",
                " B ",
                'A',
                Ic2Items.generator,
                'B',
                Ic2Items.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 27),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 237)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine1, 1, 8),
                "CCC",
                " A ",
                " B ",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 35)
                ,
                'A',
                Ic2Items.machine,
                'B',
                Ic2Items.elemotor
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.oilrefiner),
                "DDD",
                "FBE",
                " C ",
                'A',
                new ItemStack(IUItem.cell_all),
                'B',
                Ic2Items.advancedMachine,
                'C',
                Ic2Items.elemotor,
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 43),
                'E', new ItemStack(IUItem.crafting_elements, 1, 98), 'F', new ItemStack(IUItem.crafting_elements, 1, 33)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.oiladvrefiner),
                " A ",
                " B ",
                " C ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 9),
                'B',
                IUItem.oilrefiner,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 20)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine1, 1, 10),
                " C ",
                "DAE",
                " B ",
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 229),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 154),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 27),
                'B',
                Ic2Items.elemotor,
                'A',
                Ic2Items.machine
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine1, 1, 11),
                " E ",
                "CAB",
                " D ",
                'D',
                Ic2Items.elemotor,
                'A',
                Ic2Items.machine,
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 47),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 27),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 65)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine1, 1, 13),
                " E ",
                "CAB",
                " D ",
                'D',
                Ic2Items.elemotor,
                'A',
                Ic2Items.machine,
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 44),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 27),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 65)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine1, 1, 12),
                "   ",
                "CAD",
                " B ",
                'A',
                Ic2Items.generator,
                'B',
                Ic2Items.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 27),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 228)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine1, 1, 14),
                "   ",
                "CAD",
                " B ",
                'A',
                Ic2Items.generator,
                'B',
                Ic2Items.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 27),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 238)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine1, 1, 15),
                " E ",
                "CAD",
                " B ",
                'A',
                Ic2Items.machine,
                'B',
                Ic2Items.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 27),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 154),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 244)
        );
        Recipes.advRecipes.addRecipe(
                Ic2Items.nuclearReactor,
                " E ", "BAC", "DDD",
                'A', Ic2Items.generator,

                'B', new ItemStack(IUItem.crafting_elements, 1, 31),
                'C', new ItemStack(IUItem.crafting_elements, 1, 38),
                'E', new ItemStack(IUItem.crafting_elements, 1, 36),
                'D', Ic2Items.reactorChamber
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine, 1, 7),
                "DED", "BAC", "D D",
                'A', Ic2Items.nuclearReactor,

                'B', new ItemStack(IUItem.crafting_elements, 1, 7),
                'C', new ItemStack(IUItem.crafting_elements, 1, 14),
                'E', new ItemStack(IUItem.crafting_elements, 1, 13),
                'D', new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine, 1, 8),
                "DED", "BAC", "D D",
                'A', new ItemStack(IUItem.basemachine, 1, 7),

                'B', new ItemStack(IUItem.crafting_elements, 1, 84),
                'C', new ItemStack(IUItem.crafting_elements, 1, 89),
                'E', new ItemStack(IUItem.crafting_elements, 1, 88),
                'D', new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine, 1, 9),
                "DED", "BAC", "D D",
                'A', new ItemStack(IUItem.basemachine, 1, 8),

                'B', new ItemStack(IUItem.crafting_elements, 1, 104),
                'C', new ItemStack(IUItem.crafting_elements, 1, 114),
                'E', new ItemStack(IUItem.crafting_elements, 1, 113),
                'D', new ItemStack(IUItem.crafting_elements, 1, 140)
        );
        Recipes.advRecipes.addShapelessRecipe(
                Ic2Items.reactorChamber, Ic2Items.machine, new ItemStack(IUItem.crafting_elements, 1, 242)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.advchamberblock), "CBC", " A ", "C C", 'A', Ic2Items.reactorChamber, 'B',
                new ItemStack(IUItem.crafting_elements, 1, 249), 'C', new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.impchamberblock), "CBC", " A ", "C C", 'A', new ItemStack(IUItem.advchamberblock), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 246), 'C', new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.perchamberblock), "CBC", " A ", "C C", 'A', new ItemStack(IUItem.impchamberblock), 'B',
                new ItemStack(IUItem.crafting_elements, 1, 247), 'C', new ItemStack(IUItem.crafting_elements, 1, 140)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines, 1, 10),
                "BAB",
                " D ",
                "B B",
                'D',
                Ic2Items.generator,
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 234),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines, 1, 11),
                "BAB",
                " D ",
                "B B",
                'D',
                new ItemStack(IUItem.machines, 1, 10),
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 232),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.machines, 1, 12),
                "BAB",
                " D ",
                "B B",
                'D',
                new ItemStack(IUItem.machines, 1, 11),
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 233),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 140)
        );
        Recipes.advRecipes.addRecipe(
                Ic2Items.geothermalGenerator,
                "   ",
                "ABC",
                "   ",
                'B',
                Ic2Items.machine,
                'A',
                new ItemStack(IUItem.crafting_elements,
                        1, 11
                ),
                'C',
                new ItemStack(IUItem.crafting_elements,
                        1, 22
                )
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine, 1, 4),
                "B B",
                "ADC",
                "B B",
                'D',
                Ic2Items.geothermalGenerator,
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 234),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 5),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine, 1, 5),
                "B B",
                "ADC",
                "B B",
                'D',
                new ItemStack(IUItem.basemachine, 1, 4),
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 232),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 82),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine, 1, 6),
                "B B",
                "ADC",
                "B B",
                'D',
                new ItemStack(IUItem.basemachine, 1, 5),
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 233),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 106),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 140)
        );

        Recipes.advRecipes.addRecipe(
                Ic2Items.solarPanel,
                "ABA", "BDB", " E ",
                'E', Ic2Items.generator, 'D', new ItemStack(IUItem.crafting_elements, 1, 37)
                , 'A', Ic2Items.coalDust, 'B', Blocks.GLASS
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 9),
                "   ",
                "CAD",
                " B ",
                'A',
                Ic2Items.machine,
                'B',
                Ic2Items.elemotor,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 101), 'D', new ItemStack(IUItem.crafting_elements, 1, 11)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.blockmolecular),
                "MXM", "ABA", "MXM", 'M', Ic2Items.advancedMachine,
                'X', new ItemStack(IUItem.tranformer, 1, 10), 'A',
                Ic2Items.advancedCircuit, 'B', new ItemStack(IUItem.crafting_elements, 1, 75)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.blockdoublemolecular),
                "BDB",
                "CAC",
                "BEB",
                'C',
                (IUItem.cirsuitQuantum),
                'B',
                Ic2Items.advancedMachine,
                'A',
                new ItemStack(IUItem.blockmolecular),
                'D',
                new ItemStack(IUItem.tranformer, 1, 1), 'E',
                new ItemStack(IUItem.crafting_elements, 1, 10)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 4),
                "   ",
                "BAB",
                "DCE",
                'A',
                Ic2Items.advancedMachine,
                'B',
                new ItemStack(IUItem.entitymodules, 1, 1),
                'C',
                Ic2Items.elemotor,
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 59),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 54)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.blockSE),
                " B ",
                " A ",
                "   ",
                'B',
                new ItemStack(IUItem.crafting_elements,1,79),
                'A',
                Ic2Items.solarPanel
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.adv_se_generator),
                "   ", "CAC", " B ",
                'B',
                new ItemStack(IUItem.crafting_elements,1,234),
                'A', new ItemStack(IUItem.crafting_elements,1,176),
                'C', new ItemStack(IUItem.blockSE)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 12),
                " A ",
                " D ",
                " B ",
                'A',
                new ItemStack(IUItem.crafting_elements,1,56),
                'B',
                new ItemStack(IUItem.crafting_elements,1,175),
                'C',
                OreDictionary.getOres("doubleplateDuralumin"),
                'E',
                IUItem.cirsuitQuantum,
                'D',
                IUItem.imp_se_generator
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.combinersolidmatter),
                "ABC",
                "DJE",
                "FGH",
                'J',
                new ItemStack(IUItem.crafting_elements, 1, 56),
                'H',
                new ItemStack(IUItem.crafting_elements, 1, 145),
                'G',
                new ItemStack(IUItem.crafting_elements, 1, 147),
                'F',
                new ItemStack(IUItem.crafting_elements, 1, 148),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 149),
                'D',
                new ItemStack(IUItem.crafting_elements, 1, 150),
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 151),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 152),
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 153)
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.imp_se_generator),
                "   ", "CAC", " B ",
                'B',
                new ItemStack(IUItem.crafting_elements,1,232),
                'A', new ItemStack(IUItem.crafting_elements,1,174),
                'C', new ItemStack(IUItem.adv_se_generator)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 145),
                " B ","BAB"," B ",'A',DEFAULT_SENSOR,
                'B',   new ItemStack(IUItem.matter, 1, 2)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 147),
                " B ","BAB"," B ",'A',DEFAULT_SENSOR,
                'B',   new ItemStack(IUItem.matter, 1, 7)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 148),
                " B ","BAB"," B ",'A',DEFAULT_SENSOR,
                'B',   new ItemStack(IUItem.matter, 1, 1)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 149),
                " B ","BAB"," B ",'A',DEFAULT_SENSOR,
                'B',   new ItemStack(IUItem.matter, 1, 4)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 150),
                " B ","BAB"," B ",'A',DEFAULT_SENSOR,
                'B',   new ItemStack(IUItem.matter, 1, 3)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 151),
                " B ","BAB"," B ",'A',DEFAULT_SENSOR,
                'B',   new ItemStack(IUItem.matter, 1, 0)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 152),
                " B ","BAB"," B ",'A',DEFAULT_SENSOR,
                'B',   new ItemStack(IUItem.matter, 1, 6)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 153),
                " B ","BAB"," B ",'A',DEFAULT_SENSOR,
                'B',   new ItemStack(IUItem.matter, 1, 5)
        );

        Recipes.advRecipes.addRecipe(
                Ic2Items.reactorVent,"CBC","BAB","CBC",'A',Ic2Items.electronicCircuit,
                'B',"plateIron",'C',Blocks.IRON_BARS
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 251),
                "BCB","BAB","BCB",'A',DEFAULT_SENSOR,
                'B',   ModUtils.getCellFromFluid("iufluidneft"),'C',Ic2Items.advancedCircuit
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.oilgetter),
                " A ",
                " B ",
                " D ",
                'D',
                Ic2Items.elemotor,
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 251),
                'B',
                Ic2Items.machine
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.convertersolidmatter, 1),
                "E F",
                "ABC",
                " D ",
                'F',
                new ItemStack(IUItem.crafting_elements, 1, 44),
                'E',
                new ItemStack(IUItem.crafting_elements, 1, 75),
                'A',
                getBlockStack(BlockBaseMachine3.replicator_iu),
                'C',
                getBlockStack(BlockBaseMachine3.scanner_iu),
                'D',
                IUItem.combinersolidmatter,
                'B',
                Ic2Items.advancedMachine
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 23),
                " C ",
                " D ",
                " A ",
                'D',
                IUItem.blockmolecular,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 10),
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 152)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 39),
                " C ",
                " D ",
                " A ",
                'D',
                IUItem.blockmolecular,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 10),
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 151)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 37),
                " C ",
                " D ",
                " A ",
                'D',
                IUItem.blockmolecular,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 10),
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 150)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 36),
                " C ",
                " D ",
                " A ",
                'D',
                IUItem.blockmolecular,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 10),
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 153)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 35),
                " C ",
                " D ",
                " A ",
                'D',
                IUItem.blockmolecular,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 10),
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 145)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 34),
                " C ",
                " D ",
                " A ",
                'D',
                IUItem.blockmolecular,
                'C',
                new ItemStack(IUItem.crafting_elements, 1, 10),
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 147)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.veinsencor, 1),
                " BC","BDB","BAB",'A',Ic2Items.electronicCircuit,
                'B',"plateIron",'C',Items.REDSTONE,'D',"plateChromium"
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 169),
                "CED","ABA","CFD",'A',IUItem.circuitNano,'B',"plateIron",'C',Items.REDSTONE,'D',new ItemStack(Items.DYE,1,4)
                ,'E',IUItem.advnanobox,'F', getBlockStack(BlockBaseMachine3.energy_controller)
        );
        Recipes.advRecipes.addShapelessRecipe(
                getBlockStack(BlockBaseMachine3.limiter), new ItemStack(IUItem.crafting_elements, 1, 169),Ic2Items.advancedMachine

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 261), " C ", "BAB", " D ", 'A', ADV_SENSOR, 'B', "plateIron", 'C',
                new ItemStack(Ic2Items.energyCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),'D',Blocks.REDSTONE_BLOCK
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 259), " C ", "BAB", " D ", 'A', IMP_SENSOR, 'B', "plateIron", 'C',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),'D',Blocks.REDSTONE_BLOCK
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 260), " C ", "BAB", " D ", 'A', PER_SENSOR, 'B', "plateIron", 'C',
                new ItemStack(IUItem.lapotronCrystal, 1, OreDictionary.WILDCARD_VALUE),'D',Blocks.REDSTONE_BLOCK
        );
        Recipes.advRecipes.addRecipe( new ItemStack(IUItem.crafting_elements, 1, 258),
                " C ",
                "BAB",
                " D ",
                'A',
                new ItemStack(IUItem.crafting_elements, 1,
                        21
                ),
                'B',
                "plateIron",
                'C',
                new ItemStack(Ic2Items.reBattery.getItem(), 1, OreDictionary.WILDCARD_VALUE),'D',Blocks.REDSTONE_BLOCK
        );

        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 71),
                "BAB",
                " D ",
                "B B",
                'D',
                new ItemStack(IUItem.basemachine2, 1, 70),
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 261),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 138)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 72),
                "BAB",
                " D ",
                "B B",
                'D',
                new ItemStack(IUItem.basemachine2, 1, 71),
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 259),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 139)
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.basemachine2, 1, 73),
                "BAB",
                " D ",
                "B B",
                'D',
                new ItemStack(IUItem.basemachine2, 1, 72),
                'A',
                new ItemStack(IUItem.crafting_elements, 1, 260),
                'B',
                new ItemStack(IUItem.crafting_elements, 1, 140)
        );
        Recipes.advRecipes.addShapelessRecipe(
                getBlockStack(BlockBaseMachine3.redstone_generator),
                new ItemStack(IUItem.crafting_elements, 1, 258),Ic2Items.machine
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 206),
                "ABA",
                "ACA",
                "ABA",
                'B',
                Ic2Items.insulatedTinCableItem,
                'A',
                OreDictionary.getOres("plankWood"),
                'C',
               DEFAULT_SENSOR
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 207),
                "   ",
                "ABA",
                "   ",
                'A',
                Ic2Items.insulatedCopperCableItem,
                'B',
                DEFAULT_SENSOR
        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 208),
                " A ",
                "DBC",
                " A ",
                'B',
               DEFAULT_SENSOR,
                'A',
                Ic2Items.insulatedGoldCableItem,
                'D',
                Ic2Items.electronicCircuit,
                'C',
                new ItemStack(Ic2Items.advBattery.getItem(), 1, OreDictionary.WILDCARD_VALUE)

        );
        Recipes.advRecipes.addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 209),
                " A ",
                "DBC",
                " A ",
                'B',
                DEFAULT_SENSOR,
                'A',
                Ic2Items.trippleInsulatedIronCableItem,
                'D',
                Ic2Items.advancedCircuit,
                'C',
                new ItemStack(Ic2Items.lapotronCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE)

        );
        for(int i = 0; i < 3;i++)
        Recipes.advRecipes.addShapelessRecipe(
                new ItemStack(IUItem.tranformer, 1, i + 8),
                new ItemStack(IUItem.tranformer, 1, i + 7),
                new ItemStack(IUItem.crafting_elements, 1, 207 + i)
        );
        Recipes.advRecipes.addShapelessRecipe(
                new ItemStack(IUItem.tranformer, 1, 7),Ic2Items.machine,new ItemStack(IUItem.crafting_elements, 1, 206));

    }

    public static ItemStack getBlockStack(ITeBlock block) {
        return TeBlockRegistry.get(block.getIdentifier()).getItemStack(block);
    }

}

