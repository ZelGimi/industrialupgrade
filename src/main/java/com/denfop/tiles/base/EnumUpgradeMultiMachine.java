package com.denfop.tiles.base;

import com.denfop.tiles.mechanism.TileEntityCombDoubleMacerator;
import com.denfop.tiles.mechanism.TileEntityCombQuadMacerator;
import com.denfop.tiles.mechanism.TileEntityCombTripleMacerator;
import com.denfop.tiles.mechanism.TileEntityDoubleAssamplerScrap;
import com.denfop.tiles.mechanism.TileEntityDoubleCutting;
import com.denfop.tiles.mechanism.TileEntityDoubleExtruding;
import com.denfop.tiles.mechanism.TileEntityDoubleFermer;
import com.denfop.tiles.mechanism.TileEntityDoubleRolling;
import com.denfop.tiles.mechanism.TileEntityQuadAssamplerScrap;
import com.denfop.tiles.mechanism.TileEntityQuadCombRecycler;
import com.denfop.tiles.mechanism.TileEntityQuadCompressor;
import com.denfop.tiles.mechanism.TileEntityQuadCutting;
import com.denfop.tiles.mechanism.TileEntityQuadElectricFurnace;
import com.denfop.tiles.mechanism.TileEntityQuadExtractor;
import com.denfop.tiles.mechanism.TileEntityQuadExtruding;
import com.denfop.tiles.mechanism.TileEntityQuadFermer;
import com.denfop.tiles.mechanism.TileEntityQuadMacerator;
import com.denfop.tiles.mechanism.TileEntityQuadMetalFormer;
import com.denfop.tiles.mechanism.TileEntityQuadRecycler;
import com.denfop.tiles.mechanism.TileEntityQuadRolling;
import com.denfop.tiles.mechanism.TileEntityTripleAssamplerScrap;
import com.denfop.tiles.mechanism.TileEntityTripleCombRecycler;
import com.denfop.tiles.mechanism.TileEntityTripleCompressor;
import com.denfop.tiles.mechanism.TileEntityTripleCutting;
import com.denfop.tiles.mechanism.TileEntityTripleElectricFurnace;
import com.denfop.tiles.mechanism.TileEntityTripleExtractor;
import com.denfop.tiles.mechanism.TileEntityTripleExtruding;
import com.denfop.tiles.mechanism.TileEntityTripleFermer;
import com.denfop.tiles.mechanism.TileEntityTripleMacerator;
import com.denfop.tiles.mechanism.TileEntityTripleMetalFormer;
import com.denfop.tiles.mechanism.TileEntityTripleRecycler;
import com.denfop.tiles.mechanism.TileEntityTripleRolling;
import ic2.core.block.TileEntityBlock;

public enum EnumUpgradeMultiMachine {
    DOUBLE_MACERATOR(EnumMultiMachine.DOUBLE_MACERATOR, new TileEntityTripleMacerator()),
    TRIPLE_MACERATOR(EnumMultiMachine.TRIPLE_MACERATOR, new TileEntityQuadMacerator()),
    QUAD_MACERATOR(EnumMultiMachine.QUAD_MACERATOR),

    DOUBLE_COMPRESSER(EnumMultiMachine.DOUBLE_COMPRESSER, new TileEntityTripleCompressor()),
    TRIPLE_COMPRESSER(EnumMultiMachine.TRIPLE_COMPRESSER, new TileEntityQuadCompressor()),
    QUAD_COMPRESSER(EnumMultiMachine.QUAD_COMPRESSER),

    DOUBLE_EXTRACTOR(EnumMultiMachine.DOUBLE_EXTRACTOR, new TileEntityTripleExtractor()),
    TRIPLE_EXTRACTOR(EnumMultiMachine.TRIPLE_EXTRACTOR, new TileEntityQuadExtractor()),
    QUAD_EXTRACTOR(EnumMultiMachine.QUAD_EXTRACTOR),

    DOUBLE_ELECTRIC_FURNACE(EnumMultiMachine.DOUBLE_ELECTRIC_FURNACE, new TileEntityTripleElectricFurnace()),
    TRIPLE_ELECTRIC_FURNACE(EnumMultiMachine.TRIPLE_ELECTRIC_FURNACE, new TileEntityQuadElectricFurnace()),
    QUAD_ELECTRIC_FURNACE(EnumMultiMachine.QUAD_ELECTRIC_FURNACE),

