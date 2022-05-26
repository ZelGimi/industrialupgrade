package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.tiles.mechanism.EnumTypeMachines;
import net.minecraft.block.Block;

public enum EnumMultiMachine {

    DOUBLE_MACERATOR("Macerator Double", 2, 300, 2, IUItem.machines_base, 1, 1, 0, EnumTypeMachines.MACERATOR),
    TRIPLE_MACERATOR("Macerator Triple", 2, 300, 3, IUItem.machines_base, 2, 2, 1, EnumTypeMachines.MACERATOR),
    QUAD_MACERATOR("Macerator Quad", 2, 300, 4, null, 0, -1, 2, EnumTypeMachines.MACERATOR),

    DOUBLE_COMPRESSER("Compressor Double", 2, 300, 2, IUItem.machines_base, 4, 1, 3, EnumTypeMachines.COMPRESSOR),
    TRIPLE_COMPRESSER("Compressor Triple", 2, 300, 3, IUItem.machines_base, 5, 2, 4, EnumTypeMachines.COMPRESSOR),
    QUAD_COMPRESSER("Compressor Quad", 2, 300, 4, null, 0, -1, 5, EnumTypeMachines.COMPRESSOR),

    DOUBLE_EXTRACTOR("Extractor Double", 2, 300, 2, IUItem.machines_base, 10, 1, 9, EnumTypeMachines.EXTRACTOR),
    TRIPLE_EXTRACTOR("Extractor Triple", 2, 300, 3, IUItem.machines_base, 11, 2, 10, EnumTypeMachines.EXTRACTOR),
    QUAD_EXTRACTOR("Extractor Quad", 2, 300, 4, null, 0, -1, 11, EnumTypeMachines.EXTRACTOR),

    DOUBLE_ELECTRIC_FURNACE(
            "Double Electric Furnace",
            3,
            100,
            2,
            IUItem.machines_base,
            7,
            1,
            6,
            EnumTypeMachines.ELECTRICFURNACE
    ),
    TRIPLE_ELECTRIC_FURNACE("Triple Electric Furnace", 3, 100, 3, IUItem.machines_base, 8, 2, 7, EnumTypeMachines.EXTRACTOR),
    QUAD_ELECTRIC_FURNACE("Quad Electric Furnace", 3, 100, 4, null, 0, -1, 8, EnumTypeMachines.ELECTRICFURNACE),

    DOUBLE_METAL_FORMER("Metal Former Double", 10, 200, 2, IUItem.machines_base, 13, 1, 12, EnumTypeMachines.METALFOMER),
    TRIPLE_METAL_FORMER("Metal Former Triple", 10, 200, 3, IUItem.machines_base, 14, 1, 13, EnumTypeMachines.METALFOMER),
    QUAD_METAL_FORMER("Metal Former Quad", 10, 200, 4, null, 0, -1, 14, EnumTypeMachines.METALFOMER),

    DOUBLE_RECYCLER("Double Recycler", 1, 45, 2, IUItem.machines_base1, 1, 1, 0, EnumTypeMachines.RECYCLER),
    TRIPLE_RECYCLER("Triple Recycler", 1, 45, 3, IUItem.machines_base1, 2, 2, 1, EnumTypeMachines.RECYCLER),
    QUAD_RECYCLER("Quad Recycler", 1, 45, 4, null, 0, -1, 2, EnumTypeMachines.RECYCLER),

    DOUBLE_COMB_RECYCLER("Double Combined  Recycler", 1, 45, 2, IUItem.machines_base1, 4, 1, 3, EnumTypeMachines.COMBRECYCLER),
    TRIPLE_COMB_RRECYCLER("Triple Combined  Recycler", 1, 45, 3, IUItem.machines_base1, 5, 2, 4, EnumTypeMachines.COMBRECYCLER),
    QUAD_COMB_RRECYCLER("Quad Combined  Recycler", 1, 45, 4, null, 0, -1, 5, EnumTypeMachines.COMBRECYCLER),
    COMB_MACERATOR("Combined Macerator", 2, 300, 1, IUItem.machines_base1, 7, 0, 6, EnumTypeMachines.COMBMACERATOR),
    COMB_DOUBLE_MACERATOR("Combined Macerator Double", 2, 300, 2, IUItem.machines_base1, 8, 1, 7, EnumTypeMachines.COMBMACERATOR),
    COMB_TRIPLE_MACERATOR("Combined Macerator Triple", 2, 300, 3, IUItem.machines_base1, 9, 2, 8, EnumTypeMachines.COMBMACERATOR),
    COMB_QUAD_MACERATOR("Combined Macerator Quad", 2, 300, 4, null, 0, -1, 9, EnumTypeMachines.COMBMACERATOR),

