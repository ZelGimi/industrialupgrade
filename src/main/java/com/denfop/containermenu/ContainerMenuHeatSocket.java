package com.denfop.containermenu;

import com.denfop.blockentity.reactors.heat.socket.BlockEntityMainSocket;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuHeatSocket extends ContainerMenuFullInv<BlockEntityMainSocket> {

    public ContainerMenuHeatSocket(BlockEntityMainSocket tileEntityMainSocket, Player var1) {
        super(var1, tileEntityMainSocket, 188, 210);
    }

}
