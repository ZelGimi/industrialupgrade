package com.denfop.container;

import com.denfop.tiles.mechanism.energy.TileEnergyController;
import net.minecraft.world.entity.player.Player;

public class ContainerController extends ContainerFullInv<TileEnergyController> {

    public ContainerController(TileEnergyController tileEntity1, Player entityPlayer) {
        super(entityPlayer, tileEntity1);
    }


}
