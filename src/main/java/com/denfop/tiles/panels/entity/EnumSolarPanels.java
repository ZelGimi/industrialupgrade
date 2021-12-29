package com.denfop.tiles.panels.entity;


import com.denfop.Config;
import com.denfop.IUItem;
import com.denfop.integration.avaritia.AvaritiaIntegration;
import com.denfop.integration.botania.BotaniaIntegration;
import com.denfop.integration.compactsolar.CompactSolarIntegration;
import com.denfop.integration.de.DraconicIntegration;
import com.denfop.integration.thaumcraft.ThaumcraftIntegration;
import com.denfop.integration.wireless.WirelessIntegration;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Loader;

public enum EnumSolarPanels {

    ADVANCED_SOLAR_PANEL(
            null,
            IUItem.blockpanel,
            0,
            "Advanced Solar Panel",
            "industrialupgrade.machines.advanced_solar_panel",
            1,
            Config.advGenDay,
            Config.advStorage,
            Config.advOutput,
            true,
            "default",
            "asp_top",
            true
    ),
    HYBRID_SOLAR_PANEL(
            ADVANCED_SOLAR_PANEL,
            IUItem.blockpanel,
            1,
            "Hybrid Solar Panel",
            "industrialupgrade.machines.hybrid_solar_panel",
            2,
            Config.hGenDay,
            Config.hStorage,
            Config.hOutput,
            true,
            "default",
            "hsp_top",
            true
    ),
    PERFECT_SOLAR_PANEL(
            HYBRID_SOLAR_PANEL,
            IUItem.blockpanel,
            2,
            "Perfect Solar Panel",
            "industrialupgrade.machines.ultimate_solar_panel",
            3,
            Config.uhGenDay,
            Config.uhStorage,
            Config.uhOutput,
            true,
            "default",
            "usp_top",
            true
    ),
    QUANTUM_SOLAR_PANEL(
            PERFECT_SOLAR_PANEL,
            IUItem.blockpanel,
            3,
            "Quantum Solar Panel",
            "industrialupgrade.machines.quantum_solar_panel",
            4,
            Config.qpGenDay,
            Config.qpStorage,
            Config.qpOutput,
            true,
            "default",
            "qsp_top",
            true
    ),
    SPECTRAL_SOLAR_PANEL(
            QUANTUM_SOLAR_PANEL,
            IUItem.blockpanel,
            4,
            "Spectral Solar Panel",
            "industrialupgrade.machines.spectral_solar_panel",
            5,
            Config.spectralpanelGenDay,
            Config.spectralpanelstorage,
            Config.spectralpanelOutput,
            true,
            "default",
            "spsp_top",
            true
    ),
    PROTON_SOLAR_PANEL(
            SPECTRAL_SOLAR_PANEL,
            IUItem.blockpanel,
            5,
            "Proton Solar Panel",
            "industrialupgrade.machines.proton_solar_panel",
            6,
            Config.protongenDay,
            Config.protonstorage,
            Config.protonOutput,
            true,
            "default",
            "psp_top",
            true
    ),
    SINGULAR_SOLAR_PANEL(
            PROTON_SOLAR_PANEL,
            IUItem.blockpanel,
            6,
            "Singular Solar Panel",
            "industrialupgrade.machines.singular_solar_panel",
            7,
            Config.singularpanelGenDay,
            Config.singularpanelstorage,
            Config.singularpanelOutput,
            true,
            "default",
            "ssp_top",
            true
    ),
    DIFFRACTION_SOLAR_PANEL(
            SINGULAR_SOLAR_PANEL,
            IUItem.blockpanel,
            7,
            "Diffraction Solar Panel",
            "industrialupgrade.machines.admin_solar_panel",
            8,
            Config.adminpanelGenDay,
            Config.AdminpanelStorage,
            Config.AdminpanelOutput,
            true,
            "default",
            "admsp_model",
            true
    ),
    PHOTONIC_SOLAR_PANEL(
            DIFFRACTION_SOLAR_PANEL,
            IUItem.blockpanel,
            8,
            "Photon Solar Panel",
            "industrialupgrade.machines.photonic_solar_panel",
            9,
            Config.photonicpanelGenDay,
            Config.photonicpanelStorage,
            Config.photonicpanelOutput,
            true,
            "default",
            "phsp_top",
            true
    ),
    NEUTRONIUN_SOLAR_PANEL(
            PHOTONIC_SOLAR_PANEL,
            IUItem.blockpanel,
            9,
            "Neutron Solar Panel",
            "industrialupgrade.machines.neutronium_solar_panel",
            10,
            Config.neutronpanelGenDay,
            Config.neutronpanelStorage,
            Config.neutronpanelOutput,
            true,
            "default",
            "nsp_top",
            true
    ),
    BARION_SOLAR_PANEL(
            NEUTRONIUN_SOLAR_PANEL,
            IUItem.blockpanel,
            10,
            "Barion Solar Panel",
            "industrialupgrade.machines.barion_solar_panel",
            11,
            Config.barGenDay,
            Config.barStorage,
            Config.barOutput,
            true,
            "default",
            "bsp_top",
            true
    ),
    HADRON_SOLAR_PANEL(
            BARION_SOLAR_PANEL,
            IUItem.blockpanel,
            11,
            "Hadron Solar Panel",
            "industrialupgrade.machines.hadron_solar_panel",
            12,
            Config.adrGenDay,
            Config.adrStorage,
            Config.adrOutput,
            true,
            "default",
            "adsp_top",
            true
    ),
    GRAVITON_SOLAR_PANEL(
            HADRON_SOLAR_PANEL,
            IUItem.blockpanel,
            12,
            "Graviton Solar Panel",
            "industrialupgrade.machines.graviton_solar_panel",
            13,
            Config.graGenDay,
            Config.graStorage,
            Config.graOutput,
            true,
            "default",
            "grasp_top",
            true
    ),
    QUARK_SOLAR_PANEL(
            GRAVITON_SOLAR_PANEL,
            IUItem.blockpanel,
            13,
            "Kvark Solar Panel",
            "industrialupgrade.machines.quark_solar_panel",
            14,
            Config.kvrGenDay,
            Config.kvrStorage,
            Config.kvrOutput,
            true,
            "default",
            "kvsp_top",
            true
    ),
    DRACONIC_SOLAR_PANEL(
            HYBRID_SOLAR_PANEL,
            Config.registerDraconicPanels && Config.DraconicLoaded && Config.Draconic ?
                    DraconicIntegration.blockDESolarPanel : null,
            0,
            "Draconian Solar Panel",
            "industrialupgrade.draconicpanel.draconium_solar_panel",
            Config.draconictier,
            Config.draconicgenday,
            Config.draconicstorage,
            Config.draconicoutput,
            Config.registerDraconicPanels && Config.DraconicLoaded && Config.Draconic,
            "draconic",
            "draconium_top",
            true
    ),
    AWAKENED_SOLAR_PANEL(
            DRACONIC_SOLAR_PANEL,
            Config.registerDraconicPanels && Config.DraconicLoaded && Config.Draconic ?
                    DraconicIntegration.blockDESolarPanel : null,
            1,
            "Awakened Solar Panel",
            "industrialupgrade.draconicpanel.awakened_solar_panel",
            Config.awakenedtier,
            Config.awakenedgenday,
            Config.awakenedstorage,
            Config.awakenedoutput,
            Config.registerDraconicPanels && Config.DraconicLoaded && Config.Draconic,
            "draconic",
            "awakened_top",
            true
    ),
    CHAOTIC_SOLAR_PANEL(
            AWAKENED_SOLAR_PANEL,
            Config.registerDraconicPanels && Config.DraconicLoaded && Config.Draconic ?
                    DraconicIntegration.blockDESolarPanel : null,
            2,
            "Chaotic Solar Panel",
            "industrialupgrade.draconicpanel.chaotic_solar_panel",
            Config.chaostier,
            Config.chaosgenday,
            Config.chaosstorage,
            Config.chaosoutput,
            Config.registerDraconicPanels && Config.DraconicLoaded && Config.Draconic,
            "draconic",
            "chaotic_top",
            true
    ),
    MANASTEEL_SOLAR_PANEL(
            ADVANCED_SOLAR_PANEL,
            BotaniaIntegration.blockBotSolarPanel,
            0,
            "Manasteel Solar Panel",
            "industrialupgrade.botaniapanel.manasteel_solar_panel",
            Config.manasteeltier,
            Config.manasteelgenday,
            Config.manasteelstorage,
            Config.manasteeloutput,
            Config.BotaniaLoaded && Config.Botania,
            "botania",
            "manasteel_top",
            true
    ),
    ELEMENTUM_SOLAR_PANEL(
            MANASTEEL_SOLAR_PANEL,
            BotaniaIntegration.blockBotSolarPanel,
            1,
            "Elementum Solar Panel",
            "industrialupgrade.botaniapanel.elementum_solar_panel",
            Config.elementiumtier,
            Config.elementiumgenday,
            Config.elementiumstorage,
            Config.elementiumoutput,
            Config.BotaniaLoaded && Config.Botania,
            "botania",
            "elementium_top",
            true
    ),
    TERRASTEEL_SOLAR_PANEL(
            ELEMENTUM_SOLAR_PANEL,
            BotaniaIntegration.blockBotSolarPanel,
            2,
            "Terrasteel Solar Panel",
            "industrialupgrade.botaniapanel.terrasteel_solar_panel",
            Config.terasteeltier,
            Config.terasteelgenday,
            Config.terasteelstorage,
            Config.terasteeloutput,
            Config.BotaniaLoaded && Config.Botania,
            "botania",
            "terasteel_top",
            true
    ),

