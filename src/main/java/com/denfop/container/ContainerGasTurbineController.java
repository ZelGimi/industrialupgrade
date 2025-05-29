package com.denfop.container;

import com.denfop.tiles.gasturbine.TileEntityGasTurbineController;
import net.minecraft.world.entity.player.Player;

public class ContainerGasTurbineController extends ContainerFullInv<TileEntityGasTurbineController> {

    public ContainerGasTurbineController(
            TileEntityGasTurbineController tileEntityGeothermalController,
            Player entityPlayer
    ) {
        super(tileEntityGeothermalController, entityPlayer);
    }

}
