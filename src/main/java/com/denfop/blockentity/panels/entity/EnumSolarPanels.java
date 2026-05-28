package com.denfop.blockentity.panels.entity;


import com.denfop.IUItem;
import com.denfop.dataregistry.DataBlockEntity;
import net.minecraft.world.item.ItemStack;

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
            5,
            3200D,
            10D,
            true
    ),
    HYBRID_SOLAR_PANEL(
            ADVANCED_SOLAR_PANEL,
            IUItem.blockpanel,
            1,
            2,
            15,
            20000D,
            30,
            true
    ),
    PERFECT_SOLAR_PANEL(
            HYBRID_SOLAR_PANEL,
            IUItem.blockpanel,
            2,
            3,
            45,
            200000D,
            90D,
            true
    ),
    QUANTUM_SOLAR_PANEL(
            PERFECT_SOLAR_PANEL,
            IUItem.blockpanel,
            3,
            4,
            135,
            1000000D,
            270,
            true
    ),
    SPECTRAL_SOLAR_PANEL(
            QUANTUM_SOLAR_PANEL,
            IUItem.blockpanel,
            4,
            5,
            405,
            5000000D,
            810,
            true
    ),
    PROTON_SOLAR_PANEL(
            SPECTRAL_SOLAR_PANEL,
            IUItem.blockpanel,
            5,
            6,
            1215,
            50000000D,
            2430,
            true
    ),
    SINGULAR_SOLAR_PANEL(
            PROTON_SOLAR_PANEL,
            IUItem.blockpanel,
            6,
            7,
            3645,
            1000000000D,
            7290,
            true
    ),
    DIFFRACTION_SOLAR_PANEL(
            SINGULAR_SOLAR_PANEL,
            IUItem.blockpanel,
            7,
            8,
            10935,
            1500000000D,
            21870,
            true
    ),
    PHOTONIC_SOLAR_PANEL(
            DIFFRACTION_SOLAR_PANEL,
            IUItem.blockpanel,
            8,
            9,
            32805,
            5000000000D,
            65610,
            true
    ),
    NEUTRONIUN_SOLAR_PANEL(
            PHOTONIC_SOLAR_PANEL,
            IUItem.blockpanel,
            9,
            10,
            131220,
            6500000000D,
            262440,
            true
    ),
    BARION_SOLAR_PANEL(
            NEUTRONIUN_SOLAR_PANEL,
            IUItem.blockpanel,
            10,
            11,
            524880,
            10000000000D,
            1049760,
            true
    ),
    HADRON_SOLAR_PANEL(
            BARION_SOLAR_PANEL,
            IUItem.blockpanel,
            11,
            12,
            2099520,
            25000000000D,
            4199040,
            true
    ),
    GRAVITON_SOLAR_PANEL(
            HADRON_SOLAR_PANEL,
            IUItem.blockpanel,
            12,
            13,
            8398080,
            250000000000D,
            16796160,
            true
    ),
    QUARK_SOLAR_PANEL(
            GRAVITON_SOLAR_PANEL,
            IUItem.blockpanel,
            13,
            14,
            33592320,
            2500000000000D,
            67184640,
            true
    ),


    ;

    public final int tier;
    public final double genday;
    public final double gennight;
    public final double maxstorage;
    public final double producing;
    public final boolean register;
    public final DataBlockEntity block;
    public final int meta;
    public final EnumSolarPanels solarold;

    EnumSolarPanels(
            EnumSolarPanels solarold,
            DataBlockEntity block,
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

            if (machine.block != null)
                IUItem.map3.put(new ItemStack(machine.block.getItem(machine.meta), 1), machine);
        }
    }


}
