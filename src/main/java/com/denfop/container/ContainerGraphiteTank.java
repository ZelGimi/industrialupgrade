package com.denfop.container;

import com.denfop.tiles.reactors.graphite.tank.TileEntityMainTank;
import net.minecraft.world.entity.player.Player;

public class ContainerGraphiteTank extends ContainerFullInv<TileEntityMainTank> {

    public ContainerGraphiteTank(TileEntityMainTank tileEntityMainTank, Player var1) {
        super(var1, tileEntityMainTank, 188, 208);
    }

}
