package com.denfop.containermenu;

import com.denfop.blockentity.gasturbine.BlockEntityGasTurbineController;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuGasTurbineController extends ContainerMenuFullInv<BlockEntityGasTurbineController> {

    public ContainerMenuGasTurbineController(
            BlockEntityGasTurbineController tileEntityGeothermalController,
            Player entityPlayer
    ) {
        super(tileEntityGeothermalController, entityPlayer);
    }

}