    DOUBLE_METAL_FORMER(EnumMultiMachine.DOUBLE_METAL_FORMER, new TileEntityTripleMetalFormer()),
    TRIPLE_METAL_FORMER(EnumMultiMachine.TRIPLE_METAL_FORMER, new TileEntityQuadMetalFormer()),
    QUAD_METAL_FORMER(EnumMultiMachine.QUAD_METAL_FORMER),

    DOUBLE_RECYCLER(EnumMultiMachine.DOUBLE_RECYCLER, new TileEntityTripleRecycler()),
    TRIPLE_RECYCLER(EnumMultiMachine.TRIPLE_RECYCLER, new TileEntityQuadRecycler()),
    QUAD_RECYCLER(EnumMultiMachine.QUAD_RECYCLER),

    DOUBLE_COMB_RECYCLER(EnumMultiMachine.DOUBLE_COMB_RECYCLER, new TileEntityTripleCombRecycler()),
    TRIPLE_COMB_RRECYCLER(EnumMultiMachine.TRIPLE_COMB_RRECYCLER, new TileEntityQuadCombRecycler()),
    QUAD_COMB_RRECYCLER(EnumMultiMachine.QUAD_COMB_RRECYCLER),
    COMB_MACERATOR(EnumMultiMachine.COMB_MACERATOR, new TileEntityCombDoubleMacerator()),
    COMB_DOUBLE_MACERATOR(EnumMultiMachine.COMB_DOUBLE_MACERATOR, new TileEntityCombTripleMacerator()),
    COMB_TRIPLE_MACERATOR(EnumMultiMachine.COMB_TRIPLE_MACERATOR, new TileEntityCombQuadMacerator()),
    COMB_QUAD_MACERATOR(EnumMultiMachine.COMB_QUAD_MACERATOR),

    Rolling(EnumMultiMachine.Rolling, new TileEntityDoubleRolling()),
    DOUBLE_Rolling(EnumMultiMachine.DOUBLE_Rolling, new TileEntityTripleRolling()),
    TRIPLE_Rolling(EnumMultiMachine.TRIPLE_Rolling, new TileEntityQuadRolling()),
    QUAD_Rolling(EnumMultiMachine.QUAD_Rolling),
    Extruding(EnumMultiMachine.Extruding, new TileEntityDoubleExtruding()),
    DOUBLE_Extruding(EnumMultiMachine.DOUBLE_Extruding, new TileEntityTripleExtruding()),
    TRIPLE_Extruding(EnumMultiMachine.TRIPLE_Extruding, new TileEntityQuadExtruding()),
    QUAD_Extruding(EnumMultiMachine.QUAD_Extruding),
    Cutting(EnumMultiMachine.Cutting, new TileEntityDoubleCutting()),
    DOUBLE_Cutting(EnumMultiMachine.DOUBLE_Cutting, new TileEntityTripleCutting()),
    TRIPLE_Cutting(EnumMultiMachine.TRIPLE_Cutting, new TileEntityQuadCutting()),
    QUAD_Cutting(EnumMultiMachine.QUAD_Cutting),

    Fermer(EnumMultiMachine.Fermer, new TileEntityDoubleFermer()),
    DOUBLE_Fermer(EnumMultiMachine.DOUBLE_Fermer, new TileEntityTripleFermer()),
    TRIPLE_Fermer(EnumMultiMachine.TRIPLE_Fermer, new TileEntityQuadFermer()),
    QUAD_Fermer(EnumMultiMachine.QUAD_Fermer),

    AssamplerScrap(EnumMultiMachine.AssamplerScrap, new TileEntityDoubleAssamplerScrap()),
    DOUBLE_AssamplerScrap(EnumMultiMachine.DOUBLE_AssamplerScrap, new TileEntityTripleAssamplerScrap()),
    TRIPLE_AssamplerScrap(EnumMultiMachine.TRIPLE_AssamplerScrap, new TileEntityQuadAssamplerScrap()),
    QUAD_AssamplerScrap(EnumMultiMachine.QUAD_AssamplerScrap);

    public final EnumMultiMachine multimachine;
    public final TileEntityBlock tile;

    EnumUpgradeMultiMachine(EnumMultiMachine multimachine, TileEntityBlock tile) {
        this.multimachine = multimachine;
        this.tile = tile;
    }

    EnumUpgradeMultiMachine(EnumMultiMachine multimachine) {
        this(multimachine, null);
    }


}
