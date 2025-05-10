package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileEntitySteamFluidHeater;
import net.minecraft.world.entity.player.Player;

public class ContainerSteamFluidHeater extends ContainerFullInv<TileEntitySteamFluidHeater> {

    public ContainerSteamFluidHeater(Player entityPlayer, TileEntitySteamFluidHeater tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);


    }


}
