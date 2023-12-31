package com.denfop.register;

import com.denfop.Config;
import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.blocks.BlockAnvil;
import com.denfop.blocks.BlockClassicOre;
import com.denfop.blocks.BlockDeposits;
import com.denfop.blocks.BlockDeposits1;
import com.denfop.blocks.BlockFoam;
import com.denfop.blocks.BlockHeavyOre;
import com.denfop.blocks.BlockIUFluid;
import com.denfop.blocks.BlockIngots1;
import com.denfop.blocks.BlockNuclearBomb;
import com.denfop.blocks.BlockOil;
import com.denfop.blocks.BlockOre;
import com.denfop.blocks.BlockOres2;
import com.denfop.blocks.BlockPrecious;
import com.denfop.blocks.BlockPreciousOre;
import com.denfop.blocks.BlockResource;
import com.denfop.blocks.BlockRubWood;
import com.denfop.blocks.BlockTexGlass;
import com.denfop.blocks.BlockThoriumOre;
import com.denfop.blocks.BlocksAlloy;
import com.denfop.blocks.BlocksIngot;
import com.denfop.blocks.BlocksRadiationOre;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.IUFluid;
import com.denfop.blocks.IULeaves;
import com.denfop.blocks.IUSapling;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.mechanism.BlockAdminPanel;
import com.denfop.blocks.mechanism.BlockAdvRefiner;
import com.denfop.blocks.mechanism.BlockAdvSolarEnergy;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.blocks.mechanism.BlockBlastFurnace;
import com.denfop.blocks.mechanism.BlockCable;
import com.denfop.blocks.mechanism.BlockChargepadStorage;
import com.denfop.blocks.mechanism.BlockCombinerSolid;
import com.denfop.blocks.mechanism.BlockConverterMatter;
import com.denfop.blocks.mechanism.BlockCoolPipes;
import com.denfop.blocks.mechanism.BlockDoubleMolecularTransfomer;
import com.denfop.blocks.mechanism.BlockEarthQuarry;
import com.denfop.blocks.mechanism.BlockEnergyStorage;
import com.denfop.blocks.mechanism.BlockExpCable;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.blocks.mechanism.BlockHeatColdPipes;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import com.denfop.blocks.mechanism.BlockImpSolarEnergy;
import com.denfop.blocks.mechanism.BlockItemPipes;
import com.denfop.blocks.mechanism.BlockMolecular;
import com.denfop.blocks.mechanism.BlockMoreMachine;
import com.denfop.blocks.mechanism.BlockMoreMachine1;
import com.denfop.blocks.mechanism.BlockMoreMachine2;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.blocks.mechanism.BlockPetrolQuarry;
import com.denfop.blocks.mechanism.BlockPipes;
import com.denfop.blocks.mechanism.BlockQCable;
import com.denfop.blocks.mechanism.BlockQuarryVein;
import com.denfop.blocks.mechanism.BlockRadPipes;
import com.denfop.blocks.mechanism.BlockRefiner;
import com.denfop.blocks.mechanism.BlockSCable;
import com.denfop.blocks.mechanism.BlockSimpleMachine;
import com.denfop.blocks.mechanism.BlockSintezator;
import com.denfop.blocks.mechanism.BlockSolarEnergy;
import com.denfop.blocks.mechanism.BlockSolarPanels;
import com.denfop.blocks.mechanism.BlockSolidMatter;
import com.denfop.blocks.mechanism.BlockSunnariumMaker;
import com.denfop.blocks.mechanism.BlockSunnariumPanelMaker;
import com.denfop.blocks.mechanism.BlockTank;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.blocks.mechanism.BlockUniversalCable;
import com.denfop.blocks.mechanism.BlockUpgradeBlock;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;
import com.denfop.integration.exnihilo.ExNihiloIntegration;
import com.denfop.integration.exnihilo.blocks.DustBlocks;
import com.denfop.integration.exnihilo.blocks.GravelBlocks;
import com.denfop.integration.exnihilo.blocks.SandBlocks;
import com.denfop.integration.exnihilo.items.ItemDustCrushed;
import com.denfop.integration.exnihilo.items.ItemGravelCrushed;
import com.denfop.integration.exnihilo.items.ItemSandCrushed;
import com.denfop.items.IUItemBase;
import com.denfop.items.ItemBaseCircuit;
import com.denfop.items.ItemCell;
import com.denfop.items.ItemChemistry;
import com.denfop.items.ItemCore;
import com.denfop.items.ItemCoreWater;
import com.denfop.items.ItemCoreWind;
import com.denfop.items.ItemCrystalMemory;
import com.denfop.items.ItemEFReader;
import com.denfop.items.ItemExcitedNucleus;
import com.denfop.items.ItemFacadeItem;
import com.denfop.items.ItemFeature;
import com.denfop.items.ItemFrequencyTransmitter;
import com.denfop.items.ItemLens;
import com.denfop.items.ItemPaints;
import com.denfop.items.ItemPhotoniumGlass;
import com.denfop.items.ItemRotorsUpgrade;
import com.denfop.items.ItemSolidMatter;
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
import com.denfop.items.armour.ItemLappack;
import com.denfop.items.armour.ItemSolarPanelHelmet;
import com.denfop.items.armour.special.EnumSubTypeArmor;
import com.denfop.items.armour.special.EnumTypeArmor;
import com.denfop.items.armour.special.ItemSpecialArmor;
import com.denfop.items.bags.ItemEnergyBags;
import com.denfop.items.bags.ItemLeadBox;
import com.denfop.items.book.ItemBook;
import com.denfop.items.energy.ItemBattery;
import com.denfop.items.energy.ItemBatterySU;
import com.denfop.items.energy.ItemEnergyBow;
import com.denfop.items.energy.ItemEnergyToolHoe;
import com.denfop.items.energy.ItemGraviTool;
import com.denfop.items.energy.ItemKatana;
import com.denfop.items.energy.ItemMagnet;
import com.denfop.items.energy.ItemNanoSaber;
import com.denfop.items.energy.ItemPurifier;
import com.denfop.items.energy.ItemQuantumSaber;
import com.denfop.items.energy.ItemSpectralSaber;
import com.denfop.items.energy.ItemSprayer;
import com.denfop.items.energy.ItemToolWrench;
import com.denfop.items.energy.ItemToolWrenchEnergy;
import com.denfop.items.energy.ItemTreetap;
import com.denfop.items.energy.ItemTreetapEnergy;
import com.denfop.items.energy.ItemWindMeter;
import com.denfop.items.energy.instruments.EnumTypeInstruments;
import com.denfop.items.energy.instruments.EnumVarietyInstruments;
import com.denfop.items.energy.instruments.ItemEnergyInstruments;
import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.items.modules.ItemBaseModules;
import com.denfop.items.modules.ItemCoolingUpgrade;
import com.denfop.items.modules.ItemEntityModule;
import com.denfop.items.modules.ItemModuleType;
import com.denfop.items.modules.ItemModuleTypePanel;
import com.denfop.items.modules.ItemQuarryModule;
import com.denfop.items.modules.ItemReactorModules;
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
import com.denfop.items.reactors.ItemExchanger;
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
import com.denfop.items.transport.ItemCable;
import com.denfop.items.transport.ItemCoolPipes;
import com.denfop.items.transport.ItemExpCable;
import com.denfop.items.transport.ItemHeatColdPipes;
import com.denfop.items.transport.ItemItemPipes;
import com.denfop.items.transport.ItemPipes;
import com.denfop.items.transport.ItemQCable;
import com.denfop.items.transport.ItemRadCable;
import com.denfop.items.transport.ItemSCable;
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
        registerfluid(FluidName.fluidNeutron, 3000, 300, false);
        registerfluid(FluidName.fluidHelium, -1000, 300, true);
        registerfluid(FluidName.fluidbenz, 3000, 500, false);
        registerfluid(FluidName.fluiddizel, 3000, 500, false);
        registerfluid(FluidName.fluidneft, 3000, 500, false);
        registerfluid(FluidName.fluidpolyeth, -3000, 2000, true);
        registerfluid(FluidName.fluidpolyprop, -3000, 2000, true);
        registerfluid(FluidName.fluidoxy, -3000, 500, true);

        registerfluid(FluidName.fluidhyd, -3000, 500, true);
        registerfluid(FluidName.fluidazot, -3000, 500, true);
        registerfluid(FluidName.fluidco2, -3000, 500, true);
        registerfluid(FluidName.fluidgas, -3000, 500, true);
        registerfluid(FluidName.fluidchlorum, -3000, 500, true);
        registerfluid(FluidName.fluidbromine, -3000, 500, true);
        registerfluid(FluidName.fluidiodine, 3000, 500, true);

        registerfluid(FluidName.fluiduu_matter, 3000, 3000, false);
        registerfluid(FluidName.fluidconstruction_foam, 10000, 50000, false);
        registerfluid(FluidName.fluidcoolant, 1000, 3000, false);
        registerfluid(FluidName.fluidhot_coolant, 1000, 3000, false);
        registerfluid(FluidName.fluidpahoehoe_lava, 50000, 250000, false);
        registerfluid(FluidName.fluidbiomass, 1000, 3000, false);
        registerfluid(FluidName.fluidbiogas, 1000, 3000, true);
        registerfluid(FluidName.fluiddistilled_water, 1000, 1000, false);
        registerfluid(FluidName.fluidsuperheated_steam, -3000, 100, true);
        registerfluid(FluidName.fluidsteam, -800, 300, true);
        registerfluid(FluidName.fluidhot_water, 1000, 1000, false);
        registerfluid(FluidName.fluidair, 0, 500, true);
        IUItem.invalid = TileBlockCreator.instance.create(MultiTileBlock.class);
        IUItem.water_reactors_component = TileBlockCreator.instance.create(BlockWaterReactors.class);

        IUItem.blockdeposits = new BlockDeposits();
        IUItem.blockdeposits1 = new BlockDeposits1();
        IUItem.ore2 = new BlockOres2();
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
        IUItem.expcableblock = TileBlockCreator.instance.create(BlockExpCable.class);
        IUItem.earthQuarry = TileBlockCreator.instance.create(BlockEarthQuarry.class);
        IUItem.anvil = TileBlockCreator.instance.create(BlockAnvil.class);

        IUItem.graphite_reactor = TileBlockCreator.instance.create(BlocksGraphiteReactors.class);
        IUItem.gas_reactor = TileBlockCreator.instance.create(BlockGasReactor.class);
        IUItem.heat_reactor = TileBlockCreator.instance.create(BlockHeatReactor.class);
        IUItem.blastfurnace = TileBlockCreator.instance.create(BlockBlastFurnace.class);
        IUItem.coolpipesblock = TileBlockCreator.instance.create(BlockCoolPipes.class);
        IUItem.rad_pipesBlock = TileBlockCreator.instance.create(BlockRadPipes.class);

        IUItem.tank = TileBlockCreator.instance.create(BlockTank.class);
        IUItem.convertersolidmatter = TileBlockCreator.instance.create(BlockConverterMatter.class);
        IUItem.heatcoolpipesblock = TileBlockCreator.instance.create(BlockHeatColdPipes.class);
        IUItem.universalcableblock = TileBlockCreator.instance.create(BlockUniversalCable.class);
        IUItem.crystalMemory = new ItemCrystalMemory();
        IUItem.solar_day_glass = new ItemDaySolarPanelGlass();
        IUItem.solar_night_glass = new ItemNightSolarPanelGlass();
        IUItem.solar_night_day_glass = new ItemDayNightSolarPanelGlass();
        IUItem.solar_battery = new ItemBatterySolarPanel();
        IUItem.solar_output = new ItemOutputSolarPanel();
        IUItem.cell_all = new ItemCell();
        IUItem.FluidCell = new ItemStack(IUItem.cell_all);
        IUItem.ForgeHammer = new ItemToolHammer();
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
        IUItem.nuclear_bomb = new BlockNuclearBomb();
        IUItem.hazmat_chestplate = new ItemArmorHazmat("hazmat_chestplate", EntityEquipmentSlot.CHEST);
        IUItem.hazmat_helmet = new ItemArmorHazmat("hazmat_helmet", EntityEquipmentSlot.HEAD);
        IUItem.hazmat_leggings = new ItemArmorHazmat("hazmat_leggings", EntityEquipmentSlot.LEGS);
        IUItem.rubber_boots = new ItemArmorHazmat("rubber_boots", EntityEquipmentSlot.FEET);
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
        IUItem.nuclear_res = new ItemNuclearResource();
        IUItem.rotors_upgrade = new ItemRotorsUpgrade();
        IUItem.rubWood = new BlockRubWood();
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
        //TODO:    IUItem.blueprint = new ItemBlueprint();
        IUItem.neutroniumingot = new IUItemBase("neutroniumingot");
        IUItem.connect_item = new IUItemBase("connect_item");
        IUItem.upgrade_speed_creation = (IUItemBase) new IUItemBase("upgrade_speed_creation").setCreativeTab(IUCore.ModuleTab);
        IUItem.core = new ItemCore();
        UpgradeSystem.system.addModification();
        IUItem.corewind = new ItemCoreWind();
        IUItem.autoheater = (IUItemBase) new IUItemBase("autoheater").setCreativeTab(IUCore.ModuleTab);
        IUItem.coolupgrade = new ItemCoolingUpgrade();
        IUItem.upgrademodule = new ItemUpgradeModule();
        IUItem.module9 = new ItemQuarryModule();
        IUItem.leadbox = new ItemLeadBox("lead_box");
        IUItem.windrod = new ItemWindRod();
        IUItem.qcable = new ItemQCable();
        IUItem.scable = new ItemSCable();
        IUItem.expcable = new ItemExpCable();
        IUItem.impBatChargeCrystal = new ItemBattery("itemadvbatchargecrystal", Config.Storagequantumsuit, 32368D, 5, true);
        IUItem.perBatChargeCrystal = new ItemBattery("itembatchargecrystal", Config.Storagequantumsuit * 4, 129472D, 6, true);
        IUItem.reBattery = new ItemBattery("re_battery", 10000.0, 100.0, 1);
        IUItem.energy_crystal = new ItemBattery("energy_crystal", 1000000.0, 2048.0, 3);
        IUItem.lapotron_crystal = new ItemBattery("lapotron_crystal", 1.0E7, 8092.0, 4);
        IUItem.charging_re_battery = new ItemBattery("charging_re_battery", 40000.0, 128.0, 1, true);
        IUItem.advanced_charging_re_battery = new ItemBattery("advanced_charging_re_battery", 400000.0, 1024.0, 2, true);
        IUItem.charging_energy_crystal = new ItemBattery("charging_energy_crystal", 4000000.0, 8192.0, 3, true);
        IUItem.charging_lapotron_crystal = new ItemBattery("charging_lapotron_crystal", 4.0E7, 32768.0, 4, true);
        IUItem.advBattery = new ItemBattery("advanced_re_battery", 100000.0, 256.0, 2);
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



        IUItem.reactorprotonSimple = new ItemStack(new ItemBaseRod("reactorprotonsimple", 1,
                95, Config.ProtonPower,3
        ));
        IUItem.reactorprotonDual = new ItemStack(new ItemBaseRod("reactorprotondual", 2,
                190, Config.ProtonPower,3
        ));
        IUItem.reactorprotonQuad = new ItemStack(new ItemBaseRod("reactorprotonquad", 4,
                380, Config.ProtonPower,3
        ));


        //
       IUItem.reactortoriySimple = new ItemStack(new ItemBaseRod("reactortoriysimple", 1,
               50, Config.toriyPower,2
        ));
        IUItem.reactortoriyDual = new ItemStack(new ItemBaseRod("reactortoriydual", 2,
                100, Config.toriyPower,2
        ));
        IUItem.reactortoriyQuad = new ItemStack(new ItemBaseRod("reactortoriyquad", 4,
                200, Config.toriyPower,2
        ));

        //
