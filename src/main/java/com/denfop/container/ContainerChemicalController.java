package com.denfop.container;

import com.denfop.tiles.chemicalplant.TileEntityChemicalPlantController;
import net.minecraft.world.entity.player.Player;

public class ContainerChemicalController extends ContainerFullInv<TileEntityChemicalPlantController> {

    public ContainerChemicalController(
            TileEntityChemicalPlantController tileEntityGeothermalController,
            Player entityPlayer
    ) {
        super(tileEntityGeothermalController, entityPlayer);
    }

}
