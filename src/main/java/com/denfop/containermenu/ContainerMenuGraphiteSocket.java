package com.denfop.containermenu;

import com.denfop.blockentity.reactors.graphite.socket.BlockEntityMainSocket;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuGraphiteSocket extends ContainerMenuFullInv<BlockEntityMainSocket> {

    public ContainerMenuGraphiteSocket(BlockEntityMainSocket tileEntityMainSocket, Player var1) {
        super(var1, tileEntityMainSocket, 188, 208);
    }

}
