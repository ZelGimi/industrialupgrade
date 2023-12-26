package com.denfop.container;

import com.denfop.tiles.reactors.gas.socket.TileEntityMainSocket;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGasSocket extends ContainerFullInv<TileEntityMainSocket> {

    public ContainerGasSocket(TileEntityMainSocket tileEntityMainSocket, EntityPlayer var1) {
        super(var1,tileEntityMainSocket,188,209);
    }

}
