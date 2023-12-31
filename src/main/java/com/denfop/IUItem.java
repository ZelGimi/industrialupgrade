package com.denfop;

import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.blocks.BlockClassicOre;
import com.denfop.blocks.BlockFoam;
import com.denfop.blocks.BlockHeavyOre;
import com.denfop.blocks.BlockOre;
import com.denfop.blocks.BlockPreciousOre;
import com.denfop.blocks.BlockResource;
import com.denfop.blocks.BlockRubWood;
import com.denfop.blocks.BlockTexGlass;
import com.denfop.blocks.BlockThoriumOre;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.BlocksRadiationOre;
import com.denfop.blocks.IULeaves;
import com.denfop.blocks.IUSapling;
import com.denfop.items.*;
import com.denfop.items.armour.BaseArmor;
import com.denfop.items.armour.ItemAdvJetpack;
import com.denfop.items.armour.ItemArmorHazmat;
import com.denfop.items.armour.ItemArmorNightvisionGoggles;
import com.denfop.items.armour.ItemLappack;
import com.denfop.items.armour.special.ItemSpecialArmor;
import com.denfop.items.bags.ItemLeadBox;
import com.denfop.items.book.ItemBook;
import com.denfop.items.energy.ItemBattery;
import com.denfop.items.energy.ItemBatterySU;
import com.denfop.items.energy.ItemEnergyShield;
import com.denfop.items.energy.ItemEnergyToolHoe;
import com.denfop.items.energy.ItemKatana;
import com.denfop.items.energy.ItemNanoSaber;
import com.denfop.items.energy.ItemSprayer;
import com.denfop.items.energy.ItemToolWrench;
import com.denfop.items.energy.ItemToolWrenchEnergy;
import com.denfop.items.energy.ItemTreetap;
import com.denfop.items.energy.ItemTreetapEnergy;
import com.denfop.items.energy.ItemWindMeter;
import com.denfop.items.energy.instruments.ItemEnergyInstruments;
import com.denfop.items.modules.ItemCoolingUpgrade;
import com.denfop.items.panel.ItemBatterySolarPanel;
import com.denfop.items.panel.ItemDayNightSolarPanelGlass;
import com.denfop.items.panel.ItemDaySolarPanelGlass;
import com.denfop.items.panel.ItemNightSolarPanelGlass;
import com.denfop.items.panel.ItemOutputSolarPanel;
import com.denfop.items.resource.ItemCraftingElements;
import com.denfop.items.resource.ItemDust;
import com.denfop.items.resource.ItemGear;
import com.denfop.items.resource.ItemNuclearResource;
import com.denfop.items.resource.ItemSmallDust;
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
    public static BlockTileEntity blockpanel;
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
    public static Item AdvlapotronCrystal;
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
    public static Item compressAlloy;
    public static Item quantumSaber;
    public static Item perfect_drill;
    public static ItemStack overclockerUpgrade_1;
    public static ItemStack overclockerUpgrade1;
    public static BlockTileEntity blockadmin;
    public static Item module;
    public static Item module5;
    public static Item module6;
    public static Item module7;
    public static ItemStack module8 = new ItemStack(IUItem.module7, 1, 10);
    public static Item itemiu;
    public static BlockTileEntity blocksintezator;
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
    public static BlockTileEntity machines;
    public static BlockTileEntity electricblock;
    public static Item matter;
    public static ItemStack moleculartransformer;
    public static BlockTileEntity machines_base;
    public static BlockTileEntity chargepadelectricblock;
    public static BlockTileEntity cableblock;
    public static BlockTileEntity machines_base1;
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
    public static BlockTileEntity blockmolecular;
    public static BlockTileEntity blockSE;
    public static Item sunnarium;
    public static BlockTileEntity adv_se_generator;
    public static Item casing;
    public static ItemDust iudust;
    public static ItemSmallDust smalldust;
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
    public static BlockTileEntity solidmatter;
    public static BlockTileEntity imp_se_generator;
    public static BlockHeavyOre heavyore;
    public static BlockOre ore;
    public static List<String> name_alloys;
    public static List<String> name_mineral;
    public static List<String> name_mineral1;
    public static ItemStack HeliumCell;
    public static BlockTileEntity tranformer;
    public static ItemStack NeftCell;
    public static ItemStack BenzCell;
    public static ItemStack DizelCell;
    public static BlockTileEntity convertersolidmatter;
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
    public static Item impBatChargeCrystal;
    public static Item perBatChargeCrystal;
    public static Item magnet;
    public static Item impmagnet;
    public static Item purifier;
    public static Item GraviTool;
    public static BlockTileEntity basemachine;
    public static ItemStack tranformerUpgrade;
    public static ItemStack tranformerUpgrade1;
    public static Item UpgradeKit;
    public static ItemStack ingot;
    public static BlockTileEntity machines_base2;
    public static Item adv_nano_helmet;
    public static Item adv_nano_chestplate;
    public static Item adv_nano_leggings;
    public static Item adv_nano_boots;
    public static Item basecircuit;
    public static ItemStack circuitSpectral = new ItemStack(IUItem.basecircuit, 1, 11);
    public static ItemStack cirsuitQuantum = new ItemStack(IUItem.basecircuit, 1, 10);
    public static ItemStack circuitNano = new ItemStack(IUItem.basecircuit, 1, 9);
    public static Item doublescrapBox;
    public static BlockTileEntity machines_base3;
    public static Item module_schedule;
    public static BlockTileEntity sunnariummaker;
    public static BlockTileEntity sunnariumpanelmaker;
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
    public static BlockTileEntity basemachine1;
    public static Item paints;
    public static Item quarrymodule;
    public static Item analyzermodule;
    public static Item upgradepanelkit;
    public static Item upgrademodule;
    public static BlockTileEntity upgradeblock;
    public static BlockTileEntity blockdoublemolecular;
    public static BlockTileEntity oilrefiner;
    public static Block oilblock;
    public static BlockTileEntity oilquarry;
    public static BlockTileEntity oilgetter;
    public static BlockTileEntity combinersolidmatter;
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
    public static BlockTileEntity oiladvrefiner;
    public static Item module_stack;
    public static Item module_quickly;
    public static Item anode;
    public static Item cathode;
    public static BlockTileEntity basemachine2;
    public static Item machinekit;
    public static Item entitymodules;
    public static Item bags;
    public static Item adv_bags;
    public static Item imp_bags;
    public static Item spawnermodules;
    public static BlockTileEntity tank;
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
    public static BlockTileEntity simplemachine;
    public static IUItemBase upgrade_speed_creation;
    public static ItemWindRotor rotor_carbon;
    public static ItemWindRotor rotor_steel;
    public static ItemWindRotor rotor_iron;
    public static ItemWindRotor rotor_wood;
    public static ItemWindRotor rotor_bronze;
    public static ItemBlueprint blueprint;
    public static Map<Integer, List<ItemStack>> map_upgrades = new HashMap<>();
    public static List<ItemStack> upgrades_panels = new ArrayList<>();
    public static ItemExpCable expcable;
    public static IUItemBase spectral_box;
    public static IUItemBase adv_spectral_box;
    public static BlockTileEntity blastfurnace;
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
    public static ItemWaterRotor water_rotor_wood;
    public static ItemWaterRotor water_rotor_bronze;
    public static ItemWaterRotor water_rotor_iron;
    public static ItemWaterRotor water_rotor_steel;
    public static ItemWaterRotor water_rotor_carbon;
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
    public static ItemVeinSensor veinsencor;
    public static ItemStack reactorDepleteduranSimple;
    public static ItemStack reactorDepleteduranDual;
    public static ItemStack reactorDepleteduranQuad;
    public static ItemStack reactorDepletedmoxSimple;
    public static ItemStack reactorDepletedmoxDual;
    public static ItemStack reactorDepletedmoxQuad;
    public static ItemStack uranium_fuel_rod;
    public static ItemStack dual_uranium_fuel_rod;
    public static ItemStack quad_uranium_fuel_rod;
    public static ItemStack mox_fuel_rod;
    public static ItemStack dual_mox_fuel_rod;
    public static ItemStack quad_mox_fuel_rod;
    public static ItemEnergyShield nano_shield;
    public static ItemEnergyInstruments drill;
    public static ItemEnergyInstruments diamond_drill;
    public static ItemNanoSaber nanosaber;
    public static ItemEnergyInstruments chainsaw;
    public static ItemToolWrenchEnergy electric_wrench;
    public static ItemTreetapEnergy electric_treetap;
    public static ItemEnergyToolHoe electric_hoe;
    public static ItemBattery reBattery;
    public static ItemBattery energy_crystal;
    public static ItemBattery lapotron_crystal;
    public static ItemBattery charging_re_battery;
    public static ItemBattery advanced_charging_re_battery;
    public static ItemBattery charging_energy_crystal;
    public static ItemBattery charging_lapotron_crystal;
    public static ItemBattery advBattery;
    public static ItemLappack lappack;
    public static ItemLappack batpack;
    public static ItemLappack advanced_batpack;
    public static ItemAdvJetpack electricJetpack;
    public static ItemFrequencyTransmitter frequency_transmitter;
    public static ItemStack lap_energystorage_upgrade;

    public static ItemStack copperCableItem;
    public static ItemStack insulatedCopperCableItem;
    public static ItemStack goldCableItem;
    public static ItemStack insulatedGoldCableItem;
    public static ItemStack ironCableItem;
    public static ItemStack insulatedIronCableItem;
    public static ItemStack insulatedTinCableItem;
    public static ItemStack glassFiberCableItem;
    public static ItemStack adv_lap_energystorage_upgrade;
    public static ItemStack imp_lap_energystorage_upgrade;
    public static ItemStack per_lap_energystorage_upgrade;
    public static ItemStack tinCableItem;
    public static Set<Map.Entry<ItemStack, BaseMachineRecipe>> fluidMatterRecipe;
    public static ItemsCoolingSensor coolingsensor;
    public static ItemsHeatSensor heatsensor;
    public static BlockRubWood rubWood;
    public static ItemCrystalMemory crystalMemory;
    public static ItemToolHammer ForgeHammer;
    public static ItemToolCutter cutter;
    public static ItemNuclearResource nuclear_res;
    public static BlockClassicOre classic_ore;
    public static BlockResource blockResource;
    public static IUSapling rubberSapling;
    public static IULeaves leaves;
    public static BlockTexGlass glass;
    public static BlockFoam foam;
    public static ItemToolWrench wrench;
    public static ItemSprayer sprayer;
    public static ItemTreetap treetap;
    public static ItemSpecialArmor nano_boots;
    public static ItemSpecialArmor nano_chestplate;
    public static ItemSpecialArmor quantum_chestplate;
    public static ItemSpecialArmor quantum_helmet;
    public static ItemSpecialArmor quantum_leggings;
    public static ItemSpecialArmor quantum_boots;
    public static ItemSpecialArmor nano_helmet;
    public static ItemSpecialArmor nano_leggings;
    public static ItemArmorNightvisionGoggles nightvision;
    public static ItemArmorHazmat hazmat_chestplate;
    public static ItemArmorHazmat hazmat_helmet;
    public static ItemArmorHazmat hazmat_leggings;
    public static ItemArmorHazmat rubber_boots;
    public static BaseArmor bronze_helmet;
    public static BaseArmor bronze_leggings;
    public static BaseArmor bronze_boots;
    public static BaseArmor bronze_chestplate;
    public static BlockTileEntity invalid;
    public static ItemStack latex;
    public static ItemStack electricHoe;
    public static ItemStack rawcrystalmemory;
    public static ItemStack fluidpullingUpgrade;
    public static ItemStack overclockerUpgrade;
    public static ItemStack transformerUpgrade;
    public static ItemStack energyStorageUpgrade;
    public static ItemStack ejectorUpgrade;
    public static ItemStack fluidEjectorUpgrade;
    public static ItemStack pullingUpgrade;
    public static ItemStack denseplateadviron;
    public static ItemStack platecopper;
    public static ItemStack platetin;
    public static ItemStack denseplateobsidian;
    public static ItemStack denseplatelapi;
    public static ItemStack denseplatelead;
    public static ItemStack denseplateiron;
    public static ItemStack denseplategold;
    public static ItemStack denseplatebronze;
    public static ItemStack denseplatetin;
    public static ItemStack denseplatecopper;
    public static ItemStack plateadviron;
    public static ItemStack plateobsidian;
    public static ItemStack platebronze;
    public static ItemStack plategold;
    public static ItemStack plateiron;
    public static ItemStack platelead;
    public static ItemStack platelapis;
    public static ItemBatterySU suBattery;
    public static ItemStack coal_chunk;
    public static ItemStack electronicCircuit;
    public static ItemStack advancedCircuit;
    public static ItemStack advancedAlloy;
    public static ItemStack carbonFiber;
    public static ItemStack carbonMesh;
    public static ItemStack carbonPlate;
    public static ItemStack iridiumOre;
    public static ItemStack iridiumPlate;
    public static ItemStack iridiumShard;
    public static ItemStack coolantCell;
    public static ItemStack hotcoolantCell;
    public static ItemStack coalBall;
    public static ItemStack compressedCoalBall;
    public static ItemStack coalChunk;
    public static ItemStack industrialDiamond;
    public static ItemStack slag;
    public static ItemStack scrap;
    public static ItemStack scrapBox;
    public static ItemStack biochaff;
    public static ItemStack advIronIngot;
    public static ItemStack leadIngot;
    public static ItemStack bronzeIngot;
    public static ItemStack tinIngot;
    public static ItemStack copperIngot;
    public static ItemStack mixedMetalIngot;
    public static ItemStack smallSulfurDust;
    public static ItemStack smallLeadDust;
    public static ItemStack smallTinDust;
    public static ItemStack smallGoldDust;
    public static ItemStack smallCopperDust;
    public static ItemStack smallIronDust;
    public static ItemStack diamondDust;
    public static ItemStack crushedTinOre;
    public static ItemStack crushedLeadOre;
    public static ItemStack crushedCopperOre;
    public static ItemStack crushedGoldOre;
    public static ItemStack casinglead;
    public static ItemStack casingiron;
    public static ItemStack casinggold;
    public static ItemStack casingbronze;
    public static ItemStack casingtin;
    public static ItemStack casingadviron;
    public static ItemStack casingcopper;
    public static ItemStack copperboiler;
    public static ItemStack heatconducto;
    public static ItemStack powerunitsmall;
    public static ItemStack powerunit;
    public static ItemStack elemotor;
    public static ItemStack purifiedCrushedUraniumOre;
    public static ItemStack purifiedCrushedTinOre;
    public static ItemStack crushedIronOre;
    public static ItemStack purifiedCrushedLeadOre;
    public static ItemStack energiumDust;
    public static ItemStack bronzeDust;
    public static ItemStack stoneDust;
    public static ItemStack purifiedCrushedGoldOre;
    public static ItemStack purifiedCrushedCopperOre;
    public static ItemStack purifiedCrushedIronOre;
    public static ItemStack crushedUraniumOre;
    public static ItemStack silicondioxideDust;
    public static ItemStack sulfurDust;
    public static ItemStack lapiDust;
    public static ItemStack obsidianDust;
    public static ItemStack leadDust;
    public static ItemStack hydratedCoalDust;
    public static ItemStack goldDust;
    public static ItemStack clayDust;
    public static ItemStack coalDust;
    public static ItemStack copperDust;
    public static ItemStack tinDust;
    public static ItemStack ironDust;
    public static ItemStack advironblock;
    public static ItemStack copperOre;
    public static ItemStack tinOre;
    public static ItemStack uraniumOre;
    public static ItemStack leadOre;
    public static ItemStack reinforcedStone;
    public static ItemStack reinforcedGlass;
    public static ItemStack copperBlock;
    public static ItemStack bronzeBlock;
    public static ItemStack tinBlock;
    public static ItemStack uraniumBlock;
    public static ItemStack leadBlock;
    public static ItemStack FluidCell;
    public static ItemStack coil;
    public static ItemStack rubber;
    public static ItemStack UranFuel;
    public static ItemStack Plutonium;
    public static ItemStack RTGPellets;
    public static ItemStack Uran238;
    public static ItemStack Uran235;
    public static ItemStack smallPlutonium;
    public static ItemStack smallUran235;
    public static ItemStack cfPowder;
    public static ItemStack mox;
    public static ItemDaySolarPanelGlass solar_day_glass;
    public static ItemNightSolarPanelGlass solar_night_glass;
    public static ItemDayNightSolarPanelGlass solar_night_day_glass;
    public static ItemBatterySolarPanel solar_battery;
    public static ItemOutputSolarPanel solar_output;
    public static BlockTileEntity blockItemPipes;
    public static BlockTileEntity pipesblock;
    public static BlockTileEntity qcableblock;
    public static BlockTileEntity expcableblock;
    public static BlockTileEntity scableblock;
    public static BlockTileEntity coolpipesblock;
    public static BlockTileEntity heatcoolpipesblock;
    public static BlockTileEntity universalcableblock;
    public static ItemStack smallObsidian;
    public static ItemStack plantBall;
    public static ItemStack tinCan;
    public static ItemStack reactorCoolantSimple;
    public static ItemStack reactorCoolantTriple;
    public static ItemStack reactorCoolantSix;
    public static ItemEFReader efReader;
    public static ItemFacadeItem facadeItem;
    public static IUItemBase connect_item;

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
