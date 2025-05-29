package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileEntitySteamPressureConverter;
import net.minecraft.world.entity.player.Player;

public class ContainerSteamPressureConverter extends ContainerFullInv<TileEntitySteamPressureConverter> {

    public ContainerSteamPressureConverter(
            Player entityPlayer,
            TileEntitySteamPressureConverter tileEntityBaseHeatMachine
    ) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);


    }


}