    Rolling("Rolling", 10, 200, 1, IUItem.machines_base2, 1, 0, 0, EnumTypeMachines.ROLLING),
    DOUBLE_Rolling(" Double Rolling", 10, 200, 2, IUItem.machines_base2, 2, 1, 1, EnumTypeMachines.ROLLING),
    TRIPLE_Rolling(" Triple Rolling", 10, 200, 3, IUItem.machines_base2, 3, 2, 2, EnumTypeMachines.ROLLING),
    QUAD_Rolling("Quad Rolling", 10, 200, 4, null, 0, -1, 3, EnumTypeMachines.ROLLING),
    Extruding("Extruding", 10, 200, 1, IUItem.machines_base2, 5, 0, 4, EnumTypeMachines.EXTRUDING),
    DOUBLE_Extruding("Double Extruding ", 10, 200, 2, IUItem.machines_base2, 6, 1, 5, EnumTypeMachines.EXTRUDING),
    TRIPLE_Extruding("Triple Extruding", 10, 200, 3, IUItem.machines_base2, 7, 2, 6, EnumTypeMachines.EXTRUDING),
    QUAD_Extruding("Quad Extruding", 10, 200, 4, null, 0, -1, 7, EnumTypeMachines.EXTRUDING),
    Cutting("Cutting", 10, 200, 1, IUItem.machines_base2, 9, 0, 8, EnumTypeMachines.CUTTING),
    DOUBLE_Cutting("Double Cutting", 10, 200, 2, IUItem.machines_base2, 10, 1, 9, EnumTypeMachines.CUTTING),
    TRIPLE_Cutting("Triple Cutting", 10, 200, 3, IUItem.machines_base2, 11, 2, 10, EnumTypeMachines.CUTTING),
    QUAD_Cutting("Quad Cutting", 10, 200, 4, null, 0, -1, 11, EnumTypeMachines.CUTTING),

    Fermer("Fermer", 4, 500, 1, IUItem.machines_base3, 1, 0, 0, EnumTypeMachines.FARMER),
    DOUBLE_Fermer("Double Fermer", 4, 500, 2, IUItem.machines_base3, 2, 1, 1, EnumTypeMachines.FARMER),
    TRIPLE_Fermer("Triple Fermer", 4, 500, 3, IUItem.machines_base3, 3, 2, 2, EnumTypeMachines.FARMER),
    QUAD_Fermer("Quad Fermer", 4, 500, 4, null, 0, -1, 3, EnumTypeMachines.FARMER),

    AssamplerScrap("AssamplerScrap", 1, 25, 1, IUItem.machines_base3, 5, 0, 4, EnumTypeMachines.ASSAMPLERSCRAP),
    DOUBLE_AssamplerScrap("Double AssamplerScrap", 1, 25, 2, IUItem.machines_base3, 6, 1, 5, EnumTypeMachines.ASSAMPLERSCRAP),
    TRIPLE_AssamplerScrap("Triple AssamplerScrap", 1, 25, 3, IUItem.machines_base3, 7, 2, 6, EnumTypeMachines.ASSAMPLERSCRAP),
    QUAD_AssamplerScrap("Quad AssamplerScrap", 1, 25, 4, null, 0, -1, 7, EnumTypeMachines.ASSAMPLERSCRAP);

    public final int usagePerTick;
    public final int lenghtOperation;
    public final int sizeWorkingSlot;
    public final Block block_new;
    public final int meta_new;
    public final int upgrade;
    public final String name;
    public final int meta;
    public final EnumTypeMachines type;

    EnumMultiMachine(
            String name,
            int usagePerTick,
            int lenghtOperation,
            int sizeWorkingSlot,
            Block block_new,
            int meta_new,
            int upgrade, int meta, EnumTypeMachines type
    ) {

        this.name = name;
        this.usagePerTick = usagePerTick;
        this.lenghtOperation = lenghtOperation;
        this.sizeWorkingSlot = sizeWorkingSlot;
        this.block_new = block_new;
        this.meta_new = meta_new;
        this.upgrade = upgrade;
        this.meta = meta;
        this.type = type;
    }


}
