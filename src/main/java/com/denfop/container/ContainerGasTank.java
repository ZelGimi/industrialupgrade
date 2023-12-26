package com.denfop.container;

import com.denfop.tiles.reactors.gas.cell.TileEntityMainTank;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGasTank extends ContainerFullInv<TileEntityMainTank> {

    public ContainerGasTank(TileEntityMainTank tileEntityMainTank, EntityPlayer var1) {
        super(var1,tileEntityMainTank,188,209);
    }

}
