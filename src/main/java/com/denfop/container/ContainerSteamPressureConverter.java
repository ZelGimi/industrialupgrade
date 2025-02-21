package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileEntitySteamPressureConverter;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamPressureConverter extends ContainerFullInv<TileEntitySteamPressureConverter> {

    public ContainerSteamPressureConverter(
            EntityPlayer entityPlayer,
            TileEntitySteamPressureConverter tileEntityBaseHeatMachine
    ) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);


    }


}
