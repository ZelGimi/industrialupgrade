package com.denfop.container;

import com.denfop.tiles.mechanism.steamturbine.tank.TileEntityBaseSteamTurbineTank;
import com.denfop.tiles.reactors.gas.cell.TileEntityMainTank;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamTurbineTank extends ContainerFullInv<TileEntityBaseSteamTurbineTank> {

    public ContainerSteamTurbineTank(TileEntityBaseSteamTurbineTank tileEntityMainTank, EntityPlayer var1) {
        super(var1, tileEntityMainTank);
    }

}
