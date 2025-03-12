package com.denfop.register;

import com.denfop.IUItem;
import com.denfop.blocks.BlockSpace;
import com.denfop.blocks.BlockSpace1;
import com.denfop.blocks.BlockSpace2;
import com.denfop.blocks.BlockSpace3;
import com.denfop.items.resource.ItemRawMetals;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterOreDictionary {

    public static final List<Item> list_item = new ArrayList<>();
    public static final List<String> list_string = itemNames();
    public static final List<String> list_string1 = itemNames1();
    public static final List<String> list_heavyore = itemNames2();
    public static final List<String> list_baseore = itemNames6();
    public static final List<Item> list_item1 = new ArrayList<>();
    public static final String[] string1 = {"casing", "doubleplate", "dust", "ingot", "nugget", "plate", "block", "gear"};

    public static final String[] string = {"casing", "crushed", "doubleplate", "dust", "ingot", "nugget", "plate",
            "purifiedcrushed", "smalldust", "stick", "verysmalldust", "block", "gear"};
    public static final String[] string2 = {"heavyore", "baseore", "radiationore", "radiationresources", "preciousore", "preciousgem"};

    public static final List<String> list_baseore1 = itemNames7();
    public static final List<String> standardList = Arrays.asList("Copper","Tin","Lead","Iron","Gold","Osmium","Tantalum",
            "Cadmium");
    public static final List<String> spaceElementList = Arrays.asList("Adamantium","Bloodstone",
            "Draconid","Meteoric","Mithril","Orichalcum");
    public static final List<String> list_mineral = itemNames8();
    public static final List<Item> list_item2 = new ArrayList<>();
    public static final int[] meta_item = {29, 28, 31, 44, 28, 22, 31, 28, 32, 22, 0, 22};

    public static final String[] string3 = {"casing", "crushed", "doubleplate", "dust", "ingot", "nugget", "plate",
            "purifiedcrushed", "smalldust", "stick", "block", "gear"};

    public static void writelist() {
        list_item.add(IUItem.casing);
        list_item.add(IUItem.crushed);
        list_item.add(IUItem.doubleplate);
        list_item.add(IUItem.iudust);
        list_item.add(IUItem.iuingot);
        list_item.add(IUItem.nugget);
        list_item.add(IUItem.plate);
        list_item.add(IUItem.purifiedcrushed);
        list_item.add(IUItem.smalldust);
        list_item.add(IUItem.stik);
        list_item.add(IUItem.verysmalldust);
        list_item.add(Item.getItemFromBlock(IUItem.block));
        list_item.add(IUItem.gear);
    }

    public static void writelist2() {
        list_item2.add(IUItem.casing);
        list_item2.add(IUItem.crushed);
        list_item2.add(IUItem.doubleplate);
        list_item2.add(IUItem.iudust);
        list_item2.add(IUItem.iuingot);
        list_item2.add(IUItem.nugget);
        list_item2.add(IUItem.plate);
        list_item2.add(IUItem.purifiedcrushed);
        list_item2.add(IUItem.smalldust);
        list_item2.add(IUItem.stik);
        list_item2.add(Item.getItemFromBlock(IUItem.block2));
        list_item2.add(IUItem.gear);
    }

    public static void writelist1() {

        list_item1.add(IUItem.alloyscasing);
        list_item1.add(IUItem.alloysdoubleplate);
        list_item1.add(IUItem.alloysdust);
        list_item1.add(IUItem.alloysingot);
        list_item1.add(IUItem.alloysnugget);
        list_item1.add(IUItem.alloysplate);
        list_item1.add(Item.getItemFromBlock(IUItem.alloysblock));
        list_item1.add(IUItem.alloygear);
    }

    public static List<String> itemNames2() {
        List<String> list = new ArrayList<>();
        list.add("Magnetite");
        list.add("Calaverite");
        list.add("Galena");
        list.add("Nickelite");
        list.add("Pyrite");
        list.add("Quartzite");
        list.add("Uranite");
        list.add("Azurite");
        list.add("Rhodonite");
        list.add("Alfildit");
        list.add("Euxenite");
        list.add("Smithsonite");
        list.add("Ilmenite");
        list.add("Todorokite");
        list.add("Ferroaugite");
        list.add("Scheelite");
        return list;
    }

    public static List<String> itemNames3() {
        List<String> list = new ArrayList<>();

        list.add("Americium");
        list.add("Neptunium");
        list.add("Curium");
        list.add("Ruby");
        list.add("Topaz");
        list.add("Sapphire");
        list.add("Thorium");

        return list;
    }

    public static List<String> itemNames1() {
        List<String> list = new ArrayList<>();
        list.add("Aluminumbronze");//0
        list.add("Alumel");//1
        list.add("Redbrass");//2
        list.add("Muntsa");//3
        list.add("Nichrome");//4
        list.add("Alcled");//5
        list.add("Vanadoalumite");//6
        list.add("Vitalium");//7
        list.add("Duralumin");//8
        list.add("Ferromanganese");//9
        list.add("AluminiumSilicon");//10
        list.add("BerylliumBronze");//11
        list.add("Zeliber");//12
        list.add("StainlessSteel");//13
        list.add("Inconel");//14
        list.add("Nitenol");//15
        list.add("Stellite");//16
        list.add("HafniumBoride");//17
        list.add("Woods");//18
        list.add("Nimonic");//19
        list.add("TantalumTungstenHafnium");//20
        list.add("Permalloy");//21
        list.add("AluminiumLithium");//22
        list.add("CobaltChrome");//23
        list.add("HafniumCarbide");//24
        list.add("MolybdenumSteel");//25
        list.add("NiobiumTitanium");//26
        list.add("Osmiridium");//27
        list.add("SuperalloyHaynes");//28
        list.add("SuperalloyRene");//29
        list.add("YttriumAluminiumGarnet");//30
        list.add("GalliumArsenic");//31
        return list;
    }
    public static List<String> itemNames4() {
        List<String> list = new ArrayList<>();
        list.add("Mikhail");//0
        list.add("Aluminium");//1
        list.add("Vanadium");//2
        list.add("Tungsten");//3
        list.add("Cobalt");//6
        list.add("Magnesium");//7
        list.add("Nickel");//8
        list.add("Platinum");//9
        list.add("Titanium");//10
        list.add("Chromium");//11
        list.add("Spinel");//12
        list.add("Silver");//14
        list.add("Zinc");//15
        list.add("Manganese");//16
        list.add("Iridium");//17
        list.add("Germanium");//18
        return list;
    }
    public static List<String> itemNames() {
        List<String> list = new ArrayList<>();
        list.add("Mikhail");//0
        list.add("Aluminium");//1
        list.add("Vanadium");//2
        list.add("Tungsten");//3
        list.add("Invar");//4
        list.add("Caravky");//5
        list.add("Cobalt");//6
        list.add("Magnesium");//7
        list.add("Nickel");//8
        list.add("Platinum");//9
        list.add("Titanium");//10
        list.add("Chromium");//11
        list.add("Spinel");//12
        list.add("Electrum");//13
        list.add("Silver");//14
        list.add("Zinc");//15
        list.add("Manganese");//16
        list.add("Iridium");//17
        list.add("Germanium");//18
        return list;
    }

    public static List<String> itemNames7() {
        return Arrays.asList(
                "Arsenic",
                "Barium",
                "Bismuth",
                "Gadolinium",
                "Gallium",
                "Hafnium",
                "Yttrium",
                "Molybdenum",
                "Neodymium",
                "Niobium",
                "Palladium",
                "Polonium",
                "Strontium",
                "Thallium",
                "Zirconium"
        );
    }

    public static List<String> itemNames8() {
        return Arrays.asList(
                "ArsenoPyrite",
                "Braggite",
                "Wolframite",
                "Germanite",
                "Coltan",
                "Crocoite",
                "Xenotime",
                "Iridosmine",
                "Theophrastite",
                "Tetrahedrite",
                "Fergusonite",
                "Celestine",
                "Zircon",
                "Crystal"
        );
    }

    public static List<String> itemNames6() {
        List<String> list = new ArrayList<>();
        list.add("Mikhail");//0
        list.add("Aluminium");//1
        list.add("Vanadium");//2
        list.add("Tungsten");//3
        list.add("Cobalt");//4
        list.add("Magnesium");//5
        list.add("Nickel");//6
        list.add("Platinum");//7
        list.add("Titanium");//8
        list.add("Chromium");//9
        list.add("Spinel");//10
        list.add("Silver");//11
        list.add("Zinc");//12
        list.add("Manganese");//13
        list.add("Iridium");//14
        list.add("Germanium");//15
        return list;
    }

    public static void deleteOre(String name, ItemStack stack) {
        final NonNullList<ItemStack> list = OreDictionary.getOres(name);
        list.removeIf(stack1 -> stack1.isItemEqual(stack));
    }

    private static void add(String name, ItemStack stack) {
        OreDictionary.registerOre(name, stack);
    }

    public static void oredict() {
        writelist();
        writelist1();
        writelist2();
        OreDictionary.registerOre("oreThorium", IUItem.toriyore);
        OreDictionary.registerOre("gemThorium", new ItemStack(IUItem.toriy, 1, 0));
        OreDictionary.registerOre("ingotUranium", new ItemStack(IUItem.itemiu, 1, 2));
        OreDictionary.registerOre("oreRedstone", Blocks.REDSTONE_ORE);
        OreDictionary.registerOre("casingBronze", IUItem.casingbronze);
        OreDictionary.registerOre("casingIron", IUItem.casingiron);
        OreDictionary.registerOre("casingGold", IUItem.casinggold);
        OreDictionary.registerOre("casingCopper", IUItem.casingcopper);
        OreDictionary.registerOre("casingTin", IUItem.casingtin);
        OreDictionary.registerOre("casingSteel", IUItem.casingadviron);
        OreDictionary.registerOre("crystalProton", IUItem.proton);
        OreDictionary.registerOre("crystalPhoton", IUItem.photoniy);
        OreDictionary.registerOre("crystalingotPhoton", IUItem.photoniy_ingot);
        OreDictionary.registerOre("ingotNeutron", IUItem.neutroniumingot);
        OreDictionary.registerOre("nuggetNeutron", IUItem.neutronium);
        OreDictionary.registerOre("casingLead", IUItem.casinglead);
        OreDictionary.registerOre("woodRubber", IUItem.rubWood);
        Item testItem = Item.getItemFromBlock(IUItem.alloysblock);
        for (int j = 0; j < spaceElementList.size(); j++) {
            OreDictionary.registerOre(
                    "plate" + spaceElementList.get(j),
                    new ItemStack(IUItem.plate, 1, 46 + j)
            );
            OreDictionary.registerOre(
                    "ingot" + spaceElementList.get(j),
                    new ItemStack(IUItem.iuingot, 1, 43 + j)
            );
            OreDictionary.registerOre(
                    "doubleplate" + spaceElementList.get(j),
                    new ItemStack(IUItem.doubleplate, 1, 46 + j)
            );
            OreDictionary.registerOre(
                    "casing" + spaceElementList.get(j),
                    new ItemStack(IUItem.casing, 1, 44 + j)
            );
            OreDictionary.registerOre(
                    "block" + spaceElementList.get(j),
                    new ItemStack(IUItem.preciousblock, 1, 3 + j)
            );
        }
        for (int j = 0; j < list_item2.size(); j++) {
            for (int i = 0; i < list_baseore1.size(); i++) {
                OreDictionary.registerOre(
                        string3[j] + list_baseore1.get(i),
                        new ItemStack(list_item2.get(j), 1, meta_item[j] + i)
                );
            }
        }
        for (int j = 0; j < list_item1.size(); j++) {
            for (int i = 0; i < list_string1.size(); i++) {
                Item item = list_item1.get(j);
                if (item == testItem) {
                    if (i < 16) {
                        OreDictionary.registerOre(
                                string1[j] + list_string1.get(i),
                                new ItemStack(item, 1, i)
                        );
                    } else {
                        OreDictionary.registerOre(
                                string1[j] + list_string1.get(i),
                                new ItemStack(IUItem.alloysblock1, 1, i - 16)
                        );
                    }
                } else {
                    OreDictionary.registerOre(
                            string1[j] + list_string1.get(i),
                            new ItemStack(item, 1, i)
                    );
                }


            }
        }

        for (int i = 0; i < list_heavyore.size(); i++) {
            OreDictionary.registerOre(
                    "ore" + list_heavyore.get(i),
                    new ItemStack(Item.getItemFromBlock(IUItem.heavyore), 1, i)
            );

        }
        for (int i = 0; i < list_mineral.size(); i++) {
            OreDictionary.registerOre(
                    "ore" + list_mineral.get(i),
                    new ItemStack(Item.getItemFromBlock(IUItem.mineral), 1, i)
            );

        }
        OreDictionary.registerOre(
                "oreQuartz",
                new ItemStack(Item.getItemFromBlock(IUItem.preciousore), 1, 3)
        );
        for (int i = 0; i < ItemRawMetals.Types.values().length; i++) {
            String s = ItemRawMetals.Types.values()[i].getName();
            s = s.substring(0, 1).toUpperCase() + s.substring(1);
            OreDictionary.registerOre(
                    "raw"+s,
                    new ItemStack(IUItem.rawMetals, 1,i)
            );
            OreDictionary.registerOre(
                    "rawIngot"+s,
                    new ItemStack(IUItem.rawIngot, 1,i)
            );
        }
        for (int i = 0; i < list_baseore.size(); i++) {
            OreDictionary.registerOre(
                    "ore" + list_baseore.get(i),
                    new ItemStack(Item.getItemFromBlock((IUItem.ore)), 1,
                            i
                    )
            );

        }
        for (int i = 0; i < list_baseore1.size(); i++) {
            OreDictionary.registerOre(
                    "ore" + list_baseore1.get(i),
                    new ItemStack(Item.getItemFromBlock((IUItem.ore3)), 1,
                            i
                    )
            );

        }


        for (int j = 0; j < list_item.size(); j++) {
            for (int i = 0; i < list_string.size(); i++) {

                if (!list_item.get(j).equals(Item.getItemFromBlock(IUItem.block))) {
                    OreDictionary.registerOre(
                            string[j] + list_string.get(i),
                            new ItemStack(list_item.get(j).setUnlocalizedName(string[j]), 1, i)
                    );
                } else {
                    if (!(string[j] + list_string.get(i)).equals("oreCaravky") && !(string[j] + list_string.get(i)).equals(
                            "oreInvar") && !(string[j] + list_string.get(i)).equals("oreElectrum")) {
                        if (i < 16) {
                            OreDictionary.registerOre(
                                    string[j] + list_string.get(i),
                                    new ItemStack(
                                            list_item.get(j).setUnlocalizedName(string[j]),
                                            1,
                                            i
                                    )
                            );
                        } else {


                            OreDictionary.registerOre(
                                    string[j] + list_string.get(i),
                                    new ItemStack(
                                            Item.getItemFromBlock(IUItem.block1).setUnlocalizedName(string[j]),
                                            1,
                                            i - 16
                                    )
                            );


                        }
                    }

                }
            }
        }
        OreDictionary.registerOre(
                "oreAmericium",
                new ItemStack(Item.getItemFromBlock(IUItem.radiationore).setUnlocalizedName(string2[2]), 1, 0)
        );
        OreDictionary.registerOre(
                "oreNeptunium",
                new ItemStack(Item.getItemFromBlock(IUItem.radiationore).setUnlocalizedName(string2[2]), 1, 1)
        );
        OreDictionary.registerOre(
                "oreCurium",
                new ItemStack(Item.getItemFromBlock(IUItem.radiationore).setUnlocalizedName(string2[2]), 1, 2)
        );

        OreDictionary.registerOre("ingotAluminum", new ItemStack(IUItem.iuingot, 1, 1));

        OreDictionary.registerOre("gemAmericium", new ItemStack(IUItem.radiationresources.setUnlocalizedName(string2[3]), 1, 0));
        OreDictionary.registerOre("gemNeptunium", new ItemStack(IUItem.radiationresources.setUnlocalizedName(string2[3]), 1, 1));
        OreDictionary.registerOre("gemCurium", new ItemStack(IUItem.radiationresources.setUnlocalizedName(string2[3]), 1, 2));

        OreDictionary.registerOre(
                "oreRuby",
                new ItemStack(Item.getItemFromBlock(IUItem.preciousore).setUnlocalizedName(string2[4]), 1, 0)
        );
        OreDictionary.registerOre(
                "oreSapphire",
                new ItemStack(Item.getItemFromBlock(IUItem.preciousore).setUnlocalizedName(string2[4]), 1, 1)
        );
        OreDictionary.registerOre(
                "oreTopaz",
                new ItemStack(Item.getItemFromBlock(IUItem.preciousore).setUnlocalizedName(string2[4]), 1, 2)
        );
        OreDictionary.registerOre("gemRuby", new ItemStack(IUItem.preciousgem.setUnlocalizedName(string2[5]), 1, 0));
        OreDictionary.registerOre("gemSapphire", new ItemStack(IUItem.preciousgem.setUnlocalizedName(string2[5]), 1, 1));
        OreDictionary.registerOre("gemTopaz", new ItemStack(IUItem.preciousgem.setUnlocalizedName(string2[5]), 1, 2));

        add("oreSulfur", new ItemStack(IUItem.ore3, 1, 15));
        add("oreSulfur", new ItemStack(IUItem.basalts, 1, 6));
        add("casingCopper", IUItem.casingcopper);
        add("casingIron", IUItem.casingiron);
        add("casingTin", IUItem.casingtin);
        add("casingLead", IUItem.casinglead);
        add("casingBronze", IUItem.casingbronze);
        add("oreCopper", IUItem.copperOre);
        add("oreLead", IUItem.leadOre);
        add("oreTin", IUItem.tinOre);
        add("oreUranium", IUItem.uraniumOre);
        add("treeLeaves", new ItemStack(IUItem.leaves));
        add("treeSapling", new ItemStack(IUItem.rubberSapling));
        add("itemRubber", IUItem.rubber);
        add("materialRubber", IUItem.rubber);
        add("materialResin", IUItem.latex);
        add("itemResin", IUItem.latex);
        add("dustStone", IUItem.stoneDust);
        add("dustBronze", IUItem.bronzeDust);
        add("dustClay", IUItem.clayDust);
        add("dustCoal", IUItem.coalDust);
        add("dustCopper", IUItem.copperDust);
        add("dustGold", IUItem.goldDust);
        add("dustIron", IUItem.ironDust);
        add("dustTin", IUItem.tinDust);
        add("dustLead", IUItem.leadDust);
        add("dustObsidian", IUItem.obsidianDust);
        add("dustLapis", IUItem.lapiDust);
        add("dustSulfur", IUItem.sulfurDust);
        add("dustDiamond", IUItem.diamondDust);
        add("dustTinyCopper", IUItem.smallCopperDust);
        add("dustTinyGold", IUItem.smallGoldDust);
        add("dustTinyIron", IUItem.smallIronDust);
        add("dustTinyTin", IUItem.smallTinDust);
        add("dustTinyLead", IUItem.smallLeadDust);
        add("dustTinySulfur", IUItem.smallSulfurDust);
        add("ingotBronze", IUItem.bronzeIngot);
        add("ingotCopper", IUItem.copperIngot);
        add("ingotSteel", IUItem.advIronIngot);
        add("ingotLead", IUItem.leadIngot);
        add("ingotTin", IUItem.tinIngot);
        add("plateIron", IUItem.plateiron);
        add("plateGold", IUItem.plategold);
        add("plateCopper", IUItem.platecopper);
        add("plateTin", IUItem.platetin);
        add("plateLead", IUItem.platelead);
        add("plateLapis", IUItem.platelapis);
        add("plateObsidian", IUItem.plateobsidian);
        add("plateBronze", IUItem.platebronze);
        add("plateSteel", IUItem.plateadviron);
        add("plateDenseSteel", IUItem.denseplateadviron);
        add("plateDenseIron", IUItem.denseplateiron);
        add("plateDenseGold", IUItem.denseplategold);
        add("plateDenseCopper", IUItem.denseplatecopper);
        add("plateDenseTin", IUItem.denseplatetin);
        add("plateDenseLead", IUItem.denseplatelead);
        add("plateDenseLapis", IUItem.denseplatelapi);
        add("plateDenseObsidian", IUItem.denseplateobsidian);
        add("plateDenseBronze", IUItem.denseplatebronze);
        add("crushedIron", IUItem.crushedIronOre);
        add("crushedGold", IUItem.crushedGoldOre);
        add("crushedLead", IUItem.crushedLeadOre);
        add("crushedCopper", IUItem.crushedCopperOre);
        add("crushedTin", IUItem.crushedTinOre);
        add("crushedUranium", IUItem.crushedUraniumOre);
        add("crushedPurifiedIron", IUItem.purifiedCrushedIronOre);
        add("crushedPurifiedGold", IUItem.purifiedCrushedGoldOre);
        add("crushedPurifiedLead", IUItem.purifiedCrushedLeadOre);
        add("crushedPurifiedCopper", IUItem.purifiedCrushedCopperOre);
        add("crushedPurifiedTin", IUItem.purifiedCrushedTinOre);
        add("crushedPurifiedUranium", IUItem.purifiedCrushedUraniumOre);
        add("blockBronze", IUItem.bronzeBlock);
        add("blockCopper", IUItem.copperBlock);
        add("blockTin", IUItem.tinBlock);
        add("blockUranium", IUItem.uraniumBlock);
        add("blockLead", IUItem.leadBlock);
        add("blockSteel", IUItem.advironblock);
        add("circuitBasic", IUItem.electronicCircuit);
        add("circuitAdvanced", IUItem.advancedCircuit);
        add("craftingToolForgeHammer", new ItemStack(IUItem.ForgeHammer, 1, OreDictionary.WILDCARD_VALUE));
        add("craftingToolWireCutter", new ItemStack(IUItem.cutter, 1, OreDictionary.WILDCARD_VALUE));
        add("plateCarbon", IUItem.carbonPlate);
        add("plateAdvancedAlloy", IUItem.advancedAlloy);
        add("plateadvancedAlloy", IUItem.advancedAlloy);
        add("itemScrap", IUItem.scrap);
        add("materialScrap", IUItem.scrap);
        add("itemScrapBox", IUItem.scrapBox);
        add("itemCarbonFibre", IUItem.carbonFiber);
        add("itemCarbonFiber", IUItem.carbonFiber);
        add("itemCarbonMesh", IUItem.carbonMesh);
        add("itemRawCarbonMesh", IUItem.carbonMesh);
        add("machineBlock", IUItem.machine);
        add("machineBlockCasing", IUItem.machine);
        add("machineBlockAdvanced", IUItem.advancedMachine);
        add("machineBlockAdvancedCasing", IUItem.advancedMachine);
        add("dustSiliconDioxide", IUItem.silicondioxideDust);
        add("ingotLithium", new ItemStack(IUItem.crafting_elements, 1, 447));
        add("gemBor", new ItemStack(IUItem.crafting_elements, 1, 448));
        add("gemBeryllium", new ItemStack(IUItem.crafting_elements, 1, 449));
        add("plateLithium", new ItemStack(IUItem.crafting_elements, 1, 450));
        add("plateBor", new ItemStack(IUItem.crafting_elements, 1, 451));
        add("plateBeryllium", new ItemStack(IUItem.crafting_elements, 1, 452));

        add("gearOsmium", new ItemStack(IUItem.gear, 1, 19));
        add("gearTantalum", new ItemStack(IUItem.gear, 1, 20));
        add("gearCadmium", new ItemStack(IUItem.gear, 1, 21));
        add("ingotOsmium", new ItemStack(IUItem.iuingot, 1, 25));
        add("ingotTantalum", new ItemStack(IUItem.iuingot, 1, 26));
        add("ingotCadmium", new ItemStack(IUItem.iuingot, 1, 27));
        add("dustOsmium", new ItemStack(IUItem.iudust, 1, 34));
        add("dustTantalum", new ItemStack(IUItem.iudust, 1, 35));
        add("dustCadmium", new ItemStack(IUItem.iudust, 1, 36));

        add("casingOsmium", new ItemStack(IUItem.casing, 1, 26));
        add("casingTantalum", new ItemStack(IUItem.casing, 1, 27));
        add("casingCadmium", new ItemStack(IUItem.casing, 1, 28));

        add("crushedOsmium", new ItemStack(IUItem.crushed, 1, 25));
        add("crushedTantalum", new ItemStack(IUItem.crushed, 1, 26));
        add("crushedCadmium", new ItemStack(IUItem.crushed, 1, 27));

        add("nuggetOsmium", new ItemStack(IUItem.nugget, 1, 19));
        add("nuggetTantalum", new ItemStack(IUItem.nugget, 1, 20));
        add("nuggetCadmium", new ItemStack(IUItem.nugget, 1, 21));

        add("plateOsmium", new ItemStack(IUItem.plate, 1, 28));
        add("plateTantalum", new ItemStack(IUItem.plate, 1, 29));
        add("plateCadmium", new ItemStack(IUItem.plate, 1, 30));

        add("crushedPurifiedOsmium", new ItemStack(IUItem.purifiedcrushed, 1, 25));
        add("crushedPurifiedTantalum", new ItemStack(IUItem.purifiedcrushed, 1, 26));
        add("crushedPurifiedCadmium", new ItemStack(IUItem.purifiedcrushed, 1, 27));

        add("stickOsmium", new ItemStack(IUItem.stik, 1, 19));
        add("stickTantalum", new ItemStack(IUItem.stik, 1, 20));
        add("stickCadmium", new ItemStack(IUItem.stik, 1, 21));

        add("doubleplateOsmium", new ItemStack(IUItem.doubleplate, 1, 28));
        add("doubleplateTantalum", new ItemStack(IUItem.doubleplate, 1, 29));
        add("doubleplateCadmium", new ItemStack(IUItem.doubleplate, 1, 30));

        add("blockOsmium", new ItemStack(IUItem.block1, 1, 3));
        add("blockTantalum", new ItemStack(IUItem.block1, 1, 4));
        add("blockCadmium", new ItemStack(IUItem.block1, 1, 5));
        add("oreOsmium", new ItemStack(IUItem.ore2, 1, 3));
        add("oreTantalum", new ItemStack(IUItem.ore2, 1, 4));
        add("oreCadmium", new ItemStack(IUItem.ore2, 1, 5));

        add("smalldustOsmium", new ItemStack(IUItem.smalldust, 1, 29));
        add("smalldustTantalum", new ItemStack(IUItem.smalldust, 1, 30));
        add("smalldustCadmium", new ItemStack(IUItem.smalldust, 1, 31));
        add("dustQuartz", new ItemStack(IUItem.iudust, 1, 76));
        add("dustNetherQuartz", new ItemStack(IUItem.iudust, 1, 76));

        add("verysmalldustOsmium", new ItemStack(IUItem.verysmalldust, 1, 19));
        add("verysmalldustTantalum", new ItemStack(IUItem.verysmalldust, 1, 20));
        add("verysmalldustCadmium", new ItemStack(IUItem.verysmalldust, 1, 21));
        add("gemCoal", new ItemStack(Items.COAL));
        for (int i = 0; i < BlockSpace.Type.values().length; i++) {
            BlockSpace.Type type = BlockSpace.Type.values()[i];
            String name = type.getName().split("_")[1];
            name = name.substring(0, 1).toUpperCase() + name
                    .substring(1)
                    .toLowerCase();
            OreDictionary.registerOre(
                    "ore" + name,
                    new ItemStack(Item.getItemFromBlock((IUItem.space_ore)), 1,
                            i
                    )
            );
            type.setRaw(name);

        }
        for (int i = 0; i < BlockSpace1.Type.values().length; i++) {
            BlockSpace1.Type type = BlockSpace1.Type.values()[i];
            String name = type.getName().split("_")[1];
            name = name.substring(0, 1).toUpperCase() + name
                    .substring(1)
                    .toLowerCase();
            OreDictionary.registerOre(
                    "ore" + name,
                    new ItemStack(Item.getItemFromBlock((IUItem.space_ore1)), 1,
                            i
                    )
            );
            type.setRaw(name);

        }
        for (int i = 0; i < BlockSpace2.Type.values().length; i++) {
            BlockSpace2.Type type = BlockSpace2.Type.values()[i];
            String name = type.getName().split("_")[1];
            name = name.substring(0, 1).toUpperCase() + name
                    .substring(1)
                    .toLowerCase();
            type.setRaw(name);
            OreDictionary.registerOre(
                    "ore" + name,
                    new ItemStack(Item.getItemFromBlock((IUItem.space_ore2)), 1,
                            i
                    )
            );

        }
        for (int i = 0; i < BlockSpace3.Type.values().length; i++) {
            BlockSpace3.Type type = BlockSpace3.Type.values()[i];
            String name = type.getName().split("_")[1];
            name = name.substring(0, 1).toUpperCase() + name
                    .substring(1)
                    .toLowerCase();
            type.setRaw(name);
            OreDictionary.registerOre(
                    "ore" + name,
                    new ItemStack(Item.getItemFromBlock((IUItem.space_ore3)), 1,
                            i
                    )
            );

        }
    }

}
