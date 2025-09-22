package com.denfop.container;

import com.denfop.tiles.mechanism.steamturbine.controller.TileEntityBaseSteamTurbineController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerBaseSteamTurbineController extends ContainerFullInv<TileEntityBaseSteamTurbineController> {

    public ContainerBaseSteamTurbineController(
            TileEntityBaseSteamTurbineController tile,
            EntityPlayer var1
    ) {
        super(var1, tile, 178 + 40, 166 + 20);
    }

}
