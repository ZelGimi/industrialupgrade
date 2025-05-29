package com.denfop;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.ArrayList;
import java.util.List;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = IUCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    public static final List<String> EntityList = new ArrayList<>();
    public static final List<Integer> DimensionList = new ArrayList<>();
    private static final String[] defaultSpawnerList = new String[]{"ExampleMob1", "ExampleMob2", "ExampleMob3 (these examples " +
            "can be deleted)"};


    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    static ForgeConfigSpec SPEC;


    static List<ForgeConfigSpec.ConfigValue> configValues = new ArrayList<>();
    private static boolean Thaumcraft;
    private static int coefficientrf;

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        ConfigReader configReader = new ConfigReader(configValues);


    }

    public static void loadValues() {
        configValues.add(BUILDER.comment("general").define("Tick update energy system", 20));
        configValues.add(BUILDER.comment("general").define("Tick transfer energy", 1));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Uran233_Cells", 5000));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Uran233_Heat", 1));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Uran233_Power", 3D));
        Thaumcraft = ModList.get().isLoaded("thaumcraft");
        //   DraconicLoaded = ModList.get().isLoaded("draconicevolution");
        //    AvaritiaLoaded = ModList.get().isLoaded("avaritia");
        //   BotaniaLoaded = ModList.get().isLoaded("botania");
        configValues.add(BUILDER.comment("Configuration nano instruments").define("max_energy", 1000000));
        configValues.add(BUILDER.comment("Configuration nano instruments").define("transfer energy", 1000));
        configValues.add(BUILDER.comment("Configuration nano instruments").define("energyPerOperation", 400));
        configValues.add(BUILDER.comment(
                "Configuration nano instruments"
        ).define("energyPerbigHolePowerOperation", 550));

        configValues.add(BUILDER.comment("Configuration quantum instruments").define("max_energy", 10000000));
        configValues.add(BUILDER.comment("Configuration quantum instruments").define("transfer energy", 2500));
        configValues.add(BUILDER.comment("Configuration quantum instruments").define("energyPerOperation", 350));
        configValues.add(BUILDER.comment(
                "Configuration quantum instruments"
        ).define("energyPerbigHolePowerOperation",
                450));

        configValues.add(BUILDER.comment("Configuration spectral instruments").define("max_energy", 50000000));
        configValues.add(BUILDER.comment("Configuration spectral instruments").define("transfer energy", 5000));
        configValues.add(BUILDER.comment("Configuration spectral instruments").define("energyPerOperation", 250));
        configValues.add(BUILDER.comment(
                "Configuration spectral instruments"

        ).define("energyPerbigHolePowerOperation",
                370));


        configValues.add(BUILDER.comment("Improved NanoArmor").define("NanoHelmet", 1E7));
        configValues.add(BUILDER.comment("Improved NanoArmor").define("NanoBodyarmor", 3E7));
        configValues.add(BUILDER.comment("Improved NanoArmor").define("NanoLeggings", 1E7));
        configValues.add(BUILDER.comment("Improved NanoArmor").define("NanoBoots", 1E7));
        configValues.add(BUILDER.comment("Improved NanoArmor").define("NanoTransfer", 5000D));
        configValues.add(BUILDER.comment("Improved NanoArmor").define("NanoTier", 5));


        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Einsteinium_Cells", 25000));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Einsteinium_Heat", 4));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Einsteinium_Power", 23D));


        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Berkelium_Cells", 22500));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Berkelium_Heat", 3));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Berkelium_Power", 20D));


        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Mendelevium_Cells", 30000));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Mendelevium_Heat", 4));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Mendelevium_Power", 26D));


        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("California_Cells", 20000));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("California_Heat", 3));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("California_Power", 18D));


        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Curium_Cells", 8000));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Curium_Heat", 2));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Curium_Power", 9.5D));


        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Neptunium_Cells", 15000));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Neptunium_Heat", 1));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Neptunium_Power", 3.5D));


        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Americium_Cells", 5000));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Americium_Heat", 1));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Americium_Power", 4.5D));

        configValues.add(BUILDER.comment("MultiMatter").define("amplifierSlot", true));

        configValues.add(BUILDER.comment("Energy Bow").define("Nano Transfer energy", 5000));
        configValues.add(BUILDER.comment("Energy Bow").define("Quantum Transfer energy", 25000));
        configValues.add(BUILDER.comment("Energy Bow").define("Spectral Transfer energy", 50000));
        configValues.add(BUILDER.comment("Energy Bow").define("Spectral Tier", 4));
        configValues.add(BUILDER.comment("Energy Bow").define("Quantum Tier", 3));
        configValues.add(BUILDER.comment("Energy Bow").define("Nano Tier", 2));
        configValues.add(BUILDER.comment("Energy Bow").define("Nano MaxEnergy", 50000));
        configValues.add(BUILDER.comment("Energy Bow").define("Quantum MaxEnergy", 80000));
        configValues.add(BUILDER.comment("Energy Bow").define("Spectral MaxEnergy", 150000));

        configValues.add(BUILDER.comment("Configuration Energy storages").define("Quarkstorage", 409.6E9D));
        configValues.add(BUILDER.comment("Configuration Energy storages").define("Quarkoutput", 61988864D));
        configValues.add(BUILDER.comment("Configuration Energy storages").define("Quarktier", 11D));

        configValues.add(BUILDER.comment("Configuration Energy storages").define("Gravitonstorage", 102.4E9D));
        configValues.add(BUILDER.comment("Configuration Energy storages").define("Gravitonoutput", 15497216D));
        configValues.add(BUILDER.comment("Configuration Energy storages").define("Gravitontier", 10D));

        configValues.add(BUILDER.comment("Configuration Energy storages").define("Hadronstorage", 25.6E9D));
        configValues.add(BUILDER.comment("Configuration Energy storages").define("Hadronoutput", 3874304D));
        configValues.add(BUILDER.comment("Configuration Energy storages").define("Hadrontier", 9D));


        configValues.add(BUILDER.comment("Configuration Energy storages").define("Barionstorage", 6.4E9D));
        configValues.add(BUILDER.comment("Configuration Energy storages").define("Barionoutput", 968576D));
        configValues.add(BUILDER.comment("Configuration Energy storages").define("Bariontier", 8D));
        configValues.add(BUILDER.comment("Configuration Energy storages").define("Perfectstorage", 1.6E9D));
        configValues.add(BUILDER.comment("Configuration Energy storages").define("Perfectoutput", 242144D));
        configValues.add(BUILDER.comment("Configuration Energy storages").define("Perfecttier", 7D));
        configValues.add(BUILDER.comment("Basic Mechanisms").define("exp storage", 500));
        configValues.add(BUILDER.comment("Quantum Quarry").define("energy consume in QE (1 QE = 16 EF)", 25000));
        configValues.add(BUILDER.comment("general").define("coefficient rf", 4));
        if (coefficientrf < 1) {
            coefficientrf = 4;
        }
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Wither Skeleton skull", 4000000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Nether Star", 250000000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Iridium Ore", 10000000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Proton", 15500000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Iridium ingot", 2500000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Photon ingot", 12000000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Gunpowder", 70000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Gravel", 45000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Diamond", 1000000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Nickel ingot", 450000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Gold ingot", 450000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Silver ingot", 800000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Tungsten ingot", 700000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Spinel ingot", 800000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Mikhalov ingot", 900000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Chromium ingot", 600000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Platium ingot", 800000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Advanced ingot", 1500D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Hybrid core", 11720D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Ultimate core", 60000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Quantum core", 300000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Spectral core", 1500000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Proton core", 7500000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Singular core", 45000000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Diffraction core", 180000000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Photnic core", 900000000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Neutron Core", 2700000000D));

        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Barion Core", 4500000000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Hadron Core", 9000000000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Graviton Core", 12000000000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Kvark Core", 21000000000D));

        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Sun lense", 25000000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Rain lense", 25000000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Nether lense", 25000000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Night lense", 25000000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Earth lense", 25000000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("End lense", 25000000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Aer lense", 25000000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Photon", 1450000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Magnesium ingot", 700000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer").define("Caravky ingot", 900000D));
        configValues.add(BUILDER.comment("Crafts Molecular Transformer")
                .define("Iridium ingot(from Iridium Ore IUCore)", 50000D));
        configValues.add(BUILDER.comment("general").define("Disable Update Check ", false));
        configValues.add(BUILDER.comment("general").define("Enable prompt about information a panel", true));

        configValues.add(BUILDER.comment("Solid Matter Generator Storage").define("Matter Generator Storage", 5E7D));
        configValues.add(BUILDER.comment("Transformer mode").define("Transformer mode", true));
        configValues.add(BUILDER.comment("spawn ore").define("Spawn Thorium Ore", true));
        configValues.add(BUILDER.comment("UltimateDrill").define("Mode 0 efficiency", 80));
        configValues.add(BUILDER.comment("UltimateDrill").define("Mode 1 efficiency", 60));
        configValues.add(BUILDER.comment("UltimateDrill").define("Mode 2 efficiency", 60));
        configValues.add(BUILDER.comment("UltimateDrill").define("Mode 3 efficiency", 60));
        configValues.add(BUILDER.comment("UltimateDrill").define("Mode 4 efficiency", 60));
        configValues.add(BUILDER.comment("UltimateDrill").define("maxCharge", 15000000));
        configValues.add(BUILDER.comment("UltimateDrill").define("tier", 2));
        configValues.add(BUILDER.comment("UltimateDrill").define("transfer Limit", 500));
        configValues.add(BUILDER.comment("UltimateDrill").define("energyPerOperation", 160));
        configValues.add(BUILDER.comment("UltimateDrill").define("energyPerLowOperation", 80));
        configValues.add(BUILDER.comment("UltimateDrill").define("energyPerBigHolesOperation (3x3)", 300));
        configValues.add(BUILDER.comment("UltimateDrill")
                .define("energyPerUltraBigHolesOperation (5x5)", 500));
        configValues.add(BUILDER.comment("UltimateDrill").define(
                "energyPerUltraBigHolesOperation (7x7)", 700));

        configValues.add(BUILDER.comment("general").define("Maximum amount of ore in a vein", 20000));
        configValues.add(BUILDER.comment("Transformer mode")
                .define("unchecking the tier", false));
        configValues.add(BUILDER.comment("Transformer mode")
                .define("unlimiting the conduction of energy in the cable", false));
        configValues.add(BUILDER.comment(
                "Transformer mode"
        ).define("Enable explosion from mechanisms is on (if enable transformer mode) ",
                true));
        configValues.add(BUILDER.comment(
                "Transformer mode"
        ).define(
                "Enable losing in cables is on (if enable transformer mode) ",
                true));
        configValues.add(BUILDER.comment("Quantum Saber").define("QuantumSaber Damage Active", 40));
        configValues.add(BUILDER.comment("Quantum Saber").define("QuantumSaber Damage Not Active", 8));
        configValues.add(BUILDER.comment("Quantum Saber").define("SpectralSaber max Charge", 200000));
        configValues.add(BUILDER.comment("Quantum Saber").define("SpectralSaber transfer Limit", 15000));
        configValues.add(BUILDER.comment("Quantum Saber").define("SpectralSaber tier", 4));
        configValues.add(BUILDER.comment("Neutronium generator").define(
                "Consumes energy to make 1 mb neutronim", 16250000.0D));
        configValues.add(BUILDER.comment("Spectral Saber").define("SpectralSaber max Charge", 600000));
        configValues.add(BUILDER.comment("Spectral Saber").define("SpectralSaber transfer Limit", 40000));
        configValues.add(BUILDER.comment("Spectral Saber").define("SpectralSaber Damage Active", 60));
        configValues.add(BUILDER.comment("Spectral Saber").define("SpectralSaber Damage Not Active", 12));
        configValues.add(BUILDER.comment("Configuration Energy storages").define("Improved denergy transfer Et/t", 8192));
        configValues.add(BUILDER.comment("Configuration Energy storages").define("Improved energy storage", 100000000));
        configValues.add(BUILDER.comment("Configuration Energy storages").define("Improved tier", 5));

        configValues.add(BUILDER.comment("Configuration Energy storages").define("Advancedtier", 6));
        configValues.add(BUILDER.comment("Configuration Energy storages").define("Advancedenergy transfer Et/t", 32768));
        configValues.add(BUILDER.comment("Configuration Energy storages").define("Advancedenergy storage", 400000000));

        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)")
                .define("InfinityGenDay", 21211520D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define(
                "InfinityGenNight", 21211520D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)")
                .define("InfinityOutput", 42423040D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("InfinityStorage", 2.5E10));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Neutrontier", 10));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("NeutronGenDay", 1325720D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)")
                .define("NeutronGenNight", 1325720D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("NeutronOutput", 2651440D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("NeutronStorage", 6.5E9));
        configValues.add(BUILDER.comment("Modules").define("percent output", 5));
        configValues.add(BUILDER.comment("Modules").define("percent storage", 5));
        configValues.add(BUILDER.comment("Modules").define("percent generation day", 5));
        configValues.add(BUILDER.comment("Modules").define("percent generation night", 5));
        configValues.add(BUILDER.comment("Battery").define("MaxEnergy", 100000000));
        configValues.add(BUILDER.comment("Integration").define("Integration The One Probe", true));
        configValues.add(BUILDER.comment("Integration").define("Integration Thaumcraft", true));
        configValues.add(BUILDER.comment("Integration").define("Integration Draconic Evolution", true));
        configValues.add(BUILDER.comment("Integration").define("Integration Botania", true));
        configValues.add(BUILDER.comment("Integration").define("Integration Avaritia", true));
        configValues.add(BUILDER.comment("Integration").define("Integration ProjectE", true));
        //TODO config solar panels
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("AdvancedSPGenDay", 5D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("AdvancedSPStorage", 3200D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("AdvancedSPOutput", 10D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("HybrydSPGenDay", 20D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("HybrydSPStorage", 20000D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("HybrydSPOutput", 40D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("PerfectSPGenDay", 80D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("PerfectSPStorage", 200000D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("PerfectSPOutput", 160D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("QuantumSPGenDay", 320D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("QuantumSPStorage", 1000000D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("QuantumSPOutput", 640D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("SpectralSPGenDay", 1280D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("SpectralSPOutput", 2560D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("SpectralSPStorage", 5000000D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("ProtonGenDay", 5120D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("ProtonOutput", 10240D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("Protonstorage", 50000000D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("SingularSPGenDay", 20480D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("SingularSPOutput", 40960D));
        configValues.add(BUILDER.comment("Configuration Solar Panels")
                .define("SingularSPStorage", 1000000000D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("DiffractionPanelGenDay", 81920D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define(
                "DiffractionPanelStorage", 1500000000D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("DiffractionPanelOutput", 163840D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("PhotonicPanelGenDay", 327680D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("PhotonicPanelOutput", 655360D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define(
                "PhotonicPanelStorage", 5000000000D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("NeutronPanelGenDay", 1310720D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("NeutronPanelOutput", 2621440D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define(
                "NeutronPanelStorage", 6500000000D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("BarionGenDay", 5242880D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("BarionOutput", 10485760D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("BarionStorage", 10000000000D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("HadrionGenDay", 20971520D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("HadrionOutput", 41943040D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("HadrionStorage", 25000000000D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("GravitonGenDay", 83886080D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("GravitonOutput", 167772160D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("GravitonStorage", 250000000000D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("KvarkGenDay", 335544320D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("KvarkOutput", 671088640D));
        configValues.add(BUILDER.comment("Configuration Solar Panels").define("KvarkStorage", 2500000000000D));


        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("TerrasteelRod_Heat", 1));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("TerrasteelRod_Cells", 20000));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("TerrasteelRod_Power", 2));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Thorium_Heat", 1));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Thorium_Cells", 10000));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Thorium_Power", 3));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Proton_Heat", 1));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Proton_Cells", 30000));
        configValues.add(BUILDER.comment("Configuration of reactor`s rods").define("Proton_Power", 6));
        configValues.add(BUILDER.comment("Configuration rotors").define("Iridium Radius", 11));
        configValues.add(BUILDER.comment("Configuration rotors")
                .define("Iridium durability", (int) (172800 / 1.4D)));
        configValues.add(BUILDER.comment("Configuration rotors").define("Iridium efficiency", 2));
        configValues.add(BUILDER.comment("Configuration rotors").define("Iridium minWindStrength", 25));
        configValues.add(BUILDER.comment("Configuration rotors").define("Iridium maxWindStrength", 110));
        configValues.add(BUILDER.comment("Configuration rotors").define("Compress Iridium Radius", 11));
        configValues.add(BUILDER.comment("Configuration rotors")
                .define("Compress Iridium durability", (int) (172800 / 1.2D)));
        configValues.add(BUILDER.comment("Configuration rotors").define("Compress Iridium efficiency", 4));

        configValues.add(BUILDER.comment("Configuration rotors").define("Compress Iridium minWindStrength", 25));
        configValues.add(BUILDER.comment("Configuration rotors").define("Compress Iridium maxWindStrength", 110));
        configValues.add(BUILDER.comment("Configuration rotors").define("Spectral Radius", 11));
        configValues.add(BUILDER.comment("Configuration rotors")
                .define("Spectral durability", (int) (172800 / 1.1D)));
        configValues.add(BUILDER.comment("Configuration rotors").define("Spectral efficiency", 8));
        configValues.add(BUILDER.comment("Configuration rotors").define("Spectral minWindStrength", 25));
        configValues.add(BUILDER.comment("Configuration rotors").define("Spectral maxWindStrength", 110));
        configValues.add(BUILDER.comment("Spectral Armor").define("Allow Streak", true));
        configValues.add(BUILDER.comment("Configuration rotors").define("Mythical Radius", 11));
        configValues.add(BUILDER.comment("Configuration rotors").define("Mythical durability", 3456000));
        configValues.add(BUILDER.comment("Configuration rotors").define("Mythical efficiency", 16));
        configValues.add(BUILDER.comment("Configuration rotors").define("Mythical minWindStrength", 25));
        configValues.add(BUILDER.comment("Configuration rotors").define("Mythical maxWindStrength", 110));
        configValues.add(BUILDER.comment("Configuration rotors").define("Neutron Radius", 11));
        configValues.add(BUILDER.comment("Configuration rotors").define("Neutron durability", 27648000));
        configValues.add(BUILDER.comment("Configuration rotors").define("Neutron efficiency", 64));
        configValues.add(BUILDER.comment("Configuration rotors").define("Neutron minWindStrength", 25));
        configValues.add(BUILDER.comment("Configuration rotors").define("Neutron maxWindStrength", 110));

        configValues.add(BUILDER.comment("Configuration rotors").define("Photon Radius", 11));
        configValues.add(BUILDER.comment("Configuration rotors").define("Photon durability", 691200));
        configValues.add(BUILDER.comment("Configuration rotors").define("Photon efficiency", 32));
        configValues.add(BUILDER.comment("Configuration rotors").define("Photon minWindStrength", 25));
        configValues.add(BUILDER.comment("Configuration rotors").define("Photon maxWindStrength", 110));
        configValues.add(BUILDER.comment("Unifier panels").define("Limit", 2));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Manasteelgenday", 80D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Manasteelgennight", 40D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define(
                "Manasteelstorage", 200000D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Manasteeloutput", 160D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Manasteeltier", 3));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Elementiumgenday", 320D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)")
                .define("Elementiumgennight", 160D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define(
                "Elementiumstorage", 1000000D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Elementiumoutput", 160D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Elementiumtier", 4));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Terasteelgenday", 1280D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Terasteelgennight", 640D));

        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define(
                "Terasteelstorage", 5000000D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Terasteeloutput", 2560D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Terasteeltier", 5));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Draconicgenday", 80D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Draconicstorage", 50000D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Draconicoutput", 160D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Draconictier", 3));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Awakanedgenday", 20480D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Awakanedstorage", 1.0E9));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Awakanedoutput", 40960D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Awakanedtier", 7));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Chaosgenday", 1325720D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Chaosstorage", 6.5E9));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Chaosoutput", 2651440D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Chaostier", 10));
        configValues.add(BUILDER.comment("Spectral Armor")
                .define("maxcharge exept Improvemed Quantum Body", 100000000));
        configValues.add(BUILDER.comment("Spectral Armor").define("transferlimit", 10000));
        configValues.add(BUILDER.comment("Spectral Armor").define("maxcharge Improvemed Quantum Body", 300000000));
        //
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Thaumgenday", 80D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Thaumstorage", 50000D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Thaumoutput", 160D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Thaumtier", 3));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Voidgenday", 320D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Voidstorage", 200000D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Voidoutput", 640D));
        configValues.add(BUILDER.comment("Configuration Solar Panels(Integration)").define("Voidtier", 4));

        configValues.add(BUILDER.comment("Configuration jetpacks").define("adv_jetpack_maxenergy", 60000));
        configValues.add(BUILDER.comment("Configuration jetpacks").define("adv_jetpack_transfer energy", 120));
        configValues.add(BUILDER.comment("Configuration jetpacks").define("adv_jetpack_tier", 2));

        configValues.add(BUILDER.comment("Configuration jetpacks").define("imp_jetpack_maxenergy", 120000));
        configValues.add(BUILDER.comment("Configuration jetpacks").define("imp_jetpack_transfer energy", 500));
        configValues.add(BUILDER.comment("Configuration jetpacks").define("imp_jetpack_tier", 3));

        configValues.add(BUILDER.comment("Configuration jetpacks").define("per_jetpack_maxenergy", 250000));
        configValues.add(BUILDER.comment("Configuration jetpacks").define("per_jetpack_transfer energy", 1000));
        configValues.add(BUILDER.comment("Configuration jetpacks").define("per_jetpack_tier", 4));

        configValues.add(BUILDER.comment("Configuration lappacks").define("adv_lappack_maxenergy", 25000000));
        configValues.add(BUILDER.comment("Configuration lappacks").define("adv_lappack_transfer energy", 50000));
        configValues.add(BUILDER.comment("Configuration lappacks").define("adv_lappack_tier", 3));

        configValues.add(BUILDER.comment("Configuration lappacks").define("imp_lappack_maxenergy", 50000000));
        configValues.add(BUILDER.comment("Configuration lappacks").define("imp_lappack_transfer energy", 100000));
        configValues.add(BUILDER.comment("Configuration lappacks").define("imp_lappack_tier", 4));

        configValues.add(BUILDER.comment("Configuration lappacks").define("per_lappack_maxenergy", 100000000));
        configValues.add(BUILDER.comment("Configuration lappacks").define("per_lappack_transfer energy", 500000));
        configValues.add(BUILDER.comment("Configuration lappacks").define("per_lappack_tier", 5));

        configValues.add(BUILDER.comment("Configuration quntum quarry").define("enable work only with veins", false));


        SPEC = BUILDER.build();
    }
}
