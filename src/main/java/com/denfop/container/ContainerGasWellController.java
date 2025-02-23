package com.denfop.container;

import com.denfop.tiles.gaswell.TileEntityGasWellAnalyzer;
import com.denfop.tiles.gaswell.TileEntityGasWellController;
import com.denfop.tiles.quarry_earth.TileEntityEarthQuarryAnalyzer;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGasWellController extends ContainerFullInv<TileEntityGasWellController> {

    public ContainerGasWellController(TileEntityGasWellController tileEntityEarthQuarryAnalyzer, EntityPlayer var1) {
        super(tileEntityEarthQuarryAnalyzer, var1);
    }

}
