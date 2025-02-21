package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityAmpereGenerator;
import com.denfop.tiles.mechanism.steam.TileEntitySteamAmpereGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerAmpereGenerator extends ContainerFullInv<TileEntityAmpereGenerator> {

    public ContainerAmpereGenerator(EntityPlayer entityPlayer, TileEntityAmpereGenerator tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);


    }


}
