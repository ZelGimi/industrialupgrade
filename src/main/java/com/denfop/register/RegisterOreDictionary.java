package com.denfop.register;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterOreDictionary {
    public static final List<String> list_baseore1 = itemNames7();
    public static final List<String> list_mineral = itemNames8();
    public static final List<String> list_string = itemNames();
    public static final List<String> list_string1 = itemNames1();
    public static final List<String> list_heavyore = itemNames2();
    public static final List<String> list_baseore = itemNames6();
    public static final List<String> spaceElementList = Arrays.asList("Adamantium", "Bloodstone",
            "Draconid", "Meteoric", "Mithril", "Orichalcum"
    );
    public static final List<String> standardList = Arrays.asList("Copper", "Tin", "Lead", "Iron", "Gold", "Osmium", "Tantalum",
            "Cadmium"
    );
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
        list.add("Vanady");//2
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
        list.add("Vanady");//2
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


}
