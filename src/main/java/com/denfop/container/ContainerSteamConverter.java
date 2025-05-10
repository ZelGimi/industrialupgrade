package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileEntitySteamConverter;
import net.minecraft.world.entity.player.Player;

public class ContainerSteamConverter extends ContainerFullInv<TileEntitySteamConverter> {

    public ContainerSteamConverter(Player entityPlayer, TileEntitySteamConverter tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);


    }


}
