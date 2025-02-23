package com.denfop.tiles.panels.entity;


import com.denfop.Config;
import com.denfop.IUItem;
import com.denfop.integration.avaritia.AvaritiaIntegration;
import com.denfop.integration.botania.BotaniaIntegration;
import com.denfop.integration.de.DraconicIntegration;
import com.denfop.integration.thaumcraft.ThaumcraftIntegration;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public enum EnumSolarPanels {
    SOLAR_PANEL_DEFAULT(
            null,
            IUItem.basemachine2,
            81,
            1,
            1,
            128,
            1
            ,
            true
    ),
    ADVANCED_SOLAR_PANEL(
            SOLAR_PANEL_DEFAULT,
            IUItem.blockpanel,
            0,
            1,
            Config.advGenDay,
            Config.advStorage,
            Config.advOutput,
            true
    ),
    HYBRID_SOLAR_PANEL(
            ADVANCED_SOLAR_PANEL,
            IUItem.blockpanel,
            1,
            2,
            Config.hGenDay,
            Config.hStorage,
            Config.hOutput,
            true
    ),
    PERFECT_SOLAR_PANEL(
            HYBRID_SOLAR_PANEL,
            IUItem.blockpanel,
            2,
            3,
            Config.uhGenDay,
            Config.uhStorage,
            Config.uhOutput,
            true
    ),
    QUANTUM_SOLAR_PANEL(
            PERFECT_SOLAR_PANEL,
            IUItem.blockpanel,
            3,
            4,
            Config.qpGenDay,
            Config.qpStorage,
            Config.qpOutput,
            true
    ),
    SPECTRAL_SOLAR_PANEL(
            QUANTUM_SOLAR_PANEL,
            IUItem.blockpanel,
            4,
            5,
            Config.spectralpanelGenDay,
            Config.spectralpanelstorage,
            Config.spectralpanelOutput,
            true
    ),
    PROTON_SOLAR_PANEL(
            SPECTRAL_SOLAR_PANEL,
            IUItem.blockpanel,
            5,
            6,
            Config.protongenDay,
            Config.protonstorage,
            Config.protonOutput,
            true
    ),
    SINGULAR_SOLAR_PANEL(
            PROTON_SOLAR_PANEL,
            IUItem.blockpanel,
            6,
            7,
            Config.singularpanelGenDay,
            Config.singularpanelstorage,
            Config.singularpanelOutput,
            true
    ),
    DIFFRACTION_SOLAR_PANEL(
            SINGULAR_SOLAR_PANEL,
            IUItem.blockpanel,
            7,
            8,
            Config.adminpanelGenDay,
            Config.AdminpanelStorage,
            Config.AdminpanelOutput,
            true
    ),
    PHOTONIC_SOLAR_PANEL(
            DIFFRACTION_SOLAR_PANEL,
            IUItem.blockpanel,
            8,
            9,
            Config.photonicpanelGenDay,
            Config.photonicpanelStorage,
            Config.photonicpanelOutput,
            true
    ),
    NEUTRONIUN_SOLAR_PANEL(
            PHOTONIC_SOLAR_PANEL,
            IUItem.blockpanel,
            9,
            10,
            Config.neutronpanelGenDay,
            Config.neutronpanelStorage,
            Config.neutronpanelOutput,
            true
    ),
    BARION_SOLAR_PANEL(
            NEUTRONIUN_SOLAR_PANEL,
            IUItem.blockpanel,
            10,
            11,
            Config.barGenDay,
            Config.barStorage,
            Config.barOutput,
            true
    ),
    HADRON_SOLAR_PANEL(
            BARION_SOLAR_PANEL,
            IUItem.blockpanel,
            11,
            12,
            Config.adrGenDay,
            Config.adrStorage,
            Config.adrOutput,
            true
    ),
    GRAVITON_SOLAR_PANEL(
            HADRON_SOLAR_PANEL,
            IUItem.blockpanel,
            12,
            13,
            Config.graGenDay,
            Config.graStorage,
            Config.graOutput,
            true
    ),
    QUARK_SOLAR_PANEL(
            GRAVITON_SOLAR_PANEL,
            IUItem.blockpanel,
            13,
            14,
            Config.kvrGenDay,
            Config.kvrStorage,
            Config.kvrOutput,
            true
    ),
    DRACONIC_SOLAR_PANEL(
            HYBRID_SOLAR_PANEL,
            Config.DraconicLoaded && Config.Draconic ?
                    DraconicIntegration.blockDESolarPanel : null,
            0,
            Config.draconictier,
            Config.draconicgenday,
            Config.draconicstorage,
            Config.draconicoutput,
            Config.DraconicLoaded && Config.Draconic
    ),
    AWAKENED_SOLAR_PANEL(
            DRACONIC_SOLAR_PANEL,
            Config.DraconicLoaded && Config.Draconic ?
                    DraconicIntegration.blockDESolarPanel : null,
            1,
            Config.awakenedtier,
            Config.awakenedgenday,
            Config.awakenedstorage,
            Config.awakenedoutput,
            Config.DraconicLoaded && Config.Draconic
    ),
    CHAOTIC_SOLAR_PANEL(
            AWAKENED_SOLAR_PANEL,
            Config.DraconicLoaded && Config.Draconic ?
                    DraconicIntegration.blockDESolarPanel : null,
            2,
            Config.chaostier,
            Config.chaosgenday,
            Config.chaosstorage,
            Config.chaosoutput,
            Config.DraconicLoaded && Config.Draconic
    ),
    MANASTEEL_SOLAR_PANEL(
            ADVANCED_SOLAR_PANEL,
            BotaniaIntegration.blockBotSolarPanel,
            0,
            Config.manasteeltier,
            Config.manasteelgenday,
            Config.manasteelstorage,
            Config.manasteeloutput,
            Config.BotaniaLoaded && Config.Botania
    ),
    ELEMENTUM_SOLAR_PANEL(
            MANASTEEL_SOLAR_PANEL,
            BotaniaIntegration.blockBotSolarPanel,
            1,
            Config.elementiumtier,
            Config.elementiumgenday,
            Config.elementiumstorage,
            Config.elementiumoutput,
            Config.BotaniaLoaded && Config.Botania
    ),
    TERRASTEEL_SOLAR_PANEL(
            ELEMENTUM_SOLAR_PANEL,
            BotaniaIntegration.blockBotSolarPanel,
            2,
            Config.terasteeltier,
            Config.terasteelgenday,
            Config.terasteelstorage,
            Config.terasteeloutput,
            Config.BotaniaLoaded && Config.Botania
    ),

    NEUTRONIUM_SOLAR_PANEL_AVARITIA(
            PHOTONIC_SOLAR_PANEL,
            Config.AvaritiaLoaded && Config.Avaritia ? AvaritiaIntegration.blockAvSolarPanel : null,
            0,
            Config.tier,
            Config.neutrongenday,
            Config.neutronStorage,
            Config.neutronOutput,
            Config.AvaritiaLoaded && Config.Avaritia
    ),
    INFINITY_SOLAR_PANEL(
            NEUTRONIUM_SOLAR_PANEL_AVARITIA,
            Config.AvaritiaLoaded && Config.Avaritia ? AvaritiaIntegration.blockAvSolarPanel : null,
            1,
            12,
            Config.InfinityGenDay,
            Config.InfinityStorage,
            Config.InfinityOutput,
            Config.AvaritiaLoaded && Config.Avaritia
    ),
    THAUM_SOLAR_PANEL(HYBRID_SOLAR_PANEL, ThaumcraftIntegration.blockThaumSolarPanel, 0,
            Config.thaumtier, Config.thaumgenday, Config.thaumstorage,
            Config.thaumoutput, Config.thaumcraft && Config.Thaumcraft
    ),
    VOID_SOLAR_PANEL(
            THAUM_SOLAR_PANEL,
            ThaumcraftIntegration.blockThaumSolarPanel,
            1,
            Config.voidtier,
            Config.voidgenday,
            Config.voidstorage,
            Config.voidoutput,
            Config.thaumcraft && Config.Thaumcraft
    ),

    LV_SOLAR_PANEL(
            null,
            null, 0,
            1,
            8,
            64,
            8,
            false
    ),
    HV_SOLAR_PANEL(LV_SOLAR_PANEL, null, 1, 2, 64, 256, 64, false),
    MV_SOLAR_PANEL(HV_SOLAR_PANEL, null, 2, 3, 512, 1024, 512, false),
    ADVANCED_SOLAR_PANEL_W(
            null,
            null,
            0,
            1,
            Config.advGenDay,
            Config.advStorage,
            Config.advOutput,
            false
    ),
    HYBRID_SOLAR_PANEL_W(
            ADVANCED_SOLAR_PANEL_W,
            null, 0,
            2,
            Config.hGenDay,
            Config.hStorage,
            Config.hOutput,
            false
    ),
    PERFECT_SOLAR_PANEL_W(
            HYBRID_SOLAR_PANEL_W,
            null, 0,
            3,
            Config.uhGenDay,
            Config.uhStorage,
            Config.uhOutput,
            false
    ),
    QUANTUM_SOLAR_PANEL_W(
            PERFECT_SOLAR_PANEL_W,
            null, 0,
            4,
            Config.qpGenDay,
            Config.qpStorage,
            Config.qpOutput,
            false
    ),
    SPECTRAL_SOLAR_PANEL_W(
            QUANTUM_SOLAR_PANEL_W,
            null, 0,
            5,
            Config.spectralpanelGenDay,
            Config.spectralpanelstorage,
            Config.spectralpanelOutput,
            false
    ),
    PROTON_SOLAR_PANEL_W(
            SPECTRAL_SOLAR_PANEL_W,
            null, 0,
            6,
            Config.protongenDay,
            Config.protonstorage,
            Config.protonOutput,
            false
    ),
    SINGULAR_SOLAR_PANEL_W(
            PROTON_SOLAR_PANEL_W,
            null, 0,
            7,
            Config.singularpanelGenDay,
            Config.singularpanelstorage,
            Config.singularpanelOutput,
            false
    ),
    DIFFRACTION_SOLAR_PANEL_W(
            SINGULAR_SOLAR_PANEL_W,
            null, 0,
            8,
            Config.adminpanelGenDay,
            Config.AdminpanelStorage,
            Config.AdminpanelOutput,
            false
    ),
    PHOTONIC_SOLAR_PANEL_W(
            DIFFRACTION_SOLAR_PANEL_W,
            null, 0,
            9,
            Config.photonicpanelGenDay,
            Config.photonicpanelStorage,
            Config.photonicpanelOutput,
            false
    ),
    NEUTRONIUN_SOLAR_PANEL_W(
            PHOTONIC_SOLAR_PANEL_W,
            null, 0,
            10,
            Config.neutronpanelGenDay,
            Config.neutronpanelStorage,
            Config.neutronpanelOutput,
            true
    ),
    SOLAR_PANEL(null, null, 0, 4, 10, 10000, 10, false),
    DOUBLE_SOLAR_PANEL(
            SOLAR_PANEL,
            null,
            1,
            4,
            100,
            10000,
            100,
            false
    ),
    TRIPLE_SOLAR_PANEL(
            DOUBLE_SOLAR_PANEL,
            null,
            2,
            4,
            1000,
            10000,
            1000,
            false
    ),
    AQUA_SOLAR_PANEL(null, null, 3, 4, 10, 10000, 10, false
    ),
    AQUA_DOUBLE_SOLAR_PANEL(
            AQUA_SOLAR_PANEL,
            null,
            4,
            4,
            100,
            10000,
            100,
            false
    ),
    AQUA_TRIPLE_SOLAR_PANEL(
            AQUA_DOUBLE_SOLAR_PANEL,
            null,
            5,
            4,
            1000,
            10000,
            1000,
            false
    ),
    DARK_SOLAR_PANEL(null, null, 6, 4, 0, 10000, 10, false),
    DARK_DOUBLE_SOLAR_PANEL(
            DARK_SOLAR_PANEL,
            null,
            7,
            4,
            0,
            10000,
            100,
            false
    ),
    DARK_TRIPLE_SOLAR_PANEL(
            DARK_DOUBLE_SOLAR_PANEL,
            null,
            8,
            4,
            0,
            10000,
            1000,
            false
    ),
    ORDO_SOLAR_PANEL(
            null,
            null,
            9,
            4,
            30,
            10000,
            10,
            false
    ),
    ORDO_DOUBLE_SOLAR_PANEL(
            ORDO_SOLAR_PANEL,
            null,
            10,
            4,
            300,
            10000,
            100,
            false
    ),
    ORDO_TRIPLE_SOLAR_PANEL(
            ORDO_DOUBLE_SOLAR_PANEL,
            null,
            11,
            4,
            3000,
            10000,
            1000,
            false
    ),
    FIRE_SOLAR_PANEL(null, null, 12, 4, 10, 10000, 10, false),
    FIRE_DOUBLE_SOLAR_PANEL(
            FIRE_SOLAR_PANEL,
            null,
            13,
            4,
            100,
            10000,
            100,
            false
    ),
    FIRE_TRIPLE_SOLAR_PANEL(
            FIRE_DOUBLE_SOLAR_PANEL,
            null,
            14,
            4,
            1000,
            10000,
            1000,
            false
    ),
    AER_SOLAR_PANEL(null, null, 15, 4, 10, 10000, 10, false),
    AER_DOUBLE_SOLAR_PANEL(
            AER_SOLAR_PANEL,
            null,
            0,
            4,
            100,
            10000,
            100,
            false
    ),
    AER_TRIPLE_SOLAR_PANEL(
            AER_DOUBLE_SOLAR_PANEL,
            null,
            1,
            4,
            1000,
            10000,
            1000,
            false
    ),
    EARTH_SOLAR_PANEL(
            null,
            null,
            2,
            4,
            10,
            10000,
            10,
            false
    ),
    EARTH_DOUBLE_SOLAR_PANEL(
            EARTH_SOLAR_PANEL,
            null,
            3,
            4,
            100,
            10000,
            100,
            false
    ),
    EARTH_TRIPLE_SOLAR_PANEL(
            EARTH_DOUBLE_SOLAR_PANEL,
            null,
            4,
            4,
            1000,
            10000,
            1000,
            false
    ),

    ;

    public final int tier;
    public final double genday;
    public final double gennight;
    public final double maxstorage;
    public final double producing;
    public final boolean register;
    public final Block block;
    public final int meta;
    public final EnumSolarPanels solarold;

    EnumSolarPanels(
            EnumSolarPanels solarold,
            Block block,
            int meta,
            int tier,
            double genday,
            double maxstorage,
            double producing,
            boolean register
    ) {

        this.tier = tier;
        this.genday = genday;
        this.gennight = genday / 2;
        this.maxstorage = maxstorage;
        this.producing = producing;
        this.register = register;
        this.solarold = solarold;
        this.block = block;
        this.meta = meta;
    }

    public static EnumSolarPanels getFromID(final int ID) {
        return values()[ID % values().length];
    }

    public static void registerTile() {
        for (EnumSolarPanels machine : EnumSolarPanels.values()) {


            IUItem.map3.put(new ItemStack(machine.block, 1, machine.meta), machine);
        }
    }


}
