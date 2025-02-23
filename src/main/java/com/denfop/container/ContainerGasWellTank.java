package com.denfop.container;

import com.denfop.tiles.gaswell.TileEntityGasWellTank;
import com.denfop.tiles.geothermalpump.TileEntityGeothermalExchanger;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGasWellTank extends ContainerFullInv<TileEntityGasWellTank> {

    public ContainerGasWellTank(TileEntityGasWellTank tileEntityGeothermalExchanger, EntityPlayer var1) {
        super(tileEntityGeothermalExchanger, var1);
    }

}
