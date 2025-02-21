package com.denfop.container;

import com.denfop.tiles.mechanism.steamturbine.TileEntitySteamTurbineControllerRod;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamTurbineControllerRod extends ContainerFullInv<TileEntitySteamTurbineControllerRod> {

    public ContainerSteamTurbineControllerRod(
            TileEntitySteamTurbineControllerRod tile,
            EntityPlayer var1
    ) {
        super(tile,var1);
    }

}
