package com.denfop.container;

import com.denfop.tiles.reactors.water.socket.TileEntityMainSocket;
import net.minecraft.world.entity.player.Player;

public class ContainerWaterSocket extends ContainerFullInv<TileEntityMainSocket> {

    public ContainerWaterSocket(TileEntityMainSocket tileEntityMainSocket, Player var1) {
        super(tileEntityMainSocket, var1);
    }

}
