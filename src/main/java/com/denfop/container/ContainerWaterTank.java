package com.denfop.container;

import com.denfop.tiles.reactors.water.tank.TileEntityMainTank;
import net.minecraft.world.entity.player.Player;

public class ContainerWaterTank extends ContainerFullInv<TileEntityMainTank> {

    public ContainerWaterTank(TileEntityMainTank tileEntityMainTank, Player var1) {
        super(tileEntityMainTank, var1);
    }

}
