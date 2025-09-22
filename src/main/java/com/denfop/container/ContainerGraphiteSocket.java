package com.denfop.container;

import com.denfop.tiles.reactors.graphite.socket.TileEntityMainSocket;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGraphiteSocket extends ContainerFullInv<TileEntityMainSocket> {

    public ContainerGraphiteSocket(TileEntityMainSocket tileEntityMainSocket, EntityPlayer var1) {
        super(var1, tileEntityMainSocket, 188, 208);
    }

}