    NEUTRONIUM_SOLAR_PANEL_AVARITIA(
            PHOTONIC_SOLAR_PANEL,
            Config.AvaritiaLoaded && Config.Avaritia ? AvaritiaIntegration.blockAvSolarPanel : null,
            0,
            "Neutron Solar Panel Avaritia",
            "industrialupgrade.avaritiapanel.neutron_solar_panel_av",
            Config.tier,
            Config.neutrongenday,
            Config.neutronStorage,
            Config.neutronOutput,
            Config.AvaritiaLoaded && Config.Avaritia,
            "avaritia",
            "neutronium_top",
            false
    ),
    INFINITY_SOLAR_PANEL(
            NEUTRONIUM_SOLAR_PANEL_AVARITIA,
            Config.AvaritiaLoaded && Config.Avaritia ? AvaritiaIntegration.blockAvSolarPanel : null,
            1,
            "Infinity Solar Panels",
            "industrialupgrade.avaritiapanel.infinity_solar_panel",
            12,
            Config.InfinityGenDay,
            Config.InfinityStorage,
            Config.InfinityOutput,
            Config.AvaritiaLoaded && Config.Avaritia,
            "avaritia",
            "infinity_top",
            false
    ),
    THAUM_SOLAR_PANEL(HYBRID_SOLAR_PANEL, ThaumcraftIntegration.blockThaumSolarPanel, 0, "Thaum Solar Panel",
            "industrialupgrade.thaumcraftpanel.thaum_solar_panel", Config.thaumtier, Config.thaumgenday, Config.thaumstorage,
            Config.thaumoutput, Config.thaumcraft && Config.Thaumcraft, "thaumcraft", "thaum_model", true
    ),
    VOID_SOLAR_PANEL(
            THAUM_SOLAR_PANEL,
            ThaumcraftIntegration.blockThaumSolarPanel,
            1,
            "Void Solar Panel",
            "industrialupgrade" +
                    ".thaumcraftpanel.void_solar_panel",
            Config.voidtier,
            Config.voidgenday,
            Config.voidstorage,
            Config.voidoutput,
            Config.thaumcraft && Config.Thaumcraft,
            "thaumcraft",
            "void_model",
            true
    ),

