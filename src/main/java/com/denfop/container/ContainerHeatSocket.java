package com.denfop.container;

import com.denfop.tiles.reactors.heat.socket.TileEntityMainSocket;
import net.minecraft.world.entity.player.Player;

public class ContainerHeatSocket extends ContainerFullInv<TileEntityMainSocket> {

    public ContainerHeatSocket(TileEntityMainSocket tileEntityMainSocket, Player var1) {
        super(var1, tileEntityMainSocket, 188, 210);
    }

}
