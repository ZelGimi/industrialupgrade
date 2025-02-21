package com.denfop.container;

import com.denfop.tiles.reactors.water.tank.TileEntityMainTank;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerWaterTank extends ContainerFullInv<TileEntityMainTank> {

    public ContainerWaterTank(TileEntityMainTank tileEntityMainTank, EntityPlayer var1) {
        super(tileEntityMainTank, var1);
    }

}
