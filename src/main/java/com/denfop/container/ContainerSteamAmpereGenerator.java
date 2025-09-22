package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileEntitySteamAmpereGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamAmpereGenerator extends ContainerFullInv<TileEntitySteamAmpereGenerator> {

    public ContainerSteamAmpereGenerator(EntityPlayer entityPlayer, TileEntitySteamAmpereGenerator tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);


    }


}
