package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileEntitySteamAmpereGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerSteamAmpereGenerator extends ContainerFullInv<TileEntitySteamAmpereGenerator> {

    public ContainerSteamAmpereGenerator(Player entityPlayer, TileEntitySteamAmpereGenerator tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);


    }


}
