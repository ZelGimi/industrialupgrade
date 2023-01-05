package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import net.minecraft.block.Block;

public enum EnumTypeMachines {
    MACERATOR(IUItem.machines_base, 2, "macerator"),
    EXTRACTOR(IUItem.machines_base, 11, "extractor"),
    COMPRESSOR(IUItem.machines_base, 5, "compressor"),
    ELECTRICFURNACE(IUItem.machines_base, 8, "furnace"),
    METALFOMER(IUItem.machines_base, 14, "extruding"),

    RECYCLER(IUItem.machines_base1, 2, "recycler"),
    COMBRECYCLER(IUItem.machines_base1, 5, "recycler"),
    COMBMACERATOR(IUItem.machines_base1, 9, "comb_macerator"),
    ROLLING(IUItem.machines_base2, 3, "rolling"),
    EXTRUDING(IUItem.machines_base2, 7, "extruding"),
    CUTTING(IUItem.machines_base2, 11, "cutting"),
    FARMER(IUItem.machines_base3, 3, "farmer"),
    ASSAMPLERSCRAP(IUItem.machines_base3, 7, "scrap"),
    OreWashing(IUItem.machines_base3, 11, "orewashing"),
    Centrifuge(IUItem.machines_base3, 15, "centrifuge"),
    Gearing(IUItem.machines_base3, 19, "gearing"),
    ;
    public final int meta;
    public final Block block;
    public String recipe;

    EnumTypeMachines(Block block, int meta, String recipe) {
        this.block = block;
        this.meta = meta;
        this.recipe = recipe;
    }

}
