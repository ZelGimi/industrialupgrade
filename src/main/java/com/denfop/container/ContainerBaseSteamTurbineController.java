package com.denfop.container;

import com.denfop.tiles.mechanism.steamturbine.controller.TileEntityBaseSteamTurbineController;
import net.minecraft.world.entity.player.Player;

public class ContainerBaseSteamTurbineController extends ContainerFullInv<TileEntityBaseSteamTurbineController> {

    public ContainerBaseSteamTurbineController(
            TileEntityBaseSteamTurbineController tile,
            Player var1
    ) {
        super(var1, tile);
    }

}
