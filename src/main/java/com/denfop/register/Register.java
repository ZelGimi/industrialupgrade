package com.denfop.register;

import com.denfop.Config;
import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.blocks.BlockHeavyOre;
import com.denfop.blocks.BlockIngots1;
import com.denfop.blocks.BlockOil;
import com.denfop.blocks.BlockOre;
import com.denfop.blocks.BlockPrecious;
import com.denfop.blocks.BlockPreciousOre;
import com.denfop.blocks.BlockThoriumOre;
import com.denfop.blocks.BlockVein;
import com.denfop.blocks.BlocksAlloy;
import com.denfop.blocks.BlocksIngot;
import com.denfop.blocks.BlocksRadiationOre;
import com.denfop.blocks.mechanism.BlockAdminPanel;
import com.denfop.blocks.mechanism.BlockAdvChamber;
import com.denfop.blocks.mechanism.BlockAdvRefiner;
import com.denfop.blocks.mechanism.BlockAdvSolarEnergy;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.blocks.mechanism.BlockCable;
import com.denfop.blocks.mechanism.BlockCombinerSolid;
import com.denfop.blocks.mechanism.BlockConverterMatter;
import com.denfop.blocks.mechanism.BlockDoubleMolecularTransfomer;
import com.denfop.blocks.mechanism.BlockImpChamber;
import com.denfop.blocks.mechanism.BlockImpSolarEnergy;
import com.denfop.blocks.mechanism.BlockMolecular;
import com.denfop.blocks.mechanism.BlockMoreMachine;
import com.denfop.blocks.mechanism.BlockMoreMachine1;
import com.denfop.blocks.mechanism.BlockMoreMachine2;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.blocks.mechanism.BlockPerChamber;
import com.denfop.blocks.mechanism.BlockPetrolQuarry;
import com.denfop.blocks.mechanism.BlockQuarryVein;
import com.denfop.blocks.mechanism.BlockRefiner;
import com.denfop.blocks.mechanism.BlockSintezator;
import com.denfop.blocks.mechanism.BlockSolarEnergy;
import com.denfop.blocks.mechanism.BlockSolidMatter;
import com.denfop.blocks.mechanism.BlockSunnariumMaker;
import com.denfop.blocks.mechanism.BlockSunnariumPanelMaker;
import com.denfop.blocks.mechanism.BlockTank;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.blocks.mechanism.BlockUpgradeBlock;
import com.denfop.blocks.mechanism.IUChargepadStorage;
import com.denfop.blocks.mechanism.IUStorage;
import com.denfop.blocks.mechanism.SSPBlock;
import com.denfop.integration.botania.BotaniaIntegration;
import com.denfop.integration.exnihilo.ExNihiloIntegration;
import com.denfop.integration.exnihilo.blocks.DustBlocks;
import com.denfop.integration.exnihilo.blocks.GravelBlocks;
import com.denfop.integration.exnihilo.blocks.SandBlocks;
import com.denfop.integration.exnihilo.items.ItemDustCrushed;
import com.denfop.integration.exnihilo.items.ItemGravelCrushed;
import com.denfop.integration.exnihilo.items.ItemSandCrushed;
import com.denfop.items.CellType;
import com.denfop.items.IUItemBase;
import com.denfop.items.ItemAdvancedWindRotor;
import com.denfop.items.ItemBaseCircuit;
import com.denfop.items.ItemCable;
import com.denfop.items.ItemCell;
import com.denfop.items.ItemChemistry;
import com.denfop.items.ItemCore;
import com.denfop.items.ItemLens;
import com.denfop.items.ItemPaints;
import com.denfop.items.ItemPhotoniumGlass;
import com.denfop.items.ItemSolidMatter;
import com.denfop.items.ItemUpgradeKit;
import com.denfop.items.ItemUpgradeMachinesKit;
import com.denfop.items.ItemUpgradeModule;
import com.denfop.items.ItemUpgradePanelKit;
import com.denfop.items.armour.BaseArmor;
import com.denfop.items.armour.ItemAdvJetpack;
import com.denfop.items.armour.ItemArmorAdvHazmat;
import com.denfop.items.armour.ItemArmorImprovemedNano;
import com.denfop.items.armour.ItemArmorImprovemedQuantum;
import com.denfop.items.armour.ItemLappack;
import com.denfop.items.armour.ItemSolarPanelHelmet;
import com.denfop.items.bags.ItemEnergyBags;
import com.denfop.items.energy.AdvancedMultiTool;
import com.denfop.items.energy.EnergyAxe;
import com.denfop.items.energy.EnergyBow;
import com.denfop.items.energy.EnergyDrill;
import com.denfop.items.energy.EnergyPickaxe;
import com.denfop.items.energy.EnergyShovel;
import com.denfop.items.energy.ItemBattery;
import com.denfop.items.energy.ItemGraviTool;
import com.denfop.items.energy.ItemMagnet;
import com.denfop.items.energy.ItemPurifier;
import com.denfop.items.energy.ItemQuantumSaber;
import com.denfop.items.energy.ItemSpectralSaber;
import com.denfop.items.modules.AdditionModule;
import com.denfop.items.modules.ItemBaseModules;
import com.denfop.items.modules.ItemEntityModule;
import com.denfop.items.modules.ItemModuleTypePanel;
import com.denfop.items.modules.ModuleType;
import com.denfop.items.modules.QuarryModule;
import com.denfop.items.modules.SpawnerModules;
import com.denfop.items.modules.UpgradeModule;
import com.denfop.items.reactors.ItemBaseRod;
import com.denfop.items.reactors.ItemDepletedRod;
import com.denfop.items.reactors.ItemRadioactive;
import com.denfop.items.reactors.ItemReactorCondensator;
import com.denfop.items.reactors.ItemReactorHeatStorage;
import com.denfop.items.reactors.ItemReactorHeatSwitch;
import com.denfop.items.reactors.ItemReactorVent;
import com.denfop.items.reactors.ItemReactorVentSpread;
import com.denfop.items.reactors.RadoationResources;
import com.denfop.items.resource.ItemCasing;
import com.denfop.items.resource.ItemCrushed;
import com.denfop.items.resource.ItemDoublePlate;
import com.denfop.items.resource.ItemDust;
import com.denfop.items.resource.ItemIUCrafring;
import com.denfop.items.resource.ItemIngots;
import com.denfop.items.resource.ItemNugget;
import com.denfop.items.resource.ItemPlate;
import com.denfop.items.resource.ItemPurifiedCrushed;
import com.denfop.items.resource.ItemStiks;
import com.denfop.items.resource.ItemSunnariumPanel;
import com.denfop.items.resource.ItemsmallDust;
import com.denfop.items.resource.ItemverysmallDust;
import com.denfop.items.resource.alloys.ItemAlloysCasing;
import com.denfop.items.resource.alloys.ItemAlloysDoublePlate;
import com.denfop.items.resource.alloys.ItemAlloysDust;
import com.denfop.items.resource.alloys.ItemAlloysIngot;
import com.denfop.items.resource.alloys.ItemAlloysNugget;
import com.denfop.items.resource.alloys.ItemAlloysPlate;
import com.denfop.items.resource.itemSunnarium;
import com.denfop.items.resource.preciousresources.ItemPreciousGem;
import ic2.core.block.TeBlockRegistry;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.Loader;

