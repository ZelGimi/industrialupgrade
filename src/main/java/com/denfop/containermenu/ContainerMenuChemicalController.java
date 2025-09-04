package com.denfop.containermenu;

import com.denfop.blockentity.chemicalplant.BlockEntityChemicalPlantController;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuChemicalController extends ContainerMenuFullInv<BlockEntityChemicalPlantController> {

    public ContainerMenuChemicalController(
            BlockEntityChemicalPlantController tileEntityGeothermalController,
            Player entityPlayer
    ) {
        super(tileEntityGeothermalController, entityPlayer);
    }

}
