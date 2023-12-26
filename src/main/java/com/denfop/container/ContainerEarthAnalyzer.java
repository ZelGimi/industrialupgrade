package com.denfop.container;

import com.denfop.tiles.quarry_earth.TileEntityEarthQuarryAnalyzer;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerEarthAnalyzer extends ContainerFullInv<TileEntityEarthQuarryAnalyzer> {

    public ContainerEarthAnalyzer(TileEntityEarthQuarryAnalyzer tileEntityEarthQuarryAnalyzer, EntityPlayer var1) {
        super(tileEntityEarthQuarryAnalyzer,var1);
    }

}
