package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityAmpereStorage;
import net.minecraft.world.entity.player.Player;

public class ContainerAmpereStorage extends ContainerFullInv<TileEntityAmpereStorage> {

    public ContainerAmpereStorage(Player entityPlayer, TileEntityAmpereStorage tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);


    }


}
