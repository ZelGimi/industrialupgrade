package com.denfop.tiles.mechanism;

import com.denfop.tiles.base.TileEntityMultiMatter;

public class TileEntityUltimateMatter extends TileEntityMultiMatter {

    public TileEntityUltimateMatter() {
        super(700000F, 12, 256000000);
    }

    @Override
    public String getInventoryName() {
        return "iu.blockMatter3.name";
    }

}
