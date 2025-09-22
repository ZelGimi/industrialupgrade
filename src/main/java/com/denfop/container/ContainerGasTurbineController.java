package com.denfop.container;

import com.denfop.tiles.gasturbine.TileEntityGasTurbineController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGasTurbineController extends ContainerFullInv<TileEntityGasTurbineController> {

    public ContainerGasTurbineController(
            TileEntityGasTurbineController tileEntityGeothermalController,
            EntityPlayer entityPlayer
    ) {
        super(tileEntityGeothermalController, entityPlayer);
    }

}
