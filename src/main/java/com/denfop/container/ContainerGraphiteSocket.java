package com.denfop.container;

import com.denfop.tiles.reactors.graphite.socket.TileEntityMainSocket;
import net.minecraft.world.entity.player.Player;

public class ContainerGraphiteSocket extends ContainerFullInv<TileEntityMainSocket> {

    public ContainerGraphiteSocket(TileEntityMainSocket tileEntityMainSocket, Player var1) {
        super(var1, tileEntityMainSocket, 188, 208);
    }

}
