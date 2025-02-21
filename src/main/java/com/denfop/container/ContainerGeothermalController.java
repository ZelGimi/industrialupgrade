package com.denfop.container;

import com.denfop.tiles.geothermalpump.TileEntityGeothermalController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGeothermalController extends ContainerFullInv<TileEntityGeothermalController> {

    public ContainerGeothermalController(
            TileEntityGeothermalController tileEntityGeothermalController,
            EntityPlayer entityPlayer
    ) {
        super(tileEntityGeothermalController, entityPlayer);
    }

}
