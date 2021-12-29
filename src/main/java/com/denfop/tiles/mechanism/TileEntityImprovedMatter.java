package com.denfop.tiles.mechanism;

import com.denfop.tiles.base.TileEntityMultiMatter;

public class TileEntityImprovedMatter extends TileEntityMultiMatter {

    public TileEntityImprovedMatter() {
        super(800000F, 10, 64000000);
    }

    @Override
    public String getInventoryName() {
        return "iu.blockMatter2.name";
    }

}
