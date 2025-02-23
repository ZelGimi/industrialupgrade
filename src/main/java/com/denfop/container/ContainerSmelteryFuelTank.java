package com.denfop.container;

import com.denfop.tiles.smeltery.TileEntitySmelteryCasting;
import com.denfop.tiles.smeltery.TileEntitySmelteryFuelTank;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSmelteryFuelTank extends ContainerFullInv<TileEntitySmelteryFuelTank> {

    public ContainerSmelteryFuelTank(
            TileEntitySmelteryFuelTank tileEntityGeothermalExchanger,
            EntityPlayer var1
    ) {
        super(var1, tileEntityGeothermalExchanger, 182);
      }

}
