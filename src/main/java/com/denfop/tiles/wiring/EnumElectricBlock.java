package com.denfop.tiles.wiring;


import com.denfop.Config;
import ic2.core.init.Localization;

public enum EnumElectricBlock {
    BATBOX("iu.blockBatBox.name", 1, 32, 40000, false,0),
    CESU("iu.blockCESU.name", 2, 128, 300000, false,1),
    MFE("iu.blockMFE1.name", 3, 512, 4000000, false,2),
    MFSU("iu.blockMFSU1.name", 4, 2048, 40000000, false,3),
    ADV_MFSU("iu.blockMFE.name", Config.tier_advmfsu, Config.adv_enegry, Config.adv_storage, false,4),
    ULT_MFSU("iu.blockMFSU.name", Config.tier_ultmfsu, Config.ult_enegry, Config.ult_storage, false,5),
    PER_MFSU("iu.blockPerMFSU.name", (int) Config.tierPerMFSU, Config.PerMFSUOutput, Config.PerMFSUStorage, false,6),
    BAR_MFSU("iu.blockBarMFSU.name", (int) Config.tierBarMFSU, Config.BarMFSUOutput, Config.BarMFSUStorage, false,7),
    HAD_MFSU("iu.blockAdrMFSU.name", (int) Config.tierHadrMFSU, Config.HadrMFSUOutput, Config.HadrMFSUStorage, false,8),
    GRA_MFSU("iu.blockGraMFSU.name", (int) Config.tierGraMFSU, Config.GraMFSUOutput, Config.GraMFSUStorage, false,9),
    KVR_MFSU("iu.blockKvrMFSU.name", (int) Config.tierKrvMFSU, Config.KrvMFSUOutput, Config.KrvMFSUStorage, false,10),

    BATBOX_CHARGEPAD("iu.blockChargepadBatBox.name", 1, 32, 40000, true,11),
    CESU_CHARGEPAD("iu.blockChargepadCESU.name", 2, 128, 300000, true,12),
    MFE_CHARGEPAD("iu.blockChargepadMFE1.name", 3, 512, 4000000, true,13),
    MFSU_CHARGEPAD("iu.blockChargepadMFSU.name", 4, 2048, 40000000, true,14),
    ADV_MFSU_CHARGEPAD("iu.blockChargepadMFE.name", Config.tier_advmfsu, Config.adv_enegry, Config.adv_storage, true,15),
    ULT_MFSU_CHARGEPAD("iu.blockChargepadMFES.name", Config.tier_ultmfsu, Config.ult_enegry, Config.ult_storage, true,16),
    PER_MFSU_CHARGEPAD(
            "iu.blockChargepadPerMFSU.name",
            (int) Config.tierPerMFSU,
            Config.PerMFSUOutput,
            Config.PerMFSUStorage,
            true,17
    ),
    BAR_MFSU_CHARGEPAD(
            "iu.blockChargepadBarMFSU.name",
            (int) Config.tierBarMFSU,
            Config.BarMFSUOutput,
            Config.BarMFSUStorage,
            true,18
    ),
    HAD_MFSU_CHARGEPAD(
            "iu.blockChargepadAdrMFSU.name",
            (int) Config.tierHadrMFSU,
            Config.HadrMFSUOutput,
            Config.HadrMFSUStorage,
            true,18
    ),
    GRA_MFSU_CHARGEPAD(
            "iu.blockChargepadGraMFSU.name",
            (int) Config.tierGraMFSU,
            Config.GraMFSUOutput,
            Config.GraMFSUStorage,
            true,19
    ),
    KVR_MFSU_CHARGEPAD(
            "iu.blockChargepadKvrMFSU.name",
            (int) Config.tierKrvMFSU,
            Config.KrvMFSUOutput,
            Config.KrvMFSUStorage,
            true,20
    ),

    ;

    public final int tier;
    public final double maxstorage;
    public final double producing;
    public final String name1;
    public final boolean chargepad;
    public final int id;

    EnumElectricBlock(String name1, int tier, double producing, double maxstorage, boolean chargepad, int id) {
        this.name1 = Localization.translate(name1);
        this.tier = tier;
        this.maxstorage = maxstorage;
        this.producing = producing;
        this.chargepad = chargepad;
        this.id = id;
    }
    public static EnumElectricBlock getFromID(final int ID) {
        return values()[ID % values().length];
    }

}