//
        IUItem.reactoramericiumSimple = new ItemStack(new ItemBaseRod("reactoramericiumsimple", 1,
                80, (float) Config.americiumPower,2
        ));
        IUItem.reactoramericiumDual = new ItemStack(new ItemBaseRod("reactoramericiumdual", 2,
                160, (float) Config.americiumPower,2
        ));
        IUItem.reactoramericiumQuad = new ItemStack(new ItemBaseRod("reactoramericiumquad", 4,
                320, (float) Config.americiumPower,2
        ));

        //
        //
          IUItem.reactorneptuniumSimple = new ItemStack(new ItemBaseRod("reactorneptuniumsimple", 1,
                  65, (float) Config.neptuniumPower,2
        ));
        IUItem.reactorneptuniumDual = new ItemStack(new ItemBaseRod("reactorneptuniumdual", 2,
                130, (float) Config.neptuniumPower,2
        ));
        IUItem.reactorneptuniumQuad = new ItemStack(new ItemBaseRod("reactorneptuniumquad", 4,
                260, (float) Config.neptuniumPower,2
        ));

        //
        IUItem.reactorcuriumSimple = new ItemStack(new ItemBaseRod("reactorcuriumsimple", 1,
                100, (float) Config.curiumPower,3
        ));
        IUItem.reactorcuriumDual = new ItemStack(new ItemBaseRod("reactorcuriumdual", 2,
                200, (float) Config.curiumPower,3
        ));
        IUItem.reactorcuriumQuad = new ItemStack(new ItemBaseRod("reactorcuriumquad", 4,
                400, (float) Config.curiumPower,3
        ));

        //
        //
        IUItem.reactorcaliforniaSimple = new ItemStack(new ItemBaseRod("reactorcaliforniasimple", 1,
                120, (float) Config.californiaPower,3
        ));
        IUItem.reactorcaliforniaDual = new ItemStack(new ItemBaseRod("reactorcaliforniadual", 2,
                240, (float) Config.californiaPower,3
        ));
        IUItem.reactorcaliforniaQuad = new ItemStack(new ItemBaseRod("reactorcaliforniaquad", 4,
                480, (float) Config.californiaPower,3
        ));

        //

        //
        IUItem.reactormendeleviumSimple = new ItemStack(new ItemBaseRod("reactormendeleviumsimple", 1,
                230, (float) Config.mendeleviumPower,4
        ));
        IUItem.reactormendeleviumDual = new ItemStack(new ItemBaseRod("reactormendeleviumdual", 2,
                460, (float) Config.mendeleviumPower,4
        ));
        IUItem.reactormendeleviumQuad = new ItemStack(new ItemBaseRod("reactormendeleviumquad", 4,
                920, (float) Config.mendeleviumPower,4
        ));

        //
        //
        IUItem.reactorberkeliumSimple = new ItemStack(new ItemBaseRod("reactorberkeliumsimple", 1,
                150, (float) Config.berkeliumPower,4
        ));
        IUItem.reactorberkeliumDual = new ItemStack(new ItemBaseRod("reactorberkeliumdual", 2,
                300, (float) Config.berkeliumPower,4
        ));
        IUItem.reactorberkeliumQuad = new ItemStack(new ItemBaseRod("reactorberkeliumquad", 4,
                600, (float) Config.berkeliumPower,4
        ));

        //
        //
        IUItem.reactoreinsteiniumSimple = new ItemStack(new ItemBaseRod("reactoreinsteiniumsimple", 1,
                180, (float) Config.einsteiniumPower,4
        ));
        IUItem.reactoreinsteiniumDual = new ItemStack(new ItemBaseRod("reactoreinsteiniumdual", 2,
                360, (float) Config.einsteiniumPower,4
        ));
        IUItem.reactoreinsteiniumQuad = new ItemStack(new ItemBaseRod("reactoreinsteiniumquad", 4,
                720, (float) Config.einsteiniumPower,4
        ));

        //
        //
        IUItem.reactoruran233Simple = new ItemStack(new ItemBaseRod("reactoruran233simple", 1,
                35, (float) Config.uran233Power,1
        ));
        IUItem.reactoruran233Dual = new ItemStack(new ItemBaseRod("reactoruran233dual", 2,
                70, (float) Config.uran233Power,1
        ));
        IUItem.reactoruran233Quad = new ItemStack(new ItemBaseRod("reactoruran233quad", 4,
                140, (float) Config.uran233Power,1
        ));
        IUItem.katana = new ItemKatana("katana");
        IUItem.basecircuit = new ItemBaseCircuit();
        IUItem.lens = new ItemLens();
        IUItem.paints = new ItemPaints();
        IUItem.coolpipes = new ItemCoolPipes();
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
        IUItem.compresscarbon = new IUItemBase("compresscarbon");
        IUItem.compressAlloy = new IUItemBase("compresscarbonultra");
        IUItem.photoniy = new IUItemBase("photoniy");
        IUItem.photoniy_ingot = new IUItemBase("photoniy_ingot");
        IUItem.compressIridiumplate = new IUItemBase("quantumitems2");
        IUItem.advQuantumtool = new IUItemBase("quantumitems3");
        IUItem.expmodule = new IUItemBase("expmodule").setCreativeTab(IUCore.ModuleTab);
        IUItem.doublecompressIridiumplate = new IUItemBase("quantumitems4");
        IUItem.nanoBox = new IUItemBase("nanobox");
        IUItem.module_schedule = new IUItemBase("module_schedule");
        IUItem.quantumtool = new IUItemBase("quantumitems6");
        IUItem.advnanobox = new IUItemBase("quantumitems7");
        IUItem.neutronium = new IUItemBase("neutronium");
        IUItem.plast = new IUItemBase("plast");
        IUItem.module_stack = new IUItemBase("module_stack").setCreativeTab(IUCore.ModuleTab);
        IUItem.module_quickly = new IUItemBase("module_quickly").setCreativeTab(IUCore.ModuleTab);
        IUItem.module_storage = new IUItemBase("module_storage").setCreativeTab(IUCore.ModuleTab);
        IUItem.module_infinity_water = new IUItemBase("module_infinity_water").setCreativeTab(IUCore.ModuleTab);
        IUItem.module_separate = new IUItemBase("module_separate").setCreativeTab(IUCore.ModuleTab);
        IUItem.plastic_plate = new IUItemBase("plastic_plate");

        IUItem.doublescrapBox = new IUItemBase("doublescrapbox");
        IUItem.quarrymodule = new IUItemBase("quarrymodule").setCreativeTab(IUCore.ModuleTab);
        IUItem.analyzermodule = new IUItemBase("analyzermodule").setCreativeTab(IUCore.ModuleTab);


        IUItem.radiationresources = new RadiationResources();
        IUItem.protonshard = new ItemRadioactive("protonshard", 150, 100);
        IUItem.proton = new ItemRadioactive("proton", 150, 100);
        IUItem.toriy = new ItemRadioactive("toriy", 0, 0);

        IUItem.sunnarium = new ItemSunnarium();
        IUItem.sunnariumpanel = new ItemSunnariumPanel();

        IUItem.anode = new ItemChemistry("anode");
        IUItem.cathode = new ItemChemistry("cathode");
        IUItem.alloyscasing = new ItemAlloysCasing();
        IUItem.alloysdoubleplate = new ItemAlloysDoublePlate();
        IUItem.alloysdust = new ItemAlloysDust();
        IUItem.alloysingot = new ItemAlloysIngot();
        IUItem.alloysnugget = new ItemAlloysNugget();
        IUItem.alloysplate = new ItemAlloysPlate();
        IUItem.preciousgem = new ItemPreciousGem();


        IUItem.uranium_fuel_rod = new ItemStack(new ItemBaseRod("uranium_fuel_rod", 1,
                25, (float) 1.5,1
        ));
        IUItem.dual_uranium_fuel_rod = new ItemStack(new ItemBaseRod("dual_uranium_fuel_rod", 2,
                50, (float) 1.5,1
        ));
        IUItem.quad_uranium_fuel_rod = new ItemStack(new ItemBaseRod("quad_uranium_fuel_rod", 4,
                100, (float) 1.5,1
        ));
        IUItem.mox_fuel_rod = new ItemStack(new ItemBaseRod("mox_fuel_rod", 1,
                40, (float) 3.5,1
        ));
        IUItem.dual_mox_fuel_rod = new ItemStack(new ItemBaseRod("dual_mox_fuel_rod", 2,
                80, (float) 3.5, 1
        ));
        IUItem.quad_mox_fuel_rod = new ItemStack(new ItemBaseRod("quad_mox_fuel_rod", 4,
                160, (float) 3.5,1
        ));
        IUItem.upgradepanelkit = new ItemUpgradePanelKit();
        IUItem.magnet = new ItemMagnet("magnet", 100000, 5000, 4, 7);
        IUItem.impmagnet = new ItemMagnet("impmagnet", 200000, 7500, 5, 11);
        IUItem.purifier = new ItemPurifier("purifier", 100000, 1000, 3);
        IUItem.entitymodules = new ItemEntityModule();


        IUItem.alloysblock = new BlocksAlloy();
        IUItem.block = new BlocksIngot();
        IUItem.block1 = new BlockIngots1();
        IUItem.preciousblock = new BlockPrecious();
        IUItem.preciousore = new BlockPreciousOre();
        IUItem.ore = new BlockOre();
        IUItem.heavyore = new BlockHeavyOre();
        IUItem.radiationore = new BlocksRadiationOre();
        IUItem.oilblock = new BlockOil();
        IUItem.toriyore = new BlockThoriumOre();
        IUItem.itemiu = new ItemIUCrafring();
        IUItem.radiationModule = new ItemReactorModules();
        IUItem.chainsaw = new ItemEnergyInstruments(EnumTypeInstruments.CHAINSAW, EnumVarietyInstruments.CHAINSAW, "chainsaw");

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

        if (Loader.isModLoaded("exnihilocreatio")) {
            ExNihiloIntegration.gravel = new GravelBlocks();
            ExNihiloIntegration.dust = new DustBlocks();
            ExNihiloIntegration.sand = new SandBlocks();
            ExNihiloIntegration.gravel_crushed = new ItemGravelCrushed();
            ExNihiloIntegration.dust_crushed = new ItemDustCrushed();
            ExNihiloIntegration.sand_crushed = new ItemSandCrushed();
        }
        IUItem.book = new ItemBook("book_iu");
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
        IUItem.RTGPellets = IUItem.nuclear_res.getItemStack(ItemNuclearResource.Types.rtg_pellet);
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

        IUItem.componentVent = new ItemComponentVent("component_vent",1,3);
        IUItem.adv_componentVent = new ItemComponentVent("adv_component_vent",2,4);
        IUItem.imp_componentVent = new ItemComponentVent("imp_component_vent",3,5);
        IUItem.per_componentVent = new ItemComponentVent("per_component_vent",4,6);

        IUItem.vent = new ItemReactorVent("vent",2500,1,8,0.9,4);
        IUItem.adv_Vent = new ItemReactorVent("adv_vent",4000,2,10,0.85,7);
        IUItem.imp_Vent = new ItemReactorVent("imp_vent",5000,3,14,0.8,11);
        IUItem.per_Vent = new ItemReactorVent("per_vent",6000,4,20,0.75,15);

        IUItem.reactor_plate = new ItemReactorPlate("reactor_plate",1,2);
        IUItem.adv_reactor_plate = new ItemReactorPlate("adv_reactor_plate",2,1.5);
        IUItem.imp_reactor_plate = new ItemReactorPlate("imp_reactor_plate",3,1.25);
        IUItem.per_reactor_plate = new ItemReactorPlate("per_reactor_plate",4,1);

        IUItem.heat_exchange = new ItemReactorHeatExchanger("heat_exchange",2500,1,10,0.8);
        IUItem.adv_heat_exchange = new ItemReactorHeatExchanger("adv_heat_exchange",5000,2,12,0.75);
        IUItem.imp_heat_exchange = new ItemReactorHeatExchanger("imp_heat_exchange",7500,3,15,0.6);
        IUItem.per_heat_exchange = new ItemReactorHeatExchanger("per_heat_exchange",10000,4,20,0.45);

        IUItem.capacitor = new ItemReactorCapacitor("capacitor",25000,1,4);
        IUItem.adv_capacitor = new ItemReactorCapacitor("adv_capacitor",50000,2,6);
        IUItem.imp_capacitor = new ItemReactorCapacitor("imp_capacitor",100000,3,8);
        IUItem.per_capacitor = new ItemReactorCapacitor("per_capacitor",200000,4,12);

        IUItem.coolant = new ItemReactorCoolant("coolant",1,100000,2);
        IUItem.adv_coolant = new ItemReactorCoolant("adv_coolant",2,250000,4);
        IUItem.imp_coolant = new ItemReactorCoolant("imp_coolant",3,500000,7);


        IUItem.pump = new ItemsPumps("pump",2000,0,1,2);
        IUItem.adv_pump= new ItemsPumps("adv_pump",5000,1,2,4);
        IUItem.imp_pump= new ItemsPumps("imp_pump",7500,2,4,9);
        IUItem.per_pump= new ItemsPumps("per_pump",10000,3,6,15);

        IUItem.fan =new ItemsFan("fan",2000,0,1,2);
        IUItem.adv_fan= new ItemsFan("adv_fan",5000,1,2,4);
        IUItem.imp_fan= new ItemsFan("imp_fan",7500,2,4,9);
        IUItem.per_fan= new ItemsFan("per_fan",10000,3,6,15);


        IUItem.simple_capacitor_item = new ItemCapacitor("simple_capacitor",0,0.02);
        IUItem.adv_capacitor_item = new ItemCapacitor("adv_capacitor",1,0.04);
        IUItem.imp_capacitor_item = new ItemCapacitor("imp_capacitor",2,0.08);
        IUItem.per_capacitor_item= new ItemCapacitor("per_capacitor",3,0.12);


        IUItem.simple_exchanger_item = new ItemExchanger("simple_exchanger",0,0.05);
        IUItem.adv_exchanger_item = new ItemExchanger("adv_exchanger",1,0.1);
        IUItem.imp_exchanger_item = new ItemExchanger("imp_exchanger",2,0.15);
        IUItem.per_exchanger_item= new ItemExchanger("per_exchanger",3,0.2);
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
        if (!FluidRegistry.registerFluid(fluid)) {
            fluid = FluidRegistry.getFluid(name.getName());
        }

        if (!fluid.canBePlacedInWorld()) {
            Block block = new BlockIUFluid(name, fluid, steam);
            fluid.setBlock(block);
            fluid.setUnlocalizedName(block.getUnlocalizedName().substring(3));
        }

        name.setInstance(fluid);
        FluidRegistry.addBucketForFluid(fluid);
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
