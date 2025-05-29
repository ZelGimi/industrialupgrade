package com.denfop.container;

import com.denfop.tiles.mechanism.steamturbine.coolant.TileEntityBaseSteamTurbineCoolant;
import net.minecraft.world.entity.player.Player;

public class ContainerSteamTurbineCoolant extends ContainerFullInv<TileEntityBaseSteamTurbineCoolant> {

    public ContainerSteamTurbineCoolant(TileEntityBaseSteamTurbineCoolant tileEntityMainTank, Player var1) {
        super(var1, tileEntityMainTank);
    }

}
