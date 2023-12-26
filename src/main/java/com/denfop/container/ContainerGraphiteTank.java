package com.denfop.container;

import com.denfop.tiles.reactors.graphite.tank.TileEntityMainTank;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGraphiteTank extends ContainerFullInv<TileEntityMainTank> {

    public ContainerGraphiteTank(TileEntityMainTank tileEntityMainTank, EntityPlayer var1) {
        super(var1,tileEntityMainTank,188,209);
    }

}
