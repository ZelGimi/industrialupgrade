package com.denfop.container;


import com.denfop.tiles.mechanism.steam.TileEntitySteamBoiler;
import net.minecraft.world.entity.player.Player;

public class ContainerSteamBoiler extends ContainerFullInv<TileEntitySteamBoiler> {

    public ContainerSteamBoiler(Player entityPlayer, TileEntitySteamBoiler tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);


    }


}
