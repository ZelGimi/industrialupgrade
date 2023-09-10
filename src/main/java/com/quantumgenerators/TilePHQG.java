package com.quantumgenerators;

import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;

public class TilePHQG extends TileQuantumGenerator {

    public TilePHQG() {
        super(9, "phsp_gen", 8);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockQG.phsp_gen;
    }

    public BlockTileEntity getBlock() {
        return QGCore.qg;
    }

}
