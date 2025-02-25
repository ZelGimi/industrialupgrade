package com.denfop.register;

import com.denfop.Config;
import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.space.rovers.enums.EnumRoversLevel;
import com.denfop.api.space.rovers.enums.EnumRoversLevelFluid;
import com.denfop.api.space.rovers.enums.EnumTypeRovers;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.blocks.BlockAnvil;
import com.denfop.blocks.BlockApatite;
import com.denfop.blocks.BlockBasaltHeavyOre;
import com.denfop.blocks.BlockBasaltHeavyOre1;
import com.denfop.blocks.BlockBasalts;
import com.denfop.blocks.BlockClassicOre;
import com.denfop.blocks.BlockCrop;
import com.denfop.blocks.BlockDeposits;
import com.denfop.blocks.BlockDeposits1;
import com.denfop.blocks.BlockDeposits2;
import com.denfop.blocks.BlockFoam;
import com.denfop.blocks.BlockGas;
import com.denfop.blocks.BlockHeavyOre;
import com.denfop.blocks.BlockHumus;
import com.denfop.blocks.BlockIUFluid;
import com.denfop.blocks.BlockIngots1;
import com.denfop.blocks.BlockIngots2;
import com.denfop.blocks.BlockMineral;
import com.denfop.blocks.BlockNuclearBomb;
import com.denfop.blocks.BlockOil;
import com.denfop.blocks.BlockOre;
import com.denfop.blocks.BlockOres2;
import com.denfop.blocks.BlockOres3;
import com.denfop.blocks.BlockPrecious;
import com.denfop.blocks.BlockPreciousOre;
import com.denfop.blocks.BlockResource;
import com.denfop.blocks.BlockRubWood;
import com.denfop.blocks.BlockSpace;
import com.denfop.blocks.BlockSpace1;
import com.denfop.blocks.BlockSpace2;
import com.denfop.blocks.BlockSpace3;
import com.denfop.blocks.BlockSpaceCobbleStone;
import com.denfop.blocks.BlockSpaceCobbleStone1;
import com.denfop.blocks.BlockSpaceStone;
import com.denfop.blocks.BlockSpaceStone1;
import com.denfop.blocks.BlockStrongAnvil;
import com.denfop.blocks.BlockSwampRubWood;
import com.denfop.blocks.BlockTexGlass;
import com.denfop.blocks.BlockThoriumOre;
import com.denfop.blocks.BlockTropicalRubWood;
import com.denfop.blocks.BlockVolcanoChest;
import com.denfop.blocks.BlocksAlloy;
import com.denfop.blocks.BlocksAlloy1;
import com.denfop.blocks.BlocksIngot;
import com.denfop.blocks.BlocksRadiationOre;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.IUFluid;
import com.denfop.blocks.IULeaves;
import com.denfop.blocks.IUSapling;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.mechanism.BlockAdminPanel;
import com.denfop.blocks.mechanism.BlockAdvCokeOven;
import com.denfop.blocks.mechanism.BlockAdvRefiner;
import com.denfop.blocks.mechanism.BlockAdvSolarEnergy;
import com.denfop.blocks.mechanism.BlockAmpereCable;
import com.denfop.blocks.mechanism.BlockApiary;
import com.denfop.blocks.mechanism.BlockBarrel;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.blocks.mechanism.BlockBioPipes;
import com.denfop.blocks.mechanism.BlockBlastFurnace;
import com.denfop.blocks.mechanism.BlockCable;
import com.denfop.blocks.mechanism.BlockChargepadStorage;
import com.denfop.blocks.mechanism.BlockChemicalPlant;
import com.denfop.blocks.mechanism.BlockCokeOven;
import com.denfop.blocks.mechanism.BlockCombinerSolid;
import com.denfop.blocks.mechanism.BlockCompressor;
import com.denfop.blocks.mechanism.BlockConverterMatter;
import com.denfop.blocks.mechanism.BlockCoolPipes;
import com.denfop.blocks.mechanism.BlockCyclotron;
import com.denfop.blocks.mechanism.BlockDoubleMolecularTransfomer;
import com.denfop.blocks.mechanism.BlockDryer;
import com.denfop.blocks.mechanism.BlockEarthQuarry;
import com.denfop.blocks.mechanism.BlockElectronicsAssembler;
import com.denfop.blocks.mechanism.BlockEnergyStorage;
import com.denfop.blocks.mechanism.BlockExpCable;
import com.denfop.blocks.mechanism.BlockGasChamber;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.blocks.mechanism.BlockGasTurbine;
import com.denfop.blocks.mechanism.BlockGasWell;
import com.denfop.blocks.mechanism.BlockGeothermalPump;
import com.denfop.blocks.mechanism.BlockHeatColdPipes;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import com.denfop.blocks.mechanism.BlockHive;
import com.denfop.blocks.mechanism.BlockHydroTurbine;
import com.denfop.blocks.mechanism.BlockImpSolarEnergy;
import com.denfop.blocks.mechanism.BlockItemPipes;
import com.denfop.blocks.mechanism.BlockLightningRod;
import com.denfop.blocks.mechanism.BlockMacerator;
import com.denfop.blocks.mechanism.BlockMiniSmeltery;
import com.denfop.blocks.mechanism.BlockMolecular;
import com.denfop.blocks.mechanism.BlockMoreMachine;
import com.denfop.blocks.mechanism.BlockMoreMachine1;
import com.denfop.blocks.mechanism.BlockMoreMachine2;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.blocks.mechanism.BlockNightPipes;
import com.denfop.blocks.mechanism.BlockPetrolQuarry;
import com.denfop.blocks.mechanism.BlockPipes;
import com.denfop.blocks.mechanism.BlockPrimalFluidHeater;
import com.denfop.blocks.mechanism.BlockPrimalFluidIntegrator;
import com.denfop.blocks.mechanism.BlockPrimalLaserPolisher;
import com.denfop.blocks.mechanism.BlockPrimalProgrammingTable;
import com.denfop.blocks.mechanism.BlockPrimalPump;
import com.denfop.blocks.mechanism.BlockPrimalSiliconCrystalHandler;
import com.denfop.blocks.mechanism.BlockPrimalWireInsulator;
import com.denfop.blocks.mechanism.BlockQCable;
import com.denfop.blocks.mechanism.BlockQuarryVein;
import com.denfop.blocks.mechanism.BlockRadPipes;
import com.denfop.blocks.mechanism.BlockRefiner;
import com.denfop.blocks.mechanism.BlockRefractoryFurnace;
import com.denfop.blocks.mechanism.BlockSCable;
import com.denfop.blocks.mechanism.BlockSimpleMachine;
import com.denfop.blocks.mechanism.BlockSintezator;
import com.denfop.blocks.mechanism.BlockSmeltery;
import com.denfop.blocks.mechanism.BlockSolarEnergy;
import com.denfop.blocks.mechanism.BlockSolarPanels;
import com.denfop.blocks.mechanism.BlockSolderingMechanism;
import com.denfop.blocks.mechanism.BlockSolidMatter;
import com.denfop.blocks.mechanism.BlockSqueezer;
import com.denfop.blocks.mechanism.BlockSteamBoiler;
import com.denfop.blocks.mechanism.BlockSteamPipe;
import com.denfop.blocks.mechanism.BlockSteamTurbine;
import com.denfop.blocks.mechanism.BlockSunnariumMaker;
import com.denfop.blocks.mechanism.BlockSunnariumPanelMaker;
import com.denfop.blocks.mechanism.BlockTank;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.blocks.mechanism.BlockUniversalCable;
import com.denfop.blocks.mechanism.BlockUpgradeBlock;
import com.denfop.blocks.mechanism.BlockWaterMill;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.blocks.mechanism.BlockWindTurbine;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.items.IUItemBase;
import com.denfop.items.ItemBaseCircuit;
import com.denfop.items.ItemBooze;
import com.denfop.items.ItemCanister;
import com.denfop.items.ItemChemistry;
import com.denfop.items.ItemCore;
import com.denfop.items.ItemCoreWater;
import com.denfop.items.ItemCoreWind;
import com.denfop.items.ItemCrystalMemory;
import com.denfop.items.ItemDeplanner;
import com.denfop.items.ItemEFReader;
import com.denfop.items.ItemExcitedNucleus;
import com.denfop.items.ItemFacadeItem;
import com.denfop.items.ItemFeature;
import com.denfop.items.ItemFluidCell;
import com.denfop.items.ItemFoodIU;
import com.denfop.items.ItemFrame;
import com.denfop.items.ItemFrequencyTransmitter;
import com.denfop.items.ItemGasSensor;
import com.denfop.items.ItemLens;
import com.denfop.items.ItemMesh;
import com.denfop.items.ItemPaints;
import com.denfop.items.ItemPhotoniumGlass;
import com.denfop.items.ItemPlaner;
import com.denfop.items.ItemRadioprotector;
import com.denfop.items.ItemReactorData;
import com.denfop.items.ItemRecipeSchedule;
import com.denfop.items.ItemRotorsUpgrade;
import com.denfop.items.ItemSolidMatter;
import com.denfop.items.ItemSteamRod;
import com.denfop.items.ItemToolCrafting;
import com.denfop.items.ItemToolCutter;
import com.denfop.items.ItemToolHammer;
import com.denfop.items.ItemVeinSensor;
import com.denfop.items.ItemWaterRod;
import com.denfop.items.ItemWaterRotor;
import com.denfop.items.ItemWaterRotorsUpgrade;
import com.denfop.items.ItemWindRod;
import com.denfop.items.ItemWindRotor;
import com.denfop.items.ItemsCoolingSensor;
import com.denfop.items.ItemsHeatSensor;
import com.denfop.items.ItemsTemplates;
import com.denfop.items.armour.BaseArmor;
import com.denfop.items.armour.ItemAdvJetpack;
import com.denfop.items.armour.ItemArmorAdvHazmat;
import com.denfop.items.armour.ItemArmorHazmat;
import com.denfop.items.armour.ItemArmorNightvisionGoggles;
import com.denfop.items.armour.ItemArmorVolcanoHazmat;
import com.denfop.items.armour.ItemLappack;
import com.denfop.items.armour.ItemSolarPanelHelmet;
import com.denfop.items.armour.special.EnumSubTypeArmor;
import com.denfop.items.armour.special.EnumTypeArmor;
import com.denfop.items.armour.special.ItemSpecialArmor;
import com.denfop.items.bags.ItemEnergyBags;
import com.denfop.items.bags.ItemLeadBox;
import com.denfop.items.bee.ItemBeeAnalyzer;
import com.denfop.items.bee.ItemJarBees;
import com.denfop.items.crop.ItemAgriculturalAnalyzer;
import com.denfop.items.crop.ItemCrops;
import com.denfop.items.energy.ItemBattery;
import com.denfop.items.energy.ItemBatterySU;
import com.denfop.items.energy.ItemEnergyBow;
import com.denfop.items.energy.ItemEnergyToolHoe;
import com.denfop.items.energy.ItemGraviTool;
import com.denfop.items.energy.ItemHammer;
import com.denfop.items.energy.ItemKatana;
import com.denfop.items.energy.ItemMagnet;
import com.denfop.items.energy.ItemNanoSaber;
import com.denfop.items.energy.ItemPurifier;
import com.denfop.items.energy.ItemQuantumSaber;
import com.denfop.items.energy.ItemSpectralSaber;
import com.denfop.items.energy.ItemSprayer;
import com.denfop.items.energy.ItemSteelHammer;
import com.denfop.items.energy.ItemToolWrench;
import com.denfop.items.energy.ItemToolWrenchEnergy;
import com.denfop.items.energy.ItemNet;
import com.denfop.items.energy.ItemTreetap;
import com.denfop.items.energy.ItemTreetapEnergy;
import com.denfop.items.energy.ItemWindMeter;
import com.denfop.items.energy.instruments.EnumTypeInstruments;
import com.denfop.items.energy.instruments.EnumVarietyInstruments;
import com.denfop.items.energy.instruments.ItemEnergyInstruments;
import com.denfop.items.genome.ItemBeeGenome;
import com.denfop.items.genome.ItemCropGenome;
import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.items.modules.ItemBaseModules;
import com.denfop.items.modules.ItemCoolingUpgrade;
import com.denfop.items.modules.ItemEntityModule;
import com.denfop.items.modules.ItemModuleType;
import com.denfop.items.modules.ItemModuleTypePanel;
import com.denfop.items.modules.ItemQuarryModule;
import com.denfop.items.modules.ItemReactorModules;
import com.denfop.items.modules.ItemSpaceUpgradeModule;
import com.denfop.items.modules.ItemSpawnerModules;
import com.denfop.items.modules.ItemUpgradeModule;
import com.denfop.items.panel.ItemBatterySolarPanel;
import com.denfop.items.panel.ItemDayNightSolarPanelGlass;
import com.denfop.items.panel.ItemDaySolarPanelGlass;
import com.denfop.items.panel.ItemNightSolarPanelGlass;
import com.denfop.items.panel.ItemOutputSolarPanel;
import com.denfop.items.reactors.ItemBaseRod;
import com.denfop.items.reactors.ItemCapacitor;
import com.denfop.items.reactors.ItemComponentVent;
import com.denfop.items.reactors.ItemEnergyCoupler;
import com.denfop.items.reactors.ItemExchanger;
import com.denfop.items.reactors.ItemNeutronProtector;
import com.denfop.items.reactors.ItemRadioactive;
import com.denfop.items.reactors.ItemReactorCapacitor;
import com.denfop.items.reactors.ItemReactorCoolant;
import com.denfop.items.reactors.ItemReactorHeatExchanger;
import com.denfop.items.reactors.ItemReactorPlate;
import com.denfop.items.reactors.ItemReactorVent;
import com.denfop.items.reactors.ItemsFan;
import com.denfop.items.reactors.ItemsPumps;
import com.denfop.items.reactors.RadiationPellets;
import com.denfop.items.reactors.RadiationResources;
import com.denfop.items.relocator.ItemRelocator;
import com.denfop.items.resource.ItemCasing;
import com.denfop.items.resource.ItemCraftingElements;
import com.denfop.items.resource.ItemCrushed;
import com.denfop.items.resource.ItemDoublePlate;
import com.denfop.items.resource.ItemDust;
import com.denfop.items.resource.ItemGear;
import com.denfop.items.resource.ItemIUCrafring;
import com.denfop.items.resource.ItemIngots;
import com.denfop.items.resource.ItemNuclearResource;
import com.denfop.items.resource.ItemNugget;
import com.denfop.items.resource.ItemPlate;
import com.denfop.items.resource.ItemPurifiedCrushed;
import com.denfop.items.resource.ItemRawIngot;
import com.denfop.items.resource.ItemRawMetals;
import com.denfop.items.resource.ItemSmallDust;
import com.denfop.items.resource.ItemSticks;
import com.denfop.items.resource.ItemSunnarium;
import com.denfop.items.resource.ItemSunnariumPanel;
import com.denfop.items.resource.ItemVerySmallDust;
import com.denfop.items.resource.alloys.ItemAlloysCasing;
import com.denfop.items.resource.alloys.ItemAlloysDoublePlate;
import com.denfop.items.resource.alloys.ItemAlloysDust;
import com.denfop.items.resource.alloys.ItemAlloysGear;
import com.denfop.items.resource.alloys.ItemAlloysIngot;
import com.denfop.items.resource.alloys.ItemAlloysNugget;
import com.denfop.items.resource.alloys.ItemAlloysPlate;
import com.denfop.items.resource.preciousresources.ItemPreciousGem;
import com.denfop.items.space.ItemColonialBuilding;
import com.denfop.items.space.ItemResearchLens;
import com.denfop.items.space.ItemRover;
import com.denfop.items.space.ItemSpace;
import com.denfop.items.transport.ItemAmpereCable;
import com.denfop.items.transport.ItemBioPipe;
import com.denfop.items.transport.ItemCable;
import com.denfop.items.transport.ItemCoolPipes;
import com.denfop.items.transport.ItemExpCable;
import com.denfop.items.transport.ItemHeatColdPipes;
import com.denfop.items.transport.ItemItemPipes;
import com.denfop.items.transport.ItemNightPipe;
import com.denfop.items.transport.ItemPipes;
import com.denfop.items.transport.ItemQCable;
import com.denfop.items.transport.ItemRadCable;
import com.denfop.items.transport.ItemSCable;
import com.denfop.items.transport.ItemSteamPipe;
import com.denfop.items.transport.ItemUniversalCable;
import com.denfop.items.upgradekit.ItemUpgradeKit;
import com.denfop.items.upgradekit.ItemUpgradeMachinesKit;
import com.denfop.items.upgradekit.ItemUpgradePanelKit;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class Register {

    static final ItemArmor.ArmorMaterial RubyMaterial = EnumHelper.addArmorMaterial("RubyMaterial", "ruby", 450,
            new int[]{2, 7, 3, 1}, 25, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F
    );
    static final ItemArmor.ArmorMaterial SapphireMaterial = EnumHelper.addArmorMaterial("SapphireMaterial", "sapphire", 550,
            new int[]{3, 5, 3, 2}, 25, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F
    );
    static final ItemArmor.ArmorMaterial TopazMaterial = EnumHelper.addArmorMaterial("TopazMaterial", "topaz", 650,
            new int[]{3, 5, 4, 1}, 25, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F
    );

    public static void init() {

        registerfluid(FluidName.fluidiron, 1000, 3000, false);
        registerfluid(FluidName.fluidquartz, 1000, 3000, false);
        registerfluid(FluidName.fluidchromium, 1000, 3000, false);
        registerfluid(FluidName.fluidnichrome, 1000, 3000, false);
        registerfluid(FluidName.fluidmagnesium, 1000, 3000, false);
        registerfluid(FluidName.fluidobsidian, 1000, 3000, false);
        registerfluid(FluidName.fluidduralumin, 1000, 3000, false);
        registerfluid(FluidName.fluidnickel, 1000, 3000, false);
        registerfluid(FluidName.fluidcarbon, 1000, 3000, false);
        registerfluid(FluidName.fluidsteel, 1000, 3000, false);
        registerfluid(FluidName.fluidtitaniumsteel, 1000, 3000, false);
        registerfluid(FluidName.fluidsodiumhydroxide, 1000, 3000, false);
        registerfluid(FluidName.fluidsodium_hypochlorite, 1000, 3000, false);
        registerfluid(FluidName.fluidgold, 1000, 3000, false);
        registerfluid(FluidName.fluidsilver, 1000, 3000, false);
        registerfluid(FluidName.fluidelectrum, 1000, 3000, false);
        registerfluid(FluidName.fluidinvar, 1000, 3000, false);
        registerfluid(FluidName.fluidcopper, 1000, 3000, false);
        registerfluid(FluidName.fluidtin, 1000, 3000, false);
        registerfluid(FluidName.fluidtungsten, 1000, 3000, false);
        registerfluid(FluidName.fluidwolframite, 1000, 3000, false);
        registerfluid(FluidName.fluidtemperedglass, 1000, 3000, false);
        registerfluid(FluidName.fluidbronze, 1000, 3000, false);
        registerfluid(FluidName.fluidgallium, 1000, 3000, false);
        registerfluid(FluidName.fluidarsenicum, 1000, 3000, false);
        registerfluid(FluidName.fluidaluminium, 1000, 3000, false);
        registerfluid(FluidName.fluidmanganese, 1000, 3000, false);
        registerfluid(FluidName.fluidferromanganese, 1000, 3000, false);
        registerfluid(FluidName.fluidaluminiumbronze, 1000, 3000, false);
        registerfluid(FluidName.fluidirontitanium, 1000, 3000, false);
        registerfluid(FluidName.fluidtitanium, 1000, 3000, false);
        registerfluid(FluidName.fluidarsenicum_gallium, 1000, 3000, false);


        registerfluid(FluidName.fluidNeutron, 3000, 300, false);
        registerfluid(FluidName.fluidHelium, -1000, 300, true);
        registerfluid(FluidName.fluidcryogen, -1000, 300, true);
        registerfluid(FluidName.fluidazurebrilliant, 1000, 300, false);
        registerfluid(FluidName.fluidglowstone, 1000, 300, false);
        registerfluid(FluidName.fluidrawlatex, 1000, 300, false);
        registerfluid(FluidName.fluidbenz, 3000, 500, false);
        registerfluid(FluidName.fluidpetrol90, 3000, 500, false);
        registerfluid(FluidName.fluidpetrol95, 3000, 500, false);
        registerfluid(FluidName.fluidpetrol100, 3000, 500, false);
        registerfluid(FluidName.fluidpetrol105, 3000, 500, false);
        registerfluid(FluidName.fluiddizel, 3000, 500, false);
        registerfluid(FluidName.fluida_diesel, 3000, 500, false);
        registerfluid(FluidName.fluidaa_diesel, 3000, 500, false);
        registerfluid(FluidName.fluidaaa_diesel, 3000, 500, false);
        registerfluid(FluidName.fluidaaaa_diesel, 3000, 500, false);
        registerfluid(FluidName.fluidneft, 3000, 500, false);


        registerfluid(FluidName.fluidsweet_medium_oil, 3000, 500, false);
        registerfluid(FluidName.fluidsweet_heavy_oil, 3000, 500, false);
        registerfluid(FluidName.fluidsour_light_oil, 3000, 500, false);
        registerfluid(FluidName.fluidsour_medium_oil, 3000, 500, false);
        registerfluid(FluidName.fluidsour_heavy_oil, 3000, 500, false);


        registerfluid(FluidName.fluidmotoroil, 3000, 500, false);
        registerfluid(FluidName.fluidblackoil, 3000, 500, false);
        registerfluid(FluidName.fluidcreosote, 3000, 500, false);
        registerfluid(FluidName.fluidindustrialoil, 3000, 500, false);
        registerfluid(FluidName.fluidpolyeth, -3000, 2000, true);
        registerfluid(FluidName.fluidpolyprop, -3000, 2000, true);
        registerfluid(FluidName.fluidacetylene, -3000, 2000, true);
        registerfluid(FluidName.fluidoxy, -3000, 500, true);
        registerfluid(FluidName.fluidnitricoxide, -3000, 500, true);

        registerfluid(FluidName.fluidnitrogenoxy, -3000, 500, true);
        registerfluid(FluidName.fluidmethane, -3000, 500, true);
        registerfluid(FluidName.fluiddibromopropane, -3000, 500, true);
        registerfluid(FluidName.fluidpropane, -3000, 500, true);
        registerfluid(FluidName.fluidethylene, -3000, 500, true);
        registerfluid(FluidName.fluidpropylene, -3000, 500, true);
        registerfluid(FluidName.fluidethane, -3000, 500, true);
        registerfluid(FluidName.fluidbutadiene, -3000, 500, true);
        registerfluid(FluidName.fluidpolybutadiene, -3000, 500, true);
        registerfluid(FluidName.fluidhyd, -3000, 500, true);
        registerfluid(FluidName.fluidnitrogenhydride, -3000, 500, true);
        registerfluid(FluidName.fluidnitrogendioxide, -3000, 500, true);
        registerfluid(FluidName.fluidfluorhyd, -3000, 500, true);
        registerfluid(FluidName.fluidazot, -3000, 500, true);
        registerfluid(FluidName.fluidco2, -3000, 500, true);
        registerfluid(FluidName.fluidgas, -3000, 500, true);
        registerfluid(FluidName.fluidchlorum, -3000, 500, true);
        registerfluid(FluidName.fluidfluor, -3000, 500, true);
        registerfluid(FluidName.fluidbromine, -3000, 500, true);
        registerfluid(FluidName.fluidiodine, 3000, 500, false);
        registerfluid(FluidName.fluidsulfuricacid, 3000, 500, false);
        registerfluid(FluidName.fluidnitricacid, 3000, 500, false);
        registerfluid(FluidName.fluidorthophosphoricacid, 3000, 500, false);

        registerfluid(FluidName.fluidbenzene, 3000, 500, false);
        registerfluid(FluidName.fluidethanol, 3000, 500, false);
        registerfluid(FluidName.fluidacrylonitrile, 3000, 500, false);
        registerfluid(FluidName.fluidpolyacrylonitrile, 3000, 500, false);
        registerfluid(FluidName.fluidbutadiene_nitrile, 3000, 500, false);
        registerfluid(FluidName.fluidtoluene, 3000, 500, false);
        registerfluid(FluidName.fluidmethylbromide, 3000, 500, false);
        registerfluid(FluidName.fluidmethylchloride, 3000, 500, false);
        registerfluid(FluidName.fluidhydrogenbromide, 3000, 500, false);
        registerfluid(FluidName.fluidtrinitrotoluene, 3000, 500, false);


        registerfluid(FluidName.fluidhydrogenchloride, 3000, 500, false);
        registerfluid(FluidName.fluidchloroethane, -3000, 500, true);
        registerfluid(FluidName.fluidtetraethyllead, 3000, 500, false);
        registerfluid(FluidName.fluidcarbonmonoxide, -3000, 500, true);
        registerfluid(FluidName.fluidmethanol, 3000, 500, false);
        registerfluid(FluidName.fluidbutene, -3000, 500, true);
        registerfluid(FluidName.fluidmethylpentane, -3000, 500, true);
        registerfluid(FluidName.fluidcyclohexane, -3000, 500, true);
        registerfluid(FluidName.fluidmethylcyclohexane, -3000, 500, true);
        registerfluid(FluidName.fluidmethylpentanal, -3000, 500, true);
        registerfluid(FluidName.fluidethylhexanol, 3000, 500, false);
        registerfluid(FluidName.fluidethylhexylnitrate, 3000, 500, false);
        registerfluid(FluidName.fluidmethylcyclohexylnitrate, 3000, 500, false);
        registerfluid(FluidName.fluidtertbutylsulfuricacid, 3000, 500, false);
        registerfluid(FluidName.fluidtertbutylalcohol, 3000, 500, false);
        registerfluid(FluidName.fluidisobutylene, -3000, 500, true);
        registerfluid(FluidName.fluidtertbutylmethylether, 3000, 500, false);
        registerfluid(FluidName.fluidmonochlorobenzene, 3000, 500, false);
        registerfluid(FluidName.fluidaniline, 3000, 500, false);
        registerfluid(FluidName.fluidmethyltrichloroaniline, 3000, 500, false);
        registerfluid(FluidName.fluidtrichloroaniline, 3000, 500, false);
        registerfluid(FluidName.fluidmethylsulfate, 3000, 500, false);
        registerfluid(FluidName.fluidpropionic_acid, 3000, 500, false);
        registerfluid(FluidName.fluidacetic_acid, 3000, 500, false);
        registerfluid(FluidName.fluidglucose, 3000, 500, false);


        registerfluid(FluidName.fluidwastesulfuricacid, 3000, 500, false);
        registerfluid(FluidName.fluidsulfuroxide, -3000, 500, false);
        registerfluid(FluidName.fluidsulfurtrioxide, -3000, 500, false);
        registerfluid(FluidName.fluidhydrogensulfide, -3000, 500, false);
        registerfluid(FluidName.fluidcoppersulfate, -3000, 500, false);

        registerfluid(FluidName.fluiduu_matter, 3000, 3000, false);
        registerfluid(FluidName.fluidconstruction_foam, 10000, 50000, false);
        registerfluid(FluidName.fluidcoolant, 1000, 3000, false);

        registerfluid(FluidName.fluidapianroyaljelly, 1000, 100, false);
        registerfluid(FluidName.fluidprotein, 1000, 100, false);
        registerfluid(FluidName.fluidbeeswax, 1000, 100, false);
        registerfluid(FluidName.fluidseedoil, 1000, 100, false);
        registerfluid(FluidName.fluidbacteria, 1000, 100, false);
        registerfluid(FluidName.fluidplantmixture, 1000, 100, false);
        registerfluid(FluidName.fluidbeerna, 1000, 100, false);
        registerfluid(FluidName.fluidcroprna, 1000, 100, false);
        registerfluid(FluidName.fluidbeedna, 1000, 100, false);
        registerfluid(FluidName.fluidcropdna, 1000, 100, false);
        registerfluid(FluidName.fluidunstablemutagen, 1000, 100, false);
        registerfluid(FluidName.fluidmutagen, 1000, 100, false);
        registerfluid(FluidName.fluidbeegenetic, 1000, 100, false);
        registerfluid(FluidName.fluidcropgenetic, 1000, 100, false);



        registerfluid(FluidName.fluidhot_coolant, 1000, 3000, false);
        registerfluid(FluidName.fluidpahoehoe_lava, 50000, 250000, false);
        registerfluid(FluidName.fluidbiomass, 1000, 3000, false);
        registerfluid(FluidName.fluidbiogas, 1000, 3000, true);
        registerfluid(FluidName.fluiddistilled_water, 1000, 1000, false);
        registerfluid(FluidName.fluidweed_ex, 1000, 1000, false);
        registerfluid(FluidName.fluidsuperheated_steam, -3000, 100, true);
        registerfluid(FluidName.fluidsteam, -800, 300, true);
        registerfluid(FluidName.fluidhot_water, 1000, 1000, false);
        registerfluid(FluidName.fluidair, 0, 500, true);

        registerfluid(FluidName.fluidwater, 1000, 1000, false);
        registerfluid(FluidName.fluidlava, 50000, 250000, false);

        registerfluid(FluidName.fluidroyaljelly, 1000, 50, false);
        registerfluid(FluidName.fluidhoney, 1000, 50, false);

        registerfluid(FluidName.fluidmoltenmikhail, 1000, 1200, false);
        registerfluid(FluidName.fluidmoltenaluminium, 2200, 660, false);
        registerfluid(FluidName.fluidmoltenvanadium, 6000, 1910, false);
        registerfluid(FluidName.fluidmoltentungsten, 19250, 3422, false);
        registerfluid(FluidName.fluidmoltencobalt, 8000, 1495, false);
        registerfluid(FluidName.fluidmoltenmagnesium, 1700, 650, false);
        registerfluid(FluidName.fluidmoltennickel, 8900, 1455, false);
        registerfluid(FluidName.fluidmoltenplatinum, 21400, 1768, false);
        registerfluid(FluidName.fluidmoltentitanium, 4500, 1668, false);
        registerfluid(FluidName.fluidmoltenchromium, 7180, 1907, false);
        registerfluid(FluidName.fluidmoltenspinel, 5500, 2150, false);
        registerfluid(FluidName.fluidmoltensilver, 10500, 962, false);
        registerfluid(FluidName.fluidmoltenzinc, 7100, 419, false);
        registerfluid(FluidName.fluidmoltenmanganese, 7300, 1244, false);
        registerfluid(FluidName.fluidmolteniridium, 22500, 2446, false);
        registerfluid(FluidName.fluidmoltengermanium, 5320, 938, false);
        registerfluid(FluidName.fluidmoltencopper, 8900, 1085, false);
        registerfluid(FluidName.fluidmoltengold, 19300, 1064, false);
        registerfluid(FluidName.fluidmolteniron, 7850, 1538, false);
        registerfluid(FluidName.fluidmoltenlead, 11340, 327, false);
        registerfluid(FluidName.fluidmoltentin, 7300, 231, false);
        registerfluid(FluidName.fluidmoltenuranium, 18800, 1135, false);
        registerfluid(FluidName.fluidmoltenosmium, 22600, 3033, false);
        registerfluid(FluidName.fluidmoltentantalum, 16600, 3017, false);
        registerfluid(FluidName.fluidmoltencadmium, 8650, 321, false);
        registerfluid(FluidName.fluidmoltenarsenic, 5720, 613, false);
        registerfluid(FluidName.fluidmoltenbarium, 3590, 1000, false);
        registerfluid(FluidName.fluidmoltenbismuth, 9800, 271, false);
        registerfluid(FluidName.fluidmoltengadolinium, 7800, 1312, false);
        registerfluid(FluidName.fluidmoltengallium, 5900, 2204, false);
        registerfluid(FluidName.fluidmoltenhafnium, 13400, 2233, false);
        registerfluid(FluidName.fluidmoltenyttrium, 4450, 1526, false);
        registerfluid(FluidName.fluidmoltenmolybdenum, 10100, 2623, false);
        registerfluid(FluidName.fluidmoltenneodymium, 7200, 1021, false);
        registerfluid(FluidName.fluidmoltenniobium, 8600, 2477, false);
        registerfluid(FluidName.fluidmoltenpalladium, 12250, 1554, false);
        registerfluid(FluidName.fluidmoltenpolonium, 10000, 254, false);
        registerfluid(FluidName.fluidmoltenstrontium, 2600, 1373, false);
        registerfluid(FluidName.fluidmoltenthallium, 11800, 304, false);
        registerfluid(FluidName.fluidmoltenzirconium, 6500, 1855, false);


        registerfluid(FluidName.fluiddimethylhydrazine, 6500, 50, false);
        registerfluid(FluidName.fluidhydrazine, 6500, 50, false);
        registerfluid(FluidName.fluiddecane, 6500, 50, false);
        registerfluid(FluidName.fluidxenon, 6500, -50, true);


        IUItem.invalid = TileBlockCreator.instance.create(MultiTileBlock.class);
        IUItem.water_reactors_component = TileBlockCreator.instance.create(BlockWaterReactors.class);
        IUItem.steam_turbine = TileBlockCreator.instance.create(BlockSteamTurbine.class);
        IUItem.blockdeposits = new BlockDeposits();
        IUItem.blockdeposits1 = new BlockDeposits1();
        IUItem.blockdeposits2 = new BlockDeposits2();

        IUItem.ore2 = new BlockOres2();
        IUItem.fluidCell = new ItemFluidCell();
        IUItem.blockpanel = TileBlockCreator.instance.create(BlockSolarPanels.class);
        IUItem.electricblock = TileBlockCreator.instance.create(BlockEnergyStorage.class);
        IUItem.chargepadelectricblock = TileBlockCreator.instance.create(BlockChargepadStorage.class);
        IUItem.machines_base = TileBlockCreator.instance.create(BlockMoreMachine.class);
        IUItem.machines_base1 = TileBlockCreator.instance.create(BlockMoreMachine1.class);
        IUItem.machines_base2 = TileBlockCreator.instance.create(BlockMoreMachine2.class);
        IUItem.machines_base3 = TileBlockCreator.instance.create(BlockMoreMachine3.class);
        IUItem.machines = TileBlockCreator.instance.create(BlockBaseMachine.class);
        IUItem.solidmatter = TileBlockCreator.instance.create(BlockSolidMatter.class);
        IUItem.combinersolidmatter = TileBlockCreator.instance.create(BlockCombinerSolid.class);
        IUItem.blockSE = TileBlockCreator.instance.create(BlockSolarEnergy.class);
        IUItem.adv_se_generator = TileBlockCreator.instance.create(BlockAdvSolarEnergy.class);
        IUItem.imp_se_generator = TileBlockCreator.instance.create(BlockImpSolarEnergy.class);
        IUItem.blockmolecular = TileBlockCreator.instance.create(BlockMolecular.class);
        IUItem.basemachine = TileBlockCreator.instance.create(BlockBaseMachine1.class);
        IUItem.basemachine1 = TileBlockCreator.instance.create(BlockBaseMachine2.class);
        IUItem.basemachine2 = TileBlockCreator.instance.create(BlockBaseMachine3.class);
        IUItem.blocksintezator = TileBlockCreator.instance.create(BlockSintezator.class);
        IUItem.sunnariummaker = TileBlockCreator.instance.create(BlockSunnariumMaker.class);
        IUItem.sunnariumpanelmaker = TileBlockCreator.instance.create(BlockSunnariumPanelMaker.class);
        IUItem.primal_pump = TileBlockCreator.instance.create(BlockPrimalPump.class);
        IUItem.apiary = TileBlockCreator.instance.create(BlockApiary.class);
        IUItem.barrel = TileBlockCreator.instance.create(BlockBarrel.class);
        IUItem.gas_well = TileBlockCreator.instance.create(BlockGasWell.class);
        IUItem.lightning_rod = TileBlockCreator.instance.create(BlockLightningRod.class);


        IUItem.oilrefiner = TileBlockCreator.instance.create(BlockRefiner.class);
        IUItem.blockItemPipes = TileBlockCreator.instance.create(BlockItemPipes.class);
        IUItem.oiladvrefiner = TileBlockCreator.instance.create(BlockAdvRefiner.class);
        IUItem.upgradeblock = TileBlockCreator.instance.create(BlockUpgradeBlock.class);
        IUItem.oilgetter = TileBlockCreator.instance.create(BlockPetrolQuarry.class);
        IUItem.simplemachine = TileBlockCreator.instance.create(BlockSimpleMachine.class);
        IUItem.tranformer = TileBlockCreator.instance.create(BlockTransformer.class);
        IUItem.blockdoublemolecular = TileBlockCreator.instance.create(BlockDoubleMolecularTransfomer.class);
        IUItem.blockadmin = TileBlockCreator.instance.create(BlockAdminPanel.class);
        IUItem.cableblock = TileBlockCreator.instance.create(BlockCable.class);
        IUItem.pipesblock = TileBlockCreator.instance.create(BlockPipes.class);
        IUItem.oilquarry = TileBlockCreator.instance.create(BlockQuarryVein.class);
        IUItem.qcableblock = TileBlockCreator.instance.create(BlockQCable.class);
        IUItem.scableblock = TileBlockCreator.instance.create(BlockSCable.class);
        IUItem.steamPipeBlock = TileBlockCreator.instance.create(BlockSteamPipe.class);
        IUItem.pho_machine = TileBlockCreator.instance.create(BlocksPhotonicMachine.class);
        IUItem.expcableblock = TileBlockCreator.instance.create(BlockExpCable.class);
        IUItem.earthQuarry = TileBlockCreator.instance.create(BlockEarthQuarry.class);
        IUItem.chemicalPlant = TileBlockCreator.instance.create(BlockChemicalPlant.class);
        IUItem.anvil = TileBlockCreator.instance.create(BlockAnvil.class);
        IUItem.refractoryFurnace = TileBlockCreator.instance.create(BlockRefractoryFurnace.class);
        IUItem.strong_anvil = TileBlockCreator.instance.create(BlockStrongAnvil.class);
        IUItem.squeezer = TileBlockCreator.instance.create(BlockSqueezer.class);
        IUItem.blockMacerator = TileBlockCreator.instance.create(BlockMacerator.class);
        IUItem.fluidIntegrator = TileBlockCreator.instance.create(BlockPrimalFluidIntegrator.class);
        IUItem.blockCompressor = TileBlockCreator.instance.create(BlockCompressor.class);
        IUItem.primalFluidHeater = TileBlockCreator.instance.create(BlockPrimalFluidHeater.class);
        IUItem.gasChamber = TileBlockCreator.instance.create(BlockGasChamber.class);
        IUItem.volcanoChest = TileBlockCreator.instance.create(BlockVolcanoChest.class);
        IUItem.basalts = new BlockBasalts();
        IUItem.apatite = new BlockApatite();

        IUItem.graphite_reactor = TileBlockCreator.instance.create(BlocksGraphiteReactors.class);
        IUItem.gas_reactor = TileBlockCreator.instance.create(BlockGasReactor.class);
        IUItem.mineral = new BlockMineral();
        IUItem.heat_reactor = TileBlockCreator.instance.create(BlockHeatReactor.class);
        IUItem.primalSiliconCrystal = TileBlockCreator.instance.create(BlockPrimalSiliconCrystalHandler.class);
        IUItem.programming_table = TileBlockCreator.instance.create(BlockPrimalProgrammingTable.class);
        IUItem.dryer = TileBlockCreator.instance.create(BlockDryer.class);
        IUItem.mini_smeltery = TileBlockCreator.instance.create(BlockMiniSmeltery.class);

        IUItem.hive =  TileBlockCreator.instance.create(BlockHive.class);
        IUItem.blastfurnace = TileBlockCreator.instance.create(BlockBlastFurnace.class);
        IUItem.cokeoven = TileBlockCreator.instance.create(BlockCokeOven.class);
        IUItem.adv_cokeoven = TileBlockCreator.instance.create(BlockAdvCokeOven.class);
        IUItem.coolpipesblock = TileBlockCreator.instance.create(BlockCoolPipes.class);
        IUItem.amperecableblock = TileBlockCreator.instance.create(BlockAmpereCable.class);
        IUItem.rad_pipesBlock = TileBlockCreator.instance.create(BlockRadPipes.class);
        IUItem.geothermalpump = TileBlockCreator.instance.create(BlockGeothermalPump.class);
        IUItem.tank = TileBlockCreator.instance.create(BlockTank.class);

        IUItem.convertersolidmatter = TileBlockCreator.instance.create(BlockConverterMatter.class);
        IUItem.heatcoolpipesblock = TileBlockCreator.instance.create(BlockHeatColdPipes.class);
        IUItem.universalcableblock = TileBlockCreator.instance.create(BlockUniversalCable.class);
        IUItem.primalPolisher = TileBlockCreator.instance.create(BlockPrimalLaserPolisher.class);
        IUItem.electronics_assembler = TileBlockCreator.instance.create(BlockElectronicsAssembler.class);
        IUItem.solderingMechanism = TileBlockCreator.instance.create(BlockSolderingMechanism.class);
        IUItem.smeltery = TileBlockCreator.instance.create(BlockSmeltery.class);
        IUItem.cyclotron = TileBlockCreator.instance.create(BlockCyclotron.class);
        IUItem.windTurbine = TileBlockCreator.instance.create(BlockWindTurbine.class);
        IUItem.hydroTurbine = TileBlockCreator.instance.create(BlockHydroTurbine.class);
        IUItem.gasTurbine = TileBlockCreator.instance.create(BlockGasTurbine.class);
        IUItem.steam_boiler = TileBlockCreator.instance.create(BlockSteamBoiler.class);
        IUItem.blockwireinsulator = TileBlockCreator.instance.create(BlockPrimalWireInsulator.class);
        IUItem.crop = TileBlockCreator.instance.create(BlockCrop.class);
        IUItem.blockBioPipe = TileBlockCreator.instance.create(BlockBioPipes.class);
        IUItem.blockNightPipe = TileBlockCreator.instance.create(BlockNightPipes.class);

        IUItem.humus = new BlockHumus();
        IUItem.crystalMemory = new ItemCrystalMemory();
        IUItem.solar_day_glass = new ItemDaySolarPanelGlass();
        IUItem.solar_night_glass = new ItemNightSolarPanelGlass();
        IUItem.solar_night_day_glass = new ItemDayNightSolarPanelGlass();
        IUItem.solar_battery = new ItemBatterySolarPanel();
        IUItem.solar_output = new ItemOutputSolarPanel();
        IUItem.FluidCell = new ItemStack(IUItem.fluidCell);
        IUItem.ForgeHammer = new ItemToolHammer("forge_hammer", 80);
        IUItem.ObsidianForgeHammer = new ItemToolHammer("obsidian_hammer", 240);
        IUItem.solderingIron = new ItemToolCrafting("solderingIron", 1500);
        IUItem.laser = new ItemToolCrafting("laser", 60);
        IUItem.cutter = new ItemToolCutter();
        IUItem.pellets = new RadiationPellets();
        IUItem.radcable_item = new ItemRadCable();
        IUItem.classic_ore = new BlockClassicOre();
        IUItem.blockResource = new BlockResource();
        IUItem.rubberSapling = new IUSapling();
        IUItem.leaves = new IULeaves();
        IUItem.glass = new BlockTexGlass();
        IUItem.foam = new BlockFoam();
        IUItem.efReader = new ItemEFReader();
        IUItem.planner = new ItemPlaner();
        IUItem.gasSensor = new ItemGasSensor();
        IUItem.frame = new ItemFrame();
        IUItem.reactorData = new ItemReactorData();
        IUItem.recipe_schedule = new ItemRecipeSchedule();
        IUItem.deplanner = new ItemDeplanner();
        IUItem.molot = new ItemHammer();
        IUItem.nuclear_bomb = new BlockNuclearBomb();
        IUItem.space_ore = new BlockSpace();
        IUItem.space_ore1 = new BlockSpace1();
        IUItem.space_stone = new BlockSpaceStone();
        IUItem.space_stone1 = new BlockSpaceStone1();
        IUItem.space_cobblestone = new BlockSpaceCobbleStone();
        IUItem.space_cobblestone1 = new BlockSpaceCobbleStone1();
        IUItem.space_ore2 = new BlockSpace2();
        IUItem.space_ore3 = new BlockSpace3();
        IUItem.hazmat_chestplate = new ItemArmorHazmat("hazmat_chestplate", EntityEquipmentSlot.CHEST);
        IUItem.hazmat_helmet = new ItemArmorHazmat("hazmat_helmet", EntityEquipmentSlot.HEAD);
        IUItem.hazmat_leggings = new ItemArmorHazmat("hazmat_leggings", EntityEquipmentSlot.LEGS);
        IUItem.rubber_boots = new ItemArmorHazmat("rubber_boots", EntityEquipmentSlot.FEET);


        IUItem.volcano_hazmat_chestplate = new ItemArmorVolcanoHazmat("volcano_hazmat_chestplate", EntityEquipmentSlot.CHEST);
        IUItem.volcano_hazmat_helmet = new ItemArmorVolcanoHazmat("volcano_hazmat_helmet", EntityEquipmentSlot.HEAD);
        IUItem.volcano_hazmat_leggings = new ItemArmorVolcanoHazmat("volcano_hazmat_leggings", EntityEquipmentSlot.LEGS);
        IUItem.volcano_rubber_boots = new ItemArmorVolcanoHazmat("volcano_rubber_boots", EntityEquipmentSlot.FEET);
        IUItem.crops = new ItemCrops();
        IUItem.net = new ItemNet();
        IUItem.facadeItem = new ItemFacadeItem("facadeItem");
        ItemArmor.ArmorMaterial bronzeArmorMaterial = EnumHelper.addArmorMaterial("IU_BRONZE", "IU_BRONZE", 15, new int[]{2, 5,
                6, 2}, 9, null, 0.0F);
        IUItem.bronze_boots = new BaseArmor(
                "bronze_boots",
                bronzeArmorMaterial, 3, EntityEquipmentSlot.FEET,
                "bronze"

        ) {
            public String getUnlocalizedName() {
                return Constants.ABBREVIATION + "." + super.getUnlocalizedName().substring(5);
            }

            public String getUnlocalizedName(ItemStack stack) {
                return this.getUnlocalizedName();
            }

        };
        IUItem.steelHammer = new ItemSteelHammer();
        new ItemFeature();
        IUItem.bronze_chestplate = new BaseArmor(
                "bronze_chestplate",
                bronzeArmorMaterial, 3, EntityEquipmentSlot.CHEST,
                "bronze"

        ) {
            public String getUnlocalizedName() {
                return Constants.ABBREVIATION + "." + super.getUnlocalizedName().substring(5);
            }

            public String getUnlocalizedName(ItemStack stack) {
                return this.getUnlocalizedName();
            }

        };
        IUItem.bronze_helmet = new BaseArmor(
                "bronze_helmet",
                bronzeArmorMaterial,
                3, EntityEquipmentSlot.HEAD,
                "bronze"

        ) {
            public String getUnlocalizedName() {
                return Constants.ABBREVIATION + "." + super.getUnlocalizedName().substring(5);
            }

            public String getUnlocalizedName(ItemStack stack) {
                return this.getUnlocalizedName();
            }

        };
        IUItem.bronze_leggings = new BaseArmor("bronze_leggings", bronzeArmorMaterial, 2, EntityEquipmentSlot.LEGS, "bronze") {
            public String getUnlocalizedName() {
                return Constants.ABBREVIATION + "." + super.getUnlocalizedName().substring(5);
            }

            public String getUnlocalizedName(ItemStack stack) {
                return this.getUnlocalizedName();
            }

        };

        IUItem.treetap = new ItemTreetap();
        IUItem.wrench = new ItemToolWrench();
        IUItem.sprayer = new ItemSprayer();
        IUItem.radioprotector = new ItemRadioprotector();
        IUItem.nuclear_res = new ItemNuclearResource();

        IUItem.rotors_upgrade = new ItemRotorsUpgrade();
        IUItem.rubWood = new BlockRubWood();
        IUItem.swampRubWood = new BlockSwampRubWood();
        IUItem.tropicalRubWood = new BlockTropicalRubWood();
        IUItem.water_rotors_upgrade = new ItemWaterRotorsUpgrade();
        IUItem.water_rod = new ItemWaterRod();
        IUItem.excitednucleus = new ItemExcitedNucleus();
        IUItem.templates = new ItemsTemplates();
        IUItem.crafting_elements = new ItemCraftingElements();
        IUItem.item_pipes = new ItemItemPipes();
        IUItem.gear = new ItemGear();
        IUItem.veinsencor = new ItemVeinSensor();
        IUItem.alloygear = new ItemAlloysGear();
        IUItem.corewater = new ItemCoreWater();
        IUItem.heatcold_pipes = new ItemHeatColdPipes();
        IUItem.universal_cable = new ItemUniversalCable();
        IUItem.coolingsensor = new ItemsCoolingSensor();
        IUItem.heatsensor = new ItemsHeatSensor();
        IUItem.frequency_transmitter = new ItemFrequencyTransmitter();

        IUItem.wood_steam_blade = new ItemSteamRod("wood", 0,0.02, 10800 / 2, new ResourceLocation(
                Constants.MOD_ID,
                "textures/items/rotor" +
                        "/wood_rotor_model.png"
        ));
        IUItem.bronze_steam_blade = new ItemSteamRod("bronze", 0,0.04, 10800, new ResourceLocation(
                Constants.MOD_ID,
                "textures/items/rotor/bronze_rotor_model_1.png"
        ));
        IUItem.iron_steam_blade = new ItemSteamRod("iron", 0, 0.08, (int) (10800 * 1.5),new ResourceLocation(
                Constants.MOD_ID,
                "textures/items/rotor/iron_rotor_model_1.png"
        ));
        IUItem.steel_steam_blade = new ItemSteamRod("steel", 0,0.1, (int) (20800 * 1.5), new ResourceLocation(
                Constants.MOD_ID,
                "textures/items/rotor/steel_rotor_model_1.png"
        ));
        IUItem.carbon_steam_blade = new ItemSteamRod("carbon", 1,0.15, (int) (60800 * 1.5), new ResourceLocation(
                Constants.MOD_ID, "textures/items/rotor/carbon_rotor_model_1.png"));


        IUItem.iridium_steam_blade = new ItemStack(new ItemSteamRod("iridium", 1,0.2, (int) (60800 * 2),
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbo_rotor_model1.png")
        ));
        IUItem.compressiridium_steam_blade = new ItemStack(new ItemSteamRod("compressiridium",1,0.25, (int) (60800 * 3),
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_2.png")
        ));
        IUItem.spectral_steam_blade = new ItemStack(new ItemSteamRod("spectral",1,0.35, (int) (60800 * 4),
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_3.png")
        ));
        IUItem.myphical_steam_blade = new ItemStack(new ItemSteamRod("myphical",2,0.45, (int) (60800 * 6),
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_4.png")
        ));

        IUItem.photon_steam_blade = new ItemStack(new ItemSteamRod("photon",2,0.6, (int) (60800 * 10),
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_6.png")
        ));
        IUItem.neutron_steam_blade = new ItemStack(new ItemSteamRod("neutron",2,0.7, (int) (60800 * 15),
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_5.png")
        ));

        IUItem.barion_steam_blade = new ItemStack(new ItemSteamRod("barion",3,0.825, (int) (60800 * 20),
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_7.png")
        ));

        IUItem.hadron_steam_blade = new ItemStack(new ItemSteamRod("hadron",3,0.95, (int) (60800 * 30),
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_8.png")
        ));
        IUItem.ultramarine_steam_blade = new ItemStack(new ItemSteamRod("ultramarine",3,1.15, (int) (60800 * 50),
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_9.png")
        ));


        IUItem.rotor_wood = new ItemWindRotor("rotor_wood", 5, 10800 / 2, 0.25F, new ResourceLocation(
                Constants.MOD_ID,
                "textures/items/rotor" +
                        "/wood_rotor_model.png"
        ), 1, 0);
        IUItem.rotor_bronze = new ItemWindRotor("rotor_bronze", 7, 86400 / 2, 0.5F, new ResourceLocation(
                Constants.MOD_ID,
                "textures/items/rotor/bronze_rotor_model_1.png"
        ), 2, 1);
        IUItem.rotor_iron = new ItemWindRotor("rotor_iron", 7, (int) (86400 / 1.5), 0.5F, new ResourceLocation(
                Constants.MOD_ID,
                "textures/items/rotor/iron_rotor_model_1.png"
        ), 3, 2);
        IUItem.rotor_steel = new ItemWindRotor("rotor_steel", 9, (int) (172800 / 1.5), 0.75F, new ResourceLocation(
                Constants.MOD_ID,
                "textures/items/rotor/steel_rotor_model_1.png"
        ), 4, 3);
        IUItem.rotor_carbon = new ItemWindRotor("rotor_carbon", 11, (int) (604800 / 1.5), 1.0F, new ResourceLocation(
                Constants.MOD_ID, "textures/items/rotor/carbon_rotor_model_1.png"), 5, 4);

        IUItem.water_rotor_wood = new ItemWaterRotor("water_rotor_wood", 10800 / 2, 0.25F, new ResourceLocation(
                Constants.MOD_ID,
                "textures/items/rotor" +
                        "/wood_rotor_model.png"
        ), 1, 0);
        IUItem.water_rotor_bronze = new ItemWaterRotor("water_rotor_bronze", 86400 / 2, 0.5F, new ResourceLocation(
                Constants.MOD_ID,
                "textures/items/rotor/bronze_rotor_model_1.png"
        ), 2, 1);
        IUItem.water_rotor_iron = new ItemWaterRotor("water_rotor_iron", (int) (86400 / 1.5), 0.5F, new ResourceLocation(
                Constants.MOD_ID,
                "textures/items/rotor/iron_rotor_model_1.png"
        ), 3, 2);
        IUItem.water_rotor_steel = new ItemWaterRotor(
                "water_rotor_steel",
                (int) (172800 / 1.5),
                0.75F,
                new ResourceLocation(
                        Constants.MOD_ID,
                        "textures/items/rotor/steel_rotor_model_1.png"
                ),
                4,
                3
        );
        IUItem.water_rotor_carbon = new ItemWaterRotor(
                "water_rotor_carbon",
                (int) (604800 / 1.5),
                1.0F,
                new ResourceLocation(
                        Constants.MOD_ID, "textures/items/rotor/carbo_rotor_model1.png"),
                5,
                4
        );
        IUItem.water_iridium = new ItemStack(new ItemWaterRotor("water_iridium", Config.durability,
                Config.efficiency,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbo_rotor_model1.png"), 6, 5
        ));
        IUItem.water_compressiridium = new ItemStack(new ItemWaterRotor("water_compressiridium",
                Config.durability1, Config.efficiency1,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_2.png"), 7, 6
        ));
        IUItem.water_spectral = new ItemStack(new ItemWaterRotor("water_spectral",
                Config.durability2, Config.efficiency2,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_3.png"), 8, 7
        ));
        IUItem.water_myphical = new ItemStack(new ItemWaterRotor("water_myphical",
                Config.durability5, Config.efficiency5,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_4.png"), 9, 8
        ));

        IUItem.water_photon = new ItemStack(new ItemWaterRotor("water_photon", Config.durability3,
                Config.efficiency3,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_6.png"), 10, 9
        ));
        IUItem.water_neutron = new ItemStack(new ItemWaterRotor("water_neutron", Config.durability4,
                Config.efficiency4,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_5.png"), 11, 10
        ));

        IUItem.water_barionrotor = new ItemStack(new ItemWaterRotor("water_barionrotor",
                Config.durability4 * 4, Config.efficiency4 * 2,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_7.png"), 12, 11
        ));

        IUItem.water_adronrotor = new ItemStack(new ItemWaterRotor("water_adronrotor",
                Config.durability4 * 16, Config.efficiency4 * 4,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_8.png"), 13, 12
        ));
        IUItem.water_ultramarinerotor = new ItemStack(new ItemWaterRotor("water_ultramarinerotor",
                Config.durability4 * 64, Config.efficiency4 * 8,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_9.png"), 14, 13
        ));
        IUItem.neutroniumingot = new IUItemBase("neutroniumingot");
        IUItem.connect_item = new IUItemBase("connect_item");
        IUItem.nether_star_ingot = new IUItemBase("nether_star_ingot");
        IUItem.upgrade_speed_creation = (IUItemBase) new IUItemBase("upgrade_speed_creation").setCreativeTab(IUCore.ModuleTab);
        IUItem.core = new ItemCore();
        UpgradeSystem.system.addModification();
        IUItem.corewind = new ItemCoreWind();
        IUItem.raw_apatite = (IUItemBase) new IUItemBase("raw_apatite").setCreativeTab(IUCore.RecourseTab);
        IUItem.apatite_cube = (IUItemBase) new IUItemBase("apatite_cube").setCreativeTab(IUCore.RecourseTab);
        IUItem.hops = (IUItemBase) new IUItemBase("hops").setCreativeTab(IUCore.CropsTab);
        IUItem.tomato =  new ItemFoodIU("tomato",3, 0.6F).setCreativeTab(IUCore.CropsTab);
        IUItem.terra_wart =  new ItemFoodIU("terra_wart",1, 0.4F).setAlwaysEdible().setCreativeTab(IUCore.CropsTab);
        IUItem.corn =  new ItemFoodIU("corn",2, 0.4f).setCreativeTab(IUCore.CropsTab);
        IUItem.raspberry =  new ItemFoodIU("raspberry",2, 0.4f).setCreativeTab(IUCore.CropsTab);
        IUItem.fertilizer = (IUItemBase) new IUItemBase("fertilizer");
        IUItem.white_phosphorus = (IUItemBase) new IUItemBase("white_phosphorus");
        IUItem.red_phosphorus = (IUItemBase) new IUItemBase("red_phosphorus");
        IUItem.phosphorus_oxide = (IUItemBase) new IUItemBase("phosphorus_oxide");
        IUItem.honeycomb = (IUItemBase) new IUItemBase("honeycomb");
        IUItem.relocator = new ItemRelocator("relocator");
        IUItem.honey_drop = (IUItemBase) new IUItemBase("honey_drop");
        IUItem.beeswax = (IUItemBase) new IUItemBase("beeswax");
        IUItem.wax_stick = (IUItemBase) new IUItemBase("wax_stick");
        IUItem.royal_jelly= (IUItemBase) new IUItemBase("royal_jelly");
        IUItem.bee_pollen = (IUItemBase) new IUItemBase("bee_pollen");
        IUItem.polished_stick= (IUItemBase) new IUItemBase("polished_stick");
        IUItem.bee_frame_template= (IUItemBase) new IUItemBase("bee_frame_template");
        IUItem.adv_bee_frame_template= (IUItemBase) new IUItemBase("adv_bee_frame_template");
        IUItem.imp_bee_frame_template= (IUItemBase) new IUItemBase("imp_bee_frame_template");
        IUItem.wolframite= (IUItemBase) new IUItemBase("wolframite");
        IUItem.larva= (IUItemBase) new IUItemBase("larva");
        IUItem.plant_mixture= (IUItemBase) new IUItemBase("plant_mixture");
        IUItem.genome_bee = new ItemBeeGenome();
        IUItem.genome_crop = new ItemCropGenome();

        IUItem.autoheater = (IUItemBase) new IUItemBase("autoheater").setCreativeTab(IUCore.ModuleTab);
        IUItem.antiairpollution = (IUItemBase) new IUItemBase("antiairpollution").setCreativeTab(IUCore.ModuleTab);
        IUItem.antiairpollution1 = (IUItemBase) new IUItemBase("antiairpollution1").setCreativeTab(IUCore.ModuleTab);
        IUItem.antisoilpollution = (IUItemBase) new IUItemBase("antisoilpollution").setCreativeTab(IUCore.ModuleTab);
        IUItem.antisoilpollution1 = (IUItemBase) new IUItemBase("antisoilpollution1").setCreativeTab(IUCore.ModuleTab);
        IUItem.double_molecular = (IUItemBase) new IUItemBase("double_molecular").setCreativeTab(IUCore.ModuleTab);
        IUItem.quad_molecular = (IUItemBase) new IUItemBase("quad_molecular").setCreativeTab(IUCore.ModuleTab);

        IUItem.cooling_mixture = (IUItemBase) new IUItemBase("cooling_mixture");
        IUItem.helium_cooling_mixture = (IUItemBase) new IUItemBase("helium_cooling_mixture");
        IUItem.cryogenic_cooling_mixture = (IUItemBase) new IUItemBase("cryogenic_cooling_mixture");
        IUItem.medium_current_converter_to_low = (IUItemBase) new IUItemBase("medium_current_converter_to_low");
        IUItem.high_current_converter_to_low = (IUItemBase) new IUItemBase("high_current_converter_to_low");
        IUItem.extreme_current_converter_to_low = (IUItemBase) new IUItemBase("extreme_current_converter_to_low");
        IUItem.upgrade_casing = (IUItemBase) new IUItemBase("upgrade_casing");
        IUItem.voltage_sensor_for_mechanism = (IUItemBase) new IUItemBase("voltage_sensor_for_mechanism");
        IUItem.graphene_wire = (IUItemBase) new IUItemBase("graphene_wire");
        IUItem.graphene = (IUItemBase) new IUItemBase("graphene");
        IUItem.graphene_plate = (IUItemBase) new IUItemBase("graphene_plate");
        IUItem.motors_with_improved_bearings_ = (IUItemBase) new IUItemBase("motors_with_improved_bearings_");
        IUItem.adv_motors_with_improved_bearings_ = (IUItemBase) new IUItemBase("adv_motors_with_improved_bearings_");
        IUItem.imp_motors_with_improved_bearings_ = (IUItemBase) new IUItemBase("imp_motors_with_improved_bearings_");
        IUItem.compressed_redstone = (IUItemBase) new IUItemBase("compressed_redstone");
        IUItem.electronic_stabilizers = (IUItemBase) new IUItemBase("electronic_stabilizers");
        IUItem.polonium_palladium_composite = new IUItemBase("polonium_palladium_composite");
        IUItem.booze_mug = new ItemBooze("booze_mug");


        IUItem.coolupgrade = new ItemCoolingUpgrade();
        IUItem.upgrademodule = new ItemUpgradeModule();
        IUItem.spaceupgrademodule_schedule = (IUItemBase) new IUItemBase("spaceupgrademodule_schedule");
        IUItem.spaceupgrademodule = new ItemSpaceUpgradeModule();
        IUItem.module9 = new ItemQuarryModule();
        IUItem.leadbox = new ItemLeadBox("lead_box");
        IUItem.agricultural_analyzer = new ItemAgriculturalAnalyzer("agricultural_analyzer");
        IUItem.bee_analyzer = new ItemBeeAnalyzer("bee_analyzer");
        IUItem.windrod = new ItemWindRod();
        IUItem.qcable = new ItemQCable();
        IUItem.scable = new ItemSCable();
        IUItem.steamPipe = new ItemSteamPipe();
        IUItem.spaceItem = new ItemSpace();
        IUItem.rocket = new ItemRover("rocket",10000, EnumRoversLevel.ONE, EnumTypeRovers.ROCKET,2,500000,2048,
                EnumRoversLevelFluid.ONE,2);
        IUItem.adv_rocket = new ItemRover("adv_rocket",20000, EnumRoversLevel.TWO, EnumTypeRovers.ROCKET,3,1000000,4096,
                EnumRoversLevelFluid.TWO,3.5);
        IUItem.imp_rocket = new ItemRover("imp_rocket",30000, EnumRoversLevel.THREE, EnumTypeRovers.ROCKET,4,2000000,8192,
                EnumRoversLevelFluid.THREE,5);
        IUItem.per_rocket = new ItemRover("per_rocket",40000, EnumRoversLevel.FOUR, EnumTypeRovers.ROCKET,5,5000000,16384,
                EnumRoversLevelFluid.FOUR,7.5);
        IUItem.probe = new ItemRover("probe",10000, EnumRoversLevel.ONE, EnumTypeRovers.PROBE,2,500000,2048,
                EnumRoversLevelFluid.ONE,1.35);
        IUItem.adv_probe = new ItemRover("adv_probe",20000, EnumRoversLevel.TWO, EnumTypeRovers.PROBE,3,1000000,4096,
                EnumRoversLevelFluid.TWO,2.25);
        IUItem.imp_probe = new ItemRover("imp_probe",30000, EnumRoversLevel.THREE, EnumTypeRovers.PROBE,4,2000000,8192,
                EnumRoversLevelFluid.THREE,3);
        IUItem.per_probe = new ItemRover("per_probe",40000, EnumRoversLevel.FOUR, EnumTypeRovers.PROBE,5,5000000,16384,
                EnumRoversLevelFluid.FOUR,4);
        IUItem.rover =  new ItemRover("rover",10000, EnumRoversLevel.ONE, EnumTypeRovers.ROVERS,2,500000,2048,
                EnumRoversLevelFluid.ONE,1);
        IUItem.adv_rover = new ItemRover("adv_rover",20000, EnumRoversLevel.TWO, EnumTypeRovers.ROVERS,3,1000000,4096,
                EnumRoversLevelFluid.TWO,1.5);
        IUItem.imp_rover = new ItemRover("imp_rover",30000, EnumRoversLevel.THREE, EnumTypeRovers.ROVERS,4,2000000,8192,
                EnumRoversLevelFluid.THREE,2);
        IUItem.per_rover = new ItemRover("per_rover",40000, EnumRoversLevel.FOUR, EnumTypeRovers.ROVERS,5,5000000,16384,
                EnumRoversLevelFluid.FOUR,3);
        IUItem.satellite =  new ItemRover("satellite",10000, EnumRoversLevel.ONE, EnumTypeRovers.SATELLITE,2,500000,2048,
                EnumRoversLevelFluid.ONE,1.65);
        IUItem.adv_satellite = new ItemRover("adv_satellite",20000, EnumRoversLevel.TWO, EnumTypeRovers.SATELLITE,3,1000000,4096,
                EnumRoversLevelFluid.TWO,3.1);
        IUItem.imp_satellite = new ItemRover("imp_satellite",30000, EnumRoversLevel.THREE, EnumTypeRovers.SATELLITE,4,2000000,8192,
                EnumRoversLevelFluid.THREE,4);
        IUItem.per_satellite = new ItemRover("per_satellite",40000, EnumRoversLevel.FOUR, EnumTypeRovers.SATELLITE,5,5000000,16384,
                EnumRoversLevelFluid.FOUR,6);
        IUItem.research_lens = new ItemResearchLens();
        IUItem.colonial_building = new ItemColonialBuilding();
        IUItem.ironMesh = new ItemMesh("ironmesh", 1000, 1);
        IUItem.steelMesh = new ItemMesh("steelmesh", 2000, 2);
        IUItem.boridehafniumMesh = new ItemMesh("boridehafniummesh", 3500, 3);
        IUItem.vanadiumaluminumMesh = new ItemMesh("vanadiumaluminummesh", 5000, 4);
        IUItem.steleticMesh = new ItemMesh("steleticmesh", 8000, 5);
        IUItem.rawMetals = new ItemRawMetals();
        IUItem.rawIngot = new ItemRawIngot();
        IUItem.expcable = new ItemExpCable();
        IUItem.canister = new ItemCanister();
        IUItem.impBatChargeCrystal = new ItemBattery("itemadvbatchargecrystal", Config.Storagequantumsuit, 32368D, 5, true);
        IUItem.perBatChargeCrystal = new ItemBattery("itembatchargecrystal", Config.Storagequantumsuit * 4, 129472D, 6, true);
        IUItem.reBattery = new ItemBattery("re_battery", 100000.0, 100.0, 1);
        IUItem.energy_crystal = new ItemBattery("energy_crystal", 1000000.0, 2048.0, 3);
        IUItem.lapotron_crystal = new ItemBattery("lapotron_crystal", 1.0E7, 8092.0, 4);
        IUItem.charging_re_battery = new ItemBattery("charging_re_battery", 40000.0, 128.0, 1, true);
        IUItem.advanced_charging_re_battery = new ItemBattery("advanced_charging_re_battery", 400000.0, 1024.0, 2, true);
        IUItem.charging_energy_crystal = new ItemBattery("charging_energy_crystal", 4000000.0, 8192.0, 3, true);
        IUItem.charging_lapotron_crystal = new ItemBattery("charging_lapotron_crystal", 4.0E7, 32768.0, 4, true);
        IUItem.advBattery = new ItemBattery("advanced_re_battery", 10000.0, 256.0, 2);
        IUItem.batpack = new ItemLappack("batpack", 60000.0, 1, 100.0);
        IUItem.advanced_batpack = new ItemLappack("advanced_batpack", 600000.0, 2, 1000.0);
        IUItem.electricJetpack = new ItemAdvJetpack("jetpack", 30000.0, 60.0, 1);
        IUItem.lappack = new ItemLappack("lappack", 2.0E6, 3, 2500.0);
        IUItem.suBattery = new ItemBatterySU("single_use_battery", 1200, 1);
        IUItem.UpgradeKit = new ItemUpgradeKit();
        IUItem.module = new com.denfop.items.ItemUpgradeModule();
        IUItem.GraviTool = new ItemGraviTool("GraviTool");
        IUItem.cable = new ItemCable();
        IUItem.pipes = new ItemPipes();


        IUItem.ruby_helmet = new BaseArmor("ruby_helmet", RubyMaterial, 3, EntityEquipmentSlot.HEAD, "ruby");
        IUItem.ruby_chestplate = new BaseArmor("ruby_chestplate", RubyMaterial, 3, EntityEquipmentSlot.CHEST, "ruby");
        IUItem.ruby_leggings = new BaseArmor("ruby_leggings", RubyMaterial, 3, EntityEquipmentSlot.LEGS, "ruby");
        IUItem.ruby_boots = new BaseArmor("ruby_boots", RubyMaterial, 3, EntityEquipmentSlot.FEET, "ruby");
        IUItem.topaz_helmet = new BaseArmor("topaz_helmet", TopazMaterial, 3, EntityEquipmentSlot.HEAD, "topaz");
        IUItem.topaz_chestplate = new BaseArmor("topaz_chestplate", TopazMaterial, 3, EntityEquipmentSlot.CHEST, "topaz");
        IUItem.topaz_leggings = new BaseArmor("topaz_leggings", TopazMaterial, 3, EntityEquipmentSlot.LEGS, "topaz");
        IUItem.topaz_boots = new BaseArmor("topaz_boots", TopazMaterial, 3, EntityEquipmentSlot.FEET, "topaz");
        IUItem.sapphire_helmet = new BaseArmor("sapphire_helmet", SapphireMaterial, 3, EntityEquipmentSlot.HEAD, "sapphire");
        IUItem.sapphire_chestplate = new BaseArmor(
                "sapphire_chestplate",
                SapphireMaterial,
                3,
                EntityEquipmentSlot.CHEST,
                "sapphire"
        );
        IUItem.sapphire_leggings = new BaseArmor("sapphire_leggings", SapphireMaterial, 3, EntityEquipmentSlot.LEGS, "sapphire");
        IUItem.sapphire_boots = new BaseArmor("sapphire_boots", SapphireMaterial, 3, EntityEquipmentSlot.FEET, "sapphire");
        IUItem.hazmathelmet = new ItemArmorAdvHazmat("hazmathelmet", EntityEquipmentSlot.HEAD);
        IUItem.hazmatchest = new ItemArmorAdvHazmat("hazmatchest", EntityEquipmentSlot.CHEST);
        IUItem.hazmatleggins = new ItemArmorAdvHazmat("hazmatleggins", EntityEquipmentSlot.LEGS);
        IUItem.hazmatboosts = new ItemArmorAdvHazmat("hazmatboosts", EntityEquipmentSlot.FEET);
        IUItem.adv_nano_helmet = new ItemSpecialArmor(EnumSubTypeArmor.HELMET, EnumTypeArmor.ADV_NANO);
        IUItem.adv_nano_chestplate = new ItemSpecialArmor(EnumSubTypeArmor.CHESTPLATE, EnumTypeArmor.ADV_NANO);
        IUItem.adv_nano_leggings = new ItemSpecialArmor(EnumSubTypeArmor.LEGGINGS, EnumTypeArmor.ADV_NANO);
        IUItem.adv_nano_boots = new ItemSpecialArmor(EnumSubTypeArmor.BOOTS, EnumTypeArmor.ADV_NANO);
        IUItem.bags = new ItemEnergyBags("iu_bags", 27, 50000, 500);
        IUItem.adv_bags = new ItemEnergyBags("adv_iu_bags", 45, 75000, 750);
        IUItem.imp_bags = new ItemEnergyBags("imp_iu_bags", 63, 100000, 1000);

        IUItem.AdvlapotronCrystal = new ItemBattery("itembatlamacrystal", Config.Storagequantumsuit, 8092.0D, 4, false);

        IUItem.iridium = new ItemStack(new ItemWindRotor("iridium", Config.Radius, Config.durability,
                Config.efficiency,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_1.png"), 6, 5
        ));
        IUItem.compressiridium = new ItemStack(new ItemWindRotor("compressiridium", Config.Radius1,
                Config.durability1, Config.efficiency1,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_2.png"), 7, 6
        ));
        IUItem.spectral = new ItemStack(new ItemWindRotor("spectral", Config.Radius2,
                Config.durability2, Config.efficiency2,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_3.png"), 8, 7
        ));
        IUItem.myphical = new ItemStack(new ItemWindRotor("myphical", Config.Radius5,
                Config.durability5, Config.efficiency5,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_4.png"), 9, 8
        ));

        IUItem.photon = new ItemStack(new ItemWindRotor("photon", Config.Radius3, Config.durability3,
                Config.efficiency3,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_6.png"), 10, 9
        ));
        IUItem.neutron = new ItemStack(new ItemWindRotor("neutron", Config.Radius4, Config.durability4,
                Config.efficiency4,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_5.png"), 11, 10
        ));

        IUItem.barionrotor = new ItemStack(new ItemWindRotor("barionrotor", Config.Radius5,
                Config.durability4 * 4, Config.efficiency4 * 2,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_7.png"), 12, 11
        ));

        IUItem.adronrotor = new ItemStack(new ItemWindRotor("adronrotor", Config.Radius5,
                Config.durability4 * 16, Config.efficiency4 * 4,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_8.png"), 13, 12
        ));
        IUItem.ultramarinerotor = new ItemStack(new ItemWindRotor("ultramarinerotor", Config.Radius5,
                Config.durability4 * 64, Config.efficiency4 * 8,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model_9.png"), 14, 13
        ));
        IUItem.jarBees = new ItemJarBees();
        IUItem.reactorprotonSimple = new ItemStack(new ItemBaseRod("reactorprotonsimple", 1,
                95, Config.ProtonPower, 3
        ));
        IUItem.reactorprotonDual = new ItemStack(new ItemBaseRod("reactorprotondual", 2,
                190, Config.ProtonPower, 3
        ));
        IUItem.reactorprotonQuad = new ItemStack(new ItemBaseRod("reactorprotonquad", 4,
                380, Config.ProtonPower, 3
        ));


        //
        IUItem.reactortoriySimple = new ItemStack(new ItemBaseRod("reactortoriysimple", 1,
                50, Config.toriyPower, 2
        ));
        IUItem.reactortoriyDual = new ItemStack(new ItemBaseRod("reactortoriydual", 2,
                100, Config.toriyPower, 2
        ));
        IUItem.reactortoriyQuad = new ItemStack(new ItemBaseRod("reactortoriyquad", 4,
                200, Config.toriyPower, 2
        ));

        //
//
        IUItem.reactoramericiumSimple = new ItemStack(new ItemBaseRod("reactoramericiumsimple", 1,
                80, (float) Config.americiumPower, 2
        ));
        IUItem.reactoramericiumDual = new ItemStack(new ItemBaseRod("reactoramericiumdual", 2,
                160, (float) Config.americiumPower, 2
        ));
        IUItem.reactoramericiumQuad = new ItemStack(new ItemBaseRod("reactoramericiumquad", 4,
                320, (float) Config.americiumPower, 2
        ));

        //
        //
        IUItem.reactorneptuniumSimple = new ItemStack(new ItemBaseRod("reactorneptuniumsimple", 1,
                65, (float) Config.neptuniumPower, 2
        ));
        IUItem.reactorneptuniumDual = new ItemStack(new ItemBaseRod("reactorneptuniumdual", 2,
                130, (float) Config.neptuniumPower, 2
        ));
        IUItem.reactorneptuniumQuad = new ItemStack(new ItemBaseRod("reactorneptuniumquad", 4,
                260, (float) Config.neptuniumPower, 2
        ));

        //
        IUItem.reactorcuriumSimple = new ItemStack(new ItemBaseRod("reactorcuriumsimple", 1,
                100, (float) Config.curiumPower, 3
        ));
        IUItem.reactorcuriumDual = new ItemStack(new ItemBaseRod("reactorcuriumdual", 2,
                200, (float) Config.curiumPower, 3
        ));
        IUItem.reactorcuriumQuad = new ItemStack(new ItemBaseRod("reactorcuriumquad", 4,
                400, (float) Config.curiumPower, 3
        ));

        //
        //
        IUItem.reactorcaliforniaSimple = new ItemStack(new ItemBaseRod("reactorcaliforniasimple", 1,
                120, (float) Config.californiaPower, 3
        ));
        IUItem.reactorcaliforniaDual = new ItemStack(new ItemBaseRod("reactorcaliforniadual", 2,
                240, (float) Config.californiaPower, 3
        ));
        IUItem.reactorcaliforniaQuad = new ItemStack(new ItemBaseRod("reactorcaliforniaquad", 4,
                480, (float) Config.californiaPower, 3
        ));

        //
        //
        IUItem.reactorfermiumSimple = new ItemStack(new ItemBaseRod("reactorfermiumsimple", 1,
                230, (float) Config.mendeleviumPower, 4
        ));
        IUItem.reactorfermiumDual = new ItemStack(new ItemBaseRod("reactorfermiumdual", 2,
                460, (float) Config.mendeleviumPower, 4
        ));
        IUItem.reactorfermiumQuad = new ItemStack(new ItemBaseRod("reactorfermiumquad", 4,
                920, (float) Config.mendeleviumPower, 4
        ));

        //
        //
        IUItem.reactormendeleviumSimple = new ItemStack(new ItemBaseRod("reactormendeleviumsimple", 1,
                260, (float) 36, 4
        ));
        IUItem.reactormendeleviumDual = new ItemStack(new ItemBaseRod("reactormendeleviumdual", 2,
                520, (float) 36, 4
        ));
        IUItem.reactormendeleviumQuad = new ItemStack(new ItemBaseRod("reactormendeleviumquad", 4,
                1050, (float) 36, 4
        ));
        //
        IUItem.reactornobeliumSimple = new ItemStack(new ItemBaseRod("reactornobeliumsimple", 1,
                285, (float) 49, 4
        ));
        IUItem.reactornobeliumDual = new ItemStack(new ItemBaseRod("reactornobeliumdual", 2,
                590, (float) 49, 4
        ));
        IUItem.reactornobeliumQuad = new ItemStack(new ItemBaseRod("reactornobeliumquad", 4,
                1200, (float) 49, 4
        ));

        //
        //
        IUItem.reactorlawrenciumSimple = new ItemStack(new ItemBaseRod("reactorlawrenciumsimple", 1,
                300, (float) 60, 4
        ));
        IUItem.reactorlawrenciumDual = new ItemStack(new ItemBaseRod("reactorlawrenciumdual", 2,
                620, (float) 60, 4
        ));
        IUItem.reactorlawrenciumQuad = new ItemStack(new ItemBaseRod("reactorlawrenciumquad", 4,
                1300, (float) 60, 4
        ));
        //
        IUItem.reactorberkeliumSimple = new ItemStack(new ItemBaseRod("reactorberkeliumsimple", 1,
                150, (float) Config.berkeliumPower, 4
        ));
        IUItem.reactorberkeliumDual = new ItemStack(new ItemBaseRod("reactorberkeliumdual", 2,
                300, (float) Config.berkeliumPower, 4
        ));
        IUItem.reactorberkeliumQuad = new ItemStack(new ItemBaseRod("reactorberkeliumquad", 4,
                600, (float) Config.berkeliumPower, 4
        ));

        //
        //
        IUItem.reactoreinsteiniumSimple = new ItemStack(new ItemBaseRod("reactoreinsteiniumsimple", 1,
                180, (float) Config.einsteiniumPower, 4
        ));
        IUItem.reactoreinsteiniumDual = new ItemStack(new ItemBaseRod("reactoreinsteiniumdual", 2,
                360, (float) Config.einsteiniumPower, 4
        ));
        IUItem.reactoreinsteiniumQuad = new ItemStack(new ItemBaseRod("reactoreinsteiniumquad", 4,
                720, (float) Config.einsteiniumPower, 4
        ));

        //
        //
        IUItem.reactoruran233Simple = new ItemStack(new ItemBaseRod("reactoruran233simple", 1,
                35, (float) Config.uran233Power, 1
        ));
        IUItem.reactoruran233Dual = new ItemStack(new ItemBaseRod("reactoruran233dual", 2,
                70, (float) Config.uran233Power, 1
        ));
        IUItem.reactoruran233Quad = new ItemStack(new ItemBaseRod("reactoruran233quad", 4,
                140, (float) Config.uran233Power, 1
        ));
        IUItem.katana = new ItemKatana("katana");
        IUItem.basecircuit = new ItemBaseCircuit();
        IUItem.lens = new ItemLens();
        IUItem.paints = new ItemPaints();
        IUItem.coolpipes = new ItemCoolPipes();
        IUItem.biopipes = new ItemBioPipe();
        IUItem.nightpipes =  new ItemNightPipe();
        IUItem.amperepipes =  new ItemAmpereCable();
        IUItem.photonglass = new ItemPhotoniumGlass();
        IUItem.matter = new ItemSolidMatter();
        IUItem.spawnermodules = new ItemSpawnerModules();

        IUItem.iuingot = new ItemIngots();
        IUItem.iudust = new ItemDust();
        IUItem.nugget = new ItemNugget();
        IUItem.casing = new ItemCasing();
        IUItem.crushed = new ItemCrushed();
        IUItem.doubleplate = new ItemDoublePlate();
        IUItem.plate = new ItemPlate();
        IUItem.purifiedcrushed = new ItemPurifiedCrushed();
        IUItem.smalldust = new ItemSmallDust();
        IUItem.verysmalldust = new ItemVerySmallDust();
        IUItem.stik = new ItemSticks();
        IUItem.basemodules = new ItemBaseModules();
        IUItem.module7 = new ItemAdditionModule();
        IUItem.module5 = new ItemModuleType();
        IUItem.module6 = new ItemModuleTypePanel();
        IUItem.windmeter = new ItemWindMeter();
        IUItem.rotorupgrade_schemes = new IUItemBase("rotorupgrade_schemes");
        IUItem.spectral_box = new IUItemBase("spectral_box");
        IUItem.adv_spectral_box = new IUItemBase("adv_spectral_box");

        IUItem.coal_chunk1 = new IUItemBase("coal_chunk");
        IUItem.rawLatex = new IUItemBase("raw_latex");
        IUItem.compresscarbon = new IUItemBase("compresscarbon");
        IUItem.compressAlloy = new IUItemBase("compresscarbonultra");
        IUItem.photoniy = new IUItemBase("photoniy");
        IUItem.photoniy_ingot = new IUItemBase("photoniy_ingot");
        IUItem.compressIridiumplate = new IUItemBase("quantumitems2");
        IUItem.advQuantumtool = new IUItemBase("quantumitems3");
        IUItem.expmodule = new IUItemBase("expmodule").setCreativeTab(IUCore.ModuleTab);
        IUItem.doublecompressIridiumplate = new IUItemBase("quantumitems4");
        IUItem.nanoBox = new IUItemBase("nanobox");
        IUItem.synthetic_rubber = new IUItemBase("synthetic_rubber");
        IUItem.synthetic_plate = new IUItemBase("synthetic_plate");
        IUItem.module_schedule = new IUItemBase("module_schedule");
        IUItem.quantumtool = new IUItemBase("quantumitems6");
        IUItem.advnanobox = new IUItemBase("quantumitems7");
        IUItem.neutronium = new IUItemBase("neutronium");
        IUItem.plast = new IUItemBase("plast");
        IUItem.charged_redstone = new IUItemBase("charged_redstone");
        IUItem.charged_quartz = new IUItemBase("charged_quartz");
        IUItem.module_stack = new IUItemBase("module_stack").setCreativeTab(IUCore.ModuleTab);
        IUItem.module_quickly = new IUItemBase("module_quickly").setCreativeTab(IUCore.ModuleTab);
        IUItem.module_storage = new IUItemBase("module_storage").setCreativeTab(IUCore.ModuleTab);
        IUItem.module_infinity_water = new IUItemBase("module_infinity_water").setCreativeTab(IUCore.ModuleTab);
        IUItem.module_separate = new IUItemBase("module_separate").setCreativeTab(IUCore.ModuleTab);
        IUItem.plastic_plate = new IUItemBase("plastic_plate");

        IUItem.doublescrapBox = new IUItemBase("doublescrapbox");
        IUItem.quarrymodule = new IUItemBase("quarrymodule").setCreativeTab(IUCore.ModuleTab);
        IUItem.analyzermodule = new IUItemBase("analyzermodule").setCreativeTab(IUCore.ModuleTab);
        IUItem.peat_balls = new IUItemBase("peat_balls");
        IUItem.cultivated_peat_balls = new IUItemBase("cultivated_peat");

        IUItem.radiationresources = new RadiationResources();
        IUItem.protonshard = new ItemRadioactive("protonshard", 150, 100);
        IUItem.proton = new ItemRadioactive("proton", 150, 100);
        IUItem.toriy = new ItemRadioactive("toriy", 0, 0);

        IUItem.sunnarium = new ItemSunnarium();
        IUItem.sunnariumpanel = new ItemSunnariumPanel();

        IUItem.anode = new ItemChemistry("anode");
        IUItem.cathode = new ItemChemistry("cathode");
        IUItem.adv_anode = new ItemChemistry("adv_anode").setMaxDamage(100000);
        IUItem.adv_cathode = new ItemChemistry("adv_cathode").setMaxDamage(100000);
        IUItem.alloyscasing = new ItemAlloysCasing();
        IUItem.alloysdoubleplate = new ItemAlloysDoublePlate();
        IUItem.alloysdust = new ItemAlloysDust();
        IUItem.alloysingot = new ItemAlloysIngot();
        IUItem.alloysnugget = new ItemAlloysNugget();
        IUItem.alloysplate = new ItemAlloysPlate();
        IUItem.preciousgem = new ItemPreciousGem();


        IUItem.uranium_fuel_rod = new ItemStack(new ItemBaseRod("uranium_fuel_rod", 1,
                25, (float) 1.5, 1
        ));
        IUItem.dual_uranium_fuel_rod = new ItemStack(new ItemBaseRod("dual_uranium_fuel_rod", 2,
                50, (float) 1.5, 1
        ));
        IUItem.quad_uranium_fuel_rod = new ItemStack(new ItemBaseRod("quad_uranium_fuel_rod", 4,
                100, (float) 1.5, 1
        ));
        IUItem.mox_fuel_rod = new ItemStack(new ItemBaseRod("mox_fuel_rod", 1,
                40, (float) 3.5, 1
        ));
        IUItem.dual_mox_fuel_rod = new ItemStack(new ItemBaseRod("dual_mox_fuel_rod", 2,
                80, (float) 3.5, 1
        ));
        IUItem.quad_mox_fuel_rod = new ItemStack(new ItemBaseRod("quad_mox_fuel_rod", 4,
                160, (float) 3.5, 1
        ));
        IUItem.upgradepanelkit = new ItemUpgradePanelKit();
        IUItem.magnet = new ItemMagnet("magnet", 100000, 5000, 4, 7);
        IUItem.impmagnet = new ItemMagnet("impmagnet", 200000, 7500, 5, 11);
        IUItem.purifier = new ItemPurifier("purifier", 100000, 1000, 3);
        IUItem.entitymodules = new ItemEntityModule();


        IUItem.alloysblock = new BlocksAlloy();
        IUItem.alloysblock1 = new BlocksAlloy1();
        IUItem.block = new BlocksIngot();
        IUItem.block1 = new BlockIngots1();
        IUItem.block2 = new BlockIngots2();
        IUItem.preciousblock = new BlockPrecious();
        IUItem.preciousore = new BlockPreciousOre();
        IUItem.ore = new BlockOre();
        IUItem.ore3 = new BlockOres3();
        IUItem.heavyore = new BlockHeavyOre();
        IUItem.basaltheavyore = new BlockBasaltHeavyOre();
        IUItem.basaltheavyore1 = new BlockBasaltHeavyOre1();
        IUItem.radiationore = new BlocksRadiationOre();
        IUItem.oilblock = new BlockOil();
        IUItem.gasBlock = new BlockGas();
        IUItem.toriyore = new BlockThoriumOre();
        IUItem.itemiu = new ItemIUCrafring();
        IUItem.radiationModule = new ItemReactorModules();
        IUItem.chainsaw = new ItemEnergyInstruments(EnumTypeInstruments.CHAINSAW, EnumVarietyInstruments.CHAINSAW, "chainsaw");
        IUItem.vajra = new ItemEnergyInstruments(EnumTypeInstruments.VAJRA, EnumVarietyInstruments.VAJRA, "vajra");
        IUItem.ult_vajra = new ItemEnergyInstruments(EnumTypeInstruments.ULT_VAJRA, EnumVarietyInstruments.VAJRA, "ult_vajra");

        IUItem.drill = new ItemEnergyInstruments(EnumTypeInstruments.SIMPLE_DRILL, EnumVarietyInstruments.SIMPLE, "drill");
        IUItem.diamond_drill = new ItemEnergyInstruments(EnumTypeInstruments.DIAMOND_DRILL, EnumVarietyInstruments.DIAMOND,
                "diamond_drill"
        );

        IUItem.nanopickaxe = new ItemEnergyInstruments(EnumTypeInstruments.PICKAXE, EnumVarietyInstruments.NANO, "nanopickaxe");
        IUItem.nanoshovel = new ItemEnergyInstruments(EnumTypeInstruments.SHOVEL, EnumVarietyInstruments.NANO, "nanoshovel");
        IUItem.nanoaxe = new ItemEnergyInstruments(EnumTypeInstruments.AXE, EnumVarietyInstruments.NANO, "nanoaxe");
        IUItem.quantumpickaxe = new ItemEnergyInstruments(
                EnumTypeInstruments.PICKAXE,
                EnumVarietyInstruments.QUANTUM,
                "quantumpickaxe"
        );
        IUItem.quantumshovel = new ItemEnergyInstruments(
                EnumTypeInstruments.SHOVEL,
                EnumVarietyInstruments.QUANTUM,
                "quantumshovel"
        );
        IUItem.quantumaxe = new ItemEnergyInstruments(EnumTypeInstruments.AXE, EnumVarietyInstruments.QUANTUM, "quantumaxe");
        IUItem.spectralpickaxe = new ItemEnergyInstruments(
                EnumTypeInstruments.PICKAXE,
                EnumVarietyInstruments.SPECTRAL,
                "spectralpickaxe"
        );
        IUItem.perfect_drill = new ItemEnergyInstruments(
                EnumTypeInstruments.PERFECT_DRILL,
                EnumVarietyInstruments.PERFECT_DRILL,
                "ultDDrill"
        );
        IUItem.spectralshovel = new ItemEnergyInstruments(
                EnumTypeInstruments.SHOVEL,
                EnumVarietyInstruments.SPECTRAL,
                "spectralshovel"
        );
        IUItem.spectralaxe = new ItemEnergyInstruments(EnumTypeInstruments.AXE, EnumVarietyInstruments.SPECTRAL, "spectralaxe");
        IUItem.nano_bow = new ItemEnergyBow(
                "nano_bow",
                0,
                Config.tier_nano_bow,
                Config.transfer_nano_bow,
                Config.maxenergy_nano_bow,
                1f
        );
        IUItem.quantum_bow = new ItemEnergyBow(
                "quantum_bow",
                0,
                Config.tier_quantum_bow,
                Config.transfer_quantum_bow,
                Config.maxenergy_quantum_bow,
                2f
        );
        IUItem.spectral_bow = new ItemEnergyBow(
                "spectral_bow",
                0,
                Config.tier_spectral_bow,
                Config.transfer_spectral_bow,
                Config.maxenergy_spectral_bow,
                4f
        );
        IUItem.spectralSaber = new ItemSpectralSaber("itemNanoSaber", Config.maxCharge, Config.transferLimit, Config.tier1,
                Config.spectralsaberactive, Config.spectralsabernotactive
        );
        IUItem.quantumSaber = new ItemQuantumSaber("itemNanoSaber1", Config.maxCharge1, Config.transferLimit1,
                Config.tier1, Config.spectralsaberactive1, Config.spectralsabernotactive1
        );
        IUItem.electric_hoe = new ItemEnergyToolHoe();
        IUItem.electric_treetap = new ItemTreetapEnergy();
        IUItem.electric_wrench = new ItemToolWrenchEnergy();
        IUItem.nanosaber = new ItemNanoSaber("nano_saber", 160000, 500, 3, 19, 4);
        IUItem.spectral_helmet = new ItemSpecialArmor(
                EnumSubTypeArmor.HELMET, EnumTypeArmor.SPECTRAL
        );
        IUItem.spectral_chestplate = new ItemSpecialArmor(
                EnumSubTypeArmor.CHESTPLATE, EnumTypeArmor.SPECTRAL
        );
        IUItem.spectral_leggings = new ItemSpecialArmor(
                EnumSubTypeArmor.LEGGINGS, EnumTypeArmor.SPECTRAL
        );
        IUItem.spectral_boots = new ItemSpecialArmor(
                EnumSubTypeArmor.BOOTS, EnumTypeArmor.SPECTRAL
        );
        IUItem.nanodrill = new ItemEnergyInstruments(EnumTypeInstruments.DRILL, EnumVarietyInstruments.NANO, "nanodrill");
        IUItem.quantumdrill = new ItemEnergyInstruments(
                EnumTypeInstruments.DRILL,
                EnumVarietyInstruments.QUANTUM,
                "quantumdrill"
        );
        IUItem.spectraldrill = new ItemEnergyInstruments(
                EnumTypeInstruments.DRILL,
                EnumVarietyInstruments.SPECTRAL,
                "spectraldrill"
        );
        IUItem.advjetpack = new ItemAdvJetpack(
                "advjetpack",
                Config.adv_jetpack_maxenergy,
                Config.adv_jetpack_transfer,
                Config.adv_jetpack_tier
        );
        IUItem.impjetpack = new ItemAdvJetpack(
                "impjetpack",
                Config.imp_jetpack_maxenergy,
                Config.imp_jetpack_transfer,
                Config.imp_jetpack_tier
        );
        IUItem.perjetpack = new ItemAdvJetpack(
                "perjetpack",
                Config.per_jetpack_maxenergy,
                Config.per_jetpack_transfer,
                Config.per_jetpack_tier
        );
        IUItem.spectralSolarHelmet = new ItemSolarPanelHelmet(

                4,
                "spectral_solar_helmet"
        );
        IUItem.singularSolarHelmet = new ItemSolarPanelHelmet(

                5,
                "singular_solar_helmet"
        );
        IUItem.advancedSolarHelmet = new ItemSolarPanelHelmet(

                1,
                "advanced_solar_helmet"
        );
        IUItem.hybridSolarHelmet = new ItemSolarPanelHelmet(

                2,
                "hybrid_solar_helmet"
        );
        IUItem.ultimateSolarHelmet = new ItemSolarPanelHelmet(

                3,
                "ultimate_solar_helmet"
        );
        IUItem.adv_lappack = new ItemLappack(
                "adv_lappack",
                Config.adv_lappack_maxenergy,
                Config.adv_lappack_tier,
                Config.adv_lappack_transfer
        );
        IUItem.imp_lappack = new ItemLappack(
                "imp_lappack",
                Config.imp_lappack_maxenergy,
                Config.imp_lappack_tier,
                Config.imp_lappack_transfer
        );
        IUItem.per_lappack = new ItemLappack(
                "per_lappack",
                Config.per_lappack_maxenergy,
                Config.per_lappack_tier,
                Config.per_lappack_transfer
        );
        IUItem.machinekit = new ItemUpgradeMachinesKit();
        IUItem.circuitSpectral = new ItemStack(IUItem.basecircuit, 1, 11);
        IUItem.cirsuitQuantum = new ItemStack(IUItem.basecircuit, 1, 10);
        IUItem.circuitNano = new ItemStack(IUItem.basecircuit, 1, 9);
        IUItem.module8 = new ItemStack(IUItem.module7, 1, 10);
        IUItem.module1 = new ItemStack(IUItem.basemodules, 1, 0);
        IUItem.module2 = new ItemStack(IUItem.basemodules, 1, 3);
        IUItem.module3 = new ItemStack(IUItem.basemodules, 1, 6);
        IUItem.module4 = new ItemStack(IUItem.basemodules, 1, 9);
        IUItem.genmodule = new ItemStack(IUItem.basemodules, 1, 1);
        IUItem.genmodule1 = new ItemStack(IUItem.basemodules, 1, 2);
        IUItem.gennightmodule = new ItemStack(IUItem.basemodules, 1, 4);
        IUItem.gennightmodule1 = new ItemStack(IUItem.basemodules, 1, 5);
        IUItem.storagemodule = new ItemStack(IUItem.basemodules, 1, 7);
        IUItem.storagemodule1 = new ItemStack(IUItem.basemodules, 1, 8);
        IUItem.outputmodule = new ItemStack(IUItem.basemodules, 1, 10);
        IUItem.outputmodule1 = new ItemStack(IUItem.basemodules, 1, 11);
        IUItem.phase_module = new ItemStack(IUItem.basemodules, 1, 12);
        IUItem.phase_module1 = new ItemStack(IUItem.basemodules, 1, 13);
        IUItem.phase_module2 = new ItemStack(IUItem.basemodules, 1, 14);
        IUItem.moonlinse_module = new ItemStack(IUItem.basemodules, 1, 15);
        IUItem.moonlinse_module1 = new ItemStack(IUItem.basemodules, 1, 16);
        IUItem.moonlinse_module2 = new ItemStack(IUItem.basemodules, 1, 17);
        IUItem.copperCableItem = new ItemStack(IUItem.cable, 1, 11);
        IUItem.insulatedCopperCableItem = new ItemStack(IUItem.cable, 1, 12);
        IUItem.glassFiberCableItem = new ItemStack(IUItem.cable, 1, 13);
        IUItem.goldCableItem = new ItemStack(IUItem.cable, 1, 14);
        IUItem.insulatedGoldCableItem = new ItemStack(IUItem.cable, 1, 15);
        IUItem.ironCableItem = new ItemStack(IUItem.cable, 1, 16);
        IUItem.insulatedIronCableItem = new ItemStack(IUItem.cable, 1, 17);
        IUItem.tinCableItem = new ItemStack(IUItem.cable, 1, 18);
        IUItem.insulatedTinCableItem = new ItemStack(IUItem.cable, 1, 19);


       // IUItem.book = new ItemBook("book_iu");
        IUItem.nano_boots = new ItemSpecialArmor(EnumSubTypeArmor.BOOTS, EnumTypeArmor.NANO);
        IUItem.nano_chestplate = new ItemSpecialArmor(EnumSubTypeArmor.CHESTPLATE, EnumTypeArmor.NANO);
        IUItem.nano_helmet = new ItemSpecialArmor(EnumSubTypeArmor.HELMET, EnumTypeArmor.NANO);
        IUItem.nano_leggings = new ItemSpecialArmor(EnumSubTypeArmor.LEGGINGS, EnumTypeArmor.NANO);
        IUItem.nightvision = new ItemArmorNightvisionGoggles();
        IUItem.quantum_boots = new ItemSpecialArmor(EnumSubTypeArmor.BOOTS, EnumTypeArmor.QUANTUM);
        IUItem.quantum_chestplate = new ItemSpecialArmor(EnumSubTypeArmor.CHESTPLATE, EnumTypeArmor.QUANTUM);
        IUItem.quantum_helmet = new ItemSpecialArmor(EnumSubTypeArmor.HELMET, EnumTypeArmor.QUANTUM);
        IUItem.quantum_leggings = new ItemSpecialArmor(EnumSubTypeArmor.LEGGINGS, EnumTypeArmor.QUANTUM);


        IUItem.electricHoe = IUItem.electric_hoe.getItemStack();

        IUItem.denseplateadviron = new ItemStack(IUItem.doubleplate, 1, 26);
        IUItem.platecopper = new ItemStack(IUItem.plate, 1, 20);
        IUItem.platetin = new ItemStack(IUItem.plate, 1, 27);
        IUItem.platebronze = new ItemStack(IUItem.plate, 1, 19);
        IUItem.plategold = new ItemStack(IUItem.plate, 1, 21);
        IUItem.plateiron = new ItemStack(IUItem.plate, 1, 22);
        IUItem.platelead = new ItemStack(IUItem.plate, 1, 24);
        IUItem.platelapis = new ItemStack(IUItem.plate, 1, 23);
        IUItem.plateobsidian = new ItemStack(IUItem.plate, 1, 25);
        IUItem.plateadviron = new ItemStack(IUItem.plate, 1, 26);
        IUItem.denseplatecopper = new ItemStack(IUItem.doubleplate, 1, 20);
        IUItem.denseplatetin = new ItemStack(IUItem.doubleplate, 1, 27);
        IUItem.denseplatebronze = new ItemStack(IUItem.doubleplate, 1, 19);
        IUItem.denseplategold = new ItemStack(IUItem.doubleplate, 1, 21);
        IUItem.denseplateiron = new ItemStack(IUItem.doubleplate, 1, 22);
        IUItem.denseplatelead = new ItemStack(IUItem.doubleplate, 1, 24);
        IUItem.denseplatelapi = new ItemStack(IUItem.doubleplate, 1, 23);
        IUItem.denseplateobsidian = new ItemStack(IUItem.doubleplate, 1, 25);
        IUItem.coolantCell = ModUtils.getCellFromFluid(FluidName.fluidcoolant.getInstance());
        IUItem.hotcoolantCell = ModUtils.getCellFromFluid(FluidName.fluidhot_coolant.getInstance());
        IUItem.copperOre = IUItem.classic_ore.getItemStack(BlockClassicOre.Type.copper);
        IUItem.tinOre = IUItem.classic_ore.getItemStack(BlockClassicOre.Type.tin);
        IUItem.uraniumOre = IUItem.classic_ore.getItemStack(BlockClassicOre.Type.uranium);
        IUItem.leadOre = IUItem.classic_ore.getItemStack(BlockClassicOre.Type.lead);
        IUItem.reinforcedStone = IUItem.blockResource.getItemStack(BlockResource.Type.reinforced_stone);
        IUItem.reinforcedGlass = new ItemStack(IUItem.glass);
        IUItem.bronzeBlock = IUItem.blockResource.getItemStack(BlockResource.Type.bronze_block);
        IUItem.copperBlock = IUItem.blockResource.getItemStack(BlockResource.Type.copper_block);
        IUItem.tinBlock = IUItem.blockResource.getItemStack(BlockResource.Type.tin_block);
        IUItem.uraniumBlock = IUItem.blockResource.getItemStack(BlockResource.Type.uranium_block);
        IUItem.leadBlock = IUItem.blockResource.getItemStack(BlockResource.Type.lead_block);
        IUItem.advironblock = IUItem.blockResource.getItemStack(BlockResource.Type.steel_block);
        IUItem.machine = IUItem.blockResource.getItemStack(BlockResource.Type.machine);
        IUItem.advancedMachine = IUItem.blockResource.getItemStack(BlockResource.Type.advanced_machine);


        IUItem.UranFuel = IUItem.nuclear_res.getItemStack(ItemNuclearResource.Types.uranium);
        IUItem.Plutonium = IUItem.nuclear_res.getItemStack(ItemNuclearResource.Types.plutonium);
        IUItem.smallPlutonium = IUItem.nuclear_res.getItemStack(ItemNuclearResource.Types.small_plutonium);
        IUItem.Uran235 = IUItem.nuclear_res.getItemStack(ItemNuclearResource.Types.uranium_235);
        IUItem.smallUran235 = IUItem.nuclear_res.getItemStack(ItemNuclearResource.Types.small_uranium_235);
        IUItem.Uran238 = IUItem.nuclear_res.getItemStack(ItemNuclearResource.Types.uranium_238);
        IUItem.mox = IUItem.nuclear_res.getItemStack(ItemNuclearResource.Types.mox);
        IUItem.latex = new ItemStack(IUItem.crafting_elements, 1, 290);
        IUItem.rubber = new ItemStack(IUItem.crafting_elements, 1, 271);
        IUItem.elemotor = new ItemStack(IUItem.crafting_elements, 1, 276);
        IUItem.powerunit = new ItemStack(IUItem.crafting_elements, 1, 279);
        IUItem.powerunitsmall = new ItemStack(IUItem.crafting_elements, 1, 278);
        IUItem.heatconducto = new ItemStack(IUItem.crafting_elements, 1, 277);
        IUItem.cfPowder = new ItemStack(IUItem.crafting_elements, 1, 289);
        IUItem.electronicCircuit = new ItemStack(IUItem.crafting_elements, 1, 272);
        IUItem.advancedCircuit = new ItemStack(IUItem.crafting_elements, 1, 273);
        IUItem.advancedAlloy = new ItemStack(IUItem.crafting_elements, 1, 274);
        IUItem.iridiumOre = new ItemStack(IUItem.crafting_elements, 1, 275);
        IUItem.iridiumShard = new ItemStack(IUItem.crafting_elements, 1, 291);
        IUItem.scrap = new ItemStack(IUItem.crafting_elements, 1, 287);
        IUItem.coalBall = new ItemStack(IUItem.crafting_elements, 1, 283);
        IUItem.carbonPlate = new ItemStack(IUItem.crafting_elements, 1, 282);
        IUItem.carbonMesh = new ItemStack(IUItem.crafting_elements, 1, 281);
        IUItem.carbonFiber = new ItemStack(IUItem.crafting_elements, 1, 280);
        IUItem.scrapBox = new ItemStack(IUItem.crafting_elements, 1, 288);
        IUItem.iridiumPlate = new ItemStack(IUItem.crafting_elements, 1, 285);
        IUItem.coal_chunk = new ItemStack(IUItem.crafting_elements, 1, 286);
        IUItem.rawcrystalmemory = new ItemStack(IUItem.crafting_elements, 1, 292);
        IUItem.biochaff = new ItemStack(IUItem.crafting_elements, 1, 293);
        IUItem.casingadviron = new ItemStack(IUItem.casing, 1, 24);
        IUItem.casingcopper = new ItemStack(IUItem.casing, 1, 20);
        IUItem.casingtin = new ItemStack(IUItem.casing, 1, 25);
        IUItem.casingbronze = new ItemStack(IUItem.casing, 1, 19);
        IUItem.casinggold = new ItemStack(IUItem.casing, 1, 21);
        IUItem.casingiron = new ItemStack(IUItem.casing, 1, 22);
        IUItem.casinglead = new ItemStack(IUItem.casing, 1, 23);
        IUItem.crushedIronOre = new ItemStack(IUItem.crushed, 1, 21);
        IUItem.crushedCopperOre = new ItemStack(IUItem.crushed, 1, 19);
        IUItem.crushedGoldOre = new ItemStack(IUItem.crushed, 1, 20);
        IUItem.crushedTinOre = new ItemStack(IUItem.crushed, 1, 23);
        IUItem.crushedUraniumOre = new ItemStack(IUItem.crushed, 1, 24);
        IUItem.crushedLeadOre = new ItemStack(IUItem.crushed, 1, 22);
        IUItem.purifiedCrushedIronOre = new ItemStack(IUItem.purifiedcrushed, 1, 21);
        IUItem.purifiedCrushedCopperOre = new ItemStack(IUItem.purifiedcrushed, 1, 19);
        IUItem.purifiedCrushedGoldOre = new ItemStack(IUItem.purifiedcrushed, 1, 20);
        IUItem.purifiedCrushedTinOre = new ItemStack(IUItem.purifiedcrushed, 1, 23);
        IUItem.purifiedCrushedUraniumOre = new ItemStack(IUItem.purifiedcrushed, 1, 24);
        IUItem.purifiedCrushedLeadOre = new ItemStack(IUItem.purifiedcrushed, 1, 22);
        IUItem.stoneDust = new ItemStack(IUItem.iudust, 1, 30);
        IUItem.energiumDust = new ItemStack(IUItem.iudust, 1, 24);
        IUItem.bronzeDust = new ItemStack(IUItem.iudust, 1, 19);
        IUItem.clayDust = new ItemStack(IUItem.iudust, 1, 20);
        IUItem.coalDust = new ItemStack(IUItem.iudust, 1, 21);
        IUItem.copperDust = new ItemStack(IUItem.iudust, 1, 22);
        IUItem.goldDust = new ItemStack(IUItem.iudust, 1, 25);
        IUItem.ironDust = new ItemStack(IUItem.iudust, 1, 26);
        IUItem.tinDust = new ItemStack(IUItem.iudust, 1, 32);
        IUItem.leadDust = new ItemStack(IUItem.iudust, 1, 28);
        IUItem.obsidianDust = new ItemStack(IUItem.iudust, 1, 29);
        IUItem.lapiDust = new ItemStack(IUItem.iudust, 1, 27);
        IUItem.sulfurDust = new ItemStack(IUItem.iudust, 1, 31);
        IUItem.silicondioxideDust = new ItemStack(IUItem.iudust, 1, 33);
        IUItem.diamondDust = new ItemStack(IUItem.iudust, 1, 23);
        IUItem.smallIronDust = new ItemStack(IUItem.smalldust, 1, 22);
        IUItem.smallCopperDust = new ItemStack(IUItem.smalldust, 1, 20);
        IUItem.smallGoldDust = new ItemStack(IUItem.smalldust, 1, 21);
        IUItem.smallTinDust = new ItemStack(IUItem.smalldust, 1, 27);
        IUItem.smallLeadDust = new ItemStack(IUItem.smalldust, 1, 24);
        IUItem.smallSulfurDust = new ItemStack(IUItem.smalldust, 1, 26);
        IUItem.smallObsidian = new ItemStack(IUItem.smalldust, 1, 25);
        IUItem.mixedMetalIngot = new ItemStack(IUItem.iuingot, 1, 19);
        IUItem.copperIngot = new ItemStack(IUItem.iuingot, 1, 21);
        IUItem.tinIngot = new ItemStack(IUItem.iuingot, 1, 24);
        IUItem.bronzeIngot = new ItemStack(IUItem.iuingot, 1, 20);
        IUItem.leadIngot = new ItemStack(IUItem.iuingot, 1, 22);
        IUItem.advIronIngot = new ItemStack(IUItem.iuingot, 1, 23);
        IUItem.coil = new ItemStack(IUItem.crafting_elements, 1, 294);
        IUItem.plantBall = new ItemStack(IUItem.crafting_elements, 1, 295);
        IUItem.tinCan = new ItemStack(IUItem.crafting_elements, 1, 296);
        IUItem.compressedCoalBall = new ItemStack(IUItem.crafting_elements, 1, 297);
        IUItem.copperboiler = new ItemStack(IUItem.crafting_elements, 1, 284);
        IUItem.reactorCoolantSimple = new ItemStack(IUItem.crafting_elements, 1, 298);
        IUItem.reactorCoolantTriple = new ItemStack(IUItem.crafting_elements, 1, 299);
        IUItem.reactorCoolantSix = new ItemStack(IUItem.crafting_elements, 1, 300);

        IUItem.componentVent = new ItemComponentVent("component_vent", 1, 3);
        IUItem.adv_componentVent = new ItemComponentVent("adv_component_vent", 2, 4);
        IUItem.imp_componentVent = new ItemComponentVent("imp_component_vent", 3, 5);
        IUItem.per_componentVent = new ItemComponentVent("per_component_vent", 4, 6);

        IUItem.vent = new ItemReactorVent("vent", 2500, 1, 8, 0.9, 4);
        IUItem.adv_Vent = new ItemReactorVent("adv_vent", 4000, 2, 10, 0.85, 7);
        IUItem.imp_Vent = new ItemReactorVent("imp_vent", 5000, 3, 14, 0.8, 11);
        IUItem.per_Vent = new ItemReactorVent("per_vent", 6000, 4, 20, 0.75, 15);

        IUItem.reactor_plate = new ItemReactorPlate("reactor_plate", 1, 2);
        IUItem.adv_reactor_plate = new ItemReactorPlate("adv_reactor_plate", 2, 1.5);
        IUItem.imp_reactor_plate = new ItemReactorPlate("imp_reactor_plate", 3, 1.25);
        IUItem.per_reactor_plate = new ItemReactorPlate("per_reactor_plate", 4, 1);

        IUItem.heat_exchange = new ItemReactorHeatExchanger("heat_exchange", 2500, 1, 10, 0.8);
        IUItem.adv_heat_exchange = new ItemReactorHeatExchanger("adv_heat_exchange", 5000, 2, 12, 0.75);
        IUItem.imp_heat_exchange = new ItemReactorHeatExchanger("imp_heat_exchange", 7500, 3, 15, 0.6);
        IUItem.per_heat_exchange = new ItemReactorHeatExchanger("per_heat_exchange", 10000, 4, 20, 0.45);

        IUItem.proton_energy_coupler = new ItemEnergyCoupler("proton_energy_coupler", (int) (3600*2.5), 1, 0.05);
        IUItem.adv_proton_energy_coupler = new ItemEnergyCoupler("adv_proton_energy_coupler", (int) (7200*2.5), 2, 0.1);
        IUItem.imp_proton_energy_coupler = new ItemEnergyCoupler("imp_proton_energy_coupler", 10800*3, 3, 0.15);
        IUItem.per_proton_energy_coupler = new ItemEnergyCoupler("per_proton_energy_coupler", (int) (14400*3.5), 4, 0.2);

        IUItem.neutron_protector = new ItemNeutronProtector("neutron_protector", 3600*4, 1);
        IUItem.adv_neutron_protector = new ItemNeutronProtector("adv_neutron_protector", 7200*6, 2);
        IUItem.imp_neutron_protector = new ItemNeutronProtector("imp_neutron_protector", (int) (10800*6), 3);
        IUItem.per_neutron_protector = new ItemNeutronProtector("per_neutron_protector", (int) (14400*6), 4);


        IUItem.capacitor = new ItemReactorCapacitor("capacitor", 25000, 1, 4);
        IUItem.adv_capacitor = new ItemReactorCapacitor("adv_capacitor", 50000, 2, 6);
        IUItem.imp_capacitor = new ItemReactorCapacitor("imp_capacitor", 100000, 3, 8);
        IUItem.per_capacitor = new ItemReactorCapacitor("per_capacitor", 200000, 4, 12);

        IUItem.coolant = new ItemReactorCoolant("coolant", 1, 100000, 2);
        IUItem.adv_coolant = new ItemReactorCoolant("adv_coolant", 2, 250000, 4);
        IUItem.imp_coolant = new ItemReactorCoolant("imp_coolant", 3, 500000, 7);


        IUItem.pump = new ItemsPumps("pump", 2000, 0, 1, 2);
        IUItem.adv_pump = new ItemsPumps("adv_pump", 5000, 1, 2, 4);
        IUItem.imp_pump = new ItemsPumps("imp_pump", 7500, 2, 4, 9);
        IUItem.per_pump = new ItemsPumps("per_pump", 10000, 3, 6, 15);

        IUItem.fan = new ItemsFan("fan", 2000, 0, 1, 2);
        IUItem.adv_fan = new ItemsFan("adv_fan", 5000, 1, 2, 4);
        IUItem.imp_fan = new ItemsFan("imp_fan", 7500, 2, 4, 9);
        IUItem.per_fan = new ItemsFan("per_fan", 10000, 3, 6, 15);


        IUItem.simple_capacitor_item = new ItemCapacitor("simple_capacitor", 0, 0.02, 10000);
        IUItem.adv_capacitor_item = new ItemCapacitor("adv_capacitor", 1, 0.04, 15000);
        IUItem.imp_capacitor_item = new ItemCapacitor("imp_capacitor", 2, 0.08, 20000);
        IUItem.per_capacitor_item = new ItemCapacitor("per_capacitor", 3, 0.12, 30000);


        IUItem.simple_exchanger_item = new ItemExchanger("simple_exchanger", 0, 0.05, 10000);
        IUItem.adv_exchanger_item = new ItemExchanger("adv_exchanger", 1, 0.1, 15000);
        IUItem.imp_exchanger_item = new ItemExchanger("imp_exchanger", 2, 0.15, 20000);
        IUItem.per_exchanger_item = new ItemExchanger("per_exchanger", 3, 0.2, 30000);
    }

    private static void registerfluid(
            FluidName name, int density,
            int temperature, boolean isGaseous
    ) {
        Material steam = new MaterialLiquid(MapColor.SILVER);
        Fluid fluid =
                (new IUFluid(name))
                        .setDensity(density)
                        .setTemperature(temperature)
                        .setGaseous(isGaseous);
        FluidRegistry.registerFluid(fluid);
        if (!fluid.canBePlacedInWorld()) {
            Block block = new BlockIUFluid(name, fluid, steam);
            fluid.setBlock(block);
            fluid.setUnlocalizedName(block.getUnlocalizedName().substring(3));
        }

        name.setInstance(fluid);
        if (name != FluidName.fluidlava && name != FluidName.fluidwater) {
            FluidRegistry.addBucketForFluid(fluid);
        }
    }

    public static <T extends Item> T registerItem(T item, ResourceLocation rl) {
        item.setRegistryName(rl);
        return registerItem(item);
    }

    public static <T extends Item> T registerItem(T item) {
        ForgeRegistries.ITEMS.register(item);
        return item;
    }

    public static <T extends Block> T registerBlock(T item, ResourceLocation rl) {
        item.setRegistryName(rl);
        return registerBlock(item);
    }

    public static <T extends Block> T registerBlock(T item) {
        ForgeRegistries.BLOCKS.register(item);
        return item;
    }

}
