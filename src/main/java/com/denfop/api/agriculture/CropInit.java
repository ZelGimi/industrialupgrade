package com.denfop.api.agriculture;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.agriculture.genetics.GeneticTraits;
import com.denfop.api.agriculture.genetics.GeneticsManager;
import com.denfop.api.agriculture.genetics.Genome;
import com.denfop.api.bee.BeeInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.Arrays;
import java.util.Collections;

import static com.denfop.api.agriculture.genetics.Genome.geneticBiomes;
import static com.denfop.api.bee.BeeInit.*;

public class CropInit {

    public static CropBase wheat_seed;
    public static CropBase reed_seed;
    public static CropBase rubber_reed_seed;
    public static CropBase weed_seed;
    public static CropBase red_mushroom;
    public static CropBase tulip_pink;
    public static CropBase poppy;
    public static CropBase haustonia_gray;
    public static CropBase onion;
    public static CropBase brown_mushroom;
    public static CropBase tulip_orange;
    public static CropBase chamomile;
    public static CropBase tulip_white;
    public static CropBase dandelion;
    public static CropBase tulip_red;
    public static CropBase blue_orchid;
    public static CropBase corn;
    public static CropBase beet;
    public static CropBase carrot;
    public static CropBase potato;
    public static CropBase raspberry;
    public static CropBase hops;
    public static CropBase tomato;
    public static CropBase melon;
    public static CropBase pumpkin;
    public static CropBase mikhailovskaya_lavra;
    public static CropBase aluminum_plutonka;
    public static CropBase vanadium_dawn;
    public static CropBase tungsten_moon_flower;
    public static CropBase invar_crystal;
    public static CropBase karav_green;
    public static CropBase cobalt_grapefruit;
    public static CropBase magnesium_diamond;
    public static CropBase nickel_firework;
    public static CropBase platinum_turner;
    public static CropBase titanium_fantasy;
    public static CropBase chromium_gentleness;
    public static CropBase spinel_zenith;
    public static CropBase electrum_symphony;
    public static CropBase silver_melody;
    public static CropBase zinc_storm;
    public static CropBase manganese_lotus;
    public static CropBase iridium_snowflake;
    public static CropBase germanite_wonders;
    public static CropBase copper_heart;
    public static CropBase gold_astral;
    public static CropBase iron_crimson;
    public static CropBase diamond_island;
    public static CropBase emerald_heart;
    public static CropBase quartz_storm;
    public static CropBase uranium_fairy;
    public static CropBase lead_stream;
    public static CropBase tin_ghost;
    public static CropBase arsenite_flower;
    public static CropBase barium_star;
    public static CropBase bismuth_garden;
    public static CropBase gadolinium_lotus;
    public static CropBase gallium_song;
    public static CropBase hafnium_leaf;
    public static CropBase ytterbium_bright;
    public static CropBase molybdenum_whirlwind;
    public static CropBase neodymium_glow;
    public static CropBase niobium_obelisk;
    public static CropBase palladium_gentleness;
    public static CropBase polonium_crystal;
    public static CropBase strontium_storm;
    public static CropBase thallium_sunlight;
    public static CropBase zirconium_dragon;
    public static CropBase osmium_silk;
    public static CropBase tantalum_moon;
    public static CropBase cadmium_gentleness;
    public static CropBase ender_lily;
    public static CropBase blaze_storm;
    public static CropBase red_fury;
    public static CropBase silicon_avalanche;
    public static CropBase ghast_dew;
    public static CropBase bone_leaf;
    public static CropBase curie_berry;
    public static CropBase neptunium_wisdom;
    public static CropBase americium_moss;
    public static CropBase thorium_fist;
    public static CropBase nether_wart;
    public static CropBase terra_wart;

