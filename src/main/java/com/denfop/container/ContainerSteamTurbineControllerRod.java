package com.denfop.container;

import com.denfop.tiles.mechanism.steamturbine.TileEntitySteamTurbineControllerRod;
import net.minecraft.world.entity.player.Player;

public class ContainerSteamTurbineControllerRod extends ContainerFullInv<TileEntitySteamTurbineControllerRod> {

    public ContainerSteamTurbineControllerRod(
            TileEntitySteamTurbineControllerRod tile,
            Player var1
    ) {
        super(var1, tile,200,166);
    }

}
