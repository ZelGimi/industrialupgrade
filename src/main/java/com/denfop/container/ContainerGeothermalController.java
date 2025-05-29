package com.denfop.container;

import com.denfop.tiles.geothermalpump.TileEntityGeothermalController;
import net.minecraft.world.entity.player.Player;

public class ContainerGeothermalController extends ContainerFullInv<TileEntityGeothermalController> {

    public ContainerGeothermalController(
            TileEntityGeothermalController tileEntityGeothermalController,
            Player entityPlayer
    ) {
        super(tileEntityGeothermalController, entityPlayer);
    }

}
