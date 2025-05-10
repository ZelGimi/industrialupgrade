package com.denfop.container;

import com.denfop.tiles.reactors.gas.cell.TileEntityMainTank;
import net.minecraft.world.entity.player.Player;

public class ContainerGasTank extends ContainerFullInv<TileEntityMainTank> {

    public ContainerGasTank(TileEntityMainTank tileEntityMainTank, Player var1) {
        super(var1, tileEntityMainTank, 188, 209);
    }

}
