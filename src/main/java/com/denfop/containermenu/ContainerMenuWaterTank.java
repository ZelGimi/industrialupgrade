package com.denfop.containermenu;

import com.denfop.blockentity.reactors.water.tank.BlockEntityMainTank;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuWaterTank extends ContainerMenuFullInv<BlockEntityMainTank> {

    public ContainerMenuWaterTank(BlockEntityMainTank tileEntityMainTank, Player var1) {
        super(tileEntityMainTank, var1);
    }

}
