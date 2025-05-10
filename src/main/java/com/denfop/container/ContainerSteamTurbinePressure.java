package com.denfop.container;

import com.denfop.tiles.mechanism.steamturbine.pressure.TileEntityBaseSteamTurbinePressure;
import net.minecraft.world.entity.player.Player;

public class ContainerSteamTurbinePressure extends ContainerFullInv<TileEntityBaseSteamTurbinePressure> {

    public ContainerSteamTurbinePressure(TileEntityBaseSteamTurbinePressure tileEntityBaseCompressor, Player var1) {
        super(var1, tileEntityBaseCompressor);
    }

}
