package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileEntitySteamConverter;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamConverter extends ContainerFullInv<TileEntitySteamConverter> {

    public ContainerSteamConverter(EntityPlayer entityPlayer, TileEntitySteamConverter tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);


    }


}
