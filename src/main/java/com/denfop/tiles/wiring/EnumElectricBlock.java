package com.denfop.tiles.wiring;


import com.denfop.Config;
import ic2.core.init.Localization;

public enum EnumElectricBlock {
    BATBOX("iu.blockBatBox.name", 1, 32, 40000, false, 0,5,-1),
    CESU("iu.blockCESU.name", 2, 128, 300000, false, 1,3,0),
    MFE("iu.blockMFE1.name", 3, 512, 4000000, false, 2,4,1),
    MFSU("iu.blockMFSU1.name", 4, 2048, 40000000, false, 3,0,2),
    ADV_MFSU("iu.blockMFE.name", Config.tier_advmfsu, Config.adv_enegry, Config.adv_storage, false, 4,1,3),
    ULT_MFSU("iu.blockMFSU.name", Config.tier_ultmfsu, Config.ult_enegry, Config.ult_storage, false, 5,6,4),
    PER_MFSU("iu.blockPerMFSU.name", (int) Config.tierPerMFSU, Config.PerMFSUOutput, Config.PerMFSUStorage, false, 6,7,5),
    BAR_MFSU("iu.blockBarMFSU.name", (int) Config.tierBarMFSU, Config.BarMFSUOutput, Config.BarMFSUStorage, false, 7,8,6),
    HAD_MFSU("iu.blockAdrMFSU.name", (int) Config.tierHadrMFSU, Config.HadrMFSUOutput, Config.HadrMFSUStorage, false, 8,9,7),
    GRA_MFSU("iu.blockGraMFSU.name", (int) Config.tierGraMFSU, Config.GraMFSUOutput, Config.GraMFSUStorage, false, 9,10,8),
    KVR_MFSU("iu.blockKvrMFSU.name", (int) Config.tierKrvMFSU, Config.KrvMFSUOutput, Config.KrvMFSUStorage, false, 10,11,9),

    BATBOX_CHARGEPAD("iu.blockChargepadBatBox.name", 1, 32, 40000, true, 11,3,-1),
    CESU_CHARGEPAD("iu.blockChargepadCESU.name", 2, 128, 300000, true, 12,4,0),
    MFE_CHARGEPAD("iu.blockChargepadMFE1.name", 3, 512, 4000000, true, 13,5,1),
    MFSU_CHARGEPAD("iu.blockChargepadMFSU.name", 4, 2048, 40000000, true, 14,0,2),
    ADV_MFSU_CHARGEPAD("iu.blockChargepadMFE.name", Config.tier_advmfsu, Config.adv_enegry, Config.adv_storage, true, 15,1,3),
    ULT_MFSU_CHARGEPAD("iu.blockChargepadMFES.name", Config.tier_ultmfsu, Config.ult_enegry, Config.ult_storage, true, 16,6,4),
    PER_MFSU_CHARGEPAD(
            "iu.blockChargepadPerMFSU.name",
            (int) Config.tierPerMFSU,
            Config.PerMFSUOutput,
            Config.PerMFSUStorage,
            true, 17,7,5
    ),
    BAR_MFSU_CHARGEPAD(
            "iu.blockChargepadBarMFSU.name",
            (int) Config.tierBarMFSU,
            Config.BarMFSUOutput,
            Config.BarMFSUStorage,
            true, 18,8,6
    ),
    HAD_MFSU_CHARGEPAD(
            "iu.blockChargepadAdrMFSU.name",
            (int) Config.tierHadrMFSU,
            Config.HadrMFSUOutput,
            Config.HadrMFSUStorage,
            true, 18,9,7
    ),
    GRA_MFSU_CHARGEPAD(
            "iu.blockChargepadGraMFSU.name",
            (int) Config.tierGraMFSU,
            Config.GraMFSUOutput,
            Config.GraMFSUStorage,
            true, 19,10,8
    ),
    KVR_MFSU_CHARGEPAD(
            "iu.blockChargepadKvrMFSU.name",
            (int) Config.tierKrvMFSU,
            Config.KrvMFSUOutput,
            Config.KrvMFSUStorage,
            true, 20,11,9
    ),

    ;

    public final int tier;
    public final double maxstorage;
    public final double producing;
    public final String name1;
    public final boolean chargepad;
    public final int id;
    public final int meta;
    public final int kit_meta;

    EnumElectricBlock(String name1, int tier, double producing, double maxstorage, boolean chargepad, int id,int meta,
                      int kit_meta) {
        this.name1 = Localization.translate(name1);
        this.tier = tier;
        this.maxstorage = maxstorage;
        this.producing = producing;
        this.chargepad = chargepad;
        this.id = id;
        this.meta=meta;
        this.kit_meta=kit_meta;
    }

    public static EnumElectricBlock getFromID(final int ID) {
        return values()[ID % values().length];
    }

}
