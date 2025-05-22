package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileEntitySteamFluidHeater;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamFluidHeater extends ContainerFullInv<TileEntitySteamFluidHeater> {

    public ContainerSteamFluidHeater(EntityPlayer entityPlayer, TileEntitySteamFluidHeater tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);


    }


}
