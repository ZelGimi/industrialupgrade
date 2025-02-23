package com.denfop.container;

import com.denfop.tiles.cyclotron.TileEntityCyclotronController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCyclotronController extends ContainerFullInv<TileEntityCyclotronController> {

    public ContainerCyclotronController(
            TileEntityCyclotronController tileEntityGeothermalExchanger,
            EntityPlayer var1
    ) {
        super(var1, tileEntityGeothermalExchanger, 166);

    }

}
