package com.denfop;

import com.denfop.blocks.BlockHeavyOre;
import com.denfop.blocks.BlockOre;
import com.denfop.blocks.BlockPreciousOre;
import com.denfop.blocks.BlockThoriumOre;
import com.denfop.blocks.BlocksRadiationOre;
import com.denfop.items.ItemCell;
import com.denfop.items.ItemUpgradePanelKit;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.mechanism.EnumUpgradesMultiMachine;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import ic2.core.block.TileEntityBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IUItem {

    public static Block blockpanel;
    public static Item photoniy;
    public static Item photoniy_ingot;
    public static Item quantumHelmet;
    public static Item quantumBodyarmor;
    public static Item quantumLeggings;
    public static Item quantumBoots;
    public static Item spectralSaber;
    public static Item nanoBox;
    public static Item singularSolarHelmet;
    public static Item spectralSolarHelmet;
    public static Item compressIridiumplate;
    public static Item advQuantumtool;
    public static Item doublecompressIridiumplate;
    public static ItemStack circuitSpectral = new ItemStack(IUItem.basecircuit, 1, 11);
    public static Item lapotronCrystal;
    public static Item neutronium;
    public static Item neutroniumingot;
    public static Item quantumtool;
    public static Item advnanobox;
    public static ItemStack cell;
    public static ItemCell cell_all;
    public static ItemStack uuMatterCell;
    public static Item advancedSolarHelmet;
    public static Item hybridSolarHelmet;
    public static Item ultimateSolarHelmet;
    public static ItemStack machine;
    public static ItemStack advancedMachine;
    public static ItemStack macerator;
    public static ItemStack compressor;
    public static ItemStack iridium;
    public static ItemStack compressiridium;
    public static ItemStack spectral;
    public static ItemStack reactorDepletedprotonSimple;
    public static ItemStack reactorDepletedprotonDual;
    public static ItemStack reactorDepletedprotonQuad;
    public static ItemStack reactorDepletedprotoneit;
    public static ItemStack reactorprotonSimple;
    public static ItemStack reactorprotonDual;
    public static ItemStack reactorprotonQuad;
    public static ItemStack reactorprotoneit;
    public static Item proton;
    public static Item protonshard;
    public static ItemStack cirsuitQuantum = new ItemStack(IUItem.basecircuit, 1, 10);
    public static ItemStack QuantumItems9 = new ItemStack(IUItem.basecircuit, 1, 9);
    public static Item coal_chunk1;
    public static Item compresscarbon;
    public static Item compresscarbonultra;
    public static ItemStack reactorCoolanttwelve;
    public static ItemStack reactorCoolantmax;
    public static Item quantumSaber;
    public static Item ultDDrill;
    public static ItemStack module8 = new ItemStack(IUItem.module7, 1, 10);
    public static ItemStack overclockerUpgrade;
    public static ItemStack overclockerUpgrade1;
    public static ItemStack constructionFoam;
    public static ItemStack constructionFoamWall;
    public static Block blockadmin;
    public static Item module;
    public static ItemStack module1 = new ItemStack(IUItem.basemodules, 1, 0);
    public static ItemStack module2 = new ItemStack(IUItem.basemodules, 1, 3);
    public static ItemStack module3 = new ItemStack(IUItem.basemodules, 1, 6);
    public static ItemStack module4 = new ItemStack(IUItem.basemodules, 1, 9);
    public static Item module5;
    public static Item module6;
    public static Item module7;
    public static Item itemSSP;
    public static Block blocksintezator;
    public static BlockThoriumOre toriyore;
    public static Item toriy;
    public static ItemStack reactorDepletedtoriySimple;
    public static ItemStack reactorDepletedtoriyDual;
    public static ItemStack reactorDepletedtoriyQuad;
    public static ItemStack reactortoriySimple;
    public static ItemStack reactortoriyDual;
    public static ItemStack reactortoriyQuad;
    public static ItemStack photon;
    public static ItemStack neutron;
    public static ItemStack myphical;
    public static Block machines;
    public static Block electricblock;
    public static Item matter;
    public static ItemStack moleculartransformer;
    public static Block machines_base;
    public static Block Chargepadelectricblock;
    public static Block cableblock;
    public static Block machines_base1;
    public static Item module9;
    public static Item nanopickaxe;
    public static Item nanoshovel;
    public static Item nanoaxe;
    public static Item quantumpickaxe;
    public static Item quantumshovel;
    public static Item quantumaxe;
    public static Item spectralpickaxe;
    public static Item spectralshovel;
    public static Item spectralaxe;
    public static ItemStack barionrotor;
    public static ItemStack adronrotor;
    public static ItemStack ultramarinerotor;
    public static Item plate;
    public static Block blockmolecular;
    public static Block blockSE;
    public static Item sunnarium;
    public static Block AdvblockSE;
    public static Item casing;
    public static Item iudust;
    public static Item smalldust;
    public static Item stik;
    public static Item verysmalldust;
    public static Item photonglass;
    public static Item lens;
    public static Item doubleplate;
    public static Item nugget;
    public static Item iuingot;
    public static Block block;
    public static Item crushed;
    public static Item purifiedcrushed;
    public static Block solidmatter;
    public static Block ImpblockSE;
    public static BlockHeavyOre heavyore;
    public static BlockOre ore;
    public static List<String> name_alloys;
    public static List<String> name_mineral;
    public static List<String> name_mineral1;
    public static ItemStack HeliumCell;
    public static Block tranformer;
    public static ItemStack NeftCell;
    public static ItemStack BenzCell;
    public static ItemStack DizelCell;
    public static Block convertersolidmatter;
    public static Item nano_bow;
    public static Item quantum_bow;
    public static Item spectral_bow;
    public static Item adv_lappack;
    public static Item imp_lappack;
    public static Item per_lappack;
    public static Item ruby_helmet;
    public static Item ruby_chestplate;
    public static Item ruby_leggings;
    public static Item ruby_boots;
    public static Item sapphire_helmet;
    public static Item sapphire_chestplate;
    public static Item sapphire_leggings;
    public static Item sapphire_boots;
    public static Item topaz_helmet;
    public static Item topaz_chestplate;
    public static Item topaz_leggings;
    public static Item topaz_boots;
    public static ItemStack reactorDepletedamericiumSimple;
    public static ItemStack reactorDepletedamericiumDual;
    public static ItemStack reactorDepletedamericiumQuad;
    public static ItemStack reactoramericiumSimple;
    public static ItemStack reactoramericiumDual;
    public static ItemStack reactoramericiumQuad;
    public static ItemStack reactorDepletedneptuniumSimple;
    public static ItemStack reactorDepletedneptuniumDual;
    public static ItemStack reactorDepletedneptuniumQuad;
    public static ItemStack reactorneptuniumSimple;
    public static ItemStack reactorneptuniumDual;
    public static ItemStack reactorneptuniumQuad;
    public static ItemStack reactorDepletedcuriumSimple;
    public static ItemStack reactorDepletedcuriumDual;
    public static ItemStack reactorDepletedcuriumQuad;
    public static ItemStack reactorcuriumSimple;
    public static ItemStack reactorcuriumDual;
    public static ItemStack reactorcuriumQuad;
    public static ItemStack reactorDepletedcaliforniaSimple;
    public static ItemStack reactorDepletedcaliforniaDual;
    public static ItemStack reactorDepletedcaliforniaQuad;
    public static ItemStack reactorcaliforniaSimple;
    public static ItemStack reactorcaliforniaDual;
    public static ItemStack reactorcaliforniaQuad;
    public static BlocksRadiationOre radiationore;
    public static Item radiationresources;
    public static Item sunnariumpanel;
    public static Item alloyscasing;
    public static Item alloysdoubleplate;
    public static Item alloysdust;
    public static Item alloysingot;
    public static Item alloysnugget;
    public static Item alloysplate;
    public static Block alloysblock;
    public static Item preciousgem;
    public static BlockPreciousOre preciousore;
    public static Block preciousblock;
    public static Item core;
    public static Item advBatChargeCrystal;
    public static Item itemBatChargeCrystal;
    public static Item magnet;
    public static Item impmagnet;
    public static Item Purifier;
    public static Item GraviTool;
    public static Block basemachine;
    public static ItemStack tranformerUpgrade;
    public static ItemStack tranformerUpgrade1;
    public static Item UpgradeKit;
    public static ItemStack ingot;
    public static Block machines_base2;
    public static Item NanoHelmet;
    public static Item NanoBodyarmor;
    public static Item NanoLeggings;
    public static Item NanoBoots;
    public static Item basecircuit;
    public static Item doublescrapBox;
    public static Block advchamberblock;
    public static Block impchamberblock;
    public static Block perchamberblock;
    public static Block machines_base3;
    public static Item module_schedule;
    public static Block sunnariummaker;
    public static Block sunnariumpanelmaker;
    public static Item Helium;
    public static Block block1;
    public static ItemStack reactorDepletedmendeleviumSimple;
    public static ItemStack reactorDepletedmendeleviumDual;
    public static ItemStack reactorDepletedmendeleviumQuad;
    public static ItemStack reactormendeleviumSimple;
    public static ItemStack reactormendeleviumDual;
    public static ItemStack reactormendeleviumQuad;
    public static ItemStack reactorDepletedberkeliumSimple;
    public static ItemStack reactorDepletedberkeliumDual;
    public static ItemStack reactorDepletedberkeliumQuad;
    public static ItemStack reactorberkeliumSimple;
    public static ItemStack reactorberkeliumDual;
    public static ItemStack reactorberkeliumQuad;
    public static ItemStack reactorDepletedeinsteiniumSimple;
    public static ItemStack reactorDepletedeinsteiniumDual;
    public static ItemStack reactorDepletedeinsteiniumQuad;
    public static ItemStack reactoreinsteiniumSimple;
    public static ItemStack reactoreinsteiniumDual;
    public static ItemStack reactoreinsteiniumQuad;
    public static ItemStack reactorDepleteduran233Simple;
    public static ItemStack reactorDepleteduran233Dual;
    public static ItemStack reactorDepleteduran233Quad;
    public static ItemStack reactoruran233Simple;
    public static ItemStack reactoruran233Dual;
    public static ItemStack reactoruran233Quad;
    public static Item nanodrill;
    public static Item quantumdrill;
    public static Item spectraldrill;
    public static Block basemachine1;
    public static ItemStack genmodule = new ItemStack(IUItem.basemodules, 1, 1);
    public static ItemStack genmodule1 = new ItemStack(IUItem.basemodules, 1, 2);
    public static ItemStack gennightmodule = new ItemStack(IUItem.basemodules, 1, 4);
    public static ItemStack gennightmodule1 = new ItemStack(IUItem.basemodules, 1, 5);
    public static ItemStack storagemodule = new ItemStack(IUItem.basemodules, 1, 7);
    public static ItemStack storagemodule1 = new ItemStack(IUItem.basemodules, 1, 8);
    public static ItemStack outputmodule = new ItemStack(IUItem.basemodules, 1, 10);
    public static ItemStack outputmodule1 = new ItemStack(IUItem.basemodules, 1, 11);
    public static Item paints;
    public static Item quarrymodule;
    public static Item analyzermodule;


    public static Item UpgradePanelKit;
    public static Item upgrademodule;
    public static Block upgradeblock;
    public static Block blockdoublemolecular;
    public static Block oilrefiner;
    public static Block oilblock;
    public static Block oilquarry;
    public static Block oilgetter;
    public static Block combinersolidmatter;
    public static Item hazmathelmet;
    public static Item hazmatchest;
    public static Item hazmatleggins;
    public static Item hazmatboosts;
    public static Item advjetpack;
    public static Item impjetpack;
    public static Item perjetpack;
    public static Item expmodule;
    public static Item plast;
    public static Item plastic_plate;
    public static ItemStack PolyethCell;
    public static ItemStack PolypropCell;
    public static ItemStack OxyCell;
    public static ItemStack HybCell;
    public static Block oiladvrefiner;
    public static Item module_stack;
    public static Item module_quickly;
    public static Item anode;
    public static Item cathode;
    public static Block basemachine2;
    public static Item machinekit;
    public static Item advventspread;
    public static Item impventspread;
    public static Item reactoradvVent;
    public static Item reactorimpVent;
    public static Item reactorCondensatorDiamond;
    public static Item advheatswitch;
    public static Item impheatswitch;
    public static Item entitymodules;
    public static Item bags;
    public static Item adv_bags;
    public static Item imp_bags;
    public static Item spawnermodules;
    public static Block tank;
    public static Block vein;
    public static ItemStack phase_module = new ItemStack(IUItem.basemodules, 1, 12);
    public static ItemStack phase_module1 = new ItemStack(IUItem.basemodules, 1, 13);
    public static ItemStack phase_module2 = new ItemStack(IUItem.basemodules, 1, 14);
    public static ItemStack moonlinse_module = new ItemStack(IUItem.basemodules, 1, 15);
    public static ItemStack moonlinse_module1 = new ItemStack(IUItem.basemodules, 1, 16);
    public static ItemStack moonlinse_module2 = new ItemStack(IUItem.basemodules, 1, 17);
    public static Item basemodules;
    public static final Map<Integer, EnumSolarPanels> map1 = new HashMap<>();
    public static Map<Integer, ItemUpgradePanelKit.EnumSolarPanelsKit> map2 = new HashMap<>();
    public static Map<Fluid, Integer> celltype = new HashMap<>();
    public static Map<Integer, Fluid> celltype1 = new HashMap<>();
    public static Item cable;
    public static Map<String, EnumSolarPanels> map3 = new HashMap<>();
    public static final Map<String, List> panel_list = new HashMap<>();
    public static final Map<String, EnumUpgradesMultiMachine> map4 = new HashMap<>();

    public static void register_mineral() {
        name_mineral = new ArrayList<>();
        name_alloys = new ArrayList<>();
        name_mineral1 = new ArrayList<>();

        name_mineral.add("mikhail");//0
        name_mineral.add("aluminium");//1
        name_mineral.add("vanady");//2
        name_mineral.add("wolfram");//3
        name_mineral.add("invar");//4
        name_mineral.add("caravky");//5
        name_mineral.add("cobalt");//6
        name_mineral.add("magnesium");//7
        name_mineral.add("nickel");//8
        name_mineral.add("platium");//9
        name_mineral.add("titanium");//10
        name_mineral.add("chromium");//11
        name_mineral.add("spinel");//12
        name_mineral.add("electrium");//13
        name_mineral.add("silver");//14
        name_mineral.add("zinc");//15
        name_mineral.add("manganese");//0 ore1
        name_mineral.add("iridium");//1
        name_mineral.add("germanium");//2
        //
        name_mineral1.add("mikhail");//0
        name_mineral1.add("aluminium");//1
        name_mineral1.add("vanady");//2
        name_mineral1.add("wolfram");//3
        name_mineral1.add("cobalt");//4
        name_mineral1.add("magnesium");//5
        name_mineral1.add("nickel");//6
        name_mineral1.add("platium");//7
        name_mineral1.add("titanium");//8
        name_mineral1.add("chromium");//9
        name_mineral1.add("spinel");//10
        name_mineral1.add("silver");//11
        name_mineral1.add("zinc");//12
        name_mineral1.add("manganese");// 13
        //
        name_alloys.add("aluminum_bronze");
        name_alloys.add("alumel");
        name_alloys.add("red_brass");
        name_alloys.add("muntsa");
        name_alloys.add("nichrome");

        name_alloys.add("alcled");
        name_alloys.add("vanadoalumite");
        name_alloys.add("vitalium");
        name_alloys.add("duralumin");
        name_alloys.add("ferromanganese");


    }

}
