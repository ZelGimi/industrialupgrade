package com.denfop.container;

import com.denfop.tiles.reactors.gas.socket.TileEntityMainSocket;
import net.minecraft.world.entity.player.Player;

public class ContainerGasSocket extends ContainerFullInv<TileEntityMainSocket> {

    public ContainerGasSocket(TileEntityMainSocket tileEntityMainSocket, Player var1) {
        super(var1, tileEntityMainSocket, 188, 209);
    }

}
