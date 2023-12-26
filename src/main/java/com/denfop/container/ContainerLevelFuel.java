package com.denfop.container;

import com.denfop.tiles.reactors.water.controller.TileEntityMainController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerLevelFuel extends ContainerFullInv<TileEntityMainController> {

    public ContainerLevelFuel(TileEntityMainController main, EntityPlayer var1) {
        super(var1,main,188,211);
    }

}
