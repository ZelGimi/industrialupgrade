package com.denfop.container;

import com.denfop.tiles.reactors.heat.fueltank.TileEntityMainTank;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerHeatTank extends ContainerFullInv<TileEntityMainTank> {

    public ContainerHeatTank(TileEntityMainTank tileEntityMainTank, EntityPlayer var1) {
        super(var1,tileEntityMainTank,188,209);
    }

}
