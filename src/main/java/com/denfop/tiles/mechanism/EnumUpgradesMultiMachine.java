package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import ic2.core.block.TileEntityBlock;

@SuppressWarnings("SameParameterValue")
public enum EnumUpgradesMultiMachine {
    DOUBLE_MACERATOR("ic2.te.macerator", 0, new TileEntityDoubleMacerator()),
    DOUBLE_EXTRACTOR("ic2.te.extractor", 0, new TileEntityDoubleExtractor()),
    DOUBLE_FURNACE("ic2.te.electric_furnace", 0, new TileEntityDoubleElectricFurnace()),
    DOUBLE_COMPRESSOR("ic2.te.compressor", 0, new TileEntityDoubleCompressor()),
    DOUBLE_RECYLER("ic2.te.recycler", 0, new TileEntityDoubleRecycler()),
    DOUBLE_METALFORMER("ic2.te.metal_former", 0, new TileEntityDoubleMetalFormer());

    public final int meta_item;
    public final String name;
    public final TileEntityBlock blockstate;

    EnumUpgradesMultiMachine(String name, int meta_item, TileEntityBlock state) {
        this.name = name;
        this.meta_item = meta_item;
        this.blockstate = state;
    }

    public static void register() {
        for (EnumUpgradesMultiMachine value : values()) {
            IUItem.map4.put(value.name, value);
        }
    }
}
