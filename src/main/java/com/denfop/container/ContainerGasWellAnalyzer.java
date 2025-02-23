package com.denfop.container;

import com.denfop.tiles.gaswell.TileEntityGasWellAnalyzer;
import com.denfop.tiles.quarry_earth.TileEntityEarthQuarryAnalyzer;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGasWellAnalyzer extends ContainerFullInv<TileEntityGasWellAnalyzer> {

    public ContainerGasWellAnalyzer(TileEntityGasWellAnalyzer tileEntityEarthQuarryAnalyzer, EntityPlayer var1) {
        super(tileEntityEarthQuarryAnalyzer, var1);
    }

}
