package com.denfop.tiles.base;

import com.denfop.IUItem;
import net.minecraft.block.Block;

public enum EnumMultiMachine {

    DOUBLE_MACERATOR("Macerator Double", 2, 300, 2, IUItem.machines_base, 1, 1, 0),
    TRIPLE_MACERATOR("Macerator Triple", 2, 300, 3, IUItem.machines_base, 2, 2, 1),
    QUAD_MACERATOR("Macerator Quad", 2, 300, 4, null, 0, 3, 2),

    DOUBLE_COMPRESSER("Compressor Double", 2, 300, 2, IUItem.machines_base, 4, 1, 3),
    TRIPLE_COMPRESSER("Compressor Triple", 2, 300, 3, IUItem.machines_base, 5, 2, 4),
    QUAD_COMPRESSER("Compressor Quad", 2, 300, 4, null, 0, 3, 5),

    DOUBLE_EXTRACTOR("Extractor Double", 2, 300, 2, IUItem.machines_base, 10, 1, 9),
    TRIPLE_EXTRACTOR("Extractor Triple", 2, 300, 3, IUItem.machines_base, 11, 2, 10),
    QUAD_EXTRACTOR("Extractor Quad", 2, 300, 4, null, 0, 3, 11),

    DOUBLE_ELECTRIC_FURNACE("Double Electric Furnace", 3, 100, 2, IUItem.machines_base, 7, 1, 6),
    TRIPLE_ELECTRIC_FURNACE("Triple Electric Furnace", 3, 100, 3, IUItem.machines_base, 8, 2, 7),
    QUAD_ELECTRIC_FURNACE("Quad Electric Furnace", 3, 100, 4, null, 0, 3, 8),

    DOUBLE_METAL_FORMER("Metal Former Double", 10, 200, 2, IUItem.machines_base, 13, 1, 12),
    TRIPLE_METAL_FORMER("Metal Former Triple", 10, 200, 3, IUItem.machines_base, 14, 1, 13),
    QUAD_METAL_FORMER("Metal Former Quad", 10, 200, 4, null, 0, 3, 14),

    DOUBLE_RECYCLER("Double Recycler", 1, 45, 2, IUItem.machines_base1, 1, 1, 0),
    TRIPLE_RECYCLER("Triple Recycler", 1, 45, 3, IUItem.machines_base1, 2, 2, 1),
    QUAD_RECYCLER("Quad Recycler", 1, 45, 4, null, 0, 3, 2),

    DOUBLE_COMB_RECYCLER("Double Combined  Recycler", 1, 45, 2, IUItem.machines_base1, 4, 1, 3),
    TRIPLE_COMB_RRECYCLER("Triple Combined  Recycler", 1, 45, 3, IUItem.machines_base1, 5, 2, 4),
    QUAD_COMB_RRECYCLER("Quad Combined  Recycler", 1, 45, 4, null, 0, 3, 5),
    COMB_MACERATOR("Combined Macerator", 2, 300, 1, IUItem.machines_base1, 7, 0, 6),
    COMB_DOUBLE_MACERATOR("Combined Macerator Double", 2, 300, 2, IUItem.machines_base1, 8, 1, 7),
    COMB_TRIPLE_MACERATOR("Combined Macerator Triple", 2, 300, 3, IUItem.machines_base1, 9, 2, 8),
    COMB_QUAD_MACERATOR("Combined Macerator Quad", 2, 300, 4, null, 0, 3, 9),

    Rolling("Rolling", 10, 200, 1, IUItem.machines_base2, 1, 0, 0),
    DOUBLE_Rolling(" Double Rolling", 10, 200, 2, IUItem.machines_base2, 2, 1, 1),
    TRIPLE_Rolling(" Triple Rolling", 10, 200, 3, IUItem.machines_base2, 3, 2, 2),
    QUAD_Rolling("Quad Rolling", 10, 200, 4, null, 0, 3, 3),
    Extruding("Extruding", 10, 200, 1, IUItem.machines_base2, 5, 0, 4),
    DOUBLE_Extruding("Double Extruding ", 10, 200, 2, IUItem.machines_base2, 6, 1, 5),
    TRIPLE_Extruding("Triple Extruding", 10, 200, 3, IUItem.machines_base2, 7, 2, 6),
    QUAD_Extruding("Quad Extruding", 10, 200, 4, null, 0, 3, 7),
    Cutting("Cutting", 10, 200, 1, IUItem.machines_base2, 9, 0, 8),
    DOUBLE_Cutting("Double Cutting", 10, 200, 2, IUItem.machines_base2, 10, 1, 9),
    TRIPLE_Cutting("Triple Cutting", 10, 200, 3, IUItem.machines_base2, 12, 2, 10),
    QUAD_Cutting("Quad Cutting", 10, 200, 4, null, 0, 3, 11),

    Fermer("Fermer", 4, 500, 1, IUItem.machines_base3, 1, 0, 0),
    DOUBLE_Fermer("Double Fermer", 4, 500, 2, IUItem.machines_base3, 2, 1, 1),
    TRIPLE_Fermer("Triple Fermer", 4, 500, 3, IUItem.machines_base3, 3, 2, 2),
    QUAD_Fermer("Quad Fermer", 4, 500, 4, null, 0, 3, 3),

    AssamplerScrap("AssamplerScrap", 1, 25, 1, IUItem.machines_base3, 5, 0, 4),
    DOUBLE_AssamplerScrap("Double AssamplerScrap", 1, 25, 2, IUItem.machines_base3, 6, 1, 5),
    TRIPLE_AssamplerScrap("Triple AssamplerScrap", 1, 25, 3, IUItem.machines_base3, 7, 2, 6),
    QUAD_AssamplerScrap("Quad AssamplerScrap", 1, 25, 4, null, 0, 3, 7);

    public final int usagePerTick;
    public final int lenghtOperation;
    public final int sizeWorkingSlot;
    public final Block block_new;
    public final int meta_new;
    public final int upgrade;
    public final String name;
    public final int meta;

    EnumMultiMachine(
            String name,
            int usagePerTick,
            int lenghtOperation,
            int sizeWorkingSlot,
            Block block_new,
            int meta_new,
            int upgrade, int meta
    ) {

        this.name = name;
        this.usagePerTick = usagePerTick;
        this.lenghtOperation = lenghtOperation;
        this.sizeWorkingSlot = sizeWorkingSlot;
        this.block_new = block_new;
        this.meta_new = meta_new;
        this.upgrade = upgrade;
        this.meta = meta;
    }


}
