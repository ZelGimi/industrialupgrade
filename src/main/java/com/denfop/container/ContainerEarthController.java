package com.denfop.container;

import com.denfop.tiles.quarry_earth.TileEntityEarthQuarryController;
import net.minecraft.world.entity.player.Player;

public class ContainerEarthController extends ContainerFullInv<TileEntityEarthQuarryController> {

    public ContainerEarthController(TileEntityEarthQuarryController tileEntityEarthQuarryController, Player entityPlayer) {
        super(tileEntityEarthQuarryController, entityPlayer);
    }

}