    LV_SOLAR_PANEL(
            null,
            Loader.isModLoaded("compactsolars") ? CompactSolarIntegration.solar : null,
            0,
            null,
            "tile.compactsolars:low_voltage_block",
            1,
            8,
            64,
            8,
            false,
            "compact",
            "lv_model",
            false
    ),
    HV_SOLAR_PANEL(LV_SOLAR_PANEL, Loader.isModLoaded("compactsolars") ? CompactSolarIntegration.solar : null, 1, null, "tile" +
            ".compactsolars:medium_voltage_block", 2, 64, 256, 64, false, "compact", "mv_model", false),
    MV_SOLAR_PANEL(HV_SOLAR_PANEL, Loader.isModLoaded("compactsolars") ? CompactSolarIntegration.solar : null, 2, null, "tile" +
            ".compactsolars:high_voltage_block", 3, 512, 1024, 512, false, "compact", "hv_model", false),
    ADVANCED_SOLAR_PANEL_W(
            null,
            Loader.isModLoaded("wirelesstools") ? WirelessIntegration.panel : null,
            0,
            null,
            "wirelesstools.machines.advanced_solar_panel_personal",
            1,
            Config.advGenDay,
            Config.advStorage,
            Config.advOutput,
            false,
            "wireless",
            "asp_top_w",
            false
    ),
    HYBRID_SOLAR_PANEL_W(
            ADVANCED_SOLAR_PANEL_W,
            Loader.isModLoaded("wirelesstools") ? WirelessIntegration.panel : null,
            0,
            null,
            "wirelesstools.machines.hybrid_solar_panel_personal",
            2,
            Config.hGenDay,
            Config.hStorage,
            Config.hOutput,
            false,
            "wireless",
            "hsp_top_w",
            false
    ),
    PERFECT_SOLAR_PANEL_W(
            HYBRID_SOLAR_PANEL_W,
            Loader.isModLoaded("wirelesstools") ? WirelessIntegration.panel : null,
            0,
            null,
            "wirelesstools.machines.ultimate_solar_panel_personal",
            3,
            Config.uhGenDay,
            Config.uhStorage,
            Config.uhOutput,
            false,
            "wireless",
            "usp_top_w",
            false
    ),
    QUANTUM_SOLAR_PANEL_W(
            PERFECT_SOLAR_PANEL_W,
            Loader.isModLoaded("wirelesstools") ? WirelessIntegration.panel : null,
            0,
            null,
            "wirelesstools.machines.quantum_solar_panel_personal",
            4,
            Config.qpGenDay,
            Config.qpStorage,
            Config.qpOutput,
            false,
            "wireless",
            "qsp_top_w",
            false
    ),
    SPECTRAL_SOLAR_PANEL_W(
            QUANTUM_SOLAR_PANEL_W,
            Loader.isModLoaded("wirelesstools") ? WirelessIntegration.panel : null,
            0,
            null,
            "wirelesstools.machines.spectral_solar_panel_personal",
            5,
            Config.spectralpanelGenDay,
            Config.spectralpanelstorage,
            Config.spectralpanelOutput,
            false,
            "wireless",
            "spsp_top_w",
            false
    ),
    PROTON_SOLAR_PANEL_W(
            SPECTRAL_SOLAR_PANEL_W,
            Loader.isModLoaded("wirelesstools") ? WirelessIntegration.panel : null,
            0,
            null,
            "wirelesstools.machines.proton_solar_panel_personal",
            6,
            Config.protongenDay,
            Config.protonstorage,
            Config.protonOutput,
            false,
            "wireless",
            "psp_top_w",
            false
    ),
    SINGULAR_SOLAR_PANEL_W(
            PROTON_SOLAR_PANEL_W,
            Loader.isModLoaded("wirelesstools") ? WirelessIntegration.panel : null,
            0,
            null,
            "wirelesstools.machines.singular_solar_panel_personal",
            7,
            Config.singularpanelGenDay,
            Config.singularpanelstorage,
            Config.singularpanelOutput,
            false,
            "wireless",
            "ssp_top_w",
            false
    ),
    DIFFRACTION_SOLAR_PANEL_W(
            SINGULAR_SOLAR_PANEL_W,
            Loader.isModLoaded("wirelesstools") ? WirelessIntegration.panel : null,
            0,
            null,
            "wirelesstools.machines.absorbing_solar_panel_personal",
            8,
            Config.adminpanelGenDay,
            Config.AdminpanelStorage,
            Config.AdminpanelOutput,
            false,
            "wireless",
            "admsp_model_w",
            false
    ),
    PHOTONIC_SOLAR_PANEL_W(
            DIFFRACTION_SOLAR_PANEL_W,
            Loader.isModLoaded("wirelesstools") ? WirelessIntegration.panel : null,
            0,
            null,
            "wirelesstools.machines.photonic_solar_panel_personal",
            9,
            Config.photonicpanelGenDay,
            Config.photonicpanelStorage,
            Config.photonicpanelOutput,
            false,
            "wireless_w",
            "phsp_top_w",
            false
    ),
    NEUTRONIUN_SOLAR_PANEL_W(
            PHOTONIC_SOLAR_PANEL_W,
            Loader.isModLoaded("wirelesstools") ? WirelessIntegration.panel : null,
            0,
            null,
            "wirelesstools.machines.neutron_solar_panel_personal",
            10,
            Config.neutronpanelGenDay,
            Config.neutronpanelStorage,
            Config.neutronpanelOutput,
            true,
            "wireless",
            "nsp_top_w",
            false
    ),
    SOLAR_PANEL(null, null, 0, null, "tile.solar_compressed", 4, 10, 10000, 10, false, "emt", "panel_model", false),
    DOUBLE_SOLAR_PANEL(
            SOLAR_PANEL,
            null,
            1,
            null,
            "tile.solar_doublecompressed",
            4,
            100,
            10000,
            100,
            false,
            "emt",
            "doublepanel_model",
            false
    ),
    TRIPLE_SOLAR_PANEL(
            DOUBLE_SOLAR_PANEL,
            null,
            2,
            null,
            "tile.solar_triplecompressed",
            4,
            1000,
            10000,
            1000,
            false,
            "emt",
            "triplepanel_model",
            false
    ),
    AQUA_SOLAR_PANEL(null, null, 3, null, "tile.solar_watercompressed", 4, 10, 10000, 10, false, "emt", "waterpanel_model",
            false
    ),
    AQUA_DOUBLE_SOLAR_PANEL(
            AQUA_SOLAR_PANEL,
            null,
            4,
            null,
            "tile.solar_waterdoublecompressed",
            4,
            100,
            10000,
            100,
            false,
            "emt",
            "waterdoublepanel_model",
            false
    ),
    AQUA_TRIPLE_SOLAR_PANEL(
            AQUA_DOUBLE_SOLAR_PANEL,
            null,
            5,
            null,
            "tile.solar_watertriplecompressed",
            4,
            1000,
            10000,
            1000,
            false,
            "emt",
            "watertriplepanel_model",
            false
    ),
    DARK_SOLAR_PANEL(null, null, 6, null, "tile.solar_darkcompressed", 4, 0, 10000, 10, false, "emt", "darkpanel_model", false),
    DARK_DOUBLE_SOLAR_PANEL(
            DARK_SOLAR_PANEL,
            null,
            7,
            null,
            "tile.solar_darkdoublecompressed",
            4,
            0,
            10000,
            100,
            false,
            "emt",
            "darkdoublepanel_model",
            false
    ),
    DARK_TRIPLE_SOLAR_PANEL(
            DARK_DOUBLE_SOLAR_PANEL,
            null,
            8,
            null,
            "tile.solar_darktriplecompressed",
            4,
            0,
            10000,
            1000,
            false,
            "emt",
            "darktriplepanel_model",
            false
    ),
    ORDO_SOLAR_PANEL(
            null,
            null,
            9,
            null,
            "tile.solar_ordercompressed",
            4,
            30,
            10000,
            10,
            false,
            "emt",
            "orderpanel_model",
            false
    ),
    ORDO_DOUBLE_SOLAR_PANEL(
            ORDO_SOLAR_PANEL,
            null,
            10,
            null,
            "tile.solar_orderdoublecompressed",
            4,
            300,
            10000,
            100,
            false,
            "emt",
            "orderdoublepanel_model",
            false
    ),
    ORDO_TRIPLE_SOLAR_PANEL(
            ORDO_DOUBLE_SOLAR_PANEL,
            null,
            11,
            null,
            "tile.solar_ordertriplecompressed",
            4,
            3000,
            10000,
            1000,
            false,
            "emt",
            "ordertriplepanel_model",
            false
    ),
    FIRE_SOLAR_PANEL(null, null, 12, null, "tile.solar_firecompressed", 4, 10, 10000, 10, false, "emt", "firepanel_model", false),
    FIRE_DOUBLE_SOLAR_PANEL(
            FIRE_SOLAR_PANEL,
            null,
            13,
            null,
            "tile.solar_firedoublecompressed",
            4,
            100,
            10000,
            100,
            false,
            "emt",
            "firedoublepanel_model",
            false
    ),
    FIRE_TRIPLE_SOLAR_PANEL(
            FIRE_DOUBLE_SOLAR_PANEL,
            null,
            14,
            null,
            "tile.solar_firetriplecompressed",
            4,
            1000,
            10000,
            1000,
            false,
            "emt",
            "firetriplepanel_model",
            false
    ),
    AER_SOLAR_PANEL(null, null, 15, null, "tile.solar_aircompressed", 4, 10, 10000, 10, false, "emt", "airpanel_model", false),
    AER_DOUBLE_SOLAR_PANEL(
            AER_SOLAR_PANEL,
            null,
            0,
            null,
            "tile.solar_airdoublecompressed",
            4,
            100,
            10000,
            100,
            false,
            "emt",
            "airdoublepanel_model",
            false
    ),
    AER_TRIPLE_SOLAR_PANEL(
            AER_DOUBLE_SOLAR_PANEL,
            null,
            1,
            null,
            "tile.solar_airtriplecompressed",
            4,
            1000,
            10000,
            1000,
            false,
            "emt",
            "airtriplepanel_model",
            false
    ),
    EARTH_SOLAR_PANEL(
            null,
            null,
            2,
            null,
            "tile.solar_earthcompressed",
            4,
            10,
            10000,
            10,
            false,
            "emt",
            "earthpanel_model",
            false
    ),
    EARTH_DOUBLE_SOLAR_PANEL(
            EARTH_SOLAR_PANEL,
            null,
            3,
            null,
            "tile.solar_earthdoublecompressed",
            4,
            100,
            10000,
            100,
            false,
            "emt",
            "earthdoublepanel_model",
            false
    ),
    EARTH_TRIPLE_SOLAR_PANEL(
            EARTH_DOUBLE_SOLAR_PANEL,
            null,
            4,
            null,
            "tile.solar_earthtriplecompressed",
            4,
            1000,
            10000,
            1000,
            false,
            "emt",
            "earthtriplepanel_model",
            false
    ),

