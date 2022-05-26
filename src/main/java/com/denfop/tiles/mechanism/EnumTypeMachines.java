package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import net.minecraft.block.Block;

public enum EnumTypeMachines {
    MACERATOR(IUItem.machines_base, 2),
    EXTRACTOR(IUItem.machines_base, 11),
    COMPRESSOR(IUItem.machines_base, 5),
    ELECTRICFURNACE(IUItem.machines_base, 8),
    METALFOMER(IUItem.machines_base, 14),

    RECYCLER(IUItem.machines_base1, 2),
    COMBRECYCLER(IUItem.machines_base1, 5),
    COMBMACERATOR(IUItem.machines_base1, 9),
    ROLLING(IUItem.machines_base2, 3),
    EXTRUDING(IUItem.machines_base2, 7),
    CUTTING(IUItem.machines_base2, 11),
    FARMER(IUItem.machines_base3, 3),
    ASSAMPLERSCRAP(IUItem.machines_base3, 7),
    ;
    public final int meta;
    public final Block block;

    EnumTypeMachines(Block block, int meta) {
        this.block = block;
        this.meta = meta;
    }

}
