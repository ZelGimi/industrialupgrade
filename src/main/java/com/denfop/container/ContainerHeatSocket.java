package com.denfop.container;

import com.denfop.tiles.reactors.heat.socket.TileEntityMainSocket;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerHeatSocket extends ContainerFullInv<TileEntityMainSocket> {

    public ContainerHeatSocket(TileEntityMainSocket tileEntityMainSocket, EntityPlayer var1) {
        super(var1, tileEntityMainSocket, 188, 210);
    }

}