    ;

    public final String name;
    public final int tier;
    public final double genday;
    public final double gennight;
    public final double maxstorage;
    public final double producing;
    public final String name1;
    public final boolean register;
    public final Block block;
    public final int meta;
    public final String type;
    public final String texturesmodels;
    public final EnumSolarPanels solarold;
    public final boolean rendertype;

    EnumSolarPanels(
            EnumSolarPanels solarold,
            Block block,
            int meta,
            String name,
            String name1,
            int tier,
            double genday,
            double maxstorage,
            double producing,
            boolean register,
            String type,
            String texturesmodels,
            boolean rendertype
    ) {

        this.name = name;
        this.name1 = name1;
        this.tier = tier;
        this.genday = genday;
        this.gennight = genday / 2;
        this.maxstorage = maxstorage;
        this.producing = producing;
        this.register = register;
        this.solarold = solarold;
        this.block = block;
        this.meta = meta;
        this.type = type;
        this.texturesmodels = texturesmodels;
        this.rendertype = rendertype;
    }

    public static EnumSolarPanels getFromID(final int ID) {
        return values()[ID % values().length];
    }

    public static void registerTile() {
        for (EnumSolarPanels machine : EnumSolarPanels.values()) {


            IUItem.map3.put(machine.name1, machine);
        }
    }


}
