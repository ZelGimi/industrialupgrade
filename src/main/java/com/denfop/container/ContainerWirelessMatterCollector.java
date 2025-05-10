package com.denfop.container;

import com.denfop.tiles.base.TileEntityWirelessMatterCollector;
import net.minecraft.world.entity.player.Player;

public class ContainerWirelessMatterCollector extends ContainerFullInv<TileEntityWirelessMatterCollector> {

    public ContainerWirelessMatterCollector(
            TileEntityWirelessMatterCollector tileEntityWirelessMatterCollector,
            Player var1
    ) {
        super(tileEntityWirelessMatterCollector, var1);
    }

}
