package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityWirelessMatterCollector;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuWirelessMatterCollector extends ContainerMenuFullInv<BlockEntityWirelessMatterCollector> {

    public ContainerMenuWirelessMatterCollector(
            BlockEntityWirelessMatterCollector tileEntityWirelessMatterCollector,
            Player var1
    ) {
        super(tileEntityWirelessMatterCollector, var1);
    }

}
