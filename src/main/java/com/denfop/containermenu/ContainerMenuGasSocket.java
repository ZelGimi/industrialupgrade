package com.denfop.containermenu;

import com.denfop.blockentity.reactors.gas.socket.BlockEntityMainSocket;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuGasSocket extends ContainerMenuFullInv<BlockEntityMainSocket> {

    public ContainerMenuGasSocket(BlockEntityMainSocket tileEntityMainSocket, Player var1) {
        super(var1, tileEntityMainSocket, 188, 209);
    }

}
