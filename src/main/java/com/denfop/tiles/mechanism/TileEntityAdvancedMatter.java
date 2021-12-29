package com.denfop.tiles.mechanism;

import com.denfop.tiles.base.TileEntityMultiMatter;


public class TileEntityAdvancedMatter extends TileEntityMultiMatter {

    public TileEntityAdvancedMatter() {
        super(900000F, 8, 8000000);
    }

    public String getInventoryName() {
        return "iu.blockMatter1.name";
    }

}
