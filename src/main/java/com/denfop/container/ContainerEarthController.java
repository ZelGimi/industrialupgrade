package com.denfop.container;

import com.denfop.tiles.quarry_earth.TileEntityEarthQuarryController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerEarthController extends ContainerFullInv<TileEntityEarthQuarryController> {

    public ContainerEarthController(TileEntityEarthQuarryController tileEntityEarthQuarryController, EntityPlayer entityPlayer) {
        super(tileEntityEarthQuarryController, entityPlayer);
    }

}
