package com.denfop.container;

import com.denfop.tiles.reactors.heat.fueltank.TileEntityMainTank;
import net.minecraft.world.entity.player.Player;

public class ContainerHeatTank extends ContainerFullInv<TileEntityMainTank> {

    public ContainerHeatTank(TileEntityMainTank tileEntityMainTank, Player var1) {
        super(var1, tileEntityMainTank, 188, 210);
    }

}
