package com.denfop.tiles.wiring;

import com.denfop.IUItem;
import com.denfop.tiles.base.TileEntityElectricBlock;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadAdvMFSU;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadBarMFSU;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadGraMFSU;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadHadrMFSU;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadKvrMFSU;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadMFE;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadMFSU;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadPerMFSU;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadUltMFSU;
import com.denfop.tiles.wiring.storage.TileEntityElectricAdvMFSU;
import com.denfop.tiles.wiring.storage.TileEntityElectricBarMFSU;
import com.denfop.tiles.wiring.storage.TileEntityElectricGraMFSU;
import com.denfop.tiles.wiring.storage.TileEntityElectricHadrMFSU;
import com.denfop.tiles.wiring.storage.TileEntityElectricKvrMFSU;
import com.denfop.tiles.wiring.storage.TileEntityElectricMFE;
import com.denfop.tiles.wiring.storage.TileEntityElectricMFSU;
import com.denfop.tiles.wiring.storage.TileEntityElectricPerMFSU;
import com.denfop.tiles.wiring.storage.TileEntityElectricUltMFSU;

public enum EnumElectricBlockState {
    BATBOX(EnumElectricBlock.BATBOX),
    CESU(EnumElectricBlock.CESU, new TileEntityElectricMFE(), 0),
    MFE(EnumElectricBlock.MFE, new TileEntityElectricMFSU(), 1),
    MFSU(EnumElectricBlock.MFSU, new TileEntityElectricAdvMFSU(), 2),
    ADV_MFSU(EnumElectricBlock.ADV_MFSU, new TileEntityElectricUltMFSU(), 3),
    ULT_MFSU(EnumElectricBlock.ULT_MFSU, new TileEntityElectricPerMFSU(), 4),
    PER_MFSU(EnumElectricBlock.PER_MFSU, new TileEntityElectricBarMFSU(), 5),
    BAR_MFSU(EnumElectricBlock.BAR_MFSU, new TileEntityElectricHadrMFSU(), 6),
    HAD_MFSU(EnumElectricBlock.HAD_MFSU, new TileEntityElectricGraMFSU(), 7),
    GRA_MFSU(EnumElectricBlock.GRA_MFSU, new TileEntityElectricKvrMFSU(), 8),
    KVR_MFSU(EnumElectricBlock.KVR_MFSU),

    BATBOX_CHARGEPAD(EnumElectricBlock.BATBOX_CHARGEPAD),
    CESU_CHARGEPAD(EnumElectricBlock.CESU_CHARGEPAD, new TileEntityChargepadMFE(), 0),
    MFE_CHARGEPAD(EnumElectricBlock.MFE_CHARGEPAD, new TileEntityChargepadMFSU(), 1),
    MFSU_CHARGEPAD(EnumElectricBlock.MFSU_CHARGEPAD, new TileEntityChargepadAdvMFSU(), 2),
    ADV_MFSU_CHARGEPAD(EnumElectricBlock.ADV_MFSU_CHARGEPAD, new TileEntityChargepadUltMFSU(), 3),
    ULT_MFSU_CHARGEPAD(EnumElectricBlock.ULT_MFSU_CHARGEPAD, new TileEntityChargepadPerMFSU(), 4),
    PER_MFSU_CHARGEPAD(EnumElectricBlock.PER_MFSU_CHARGEPAD, new TileEntityChargepadBarMFSU(), 5),
    BAR_MFSU_CHARGEPAD(EnumElectricBlock.BAR_MFSU_CHARGEPAD, new TileEntityChargepadHadrMFSU(), 6),
    HAD_MFSU_CHARGEPAD(EnumElectricBlock.HAD_MFSU_CHARGEPAD, new TileEntityChargepadGraMFSU(), 7),
    GRA_MFSU_CHARGEPAD(EnumElectricBlock.GRA_MFSU_CHARGEPAD, new TileEntityChargepadKvrMFSU(), 8),
    KVR_MFSU_CHARGEPAD(EnumElectricBlock.KVR_MFSU_CHARGEPAD),
    ;
    public final EnumElectricBlock block;
    public final TileEntityElectricBlock state;
    public final int kit_meta;

    EnumElectricBlockState(EnumElectricBlock block) {
        this(block, null, 9);
    }

    EnumElectricBlockState(EnumElectricBlock block, TileEntityElectricBlock state, int kit_meta) {
        this.block = block;
        this.state = state;
        this.kit_meta = kit_meta;
    }
    public static EnumElectricBlockState getFromID(final int ID) {
        return values()[ID % values().length];
    }


}
