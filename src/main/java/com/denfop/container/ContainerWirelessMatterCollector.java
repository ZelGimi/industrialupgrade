package com.denfop.container;

import com.denfop.tiles.base.TileEntityWirelessMatterCollector;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerWirelessMatterCollector extends ContainerFullInv<TileEntityWirelessMatterCollector> {

    public ContainerWirelessMatterCollector(
            TileEntityWirelessMatterCollector tileEntityWirelessMatterCollector,
            EntityPlayer var1
    ) {
        super(tileEntityWirelessMatterCollector, var1);
    }

}
