package com.denfop.container;

import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerFullInv;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.reactors.water.IInput;
import com.denfop.tiles.reactors.water.ISecurity;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerWaterSecurity extends ContainerFullInv<TileEntityInventory> {

    public ContainerWaterSecurity(ISecurity tileEntityPerSecurity, EntityPlayer var1) {
        super((TileEntityInventory) tileEntityPerSecurity,var1);
    }

}
