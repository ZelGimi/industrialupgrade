package com.denfop.containermenu;

import com.denfop.blockentity.reactors.graphite.tank.BlockEntityMainTank;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuGraphiteTank extends ContainerMenuFullInv<BlockEntityMainTank> {

    public ContainerMenuGraphiteTank(BlockEntityMainTank tileEntityMainTank, Player var1) {
        super(var1, tileEntityMainTank, 188, 208);
    }

}