    public static void init(){
        CropNetwork.init();
        wheat_seed = new CropBase("wheat",0,EnumSoil.FARMLAND,1,0,4,1,90,8,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/wheat_stage"),false, Collections.emptyList(),2,false,4800,90,
                true,false, Arrays.asList(new ItemStack(Items.WHEAT)),10,Collections.emptyList(),0
        );
        reed_seed = new CropBase("reed",1,EnumSoil.SAND,1,1,1,1,60,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/stickreed"),false, Collections.emptyList(),5,false,5000,80,
                true,true, Arrays.asList(new ItemStack(Items.SUGAR_CANE)),10,Collections.emptyList(),1
        );

       weed_seed = new CropBase("weed_seed",3,EnumSoil.SAND,1,2,0,1,0,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/weed_seed"),false, Collections.emptyList(),0,
                false,400,0,
                true,true, Collections.emptyList(),0,Collections.emptyList(),0
        );
        tulip_pink = new CropBase("tulip_pink",4,EnumSoil.FARMLAND,1,2,5,1,60,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/tulip_pink"),false, Collections.emptyList(),5,
                false,3000,60,
                true,false, Arrays.asList(new ItemStack(Blocks.PINK_TULIP)),10,Collections.emptyList(),1
        );
        haustonia_gray = new CropBase("haustonia_gray",5,EnumSoil.FARMLAND,1,2,5,1,60,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/haustonia_gray"),false, Collections.emptyList(),5,
                false,3000,60,
                true,false,Arrays.asList(new ItemStack(Blocks.AZURE_BLUET)),10,Collections.emptyList(),1
        );
        poppy= new CropBase("poppy",6,EnumSoil.FARMLAND,1,2,5,1,60,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/poppy"),false, Collections.emptyList(),5,
                false,3000,60,
                true,false,Arrays.asList(new ItemStack(Blocks.POPPY,1)),10,Collections.emptyList(),1
        );
        onion= new CropBase("onion",7,EnumSoil.FARMLAND,1,2,5,1,60,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/onion"),false, Collections.emptyList(),5,
                false,3000,60,
                true,false, Arrays.asList(new ItemStack(Blocks.ALLIUM)),10,Collections.emptyList(),1
        );
        brown_mushroom= new CropBase("brown_mushroom",8,EnumSoil.MYCELIUM,1,2,5,1,40,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/brown_mushroom"),false, Collections.emptyList(),4,
                false,7000,90,
                true,false, Arrays.asList(new ItemStack(Blocks.BROWN_MUSHROOM)),0,Collections.emptyList(),1
        );
        tulip_orange= new CropBase("tulip_orange",9,EnumSoil.FARMLAND,1,2,5,1,60,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/tulip_orange"),false, Collections.emptyList(),5,
                false,3000,60,
                true,false, Arrays.asList(new ItemStack(Blocks.ORANGE_TULIP)),10,Collections.emptyList(),1
        );

        chamomile= new CropBase("chamomile",10,EnumSoil.FARMLAND,1,2,5,1,60,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/chamomile"),false, Collections.emptyList(),5,
                false,3000,60,
                true,false, Arrays.asList(new ItemStack(Blocks.OXEYE_DAISY)),10,Collections.emptyList(),1
        );
        tulip_white= new CropBase("tulip_white",11,EnumSoil.FARMLAND,1,2,5,1,60,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/tulip_white"),false, Collections.emptyList(),5,
                false,3000,60,
                true,false,Arrays.asList(new ItemStack(Blocks.WHITE_TULIP)),10,Collections.emptyList(),1
        );
        dandelion= new CropBase("dandelion",12,EnumSoil.FARMLAND,1,2,5,1,60,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/dandelion"),false, Collections.emptyList(),5,
                false,3000,60,
                true,false,Arrays.asList(new ItemStack(Blocks.DANDELION)),10,Collections.emptyList(),1
        );
        tulip_red= new CropBase("tulip_red",13,EnumSoil.FARMLAND,1,2,5,1,60,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/tulip_red"),false, Collections.emptyList(),5,
                false,3000,60,
                true,false,Arrays.asList(new ItemStack(Blocks.RED_TULIP)),10,Collections.emptyList(),1
        );
        red_mushroom= new CropBase("red_mushroom",14,EnumSoil.MYCELIUM,1,2,0,1,40,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/red_mushroom"),false, Collections.emptyList(),4,
                false,7000,90,
                true,false, Arrays.asList(new ItemStack(Blocks.RED_MUSHROOM)),0,Collections.emptyList(),1
        );
        blue_orchid= new CropBase("blue_orchid",15,EnumSoil.FARMLAND,1,2,5,1,60,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/blue_orchid"),false, Collections.emptyList(),5,
                false,3000,60,
                true,false,Arrays.asList(new ItemStack(Blocks.BLUE_ORCHID)),10,Collections.emptyList(),1
        );
        corn= new CropBase("corn",16,EnumSoil.FARMLAND,2,1,6,1,75,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/corn"),true,  Arrays.asList(dandelion,brown_mushroom),5,
                false,5000,100,
                true,false, Arrays.asList(new ItemStack(IUItem.corn.getItem())),5, Arrays.asList(),3
        );
        rubber_reed_seed = new CropBase("rubber_reed",2,EnumSoil.SAND,1,1,1,1,60,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/stickreed_rubber"),true, Arrays.asList(reed_seed,corn),3,
                false,5000,80,
                true,true, Arrays.asList(new ItemStack(IUItem.rawLatex.getItem())),10, Arrays.asList(),1
        );
        beet= new CropBase("beet",17,EnumSoil.FARMLAND,1,1,4,1,70,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/beetroots_stage"),false, Collections.emptyList(),5,
                false,5000,70,
                true,false, Arrays.asList(new ItemStack(Items.BEETROOT)),5,Collections.emptyList(),0
        );
        carrot= new CropBase("carrot",18,EnumSoil.FARMLAND,1,0,4,1,60,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/carrots_stage"),false, Collections.emptyList(),5,
                false,4000,60,
                true,false, Arrays.asList(new ItemStack(Items.CARROT)),5,Collections.emptyList(),0
        );
        potato= new CropBase("potato",19,EnumSoil.FARMLAND,1,0,4,1,60,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/potatoes_stage"),false, Collections.emptyList(),5,
                false,4000,60,
                true,false, Arrays.asList(new ItemStack(Items.POTATO)),5,Collections.emptyList(),0
        );
        tomato = new CropBase("tomato",22,EnumSoil.FARMLAND,1,0,4,1,60,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/tomato"),true,  Arrays.asList(potato,carrot,red_mushroom),4,
                false,4000,60,
                true,false,Arrays.asList(new ItemStack(IUItem.tomato.getItem())),5,Collections.emptyList(),0
        );
        raspberry= new CropBase("raspberry",20,EnumSoil.FARMLAND,2,0,4,1,40,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/raspberry"),true,  Arrays.asList(tulip_red,tomato),3,
                false,4000,50,
                true,false, Arrays.asList(new ItemStack(IUItem.raspberry.getItem())),5, Arrays.asList(),0
        );
        hops= new CropBase("hops",21,EnumSoil.FARMLAND,1,1,4,1,20,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/hops"),true, Arrays.asList(wheat_seed,tomato,corn),1,
                false,8000,70,
                true,false,  Arrays.asList(new ItemStack(IUItem.hops.getItem())),3,Arrays.asList(),3
        );

        melon = new CropBase("melon",23,EnumSoil.FARMLAND,1,0,4,1,60,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/melon"),false,  Arrays.asList(),4,
                false,5000,60,
                true,false, Arrays.asList(new ItemStack(Items.MELON)),5,Collections.emptyList(),1
        );
        pumpkin = new CropBase("pumpkin",24,EnumSoil.FARMLAND,1,0,4,1,60,4,1,
                new ResourceLocation(Constants.MOD_ID,"blocks/crop/pumpkin"),false,  Arrays.asList(),4,
                false,5000,60,
                true,false, Arrays.asList(new ItemStack(Blocks.PUMPKIN)),5,Collections.emptyList(),1
        );
        copper_heart = new CropBase("copper_heart", 44, EnumSoil.COPPER, 1, 0, 1, 1, 50, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/copper_heart"), true,Arrays.asList(potato
                ,tulip_orange), 4,
                false, 5000, 40, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(20))), 10, Collections.emptyList(),
                1);
        iron_crimson = new CropBase("iron_crimson", 46, EnumSoil.IRON, 1, 0, 1, 1, 70, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/iron_crimson"), true, Arrays.asList(tulip_white
                ,wheat_seed), 5,
                false, 5000, 60, true, false,Arrays.asList(new ItemStack(IUItem.smalldust.getStack(22))), 10, Arrays.asList(), 1);
        gold_astral = new CropBase("gold_astral", 45, EnumSoil.GOLD, 1, 0, 1, 1, 60, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/gold_astral"), true,Arrays.asList(iron_crimson
                ,copper_heart,dandelion), 2,
                false, 4500, 50, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(21))), 10, Arrays.asList(),
                1);

        diamond_island = new CropBase("diamond_island", 47, EnumSoil.DIAMOND, 1, 0, 1, 1, 65, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/diamond_island"), true, Arrays.asList(gold_astral,blue_orchid,carrot), 4,
                false, 4800, 50, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(28))), 10,
                Arrays.asList(),
                1);

        emerald_heart = new CropBase("emerald_heart", 48, EnumSoil.EMERALD, 1, 0, 1, 1, 55, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/emerald_heart"), true, Arrays.asList(diamond_island,melon), 3,
                false, 4000, 45, true, false,  Arrays.asList(new ItemStack(IUItem.smalldust.getStack(47))), 10, Collections.emptyList(),
                1);

        quartz_storm = new CropBase("quartz_storm", 49, EnumSoil.QUARTZ, 1, 0, 1, 1, 50, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/quartz_storm"), true,Arrays.asList(iron_crimson
                ,haustonia_gray), 4,
                false, 5000, 40, true, false,  Arrays.asList(new ItemStack(IUItem.smalldust.getStack(48))), 10, Collections.emptyList()
                , 1);

        lead_stream = new CropBase("lead_stream", 51, EnumSoil.LEAD, 1, 0, 1, 1, 70, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/lead_stream"), true,Arrays.asList(haustonia_gray,
                brown_mushroom), 5,
                false, 5000, 60, true, false,Arrays.asList(new ItemStack(IUItem.smalldust.getStack(24))), 10, Collections.emptyList(), 1);

        tin_ghost = new CropBase("tin_ghost", 52, EnumSoil.TIN, 1, 0, 1, 1, 65, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/tin_ghost"), true, Arrays.asList(iron_crimson,
                quartz_storm), 4,
                false, 4800, 50, true, false,Arrays.asList(new ItemStack(IUItem.smalldust.getStack(27))), 10, Collections.emptyList(), 1);

        red_fury = new CropBase("red_fury", 73, EnumSoil.REDSTONE, 1, 0, 1, 1, 55, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/red_fury"), true, Arrays.asList(poppy,red_mushroom,tulip_red), 3,
                false, 4000, 40, true, false,Arrays.asList(new ItemStack(Items.REDSTONE)), 10, Collections.emptyList(), 1);
        silicon_avalanche = new CropBase("silicon_avalanche", 74, EnumSoil.GRAVEL, 1, 0, 1, 1, 50, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/silicon_avalanche"), true,Arrays.asList(tin_ghost,iron_crimson), 4,
                false, 5000, 40, true, false,Arrays.asList(new ItemStack(Items.FLINT)), 10, Collections.emptyList(), 1);

        mikhailovskaya_lavra = new CropBase("mikhailovskaya_lavra", 25, EnumSoil.MICHALOV, 1, 0, 1, 1, 50, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/mikhailovskaya_lavra"), false, Arrays.asList(lead_stream,
                blue_orchid), 3,
                false, 6000, 40, true, false,Arrays.asList(new ItemStack(IUItem.smalldust.getStack(0))), 3, Collections.emptyList(), 1);

        aluminum_plutonka = new CropBase("aluminum_plutonka", 26, EnumSoil.ALUMINUM, 1, 0, 1, 1, 60, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/aluminum_plutonka"), false,Arrays.asList(tulip_pink,
                red_fury,iron_crimson), 4,
                false, 5000, 50, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(1))), 10, Collections.emptyList(), 1);


        tungsten_moon_flower = new CropBase("tungsten_moon_flower", 28, EnumSoil.TUNGSTEN, 1, 0, 1, 1, 65, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/tungsten_moon_flower"), false,Arrays.asList(iron_crimson,
                tin_ghost,onion), 3,
                false, 5000, 50, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(3))), 10, Collections.emptyList(), 1);
        silver_melody = new CropBase("silver_melody", 39, EnumSoil.SILVER, 1, 0, 1, 1, 50, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/silver_melody"), false, Arrays.asList(iron_crimson,
                tin_ghost,chamomile), 3,
                false, 4000, 40, true, false,Arrays.asList(new ItemStack(IUItem.smalldust.getStack(14))), 10, Collections.emptyList(), 1);
        nickel_firework = new CropBase("nickel_firework", 33, EnumSoil.NICKEL, 1, 0, 1, 1, 65, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/nickel_firework"), false, Arrays.asList(lead_stream,
                tin_ghost), 4,
                false, 4800, 50, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(8))), 10, Collections.emptyList(), 1);

        invar_crystal = new CropBase("invar_crystal", 29, EnumSoil.IRON, 1, 0, 1, 1, 55, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/invar_crystal"), false, Arrays.asList(nickel_firework,
                iron_crimson), 4,
                false, 4800, 45, true, false,Arrays.asList(new ItemStack(IUItem.smalldust.getStack(4))), 10, Collections.emptyList(), 1);

        cobalt_grapefruit = new CropBase("cobalt_grapefruit", 31, EnumSoil.COBALT, 1, 0, 1, 1, 60, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/cobalt_grapefruit"), false, Arrays.asList(mikhailovskaya_lavra,
                gold_astral), 2,
                false, 4500, 50, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(6))), 10, Collections.emptyList(), 1);
        magnesium_diamond = new CropBase("magnesium_diamond", 32, EnumSoil.MAGNESIUM, 1, 0, 1, 1, 70, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/magnesium_diamond"), false, Arrays.asList(diamond_island,
                tulip_pink,beet), 5,
                false, 5000, 60, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(7))), 10, Collections.emptyList(), 1);

        platinum_turner = new CropBase("platinum_turner", 34, EnumSoil.PLATINUM, 1, 0, 1, 1, 55, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/platinum_turner"), false, Arrays.asList(cobalt_grapefruit,
                magnesium_diamond), 3,
                false, 4000, 45, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(9))), 10, Collections.emptyList(), 1);
        spinel_zenith = new CropBase("spinel_zenith", 37, EnumSoil.SPINEL, 1, 0, 1, 1, 65, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/spinel_zenith"), false,  Arrays.asList(diamond_island,
                reed_seed,corn), 3,
                false, 5000, 50, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(12))), 10, Collections.emptyList(),
                1);
        iridium_snowflake = new CropBase("iridium_snowflake", 42, EnumSoil.IRIDIUM, 1, 0, 1, 1, 65, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/iridium_snowflake"), false, Arrays.asList(platinum_turner,
                spinel_zenith), 4,
                false, 4800, 50, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(17))), 10, Collections.emptyList(),
                1);
        karav_green = new CropBase("karav_green", 30, EnumSoil.SPINEL, 1, 0, 1, 1, 50, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/karav_green"), false, Arrays.asList(spinel_zenith,
                raspberry,cobalt_grapefruit), 3,
                false, 4000, 40, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(5))), 10, Collections.emptyList(), 1);



        titanium_fantasy = new CropBase("titanium_fantasy", 35, EnumSoil.TITANIUM, 1, 0, 1, 1, 50, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/titanium_fantasy"), false, Arrays.asList(tungsten_moon_flower,
                invar_crystal), 4,
                false, 5000, 40, true, false,Arrays.asList(new ItemStack(IUItem.smalldust.getStack(10))), 10, Collections.emptyList(), 1);

        chromium_gentleness = new CropBase("chromium_gentleness", 36, EnumSoil.CHROMIUM, 1, 0, 1, 1, 70, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/chromium_gentleness"), false, Arrays.asList(reed_seed,
                quartz_storm,titanium_fantasy), 2,
                false, 4500, 60, true, false,Arrays.asList(new ItemStack(IUItem.smalldust.getStack(11))), 10, Collections.emptyList(), 1);



        electrum_symphony = new CropBase("electrum_symphony", 38, EnumSoil.GOLD, 1, 0, 1, 1, 55, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/electrum_symphony"), false, Arrays.asList(silver_melody,
                gold_astral,dandelion), 4,
                false, 4800, 45, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(13))), 10, Collections.emptyList(),
                1);
        zinc_storm = new CropBase("zinc_storm", 40, EnumSoil.ZINC, 1, 0, 1, 1, 60, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/zinc_storm"), false, Arrays.asList(lead_stream,
                onion,tin_ghost), 2,
                false, 4500, 50, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(15))), 10, Collections.emptyList(),
                1);

        manganese_lotus = new CropBase("manganese_lotus", 41, EnumSoil.MANGANESE, 1, 0, 1, 1, 70, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/manganese_lotus"), false, Arrays.asList(magnesium_diamond,
                raspberry), 5,
                false, 5000, 60, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(16))), 10, Collections.emptyList(),
                1);



        germanite_wonders = new CropBase("germanite_wonders", 43, EnumSoil.GERMANIUM, 1, 0, 1, 1, 55, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/germanite_wonders"), false, Arrays.asList(iridium_snowflake,
                tulip_orange,titanium_fantasy), 3,
                false, 4000, 40, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(18))), 10, Collections.emptyList(),
                1);



        uranium_fairy = new CropBase("uranium_fairy", 50, EnumSoil.URANIUM, 1, 0, 1, 1, 60, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/uranium_fairy"), false, Arrays.asList(chromium_gentleness,
                hops,brown_mushroom), 2,
                false, 4500, 50, true, false, Arrays.asList(new ItemStack(IUItem.nuclear_res.getStack(21))), 10, Collections.emptyList(),
                1);
        vanadium_dawn = new CropBase("vanadium_dawn", 27, EnumSoil.VANADIUM, 1, 0, 1, 1, 70, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/vanadium_dawn"), false, Arrays.asList(chromium_gentleness,
                manganese_lotus
        ), 2,
                false, 4500, 60, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(2), 1)), 10, Collections.emptyList(), 1
        );

        arsenite_flower = new CropBase("arsenite_flower", 53, EnumSoil.ARSENIC, 1, 0, 1, 1, 55, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/arsenite_flower"), false,Arrays.asList(chromium_gentleness,
                hops), 3,
                false, 4000, 40, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(32))), 10, Collections.emptyList(),
                1);

        barium_star = new CropBase("barium_star", 54, EnumSoil.BARIUM, 1, 0, 1, 1, 50, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/barium_star"), false, Arrays.asList(electrum_symphony,
                vanadium_dawn), 4,
                false, 5000, 40, true, false,  Arrays.asList(new ItemStack(IUItem.smalldust.getStack(33))), 10, Collections.emptyList(),
                1);

        bismuth_garden = new CropBase("bismuth_garden", 55, EnumSoil.BISMUTH, 1, 0, 1, 1, 60, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/bismuth_garden"), false, Arrays.asList(vanadium_dawn,
                cobalt_grapefruit), 2,
                false, 4500, 50, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(34))), 10, Collections.emptyList(),
                1);

        gadolinium_lotus = new CropBase("gadolinium_lotus", 56, EnumSoil.GADOLINIUM, 1, 0, 1, 1, 70, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/gadolinium_lotus"), false, Arrays.asList(copper_heart,
                electrum_symphony), 5,
                false, 5000, 60, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(35))), 10, Collections.emptyList(),
                1);

        gallium_song = new CropBase("gallium_song", 57, EnumSoil.GALLIUM, 1, 0, 1, 1, 65, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/gallium_song"), false, Arrays.asList(copper_heart,
                invar_crystal), 4,
                false, 4800, 50, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(36))), 10, Collections.emptyList(),
                1);

        hafnium_leaf = new CropBase("hafnium_leaf", 58, EnumSoil.HAFNIUM, 1, 0, 1, 1, 55, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/hafnium_leaf"), false, Arrays.asList(mikhailovskaya_lavra,
                platinum_turner), 3,
                false, 4000, 40, true, false,  Arrays.asList(new ItemStack(IUItem.smalldust.getStack(37))), 10, Collections.emptyList(),
                1);

        ytterbium_bright = new CropBase("ytterbium_bright", 59, EnumSoil.YTTRIUM, 1, 0, 1, 1, 50, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/ytterbium_bright"), false, Arrays.asList(magnesium_diamond,
                spinel_zenith), 4,
                false, 5000, 40, true, false,  Arrays.asList(new ItemStack(IUItem.smalldust.getStack(38))), 10, Collections.emptyList(),
                1);

        molybdenum_whirlwind = new CropBase("molybdenum_whirlwind", 60, EnumSoil.MOLYBDENUM, 1, 0, 1, 1, 60, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/molybdenum_whirlwind"), false,Arrays.asList(vanadium_dawn,
                bismuth_garden), 2,
                false, 4500, 50, true, false,  Arrays.asList(new ItemStack(IUItem.smalldust.getStack(39))), 10, Collections.emptyList(),
                1);

        neodymium_glow = new CropBase("neodymium_glow", 61, EnumSoil.NEODYMIUM, 1, 0, 1, 1, 70, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/neodymium_glow"), false, Arrays.asList(magnesium_diamond,
                platinum_turner), 5,
                false, 5000, 60, true, false,  Arrays.asList(new ItemStack(IUItem.smalldust.getStack(40))), 10, Collections.emptyList(),
                1);

        niobium_obelisk = new CropBase("niobium_obelisk", 62, EnumSoil.NIOBIUM, 1, 0, 1, 1, 65, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/niobium_obelisk"), false, Arrays.asList(platinum_turner,
                cobalt_grapefruit,mikhailovskaya_lavra), 4,
                false, 4800, 50, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(41))), 10, Collections.emptyList(),
                1);

        palladium_gentleness = new CropBase("palladium_gentleness", 63, EnumSoil.PALLADIUM, 1, 0, 1, 1, 55, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/palladium_gentleness"), false, Arrays.asList(gadolinium_lotus,
                gallium_song), 3,
                false, 4000, 40, true, false,  Arrays.asList(new ItemStack(IUItem.smalldust.getStack(42))), 10, Collections.emptyList(),
                1);

        polonium_crystal = new CropBase("polonium_crystal", 64, EnumSoil.POLONIUM, 1, 0, 1, 1, 50, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/polonium_crystal"), false, Arrays.asList(neodymium_glow,
                niobium_obelisk), 4,
                false, 5000, 40, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(43))), 10, Collections.emptyList(),
                1);

        strontium_storm = new CropBase("strontium_storm", 65, EnumSoil.STRONTIUM, 1, 0, 1, 1, 60, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/strontium_storm"), false, Arrays.asList(karav_green,
                germanite_wonders), 2,
                false, 4500, 50, true, false,  Arrays.asList(new ItemStack(IUItem.smalldust.getStack(44))), 10, Collections.emptyList(),
                1);

        thallium_sunlight = new CropBase("thallium_sunlight", 66, EnumSoil.THALLIUM, 1, 0, 1, 1, 70, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/thallium_sunlight"), false, Arrays.asList(arsenite_flower,
                barium_star), 5,
                false, 5000, 60, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(45))), 10, Collections.emptyList(),
                1);

        zirconium_dragon = new CropBase("zirconium_dragon", 67, EnumSoil.ZIRCONIUM, 1, 0, 1, 1, 65, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/zirconium_dragon"), false, Arrays.asList(ytterbium_bright,
                molybdenum_whirlwind), 4,
                false, 4800, 50, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(46))), 10, Collections.emptyList(),
                1);

        osmium_silk = new CropBase("osmium_silk", 68, EnumSoil.OSMIUM, 1, 0, 1, 1, 55, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/osmium_silk"), false,Arrays.asList(iridium_snowflake,
                platinum_turner,copper_heart), 3,
                false, 4000, 40, true, false,  Arrays.asList(new ItemStack(IUItem.smalldust.getStack(29))), 10, Collections.emptyList(),
                1);

        tantalum_moon = new CropBase("tantalum_moon", 69, EnumSoil.TANTALUM, 1, 0, 1, 1, 50, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/tantalum_moon"), false, Arrays.asList(thallium_sunlight,
                arsenite_flower), 4,
                false, 5000, 40, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(30))), 10, Collections.emptyList(),
                1);

        cadmium_gentleness = new CropBase("cadmium_gentleness", 70, EnumSoil.CADMIUM, 1, 0, 1, 1, 60, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/cadmium_gentleness"), false, Arrays.asList(molybdenum_whirlwind,
                silver_melody,silicon_avalanche), 2,
                false, 4500, 50, true, false,  Arrays.asList(new ItemStack(IUItem.smalldust.getStack(31))), 10, Collections.emptyList(),
                1);
        nether_wart = new CropBase("nether_wart", 81, EnumSoil.SOULSAND, 1, 2, 1, 1, 60, 3, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/nether_wart_stage"), false, Collections.emptyList(), 3,
                false, 4500, 50, true, true, Arrays.asList(new ItemStack(Items.NETHER_WART)), 0, Collections.emptyList(), 1);

        ender_lily = new CropBase("ender_lily", 71, EnumSoil.ENDER, 1, 0, 1, 1, 70, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/ender_lily"), false,Arrays.asList(nether_wart,
                polonium_crystal,corn,carrot), 5,
                false, 5000, 60, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(49))), 10, Collections.emptyList(),
                1);

        blaze_storm = new CropBase("blaze_storm", 72, EnumSoil.SOULSAND, 1, 0, 1, 1, 65, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/blaze_storm"), false, Arrays.asList(nether_wart,
                germanite_wonders,wheat_seed,red_fury), 4,
                false, 4800, 50, true, false, Arrays.asList(new ItemStack(Items.BLAZE_POWDER)), 10, Collections.emptyList(), 1);

        ghast_dew = new CropBase("ghast_dew", 75, EnumSoil.SOULSAND, 1, 0, 1, 1, 60, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/ghast_dew"), false, Arrays.asList(blaze_storm,
                silver_melody,silicon_avalanche,zinc_storm), 2,
                false, 4500, 50, true, false, Arrays.asList(new ItemStack(IUItem.smalldust.getStack(50))), 10, Collections.emptyList(),
                1);
        bone_leaf = new CropBase("bone_leaf", 76, EnumSoil.SOULSAND, 1, 0, 1, 1, 70, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/bone_leaf"), false, Arrays.asList(nether_wart,
                silicon_avalanche,lead_stream), 5,
                false, 5000, 60, true, false,  Arrays.asList(new ItemStack(IUItem.crafting_elements.getStack(481))), 10,
                Collections.emptyList(), 1);
        americium_moss = new CropBase("americium_moss", 79, EnumSoil.URANIUM, 1, 0, 1, 1, 50, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/americium_moss"), false, Arrays.asList(uranium_fairy,
                nether_wart,zirconium_dragon), 4,
                false, 5000, 40, true, false,Arrays.asList(new ItemStack(IUItem.nuclear_res.getStack(13))), 10, Collections.emptyList(),
                1);
        neptunium_wisdom = new CropBase("neptunium_wisdom", 78, EnumSoil.URANIUM, 1, 0, 1, 1, 55, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/neptunium_wisdom"), false, Arrays.asList(americium_moss,
                arsenite_flower,bismuth_garden,red_mushroom), 3,
                false, 4000, 40, true, false,Arrays.asList(new ItemStack(IUItem.nuclear_res.getStack(14))), 10, Collections.emptyList(),
                1);
        curie_berry = new CropBase("curie_berry", 77, EnumSoil.URANIUM, 1, 0, 1, 1, 65, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/curie_berry"), false, Arrays.asList(polonium_crystal,
                raspberry,uranium_fairy,neptunium_wisdom), 4,
                false, 4800, 50, true, false,  Arrays.asList(new ItemStack(IUItem.nuclear_res.getStack(15))), 10,
                Collections.emptyList(), 1);


        thorium_fist = new CropBase("thorium_fist", 80, EnumSoil.URANIUM, 1, 0, 1, 1, 60, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/thorium_fist"), false, Arrays.asList(uranium_fairy,
                titanium_fantasy,tungsten_moon_flower), 2,
                false, 4500, 50, true, false, Arrays.asList(new ItemStack(IUItem.nuclear_res.getStack(16))), 10, Collections.emptyList()
                , 1);
        terra_wart = new CropBase("terra_wart", 82, EnumSoil.SOULSAND, 1, 0, 1, 1, 60, 4, 1,
                new ResourceLocation(Constants.MOD_ID, "blocks/crop/terra_wart"), false, Arrays.asList(ender_lily,
                ghast_dew,nether_wart), 2,
                false, 4500, 50, true, false, Arrays.asList(new ItemStack(IUItem.terra_wart.getItem())), 10, Collections.emptyList()
                , 1);
        weed_seed.setIgnoreSoil(true);
        GeneticsManager.init();
        com.denfop.api.bee.genetics.GeneticsManager.init();
        BeeInit.init();
        GeneticTraits.init();
        com.denfop.api.bee.genetics.GeneticTraits.init();
    }
    public static void initBiomes(){
        if (geneticBiomes.isEmpty()){
            Genome.init();
        }
        geneticBiomes.get(GeneticTraits.BIOME).forEach(biome -> {
            wheat_seed.addBiome(biome);
            reed_seed.addBiome(biome);
            rubber_reed_seed.addBiome(biome);
            weed_seed.addBiome(biome);
            tulip_red.addBiome(biome);
            tulip_orange.addBiome(biome);
            tulip_pink.addBiome(biome);
            tulip_white.addBiome(biome);
            onion.addBiome(biome);
            brown_mushroom.addBiome(biome);
            brown_mushroom.addBiome(biome);
            poppy.addBiome(biome);
            dandelion.addBiome(biome);
            blue_orchid.addBiome(biome);
            haustonia_gray.addBiome(biome);
            chamomile.addBiome(biome);
            red_mushroom.addBiome(biome);
            corn.addBiome(biome);
            beet.addBiome(biome);
            potato.addBiome(biome);
            carrot.addBiome(biome);
            raspberry.addBiome(biome);
            hops.addBiome(biome);
            tomato.addBiome(biome);
            melon.addBiome(biome);
            pumpkin.addBiome(biome);
            mikhailovskaya_lavra.addBiome(biome);
            aluminum_plutonka.addBiome(biome);
            vanadium_dawn.addBiome(biome);
            tungsten_moon_flower.addBiome(biome);
            invar_crystal.addBiome(biome);
            karav_green.addBiome(biome);
            cobalt_grapefruit.addBiome(biome);
            magnesium_diamond.addBiome(biome);
            nickel_firework.addBiome(biome);
            platinum_turner.addBiome(biome);
            titanium_fantasy.addBiome(biome);
            chromium_gentleness.addBiome(biome);
            spinel_zenith.addBiome(biome);
            electrum_symphony.addBiome(biome);
            silver_melody.addBiome(biome);
            zinc_storm.addBiome(biome);
            manganese_lotus.addBiome(biome);
            iridium_snowflake.addBiome(biome);
            germanite_wonders.addBiome(biome);
            copper_heart.addBiome(biome);
            gold_astral.addBiome(biome);
            iron_crimson.addBiome(biome);
            diamond_island.addBiome(biome);
            emerald_heart.addBiome(biome);
            quartz_storm.addBiome(biome);
            uranium_fairy.addBiome(biome);
            lead_stream.addBiome(biome);
            tin_ghost.addBiome(biome);
            arsenite_flower.addBiome(biome);
            barium_star.addBiome(biome);
            bismuth_garden.addBiome(biome);
            gadolinium_lotus.addBiome(biome);
            gallium_song.addBiome(biome);
            hafnium_leaf.addBiome(biome);
            ytterbium_bright.addBiome(biome);
            molybdenum_whirlwind.addBiome(biome);
            neodymium_glow.addBiome(biome);
            niobium_obelisk.addBiome(biome);
            palladium_gentleness.addBiome(biome);
            polonium_crystal.addBiome(biome);
            strontium_storm.addBiome(biome);
            thallium_sunlight.addBiome(biome);
            zirconium_dragon.addBiome(biome);
            osmium_silk.addBiome(biome);
            tantalum_moon.addBiome(biome);
            cadmium_gentleness.addBiome(biome);
            ender_lily.addBiome(biome);
            blaze_storm.addBiome(biome);
            red_fury.addBiome(biome);
            silicon_avalanche.addBiome(biome);
            ghast_dew.addBiome(biome);
            bone_leaf.addBiome(biome);
            curie_berry.addBiome(biome);
            neptunium_wisdom.addBiome(biome);
            americium_moss.addBiome(biome);
            thorium_fist.addBiome(biome);
            nether_wart.addBiome(biome);
            terra_wart.addBiome(biome);
            FOREST_BEE.addBiome(biome);
            PLAINS_BEE.addBiome(biome);
        });
        geneticBiomes.get(GeneticTraits.BIOME_I).forEach(biome -> {
            weed_seed.addBiome(biome);
            hops.addBiome(biome);
            nether_wart.addBiome(biome);
            terra_wart.addBiome(biome);
            TROPICAL_BEE.addBiome(biome);
            SWAMP_BEE.addBiome(biome);
        });
        geneticBiomes.get(GeneticTraits.BIOME_II).forEach(biome -> {
            weed_seed.addBiome(biome);
            nether_wart.addBiome(biome);
            terra_wart.addBiome(biome);
        });
        geneticBiomes.get(GeneticTraits.BIOME_III).forEach(biome -> {
            weed_seed.addBiome(biome);
            hops.addBiome(biome);
            PLAINS_BEE.addBiome(biome);
        });
        geneticBiomes.get(GeneticTraits.BIOME_IV).forEach(biome -> {
            weed_seed.addBiome(biome);
            WINTER_BEE.addBiome(biome);
        });
    }
}
