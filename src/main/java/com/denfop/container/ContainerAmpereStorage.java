package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityAmpereGenerator;
import com.denfop.tiles.mechanism.TileEntityAmpereStorage;
import com.denfop.tiles.mechanism.steam.TileEntitySteamAmpereGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerAmpereStorage extends ContainerFullInv<TileEntityAmpereStorage> {

    public ContainerAmpereStorage(EntityPlayer entityPlayer, TileEntityAmpereStorage tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);


    }


}
