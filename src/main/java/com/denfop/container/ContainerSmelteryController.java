package com.denfop.container;

import com.denfop.tiles.smeltery.TileEntitySmelteryController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSmelteryController extends ContainerFullInv<TileEntitySmelteryController> {

    public ContainerSmelteryController(
            TileEntitySmelteryController tileEntityGeothermalExchanger,
            EntityPlayer var1
    ) {
        super(var1, tileEntityGeothermalExchanger, 182);

    }

}
