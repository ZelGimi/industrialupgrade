package com.denfop.container;

import com.denfop.tiles.reactors.water.socket.TileEntityMainSocket;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerWaterSocket extends ContainerFullInv<TileEntityMainSocket> {

    public ContainerWaterSocket(TileEntityMainSocket tileEntityMainSocket, EntityPlayer var1) {
        super(tileEntityMainSocket, var1);
    }

}
