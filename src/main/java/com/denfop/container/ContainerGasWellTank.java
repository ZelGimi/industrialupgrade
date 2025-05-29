package com.denfop.container;

import com.denfop.tiles.gaswell.TileEntityGasWellTank;
import net.minecraft.world.entity.player.Player;

public class ContainerGasWellTank extends ContainerFullInv<TileEntityGasWellTank> {

    public ContainerGasWellTank(TileEntityGasWellTank tileEntityGeothermalExchanger, Player var1) {
        super(tileEntityGeothermalExchanger, var1);
    }

}
