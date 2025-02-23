package com.denfop.container;

import com.denfop.tiles.mechanism.steamturbine.pressure.TileEntityBaseSteamTurbinePressure;
import com.denfop.tiles.reactors.gas.compressor.TileEntityBaseCompressor;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamTurbinePressure extends ContainerFullInv<TileEntityBaseSteamTurbinePressure> {

    public ContainerSteamTurbinePressure(TileEntityBaseSteamTurbinePressure tileEntityBaseCompressor, EntityPlayer var1) {
        super(var1, tileEntityBaseCompressor);
    }

}
