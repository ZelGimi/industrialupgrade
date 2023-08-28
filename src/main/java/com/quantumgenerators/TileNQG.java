package com.quantumgenerators;

import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;

public class TileNQG extends TileQuantumGenerator {

    public TileNQG() {
        super(10, "nsp_gen", 9);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockQG.nsp_gen;
    }

    public BlockTileEntity getBlock() {
        return QGCore.qg;
    }

}
