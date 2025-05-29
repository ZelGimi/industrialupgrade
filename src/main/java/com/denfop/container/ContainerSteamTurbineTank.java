package com.denfop.container;

import com.denfop.tiles.mechanism.steamturbine.tank.TileEntityBaseSteamTurbineTank;
import net.minecraft.world.entity.player.Player;

public class ContainerSteamTurbineTank extends ContainerFullInv<TileEntityBaseSteamTurbineTank> {

    public ContainerSteamTurbineTank(TileEntityBaseSteamTurbineTank tileEntityMainTank, Player var1) {
        super(var1, tileEntityMainTank);
    }

}
