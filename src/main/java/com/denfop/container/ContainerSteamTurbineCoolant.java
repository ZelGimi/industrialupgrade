package com.denfop.container;

import com.denfop.tiles.mechanism.steamturbine.coolant.TileEntityBaseSteamTurbineCoolant;
import com.denfop.tiles.mechanism.steamturbine.tank.TileEntityBaseSteamTurbineTank;
import com.denfop.tiles.reactors.gas.cell.TileEntityMainTank;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamTurbineCoolant extends ContainerFullInv<TileEntityBaseSteamTurbineCoolant> {

    public ContainerSteamTurbineCoolant(TileEntityBaseSteamTurbineCoolant tileEntityMainTank, EntityPlayer var1) {
        super(var1, tileEntityMainTank);
    }

}
