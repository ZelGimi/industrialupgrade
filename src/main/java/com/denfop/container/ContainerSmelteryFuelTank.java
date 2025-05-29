package com.denfop.container;

import com.denfop.tiles.smeltery.TileEntitySmelteryFuelTank;
import net.minecraft.world.entity.player.Player;

public class ContainerSmelteryFuelTank extends ContainerFullInv<TileEntitySmelteryFuelTank> {

    public ContainerSmelteryFuelTank(
            TileEntitySmelteryFuelTank tileEntityGeothermalExchanger,
            Player var1
    ) {
        super(var1, tileEntityGeothermalExchanger, 166);
    }

}
