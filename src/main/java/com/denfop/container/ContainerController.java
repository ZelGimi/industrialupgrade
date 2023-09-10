package com.denfop.container;

import com.denfop.tiles.mechanism.energy.TileEnergyController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerController extends ContainerFullInv<TileEnergyController> {

    public ContainerController(TileEnergyController tileEntity1, EntityPlayer entityPlayer) {
        super(entityPlayer, tileEntity1, 179);
    }


}
