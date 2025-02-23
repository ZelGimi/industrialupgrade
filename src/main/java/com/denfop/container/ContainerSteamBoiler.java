package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileEntitySteamBoiler;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamBoiler extends ContainerFullInv<TileEntitySteamBoiler> {

    public ContainerSteamBoiler(EntityPlayer entityPlayer, TileEntitySteamBoiler tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);


    }


}
