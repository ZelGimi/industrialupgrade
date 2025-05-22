package com.denfop.tiles.wiring;

import com.denfop.tiles.base.TileElectricBlock;
import com.denfop.tiles.wiring.chargepad.TileChargepadAdvMFSU;
import com.denfop.tiles.wiring.chargepad.TileChargepadBarMFSU;
import com.denfop.tiles.wiring.chargepad.TileChargepadGraMFSU;
import com.denfop.tiles.wiring.chargepad.TileChargepadHadrMFSU;
import com.denfop.tiles.wiring.chargepad.TileChargepadKvrMFSU;
import com.denfop.tiles.wiring.chargepad.TileChargepadMFE;
import com.denfop.tiles.wiring.chargepad.TileChargepadMFSU;
import com.denfop.tiles.wiring.chargepad.TileChargepadPerMFSU;
import com.denfop.tiles.wiring.chargepad.TileChargepadUltMFSU;
import com.denfop.tiles.wiring.storage.TileElectricAdvMFSU;
import com.denfop.tiles.wiring.storage.TileElectricBarMFSU;
import com.denfop.tiles.wiring.storage.TileElectricGraMFSU;
import com.denfop.tiles.wiring.storage.TileElectricHadrMFSU;
import com.denfop.tiles.wiring.storage.TileElectricKvrMFSU;
import com.denfop.tiles.wiring.storage.TileElectricMFE;
import com.denfop.tiles.wiring.storage.TileElectricMFSU;
import com.denfop.tiles.wiring.storage.TileElectricPerMFSU;
import com.denfop.tiles.wiring.storage.TileElectricUltMFSU;

public enum EnumElectricBlockState {
    BATBOX(EnumElectricBlock.BATBOX),
    CESU(EnumElectricBlock.CESU, new TileElectricMFE(), 0),
    MFE(EnumElectricBlock.MFE, new TileElectricMFSU(), 1),
    MFSU(EnumElectricBlock.MFSU, new TileElectricAdvMFSU(), 2),
    ADV_MFSU(EnumElectricBlock.ADV_MFSU, new TileElectricUltMFSU(), 3),
    ULT_MFSU(EnumElectricBlock.ULT_MFSU, new TileElectricPerMFSU(), 4),
    PER_MFSU(EnumElectricBlock.PER_MFSU, new TileElectricBarMFSU(), 5),
    BAR_MFSU(EnumElectricBlock.BAR_MFSU, new TileElectricHadrMFSU(), 6),
    HAD_MFSU(EnumElectricBlock.HAD_MFSU, new TileElectricGraMFSU(), 7),
    GRA_MFSU(EnumElectricBlock.GRA_MFSU, new TileElectricKvrMFSU(), 8),
    KVR_MFSU(EnumElectricBlock.KVR_MFSU),

    BATBOX_CHARGEPAD(EnumElectricBlock.BATBOX_CHARGEPAD),
    CESU_CHARGEPAD(EnumElectricBlock.CESU_CHARGEPAD, new TileChargepadMFE(), 0),
    MFE_CHARGEPAD(EnumElectricBlock.MFE_CHARGEPAD, new TileChargepadMFSU(), 1),
    MFSU_CHARGEPAD(EnumElectricBlock.MFSU_CHARGEPAD, new TileChargepadAdvMFSU(), 2),
    ADV_MFSU_CHARGEPAD(EnumElectricBlock.ADV_MFSU_CHARGEPAD, new TileChargepadUltMFSU(), 3),
    ULT_MFSU_CHARGEPAD(EnumElectricBlock.ULT_MFSU_CHARGEPAD, new TileChargepadPerMFSU(), 4),
    PER_MFSU_CHARGEPAD(EnumElectricBlock.PER_MFSU_CHARGEPAD, new TileChargepadBarMFSU(), 5),
    BAR_MFSU_CHARGEPAD(EnumElectricBlock.BAR_MFSU_CHARGEPAD, new TileChargepadHadrMFSU(), 6),
    HAD_MFSU_CHARGEPAD(EnumElectricBlock.HAD_MFSU_CHARGEPAD, new TileChargepadGraMFSU(), 7),
    GRA_MFSU_CHARGEPAD(EnumElectricBlock.GRA_MFSU_CHARGEPAD, new TileChargepadKvrMFSU(), 8),
    KVR_MFSU_CHARGEPAD(EnumElectricBlock.KVR_MFSU_CHARGEPAD),
    ;
    public final EnumElectricBlock block;
    public final TileElectricBlock state;
    public final int kit_meta;

    EnumElectricBlockState(EnumElectricBlock block) {
        this(block, null, 9);
    }

    EnumElectricBlockState(EnumElectricBlock block, TileElectricBlock state, int kit_meta) {
        this.block = block;
        this.state = state;
        this.kit_meta = kit_meta;
    }

    public static EnumElectricBlockState getFromID(final int ID) {
        return values()[ID % values().length];
    }


}
