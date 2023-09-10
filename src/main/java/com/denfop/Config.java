package com.denfop;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Config {

    public static final List<String> EntityList = new ArrayList<>();
    public static final List<Integer> DimensionList = new ArrayList<>();
    private static final String[] defaultSpawnerList = new String[]{"ExampleMob1", "ExampleMob2", "ExampleMob3 (these examples " +
            "can be deleted)"};
    public static double SolidMatterStorage;
    public static boolean experiment;
    public static int limit;
    public static int tier;
    public static int coefficientrf;
    public static double neutrongenday;
    public static double neutronGenNight;
    public static double neutronStorage;
    public static double neutronOutput;
    public static double InfinityGenNight;
    public static double InfinityGenDay;
    public static double InfinityOutput;
    public static double InfinityStorage;
    public static int manasteeltier;
    public static double manasteeloutput;
    public static double manasteelstorage;
    public static double manasteelgennight;
    public static double manasteelgenday;
    public static double elementiumstorage;
    public static double elementiumgenday;
    public static double elementiumgennight;
    public static double elementiumoutput;
    public static int elementiumtier;
    public static double terasteelgenday;
    public static double terasteelgennight;
    public static double terasteelstorage;
    public static double terasteeloutput;
    public static int terasteeltier;
    public static double chaosgenday;
    public static double chaosstorage;
    public static double chaosoutput;
    public static int chaostier;
    public static boolean blacklist;
    //
    public static double thaumgenday;
    public static double thaumstorage;
    public static double thaumoutput;
    public static int thaumtier;
    public static double voidgenday;
    public static double voidstorage;
    public static double voidoutput;
    public static int voidtier;
    //
    public static double NanoHelmet;
    public static double NanoBodyarmor;
    public static double NanoLeggings;
    public static double NanoBoots;
    public static double NanoTransfer;
    public static int Nanotier;
    //
    public static int awakenedtier;
    public static double awakenedoutput;
    public static double awakenedstorage;
    public static double awakenedgenday;
    public static int draconictier;
    public static double draconicoutput;
    public static double draconicstorage;
    public static double draconicgenday;
    public static int toriyRodCells;
    public static int toriyRodHeat;
    public static float toriyPower;
    public static boolean registerDraconicPanels;
    public static int effPower;
    public static int bigHolePower;
    public static int spectralsaberactive;
    public static int spectralsabernotactive;
    public static int Storagequantumsuit;
    public static double neutronpanelGenDay;
    public static double neutronpanelOutput;
    public static double neutronpanelStorage;
    public static int Radius3;
    public static int durability3;
    public static int efficiency3;
    public static int minWindStrength3;
    public static int maxWindStrength3;
    public static int Radius4;
    public static int durability4;
    public static int efficiency4;
    public static int minWindStrength4;
    public static int maxWindStrength4;
    public static int Radius5;
    public static int durability5;
    public static int efficiency5;
    public static int maxWindStrength5;
    public static int minWindStrength5;
    public static double protonOutput;
    public static double protonstorage;
    public static double protongenDay;
    public static int adv_enegry;
    public static int adv_storage;
    public static int tier1;
    public static double energy;
    public static int ult_enegry;
    public static int ult_storage;
    public static int lowPower;
    public static int ultraLowPower;
    public static int ultdrillmaxCharge;
    public static int ultdrilltier;
    public static int energyPerOperation;
    public static int energyPerLowOperation;
    public static int energyPerultraLowPowerOperation;
    public static int energyPerbigHolePowerOperation;
    public static int ultdrilltransferLimit;
    public static double advGenDay;
    public static boolean AvaritiaLoaded;
    public static boolean BotaniaLoaded;
    public static boolean Draconic;
    public static boolean Botania;
    public static boolean Avaritia;
    public static boolean enableexlposion;
    public static boolean enableIC2EasyMode;
    public static boolean EnableToriyOre;
    public static boolean newsystem;
    public static int armor_maxcharge;
    public static int armor_transferlimit;
    public static int armor_maxcharge_body;
    public static int percent_output;
    public static int percent_storage;
    public static int percent_day;
    public static int percent_night;
    public static double spectralpanelGenDay;
    public static double singularpanelGenDay;
    public static double singularpanelOutput;
    public static double spectralpanelOutput;
    public static double adminpanelGenDay;
    public static double AdminpanelStorage;
    public static double AdminpanelOutput;
    public static double photonicpanelGenDay;
    public static double photonicpanelOutput;
    public static double photonicpanelStorage;
    public static int tier_advmfsu;
    public static int tier_ultmfsu;
    public static double singularpanelstorage;
    public static boolean thaumcraft;
    public static double advStorage;
    public static double advOutput;
    public static double hGenDay;
    public static double hStorage;
    public static double hOutput;
    public static double uhGenDay;
    public static double uhStorage;
    public static double uhOutput;
    public static double qpGenDay;
    public static double qpStorage;
    public static double qpOutput;
    public static double spectralpanelstorage;
    public static int maxCharge;
    public static int transferLimit;
    public static int Radius;
    public static int durability;
    public static int efficiency;
    public static int minWindStrength;
    public static int maxWindStrength;
    public static int Radius1;
    public static int durability1;
    public static int efficiency1;
    public static int minWindStrength1;
    public static int maxWindStrength1;
    public static int Radius2;
    public static int durability2;
    public static int efficiency2;
    public static int minWindStrength2;
    public static int maxWindStrength2;
    public static boolean Streak;
    public static int TerrasteelRodCells;
    public static int TerrasteelRodHeat;
    public static float TerrasteelPower;
    public static int ProtonRodHeat;
    public static int ProtonRodCells;
    public static float ProtonPower;
    public static boolean DraconicLoaded;
    public static int spectralsaberactive1;
    public static int spectralsabernotactive1;
    public static int maxCharge1;
    public static int transferLimit1;
    public static double molecular;
    public static double molecular1;
    public static double molecular2;
    public static double molecular3;
    public static double molecular4;
    public static double molecular5;
    public static double molecular6;
    public static double molecular7;
    public static double molecular8;
    public static double molecular9;
    public static double molecular10;
    public static double molecular11;
    public static double molecular12;
    public static double molecular13;
    public static double molecular14;
    public static double molecular15;
    public static double molecular16;
    public static double molecular17;
    public static double molecular18;
    public static double molecular19;
    public static double molecular20;
    public static double molecular21;
    public static double molecular22;
    public static double molecular23;
    public static double molecular24;
    public static double molecular25;
    public static double molecular26;
    public static double molecular27;
    public static double molecular28;
    public static double molecular29;
    public static double molecular30;
    public static double molecular31;
    public static double molecular32;
    public static double molecular33;
    public static double molecular34;
    public static double molecular35;
    public static int enerycost;
    public static int expstorage;
    public static double molecular36;
    public static int energyPerultraLowPowerOperation1;
    public static int ultraLowPower1;
    public static boolean promt;
    public static double tierPerMFSU;
    public static double PerMFSUOutput;
    public static double PerMFSUStorage;
    public static double tierBarMFSU;
    public static double BarMFSUOutput;
    public static double BarMFSUStorage;
    public static boolean disableUpdateCheck;
    public static double adrGenDay;
    public static double adrOutput;
    public static double adrStorage;
    public static double barGenDay;
    public static double barOutput;
    public static double barStorage;
    public static double HadrMFSUStorage;
    public static double HadrMFSUOutput;
    public static double tierHadrMFSU;
    public static double GraMFSUStorage;
    public static double GraMFSUOutput;
    public static double tierGraMFSU;
    public static double KrvMFSUStorage;
    public static double KrvMFSUOutput;
    public static double tierKrvMFSU;
    public static int impheatswitch_switchreactor;
    public static int transfer_nano_bow;
    public static int transfer_quantum_bow;

    public static int transfer_spectral_bow;

    public static int tier_nano_bow;

    public static int tier_quantum_bow;

    public static int tier_spectral_bow;

    public static int maxenergy_nano_bow;

    public static int maxenergy_quantum_bow;

    public static int maxenergy_spectral_bow;
    public static int americiumRodCells;
    public static int americiumRodHeat;
    public static double americiumPower;
    public static int neptuniumRodCells;
    public static double neptuniumPower;
    public static int neptuniumRodHeat;
    public static int curiumRodCells;
    public static int curiumRodHeat;
    public static double curiumPower;
    public static int californiaRodCells;
    public static int californiaRodHeat;
    public static double californiaPower;
    public static double molecular37;
    public static double molecular38;
    public static double molecular39;
    public static double molecular40;
    public static double molecular41;
    public static double graGenDay;
    public static double graOutput;
    public static double graStorage;
    public static double kvrGenDay;
    public static double kvrOutput;
    public static double kvrStorage;
    public static boolean amplifierSlot;
    public static boolean spectralquantumprotection;
    public static boolean explode;
    public static int mendeleviumRodCells;
    public static int mendeleviumRodHeat;
    public static double mendeleviumPower;
    public static int berkeliumRodCells;
    public static int berkeliumRodHeat;
    public static double berkeliumPower;
    public static int einsteiniumRodCells;
    public static int einsteiniumRodHeat;
    public static double einsteiniumPower;
    public static int uran233RodCells;
    public static int uran233RodHeat;
    public static double uran233Power;
    public static boolean Thaumcraft;
    public static boolean SkeletonType;
    public static boolean MikhailOre;
    public static boolean AluminiumOre;
    public static boolean VanadiumOre;
    public static boolean TungstenOre;
    public static boolean CobaltOre;
    public static boolean MagnesiumOre;
    public static boolean NickelOre;
    public static boolean PlatiumOre;
    public static boolean TitaniumOre;
    public static boolean ChromiumOre;
    public static boolean SpinelOre;
    public static boolean SilverOre;
    public static boolean ZincOre;
    public static boolean ManganeseOre;
    public static boolean IridiumOre;
    public static boolean GermaniumOre;
    public static boolean MagnetiteOre;
    public static boolean CalaveriteOre;
    public static boolean AzuriteOre;
    public static boolean GalenaOre;
    public static boolean NickeliteOre;
    public static boolean PyriteOre;
    public static boolean QuartziteOre;
    public static boolean UraniteOre;
    public static boolean RhodoniteOre;
    public static boolean AlfilditOre;
    public static boolean EuxeniteOre;
    public static boolean SmithsoniteOre;
    public static boolean AmericiumOre;
    public static boolean NeptuniumOre;
    public static boolean CuriumOre;
    public static boolean RubyOre;
    public static boolean TopazOre;
    public static boolean SapphireOre;
    public static int maxVein;
    public static int nano_transfer;
    public static int nano_energyPerOperation;
    public static int nano_energyPerbigHolePowerOperation;
    public static int quantum_maxEnergy;
    public static int quantum_transfer;
    public static int quantum_energyPerOperation;
    public static int quantum_energyPerbigHolePowerOperation;
    public static int spectral_maxEnergy;
    public static int spectral_transfer;
    public static int spectral_energyPerOperation;
    public static int spectral_energyPerbigHolePowerOperation;
    public static int adv_jetpack_maxenergy;
    public static int per_jetpack_tier;
    public static int per_jetpack_transfer;
    public static int per_jetpack_maxenergy;
    public static int imp_jetpack_tier;
    public static int imp_jetpack_transfer;
    public static int imp_jetpack_maxenergy;
    public static int adv_jetpack_transfer;
    public static int adv_jetpack_tier;

    public static int adv_lappack_maxenergy;
    public static int per_lappack_tier;
    public static int per_lappack_transfer;
    public static int per_lappack_maxenergy;
    public static int imp_lappack_tier;
    public static int imp_lappack_transfer;
    public static int imp_lappack_maxenergy;
    public static int adv_lappack_transfer;
    public static int adv_lappack_tier;
    public static boolean enableonlyvein;


    public static int advventspread_sidevent;
    public static int impventspread_sidevent;

    public static int reactoradvVent_heatStorage1;
    public static int reactoradvVent_selfvent;
    public static int reactoradvVent_reactorvent;
    public static int reactorimpVent_heatStorage1;
    public static int reactorimpVent_selfvent;
    public static int reactorimpVent_reactorvent;

    public static int advheatswitch_heatStorage1;
    public static int advheatswitch_switchside;
    public static int advheatswitch_switchreactor;
    public static int impheatswitch_heatStorage1;
    public static int impheatswitch_switchside;
    public static int nano_maxEnergy;
    public static boolean IlmeniteOre;
    public static boolean TodorokiteOre;
    public static boolean FerroaugiteOre;
    public static boolean SheeliteeOre;
    public static boolean ProjectE;
    public static boolean cableEasyMode;
    public static boolean coolingsystem = true;
    public static int tickupdateenergysystem;
    public static int ticktransferenergy;
    public static boolean enablelosing;
    public static boolean CopperOre;
    public static boolean TinOre;
    public static boolean LeadOre;
    public static boolean UraniumOre;


    public static void loadNormalConfig(final File configFile) {
        IUCore.log.info("Loading IU Config from " + configFile.getAbsolutePath());

        final Configuration config = new Configuration(configFile);
        try {
            config.load();
            experiment = config.get("Experiment 3.0", "Enable", false).getBoolean(false);
            tickupdateenergysystem = config.get("general", "Tick update energy system", 20).getInt(20);
            ticktransferenergy = config.get("general", "Tick transfer energy", 1).getInt(1);
            uran233RodCells = config.get("Configuration of reactor`s rods", "Uran233_Cells", 5000).getInt(5000);
            uran233RodHeat = config.get("Configuration of reactor`s rods", "Uran233_Heat", 1).getInt(1);
            uran233Power = config.get("Configuration of reactor`s rods", "Uran233_Power", 3D).getDouble(3D);
            Thaumcraft = Loader.isModLoaded("thaumcraft");
            DraconicLoaded = Loader.isModLoaded("draconicevolution");
            AvaritiaLoaded = Loader.isModLoaded("avaritia");
            BotaniaLoaded = Loader.isModLoaded("botania");
            nano_maxEnergy = config.get("Configuration nano instruments", "max_energy", 1000000).getInt(1000000);
            nano_transfer = config.get("Configuration nano instruments", "transfer energy", 1000).getInt(1000);
            nano_energyPerOperation = config.get("Configuration nano instruments", "energyPerOperation", 400).getInt(400);
            nano_energyPerbigHolePowerOperation = config.get(
                    "Configuration nano instruments",
                    "energyPerbigHolePowerOperation",
                    550
            ).getInt(550);

            quantum_maxEnergy = config.get("Configuration quantum instruments", "max_energy", 10000000).getInt(10000000);
            quantum_transfer = config.get("Configuration quantum instruments", "transfer energy", 2500).getInt(2500);
            quantum_energyPerOperation = config.get("Configuration quantum instruments", "energyPerOperation", 350).getInt(350);
            quantum_energyPerbigHolePowerOperation = config.get(
                    "Configuration quantum instruments",
                    "energyPerbigHolePowerOperation",
                    450
            ).getInt(450);

            spectral_maxEnergy = config.get("Configuration spectral instruments", "max_energy", 50000000).getInt(50000000);
            spectral_transfer = config.get("Configuration spectral instruments", "transfer energy", 5000).getInt(5000);
            spectral_energyPerOperation = config.get("Configuration spectral instruments", "energyPerOperation", 250).getInt(250);
            spectral_energyPerbigHolePowerOperation = config.get(
                    "Configuration spectral instruments",
                    "energyPerbigHolePowerOperation",
                    370
            ).getInt(370);


            NanoHelmet = config.get("Improved NanoArmor", "NanoHelmet", 1E7).getDouble(1E7);
            NanoBodyarmor = config.get("Improved NanoArmor", "NanoBodyarmor", 3E7).getDouble(3E7);
            NanoLeggings = config.get("Improved NanoArmor", "NanoLeggings", 1E7).getDouble(1E7);
            NanoBoots = config.get("Improved NanoArmor", "NanoBoots", 1E7).getDouble(1E7);
            NanoTransfer = config.get("Improved NanoArmor", "NanoTransfer", 5000).getDouble(5000);
            Nanotier = config.get("Improved NanoArmor", "NanoTier", 5).getInt(5);


            einsteiniumRodCells = config.get("Configuration of reactor`s rods", "Einsteinium_Cells", 25000).getInt(25000);
            einsteiniumRodHeat = config.get("Configuration of reactor`s rods", "Einsteinium_Heat", 4).getInt(4);
            einsteiniumPower = config.get("Configuration of reactor`s rods", "Einsteinium_Power", 23D).getDouble(23D);


            berkeliumRodCells = config.get("Configuration of reactor`s rods", "Berkelium_Cells", 22500).getInt(22500);
            berkeliumRodHeat = config.get("Configuration of reactor`s rods", "Berkelium_Heat", 3).getInt(3);
            berkeliumPower = config.get("Configuration of reactor`s rods", "Berkelium_Power", 20D).getDouble(20D);


            mendeleviumRodCells = config.get("Configuration of reactor`s rods", "Mendelevium_Cells", 30000).getInt(30000);
            mendeleviumRodHeat = config.get("Configuration of reactor`s rods", "Mendelevium_Heat", 4).getInt(4);
            mendeleviumPower = config.get("Configuration of reactor`s rods", "Mendelevium_Power", 26D).getDouble(26D);


            californiaRodCells = config.get("Configuration of reactor`s rods", "California_Cells", 20000).getInt(20000);
            californiaRodHeat = config.get("Configuration of reactor`s rods", "California_Heat", 3).getInt(3);
            californiaPower = config.get("Configuration of reactor`s rods", "California_Power", 18D).getDouble(18D);


            curiumRodCells = config.get("Configuration of reactor`s rods", "Curium_Cells", 8000).getInt(8000);
            curiumRodHeat = config.get("Configuration of reactor`s rods", "Curium_Heat", 2).getInt(2);
            curiumPower = config.get("Configuration of reactor`s rods", "Curium_Power", 9.5D).getDouble(9.5D);


            neptuniumRodCells = config.get("Configuration of reactor`s rods", "Neptunium_Cells", 15000).getInt(15000);
            neptuniumRodHeat = config.get("Configuration of reactor`s rods", "Neptunium_Heat", 1).getInt(1);
            neptuniumPower = config.get("Configuration of reactor`s rods", "Neptunium_Power", 3.5D).getDouble(3.5D);


            americiumRodCells = config.get("Configuration of reactor`s rods", "Americium_Cells", 5000).getInt(5000);
            americiumRodHeat = config.get("Configuration of reactor`s rods", "Americium_Heat", 1).getInt(1);
            americiumPower = config.get("Configuration of reactor`s rods", "Americium_Power", 4.5D).getDouble(4.5D);

            amplifierSlot = config.get("MultiMatter", "amplifierSlot", true).getBoolean(true);
            spectralquantumprotection = config.get("Spectral Armor", "protection", true).getBoolean(true);
            explode = config.get("Reactor", "explode", false).getBoolean(false);


            transfer_nano_bow = config.get("Energy Bow", "Nano Transfer energy", 5000).getInt(5000);
            transfer_quantum_bow = config.get("Energy Bow", "Quantum Transfer energy", 25000).getInt(25000);
            transfer_spectral_bow = config.get("Energy Bow", "Spectral Transfer energy", 50000).getInt(50000);
            tier_spectral_bow = config.get("Energy Bow", "Spectral Tier", 4).getInt(4);
            tier_quantum_bow = config.get("Energy Bow", "Quantum Tier", 3).getInt(3);
            tier_nano_bow = config.get("Energy Bow", "Nano Tier", 2).getInt(2);
            maxenergy_nano_bow = config.get("Energy Bow", "Nano MaxEnergy", 50000).getInt(50000);
            maxenergy_quantum_bow = config.get("Energy Bow", "Quantum MaxEnergy", 80000).getInt(80000);
            maxenergy_spectral_bow = config.get("Energy Bow", "Spectral MaxEnergy", 150000).getInt(150000);

            KrvMFSUStorage = config.get("Configuration Energy storages", "Quarkstorage", 409.6E9D).getDouble(409.6E9D);
            KrvMFSUOutput = config.get("Configuration Energy storages", "Quarkoutput", 61988864).getDouble(61988864);
            tierKrvMFSU = config.get("Configuration Energy storages", "Quarktier", 11).getDouble(11);

            GraMFSUStorage = config.get("Configuration Energy storages", "Gravitonstorage", 102.4E9D).getDouble(102.4E9D);
            GraMFSUOutput = config.get("Configuration Energy storages", "Gravitonoutput", 15497216).getDouble(15497216);
            tierGraMFSU = config.get("Configuration Energy storages", "Gravitontier", 10).getDouble(10);

            HadrMFSUStorage = config.get("Configuration Energy storages", "Hadronstorage", 25.6E9D).getDouble(25.6E9D);
            HadrMFSUOutput = config.get("Configuration Energy storages", "Hadronoutput", 3874304).getDouble(3874304);
            tierHadrMFSU = config.get("Configuration Energy storages", "Hadrontier", 9).getDouble(9);


            BarMFSUStorage = config.get("Configuration Energy storages", "Barionstorage", 6.4E9D).getDouble(6.4E9D);
            BarMFSUOutput = config.get("Configuration Energy storages", "Barionoutput", 968576).getDouble(968576);
            tierBarMFSU = config.get("Configuration Energy storages", "Bariontier", 8).getDouble(8);
            PerMFSUStorage = config.get("Configuration Energy storages", "Perfectstorage", 1.6E9D).getDouble(1.6E9D);
            PerMFSUOutput = config.get("Configuration Energy storages", "Perfectoutput", 242144).getDouble(242144);
            tierPerMFSU = config.get("Configuration Energy storages", "Perfecttier", 7).getDouble(7);
            expstorage = config.get("Basic Mechanisms", "exp storage", 500).getInt(500);
            enerycost = config.get("Quantum Quarry", "energy consume in QE (1 QE = 16 EF)", 25000).getInt(25000);
            coefficientrf = config.get("general", "coefficient rf", 4).getInt(4);
            if (coefficientrf < 1) {
                coefficientrf = 4;
            }
            molecular = config.get("Crafts Molecular Transformer", "Wither Skeleton skull", 4000000D).getDouble(4000000D);
            molecular1 = config.get("Crafts Molecular Transformer", "Nether Star", 250000000D).getDouble(250000000D);
            molecular2 = config.get("Crafts Molecular Transformer", "Iridium Ore", 10000000D).getDouble(10000000D);
            molecular3 = config.get("Crafts Molecular Transformer", "Proton", 15500000D).getDouble(15500000D);
            molecular4 = config.get("Crafts Molecular Transformer", "Iridium ingot", 2500000D).getDouble(2500000D);
            molecular5 = config.get("Crafts Molecular Transformer", "Photon ingot", 12000000D).getDouble(12000000D);
            molecular6 = config.get("Crafts Molecular Transformer", "Gunpowder", 70000D).getDouble(70000D);
            molecular7 = config.get("Crafts Molecular Transformer", "Gravel", 45000D).getDouble(45000D);
            molecular8 = config.get("Crafts Molecular Transformer", "Diamond", 1000000D).getDouble(1000000D);
            molecular9 = config.get("Crafts Molecular Transformer", "Nickel ingot", 450000D).getDouble(450000D);
            molecular10 = config.get("Crafts Molecular Transformer", "Gold ingot", 450000D).getDouble(450000D);
            molecular11 = config.get("Crafts Molecular Transformer", "Silver ingot", 800000D).getDouble(800000D);
            molecular12 = config.get("Crafts Molecular Transformer", "Tungsten ingot", 700000D).getDouble(700000D);
            molecular13 = config.get("Crafts Molecular Transformer", "Spinel ingot", 800000D).getDouble(800000D);
            molecular14 = config.get("Crafts Molecular Transformer", "Mikhalov ingot", 900000D).getDouble(900000D);
            molecular15 = config.get("Crafts Molecular Transformer", "Chromium ingot", 600000D).getDouble(600000D);
            molecular16 = config.get("Crafts Molecular Transformer", "Platium ingot", 800000D).getDouble(800000D);
            molecular17 = config.get("Crafts Molecular Transformer", "Advanced ingot", 1500D).getDouble(1500D);
            molecular18 = config.get("Crafts Molecular Transformer", "Hybrid core", 11720D).getDouble(11720D);
            molecular19 = config.get("Crafts Molecular Transformer", "Ultimate core", 60000D).getDouble(60000D);
            molecular20 = config.get("Crafts Molecular Transformer", "Quantum core", 300000D).getDouble(300000D);
            molecular21 = config.get("Crafts Molecular Transformer", "Spectral core", 1500000D).getDouble(1500000D);
            molecular22 = config.get("Crafts Molecular Transformer", "Proton core", 7500000D).getDouble(7500000D);
            molecular23 = config.get("Crafts Molecular Transformer", "Singular core", 45000000D).getDouble(45000000D);
            molecular24 = config.get("Crafts Molecular Transformer", "Diffraction core", 180000000D).getDouble(180000000D);
            molecular25 = config.get("Crafts Molecular Transformer", "Photnic core", 900000000D).getDouble(900000000D);
            molecular26 = config.get("Crafts Molecular Transformer", "Neutron Core", 2700000000D).getDouble(2700000000D);

            molecular38 = config.get("Crafts Molecular Transformer", "Barion Core", 4500000000D).getDouble(4500000000D);
            molecular39 = config.get("Crafts Molecular Transformer", "Hadron Core", 9000000000D).getDouble(9000000000D);
            molecular40 = config.get("Crafts Molecular Transformer", "Graviton Core", 12000000000D).getDouble(12000000000D);
            molecular41 = config.get("Crafts Molecular Transformer", "Kvark Core", 21000000000D).getDouble(21000000000D);

            molecular27 = config.get("Crafts Molecular Transformer", "Sun lense", 25000000D).getDouble(25000000D);
            molecular28 = config.get("Crafts Molecular Transformer", "Rain lense", 25000000D).getDouble(25000000D);
            molecular29 = config.get("Crafts Molecular Transformer", "Nether lense", 25000000D).getDouble(25000000D);
            molecular30 = config.get("Crafts Molecular Transformer", "Night lense", 25000000D).getDouble(25000000D);
            molecular31 = config.get("Crafts Molecular Transformer", "Earth lense", 25000000D).getDouble(25000000D);
            molecular32 = config.get("Crafts Molecular Transformer", "End lense", 25000000D).getDouble(25000000D);
            molecular33 = config.get("Crafts Molecular Transformer", "Aer lense", 25000000D).getDouble(25000000D);
            molecular34 = config.get("Crafts Molecular Transformer", "Photon", 1450000D).getDouble(1450000D);
            molecular35 = config.get("Crafts Molecular Transformer", "Magnesium ingot", 700000D).getDouble(700000D);
            molecular36 = config.get("Crafts Molecular Transformer", "Caravky ingot", 900000D).getDouble(900000D);
            molecular37 = config
                    .get("Crafts Molecular Transformer", "Iridium ingot(from Iridium Ore IUCore)", 50000)
                    .getDouble(50000);

            disableUpdateCheck = config.get("general", "Disable Update Check ", false).getBoolean(false);
            promt = config.get("general", "Enable prompt about information a panel", true).getBoolean(true);

            SolidMatterStorage = config.get("Solid Matter Generator Storage", "Matter Generator Storage", 5E7D).getDouble(5E7D);
            newsystem = config.get("Transformer mode", "Transformer mode", true).getBoolean(true);
            EnableToriyOre = config.get("spawn ore", "Spawn Thorium Ore", true).getBoolean(true);
            effPower = config.get("UltimateDrill", "Mode 0 efficiency", 80).getInt(80);
            lowPower = config.get("UltimateDrill", "Mode 1 efficiency", 60).getInt(60);
            bigHolePower = config.get("UltimateDrill", "Mode 2 efficiency", 60).getInt(60);
            ultraLowPower = config.get("UltimateDrill", "Mode 3 efficiency", 60).getInt(60);
            ultraLowPower1 = config.get("UltimateDrill", "Mode 4 efficiency", 60).getInt(60);
            ultdrillmaxCharge = config.get("UltimateDrill", "maxCharge", 15000000).getInt(15000000);
            ultdrilltier = config.get("UltimateDrill", "tier", 2).getInt(2);
            ultdrilltransferLimit = config.get("UltimateDrill", "transfer Limit", 500).getInt(500);
            energyPerOperation = config.get("UltimateDrill", "energyPerOperation", 160).getInt(160);
            energyPerLowOperation = config.get("UltimateDrill", "energyPerLowOperation", 80).getInt(80);
            energyPerbigHolePowerOperation = config.get("UltimateDrill", "energyPerBigHolesOperation (3x3)", 300).getInt(300);
            energyPerultraLowPowerOperation = config
                    .get("UltimateDrill", "energyPerUltraBigHolesOperation (5x5)", 500)
                    .getInt(500);
            energyPerultraLowPowerOperation1 = config.get("UltimateDrill", "energyPerUltraBigHolesOperation (7x7)", 700).getInt(
                    700);

            maxVein = config.get("general", "Maximum amount of ore in a vein", 30000).getInt(30000);
            enableIC2EasyMode = config
                    .get("Transformer mode", "unchecking the tier", false)
                    .getBoolean(false);
            cableEasyMode = config
                    .get("Transformer mode", "unlimiting the conduction of energy in the cable", false)
                    .getBoolean(false);
            enableexlposion = config.get(
                    "Transformer mode",
                    "Enable explosion from mechanisms is on (if enable transformer mode) ",
                    true
            ).getBoolean(true);
            enablelosing = config.get(
                    "Transformer mode",
                    "Enable losing in cables is on (if enable transformer mode) ",
                    true
            ).getBoolean(true);
            spectralsaberactive1 = config.get("Quantum Saber", "QuantumSaber Damage Active", 40).getInt(40);
            spectralsabernotactive1 = config.get("Quantum Saber", "QuantumSaber Damage Not Active", 8).getInt(8);
            maxCharge1 = config.get("Quantum Saber", "SpectralSaber max Charge", 200000).getInt(200000);
            transferLimit1 = config.get("Quantum Saber", "SpectralSaber transfer Limit", 15000).getInt(20000);
            tier1 = config.get("Quantum Saber", "SpectralSaber tier", 4).getInt(4);
            energy = config.get("Neutronium generator", "Consumes energy to make 1 mb neutronim", 16250000.0D).getDouble(
                    16250000.0D);
            maxCharge = config.get("Spectral Saber", "SpectralSaber max Charge", 600000).getInt(300000);
            transferLimit = config.get("Spectral Saber", "SpectralSaber transfer Limit", 40000).getInt(20000);
            spectralsaberactive = config.get("Spectral Saber", "SpectralSaber Damage Active", 60).getInt(60);
            spectralsabernotactive = config.get("Spectral Saber", "SpectralSaber Damage Not Active", 12).getInt(12);
            adv_enegry = config.get("Configuration Energy storages", "Improved denergy transfer Et/t", 8192).getInt(8192);
            adv_storage = config.get("Configuration Energy storages", "Improved energy storage", 100000000).getInt(100000000);
            tier_advmfsu = config.get("Configuration Energy storages", "Improved tier", 5).getInt(5);

            tier_ultmfsu = config.get("Configuration Energy storages", "Advancedtier", 6).getInt(6);
            ult_enegry = config.get("Configuration Energy storages", "Advancedenergy transfer Et/t", 32768).getInt(32768);
            ult_storage = config.get("Configuration Energy storages", "Advancedenergy storage", 400000000).getInt(400000000);
            InfinityGenDay = config
                    .get("Configuration Solar Panels(Integration)", "InfinityGenDay", 21211520)
                    .getDouble(21211520);
            InfinityGenNight = config.get("Configuration Solar Panels(Integration)", "InfinityGenNight", 21211520).getDouble(
                    21211520);
            InfinityOutput = config
                    .get("Configuration Solar Panels(Integration)", "InfinityOutput", 42423040)
                    .getDouble(42423040);
            InfinityStorage = config.get("Configuration Solar Panels(Integration)", "InfinityStorage", 2.5E10).getDouble(2.5E10);
            tier = config.get("Configuration Solar Panels(Integration)", "Neutrontier", 10).getInt(10);
            neutrongenday = config.get("Configuration Solar Panels(Integration)", "NeutronGenDay", 1325720).getDouble(1325720);
            neutronGenNight = config
                    .get("Configuration Solar Panels(Integration)", "NeutronGenNight", 1325720)
                    .getDouble(1325720);
            neutronOutput = config.get("Configuration Solar Panels(Integration)", "NeutronOutput", 2651440).getDouble(2651440);
            neutronStorage = config.get("Configuration Solar Panels(Integration)", "NeutronStorage", 6.5E9).getDouble(6.5E9);
            percent_output = config.get("Modules", "percent output", 5).getInt(5);
            percent_storage = config.get("Modules", "percent storage", 5).getInt(5);
            percent_day = config.get("Modules", "percent generation day", 5).getInt(5);
            percent_night = config.get("Modules", "percent generation night", 5).getInt(5);
            Storagequantumsuit = config.get("Battery", "MaxEnergy", 100000000).getInt(100000000);

            thaumcraft = config.get("Integration", "Integration Thaumcraft", true).getBoolean(true);
            Draconic = config.get("Integration", "Integration Draconic Evolution", true).getBoolean(true);
            Botania = config.get("Integration", "Integration Botania", true).getBoolean(true);
            Avaritia = config.get("Integration", "Integration Avaritia", true).getBoolean(true);
            ProjectE = config.get("Integration", "Integration ProjectE", true).getBoolean(true);
            //TODO config solar panels
            advGenDay = config.get("Configuration Solar Panels", "AdvancedSPGenDay", 5).getDouble(5);
            advStorage = config.get("Configuration Solar Panels", "AdvancedSPStorage", 3200D).getDouble(3200D);
            advOutput = config.get("Configuration Solar Panels", "AdvancedSPOutput", 10D).getDouble(10D);
            hGenDay = config.get("Configuration Solar Panels", "HybrydSPGenDay", 20).getDouble(20);
            hStorage = config.get("Configuration Solar Panels", "HybrydSPStorage", 20000D).getDouble(20000D);
            hOutput = config.get("Configuration Solar Panels", "HybrydSPOutput", 40).getDouble(40);
            uhGenDay = config.get("Configuration Solar Panels", "UltimateHSPGenDay", 80).getDouble(80);
            uhStorage = config.get("Configuration Solar Panels", "UltimateHSPStorage", 200000D).getDouble(200000D);
            uhOutput = config.get("Configuration Solar Panels", "UltimateHSPOutput", 160D).getDouble(160D);
            qpGenDay = config.get("Configuration Solar Panels", "QuantumSPGenDay", 320).getDouble(320);
            qpStorage = config.get("Configuration Solar Panels", "QuantumSPStorage", 1000000D).getDouble(1000000D);
            qpOutput = config.get("Configuration Solar Panels", "QuantumSPOutput", 640).getDouble(640);
            spectralpanelGenDay = config.get("Configuration Solar Panels", "SpectralSPGenDay", 1280).getDouble(1280);
            spectralpanelOutput = config.get("Configuration Solar Panels", "SpectralSPOutput", 2560).getDouble(2560);
            spectralpanelstorage = config.get("Configuration Solar Panels", "SpectralSPStorage", 5000000D).getDouble(500000D);
            protongenDay = config.get("Configuration Solar Panels", "ProtonGenDay", 5120).getDouble(5120);
            protonOutput = config.get("Configuration Solar Panels", "ProtonOutput", 10240).getDouble(10240);
            protonstorage = config.get("Configuration Solar Panels", "Protonstorage", 50000000D).getDouble(5000000D);
            singularpanelGenDay = config.get("Configuration Solar Panels", "SingularSPGenDay", 20480).getDouble(20480);
            singularpanelOutput = config.get("Configuration Solar Panels", "SingularSPOutput", 40960).getDouble(40960);
            singularpanelstorage = config
                    .get("Configuration Solar Panels", "SingularSPStorage", 1000000000D)
                    .getDouble(100000000D);
            adminpanelGenDay = config.get("Configuration Solar Panels", "DiffractionPanelGenDay", 81920).getDouble(81920);
            AdminpanelStorage = config.get("Configuration Solar Panels", "DiffractionPanelStorage", 1500000000D).getDouble(
                    1500000000D);
            AdminpanelOutput = config.get("Configuration Solar Panels", "DiffractionPanelOutput", 163840).getDouble(163840);
            photonicpanelGenDay = config.get("Configuration Solar Panels", "PhotonicPanelGenDay", 327680).getDouble(327680);
            photonicpanelOutput = config.get("Configuration Solar Panels", "PhotonicPanelOutput", 655360).getDouble(655360);
            photonicpanelStorage = config.get("Configuration Solar Panels", "PhotonicPanelStorage", 5000000000D).getDouble(
                    5000000000D);
            neutronpanelGenDay = config.get("Configuration Solar Panels", "NeutronPanelGenDay", 1310720).getDouble(1310720);
            neutronpanelOutput = config.get("Configuration Solar Panels", "NeutronPanelOutput", 2621440).getDouble(2621440);
            neutronpanelStorage = config.get("Configuration Solar Panels", "NeutronPanelStorage", 6500000000D).getDouble(
                    6500000000D);
            barGenDay = config.get("Configuration Solar Panels", "BarionGenDay", 5242880).getDouble(5242880);
            barOutput = config.get("Configuration Solar Panels", "BarionOutput", 10485760).getDouble(10485760);
            barStorage = config.get("Configuration Solar Panels", "BarionStorage", 10000000000D).getDouble(10000000000D);
            adrGenDay = config.get("Configuration Solar Panels", "HadrionGenDay", 20971520).getDouble(20971520);
            adrOutput = config.get("Configuration Solar Panels", "HadrionOutput", 41943040).getDouble(41943040);
            adrStorage = config.get("Configuration Solar Panels", "HadrionStorage", 25000000000D).getDouble(25000000000D);
            graGenDay = config.get("Configuration Solar Panels", "GravitonGenDay", 83886080).getDouble(83886080);
            graOutput = config.get("Configuration Solar Panels", "GravitonOutput", 167772160).getDouble(167772160);
            graStorage = config.get("Configuration Solar Panels", "GravitonStorage", 250000000000D).getDouble(250000000000D);
            kvrGenDay = config.get("Configuration Solar Panels", "KvarkGenDay", 335544320).getDouble(335544320);
            kvrOutput = config.get("Configuration Solar Panels", "KvarkOutput", 671088640).getDouble(678768640);
            kvrStorage = config.get("Configuration Solar Panels", "KvarkStorage", 2500000000000D).getDouble(2500000000000D);


            TerrasteelRodHeat = config.get("Configuration of reactor`s rods", "TerrasteelRod_Heat", 1).getInt(1);
            TerrasteelRodCells = config.get("Configuration of reactor`s rods", "TerrasteelRod_Cells", 20000).getInt(20000);
            TerrasteelPower = config.get("Configuration of reactor`s rods", "TerrasteelRod_Power", 2).getInt(2);
            toriyRodHeat = config.get("Configuration of reactor`s rods", "Thorium_Heat", 1).getInt(1);
            toriyRodCells = config.get("Configuration of reactor`s rods", "Thorium_Cells", 10000).getInt(10000);
            toriyPower = config.get("Configuration of reactor`s rods", "Thorium_Power", 3).getInt(3);
            ProtonRodHeat = config.get("Configuration of reactor`s rods", "Proton_Heat", 1).getInt(1);
            ProtonRodCells = config.get("Configuration of reactor`s rods", "Proton_Cells", 30000).getInt(30000);
            ProtonPower = config.get("Configuration of reactor`s rods", "Proton_Power", 6).getInt(6);
            Radius = config.get("Configuration rotors", "Iridium Radius", 11).getInt(11);
            durability = config
                    .get("Configuration rotors", "Iridium durability", (int) (172800 / 1.4D))
                    .getInt((int) (172800 / 1.4D));
            efficiency = config.get("Configuration rotors", "Iridium efficiency", 2.0F).getInt(2);
            minWindStrength = config.get("Configuration rotors", "Iridium minWindStrength", 25).getInt(25);
            maxWindStrength = config.get("Configuration rotors", "Iridium maxWindStrength", 110).getInt(110);
            Radius1 = config.get("Configuration rotors", "Compress Iridium Radius", 11).getInt(11);
            durability1 =
                    config
                            .get("Configuration rotors", "Compress Iridium durability", (int) (172800 / 1.2D))
                            .getInt((int) (172800 / 1.2D));
            efficiency1 = config.get("Configuration rotors", "Compress Iridium efficiency", 4.0F).getInt(4);

            minWindStrength1 = config.get("Configuration rotors", "Compress Iridium minWindStrength", 25).getInt(25);
            maxWindStrength1 = config.get("Configuration rotors", "Compress Iridium maxWindStrength", 110).getInt(110);
            Radius2 = config.get("Configuration rotors", "Spectral Radius", 11).getInt(11);
            durability2 =
                    config
                            .get("Configuration rotors", "Spectral durability", (int) (172800 / 1.1D))
                            .getInt((int) (172800 / 1.1D));
            efficiency2 = config.get("Configuration rotors", "Spectral efficiency", 8.0F).getInt((int) 8.0F);
            minWindStrength2 = config.get("Configuration rotors", "Spectral minWindStrength", 25).getInt(25);
            maxWindStrength2 = config.get("Configuration rotors", "Spectral maxWindStrength", 110).getInt(110);
            Streak = config.get("Spectral Armor", "Allow Streak", true).getBoolean(true);
            Radius5 = config.get("Configuration rotors", "Mythical Radius", 11).getInt(11);
            durability5 = config.get("Configuration rotors", "Mythical durability", 3456000).getInt(3456000);
            efficiency5 = config.get("Configuration rotors", "Mythical efficiency", 16.0F).getInt((int) 16.0F);
            minWindStrength5 = config.get("Configuration rotors", "Mythical minWindStrength", 25).getInt(25);
            maxWindStrength5 = config.get("Configuration rotors", "Mythical maxWindStrength", 110).getInt(110);
            Radius4 = config.get("Configuration rotors", "Neutron Radius", 11).getInt(11);
            durability4 = config.get("Configuration rotors", "Neutron durability", 27648000).getInt(27648000);
            efficiency4 = config.get("Configuration rotors", "Neutron efficiency", 64).getInt(64);
            minWindStrength4 = config.get("Configuration rotors", "Neutron minWindStrength", 25).getInt(25);
            maxWindStrength4 = config.get("Configuration rotors", "Neutron maxWindStrength", 110).getInt(110);

            Radius3 = config.get("Configuration rotors", "Photon Radius", 11).getInt(11);
            durability3 = config.get("Configuration rotors", "Photon durability", 691200).getInt(691200);
            efficiency3 = config.get("Configuration rotors", "Photon efficiency", 32).getInt(32);
            minWindStrength3 = config.get("Configuration rotors", "Photon minWindStrength", 25).getInt(25);
            maxWindStrength3 = config.get("Configuration rotors", "Photon maxWindStrength", 110).getInt(110);
            limit = config.get("Unifier panels", "Limit", 2).getInt(2);
            manasteelgenday = config.get("Configuration Solar Panels(Integration)", "Manasteelgenday", 80).getDouble(80);
            manasteelgennight = config.get("Configuration Solar Panels(Integration)", "Manasteelgennight", 40D).getDouble(40D);
            manasteelstorage = config.get("Configuration Solar Panels(Integration)", "Manasteelstorage", 200000D).getDouble(
                    200000D);
            manasteeloutput = config.get("Configuration Solar Panels(Integration)", "Manasteeloutput", 160).getDouble(160);
            manasteeltier = config.get("Configuration Solar Panels(Integration)", "Manasteeltier", 3).getInt(3);
            elementiumgenday = config.get("Configuration Solar Panels(Integration)", "Elementiumgenday", 320).getDouble(320);
            elementiumgennight = config
                    .get("Configuration Solar Panels(Integration)", "Elementiumgennight", 160D)
                    .getDouble(160D);
            elementiumstorage = config.get("Configuration Solar Panels(Integration)", "Elementiumstorage", 1000000).getDouble(
                    1000000);
            elementiumoutput = config.get("Configuration Solar Panels(Integration)", "Elementiumoutput", 160).getDouble(160);
            elementiumtier = config.get("Configuration Solar Panels(Integration)", "Elementiumtier", 4).getInt(4);
            terasteelgenday = config.get("Configuration Solar Panels(Integration)", "Terasteelgenday", 1280).getDouble(1280);
            terasteelgennight = config.get("Configuration Solar Panels(Integration)", "Terasteelgennight", 640).getDouble(640);

            terasteelstorage = config.get("Configuration Solar Panels(Integration)", "Terasteelstorage", 5000000).getDouble(
                    5000000);
            terasteeloutput = config.get("Configuration Solar Panels(Integration)", "Terasteeloutput", 2560).getDouble(2560);
            terasteeltier = config.get("Configuration Solar Panels(Integration)", "Terasteeltier", 5).getInt(5);
            draconicgenday = config.get("Configuration Solar Panels(Integration)", "Draconicgenday", 80D).getDouble(80D);
            draconicstorage = config.get("Configuration Solar Panels(Integration)", "Draconicstorage", 50000D).getDouble(50000D);
            draconicoutput = config.get("Configuration Solar Panels(Integration)", "Draconicoutput", 160).getDouble(160);
            draconictier = config.get("Configuration Solar Panels(Integration)", "Draconictier", 3).getInt(3);
            awakenedgenday = config.get("Configuration Solar Panels(Integration)", "Awakanedgenday", 20480).getDouble(20480);
            awakenedstorage = config.get("Configuration Solar Panels(Integration)", "Awakanedstorage", 1.0E9).getDouble(1.0E9);
            awakenedoutput = config.get("Configuration Solar Panels(Integration)", "Awakanedoutput", 40960).getDouble(40960);
            awakenedtier = config.get("Configuration Solar Panels(Integration)", "Awakanedtier", 7).getInt(7);
            chaosgenday = config.get("Configuration Solar Panels(Integration)", "Chaosgenday", 1325720).getDouble(1325720);
            chaosstorage = config.get("Configuration Solar Panels(Integration)", "Chaosstorage", 6.5E9).getDouble(6.5E9);
            chaosoutput = config.get("Configuration Solar Panels(Integration)", "Chaosoutput", 2651440).getDouble(2651440);
            chaostier = config.get("Configuration Solar Panels(Integration)", "Chaostier", 10).getInt(10);
            armor_maxcharge = config
                    .get("Spectral Armor", "maxcharge exept Improvemed Quantum Body", 100000000)
                    .getInt(100000000);
            armor_transferlimit = config.get("Spectral Armor", "transferlimit", 10000).getInt(10000);
            armor_maxcharge_body = config.get("Spectral Armor", "maxcharge Improvemed Quantum Body", 300000000).getInt(300000000);
            //
            thaumgenday = config.get("Configuration Solar Panels(Integration)", "Thaumgenday", 80).getDouble(80);
            thaumstorage = config.get("Configuration Solar Panels(Integration)", "Thaumstorage", 50000D).getDouble(50000D);
            thaumoutput = config.get("Configuration Solar Panels(Integration)", "Thaumoutput", 160).getDouble(160);
            thaumtier = config.get("Configuration Solar Panels(Integration)", "Thaumtier", 3).getInt(3);
            voidgenday = config.get("Configuration Solar Panels(Integration)", "Voidgenday", 320).getDouble(320);
            voidstorage = config.get("Configuration Solar Panels(Integration)", "Voidstorage", 200000D).getDouble(200000D);
            voidoutput = config.get("Configuration Solar Panels(Integration)", "Voidoutput", 640).getDouble(640);
            voidtier = config.get("Configuration Solar Panels(Integration)", "Voidtier", 4).getInt(4);
            String[] spawnerList = config.getStringList(
                    "Spawn List",
                    "spawner",
                    defaultSpawnerList,
                    "List of names that will be ether accepted or rejected by the spawner depending on the list type"
            );
            Collections.addAll(EntityList, spawnerList);
            String[] dimensionList = config.getStringList(
                    "Dimension List",
                    "spawn ore",
                    new String[]{"0", "-6"},
                    "List of dimensions in which ore will spawn from the mod"
            );
            for (String s : dimensionList) {
                DimensionList.add(Integer.parseInt(s));
            }

            SkeletonType = config.get("spawner", "Enable spawn Wither Skeleton", true).getBoolean(true);

            MikhailOre = config.get("spawn ore", "Enable spawn MikhailOre", true).getBoolean(true);
            AluminiumOre = config.get("spawn ore", "Enable spawn AluminiumOre", true).getBoolean(true);
            VanadiumOre = config.get("spawn ore", "Enable spawn VanadiumOre", true).getBoolean(true);
            TungstenOre = config.get("spawn ore", "Enable spawn TungstenOre", true).getBoolean(true);
            CobaltOre = config.get("spawn ore", "Enable spawn CobaltOre", true).getBoolean(true);
            MagnesiumOre = config.get("spawn ore", "Enable spawn MagnesiumOre", true).getBoolean(true);
            NickelOre = config.get("spawn ore", "Enable spawn NickelOre", true).getBoolean(true);
            PlatiumOre = config.get("spawn ore", "Enable spawn PlatiumOre", true).getBoolean(true);
            TitaniumOre = config.get("spawn ore", "Enable spawn TitaniumOre", true).getBoolean(true);
            ChromiumOre = config.get("spawn ore", "Enable spawn ChromiumOre", true).getBoolean(true);
            SpinelOre = config.get("spawn ore", "Enable spawn SpinelOre", true).getBoolean(true);
            SilverOre = config.get("spawn ore", "Enable spawn SilverOre", true).getBoolean(true);
            ZincOre = config.get("spawn ore", "Enable spawn ZincOre", true).getBoolean(true);
            ManganeseOre = config.get("spawn ore", "Enable spawn ManganeseOre", true).getBoolean(true);
            IridiumOre = config.get("spawn ore", "Enable spawn IridiumOre", true).getBoolean(true);
            GermaniumOre = config.get("spawn ore", "Enable spawn GermaniumOre", true).getBoolean(true);
            CopperOre = config.get("spawn ore", "Enable spawn CopperOre", true).getBoolean(true);
            TinOre = config.get("spawn ore", "Enable spawn TinOre", true).getBoolean(true);
            LeadOre = config.get("spawn ore", "Enable spawn LeadOre", true).getBoolean(true);
            UraniumOre = config.get("spawn ore", "Enable spawn UraniumOre", true).getBoolean(true);

            MagnetiteOre = config.get("spawn ore", "Enable spawn MagnetiteOre", true).getBoolean(true);
            CalaveriteOre = config.get("spawn ore", "Enable spawn CalaveriteOre", true).getBoolean(true);
            GalenaOre = config.get("spawn ore", "Enable spawn GalenaOre", true).getBoolean(true);
            NickeliteOre = config.get("spawn ore", "Enable spawn NickeliteOre", true).getBoolean(true);
            PyriteOre = config.get("spawn ore", "Enable spawn PyriteOre", true).getBoolean(true);
            QuartziteOre = config.get("spawn ore", "Enable spawn QuartziteOre", true).getBoolean(true);
            UraniteOre = config.get("spawn ore", "Enable spawn UraniteOre", true).getBoolean(true);
            AzuriteOre = config.get("spawn ore", "Enable spawn AzuriteOre", true).getBoolean(true);
            RhodoniteOre = config.get("spawn ore", "Enable spawn RhodoniteOre", true).getBoolean(true);
            AlfilditOre = config.get("spawn ore", "Enable spawn AlfilditOre", true).getBoolean(true);
            EuxeniteOre = config.get("spawn ore", "Enable spawn EuxeniteOre", true).getBoolean(true);
            SmithsoniteOre = config.get("spawn ore", "Enable spawn SmithsoniteOre", true).getBoolean(true);
            IlmeniteOre = config.get("spawn ore", "Enable spawn IlmeniteOre", true).getBoolean(true);
            TodorokiteOre = config.get("spawn ore", "Enable spawn TodorokiteOre", true).getBoolean(true);
            FerroaugiteOre = config.get("spawn ore", "Enable spawn FerroaugiteOre", true).getBoolean(true);
            SheeliteeOre = config.get("spawn ore", "Enable spawn SheeliteeOre", true).getBoolean(true);


            AmericiumOre = config.get("spawn ore", "Enable spawn AmericiumOre", true).getBoolean(true);
            NeptuniumOre = config.get("spawn ore", "Enable spawn NeptuniumOre", true).getBoolean(true);
            CuriumOre = config.get("spawn ore", "Enable spawn CuriumOre", true).getBoolean(true);

            RubyOre = config.get("spawn ore", "Enable spawn RubyOre", true).getBoolean(true);
            SapphireOre = config.get("spawn ore", "Enable spawn SapphireOre", true).getBoolean(true);
            TopazOre = config.get("spawn ore", "Enable spawn TopazOre", true).getBoolean(true);


            adv_jetpack_maxenergy = config.get("Configuration jetpacks", "adv_jetpack_maxenergy", 60000).getInt(60000);
            adv_jetpack_transfer = config.get("Configuration jetpacks", "adv_jetpack_transfer energy", 120).getInt(120);
            adv_jetpack_tier = config.get("Configuration jetpacks", "adv_jetpack_tier", 2).getInt(2);

            imp_jetpack_maxenergy = config.get("Configuration jetpacks", "imp_jetpack_maxenergy", 120000).getInt(120000);
            imp_jetpack_transfer = config.get("Configuration jetpacks", "imp_jetpack_transfer energy", 500).getInt(500);
            imp_jetpack_tier = config.get("Configuration jetpacks", "imp_jetpack_tier", 3).getInt(3);

            per_jetpack_maxenergy = config.get("Configuration jetpacks", "per_jetpack_maxenergy", 250000).getInt(250000);
            per_jetpack_transfer = config.get("Configuration jetpacks", "per_jetpack_transfer energy", 1000).getInt(1000);
            per_jetpack_tier = config.get("Configuration jetpacks", "per_jetpack_tier", 4).getInt(4);

            adv_lappack_maxenergy = config.get("Configuration lappacks", "adv_lappack_maxenergy", 25000000).getInt(25000000);
            adv_lappack_transfer = config.get("Configuration lappacks", "adv_lappack_transfer energy", 50000).getInt(50000);
            adv_lappack_tier = config.get("Configuration lappacks", "adv_lappack_tier", 3).getInt(3);

            imp_lappack_maxenergy = config.get("Configuration lappacks", "imp_lappack_maxenergy", 50000000).getInt(50000000);
            imp_lappack_transfer = config.get("Configuration lappacks", "imp_lappack_transfer energy", 100000).getInt(100000);
            imp_lappack_tier = config.get("Configuration lappacks", "imp_lappack_tier", 4).getInt(4);

            per_lappack_maxenergy = config.get("Configuration lappacks", "per_lappack_maxenergy", 100000000).getInt(100000000);
            per_lappack_transfer = config.get("Configuration lappacks", "per_lappack_transfer energy", 500000).getInt(500000);
            per_lappack_tier = config.get("Configuration lappacks", "per_lappack_tier", 5).getInt(5);

            enableonlyvein = config.get("Configuration quntum quarry", "enable work only with veins", false).getBoolean(false);

            advheatswitch_heatStorage1 = config
                    .get("Configuration of upgrade reactors", "advheatswitch_heatStorage1", 5000)
                    .getInt(5000);
            advheatswitch_switchside = config.get("Configuration of upgrade reactors", "advheatswitch_switchside", 40).getInt(40);
            advheatswitch_switchreactor = config
                    .get("Configuration of upgrade reactors", "advheatswitch_switchreactor", 45)
                    .getInt(45);

            impheatswitch_heatStorage1 = config
                    .get("Configuration of upgrade reactors", "impheatswitch_heatStorage1", 5000)
                    .getInt(5000);
            impheatswitch_switchside = config.get("Configuration of upgrade reactors", "impheatswitch_switchside", 60).getInt(60);
            impheatswitch_switchreactor = config
                    .get("Configuration of upgrade reactors", "impheatswitch_switchreactor", 70)
                    .getInt(70);


            advventspread_sidevent = config.get("Configuration of upgrade reactors", "advventspread_sidevent", 5).getInt(5);
            impventspread_sidevent = config.get("Configuration of upgrade reactors", "impventspread_sidevent", 6).getInt(6);

            reactoradvVent_heatStorage1 = config
                    .get("Configuration of upgrade reactors", "reactoradvVent_heatStorage1", 1000)
                    .getInt(1000);
            reactoradvVent_selfvent = config.get("Configuration of upgrade reactors", "reactoradvVent_selfvent", 25).getInt(25);
            reactoradvVent_reactorvent = config.get("Configuration of upgrade reactors", "reactoradvVent_reactorvent", 35).getInt(
                    35);

            reactorimpVent_heatStorage1 = config
                    .get("Configuration of upgrade reactors", "reactorimpVent_heatStorage1", 1500)
                    .getInt(1500);
            reactorimpVent_selfvent = config.get("Configuration of upgrade reactors", "reactorimpVent_selfvent", 30).getInt(30);
            reactorimpVent_reactorvent = config.get("Configuration of upgrade reactors", "reactorimpVent_reactorvent", 25).getInt(
                    25);


            //

            //
        } catch (Exception e) {
            IUCore.log.fatal("Fatal error reading config file.", e);
            throw new RuntimeException(e);
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }


}