public class  Register {

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
        IUItem.blockpanel = TeBlockRegistry.get(SSPBlock.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.electricblock = TeBlockRegistry.get(IUStorage.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.Chargepadelectricblock = TeBlockRegistry.get(IUChargepadStorage.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.solidmatter = TeBlockRegistry.get(BlockSolidMatter.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.machines = TeBlockRegistry.get(BlockBaseMachine.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.advBatChargeCrystal = new ItemBattery("itemadvbatchargecrystal", Config.Storagequantumsuit, 32368D, 5, true);
        IUItem.itemBatChargeCrystal = new ItemBattery("itembatchargecrystal", Config.Storagequantumsuit * 4, 129472D, 6, true);
        IUItem.blockSE = TeBlockRegistry.get(BlockSolarEnergy.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.UpgradeKit = new ItemUpgradeKit();
        IUItem.AdvblockSE = TeBlockRegistry.get(BlockAdvSolarEnergy.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.ImpblockSE = TeBlockRegistry.get(BlockImpSolarEnergy.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.blockmolecular = TeBlockRegistry.get(BlockMolecular.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.basemachine = TeBlockRegistry.get(BlockBaseMachine1.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.basemachine1 = TeBlockRegistry.get(BlockBaseMachine2.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.basemachine2 = TeBlockRegistry.get(BlockBaseMachine3.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.blocksintezator = TeBlockRegistry.get(BlockSintezator.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.sunnariummaker = TeBlockRegistry.get(BlockSunnariumMaker.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.sunnariumpanelmaker = TeBlockRegistry.get(BlockSunnariumPanelMaker.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.oilrefiner = TeBlockRegistry.get(BlockRefiner.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.oiladvrefiner = TeBlockRegistry.get(BlockAdvRefiner.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.upgradeblock = TeBlockRegistry.get(BlockUpgradeBlock.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.module = new ItemUpgradeModule();
        IUItem.advchamberblock = TeBlockRegistry.get(BlockAdvChamber.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.impchamberblock = TeBlockRegistry.get(BlockImpChamber.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.perchamberblock = TeBlockRegistry.get(BlockPerChamber.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.tranformer = TeBlockRegistry.get(BlockTransformer.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.GraviTool = new ItemGraviTool("GraviTool");
        IUItem.blockdoublemolecular = TeBlockRegistry.get(BlockDoubleMolecularTransfomer.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.blockadmin = TeBlockRegistry.get(BlockAdminPanel.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.cableblock = TeBlockRegistry.get(BlockCable.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.oilquarry = TeBlockRegistry.get(BlockQuarryVein.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.cable = new ItemCable();
        IUItem.convertersolidmatter = TeBlockRegistry.get(BlockConverterMatter.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.oilgetter=TeBlockRegistry.get(BlockPetrolQuarry.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.combinersolidmatter=TeBlockRegistry.get(BlockCombinerSolid.IDENTITY).setCreativeTab(IUCore.SSPTab);

        IUItem.machines_base = TeBlockRegistry.get(BlockMoreMachine.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.machines_base1 = TeBlockRegistry.get(BlockMoreMachine1.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.machines_base2 = TeBlockRegistry.get(BlockMoreMachine2.IDENTITY).setCreativeTab(IUCore.SSPTab);
        IUItem.machines_base3 = TeBlockRegistry.get(BlockMoreMachine3.IDENTITY).setCreativeTab(IUCore.SSPTab);

        IUItem.tank = TeBlockRegistry.get(BlockTank.IDENTITY).setCreativeTab(IUCore.SSPTab);

        if (Config.BotaniaLoaded) {
            ItemStack[] stack4 = {BotaniaIntegration.reactorDepletedterastrellSimple, BotaniaIntegration.reactorDepletedterastrellDual, BotaniaIntegration.reactorDepletedterastrellQuad};
            BotaniaIntegration.reactorDepletedterastrellSimple = new ItemStack(
                    new ItemDepletedRod("reactorDepletedterastrellSimple"));
            BotaniaIntegration.reactorDepletedterastrellDual = new ItemStack(
                    new ItemDepletedRod("reactorDepletedterastrellDual"));
            BotaniaIntegration.reactorDepletedterastrellQuad = new ItemStack(
                    new ItemDepletedRod("reactorDepletedterastrellQuad"));
            BotaniaIntegration.reactorterastrellSimple = new ItemStack(new ItemBaseRod("reactorterastrellSimple", 1,
                    Config.TerrasteelRodCells, Config.TerrasteelRodHeat, Config.TerrasteelPower, stack4));

            BotaniaIntegration.reactorterastrellDual = new ItemStack(new ItemBaseRod("reactorterastrellDual", 2,
                    Config.TerrasteelRodCells, Config.TerrasteelRodHeat, Config.TerrasteelPower, stack4));

            BotaniaIntegration.reactorterastrellQuad = new ItemStack(new ItemBaseRod("reactorterastrellQuad", 4,
                    Config.TerrasteelRodCells, Config.TerrasteelRodHeat, Config.TerrasteelPower, stack4));


        }


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
        IUItem.NanoHelmet = new ItemArmorImprovemedNano("itemArmorNanoHelmet",
                EntityEquipmentSlot.HEAD,
                (float) Config.NanoHelmet,
                (float) Config.NanoTransfer,
                Config.Nanotier
        );
        IUItem.NanoBodyarmor = new ItemArmorImprovemedNano("itemArmorNanoChestplate", EntityEquipmentSlot.CHEST,
                (float) Config.NanoBodyarmor, Config.NanoTransfer, Config.Nanotier
        );
        IUItem.NanoLeggings = new ItemArmorImprovemedNano("itemArmorNanoLegs",
                EntityEquipmentSlot.LEGS,
                (float) Config.NanoLeggings,
                (float) Config.NanoTransfer,
                Config.Nanotier
        );
        IUItem.NanoBoots = new ItemArmorImprovemedNano("itemArmorNanoBoots", EntityEquipmentSlot.FEET, (float) Config.NanoBoots,
                (float) Config.NanoTransfer, Config.Nanotier
        );
        IUItem.bags = new ItemEnergyBags("iu_bags", 27, 50000, 500);
        IUItem.adv_bags = new ItemEnergyBags("adv_iu_bags", 45, 75000, 750);
        IUItem.imp_bags = new ItemEnergyBags("imp_iu_bags", 63, 100000, 1000);

        IUItem.lapotronCrystal = new ItemBattery("itembatlamacrystal", Config.Storagequantumsuit, 8092.0D, 4, false);

        IUItem.iridium = new ItemStack(new ItemAdvancedWindRotor("iridium", Config.Radius, Config.durability,
                Config.efficiency, Config.minWindStrength, Config.maxWindStrength,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model1.png")
        ));
        IUItem.compressiridium = new ItemStack(new ItemAdvancedWindRotor("compressiridium", Config.Radius1,
                Config.durability1, Config.efficiency1, Config.minWindStrength1, Config.maxWindStrength1,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model2.png")
        ));
        IUItem.spectral = new ItemStack(new ItemAdvancedWindRotor("spectral", Config.Radius2,
                Config.durability2, Config.efficiency2, Config.minWindStrength2, Config.maxWindStrength2,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model3.png")
        ));
        IUItem.myphical = new ItemStack(new ItemAdvancedWindRotor("myphical", Config.Radius5,
                Config.durability5, Config.efficiency5, Config.minWindStrength5, Config.maxWindStrength5,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model4.png")
        ));

        IUItem.photon = new ItemStack(new ItemAdvancedWindRotor("photon", Config.Radius3, Config.durability3,
                Config.efficiency3, Config.minWindStrength3, Config.maxWindStrength3,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model6.png")
        ));
        IUItem.neutron = new ItemStack(new ItemAdvancedWindRotor("neutron", Config.Radius4, Config.durability4,
                Config.efficiency4, Config.minWindStrength4, Config.maxWindStrength4,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model5.png")
        ));

        IUItem.barionrotor = new ItemStack(new ItemAdvancedWindRotor("barionrotor", Config.Radius5,
                Config.durability5, Config.efficiency4 * 2, Config.minWindStrength5, Config.maxWindStrength5,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model7.png")
        ));

        IUItem.adronrotor = new ItemStack(new ItemAdvancedWindRotor("adronrotor", Config.Radius5,
                Config.durability5, Config.efficiency4 * 4, Config.minWindStrength5, Config.maxWindStrength5,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model8.png")
        ));
        IUItem.ultramarinerotor = new ItemStack(new ItemAdvancedWindRotor("ultramarinerotor", Config.Radius5,
                Config.durability5, Config.efficiency4 * 8, Config.minWindStrength5, Config.maxWindStrength5,
                new ResourceLocation(Constants.MOD_ID, "textures/items/carbon_rotor_model9.png")
        ));


        IUItem.reactorDepletedprotonSimple = new ItemStack(
                new ItemDepletedRod("reactordepletedprotonsimple"));
        IUItem.reactorDepletedprotonDual = new ItemStack(
                new ItemDepletedRod("reactordepletedprotondual"));
        IUItem.reactorDepletedprotonQuad = new ItemStack(
                new ItemDepletedRod("reactordepletedprotonquad"));
        IUItem.reactorDepletedprotoneit = new ItemStack(
                new ItemDepletedRod("reactordepletedprotoneit"));
        ItemStack[] stack = {IUItem.reactorDepletedprotonSimple, IUItem.reactorDepletedprotonDual, IUItem.reactorDepletedprotonQuad, IUItem.reactorDepletedprotoneit};

        IUItem.reactorprotonSimple = new ItemStack(new ItemBaseRod("reactorprotonsimple", 1,
                Config.ProtonRodCells, Config.ProtonRodHeat, Config.ProtonPower, stack
        ));
        IUItem.reactorprotonDual = new ItemStack(new ItemBaseRod("reactorprotondual", 2,
                Config.ProtonRodCells, Config.ProtonRodHeat, Config.ProtonPower, stack
        ));
        IUItem.reactorprotonQuad = new ItemStack(new ItemBaseRod("reactorprotonquad", 4,
                Config.ProtonRodCells, Config.ProtonRodHeat, Config.ProtonPower, stack
        ));
        IUItem.reactorprotoneit = new ItemStack(new ItemBaseRod("reactorprotoneit", 8,
                Config.ProtonRodCells, Config.ProtonRodHeat, Config.ProtonPower, stack
        ));

        //
        IUItem.reactorDepletedtoriySimple = new ItemStack(
                new ItemDepletedRod("reactordepletedtoriysimple"));
        IUItem.reactorDepletedtoriyDual = new ItemStack(
                new ItemDepletedRod("reactordepletedtoriydual"));
        IUItem.reactorDepletedtoriyQuad = new ItemStack(
                new ItemDepletedRod("reactordepletedtoriyquad"));
        ItemStack[] stack1 = {IUItem.reactorDepletedtoriySimple, IUItem.reactorDepletedtoriyDual, IUItem.reactorDepletedtoriyQuad};
        IUItem.reactortoriySimple = new ItemStack(new ItemBaseRod("reactortoriysimple", 1,
                Config.toriyRodCells, Config.toriyRodHeat, Config.toriyPower, stack1
        ));
        IUItem.reactortoriyDual = new ItemStack(new ItemBaseRod("reactortoriydual", 2,
                Config.toriyRodCells, Config.toriyRodHeat, Config.toriyPower, stack1
        ));
        IUItem.reactortoriyQuad = new ItemStack(new ItemBaseRod("reactortoriyquad", 4,
                Config.toriyRodCells, Config.toriyRodHeat, Config.toriyPower, stack1
        ));

        //
//
        IUItem.reactorDepletedamericiumSimple = new ItemStack(
                new ItemDepletedRod("reactordepletedamericiumsimple"));
        IUItem.reactorDepletedamericiumDual = new ItemStack(
                new ItemDepletedRod("reactordepletedamericiumdual"));
        IUItem.reactorDepletedamericiumQuad = new ItemStack(
                new ItemDepletedRod("reactordepletedamericiumquad"));
        stack1 = new ItemStack[]{IUItem.reactorDepletedamericiumSimple, IUItem.reactorDepletedamericiumDual, IUItem.reactorDepletedamericiumQuad};
        IUItem.reactoramericiumSimple = new ItemStack(new ItemBaseRod("reactoramericiumsimple", 1,
                Config.americiumRodCells, Config.americiumRodHeat, (float) Config.americiumPower, stack1
        ));
        IUItem.reactoramericiumDual = new ItemStack(new ItemBaseRod("reactoramericiumdual", 2,
                Config.americiumRodCells, Config.americiumRodHeat, (float) Config.americiumPower, stack1
        ));
        IUItem.reactoramericiumQuad = new ItemStack(new ItemBaseRod("reactoramericiumquad", 4,
                Config.americiumRodCells, Config.americiumRodHeat, (float) Config.americiumPower, stack1
        ));

        //
        //
        IUItem.reactorDepletedneptuniumSimple = new ItemStack(
                new ItemDepletedRod("reactordepletedneptuniumsimple"));
        IUItem.reactorDepletedneptuniumDual = new ItemStack(
                new ItemDepletedRod("reactordepletedneptuniumdual"));
        IUItem.reactorDepletedneptuniumQuad = new ItemStack(
                new ItemDepletedRod("reactordepletedneptuniumquad"));
        stack1 = new ItemStack[]{IUItem.reactorDepletedneptuniumSimple, IUItem.reactorDepletedneptuniumDual, IUItem.reactorDepletedneptuniumQuad};
        IUItem.reactorneptuniumSimple = new ItemStack(new ItemBaseRod("reactorneptuniumsimple", 1,
                Config.neptuniumRodCells, Config.neptuniumRodHeat, (float) Config.neptuniumPower, stack1
        ));
        IUItem.reactorneptuniumDual = new ItemStack(new ItemBaseRod("reactorneptuniumdual", 2,
                Config.neptuniumRodCells, Config.neptuniumRodHeat, (float) Config.neptuniumPower, stack1
        ));
        IUItem.reactorneptuniumQuad = new ItemStack(new ItemBaseRod("reactorneptuniumquad", 4,
                Config.neptuniumRodCells, Config.neptuniumRodHeat, (float) Config.neptuniumPower, stack1
        ));

        //
        IUItem.reactorDepletedcuriumSimple = new ItemStack(
                new ItemDepletedRod("reactordepletedcuriumsimple"));
        IUItem.reactorDepletedcuriumDual = new ItemStack(
                new ItemDepletedRod("reactordepletedcuriumdual"));
        IUItem.reactorDepletedcuriumQuad = new ItemStack(
                new ItemDepletedRod("reactordepletedcuriumquad"));
        stack1 = new ItemStack[]{IUItem.reactorDepletedcuriumSimple, IUItem.reactorDepletedcuriumDual, IUItem.reactorDepletedcuriumQuad};
        IUItem.reactorcuriumSimple = new ItemStack(new ItemBaseRod("reactorcuriumsimple", 1,
                Config.curiumRodCells, Config.curiumRodHeat, (float) Config.curiumPower, stack1
        ));
        IUItem.reactorcuriumDual = new ItemStack(new ItemBaseRod("reactorcuriumdual", 2,
                Config.curiumRodCells, Config.curiumRodHeat, (float) Config.curiumPower, stack1
        ));
        IUItem.reactorcuriumQuad = new ItemStack(new ItemBaseRod("reactorcuriumquad", 4,
                Config.curiumRodCells, Config.curiumRodHeat, (float) Config.curiumPower, stack1
        ));

        //
        //
        IUItem.reactorDepletedcaliforniaSimple = new ItemStack(
                new ItemDepletedRod("reactordepletedcaliforniasimple"));
        IUItem.reactorDepletedcaliforniaDual = new ItemStack(
                new ItemDepletedRod("reactordepletedcaliforniadual"));
        IUItem.reactorDepletedcaliforniaQuad = new ItemStack(
                new ItemDepletedRod("reactordepletedcaliforniaquad"));
        stack1 = new ItemStack[]{IUItem.reactorDepletedcaliforniaSimple, IUItem.reactorDepletedcaliforniaDual, IUItem.reactorDepletedcaliforniaQuad};
        IUItem.reactorcaliforniaSimple = new ItemStack(new ItemBaseRod("reactorcaliforniasimple", 1,
                Config.californiaRodCells, Config.californiaRodHeat, (float) Config.californiaPower, stack1
        ));
        IUItem.reactorcaliforniaDual = new ItemStack(new ItemBaseRod("reactorcaliforniadual", 2,
                Config.californiaRodCells, Config.californiaRodHeat, (float) Config.californiaPower, stack1
        ));
        IUItem.reactorcaliforniaQuad = new ItemStack(new ItemBaseRod("reactorcaliforniaquad", 4,
                Config.californiaRodCells, Config.californiaRodHeat, (float) Config.californiaPower, stack1
        ));

        //

        //
        IUItem.reactorDepletedmendeleviumSimple = new ItemStack(
                new ItemDepletedRod("reactordepletedmendeleviumsimple"));
        IUItem.reactorDepletedmendeleviumDual = new ItemStack(
                new ItemDepletedRod("reactordepletedmendeleviumdual"));
        IUItem.reactorDepletedmendeleviumQuad = new ItemStack(
                new ItemDepletedRod("reactordepletedmendeleviumquad"));
        stack1 = new ItemStack[]{IUItem.reactorDepletedmendeleviumSimple, IUItem.reactorDepletedmendeleviumDual, IUItem.reactorDepletedmendeleviumQuad};
        IUItem.reactormendeleviumSimple = new ItemStack(new ItemBaseRod("reactormendeleviumsimple", 1,
                Config.mendeleviumRodCells, Config.mendeleviumRodHeat, (float) Config.mendeleviumPower, stack1
        ));
        IUItem.reactormendeleviumDual = new ItemStack(new ItemBaseRod("reactormendeleviumdual", 2,
                Config.mendeleviumRodCells, Config.mendeleviumRodHeat, (float) Config.mendeleviumPower, stack1
        ));
        IUItem.reactormendeleviumQuad = new ItemStack(new ItemBaseRod("reactormendeleviumquad", 4,
                Config.mendeleviumRodCells, Config.mendeleviumRodHeat, (float) Config.mendeleviumPower, stack1
        ));

        //
        //
        IUItem.reactorDepletedberkeliumSimple = new ItemStack(
                new ItemDepletedRod("reactordepletedberkeliumsimple"));
        IUItem.reactorDepletedberkeliumDual = new ItemStack(
                new ItemDepletedRod("reactordepletedberkeliumdual"));
        IUItem.reactorDepletedberkeliumQuad = new ItemStack(
                new ItemDepletedRod("reactordepletedberkeliumquad"));
        stack1 = new ItemStack[]{IUItem.reactorDepletedberkeliumSimple, IUItem.reactorDepletedberkeliumDual, IUItem.reactorDepletedberkeliumQuad};
        IUItem.reactorberkeliumSimple = new ItemStack(new ItemBaseRod("reactorberkeliumsimple", 1,
                Config.berkeliumRodCells, Config.berkeliumRodHeat, (float) Config.berkeliumPower, stack1
        ));
        IUItem.reactorberkeliumDual = new ItemStack(new ItemBaseRod("reactorberkeliumdual", 2,
                Config.berkeliumRodCells, Config.berkeliumRodHeat, (float) Config.berkeliumPower, stack1
        ));
        IUItem.reactorberkeliumQuad = new ItemStack(new ItemBaseRod("reactorberkeliumquad", 4,
                Config.berkeliumRodCells, Config.berkeliumRodHeat, (float) Config.berkeliumPower, stack1
        ));

        //
        //
        IUItem.reactorDepletedeinsteiniumSimple = new ItemStack(
                new ItemDepletedRod("reactordepletedeinsteiniumsimple"));
        IUItem.reactorDepletedeinsteiniumDual = new ItemStack(
                new ItemDepletedRod("reactordepletedeinsteiniumdual"));
        IUItem.reactorDepletedeinsteiniumQuad = new ItemStack(
                new ItemDepletedRod("reactordepletedeinsteiniumquad"));
        stack1 = new ItemStack[]{IUItem.reactorDepletedeinsteiniumSimple, IUItem.reactorDepletedeinsteiniumDual, IUItem.reactorDepletedeinsteiniumQuad};
        IUItem.reactoreinsteiniumSimple = new ItemStack(new ItemBaseRod("reactoreinsteiniumsimple", 1,
                Config.einsteiniumRodCells, Config.einsteiniumRodHeat, (float) Config.einsteiniumPower, stack1
        ));
        IUItem.reactoreinsteiniumDual = new ItemStack(new ItemBaseRod("reactoreinsteiniumdual", 2,
                Config.einsteiniumRodCells, Config.einsteiniumRodHeat, (float) Config.einsteiniumPower, stack1
        ));
        IUItem.reactoreinsteiniumQuad = new ItemStack(new ItemBaseRod("reactoreinsteiniumquad", 4,
                Config.einsteiniumRodCells, Config.einsteiniumRodHeat, (float) Config.einsteiniumPower, stack1
        ));

        //
        //
        IUItem.reactorDepleteduran233Simple = new ItemStack(
                new ItemDepletedRod("reactordepleteduran233simple"));
        IUItem.reactorDepleteduran233Dual = new ItemStack(
                new ItemDepletedRod("reactordepleteduran233dual"));
        IUItem.reactorDepleteduran233Quad = new ItemStack(
                new ItemDepletedRod("reactordepleteduran233quad"));
        stack1 = new ItemStack[]{IUItem.reactorDepleteduran233Simple, IUItem.reactorDepleteduran233Dual, IUItem.reactorDepleteduran233Quad};
        IUItem.reactoruran233Simple = new ItemStack(new ItemBaseRod("reactoruran233simple", 1,
                Config.uran233RodCells, Config.uran233RodHeat, (float) Config.uran233Power, stack1
        ));
        IUItem.reactoruran233Dual = new ItemStack(new ItemBaseRod("reactoruran233dual", 2,
                Config.uran233RodCells, Config.uran233RodHeat, (float) Config.uran233Power, stack1
        ));
        IUItem.reactoruran233Quad = new ItemStack(new ItemBaseRod("reactoruran233quad", 4,
                Config.uran233RodCells, Config.uran233RodHeat, (float) Config.uran233Power, stack1
        ));
        IUItem.basecircuit = new ItemBaseCircuit();
        IUItem.lens = new ItemLens();
        IUItem.paints = new ItemPaints();
        IUItem.photonglass = new ItemPhotoniumGlass();
        IUItem.matter = new ItemSolidMatter();
        IUItem.spawnermodules = new SpawnerModules();
        IUItem.upgrademodule = new UpgradeModule();
        IUItem.iuingot = new ItemIngots();
        IUItem.iudust = new ItemDust();
        IUItem.nugget = new ItemNugget();
        IUItem.casing = new ItemCasing();
        IUItem.crushed = new ItemCrushed();
        IUItem.doubleplate = new ItemDoublePlate();
        IUItem.plate = new ItemPlate();
        IUItem.purifiedcrushed = new ItemPurifiedCrushed();
        IUItem.smalldust = new ItemsmallDust();
        IUItem.verysmalldust = new ItemverysmallDust();
        IUItem.stik = new ItemStiks();
        IUItem.basemodules = new ItemBaseModules();
        IUItem.module7 = new AdditionModule();
        IUItem.module5 = new ModuleType();
        IUItem.module6 = new ItemModuleTypePanel();
        IUItem.module9 = new QuarryModule();

        IUItem.Helium = new IUItemBase("helium");
        IUItem.coal_chunk1 = new IUItemBase("coal_chunk");
        IUItem.compresscarbon = new IUItemBase("compresscarbon");
        IUItem.compresscarbonultra = new IUItemBase("compresscarbonultra");
        IUItem.photoniy = new IUItemBase("photoniy");
        IUItem.photoniy_ingot = new IUItemBase("photoniy_ingot");
        IUItem.compressIridiumplate = new IUItemBase("quantumitems2");
        IUItem.advQuantumtool = new IUItemBase("quantumitems3");
        IUItem.expmodule = new IUItemBase("expmodule").setCreativeTab(IUCore.tabssp1);
        IUItem.doublecompressIridiumplate = new IUItemBase("quantumitems4");
        IUItem.nanoBox = new IUItemBase("nanobox");
        IUItem.module_schedule = new IUItemBase("module_schedule");
        IUItem.quantumtool = new IUItemBase("quantumitems6");
        IUItem.advnanobox = new IUItemBase("quantumitems7");
        IUItem.neutronium = new IUItemBase("neutronium");
        IUItem.plast = new IUItemBase("plast");
        IUItem.module_stack = new IUItemBase("module_stack").setCreativeTab(IUCore.tabssp1);
        IUItem.module_quickly = new IUItemBase("module_quickly").setCreativeTab(IUCore.tabssp1);
        IUItem.plastic_plate = new IUItemBase("plastic_plate");
        IUItem.neutroniumingot = new IUItemBase("neutroniumingot");
        IUItem.doublescrapBox = new IUItemBase("doublescrapbox");
        IUItem.quarrymodule = new IUItemBase("quarrymodule").setCreativeTab(IUCore.tabssp1);
        IUItem.analyzermodule = new IUItemBase("analyzermodule").setCreativeTab(IUCore.tabssp1);


        IUItem.radiationresources = new RadoationResources();
        IUItem.protonshard = new ItemRadioactive("protonshard", 150, 100);
        IUItem.proton = new ItemRadioactive("proton", 150, 100);
        IUItem.toriy = new ItemRadioactive("toriy", 0, 0);

        IUItem.sunnarium = new itemSunnarium();
        IUItem.sunnariumpanel = new ItemSunnariumPanel();
        IUItem.core = new ItemCore();
        IUItem.anode = new ItemChemistry("anode");
        IUItem.cathode = new ItemChemistry("cathode");
        IUItem.alloyscasing = new ItemAlloysCasing();
        IUItem.alloysdoubleplate = new ItemAlloysDoublePlate();
        IUItem.alloysdust = new ItemAlloysDust();
        IUItem.alloysingot = new ItemAlloysIngot();
        IUItem.alloysnugget = new ItemAlloysNugget();
        IUItem.alloysplate = new ItemAlloysPlate();
        IUItem.preciousgem = new ItemPreciousGem();

        IUItem.advventspread = new ItemReactorVentSpread("advventspread", 5);
        IUItem.impventspread = new ItemReactorVentSpread("impventspread", 6);
        IUItem.reactoradvVent = new ItemReactorVent("reactoradvvent", 1000, 25, 32);
        IUItem.reactorimpVent = new ItemReactorVent("reactorimpvent", 1500, 30, 25);
        IUItem.reactorCondensatorDiamond = new ItemReactorCondensator("reactorcondensatordiamond", 500000);
        IUItem.advheatswitch = new ItemReactorHeatSwitch("advheatswitch", 5000, 40, 45);
        IUItem.impheatswitch = new ItemReactorHeatSwitch("impheatswitch", 5000, 60, 70);
        IUItem.reactorCoolantmax = new ItemStack(new ItemReactorHeatStorage("reactorcoolantmax", 240000));
        IUItem.reactorCoolanttwelve = new ItemStack(new ItemReactorHeatStorage("reactorcoolanttwelve", 120000));
        IUItem.UpgradePanelKit = new ItemUpgradePanelKit();
        IUItem.magnet = new ItemMagnet("magnet", 100000, 5000, 4, 7);
        IUItem.impmagnet = new ItemMagnet("impmagnet", 200000, 7500, 5, 11);
        IUItem.Purifier = new ItemPurifier("purifier", 100000, 1000, 3);
        IUItem.entitymodules = new ItemEntityModule();
        IUItem.cell_all = new ItemCell();
        IUItem.cell_all.setUseHandler(CellType.empty, ItemCell.emptyCellFill);
        IUItem.cell_all.addCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, (stac) -> {
            CellType type = IUItem.cell_all.getType(stac);
            CellType.CellFluidHandler var10000;
            if (type.isFluidContainer()) {
                IUItem.cell_all.getClass();
                var10000 = new CellType.CellFluidHandler(stac, IUItem.cell_all::getType);
            } else {
                var10000 = null;
            }

            return var10000;
        });
        IUItem.alloysblock = new BlocksAlloy();
        IUItem.block = new BlocksIngot();
        IUItem.block1 = new BlockIngots1();
        IUItem.preciousblock = new BlockPrecious();
        IUItem.preciousore = new BlockPreciousOre();
        IUItem.ore = new BlockOre();
        IUItem.heavyore = new BlockHeavyOre();
        IUItem.radiationore = new BlocksRadiationOre();
        IUItem.vein = new BlockVein();
        IUItem.oilblock = new BlockOil();
        IUItem.toriyore = new BlockThoriumOre();
        IUItem.itemSSP = new ItemIUCrafring();
        IUItem.nanopickaxe = new EnergyPickaxe(Item.ToolMaterial.DIAMOND,
                "nanopickaxe",
                1,
                1,
                Config.nano_transfer,
                Config.nano_maxEnergy,
                2,
                20,
                15,
                Config.nano_energyPerOperation,
                Config.nano_energyPerbigHolePowerOperation
        );
        IUItem.nanoshovel = new EnergyShovel(Item.ToolMaterial.DIAMOND,
                "nanoshovel",
                1,
                1,
                Config.nano_transfer,
                Config.nano_maxEnergy,
                2,
                20,
                10,
                Config.nano_energyPerOperation,
                Config.nano_energyPerbigHolePowerOperation
        );
        IUItem.nanoaxe = new EnergyAxe(
                Item.ToolMaterial.DIAMOND,
                "nanoaxe",
                1,
                1,
                Config.nano_transfer,
                Config.nano_maxEnergy,
                2,
                20,
                15,
                Config.nano_energyPerOperation,
                Config.nano_energyPerbigHolePowerOperation
        );
        IUItem.quantumpickaxe = new EnergyPickaxe(Item.ToolMaterial.DIAMOND,
                "quantumpickaxe",
                3,
                2,
                Config.quantum_transfer,
                Config.quantum_maxEnergy,
                3,
                25,
                20,
                Config.quantum_energyPerOperation,
                Config.quantum_energyPerbigHolePowerOperation
        );
        IUItem.quantumshovel = new EnergyShovel(Item.ToolMaterial.DIAMOND,
                "quantumshovel",
                3,
                2,
                Config.quantum_transfer,
                Config.quantum_maxEnergy,
                3,
                25,
                10,
                Config.quantum_energyPerOperation,
                Config.quantum_energyPerbigHolePowerOperation
        );
        IUItem.quantumaxe = new EnergyAxe(Item.ToolMaterial.DIAMOND,
                "quantumaxe",
                3,
                2,
                Config.quantum_transfer,
                Config.quantum_maxEnergy,
                3,
                25,
                20,
                Config.quantum_energyPerOperation,
                Config.quantum_energyPerbigHolePowerOperation
        );
        IUItem.spectralpickaxe = new EnergyPickaxe(Item.ToolMaterial.DIAMOND,
                "spectralpickaxe",
                5,
                3,
                Config.spectral_transfer,
                Config.spectral_maxEnergy,
                4,
                30,
                25,
                Config.spectral_energyPerOperation,
                Config.spectral_energyPerbigHolePowerOperation
        );
        IUItem.ultDDrill = new AdvancedMultiTool(Item.ToolMaterial.DIAMOND, "ultDDrill");

        IUItem.spectralshovel = new EnergyShovel(Item.ToolMaterial.DIAMOND,
                "spectralshovel",
                5,
                3,
                Config.spectral_transfer,
                Config.spectral_maxEnergy,
                4,
                30,
                10,
                Config.spectral_energyPerOperation,
                Config.spectral_energyPerbigHolePowerOperation
        );
        IUItem.spectralaxe = new EnergyAxe(Item.ToolMaterial.DIAMOND,
                "spectralaxe",
                5,
                3,
                Config.spectral_transfer,
                Config.spectral_maxEnergy,
                4,
                30,
                25,
                Config.spectral_energyPerOperation,
                Config.spectral_energyPerbigHolePowerOperation
        );
        IUItem.nano_bow = new EnergyBow(
                "nano_bow",
                0,
                Config.tier_nano_bow,
                Config.transfer_nano_bow,
                Config.maxenergy_nano_bow,
                1f
        );
        IUItem.quantum_bow = new EnergyBow(
                "quantum_bow",
                0,
                Config.tier_quantum_bow,
                Config.transfer_quantum_bow,
                Config.maxenergy_quantum_bow,
                2f
        );
        IUItem.spectral_bow = new EnergyBow(
                "spectral_bow",
                0,
                Config.tier_spectral_bow,
                Config.transfer_spectral_bow,
                Config.maxenergy_spectral_bow,
                4f
        );
        IUItem.spectralSaber = new ItemSpectralSaber("itemNanoSaber", Config.maxCharge, Config.transferLimit, Config.tier,
                Config.spectralsaberactive, Config.spectralsabernotactive
        );
        IUItem.quantumSaber = new ItemQuantumSaber("itemNanoSaber1", Config.maxCharge1, Config.transferLimit1,
                Config.tier1, Config.spectralsaberactive1, Config.spectralsabernotactive1
        );
        IUItem.quantumHelmet = new ItemArmorImprovemedQuantum("itemArmorQuantumHelmet",
                EntityEquipmentSlot.HEAD,
                Config.armor_maxcharge,
                Config.armor_transferlimit,
                Config.tier
        );
        IUItem.quantumBodyarmor = new ItemArmorImprovemedQuantum("itemArmorQuantumChestplate", EntityEquipmentSlot.CHEST,
                Config.armor_maxcharge_body, Config.armor_transferlimit, Config.tier
        );
        IUItem.quantumLeggings = new ItemArmorImprovemedQuantum("itemArmorQuantumLegs", EntityEquipmentSlot.LEGS,
                Config.armor_maxcharge,
                Config.armor_transferlimit, Config.tier
        );
        IUItem.quantumBoots = new ItemArmorImprovemedQuantum("itemArmorQuantumBoots", EntityEquipmentSlot.FEET,
                Config.armor_maxcharge,
                Config.armor_transferlimit, Config.tier
        );
        IUItem.nanodrill = new EnergyDrill(
                Item.ToolMaterial.DIAMOND,
                "nanodrill",
                1,
                1,
                Config.nano_transfer,
                Config.nano_maxEnergy,
                2,
                20,
                15,
                Config.nano_energyPerOperation,
                Config.nano_energyPerbigHolePowerOperation
        );
        IUItem.quantumdrill = new EnergyDrill(
                Item.ToolMaterial.DIAMOND,
                "quantumdrill",
                3,
                2,
                Config.quantum_transfer,
                Config.quantum_maxEnergy,
                3,
                25,
                20,
                Config.quantum_energyPerOperation,
                Config.quantum_energyPerbigHolePowerOperation
        );
        IUItem.spectraldrill = new EnergyDrill(
                Item.ToolMaterial.DIAMOND,
                "spectraldrill",
                5,
                3,
                Config.spectral_transfer,
                Config.spectral_maxEnergy,
                4,
                30,
                25,
                Config.spectral_energyPerOperation,
                Config.spectral_energyPerbigHolePowerOperation
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
                ItemArmor.ArmorMaterial.DIAMOND,
                IUCore.proxy.addArmor("spectralSolarHelmet"),
                0,
                4,
                "spectral_solar_helmet"
        );
        IUItem.singularSolarHelmet = new ItemSolarPanelHelmet(
                ItemArmor.ArmorMaterial.DIAMOND,
                IUCore.proxy.addArmor("singularSolarHelmet"),
                0,
                5,
                "singular_solar_helmet"
        );
        IUItem.advancedSolarHelmet = new ItemSolarPanelHelmet(
                ItemArmor.ArmorMaterial.DIAMOND,
                IUCore.proxy.addArmor("advancedSolarHelmet"),
                0,
                1,
                "advanced_solar_helmet"
        );
        IUItem.hybridSolarHelmet = new ItemSolarPanelHelmet(
                ItemArmor.ArmorMaterial.DIAMOND,
                IUCore.proxy.addArmor("hybridSolarHelmet"),
                0,
                2,
                "hybrid_solar_helmet"
        );
        IUItem.ultimateSolarHelmet = new ItemSolarPanelHelmet(
                ItemArmor.ArmorMaterial.DIAMOND,
                IUCore.proxy.addArmor("ultimateSolarHelmet"),
                0,
                3,
                "ultimate_solar_helmet"
        );
        IUItem.adv_lappack = new ItemLappack(
                "adv_lappack",
                ItemArmor.ArmorMaterial.DIAMOND,
                Config.adv_lappack_maxenergy,
                Config.adv_lappack_tier,
                Config.adv_lappack_transfer
        );
        IUItem.imp_lappack = new ItemLappack(
                "imp_lappack",
                ItemArmor.ArmorMaterial.DIAMOND,
                Config.imp_lappack_maxenergy,
                Config.imp_lappack_tier,
                Config.imp_lappack_transfer
        );
        IUItem.per_lappack = new ItemLappack(
                "per_lappack",
                ItemArmor.ArmorMaterial.DIAMOND,
                Config.per_lappack_maxenergy,
                Config.per_lappack_tier,
                Config.per_lappack_transfer
        );
        IUItem.machinekit = new ItemUpgradeMachinesKit();
        IUItem.circuitSpectral= new ItemStack(IUItem.basecircuit,1,11);
        IUItem.cirsuitQuantum= new ItemStack(IUItem.basecircuit,1,10);
        IUItem.QuantumItems9= new ItemStack(IUItem.basecircuit,1,9);
        IUItem.module8= new ItemStack(IUItem.module7,1,10);
        IUItem.module1= new ItemStack(IUItem.basemodules,1,0);
        IUItem.module2= new ItemStack(IUItem.basemodules,1,3);
        IUItem.module3= new ItemStack(IUItem.basemodules,1,6);
        IUItem.module4= new ItemStack(IUItem.basemodules,1,9);
        IUItem.genmodule= new ItemStack(IUItem.basemodules,1,1);
        IUItem.genmodule1= new ItemStack(IUItem.basemodules,1,2);
        IUItem.gennightmodule= new ItemStack(IUItem.basemodules,1,4);
        IUItem.gennightmodule1= new ItemStack(IUItem.basemodules,1,5);
        IUItem.storagemodule= new ItemStack(IUItem.basemodules,1,7);
        IUItem.storagemodule1= new ItemStack(IUItem.basemodules,1,8);
        IUItem.outputmodule= new ItemStack(IUItem.basemodules,1,10);
        IUItem.outputmodule1= new ItemStack(IUItem.basemodules,1,11);
        IUItem.phase_module = new ItemStack(IUItem.basemodules,1,12);
        IUItem.phase_module1 = new ItemStack(IUItem.basemodules,1,13);
        IUItem.phase_module2 = new ItemStack(IUItem.basemodules,1,14);
        IUItem.moonlinse_module = new ItemStack(IUItem.basemodules,1,15);
        IUItem.moonlinse_module1 = new ItemStack(IUItem.basemodules,1,16);
        IUItem.moonlinse_module2 = new ItemStack(IUItem.basemodules,1,17);
        if(Loader.isModLoaded("exnihilocreatio")){
            ExNihiloIntegration.gravel = new GravelBlocks();
            ExNihiloIntegration.dust = new DustBlocks();
            ExNihiloIntegration.sand = new SandBlocks();
            ExNihiloIntegration.gravel_crushed = new ItemGravelCrushed();
            ExNihiloIntegration.dust_crushed = new ItemDustCrushed();
            ExNihiloIntegration.sand_crushed = new ItemSandCrushed();
        }
    }

}
