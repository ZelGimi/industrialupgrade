package com.denfop.container;

import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.reactors.water.ISecurity;
import net.minecraft.world.entity.player.Player;

public class ContainerWaterSecurity extends ContainerFullInv<TileEntityInventory> {

    public ContainerWaterSecurity(ISecurity tileEntityPerSecurity, Player var1) {
        super((TileEntityInventory) tileEntityPerSecurity, var1);
    }

}
