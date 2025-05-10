package com.denfop.container;

import com.denfop.tiles.cyclotron.TileEntityCyclotronController;
import net.minecraft.world.entity.player.Player;

public class ContainerCyclotronController extends ContainerFullInv<TileEntityCyclotronController> {

    public ContainerCyclotronController(
            TileEntityCyclotronController tileEntityGeothermalExchanger,
            Player var1
    ) {
        super(var1, tileEntityGeothermalExchanger, 166);

    }

}
