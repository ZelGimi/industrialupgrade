package com.denfop.container;

import com.denfop.tiles.reactors.water.controller.TileEntityMainController;
import net.minecraft.world.entity.player.Player;

public class ContainerLevelFuel extends ContainerFullInv<TileEntityMainController> {

    public ContainerLevelFuel(TileEntityMainController main, Player var1) {
        super(var1, main);
    }

}
