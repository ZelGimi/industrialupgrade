package com.denfop.container;

import com.denfop.tiles.chemicalplant.TileEntityChemicalPlantController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerChemicalController extends ContainerFullInv<TileEntityChemicalPlantController> {

    public ContainerChemicalController(
            TileEntityChemicalPlantController tileEntityGeothermalController,
            EntityPlayer entityPlayer
    ) {
        super(tileEntityGeothermalController, entityPlayer);
    }

}
