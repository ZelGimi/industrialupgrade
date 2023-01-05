package com.denfop;

import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.blocks.BlockHeavyOre;
import com.denfop.blocks.BlockOre;
import com.denfop.blocks.BlockPreciousOre;
import com.denfop.blocks.BlockThoriumOre;
import com.denfop.blocks.BlocksRadiationOre;
import com.denfop.items.IUItemBase;
import com.denfop.items.ItemAdvancedWaterRotor;
import com.denfop.items.ItemAdvancedWindRotor;
import com.denfop.items.ItemBlueprint;
import com.denfop.items.ItemCell;
import com.denfop.items.ItemCoreWater;
import com.denfop.items.ItemCoreWind;
import com.denfop.items.ItemExcitedNucleus;
import com.denfop.items.ItemRotorsUpgrade;
import com.denfop.items.ItemToolLimiter;
import com.denfop.items.ItemWaterRod;
import com.denfop.items.ItemWaterRotorsUpgrade;
import com.denfop.items.ItemWindRod;
import com.denfop.items.ItemsTemplates;
import com.denfop.items.bags.ItemLeadBox;
import com.denfop.items.book.ItemBook;
import com.denfop.items.energy.ItemKatana;
import com.denfop.items.energy.ItemWindMeter;
import com.denfop.items.machines.ItemsAdminPanel;
import com.denfop.items.machines.ItemsAdvRefiner;
import com.denfop.items.machines.ItemsQuarryVein;
import com.denfop.items.machines.ItemsRefiner;
import com.denfop.items.machines.ItemsSintezator;
import com.denfop.items.machines.ItemsTank;
import com.denfop.items.modules.ItemCoolingUpgrade;
import com.denfop.items.resource.ItemCraftingElements;
import com.denfop.items.resource.ItemGear;
import com.denfop.items.resource.alloys.ItemAlloysGear;
import com.denfop.items.transport.ItemCoolPipes;
import com.denfop.items.transport.ItemExpCable;
import com.denfop.items.transport.ItemHeatColdPipes;
import com.denfop.items.transport.ItemItemPipes;
import com.denfop.items.transport.ItemPipes;
import com.denfop.items.transport.ItemQCable;
import com.denfop.items.transport.ItemSCable;
import com.denfop.items.transport.ItemUniversalCable;
import com.denfop.items.upgradekit.ItemUpgradePanelKit;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IUItem {

    public static final Map<Integer, EnumSolarPanels> map1 = new HashMap<>();
    public static final Map<String, List> panel_list = new HashMap<>();
    public static Block blockpanel;
    public static Item photoniy;
    public static Item photoniy_ingot;
    public static Item spectral_helmet;
    public static Item spectral_chestplate;
    public static Item spectral_leggings;
    public static Item spectral_boots;
    public static Item spectralSaber;
    public static Item nanoBox;
    public static Item singularSolarHelmet;
    public static Item spectralSolarHelmet;
    public static Item compressIridiumplate;
    public static Item advQuantumtool;
    public static Item doublecompressIridiumplate;
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
    public static List<String> list = new ArrayList<>();
    public static List<String> list1 = new ArrayList<>();
    public static List<String> list_space_upgrades = new ArrayList<>();
    public static Set<Map.Entry<ItemStack, BaseMachineRecipe>> machineRecipe = new HashSet<>();
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
    public static Item coal_chunk1;
    public static Item compresscarbon;
    public static Item compresscarbonultra;
    public static ItemStack reactorCoolanttwelve;
    public static ItemStack reactorCoolantmax;
    public static Item quantumSaber;
    public static Item perfect_drill;
    public static ItemStack overclockerUpgrade;
    public static ItemStack overclockerUpgrade1;
    public static ItemsAdminPanel blockadmin;
    public static Item module;
    public static Item module5;
    public static Item module6;
    public static Item module7;
    public static ItemStack module8 = new ItemStack(IUItem.module7, 1, 10);
    public static Item itemiu;
    public static ItemsSintezator blocksintezator;
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
    public static Block chargepadelectricblock;
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
    public static Block adv_se_generator;
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
    public static Block imp_se_generator;
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
    public static Item purifier;
    public static Item GraviTool;
    public static Block basemachine;
    public static ItemStack tranformerUpgrade;
    public static ItemStack tranformerUpgrade1;
    public static Item UpgradeKit;
    public static ItemStack ingot;
    public static Block machines_base2;
    public static Item adv_nano_helmet;
    public static Item adv_nano_chestplate;
    public static Item adv_nano_leggings;
    public static Item adv_nano_boots;
    public static Item basecircuit;
    public static ItemStack circuitSpectral = new ItemStack(IUItem.basecircuit, 1, 11);
    public static ItemStack cirsuitQuantum = new ItemStack(IUItem.basecircuit, 1, 10);
    public static ItemStack circuitNano = new ItemStack(IUItem.basecircuit, 1, 9);
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
    public static Item paints;
    public static Item quarrymodule;
    public static Item analyzermodule;
    public static Item upgradepanelkit;
    public static Item upgrademodule;
    public static Block upgradeblock;
    public static Block blockdoublemolecular;
    public static ItemsRefiner oilrefiner;
    public static Block oilblock;
    public static ItemsQuarryVein oilquarry;
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
    public static ItemsAdvRefiner oiladvrefiner;
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
    public static ItemsTank tank;
    public static Item basemodules;
    public static ItemStack module1 = new ItemStack(IUItem.basemodules, 1, 0);
    public static ItemStack module2 = new ItemStack(IUItem.basemodules, 1, 3);
    public static ItemStack module3 = new ItemStack(IUItem.basemodules, 1, 6);
    public static ItemStack module4 = new ItemStack(IUItem.basemodules, 1, 9);
    public static ItemStack genmodule = new ItemStack(IUItem.basemodules, 1, 1);
    public static ItemStack genmodule1 = new ItemStack(IUItem.basemodules, 1, 2);
    public static ItemStack gennightmodule = new ItemStack(IUItem.basemodules, 1, 4);
    public static ItemStack gennightmodule1 = new ItemStack(IUItem.basemodules, 1, 5);
    public static ItemStack storagemodule = new ItemStack(IUItem.basemodules, 1, 7);
    public static ItemStack storagemodule1 = new ItemStack(IUItem.basemodules, 1, 8);
    public static ItemStack outputmodule = new ItemStack(IUItem.basemodules, 1, 10);
    public static ItemStack outputmodule1 = new ItemStack(IUItem.basemodules, 1, 11);
    public static ItemStack phase_module = new ItemStack(IUItem.basemodules, 1, 12);
    public static ItemStack phase_module1 = new ItemStack(IUItem.basemodules, 1, 13);
    public static ItemStack phase_module2 = new ItemStack(IUItem.basemodules, 1, 14);
    public static ItemStack moonlinse_module = new ItemStack(IUItem.basemodules, 1, 15);
    public static ItemStack moonlinse_module1 = new ItemStack(IUItem.basemodules, 1, 16);
    public static ItemStack moonlinse_module2 = new ItemStack(IUItem.basemodules, 1, 17);
    public static Map<Integer, ItemUpgradePanelKit.EnumSolarPanelsKit> map2 = new HashMap<>();
    public static Map<Fluid, Integer> celltype = new HashMap<>();
    public static Map<Integer, Fluid> celltype1 = new HashMap<>();
    public static Item cable;
    public static Map<String, EnumSolarPanels> map3 = new HashMap<>();
    public static Item module_storage;
    public static ItemBook book;
    public static ItemPipes pipes;
    public static ItemsTemplates templates;
    public static ItemExcitedNucleus excitednucleus;
    public static ItemWindRod windrod;
    public static ItemQCable qcable;
    public static ItemLeadBox leadbox;
    public static ItemSCable scable;
    public static ItemKatana katana;
    public static ItemCoolPipes coolpipes;
    public static ItemCoolingUpgrade coolupgrade;
    public static IUItemBase autoheater;
    public static ItemCoreWind corewind;
    public static Block simplemachine;
    public static IUItemBase upgrade_speed_creation;
    public static ItemAdvancedWindRotor rotor_carbon;
    public static ItemAdvancedWindRotor rotor_steel;
    public static ItemAdvancedWindRotor rotor_iron;
    public static ItemAdvancedWindRotor rotor_wood;
    public static ItemAdvancedWindRotor rotor_bronze;
    public static ItemBlueprint blueprint;
    public static Map<Integer, List<ItemStack>> map_upgrades = new HashMap<>();
    public static List<ItemStack> upgrades_panels = new ArrayList<>();
    public static ItemExpCable expcable;
    public static IUItemBase spectral_box;
    public static IUItemBase adv_spectral_box;
    public static Block blastfurnace;
    public static ItemRotorsUpgrade rotors_upgrade;
    public static IUItemBase rotorupgrade_schemes;
    public static ItemWindMeter windmeter;
    public static ItemCraftingElements crafting_elements;
    public static ItemItemPipes item_pipes;
    public static ItemGear gear;
    public static ItemWaterRotorsUpgrade water_rotors_upgrade;
    public static ItemWaterRod water_rod;
    public static ItemCoreWater corewater;
    public static ItemAlloysGear alloygear;
    public static ItemAdvancedWaterRotor water_rotor_wood;
    public static ItemAdvancedWaterRotor water_rotor_bronze;
    public static ItemAdvancedWaterRotor water_rotor_iron;
    public static ItemAdvancedWaterRotor water_rotor_steel;
    public static ItemAdvancedWaterRotor water_rotor_carbon;
    public static ItemStack water_iridium;
    public static ItemStack water_compressiridium;
    public static ItemStack water_spectral;
    public static ItemStack water_myphical;
    public static ItemStack water_photon;
    public static ItemStack water_neutron;
    public static ItemStack water_barionrotor;
    public static ItemStack water_adronrotor;
    public static ItemStack water_ultramarinerotor;
    public static ItemHeatColdPipes heatcold_pipes;
    public static ItemUniversalCable universal_cable;
    public static ItemToolLimiter limiter;

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
