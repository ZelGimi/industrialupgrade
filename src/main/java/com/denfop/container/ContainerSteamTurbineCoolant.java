package com.denfop.container;

import com.denfop.tiles.mechanism.steamturbine.coolant.TileEntityBaseSteamTurbineCoolant;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamTurbineCoolant extends ContainerFullInv<TileEntityBaseSteamTurbineCoolant> {

    public ContainerSteamTurbineCoolant(TileEntityBaseSteamTurbineCoolant tileEntityMainTank, EntityPlayer var1) {
        super(var1, tileEntityMainTank);
    }

}
