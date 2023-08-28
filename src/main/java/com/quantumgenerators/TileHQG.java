package com.quantumgenerators;

import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;

public class TileHQG extends TileQuantumGenerator {

    public TileHQG() {
        super(12, "adsp_gen", 11);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockQG.adsp_gen;
    }

    public BlockTileEntity getBlock() {
        return QGCore.qg;
    }

}
