package com.quantumgenerators;

import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;

public class TileKQG extends TileQuantumGenerator {

    public TileKQG() {
        super(14, "kvsp_gen", 13);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockQG.kvsp_gen;
    }

    public BlockTileEntity getBlock() {
        return QGCore.qg;
    }

}
